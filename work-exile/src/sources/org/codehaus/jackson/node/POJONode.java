package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class POJONode extends ValueNode {
    protected final Object _value;

    public POJONode(Object v) {
        this._value = v;
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.VALUE_EMBEDDED_OBJECT;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isPojo() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return this._value == null ? "null" : this._value.toString();
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean getValueAsBoolean(boolean defaultValue) {
        if (this._value != null && (this._value instanceof Boolean)) {
            return ((Boolean) this._value).booleanValue();
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonNode
    public int getValueAsInt(int defaultValue) {
        if (this._value instanceof Number) {
            int defaultValue2 = ((Number) this._value).intValue();
            return defaultValue2;
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonNode
    public long getValueAsLong(long defaultValue) {
        if (this._value instanceof Number) {
            long defaultValue2 = ((Number) this._value).longValue();
            return defaultValue2;
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonNode
    public double getValueAsDouble(double defaultValue) {
        if (this._value instanceof Number) {
            double defaultValue2 = ((Number) this._value).doubleValue();
            return defaultValue2;
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        if (this._value == null) {
            jg.writeNull();
        } else {
            jg.writeObject(this._value);
        }
    }

    public Object getPojo() {
        return this._value;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            POJONode other = (POJONode) o;
            if (this._value == null) {
                return other._value == null;
            }
            return this._value.equals(other._value);
        }
        return false;
    }

    public int hashCode() {
        return this._value.hashCode();
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.JsonNode
    public String toString() {
        return String.valueOf(this._value);
    }
}
