package com.sessionm.a;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.sessionm.core.Session;
import com.sessionm.core.SessionMAndroidConfig;
import com.sessionm.integralblue.httpresponsecache.HttpResponseCache;
import com.sessionm.json.JSONObject;
import com.sessionm.logging.Logger;
import com.sessionm.net.Request;
import com.sessionm.net.RequestListener;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import oauth.signpost.OAuth;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public final class e {
    private static final String TAG = "Utils";
    public static final char[] X = {239, 213, 'G', 133, 253, 'n', 'a', 224, 'a', '\b', 'N', 15, '\f', ' ', '0', 160, 'i', 241, 25, 231, 155, 185, 134, '\t', 'r', 'm', 152, 253, 248, 214, 31, 164, 181, 220, SignatureVisitor.EXTENDS, ',', SignatureVisitor.INSTANCEOF, '$', '8', ' ', 207, 179, 160, 'I', 141, 223, 231, 22, 'N', 20, '~', 'm', '\n', 186, SignatureVisitor.SUPER, 158, 7, 'k', '9', ' ', 237, '8', 225, 173, 's', 132, ';', 186, 'w', 165, 238, 'J', 'F', 'w', 133, 132, 150, 198, '&', 'l', 137, '*', 231, 237, 221, 137, 'j', '#', 11, 208, '>', 171, 'C', SignatureVisitor.SUPER, 5, 'j', 184, 'X', 'Y', '\r', 180, 'e', '\r', 239, 't', 226, 235, 'I', 211, 239, '|', 199, ']', 'i', 162, ';', 146, 245, 254, 193, 143, 236, ']', 'A', 228, '6', 153, 218, 17, 177, 232, 29, 200, 'y', 240, 255, '^', 3, 'c', 'c', '>', 202, '&', '\b', '8', '{', 19, '/', 244, 'L', 29, ']', 239, 't', 180, 230, 163, 'V', 237, '&', 'n', 'K', '#'};
    public static final char[] Y = {200, 21, 192, 153, 139, 141, 185, 235, 26, 181, 132, '7', 'X', 166, 162, 't', 176, 185, 200, 181, 190, 'I', 209, 138, '4', 196, 222, 133, 'u', 'M', 203, 246, 'N', 0, 17, 'h', '(', 215, 171, 'e', 133, 211, 31, '}', 'N', 'M', 232, 145, 'C', 239, 187, 229, '\t', 172, 131, 31, 175, 135, '\r', 171, 222, '2', 208, 25, 128, 195, '>', 223, 247, 239, 154, 165, 'B', 3, 197, 154, 150, 'K', 'T', 208, 153, 'p', 248, 172, 142, 2, 's', 5, 'E', ';', 'T', 187, 'G', 31, 'C', '\"', '.', 185, 152, 175, 132, 246, 17, 170, 142, 'M', 232, 233, 14, 189, 6, 20, 128, 243, 205, 197, 11, 187, 160, 0, 3, 218, 201, 200, 'F', 'J', 'q', 0, 'k', 191, 7, 'p', 187, 153, 235, '^', 226, 'E', 155, 25, 218, 'U', '3', 212, 's', 'V', 219, 152, 194, 'i', 197, 232, 31, 170, 'O', 195, 253, 254, 225, 175, 247, 168, 4, 212, 's', 31, 236, 17, 169, 'R', 'n', 185, 252, ')', 'f', 178, 249, 150, 'J', '6', 176, '9', 230, 151, 196, 177, ',', '}', 145, 202, 196, 11, 31, 146, '~', 162, 182, '^', '$', '2', '/', 'e', 206, 172, 246, 185, '<', 127, '{', 206, 6, 224, 'O', '^', '[', '8', '?', 191, '<', 5, 249, 236, '(', 140};
    private static SharedPreferences.OnSharedPreferenceChangeListener Z;

    private e() {
    }

    public static int a(Context context, int i) {
        if (context == null || i <= 0) {
            return 0;
        }
        return (int) ((context.getResources().getDisplayMetrics().density * i) + 0.5f);
    }

    public static Bitmap a(Context context, String str) throws Throwable {
        AssetManager assets;
        InputStream inputStreamOpen;
        Throwable th;
        Bitmap bitmapDecodeStream = null;
        if (context != null && str != null && str.length() != 0 && (assets = context.getResources().getAssets()) != null) {
            try {
                inputStreamOpen = assets.open("imgs/" + str);
                try {
                    bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpen);
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e2) {
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (Exception e3) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                inputStreamOpen = null;
            } catch (Throwable th3) {
                inputStreamOpen = null;
                th = th3;
            }
        }
        return bitmapDecodeStream;
    }

    public static IntentFilter b() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_INSTALL");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("com.android.vending.INSTALL_REFERRER");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        return intentFilter;
    }

    public static String a(String str, Request.Method method, String str2) {
        Uri uri = Uri.parse(str);
        String encodedPath = uri.getEncodedPath() == null ? "" : uri.getEncodedPath();
        String apiKey = Session.getSession().getApiKey();
        String sessionId = Session.getSession().getSessionId();
        if (sessionId == null) {
            sessionId = "";
        }
        try {
            return a.a(String.format("%s%s%s%s%s%s", method.toString().toUpperCase(), SessionMAndroidConfig.getInstance().serverUrl(), encodedPath, c(str2), apiKey, sessionId), X, Y);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> a(HashMap<String, String> map) {
        Iterator<String> it = map.keySet().iterator();
        HashMap<String, String> map2 = new HashMap<>();
        while (it.hasNext()) {
            String string = it.next().toString();
            map2.put(string, map.get(string));
        }
        return map2;
    }

    public static String a(Object obj) throws Exception {
        return a(obj, (String) null);
    }

    public static String a(Object obj, String str) throws Exception {
        int i = 0;
        if (obj instanceof JSONObject) {
            return a(((JSONObject) obj).toJSONObject(), str);
        }
        if (obj instanceof org.json.JSONObject) {
            org.json.JSONObject jSONObject = (org.json.JSONObject) obj;
            String[] strArr = new String[jSONObject.length()];
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String string = itKeys.next().toString();
                String strEncode = str != null ? str + "[" + URLEncoder.encode(string, OAuth.ENCODING) + "]" : URLEncoder.encode(string, OAuth.ENCODING);
                Object obj2 = jSONObject.get(string);
                if (jSONObject.isNull(string)) {
                    obj2 = null;
                }
                strArr[i] = a(obj2, strEncode);
                i++;
            }
            return a("&", strArr);
        }
        if (obj instanceof HashMap) {
            HashMap map = (HashMap) obj;
            String[] strArr2 = new String[map.size()];
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String string2 = it.next().toString();
                strArr2[i] = a(map.get(string2), str != null ? str + "[" + URLEncoder.encode(string2, OAuth.ENCODING) + "]" : URLEncoder.encode(string2, OAuth.ENCODING));
                i++;
            }
            return a("&", strArr2);
        }
        if (obj instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) obj;
            String[] strArr3 = new String[jSONArray.length()];
            while (i < jSONArray.length()) {
                strArr3[i] = a(jSONArray.get(i), str + "[]");
                i++;
            }
            return a("&", strArr3);
        }
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            String[] strArr4 = new String[objArr.length];
            while (i < objArr.length) {
                strArr4[i] = a(objArr[i], str + "[]");
                i++;
            }
            return a("&", strArr4);
        }
        if (str != null) {
            return obj != null ? str + "=" + URLEncoder.encode(obj.toString(), OAuth.ENCODING) : str + "=";
        }
        return obj.toString();
    }

    public static HashMap<String, String> a(String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (str.equals("")) {
            str = "z=" + jCurrentTimeMillis;
        } else if (str.indexOf("&z=") < 0 && !str.startsWith("z=")) {
            str = str + "&z=" + jCurrentTimeMillis;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("query", str);
        map.put("z", String.valueOf(jCurrentTimeMillis));
        return map;
    }

    public static boolean b(String str) {
        return str.startsWith(SessionMAndroidConfig.getInstance().serverUrl());
    }

    public static String c(String str) {
        String[] strArrSplit = str.split("&");
        Arrays.sort(strArrSplit, new d());
        return a("&", strArrSplit);
    }

    public static String a(String str, String[] strArr) {
        if (strArr.length == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strArr[0]);
        for (int i = 1; i < strArr.length; i++) {
            stringBuffer.append(str);
            stringBuffer.append(strArr[i]);
        }
        return stringBuffer.toString();
    }

    public static boolean b(Context context, String str) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(Opcodes.ACC_ANNOTATION);
        int size = installedPackages.size();
        for (int i = 0; i < size; i++) {
            PackageInfo packageInfo = installedPackages.get(i);
            Logger.debug(TAG, packageInfo.packageName);
            if (str.equalsIgnoreCase(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void a(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("smInstallsFile", 3);
        Map<String, ?> all = sharedPreferences.getAll();
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(Opcodes.ACC_ANNOTATION);
        int size = installedPackages.size();
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        if (all.size() > 0) {
            for (int i = 0; i < size; i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                Logger.debug(TAG, packageInfo.packageName);
                if (all.containsKey(packageInfo.packageName)) {
                    editorEdit.remove(packageInfo.packageName);
                    try {
                        a(context, packageInfo.packageName, new org.json.JSONObject(all.get(packageInfo.packageName).toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            editorEdit.commit();
        }
        if (Z == null) {
            Z = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.sessionm.a.e.1
                @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences2, String str) {
                    try {
                        e.a(context, str, new org.json.JSONObject(sharedPreferences2.getString(str, "")));
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            };
            sharedPreferences.registerOnSharedPreferenceChangeListener(Z);
        }
    }

    public static void a(Context context, boolean z) {
        HashMap map = new HashMap();
        map.put("member[opted_out]", z ? "1" : "0");
        Request request = new Request(Request.Type.OPT_IN_OUT, map);
        request.setListener(new RequestListener() { // from class: com.sessionm.a.e.2
            @Override // com.sessionm.net.RequestListener
            public void onRequestSent(Request request2) {
            }

            @Override // com.sessionm.net.RequestListener
            public void onReplyReceived(Request request2) {
            }
        });
        request.send();
    }

    public static void a(final Context context, final String str, org.json.JSONObject jSONObject) {
        Request request = new Request(Request.Type.INSTALL, jSONObject);
        request.setListener(new RequestListener() { // from class: com.sessionm.a.e.3
            @Override // com.sessionm.net.RequestListener
            public void onRequestSent(Request request2) {
            }

            @Override // com.sessionm.net.RequestListener
            public void onReplyReceived(Request request2) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("smInstallsFile", 3);
                sharedPreferences.getAll();
                sharedPreferences.edit().remove(str).commit();
            }
        });
        request.send();
    }

    public static String d(String str) {
        int iIndexOf = str.indexOf(46);
        int iLastIndexOf = str.lastIndexOf(58);
        return iLastIndexOf < iIndexOf ? str.substring(iIndexOf) : str.substring(iIndexOf, iLastIndexOf);
    }

    public static void b(Context context) {
        File file = new File(context.getCacheDir(), "sessionmcache");
        try {
            if (Class.forName("android.net.http.HttpResponseCache").getMethod("getInstalled", new Class[0]).invoke(null, new Object[0]) == null) {
                Class.forName("android.net.http.HttpResponseCache").getMethod("install", File.class, Long.TYPE).invoke(null, file, 10485760L);
            }
        } catch (Exception e) {
            Logger.debug(TAG, "android.net.http.HttpResponseCache not available, probably because we're running on a pre-ICS version of Android. Using com.integralblue.httpresponsecache.HttpHttpResponseCache.");
            try {
                if (HttpResponseCache.getInstalled() != null) {
                    Logger.debug(TAG, "cache already installed.");
                } else {
                    HttpResponseCache.install(file, 10485760L);
                }
            } catch (Exception e2) {
                Logger.error(TAG, "Failed to set up com.integralblue.httpresponsecache.HttpResponseCache");
            }
        }
    }

    public static void c(Context context) {
        try {
            Object objInvoke = Class.forName("android.net.http.HttpResponseCache").getMethod("getInstalled", new Class[0]).invoke(null, new Object[0]);
            if (objInvoke != null) {
                objInvoke.getClass().getMethod("delete", new Class[0]).invoke(objInvoke, new Object[0]);
                objInvoke.getClass().getMethod("close", new Class[0]).invoke(objInvoke, new Object[0]);
            }
        } catch (Exception e) {
            Logger.debug(TAG, "android.net.http.HttpResponseCache not available, probably because we're running on a pre-ICS version of Android. Using com.integralblue.httpresponsecache.HttpHttpResponseCache.");
            try {
                HttpResponseCache.getInstalled().delete();
            } catch (Exception e2) {
                Logger.error(TAG, "Failed to remove com.integralblue.httpresponsecache.HttpResponseCache");
            }
        }
        b(context);
    }
}
