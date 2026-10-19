package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class BooleanNode extends ValueNode {
    public static final BooleanNode TRUE = new BooleanNode();
    public static final BooleanNode FALSE = new BooleanNode();

    private BooleanNode() {
    }

    public static BooleanNode getTrue() {
        return TRUE;
    }

    public static BooleanNode getFalse() {
        return FALSE;
    }

    public static BooleanNode valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return this == TRUE ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isBoolean() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean getBooleanValue() {
        return this == TRUE;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return this == TRUE ? "true" : "false";
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean getValueAsBoolean() {
        return this == TRUE;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean getValueAsBoolean(boolean defaultValue) {
        return this == TRUE;
    }

    @Override // org.codehaus.jackson.JsonNode
    public int getValueAsInt(int defaultValue) {
        return this == TRUE ? 1 : 0;
    }

    @Override // org.codehaus.jackson.JsonNode
    public long getValueAsLong(long defaultValue) {
        return this == TRUE ? 1L : 0L;
    }

    @Override // org.codehaus.jackson.JsonNode
    public double getValueAsDouble(double defaultValue) {
        return this == TRUE ? 1.0d : 0.0d;
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeBoolean(this == TRUE);
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        return o == this;
    }
}
