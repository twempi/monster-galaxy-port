package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.util.Arrays;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class FixedLengthOutputStream extends AbstractHttpOutputStream {
    private int bytesRemaining;
    private final OutputStream socketOut;

    public FixedLengthOutputStream(OutputStream outputStream, int i) {
        this.socketOut = outputStream;
        this.bytesRemaining = i;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        checkNotClosed();
        Arrays.checkOffsetAndCount(bArr.length, i, i2);
        if (i2 > this.bytesRemaining) {
            throw new IOException("expected " + this.bytesRemaining + " bytes but received " + i2);
        }
        this.socketOut.write(bArr, i, i2);
        this.bytesRemaining -= i2;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (!this.closed) {
            this.socketOut.flush();
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (this.bytesRemaining > 0) {
                throw new IOException("unexpected end of stream");
            }
        }
    }
}
