package com.sessionm.integralblue.httpresponsecache.compat;

import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpHandler;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpsHandler;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class URLStreamHandlerFactoryImpl implements URLStreamHandlerFactory {
    @Override // java.net.URLStreamHandlerFactory
    public URLStreamHandler createURLStreamHandler(String str) {
        if (str.equals("http")) {
            return new HttpHandler();
        }
        if (str.equals("https")) {
            return new HttpsHandler();
        }
        return null;
    }
}
