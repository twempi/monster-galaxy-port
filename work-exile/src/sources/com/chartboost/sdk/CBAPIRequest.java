package com.chartboost.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.tapjoy.TapjoyConstants;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CBAPIRequest {
    public static final String CB_PARAM_AD_ID = "ad_id";
    private static final String CB_PARAM_AUID = "auid";
    public static final String CB_PARAM_CACHE = "cache";
    private static final String CB_PARAM_COUNTRY = "country";
    private static final String CB_PARAM_HEIGHT = "h";
    public static final String CB_PARAM_IMEI = "imei";
    private static final String CB_PARAM_LANGUAGE = "language";
    private static final String CB_PARAM_MODEL = "model";
    private static final String CB_PARAM_OS = "os";
    private static final String CB_PARAM_SCALE = "scale";
    private static final String CB_PARAM_SDK = "sdk";
    private static final String CB_PARAM_UUID = "uuid";
    private static final String CB_PARAM_VERSION = "bundle";
    private static final String CB_PARAM_WIDTH = "w";
    private String action;
    private Map<String, String> body;
    private Context context;
    private Object contextInfoObject;
    private String controller;
    private List<String> params;
    private Map<String, String> query;
    private int displayWidth = 0;
    private int displayHeight = 0;

    public CBAPIRequest(Context context, String controller, String action) {
        this.context = context;
        this.controller = controller;
        this.action = action;
    }

    public void appendBodyArgument(String key, String value) {
        if (this.body == null) {
            this.body = new HashMap();
        }
        this.body.put(key, value);
    }

    public void appendQueryArgument(String key, String value) {
        if (this.query == null) {
            this.query = new HashMap();
        }
        this.query.put(key, value);
    }

    public void measureContainer(ViewGroup view) {
        if (view != null) {
            this.displayWidth = view.getMeasuredWidth();
            this.displayHeight = view.getMeasuredHeight();
            return;
        }
        int statusBarOffset = 0;
        if (Build.VERSION.SDK_INT <= 8) {
            Rect rectangle = new Rect();
            Window window = ((Activity) this.context).getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            statusBarOffset = rectangle.top;
        }
        Display display = ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay();
        this.displayWidth = display.getWidth();
        this.displayHeight = display.getHeight() - statusBarOffset;
    }

    public void appendDeviceInfoParams() throws Exception {
        appendDeviceInfoParams(null);
    }

    public void appendDeviceInfoParams(ViewGroup view) throws Exception {
        if (this.context == null) {
            throw new Exception();
        }
        if (Build.PRODUCT.equals(CB_PARAM_SDK)) {
            appendBodyArgument(CB_PARAM_MODEL, "Android Simulator");
            appendBodyArgument("uuid", CBUtility.AUID_STATIC_EMULATOR);
            appendBodyArgument(CB_PARAM_AUID, CBUtility.AUID_STATIC_EMULATOR);
        } else {
            appendBodyArgument(CB_PARAM_MODEL, Build.MODEL);
            appendBodyArgument("uuid", Settings.Secure.getString(this.context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID));
            appendBodyArgument(CB_PARAM_AUID, CBUtility.getAUID(this.context));
        }
        appendBodyArgument(CB_PARAM_OS, "Android " + Build.VERSION.RELEASE);
        appendBodyArgument(CB_PARAM_COUNTRY, Locale.getDefault().getCountry());
        appendBodyArgument(CB_PARAM_LANGUAGE, Locale.getDefault().getDisplayLanguage());
        appendBodyArgument(CB_PARAM_SDK, CBConstants.CB_SDK_VERSION);
        measureContainer(view);
        appendBodyArgument(CB_PARAM_WIDTH, new StringBuilder().append(this.displayWidth).toString());
        appendBodyArgument(CB_PARAM_HEIGHT, new StringBuilder().append(this.displayHeight).toString());
        appendBodyArgument(CB_PARAM_SCALE, new StringBuilder().append(this.context.getResources().getDisplayMetrics().density).toString());
        try {
            TelephonyManager tm = (TelephonyManager) this.context.getSystemService("phone");
            appendBodyArgument(CB_PARAM_IMEI, tm.getDeviceId() == null ? "" : tm.getDeviceId());
        } catch (Exception e) {
        }
        try {
            PackageInfo manager = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 128);
            String version = manager.versionName;
            appendBodyArgument(CB_PARAM_VERSION, version);
        } catch (Exception e2) {
        }
    }

    public void sign(String appId, String appSignature) throws Exception {
        String string = "controller:" + this.controller + ";action:" + this.action + ";";
        if (this.query != null) {
            string = String.valueOf(string) + serializeDictionaryToString(this.query);
        }
        if (this.body != null) {
            string = String.valueOf(string) + serializeDictionaryToString(this.body);
        }
        if (this.params != null) {
            int count = 0;
            for (String param : this.params) {
                string = String.valueOf(string) + count + ":" + param + ";";
                count++;
            }
        }
        String string2 = String.valueOf(string) + "app:" + appId + ";signature:" + appSignature + ";";
        Log.i("STRING", string2);
        byte[] stringBinary = string2.getBytes("ISO-8859-1");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBinary = md.digest(stringBinary);
        String hash = "";
        for (byte b : hashBinary) {
            hash = String.valueOf(hash) + String.format("%02x", Integer.valueOf(b & 255));
        }
        appendBodyArgument("app", appId);
        appendBodyArgument("signature", hash);
    }

    public String getController() {
        return this.controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getBody() {
        return this.body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getQuery() {
        return this.query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public List<String> getParams() {
        return this.params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public Object getContextInfoObject() {
        return this.contextInfoObject;
    }

    public void setContextInfoObject(Object contextInfoObject) {
        this.contextInfoObject = contextInfoObject;
    }

    private String serializeDictionaryToString(Map<String, String> dictionary) {
        String string = "";
        TreeSet<String> keys = new TreeSet<>(dictionary.keySet());
        for (String key : keys) {
            if (!key.equals("app") && !key.equals("signature")) {
                String value = dictionary.get(key);
                string = String.valueOf(string) + key + ":" + value + ";";
            }
        }
        return string;
    }
}
