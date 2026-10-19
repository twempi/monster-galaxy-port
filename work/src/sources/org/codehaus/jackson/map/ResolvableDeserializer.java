package org.codehaus.jackson.map;

/* JADX INFO: loaded from: classes.dex */
public interface ResolvableDeserializer {
    void resolve(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider) throws JsonMappingException;
}
