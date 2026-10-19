package com.sessionm.integralblue.httpresponsecache.compat;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class URLs {
    public static final int getEffectivePort(URL url) {
        return URIs.getEffectivePort(url.getProtocol(), url.getPort());
    }

    public static final URI toURILenient(URL url) throws URISyntaxException {
        return url.toURI();
    }
}
