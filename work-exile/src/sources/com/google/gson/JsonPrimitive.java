package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class JsonPrimitive extends JsonElement {
    private Object value;
    private static final Class<?>[] PRIMITIVE_TYPES = {Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
    private static final BigInteger INTEGER_MAX = BigInteger.valueOf(2147483647L);
    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    public JsonPrimitive(Boolean bool) {
        setValue(bool);
    }

    public JsonPrimitive(Number number) {
        setValue(number);
    }

    public JsonPrimitive(String string) {
        setValue(string);
    }

    public JsonPrimitive(Character c) {
        setValue(c);
    }

    JsonPrimitive(Object primitive) {
        setValue(primitive);
    }

    void setValue(Object primitive) {
        if (primitive instanceof Character) {
            char c = ((Character) primitive).charValue();
            this.value = String.valueOf(c);
        } else {
            C$Gson$Preconditions.checkArgument((primitive instanceof Number) || isPrimitiveOrString(primitive));
            this.value = primitive;
        }
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    @Override // com.google.gson.JsonElement
    Boolean getAsBooleanWrapper() {
        return (Boolean) this.value;
    }

    @Override // com.google.gson.JsonElement
    public boolean getAsBoolean() {
        return isBoolean() ? getAsBooleanWrapper().booleanValue() : Boolean.parseBoolean(getAsString());
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    @Override // com.google.gson.JsonElement
    public Number getAsNumber() {
        return this.value instanceof String ? stringToNumber((String) this.value) : (Number) this.value;
    }

    static Number stringToNumber(String value) {
        Number numberValueOf;
        try {
            long longValue = Long.parseLong(value);
            if (longValue >= -2147483648L && longValue <= 2147483647L) {
                numberValueOf = Integer.valueOf((int) longValue);
            } else {
                numberValueOf = Long.valueOf(longValue);
            }
            return numberValueOf;
        } catch (NumberFormatException e) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e2) {
                return Double.valueOf(Double.parseDouble(value));
            }
        }
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    @Override // com.google.gson.JsonElement
    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        }
        if (isBoolean()) {
            return getAsBooleanWrapper().toString();
        }
        return (String) this.value;
    }

    @Override // com.google.gson.JsonElement
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public BigDecimal getAsBigDecimal() {
        return this.value instanceof BigDecimal ? (BigDecimal) this.value : new BigDecimal(this.value.toString());
    }

    @Override // com.google.gson.JsonElement
    public BigInteger getAsBigInteger() {
        return this.value instanceof BigInteger ? (BigInteger) this.value : new BigInteger(this.value.toString());
    }

    @Override // com.google.gson.JsonElement
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public char getAsCharacter() {
        return getAsString().charAt(0);
    }

    @Override // com.google.gson.JsonElement
    Object getAsObject() {
        if (this.value instanceof BigInteger) {
            BigInteger big = (BigInteger) this.value;
            if (big.compareTo(INTEGER_MAX) < 0) {
                return Integer.valueOf(big.intValue());
            }
            if (big.compareTo(LONG_MAX) < 0) {
                return Long.valueOf(big.longValue());
            }
        }
        return this.value;
    }

    @Override // com.google.gson.JsonElement
    protected void toString(Appendable sb, Escaper escaper) throws IOException {
        if (isString()) {
            sb.append('\"');
            sb.append(escaper.escapeJsonString(this.value.toString()));
            sb.append('\"');
            return;
        }
        sb.append(this.value.toString());
    }

    private static boolean isPrimitiveOrString(Object target) {
        if (target instanceof String) {
            return true;
        }
        Class<?> classOfPrimitive = target.getClass();
        for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
            if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (isIntegral(this)) {
            long value = getAsNumber().longValue();
            return (int) ((value >>> 32) ^ value);
        }
        if (isFloatingPoint(this)) {
            long value2 = Double.doubleToLongBits(getAsNumber().doubleValue());
            return (int) ((value2 >>> 32) ^ value2);
        }
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JsonPrimitive other = (JsonPrimitive) obj;
        if (this.value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (isFloatingPoint(this) && isFloatingPoint(other)) {
            double a = getAsNumber().doubleValue();
            double b = other.getAsNumber().doubleValue();
            if (a == b || (Double.isNaN(a) && Double.isNaN(b))) {
                z = true;
            }
            return z;
        }
        return this.value.equals(other.value);
    }

    private static boolean isIntegral(JsonPrimitive primitive) {
        if (!(primitive.value instanceof Number)) {
            return false;
        }
        Number number = (Number) primitive.value;
        return (number instanceof BigInteger) || (number instanceof Long) || (number instanceof Integer) || (number instanceof Short) || (number instanceof Byte);
    }

    private static boolean isFloatingPoint(JsonPrimitive primitive) {
        if (!(primitive.value instanceof Number)) {
            return false;
        }
        Number number = (Number) primitive.value;
        return (number instanceof BigDecimal) || (number instanceof Double) || (number instanceof Float);
    }
}
