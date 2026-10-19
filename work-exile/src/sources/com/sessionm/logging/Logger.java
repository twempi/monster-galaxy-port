package com.sessionm.logging;

import android.util.Log;
import com.sessionm.a.b;
import com.sessionm.core.SessionMAndroidConfig;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Logger {
    private static AtomicBoolean enableLogging;
    private static AtomicInteger loggingLevel;

    static {
        SessionMAndroidConfig sessionMAndroidConfig = SessionMAndroidConfig.getInstance();
        enableLogging = new AtomicBoolean(false);
        if ("true".equalsIgnoreCase((String) sessionMAndroidConfig.getConfigurationByKey(b.F))) {
            enableLogging.set(true);
        }
        loggingLevel = new AtomicInteger(4);
        String str = (String) sessionMAndroidConfig.getConfigurationByKey(b.G);
        if (!"VERBOSE".equalsIgnoreCase(str)) {
            if (!"DEBUG".equalsIgnoreCase(str)) {
                if (!"INFO".equalsIgnoreCase(str)) {
                    if (!"WARN".equalsIgnoreCase(str) && !"WARNNING".equalsIgnoreCase(str)) {
                        if ("ERROR".equalsIgnoreCase(str)) {
                            loggingLevel.set(6);
                            return;
                        }
                        return;
                    }
                    loggingLevel.set(5);
                    return;
                }
                loggingLevel.set(4);
                return;
            }
            loggingLevel.set(3);
            return;
        }
        loggingLevel.set(2);
    }

    public static void enableLogging() {
        enableLogging.compareAndSet(false, true);
    }

    public static void disableLogging() {
        enableLogging.compareAndSet(true, false);
    }

    public static void setLevel(int i) {
        if (i >= 2 && i <= 6 && enableLogging.get()) {
            loggingLevel.getAndSet(i);
        }
    }

    public static void enableAndSetLevel(int i) {
        enableLogging();
        setLevel(i);
    }

    public static void verbose(String str, String str2) {
        logInternal(2, str, str2);
    }

    public static void debug(String str, String str2) {
        logInternal(3, str, str2);
    }

    public static void info(String str, String str2) {
        logInternal(4, str, str2);
    }

    public static void warn(String str, String str2) {
        logInternal(5, str, str2);
    }

    public static void error(String str, String str2) {
        logInternal(6, str, str2);
    }

    public static void fatal(String str, String str2) {
        logInternal(7, str, str2);
    }

    private static void logInternal(int i, String str, String str2) {
        if (enableLogging.get() && i >= loggingLevel.get() && str2 != null && str2.length() != 0) {
            if (i < 4) {
                i = 4;
            }
            if (str == null) {
                str = "";
            }
            if (i != 4) {
                if (i != 5) {
                    if (i != 6) {
                        if (i == 3) {
                            Log.d(str, str2);
                            return;
                        }
                        return;
                    }
                    Log.e(str, str2);
                    return;
                }
                Log.w(str, str2);
                return;
            }
            Log.i(str, str2);
        }
    }

    private Logger() {
    }
}
