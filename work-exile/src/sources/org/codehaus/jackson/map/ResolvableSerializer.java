package org.codehaus.jackson.map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ResolvableSerializer {
    void resolve(SerializerProvider serializerProvider) throws JsonMappingException;
}
