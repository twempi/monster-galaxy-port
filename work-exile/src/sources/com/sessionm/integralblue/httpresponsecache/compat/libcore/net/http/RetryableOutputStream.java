package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class RetryableOutputStream extends AbstractHttpOutputStream {
    private final ByteArrayOutputStream content;
    private final int limit;

    public RetryableOutputStream(int i) {
        this.limit = i;
        this.content = new ByteArrayOutputStream(i);
    }

    public RetryableOutputStream() {
        this.limit = -1;
        this.content = new ByteArrayOutputStream();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (this.content.size() < this.limit) {
                throw new IOException("content-length promised " + this.limit + " bytes, but received " + this.content.size());
            }
        }
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        checkNotClosed();
        Arrays.checkOffsetAndCount(bArr.length, i, i2);
        if (this.limit != -1 && this.content.size() > this.limit - i2) {
            throw new IOException("exceeded content-length limit of " + this.limit + " bytes");
        }
        this.content.write(bArr, i, i2);
    }

    public synchronized int contentLength() throws IOException {
        close();
        return this.content.size();
    }

    public void writeToSocket(OutputStream outputStream) throws IOException {
        this.content.writeTo(outputStream);
    }
}
