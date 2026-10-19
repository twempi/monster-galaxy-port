package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.CharTypes;

/* JADX INFO: loaded from: classes.dex */
public class Utf8Generator extends JsonGeneratorBase {
    private static final byte BYTE_0 = 48;
    private static final byte BYTE_BACKSLASH = 92;
    private static final byte BYTE_COLON = 58;
    private static final byte BYTE_COMMA = 44;
    private static final byte BYTE_LBRACKET = 91;
    private static final byte BYTE_LCURLY = 123;
    private static final byte BYTE_QUOTE = 34;
    private static final byte BYTE_RBRACKET = 93;
    private static final byte BYTE_RCURLY = 125;
    private static final byte BYTE_SPACE = 32;
    private static final int MAX_BYTES_TO_BUFFER = 512;
    protected static final int SURR1_FIRST = 55296;
    protected static final int SURR1_LAST = 56319;
    protected static final int SURR2_FIRST = 56320;
    protected static final int SURR2_LAST = 57343;
    protected boolean _bufferRecyclable;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected CharacterEscapes _characterEscapes;
    protected byte[] _entityBuffer;
    protected final IOContext _ioContext;
    protected int _maximumNonEscapedChar;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected int[] _outputEscapes;
    protected final int _outputMaxContiguous;
    protected final OutputStream _outputStream;
    protected int _outputTail;
    static final byte[] HEX_CHARS = CharTypes.copyHexBytes();
    private static final byte BYTE_u = 117;
    private static final byte[] NULL_BYTES = {110, BYTE_u, 108, 108};
    private static final byte[] TRUE_BYTES = {116, 114, BYTE_u, 101};
    private static final byte[] FALSE_BYTES = {102, 97, 108, 115, 101};
    protected static final int[] sOutputEscapes = CharTypes.get7BitOutputEscapes();

