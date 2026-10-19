package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public interface KeyDeserializers {
    KeyDeserializer findKeyDeserializer(JavaType javaType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, BeanProperty beanProperty) throws JsonMappingException;
}
