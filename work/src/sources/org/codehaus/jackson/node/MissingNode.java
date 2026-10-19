package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

/* JADX INFO: loaded from: classes.dex */
public final class MissingNode extends BaseJsonNode {
    private static final MissingNode instance = new MissingNode();

    private MissingNode() {
    }

    public static MissingNode getInstance() {
        return instance;
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.NOT_AVAILABLE;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isMissingNode() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return null;
    }

    @Override // org.codehaus.jackson.JsonNode
    public int getValueAsInt(int defaultValue) {
        return 0;
    }

    @Override // org.codehaus.jackson.JsonNode
    public long getValueAsLong(long defaultValue) {
        return 0L;
    }

    @Override // org.codehaus.jackson.JsonNode
    public double getValueAsDouble(double defaultValue) {
        return 0.0d;
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode path(String fieldName) {
        return this;
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode path(int index) {
        return this;
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeNull();
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializableWithType
    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        jg.writeNull();
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        return o == this;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String toString() {
        return "";
    }
}
