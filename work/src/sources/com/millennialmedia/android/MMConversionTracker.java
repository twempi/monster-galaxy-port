package com.millennialmedia.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
public final class MMConversionTracker extends BroadcastReceiver {
    private static final String ACTION_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String EXTRA_REFERRER = "referrer";
    private static final String KEY_REFERRER = "installReferrer";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String referrerString;
        if (intent.getAction().equals(ACTION_REFERRER) && (referrerString = intent.getStringExtra(EXTRA_REFERRER)) != null && referrerString.length() != 0) {
            String referrerString2 = URLDecoder.decode(referrerString);
            MMAdViewSDK.Log.i("Received referrer: %s", referrerString2);
            SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(KEY_REFERRER, referrerString2);
            editor.commit();
        }
    }

    public static synchronized void trackConversion(Context context, final String goalId) {
        final TreeMap<String, String> extraParams;
        long installTime = 0;
        if (context != null && goalId != null) {
            if (goalId.length() != 0) {
                SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
                final boolean isFirstLaunch = settings.getBoolean("firstLaunch_" + goalId, true);
                String referrerString = settings.getString(KEY_REFERRER, null);
                if (referrerString != null) {
                    extraParams = new TreeMap<>();
                    String[] components = referrerString.split("&");
                    for (String param : components) {
                        String[] subComponents = param.split("=");
                        if (subComponents.length >= 2) {
                            extraParams.put(subComponents[0], subComponents[1]);
                        }
                    }
                } else {
                    extraParams = null;
                }
                if (isFirstLaunch) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("firstLaunch_" + goalId, false);
                    editor.commit();
                }
                try {
                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    try {
                        Field field = info.getClass().getField("firstInstallTime");
                        installTime = field.getLong(info);
                    } catch (Exception e) {
                    }
                } catch (PackageManager.NameNotFoundException e2) {
                }
                if (installTime > 0) {
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(installTime);
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                    installTime = calendar.getTimeInMillis();
                }
                final long installTimeUTC = installTime;
                final String auid = MMAdViewSDK.getHdid(context);
                if (MMAdViewSDK.isConnected(context)) {
                    Thread thread = new Thread() { // from class: com.millennialmedia.android.MMConversionTracker.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            HttpGetRequest tc = new HttpGetRequest();
                            try {
                                tc.trackConversion(goalId, auid, isFirstLaunch, installTimeUTC, extraParams);
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    MMAdViewSDK.Log.w("No network available for conversion tracking.");
                }
            }
        }
    }
}
