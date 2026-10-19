package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.chartboost.sdk.CBUtility;
import com.tapjoy.TapjoyConstants;
import java.security.MessageDigest;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class MMAdViewSDK {
    static final String BASEURL = "http://androidsdk.ads.mp.mydas.mobi/getAd.php5?";
    static final String BASE_PATH = "getAd.php5?";
    static final int CACHE_REQUEST_TIMEOUT = 30000;
    static final int CLOSE_ACTIVITY_DURATION = 400;
    public static final String DEFAULT_APID = "28911";
    public static final int DEFAULT_VIEWID = 1897808289;
    static final String EMPTY = "";
    static final String ERR_BROKENREF = "The reference to the ad view was broken.";
    static final String ERR_NOACTIVITY = "The ad view does not have a parent activity.";
    static final String ERR_NOT_MAIN_THREAD = "Only the main thread can access an MMAdView.";
    static final String HTTPS = "https";
    static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss ZZZZ";
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_NORMAL = 0;
    public static final int LOG_LEVEL_PRIVATE = 3;
    public static final int LOG_LEVEL_PRIVATE_VERBOSE = 4;
    public static final int LOG_LEVEL_VERBOSE = 2;
    static final int LOG_REQUEST_TIMEOUT = 10000;
    static final int MIN_REFRESH = 15;
    static final int OPEN_ACTIVITY_DURATION = 600;
    static final String PREFS_NAME = "MillennialMediaSettings";
    static final String PRIVATE_CACHE_DIR = ".mmsyscache";
    static final int REQUEST_TIMEOUT = 3000;
    public static final String SDKLOG = "MillennialMediaSDK";
    public static final String SDKVER = "4.6.0-12.07.16.a";
    public static boolean broadcastEvents;
    public static boolean debugMode;
    public static Location location;
    public static int logLevel;
    static String macId;
    static boolean disableAdMinRefresh = false;
    static MMDemographic demographic = new MMDemographic();
    static String COMMA = ",";
    static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static String getMMdidValue = null;
    private static String getAuidValue = null;
    private static String getHdidValue = null;
    private static String getAuidOrHdidValue = null;

    private MMAdViewSDK() {
    }

    public static void resetCache(Context context) {
        AdCache.resetCache(context);
    }

    static class Log {
        Log() {
        }

        static void i(String message) {
            android.util.Log.i(MMAdViewSDK.SDKLOG, message);
        }

        static void i(String format, Object... args) {
            android.util.Log.i(MMAdViewSDK.SDKLOG, String.format(format, args));
        }

        static void i(Throwable tr) {
            i(android.util.Log.getStackTraceString(tr));
        }

        static void w(String message) {
            android.util.Log.w(MMAdViewSDK.SDKLOG, message);
        }

        static void w(String format, Object... args) {
            android.util.Log.w(MMAdViewSDK.SDKLOG, String.format(format, args));
        }

        static void w(Throwable tr) {
            w(android.util.Log.getStackTraceString(tr));
        }

        static void e(String message) {
            android.util.Log.e(MMAdViewSDK.SDKLOG, message);
        }

        static void e(String format, Object... args) {
            android.util.Log.e(MMAdViewSDK.SDKLOG, String.format(format, args));
        }

        static void e(Throwable tr) {
            e(android.util.Log.getStackTraceString(tr));
        }

        static void d(String message) {
            if (MMAdViewSDK.logLevel >= 1) {
                android.util.Log.i(MMAdViewSDK.SDKLOG, "Diagnostic - " + message);
            }
        }

        static void d(String format, Object... args) {
            if (MMAdViewSDK.logLevel >= 1) {
                android.util.Log.i(MMAdViewSDK.SDKLOG, "Diagnostic - " + String.format(format, args));
            }
        }

        static void d(Throwable tr) {
            if (MMAdViewSDK.logLevel >= 1) {
                d(android.util.Log.getStackTraceString(tr));
            }
        }

        static void v(String message) {
            if (MMAdViewSDK.logLevel >= 2) {
                android.util.Log.i(MMAdViewSDK.SDKLOG, "Verbose - " + message);
            }
        }

        static void v(String format, Object... args) {
            if (MMAdViewSDK.logLevel >= 2) {
                android.util.Log.i(MMAdViewSDK.SDKLOG, "Verbose - " + String.format(format, args));
            }
        }

        static void v(Throwable tr) {
            if (MMAdViewSDK.logLevel >= 2) {
                v(android.util.Log.getStackTraceString(tr));
            }
        }

        static void p(String message) {
            if (MMAdViewSDK.logLevel > 2) {
                android.util.Log.i(MMAdViewSDK.SDKLOG, "Private - " + message);
            }
        }

        static void p(String format, Object... args) {
            if (MMAdViewSDK.logLevel > 2) {
                android.util.Log.i(MMAdViewSDK.SDKLOG, "Private - " + String.format(format, args));
            }
        }

        static void p(Throwable tr) {
            if (MMAdViewSDK.logLevel > 2) {
                p(android.util.Log.getStackTraceString(tr));
            }
        }
    }

    static class Event {
        public static final String ACTION_DISPLAY_STARTED = "millennialmedia.action.ACTION_DISPLAY_STARTED";
        public static final String ACTION_FETCH_FAILED = "millennialmedia.action.ACTION_FETCH_FAILED";
        public static final String ACTION_FETCH_FINISHED_CACHING = "millennialmedia.action.ACTION_FETCH_FINISHED_CACHING";
        public static final String ACTION_FETCH_STARTED_CACHING = "millennialmedia.action.ACTION_FETCH_STARTED_CACHING";
        public static final String ACTION_GETAD_FAILED = "millennialmedia.action.ACTION_GETAD_FAILED";
        public static final String ACTION_GETAD_SUCCEEDED = "millennialmedia.action.ACTION_GETAD_SUCCEEDED";
        public static final String ACTION_INTENT_STARTED = "millennialmedia.action.ACTION_INTENT_STARTED";
        public static final String ACTION_OVERLAY_CLOSED = "millennialmedia.action.ACTION_OVERLAY_CLOSED";
        public static final String ACTION_OVERLAY_OPENED = "millennialmedia.action.ACTION_OVERLAY_OPENED";
        public static final String ACTION_OVERLAY_TAP = "millennialmedia.action.ACTION_OVERLAY_TAP";
        public static final String CATEGORY_SDK = "millennialmedia.category.CATEGORY_SDK";
        public static final String INTENT_EMAIL = "email";
        public static final String INTENT_EXTERNAL_BROWSER = "browser";
        public static final String INTENT_MAPS = "geo";
        public static final String INTENT_MARKET = "market";
        public static final String INTENT_PHONE_CALL = "tel";
        public static final String INTENT_STREAMING_VIDEO = "streamingVideo";
        public static final String INTENT_TXT_MESSAGE = "sms";
        private static final String KEY_ERROR = "error";
        private static final String KEY_ID = "id";
        static final String KEY_INTENT_TYPE = "intentType";
        static final String KEY_INTERNAL_ID = "internalId";
        static final String KEY_PACKAGE_NAME = "packageName";

        Event() {
        }

        static void getAdSuccess(Context context, final MMAdView adView, final MMAdView.Request request) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.1
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdReturned(adView);
                        } catch (Exception e) {
                            Log.w("Exception raised in your MMAdListener: ", e);
                        }
                    }
                    if (request.requestListener != null) {
                        request.requestListener.requestSucceeded(adView);
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_GETAD_SUCCEEDED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                context.sendBroadcast(intent);
            }
        }

        static void getAdFailed(Context context, final MMAdView adView, final MMAdView.Request request, final MMError error) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.2
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdFailed(adView);
                        } catch (Exception e) {
                            Log.w("Exception raised in your MMAdListener: ", e);
                        }
                    }
                    if (request.requestListener != null) {
                        request.requestListener.requestFailed(adView, error);
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_GETAD_FAILED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                intent.putExtra(KEY_ERROR, error);
                context.sendBroadcast(intent);
            }
        }

        static void overlayOpened(Context context, final MMAdView adView) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.3
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdOverlayLaunched(adView);
                        } catch (Exception exception) {
                            Log.w("Exception raised in your MMAdListener: ", exception);
                        }
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_OVERLAY_OPENED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                context.sendBroadcast(intent);
            }
        }

        static void overlayClosed(Context context) {
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_OVERLAY_CLOSED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                context.sendBroadcast(intent);
            }
        }

        static void overlayTap(Context context) {
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_OVERLAY_TAP);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                context.sendBroadcast(intent);
            }
        }

        static void intentStarted(Context context, MMAdView adView, String intentType) {
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_INTENT_STARTED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                intent.putExtra(KEY_INTENT_TYPE, intentType);
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                context.sendBroadcast(intent);
            }
        }

        static void fetchStartedCaching(Context context, final MMAdView adView) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.4
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdRequestIsCaching(adView);
                        } catch (Exception e) {
                            Log.w("Exception raised in your MMAdListener: ", e);
                        }
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_FETCH_STARTED_CACHING);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                context.sendBroadcast(intent);
            }
        }

        static void fetchFinishedCaching(Context context, final MMAdView adView, final MMAdView.Request request) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.5
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdCachingCompleted(adView, true);
                        } catch (Exception e) {
                            Log.w("Exception raised in your MMAdListener: ", e);
                        }
                    }
                    if (request.requestListener != null) {
                        request.requestListener.requestSucceeded(adView);
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_FETCH_FINISHED_CACHING);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                context.sendBroadcast(intent);
            }
        }

        static void fetchFailed(Context context, final MMAdView adView, final MMAdView.Request request, final MMError error) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.6
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdCachingCompleted(adView, false);
                        } catch (Exception e) {
                            Log.w("Exception raised in your MMAdListener: ", e);
                        }
                    }
                    if (request.requestListener != null) {
                        request.requestListener.requestFailed(adView, error);
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_FETCH_FAILED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                intent.putExtra(KEY_ERROR, error);
                context.sendBroadcast(intent);
            }
        }

        static void displayStarted(Context context, final MMAdView adView) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewSDK.Event.7
                @Override // java.lang.Runnable
                public void run() {
                    if (adView != null && adView.listener != null) {
                        try {
                            adView.listener.MMAdOverlayLaunched(adView);
                        } catch (Exception exception) {
                            Log.w("Exception raised in your MMAdListener: ", exception);
                        }
                    }
                }
            });
            if (MMAdViewSDK.broadcastEvents) {
                Intent intent = new Intent(ACTION_DISPLAY_STARTED);
                intent.addCategory(CATEGORY_SDK);
                intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
                if (adView != null) {
                    intent.putExtra(KEY_ID, adView.getId());
                    intent.putExtra(KEY_INTERNAL_ID, adView.adViewId.longValue());
                }
                context.sendBroadcast(intent);
            }
        }

        static void requestFailed(Context context, MMAdView adView, MMAdView.Request request, MMError exception) {
            if (request.fetch) {
                fetchFailed(context, adView, request, exception);
            } else {
                getAdFailed(context, adView, request, exception);
            }
        }
    }

    static void runOnUiThread(Runnable action) {
        if (mainHandler.getLooper() == Looper.myLooper()) {
            action.run();
        } else {
            mainHandler.post(action);
        }
    }

    static boolean isUiThread() {
        return mainHandler.getLooper() == Looper.myLooper();
    }

    static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    static String getConnectionType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return CBUtility.AUID_STATIC_ERROR;
        }
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            int type = connectivityManager.getActiveNetworkInfo().getType();
            int subType = connectivityManager.getActiveNetworkInfo().getSubtype();
            if (type == 1) {
                return "wifi";
            }
            if (type == 0) {
                switch (subType) {
                    case 1:
                        return "gprs";
                    case 2:
                        return "edge";
                    case 3:
                        return "umts";
                    case 4:
                        return "cdma";
                    case 5:
                        return "evdo_0";
                    case 6:
                        return "evdo_a";
                    case 7:
                        return "1xrtt";
                    case 8:
                        return "hsdpa";
                    case 9:
                        return "hsupa";
                    case 10:
                        return "hspa";
                    case 11:
                        return "iden";
                    case 12:
                        return "evdo_b";
                    case 13:
                        return "lte";
                    case 14:
                        return "ehrpd";
                    case 15:
                        return "hspap";
                    default:
                        return CBUtility.AUID_STATIC_ERROR;
                }
            }
            return CBUtility.AUID_STATIC_ERROR;
        }
        return "offline";
    }

    static synchronized String getMMdid(Context context) {
        String string = null;
        synchronized (MMAdViewSDK.class) {
            if (getMMdidValue != null) {
                string = getMMdidValue;
            } else {
                String mmdid = Settings.Secure.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
                if (mmdid != null) {
                    StringBuilder mmdidBuilder = new StringBuilder("mmh_");
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        byte[] hashBytes = md.digest(mmdid.getBytes());
                        mmdidBuilder.append(byteArrayToString(hashBytes));
                        mmdidBuilder.append("_");
                        MessageDigest md2 = MessageDigest.getInstance("SHA1");
                        byte[] hashBytes2 = md2.digest(mmdid.getBytes());
                        mmdidBuilder.append(byteArrayToString(hashBytes2));
                        string = mmdidBuilder.toString();
                        getMMdidValue = string;
                    } catch (Exception e) {
                        Log.v(e.getMessage());
                    }
                }
            }
        }
        return string;
    }

    static synchronized void setMMdid(String value) {
        getMMdidValue = value;
    }

    public static String getAuid(Context context) {
        if (getAuidValue != null) {
            return getAuidValue;
        }
        String auid = "android_idandroid_id";
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm != null) {
            try {
                auid = tm.getDeviceId();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        if ((auid == null || auid.length() == 0) && context != null) {
            auid = Settings.Secure.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
        }
        if (auid == null || auid.length() == 0) {
            auid = null;
        }
        getAuidValue = auid;
        return auid;
    }

    private static String byteArrayToString(byte[] ba) {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba) {
            hex.append(String.format("%02X", Byte.valueOf(b)));
        }
        return hex.toString();
    }

    static String getHdid(Context context) {
        if (getHdidValue != null) {
            return getHdidValue;
        }
        String auid = getAuid(context);
        if (auid == null) {
            return null;
        }
        StringBuilder hdid = new StringBuilder("mmh_");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(auid.getBytes());
            hdid.append(byteArrayToString(hashBytes));
            hdid.append("_");
            MessageDigest md2 = MessageDigest.getInstance("SHA1");
            byte[] hashBytes2 = md2.digest(auid.getBytes());
            hdid.append(byteArrayToString(hashBytes2));
            String string = hdid.toString();
            getHdidValue = string;
            return string;
        } catch (Exception e) {
            Log.v(e.getMessage());
            return null;
        }
    }

    static String getAuidOrHdid(Context context) {
        boolean hdid = HandShake.sharedHandShake(context).hdid;
        if (getAuidOrHdidValue != null) {
            if (hdid && getAuidOrHdidValue.startsWith("mmh_")) {
                return getAuidOrHdidValue;
            }
            if (!hdid && !getAuidOrHdidValue.startsWith("mmh_")) {
                return getAuidOrHdidValue;
            }
        }
        if (hdid) {
            String hdid2 = getHdid(context);
            getAuidOrHdidValue = hdid2;
            return hdid2;
        }
        String auid = getAuid(context);
        getAuidOrHdidValue = auid;
        return auid;
    }

    static int getDpi(Context context) {
        return 160;
    }

    public static String hasAccelerometer(Context context) {
        if (context == null) {
            return "false";
        }
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        List<Sensor> sensors = sensorManager.getSensorList(1);
        boolean hasAccelerometer = false;
        for (Sensor aSensor : sensors) {
            if (aSensor.getType() == 1) {
                hasAccelerometer = true;
            }
        }
        return hasAccelerometer ? "true" : "false";
    }
}
