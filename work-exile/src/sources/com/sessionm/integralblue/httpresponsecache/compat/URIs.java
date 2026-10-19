package com.sessionm.integralblue.httpresponsecache.compat;

import java.net.URI;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class URIs {
    public static final int getEffectivePort(URI uri) {
        return getEffectivePort(uri.getScheme(), uri.getPort());
    }

    public static int getEffectivePort(String str, int i) {
        if (i == -1) {
            if ("http".equalsIgnoreCase(str)) {
                return 80;
            }
            return "https".equalsIgnoreCase(str) ? 443 : -1;
        }
        return i;
    }
}
