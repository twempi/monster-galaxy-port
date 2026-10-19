package org.codehaus.jackson.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.VersionUtil;

/* JADX INFO: loaded from: classes.dex */
public abstract class JsonGeneratorBase extends JsonGenerator {
    protected boolean _closed;
    protected int _features;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext = JsonWriteContext.createRootContext();
    protected boolean _cfgNumbersAsStrings = isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);

    protected abstract void _releaseBuffers();

    protected abstract void _verifyValueWrite(String str) throws IOException;

    @Override // org.codehaus.jackson.JsonGenerator
    public abstract void flush() throws IOException;

    protected JsonGeneratorBase(int features, ObjectCodec codec) {
        this._features = features;
        this._objectCodec = codec;
    }

    @Override // org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.Versioned
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator enable(JsonGenerator.Feature f) {
        this._features |= f.getMask();
        if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
            this._cfgNumbersAsStrings = true;
        } else if (f == JsonGenerator.Feature.ESCAPE_NON_ASCII) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator disable(JsonGenerator.Feature f) {
        this._features &= f.getMask() ^ (-1);
        if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
            this._cfgNumbersAsStrings = false;
        } else if (f == JsonGenerator.Feature.ESCAPE_NON_ASCII) {
            setHighestNonEscapedChar(0);
        }
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final boolean isEnabled(JsonGenerator.Feature f) {
        return (this._features & f.getMask()) != 0;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator useDefaultPrettyPrinter() {
        return setPrettyPrinter(new org.codehaus.jackson.util.DefaultPrettyPrinter());
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final ObjectCodec getCodec() {
        return this._objectCodec;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
        } else {
            _writeStartArray();
        }
    }

    @Deprecated
    protected void _writeStartArray() throws IOException {
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeEndArray() throws IOException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            _writeEndArray();
        }
        this._writeContext = this._writeContext.getParent();
    }

    @Deprecated
    protected void _writeEndArray() throws IOException {
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
        } else {
            _writeStartObject();
        }
    }

    @Deprecated
    protected void _writeStartObject() throws IOException {
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeEndObject() throws IOException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            _writeEndObject();
        }
    }

    @Deprecated
    protected void _writeEndObject() throws IOException {
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawValue(String text) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawValue(String text, int offset, int len) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawValue(char[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeObject(Object value) throws IOException {
        if (value == null) {
            writeNull();
        } else if (this._objectCodec != null) {
            this._objectCodec.writeValue(this, value);
        } else {
            _writeSimpleObject(value);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeTree(JsonNode rootNode) throws IOException {
        if (rootNode == null) {
            writeNull();
        } else {
            if (this._objectCodec == null) {
                throw new IllegalStateException("No ObjectCodec defined for the generator, can not serialize JsonNode-based trees");
            }
            this._objectCodec.writeTree(this, rootNode);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this._closed = true;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public boolean isClosed() {
        return this._closed;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void copyCurrentEvent(JsonParser jp) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            _reportError("No current event to copy");
        }
        switch (t) {
            case START_OBJECT:
                writeStartObject();
                break;
            case END_OBJECT:
                writeEndObject();
                break;
            case START_ARRAY:
                writeStartArray();
                break;
            case END_ARRAY:
                writeEndArray();
                break;
            case FIELD_NAME:
                writeFieldName(jp.getCurrentName());
                break;
            case VALUE_STRING:
                if (jp.hasTextCharacters()) {
                    writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
                } else {
                    writeString(jp.getText());
                }
                break;
            case VALUE_NUMBER_INT:
                switch (jp.getNumberType()) {
                    case INT:
                        writeNumber(jp.getIntValue());
                        break;
                    case BIG_INTEGER:
                        writeNumber(jp.getBigIntegerValue());
                        break;
                    default:
                        writeNumber(jp.getLongValue());
                        break;
                }
                break;
            case VALUE_NUMBER_FLOAT:
                switch (jp.getNumberType()) {
                    case BIG_DECIMAL:
                        writeNumber(jp.getDecimalValue());
                        break;
                    case FLOAT:
                        writeNumber(jp.getFloatValue());
                        break;
                    default:
                        writeNumber(jp.getDoubleValue());
                        break;
                }
                break;
            case VALUE_TRUE:
                writeBoolean(true);
                break;
            case VALUE_FALSE:
                writeBoolean(false);
                break;
            case VALUE_NULL:
                writeNull();
                break;
            case VALUE_EMBEDDED_OBJECT:
                writeObject(jp.getEmbeddedObject());
                break;
            default:
                _cantHappen();
                break;
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void copyCurrentStructure(JsonParser jp) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.FIELD_NAME) {
            writeFieldName(jp.getCurrentName());
            t = jp.nextToken();
        }
        switch (t) {
            case START_OBJECT:
                writeStartObject();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    copyCurrentStructure(jp);
                }
                writeEndObject();
                break;
            case END_OBJECT:
            default:
                copyCurrentEvent(jp);
                break;
            case START_ARRAY:
                writeStartArray();
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    copyCurrentStructure(jp);
                }
                writeEndArray();
                break;
        }
    }

    protected void _reportError(String msg) throws JsonGenerationException {
        throw new JsonGenerationException(msg);
    }

    protected void _cantHappen() {
        throw new RuntimeException("Internal error: should never end up through this code path");
    }

    protected void _writeSimpleObject(Object value) throws IOException {
        if (value == null) {
            writeNull();
            return;
        }
        if (value instanceof String) {
            writeString((String) value);
            return;
        }
        if (value instanceof Number) {
            Number n = (Number) value;
            if (n instanceof Integer) {
                writeNumber(n.intValue());
                return;
            }
            if (n instanceof Long) {
                writeNumber(n.longValue());
                return;
            }
            if (n instanceof Double) {
                writeNumber(n.doubleValue());
                return;
            }
            if (n instanceof Float) {
                writeNumber(n.floatValue());
                return;
            }
            if (n instanceof Short) {
                writeNumber((int) n.shortValue());
                return;
            }
            if (n instanceof Byte) {
                writeNumber((int) n.byteValue());
                return;
            }
            if (n instanceof BigInteger) {
                writeNumber((BigInteger) n);
                return;
            }
            if (n instanceof BigDecimal) {
                writeNumber((BigDecimal) n);
                return;
            } else if (n instanceof AtomicInteger) {
                writeNumber(((AtomicInteger) n).get());
                return;
            } else if (n instanceof AtomicLong) {
                writeNumber(((AtomicLong) n).get());
                return;
            }
        } else if (value instanceof byte[]) {
            writeBinary((byte[]) value);
            return;
        } else if (value instanceof Boolean) {
            writeBoolean(((Boolean) value).booleanValue());
            return;
        } else if (value instanceof AtomicBoolean) {
            writeBoolean(((AtomicBoolean) value).get());
            return;
        }
        throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + value.getClass().getName() + ")");
    }

    protected final void _throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }

    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Operation not supported by generator of type " + getClass().getName());
    }
}
