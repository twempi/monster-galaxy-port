package com.chartboost.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CBUtility {
    private static String ANDROID_AUID = null;
    public static final int ANDROID_AUID_COOKIE_FLAG = 1;
    public static final String ANDROID_AUID_COOKIE_KEY = "cb_auid";
    public static final String ANDROID_AUID_COOKIE_PREFIX = "android-";
    public static final String AUID_STATIC_EMULATOR = "ffff";
    public static final String AUID_STATIC_ERROR = "unknown";
    public static final String PREFERENCES_FILE = "ChartBoost.cb";
    public static final String PREFERENCES_SDCARD_PATH = "/Android/data/com.chartboost.sdk/files";

    public static String getAUID(Context ctx) {
        if (ANDROID_AUID != null) {
            return ANDROID_AUID;
        }
        String cookie = null;
        boolean doBackup = true;
        SharedPreferences preferences = ctx.getSharedPreferences(PREFERENCES_FILE, 1);
        String value = preferences.getString(ANDROID_AUID_COOKIE_KEY, null);
        if (value != null) {
            cookie = value;
        }
        String cookieUri = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + PREFERENCES_SDCARD_PATH + "/" + PREFERENCES_FILE;
        File cookiePath = new File(cookieUri);
        if (cookie == null) {
            try {
                if (Environment.getExternalStorageState().equals("mounted") && cookiePath.isFile()) {
                    StringBuffer cookieContent = new StringBuffer("");
                    FileInputStream fis = new FileInputStream(cookiePath);
                    while (true) {
                        int c = fis.read();
                        if (c == -1) {
                            break;
                        }
                        cookieContent.append((char) c);
                    }
                    fis.close();
                    if (cookieContent.length() == 40) {
                        cookie = cookieContent.toString();
                        doBackup = false;
                    }
                }
            } catch (Exception e) {
            }
        }
        if (cookie == null) {
            UUID myUUID = UUID.randomUUID();
            if (myUUID == null) {
                return AUID_STATIC_ERROR;
            }
            String cookie2 = myUUID.toString();
            cookie = ANDROID_AUID_COOKIE_PREFIX + cookie2.replace("-", "");
        }
        if (!preferences.edit().putString(ANDROID_AUID_COOKIE_KEY, cookie).commit()) {
            return AUID_STATIC_ERROR;
        }
        if (doBackup) {
            try {
                if (cookiePath.exists()) {
                    cookiePath.delete();
                } else {
                    cookiePath.mkdirs();
                }
                if (cookiePath.createNewFile()) {
                    FileWriter filewriter = new FileWriter(cookiePath, false);
                    filewriter.write(cookie);
                    filewriter.close();
                }
            } catch (IOException e2) {
            }
        }
        ANDROID_AUID = cookie;
        return cookie;
    }
}
