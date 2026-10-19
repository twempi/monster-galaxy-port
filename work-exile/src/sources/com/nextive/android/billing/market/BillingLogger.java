package com.nextive.android.billing.market;

import android.util.Log;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class BillingLogger {
    public static boolean DEBUG = true;
    public static final String TAG = "InAppBilling";

    BillingLogger() {
    }

    public static void log(String tag, String message) {
        if (DEBUG && tag != null && message != null) {
            Log.i(tag, message);
        }
    }

    public static void log(String message) {
        log(TAG, message);
    }
}
