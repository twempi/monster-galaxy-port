package org.codehaus.jackson.map;

/* JADX INFO: loaded from: classes.dex */
public interface ContextualKeyDeserializer {
    KeyDeserializer createContextual(DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;
}
