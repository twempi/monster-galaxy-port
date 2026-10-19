package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.Streams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
abstract class AbstractHttpInputStream extends InputStream {
    private final OutputStream cacheBody;
    private final CacheRequest cacheRequest;
    protected boolean closed;
    protected final HttpEngine httpEngine;
    protected final InputStream in;

    AbstractHttpInputStream(InputStream inputStream, HttpEngine httpEngine, CacheRequest cacheRequest) throws IOException {
        this.in = inputStream;
        this.httpEngine = httpEngine;
        OutputStream body = cacheRequest != null ? cacheRequest.getBody() : null;
        cacheRequest = body == null ? null : cacheRequest;
        this.cacheBody = body;
        this.cacheRequest = cacheRequest;
    }

    @Override // java.io.InputStream
    public final int read() throws IOException {
        return Streams.readSingleByte(this);
    }

    protected final void checkNotClosed() throws IOException {
        if (this.closed) {
            throw new IOException("stream closed");
        }
    }

    protected final void cacheWrite(byte[] bArr, int i, int i2) throws IOException {
        if (this.cacheBody != null) {
            this.cacheBody.write(bArr, i, i2);
        }
    }

    protected final void endOfInput(boolean z) throws IOException {
        if (this.cacheRequest != null) {
            this.cacheBody.close();
        }
        this.httpEngine.release(z);
    }

    protected final void unexpectedEndOfInput() {
        if (this.cacheRequest != null) {
            this.cacheRequest.abort();
        }
        this.httpEngine.release(false);
    }
}
