package org.codehaus.jackson.smile;

import com.badlogic.gdx.Input;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.impl.StreamBasedParserBase;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SmileParser extends StreamBasedParserBase {
    private static final int[] NO_INTS = new int[0];
    private static final String[] NO_STRINGS = new String[0];
    protected static final ThreadLocal<SoftReference<SmileBufferRecycler<String>>> _smileRecyclerRef = new ThreadLocal<>();
    protected boolean _got32BitFloat;
    protected boolean _mayContainRawBinary;
    protected ObjectCodec _objectCodec;
    protected int _quad1;
    protected int _quad2;
    protected int[] _quadBuffer;
    protected int _seenNameCount;
    protected String[] _seenNames;
    protected int _seenStringValueCount;
    protected String[] _seenStringValues;
    protected final SmileBufferRecycler<String> _smileBufferRecycler;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;
    protected int _typeByte;

    public enum Feature {
        REQUIRE_HEADER(true);

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

    public SmileParser(IOContext ctxt, int parserFeatures, int smileFeatures, ObjectCodec codec, BytesToNameCanonicalizer sym, InputStream in, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, parserFeatures, in, inputBuffer, start, end, bufferRecyclable);
        this._tokenIncomplete = false;
        this._quadBuffer = NO_INTS;
        this._seenNames = NO_STRINGS;
        this._seenNameCount = 0;
        this._seenStringValues = null;
        this._seenStringValueCount = -1;
        this._objectCodec = codec;
        this._symbols = sym;
        this._tokenInputRow = -1;
        this._tokenInputCol = -1;
        this._smileBufferRecycler = _smileBufferRecycler();
    }

    @Override // org.codehaus.jackson.JsonParser
    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    @Override // org.codehaus.jackson.JsonParser
    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    protected boolean handleSignature(boolean consumeFirstByte, boolean throwException) throws IOException {
        if (consumeFirstByte) {
            this._inputPtr++;
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if (this._inputBuffer[this._inputPtr] != 41) {
            if (!throwException) {
                return false;
            }
            _reportError("Malformed content: signature not valid, starts with 0x3a but followed by 0x" + Integer.toHexString(this._inputBuffer[this._inputPtr]) + ", not 0x29");
            return false;
        }
        int i = this._inputPtr + 1;
        this._inputPtr = i;
        if (i >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if (this._inputBuffer[this._inputPtr] != 10) {
            if (!throwException) {
                return false;
            }
            _reportError("Malformed content: signature not valid, starts with 0x3a, 0x29, but followed by 0x" + Integer.toHexString(this._inputBuffer[this._inputPtr]) + ", not 0xA");
            return false;
        }
        int i2 = this._inputPtr + 1;
        this._inputPtr = i2;
        if (i2 >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        int ch = bArr[i3];
        int versionBits = (ch >> 4) & 15;
        if (versionBits != 0) {
            _reportError("Header version number bits (0x" + Integer.toHexString(versionBits) + ") indicate unrecognized version; only 0x0 handled by parser");
        }
        if ((ch & 1) == 0) {
            this._seenNames = null;
            this._seenNameCount = -1;
        }
        if ((ch & 2) != 0) {
            this._seenStringValues = NO_STRINGS;
            this._seenStringValueCount = 0;
        }
        this._mayContainRawBinary = (ch & 4) != 0;
        return true;
    }

    protected static final SmileBufferRecycler<String> _smileBufferRecycler() {
        SoftReference<SmileBufferRecycler<String>> ref = _smileRecyclerRef.get();
        SmileBufferRecycler<String> br = ref == null ? null : ref.get();
        if (br == null) {
            SmileBufferRecycler<String> br2 = new SmileBufferRecycler<>();
            _smileRecyclerRef.set(new SoftReference<>(br2));
            return br2;
        }
        return br;
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected void _finishString() throws IOException {
        _throwInternal();
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase, org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    @Override // org.codehaus.jackson.impl.StreamBasedParserBase, org.codehaus.jackson.impl.JsonParserBase
    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        String[] nameBuf = this._seenNames;
        if (nameBuf != null && nameBuf.length > 0) {
            this._seenNames = null;
            Arrays.fill(nameBuf, 0, this._seenNameCount, (Object) null);
            this._smileBufferRecycler.releaseSeenNamesBuffer(nameBuf);
        }
        String[] valueBuf = this._seenStringValues;
        if (valueBuf != null && valueBuf.length > 0) {
            this._seenStringValues = null;
            Arrays.fill(valueBuf, 0, this._seenStringValueCount, (Object) null);
            this._smileBufferRecycler.releaseSeenStringValuesBuffer(valueBuf);
        }
    }

    public boolean mayContainRawBinary() {
        return this._mayContainRawBinary;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public JsonToken nextToken() throws IOException {
        if (this._tokenIncomplete) {
            _skipIncomplete();
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._binaryValue = null;
        if (this._parsingContext.inObject() && this._currToken != JsonToken.FIELD_NAME) {
            JsonToken jsonToken_handleFieldName = _handleFieldName();
            this._currToken = jsonToken_handleFieldName;
            return jsonToken_handleFieldName;
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _handleEOF();
            close();
            this._currToken = null;
            return null;
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i];
        this._typeByte = ch;
        switch ((ch >> 5) & 7) {
            case 0:
                if (ch == 0) {
                    _reportError("Invalid token byte 0x00");
                }
                return _handleSharedString(ch - 1);
            case 1:
                int typeBits = ch & 31;
                if (typeBits < 4) {
                    switch (typeBits) {
                        case 0:
                            this._textBuffer.resetWithEmpty();
                            JsonToken jsonToken = JsonToken.VALUE_STRING;
                            this._currToken = jsonToken;
                            return jsonToken;
                        case 1:
                            JsonToken jsonToken2 = JsonToken.VALUE_NULL;
                            this._currToken = jsonToken2;
                            return jsonToken2;
                        case 2:
                            JsonToken jsonToken3 = JsonToken.VALUE_FALSE;
                            this._currToken = jsonToken3;
                            return jsonToken3;
                        default:
                            JsonToken jsonToken4 = JsonToken.VALUE_TRUE;
                            this._currToken = jsonToken4;
                            return jsonToken4;
                    }
                }
                if (typeBits < 8) {
                    if ((typeBits & 3) <= 2) {
                        this._tokenIncomplete = true;
                        this._numTypesValid = 0;
                        JsonToken jsonToken5 = JsonToken.VALUE_NUMBER_INT;
                        this._currToken = jsonToken5;
                        return jsonToken5;
                    }
                } else if (typeBits < 12) {
                    int subtype = typeBits & 3;
                    if (subtype <= 2) {
                        this._tokenIncomplete = true;
                        this._numTypesValid = 0;
                        this._got32BitFloat = subtype == 0;
                        JsonToken jsonToken6 = JsonToken.VALUE_NUMBER_FLOAT;
                        this._currToken = jsonToken6;
                        return jsonToken6;
                    }
                } else {
                    if (typeBits == 26 && handleSignature(false, false)) {
                        if (this._currToken == null) {
                            return nextToken();
                        }
                        this._currToken = null;
                        return null;
                    }
                    _reportError("Unrecognized token byte 0x3A (malformed segment header?");
                }
                break;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                this._currToken = JsonToken.VALUE_STRING;
                if (this._seenStringValueCount >= 0) {
                    _addSeenStringValue();
                } else {
                    this._tokenIncomplete = true;
                }
                return this._currToken;
            case 6:
                this._numberInt = SmileUtil.zigzagDecode(ch & 31);
                this._numTypesValid = 1;
                JsonToken jsonToken7 = JsonToken.VALUE_NUMBER_INT;
                this._currToken = jsonToken7;
                return jsonToken7;
            case 7:
                switch (ch & 31) {
                    case 0:
                    case 4:
                        this._tokenIncomplete = true;
                        JsonToken jsonToken8 = JsonToken.VALUE_STRING;
                        this._currToken = jsonToken8;
                        return jsonToken8;
                    case 8:
                        this._tokenIncomplete = true;
                        JsonToken jsonToken9 = JsonToken.VALUE_EMBEDDED_OBJECT;
                        this._currToken = jsonToken9;
                        return jsonToken9;
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        byte[] bArr2 = this._inputBuffer;
                        int i2 = this._inputPtr;
                        this._inputPtr = i2 + 1;
                        return _handleSharedString(((ch & 3) << 8) + (bArr2[i2] & 255));
                    case 24:
                        this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                        JsonToken jsonToken10 = JsonToken.START_ARRAY;
                        this._currToken = jsonToken10;
                        return jsonToken10;
                    case 25:
                        if (!this._parsingContext.inArray()) {
                            _reportMismatchedEndMarker(93, '}');
                        }
                        this._parsingContext = this._parsingContext.getParent();
                        JsonToken jsonToken11 = JsonToken.END_ARRAY;
                        this._currToken = jsonToken11;
                        return jsonToken11;
                    case Input.Keys.POWER /* 26 */:
                        this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                        JsonToken jsonToken12 = JsonToken.START_OBJECT;
                        this._currToken = jsonToken12;
                        return jsonToken12;
                    case Input.Keys.CAMERA /* 27 */:
                        _reportError("Invalid type marker byte 0xFB in value mode (would be END_OBJECT in key mode)");
                        break;
                    case Input.Keys.A /* 29 */:
                        break;
                    case Input.Keys.C /* 31 */:
                        this._currToken = null;
                        return null;
                }
                this._tokenIncomplete = true;
                JsonToken jsonToken13 = JsonToken.VALUE_EMBEDDED_OBJECT;
                this._currToken = jsonToken13;
                return jsonToken13;
        }
        _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch & 255) + " for expected value token");
        return null;
    }

    private final JsonToken _handleSharedString(int index) throws IOException {
        if (index >= this._seenStringValueCount) {
            _reportInvalidSharedStringValue(index);
        }
        this._textBuffer.resetWithString(this._seenStringValues[index]);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final void _addSeenStringValue() throws IOException {
        _finishToken();
        if (this._seenStringValueCount < this._seenStringValues.length) {
            String[] strArr = this._seenStringValues;
            int i = this._seenStringValueCount;
            this._seenStringValueCount = i + 1;
            strArr[i] = this._textBuffer.contentsAsString();
            return;
        }
        _expandSeenStringValues();
    }

    private final void _expandSeenStringValues() {
        String[] newShared;
        String[] oldShared = this._seenStringValues;
        int len = oldShared.length;
        if (len == 0) {
            newShared = this._smileBufferRecycler.allocSeenStringValuesBuffer();
            if (newShared == null) {
                newShared = new String[64];
            }
        } else if (len == 1024) {
            newShared = oldShared;
            this._seenStringValueCount = 0;
        } else {
            int newSize = len == 64 ? 256 : 1024;
            newShared = new String[newSize];
            System.arraycopy(oldShared, 0, newShared, 0, oldShared.length);
        }
        this._seenStringValues = newShared;
        String[] strArr = this._seenStringValues;
        int i = this._seenStringValueCount;
        this._seenStringValueCount = i + 1;
        strArr[i] = this._textBuffer.contentsAsString();
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase, org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public String getCurrentName() throws IOException {
        return this._parsingContext.getCurrentName();
    }

    @Override // org.codehaus.jackson.impl.JsonNumericParserBase, org.codehaus.jackson.JsonParser
    public JsonParser.NumberType getNumberType() throws IOException {
        return this._got32BitFloat ? JsonParser.NumberType.FLOAT : super.getNumberType();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public String getText() throws IOException {
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            int tb = this._typeByte;
            int type = (tb >> 5) & 7;
            if (type == 2 || type == 3) {
                _decodeShortAsciiValue((tb & 63) + 1);
                return this._textBuffer.contentsAsString();
            }
            if (type == 4 || type == 5) {
                _decodeShortUnicodeValue((tb & 63) + 2);
                return this._textBuffer.contentsAsString();
            }
            _finishToken();
        }
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsAsString();
        }
        JsonToken t = this._currToken;
        if (t == null) {
            return null;
        }
        if (t == JsonToken.FIELD_NAME) {
            return this._parsingContext.getCurrentName();
        }
        if (t.isNumeric()) {
            return getNumberValue().toString();
        }
        return this._currToken.asString();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public char[] getTextCharacters() throws IOException {
        if (this._currToken != null) {
            if (this._tokenIncomplete) {
                _finishToken();
            }
            switch (this._currToken) {
                case VALUE_STRING:
                    return this._textBuffer.getTextBuffer();
                case FIELD_NAME:
                    if (!this._nameCopied) {
                        String name = this._parsingContext.getCurrentName();
                        int nameLen = name.length();
                        if (this._nameCopyBuffer == null) {
                            this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
                        } else if (this._nameCopyBuffer.length < nameLen) {
                            this._nameCopyBuffer = new char[nameLen];
                        }
                        name.getChars(0, nameLen, this._nameCopyBuffer, 0);
                        this._nameCopied = true;
                    }
                    return this._nameCopyBuffer;
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    return getNumberValue().toString().toCharArray();
                default:
                    return this._currToken.asCharArray();
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public int getTextLength() throws IOException {
        if (this._currToken != null) {
            if (this._tokenIncomplete) {
                _finishToken();
            }
            switch (this._currToken) {
                case VALUE_STRING:
                    return this._textBuffer.size();
                case FIELD_NAME:
                    return this._parsingContext.getCurrentName().length();
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    return getNumberValue().toString().length();
                default:
                    return this._currToken.asCharArray().length;
            }
        }
        return 0;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public int getTextOffset() throws IOException {
        return 0;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        if (this._tokenIncomplete) {
            _finishToken();
        }
        if (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) {
            _reportError("Current token (" + this._currToken + ") not VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        return this._binaryValue;
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException {
        _throwInternal();
        return null;
    }

    protected final JsonToken _handleFieldName() throws IOException {
        String name;
        String name2;
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i];
        this._typeByte = ch;
        switch ((ch >> 6) & 3) {
            case 0:
                switch (ch) {
                    case 32:
                        this._parsingContext.setCurrentName("");
                        return JsonToken.FIELD_NAME;
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        byte[] bArr2 = this._inputBuffer;
                        int i2 = this._inputPtr;
                        this._inputPtr = i2 + 1;
                        int index = ((ch & 3) << 8) + (bArr2[i2] & 255);
                        if (index >= this._seenNameCount) {
                            _reportInvalidSharedName(index);
                        }
                        this._parsingContext.setCurrentName(this._seenNames[index]);
                        return JsonToken.FIELD_NAME;
                    case 52:
                        _handleLongFieldName();
                        return JsonToken.FIELD_NAME;
                }
            case 1:
                int index2 = ch & 63;
                if (index2 >= this._seenNameCount) {
                    _reportInvalidSharedName(index2);
                }
                this._parsingContext.setCurrentName(this._seenNames[index2]);
                return JsonToken.FIELD_NAME;
            case 2:
                int len = (ch & 63) + 1;
                Name n = _findDecodedFromSymbols(len);
                if (n != null) {
                    name2 = n.getName();
                    this._inputPtr += len;
                } else {
                    String name3 = _decodeShortAsciiName(len);
                    name2 = _addDecodedToSymbols(len, name3);
                }
                if (this._seenNames != null) {
                    if (this._seenNameCount >= this._seenNames.length) {
                        this._seenNames = _expandSeenNames(this._seenNames);
                    }
                    String[] strArr = this._seenNames;
                    int i3 = this._seenNameCount;
                    this._seenNameCount = i3 + 1;
                    strArr[i3] = name2;
                }
                this._parsingContext.setCurrentName(name2);
                return JsonToken.FIELD_NAME;
            case 3:
                int len2 = ch & 63;
                if (len2 > 55) {
                    if (len2 == 59) {
                        if (!this._parsingContext.inObject()) {
                            _reportMismatchedEndMarker(Opcodes.LUSHR, ']');
                        }
                        this._parsingContext = this._parsingContext.getParent();
                        return JsonToken.END_OBJECT;
                    }
                } else {
                    int len3 = len2 + 2;
                    Name n2 = _findDecodedFromSymbols(len3);
                    if (n2 != null) {
                        name = n2.getName();
                        this._inputPtr += len3;
                    } else {
                        String name4 = _decodeShortUnicodeName(len3);
                        name = _addDecodedToSymbols(len3, name4);
                    }
                    if (this._seenNames != null) {
                        if (this._seenNameCount >= this._seenNames.length) {
                            this._seenNames = _expandSeenNames(this._seenNames);
                        }
                        String[] strArr2 = this._seenNames;
                        int i4 = this._seenNameCount;
                        this._seenNameCount = i4 + 1;
                        strArr2[i4] = name;
                    }
                    this._parsingContext.setCurrentName(name);
                    return JsonToken.FIELD_NAME;
                }
        }
        _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch) + " for expected field name (or END_OBJECT marker)");
        return null;
    }

    private final String[] _expandSeenNames(String[] oldShared) {
        int len = oldShared.length;
        if (len == 0) {
            String[] newShared = this._smileBufferRecycler.allocSeenNamesBuffer();
            if (newShared == null) {
                return new String[64];
            }
            return newShared;
        }
        if (len == 1024) {
            this._seenNameCount = 0;
            return oldShared;
        }
        int newSize = len == 64 ? 256 : 1024;
        String[] newShared2 = new String[newSize];
        System.arraycopy(oldShared, 0, newShared2, 0, oldShared.length);
        return newShared2;
    }

    private final String _addDecodedToSymbols(int len, String name) {
        if (len < 5) {
            return this._symbols.addName(name, this._quad1, 0).getName();
        }
        if (len < 9) {
            return this._symbols.addName(name, this._quad1, this._quad2).getName();
        }
        int qlen = (len + 3) >> 2;
        return this._symbols.addName(name, this._quadBuffer, qlen).getName();
    }

    /* JADX WARN: Code duplicated, block: B:14:0x006b A[PHI: r3 r7
      0x006b: PHI (r3v2 'inPtr' int) = (r3v1 'inPtr' int), (r3v3 'inPtr' int) binds: [B:6:0x003a, B:10:0x0052] A[DONT_GENERATE, DONT_INLINE]
      0x006b: PHI (r7v2 'outPtr' int) = (r7v1 'outPtr' int), (r7v3 'outPtr' int) binds: [B:6:0x003a, B:10:0x0052] A[DONT_GENERATE, DONT_INLINE]] */
    private final String _decodeShortAsciiName(int len) throws IOException {
        int inPtr;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        byte[] inBuf = this._inputBuffer;
        int inPtr2 = this._inputPtr;
        int inEnd = (inPtr2 + len) - 3;
        int inPtr3 = inPtr2;
        int outPtr = 0;
        while (inPtr3 < inEnd) {
            int outPtr2 = outPtr + 1;
            int inPtr4 = inPtr3 + 1;
            outBuf[outPtr] = (char) inBuf[inPtr3];
            int outPtr3 = outPtr2 + 1;
            int inPtr5 = inPtr4 + 1;
            outBuf[outPtr2] = (char) inBuf[inPtr4];
            int outPtr4 = outPtr3 + 1;
            int inPtr6 = inPtr5 + 1;
            outBuf[outPtr3] = (char) inBuf[inPtr5];
            outPtr = outPtr4 + 1;
            inPtr3 = inPtr6 + 1;
            outBuf[outPtr4] = (char) inBuf[inPtr6];
        }
        int left = len & 3;
        if (left > 0) {
            int outPtr5 = outPtr + 1;
            inPtr = inPtr3 + 1;
            outBuf[outPtr] = (char) inBuf[inPtr3];
            if (left > 1) {
                outPtr = outPtr5 + 1;
                inPtr3 = inPtr + 1;
                outBuf[outPtr5] = (char) inBuf[inPtr];
                if (left > 2) {
                    int i = outPtr + 1;
                    inPtr = inPtr3 + 1;
                    outBuf[outPtr] = (char) inBuf[inPtr3];
                } else {
                    inPtr = inPtr3;
                }
            }
        } else {
            inPtr = inPtr3;
        }
        this._inputPtr = inPtr;
        this._textBuffer.setCurrentLength(len);
        return this._textBuffer.contentsAsString();
    }

    private final String _decodeShortUnicodeName(int len) throws IOException {
        int outPtr;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int inPtr = this._inputPtr;
        this._inputPtr += len;
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inBuf = this._inputBuffer;
        int end = inPtr + len;
        int inPtr2 = inPtr;
        int outPtr2 = 0;
        while (inPtr2 < end) {
            int inPtr3 = inPtr2 + 1;
            int i = inBuf[inPtr2] & 255;
            int code = codes[i];
            if (code != 0) {
                switch (code) {
                    case 1:
                        i = ((i & 31) << 6) | (inBuf[inPtr3] & 63);
                        inPtr3++;
                        outPtr = outPtr2;
                        continue;
                    case 2:
                        int inPtr4 = inPtr3 + 1;
                        int i2 = ((i & 15) << 12) | ((inBuf[inPtr3] & 63) << 6);
                        inPtr3 = inPtr4 + 1;
                        i = i2 | (inBuf[inPtr4] & 63);
                        outPtr = outPtr2;
                        continue;
                    case 3:
                        int inPtr5 = inPtr3 + 1;
                        int i3 = ((i & 7) << 18) | ((inBuf[inPtr3] & 63) << 12);
                        int inPtr6 = inPtr5 + 1;
                        int i4 = ((i3 | ((inBuf[inPtr5] & 63) << 6)) | (inBuf[inPtr6] & 63)) - 65536;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) (55296 | (i4 >> 10));
                        i = 56320 | (i4 & 1023);
                        inPtr3 = inPtr6 + 1;
                        continue;
                    default:
                        _reportError("Invalid byte " + Integer.toHexString(i) + " in short Unicode text block");
                        break;
                }
                outPtr = outPtr2;
            } else {
                outPtr = outPtr2;
            }
            outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) i;
            inPtr2 = inPtr3;
        }
        this._textBuffer.setCurrentLength(outPtr2);
        return this._textBuffer.contentsAsString();
    }

    /* JADX WARN: Code duplicated, block: B:53:0x010b A[PHI: r4 r8
      0x010b: PHI (r4v2 'ch' int) = (r4v1 'ch' int), (r4v9 'ch' int) binds: [B:9:0x0032, B:31:0x00a5] A[DONT_GENERATE, DONT_INLINE]
      0x010b: PHI (r8v3 'ix' int) = (r8v2 'ix' int), (r8v6 'ix' int) binds: [B:9:0x0032, B:31:0x00a5] A[DONT_GENERATE, DONT_INLINE]] */
    private final Name _decodeLongUnicodeName(int[] quads, int byteLen, int quadLen) throws IOException {
        int lastQuad;
        int cix;
        int ch;
        int needed;
        int lastQuadBytes = byteLen & 3;
        if (lastQuadBytes < 4) {
            lastQuad = quads[quadLen - 1];
            quads[quadLen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
        } else {
            lastQuad = 0;
        }
        char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
        int ix = 0;
        int cix2 = 0;
        while (ix < byteLen) {
            int byteIx = ix & 3;
            int ch2 = (quads[ix >> 2] >> ((3 - byteIx) << 3)) & 255;
            ix++;
            if (ch2 <= 127) {
                cix = cix2;
            } else {
                if ((ch2 & 224) == 192) {
                    ch = ch2 & 31;
                    needed = 1;
                } else if ((ch2 & 240) == 224) {
                    ch = ch2 & 15;
                    needed = 2;
                } else if ((ch2 & Input.Keys.F5) == 240) {
                    ch = ch2 & 7;
                    needed = 3;
                } else {
                    _reportInvalidInitial(ch2);
                    ch = 1;
                    needed = 1;
                }
                if (ix + needed > byteLen) {
                    _reportInvalidEOF(" in long field name");
                }
                int byteIx2 = ix & 3;
                int ch3 = quads[ix >> 2] >> ((3 - byteIx2) << 3);
                ix++;
                if ((ch3 & 192) != 128) {
                    _reportInvalidOther(ch3);
                }
                ch2 = (ch << 6) | (ch3 & 63);
                if (needed > 1) {
                    int byteIx3 = ix & 3;
                    int ch4 = quads[ix >> 2] >> ((3 - byteIx3) << 3);
                    ix++;
                    if ((ch4 & 192) != 128) {
                        _reportInvalidOther(ch4);
                    }
                    ch2 = (ch2 << 6) | (ch4 & 63);
                    if (needed > 2) {
                        int byteIx4 = ix & 3;
                        int ch5 = quads[ix >> 2] >> ((3 - byteIx4) << 3);
                        ix++;
                        if ((ch5 & 192) != 128) {
                            _reportInvalidOther(ch5 & 255);
                        }
                        ch2 = (ch2 << 6) | (ch5 & 63);
                    }
                }
                if (needed > 2) {
                    int ch6 = ch2 - 65536;
                    if (cix2 >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    cix = cix2 + 1;
                    cbuf[cix2] = (char) (55296 + (ch6 >> 10));
                    ch2 = 56320 | (ch6 & 1023);
                } else {
                    cix = cix2;
                }
            }
            if (cix >= cbuf.length) {
                cbuf = this._textBuffer.expandCurrentSegment();
            }
            cix2 = cix + 1;
            cbuf[cix] = (char) ch2;
        }
        String baseName = new String(cbuf, 0, cix2);
        if (lastQuadBytes < 4) {
            quads[quadLen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, quadLen);
    }

    private final void _handleLongFieldName() throws IOException {
        int bytes;
        String name;
        byte[] inBuf = this._inputBuffer;
        int quads = 0;
        int q = 0;
        while (true) {
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            byte b = inBuf[i];
            if (-4 == b) {
                bytes = 0;
                break;
            }
            q = b & 255;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            byte b2 = inBuf[i2];
            if (-4 == b2) {
                bytes = 1;
                break;
            }
            q = (q << 8) | (b2 & 255);
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            byte b3 = inBuf[i3];
            if (-4 == b3) {
                bytes = 2;
                break;
            }
            q = (q << 8) | (b3 & 255);
            int i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            byte b4 = inBuf[i4];
            if (-4 == b4) {
                bytes = 3;
                break;
            }
            q = (q << 8) | (b4 & 255);
            if (quads >= this._quadBuffer.length) {
                this._quadBuffer = _growArrayTo(this._quadBuffer, this._quadBuffer.length + 256);
            }
            this._quadBuffer[quads] = q;
            quads++;
        }
        int byteLen = quads << 2;
        if (bytes > 0) {
            if (quads >= this._quadBuffer.length) {
                this._quadBuffer = _growArrayTo(this._quadBuffer, this._quadBuffer.length + 256);
            }
            this._quadBuffer[quads] = q;
            byteLen += bytes;
            quads++;
        }
        Name n = this._symbols.findName(this._quadBuffer, quads);
        if (n != null) {
            name = n.getName();
        } else {
            name = _decodeLongUnicodeName(this._quadBuffer, byteLen, quads).getName();
        }
        if (this._seenNames != null) {
            if (this._seenNameCount >= this._seenNames.length) {
                this._seenNames = _expandSeenNames(this._seenNames);
            }
            String[] strArr = this._seenNames;
            int i5 = this._seenNameCount;
            this._seenNameCount = i5 + 1;
            strArr[i5] = name;
        }
        this._parsingContext.setCurrentName(name);
    }

    /* JADX WARN: Code duplicated, block: B:27:0x00af A[PHI: r2 r5
      0x00af: PHI (r2v3 'inPtr' int) = (r2v2 'inPtr' int), (r2v4 'inPtr' int) binds: [B:19:0x0077, B:23:0x0093] A[DONT_GENERATE, DONT_INLINE]
      0x00af: PHI (r5v1 'q2' int) = (r5v0 'q2' int), (r5v4 'q2' int) binds: [B:19:0x0077, B:23:0x0093] A[DONT_GENERATE, DONT_INLINE]] */
    private final Name _findDecodedFromSymbols(int len) throws IOException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        if (len < 5) {
            int inPtr = this._inputPtr;
            byte[] inBuf = this._inputBuffer;
            int q = inBuf[inPtr] & 255;
            int len2 = len - 1;
            if (len2 > 0) {
                int inPtr2 = inPtr + 1;
                q = (q << 8) + (inBuf[inPtr2] & 255);
                int len3 = len2 - 1;
                if (len3 > 0) {
                    int inPtr3 = inPtr2 + 1;
                    q = (q << 8) + (inBuf[inPtr3] & 255);
                    if (len3 - 1 > 0) {
                        q = (q << 8) + (inBuf[inPtr3 + 1] & 255);
                    }
                }
            }
            this._quad1 = q;
            return this._symbols.findName(q);
        }
        if (len < 9) {
            int inPtr4 = this._inputPtr;
            byte[] inBuf2 = this._inputBuffer;
            int inPtr5 = inPtr4 + 1;
            int q1 = inBuf2[inPtr4] << 8;
            int inPtr6 = inPtr5 + 1;
            int q2 = (q1 + (inBuf2[inPtr5] & 255)) << 8;
            int inPtr7 = inPtr6 + 1;
            int q3 = (q2 + (inBuf2[inPtr6] & 255)) << 8;
            int inPtr8 = inPtr7 + 1;
            int q4 = q3 + (inBuf2[inPtr7] & 255);
            int inPtr9 = inPtr8 + 1;
            int q5 = inBuf2[inPtr8] & 255;
            int len4 = len - 5;
            if (len4 > 0) {
                int inPtr10 = inPtr9 + 1;
                q5 = (q5 << 8) + (inBuf2[inPtr9] & 255);
                int len5 = len4 - 1;
                if (len5 >= 0) {
                    inPtr9 = inPtr10 + 1;
                    q5 = (q5 << 8) + (inBuf2[inPtr10] & 255);
                    if (len5 - 1 >= 0) {
                        int i = inPtr9 + 1;
                        q5 = (q5 << 8) + (inBuf2[inPtr9] & 255);
                    }
                }
            }
            this._quad1 = q4;
            this._quad2 = q5;
            return this._symbols.findName(q4, q5);
        }
        return _findDecodedMedium(len);
    }

    private final Name _findDecodedMedium(int len) throws IOException {
        int offset;
        int offset2;
        int bufLen = (len + 3) >> 2;
        if (bufLen > this._quadBuffer.length) {
            this._quadBuffer = _growArrayTo(this._quadBuffer, bufLen);
        }
        int offset3 = 0;
        int inPtr = this._inputPtr;
        byte[] inBuf = this._inputBuffer;
        while (true) {
            int inPtr2 = inPtr + 1;
            int q = (inBuf[inPtr] & 255) << 8;
            int inPtr3 = inPtr2 + 1;
            int q2 = (q | (inBuf[inPtr2] & 255)) << 8;
            int inPtr4 = inPtr3 + 1;
            int q3 = (q2 | (inBuf[inPtr3] & 255)) << 8;
            inPtr = inPtr4 + 1;
            offset = offset3 + 1;
            this._quadBuffer[offset3] = q3 | (inBuf[inPtr4] & 255);
            len -= 4;
            if (len <= 3) {
                break;
            }
            offset3 = offset;
        }
        if (len > 0) {
            int inPtr5 = inPtr + 1;
            int q4 = inBuf[inPtr] & 255;
            int len2 = len - 1;
            if (len2 >= 0) {
                int inPtr6 = inPtr5 + 1;
                q4 = (q4 << 8) + (inBuf[inPtr5] & 255);
                if (len2 - 1 >= 0) {
                    int i = inPtr6 + 1;
                    q4 = (q4 << 8) + (inBuf[inPtr6] & 255);
                }
            }
            offset2 = offset + 1;
            this._quadBuffer[offset] = q4;
        } else {
            offset2 = offset;
        }
        return this._symbols.findName(this._quadBuffer, offset2);
    }

    private static int[] _growArrayTo(int[] arr, int minSize) {
        int[] newArray = new int[minSize + 4];
        if (arr != null) {
            System.arraycopy(arr, 0, newArray, 0, arr.length);
        }
        return newArray;
    }

    @Override // org.codehaus.jackson.impl.JsonNumericParserBase
    protected void _parseNumericValue(int expType) throws IOException {
        if (this._tokenIncomplete) {
            int tb = this._typeByte;
            if (((tb >> 5) & 7) != 1) {
                _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
            }
            this._tokenIncomplete = false;
            _finishNumberToken(tb);
        }
    }

    protected void _finishToken() throws IOException {
        this._tokenIncomplete = false;
        int tb = this._typeByte;
        int type = (tb >> 5) & 7;
        if (type == 1) {
            _finishNumberToken(tb);
            return;
        }
        if (type <= 3) {
            _decodeShortAsciiValue((tb & 63) + 1);
            return;
        }
        if (type <= 5) {
            _decodeShortUnicodeValue((tb & 63) + 2);
            return;
        }
        if (type == 7) {
            switch ((tb & 31) >> 2) {
                case 0:
                    _decodeLongAscii();
                    break;
                case 1:
                    _decodeLongUnicode();
                    break;
                case 2:
                    this._binaryValue = _read7BitBinaryWithLength();
                    break;
                case 7:
                    _finishRawBinary();
                    break;
            }
            return;
        }
        _throwInternal();
    }

    protected final void _finishNumberToken(int tb) throws IOException {
        int tb2 = tb & 31;
        int type = tb2 >> 2;
        if (type == 1) {
            int subtype = tb2 & 3;
            if (subtype == 0) {
                _finishInt();
                return;
            }
            if (subtype == 1) {
                _finishLong();
                return;
            } else if (subtype == 2) {
                _finishBigInteger();
                return;
            } else {
                _throwInternal();
                return;
            }
        }
        if (type == 2) {
            switch (tb2 & 3) {
                case 0:
                    _finishFloat();
                    break;
                case 1:
                    _finishDouble();
                    break;
                case 2:
                    _finishBigDecimal();
                    break;
            }
            return;
        }
        _throwInternal();
    }

    private final void _finishInt() throws IOException {
        int value;
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i];
        if (i2 < 0) {
            value = i2 & 63;
        } else {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr2 = this._inputBuffer;
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            byte i4 = bArr2[i3];
            if (i4 >= 0) {
                i2 = (i2 << 7) + i4;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr3 = this._inputBuffer;
                int i5 = this._inputPtr;
                this._inputPtr = i5 + 1;
                i4 = bArr3[i5];
                if (i4 >= 0) {
                    i2 = (i2 << 7) + i4;
                    if (this._inputPtr >= this._inputEnd) {
                        loadMoreGuaranteed();
                    }
                    byte[] bArr4 = this._inputBuffer;
                    int i6 = this._inputPtr;
                    this._inputPtr = i6 + 1;
                    i4 = bArr4[i6];
                    if (i4 >= 0) {
                        i2 = (i2 << 7) + i4;
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        byte[] bArr5 = this._inputBuffer;
                        int i7 = this._inputPtr;
                        this._inputPtr = i7 + 1;
                        i4 = bArr5[i7];
                        if (i4 >= 0) {
                            _reportError("Corrupt input; 32-bit VInt extends beyond 5 data bytes");
                        }
                    }
                }
            }
            value = (i2 << 6) + (i4 & 63);
        }
        this._numberInt = SmileUtil.zigzagDecode(value);
        this._numTypesValid = 1;
    }

    private final void _finishLong() throws IOException {
        long l = _fourBytesToInt();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int value = bArr[i];
            if (value < 0) {
                this._numberLong = SmileUtil.zigzagDecode((l << 6) + ((long) (value & 63)));
                this._numTypesValid = 2;
                return;
            }
            l = (l << 7) + ((long) value);
        }
    }

    private final void _finishBigInteger() throws IOException {
        byte[] raw = _read7BitBinaryWithLength();
        this._numberBigInt = new BigInteger(raw);
        this._numTypesValid = 4;
    }

    private final void _finishFloat() throws IOException {
        int i = _fourBytesToInt();
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        float f = Float.intBitsToFloat((i << 7) + bArr[i2]);
        this._numberDouble = f;
        this._numTypesValid = 8;
    }

    private final void _finishDouble() throws IOException {
        long hi = _fourBytesToInt();
        long value = (hi << 28) + ((long) _fourBytesToInt());
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        long value2 = (value << 7) + ((long) bArr[i]);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        this._numberDouble = Double.longBitsToDouble((value2 << 7) + ((long) bArr2[i2]));
        this._numTypesValid = 8;
    }

    private final int _fourBytesToInt() throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        int i4 = (i2 << 7) + bArr2[i3];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr3 = this._inputBuffer;
        int i5 = this._inputPtr;
        this._inputPtr = i5 + 1;
        int i6 = (i4 << 7) + bArr3[i5];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr4 = this._inputBuffer;
        int i7 = this._inputPtr;
        this._inputPtr = i7 + 1;
        return (i6 << 7) + bArr4[i7];
    }

    private final void _finishBigDecimal() throws IOException {
        int scale = SmileUtil.zigzagDecode(_readUnsignedVInt());
        byte[] raw = _read7BitBinaryWithLength();
        this._numberBigDecimal = new BigDecimal(new BigInteger(raw), scale);
        this._numTypesValid = 16;
    }

    private final int _readUnsignedVInt() throws IOException {
        int value = 0;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i];
            if (i2 < 0) {
                return (value << 6) + (i2 & 63);
            }
            value = (value << 7) + i2;
        }
    }

    private final byte[] _read7BitBinaryWithLength() throws IOException {
        int byteLen = _readUnsignedVInt();
        byte[] result = new byte[byteLen];
        int lastOkPtr = byteLen - 7;
        int ptr = 0;
        while (ptr <= lastOkPtr) {
            if (this._inputEnd - this._inputPtr < 8) {
                _loadToHaveAtLeast(8);
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] << 25;
            byte[] bArr2 = this._inputBuffer;
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            int i4 = i2 + (bArr2[i3] << 18);
            byte[] bArr3 = this._inputBuffer;
            int i5 = this._inputPtr;
            this._inputPtr = i5 + 1;
            int i6 = i4 + (bArr3[i5] << 11);
            byte[] bArr4 = this._inputBuffer;
            int i7 = this._inputPtr;
            this._inputPtr = i7 + 1;
            int i1 = i6 + (bArr4[i7] << 4);
            byte[] bArr5 = this._inputBuffer;
            int i8 = this._inputPtr;
            this._inputPtr = i8 + 1;
            byte x = bArr5[i8];
            int i9 = i1 + (x >> 3);
            byte[] bArr6 = this._inputBuffer;
            int i10 = this._inputPtr;
            this._inputPtr = i10 + 1;
            int i11 = ((x & 7) << 21) + (bArr6[i10] << 14);
            byte[] bArr7 = this._inputBuffer;
            int i12 = this._inputPtr;
            this._inputPtr = i12 + 1;
            int i13 = i11 + (bArr7[i12] << 7);
            byte[] bArr8 = this._inputBuffer;
            int i14 = this._inputPtr;
            this._inputPtr = i14 + 1;
            int i15 = i13 + bArr8[i14];
            int ptr2 = ptr + 1;
            result[ptr] = (byte) (i9 >> 24);
            int ptr3 = ptr2 + 1;
            result[ptr2] = (byte) (i9 >> 16);
            int ptr4 = ptr3 + 1;
            result[ptr3] = (byte) (i9 >> 8);
            int ptr5 = ptr4 + 1;
            result[ptr4] = (byte) i9;
            int ptr6 = ptr5 + 1;
            result[ptr5] = (byte) (i15 >> 16);
            int ptr7 = ptr6 + 1;
            result[ptr6] = (byte) (i15 >> 8);
            result[ptr7] = (byte) i15;
            ptr = ptr7 + 1;
        }
        int toDecode = result.length - ptr;
        if (toDecode > 0) {
            if (this._inputEnd - this._inputPtr < toDecode + 1) {
                _loadToHaveAtLeast(toDecode + 1);
            }
            byte[] bArr9 = this._inputBuffer;
            int i16 = this._inputPtr;
            this._inputPtr = i16 + 1;
            int value = bArr9[i16];
            int i17 = 1;
            while (i17 < toDecode) {
                byte[] bArr10 = this._inputBuffer;
                int i18 = this._inputPtr;
                this._inputPtr = i18 + 1;
                value = (value << 7) + bArr10[i18];
                result[ptr] = (byte) (value >> (7 - i17));
                i17++;
                ptr++;
            }
            byte[] bArr11 = this._inputBuffer;
            int i19 = this._inputPtr;
            this._inputPtr = i19 + 1;
            result[ptr] = (byte) (bArr11[i19] + (value << toDecode));
        }
        return result;
    }

    protected final void _decodeShortAsciiValue(int len) throws IOException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        byte[] inBuf = this._inputBuffer;
        int inPtr = this._inputPtr;
        int end = inPtr + len;
        int outPtr = 0;
        while (inPtr < end) {
            outBuf[outPtr] = (char) inBuf[inPtr];
            inPtr++;
            outPtr++;
        }
        this._inputPtr = inPtr;
        this._textBuffer.setCurrentLength(len);
    }

    protected final void _decodeShortUnicodeValue(int len) throws IOException {
        int outPtr;
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int inPtr = this._inputPtr;
        this._inputPtr += len;
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inputBuf = this._inputBuffer;
        int end = inPtr + len;
        int inPtr2 = inPtr;
        int outPtr2 = 0;
        while (inPtr2 < end) {
            int inPtr3 = inPtr2 + 1;
            int i = inputBuf[inPtr2] & 255;
            int code = codes[i];
            if (code != 0) {
                switch (code) {
                    case 1:
                        i = ((i & 31) << 6) | (inputBuf[inPtr3] & 63);
                        inPtr3++;
                        outPtr = outPtr2;
                        continue;
                    case 2:
                        int inPtr4 = inPtr3 + 1;
                        int i2 = ((i & 15) << 12) | ((inputBuf[inPtr3] & 63) << 6);
                        inPtr3 = inPtr4 + 1;
                        i = i2 | (inputBuf[inPtr4] & 63);
                        outPtr = outPtr2;
                        continue;
                    case 3:
                        int inPtr5 = inPtr3 + 1;
                        int i3 = ((i & 7) << 18) | ((inputBuf[inPtr3] & 63) << 12);
                        int inPtr6 = inPtr5 + 1;
                        int i4 = ((i3 | ((inputBuf[inPtr5] & 63) << 6)) | (inputBuf[inPtr6] & 63)) - 65536;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) (55296 | (i4 >> 10));
                        i = 56320 | (i4 & 1023);
                        inPtr3 = inPtr6 + 1;
                        continue;
                    default:
                        _reportError("Invalid byte " + Integer.toHexString(i) + " in short Unicode text block");
                        break;
                }
                outPtr = outPtr2;
            } else {
                outPtr = outPtr2;
            }
            outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) i;
            inPtr2 = inPtr3;
        }
        this._textBuffer.setCurrentLength(outPtr2);
    }

    private final void _decodeLongAscii() throws IOException {
        int inPtr;
        int outPtr;
        int outPtr2 = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            int inPtr2 = this._inputPtr;
            int left = this._inputEnd - inPtr2;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr2 = 0;
            }
            int left2 = Math.min(left, outBuf.length - outPtr2);
            while (true) {
                inPtr = inPtr2 + 1;
                byte b = this._inputBuffer[inPtr2];
                if (b == -4) {
                    this._inputPtr = inPtr;
                    this._textBuffer.setCurrentLength(outPtr2);
                    return;
                }
                outPtr = outPtr2 + 1;
                outBuf[outPtr2] = (char) b;
                left2--;
                if (left2 <= 0) {
                    break;
                }
                inPtr2 = inPtr;
                outPtr2 = outPtr;
            }
            this._inputPtr = inPtr;
            outPtr2 = outPtr;
        }
    }

    private final void _decodeLongUnicode() throws IOException {
        int outPtr;
        int outPtr2 = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr = this._inputPtr;
            if (ptr >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr = this._inputPtr;
            }
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr2 = 0;
            }
            int max = this._inputEnd;
            int max2 = ptr + (outBuf.length - outPtr2);
            if (max2 < max) {
                max = max2;
            }
            while (true) {
                if (ptr < max) {
                    int ptr2 = ptr + 1;
                    int c = inputBuffer[ptr] & 255;
                    if (codes[c] != 0) {
                        this._inputPtr = ptr2;
                        if (c != 252) {
                            switch (codes[c]) {
                                case 1:
                                    c = _decodeUtf8_2(c);
                                    outPtr = outPtr2;
                                    break;
                                case 2:
                                    if (this._inputEnd - this._inputPtr >= 2) {
                                        c = _decodeUtf8_3fast(c);
                                        outPtr = outPtr2;
                                    } else {
                                        c = _decodeUtf8_3(c);
                                        outPtr = outPtr2;
                                    }
                                    break;
                                case 3:
                                default:
                                    _reportInvalidChar(c);
                                    outPtr = outPtr2;
                                    break;
                                case 4:
                                    int c2 = _decodeUtf8_4(c);
                                    outPtr = outPtr2 + 1;
                                    outBuf[outPtr2] = (char) (55296 | (c2 >> 10));
                                    if (outPtr >= outBuf.length) {
                                        outBuf = this._textBuffer.finishCurrentSegment();
                                        outPtr = 0;
                                    }
                                    c = 56320 | (c2 & 1023);
                                    break;
                            }
                            if (outPtr >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr = 0;
                            }
                            outBuf[outPtr] = (char) c;
                            outPtr2 = outPtr + 1;
                            break;
                        }
                        this._textBuffer.setCurrentLength(outPtr2);
                        return;
                    }
                    outBuf[outPtr2] = (char) c;
                    ptr = ptr2;
                    outPtr2++;
                } else {
                    this._inputPtr = ptr;
                    outPtr2 = outPtr2;
                    break;
                }
            }
        }
    }

    private final void _finishRawBinary() throws IOException {
        int byteLen = _readUnsignedVInt();
        this._binaryValue = new byte[byteLen];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int ptr = 0;
        while (true) {
            int toAdd = Math.min(byteLen, this._inputEnd - this._inputPtr);
            System.arraycopy(this._inputBuffer, this._inputPtr, this._binaryValue, ptr, toAdd);
            this._inputPtr += toAdd;
            ptr += toAdd;
            byteLen -= toAdd;
            if (byteLen <= 0) {
                return;
            } else {
                loadMoreGuaranteed();
            }
        }
    }

    protected void _skipIncomplete() throws IOException {
        this._tokenIncomplete = false;
        int tb = this._typeByte;
        switch ((tb >> 5) & 7) {
            case 1:
                int tb2 = tb & 31;
                switch (tb2 >> 2) {
                    case 1:
                        switch (tb2 & 3) {
                            case 1:
                                _skipBytes(4);
                                break;
                            case 2:
                                _skip7BitBinary();
                                return;
                        }
                        while (true) {
                            int end = this._inputEnd;
                            byte[] buf = this._inputBuffer;
                            while (this._inputPtr < end) {
                                int i = this._inputPtr;
                                this._inputPtr = i + 1;
                                if (buf[i] < 0) {
                                    return;
                                }
                            }
                            loadMoreGuaranteed();
                        }
                        break;
                    case 2:
                        switch (tb2 & 3) {
                            case 0:
                                _skipBytes(5);
                                break;
                            case 1:
                                _skipBytes(10);
                                break;
                            case 2:
                                _readUnsignedVInt();
                                _skip7BitBinary();
                                break;
                        }
                }
                break;
            case 2:
            case 3:
                _skipBytes((tb & 63) + 1);
                return;
            case 4:
            case 5:
                _skipBytes((tb & 63) + 2);
                return;
            case 7:
                switch ((tb & 31) >> 2) {
                    case 2:
                        _skip7BitBinary();
                        return;
                    case 7:
                        _skipBytes(_readUnsignedVInt());
                        return;
                }
                while (true) {
                    int end2 = this._inputEnd;
                    byte[] buf2 = this._inputBuffer;
                    while (this._inputPtr < end2) {
                        int i2 = this._inputPtr;
                        this._inputPtr = i2 + 1;
                        if (buf2[i2] == -4) {
                            return;
                        }
                    }
                    loadMoreGuaranteed();
                }
                break;
        }
        _throwInternal();
    }

    protected void _skipBytes(int len) throws IOException {
        while (true) {
            int toAdd = Math.min(len, this._inputEnd - this._inputPtr);
            this._inputPtr += toAdd;
            len -= toAdd;
            if (len <= 0) {
                return;
            } else {
                loadMoreGuaranteed();
            }
        }
    }

    protected void _skip7BitBinary() throws IOException {
        int origBytes = _readUnsignedVInt();
        int chunks = origBytes / 7;
        int encBytes = chunks * 8;
        int origBytes2 = origBytes - (chunks * 7);
        if (origBytes2 > 0) {
            encBytes += origBytes2 + 1;
        }
        _skipBytes(encBytes);
    }

    private final int _decodeUtf8_2(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        return ((c & 31) << 6) | (d & 63);
    }

    private final int _decodeUtf8_3(int c1) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int c2 = c1 & 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        int c = (c2 << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int d2 = bArr2[i2];
        if ((d2 & 192) != 128) {
            _reportInvalidOther(d2 & 255, this._inputPtr);
        }
        return (c << 6) | (d2 & 63);
    }

    private final int _decodeUtf8_3fast(int c1) throws IOException {
        int c2 = c1 & 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        int c = (c2 << 6) | (d & 63);
        byte[] bArr2 = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int d2 = bArr2[i2];
        if ((d2 & 192) != 128) {
            _reportInvalidOther(d2 & 255, this._inputPtr);
        }
        return (c << 6) | (d2 & 63);
    }

    private final int _decodeUtf8_4(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        int c2 = ((c & 7) << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int d2 = bArr2[i2];
        if ((d2 & 192) != 128) {
            _reportInvalidOther(d2 & 255, this._inputPtr);
        }
        int c3 = (c2 << 6) | (d2 & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr3 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        int d3 = bArr3[i3];
        if ((d3 & 192) != 128) {
            _reportInvalidOther(d3 & 255, this._inputPtr);
        }
        return ((c3 << 6) | (d3 & 63)) - 65536;
    }

    protected void _reportInvalidSharedName(int index) throws IOException {
        if (this._seenNames == null) {
            _reportError("Encountered shared name reference, even though document header explicitly declared no shared name references are included");
        }
        _reportError("Invalid shared name reference " + index + "; only got " + this._seenNameCount + " names in buffer (invalid content)");
    }

    protected void _reportInvalidSharedStringValue(int index) throws IOException {
        if (this._seenStringValues == null) {
            _reportError("Encountered shared text value reference, even though document header did not declared shared text value references may be included");
        }
        _reportError("Invalid shared text value reference " + index + "; only got " + this._seenStringValueCount + " names in buffer (invalid content)");
    }

    protected void _reportInvalidChar(int c) throws JsonParseException {
        if (c < 32) {
            _throwInvalidSpace(c);
        }
        _reportInvalidInitial(c);
    }

    protected void _reportInvalidInitial(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask, int ptr) throws JsonParseException {
        this._inputPtr = ptr;
        _reportInvalidOther(mask);
    }
}
