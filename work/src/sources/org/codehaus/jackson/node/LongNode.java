package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: classes.dex */
public final class LongNode extends NumericNode {
    final long _value;

    public LongNode(long v) {
        this._value = v;
    }

    public static LongNode valueOf(long l) {
        return new LongNode(l);
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_INT;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonParser.NumberType getNumberType() {
        return JsonParser.NumberType.LONG;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isIntegralNumber() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isLong() {
        return true;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public Number getNumberValue() {
        return Long.valueOf(this._value);
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public int getIntValue() {
        return (int) this._value;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public long getLongValue() {
        return this._value;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public double getDoubleValue() {
        return this._value;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf(this._value);
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public BigInteger getBigIntegerValue() {
        return BigInteger.valueOf(this._value);
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return NumberOutput.toString(this._value);
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean getValueAsBoolean(boolean defaultValue) {
        return this._value != 0;
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeNumber(this._value);
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return o != null && o.getClass() == getClass() && ((LongNode) o)._value == this._value;
    }

    public int hashCode() {
        return ((int) this._value) ^ ((int) (this._value >> 32));
    }
}
