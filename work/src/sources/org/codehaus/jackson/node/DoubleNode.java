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
public final class DoubleNode extends NumericNode {
    protected final double _value;

    public DoubleNode(double v) {
        this._value = v;
    }

    public static DoubleNode valueOf(double v) {
        return new DoubleNode(v);
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_FLOAT;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonParser.NumberType getNumberType() {
        return JsonParser.NumberType.DOUBLE;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isFloatingPointNumber() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isDouble() {
        return true;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public Number getNumberValue() {
        return Double.valueOf(this._value);
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public int getIntValue() {
        return (int) this._value;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public long getLongValue() {
        return (long) this._value;
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
        return getDecimalValue().toBigInteger();
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return NumberOutput.toString(this._value);
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
        return o != null && o.getClass() == getClass() && ((DoubleNode) o)._value == this._value;
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this._value);
        return ((int) l) ^ ((int) (l >> 32));
    }
}
