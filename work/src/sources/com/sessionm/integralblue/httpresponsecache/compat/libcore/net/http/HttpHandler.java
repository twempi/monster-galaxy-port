package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public final class HttpHandler extends URLStreamHandler {
    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) throws IOException {
        return new HttpURLConnectionImpl(url, getDefaultPort());
    }

    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        if (url == null || proxy == null) {
            throw new IllegalArgumentException("url == null || proxy == null");
        }
        return new HttpURLConnectionImpl(url, getDefaultPort(), proxy);
    }

    @Override // java.net.URLStreamHandler
    protected int getDefaultPort() {
        return 80;
    }
}
