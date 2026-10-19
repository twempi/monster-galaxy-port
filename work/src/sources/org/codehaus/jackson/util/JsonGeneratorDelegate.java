package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.io.SerializedString;

/* JADX INFO: loaded from: classes.dex */
public class JsonGeneratorDelegate extends JsonGenerator {
    protected JsonGenerator delegate;

    public JsonGeneratorDelegate(JsonGenerator d) {
        this.delegate = d;
    }

    @Override // org.codehaus.jackson.JsonGenerator, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void copyCurrentEvent(JsonParser jp) throws IOException {
        this.delegate.copyCurrentEvent(jp);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void copyCurrentStructure(JsonParser jp) throws IOException {
        this.delegate.copyCurrentStructure(jp);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator disable(JsonGenerator.Feature f) {
        return this.delegate.disable(f);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator enable(JsonGenerator.Feature f) {
        return this.delegate.enable(f);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void flush() throws IOException {
        this.delegate.flush();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public ObjectCodec getCodec() {
        return this.delegate.getCodec();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonStreamContext getOutputContext() {
        return this.delegate.getOutputContext();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void setSchema(FormatSchema schema) {
        this.delegate.setSchema(schema);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public boolean canUseSchema(FormatSchema schema) {
        return this.delegate.canUseSchema(schema);
    }

    @Override // org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.Versioned
    public Version version() {
        return this.delegate.version();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public Object getOutputTarget() {
        return this.delegate.getOutputTarget();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public boolean isEnabled(JsonGenerator.Feature f) {
        return this.delegate.isEnabled(f);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator setCodec(ObjectCodec oc) {
        this.delegate.setCodec(oc);
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator useDefaultPrettyPrinter() {
        this.delegate.useDefaultPrettyPrinter();
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
        this.delegate.writeBinary(b64variant, data, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeBoolean(boolean state) throws IOException {
        this.delegate.writeBoolean(state);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeEndArray() throws IOException {
        this.delegate.writeEndArray();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeEndObject() throws IOException {
        this.delegate.writeEndObject();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeFieldName(String name) throws IOException {
        this.delegate.writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeFieldName(SerializedString name) throws IOException {
        this.delegate.writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeFieldName(SerializableString name) throws IOException {
        this.delegate.writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNull() throws IOException {
        this.delegate.writeNull();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(int v) throws IOException {
        this.delegate.writeNumber(v);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(long v) throws IOException {
        this.delegate.writeNumber(v);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(BigInteger v) throws IOException {
        this.delegate.writeNumber(v);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(double v) throws IOException {
        this.delegate.writeNumber(v);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(float v) throws IOException {
        this.delegate.writeNumber(v);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(BigDecimal v) throws IOException {
        this.delegate.writeNumber(v);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(String encodedValue) throws UnsupportedOperationException, IOException {
        this.delegate.writeNumber(encodedValue);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeObject(Object pojo) throws IOException {
        this.delegate.writeObject(pojo);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(String text) throws IOException {
        this.delegate.writeRaw(text);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(String text, int offset, int len) throws IOException {
        this.delegate.writeRaw(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(char[] text, int offset, int len) throws IOException {
        this.delegate.writeRaw(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(char c) throws IOException {
        this.delegate.writeRaw(c);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawValue(String text) throws IOException {
        this.delegate.writeRawValue(text);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawValue(String text, int offset, int len) throws IOException {
        this.delegate.writeRawValue(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawValue(char[] text, int offset, int len) throws IOException {
        this.delegate.writeRawValue(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeStartArray() throws IOException {
        this.delegate.writeStartArray();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeStartObject() throws IOException {
        this.delegate.writeStartObject();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(String text) throws IOException {
        this.delegate.writeString(text);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(char[] text, int offset, int len) throws IOException {
        this.delegate.writeString(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(SerializableString text) throws IOException {
        this.delegate.writeString(text);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
        this.delegate.writeRawUTF8String(text, offset, length);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
        this.delegate.writeUTF8String(text, offset, length);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeTree(JsonNode rootNode) throws IOException {
        this.delegate.writeTree(rootNode);
    }
}
