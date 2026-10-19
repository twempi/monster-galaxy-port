package com.kontagent;

import android.util.Log;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class KontagentLog {
    public static final String LOG_TAG = "Kontagent";
    private static boolean log = false;
    private static int level = 2;

    public static void enable() {
        enable(true);
    }

    public static void enable(boolean enable) {
        log = enable;
    }

    public static void setLevel(int level2) {
        level = level2;
    }

    private static String asTag(String tag) {
        return (tag == null || tag.length() <= 0) ? LOG_TAG : "Kontagent:" + tag;
    }

    private static String getThreadId() {
        return String.format("T#%02d:", Long.valueOf(Thread.currentThread().getId()));
    }

    private static String asMsg(String msg) {
        return getThreadId() + msg;
    }

    public static int d(String tag, String msg) {
        if (!log || 3 < level) {
            return -1;
        }
        return Log.d(asTag(tag), asMsg(msg));
    }

    public static int d(String msg) {
        return d(null, msg);
    }

    public static int i(String tag, String msg) {
        if (!log || 4 < level) {
            return -1;
        }
        return Log.i(asTag(tag), asMsg(msg));
    }

    public static int i(String msg) {
        return i(null, msg);
    }

    public static int w(String tag, String msg) {
        if (!log || 5 < level) {
            return -1;
        }
        return Log.w(asTag(tag), asMsg(msg));
    }

    public static int w(String msg) {
        return w(null, msg);
    }

    public static int e(String tag, String msg, Throwable e) {
        return Log.e(asTag(tag), asMsg(msg), e);
    }

    public static int e(String msg, Throwable ex) {
        return e(null, msg, ex);
    }
}
