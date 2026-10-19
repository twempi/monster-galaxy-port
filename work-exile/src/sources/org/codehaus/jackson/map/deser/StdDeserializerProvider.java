package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualDeserializer;
import org.codehaus.jackson.map.ContextualKeyDeserializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class StdDeserializerProvider extends DeserializerProvider {
    static final HashMap<JavaType, KeyDeserializer> _keyDeserializers = StdKeyDeserializers.constructAll();
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers;
    protected DeserializerFactory _factory;
    protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers;

    public StdDeserializerProvider() {
        this(BeanDeserializerFactory.instance);
    }

    public StdDeserializerProvider(DeserializerFactory f) {
        this._cachedDeserializers = new ConcurrentHashMap<>(64, 0.75f, 2);
        this._incompleteDeserializers = new HashMap<>(8);
        this._factory = f;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public DeserializerProvider withAdditionalDeserializers(Deserializers d) {
        this._factory = this._factory.withAdditionalDeserializers(d);
        return this;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public DeserializerProvider withAdditionalKeyDeserializers(KeyDeserializers d) {
        this._factory = this._factory.withAdditionalKeyDeserializers(d);
        return this;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public DeserializerProvider withDeserializerModifier(BeanDeserializerModifier modifier) {
        this._factory = this._factory.withDeserializerModifier(modifier);
        return this;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public DeserializerProvider withAbstractTypeResolver(AbstractTypeResolver resolver) {
        this._factory = this._factory.withAbstractTypeResolver(resolver);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [org.codehaus.jackson.map.JsonDeserializer<java.lang.Object>] */
    /* JADX WARN: Type inference failed for: r2v1, types: [org.codehaus.jackson.map.JsonDeserializer<java.lang.Object>] */
    @Override // org.codehaus.jackson.map.DeserializerProvider
    public JsonDeserializer<Object> findValueDeserializer(DeserializationConfig config, JavaType propertyType, BeanProperty property) throws JsonMappingException {
        Object obj_findCachedDeserializer = _findCachedDeserializer(propertyType);
        if (obj_findCachedDeserializer != null) {
            if (obj_findCachedDeserializer instanceof ContextualDeserializer) {
                Object d = ((ContextualDeserializer) obj_findCachedDeserializer).createContextual(config, property);
                obj_findCachedDeserializer = d;
            }
            return obj_findCachedDeserializer;
        }
        Object obj_createAndCacheValueDeserializer = _createAndCacheValueDeserializer(config, propertyType, property);
        if (obj_createAndCacheValueDeserializer == null) {
            obj_createAndCacheValueDeserializer = _handleUnknownValueDeserializer(propertyType);
        }
        if (obj_createAndCacheValueDeserializer instanceof ContextualDeserializer) {
            Object d2 = ((ContextualDeserializer) obj_createAndCacheValueDeserializer).createContextual(config, property);
            obj_createAndCacheValueDeserializer = d2;
        }
        return obj_createAndCacheValueDeserializer;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = findValueDeserializer(config, type, property);
        TypeDeserializer typeDeser = this._factory.findTypeDeserializer(config, type, property);
        if (typeDeser != null) {
            return new WrappedDeserializer(typeDeser, deser);
        }
        return deser;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [org.codehaus.jackson.map.KeyDeserializer] */
    @Override // org.codehaus.jackson.map.DeserializerProvider
    public KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        Object objCreateKeyDeserializer = this._factory.createKeyDeserializer(config, type, property);
        if (objCreateKeyDeserializer == null) {
            Class<?> raw = type.getRawClass();
            if (raw == String.class || raw == Object.class) {
                return null;
            }
            KeyDeserializer kdes = _keyDeserializers.get(type);
            if (kdes == null) {
                if (type.isEnumType()) {
                    return StdKeyDeserializers.constructEnumKeyDeserializer(config, type);
                }
                KeyDeserializer kdes2 = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
                if (kdes2 == null) {
                    if (objCreateKeyDeserializer == null) {
                        return _handleUnknownKeyDeserializer(type);
                    }
                } else {
                    return kdes2;
                }
            } else {
                return kdes;
            }
        }
        if (objCreateKeyDeserializer instanceof ContextualKeyDeserializer) {
            objCreateKeyDeserializer = ((ContextualKeyDeserializer) objCreateKeyDeserializer).createContextual(config, property);
        }
        return objCreateKeyDeserializer;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public boolean hasValueDeserializerFor(DeserializationConfig config, JavaType type) {
        JsonDeserializer<Object> deser = _findCachedDeserializer(type);
        if (deser == null) {
            try {
                deser = _createAndCacheValueDeserializer(config, type, null);
            } catch (Exception e) {
                return false;
            }
        }
        return deser != null;
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public int cachedDeserializersCount() {
        return this._cachedDeserializers.size();
    }

    @Override // org.codehaus.jackson.map.DeserializerProvider
    public void flushCachedDeserializers() {
        this._cachedDeserializers.clear();
    }

    protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type) {
        return this._cachedDeserializers.get(type);
    }

    protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser;
        synchronized (this._incompleteDeserializers) {
            JsonDeserializer<Object> deser2 = _findCachedDeserializer(type);
            if (deser2 != null) {
                return deser2;
            }
            int count = this._incompleteDeserializers.size();
            if (count > 0 && (deser = this._incompleteDeserializers.get(type)) != null) {
                return deser;
            }
            try {
                JsonDeserializer<Object> jsonDeserializer_createAndCache2 = _createAndCache2(config, type, property);
                if (count == 0 && this._incompleteDeserializers.size() > 0) {
                    this._incompleteDeserializers.clear();
                }
                return jsonDeserializer_createAndCache2;
            } catch (Throwable th) {
                if (count == 0 && this._incompleteDeserializers.size() > 0) {
                    this._incompleteDeserializers.clear();
                }
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected JsonDeserializer<Object> _createAndCache2(DeserializationConfig deserializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException {
        try {
            JsonDeserializer<Object> jsonDeserializer_createDeserializer = _createDeserializer(deserializationConfig, javaType, beanProperty);
            if (jsonDeserializer_createDeserializer == 0) {
                return null;
            }
            boolean z = jsonDeserializer_createDeserializer instanceof ResolvableDeserializer;
            boolean zBooleanValue = jsonDeserializer_createDeserializer.getClass() == BeanDeserializer.class;
            if (!zBooleanValue && deserializationConfig.isEnabled(DeserializationConfig.Feature.USE_ANNOTATIONS)) {
                AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
                Boolean boolFindCachability = annotationIntrospector.findCachability(AnnotatedClass.construct(jsonDeserializer_createDeserializer.getClass(), annotationIntrospector, null));
                if (boolFindCachability != null) {
                    zBooleanValue = boolFindCachability.booleanValue();
                }
            }
            if (z) {
                this._incompleteDeserializers.put(javaType, jsonDeserializer_createDeserializer);
                _resolveDeserializer(deserializationConfig, (ResolvableDeserializer) jsonDeserializer_createDeserializer);
                this._incompleteDeserializers.remove(javaType);
            }
            if (zBooleanValue) {
                this._cachedDeserializers.put(javaType, jsonDeserializer_createDeserializer);
                return jsonDeserializer_createDeserializer;
            }
            return jsonDeserializer_createDeserializer;
        } catch (IllegalArgumentException e) {
            throw new JsonMappingException(e.getMessage(), null, e);
        }
    }

    protected JsonDeserializer<Object> _createDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        if (type.isEnumType()) {
            return this._factory.createEnumDeserializer(config, this, type, property);
        }
        if (type.isContainerType()) {
            if (type.isArrayType()) {
                return this._factory.createArrayDeserializer(config, this, (ArrayType) type, property);
            }
            if (type.isMapLikeType()) {
                MapLikeType mlt = (MapLikeType) type;
                if (mlt.isTrueMapType()) {
                    return this._factory.createMapDeserializer(config, this, (MapType) mlt, property);
                }
                return this._factory.createMapLikeDeserializer(config, this, mlt, property);
            }
            if (type.isCollectionLikeType()) {
                CollectionLikeType clt = (CollectionLikeType) type;
                if (clt.isTrueCollectionType()) {
                    return this._factory.createCollectionDeserializer(config, this, (CollectionType) clt, property);
                }
                return this._factory.createCollectionLikeDeserializer(config, this, clt, property);
            }
        }
        if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
            return this._factory.createTreeDeserializer(config, this, type, property);
        }
        return this._factory.createBeanDeserializer(config, this, type, property);
    }

    protected void _resolveDeserializer(DeserializationConfig config, ResolvableDeserializer ser) throws JsonMappingException {
        ser.resolve(config, this);
    }

    protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType type) throws JsonMappingException {
        Class<?> rawClass = type.getRawClass();
        if (!ClassUtil.isConcrete(rawClass)) {
            throw new JsonMappingException("Can not find a Value deserializer for abstract type " + type);
        }
        throw new JsonMappingException("Can not find a Value deserializer for type " + type);
    }

    protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType type) throws JsonMappingException {
        throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + type);
    }

    protected static final class WrappedDeserializer extends JsonDeserializer<Object> {
        final JsonDeserializer<Object> _deserializer;
        final TypeDeserializer _typeDeserializer;

        public WrappedDeserializer(TypeDeserializer typeDeser, JsonDeserializer<Object> deser) {
            this._typeDeserializer = typeDeser;
            this._deserializer = deser;
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return this._deserializer.deserializeWithType(jp, ctxt, this._typeDeserializer);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
        }
    }
}
