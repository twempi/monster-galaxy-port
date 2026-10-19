package org.codehaus.jackson.smile;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.impl.JsonGeneratorBase;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class SmileGenerator extends JsonGeneratorBase {
    protected static final long MAX_INT_AS_LONG = 2147483647L;
    private static final int MIN_BUFFER_LENGTH = 770;
    protected static final long MIN_INT_AS_LONG = -2147483648L;
    protected static final int SURR1_FIRST = 55296;
    protected static final int SURR1_LAST = 56319;
    protected static final int SURR2_FIRST = 56320;
    protected static final int SURR2_LAST = 57343;
    protected static final byte TOKEN_BYTE_BIG_DECIMAL = 42;
    protected static final byte TOKEN_BYTE_BIG_INTEGER = 38;
    protected static final byte TOKEN_BYTE_FLOAT_32 = 40;
    protected static final byte TOKEN_BYTE_FLOAT_64 = 41;
    protected static final byte TOKEN_BYTE_INT_32 = 36;
    protected static final byte TOKEN_BYTE_INT_64 = 37;
    protected static final byte TOKEN_BYTE_LONG_STRING_ASCII = -32;
    protected static final byte TOKEN_BYTE_LONG_STRING_UNICODE = -28;
    protected static final ThreadLocal<SoftReference<SmileBufferRecycler<SharedStringNode>>> _smileRecyclerRef = new ThreadLocal<>();
    protected boolean _bufferRecyclable;
    protected int _bytesWritten;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected final IOContext _ioContext;
    protected final OutputStream _out;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected int _outputTail;
    protected int _seenNameCount;
    protected SharedStringNode[] _seenNames;
    protected int _seenStringValueCount;
    protected SharedStringNode[] _seenStringValues;
    protected final SmileBufferRecycler<SharedStringNode> _smileBufferRecycler;
    protected int _smileFeatures;

    public enum Feature {
        WRITE_HEADER(true),
        WRITE_END_MARKER(false),
        ENCODE_BINARY_AS_7BIT(true),
        CHECK_SHARED_NAMES(true),
        CHECK_SHARED_STRING_VALUES(false);

        protected final boolean _defaultState;
        protected final int _mask = 1 << ordinal();

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

    protected static final class SharedStringNode {
        public final int index;
        public SharedStringNode next;
        public final String value;

        public SharedStringNode(String value, int index, SharedStringNode next) {
            this.value = value;
            this.index = index;
            this.next = next;
        }
    }

    public SmileGenerator(IOContext ctxt, int jsonFeatures, int smileFeatures, ObjectCodec codec, OutputStream out) {
        super(jsonFeatures, codec);
        this._outputTail = 0;
        this._smileFeatures = smileFeatures;
        this._ioContext = ctxt;
        this._smileBufferRecycler = _smileBufferRecycler();
        this._out = out;
        this._bufferRecyclable = true;
        this._outputBuffer = ctxt.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (this._outputEnd < 770) {
            throw new IllegalStateException("Internal encoding buffer length (" + this._outputEnd + ") too short, must be at least 770");
        }
        if ((Feature.CHECK_SHARED_NAMES.getMask() & smileFeatures) == 0) {
            this._seenNames = null;
            this._seenNameCount = -1;
        } else {
            this._seenNames = this._smileBufferRecycler.allocSeenNamesBuffer();
            if (this._seenNames == null) {
                this._seenNames = new SharedStringNode[64];
            }
            this._seenNameCount = 0;
        }
        if ((Feature.CHECK_SHARED_STRING_VALUES.getMask() & smileFeatures) == 0) {
            this._seenStringValues = null;
            this._seenStringValueCount = -1;
        } else {
            this._seenStringValues = this._smileBufferRecycler.allocSeenStringValuesBuffer();
            if (this._seenStringValues == null) {
                this._seenStringValues = new SharedStringNode[64];
            }
            this._seenStringValueCount = 0;
        }
    }

    public SmileGenerator(IOContext ctxt, int jsonFeatures, int smileFeatures, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int offset, boolean bufferRecyclable) {
        super(jsonFeatures, codec);
        this._outputTail = 0;
        this._smileFeatures = smileFeatures;
        this._ioContext = ctxt;
        this._smileBufferRecycler = _smileBufferRecycler();
        this._out = out;
        this._bufferRecyclable = bufferRecyclable;
        this._outputTail = offset;
        this._outputBuffer = outputBuffer;
        this._outputEnd = this._outputBuffer.length;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (this._outputEnd < 770) {
            throw new IllegalStateException("Internal encoding buffer length (" + this._outputEnd + ") too short, must be at least 770");
        }
        if ((Feature.CHECK_SHARED_NAMES.getMask() & smileFeatures) == 0) {
            this._seenNames = null;
            this._seenNameCount = -1;
        } else {
            this._seenNames = this._smileBufferRecycler.allocSeenNamesBuffer();
            if (this._seenNames == null) {
                this._seenNames = new SharedStringNode[64];
            }
            this._seenNameCount = 0;
        }
        if ((Feature.CHECK_SHARED_STRING_VALUES.getMask() & smileFeatures) == 0) {
            this._seenStringValues = null;
            this._seenStringValueCount = -1;
        } else {
            this._seenStringValues = this._smileBufferRecycler.allocSeenStringValuesBuffer();
            if (this._seenStringValues == null) {
                this._seenStringValues = new SharedStringNode[64];
            }
            this._seenStringValueCount = 0;
        }
    }

    public void writeHeader() throws IOException {
        int last = 0;
        if ((this._smileFeatures & Feature.CHECK_SHARED_NAMES.getMask()) != 0) {
            last = 0 | 1;
        }
        if ((this._smileFeatures & Feature.CHECK_SHARED_STRING_VALUES.getMask()) != 0) {
            last |= 2;
        }
        if ((this._smileFeatures & Feature.ENCODE_BINARY_AS_7BIT.getMask()) == 0) {
            last |= 4;
        }
        _writeBytes(SmileConstants.HEADER_BYTE_1, (byte) 41, (byte) 10, (byte) last);
    }

    protected static final SmileBufferRecycler<SharedStringNode> _smileBufferRecycler() {
        SoftReference<SmileBufferRecycler<SharedStringNode>> ref = _smileRecyclerRef.get();
        SmileBufferRecycler<SharedStringNode> br = ref == null ? null : ref.get();
        if (br == null) {
            SmileBufferRecycler<SharedStringNode> br2 = new SmileBufferRecycler<>();
            _smileRecyclerRef.set(new SoftReference<>(br2));
            return br2;
        }
        return br;
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public JsonGenerator useDefaultPrettyPrinter() {
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public JsonGenerator setPrettyPrinter(PrettyPrinter pp) {
        return this;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public Object getOutputTarget() {
        return this._out;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeFieldName(String name) throws IOException {
        if (this._writeContext.writeFieldName(name) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeFieldName(SerializedString name) throws IOException {
        if (this._writeContext.writeFieldName(name.getValue()) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeFieldName(SerializableString name) throws IOException {
        if (this._writeContext.writeFieldName(name.getValue()) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeStringField(String fieldName, String value) throws IOException {
        if (this._writeContext.writeFieldName(fieldName) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(fieldName);
        writeString(value);
    }

    public SmileGenerator enable(Feature f) {
        this._smileFeatures |= f.getMask();
        return this;
    }

    public SmileGenerator disable(Feature f) {
        this._smileFeatures &= f.getMask() ^ (-1);
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (this._smileFeatures & f.getMask()) != 0;
    }

    public SmileGenerator configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public void writeRaw(byte b) throws IOException {
        _writeByte((byte) -8);
    }

    public void writeBytes(byte[] data, int offset, int len) throws IOException {
        _writeBytes(data, offset, len);
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
        } else {
            _writeByte((byte) -8);
        }
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeEndArray() throws IOException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            _writeByte((byte) -7);
        }
        this._writeContext = this._writeContext.getParent();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
        } else {
            _writeByte((byte) -6);
        }
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void writeEndObject() throws IOException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            _writeByte((byte) -5);
        }
    }

    private final void _writeFieldName(String name) throws IOException {
        byte typeToken;
        int ix;
        int len = name.length();
        if (len == 0) {
            _writeByte((byte) 32);
            return;
        }
        if (this._seenNameCount >= 0 && (ix = _findSeenName(name)) >= 0) {
            _writeSharedNameReference(ix);
            return;
        }
        if (len > 56) {
            _writeNonShortFieldName(name, len);
            return;
        }
        if (this._outputTail + SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING >= this._outputEnd) {
            _flushBuffer();
        }
        name.getChars(0, len, this._charBuffer, 0);
        int origOffset = this._outputTail;
        this._outputTail++;
        int byteLen = _shortUTF8Encode(this._charBuffer, 0, len);
        if (byteLen == len) {
            if (byteLen <= 64) {
                typeToken = (byte) (byteLen + Opcodes.LAND);
            } else {
                typeToken = SmileConstants.TOKEN_KEY_LONG_STRING;
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = -4;
            }
        } else if (byteLen <= 56) {
            typeToken = (byte) (byteLen + Opcodes.ARRAYLENGTH);
        } else {
            typeToken = SmileConstants.TOKEN_KEY_LONG_STRING;
            byte[] bArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr2[i2] = -4;
        }
        this._outputBuffer[origOffset] = typeToken;
        if (this._seenNameCount >= 0) {
            _addSeenName(name);
        }
    }

    private final void _writeNonShortFieldName(String name, int len) throws IOException {
        _writeByte(SmileConstants.TOKEN_KEY_LONG_STRING);
        if (len > this._charBufferLength) {
            _slowUTF8Encode(name);
        } else {
            name.getChars(0, len, this._charBuffer, 0);
            int maxLen = len + len + len;
            if (maxLen <= this._outputBuffer.length) {
                if (this._outputTail + maxLen >= this._outputEnd) {
                    _flushBuffer();
                }
                _shortUTF8Encode(this._charBuffer, 0, len);
            } else {
                _mediumUTF8Encode(this._charBuffer, 0, len);
            }
        }
        if (this._seenNameCount >= 0) {
            _addSeenName(name);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = -4;
    }

    protected final void _writeFieldName(SerializableString name) throws IOException {
        int ix;
        int charLen = name.charLength();
        if (charLen == 0) {
            _writeByte((byte) 32);
            return;
        }
        byte[] bytes = name.asUnquotedUTF8();
        int byteLen = bytes.length;
        if (byteLen != charLen) {
            _writeFieldNameUnicode(name, bytes);
            return;
        }
        if (this._seenNameCount >= 0 && (ix = _findSeenName(name.getValue())) >= 0) {
            _writeSharedNameReference(ix);
            return;
        }
        if (byteLen <= 64) {
            if (this._outputTail + byteLen >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) (byteLen + Opcodes.LAND);
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
            if (this._seenNameCount >= 0) {
                _addSeenName(name.getValue());
                return;
            }
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = SmileConstants.TOKEN_KEY_LONG_STRING;
        if (this._outputTail + byteLen + 1 < this._outputEnd) {
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
        } else {
            _flushBuffer();
            if (byteLen < 770) {
                System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
                this._outputTail += byteLen;
            } else {
                if (this._outputTail > 0) {
                    _flushBuffer();
                }
                this._out.write(bytes, 0, byteLen);
            }
        }
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = -4;
        if (this._seenNameCount >= 0) {
            _addSeenName(name.getValue());
        }
    }

    protected final void _writeFieldNameUnicode(SerializableString name, byte[] bytes) throws IOException {
        int ix;
        if (this._seenNameCount >= 0 && (ix = _findSeenName(name.getValue())) >= 0) {
            _writeSharedNameReference(ix);
            return;
        }
        int byteLen = bytes.length;
        if (byteLen <= 56) {
            if (this._outputTail + byteLen >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) (byteLen + Opcodes.ARRAYLENGTH);
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
            if (this._seenNameCount >= 0) {
                _addSeenName(name.getValue());
                return;
            }
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = SmileConstants.TOKEN_KEY_LONG_STRING;
        if (this._outputTail + byteLen + 1 < this._outputEnd) {
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
        } else {
            _flushBuffer();
            if (byteLen < 770) {
                System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
                this._outputTail += byteLen;
            } else {
                if (this._outputTail > 0) {
                    _flushBuffer();
                }
                this._out.write(bytes, 0, byteLen);
            }
        }
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = -4;
        if (this._seenNameCount >= 0) {
            _addSeenName(name.getValue());
        }
    }

    private final void _writeSharedNameReference(int ix) throws IOException {
        if (ix >= this._seenNameCount) {
            throw new IllegalArgumentException("Internal error: trying to write shared name with index " + ix + "; but have only seen " + this._seenNameCount + " so far!");
        }
        if (ix < 64) {
            _writeByte((byte) (ix + 64));
        } else {
            _writeBytes((byte) ((ix >> 8) + 48), (byte) ix);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(String text) throws IOException {
        int ix;
        _verifyValueWrite("write String value");
        int len = text.length();
        if (len == 0) {
            _writeByte((byte) 32);
            return;
        }
        if (len > 65) {
            _writeNonSharedString(text, len);
            return;
        }
        if (this._seenStringValueCount >= 0 && (ix = _findSeenStringValue(text)) >= 0) {
            _writeSharedStringValueReference(ix);
            return;
        }
        if (this._outputTail + SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING >= this._outputEnd) {
            _flushBuffer();
        }
        text.getChars(0, len, this._charBuffer, 0);
        int origOffset = this._outputTail;
        this._outputTail++;
        int byteLen = _shortUTF8Encode(this._charBuffer, 0, len);
        if (byteLen <= 64) {
            if (this._seenStringValueCount >= 0) {
                _addSeenStringValue(text);
            }
            if (byteLen == len) {
                this._outputBuffer[origOffset] = (byte) (byteLen + 63);
                return;
            } else {
                this._outputBuffer[origOffset] = (byte) (byteLen + Opcodes.IAND);
                return;
            }
        }
        this._outputBuffer[origOffset] = byteLen == len ? TOKEN_BYTE_LONG_STRING_ASCII : TOKEN_BYTE_LONG_STRING_UNICODE;
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = -4;
    }

    private final void _writeSharedStringValueReference(int ix) throws IOException {
        if (ix >= this._seenStringValueCount) {
            throw new IllegalArgumentException("Internal error: trying to write shared String value with index " + ix + "; but have only seen " + this._seenStringValueCount + " so far!");
        }
        if (ix < 31) {
            _writeByte((byte) (ix + 1));
        } else {
            _writeBytes((byte) ((ix >> 8) + SmileConstants.TOKEN_MISC_SHARED_STRING_LONG), (byte) ix);
        }
    }

    private final void _writeNonSharedString(String text, int len) throws IOException {
        if (len > this._charBufferLength) {
            _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
            _slowUTF8Encode(text);
            _writeByte((byte) -4);
            return;
        }
        text.getChars(0, len, this._charBuffer, 0);
        int maxLen = len + len + len + 2;
        if (maxLen > this._outputBuffer.length) {
            _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
            _mediumUTF8Encode(this._charBuffer, 0, len);
            _writeByte((byte) -4);
            return;
        }
        if (this._outputTail + maxLen >= this._outputEnd) {
            _flushBuffer();
        }
        int origOffset = this._outputTail;
        _writeByte(TOKEN_BYTE_LONG_STRING_ASCII);
        int byteLen = _shortUTF8Encode(this._charBuffer, 0, len);
        if (byteLen > len) {
            this._outputBuffer[origOffset] = TOKEN_BYTE_LONG_STRING_UNICODE;
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = -4;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeString(char[] text, int offset, int len) throws IOException {
        byte typeToken;
        if (len <= 65 && this._seenStringValueCount >= 0 && len > 0) {
            writeString(new String(text, offset, len));
            return;
        }
        _verifyValueWrite("write String value");
        if (len == 0) {
            _writeByte((byte) 32);
            return;
        }
        if (len <= 64) {
            if (this._outputTail + SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING >= this._outputEnd) {
                _flushBuffer();
            }
            int origOffset = this._outputTail;
            this._outputTail++;
            int byteLen = _shortUTF8Encode(text, offset, offset + len);
            if (byteLen <= 64) {
                if (byteLen == len) {
                    typeToken = (byte) (byteLen + 63);
                } else {
                    typeToken = (byte) (byteLen + Opcodes.IAND);
                }
            } else {
                typeToken = TOKEN_BYTE_LONG_STRING_UNICODE;
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = -4;
            }
            this._outputBuffer[origOffset] = typeToken;
            return;
        }
        int maxLen = len + len + len + 2;
        if (maxLen <= this._outputBuffer.length) {
            if (this._outputTail + maxLen >= this._outputEnd) {
                _flushBuffer();
            }
            int origOffset2 = this._outputTail;
            _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
            if (_shortUTF8Encode(text, offset, offset + len) == len) {
                this._outputBuffer[origOffset2] = TOKEN_BYTE_LONG_STRING_ASCII;
            }
            byte[] bArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr2[i2] = -4;
            return;
        }
        _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
        _mediumUTF8Encode(text, offset, offset + len);
        _writeByte((byte) -4);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeString(SerializableString sstr) throws IOException {
        int ix;
        _verifyValueWrite("write String value");
        String str = sstr.getValue();
        int len = str.length();
        if (len == 0) {
            _writeByte((byte) 32);
            return;
        }
        if (len <= 65 && this._seenStringValueCount >= 0 && (ix = _findSeenStringValue(str)) >= 0) {
            _writeSharedStringValueReference(ix);
            return;
        }
        byte[] raw = sstr.asUnquotedUTF8();
        int byteLen = raw.length;
        if (byteLen <= 64) {
            if (this._outputTail + byteLen + 1 >= this._outputEnd) {
                _flushBuffer();
            }
            int typeToken = byteLen == len ? byteLen + 63 : byteLen + Opcodes.IAND;
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) typeToken;
            System.arraycopy(raw, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
            if (this._seenStringValueCount >= 0) {
                _addSeenStringValue(sstr.getValue());
                return;
            }
            return;
        }
        byte typeToken2 = byteLen == len ? TOKEN_BYTE_LONG_STRING_ASCII : TOKEN_BYTE_LONG_STRING_UNICODE;
        _writeByte(typeToken2);
        _writeBytes(raw, 0, raw.length);
        _writeByte((byte) -4);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRawUTF8String(byte[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write String value");
        if (len == 0) {
            _writeByte((byte) 32);
            return;
        }
        if (this._seenStringValueCount >= 0) {
            throw new UnsupportedOperationException("Can not use direct UTF-8 write methods when 'Feature.CHECK_SHARED_STRING_VALUES' enabled");
        }
        if (len <= 65) {
            if (this._outputTail + len >= this._outputEnd) {
                _flushBuffer();
            }
            if (len == 1) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = 64;
                byte[] bArr2 = this._outputBuffer;
                int i2 = this._outputTail;
                this._outputTail = i2 + 1;
                bArr2[i2] = text[offset];
                return;
            }
            byte[] bArr3 = this._outputBuffer;
            int i3 = this._outputTail;
            this._outputTail = i3 + 1;
            bArr3[i3] = (byte) (len + Opcodes.IAND);
            System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
            return;
        }
        int maxLen = len + len + len + 2;
        if (maxLen <= this._outputBuffer.length) {
            if (this._outputTail + maxLen >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr4 = this._outputBuffer;
            int i4 = this._outputTail;
            this._outputTail = i4 + 1;
            bArr4[i4] = TOKEN_BYTE_LONG_STRING_UNICODE;
            System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
            byte[] bArr5 = this._outputBuffer;
            int i5 = this._outputTail;
            this._outputTail = i5 + 1;
            bArr5[i5] = -4;
            return;
        }
        _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
        _writeBytes(text, offset, len);
        _writeByte((byte) -4);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public final void writeUTF8String(byte[] text, int offset, int len) throws IOException {
        writeRawUTF8String(text, offset, len);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(String text) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(String text, int offset, int len) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(char[] text, int offset, int len) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeRaw(char c) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public void writeRawValue(String text) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public void writeRawValue(String text, int offset, int len) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public void writeRawValue(char[] text, int offset, int len) throws IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
        if (data == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write Binary value");
        if (isEnabled(Feature.ENCODE_BINARY_AS_7BIT)) {
            _writeByte((byte) -24);
            _write7BitBinaryWithLength(data, offset, len);
        } else {
            _writeByte((byte) -3);
            _writePositiveVInt(len);
            _writeBytes(data, offset, len);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeBoolean(boolean state) throws IOException {
        _verifyValueWrite("write boolean value");
        if (state) {
            _writeByte(SmileConstants.TOKEN_LITERAL_TRUE);
        } else {
            _writeByte(SmileConstants.TOKEN_LITERAL_FALSE);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNull() throws IOException {
        _verifyValueWrite("write null value");
        _writeByte(SmileConstants.TOKEN_LITERAL_NULL);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(int i) throws IOException {
        _verifyValueWrite("write number");
        int i2 = SmileUtil.zigzagEncode(i);
        if (i2 <= 63 && i2 >= 0) {
            if (i2 <= 31) {
                _writeByte((byte) (i2 + 192));
                return;
            } else {
                _writeBytes(TOKEN_BYTE_INT_32, (byte) (i2 + 128));
                return;
            }
        }
        byte b0 = (byte) ((i2 & 63) + 128);
        int i3 = i2 >>> 6;
        if (i3 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_32, (byte) i3, b0);
            return;
        }
        byte b1 = (byte) (i3 & Opcodes.LAND);
        int i4 = i3 >> 7;
        if (i4 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_32, (byte) i4, b1, b0);
            return;
        }
        byte b2 = (byte) (i4 & Opcodes.LAND);
        int i5 = i4 >> 7;
        if (i5 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_32, (byte) i5, b2, b1, b0);
        } else {
            byte b3 = (byte) (i5 & Opcodes.LAND);
            _writeBytes(TOKEN_BYTE_INT_32, (byte) (i5 >> 7), b3, b2, b1, b0);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(long l) throws IOException {
        if (l <= MAX_INT_AS_LONG && l >= MIN_INT_AS_LONG) {
            writeNumber((int) l);
            return;
        }
        _verifyValueWrite("write number");
        long l2 = SmileUtil.zigzagEncode(l);
        int i = (int) l2;
        byte b0 = (byte) ((i & 63) + 128);
        byte b1 = (byte) ((i >> 6) & Opcodes.LAND);
        byte b2 = (byte) ((i >> 13) & Opcodes.LAND);
        byte b3 = (byte) ((i >> 20) & Opcodes.LAND);
        long l3 = l2 >>> 27;
        byte b4 = (byte) (((int) l3) & Opcodes.LAND);
        int i2 = (int) (l3 >> 7);
        if (i2 == 0) {
            _writeBytes(TOKEN_BYTE_INT_64, b4, b3, b2, b1, b0);
            return;
        }
        if (i2 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_64, (byte) i2);
            _writeBytes(b4, b3, b2, b1, b0);
            return;
        }
        byte b5 = (byte) (i2 & Opcodes.LAND);
        int i3 = i2 >> 7;
        if (i3 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_64, (byte) i3);
            _writeBytes(b5, b4, b3, b2, b1, b0);
            return;
        }
        byte b6 = (byte) (i3 & Opcodes.LAND);
        int i4 = i3 >> 7;
        if (i4 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_64, (byte) i4, b6);
            _writeBytes(b5, b4, b3, b2, b1, b0);
            return;
        }
        byte b7 = (byte) (i4 & Opcodes.LAND);
        int i5 = i4 >> 7;
        if (i5 <= 127) {
            _writeBytes(TOKEN_BYTE_INT_64, (byte) i5, b7, b6);
            _writeBytes(b5, b4, b3, b2, b1, b0);
        } else {
            byte b8 = (byte) (i5 & Opcodes.LAND);
            _writeBytes(TOKEN_BYTE_INT_64, (byte) (i5 >> 7), b8, b7, b6);
            _writeBytes(b5, b4, b3, b2, b1, b0);
        }
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(BigInteger v) throws IOException {
        if (v == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write number");
        _writeByte(TOKEN_BYTE_BIG_INTEGER);
        byte[] data = v.toByteArray();
        _write7BitBinaryWithLength(data, 0, data.length);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(double d) throws IOException {
        _ensureRoomForOutput(11);
        _verifyValueWrite("write number");
        long l = Double.doubleToRawLongBits(d);
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = 41;
        int hi5 = (int) (l >>> 35);
        this._outputBuffer[this._outputTail + 4] = (byte) (hi5 & Opcodes.LAND);
        int hi6 = hi5 >> 7;
        this._outputBuffer[this._outputTail + 3] = (byte) (hi6 & Opcodes.LAND);
        int hi7 = hi6 >> 7;
        this._outputBuffer[this._outputTail + 2] = (byte) (hi7 & Opcodes.LAND);
        int hi8 = hi7 >> 7;
        this._outputBuffer[this._outputTail + 1] = (byte) (hi8 & Opcodes.LAND);
        this._outputBuffer[this._outputTail] = (byte) (hi8 >> 7);
        this._outputTail += 5;
        int mid = (int) (l >> 28);
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = (byte) (mid & Opcodes.LAND);
        int lo4 = (int) l;
        this._outputBuffer[this._outputTail + 3] = (byte) (lo4 & Opcodes.LAND);
        int lo5 = lo4 >> 7;
        this._outputBuffer[this._outputTail + 2] = (byte) (lo5 & Opcodes.LAND);
        int lo6 = lo5 >> 7;
        this._outputBuffer[this._outputTail + 1] = (byte) (lo6 & Opcodes.LAND);
        this._outputBuffer[this._outputTail] = (byte) ((lo6 >> 7) & Opcodes.LAND);
        this._outputTail += 4;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(float f) throws IOException {
        _ensureRoomForOutput(6);
        _verifyValueWrite("write number");
        int i = Float.floatToRawIntBits(f);
        byte[] bArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = TOKEN_BYTE_FLOAT_32;
        this._outputBuffer[this._outputTail + 4] = (byte) (i & Opcodes.LAND);
        int i3 = i >> 7;
        this._outputBuffer[this._outputTail + 3] = (byte) (i3 & Opcodes.LAND);
        int i4 = i3 >> 7;
        this._outputBuffer[this._outputTail + 2] = (byte) (i4 & Opcodes.LAND);
        int i5 = i4 >> 7;
        this._outputBuffer[this._outputTail + 1] = (byte) (i5 & Opcodes.LAND);
        this._outputBuffer[this._outputTail] = (byte) ((i5 >> 7) & Opcodes.LAND);
        this._outputTail += 5;
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(BigDecimal dec) throws IOException {
        if (dec == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write number");
        _writeByte(TOKEN_BYTE_BIG_DECIMAL);
        int scale = dec.scale();
        _writeSignedVInt(scale);
        BigInteger unscaled = dec.unscaledValue();
        byte[] data = unscaled.toByteArray();
        _write7BitBinaryWithLength(data, 0, data.length);
    }

    @Override // org.codehaus.jackson.JsonGenerator
    public void writeNumber(String encodedValue) throws UnsupportedOperationException, IOException {
        throw _notSupported();
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase
    protected final void _verifyValueWrite(String typeMsg) throws IOException {
        int status = this._writeContext.writeValue();
        if (status == 5) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator
    public final void flush() throws IOException {
        _flushBuffer();
        if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
            this._out.flush();
        }
    }

    @Override // org.codehaus.jackson.impl.JsonGeneratorBase, org.codehaus.jackson.JsonGenerator, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        boolean wasClosed = this._closed;
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
        if (!wasClosed && isEnabled(Feature.WRITE_END_MARKER)) {
            _writeByte((byte) -1);
        }
        _flushBuffer();
        if (this._ioContext.isResourceManaged() || isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {
            this._out.close();
        } else {
            this._out.flush();
        }
        _releaseBuffers();
    }

    private final int _shortUTF8Encode(char[] str, int i, int end) {
        int ptr = this._outputTail;
        byte[] outBuf = this._outputBuffer;
        while (true) {
            char c = str[i];
            if (c > 127) {
                return _shortUTF8Encode2(str, i, end, ptr);
            }
            int ptr2 = ptr + 1;
            outBuf[ptr] = (byte) c;
            i++;
            if (i >= end) {
                int i2 = ptr2 - this._outputTail;
                this._outputTail = ptr2;
                return i2;
            }
            ptr = ptr2;
        }
    }

    private final int _shortUTF8Encode2(char[] str, int i, int end, int outputPtr) {
        byte[] outBuf = this._outputBuffer;
        int outputPtr2 = outputPtr;
        int i2 = i;
        while (i2 < end) {
            int i3 = i2 + 1;
            char c = str[i2];
            if (c <= 127) {
                outBuf[outputPtr2] = (byte) c;
                outputPtr2++;
                i2 = i3;
            } else if (c < 2048) {
                int outputPtr3 = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c >> 6) | 192);
                outputPtr2 = outputPtr3 + 1;
                outBuf[outputPtr3] = (byte) ((c & '?') | 128);
                i2 = i3;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                int outputPtr4 = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c >> '\f') | 224);
                int outputPtr5 = outputPtr4 + 1;
                outBuf[outputPtr4] = (byte) (((c >> 6) & 63) | 128);
                outBuf[outputPtr5] = (byte) ((c & '?') | 128);
                outputPtr2 = outputPtr5 + 1;
                i2 = i3;
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (i3 >= end) {
                    _throwIllegalSurrogate(c);
                }
                i2 = i3 + 1;
                int c2 = _convertSurrogate(c, str[i3]);
                if (c2 > 1114111) {
                    _throwIllegalSurrogate(c2);
                }
                int outputPtr6 = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c2 >> 18) | 240);
                int outputPtr7 = outputPtr6 + 1;
                outBuf[outputPtr6] = (byte) (((c2 >> 12) & 63) | 128);
                int outputPtr8 = outputPtr7 + 1;
                outBuf[outputPtr7] = (byte) (((c2 >> 6) & 63) | 128);
                outputPtr2 = outputPtr8 + 1;
                outBuf[outputPtr8] = (byte) ((c2 & 63) | 128);
            }
        }
        int codedLen = outputPtr2 - this._outputTail;
        this._outputTail = outputPtr2;
        return codedLen;
    }

    private void _slowUTF8Encode(String str) throws IOException {
        int inputPtr;
        int len = str.length();
        int bufferEnd = this._outputEnd - 4;
        int inputPtr2 = 0;
        while (inputPtr2 < len) {
            if (this._outputTail >= bufferEnd) {
                _flushBuffer();
            }
            int inputPtr3 = inputPtr2 + 1;
            int c = str.charAt(inputPtr2);
            if (c <= 127) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) c;
                int maxInCount = len - inputPtr3;
                int maxOutCount = bufferEnd - this._outputTail;
                if (maxInCount > maxOutCount) {
                    maxInCount = maxOutCount;
                }
                int maxInCount2 = maxInCount + inputPtr3;
                inputPtr2 = inputPtr3;
                while (true) {
                    if (inputPtr2 < maxInCount2) {
                        int inputPtr4 = inputPtr2 + 1;
                        c = str.charAt(inputPtr2);
                        if (c > 127) {
                            inputPtr2 = inputPtr4;
                        } else {
                            byte[] bArr2 = this._outputBuffer;
                            int i2 = this._outputTail;
                            this._outputTail = i2 + 1;
                            bArr2[i2] = (byte) c;
                            inputPtr2 = inputPtr4;
                        }
                    }
                }
            } else {
                inputPtr2 = inputPtr3;
            }
            if (c < 2048) {
                byte[] bArr3 = this._outputBuffer;
                int i3 = this._outputTail;
                this._outputTail = i3 + 1;
                bArr3[i3] = (byte) ((c >> 6) | 192);
                byte[] bArr4 = this._outputBuffer;
                int i4 = this._outputTail;
                this._outputTail = i4 + 1;
                bArr4[i4] = (byte) ((c & 63) | 128);
                inputPtr = inputPtr2;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                byte[] bArr5 = this._outputBuffer;
                int i5 = this._outputTail;
                this._outputTail = i5 + 1;
                bArr5[i5] = (byte) ((c >> 12) | 224);
                byte[] bArr6 = this._outputBuffer;
                int i6 = this._outputTail;
                this._outputTail = i6 + 1;
                bArr6[i6] = (byte) (((c >> 6) & 63) | 128);
                byte[] bArr7 = this._outputBuffer;
                int i7 = this._outputTail;
                this._outputTail = i7 + 1;
                bArr7[i7] = (byte) ((c & 63) | 128);
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (inputPtr2 >= len) {
                    _throwIllegalSurrogate(c);
                }
                inputPtr = inputPtr2 + 1;
                int c2 = _convertSurrogate(c, str.charAt(inputPtr2));
                if (c2 > 1114111) {
                    _throwIllegalSurrogate(c2);
                }
                byte[] bArr8 = this._outputBuffer;
                int i8 = this._outputTail;
                this._outputTail = i8 + 1;
                bArr8[i8] = (byte) ((c2 >> 18) | 240);
                byte[] bArr9 = this._outputBuffer;
                int i9 = this._outputTail;
                this._outputTail = i9 + 1;
                bArr9[i9] = (byte) (((c2 >> 12) & 63) | 128);
                byte[] bArr10 = this._outputBuffer;
                int i10 = this._outputTail;
                this._outputTail = i10 + 1;
                bArr10[i10] = (byte) (((c2 >> 6) & 63) | 128);
                byte[] bArr11 = this._outputBuffer;
                int i11 = this._outputTail;
                this._outputTail = i11 + 1;
                bArr11[i11] = (byte) ((c2 & 63) | 128);
            }
            inputPtr2 = inputPtr;
        }
    }

    private void _mediumUTF8Encode(char[] str, int inputPtr, int inputEnd) throws IOException {
        int inputPtr2;
        int bufferEnd = this._outputEnd - 4;
        int inputPtr3 = inputPtr;
        while (inputPtr3 < inputEnd) {
            if (this._outputTail >= bufferEnd) {
                _flushBuffer();
            }
            int inputPtr4 = inputPtr3 + 1;
            char c = str[inputPtr3];
            if (c <= 127) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) c;
                int maxInCount = inputEnd - inputPtr4;
                int maxOutCount = bufferEnd - this._outputTail;
                if (maxInCount > maxOutCount) {
                    maxInCount = maxOutCount;
                }
                int maxInCount2 = maxInCount + inputPtr4;
                inputPtr3 = inputPtr4;
                while (true) {
                    if (inputPtr3 < maxInCount2) {
                        int inputPtr5 = inputPtr3 + 1;
                        c = str[inputPtr3];
                        if (c > 127) {
                            inputPtr3 = inputPtr5;
                        } else {
                            byte[] bArr2 = this._outputBuffer;
                            int i2 = this._outputTail;
                            this._outputTail = i2 + 1;
                            bArr2[i2] = (byte) c;
                            inputPtr3 = inputPtr5;
                        }
                    }
                }
            } else {
                inputPtr3 = inputPtr4;
            }
            if (c < 2048) {
                byte[] bArr3 = this._outputBuffer;
                int i3 = this._outputTail;
                this._outputTail = i3 + 1;
                bArr3[i3] = (byte) ((c >> 6) | 192);
                byte[] bArr4 = this._outputBuffer;
                int i4 = this._outputTail;
                this._outputTail = i4 + 1;
                bArr4[i4] = (byte) ((c & '?') | 128);
                inputPtr2 = inputPtr3;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                byte[] bArr5 = this._outputBuffer;
                int i5 = this._outputTail;
                this._outputTail = i5 + 1;
                bArr5[i5] = (byte) ((c >> '\f') | 224);
                byte[] bArr6 = this._outputBuffer;
                int i6 = this._outputTail;
                this._outputTail = i6 + 1;
                bArr6[i6] = (byte) (((c >> 6) & 63) | 128);
                byte[] bArr7 = this._outputBuffer;
                int i7 = this._outputTail;
                this._outputTail = i7 + 1;
                bArr7[i7] = (byte) ((c & '?') | 128);
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (inputPtr3 >= inputEnd) {
                    _throwIllegalSurrogate(c);
                }
                inputPtr2 = inputPtr3 + 1;
                int c2 = _convertSurrogate(c, str[inputPtr3]);
                if (c2 > 1114111) {
                    _throwIllegalSurrogate(c2);
                }
                byte[] bArr8 = this._outputBuffer;
                int i8 = this._outputTail;
                this._outputTail = i8 + 1;
                bArr8[i8] = (byte) ((c2 >> 18) | 240);
                byte[] bArr9 = this._outputBuffer;
                int i9 = this._outputTail;
                this._outputTail = i9 + 1;
                bArr9[i9] = (byte) (((c2 >> 12) & 63) | 128);
                byte[] bArr10 = this._outputBuffer;
                int i10 = this._outputTail;
                this._outputTail = i10 + 1;
                bArr10[i10] = (byte) (((c2 >> 6) & 63) | 128);
                byte[] bArr11 = this._outputBuffer;
                int i11 = this._outputTail;
                this._outputTail = i11 + 1;
                bArr11[i11] = (byte) ((c2 & 63) | 128);
            }
            inputPtr3 = inputPtr2;
        }
    }

    private int _convertSurrogate(int firstPart, int secondPart) {
        if (secondPart < SURR2_FIRST || secondPart > SURR2_LAST) {
            throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
        }
        return 65536 + ((firstPart - SURR1_FIRST) << 10) + (secondPart - SURR2_FIRST);
    }

    private void _throwIllegalSurrogate(int code) {
        if (code > 1114111) {
            throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
        }
        if (code >= SURR1_FIRST) {
            if (code <= SURR1_LAST) {
                throw new IllegalArgumentException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
            }
            throw new IllegalArgumentException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        }
        throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
    }

    private final void _ensureRoomForOutput(int needed) throws IOException {
        if (this._outputTail + needed >= this._outputEnd) {
            _flushBuffer();
        }
    }

    private final void _writeByte(byte b) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b;
    }

    private final void _writeBytes(byte b1, byte b2) throws IOException {
        if (this._outputTail + 1 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = b2;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3) throws IOException {
        if (this._outputTail + 2 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = b2;
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = b3;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3, byte b4) throws IOException {
        if (this._outputTail + 3 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = b2;
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = b3;
        byte[] bArr4 = this._outputBuffer;
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        bArr4[i4] = b4;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3, byte b4, byte b5) throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = b2;
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = b3;
        byte[] bArr4 = this._outputBuffer;
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        bArr4[i4] = b4;
        byte[] bArr5 = this._outputBuffer;
        int i5 = this._outputTail;
        this._outputTail = i5 + 1;
        bArr5[i5] = b5;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) throws IOException {
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        byte[] bArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr2[i2] = b2;
        byte[] bArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        bArr3[i3] = b3;
        byte[] bArr4 = this._outputBuffer;
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        bArr4[i4] = b4;
        byte[] bArr5 = this._outputBuffer;
        int i5 = this._outputTail;
        this._outputTail = i5 + 1;
        bArr5[i5] = b5;
        byte[] bArr6 = this._outputBuffer;
        int i6 = this._outputTail;
        this._outputTail = i6 + 1;
        bArr6[i6] = b6;
    }

    private final void _writeBytes(byte[] data, int offset, int len) throws IOException {
        if (len != 0) {
            if (this._outputTail + len >= this._outputEnd) {
                _writeBytesLong(data, offset, len);
            } else {
                System.arraycopy(data, offset, this._outputBuffer, this._outputTail, len);
                this._outputTail += len;
            }
        }
    }

    private final void _writeBytesLong(byte[] data, int offset, int len) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        while (true) {
            int currLen = Math.min(len, this._outputEnd - this._outputTail);
            System.arraycopy(data, offset, this._outputBuffer, this._outputTail, currLen);
            this._outputTail += currLen;
            len -= currLen;
            if (len != 0) {
                offset += currLen;
                _flushBuffer();
            } else {
                return;
            }
        }
    }

    private void _writePositiveVInt(int i) throws IOException {
        _ensureRoomForOutput(5);
        byte b0 = (byte) ((i & 63) + 128);
        int i2 = i >> 6;
        if (i2 <= 127) {
            if (i2 > 0) {
                byte[] bArr = this._outputBuffer;
                int i3 = this._outputTail;
                this._outputTail = i3 + 1;
                bArr[i3] = (byte) i2;
            }
            byte[] bArr2 = this._outputBuffer;
            int i4 = this._outputTail;
            this._outputTail = i4 + 1;
            bArr2[i4] = b0;
            return;
        }
        byte b1 = (byte) (i2 & Opcodes.LAND);
        int i5 = i2 >> 7;
        if (i5 <= 127) {
            byte[] bArr3 = this._outputBuffer;
            int i6 = this._outputTail;
            this._outputTail = i6 + 1;
            bArr3[i6] = (byte) i5;
            byte[] bArr4 = this._outputBuffer;
            int i7 = this._outputTail;
            this._outputTail = i7 + 1;
            bArr4[i7] = b1;
            byte[] bArr5 = this._outputBuffer;
            int i8 = this._outputTail;
            this._outputTail = i8 + 1;
            bArr5[i8] = b0;
            return;
        }
        byte b2 = (byte) (i5 & Opcodes.LAND);
        int i9 = i5 >> 7;
        if (i9 <= 127) {
            byte[] bArr6 = this._outputBuffer;
            int i10 = this._outputTail;
            this._outputTail = i10 + 1;
            bArr6[i10] = (byte) i9;
            byte[] bArr7 = this._outputBuffer;
            int i11 = this._outputTail;
            this._outputTail = i11 + 1;
            bArr7[i11] = b2;
            byte[] bArr8 = this._outputBuffer;
            int i12 = this._outputTail;
            this._outputTail = i12 + 1;
            bArr8[i12] = b1;
            byte[] bArr9 = this._outputBuffer;
            int i13 = this._outputTail;
            this._outputTail = i13 + 1;
            bArr9[i13] = b0;
            return;
        }
        byte b3 = (byte) (i9 & Opcodes.LAND);
        byte[] bArr10 = this._outputBuffer;
        int i14 = this._outputTail;
        this._outputTail = i14 + 1;
        bArr10[i14] = (byte) (i9 >> 7);
        byte[] bArr11 = this._outputBuffer;
        int i15 = this._outputTail;
        this._outputTail = i15 + 1;
        bArr11[i15] = b3;
        byte[] bArr12 = this._outputBuffer;
        int i16 = this._outputTail;
        this._outputTail = i16 + 1;
        bArr12[i16] = b2;
        byte[] bArr13 = this._outputBuffer;
        int i17 = this._outputTail;
        this._outputTail = i17 + 1;
        bArr13[i17] = b1;
        byte[] bArr14 = this._outputBuffer;
        int i18 = this._outputTail;
        this._outputTail = i18 + 1;
        bArr14[i18] = b0;
    }

    private void _writeSignedVInt(int input) throws IOException {
        _writePositiveVInt(SmileUtil.zigzagEncode(input));
    }

    protected void _write7BitBinaryWithLength(byte[] data, int offset, int len) throws IOException {
        int offset2;
        _writePositiveVInt(len);
        while (true) {
            offset2 = offset;
            if (len < 7) {
                break;
            }
            if (this._outputTail + 8 >= this._outputEnd) {
                _flushBuffer();
            }
            int offset3 = offset2 + 1;
            int i = data[offset2];
            byte[] bArr = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 1) & Opcodes.LAND);
            int offset4 = offset3 + 1;
            int i3 = (i << 8) | (data[offset3] & 255);
            byte[] bArr2 = this._outputBuffer;
            int i4 = this._outputTail;
            this._outputTail = i4 + 1;
            bArr2[i4] = (byte) ((i3 >> 2) & Opcodes.LAND);
            int offset5 = offset4 + 1;
            int i5 = (i3 << 8) | (data[offset4] & 255);
            byte[] bArr3 = this._outputBuffer;
            int i6 = this._outputTail;
            this._outputTail = i6 + 1;
            bArr3[i6] = (byte) ((i5 >> 3) & Opcodes.LAND);
            int offset6 = offset5 + 1;
            int i7 = (i5 << 8) | (data[offset5] & 255);
            byte[] bArr4 = this._outputBuffer;
            int i8 = this._outputTail;
            this._outputTail = i8 + 1;
            bArr4[i8] = (byte) ((i7 >> 4) & Opcodes.LAND);
            int offset7 = offset6 + 1;
            int i9 = (i7 << 8) | (data[offset6] & 255);
            byte[] bArr5 = this._outputBuffer;
            int i10 = this._outputTail;
            this._outputTail = i10 + 1;
            bArr5[i10] = (byte) ((i9 >> 5) & Opcodes.LAND);
            int offset8 = offset7 + 1;
            int i11 = (i9 << 8) | (data[offset7] & 255);
            byte[] bArr6 = this._outputBuffer;
            int i12 = this._outputTail;
            this._outputTail = i12 + 1;
            bArr6[i12] = (byte) ((i11 >> 6) & Opcodes.LAND);
            offset = offset8 + 1;
            int i13 = (i11 << 8) | (data[offset8] & 255);
            byte[] bArr7 = this._outputBuffer;
            int i14 = this._outputTail;
            this._outputTail = i14 + 1;
            bArr7[i14] = (byte) ((i13 >> 7) & Opcodes.LAND);
            byte[] bArr8 = this._outputBuffer;
            int i15 = this._outputTail;
            this._outputTail = i15 + 1;
            bArr8[i15] = (byte) (i13 & Opcodes.LAND);
            len -= 7;
        }
        if (len > 0) {
            if (this._outputTail + 7 >= this._outputEnd) {
                _flushBuffer();
            }
            int offset9 = offset2 + 1;
            int i16 = data[offset2];
            byte[] bArr9 = this._outputBuffer;
            int i17 = this._outputTail;
            this._outputTail = i17 + 1;
            bArr9[i17] = (byte) ((i16 >> 1) & Opcodes.LAND);
            if (len > 1) {
                int offset10 = offset9 + 1;
                int i18 = ((i16 & 1) << 8) | (data[offset9] & 255);
                byte[] bArr10 = this._outputBuffer;
                int i19 = this._outputTail;
                this._outputTail = i19 + 1;
                bArr10[i19] = (byte) ((i18 >> 2) & Opcodes.LAND);
                if (len > 2) {
                    int offset11 = offset10 + 1;
                    int i20 = ((i18 & 3) << 8) | (data[offset10] & 255);
                    byte[] bArr11 = this._outputBuffer;
                    int i21 = this._outputTail;
                    this._outputTail = i21 + 1;
                    bArr11[i21] = (byte) ((i20 >> 3) & Opcodes.LAND);
                    if (len > 3) {
                        int offset12 = offset11 + 1;
                        int i22 = ((i20 & 7) << 8) | (data[offset11] & 255);
                        byte[] bArr12 = this._outputBuffer;
                        int i23 = this._outputTail;
                        this._outputTail = i23 + 1;
                        bArr12[i23] = (byte) ((i22 >> 4) & Opcodes.LAND);
                        if (len > 4) {
                            int offset13 = offset12 + 1;
                            int i24 = ((i22 & 15) << 8) | (data[offset12] & 255);
                            byte[] bArr13 = this._outputBuffer;
                            int i25 = this._outputTail;
                            this._outputTail = i25 + 1;
                            bArr13[i25] = (byte) ((i24 >> 5) & Opcodes.LAND);
                            if (len > 5) {
                                int i26 = offset13 + 1;
                                int i27 = ((i24 & 31) << 8) | (data[offset13] & 255);
                                byte[] bArr14 = this._outputBuffer;
                                int i28 = this._outputTail;
                                this._outputTail = i28 + 1;
                                bArr14[i28] = (byte) ((i27 >> 6) & Opcodes.LAND);
                                byte[] bArr15 = this._outputBuffer;
                                int i29 = this._outputTail;
                                this._outputTail = i29 + 1;
                                bArr15[i29] = (byte) (i27 & 63);
                                return;
                            }
                            byte[] bArr16 = this._outputBuffer;
                            int i30 = this._outputTail;
                            this._outputTail = i30 + 1;
                            bArr16[i30] = (byte) (i24 & 31);
                            return;
                        }
                        byte[] bArr17 = this._outputBuffer;
                        int i31 = this._outputTail;
                        this._outputTail = i31 + 1;
                        bArr17[i31] = (byte) (i22 & 15);
                        return;
                    }
                    byte[] bArr18 = this._outputBuffer;
                    int i32 = this._outputTail;
                    this._outputTail = i32 + 1;
                    bArr18[i32] = (byte) (i20 & 7);
                    return;
                }
                byte[] bArr19 = this._outputBuffer;
                int i33 = this._outputTail;
                this._outputTail = i33 + 1;
                bArr19[i33] = (byte) (i18 & 3);
                return;
            }
            byte[] bArr20 = this._outputBuffer;
            int i34 = this._outputTail;
            this._outputTail = i34 + 1;
            bArr20[i34] = (byte) (i16 & 1);
        }
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
        SharedStringNode[] nameBuf = this._seenNames;
        if (nameBuf != null && nameBuf.length == 64) {
            this._seenNames = null;
            this._smileBufferRecycler.releaseSeenNamesBuffer(nameBuf);
        }
        SharedStringNode[] valueBuf = this._seenStringValues;
        if (valueBuf != null && valueBuf.length == 64) {
            this._seenStringValues = null;
            this._smileBufferRecycler.releaseSeenStringValuesBuffer(valueBuf);
        }
    }

    protected final void _flushBuffer() throws IOException {
        if (this._outputTail > 0) {
            this._bytesWritten += this._outputTail;
            this._out.write(this._outputBuffer, 0, this._outputTail);
            this._outputTail = 0;
        }
    }

    private final int _findSeenName(String name) {
        int hash = name.hashCode();
        SharedStringNode head = this._seenNames[(this._seenNames.length - 1) & hash];
        if (head == null) {
            return -1;
        }
        SharedStringNode node = head;
        if (node.value == name) {
            return node.index;
        }
        do {
            node = node.next;
            if (node == null) {
                SharedStringNode node2 = head;
                do {
                    String value = node2.value;
                    if (value.hashCode() == hash && value.equals(name)) {
                        return node2.index;
                    }
                    node2 = node2.next;
                } while (node2 != null);
                return -1;
            }
        } while (node.value != name);
        return node.index;
    }

    private final void _addSeenName(String name) {
        if (this._seenNameCount == this._seenNames.length) {
            if (this._seenNameCount == 1024) {
                Arrays.fill(this._seenNames, (Object) null);
                this._seenNameCount = 0;
            } else {
                SharedStringNode[] old = this._seenNames;
                this._seenNames = new SharedStringNode[1024];
                for (SharedStringNode node : old) {
                    for (; node != null; node = node.next) {
                        int ix = node.value.hashCode() & 1023;
                        node.next = this._seenNames[ix];
                        this._seenNames[ix] = node;
                    }
                }
            }
        }
        int ix2 = name.hashCode() & (this._seenNames.length - 1);
        this._seenNames[ix2] = new SharedStringNode(name, this._seenNameCount, this._seenNames[ix2]);
        this._seenNameCount++;
    }

    private final int _findSeenStringValue(String text) {
        int hash = text.hashCode();
        SharedStringNode head = this._seenStringValues[(this._seenStringValues.length - 1) & hash];
        if (head != null) {
            SharedStringNode node = head;
            while (node.value != text) {
                node = node.next;
                if (node == null) {
                    SharedStringNode node2 = head;
                    do {
                        String value = node2.value;
                        if (value.hashCode() == hash && value.equals(text)) {
                            return node2.index;
                        }
                        node2 = node2.next;
                    } while (node2 != null);
                }
            }
            return node.index;
        }
        return -1;
    }

    private final void _addSeenStringValue(String text) {
        if (this._seenStringValueCount == this._seenStringValues.length) {
            if (this._seenStringValueCount == 1024) {
                Arrays.fill(this._seenStringValues, (Object) null);
                this._seenStringValueCount = 0;
            } else {
                SharedStringNode[] old = this._seenStringValues;
                this._seenStringValues = new SharedStringNode[1024];
                for (SharedStringNode node : old) {
                    for (; node != null; node = node.next) {
                        int ix = node.value.hashCode() & 1023;
                        node.next = this._seenStringValues[ix];
                        this._seenStringValues[ix] = node;
                    }
                }
            }
        }
        int ix2 = text.hashCode() & (this._seenStringValues.length - 1);
        this._seenStringValues[ix2] = new SharedStringNode(text, this._seenStringValueCount, this._seenStringValues[ix2]);
        this._seenStringValueCount++;
    }

    protected long outputOffset() {
        return this._bytesWritten + this._outputTail;
    }

    protected UnsupportedOperationException _notSupported() {
        return new UnsupportedOperationException();
    }
}
