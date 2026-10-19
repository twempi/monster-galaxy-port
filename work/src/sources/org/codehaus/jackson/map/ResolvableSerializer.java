package org.codehaus.jackson.map;

/* JADX INFO: loaded from: classes.dex */
public interface ResolvableSerializer {
    void resolve(SerializerProvider serializerProvider) throws JsonMappingException;
}
