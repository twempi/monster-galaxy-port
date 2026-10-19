package org.codehaus.jackson.map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ContextualSerializer<T> {
    JsonSerializer<T> createContextual(SerializationConfig serializationConfig, BeanProperty beanProperty) throws JsonMappingException;
}
