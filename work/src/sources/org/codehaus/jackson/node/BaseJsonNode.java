package org.codehaus.jackson.node;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseJsonNode extends JsonNode implements JsonSerializableWithType {
    @Override // org.codehaus.jackson.JsonNode
    public abstract JsonToken asToken();

    public abstract void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException;

    public abstract void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException;

    protected BaseJsonNode() {
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode findValue(String fieldName) {
        return null;
    }

    @Override // org.codehaus.jackson.JsonNode
    public final JsonNode findPath(String fieldName) {
        JsonNode value = findValue(fieldName);
        if (value == null) {
            return MissingNode.getInstance();
        }
        return value;
    }

    @Override // org.codehaus.jackson.JsonNode
    public ObjectNode findParent(String fieldName) {
        return null;
    }

    @Override // org.codehaus.jackson.JsonNode
    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.JsonNode
    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.JsonNode
    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonParser traverse() {
        return new TreeTraversingParser(this);
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonParser.NumberType getNumberType() {
        return null;
    }

    @Override // org.codehaus.jackson.JsonNode
    public final void writeTo(JsonGenerator jgen) throws IOException {
        serialize(jgen, null);
    }
}
