package org.codehaus.jackson.format;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonFactory;

/* JADX INFO: loaded from: classes.dex */
public interface InputAccessor {
    boolean hasMoreBytes() throws IOException;

    byte nextByte() throws IOException;

    void reset();

    public static class Std implements InputAccessor {
        protected final byte[] _buffer;
        protected int _bufferedAmount;
        protected final InputStream _in;
        protected int _ptr;

        public Std(InputStream in, byte[] buffer) {
            this._in = in;
            this._buffer = buffer;
            this._bufferedAmount = 0;
        }

        public Std(byte[] inputDocument) {
            this._in = null;
            this._buffer = inputDocument;
            this._bufferedAmount = inputDocument.length;
        }

        @Override // org.codehaus.jackson.format.InputAccessor
        public boolean hasMoreBytes() throws IOException {
            int count;
            if (this._ptr < this._bufferedAmount) {
                return true;
            }
            int amount = this._buffer.length - this._ptr;
            if (amount >= 1 && (count = this._in.read(this._buffer, this._ptr, amount)) > 0) {
                this._bufferedAmount += count;
                return true;
            }
            return false;
        }

        @Override // org.codehaus.jackson.format.InputAccessor
        public byte nextByte() throws IOException {
            if (this._ptr > (-this._bufferedAmount) && !hasMoreBytes()) {
                throw new EOFException("Could not read more than " + this._ptr + " bytes (max buffer size: " + this._buffer.length + ")");
            }
            byte[] bArr = this._buffer;
            int i = this._ptr;
            this._ptr = i + 1;
            return bArr[i];
        }

        @Override // org.codehaus.jackson.format.InputAccessor
        public void reset() {
            this._ptr = 0;
        }

        public DataFormatMatcher createMatcher(JsonFactory match, MatchStrength matchStrength) {
            return new DataFormatMatcher(this._in, this._buffer, this._bufferedAmount, match, matchStrength);
        }
    }
}
