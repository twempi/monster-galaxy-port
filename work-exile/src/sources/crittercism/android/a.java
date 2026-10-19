package crittercism.android;

import android.content.Context;
import android.util.Log;
import java.util.HashMap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class a {
    private static String a = "";
    private static String b = "en";

    /* JADX INFO: renamed from: crittercism.android.a$a, reason: collision with other inner class name */
    public static class C0011a {
        public static String a = "uhe";
        public static String b = "uhe-bg";
        public static String c = "he";
        public static String d = "he-bg";
        public static String e = "appload";
        public static String f = "error";
    }

    public static class b {
        private static HashMap a = new HashMap();
        private static final String b = a.a + "/strings/";

        public static synchronized String a(int i) {
            return a.containsKey(Integer.valueOf(i)) ? (String) a.get(Integer.valueOf(i)) : "";
        }

        public static void a() {
            a.put(10, "Error: no internet connection");
            a.put(11, "Error: connection timed out, please try again later.");
            a.put(13, "Unknown Error");
            a.put(28, "Developer Reply");
            a.put(29, "Loading...");
        }
    }

    public static class c {
        public static final String a = d.a();
        public static final String b = a + "/android_v2/handle_app_loads";
        public static final String c = a + "/android_v2/handle_exceptions";
        public static final String d = a + "/android_v2/handle_crashes";
        public static final String e = a + "/android_v2/update_user_metadata";
        public static final String f = a + "/android_v2/update_package_name";
        public static final String g = a + "/android_v2/ndk_crash";
        public static final String h = a + "/forum/springboard";
    }

    public static String a(int i) {
        return b.a(i);
    }

    public static void a(Context context) {
        try {
            a = context.getCacheDir().getAbsolutePath();
            b.a();
            b = b(context);
        } catch (Exception e) {
            Log.e("Crittercism", "Unable to initialize Crittercism's resources. Please report this error to support@crittercism.com.");
        }
    }

    public static String b(Context context) {
        try {
            String language = context.getResources().getConfiguration().locale.getLanguage();
            b = language;
            if (language == null || b.equals("")) {
                b = "en";
            }
        } catch (Exception e) {
            String str = "Exception in getLocale(): " + e.getClass().getName();
        }
        return b;
    }
}
