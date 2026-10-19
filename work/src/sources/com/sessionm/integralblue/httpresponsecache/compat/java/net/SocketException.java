package com.sessionm.integralblue.httpresponsecache.compat.java.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class SocketException extends java.net.SocketException {
    public SocketException() {
    }

    public SocketException(String str) {
        super(str);
    }

    public SocketException(String str, Throwable th) {
        super(str + "\n" + th.toString());
    }
}
