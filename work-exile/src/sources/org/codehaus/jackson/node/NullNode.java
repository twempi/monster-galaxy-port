package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class NullNode extends ValueNode {
    public static final NullNode instance = new NullNode();

    private NullNode() {
    }

    public static NullNode getInstance() {
        return instance;
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.VALUE_NULL;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isNull() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return "null";
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

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeNull();
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        return o == this;
    }
}
