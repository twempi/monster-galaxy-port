package org.codehaus.jackson.map;

/* JADX INFO: loaded from: classes.dex */
public interface ContextualDeserializer<T> {
    JsonDeserializer<T> createContextual(DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;
}
