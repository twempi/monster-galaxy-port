package org.codehaus.jackson.map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ContextualKeyDeserializer {
    KeyDeserializer createContextual(DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;
}
