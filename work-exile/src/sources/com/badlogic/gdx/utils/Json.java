package com.badlogic.gdx.utils;

import com.badlogic.gdx.files.FileHandle;
import com.sessionm.ui.SessionMActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Json {
    private static final boolean debug = false;
    private final ObjectMap<Class, ObjectMap<String, Field>> typeToFields = new ObjectMap<>();
    private final ObjectMap<String, Class> tagToClass = new ObjectMap<>();
    private final ObjectMap<Class, String> classToTag = new ObjectMap<>();
    private final ObjectMap<Class, Serializer> classToSerializer = new ObjectMap<>();
    private final ObjectMap<Class, Object[]> classToDefaultValues = new ObjectMap<>();
    private String typeName = SessionMActivity.INTENT_PARAM_TYPE;
    private boolean usePrototypes = true;

    public interface Serializable {
        void read(Json json, ObjectMap objectMap);

        void write(Json json, JsonWriter jsonWriter) throws IOException;
    }

    public interface Serializer<T> {
        T read(Json json, ObjectMap objectMap, Class cls);

        void write(Json json, JsonWriter jsonWriter, String str, T t, Class cls) throws IOException;
    }

    public void addClassTag(String tag, Class type) {
        this.tagToClass.put(tag, type);
        this.classToTag.put(type, tag);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public <T> void setSerializer(Class<T> type, Serializer<T> serializer) {
        this.classToSerializer.put(type, serializer);
    }

    private ObjectMap<String, Field> cacheFields(Class type) {
        ArrayList<Field> allFields = new ArrayList<>();
        for (Class nextClass = type; nextClass != Object.class; nextClass = nextClass.getSuperclass()) {
            Collections.addAll(allFields, nextClass.getDeclaredFields());
        }
        ObjectMap<String, Field> nameToField = new ObjectMap<>();
        int n = allFields.size();
        for (int i = 0; i < n; i++) {
            Field field = allFields.get(i);
            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers) && !field.isSynthetic()) {
                if (!field.isAccessible()) {
                    try {
                        field.setAccessible(true);
                        nameToField.put(field.getName(), field);
                    } catch (AccessControlException e) {
                    }
                } else {
                    nameToField.put(field.getName(), field);
                }
            }
        }
        this.typeToFields.put(type, nameToField);
        return nameToField;
    }

    public String write(Object object) {
        StringWriter buffer = new StringWriter();
        write(object, buffer);
        return buffer.toString();
    }

    public void write(Object object, Writer writer) {
        if (!(writer instanceof JsonWriter)) {
            writer = new JsonWriter(writer);
        }
        try {
            writeValue(null, object, object.getClass(), (JsonWriter) writer);
        } catch (IOException ex) {
            throw new SerializationException("Error writing JSON.", ex);
        }
    }

    public void writeFields(Object object, JsonWriter writer) throws IOException {
        Class<?> cls = object.getClass();
        Object[] defaultValues = getDefaultValues(cls);
        ObjectMap<String, Field> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        int i = 0;
        for (Field field : fields.values()) {
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
                    } catch (RuntimeException e3) {
                        runtimeEx = e3;
                        SerializationException ex = new SerializationException(runtimeEx);
                        ex.addTrace(field + " (" + cls.getName() + ")");
                        throw ex;
                    }
                }
                writeValue(field.getName(), value, field.getType(), writer);
            } catch (SerializationException e4) {
                ex = e4;
            } catch (IllegalAccessException e5) {
                ex = e5;
            } catch (RuntimeException e6) {
                runtimeEx = e6;
            }
        }
    }

    private Object[] getDefaultValues(Class type) {
        if (!this.usePrototypes) {
            return null;
        }
        Object[] values = this.classToDefaultValues.get(type);
        if (values == null) {
            Object object = newInstance(type);
            ObjectMap<String, Field> fields = this.typeToFields.get(type);
            if (fields == null) {
                fields = cacheFields(type);
            }
            Object[] values2 = new Object[fields.size];
            this.classToDefaultValues.put(type, values2);
            int i = 0;
            for (Field field : fields.values()) {
                int i2 = i + 1;
                try {
                    values2[i] = field.get(object);
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
            return values2;
        }
        return values;
    }

    public void writeField(Object object, String name, JsonWriter writer) throws IOException {
        Class<?> cls = object.getClass();
        ObjectMap<String, Field> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        Field field = fields.get(name);
        if (field == null) {
            throw new SerializationException("Field not found: " + name + " (" + cls.getName() + ")");
        }
        try {
            writeValue(field.getName(), field.get(object), field.getType(), writer);
        } catch (SerializationException ex) {
            ex.addTrace(field + " (" + cls.getName() + ")");
            throw ex;
        } catch (IllegalAccessException ex2) {
            throw new SerializationException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", ex2);
        } catch (RuntimeException runtimeEx) {
            SerializationException ex3 = new SerializationException(runtimeEx);
            ex3.addTrace(field + " (" + cls.getName() + ")");
            throw ex3;
        }
    }

    public void writeValue(String name, Object value, JsonWriter writer) throws IOException {
        writeValue(name, value, value.getClass(), writer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeValue(String name, Object value, Class valueType, JsonWriter writer) throws IOException {
        if (value == null) {
            if (name == null) {
                writer.add(value);
                return;
            } else {
                writer.set(name, value);
                return;
            }
        }
        Class<?> cls = value.getClass();
        if (cls.isPrimitive() || cls == String.class || cls == Integer.class || cls == Boolean.class || cls == Float.class || cls == Long.class || cls == Double.class || cls == Short.class || cls == Byte.class || cls == Character.class) {
            if (name == null) {
                writer.add(value);
                return;
            } else {
                writer.set(name, value);
                return;
            }
        }
        if (value instanceof Serializable) {
            startObject(name, valueType, cls, writer);
            ((Serializable) value).write(this, writer);
            writer.pop();
            return;
        }
        Serializer serializer = this.classToSerializer.get(cls);
        if (serializer != null) {
            serializer.write(this, writer, name, value, valueType);
            return;
        }
        if (value instanceof Collection) {
            if (name == null) {
                writer.array();
            } else {
                writer.array(name);
            }
            for (Object item : (Collection) value) {
                writeValue(null, item, null, writer);
            }
            writer.pop();
            return;
        }
        if (value instanceof Array) {
            if (name == null) {
                writer.array();
            } else {
                writer.array(name);
            }
            for (Object item2 : (Array) value) {
                writeValue(null, item2, null, writer);
            }
            writer.pop();
            return;
        }
        if (cls.isArray()) {
            if (name == null) {
                writer.array();
            } else {
                writer.array(name);
            }
            Class<?> componentType = cls.getComponentType();
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                writeValue(null, java.lang.reflect.Array.get(value, i), componentType, writer);
            }
            writer.pop();
            return;
        }
        if (value instanceof ObjectMap) {
            startObject(name, valueType, cls, writer);
            for (ObjectMap.Entry entry : ((ObjectMap) value).entries()) {
                writeValue((String) entry.key, entry.value, null, writer);
            }
            writer.pop();
            return;
        }
        if (value instanceof Map) {
            startObject(name, valueType, cls, writer);
            for (Map.Entry entry2 : ((Map) value).entrySet()) {
                writeValue((String) entry2.getKey(), entry2.getValue(), null, writer);
            }
            writer.pop();
            return;
        }
        if (cls.isEnum()) {
            writer.set(name, value);
            return;
        }
        startObject(name, valueType, cls, writer);
        writeFields(value, writer);
        writer.pop();
    }

    public void startObject(String name, JsonWriter writer) throws IOException {
        if (name == null) {
            writer.object();
        } else {
            writer.object(name);
        }
    }

    public void startObject(String name, Class valueType, Class actualType, JsonWriter writer) throws IOException {
        if (name == null) {
            writer.object();
        } else {
            writer.object(name);
        }
        if ((valueType == null || valueType != actualType) && this.typeName != null) {
            String className = this.classToTag.get(actualType);
            if (className == null) {
                className = actualType.getName();
            }
            writer.set(this.typeName, className);
        }
    }

    public <T> T read(Class<T> cls, Reader reader) {
        return (T) readValue(cls, new JsonReader().parse(reader));
    }

    public <T> T read(Class<T> cls, InputStream inputStream) {
        return (T) readValue(cls, new JsonReader().parse(inputStream));
    }

    public <T> T read(Class<T> cls, FileHandle fileHandle) {
        return (T) readValue(cls, new JsonReader().parse(fileHandle));
    }

    public <T> T read(Class<T> cls, char[] cArr, int i, int i2) {
        return (T) readValue(cls, new JsonReader().parse(cArr, i, i2));
    }

    public <T> T read(Class<T> cls, String str) {
        return (T) readValue(cls, new JsonReader().parse(str));
    }

    public void readField(Object object, String name, ObjectMap map) {
        Class<?> cls = object.getClass();
        ObjectMap<String, Field> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        Field field = fields.get(name);
        if (field == null) {
            throw new SerializationException("Unable to find field: " + name + " (" + cls.getName() + ")");
        }
        Object value = map.get(name);
        if (value != null) {
            try {
                field.set(object, readValue(field.getType(), value));
            } catch (Exception ex) {
                throw new SerializationException("Error setting field: " + field.getName() + " (" + cls.getName() + ")", ex);
            }
        }
    }

    public void readFields(Object object, ObjectMap<String, Object> map) {
        Class<?> cls = object.getClass();
        ObjectMap<String, Field> fields = this.typeToFields.get(cls);
        if (fields == null) {
            fields = cacheFields(cls);
        }
        for (ObjectMap.Entry<String, Object> entry : map.entries()) {
            Field field = fields.get(entry.key);
            if (field == null) {
                throw new SerializationException("Unable to find field: " + entry.key + " (" + cls.getName() + ")");
            }
            if (entry.value != null) {
                try {
                    field.set(object, readValue(field.getType(), entry.value));
                } catch (Exception ex) {
                    throw new SerializationException("Error setting field: " + field.getName() + " (" + cls.getName() + ")", ex);
                }
            }
        }
    }

    public <T> T readValue(String str, Class<T> cls, ObjectMap objectMap) {
        return (T) readValue(cls, objectMap.get(str));
    }

    public <T> T readValue(String str, Class<T> cls, ObjectMap objectMap, T t) {
        Object obj = objectMap.get(str);
        return obj == null ? t : (T) readValue(cls, obj);
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

    private Object readValue(Class type, Object value) {
        Object objValueOf;
        Object objValueOf2;
        if (value instanceof ObjectMap) {
            ObjectMap<String, Object> map = (ObjectMap) value;
            String className = this.typeName == null ? null : (String) map.remove(this.typeName);
            if (className != null) {
                try {
                    type = Class.forName(className);
                } catch (ClassNotFoundException ex) {
                    type = this.tagToClass.get(className);
                    if (type == null) {
                        throw new SerializationException(ex);
                    }
                }
            }
            Serializer serializer = this.classToSerializer.get(type);
            if (serializer != null) {
                return serializer.read(this, map, type);
            }
            Object object = newInstance(type);
            if (object instanceof Serializable) {
                ((Serializable) object).read(this, map);
                return object;
            }
            if (object instanceof ObjectMap) {
                ObjectMap result = (ObjectMap) object;
                for (ObjectMap.Entry<String, Object> entry : map.entries()) {
                    result.put(entry.key, readValue(String.class, entry.value));
                }
                return result;
            }
            if (object instanceof HashMap) {
                HashMap result2 = (HashMap) object;
                for (ObjectMap.Entry<String, Object> entry2 : map.entries()) {
                    result2.put(entry2.key, readValue(String.class, entry2.value));
                }
                return result2;
            }
            ObjectMap<String, Field> fields = this.typeToFields.get(type);
            if (fields == null) {
                fields = cacheFields(type);
            }
            for (ObjectMap.Entry<String, Object> entry3 : map.entries()) {
                Field field = fields.get(entry3.key);
                if (field == null) {
                    throw new SerializationException("Unable to find field: " + entry3.key + " (" + type.getName() + ")");
                }
                try {
                    field.set(object, readValue(field.getType(), entry3.value));
                } catch (Exception ex2) {
                    throw new SerializationException("Error setting field: " + field.getName() + " (" + type.getName() + ")", ex2);
                }
            }
            return object;
        }
        if (value instanceof Array) {
            Array array = (Array) value;
            if (type.isAssignableFrom(Array.class)) {
                Array newArray = new Array(array.size);
                int n = array.size;
                for (int i = 0; i < n; i++) {
                    newArray.add(readValue(String.class, array.get(i)));
                }
                return newArray;
            }
            if (type.isAssignableFrom(ArrayList.class)) {
                ArrayList newArray2 = new ArrayList(array.size);
                int n2 = array.size;
                for (int i2 = 0; i2 < n2; i2++) {
                    newArray2.add(readValue(String.class, array.get(i2)));
                }
                return newArray2;
            }
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                Object newArray3 = java.lang.reflect.Array.newInstance(componentType, array.size);
                int n3 = array.size;
                for (int i3 = 0; i3 < n3; i3++) {
                    java.lang.reflect.Array.set(newArray3, i3, readValue(componentType, array.get(i3)));
                }
                return newArray3;
            }
            throw new SerializationException("Unable to convert value to required type: " + value + " (" + type.getName() + ")");
        }
        if (value instanceof Float) {
            Float floatValue = (Float) value;
            try {
                if (type == Integer.TYPE || type == Integer.class) {
                    objValueOf2 = Integer.valueOf(floatValue.intValue());
                } else if (type == Float.TYPE || type == Float.class) {
                    objValueOf2 = floatValue;
                } else if (type == Long.TYPE || type == Long.class) {
                    objValueOf2 = Long.valueOf(floatValue.longValue());
                } else if (type == Double.TYPE || type == Double.class) {
                    objValueOf2 = Double.valueOf(floatValue.doubleValue());
                } else if (type == Short.TYPE || type == Short.class) {
                    objValueOf2 = Short.valueOf(floatValue.shortValue());
                } else {
                    if (type == Byte.TYPE || type == Byte.class) {
                        objValueOf2 = Byte.valueOf(floatValue.byteValue());
                    }
                    value = String.valueOf(value);
                }
                return objValueOf2;
            } catch (NumberFormatException e) {
            }
        }
        Object objValueOf3 = value instanceof Boolean ? String.valueOf(value) : value;
        if (objValueOf3 instanceof String) {
            String string = (String) objValueOf3;
            if (type == String.class || objValueOf3 == null) {
                return objValueOf3;
            }
            try {
                if (type == Integer.TYPE || type == Integer.class) {
                    objValueOf = Integer.valueOf(string);
                } else if (type == Float.TYPE || type == Float.class) {
                    objValueOf = Float.valueOf(string);
                } else if (type == Long.TYPE || type == Long.class) {
                    objValueOf = Long.valueOf(string);
                } else if (type == Double.TYPE || type == Double.class) {
                    objValueOf = Double.valueOf(string);
                } else {
                    if (type != Short.TYPE && type != Short.class) {
                        if (type == Byte.TYPE || type == Byte.class) {
                            objValueOf = Byte.valueOf(string);
                        }
                        if (type == Boolean.TYPE || type == Boolean.class) {
                            return Boolean.valueOf(string);
                        }
                        if (type == Character.TYPE || type == Character.class) {
                            return Character.valueOf(string.charAt(0));
                        }
                        if (type.isEnum()) {
                            Object[] constants = type.getEnumConstants();
                            int n4 = constants.length;
                            for (int i4 = 0; i4 < n4; i4++) {
                                if (string.equals(constants[i4].toString())) {
                                    return constants[i4];
                                }
                            }
                        }
                        if (type == CharSequence.class) {
                            return string;
                        }
                        throw new SerializationException("Unable to convert value to required type: " + objValueOf3 + " (" + type.getName() + ")");
                    }
                    objValueOf = Short.valueOf(string);
                }
                return objValueOf;
            } catch (NumberFormatException e2) {
            }
        } else {
            return null;
        }
    }

    public String prettyPrint(Object object) {
        return prettyPrint(write(object));
    }

    public static String prettyPrint(String json) {
        JsonReader reader = new JsonReader();
        StringBuilder buffer = new StringBuilder(512);
        prettyPrint(reader.parse(json), buffer, 0);
        return buffer.toString();
    }

    private static void prettyPrint(Object object, StringBuilder buffer, int indent) {
        if (object instanceof ObjectMap) {
            ObjectMap<?, ?> map = (ObjectMap) object;
            if (map.size == 0) {
                buffer.append("{}");
                return;
            }
            buffer.append("{\n");
            int i = 0;
            for (ObjectMap.Entry<?, ?> entry : map.entries()) {
                indent(indent, buffer);
                buffer.append('\"');
                buffer.append(entry.key);
                buffer.append("\": ");
                prettyPrint(entry.value, buffer, indent + 1);
                int i2 = i + 1;
                if (i < map.size - 1) {
                    buffer.append(",");
                }
                buffer.append('\n');
                i = i2;
            }
            indent(indent - 1, buffer);
            buffer.append('}');
            return;
        }
        if (object instanceof Array) {
            Array array = (Array) object;
            if (array.size == 0) {
                buffer.append("[]");
                return;
            }
            buffer.append("[\n");
            int n = array.size;
            for (int i3 = 0; i3 < n; i3++) {
                indent(indent, buffer);
                prettyPrint(array.get(i3), buffer, indent + 1);
                if (i3 < array.size - 1) {
                    buffer.append(",");
                }
                buffer.append('\n');
            }
            indent(indent - 1, buffer);
            buffer.append(']');
            return;
        }
        if (object instanceof String) {
            buffer.append('\"');
            buffer.append(object);
            buffer.append('\"');
        } else {
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
                throw new IllegalArgumentException("Unknown object type: " + object.getClass());
            }
        }
    }

    private static void indent(int count, StringBuilder buffer) {
        for (int i = 0; i < count; i++) {
            buffer.append('\t');
        }
    }
}
