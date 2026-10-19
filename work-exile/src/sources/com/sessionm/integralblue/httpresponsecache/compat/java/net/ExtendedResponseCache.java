package com.sessionm.integralblue.httpresponsecache.compat.java.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ExtendedResponseCache {
    void trackConditionalCacheHit();

    void trackResponse(ResponseSource responseSource);
}
