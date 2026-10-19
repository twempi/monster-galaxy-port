package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import com.chartboost.sdk.CBConstants;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class d {
    public static final a a = a.PRODUCTION;

    public enum a {
        LOCAL_EMULATOR,
        LOCAL_DEVICE,
        OFFICE,
        OFFICE_YOUSEF,
        APT_YOUSEF,
        OTHER,
        KEVIN_ROB,
        STAGING,
        PRODUCTION
    }

    public static String a() {
        switch (a) {
            case LOCAL_EMULATOR:
                return "http://10.0.2.2:6013";
            case LOCAL_DEVICE:
                return "http://192.168.2.1:6013";
            case OFFICE:
                return "http://192.168.1.110:6013";
            case OFFICE_YOUSEF:
                return "http://192.168.1.250:6013";
            case APT_YOUSEF:
                return "http://10.0.1.5:6013";
            case OTHER:
                return "http://10.1.10.27:6013";
            case KEVIN_ROB:
                return "http://192.168.1.100:6013";
            case STAGING:
                return "https://www.appcred.com";
            default:
                return "https://api.crittercism.com";
        }
    }

    public static String a(Context context, String str) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.crittercism.prefs", 0);
            if (sharedPreferences != null) {
                return sharedPreferences.getString(str, null);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.crittercism.prefs", 0);
            if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null) {
                return;
            }
            editorEdit.remove(str);
            editorEdit.putString(str, str2);
            editorEdit.commit();
        } catch (Exception e) {
            e.toString();
        }
    }

    public static String b() {
        return CBConstants.CB_SDK_VERSION;
    }
}
