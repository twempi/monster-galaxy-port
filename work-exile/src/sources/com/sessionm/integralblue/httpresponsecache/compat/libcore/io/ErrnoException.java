package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

import java.io.IOException;
import java.net.SocketException;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class ErrnoException extends Exception {
    public final int errno;
    private final String functionName;

    public ErrnoException(String str, int i) {
        this.functionName = str;
        this.errno = i;
    }

    public ErrnoException(String str, int i, Throwable th) {
        super(th);
        this.functionName = str;
        this.errno = i;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String strErrnoName = OsConstants.errnoName(this.errno);
        if (strErrnoName == null) {
            strErrnoName = "errno " + this.errno;
        }
        return this.functionName + " failed: " + strErrnoName;
    }

    public IOException rethrowAsIOException() throws IOException {
        IOException iOException = new IOException(getMessage());
        iOException.initCause(this);
        throw iOException;
    }

    public SocketException rethrowAsSocketException() throws SocketException {
        throw new com.sessionm.integralblue.httpresponsecache.compat.java.net.SocketException(getMessage(), this);
    }
}
