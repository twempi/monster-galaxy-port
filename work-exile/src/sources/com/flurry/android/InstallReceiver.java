package com.flurry.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class InstallReceiver extends BroadcastReceiver {
    private final Handler a;
    private File b = null;

    private InstallReceiver() {
        HandlerThread handlerThread = new HandlerThread("InstallReceiver");
        handlerThread.start();
        this.a = new Handler(handlerThread.getLooper());
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        this.b = context.getFileStreamPath(".flurryinstallreceiver." + Integer.toString(FlurryAgent.e().hashCode(), 16));
        if (FlurryAgent.isCaptureUncaughtExceptions()) {
            Thread.setDefaultUncaughtExceptionHandler(new FlurryAgent.FlurryDefaultExceptionHandler());
        }
        String stringExtra = intent.getStringExtra("referrer");
        if (stringExtra != null && "com.android.vending.INSTALL_REFERRER".equals(intent.getAction())) {
            try {
                a(a(stringExtra));
            } catch (IllegalArgumentException e) {
                ah.c("InstallReceiver", "Invalid referrer Tag: " + e.getMessage());
            }
        }
    }

    private static Map a(String str) {
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Referrer is null or empty");
        }
        HashMap map = new HashMap();
        String[] strArrSplit = str.split("&");
        int length = strArrSplit.length;
        for (int i = 0; i < length; i++) {
            String[] strArrSplit2 = strArrSplit[i].split("=");
            if (strArrSplit2.length != 2) {
                ah.a("InstallReceiver", "Invalid referrer Element: " + strArrSplit[i] + " in referrer tag " + str);
            } else {
                map.put(strArrSplit2[0], strArrSplit2[1]);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (map.get("utm_source") == null) {
            sb.append("Campaign Source is missing.\n");
        }
        if (map.get("utm_medium") == null) {
            sb.append("Campaign Medium is missing.\n");
        }
        if (map.get("utm_campaign") == null) {
            sb.append("Campaign Name is missing.\n");
        }
        if (sb.length() > 0) {
            throw new IllegalArgumentException(sb.toString());
        }
        return map;
    }

    private synchronized void a(Map map) {
        this.a.post(new t(this, map));
    }
}
