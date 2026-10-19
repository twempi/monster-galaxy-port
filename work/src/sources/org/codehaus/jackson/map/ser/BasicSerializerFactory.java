package org.codehaus.jackson.map.ser;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.TimeZone;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualSerializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer;
import org.codehaus.jackson.map.ser.impl.InetAddressSerializer;
import org.codehaus.jackson.map.ser.impl.ObjectArraySerializer;
import org.codehaus.jackson.map.ser.impl.StringCollectionSerializer;
import org.codehaus.jackson.map.ser.impl.TimeZoneSerializer;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasicSerializerFactory extends SerializerFactory {
    protected static final HashMap<String, JsonSerializer<?>> _arraySerializers;
    protected static final HashMap<String, JsonSerializer<?>> _concrete = new HashMap<>();
    protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy = new HashMap<>();
    protected OptionalHandlerFactory optionalHandlers = OptionalHandlerFactory.instance;

    @Override // org.codehaus.jackson.map.SerializerFactory
    public abstract JsonSerializer<Object> createSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    protected abstract Iterable<Serializers> customSerializers();

    static {
        _concrete.put(String.class.getName(), new StdSerializers.StringSerializer());
        ToStringSerializer sls = ToStringSerializer.instance;
        _concrete.put(StringBuffer.class.getName(), sls);
        _concrete.put(StringBuilder.class.getName(), sls);
        _concrete.put(Character.class.getName(), sls);
        _concrete.put(Character.TYPE.getName(), sls);
        _concrete.put(Boolean.TYPE.getName(), new StdSerializers.BooleanSerializer(true));
        _concrete.put(Boolean.class.getName(), new StdSerializers.BooleanSerializer(false));
        JsonSerializer<?> intS = new StdSerializers.IntegerSerializer();
        _concrete.put(Integer.class.getName(), intS);
        _concrete.put(Integer.TYPE.getName(), intS);
        _concrete.put(Long.class.getName(), StdSerializers.LongSerializer.instance);
        _concrete.put(Long.TYPE.getName(), StdSerializers.LongSerializer.instance);
        _concrete.put(Byte.class.getName(), StdSerializers.IntLikeSerializer.instance);
        _concrete.put(Byte.TYPE.getName(), StdSerializers.IntLikeSerializer.instance);
        _concrete.put(Short.class.getName(), StdSerializers.IntLikeSerializer.instance);
        _concrete.put(Short.TYPE.getName(), StdSerializers.IntLikeSerializer.instance);
        _concrete.put(Float.class.getName(), StdSerializers.FloatSerializer.instance);
        _concrete.put(Float.TYPE.getName(), StdSerializers.FloatSerializer.instance);
        _concrete.put(Double.class.getName(), StdSerializers.DoubleSerializer.instance);
        _concrete.put(Double.TYPE.getName(), StdSerializers.DoubleSerializer.instance);
        JsonSerializer<?> ns = new StdSerializers.NumberSerializer();
        _concrete.put(BigInteger.class.getName(), ns);
        _concrete.put(BigDecimal.class.getName(), ns);
        _concrete.put(Calendar.class.getName(), StdSerializers.CalendarSerializer.instance);
        _concrete.put(Date.class.getName(), StdSerializers.UtilDateSerializer.instance);
        _concrete.put(java.sql.Date.class.getName(), new StdSerializers.SqlDateSerializer());
        _concrete.put(Time.class.getName(), new StdSerializers.SqlTimeSerializer());
        _concrete.put(Timestamp.class.getName(), StdSerializers.UtilDateSerializer.instance);
        for (Map.Entry<Class<?>, Object> en : new JdkSerializers().provide()) {
            Object value = en.getValue();
            if (value instanceof JsonSerializer) {
                _concrete.put(en.getKey().getName(), (JsonSerializer) value);
            } else if (value instanceof Class) {
                Class<? extends JsonSerializer<?>> cls = (Class) value;
                _concreteLazy.put(en.getKey().getName(), cls);
            } else {
                throw new IllegalStateException("Internal error: unrecognized value of type " + en.getClass().getName());
            }
        }
        _concreteLazy.put(TokenBuffer.class.getName(), StdSerializers.TokenBufferSerializer.class);
        _arraySerializers = new HashMap<>();
        _arraySerializers.put(boolean[].class.getName(), new ArraySerializers.BooleanArraySerializer());
        _arraySerializers.put(byte[].class.getName(), new ArraySerializers.ByteArraySerializer());
        _arraySerializers.put(char[].class.getName(), new ArraySerializers.CharArraySerializer());
        _arraySerializers.put(short[].class.getName(), new ArraySerializers.ShortArraySerializer());
        _arraySerializers.put(int[].class.getName(), new ArraySerializers.IntArraySerializer());
        _arraySerializers.put(long[].class.getName(), new ArraySerializers.LongArraySerializer());
        _arraySerializers.put(float[].class.getName(), new ArraySerializers.FloatArraySerializer());
        _arraySerializers.put(double[].class.getName(), new ArraySerializers.DoubleArraySerializer());
    }

    protected BasicSerializerFactory() {
    }

    @Override // org.codehaus.jackson.map.SerializerFactory
    public TypeSerializer createTypeSerializer(SerializationConfig config, JavaType baseType, BeanProperty property) {
        BasicBeanDescription bean = (BasicBeanDescription) config.introspectClassAnnotations(baseType.getRawClass());
        AnnotatedClass ac = bean.getClassInfo();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
        } else {
            subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
        }
        if (b == null) {
            return null;
        }
        return b.buildTypeSerializer(config, baseType, subtypes, property);
    }

    public final JsonSerializer<?> getNullSerializer() {
        return NullSerializer.instance;
    }

    public final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        Class<?> raw = type.getRawClass();
        String clsName = raw.getName();
        JsonSerializer<?> ser = _concrete.get(clsName);
        if (ser == null) {
            Class<? extends JsonSerializer<?>> serClass = _concreteLazy.get(clsName);
            if (serClass != null) {
                try {
                    return serClass.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to instantiate standard serializer (of type " + serClass.getName() + "): " + e.getMessage(), e);
                }
            }
            return null;
        }
        return ser;
    }

    public final JsonSerializer<?> findSerializerByPrimaryType(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) throws JsonMappingException {
        Class<?> raw = type.getRawClass();
        if (JsonSerializable.class.isAssignableFrom(raw)) {
            if (JsonSerializableWithType.class.isAssignableFrom(raw)) {
                return StdSerializers.SerializableWithTypeSerializer.instance;
            }
            return StdSerializers.SerializableSerializer.instance;
        }
        AnnotatedMethod valueMethod = beanDesc.findJsonValueMethod();
        if (valueMethod != null) {
            Method m = valueMethod.getAnnotated();
            if (config.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                ClassUtil.checkAndFixAccess(m);
            }
            return new JsonValueSerializer(m, findSerializerFromAnnotation(config, valueMethod, property), property);
        }
        if (InetAddress.class.isAssignableFrom(raw)) {
            return InetAddressSerializer.instance;
        }
        if (TimeZone.class.isAssignableFrom(raw)) {
            return TimeZoneSerializer.instance;
        }
        JsonSerializer<?> ser = this.optionalHandlers.findSerializer(config, type);
        if (ser == null) {
            if (Number.class.isAssignableFrom(raw)) {
                return StdSerializers.NumberSerializer.instance;
            }
            if (Enum.class.isAssignableFrom(raw)) {
                return EnumSerializer.construct(raw, config, beanDesc);
            }
            if (Calendar.class.isAssignableFrom(raw)) {
                return StdSerializers.CalendarSerializer.instance;
            }
            if (Date.class.isAssignableFrom(raw)) {
                return StdSerializers.UtilDateSerializer.instance;
            }
            return null;
        }
        return ser;
    }

    public final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) throws JsonMappingException {
        Class<?> type = javaType.getRawClass();
        if (Iterator.class.isAssignableFrom(type)) {
            return buildIteratorSerializer(config, javaType, beanDesc, property, staticTyping);
        }
        if (Iterable.class.isAssignableFrom(type)) {
            return buildIterableSerializer(config, javaType, beanDesc, property, staticTyping);
        }
        if (CharSequence.class.isAssignableFrom(type)) {
            return ToStringSerializer.instance;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated a, BeanProperty property) throws JsonMappingException {
        Object serDef = config.getAnnotationIntrospector().findSerializer(a);
        if (serDef == null) {
            return null;
        }
        if (serDef instanceof JsonSerializer) {
            JsonSerializer<Object> jsonSerializer = (JsonSerializer) serDef;
            if (jsonSerializer instanceof ContextualSerializer) {
                JsonSerializer<Object> ser = ((ContextualSerializer) jsonSerializer).createContextual(config, property);
                return ser;
            }
            return jsonSerializer;
        }
        if (!(serDef instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned value of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
        }
        Class<?> cls = (Class) serDef;
        if (!JsonSerializer.class.isAssignableFrom(cls)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<JsonSerializer>");
        }
        JsonSerializer<Object> jsonSerializerSerializerInstance = config.serializerInstance(a, cls);
        if (jsonSerializerSerializerInstance instanceof ContextualSerializer) {
            JsonSerializer<Object> ser2 = ((ContextualSerializer) jsonSerializerSerializerInstance).createContextual(config, property);
            return ser2;
        }
        return jsonSerializerSerializerInstance;
    }

    public JsonSerializer<?> buildContainerSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        JavaType elementType = type.getContentType();
        TypeSerializer elementTypeSerializer = createTypeSerializer(config, elementType, property);
        if (elementTypeSerializer != null) {
            staticTyping = false;
        } else if (!staticTyping) {
            staticTyping = usesStaticTyping(config, beanDesc, elementTypeSerializer, property);
        }
        JsonSerializer<Object> elementValueSerializer = findContentSerializer(config, beanDesc.getClassInfo(), property);
        if (type.isMapLikeType()) {
            MapLikeType mlt = (MapLikeType) type;
            JsonSerializer<Object> keySerializer = findKeySerializer(config, beanDesc.getClassInfo(), property);
            if (mlt.isTrueMapType()) {
                return buildMapSerializer(config, (MapType) mlt, beanDesc, property, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
            }
            return buildMapLikeSerializer(config, mlt, beanDesc, property, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
        }
        if (type.isCollectionLikeType()) {
            CollectionLikeType clt = (CollectionLikeType) type;
            if (clt.isTrueCollectionType()) {
                return buildCollectionSerializer(config, (CollectionType) clt, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
            }
            return buildCollectionLikeSerializer(config, clt, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
        }
        if (type.isArrayType()) {
            return buildArraySerializer(config, (ArrayType) type, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
        }
        return null;
    }

    protected JsonSerializer<?> buildCollectionLikeSerializer(SerializationConfig config, CollectionLikeType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        for (Serializers serializers : customSerializers()) {
            JsonSerializer<?> ser = serializers.findCollectionLikeSerializer(config, type, beanDesc, property, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        return null;
    }

    protected JsonSerializer<?> buildCollectionSerializer(SerializationConfig config, CollectionType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        for (Serializers serializers : customSerializers()) {
            JsonSerializer<?> ser = serializers.findCollectionSerializer(config, type, beanDesc, property, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        Class<?> raw = type.getRawClass();
        if (EnumSet.class.isAssignableFrom(raw)) {
            JsonSerializer<?> ser2 = buildEnumSetSerializer(config, type, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
            return ser2;
        }
        Class<?> elementRaw = type.getContentType().getRawClass();
        if (isIndexedList(raw)) {
            if (elementRaw == String.class) {
                JsonSerializer<?> ser3 = new IndexedStringListSerializer(property);
                return ser3;
            }
            JsonSerializer<?> ser4 = ContainerSerializers.indexedListSerializer(type.getContentType(), staticTyping, elementTypeSerializer, property, elementValueSerializer);
            return ser4;
        }
        if (elementRaw == String.class) {
            JsonSerializer<?> ser5 = new StringCollectionSerializer(property);
            return ser5;
        }
        JsonSerializer<?> ser6 = ContainerSerializers.collectionSerializer(type.getContentType(), staticTyping, elementTypeSerializer, property, elementValueSerializer);
        return ser6;
    }

    protected JsonSerializer<?> buildEnumSetSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        JavaType enumType = type.getContentType();
        if (!enumType.isEnumType()) {
            enumType = null;
        }
        return ContainerSerializers.enumSetSerializer(enumType, property);
    }

    protected boolean isIndexedList(Class<?> cls) {
        return RandomAccess.class.isAssignableFrom(cls);
    }

    protected JsonSerializer<?> buildMapLikeSerializer(SerializationConfig config, MapLikeType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        for (Serializers serializers : customSerializers()) {
            JsonSerializer<?> ser = serializers.findMapLikeSerializer(config, type, beanDesc, property, keySerializer, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        return null;
    }

    protected JsonSerializer<?> buildMapSerializer(SerializationConfig config, MapType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        for (Serializers serializers : customSerializers()) {
            JsonSerializer<?> ser = serializers.findMapSerializer(config, type, beanDesc, property, keySerializer, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        if (EnumMap.class.isAssignableFrom(type.getRawClass())) {
            JsonSerializer<?> ser2 = buildEnumMapSerializer(config, type, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
            return ser2;
        }
        JsonSerializer<?> ser3 = MapSerializer.construct(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()), type, staticTyping, elementTypeSerializer, property, keySerializer, elementValueSerializer);
        return ser3;
    }

    protected JsonSerializer<?> buildEnumMapSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        JavaType keyType = type.getKeyType();
        EnumValues enums = null;
        if (keyType.isEnumType()) {
            enums = EnumValues.construct(keyType.getRawClass(), config.getAnnotationIntrospector());
        }
        return new EnumMapSerializer(type.getContentType(), staticTyping, enums, elementTypeSerializer, property, elementValueSerializer);
    }

    protected JsonSerializer<?> buildArraySerializer(SerializationConfig config, ArrayType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        Class<?> raw = type.getRawClass();
        if (String[].class == raw) {
            return new ArraySerializers.StringArraySerializer(property);
        }
        JsonSerializer<?> ser = _arraySerializers.get(raw.getName());
        return ser == null ? new ObjectArraySerializer(type.getContentType(), staticTyping, elementTypeSerializer, property, elementValueSerializer) : ser;
    }

    protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        JavaType valueType = type.containedType(0);
        if (valueType == null) {
            valueType = TypeFactory.unknownType();
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.iteratorSerializer(valueType, usesStaticTyping(config, beanDesc, vts, property), vts, property);
    }

    protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        JavaType valueType = type.containedType(0);
        if (valueType == null) {
            valueType = TypeFactory.unknownType();
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.iterableSerializer(valueType, usesStaticTyping(config, beanDesc, vts, property), vts, property);
    }

    protected <T extends JavaType> T modifyTypeByAnnotation(SerializationConfig serializationConfig, Annotated annotated, T t) {
        Class<?> clsFindSerializationType = serializationConfig.getAnnotationIntrospector().findSerializationType(annotated);
        if (clsFindSerializationType != null) {
            try {
                t = (T) t.widenBy(clsFindSerializationType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to widen type " + t + " with concrete-type annotation (value " + clsFindSerializationType.getName() + "), method '" + annotated.getName() + "': " + e.getMessage());
            }
        }
        return (T) modifySecondaryTypesByAnnotation(serializationConfig, annotated, t);
    }

    protected static <T extends JavaType> T modifySecondaryTypesByAnnotation(SerializationConfig serializationConfig, Annotated annotated, T t) {
        AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        if (t.isContainerType()) {
            Class<?> clsFindSerializationKeyType = annotationIntrospector.findSerializationKeyType(annotated, t.getKeyType());
            if (clsFindSerializationKeyType != null) {
                if (!(t instanceof MapType)) {
                    throw new IllegalArgumentException("Illegal key-type annotation: type " + t + " is not a Map type");
                }
                try {
                    t = (T) ((MapType) t).widenKey(clsFindSerializationKeyType);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Failed to narrow key type " + t + " with key-type annotation (" + clsFindSerializationKeyType.getName() + "): " + e.getMessage());
                }
            }
            Class<?> clsFindSerializationContentType = annotationIntrospector.findSerializationContentType(annotated, t.getContentType());
            if (clsFindSerializationContentType == null) {
                return t;
            }
            try {
                return (T) t.widenContentsBy(clsFindSerializationContentType);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException("Failed to narrow content type " + t + " with content-type annotation (" + clsFindSerializationContentType.getName() + "): " + e2.getMessage());
            }
        }
        return t;
    }

    protected static JsonSerializer<Object> findKeySerializer(SerializationConfig config, Annotated a, BeanProperty property) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Class<? extends JsonSerializer<?>> serClass = intr.findKeySerializer(a);
        if ((serClass == null || serClass == JsonSerializer.None.class) && property != null) {
            serClass = intr.findKeySerializer(property.getMember());
        }
        if (serClass == null || serClass == JsonSerializer.None.class) {
            return null;
        }
        return config.serializerInstance(a, serClass);
    }

    protected static JsonSerializer<Object> findContentSerializer(SerializationConfig config, Annotated a, BeanProperty property) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Class<? extends JsonSerializer<?>> serClass = intr.findContentSerializer(a);
        if ((serClass == null || serClass == JsonSerializer.None.class) && property != null) {
            serClass = intr.findContentSerializer(property.getMember());
        }
        if (serClass == null || serClass == JsonSerializer.None.class) {
            return null;
        }
        return config.serializerInstance(a, serClass);
    }

    protected boolean usesStaticTyping(SerializationConfig config, BasicBeanDescription beanDesc, TypeSerializer typeSer, BeanProperty property) {
        if (typeSer != null) {
            return false;
        }
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        JsonSerialize.Typing t = intr.findSerializationTyping(beanDesc.getClassInfo());
        if (t != null) {
            if (t == JsonSerialize.Typing.STATIC) {
                return true;
            }
        } else if (config.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING)) {
            return true;
        }
        if (property == null) {
            return false;
        }
        JavaType type = property.getType();
        if (!type.isContainerType()) {
            return false;
        }
        if (intr.findSerializationContentType(property.getMember(), property.getType()) != null) {
            return true;
        }
        return (type instanceof MapType) && intr.findSerializationKeyType(property.getMember(), property.getType()) != null;
    }
}
