package com.google.gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class JsonElement {
    private static final Escaper BASIC_ESCAPER = new Escaper(false);

    protected abstract void toString(Appendable appendable, Escaper escaper) throws IOException;

    public boolean isJsonArray() {
        return this instanceof JsonArray;
    }

    public boolean isJsonObject() {
        return this instanceof JsonObject;
    }

    public boolean isJsonPrimitive() {
        return this instanceof JsonPrimitive;
    }

    public boolean isJsonNull() {
        return this instanceof JsonNull;
    }

    public JsonObject getAsJsonObject() {
        if (isJsonObject()) {
            return (JsonObject) this;
        }
        throw new IllegalStateException("This is not a JSON Object.");
    }

    public JsonArray getAsJsonArray() {
        if (isJsonArray()) {
            return (JsonArray) this;
        }
        throw new IllegalStateException("This is not a JSON Array.");
    }

    public JsonPrimitive getAsJsonPrimitive() {
        if (isJsonPrimitive()) {
            return (JsonPrimitive) this;
        }
        throw new IllegalStateException("This is not a JSON Primitive.");
    }

    public JsonNull getAsJsonNull() {
        if (isJsonNull()) {
            return (JsonNull) this;
        }
        throw new IllegalStateException("This is not a JSON Null.");
    }

    public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
    }

    Boolean getAsBooleanWrapper() {
        throw new UnsupportedOperationException();
    }

    public Number getAsNumber() {
        throw new UnsupportedOperationException();
    }

    public String getAsString() {
        throw new UnsupportedOperationException();
    }

    public double getAsDouble() {
        throw new UnsupportedOperationException();
    }

    public float getAsFloat() {
        throw new UnsupportedOperationException();
    }

    public long getAsLong() {
        throw new UnsupportedOperationException();
    }

    public int getAsInt() {
        throw new UnsupportedOperationException();
    }

    public byte getAsByte() {
        throw new UnsupportedOperationException();
    }

    public char getAsCharacter() {
        throw new UnsupportedOperationException();
    }

    public BigDecimal getAsBigDecimal() {
        throw new UnsupportedOperationException();
    }

    public BigInteger getAsBigInteger() {
        throw new UnsupportedOperationException();
    }

    public short getAsShort() {
        throw new UnsupportedOperationException();
    }

    Object getAsObject() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            toString(sb, BASIC_ESCAPER);
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
