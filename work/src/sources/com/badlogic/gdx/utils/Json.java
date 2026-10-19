package com.badlogic.gdx.utils;

import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Json {
    private static final boolean debug = false;
    private final ObjectMap<Class, Object[]> classToDefaultValues;
    private final ObjectMap<Class, Serializer> classToSerializer;
    private final ObjectMap<Class, String> classToTag;
    private boolean ignoreUnknownFields;
    private JsonWriter.OutputType outputType;
    private final ObjectMap<String, Class> tagToClass;
    private String typeName;
    private final ObjectMap<Class, ObjectMap<String, FieldMetadata>> typeToFields;
    private boolean usePrototypes;
    private JsonWriter writer;

    public interface Serializable {
        void read(Json json, OrderedMap<String, Object> orderedMap);

        void write(Json json);
    }

    public interface Serializer<T> {
        T read(Json json, Object obj, Class cls);

        void write(Json json, T t, Class cls);
    }

    public Json() {
        this.typeName = "class";
        this.usePrototypes = true;
        this.typeToFields = new ObjectMap<>();
        this.tagToClass = new ObjectMap<>();
        this.classToTag = new ObjectMap<>();
        this.classToSerializer = new ObjectMap<>();
        this.classToDefaultValues = new ObjectMap<>();
        this.outputType = JsonWriter.OutputType.minimal;
    }

    public Json(JsonWriter.OutputType outputType) {
        this.typeName = "class";
        this.usePrototypes = true;
        this.typeToFields = new ObjectMap<>();
        this.tagToClass = new ObjectMap<>();
        this.classToTag = new ObjectMap<>();
        this.classToSerializer = new ObjectMap<>();
        this.classToDefaultValues = new ObjectMap<>();
        this.outputType = outputType;
    }

    public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
        this.ignoreUnknownFields = ignoreUnknownFields;
    }

    public void setOutputType(JsonWriter.OutputType outputType) {
        this.outputType = outputType;
    }

    public void addClassTag(String tag, Class type) {
        this.tagToClass.put(tag, type);
        this.classToTag.put(type, tag);
    }

    public Class getClass(String tag) {
        Class type = this.tagToClass.get(tag);
        if (type != null) {
            return type;
        }
        try {
            return Class.forName(tag);
        } catch (ClassNotFoundException ex) {
            throw new SerializationException(ex);
        }
    }

    public String getTag(Class type) {
        String tag = this.classToTag.get(type);
        return tag != null ? tag : type.getName();
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public <T> void setSerializer(Class<T> type, Serializer<T> serializer) {
        this.classToSerializer.put(type, serializer);
    }

    public <T> Serializer<T> getSerializer(Class<T> type) {
        return this.classToSerializer.get(type);
    }

    public void setUsePrototypes(boolean usePrototypes) {
        this.usePrototypes = usePrototypes;
    }

    public void setElementType(Class type, String fieldName, Class elementType) {
        ObjectMap<String, FieldMetadata> fields = this.typeToFields.get(type);
        if (fields == null) {
            fields = cacheFields(type);
        }
        FieldMetadata metadata = fields.get(fieldName);
        if (metadata == null) {
            throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")");
        }
        metadata.elementType = elementType;
    }

    private ObjectMap<String, FieldMetadata> cacheFields(Class type) {
        ArrayList<Field> allFields = new ArrayList<>();
        for (Class nextClass = type; nextClass != Object.class; nextClass = nextClass.getSuperclass()) {
            Collections.addAll(allFields, nextClass.getDeclaredFields());
        }
        ObjectMap<String, FieldMetadata> nameToField = new ObjectMap<>();
        int n = allFields.size();
        for (int i = 0; i < n; i++) {
            Field field = allFields.get(i);
            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers) && !field.isSynthetic()) {
                if (!field.isAccessible()) {
                    try {
                        field.setAccessible(true);
                        nameToField.put(field.getName(), new FieldMetadata(field));
                    } catch (AccessControlException e) {
                    }
                } else {
                    nameToField.put(field.getName(), new FieldMetadata(field));
                }
            }
        }
        this.typeToFields.put(type, nameToField);
        return nameToField;
    }

    public String toJson(Object object) {
        return toJson(object, object == null ? null : object.getClass(), (Class) null);
    }

    public String toJson(Object object, Class knownType) {
        return toJson(object, knownType, (Class) null);
    }

    public String toJson(Object object, Class knownType, Class elementType) {
        StringWriter buffer = new StringWriter();
        toJson(object, knownType, elementType, buffer);
        return buffer.toString();
    }

    public void toJson(Object object, FileHandle file) {
        toJson(object, object == null ? null : object.getClass(), (Class) null, file);
    }

    public void toJson(Object object, Class knownType, FileHandle file) {
        toJson(object, knownType, (Class) null, file);
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0029 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void toJson(Object object, Class knownType, Class elementType, FileHandle file) {
        Writer writer = null;
        try {
            writer = file.writer(false);
            toJson(object, knownType, elementType, writer);
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception ex) {
            throw new SerializationException("Error writing file: " + file, ex);
        }
    }

    public void toJson(Object object, Writer writer) {
        toJson(object, object == null ? null : object.getClass(), (Class) null, writer);
    }

    public void toJson(Object object, Class knownType, Writer writer) {
        toJson(object, knownType, (Class) null, writer);
    }

    public void toJson(Object object, Class knownType, Class elementType, Writer writer) {
        if (!(writer instanceof JsonWriter)) {
            writer = new JsonWriter(writer);
        }
        ((JsonWriter) writer).setOutputType(this.outputType);
        this.writer = (JsonWriter) writer;
        try {
            writeValue(object, knownType, elementType);
        } finally {
            this.writer = null;
        }
    }

    public void writeFields(Object object) {
        Class<?> cls = object.getClass();
        Object[] defaultValues = getDefaultValues(cls);
        ObjectMap<String, FieldMetadata> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        int i = 0;
        for (FieldMetadata metadata : fields.values()) {
            Field field = metadata.field;
            try {
                Object value = field.get(object);
                if (defaultValues != null) {
                    int i2 = i + 1;
                    try {
                        Object defaultValue = defaultValues[i];
                        if (value == null && defaultValue == null) {
                            i = i2;
                        } else if (value == null || defaultValue == null || !value.equals(defaultValue)) {
                            i = i2;
                        } else {
                            i = i2;
                        }
                    } catch (SerializationException e) {
                        ex = e;
                        ex.addTrace(field + " (" + cls.getName() + ")");
                        throw ex;
                    } catch (IllegalAccessException e2) {
                        ex = e2;
                        throw new SerializationException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", ex);
                    } catch (Exception e3) {
                        runtimeEx = e3;
                        SerializationException ex = new SerializationException(runtimeEx);
                        ex.addTrace(field + " (" + cls.getName() + ")");
                        throw ex;
                    }
                }
                this.writer.name(field.getName());
                writeValue(value, field.getType(), metadata.elementType);
            } catch (SerializationException e4) {
                ex = e4;
            } catch (IllegalAccessException e5) {
                ex = e5;
            } catch (Exception e6) {
                runtimeEx = e6;
            }
        }
    }

    private Object[] getDefaultValues(Class type) {
        if (!this.usePrototypes) {
            return null;
        }
        if (this.classToDefaultValues.containsKey(type)) {
            return this.classToDefaultValues.get(type);
        }
        try {
            Object object = newInstance(type);
            ObjectMap<String, FieldMetadata> fields = this.typeToFields.get(type);
            if (fields == null) {
                fields = cacheFields(type);
            }
            Object[] values = new Object[fields.size];
            this.classToDefaultValues.put(type, values);
            int i = 0;
            for (FieldMetadata metadata : fields.values()) {
                Field field = metadata.field;
                int i2 = i + 1;
                try {
                    values[i] = field.get(object);
                    i = i2;
                } catch (SerializationException ex) {
                    ex.addTrace(field + " (" + type.getName() + ")");
                    throw ex;
                } catch (IllegalAccessException ex2) {
                    throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex2);
                } catch (RuntimeException runtimeEx) {
                    SerializationException ex3 = new SerializationException(runtimeEx);
                    ex3.addTrace(field + " (" + type.getName() + ")");
                    throw ex3;
                }
            }
            return values;
        } catch (Exception e) {
            this.classToDefaultValues.put(type, null);
            return null;
        }
    }

    public void writeField(Object object, String name) {
        writeField(object, name, name, null);
    }

    public void writeField(Object object, String name, Class elementType) {
        writeField(object, name, name, elementType);
    }

    public void writeField(Object object, String fieldName, String jsonName) {
        writeField(object, fieldName, jsonName, null);
    }

    public void writeField(Object object, String fieldName, String jsonName, Class elementType) {
        Class<?> cls = object.getClass();
        ObjectMap<String, FieldMetadata> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        FieldMetadata metadata = fields.get(fieldName);
        if (metadata == null) {
            throw new SerializationException("Field not found: " + fieldName + " (" + cls.getName() + ")");
        }
        Field field = metadata.field;
        if (elementType == null) {
            elementType = metadata.elementType;
        }
        try {
            this.writer.name(jsonName);
            writeValue(field.get(object), field.getType(), elementType);
        } catch (SerializationException ex) {
            ex.addTrace(field + " (" + cls.getName() + ")");
            throw ex;
        } catch (IllegalAccessException ex2) {
            throw new SerializationException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", ex2);
        } catch (Exception runtimeEx) {
            SerializationException ex3 = new SerializationException(runtimeEx);
            ex3.addTrace(field + " (" + cls.getName() + ")");
            throw ex3;
        }
    }

    public void writeValue(String name, Object value) {
        try {
            this.writer.name(name);
            writeValue(value, value.getClass(), (Class) null);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeValue(String name, Object value, Class knownType) {
        try {
            this.writer.name(name);
            writeValue(value, knownType, (Class) null);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeValue(String name, Object value, Class knownType, Class elementType) {
        try {
            this.writer.name(name);
            writeValue(value, knownType, elementType);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeValue(Object value) {
        writeValue(value, value.getClass(), (Class) null);
    }

    public void writeValue(Object value, Class knownType) {
        writeValue(value, knownType, (Class) null);
    }

    public void writeValue(Object value, Class knownType, Class elementType) {
        try {
            if (value == null) {
                this.writer.value(null);
                return;
            }
            Class<?> cls = value.getClass();
            if (cls.isPrimitive() || cls == String.class || cls == Integer.class || cls == Boolean.class || cls == Float.class || cls == Long.class || cls == Double.class || cls == Short.class || cls == Byte.class || cls == Character.class) {
                this.writer.value(value);
                return;
            }
            if (value instanceof Serializable) {
                writeObjectStart(cls, knownType);
                ((Serializable) value).write(this);
                writeObjectEnd();
                return;
            }
            Serializer serializer = this.classToSerializer.get(cls);
            if (serializer != null) {
                serializer.write(this, value, knownType);
                return;
            }
            if (value instanceof Array) {
                if (knownType != null && cls != knownType) {
                    throw new SerializationException("Serialization of an Array other than the known type is not supported.\nKnown type: " + knownType + "\nActual type: " + cls);
                }
                writeArrayStart();
                Array array = (Array) value;
                int n = array.size;
                for (int i = 0; i < n; i++) {
                    writeValue(array.get(i), elementType, (Class) null);
                }
                writeArrayEnd();
                return;
            }
            if (value instanceof Collection) {
                if (knownType != null && cls != knownType && cls != ArrayList.class) {
                    throw new SerializationException("Serialization of a Collection other than the known type is not supported.\nKnown type: " + knownType + "\nActual type: " + cls);
                }
                writeArrayStart();
                for (Object item : (Collection) value) {
                    writeValue(item, elementType, (Class) null);
                }
                writeArrayEnd();
                return;
            }
            if (cls.isArray()) {
                if (elementType == null) {
                    elementType = cls.getComponentType();
                }
                int length = java.lang.reflect.Array.getLength(value);
                writeArrayStart();
                for (int i2 = 0; i2 < length; i2++) {
                    writeValue(java.lang.reflect.Array.get(value, i2), elementType, (Class) null);
                }
                writeArrayEnd();
                return;
            }
            if (value instanceof OrderedMap) {
                if (knownType == null) {
                    knownType = OrderedMap.class;
                }
                writeObjectStart(cls, knownType);
                OrderedMap map = (OrderedMap) value;
                for (Object key : map.orderedKeys()) {
                    this.writer.name(convertToString(key));
                    writeValue(map.get(key), elementType, (Class) null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof ArrayMap) {
                if (knownType == null) {
                    knownType = ArrayMap.class;
                }
                writeObjectStart(cls, knownType);
                ArrayMap map2 = (ArrayMap) value;
                int n2 = map2.size;
                for (int i3 = 0; i3 < n2; i3++) {
                    this.writer.name(convertToString(map2.keys[i3]));
                    writeValue(map2.values[i3], elementType, (Class) null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof ObjectMap) {
                if (knownType == null) {
                    knownType = OrderedMap.class;
                }
                writeObjectStart(cls, knownType);
                for (ObjectMap.Entry entry : ((ObjectMap) value).entries()) {
                    this.writer.name(convertToString(entry.key));
                    writeValue(entry.value, elementType, (Class) null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof Map) {
                if (knownType == null) {
                    knownType = OrderedMap.class;
                }
                writeObjectStart(cls, knownType);
                for (Map.Entry entry2 : ((Map) value).entrySet()) {
                    this.writer.name(convertToString(entry2.getKey()));
                    writeValue(entry2.getValue(), elementType, (Class) null);
                }
                writeObjectEnd();
                return;
            }
            if (cls.isEnum()) {
                this.writer.value(value);
                return;
            }
            writeObjectStart(cls, knownType);
            writeFields(value);
            writeObjectEnd();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeObjectStart(String name) {
        try {
            this.writer.name(name);
            writeObjectStart();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeObjectStart(String name, Class actualType, Class knownType) {
        try {
            this.writer.name(name);
            writeObjectStart(actualType, knownType);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeObjectStart() {
        try {
            this.writer.object();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeObjectStart(Class actualType, Class knownType) {
        try {
            this.writer.object();
            if (knownType == null || knownType != actualType) {
                writeType(actualType);
            }
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeObjectEnd() {
        try {
            this.writer.pop();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeArrayStart(String name) {
        try {
            this.writer.name(name);
            this.writer.array();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeArrayStart() {
        try {
            this.writer.array();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeArrayEnd() {
        try {
            this.writer.pop();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public void writeType(Class type) {
        if (this.typeName != null) {
            String className = this.classToTag.get(type);
            if (className == null) {
                className = type.getName();
            }
            try {
                this.writer.set(this.typeName, className);
            } catch (IOException ex) {
                throw new SerializationException(ex);
            }
        }
    }

    public <T> T fromJson(Class<T> cls, Reader reader) {
        return (T) readValue(cls, (Class) null, new JsonReader().parse(reader));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, Reader reader) {
        return (T) readValue(cls, cls2, new JsonReader().parse(reader));
    }

    public <T> T fromJson(Class<T> cls, InputStream inputStream) {
        return (T) readValue(cls, (Class) null, new JsonReader().parse(inputStream));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, InputStream inputStream) {
        return (T) readValue(cls, cls2, new JsonReader().parse(inputStream));
    }

    public <T> T fromJson(Class<T> cls, FileHandle fileHandle) {
        try {
            return (T) readValue(cls, (Class) null, new JsonReader().parse(fileHandle));
        } catch (Exception e) {
            throw new SerializationException("Error reading file: " + fileHandle, e);
        }
    }

    public <T> T fromJson(Class<T> cls, Class cls2, FileHandle fileHandle) {
        try {
            return (T) readValue(cls, cls2, new JsonReader().parse(fileHandle));
        } catch (Exception e) {
            throw new SerializationException("Error reading file: " + fileHandle, e);
        }
    }

    public <T> T fromJson(Class<T> cls, char[] cArr, int i, int i2) {
        return (T) readValue(cls, (Class) null, new JsonReader().parse(cArr, i, i2));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, char[] cArr, int i, int i2) {
        return (T) readValue(cls, cls2, new JsonReader().parse(cArr, i, i2));
    }

    public <T> T fromJson(Class<T> cls, String str) {
        return (T) readValue(cls, (Class) null, new JsonReader().parse(str));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, String str) {
        return (T) readValue(cls, cls2, new JsonReader().parse(str));
    }

    public void readField(Object object, String name, Object jsonData) {
        readField(object, name, name, null, jsonData);
    }

    public void readField(Object object, String name, Class elementType, Object jsonData) {
        readField(object, name, name, elementType, jsonData);
    }

    public void readField(Object object, String fieldName, String jsonName, Object jsonData) {
        readField(object, fieldName, jsonName, null, jsonData);
    }

    public void readField(Object object, String fieldName, String jsonName, Class elementType, Object jsonData) {
        OrderedMap jsonMap = (OrderedMap) jsonData;
        Class<?> cls = object.getClass();
        ObjectMap<String, FieldMetadata> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        FieldMetadata metadata = fields.get(fieldName);
        if (metadata == null) {
            throw new SerializationException("Field not found: " + fieldName + " (" + cls.getName() + ")");
        }
        Field field = metadata.field;
        Object jsonValue = jsonMap.get(jsonName);
        if (jsonValue != null) {
            if (elementType == null) {
                elementType = metadata.elementType;
            }
            try {
                field.set(object, readValue(field.getType(), elementType, jsonValue));
            } catch (SerializationException ex) {
                ex.addTrace(field.getName() + " (" + cls.getName() + ")");
                throw ex;
            } catch (IllegalAccessException ex2) {
                throw new SerializationException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", ex2);
            } catch (RuntimeException runtimeEx) {
                SerializationException ex3 = new SerializationException(runtimeEx);
                ex3.addTrace(field.getName() + " (" + cls.getName() + ")");
                throw ex3;
            }
        }
    }

    public void readFields(Object object, Object jsonData) {
        OrderedMap<String, Object> jsonMap = (OrderedMap) jsonData;
        Class<?> cls = object.getClass();
        ObjectMap<String, FieldMetadata> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        for (ObjectMap.Entry<String, Object> entry : jsonMap.entries()) {
            FieldMetadata metadata = fields.get(entry.key);
            if (metadata == null) {
                if (!this.ignoreUnknownFields) {
                    throw new SerializationException("Field not found: " + entry.key + " (" + cls.getName() + ")");
                }
            } else {
                Field field = metadata.field;
                if (entry.value != null) {
                    try {
                        field.set(object, readValue(field.getType(), metadata.elementType, entry.value));
                    } catch (SerializationException ex) {
                        ex.addTrace(field.getName() + " (" + cls.getName() + ")");
                        throw ex;
                    } catch (IllegalAccessException ex2) {
                        throw new SerializationException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", ex2);
                    } catch (RuntimeException runtimeEx) {
                        SerializationException ex3 = new SerializationException(runtimeEx);
                        ex3.addTrace(field.getName() + " (" + cls.getName() + ")");
                        throw ex3;
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public <T> T readValue(String str, Class<T> cls, Object obj) {
        return (T) readValue(cls, (Class) null, ((OrderedMap) obj).get(str));
    }

    public <T> T readValue(String str, Class<T> cls, T t, Object obj) {
        Object obj2 = ((OrderedMap) obj).get(str);
        return obj2 == null ? t : (T) readValue(cls, (Class) null, obj2);
    }

    public <T> T readValue(String str, Class<T> cls, Class cls2, Object obj) {
        return (T) readValue(cls, cls2, ((OrderedMap) obj).get(str));
    }

    public <T> T readValue(String str, Class<T> cls, Class cls2, T t, Object obj) {
        Object obj2 = ((OrderedMap) obj).get(str);
        return obj2 == null ? t : (T) readValue(cls, cls2, obj2);
    }

    public <T> T readValue(Class<T> cls, Class cls2, T t, Object obj) {
        return (T) readValue(cls, cls2, obj);
    }

    public <T> T readValue(Class<T> cls, Object obj) {
        return (T) readValue(cls, (Class) null, obj);
    }

    /* JADX WARN: Code duplicated, block: B:77:0x0241  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v74 */
    /* JADX WARN: Type inference failed for: r16v20, types: [T, com.badlogic.gdx.utils.Array] */
    /* JADX WARN: Type inference failed for: r16v22, types: [T, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r16v51 */
    /* JADX WARN: Type inference failed for: r17v1 */
    /* JADX WARN: Type inference failed for: r17v2, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r17v5 */
    /* JADX WARN: Type inference failed for: r17v6 */
    /* JADX WARN: Type inference failed for: r18v0 */
    /* JADX WARN: Type inference failed for: r18v1, types: [T, com.badlogic.gdx.utils.ObjectMap] */
    /* JADX WARN: Type inference failed for: r18v3, types: [T, java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v49, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r20v1, types: [T, java.lang.String] */
    /* JADX WARN: Type inference failed for: r25v0, types: [com.badlogic.gdx.utils.Json] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public <T> T readValue(Class<T> cls, Class cls2, Object obj) {
        T t;
        T t2;
        Serializer serializer;
        ?? r17;
        ?? r18;
        if (obj == null) {
            return null;
        }
        if (obj instanceof OrderedMap) {
            OrderedMap<String, Object> orderedMap = (OrderedMap) obj;
            String str = this.typeName == null ? null : (String) orderedMap.remove(this.typeName);
            if (str != null) {
                try {
                    cls = (Class<T>) Class.forName(str);
                } catch (ClassNotFoundException e) {
                    cls = (Class<T>) this.tagToClass.get(str);
                    if (cls == null) {
                        throw new SerializationException(e);
                    }
                }
            }
            if (cls != null) {
                Serializer serializer2 = this.classToSerializer.get(cls);
                if (serializer2 != null) {
                    return (T) serializer2.read(this, orderedMap, cls);
                }
                r18 = (T) newInstance(cls);
                if (r18 instanceof Serializable) {
                    ((Serializable) r18).read(this, orderedMap);
                    return r18;
                }
                if (r18 instanceof HashMap) {
                    r17 = r18;
                    ?? r19 = (T) ((HashMap) r18);
                    for (ObjectMap.Entry<String, Object> entry : orderedMap.entries()) {
                        r19.put(entry.key, readValue(cls2, null, entry.value));
                    }
                    return r19;
                }
            } else {
                r17 = (T) new OrderedMap();
            }
            r17 = r18;
            if (r17 instanceof ObjectMap) {
                ?? r110 = (T) ((ObjectMap) r17);
                for (String str2 : orderedMap.orderedKeys()) {
                    r110.put(str2, readValue(cls2, null, orderedMap.get(str2)));
                }
                return r110;
            }
            readFields(r17, orderedMap);
            return (T) r17;
        }
        if (cls != null && (serializer = this.classToSerializer.get(cls)) != null) {
            return (T) serializer.read(this, obj, cls);
        }
        if (obj instanceof Array) {
            Array array = (Array) obj;
            if (cls == null || Array.class.isAssignableFrom(cls)) {
                ?? r16 = (T) new Array(array.size);
                int i = array.size;
                for (int i2 = 0; i2 < i; i2++) {
                    r16.add(readValue(cls2, null, array.get(i2)));
                }
                return r16;
            }
            if (ArrayList.class.isAssignableFrom(cls)) {
                ?? r111 = (T) new ArrayList(array.size);
                int i3 = array.size;
                for (int i4 = 0; i4 < i3; i4++) {
                    r111.add(readValue(cls2, null, array.get(i4)));
                }
                return r111;
            }
            if (cls.isArray()) {
                Class<?> componentType = cls.getComponentType();
                if (cls2 == null) {
                    cls2 = componentType;
                }
                T t3 = (T) java.lang.reflect.Array.newInstance(componentType, array.size);
                int i5 = array.size;
                for (int i6 = 0; i6 < i5; i6++) {
                    java.lang.reflect.Array.set(t3, i6, readValue(cls2, null, array.get(i6)));
                }
                return t3;
            }
            throw new SerializationException("Unable to convert value to required type: " + obj + " (" + cls.getName() + ")");
        }
        if (obj instanceof Float) {
            Float f = (Float) obj;
            if (cls != null) {
                try {
                    if (cls == Float.TYPE || cls == Float.class) {
                        t2 = (T) f;
                    } else if (cls == Integer.TYPE || cls == Integer.class) {
                        t2 = (T) Integer.valueOf(f.intValue());
                    } else if (cls == Long.TYPE || cls == Long.class) {
                        t2 = (T) Long.valueOf(f.longValue());
                    } else if (cls == Double.TYPE || cls == Double.class) {
                        t2 = (T) Double.valueOf(f.doubleValue());
                    } else if (cls == Short.TYPE || cls == Short.class) {
                        t2 = (T) Short.valueOf(f.shortValue());
                    } else {
                        if (cls == Byte.TYPE || cls == Byte.class) {
                            t2 = (T) Byte.valueOf(f.byteValue());
                        }
                        obj = String.valueOf(obj);
                    }
                } catch (NumberFormatException e2) {
                }
            } else {
                t2 = (T) f;
            }
            return t2;
        }
        Object objValueOf = obj instanceof Boolean ? String.valueOf(obj) : obj;
        if (objValueOf instanceof String) {
            ?? r20 = (T) ((String) objValueOf);
            if (cls == null || cls == String.class) {
                return (T) objValueOf;
            }
            try {
                if (cls == Integer.TYPE || cls == Integer.class) {
                    t = (T) Integer.valueOf((String) r20);
                } else if (cls == Float.TYPE || cls == Float.class) {
                    t = (T) Float.valueOf((String) r20);
                } else if (cls == Long.TYPE || cls == Long.class) {
                    t = (T) Long.valueOf((String) r20);
                } else if (cls == Double.TYPE || cls == Double.class) {
                    t = (T) Double.valueOf((String) r20);
                } else {
                    if (cls != Short.TYPE && cls != Short.class) {
                        if (cls == Byte.TYPE || cls == Byte.class) {
                            t = (T) Byte.valueOf((String) r20);
                        }
                        if (cls == Boolean.TYPE || cls == Boolean.class) {
                            return (T) Boolean.valueOf((String) r20);
                        }
                        if (cls == Character.TYPE || cls == Character.class) {
                            return (T) Character.valueOf(r20.charAt(0));
                        }
                        if (cls.isEnum()) {
                            T[] enumConstants = cls.getEnumConstants();
                            int length = enumConstants.length;
                            for (int i7 = 0; i7 < length; i7++) {
                                if (r20.equals(enumConstants[i7].toString())) {
                                    return enumConstants[i7];
                                }
                            }
                        }
                        if (cls == CharSequence.class) {
                            return r20;
                        }
                        throw new SerializationException("Unable to convert value to required type: " + objValueOf + " (" + cls.getName() + ")");
                    }
                    t = (T) Short.valueOf((String) r20);
                }
                return t;
            } catch (NumberFormatException e3) {
            }
        } else {
            return null;
        }
    }

    private String convertToString(Object object) {
        return object instanceof Class ? ((Class) object).getName() : String.valueOf(object);
    }

    private Object newInstance(Class type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            ex = e;
            try {
                Constructor constructor = type.getDeclaredConstructor(new Class[0]);
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[0]);
            } catch (NoSuchMethodException e2) {
                if (type.isArray()) {
                    throw new SerializationException("Encountered JSON object when expected array of type: " + type.getName(), ex);
                }
                if (type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) {
                    throw new SerializationException("Class cannot be created (non-static member class): " + type.getName(), ex);
                }
                throw new SerializationException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex);
            } catch (SecurityException e3) {
                throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
            } catch (Exception privateConstructorException) {
                ex = privateConstructorException;
                throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
            }
        }
    }

    public String prettyPrint(Object object) {
        return prettyPrint(object, 0);
    }

    public String prettyPrint(String json) {
        return prettyPrint(json, 0);
    }

    public String prettyPrint(Object object, int singleLineColumns) {
        return prettyPrint(toJson(object), singleLineColumns);
    }

    public String prettyPrint(String json, int singleLineColumns) {
        StringBuilder buffer = new StringBuilder(512);
        prettyPrint(new JsonReader().parse(json), buffer, 0, singleLineColumns);
        return buffer.toString();
    }

    private void prettyPrint(Object object, StringBuilder buffer, int indent, int singleLineColumns) {
        int i;
        int i2;
        if (object instanceof OrderedMap) {
            OrderedMap<String, ?> map = (OrderedMap) object;
            if (map.size == 0) {
                buffer.append("{}");
                return;
            }
            boolean newLines = !isFlat(map);
            int start = buffer.length();
            loop0: while (true) {
                buffer.append(newLines ? "{\n" : "{ ");
                Iterator<String> it = map.orderedKeys().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break loop0;
                    }
                    String key = it.next();
                    if (newLines) {
                        indent(indent, buffer);
                    }
                    buffer.append(this.outputType.quoteName(key));
                    buffer.append(": ");
                    prettyPrint(map.get(key), buffer, indent + 1, singleLineColumns);
                    int i3 = i2 + 1;
                    if (i2 < map.size - 1) {
                        buffer.append(",");
                    }
                    buffer.append(newLines ? '\n' : ' ');
                    i2 = (newLines || buffer.length() - start <= singleLineColumns) ? i3 : 0;
                }
                buffer.setLength(start);
                newLines = true;
            }
            if (newLines) {
                indent(indent - 1, buffer);
            }
            buffer.append('}');
            return;
        }
        if (object instanceof Array) {
            Array array = (Array) object;
            if (array.size == 0) {
                buffer.append("[]");
                return;
            }
            boolean newLines2 = !isFlat(array);
            int start2 = buffer.length();
            loop2: while (true) {
                buffer.append(newLines2 ? "[\n" : "[ ");
                int n = array.size;
                while (true) {
                    if (i >= n) {
                        break loop2;
                    }
                    if (newLines2) {
                        indent(indent, buffer);
                    }
                    prettyPrint(array.get(i), buffer, indent + 1, singleLineColumns);
                    if (i < array.size - 1) {
                        buffer.append(",");
                    }
                    buffer.append(newLines2 ? '\n' : ' ');
                    i = (newLines2 || buffer.length() - start2 <= singleLineColumns) ? i + 1 : 0;
                }
                buffer.setLength(start2);
                newLines2 = true;
            }
            if (newLines2) {
                indent(indent - 1, buffer);
            }
            buffer.append(']');
            return;
        }
        if (object instanceof String) {
            buffer.append(this.outputType.quoteValue((String) object));
            return;
        }
        if (object instanceof Float) {
            Float floatValue = (Float) object;
            int intValue = floatValue.intValue();
            if (floatValue.floatValue() - intValue == 0.0f) {
                object = Integer.valueOf(intValue);
            }
            buffer.append(object);
            return;
        }
        if (object instanceof Boolean) {
            buffer.append(object);
        } else {
            if (object == null) {
                buffer.append("null");
                return;
            }
            throw new SerializationException("Unknown object type: " + object.getClass());
        }
    }

    private static boolean isFlat(ObjectMap<?, ?> map) {
        for (ObjectMap.Entry<?, ?> entry : map.entries()) {
            if ((entry.value instanceof ObjectMap) || (entry.value instanceof Array)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFlat(Array array) {
        int n = array.size;
        for (int i = 0; i < n; i++) {
            Object value = array.get(i);
            if ((value instanceof ObjectMap) || (value instanceof Array)) {
                return false;
            }
        }
        return true;
    }

    private static void indent(int count, StringBuilder buffer) {
        for (int i = 0; i < count; i++) {
            buffer.append('\t');
        }
    }

    private static class FieldMetadata {
        Class elementType;
        Field field;

        public FieldMetadata(Field field) {
            this.field = field;
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
                if (actualTypes.length == 1) {
                    Type actualType = actualTypes[0];
                    if (actualType instanceof Class) {
                        this.elementType = (Class) actualType;
                    } else if (actualType instanceof ParameterizedType) {
                        this.elementType = (Class) ((ParameterizedType) actualType).getRawType();
                    }
                }
            }
        }
    }

    public static abstract class ReadOnlySerializer<T> implements Serializer<T> {
        @Override // com.badlogic.gdx.utils.Json.Serializer
        public abstract T read(Json json, Object obj, Class cls);

        @Override // com.badlogic.gdx.utils.Json.Serializer
        public void write(Json json, T object, Class knownType) {
        }
    }
}
