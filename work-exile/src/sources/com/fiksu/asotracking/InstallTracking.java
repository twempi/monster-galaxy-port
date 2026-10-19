package com.fiksu.asotracking;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class InstallTracking extends BroadcastReceiver {
    private static final String FIKSU_RECEIVER = "com.fiksu.asotracking.InstallTracking";
    private static final String INTENT_NAME = "com.android.vending.INSTALL_REFERRER";
    private static final long MAX_BLOCK_MS = 3000;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        uploadConversionEvent(context, intent);
        forwardToOtherReceivers(context, intent);
    }

    protected void uploadConversionEvent(Context context, Intent intent) {
        String referrer;
        try {
            String referrer2 = intent.getStringExtra("referrer");
            if (referrer2 != null) {
                referrer = URLDecoder.decode(referrer2);
            } else {
                referrer = "";
            }
            new ConversionEventTracker(context, referrer).uploadEventSynchronously(MAX_BLOCK_MS);
        } catch (Exception e) {
            Log.e("FiksuTracking", "Unhandled exception processing intent.", e);
        }
    }

    protected void forwardToReceiver(Context context, Intent intent, String receiverClassName) {
        try {
            BroadcastReceiver receiver = (BroadcastReceiver) Class.forName(receiverClassName).newInstance();
            receiver.onReceive(context, intent);
            Log.d("FiksuTracking", "Forwarded to: " + receiverClassName);
        } catch (ClassNotFoundException e) {
            Log.e("FiksuTracking", "Forward failed, couldn't load class: " + receiverClassName);
        } catch (Exception error) {
            Log.e("FiksuTracking", "Forwarding to " + receiverClassName + " failed:", error);
        }
    }

    protected void forwardToOtherReceivers(Context context, Intent intent) {
        List<String> classNames = readTargetsFromMetaData(context);
        List<String> badlyBehaved = new ArrayList<>();
        Iterator<String> iter = classNames.iterator();
        while (iter.hasNext()) {
            String value = iter.next();
            if (value.equals(FIKSU_RECEIVER)) {
                iter.remove();
            }
            if (value.startsWith("getjar.")) {
                badlyBehaved.add(0, value);
                iter.remove();
            }
        }
        for (String className : classNames) {
            forwardToReceiver(context, intent, className);
        }
        for (String className2 : badlyBehaved) {
            forwardToReceiver(context, intent, className2);
        }
    }

    protected static List<String> readTargetsFromMetaData(Context context) {
        List<String> receivers = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            Log.e("FiksuTracking", "Couldn't get PackageManager.");
        } else {
            try {
                ActivityInfo activityInfo = packageManager.getReceiverInfo(new ComponentName(context, (Class<?>) InstallTracking.class), 128);
                if (activityInfo == null || activityInfo.metaData == null || activityInfo.metaData.keySet() == null) {
                    Log.d("FiksuTracking", "No forwarding metadata.");
                } else {
                    Bundle metaData = activityInfo.metaData;
                    List<String> keys = new ArrayList<>(metaData.keySet());
                    Collections.sort(keys);
                    for (String key : keys) {
                        if (key.startsWith("forward.")) {
                            if (metaData.getString(key) == null || metaData.getString(key).trim().equals("")) {
                                Log.e("FiksuTracking", "Couldn't parse receiver from metadata.");
                            } else {
                                receivers.add(metaData.getString(key).trim());
                            }
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("FiksuTracking", "Couldn't get info for receivers.");
            }
        }
        return receivers;
    }

    protected static List<String> readReceiversFromManifest(Context context) {
        List<String> receivers = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            Intent installReferrerIntent = new Intent(INTENT_NAME);
            installReferrerIntent.setPackage(context.getPackageName());
            List<ResolveInfo> list = packageManager.queryBroadcastReceivers(installReferrerIntent, 0);
            if (list != null && list.size() != 0) {
                for (ResolveInfo info : list) {
                    if (info != null && info.activityInfo != null && info.activityInfo.name != null) {
                        receivers.add(info.activityInfo.name);
                    }
                }
            }
        }
        return receivers;
    }

    protected static void checkForFiksuReceiver(Context context) {
        List<String> receivers = readReceiversFromManifest(context);
        if (receivers.size() == 0 || !receivers.get(0).equals(FIKSU_RECEIVER)) {
            String receiver = receivers.size() > 0 ? receivers.get(0) : "NONE";
            Log.e("FiksuTracking", "THE FIKSU INSTALL TRACKING CODE ISN'T INSTALLED CORRECTLY!");
            Log.e("FiksuTracking", "Unexpected receiver: " + receiver);
            throw new FiksuIntegrationError("The Fiksu BroadcastReceiver must be installed as the only receiver for the INSTALL_REFERRER Intent in AndroidManifest.xml.");
        }
        if (receivers.size() > 1) {
            Log.e("FiksuTracking", "THE FIKSU INSTALL TRACKING CODE ISN'T INSTALLED CORRECTLY!");
            Log.e("FiksuTracking", "Multiple receivers declared for: com.android.vending.INSTALL_REFERRER");
            throw new FiksuIntegrationError("Multiple receivers declared for: com.android.vending.INSTALL_REFERRER");
        }
    }
}
