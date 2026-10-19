package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheRequest;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class UnknownLengthHttpInputStream extends AbstractHttpInputStream {
    private boolean inputExhausted;

    UnknownLengthHttpInputStream(InputStream inputStream, CacheRequest cacheRequest, HttpEngine httpEngine) throws IOException {
        super(inputStream, httpEngine, cacheRequest);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        Arrays.checkOffsetAndCount(bArr.length, i, i2);
        checkNotClosed();
        if (this.in == null || this.inputExhausted) {
            return -1;
        }
        int i3 = this.in.read(bArr, i, i2);
        if (i3 == -1) {
            this.inputExhausted = true;
            endOfInput(false);
            return -1;
        }
        cacheWrite(bArr, i, i3);
        return i3;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        checkNotClosed();
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (!this.inputExhausted) {
                unexpectedEndOfInput();
            }
        }
    }
}
