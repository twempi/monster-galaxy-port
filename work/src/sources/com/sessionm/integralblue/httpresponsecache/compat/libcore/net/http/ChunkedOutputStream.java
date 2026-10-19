package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class ChunkedOutputStream extends AbstractHttpOutputStream {
    private final ByteArrayOutputStream bufferedChunk;
    private final byte[] hex = {0, 0, 0, 0, 0, 0, 0, 0, 13, 10};
    private final int maxChunkLength;
    private final OutputStream socketOut;
    private static final byte[] CRLF = {13, 10};
    private static final byte[] HEX_DIGITS = {48, 49, 50, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final byte[] FINAL_CHUNK = {48, 13, 10, 13, 10};

    public ChunkedOutputStream(OutputStream outputStream, int i) {
        this.socketOut = outputStream;
        this.maxChunkLength = Math.max(1, dataLength(i));
        this.bufferedChunk = new ByteArrayOutputStream(i);
    }

    private int dataLength(int i) {
        int i2 = 4;
        for (int i3 = i - 4; i3 > 0; i3 >>= 4) {
            i2++;
        }
        return i - i2;
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        int iMin;
        checkNotClosed();
        Arrays.checkOffsetAndCount(bArr.length, i, i2);
        while (i2 > 0) {
            if (this.bufferedChunk.size() > 0 || i2 < this.maxChunkLength) {
                iMin = Math.min(i2, this.maxChunkLength - this.bufferedChunk.size());
                this.bufferedChunk.write(bArr, i, iMin);
                if (this.bufferedChunk.size() == this.maxChunkLength) {
                    writeBufferedChunkToSocket();
                }
            } else {
                iMin = this.maxChunkLength;
                writeHex(iMin);
                this.socketOut.write(bArr, i, iMin);
                this.socketOut.write(CRLF);
            }
            i += iMin;
            i2 -= iMin;
        }
    }

    private void writeHex(int i) throws IOException {
        int i2 = 8;
        do {
            i2--;
            this.hex[i2] = HEX_DIGITS[i & 15];
            i >>>= 4;
        } while (i != 0);
        this.socketOut.write(this.hex, i2, this.hex.length - i2);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        if (!this.closed) {
            writeBufferedChunkToSocket();
            this.socketOut.flush();
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            writeBufferedChunkToSocket();
            this.socketOut.write(FINAL_CHUNK);
        }
    }

    private void writeBufferedChunkToSocket() throws IOException {
        int size = this.bufferedChunk.size();
        if (size > 0) {
            writeHex(size);
            this.bufferedChunk.writeTo(this.socketOut);
            this.bufferedChunk.reset();
            this.socketOut.write(CRLF);
        }
    }
}
