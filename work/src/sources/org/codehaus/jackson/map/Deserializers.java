package org.codehaus.jackson.map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public interface Deserializers {
    JsonDeserializer<?> findArrayDeserializer(ArrayType arrayType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    JsonDeserializer<?> findBeanDeserializer(JavaType javaType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanDescription beanDescription, BeanProperty beanProperty) throws JsonMappingException;

    JsonDeserializer<?> findCollectionDeserializer(CollectionType collectionType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanDescription beanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType collectionLikeType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanDescription beanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    JsonDeserializer<?> findEnumDeserializer(Class<?> cls, DeserializationConfig deserializationConfig, BeanDescription beanDescription, BeanProperty beanProperty) throws JsonMappingException;

    JsonDeserializer<?> findMapDeserializer(MapType mapType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanDescription beanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    JsonDeserializer<?> findMapLikeDeserializer(MapLikeType mapLikeType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanDescription beanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> cls, DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;

    public static class None implements Deserializers {
        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override // org.codehaus.jackson.map.Deserializers
        public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
            return null;
        }
    }
}
