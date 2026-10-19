package com.sessionm.integralblue.httpresponsecache;

import com.sessionm.integralblue.httpresponsecache.compat.URLStreamHandlerFactoryImpl;
import com.sessionm.integralblue.httpresponsecache.compat.java.net.ExtendedResponseCache;
import com.sessionm.integralblue.httpresponsecache.compat.java.net.ResponseSource;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.IoUtils;
import com.sessionm.jakewharton.DiskLruCache;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class HttpResponseCache extends ResponseCache implements ExtendedResponseCache, Closeable {
    private final com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpResponseCache delegate;

    private HttpResponseCache(File file, long j) throws IOException {
        this.delegate = new com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpResponseCache(file, j);
    }

    public static HttpResponseCache getInstalled() {
        ResponseCache responseCache = ResponseCache.getDefault();
        if (responseCache instanceof HttpResponseCache) {
            return (HttpResponseCache) responseCache;
        }
        return null;
    }

    public static HttpResponseCache install(File file, long j) throws IOException {
        HttpResponseCache installed = getInstalled();
        if (installed != null) {
            DiskLruCache cache = installed.delegate.getCache();
            if (!cache.getDirectory().equals(file) || cache.maxSize() != j || cache.isClosed()) {
                IoUtils.closeQuietly(installed);
                installed = new HttpResponseCache(file, j);
                ResponseCache.setDefault(installed);
                try {
                    URL.setURLStreamHandlerFactory(new URLStreamHandlerFactoryImpl());
                } catch (Error e) {
                }
            }
        } else {
            installed = new HttpResponseCache(file, j);
            ResponseCache.setDefault(installed);
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactoryImpl());
        }
        return installed;
    }

    @Override // java.net.ResponseCache
    public CacheResponse get(URI uri, String str, Map<String, List<String>> map) throws IOException {
        return this.delegate.get(uri, str, map);
    }

    @Override // java.net.ResponseCache
    public CacheRequest put(URI uri, URLConnection uRLConnection) throws IOException {
        return this.delegate.put(uri, uRLConnection);
    }

    public long size() {
        return this.delegate.getCache().size();
    }

    public long maxSize() {
        return this.delegate.getCache().maxSize();
    }

    public void flush() {
        try {
            this.delegate.getCache().flush();
        } catch (IOException e) {
        }
    }

    public int getNetworkCount() {
        return this.delegate.getNetworkCount();
    }

    public int getHitCount() {
        return this.delegate.getHitCount();
    }

    public int getRequestCount() {
        return this.delegate.getRequestCount();
    }

    @Override // com.sessionm.integralblue.httpresponsecache.compat.java.net.ExtendedResponseCache
    public void trackResponse(ResponseSource responseSource) {
        this.delegate.trackResponse(responseSource);
    }

    @Override // com.sessionm.integralblue.httpresponsecache.compat.java.net.ExtendedResponseCache
    public void trackConditionalCacheHit() {
        this.delegate.trackConditionalCacheHit();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (ResponseCache.getDefault() == this) {
            ResponseCache.setDefault(null);
        }
        this.delegate.getCache().close();
    }

    public void delete() throws IOException {
        if (ResponseCache.getDefault() == this) {
            ResponseCache.setDefault(null);
        }
        this.delegate.getCache().delete();
    }
}
