package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.io.IOContext;

/* JADX INFO: loaded from: classes.dex */
public abstract class ReaderBasedParserBase extends JsonNumericParserBase {
    protected char[] _inputBuffer;
    protected Reader _reader;

    protected ReaderBasedParserBase(IOContext ctxt, int features, Reader r) {
        super(ctxt, features);
        this._reader = r;
        this._inputBuffer = ctxt.allocTokenBuffer();
    }

    @Override // org.codehaus.jackson.JsonParser
    public int releaseBuffered(Writer w) throws IOException {
        int count = this._inputEnd - this._inputPtr;
        if (count < 1) {
            return 0;
        }
        int origPtr = this._inputPtr;
        w.write(this._inputBuffer, origPtr, count);
        return count;
    }

    @Override // org.codehaus.jackson.JsonParser
    public Object getInputSource() {
        return this._reader;
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected final boolean loadMore() throws IOException {
        this._currInputProcessed += (long) this._inputEnd;
        this._currInputRowStart -= this._inputEnd;
        if (this._reader == null) {
            return false;
        }
        int count = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
        if (count > 0) {
            this._inputPtr = 0;
            this._inputEnd = count;
            return true;
        }
        _closeInput();
        if (count == 0) {
            throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
        }
        return false;
    }

    protected char getNextChar(String eofMsg) throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(eofMsg);
        }
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return cArr[i];
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected void _closeInput() throws IOException {
        if (this._reader != null) {
            if (this._ioContext.isResourceManaged() || isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE)) {
                this._reader.close();
            }
            this._reader = null;
        }
    }

    @Override // org.codehaus.jackson.impl.JsonParserBase
    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        char[] buf = this._inputBuffer;
        if (buf != null) {
            this._inputBuffer = null;
            this._ioContext.releaseTokenBuffer(buf);
        }
    }

    protected final boolean _matchToken(String matchStr, int i) throws IOException {
        int len = matchStr.length();
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i), "'null', 'true', 'false' or NaN");
            }
            this._inputPtr++;
            i++;
        } while (i < len);
        if (this._inputPtr < this._inputEnd || loadMore()) {
            char c = this._inputBuffer[this._inputPtr];
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
            char c = this._inputBuffer[this._inputPtr];
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            this._inputPtr++;
            sb.append(c);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting ");
    }
}
