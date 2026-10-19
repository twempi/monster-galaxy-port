package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class TextNode extends ValueNode {
    static final TextNode EMPTY_STRING_NODE = new TextNode("");
    static final int INT_SPACE = 32;
    final String _value;

    public TextNode(String v) {
        this._value = v;
    }

    public static TextNode valueOf(String v) {
        if (v == null) {
            return null;
        }
        if (v.length() == 0) {
            return EMPTY_STRING_NODE;
        }
        return new TextNode(v);
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.VALUE_STRING;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isTextual() {
        return true;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String getTextValue() {
        return this._value;
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        int ptr;
        char ch;
        ByteArrayBuilder builder = new ByteArrayBuilder(100);
        String str = this._value;
        int ptr2 = 0;
        int len = str.length();
        loop0: while (ptr2 < len) {
            while (true) {
                ptr = ptr2 + 1;
                ch = str.charAt(ptr2);
                if (ptr >= len) {
                    break loop0;
                }
                if (ch > ' ') {
                    break;
                }
                ptr2 = ptr;
            }
            int bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                _reportInvalidBase64(b64variant, ch, 0);
            }
            if (ptr >= len) {
                _reportBase64EOF();
            }
            int ptr3 = ptr + 1;
            char ch2 = str.charAt(ptr);
            int bits2 = b64variant.decodeBase64Char(ch2);
            if (bits2 < 0) {
                _reportInvalidBase64(b64variant, ch2, 1);
            }
            int decodedData = (bits << 6) | bits2;
            if (ptr3 >= len) {
                _reportBase64EOF();
            }
            int ptr4 = ptr3 + 1;
            char ch3 = str.charAt(ptr3);
            int bits3 = b64variant.decodeBase64Char(ch3);
            if (bits3 < 0) {
                if (bits3 != -2) {
                    _reportInvalidBase64(b64variant, ch3, 2);
                }
                if (ptr4 >= len) {
                    _reportBase64EOF();
                }
                ptr2 = ptr4 + 1;
                char ch4 = str.charAt(ptr4);
                if (!b64variant.usesPaddingChar(ch4)) {
                    _reportInvalidBase64(b64variant, ch4, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
                }
                builder.append(decodedData >> 4);
            } else {
                int decodedData2 = (decodedData << 6) | bits3;
                if (ptr4 >= len) {
                    _reportBase64EOF();
                }
                ptr2 = ptr4 + 1;
                char ch5 = str.charAt(ptr4);
                int bits4 = b64variant.decodeBase64Char(ch5);
                if (bits4 < 0) {
                    if (bits4 != -2) {
                        _reportInvalidBase64(b64variant, ch5, 3);
                    }
                    builder.appendTwoBytes(decodedData2 >> 2);
                } else {
                    builder.appendThreeBytes((decodedData2 << 6) | bits4);
                }
            }
        }
        return builder.toByteArray();
    }

    @Override // org.codehaus.jackson.JsonNode
    public byte[] getBinaryValue() throws IOException {
        return getBinaryValue(Base64Variants.getDefaultVariant());
    }

    @Override // org.codehaus.jackson.JsonNode
    public String getValueAsText() {
        return this._value;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean getValueAsBoolean(boolean defaultValue) {
        if (this._value != null && "true".equals(this._value.trim())) {
            return true;
        }
        return defaultValue;
    }

    @Override // org.codehaus.jackson.JsonNode
    public int getValueAsInt(int defaultValue) {
        return NumberInput.parseAsInt(this._value, defaultValue);
    }

    @Override // org.codehaus.jackson.JsonNode
    public long getValueAsLong(long defaultValue) {
        return NumberInput.parseAsLong(this._value, defaultValue);
    }

    @Override // org.codehaus.jackson.JsonNode
    public double getValueAsDouble(double defaultValue) {
        return NumberInput.parseAsDouble(this._value, defaultValue);
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        if (this._value == null) {
            jg.writeNull();
        } else {
            jg.writeString(this._value);
        }
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return ((TextNode) o)._value.equals(this._value);
    }

    public int hashCode() {
        return this._value.hashCode();
    }

    @Override // org.codehaus.jackson.node.ValueNode, org.codehaus.jackson.JsonNode
    public String toString() {
        int len = this._value.length();
        StringBuilder sb = new StringBuilder(len + 2 + (len >> 4));
        appendQuoted(sb, this._value);
        return sb.toString();
    }

    protected static void appendQuoted(StringBuilder sb, String content) {
        sb.append('\"');
        CharTypes.appendQuoted(sb, content);
        sb.append('\"');
    }

    protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex) throws JsonParseException {
        _reportInvalidBase64(b64variant, ch, bindex, null);
    }

    protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg) throws JsonParseException {
        String base;
        if (ch <= ' ') {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        throw new JsonParseException(base, JsonLocation.NA);
    }

    protected void _reportBase64EOF() throws JsonParseException {
        throw new JsonParseException("Unexpected end-of-String when base64 content", JsonLocation.NA);
    }
}
