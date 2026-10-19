package org.codehaus.jackson;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.SerializedString;

/* JADX INFO: loaded from: classes.dex */
public abstract class JsonGenerator implements Closeable, Versioned {
    protected PrettyPrinter _cfgPrettyPrinter;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    public abstract void copyCurrentEvent(JsonParser jsonParser) throws IOException;

    public abstract void copyCurrentStructure(JsonParser jsonParser) throws IOException;

    public abstract JsonGenerator disable(Feature feature);

    public abstract JsonGenerator enable(Feature feature);

    public abstract void flush() throws IOException;

    public abstract ObjectCodec getCodec();

    public abstract JsonStreamContext getOutputContext();

    public abstract boolean isClosed();

    public abstract boolean isEnabled(Feature feature);

    public abstract JsonGenerator setCodec(ObjectCodec objectCodec);

    public abstract JsonGenerator useDefaultPrettyPrinter();

    public abstract void writeBinary(Base64Variant base64Variant, byte[] bArr, int i, int i2) throws IOException;

    public abstract void writeBoolean(boolean z) throws IOException;

    public abstract void writeEndArray() throws IOException;

    public abstract void writeEndObject() throws IOException;

    public abstract void writeFieldName(String str) throws IOException;

    public abstract void writeNull() throws IOException;

    public abstract void writeNumber(double d) throws IOException;

    public abstract void writeNumber(float f) throws IOException;

    public abstract void writeNumber(int i) throws IOException;

    public abstract void writeNumber(long j) throws IOException;

    public abstract void writeNumber(String str) throws UnsupportedOperationException, IOException;

    public abstract void writeNumber(BigDecimal bigDecimal) throws IOException;

    public abstract void writeNumber(BigInteger bigInteger) throws IOException;

    public abstract void writeObject(Object obj) throws IOException;

    public abstract void writeRaw(char c) throws IOException;

    public abstract void writeRaw(String str) throws IOException;

    public abstract void writeRaw(String str, int i, int i2) throws IOException;

    public abstract void writeRaw(char[] cArr, int i, int i2) throws IOException;

    public abstract void writeRawUTF8String(byte[] bArr, int i, int i2) throws IOException;

    public abstract void writeRawValue(String str) throws IOException;

    public abstract void writeRawValue(String str, int i, int i2) throws IOException;

    public abstract void writeRawValue(char[] cArr, int i, int i2) throws IOException;

    public abstract void writeStartArray() throws IOException;

    public abstract void writeStartObject() throws IOException;

    public abstract void writeString(String str) throws IOException;

    public abstract void writeString(char[] cArr, int i, int i2) throws IOException;

    public abstract void writeTree(JsonNode jsonNode) throws IOException;

    public abstract void writeUTF8String(byte[] bArr, int i, int i2) throws IOException;

    public enum Feature {
        AUTO_CLOSE_TARGET(true),
        AUTO_CLOSE_JSON_CONTENT(true),
        QUOTE_FIELD_NAMES(true),
        QUOTE_NON_NUMERIC_NUMBERS(true),
        WRITE_NUMBERS_AS_STRINGS(false),
        FLUSH_PASSED_TO_STREAM(true),
        ESCAPE_NON_ASCII(false);

        final boolean _defaultState;
        final int _mask = 1 << ordinal();

        public static int collectDefaults() {
            int flags = 0;
            Feature[] arr$ = values();
            for (Feature f : arr$) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }

        Feature(boolean defaultState) {
            this._defaultState = defaultState;
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return this._mask;
        }
    }

    protected JsonGenerator() {
    }

    public void setSchema(FormatSchema schema) {
        throw new UnsupportedOperationException("Generator of type " + getClass().getName() + " does not support schema of type '" + schema.getSchemaType() + "'");
    }

    public boolean canUseSchema(FormatSchema schema) {
        return false;
    }

    @Override // org.codehaus.jackson.Versioned
    public Version version() {
        return Version.unknownVersion();
    }

    public Object getOutputTarget() {
        return null;
    }

    public JsonGenerator configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    @Deprecated
    public void enableFeature(Feature f) {
        enable(f);
    }

    @Deprecated
    public void disableFeature(Feature f) {
        disable(f);
    }

    @Deprecated
    public void setFeature(Feature f, boolean state) {
        configure(f, state);
    }

    @Deprecated
    public boolean isFeatureEnabled(Feature f) {
        return isEnabled(f);
    }

    public JsonGenerator setPrettyPrinter(PrettyPrinter pp) {
        this._cfgPrettyPrinter = pp;
        return this;
    }

    public JsonGenerator setHighestNonEscapedChar(int charCode) {
        return this;
    }

    public int getHighestEscapedChar() {
        return 0;
    }

    public CharacterEscapes getCharacterEscapes() {
        return null;
    }

    public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
        return this;
    }

    public void writeFieldName(SerializedString name) throws IOException {
        writeFieldName(name.getValue());
    }

    public void writeFieldName(SerializableString name) throws IOException {
        writeFieldName(name.getValue());
    }

    public void writeString(SerializableString text) throws IOException {
        writeString(text.getValue());
    }

    public void writeBinary(byte[] data, int offset, int len) throws IOException {
        writeBinary(Base64Variants.getDefaultVariant(), data, offset, len);
    }

    public void writeBinary(byte[] data) throws IOException {
        writeBinary(Base64Variants.getDefaultVariant(), data, 0, data.length);
    }

    public void writeStringField(String fieldName, String value) throws IOException {
        writeFieldName(fieldName);
        writeString(value);
    }

    public final void writeBooleanField(String fieldName, boolean value) throws IOException {
        writeFieldName(fieldName);
        writeBoolean(value);
    }

    public final void writeNullField(String fieldName) throws IOException {
        writeFieldName(fieldName);
        writeNull();
    }

    public final void writeNumberField(String fieldName, int value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, long value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, double value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, float value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, BigDecimal value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeBinaryField(String fieldName, byte[] data) throws IOException {
        writeFieldName(fieldName);
        writeBinary(data);
    }

    public final void writeArrayFieldStart(String fieldName) throws IOException {
        writeFieldName(fieldName);
        writeStartArray();
    }

    public final void writeObjectFieldStart(String fieldName) throws IOException {
        writeFieldName(fieldName);
        writeStartObject();
    }

    public final void writeObjectField(String fieldName, Object pojo) throws IOException {
        writeFieldName(fieldName);
        writeObject(pojo);
    }
}
