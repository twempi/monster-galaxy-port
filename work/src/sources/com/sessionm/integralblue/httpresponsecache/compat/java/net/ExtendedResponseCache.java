package com.sessionm.integralblue.httpresponsecache.compat.java.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface ExtendedResponseCache {
    void trackConditionalCacheHit();

    void trackResponse(ResponseSource responseSource);
}
