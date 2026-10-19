package org.codehaus.jackson;

import java.io.IOException;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

/* JADX INFO: loaded from: classes.dex */
public abstract class ObjectCodec {
    public abstract JsonNode createArrayNode();

    public abstract JsonNode createObjectNode();

    public abstract JsonNode readTree(JsonParser jsonParser) throws IOException;

    public abstract <T> T readValue(JsonParser jsonParser, Class<T> cls) throws IOException;

    public abstract <T> T readValue(JsonParser jsonParser, JavaType javaType) throws IOException;

    public abstract <T> T readValue(JsonParser jsonParser, TypeReference<?> typeReference) throws IOException;

    public abstract JsonParser treeAsTokens(JsonNode jsonNode);

    public abstract <T> T treeToValue(JsonNode jsonNode, Class<T> cls) throws IOException;

    public abstract void writeTree(JsonGenerator jsonGenerator, JsonNode jsonNode) throws IOException;

    public abstract void writeValue(JsonGenerator jsonGenerator, Object obj) throws IOException;

    protected ObjectCodec() {
    }
}
