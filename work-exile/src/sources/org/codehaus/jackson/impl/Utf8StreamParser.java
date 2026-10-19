package org.codehaus.jackson.impl;

import com.badlogic.gdx.Input;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Utf8StreamParser extends StreamBasedParserBase {
    static final byte BYTE_LF = 10;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;
    private static final int[] sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
    private static final int[] sInputCodesLatin1 = CharTypes.getInputCodeLatin1();

    public Utf8StreamParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, BytesToNameCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features, in, inputBuffer, start, end, bufferRecyclable);
        this._quadBuffer = new int[16];
        this._tokenIncomplete = false;
        this._objectCodec = codec;
        this._symbols = sym;
        if (!JsonParser.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features)) {
            _throwInternal();
        }
    }

    @Override // org.codehaus.jackson.JsonParser
    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    @Override // org.codehaus.jackson.JsonParser
    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public String getText() throws IOException {
        JsonToken t = this._currToken;
        if (t != JsonToken.VALUE_STRING) {
            return _getText2(t);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    protected final String _getText2(JsonToken t) {
        if (t == null) {
            return null;
        }
        switch (t) {
            case FIELD_NAME:
                return this._parsingContext.getCurrentName();
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                return this._textBuffer.contentsAsString();
            default:
                return t.asString();
        }
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public char[] getTextCharacters() throws IOException {
        if (this._currToken != null) {
            switch (this._currToken) {
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
                case VALUE_STRING:
                    if (this._tokenIncomplete) {
                        this._tokenIncomplete = false;
                        _finishString();
                    }
                    break;
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    break;
                default:
                    return this._currToken.asCharArray();
            }
            return this._textBuffer.getTextBuffer();
        }
        return null;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public int getTextLength() throws IOException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken) {
            case FIELD_NAME:
                return this._parsingContext.getCurrentName().length();
            case VALUE_STRING:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                break;
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                break;
            default:
                return this._currToken.asCharArray().length;
        }
        return this._textBuffer.size();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public int getTextOffset() throws IOException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken) {
            case FIELD_NAME:
            default:
                return 0;
            case VALUE_STRING:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                break;
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                break;
        }
        return this._textBuffer.getTextOffset();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        if (this._tokenIncomplete) {
            try {
                this._binaryValue = _decodeBase64(b64variant);
                this._tokenIncomplete = false;
            } catch (IllegalArgumentException iae) {
                throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
            }
        }
        return this._binaryValue;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public JsonToken nextToken() throws IOException {
        JsonToken t;
        if (this._currToken == JsonToken.FIELD_NAME) {
            return _nextAfterName();
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            this._currToken = null;
            return null;
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
        this._binaryValue = null;
        if (i == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(i, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            JsonToken jsonToken = JsonToken.END_ARRAY;
            this._currToken = jsonToken;
            return jsonToken;
        }
        if (i == 125) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(i, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            JsonToken jsonToken2 = JsonToken.END_OBJECT;
            this._currToken = jsonToken2;
            return jsonToken2;
        }
        if (this._parsingContext.expectComma()) {
            if (i != 44) {
                _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
            }
            i = _skipWS();
        }
        if (!this._parsingContext.inObject()) {
            return _nextTokenNotInObject(i);
        }
        Name n = _parseFieldName(i);
        this._parsingContext.setCurrentName(n.getName());
        this._currToken = JsonToken.FIELD_NAME;
        int i2 = _skipWS();
        if (i2 != 58) {
            _reportUnexpectedChar(i2, "was expecting a colon to separate field name and value");
        }
        int i3 = _skipWS();
        if (i3 == 34) {
            this._tokenIncomplete = true;
            this._nextToken = JsonToken.VALUE_STRING;
            return this._currToken;
        }
        switch (i3) {
            case Input.Keys.Q /* 45 */:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                t = parseNumberText(i3);
                break;
            case 91:
                t = JsonToken.START_ARRAY;
                break;
            case 93:
            case Opcodes.LUSHR /* 125 */:
                _reportUnexpectedChar(i3, "expected a value");
                _matchToken(JsonToken.VALUE_TRUE);
                t = JsonToken.VALUE_TRUE;
                break;
            case 102:
                _matchToken(JsonToken.VALUE_FALSE);
                t = JsonToken.VALUE_FALSE;
                break;
            case 110:
                _matchToken(JsonToken.VALUE_NULL);
                t = JsonToken.VALUE_NULL;
                break;
            case Opcodes.INEG /* 116 */:
                _matchToken(JsonToken.VALUE_TRUE);
                t = JsonToken.VALUE_TRUE;
                break;
            case Opcodes.LSHR /* 123 */:
                t = JsonToken.START_OBJECT;
                break;
            default:
                t = _handleUnexpectedValue(i3);
                break;
        }
        this._nextToken = t;
        return this._currToken;
    }

    private final JsonToken _nextTokenNotInObject(int i) throws IOException {
        if (i == 34) {
            this._tokenIncomplete = true;
            JsonToken jsonToken = JsonToken.VALUE_STRING;
            this._currToken = jsonToken;
            return jsonToken;
        }
        switch (i) {
            case Input.Keys.Q /* 45 */:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                JsonToken numberText = parseNumberText(i);
                this._currToken = numberText;
                return numberText;
            case 91:
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                JsonToken jsonToken2 = JsonToken.START_ARRAY;
                this._currToken = jsonToken2;
                return jsonToken2;
            case 93:
            case Opcodes.LUSHR /* 125 */:
                _reportUnexpectedChar(i, "expected a value");
                break;
            case 102:
                _matchToken(JsonToken.VALUE_FALSE);
                JsonToken jsonToken3 = JsonToken.VALUE_FALSE;
                this._currToken = jsonToken3;
                return jsonToken3;
            case 110:
                _matchToken(JsonToken.VALUE_NULL);
                JsonToken jsonToken4 = JsonToken.VALUE_NULL;
                this._currToken = jsonToken4;
                return jsonToken4;
            case Opcodes.INEG /* 116 */:
                break;
            case Opcodes.LSHR /* 123 */:
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                JsonToken jsonToken5 = JsonToken.START_OBJECT;
                this._currToken = jsonToken5;
                return jsonToken5;
            default:
                JsonToken jsonToken_handleUnexpectedValue = _handleUnexpectedValue(i);
                this._currToken = jsonToken_handleUnexpectedValue;
                return jsonToken_handleUnexpectedValue;
        }
        _matchToken(JsonToken.VALUE_TRUE);
        JsonToken jsonToken6 = JsonToken.VALUE_TRUE;
        this._currToken = jsonToken6;
        return jsonToken6;
    }

    private final JsonToken _nextAfterName() {
        this._nameCopied = false;
        JsonToken t = this._nextToken;
        this._nextToken = null;
        if (t == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        } else if (t == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        this._currToken = t;
        return t;
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase, org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    protected final JsonToken parseNumberText(int c) throws IOException {
        int outPtr;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        boolean negative = c == 45;
        if (!negative) {
            outPtr = 0;
        } else {
            outPtr = 0 + 1;
            outBuf[0] = SignatureVisitor.SUPER;
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            c = bArr[i] & 255;
            if (c < 48 || c > 57) {
                return _handleInvalidNumberStart(c, true);
            }
        }
        if (c == 48) {
            c = _verifyNoLeadingZeroes();
        }
        int outPtr2 = outPtr + 1;
        outBuf[outPtr] = (char) c;
        int intLen = 1;
        int end = this._inputPtr + outBuf.length;
        if (end > this._inputEnd) {
            end = this._inputEnd;
        }
        while (this._inputPtr < end) {
            byte[] bArr2 = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            int c2 = bArr2[i2] & 255;
            if (c2 >= 48 && c2 <= 57) {
                intLen++;
                outBuf[outPtr2] = (char) c2;
                outPtr2++;
            } else {
                if (c2 == 46 || c2 == 101 || c2 == 69) {
                    return _parseFloatText(outBuf, outPtr2, c2, negative, intLen);
                }
                this._inputPtr--;
                this._textBuffer.setCurrentLength(outPtr2);
                return resetInt(negative, intLen);
            }
        }
        return _parserNumber2(outBuf, outPtr2, negative, intLen);
    }

    private final JsonToken _parserNumber2(char[] outBuf, int outPtr, boolean negative, int intPartLength) throws IOException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                this._textBuffer.setCurrentLength(outPtr);
                return resetInt(negative, intPartLength);
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int c = bArr[i] & 255;
            if (c > 57 || c < 48) {
                if (c == 46 || c == 101 || c == 69) {
                    return _parseFloatText(outBuf, outPtr, c, negative, intPartLength);
                }
                this._inputPtr--;
                this._textBuffer.setCurrentLength(outPtr);
                return resetInt(negative, intPartLength);
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outBuf[outPtr] = (char) c;
            intPartLength++;
            outPtr++;
        }
    }

    private final int _verifyNoLeadingZeroes() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return 48;
        }
        int ch = this._inputBuffer[this._inputPtr] & 255;
        if (ch < 48 || ch > 57) {
            return 48;
        }
        if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        this._inputPtr++;
        if (ch == 48) {
            do {
                if (this._inputPtr < this._inputEnd || loadMore()) {
                    ch = this._inputBuffer[this._inputPtr] & 255;
                    if (ch < 48 || ch > 57) {
                        return 48;
                    }
                    this._inputPtr++;
                } else {
                    return ch;
                }
            } while (ch == 48);
            return ch;
        }
        return ch;
    }

    private final JsonToken _parseFloatText(char[] outBuf, int outPtr, int c, boolean negative, int integerPartLength) throws IOException {
        int outPtr2;
        int fractLen = 0;
        boolean eof = false;
        if (c == 46) {
            int outPtr3 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            while (true) {
                outPtr = outPtr3;
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    eof = true;
                    break;
                }
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                c = bArr[i] & 255;
                if (c < 48 || c > 57) {
                    break;
                }
                fractLen++;
                if (outPtr >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr = 0;
                }
                outPtr3 = outPtr + 1;
                outBuf[outPtr] = (char) c;
            }
            if (fractLen == 0) {
                reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
            }
        }
        int expLen = 0;
        if (c == 101 || c == 69) {
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr4 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr2 = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            int c2 = bArr2[i2] & 255;
            if (c2 == 45 || c2 == 43) {
                if (outPtr4 >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr2 = 0;
                } else {
                    outPtr2 = outPtr4;
                }
                int outPtr5 = outPtr2 + 1;
                outBuf[outPtr2] = (char) c2;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr3 = this._inputBuffer;
                int i3 = this._inputPtr;
                this._inputPtr = i3 + 1;
                c2 = bArr3[i3] & 255;
                outPtr = outPtr5;
            } else {
                outPtr = outPtr4;
            }
            while (c2 <= 57 && c2 >= 48) {
                expLen++;
                if (outPtr >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr = 0;
                }
                int outPtr6 = outPtr + 1;
                outBuf[outPtr] = (char) c2;
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    eof = true;
                    outPtr = outPtr6;
                    break;
                }
                byte[] bArr4 = this._inputBuffer;
                int i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                c2 = bArr4[i4] & 255;
                outPtr = outPtr6;
            }
            if (expLen == 0) {
                reportUnexpectedNumberChar(c2, "Exponent indicator not followed by a digit");
            }
        }
        if (!eof) {
            this._inputPtr--;
        }
        this._textBuffer.setCurrentLength(outPtr);
        return resetFloat(negative, integerPartLength, fractLen, expLen);
    }

    protected final Name _parseFieldName(int i) throws IOException {
        if (i != 34) {
            return _handleUnusualFieldName(i);
        }
        if (this._inputPtr + 9 > this._inputEnd) {
            return slowParseFieldName();
        }
        byte[] input = this._inputBuffer;
        int[] codes = sInputCodesLatin1;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int q = input[i2] & 255;
        if (codes[q] == 0) {
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            int i4 = input[i3] & 255;
            if (codes[i4] == 0) {
                int q2 = (q << 8) | i4;
                int i5 = this._inputPtr;
                this._inputPtr = i5 + 1;
                int i6 = input[i5] & 255;
                if (codes[i6] == 0) {
                    int q3 = (q2 << 8) | i6;
                    int i7 = this._inputPtr;
                    this._inputPtr = i7 + 1;
                    int i8 = input[i7] & 255;
                    if (codes[i8] == 0) {
                        int q4 = (q3 << 8) | i8;
                        int i9 = this._inputPtr;
                        this._inputPtr = i9 + 1;
                        int i10 = input[i9] & 255;
                        if (codes[i10] == 0) {
                            this._quad1 = q4;
                            return parseMediumFieldName(i10, codes);
                        }
                        if (i10 == 34) {
                            return findName(q4, 4);
                        }
                        return parseFieldName(q4, i10, 4);
                    }
                    if (i8 == 34) {
                        return findName(q3, 3);
                    }
                    return parseFieldName(q3, i8, 3);
                }
                if (i6 == 34) {
                    return findName(q2, 2);
                }
                return parseFieldName(q2, i6, 2);
            }
            if (i4 == 34) {
                return findName(q, 1);
            }
            return parseFieldName(q, i4, 1);
        }
        if (q == 34) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        return parseFieldName(0, q, 0);
    }

    protected final Name parseMediumFieldName(int q2, int[] codes) throws IOException {
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & 255;
        if (codes[i2] != 0) {
            if (i2 == 34) {
                return findName(this._quad1, q2, 1);
            }
            return parseFieldName(this._quad1, q2, i2, 1);
        }
        int q3 = (q2 << 8) | i2;
        byte[] bArr2 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        int i4 = bArr2[i3] & 255;
        if (codes[i4] != 0) {
            if (i4 == 34) {
                return findName(this._quad1, q3, 2);
            }
            return parseFieldName(this._quad1, q3, i4, 2);
        }
        int q4 = (q3 << 8) | i4;
        byte[] bArr3 = this._inputBuffer;
        int i5 = this._inputPtr;
        this._inputPtr = i5 + 1;
        int i6 = bArr3[i5] & 255;
        if (codes[i6] != 0) {
            if (i6 == 34) {
                return findName(this._quad1, q4, 3);
            }
            return parseFieldName(this._quad1, q4, i6, 3);
        }
        int q5 = (q4 << 8) | i6;
        byte[] bArr4 = this._inputBuffer;
        int i7 = this._inputPtr;
        this._inputPtr = i7 + 1;
        int i8 = bArr4[i7] & 255;
        if (codes[i8] != 0) {
            if (i8 == 34) {
                return findName(this._quad1, q5, 4);
            }
            return parseFieldName(this._quad1, q5, i8, 4);
        }
        this._quadBuffer[0] = this._quad1;
        this._quadBuffer[1] = q5;
        return parseLongFieldName(i8);
    }

    protected Name parseLongFieldName(int q) throws IOException {
        int[] codes = sInputCodesLatin1;
        int qlen = 2;
        while (this._inputEnd - this._inputPtr >= 4) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & 255;
            if (codes[i2] != 0) {
                if (i2 == 34) {
                    return findName(this._quadBuffer, qlen, q, 1);
                }
                return parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 1);
            }
            int q2 = (q << 8) | i2;
            byte[] bArr2 = this._inputBuffer;
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            int i4 = bArr2[i3] & 255;
            if (codes[i4] != 0) {
                if (i4 == 34) {
                    return findName(this._quadBuffer, qlen, q2, 2);
                }
                return parseEscapedFieldName(this._quadBuffer, qlen, q2, i4, 2);
            }
            int q3 = (q2 << 8) | i4;
            byte[] bArr3 = this._inputBuffer;
            int i5 = this._inputPtr;
            this._inputPtr = i5 + 1;
            int i6 = bArr3[i5] & 255;
            if (codes[i6] != 0) {
                if (i6 == 34) {
                    return findName(this._quadBuffer, qlen, q3, 3);
                }
                return parseEscapedFieldName(this._quadBuffer, qlen, q3, i6, 3);
            }
            int q4 = (q3 << 8) | i6;
            byte[] bArr4 = this._inputBuffer;
            int i7 = this._inputPtr;
            this._inputPtr = i7 + 1;
            int i8 = bArr4[i7] & 255;
            if (codes[i8] != 0) {
                if (i8 == 34) {
                    return findName(this._quadBuffer, qlen, q4, 4);
                }
                return parseEscapedFieldName(this._quadBuffer, qlen, q4, i8, 4);
            }
            if (qlen >= this._quadBuffer.length) {
                this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
            }
            this._quadBuffer[qlen] = q4;
            q = i8;
            qlen++;
        }
        return parseEscapedFieldName(this._quadBuffer, qlen, 0, q, 0);
    }

    protected Name slowParseFieldName() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing '\"' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & 255;
        return i2 == 34 ? BytesToNameCanonicalizer.getEmptyName() : parseEscapedFieldName(this._quadBuffer, 0, 0, i2, 0);
    }

    private final Name parseFieldName(int q1, int ch, int lastQuadBytes) throws IOException {
        return parseEscapedFieldName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
    }

    private final Name parseFieldName(int q1, int q2, int ch, int lastQuadBytes) throws IOException {
        this._quadBuffer[0] = q1;
        return parseEscapedFieldName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
    }

    /* JADX WARN: Code duplicated, block: B:52:0x00c6 A[PHI: r11
      0x00c6: PHI (r11v2 'ch' int) = (r11v1 'ch' int), (r11v6 'ch' int) binds: [B:4:0x0005, B:20:0x0034] A[DONT_GENERATE, DONT_INLINE]] */
    protected Name parseEscapedFieldName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes) throws IOException {
        int qlen2;
        int qlen3;
        int qlen4;
        int[] codes = sInputCodesLatin1;
        while (true) {
            if (codes[ch] == 0) {
                qlen2 = qlen;
            } else {
                if (ch == 34) {
                    break;
                }
                if (ch != 92) {
                    _throwUnquotedSpace(ch, "name");
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > 127) {
                    if (currQuadBytes >= 4) {
                        if (qlen >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen3 = qlen + 1;
                        quads[qlen] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                    } else {
                        qlen3 = qlen;
                    }
                    if (ch < 2048) {
                        currQuad = (currQuad << 8) | (ch >> 6) | 192;
                        currQuadBytes++;
                        qlen4 = qlen3;
                    } else {
                        int currQuad2 = (currQuad << 8) | (ch >> 12) | 224;
                        int currQuadBytes2 = currQuadBytes + 1;
                        if (currQuadBytes2 >= 4) {
                            if (qlen3 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen4 = qlen3 + 1;
                            quads[qlen3] = currQuad2;
                            currQuad2 = 0;
                            currQuadBytes2 = 0;
                        } else {
                            qlen4 = qlen3;
                        }
                        currQuad = (currQuad2 << 8) | ((ch >> 6) & 63) | 128;
                        currQuadBytes = currQuadBytes2 + 1;
                    }
                    ch = (ch & 63) | 128;
                    qlen2 = qlen4;
                } else {
                    qlen2 = qlen;
                }
            }
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & 255;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            quads[qlen] = currQuad;
            qlen++;
        }
        Name name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    protected final Name _handleUnusualFieldName(int ch) throws IOException {
        if (ch == 39 && isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseApostropheFieldName();
        }
        if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar(ch, "was expecting double-quote to start field name");
        }
        int[] codes = CharTypes.getInputCodeUtf8JsNames();
        if (codes[ch] != 0) {
            _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] quads = this._quadBuffer;
        int qlen = 0;
        int currQuad = 0;
        int currQuadBytes = 0;
        while (true) {
            int qlen2 = qlen;
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            ch = this._inputBuffer[this._inputPtr] & 255;
            if (codes[ch] != 0) {
                break;
            }
            this._inputPtr++;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            quads[qlen] = currQuad;
            qlen++;
        }
        Name name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    protected final Name _parseApostropheFieldName() throws IOException {
        int qlen;
        int qlen2;
        int qlen3;
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing ''' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i] & 255;
        if (ch == 39) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int[] codes = sInputCodesLatin1;
        int qlen4 = 0;
        while (ch != 39) {
            if (ch != 34 && codes[ch] != 0) {
                if (ch != 92) {
                    _throwUnquotedSpace(ch, "name");
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > 127) {
                    if (currQuadBytes >= 4) {
                        if (qlen4 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        quads[qlen4] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                        qlen4++;
                    }
                    if (ch < 2048) {
                        currQuad = (currQuad << 8) | (ch >> 6) | 192;
                        currQuadBytes++;
                        qlen3 = qlen4;
                    } else {
                        int currQuad2 = (currQuad << 8) | (ch >> 12) | 224;
                        int currQuadBytes2 = currQuadBytes + 1;
                        if (currQuadBytes2 >= 4) {
                            if (qlen4 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen3 = qlen4 + 1;
                            quads[qlen4] = currQuad2;
                            currQuad2 = 0;
                            currQuadBytes2 = 0;
                        } else {
                            qlen3 = qlen4;
                        }
                        currQuad = (currQuad2 << 8) | ((ch >> 6) & 63) | 128;
                        currQuadBytes = currQuadBytes2 + 1;
                    }
                    ch = (ch & 63) | 128;
                    qlen4 = qlen3;
                }
            }
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen2 = qlen4;
            } else {
                if (qlen4 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen2 = qlen4 + 1;
                quads[qlen4] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            byte[] bArr2 = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            ch = bArr2[i2] & 255;
            qlen4 = qlen2;
        }
        if (currQuadBytes > 0) {
            if (qlen4 >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen = qlen4 + 1;
            quads[qlen4] = currQuad;
        } else {
            qlen = qlen4;
        }
        Name name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    private final Name findName(int q1, int lastQuadBytes) throws JsonParseException {
        Name name = this._symbols.findName(q1);
        if (name == null) {
            this._quadBuffer[0] = q1;
            return addName(this._quadBuffer, 1, lastQuadBytes);
        }
        return name;
    }

    private final Name findName(int q1, int q2, int lastQuadBytes) throws JsonParseException {
        Name name = this._symbols.findName(q1, q2);
        if (name == null) {
            this._quadBuffer[0] = q1;
            this._quadBuffer[1] = q2;
            return addName(this._quadBuffer, 2, lastQuadBytes);
        }
        return name;
    }

    private final Name findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes) throws JsonParseException {
        if (qlen >= quads.length) {
            quads = growArrayBy(quads, quads.length);
            this._quadBuffer = quads;
        }
        int qlen2 = qlen + 1;
        quads[qlen] = lastQuad;
        Name name = this._symbols.findName(quads, qlen2);
        if (name == null) {
            return addName(quads, qlen2, lastQuadBytes);
        }
        return name;
    }

    /* JADX WARN: Code duplicated, block: B:52:0x010f A[PHI: r5 r9
      0x010f: PHI (r5v2 'ch' int) = (r5v1 'ch' int), (r5v9 'ch' int) binds: [B:8:0x0036, B:30:0x00a7] A[DONT_GENERATE, DONT_INLINE]
      0x010f: PHI (r9v3 'ix' int) = (r9v2 'ix' int), (r9v6 'ix' int) binds: [B:8:0x0036, B:30:0x00a7] A[DONT_GENERATE, DONT_INLINE]] */
    private final Name addName(int[] quads, int qlen, int lastQuadBytes) throws JsonParseException {
        int lastQuad;
        int cix;
        int ch;
        int needed;
        int byteLen = ((qlen << 2) - 4) + lastQuadBytes;
        if (lastQuadBytes < 4) {
            lastQuad = quads[qlen - 1];
            quads[qlen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
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
                    _reportInvalidEOF(" in field name");
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
            quads[qlen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, qlen);
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected void _finishString() throws IOException {
        int ptr = this._inputPtr;
        if (ptr >= this._inputEnd) {
            loadMoreGuaranteed();
            ptr = this._inputPtr;
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = sInputCodesUtf8;
        int max = Math.min(this._inputEnd, outBuf.length + ptr);
        byte[] inputBuffer = this._inputBuffer;
        int outPtr = 0;
        while (ptr < max) {
            int c = inputBuffer[ptr] & 255;
            if (codes[c] != 0) {
                if (c != 34) {
                    break;
                }
                this._inputPtr = ptr + 1;
                this._textBuffer.setCurrentLength(outPtr);
                return;
            }
            ptr++;
            outBuf[outPtr] = (char) c;
            outPtr++;
        }
        this._inputPtr = ptr;
        _finishString2(outBuf, outPtr);
    }

    private final void _finishString2(char[] outBuf, int outPtr) throws IOException {
        int outPtr2;
        int[] codes = sInputCodesUtf8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr = this._inputPtr;
            if (ptr >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr = this._inputPtr;
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = Math.min(this._inputEnd, (outBuf.length - outPtr) + ptr);
            int ptr2 = ptr;
            int outPtr3 = outPtr;
            while (true) {
                if (ptr2 < max) {
                    int ptr3 = ptr2 + 1;
                    int c = inputBuffer[ptr2] & 255;
                    if (codes[c] != 0) {
                        this._inputPtr = ptr3;
                        if (c != 34) {
                            switch (codes[c]) {
                                case 1:
                                    c = _decodeEscaped();
                                    outPtr2 = outPtr3;
                                    break;
                                case 2:
                                    c = _decodeUtf8_2(c);
                                    outPtr2 = outPtr3;
                                    break;
                                case 3:
                                    if (this._inputEnd - this._inputPtr >= 2) {
                                        c = _decodeUtf8_3fast(c);
                                        outPtr2 = outPtr3;
                                    } else {
                                        c = _decodeUtf8_3(c);
                                        outPtr2 = outPtr3;
                                    }
                                    break;
                                case 4:
                                    int c2 = _decodeUtf8_4(c);
                                    outPtr2 = outPtr3 + 1;
                                    outBuf[outPtr3] = (char) (55296 | (c2 >> 10));
                                    if (outPtr2 >= outBuf.length) {
                                        outBuf = this._textBuffer.finishCurrentSegment();
                                        outPtr2 = 0;
                                    }
                                    c = 56320 | (c2 & 1023);
                                    break;
                                default:
                                    if (c < 32) {
                                        _throwUnquotedSpace(c, "string value");
                                        outPtr2 = outPtr3;
                                    } else {
                                        _reportInvalidChar(c);
                                        outPtr2 = outPtr3;
                                    }
                                    break;
                            }
                            if (outPtr2 >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr2 = 0;
                            }
                            outBuf[outPtr2] = (char) c;
                            outPtr = outPtr2 + 1;
                            break;
                        }
                        this._textBuffer.setCurrentLength(outPtr3);
                        return;
                    }
                    outBuf[outPtr3] = (char) c;
                    ptr2 = ptr3;
                    outPtr3++;
                } else {
                    this._inputPtr = ptr2;
                    outPtr = outPtr3;
                    break;
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:23:0x004e A[LOOP:1: B:6:0x0015->B:23:0x004e, LOOP_END] */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0015, code lost:
    
        r5 = r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void _skipString() throws java.io.IOException {
        /*
            r7 = this;
            r6 = 0
            r7._tokenIncomplete = r6
            int[] r1 = org.codehaus.jackson.impl.Utf8StreamParser.sInputCodesUtf8
            byte[] r2 = r7._inputBuffer
        L7:
            int r4 = r7._inputPtr
            int r3 = r7._inputEnd
            if (r4 < r3) goto L4e
            r7.loadMoreGuaranteed()
            int r4 = r7._inputPtr
            int r3 = r7._inputEnd
            r5 = r4
        L15:
            if (r5 >= r3) goto L28
            int r4 = r5 + 1
            r6 = r2[r5]
            r0 = r6 & 255(0xff, float:3.57E-43)
            r6 = r1[r0]
            if (r6 == 0) goto L4e
            r7._inputPtr = r4
            r6 = 34
            if (r0 != r6) goto L2b
            return
        L28:
            r7._inputPtr = r5
            goto L7
        L2b:
            r6 = r1[r0]
            switch(r6) {
                case 1: goto L3a;
                case 2: goto L3e;
                case 3: goto L42;
                case 4: goto L46;
                default: goto L30;
            }
        L30:
            r6 = 32
            if (r0 >= r6) goto L4a
            java.lang.String r6 = "string value"
            r7._throwUnquotedSpace(r0, r6)
            goto L7
        L3a:
            r7._decodeEscaped()
            goto L7
        L3e:
            r7._skipUtf8_2(r0)
            goto L7
        L42:
            r7._skipUtf8_3(r0)
            goto L7
        L46:
            r7._skipUtf8_4(r0)
            goto L7
        L4a:
            r7._reportInvalidChar(r0)
            goto L7
        L4e:
            r5 = r4
            goto L15
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._skipString():void");
    }

    protected JsonToken _handleUnexpectedValue(int c) throws IOException {
        switch (c) {
            case Input.Keys.K /* 39 */:
                if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
                    return _handleApostropheValue();
                }
                break;
            case Input.Keys.O /* 43 */:
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    _reportInvalidEOFInValue();
                }
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                return _handleInvalidNumberStart(bArr[i] & 255, false);
            case Input.Keys.NUM /* 78 */:
                if (_matchToken("NaN", 1)) {
                    if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                        return resetAsNaN("NaN", Double.NaN);
                    }
                    _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                }
                byte[] bArr2 = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                _reportUnexpectedChar(bArr2[i2] & 255, "expected 'NaN' or a valid value");
                break;
        }
        _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }

    protected JsonToken _handleApostropheValue() throws IOException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = sInputCodesUtf8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = this._inputEnd;
            int max2 = this._inputPtr + (outBuf.length - outPtr);
            if (max2 < max) {
                max = max2;
            }
            while (this._inputPtr < max) {
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int c = inputBuffer[i] & 255;
                if (c != 39 && codes[c] == 0) {
                    outBuf[outPtr] = (char) c;
                    outPtr++;
                } else {
                    if (c != 39) {
                        switch (codes[c]) {
                            case 1:
                                if (c != 34) {
                                    c = _decodeEscaped();
                                }
                                break;
                            case 2:
                                c = _decodeUtf8_2(c);
                                break;
                            case 3:
                                if (this._inputEnd - this._inputPtr >= 2) {
                                    c = _decodeUtf8_3fast(c);
                                } else {
                                    c = _decodeUtf8_3(c);
                                }
                                break;
                            case 4:
                                int c2 = _decodeUtf8_4(c);
                                int outPtr2 = outPtr + 1;
                                outBuf[outPtr] = (char) (55296 | (c2 >> 10));
                                if (outPtr2 >= outBuf.length) {
                                    outBuf = this._textBuffer.finishCurrentSegment();
                                    outPtr = 0;
                                } else {
                                    outPtr = outPtr2;
                                }
                                c = 56320 | (c2 & 1023);
                                break;
                            default:
                                if (c < 32) {
                                    _throwUnquotedSpace(c, "string value");
                                }
                                _reportInvalidChar(c);
                                break;
                        }
                        if (outPtr >= outBuf.length) {
                            outBuf = this._textBuffer.finishCurrentSegment();
                            outPtr = 0;
                        }
                        outBuf[outPtr] = (char) c;
                        outPtr++;
                        break;
                    }
                    this._textBuffer.setCurrentLength(outPtr);
                    return JsonToken.VALUE_STRING;
                }
            }
        }
    }

    protected JsonToken _handleInvalidNumberStart(int i, boolean negative) throws IOException {
        if (i == 73) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            i = bArr[i2];
            if (i == 78) {
                String match = negative ? "-INF" : "+INF";
                if (_matchToken(match, 3)) {
                    if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                        return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
                    }
                    _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                }
            } else if (i == 110) {
                String match2 = negative ? "-Infinity" : "+Infinity";
                if (_matchToken(match2, 3)) {
                    if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                        return resetAsNaN(match2, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
                    }
                    _reportError("Non-standard token '" + match2 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                }
            }
        }
        reportUnexpectedNumberChar(i, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }

    protected void _matchToken(JsonToken token) throws IOException {
        byte[] matchBytes = token.asByteArray();
        int len = matchBytes.length;
        for (int i = 1; i < len; i++) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (matchBytes[i] != this._inputBuffer[this._inputPtr]) {
                _reportInvalidToken(token.asString().substring(0, i), "'null', 'true' or 'false'");
            }
            this._inputPtr++;
        }
    }

    protected final boolean _matchToken(String matchStr, int i) throws IOException {
        int len = matchStr.length();
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in a value");
            }
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i), "'null', 'true', 'false' or NaN");
            }
            this._inputPtr++;
            i++;
        } while (i < len);
        if (this._inputPtr < this._inputEnd || loadMore()) {
            char c = (char) _decodeCharForError(this._inputBuffer[this._inputPtr] & 255);
            if (Character.isJavaIdentifierPart(c)) {
                this._inputPtr++;
                _reportInvalidToken(matchStr.substring(0, i), "'null', 'true', 'false' or NaN");
            }
        }
        return true;
    }

    protected void _reportInvalidToken(String matchedPart, String msg) throws IOException {
        StringBuilder sb = new StringBuilder(matchedPart);
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i];
            char c = (char) _decodeCharForError(i2);
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            this._inputPtr++;
            sb.append(c);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
    }

    private final int _skipWS() throws IOException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                if (i2 > 32) {
                    if (i2 != 47) {
                        return i2;
                    }
                    _skipComment();
                } else if (i2 != 32) {
                    if (i2 == 10) {
                        _skipLF();
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            } else {
                throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
            }
        }
    }

    private final int _skipWSOrEnd() throws IOException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                if (i2 > 32) {
                    if (i2 == 47) {
                        _skipComment();
                    } else {
                        return i2;
                    }
                } else if (i2 != 32) {
                    if (i2 == 10) {
                        _skipLF();
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            } else {
                _handleEOF();
                return -1;
            }
        }
    }

    private final void _skipComment() throws IOException {
        if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i] & 255;
        if (c == 47) {
            _skipCppComment();
        } else if (c == 42) {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCComment() throws IOException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case 10:
                            _skipLF();
                            break;
                        case 13:
                            _skipCR();
                            break;
                        case Input.Keys.N /* 42 */:
                            if (this._inputBuffer[this._inputPtr] == 47) {
                                this._inputPtr++;
                                return;
                            }
                            break;
                        default:
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                _reportInvalidEOF(" in a comment");
                return;
            }
        }
    }

    private final void _skipCppComment() throws IOException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case 10:
                            _skipLF();
                            return;
                        case 13:
                            _skipCR();
                            return;
                        case Input.Keys.N /* 42 */:
                            break;
                        default:
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                return;
            }
        }
    }

    protected final char _decodeEscaped() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in character escape sequence");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i];
        switch (c) {
            case Input.Keys.F /* 34 */:
            case 47:
            case 92:
                return (char) c;
            case 98:
                return '\b';
            case 102:
                return '\f';
            case 110:
                return '\n';
            case Opcodes.FREM /* 114 */:
                return '\r';
            case Opcodes.INEG /* 116 */:
                return '\t';
            case Opcodes.LNEG /* 117 */:
                int value = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in character escape sequence");
                    }
                    byte[] bArr2 = this._inputBuffer;
                    int i3 = this._inputPtr;
                    this._inputPtr = i3 + 1;
                    int ch = bArr2[i3];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape((char) _decodeCharForError(c));
        }
    }

    protected int _decodeCharForError(int firstByte) throws IOException {
        int needed;
        int c = firstByte;
        if (c < 0) {
            if ((c & 224) == 192) {
                c &= 31;
                needed = 1;
            } else if ((c & 240) == 224) {
                c &= 15;
                needed = 2;
            } else if ((c & Input.Keys.F5) == 240) {
                c &= 7;
                needed = 3;
            } else {
                _reportInvalidInitial(c & 255);
                needed = 1;
            }
            int d = nextByte();
            if ((d & 192) != 128) {
                _reportInvalidOther(d & 255);
            }
            int c2 = (c << 6) | (d & 63);
            if (needed > 1) {
                int d2 = nextByte();
                if ((d2 & 192) != 128) {
                    _reportInvalidOther(d2 & 255);
                }
                int c3 = (c2 << 6) | (d2 & 63);
                if (needed > 2) {
                    int d3 = nextByte();
                    if ((d3 & 192) != 128) {
                        _reportInvalidOther(d3 & 255);
                    }
                    return (c3 << 6) | (d3 & 63);
                }
                return c3;
            }
            return c2;
        }
        return c;
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

    private final void _skipUtf8_2(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c2 = bArr[i];
        if ((c2 & 192) != 128) {
            _reportInvalidOther(c2 & 255, this._inputPtr);
        }
    }

    private final void _skipUtf8_3(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c2 = bArr[i];
        if ((c2 & 192) != 128) {
            _reportInvalidOther(c2 & 255, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int c3 = bArr2[i2];
        if ((c3 & 192) != 128) {
            _reportInvalidOther(c3 & 255, this._inputPtr);
        }
    }

    private final void _skipUtf8_4(int c) throws IOException {
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
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
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
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == 10) {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected final void _skipLF() throws IOException {
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    private int nextByte() throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return bArr[i] & 255;
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

    public static int[] growArrayBy(int[] arr, int more) {
        if (arr == null) {
            return new int[more];
        }
        int len = arr.length;
        int[] arr2 = new int[len + more];
        System.arraycopy(arr, 0, arr2, 0, len);
        return arr2;
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException {
        ByteArrayBuilder builder = _getByteArrayBuilder();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int ch = bArr[i] & 255;
            if (ch > 32) {
                int bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (ch == 34) {
                        return builder.toByteArray();
                    }
                    bits = _decodeBase64Escape(b64variant, ch, 0);
                    if (bits < 0) {
                        continue;
                    }
                }
                int decodedData = bits;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr2 = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                int ch2 = bArr2[i2] & 255;
                int bits2 = b64variant.decodeBase64Char(ch2);
                if (bits2 < 0) {
                    bits2 = _decodeBase64Escape(b64variant, ch2, 1);
                }
                int decodedData2 = (decodedData << 6) | bits2;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr3 = this._inputBuffer;
                int i3 = this._inputPtr;
                this._inputPtr = i3 + 1;
                int ch3 = bArr3[i3] & 255;
                int bits3 = b64variant.decodeBase64Char(ch3);
                if (bits3 < 0) {
                    if (bits3 != -2) {
                        bits3 = _decodeBase64Escape(b64variant, ch3, 2);
                    }
                    if (bits3 == -2) {
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        byte[] bArr4 = this._inputBuffer;
                        int i4 = this._inputPtr;
                        this._inputPtr = i4 + 1;
                        int ch4 = bArr4[i4] & 255;
                        if (!b64variant.usesPaddingChar(ch4)) {
                            throw reportInvalidChar(b64variant, ch4, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
                        }
                        builder.append(decodedData2 >> 4);
                    }
                }
                int decodedData3 = (decodedData2 << 6) | bits3;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr5 = this._inputBuffer;
                int i5 = this._inputPtr;
                this._inputPtr = i5 + 1;
                int ch5 = bArr5[i5] & 255;
                int bits4 = b64variant.decodeBase64Char(ch5);
                if (bits4 < 0) {
                    if (bits4 != -2) {
                        bits4 = _decodeBase64Escape(b64variant, ch5, 3);
                    }
                    if (bits4 == -2) {
                        builder.appendTwoBytes(decodedData3 >> 2);
                    }
                }
                builder.appendThreeBytes((decodedData3 << 6) | bits4);
            }
        }
    }

    private final int _decodeBase64Escape(Base64Variant b64variant, int ch, int index) throws IOException {
        if (ch != 92) {
            throw reportInvalidChar(b64variant, ch, index);
        }
        int unescaped = _decodeEscaped();
        if (unescaped <= 32 && index == 0) {
            return -1;
        }
        int bits = b64variant.decodeBase64Char(unescaped);
        if (bits < 0) {
            throw reportInvalidChar(b64variant, unescaped, index);
        }
        return bits;
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex) throws IllegalArgumentException {
        return reportInvalidChar(b64variant, ch, bindex, null);
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex, String msg) throws IllegalArgumentException {
        String base;
        if (ch <= 32) {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ((char) ch) + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        return new IllegalArgumentException(base);
    }
}
