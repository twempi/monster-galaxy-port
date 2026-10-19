package org.codehaus.jackson.schema;

import java.lang.reflect.Type;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface SchemaAware {
    JsonNode getSchema(SerializerProvider serializerProvider, Type type) throws JsonMappingException;
}
