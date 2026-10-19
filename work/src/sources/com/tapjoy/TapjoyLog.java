package com.tapjoy;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class TapjoyLog {
    private static boolean showLog = false;

    public static void enableLogging(boolean enable) {
        Log.i("TapjoyLog", "enableLogging: " + enable);
        showLog = enable;
    }

    public static void i(String tag, String msg) {
        if (showLog) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (showLog) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (showLog) {
            Log.w(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (showLog) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
        }
    }
}
