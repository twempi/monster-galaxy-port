package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.impl.JsonParserMinimalBase;

/* JADX INFO: loaded from: classes.dex */
public class TreeTraversingParser extends JsonParserMinimalBase {
    protected boolean _closed;
    protected JsonToken _nextToken;
    protected NodeCursor _nodeCursor;
    protected ObjectCodec _objectCodec;
    protected boolean _startContainer;

    public TreeTraversingParser(JsonNode n) {
        this(n, null);
    }

    public TreeTraversingParser(JsonNode n, ObjectCodec codec) {
        super(0);
        this._objectCodec = codec;
        if (n.isArray()) {
            this._nextToken = JsonToken.START_ARRAY;
            this._nodeCursor = new NodeCursor.Array(n, null);
        } else if (n.isObject()) {
            this._nextToken = JsonToken.START_OBJECT;
            this._nodeCursor = new NodeCursor.Object(n, null);
        } else {
            this._nodeCursor = new NodeCursor.RootValue(n, null);
        }
    }

    @Override // org.codehaus.jackson.JsonParser
    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    @Override // org.codehaus.jackson.JsonParser
    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this._closed) {
            this._closed = true;
            this._nodeCursor = null;
            this._currToken = null;
        }
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public JsonToken nextToken() throws IOException {
        if (this._nextToken != null) {
            this._currToken = this._nextToken;
            this._nextToken = null;
            return this._currToken;
        }
        if (this._startContainer) {
            this._startContainer = false;
            if (!this._nodeCursor.currentHasChildren()) {
                this._currToken = this._currToken == JsonToken.START_OBJECT ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
                return this._currToken;
            }
            this._nodeCursor = this._nodeCursor.iterateChildren();
            this._currToken = this._nodeCursor.nextToken();
            if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
                this._startContainer = true;
            }
            return this._currToken;
        }
        if (this._nodeCursor == null) {
            this._closed = true;
            return null;
        }
        this._currToken = this._nodeCursor.nextToken();
        if (this._currToken != null) {
            if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
                this._startContainer = true;
            }
            return this._currToken;
        }
        this._currToken = this._nodeCursor.endToken();
        this._nodeCursor = this._nodeCursor.getParent();
        return this._currToken;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public JsonParser skipChildren() throws IOException {
        if (this._currToken == JsonToken.START_OBJECT) {
            this._startContainer = false;
            this._currToken = JsonToken.END_OBJECT;
        } else if (this._currToken == JsonToken.START_ARRAY) {
            this._startContainer = false;
            this._currToken = JsonToken.END_ARRAY;
        }
        return this;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public boolean isClosed() {
        return this._closed;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public String getCurrentName() {
        if (this._nodeCursor == null) {
            return null;
        }
        return this._nodeCursor.getCurrentName();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public JsonStreamContext getParsingContext() {
        return this._nodeCursor;
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonLocation getTokenLocation() {
        return JsonLocation.NA;
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonLocation getCurrentLocation() {
        return JsonLocation.NA;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public String getText() {
        if (this._closed) {
            return null;
        }
        switch (this._currToken) {
            case FIELD_NAME:
                return this._nodeCursor.getCurrentName();
            case VALUE_STRING:
                return currentNode().getTextValue();
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                return String.valueOf(currentNode().getNumberValue());
            case VALUE_EMBEDDED_OBJECT:
                JsonNode n = currentNode();
                if (n != null && n.isBinary()) {
                    return n.getValueAsText();
                }
                break;
        }
        if (this._currToken != null) {
            return this._currToken.asString();
        }
        return null;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public char[] getTextCharacters() throws IOException {
        return getText().toCharArray();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public int getTextLength() throws IOException {
        return getText().length();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public int getTextOffset() throws IOException {
        return 0;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public boolean hasTextCharacters() {
        return false;
    }

    @Override // org.codehaus.jackson.JsonParser
    public JsonParser.NumberType getNumberType() throws IOException {
        JsonNode n = currentNumericNode();
        if (n == null) {
            return null;
        }
        return n.getNumberType();
    }

    @Override // org.codehaus.jackson.JsonParser
    public BigInteger getBigIntegerValue() throws IOException {
        return currentNumericNode().getBigIntegerValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public BigDecimal getDecimalValue() throws IOException {
        return currentNumericNode().getDecimalValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public double getDoubleValue() throws IOException {
        return currentNumericNode().getDoubleValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public float getFloatValue() throws IOException {
        return (float) currentNumericNode().getDoubleValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public long getLongValue() throws IOException {
        return currentNumericNode().getLongValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public int getIntValue() throws IOException {
        return currentNumericNode().getIntValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public Number getNumberValue() throws IOException {
        return currentNumericNode().getNumberValue();
    }

    @Override // org.codehaus.jackson.JsonParser
    public Object getEmbeddedObject() {
        JsonNode n;
        if (this._closed || (n = currentNode()) == null || !n.isPojo()) {
            return null;
        }
        return ((POJONode) n).getPojo();
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase, org.codehaus.jackson.JsonParser
    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        JsonNode n = currentNode();
        if (n != null) {
            byte[] data = n.getBinaryValue();
            if (data == null) {
                if (n.isPojo()) {
                    Object ob = ((POJONode) n).getPojo();
                    if (ob instanceof byte[]) {
                        return (byte[]) ob;
                    }
                }
            } else {
                return data;
            }
        }
        return null;
    }

    protected JsonNode currentNode() {
        if (this._closed || this._nodeCursor == null) {
            return null;
        }
        return this._nodeCursor.currentNode();
    }

    protected JsonNode currentNumericNode() throws JsonParseException {
        JsonNode n = currentNode();
        if (n == null || !n.isNumber()) {
            JsonToken t = n == null ? null : n.asToken();
            throw _constructError("Current token (" + t + ") not numeric, can not use numeric value accessors");
        }
        return n;
    }

    @Override // org.codehaus.jackson.impl.JsonParserMinimalBase
    protected void _handleEOF() throws JsonParseException {
        _throwInternal();
    }
}
