package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;

/* JADX INFO: loaded from: classes.dex */
public class JsonParserDelegate extends JsonParser {
    protected JsonParser delegate;

    public JsonParserDelegate(JsonParser d) {
        this.delegate = d;
    }

    @Override // org.codehaus.jackson.JsonParser
    public void setCodec(ObjectCodec c) {
        this.delegate.setCodec(c);
    }

    @Override // org.codehaus.jackson.JsonParser
    public ObjectCodec getCodec() {
        return this.delegate.getCodec();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonParser enable(JsonParser.Feature f) {
        this.delegate.enable(f);
        return this;
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonParser disable(JsonParser.Feature f) {
        this.delegate.disable(f);
        return this;
    }

    @Override // org.codehaus.jackson.JsonParser
    public boolean isEnabled(JsonParser.Feature f) {
        return this.delegate.isEnabled(f);
    }

    @Override // org.codehaus.jackson.JsonParser
    public void setSchema(FormatSchema schema) {
        this.delegate.setSchema(schema);
    }

    @Override // org.codehaus.jackson.JsonParser
    public boolean canUseSchema(FormatSchema schema) {
        return this.delegate.canUseSchema(schema);
    }

    @Override // org.codehaus.jackson.JsonParser, org.codehaus.jackson.Versioned
    public Version version() {
        return this.delegate.version();
    }

    @Override // org.codehaus.jackson.JsonParser
    public Object getInputSource() {
        return this.delegate.getInputSource();
    }

    @Override // org.codehaus.jackson.JsonParser, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override // org.codehaus.jackson.JsonParser
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonToken getCurrentToken() {
        return this.delegate.getCurrentToken();
    }

    @Override // org.codehaus.jackson.JsonParser
    public boolean hasCurrentToken() {
        return this.delegate.hasCurrentToken();
    }

    @Override // org.codehaus.jackson.JsonParser
    public void clearCurrentToken() {
        this.delegate.clearCurrentToken();
    }

    @Override // org.codehaus.jackson.JsonParser
    public String getCurrentName() throws IOException {
        return this.delegate.getCurrentName();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonLocation getCurrentLocation() {
        return this.delegate.getCurrentLocation();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonToken getLastClearedToken() {
        return this.delegate.getLastClearedToken();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonStreamContext getParsingContext() {
        return this.delegate.getParsingContext();
    }

    @Override // org.codehaus.jackson.JsonParser
    public String getText() throws IOException {
        return this.delegate.getText();
    }

    @Override // org.codehaus.jackson.JsonParser
    public char[] getTextCharacters() throws IOException {
        return this.delegate.getTextCharacters();
    }

    @Override // org.codehaus.jackson.JsonParser
    public int getTextLength() throws IOException {
        return this.delegate.getTextLength();
    }

    @Override // org.codehaus.jackson.JsonParser
    public int getTextOffset() throws IOException {
        return this.delegate.getTextOffset();
    }

    @Override // org.codehaus.jackson.JsonParser
    public BigInteger getBigIntegerValue() throws IOException {
        return this.delegate.getBigIntegerValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public byte getByteValue() throws IOException {
        return this.delegate.getByteValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public short getShortValue() throws IOException {
        return this.delegate.getShortValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public BigDecimal getDecimalValue() throws IOException {
        return this.delegate.getDecimalValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public double getDoubleValue() throws IOException {
        return this.delegate.getDoubleValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public float getFloatValue() throws IOException {
        return this.delegate.getFloatValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public int getIntValue() throws IOException {
        return this.delegate.getIntValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public long getLongValue() throws IOException {
        return this.delegate.getLongValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonParser.NumberType getNumberType() throws IOException {
        return this.delegate.getNumberType();
    }

    @Override // org.codehaus.jackson.JsonParser
    public Number getNumberValue() throws IOException {
        return this.delegate.getNumberValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        return this.delegate.getBinaryValue(b64variant);
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonLocation getTokenLocation() {
        return this.delegate.getTokenLocation();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonToken nextToken() throws IOException {
        return this.delegate.nextToken();
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonParser skipChildren() throws IOException {
        this.delegate.skipChildren();
        return this;
    }
}
