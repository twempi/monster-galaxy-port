package com.sessionm.integralblue.httpresponsecache.compat;

import java.util.logging.Logger;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class System {
    private static final Logger LOGGER = Logger.getLogger(System.class.getName());

    public static final void logW(String str) {
        LOGGER.warning(str);
    }

    public static void logI(String str, Throwable th) {
        LOGGER.info(str + ": " + th);
    }
}
