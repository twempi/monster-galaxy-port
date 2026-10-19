package org.codehaus.jackson.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class UTF32Reader extends BaseReader {
    final boolean mBigEndian;
    int mByteCount;
    int mCharCount;
    char mSurrogate;

    @Override // org.codehaus.jackson.io.BaseReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public /* bridge */ /* synthetic */ void close() throws IOException {
        super.close();
    }

    @Override // org.codehaus.jackson.io.BaseReader, java.io.Reader
    public /* bridge */ /* synthetic */ int read() throws IOException {
        return super.read();
    }

    public UTF32Reader(IOContext ctxt, InputStream in, byte[] buf, int ptr, int len, boolean isBigEndian) {
        super(ctxt, in, buf, ptr, len);
        this.mSurrogate = (char) 0;
        this.mCharCount = 0;
        this.mByteCount = 0;
        this.mBigEndian = isBigEndian;
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int start, int len) throws IOException {
        int outPtr;
        int outPtr2;
        int ch;
        if (this._buffer == null) {
            return -1;
        }
        if (len < 1) {
            return len;
        }
        if (start < 0 || start + len > cbuf.length) {
            reportBounds(cbuf, start, len);
        }
        int len2 = len + start;
        if (this.mSurrogate != 0) {
            outPtr = start + 1;
            cbuf[start] = this.mSurrogate;
            this.mSurrogate = (char) 0;
        } else {
            int left = this._length - this._ptr;
            if (left < 4 && !loadMore(left)) {
                return -1;
            }
            outPtr = start;
        }
        while (outPtr < len2) {
            int ptr = this._ptr;
            if (this.mBigEndian) {
                ch = (this._buffer[ptr] << 24) | ((this._buffer[ptr + 1] & 255) << 16) | ((this._buffer[ptr + 2] & 255) << 8) | (this._buffer[ptr + 3] & 255);
            } else {
                ch = (this._buffer[ptr] & 255) | ((this._buffer[ptr + 1] & 255) << 8) | ((this._buffer[ptr + 2] & 255) << 16) | (this._buffer[ptr + 3] << 24);
            }
            this._ptr += 4;
            if (ch > 65535) {
                if (ch > 1114111) {
                    reportInvalid(ch, outPtr - start, "(above " + Integer.toHexString(1114111) + ") ");
                }
                int ch2 = ch - 65536;
                outPtr2 = outPtr + 1;
                cbuf[outPtr] = (char) (55296 + (ch2 >> 10));
                ch = 56320 | (ch2 & 1023);
                if (outPtr2 >= len2) {
                    this.mSurrogate = (char) ch;
                }
                int len3 = outPtr2 - start;
                this.mCharCount += len3;
                return len3;
            }
            outPtr2 = outPtr;
            outPtr = outPtr2 + 1;
            cbuf[outPtr2] = (char) ch;
            if (this._ptr >= this._length) {
                outPtr2 = outPtr;
                int len4 = outPtr2 - start;
                this.mCharCount += len4;
                return len4;
            }
        }
        outPtr2 = outPtr;
        int len5 = outPtr2 - start;
        this.mCharCount += len5;
        return len5;
    }

    private void reportUnexpectedEOF(int gotBytes, int needed) throws IOException {
        int bytePos = this.mByteCount + gotBytes;
        int charPos = this.mCharCount;
        throw new CharConversionException("Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + gotBytes + ", needed " + needed + ", at char #" + charPos + ", byte #" + bytePos + ")");
    }

    private void reportInvalid(int value, int offset, String msg) throws IOException {
        int bytePos = (this.mByteCount + this._ptr) - 1;
        int charPos = this.mCharCount + offset;
        throw new CharConversionException("Invalid UTF-32 character 0x" + Integer.toHexString(value) + msg + " at char #" + charPos + ", byte #" + bytePos + ")");
    }

    private boolean loadMore(int available) throws IOException {
        this.mByteCount += this._length - available;
        if (available > 0) {
            if (this._ptr > 0) {
                for (int i = 0; i < available; i++) {
                    this._buffer[i] = this._buffer[this._ptr + i];
                }
                this._ptr = 0;
            }
            this._length = available;
        } else {
            this._ptr = 0;
            int count = this._in.read(this._buffer);
            if (count < 1) {
                this._length = 0;
                if (count < 0) {
                    freeBuffers();
                    return false;
                }
                reportStrangeStream();
            }
            this._length = count;
        }
        while (this._length < 4) {
            int count2 = this._in.read(this._buffer, this._length, this._buffer.length - this._length);
            if (count2 < 1) {
                if (count2 < 0) {
                    freeBuffers();
                    reportUnexpectedEOF(this._length, 4);
                }
                reportStrangeStream();
            }
            this._length += count2;
        }
        return true;
    }
}
