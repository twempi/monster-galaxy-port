package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.util.Arrays;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.Streams;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheRequest;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class ChunkedInputStream extends AbstractHttpInputStream {
    private static final int MIN_LAST_CHUNK_LENGTH = "\r\n0\r\n\r\n".length();
    private static final int NO_CHUNK_YET = -1;
    private int bytesRemainingInChunk;
    private boolean hasMoreChunks;

    ChunkedInputStream(InputStream inputStream, CacheRequest cacheRequest, HttpEngine httpEngine) throws IOException {
        super(inputStream, httpEngine, cacheRequest);
        this.bytesRemainingInChunk = -1;
        this.hasMoreChunks = true;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        Arrays.checkOffsetAndCount(bArr.length, i, i2);
        checkNotClosed();
        if (!this.hasMoreChunks) {
            return -1;
        }
        if (this.bytesRemainingInChunk == 0 || this.bytesRemainingInChunk == -1) {
            readChunkSize();
            if (!this.hasMoreChunks) {
                return -1;
            }
        }
        int i3 = this.in.read(bArr, i, Math.min(i2, this.bytesRemainingInChunk));
        if (i3 == -1) {
            unexpectedEndOfInput();
            throw new IOException("unexpected end of stream");
        }
        this.bytesRemainingInChunk -= i3;
        cacheWrite(bArr, i, i3);
        if (this.bytesRemainingInChunk == 0 && this.in.available() >= MIN_LAST_CHUNK_LENGTH) {
            readChunkSize();
        }
        return i3;
    }

    private void readChunkSize() throws IOException {
        if (this.bytesRemainingInChunk != -1) {
            Streams.readAsciiLine(this.in);
        }
        String asciiLine = Streams.readAsciiLine(this.in);
        int iIndexOf = asciiLine.indexOf(";");
        if (iIndexOf != -1) {
            asciiLine = asciiLine.substring(0, iIndexOf);
        }
        try {
            this.bytesRemainingInChunk = Integer.parseInt(asciiLine.trim(), 16);
            if (this.bytesRemainingInChunk == 0) {
                this.hasMoreChunks = false;
                this.httpEngine.readTrailers();
                endOfInput(true);
            }
        } catch (NumberFormatException e) {
            throw new IOException("Expected a hex chunk size, but was " + asciiLine);
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        checkNotClosed();
        if (!this.hasMoreChunks || this.bytesRemainingInChunk == -1) {
            return 0;
        }
        return Math.min(this.in.available(), this.bytesRemainingInChunk);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (this.hasMoreChunks) {
                unexpectedEndOfInput();
            }
        }
    }
}
