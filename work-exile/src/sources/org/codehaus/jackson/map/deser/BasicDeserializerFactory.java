package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualDeserializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.impl.StringCollectionDeserializer;
import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class BasicDeserializerFactory extends DeserializerFactory {
    protected static final HashMap<JavaType, JsonDeserializer<Object>> _arrayDeserializers;
    static final HashMap<String, Class<? extends Collection>> _collectionFallbacks;
    protected OptionalHandlerFactory optionalHandlers = OptionalHandlerFactory.instance;
    static final HashMap<JavaType, JsonDeserializer<Object>> _simpleDeserializers = StdDeserializers.constructAll();
    static final HashMap<String, Class<? extends Map>> _mapFallbacks = new HashMap<>();

    protected abstract JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType arrayType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType collectionType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType collectionLikeType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> cls, DeserializationConfig deserializationConfig, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomMapDeserializer(MapType mapType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType mapLikeType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> cls, DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;

    protected abstract JavaType mapAbstractType(DeserializationConfig deserializationConfig, JavaType javaType) throws JsonMappingException;

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public abstract DeserializerFactory withConfig(DeserializerFactory.Config config);

    static {
        _mapFallbacks.put(Map.class.getName(), LinkedHashMap.class);
        _mapFallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
        _mapFallbacks.put(SortedMap.class.getName(), TreeMap.class);
        _mapFallbacks.put("java.util.NavigableMap", TreeMap.class);
        try {
            _mapFallbacks.put(Class.forName("java.util.ConcurrentNavigableMap").getName(), (Class<? extends Map>) Class.forName("java.util.ConcurrentSkipListMap"));
        } catch (ClassNotFoundException e) {
        }
        _collectionFallbacks = new HashMap<>();
        _collectionFallbacks.put(Collection.class.getName(), ArrayList.class);
        _collectionFallbacks.put(List.class.getName(), ArrayList.class);
        _collectionFallbacks.put(Set.class.getName(), HashSet.class);
        _collectionFallbacks.put(SortedSet.class.getName(), TreeSet.class);
        _collectionFallbacks.put(Queue.class.getName(), LinkedList.class);
        _collectionFallbacks.put("java.util.Deque", LinkedList.class);
        _collectionFallbacks.put("java.util.NavigableSet", TreeSet.class);
        _arrayDeserializers = ArrayDeserializers.getAll();
    }

    protected BasicDeserializerFactory() {
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, DeserializerProvider p, ArrayType type, BeanProperty property) throws JsonMappingException {
        JavaType elemType = type.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) elemType.getValueHandler();
        if (contentDeser == null) {
            JsonDeserializer<?> deser = _arrayDeserializers.get(elemType);
            if (deser != null) {
                JsonDeserializer<?> custom = _findCustomArrayDeserializer(type, config, p, property, null, null);
                return custom != null ? custom : deser;
            }
            if (elemType.isPrimitive()) {
                throw new IllegalArgumentException("Internal error: primitive type (" + type + ") passed, no array deserializer found");
            }
        }
        TypeDeserializer elemTypeDeser = (TypeDeserializer) elemType.getTypeHandler();
        if (elemTypeDeser == null) {
            elemTypeDeser = findTypeDeserializer(config, elemType, property);
        }
        JsonDeserializer<?> custom2 = _findCustomArrayDeserializer(type, config, p, property, elemTypeDeser, contentDeser);
        if (custom2 != null) {
            return custom2;
        }
        if (contentDeser == null) {
            contentDeser = p.findValueDeserializer(config, elemType, property);
        }
        return new ArrayDeserializer(type, contentDeser, elemTypeDeser);
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, DeserializerProvider p, CollectionType type, BeanProperty property) throws Throwable {
        CollectionType type2 = (CollectionType) mapAbstractType(config, type);
        Class<?> collectionClass = type2.getRawClass();
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(collectionClass);
        JsonDeserializer<?> jsonDeserializerFindDeserializerFromAnnotation = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (jsonDeserializerFindDeserializerFromAnnotation == null) {
            CollectionType type3 = (CollectionType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
            JavaType contentType = type3.getContentType();
            JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
            TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
            if (contentTypeDeser == null) {
                contentTypeDeser = findTypeDeserializer(config, contentType, property);
            }
            JsonDeserializer<?> custom = _findCustomCollectionDeserializer(type3, config, p, beanDesc, property, contentTypeDeser, contentDeser);
            if (custom != null) {
                return custom;
            }
            if (contentDeser == null) {
                if (EnumSet.class.isAssignableFrom(collectionClass)) {
                    JsonDeserializer<Object> deser = new EnumSetDeserializer(constructEnumResolver(contentType.getRawClass(), config));
                    return deser;
                }
                contentDeser = p.findValueDeserializer(config, contentType, property);
            }
            if (type3.isInterface() || type3.isAbstract()) {
                Class<? extends Collection> fallback = _collectionFallbacks.get(collectionClass.getName());
                if (fallback == null) {
                    throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + type3);
                }
                collectionClass = fallback;
            }
            boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
            Constructor<Collection<Object>> ctor = ClassUtil.findConstructor(collectionClass, fixAccess);
            if (contentType.getRawClass() == String.class) {
                JsonDeserializer<Object> deser2 = new StringCollectionDeserializer(type3, contentDeser, ctor);
                return deser2;
            }
            JsonDeserializer<Object> deser3 = new CollectionDeserializer(type3, contentDeser, contentTypeDeser, ctor);
            return deser3;
        }
        return jsonDeserializerFindDeserializerFromAnnotation;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationConfig config, DeserializerProvider p, CollectionLikeType type, BeanProperty property) throws JsonMappingException {
        CollectionLikeType type2 = (CollectionLikeType) mapAbstractType(config, type);
        Class<?> collectionClass = type2.getRawClass();
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(collectionClass);
        JsonDeserializer<?> jsonDeserializerFindDeserializerFromAnnotation = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (jsonDeserializerFindDeserializerFromAnnotation == null) {
            CollectionLikeType type3 = (CollectionLikeType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
            JavaType contentType = type3.getContentType();
            JsonDeserializer<?> jsonDeserializer = (JsonDeserializer) contentType.getValueHandler();
            TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
            if (contentTypeDeser == null) {
                contentTypeDeser = findTypeDeserializer(config, contentType, property);
            }
            return _findCustomCollectionLikeDeserializer(type3, config, p, beanDesc, property, contentTypeDeser, jsonDeserializer);
        }
        return jsonDeserializerFindDeserializerFromAnnotation;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, DeserializerProvider p, MapType type, BeanProperty property) throws JsonMappingException {
        MapType type2 = (MapType) mapAbstractType(config, type);
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        JsonDeserializer<?> jsonDeserializerFindDeserializerFromAnnotation = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (jsonDeserializerFindDeserializerFromAnnotation == null) {
            MapType type3 = (MapType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
            JavaType keyType = type3.getKeyType();
            JavaType contentType = type3.getContentType();
            JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
            KeyDeserializer keyDes = (KeyDeserializer) keyType.getValueHandler();
            if (keyDes == null) {
                keyDes = p.findKeyDeserializer(config, keyType, property);
            }
            TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
            if (contentTypeDeser == null) {
                contentTypeDeser = findTypeDeserializer(config, contentType, property);
            }
            JsonDeserializer<?> custom = _findCustomMapDeserializer(type3, config, p, beanDesc, property, keyDes, contentTypeDeser, contentDeser);
            if (custom != null) {
                return custom;
            }
            if (contentDeser == null) {
                contentDeser = p.findValueDeserializer(config, contentType, property);
            }
            Class<?> mapClass = type3.getRawClass();
            if (EnumMap.class.isAssignableFrom(mapClass)) {
                Class<?> kt = keyType.getRawClass();
                if (kt == null || !kt.isEnum()) {
                    throw new IllegalArgumentException("Can not construct EnumMap; generic (key) type not available");
                }
                JsonDeserializer<Object> deser = new EnumMapDeserializer(constructEnumResolver(kt, config), contentDeser);
                return deser;
            }
            if (type3.isInterface() || type3.isAbstract()) {
                Class<? extends Map> fallback = _mapFallbacks.get(mapClass.getName());
                if (fallback == null) {
                    throw new IllegalArgumentException("Can not find a deserializer for non-concrete Map type " + type3);
                }
                type3 = (MapType) type3.forcedNarrowBy(fallback);
                beanDesc = (BasicBeanDescription) config.introspectForCreation(type3);
            }
            boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
            Constructor<?> constructorFindDefaultConstructor = beanDesc.findDefaultConstructor();
            if (constructorFindDefaultConstructor != null && fixAccess) {
                ClassUtil.checkAndFixAccess(constructorFindDefaultConstructor);
            }
            MapDeserializer md = new MapDeserializer(type3, constructorFindDefaultConstructor, keyDes, contentDeser, contentTypeDeser);
            md.setIgnorableProperties(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()));
            md.setCreators(findMapCreators(config, beanDesc));
            return md;
        }
        return jsonDeserializerFindDeserializerFromAnnotation;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createMapLikeDeserializer(DeserializationConfig config, DeserializerProvider p, MapLikeType type, BeanProperty property) throws JsonMappingException {
        MapLikeType type2 = (MapLikeType) mapAbstractType(config, type);
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        JsonDeserializer<?> jsonDeserializerFindDeserializerFromAnnotation = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (jsonDeserializerFindDeserializerFromAnnotation == null) {
            MapLikeType type3 = (MapLikeType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
            JavaType keyType = type3.getKeyType();
            JavaType contentType = type3.getContentType();
            JsonDeserializer<?> jsonDeserializer = (JsonDeserializer) contentType.getValueHandler();
            KeyDeserializer keyDes = (KeyDeserializer) keyType.getValueHandler();
            if (keyDes == null) {
                keyDes = p.findKeyDeserializer(config, keyType, property);
            }
            TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
            if (contentTypeDeser == null) {
                contentTypeDeser = findTypeDeserializer(config, contentType, property);
            }
            return _findCustomMapLikeDeserializer(type3, config, p, beanDesc, property, keyDes, contentTypeDeser, jsonDeserializer);
        }
        return jsonDeserializerFindDeserializerFromAnnotation;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type);
        JsonDeserializer<?> des = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (des == null) {
            Class<?> enumClass = type.getRawClass();
            JsonDeserializer<?> custom = _findCustomEnumDeserializer(enumClass, config, beanDesc, property);
            if (custom != null) {
                return custom;
            }
            for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
                if (config.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
                    int argCount = factory.getParameterCount();
                    if (argCount == 1) {
                        Class<?> returnType = factory.getRawType();
                        if (returnType.isAssignableFrom(enumClass)) {
                            return EnumDeserializer.deserializerForCreator(config, enumClass, factory);
                        }
                    }
                    throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
                }
            }
            return new EnumDeserializer(constructEnumResolver(enumClass, config));
        }
        return des;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType nodeType, BeanProperty property) throws JsonMappingException {
        Class<?> rawClass = nodeType.getRawClass();
        JsonDeserializer<?> custom = _findCustomTreeNodeDeserializer(rawClass, config, property);
        return custom != null ? custom : JsonNodeDeserializer.getDeserializer(rawClass);
    }

    protected JsonDeserializer<Object> findStdBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        JavaType referencedType;
        JsonDeserializer<Object> deser = _simpleDeserializers.get(type);
        if (deser == null) {
            Class<?> cls = type.getRawClass();
            if (AtomicReference.class.isAssignableFrom(cls)) {
                TypeFactory tf = config.getTypeFactory();
                JavaType[] params = tf.findTypeParameters(type, AtomicReference.class);
                if (params == null || params.length < 1) {
                    referencedType = TypeFactory.unknownType();
                } else {
                    referencedType = params[0];
                }
                JsonDeserializer<?> d2 = new StdDeserializer.AtomicReferenceDeserializer(referencedType, property);
                return d2;
            }
            JsonDeserializer<?> d = this.optionalHandlers.findDeserializer(type, config, p);
            if (d != null) {
                return d;
            }
            return null;
        }
        return deser;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property) {
        Class<?> cls = baseType.getRawClass();
        BasicBeanDescription bean = (BasicBeanDescription) config.introspectClassAnnotations(cls);
        AnnotatedClass ac = bean.getClassInfo();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
            if (b == null) {
                return null;
            }
        } else {
            subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
        }
        return b.buildTypeDeserializer(config, baseType, subtypes, property);
    }

    public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated, BeanProperty property) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, annotated, baseType);
        if (b == null) {
            return findTypeDeserializer(config, baseType, property);
        }
        Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(annotated, config, ai);
        return b.buildTypeDeserializer(config, baseType, subtypes, property);
    }

    public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity, BeanProperty property) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, propertyEntity, containerType);
        JavaType contentType = containerType.getContentType();
        if (b == null) {
            return findTypeDeserializer(config, contentType, property);
        }
        Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(propertyEntity, config, ai);
        return b.buildTypeDeserializer(config, contentType, subtypes, property);
    }

    protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationConfig config, Annotated ann, BeanProperty property) throws JsonMappingException {
        Object deserDef = config.getAnnotationIntrospector().findDeserializer(ann);
        if (deserDef != null) {
            return _constructDeserializer(config, ann, property, deserDef);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    JsonDeserializer<Object> _constructDeserializer(DeserializationConfig config, Annotated ann, BeanProperty property, Object deserDef) throws JsonMappingException {
        if (deserDef instanceof JsonDeserializer) {
            JsonDeserializer<Object> jsonDeserializer = (JsonDeserializer) deserDef;
            if (jsonDeserializer instanceof ContextualDeserializer) {
                JsonDeserializer<Object> deser = ((ContextualDeserializer) jsonDeserializer).createContextual(config, property);
                return deser;
            }
            return jsonDeserializer;
        }
        if (!(deserDef instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
        }
        Class<? extends JsonDeserializer<?>> deserClass = (Class) deserDef;
        if (!JsonDeserializer.class.isAssignableFrom(deserClass)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<JsonDeserializer>");
        }
        JsonDeserializer<Object> jsonDeserializerDeserializerInstance = config.deserializerInstance(ann, deserClass);
        if (jsonDeserializerDeserializerInstance instanceof ContextualDeserializer) {
            JsonDeserializer<Object> deser2 = ((ContextualDeserializer) jsonDeserializerDeserializerInstance).createContextual(config, property);
            return deser2;
        }
        return jsonDeserializerDeserializerInstance;
    }

    protected <T extends JavaType> T modifyTypeByAnnotation(DeserializationConfig deserializationConfig, Annotated annotated, T t, String str) throws JsonMappingException {
        JavaType javaType;
        JavaType javaTypeNarrowContentsBy;
        Class<? extends JsonDeserializer<?>> clsFindContentDeserializer;
        Class<? extends KeyDeserializer> clsFindKeyDeserializer;
        AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        Class<?> clsFindDeserializationType = annotationIntrospector.findDeserializationType(annotated, t, str);
        JavaType javaType2 = t;
        if (clsFindDeserializationType != null) {
            try {
                javaType2 = (T) t.narrowBy(clsFindDeserializationType);
            } catch (IllegalArgumentException e) {
                throw new JsonMappingException("Failed to narrow type " + t + " with concrete-type annotation (value " + clsFindDeserializationType.getName() + "), method '" + annotated.getName() + "': " + e.getMessage(), null, e);
            }
        }
        boolean zIsContainerType = javaType2.isContainerType();
        JavaType javaType3 = javaType2;
        if (zIsContainerType) {
            Class<?> clsFindDeserializationKeyType = annotationIntrospector.findDeserializationKeyType(annotated, javaType2.getKeyType(), str);
            if (clsFindDeserializationKeyType != null) {
                if (!(javaType2 instanceof MapType)) {
                    javaType = javaType2;
                    throw new JsonMappingException("Illegal key-type annotation: type " + javaType2 + " is not a Map type");
                }
                try {
                    javaType = javaType2;
                    javaType = (T) javaType2.narrowKey(clsFindDeserializationKeyType);
                } catch (IllegalArgumentException e2) {
                    throw new JsonMappingException("Failed to narrow key type " + javaType2 + " with key-type annotation (" + clsFindDeserializationKeyType.getName() + "): " + e2.getMessage(), null, e2);
                }
            }
            javaType = javaType2;
            JavaType keyType = javaType.getKeyType();
            if (keyType != null && keyType.getValueHandler() == null && (clsFindKeyDeserializer = annotationIntrospector.findKeyDeserializer(annotated)) != null && clsFindKeyDeserializer != KeyDeserializer.None.class) {
                keyType.setValueHandler(deserializationConfig.keyDeserializerInstance(annotated, clsFindKeyDeserializer));
            }
            Class<?> clsFindDeserializationContentType = annotationIntrospector.findDeserializationContentType(annotated, javaType.getContentType(), str);
            javaTypeNarrowContentsBy = javaType;
            if (clsFindDeserializationContentType != null) {
                try {
                    javaTypeNarrowContentsBy = javaType.narrowContentsBy(clsFindDeserializationContentType);
                } catch (IllegalArgumentException e3) {
                    throw new JsonMappingException("Failed to narrow content type " + javaType + " with content-type annotation (" + clsFindDeserializationContentType.getName() + "): " + e3.getMessage(), null, e3);
                }
            }
            Object valueHandler = javaTypeNarrowContentsBy.getContentType().getValueHandler();
            javaType3 = javaTypeNarrowContentsBy;
            if (valueHandler == null && (clsFindContentDeserializer = annotationIntrospector.findContentDeserializer(annotated)) != null && clsFindContentDeserializer != JsonDeserializer.None.class) {
                javaType3 = javaTypeNarrowContentsBy;
                javaType3 = javaTypeNarrowContentsBy;
                javaTypeNarrowContentsBy.getContentType().setValueHandler(deserializationConfig.deserializerInstance(annotated, clsFindContentDeserializer));
                javaType3 = javaTypeNarrowContentsBy;
            }
        }
        javaType3 = javaTypeNarrowContentsBy;
        javaType3 = javaTypeNarrowContentsBy;
        javaType3 = javaTypeNarrowContentsBy;
        return (T) javaType3;
    }

    protected JavaType resolveType(DeserializationConfig config, BasicBeanDescription beanDesc, JavaType type, AnnotatedMember member, BeanProperty property) {
        TypeDeserializer valueTypeDeser;
        TypeDeserializer contentTypeDeser;
        Class<? extends KeyDeserializer> kdClass;
        if (type.isContainerType()) {
            AnnotationIntrospector intr = config.getAnnotationIntrospector();
            JavaType keyType = type.getKeyType();
            if (keyType != null && (kdClass = intr.findKeyDeserializer(member)) != null && kdClass != KeyDeserializer.None.class) {
                KeyDeserializer kd = config.keyDeserializerInstance(member, kdClass);
                keyType.setValueHandler(kd);
            }
            Class<? extends JsonDeserializer<?>> cdClass = intr.findContentDeserializer(member);
            if (cdClass != null && cdClass != JsonDeserializer.None.class) {
                JsonDeserializer<Object> cd = config.deserializerInstance(member, cdClass);
                type.getContentType().setValueHandler(cd);
            }
            if ((member instanceof AnnotatedMember) && (contentTypeDeser = findPropertyContentTypeDeserializer(config, type, member, property)) != null) {
                type = type.withContentTypeHandler(contentTypeDeser);
            }
        }
        if (member instanceof AnnotatedMember) {
            valueTypeDeser = findPropertyTypeDeserializer(config, type, member, property);
        } else {
            valueTypeDeser = findTypeDeserializer(config, type, null);
        }
        if (valueTypeDeser != null) {
            return type.withTypeHandler(valueTypeDeser);
        }
        return type;
    }

    protected EnumResolver<?> constructEnumResolver(Class<?> enumClass, DeserializationConfig config) {
        return config.isEnabled(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING) ? EnumResolver.constructUnsafeUsingToString(enumClass) : EnumResolver.constructUnsafe(enumClass, config.getAnnotationIntrospector());
    }

    protected CreatorContainer findMapCreators(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        CreatorContainer creators = new CreatorContainer(beanDesc, fixAccess);
        for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
            int argCount = ctor.getParameterCount();
            if (argCount >= 1 && intr.hasCreatorAnnotation(ctor)) {
                SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
                int nameCount = 0;
                for (int i = 0; i < argCount; i++) {
                    AnnotatedParameter param = ctor.getParameter(i);
                    String name = param == null ? null : intr.findPropertyNameForParam(param);
                    if (name == null || name.length() == 0) {
                        throw new IllegalArgumentException("Parameter #" + i + " of constructor " + ctor + " has no property name annotation: must have for @JsonCreator for a Map type");
                    }
                    nameCount++;
                    properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
                }
                creators.addPropertyConstructor(ctor, properties);
            }
        }
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            int argCount2 = factory.getParameterCount();
            if (argCount2 >= 1 && intr.hasCreatorAnnotation(factory)) {
                SettableBeanProperty[] properties2 = new SettableBeanProperty[argCount2];
                int nameCount2 = 0;
                for (int i2 = 0; i2 < argCount2; i2++) {
                    AnnotatedParameter param2 = factory.getParameter(i2);
                    String name2 = param2 == null ? null : intr.findPropertyNameForParam(param2);
                    if (name2 == null || name2.length() == 0) {
                        throw new IllegalArgumentException("Parameter #" + i2 + " of factory method " + factory + " has no property name annotation: must have for @JsonCreator for a Map type");
                    }
                    nameCount2++;
                    properties2[i2] = constructCreatorProperty(config, beanDesc, name2, i2, param2);
                }
                creators.addPropertyFactory(factory, properties2);
            }
        }
        return creators;
    }

    protected SettableBeanProperty constructCreatorProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, int index, AnnotatedParameter param) throws JsonMappingException {
        JavaType t0 = config.getTypeFactory().constructType(param.getParameterType(), beanDesc.bindingsForBeanType());
        BeanProperty.Std property = new BeanProperty.Std(name, t0, beanDesc.getClassAnnotations(), param);
        JavaType type = resolveType(config, beanDesc, t0, param, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, param, property);
        JavaType type2 = modifyTypeByAnnotation(config, param, type, name);
        TypeDeserializer typeDeser = findTypeDeserializer(config, type2, property);
        SettableBeanProperty prop = new SettableBeanProperty.CreatorProperty(name, type2, typeDeser, beanDesc.getClassAnnotations(), param, index);
        if (deser != null) {
            prop.setValueDeserializer(deser);
        }
        return prop;
    }
}
