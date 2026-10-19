package com.sessionm.integralblue.httpresponsecache.compat.java.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public enum ResponseSource {
    CACHE,
    CONDITIONAL_CACHE,
    NETWORK;

    public boolean requiresConnection() {
        return this == CONDITIONAL_CACHE || this == NETWORK;
    }
}
