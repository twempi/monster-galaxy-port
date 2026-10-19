package org.codehaus.jackson.map.module;

import java.util.HashMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class SimpleDeserializers implements Deserializers {
    protected HashMap<ClassKey, JsonDeserializer<?>> _classMappings = null;

    public <T> void addDeserializer(Class<T> forClass, JsonDeserializer<? extends T> deser) {
        ClassKey key = new ClassKey(forClass);
        if (this._classMappings == null) {
            this._classMappings = new HashMap<>();
        }
        this._classMappings.put(key, deser);
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    @Override // org.codehaus.jackson.map.Deserializers
    public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanProperty property) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(nodeType));
    }
}
