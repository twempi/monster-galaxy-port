package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class DecimalNode extends NumericNode {
    protected final BigDecimal _value;

    public DecimalNode(BigDecimal v) {
        this._value = v;
    }

    public static DecimalNode valueOf(BigDecimal d) {
        return new DecimalNode(d);
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_FLOAT;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonParser.NumberType getNumberType() {
        return JsonParser.NumberType.BIG_DECIMAL;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isFloatingPointNumber() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isBigDecimal() {
        return true;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public Number getNumberValue() {
        return this._value;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public int getIntValue() {
        return this._value.intValue();
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public long getLongValue() {
        return this._value.longValue();
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public BigInteger getBigIntegerValue() {
        return this._value.toBigInteger();
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public double getDoubleValue() {
        return this._value.doubleValue();
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public BigDecimal getDecimalValue() {
        return this._value;
    }

    @Override // org.codehaus.jackson.node.NumericNode, org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return this._value.toString();
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
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return ((DecimalNode) o)._value.equals(this._value);
    }

    public int hashCode() {
        return this._value.hashCode();
    }
}
