package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheRequest;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class FixedLengthInputStream extends AbstractHttpInputStream {
    private int bytesRemaining;

    public FixedLengthInputStream(InputStream inputStream, CacheRequest cacheRequest, HttpEngine httpEngine, int i) throws IOException {
        super(inputStream, httpEngine, cacheRequest);
        this.bytesRemaining = i;
        if (this.bytesRemaining == 0) {
            endOfInput(true);
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        Arrays.checkOffsetAndCount(bArr.length, i, i2);
        checkNotClosed();
        if (this.bytesRemaining == 0) {
            return -1;
        }
        int i3 = this.in.read(bArr, i, Math.min(i2, this.bytesRemaining));
        if (i3 == -1) {
            unexpectedEndOfInput();
            throw new IOException("unexpected end of stream");
        }
        this.bytesRemaining -= i3;
        cacheWrite(bArr, i, i3);
        if (this.bytesRemaining == 0) {
            endOfInput(true);
        }
        return i3;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        checkNotClosed();
        if (this.bytesRemaining == 0) {
            return 0;
        }
        return Math.min(this.in.available(), this.bytesRemaining);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (this.bytesRemaining != 0) {
                unexpectedEndOfInput();
            }
        }
    }
}