    public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out) {
        super(features, codec);
        this._outputEscapes = sOutputEscapes;
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._outputStream = out;
        this._bufferRecyclable = true;
        this._outputBuffer = ctxt.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII)) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
    }

    public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable) {
        super(features, codec);
        this._outputEscapes = sOutputEscapes;
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._outputStream = out;
        this._bufferRecyclable = bufferRecyclable;
        this._outputTail = outputOffset;
        this._outputBuffer = outputBuffer;
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII)) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator setHighestNonEscapedChar(int charCode) {
        if (charCode < 0) {
            charCode = 0;
        }
        this._maximumNonEscapedChar = charCode;
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public int getHighestEscapedChar() {
        return this._maximumNonEscapedChar;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
        this._characterEscapes = esc;
        if (esc == null) {
            this._outputEscapes = sOutputEscapes;
        } else {
            this._outputEscapes = esc.getEscapeCodesForAscii();
        }
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public CharacterEscapes getCharacterEscapes() {
        return this._characterEscapes;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public Object getOutputTarget() {
        return this._outputStream;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeStringField(String fieldName, String value) throws IOException {
        writeFieldName(fieldName);
        writeString(value);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeFieldName(String name) throws IOException {
        int status = this._writeContext.writeFieldName(name);
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name, status == 1);
            return;
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        _writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeFieldName(SerializedString name) throws IOException {
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name, status == 1);
            return;
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        _writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeFieldName(SerializableString name) throws IOException {
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name, status == 1);
            return;
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        _writeFieldName(name);
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_LBRACKET;
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeEndArray() throws IOException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_RBRACKET;
        }
        this._writeContext = this._writeContext.getParent();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_LCURLY;
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeEndObject() throws IOException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_RCURLY;
    }

    protected final void _writeFieldName(String name) throws IOException {
        if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            _writeStringSegments(name);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        int len = name.length();
        if (len <= this._charBufferLength) {
            name.getChars(0, len, this._charBuffer, 0);
            if (len <= this._outputMaxContiguous) {
                if (this._outputTail + len > this._outputEnd) {
                    _flushBuffer();
                }
                _writeStringSegment(this._charBuffer, 0, len);
            } else {
                _writeStringSegments(this._charBuffer, 0, len);
            }
        } else {
            _writeStringSegments(name);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    protected final void _writeFieldName(SerializableString name) throws IOException {
        byte[] raw = name.asQuotedUTF8();
        if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            _writeBytes(raw);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        int len = raw.length;
        if (this._outputTail + len + 1 < this._outputEnd) {
            System.arraycopy(raw, 0, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
            byte[] bArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr2[i2] = 34;
            return;
        }
        _writeBytes(raw);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = 34;
    }

    protected final void _writePPFieldName(String name, boolean commaBefore) throws IOException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = 34;
            int len = name.length();
            if (len <= this._charBufferLength) {
                name.getChars(0, len, this._charBuffer, 0);
                if (len <= this._outputMaxContiguous) {
                    if (this._outputTail + len > this._outputEnd) {
                        _flushBuffer();
                    }
                    _writeStringSegment(this._charBuffer, 0, len);
                } else {
                    _writeStringSegments(this._charBuffer, 0, len);
                }
            } else {
                _writeStringSegments(name);
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr2[i2] = 34;
            return;
        }
        _writeStringSegments(name);
    }

    protected final void _writePPFieldName(SerializableString name, boolean commaBefore) throws IOException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        boolean addQuotes = isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = 34;
        }
        _writeBytes(name.asQuotedUTF8());
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr2[i2] = 34;
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(String text) throws IOException {
        _verifyValueWrite("write text value");
        if (text == null) {
            _writeNull();
            return;
        }
        int len = text.length();
        if (len > this._charBufferLength) {
            _writeLongString(text);
            return;
        }
        text.getChars(0, len, this._charBuffer, 0);
        if (len > this._outputMaxContiguous) {
            _writeLongString(this._charBuffer, 0, len);
            return;
        }
        if (this._outputTail + len + 2 > this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        _writeStringSegment(this._charBuffer, 0, len);
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    private final void _writeLongString(String text) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        _writeStringSegments(text);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    private final void _writeLongString(char[] text, int offset, int len) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        _writeStringSegments(this._charBuffer, 0, len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(char[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        if (len <= this._outputMaxContiguous) {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(text, offset, len);
        } else {
            _writeStringSegments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeString(SerializableString text) throws IOException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        _writeBytes(text.asQuotedUTF8());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        _writeBytes(text, offset, length);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeUTF8String(byte[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        if (len <= this._outputMaxContiguous) {
            _writeUTF8Segment(text, offset, len);
        } else {
            _writeUTF8Segments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(String text) throws IOException {
        int start = 0;
        int len = text.length();
        while (len > 0) {
            char[] buf = this._charBuffer;
            int blen = buf.length;
            int len2 = len < blen ? len : blen;
            text.getChars(start, start + len2, buf, 0);
            writeRaw(buf, 0, len2);
            start += len2;
            len -= len2;
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(String text, int offset, int len) throws IOException {
        while (len > 0) {
            char[] buf = this._charBuffer;
            int blen = buf.length;
            int len2 = len < blen ? len : blen;
            text.getChars(offset, offset + len2, buf, 0);
            writeRaw(buf, 0, len2);
            offset += len2;
            len -= len2;
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeRaw(char[] cbuf, int offset, int len) throws IOException {
        int len3 = len + len + len;
        if (this._outputTail + len3 > this._outputEnd) {
            if (this._outputEnd < len3) {
                _writeSegmentedRaw(cbuf, offset, len);
                return;
            }
            _flushBuffer();
        }
        int len2 = len + offset;
        while (offset < len2) {
            do {
                char c = cbuf[offset];
                if (c <= 127) {
                    byte[] bArr = this._outputBuffer;
                    int i = this._outputTail;
                    this._outputTail = i + 1;
                    bArr[i] = (byte) c;
                    offset++;
                } else {
                    int offset2 = offset + 1;
                    char ch = cbuf[offset];
                    if (ch < 2048) {
                        byte[] bArr2 = this._outputBuffer;
                        int i2 = this._outputTail;
                        this._outputTail = i2 + 1;
                        bArr2[i2] = (byte) ((ch >> 6) | 192);
                        byte[] bArr3 = this._outputBuffer;
                        int i3 = this._outputTail;
                        this._outputTail = i3 + 1;
                        bArr3[i3] = (byte) ((ch & '?') | 128);
                    } else {
                        _outputRawMultiByteChar(ch, cbuf, offset2, len2);
                    }
                    offset = offset2;
                }
            } while (offset < len2);
            return;
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(char ch) throws IOException {
        if (this._outputTail + 3 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        if (ch <= 127) {
            int i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ch;
        } else {
            if (ch < 2048) {
                int i2 = this._outputTail;
                this._outputTail = i2 + 1;
                bbuf[i2] = (byte) ((ch >> 6) | 192);
                int i3 = this._outputTail;
                this._outputTail = i3 + 1;
                bbuf[i3] = (byte) ((ch & '?') | 128);
                return;
            }
            _outputRawMultiByteChar(ch, null, 0, 0);
        }
    }

    private final void _writeSegmentedRaw(char[] cbuf, int offset, int len) throws IOException {
        int end = this._outputEnd;
        byte[] bbuf = this._outputBuffer;
        while (offset < len) {
            do {
                char c = cbuf[offset];
                if (c < 128) {
                    if (this._outputTail >= end) {
                        _flushBuffer();
                    }
                    int i = this._outputTail;
                    this._outputTail = i + 1;
                    bbuf[i] = (byte) c;
                    offset++;
                } else {
                    if (this._outputTail + 3 >= this._outputEnd) {
                        _flushBuffer();
                    }
                    int offset2 = offset + 1;
                    char ch = cbuf[offset];
                    if (ch < 2048) {
                        int i2 = this._outputTail;
                        this._outputTail = i2 + 1;
                        bbuf[i2] = (byte) ((ch >> 6) | 192);
                        int i3 = this._outputTail;
                        this._outputTail = i3 + 1;
                        bbuf[i3] = (byte) ((ch & '?') | 128);
                    } else {
                        _outputRawMultiByteChar(ch, cbuf, offset2, len);
                    }
                    offset = offset2;
                }
            } while (offset < len);
            return;
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
        _verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        _writeBinary(b64variant, data, offset, offset + len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(int i) throws IOException {
        _verifyValueWrite("write number");
        if (this._outputTail + 11 >= this._outputEnd) {
            _flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            _writeQuotedInt(i);
        } else {
            this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        }
    }

    private final void _writeQuotedInt(int i) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = 34;
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        byte[] bArr2 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr2[i3] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(long l) throws IOException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedLong(l);
            return;
        }
        if (this._outputTail + 21 >= this._outputEnd) {
            _flushBuffer();
        }
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
    }

    private final void _writeQuotedLong(long l) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(BigInteger value) throws IOException {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            writeRaw(value.toString());
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(double d) throws IOException {
        if (this._cfgNumbersAsStrings || ((Double.isNaN(d) || Double.isInfinite(d)) && isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(d));
        } else {
            _verifyValueWrite("write number");
            writeRaw(String.valueOf(d));
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(float f) throws IOException {
        if (this._cfgNumbersAsStrings || ((Float.isNaN(f) || Float.isInfinite(f)) && isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(f));
        } else {
            _verifyValueWrite("write number");
            writeRaw(String.valueOf(f));
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(BigDecimal value) throws IOException {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            writeRaw(value.toString());
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(String encodedValue) throws IOException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(encodedValue);
        } else {
            writeRaw(encodedValue);
        }
    }

    private final void _writeQuotedRaw(Object value) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 34;
        writeRaw(value.toString());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = 34;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeBoolean(boolean state) throws IOException {
        _verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
        int len = keyword.length;
        System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNull() throws IOException {
        _verifyValueWrite("write null value");
        _writeNull();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase
    protected final void _verifyValueWrite(String typeMsg) throws IOException {
        byte b;
        int status = this._writeContext.writeValue();
        if (status == 5) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            switch (status) {
                case 1:
                    b = BYTE_COMMA;
                    break;
                case 2:
                    b = 58;
                    break;
                case 3:
                    b = 32;
                    break;
                default:
                    return;
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputBuffer[this._outputTail] = b;
            this._outputTail++;
            return;
        }
        _verifyPrettyValueWrite(typeMsg, status);
    }

    protected final void _verifyPrettyValueWrite(String typeMsg, int status) throws IOException {
        switch (status) {
            case 0:
                if (this._writeContext.inArray()) {
                    this._cfgPrettyPrinter.beforeArrayValues(this);
                } else if (this._writeContext.inObject()) {
                    this._cfgPrettyPrinter.beforeObjectEntries(this);
                }
                break;
            case 1:
                this._cfgPrettyPrinter.writeArrayValueSeparator(this);
                break;
            case 2:
                this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
                break;
            case 3:
                this._cfgPrettyPrinter.writeRootValueSeparator(this);
                break;
            default:
                _cantHappen();
                break;
        }
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void flush() throws IOException {
        _flushBuffer();
        if (this._outputStream != null && isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
            this._outputStream.flush();
        }
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        if (this._outputBuffer != null && isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                JsonStreamContext ctxt = getOutputContext();
                if (ctxt.inArray()) {
                    writeEndArray();
                } else if (!ctxt.inObject()) {
                    break;
                } else {
                    writeEndObject();
                }
            }
        }
        _flushBuffer();
        if (this._ioContext.isResourceManaged() || isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {
            this._outputStream.close();
        } else {
            this._outputStream.flush();
        }
        _releaseBuffers();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase
    protected void _releaseBuffers() {
        byte[] buf = this._outputBuffer;
        if (buf != null && this._bufferRecyclable) {
            this._outputBuffer = null;
            this._ioContext.releaseWriteEncodingBuffer(buf);
        }
        char[] cbuf = this._charBuffer;
        if (cbuf != null) {
            this._charBuffer = null;
            this._ioContext.releaseConcatBuffer(cbuf);
        }
    }

    private final void _writeBytes(byte[] bytes) throws IOException {
        int len = bytes.length;
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > 512) {
                this._outputStream.write(bytes, 0, len);
                return;
            }
        }
        System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException {
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > 512) {
                this._outputStream.write(bytes, offset, len);
                return;
            }
        }
        System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeStringSegments(String text) throws IOException {
        int left = text.length();
        int offset = 0;
        char[] cbuf = this._charBuffer;
        while (left > 0) {
            int len = Math.min(this._outputMaxContiguous, left);
            text.getChars(offset, offset + len, cbuf, 0);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, 0, len);
            offset += len;
            left -= len;
        }
    }

    private final void _writeStringSegments(char[] cbuf, int offset, int totalLen) throws IOException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    private final void _writeStringSegment(char[] cbuf, int offset, int len) throws IOException {
        int len2 = len + offset;
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int outputPtr2 = outputPtr;
        while (offset < len2) {
            char c = cbuf[offset];
            if (c > 127 || escCodes[c] != 0) {
                break;
            }
            outputBuffer[outputPtr2] = (byte) c;
            offset++;
            outputPtr2++;
        }
        this._outputTail = outputPtr2;
        if (offset < len2) {
            if (this._characterEscapes != null) {
                _writeCustomStringSegment2(cbuf, offset, len2);
            } else if (this._maximumNonEscapedChar == 0) {
                _writeStringSegment2(cbuf, offset, len2);
            } else {
                _writeStringSegmentASCII2(cbuf, offset, len2);
            }
        }
    }

    private final void _writeStringSegment2(char[] cbuf, int offset, int end) throws IOException {
        int outputPtr;
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr2 = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int outputPtr3 = outputPtr2;
        int offset2 = offset;
        while (offset2 < end) {
            offset2++;
            char c = cbuf[offset2];
            if (c <= 127) {
                if (escCodes[c] == 0) {
                    outputBuffer[outputPtr3] = (byte) c;
                    outputPtr3++;
                } else {
                    int escape = escCodes[c];
                    if (escape > 0) {
                        int outputPtr4 = outputPtr3 + 1;
                        outputBuffer[outputPtr3] = BYTE_BACKSLASH;
                        outputPtr3 = outputPtr4 + 1;
                        outputBuffer[outputPtr4] = (byte) escape;
                    } else {
                        outputPtr3 = _writeGenericEscape(c, outputPtr3);
                    }
                }
            } else {
                if (c <= 2047) {
                    int outputPtr5 = outputPtr3 + 1;
                    outputBuffer[outputPtr3] = (byte) ((c >> 6) | 192);
                    outputBuffer[outputPtr5] = (byte) ((c & '?') | 128);
                    outputPtr = outputPtr5 + 1;
                } else {
                    outputPtr = _outputMultiByteChar(c, outputPtr3);
                }
                outputPtr3 = outputPtr;
            }
        }
        this._outputTail = outputPtr3;
    }

    private final void _writeStringSegmentASCII2(char[] cbuf, int offset, int end) throws IOException {
        int outputPtr;
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr2 = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar;
        int outputPtr3 = outputPtr2;
        int offset2 = offset;
        while (offset2 < end) {
            offset2++;
            char c = cbuf[offset2];
            if (c <= 127) {
                if (escCodes[c] == 0) {
                    outputBuffer[outputPtr3] = (byte) c;
                    outputPtr3++;
                } else {
                    int escape = escCodes[c];
                    if (escape > 0) {
                        int outputPtr4 = outputPtr3 + 1;
                        outputBuffer[outputPtr3] = BYTE_BACKSLASH;
                        outputPtr3 = outputPtr4 + 1;
                        outputBuffer[outputPtr4] = (byte) escape;
                    } else {
                        outputPtr3 = _writeGenericEscape(c, outputPtr3);
                    }
                }
            } else if (c > maxUnescaped) {
                outputPtr3 = _writeGenericEscape(c, outputPtr3);
            } else {
                if (c <= 2047) {
                    int outputPtr5 = outputPtr3 + 1;
                    outputBuffer[outputPtr3] = (byte) ((c >> 6) | 192);
                    outputBuffer[outputPtr5] = (byte) ((c & '?') | 128);
                    outputPtr = outputPtr5 + 1;
                } else {
                    outputPtr = _outputMultiByteChar(c, outputPtr3);
                }
                outputPtr3 = outputPtr;
            }
        }
        this._outputTail = outputPtr3;
    }

    private final void _writeCustomStringSegment2(char[] cbuf, int offset, int end) throws IOException {
        int outputPtr;
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr2 = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar <= 0 ? 65535 : this._maximumNonEscapedChar;
        CharacterEscapes customEscapes = this._characterEscapes;
        int outputPtr3 = outputPtr2;
        while (true) {
            int offset2 = offset;
            if (offset2 < end) {
                offset = offset2 + 1;
                char c = cbuf[offset2];
                if (c <= 127) {
                    if (escCodes[c] == 0) {
                        outputBuffer[outputPtr3] = (byte) c;
                        outputPtr3++;
                    } else {
                        int escape = escCodes[c];
                        if (escape > 0) {
                            int outputPtr4 = outputPtr3 + 1;
                            outputBuffer[outputPtr3] = BYTE_BACKSLASH;
                            outputPtr3 = outputPtr4 + 1;
                            outputBuffer[outputPtr4] = (byte) escape;
                        } else if (escape == -2) {
                            SerializableString esc = customEscapes.getEscapeSequence(c);
                            if (esc == null) {
                                throw new JsonGenerationException("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(c) + ", although was supposed to have one");
                            }
                            outputPtr3 = _writeCustomEscape(outputBuffer, outputPtr3, esc, end - offset);
                        } else {
                            outputPtr3 = _writeGenericEscape(c, outputPtr3);
                        }
                    }
                } else if (c > maxUnescaped) {
                    outputPtr3 = _writeGenericEscape(c, outputPtr3);
                } else {
                    SerializableString esc2 = customEscapes.getEscapeSequence(c);
                    if (esc2 != null) {
                        outputPtr3 = _writeCustomEscape(outputBuffer, outputPtr3, esc2, end - offset);
                    } else {
                        if (c <= 2047) {
                            int outputPtr5 = outputPtr3 + 1;
                            outputBuffer[outputPtr3] = (byte) ((c >> 6) | 192);
                            outputBuffer[outputPtr5] = (byte) ((c & '?') | 128);
                            outputPtr = outputPtr5 + 1;
                        } else {
                            outputPtr = _outputMultiByteChar(c, outputPtr3);
                        }
                        outputPtr3 = outputPtr;
                    }
                }
            } else {
                this._outputTail = outputPtr3;
                return;
            }
        }
    }

    private int _writeCustomEscape(byte[] outputBuffer, int outputPtr, SerializableString esc, int remainingChars) throws IOException {
        byte[] raw = esc.asUnquotedUTF8();
        int len = raw.length;
        if (len > 6) {
            return _handleLongCustomEscape(outputBuffer, outputPtr, this._outputEnd, raw, remainingChars);
        }
        System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
        return outputPtr + len;
    }

    private int _handleLongCustomEscape(byte[] outputBuffer, int outputPtr, int outputEnd, byte[] raw, int remainingChars) throws IOException {
        int len = raw.length;
        if (outputPtr + len > outputEnd) {
            this._outputTail = outputPtr;
            _flushBuffer();
            int outputPtr2 = this._outputTail;
            if (len > outputBuffer.length) {
                this._outputStream.write(raw, 0, len);
                return outputPtr2;
            }
            System.arraycopy(raw, 0, outputBuffer, outputPtr2, len);
            outputPtr = outputPtr2 + len;
        }
        if ((remainingChars * 6) + outputPtr <= outputEnd) {
            return outputPtr;
        }
        _flushBuffer();
        return this._outputTail;
    }

    private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen) throws IOException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            _writeUTF8Segment(utf8, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    private final void _writeUTF8Segment(byte[] utf8, int offset, int len) throws IOException {
        int[] escCodes = this._outputEscapes;
        int end = offset + len;
        int ptr = offset;
        while (ptr < end) {
            int ptr2 = ptr + 1;
            int ch = utf8[ptr];
            if (ch >= 0 && escCodes[ch] != 0) {
                _writeUTF8Segment2(utf8, offset, len);
                return;
            }
            ptr = ptr2;
        }
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeUTF8Segment2(byte[] utf8, int offset, int len) throws IOException {
        int outputPtr;
        int outputPtr2 = this._outputTail;
        if ((len * 6) + outputPtr2 > this._outputEnd) {
            _flushBuffer();
            outputPtr2 = this._outputTail;
        }
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int len2 = len + offset;
        int outputPtr3 = outputPtr2;
        int offset2 = offset;
        while (offset2 < len2) {
            offset2++;
            byte b = utf8[offset2];
            if (b < 0 || escCodes[b] == 0) {
                outputBuffer[outputPtr3] = b;
                outputPtr3++;
            } else {
                int escape = escCodes[b];
                if (escape > 0) {
                    int outputPtr4 = outputPtr3 + 1;
                    outputBuffer[outputPtr3] = BYTE_BACKSLASH;
                    outputBuffer[outputPtr4] = (byte) escape;
                    outputPtr = outputPtr4 + 1;
                } else {
                    outputPtr = _writeGenericEscape(b, outputPtr3);
                }
                outputPtr3 = outputPtr;
            }
        }
        this._outputTail = outputPtr3;
    }

    protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd) throws IOException {
        int safeInputEnd = inputEnd - 3;
        int safeOutputEnd = this._outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
        int inputPtr2 = inputPtr;
        while (inputPtr2 <= safeInputEnd) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            int inputPtr3 = inputPtr2 + 1;
            int b24 = input[inputPtr2] << 8;
            int inputPtr4 = inputPtr3 + 1;
            int i = (b24 | (input[inputPtr3] & 255)) << 8;
            int inputPtr5 = inputPtr4 + 1;
            this._outputTail = b64variant.encodeBase64Chunk(i | (input[inputPtr4] & 255), this._outputBuffer, this._outputTail);
            chunksBeforeLF--;
            if (chunksBeforeLF <= 0) {
                byte[] bArr = this._outputBuffer;
                int i2 = this._outputTail;
                this._outputTail = i2 + 1;
                bArr[i2] = BYTE_BACKSLASH;
                byte[] bArr2 = this._outputBuffer;
                int i3 = this._outputTail;
                this._outputTail = i3 + 1;
                bArr2[i3] = 110;
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
            inputPtr2 = inputPtr5;
        }
        int inputLeft = inputEnd - inputPtr2;
        if (inputLeft > 0) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            int inputPtr6 = inputPtr2 + 1;
            int b25 = input[inputPtr2] << 16;
            if (inputLeft == 2) {
                int i4 = inputPtr6 + 1;
                b25 |= (input[inputPtr6] & 255) << 8;
            }
            this._outputTail = b64variant.encodeBase64Partial(b25, inputLeft, this._outputBuffer, this._outputTail);
        }
    }

    private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputLen) throws IOException {
        if (ch >= SURR1_FIRST && ch <= SURR2_LAST) {
            if (inputOffset >= inputLen) {
                _reportError("Split surrogate on writeRaw() input (last character)");
            }
            _outputSurrogates(ch, cbuf[inputOffset]);
            return inputOffset + 1;
        }
        byte[] bbuf = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((ch >> 12) | 224);
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bbuf[i2] = (byte) (((ch >> 6) & 63) | 128);
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bbuf[i3] = (byte) ((ch & 63) | 128);
        return inputOffset;
    }

    protected final void _outputSurrogates(int surr1, int surr2) throws IOException {
        int c = _decodeSurrogate(surr1, surr2);
        if (this._outputTail + 4 > this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c >> 18) | 240);
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bbuf[i2] = (byte) (((c >> 12) & 63) | 128);
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bbuf[i3] = (byte) (((c >> 6) & 63) | 128);
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        bbuf[i4] = (byte) ((c & 63) | 128);
    }

    private final int _outputMultiByteChar(int ch, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        if (ch >= SURR1_FIRST && ch <= SURR2_LAST) {
            int outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = BYTE_BACKSLASH;
            int outputPtr3 = outputPtr2 + 1;
            bbuf[outputPtr2] = BYTE_u;
            int outputPtr4 = outputPtr3 + 1;
            bbuf[outputPtr3] = HEX_CHARS[(ch >> 12) & 15];
            int outputPtr5 = outputPtr4 + 1;
            bbuf[outputPtr4] = HEX_CHARS[(ch >> 8) & 15];
            int outputPtr6 = outputPtr5 + 1;
            bbuf[outputPtr5] = HEX_CHARS[(ch >> 4) & 15];
            int outputPtr7 = outputPtr6 + 1;
            bbuf[outputPtr6] = HEX_CHARS[ch & 15];
            return outputPtr7;
        }
        int outputPtr8 = outputPtr + 1;
        bbuf[outputPtr] = (byte) ((ch >> 12) | 224);
        int outputPtr9 = outputPtr8 + 1;
        bbuf[outputPtr8] = (byte) (((ch >> 6) & 63) | 128);
        int outputPtr10 = outputPtr9 + 1;
        bbuf[outputPtr9] = (byte) ((ch & 63) | 128);
        return outputPtr10;
    }

    protected final int _decodeSurrogate(int surr1, int surr2) throws IOException {
        if (surr2 < SURR2_FIRST || surr2 > SURR2_LAST) {
            String msg = "Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2);
            _reportError(msg);
        }
        int c = 65536 + ((surr1 - SURR1_FIRST) << 10) + (surr2 - SURR2_FIRST);
        return c;
    }

    private final void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
        this._outputTail += 4;
    }

    private int _writeGenericEscape(int charToEscape, int outputPtr) throws IOException {
        int outputPtr2;
        byte[] bbuf = this._outputBuffer;
        int outputPtr3 = outputPtr + 1;
        bbuf[outputPtr] = BYTE_BACKSLASH;
        int outputPtr4 = outputPtr3 + 1;
        bbuf[outputPtr3] = BYTE_u;
        if (charToEscape > 255) {
            int hi = (charToEscape >> 8) & 255;
            int outputPtr5 = outputPtr4 + 1;
            bbuf[outputPtr4] = HEX_CHARS[hi >> 4];
            outputPtr2 = outputPtr5 + 1;
            bbuf[outputPtr5] = HEX_CHARS[hi & 15];
            charToEscape &= 255;
        } else {
            int outputPtr6 = outputPtr4 + 1;
            bbuf[outputPtr4] = BYTE_0;
            outputPtr2 = outputPtr6 + 1;
            bbuf[outputPtr6] = BYTE_0;
        }
        int outputPtr7 = outputPtr2 + 1;
        bbuf[outputPtr2] = HEX_CHARS[charToEscape >> 4];
        int outputPtr8 = outputPtr7 + 1;
        bbuf[outputPtr7] = HEX_CHARS[charToEscape & 15];
        return outputPtr8;
    }

    protected final void _flushBuffer() throws IOException {
        int len = this._outputTail;
        if (len > 0) {
            this._outputTail = 0;
            this._outputStream.write(this._outputBuffer, 0, len);
        }
    }
}
