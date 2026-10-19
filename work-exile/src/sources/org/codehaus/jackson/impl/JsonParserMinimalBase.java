package org.codehaus.jackson.impl;

import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class JsonParserMinimalBase extends JsonParser {
    protected static final int INT_APOSTROPHE = 39;
    protected static final int INT_ASTERISK = 42;
    protected static final int INT_BACKSLASH = 92;
    protected static final int INT_COLON = 58;
    protected static final int INT_COMMA = 44;
    protected static final int INT_CR = 13;
    protected static final int INT_LBRACKET = 91;
    protected static final int INT_LCURLY = 123;
    protected static final int INT_LF = 10;
    protected static final int INT_QUOTE = 34;
    protected static final int INT_RBRACKET = 93;
    protected static final int INT_RCURLY = 125;
    protected static final int INT_SLASH = 47;
    protected static final int INT_SPACE = 32;
    protected static final int INT_TAB = 9;
    protected static final int INT_b = 98;
    protected static final int INT_f = 102;
    protected static final int INT_n = 110;
    protected static final int INT_r = 114;
    protected static final int INT_t = 116;
    protected static final int INT_u = 117;

    protected abstract void _handleEOF() throws JsonParseException;

    @Override // org.codehaus.jackson.JsonParser, java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract byte[] getBinaryValue(Base64Variant base64Variant) throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract String getCurrentName() throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract JsonStreamContext getParsingContext();

    @Override // org.codehaus.jackson.JsonParser
    public abstract String getText() throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract char[] getTextCharacters() throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract int getTextLength() throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract int getTextOffset() throws IOException;

    @Override // org.codehaus.jackson.JsonParser
    public abstract boolean hasTextCharacters();

    @Override // org.codehaus.jackson.JsonParser
    public abstract boolean isClosed();

    @Override // org.codehaus.jackson.JsonParser
    public abstract JsonToken nextToken() throws IOException;

    protected JsonParserMinimalBase() {
    }

    protected JsonParserMinimalBase(int features) {
        super(features);
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonParser skipChildren() throws IOException {
        if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            int open = 1;
            while (true) {
                JsonToken t = nextToken();
                if (t == null) {
                    _handleEOF();
                } else {
                    switch (t) {
                        case START_OBJECT:
                        case START_ARRAY:
                            open++;
                            break;
                        case END_OBJECT:
                        case END_ARRAY:
                            open--;
                            if (open != 0) {
                            }
                            break;
                    }
                }
            }
        }
        return this;
    }

    @Override // org.codehaus.jackson.JsonParser
    public boolean getValueAsBoolean(boolean defaultValue) throws IOException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (this._currToken) {
            case VALUE_NUMBER_INT:
                return getIntValue() != 0;
            case VALUE_TRUE:
                return true;
            case VALUE_FALSE:
            case VALUE_NULL:
                return false;
            case VALUE_EMBEDDED_OBJECT:
                Object value = getEmbeddedObject();
                if (value instanceof Boolean) {
                    return ((Boolean) value).booleanValue();
                }
                break;
            case VALUE_STRING:
                break;
            default:
                return defaultValue;
        }
        String str = getText().trim();
        if ("true".equals(str)) {
            return true;
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonParser
    public int getValueAsInt(int defaultValue) throws IOException {
        if (this._currToken != null) {
            switch (this._currToken) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    return getIntValue();
                case VALUE_TRUE:
                    return 1;
                case VALUE_FALSE:
                case VALUE_NULL:
                    return 0;
                case VALUE_EMBEDDED_OBJECT:
                    Object value = getEmbeddedObject();
                    if (value instanceof Number) {
                        return ((Number) value).intValue();
                    }
                    return defaultValue;
                case VALUE_STRING:
                    return NumberInput.parseAsInt(getText(), defaultValue);
                default:
                    return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonParser
    public long getValueAsLong(long defaultValue) throws IOException {
        if (this._currToken != null) {
            switch (this._currToken) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    return getLongValue();
                case VALUE_TRUE:
                    return 1L;
                case VALUE_FALSE:
                case VALUE_NULL:
                    return 0L;
                case VALUE_EMBEDDED_OBJECT:
                    Object value = getEmbeddedObject();
                    if (value instanceof Number) {
                        return ((Number) value).longValue();
                    }
                    return defaultValue;
                case VALUE_STRING:
                    return NumberInput.parseAsLong(getText(), defaultValue);
                default:
                    return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonParser
    public double getValueAsDouble(double defaultValue) throws IOException {
        if (this._currToken != null) {
            switch (this._currToken) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    return getDoubleValue();
                case VALUE_TRUE:
                    return 1.0d;
                case VALUE_FALSE:
                case VALUE_NULL:
                    return 0.0d;
                case VALUE_EMBEDDED_OBJECT:
                    Object value = getEmbeddedObject();
                    if (value instanceof Number) {
                        return ((Number) value).doubleValue();
                    }
                    return defaultValue;
                case VALUE_STRING:
                    return NumberInput.parseAsDouble(getText(), defaultValue);
                default:
                    return defaultValue;
            }
        }
        return defaultValue;
    }

    protected void _reportUnexpectedChar(int ch, String comment) throws JsonParseException {
        String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
        if (comment != null) {
            msg = msg + ": " + comment;
        }
        _reportError(msg);
    }

    protected void _reportInvalidEOF() throws JsonParseException {
        _reportInvalidEOF(" in " + this._currToken);
    }

    protected void _reportInvalidEOF(String msg) throws JsonParseException {
        _reportError("Unexpected end-of-input" + msg);
    }

    protected void _reportInvalidEOFInValue() throws JsonParseException {
        _reportInvalidEOF(" in a value");
    }

    protected void _throwInvalidSpace(int i) throws JsonParseException {
        char c = (char) i;
        String msg = "Illegal character (" + _getCharDesc(c) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens";
        _reportError(msg);
    }

    protected void _throwUnquotedSpace(int i, String ctxtDesc) throws JsonParseException {
        if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS) || i >= 32) {
            char c = (char) i;
            String msg = "Illegal unquoted character (" + _getCharDesc(c) + "): has to be escaped using backslash to be included in " + ctxtDesc;
            _reportError(msg);
        }
    }

    protected char _handleUnrecognizedCharacterEscape(char ch) throws JsonProcessingException {
        if (!isEnabled(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER) && (ch != '\'' || !isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
            _reportError("Unrecognized character escape " + _getCharDesc(ch));
        }
        return ch;
    }

    protected static final String _getCharDesc(int ch) {
        char c = (char) ch;
        if (Character.isISOControl(c)) {
            return "(CTRL-CHAR, code " + ch + ")";
        }
        if (ch > 255) {
            return "'" + c + "' (code " + ch + " / 0x" + Integer.toHexString(ch) + ")";
        }
        return "'" + c + "' (code " + ch + ")";
    }

    protected final void _reportError(String msg) throws JsonParseException {
        throw _constructError(msg);
    }

    protected final void _wrapError(String msg, Throwable t) throws JsonParseException {
        throw _constructError(msg, t);
    }

    protected final void _throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }

    protected final JsonParseException _constructError(String msg, Throwable t) {
        return new JsonParseException(msg, getCurrentLocation(), t);
    }
}
