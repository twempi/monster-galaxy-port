package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
abstract class AbstractHttpOutputStream extends OutputStream {
    protected boolean closed;

    AbstractHttpOutputStream() {
    }

    @Override // java.io.OutputStream
    public final void write(int i) throws IOException {
        write(new byte[]{(byte) i});
    }

    protected final void checkNotClosed() throws IOException {
        if (this.closed) {
            throw new IOException("stream closed");
        }
    }
}
