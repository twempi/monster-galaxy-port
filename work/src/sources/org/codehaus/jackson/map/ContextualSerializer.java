package org.codehaus.jackson.map;

/* JADX INFO: loaded from: classes.dex */
public interface ContextualSerializer<T> {
    JsonSerializer<T> createContextual(SerializationConfig serializationConfig, BeanProperty beanProperty) throws JsonMappingException;
}
