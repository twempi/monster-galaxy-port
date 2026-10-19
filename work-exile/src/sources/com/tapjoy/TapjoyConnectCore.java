package com.tapjoy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.chartboost.sdk.CBUtility;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.w3c.dom.Document;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TapjoyConnectCore {
    public static final String TAPJOY_CONNECT = "TapjoyConnect";
    private long elapsed_time = 0;
    private Timer timer = null;
    private static Context context = null;
    private static TapjoyConnectCore tapjoyConnectCore = null;
    private static TapjoyURLConnection tapjoyURLConnection = null;
    private static String androidID = "";
    private static String deviceID = "";
    private static String deviceModel = "";
    private static String deviceManufacturer = "";
    private static String deviceType = "";
    private static String deviceOSVersion = "";
    private static String deviceCountryCode = "";
    private static String deviceLanguage = "";
    private static String appID = "";
    private static String appVersion = "";
    private static String libraryVersion = "";
    private static String deviceScreenDensity = "";
    private static String deviceScreenLayoutSize = "";
    private static String userID = "";
    private static String platformName = "";
    private static String carrierName = "";
    private static String carrierCountryCode = "";
    private static String mobileCountryCode = "";
    private static String connectionType = "";
    private static String secretKey = "";
    private static String clientPackage = "";
    private static String referralURL = "";
    private static boolean videoEnabled = false;
    private static String videoIDs = "";
    private static float currencyMultiplier = 1.0f;
    private static String paidAppActionID = null;

    public static TapjoyConnectCore getInstance() {
        return tapjoyConnectCore;
    }

    public static void requestTapjoyConnect(Context applicationContext, String app_ID, String secret_Key) {
        appID = app_ID;
        secretKey = secret_Key;
        tapjoyConnectCore = new TapjoyConnectCore(applicationContext);
    }

    public TapjoyConnectCore(Context applicationContext) {
        context = applicationContext;
        tapjoyURLConnection = new TapjoyURLConnection();
        init();
        TapjoyLog.i("TapjoyConnect", "URL parameters: " + getURLParams());
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyConnectCore.1
            @Override // java.lang.Runnable
            public void run() {
                TapjoyLog.i("TapjoyConnect", "starting connect call...");
                String connectURLParams = TapjoyConnectCore.getURLParams();
                if (!TapjoyConnectCore.referralURL.equals("")) {
                    connectURLParams = String.valueOf(connectURLParams) + "&" + TapjoyConnectCore.referralURL;
                }
                String result = TapjoyConnectCore.tapjoyURLConnection.connectToURL("https://ws.tapjoyads.com/connect?", connectURLParams);
                if (result != null && TapjoyConnectCore.handleConnectResponse(result)) {
                    TapjoyLog.i("TapjoyConnect", "Successfully connected to tapjoy site.");
                }
            }
        }).start();
    }

    public static String getURLParams() {
        String urlParams = String.valueOf(getGenericURLParams()) + "&";
        long time = System.currentTimeMillis() / 1000;
        String verifier = getVerifier(time);
        return String.valueOf(String.valueOf(urlParams) + "timestamp=" + time + "&") + "verifier=" + verifier;
    }

    public static String getGenericURLParams() {
        String urlParams = String.valueOf("") + "app_id=" + Uri.encode(appID) + "&";
        return String.valueOf(urlParams) + getParamsWithoutAppID();
    }

    private static String getParamsWithoutAppID() {
        String urlParams = String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf("") + "android_id=" + androidID + "&") + "udid=" + Uri.encode(deviceID) + "&") + "device_name=" + Uri.encode(deviceModel) + "&") + "device_manufacturer=" + Uri.encode(deviceManufacturer) + "&") + "device_type=" + Uri.encode(deviceType) + "&") + "os_version=" + Uri.encode(deviceOSVersion) + "&") + "country_code=" + Uri.encode(deviceCountryCode) + "&") + "language_code=" + Uri.encode(deviceLanguage) + "&") + "app_version=" + Uri.encode(appVersion) + "&") + "library_version=" + Uri.encode(libraryVersion) + "&") + "platform=" + Uri.encode(platformName) + "&") + "display_multiplier=" + Uri.encode(Float.toString(currencyMultiplier));
        if (carrierName.length() > 0) {
            urlParams = String.valueOf(String.valueOf(urlParams) + "&") + "carrier_name=" + Uri.encode(carrierName);
        }
        if (carrierCountryCode.length() > 0 && mobileCountryCode.length() > 0) {
            urlParams = String.valueOf(String.valueOf(String.valueOf(String.valueOf(urlParams) + "&") + "carrier_country_code=" + Uri.encode(carrierCountryCode)) + "&") + "mobile_country_code=" + Uri.encode(mobileCountryCode);
        }
        if (deviceScreenDensity.length() > 0 && deviceScreenLayoutSize.length() > 0) {
            urlParams = String.valueOf(String.valueOf(String.valueOf(urlParams) + "&") + "screen_density=" + Uri.encode(deviceScreenDensity) + "&") + "screen_layout_size=" + Uri.encode(deviceScreenLayoutSize);
        }
        connectionType = getConnectionType();
        if (connectionType.length() > 0) {
            return String.valueOf(String.valueOf(urlParams) + "&") + "connection_type=" + Uri.encode(connectionType);
        }
        return urlParams;
    }

    private void init() {
        PackageManager manager = context.getPackageManager();
        try {
            androidID = Settings.Secure.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
            deviceType = TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE;
            platformName = TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE;
            deviceModel = Build.MODEL;
            deviceManufacturer = Build.MANUFACTURER;
            deviceOSVersion = Build.VERSION.RELEASE;
            deviceCountryCode = Locale.getDefault().getCountry();
            deviceLanguage = Locale.getDefault().getLanguage();
            libraryVersion = TapjoyConstants.TJC_LIBRARY_VERSION_NUMBER;
            SharedPreferences settings = context.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    deviceID = telephonyManager.getDeviceId();
                    carrierName = telephonyManager.getNetworkOperatorName();
                    carrierCountryCode = telephonyManager.getNetworkCountryIso();
                    mobileCountryCode = telephonyManager.getNetworkOperator();
                }
                TapjoyLog.i("TapjoyConnect", "deviceID: " + deviceID);
                boolean invalidDeviceID = false;
                if (deviceID == null) {
                    TapjoyLog.e("TapjoyConnect", "Device id is null.");
                    invalidDeviceID = true;
                } else if (deviceID.length() == 0 || deviceID.equals("000000000000000") || deviceID.equals("0")) {
                    TapjoyLog.e("TapjoyConnect", "Device id is empty or an emulator.");
                    invalidDeviceID = true;
                } else {
                    deviceID = deviceID.toLowerCase();
                }
                TapjoyLog.i("TapjoyConnect", "ANDROID SDK VERSION: " + Build.VERSION.SDK);
                if (invalidDeviceID && Integer.parseInt(Build.VERSION.SDK) >= 9) {
                    TapjoyLog.i("TapjoyConnect", "TRYING TO GET SERIAL OF 2.3+ DEVICE...");
                    TapjoyHardwareUtil hardware = new TapjoyHardwareUtil();
                    deviceID = hardware.getSerial();
                    TapjoyLog.i("TapjoyConnect", "====================");
                    TapjoyLog.i("TapjoyConnect", "SERIAL: deviceID: [" + deviceID + "]");
                    TapjoyLog.i("TapjoyConnect", "====================");
                    if (deviceID == null) {
                        TapjoyLog.e("TapjoyConnect", "SERIAL: Device id is null.");
                        invalidDeviceID = true;
                    } else if (deviceID.length() == 0 || deviceID.equals("000000000000000") || deviceID.equals("0") || deviceID.equals(CBUtility.AUID_STATIC_ERROR)) {
                        TapjoyLog.e("TapjoyConnect", "SERIAL: Device id is empty or an emulator.");
                        invalidDeviceID = true;
                    } else {
                        deviceID = deviceID.toLowerCase();
                        invalidDeviceID = false;
                    }
                }
                if (invalidDeviceID) {
                    StringBuffer buff = new StringBuffer();
                    buff.append("EMULATOR");
                    String deviceId = settings.getString(TapjoyConstants.PREF_EMULATOR_DEVICE_ID, null);
                    if (deviceId != null && !deviceId.equals("")) {
                        deviceID = deviceId;
                    } else {
                        for (int i = 0; i < 32; i++) {
                            int randomChar = (int) (Math.random() * 100.0d);
                            int ch = randomChar % 30;
                            buff.append("1234567890abcdefghijklmnopqrstuvw".charAt(ch));
                        }
                        deviceID = buff.toString().toLowerCase();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(TapjoyConstants.PREF_EMULATOR_DEVICE_ID, deviceID);
                        editor.commit();
                    }
                }
            } catch (Exception e) {
                TapjoyLog.e("TapjoyConnect", "Error getting deviceID. e: " + e.toString());
                deviceID = null;
            }
            if (userID.length() == 0) {
                userID = deviceID;
            }
            try {
                if (Integer.parseInt(Build.VERSION.SDK) > 3) {
                    TapjoyDisplayMetricsUtil displayMetricsUtil = new TapjoyDisplayMetricsUtil(context);
                    deviceScreenDensity = new StringBuilder().append(displayMetricsUtil.getScreenDensity()).toString();
                    deviceScreenLayoutSize = new StringBuilder().append(displayMetricsUtil.getScreenLayoutSize()).toString();
                }
            } catch (Exception e2) {
                TapjoyLog.e("TapjoyConnect", "Error getting screen density/dimensions/layout: " + e2.toString());
            }
            String tempReferralURL = settings.getString(TapjoyConstants.PREF_REFERRAL_URL, null);
            if (tempReferralURL != null && !tempReferralURL.equals("")) {
                referralURL = tempReferralURL;
            }
            clientPackage = context.getPackageName();
            TapjoyLog.i("TapjoyConnect", "Metadata successfully loaded");
            TapjoyLog.i("TapjoyConnect", "APP_ID = [" + appID + "]");
            TapjoyLog.i("TapjoyConnect", "ANDROID_ID: [" + androidID + "]");
            TapjoyLog.i("TapjoyConnect", "CLIENT_PACKAGE = [" + clientPackage + "]");
            TapjoyLog.i("TapjoyConnect", "deviceID: [" + deviceID + "]");
            TapjoyLog.i("TapjoyConnect", "deviceName: [" + deviceModel + "]");
            TapjoyLog.i("TapjoyConnect", "deviceManufacturer: [" + deviceManufacturer + "]");
            TapjoyLog.i("TapjoyConnect", "deviceType: [" + deviceType + "]");
            TapjoyLog.i("TapjoyConnect", "libraryVersion: [" + libraryVersion + "]");
            TapjoyLog.i("TapjoyConnect", "deviceOSVersion: [" + deviceOSVersion + "]");
            TapjoyLog.i("TapjoyConnect", "COUNTRY_CODE: [" + deviceCountryCode + "]");
            TapjoyLog.i("TapjoyConnect", "LANGUAGE_CODE: [" + deviceLanguage + "]");
            TapjoyLog.i("TapjoyConnect", "density: [" + deviceScreenDensity + "]");
            TapjoyLog.i("TapjoyConnect", "screen_layout: [" + deviceScreenLayoutSize + "]");
            TapjoyLog.i("TapjoyConnect", "carrier_name: [" + carrierName + "]");
            TapjoyLog.i("TapjoyConnect", "carrier_country_code: [" + carrierCountryCode + "]");
            TapjoyLog.i("TapjoyConnect", "mobile_country_code: [" + mobileCountryCode + "]");
            TapjoyLog.i("TapjoyConnect", "referralURL: [" + referralURL + "]");
        } catch (Exception e3) {
            TapjoyLog.e("TapjoyConnect", "Error initializing Tapjoy parameters.");
        }
    }

    private class PaidAppTimerTask extends TimerTask {
        private PaidAppTimerTask() {
        }

        /* synthetic */ PaidAppTimerTask(TapjoyConnectCore tapjoyConnectCore, PaidAppTimerTask paidAppTimerTask) {
            this();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TapjoyConnectCore.this.elapsed_time += TapjoyConstants.TIMER_INCREMENT;
            TapjoyLog.i("TapjoyConnect", "elapsed_time: " + TapjoyConnectCore.this.elapsed_time + " (" + ((TapjoyConnectCore.this.elapsed_time / 1000) / 60) + "m " + ((TapjoyConnectCore.this.elapsed_time / 1000) % 60) + "s)");
            SharedPreferences prefs = TapjoyConnectCore.context.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(TapjoyConstants.PREF_ELAPSED_TIME, TapjoyConnectCore.this.elapsed_time);
            editor.commit();
            if (TapjoyConnectCore.this.elapsed_time >= TapjoyConstants.PAID_APP_TIME) {
                TapjoyLog.i("TapjoyConnect", "timer done...");
                if (TapjoyConnectCore.paidAppActionID != null && TapjoyConnectCore.paidAppActionID.length() > 0) {
                    TapjoyLog.i("TapjoyConnect", "Calling PPA actionComplete...");
                    TapjoyConnectCore.this.actionComplete(TapjoyConnectCore.paidAppActionID);
                }
                cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean handleConnectResponse(String response) {
        String nodeValue;
        Document document = TapjoyUtil.buildDocument(response);
        return (document == null || (nodeValue = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Success"))) == null || !nodeValue.equals("true")) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handlePayPerActionResponse(String response) {
        Document document = TapjoyUtil.buildDocument(response);
        if (document != null) {
            String nodeValue = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Success"));
            if (nodeValue != null && nodeValue.equals("true")) {
                TapjoyLog.i("TapjoyConnect", "Successfully sent completed Pay-Per-Action to Tapjoy server.");
                return true;
            }
            TapjoyLog.e("TapjoyConnect", "Completed Pay-Per-Action call failed.");
        }
        return false;
    }

    public void release() {
        tapjoyConnectCore = null;
        tapjoyURLConnection = null;
        TapjoyLog.i("TapjoyConnect", "Releasing core static instance.");
    }

    public static String getAppID() {
        return appID;
    }

    public static String getDeviceID() {
        return deviceID;
    }

    public static String getUserID() {
        return userID;
    }

    public static String getVideoIDs() {
        return videoIDs;
    }

    public static String getCarrierName() {
        return carrierName;
    }

    public static String getConnectionType() {
        String type = "";
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
                switch (connectivityManager.getActiveNetworkInfo().getType()) {
                    case 1:
                    case 6:
                        type = "wifi";
                        break;
                    default:
                        type = "mobile";
                        break;
                }
                TapjoyLog.i("TapjoyConnect", "connectivity: " + connectivityManager.getActiveNetworkInfo().getType());
                TapjoyLog.i("TapjoyConnect", "connection_type: " + type);
            }
        } catch (Exception e) {
            TapjoyLog.e("TapjoyConnect", "getConnectionType error: " + e.toString());
        }
        return type;
    }

    public static String getClientPackage() {
        return clientPackage;
    }

    public static Context getContext() {
        return context;
    }

    public static String getVerifier(long time) {
        try {
            String verifier = TapjoyUtil.SHA256(String.valueOf(appID) + ":" + deviceID + ":" + time + ":" + secretKey);
            return verifier;
        } catch (Exception e) {
            TapjoyLog.e("TapjoyConnect", "getVerifier ERROR: " + e.toString());
            return "";
        }
    }

    public static String getAwardPointsVerifier(long time, int amount, String guid) {
        try {
            String verifier = TapjoyUtil.SHA256(String.valueOf(appID) + ":" + deviceID + ":" + time + ":" + secretKey + ":" + amount + ":" + guid);
            return verifier;
        } catch (Exception e) {
            TapjoyLog.e("TapjoyConnect", "getAwardPointsVerifier ERROR: " + e.toString());
            return "";
        }
    }

    public static void setUserID(String id) {
        userID = id;
        TapjoyLog.i("TapjoyConnect", "URL parameters: " + getURLParams());
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyConnectCore.2
            @Override // java.lang.Runnable
            public void run() {
                TapjoyLog.i("TapjoyConnect", "setUserID...");
                String connectURLParams = String.valueOf(TapjoyConnectCore.getURLParams()) + "&publisher_user_id=" + TapjoyConnectCore.getUserID();
                if (!TapjoyConnectCore.referralURL.equals("")) {
                    connectURLParams = String.valueOf(connectURLParams) + "&" + TapjoyConnectCore.referralURL;
                }
                String result = TapjoyConnectCore.tapjoyURLConnection.connectToURL("https://ws.tapjoyads.com/set_publisher_user_id?", connectURLParams);
                if (result != null) {
                    if (TapjoyConnectCore.handleConnectResponse(result)) {
                    }
                    TapjoyLog.i("TapjoyConnect", "setUserID successful...");
                }
            }
        }).start();
    }

    public static void setVideoIDs(String ids) {
        videoIDs = ids;
    }

    public static void setVideoEnabled(boolean enabled) {
        videoEnabled = enabled;
    }

    public static boolean isVideoEnabled() {
        return videoEnabled;
    }

    public static void setDebugDeviceID(String id) {
        deviceID = id;
        SharedPreferences settings = context.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TapjoyConstants.PREF_EMULATOR_DEVICE_ID, deviceID);
        editor.commit();
    }

    public static void saveTapPointsTotal(int total) {
        SharedPreferences settings = context.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(TapjoyConstants.PREF_LAST_TAP_POINTS, total);
        editor.commit();
    }

    public static int getLocalTapPointsTotal() {
        SharedPreferences settings = context.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
        int total = settings.getInt(TapjoyConstants.PREF_LAST_TAP_POINTS, -9999);
        return total;
    }

    public void actionComplete(String actionID) {
        TapjoyLog.i("TapjoyConnect", "actionComplete: " + actionID);
        String actionURLParams = "app_id=" + actionID + "&";
        String actionURLParams2 = String.valueOf(String.valueOf(actionURLParams) + getParamsWithoutAppID()) + "&";
        long time = System.currentTimeMillis() / 1000;
        String actionURLParams3 = String.valueOf(String.valueOf(actionURLParams2) + "timestamp=" + time + "&") + "verifier=" + getVerifier(time);
        TapjoyLog.i("TapjoyConnect", "PPA URL parameters: " + actionURLParams3);
        new Thread(new PPAThread(actionURLParams3)).start();
    }

    public void enablePaidAppWithActionID(String paidAppPayPerActionID) {
        TapjoyLog.i("TapjoyConnect", "enablePaidAppWithActionID: " + paidAppPayPerActionID);
        paidAppActionID = paidAppPayPerActionID;
        SharedPreferences prefs = context.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
        this.elapsed_time = prefs.getLong(TapjoyConstants.PREF_ELAPSED_TIME, 0L);
        TapjoyLog.i("TapjoyConnect", "paidApp elapsed: " + this.elapsed_time);
        if (this.elapsed_time >= TapjoyConstants.PAID_APP_TIME) {
            if (paidAppActionID != null && paidAppActionID.length() > 0) {
                TapjoyLog.i("TapjoyConnect", "Calling PPA actionComplete...");
                actionComplete(paidAppActionID);
                return;
            }
            return;
        }
        if (this.timer == null) {
            this.timer = new Timer();
            this.timer.schedule(new PaidAppTimerTask(this, null), TapjoyConstants.TIMER_INCREMENT, TapjoyConstants.TIMER_INCREMENT);
        }
    }

    public class PPAThread implements Runnable {
        private String params;

        public PPAThread(String urlParams) {
            this.params = urlParams;
        }

        @Override // java.lang.Runnable
        public void run() {
            String result = TapjoyConnectCore.tapjoyURLConnection.connectToURL("https://ws.tapjoyads.com/connect?", this.params);
            if (result != null) {
                TapjoyConnectCore.this.handlePayPerActionResponse(result);
            }
        }
    }

    public void setCurrencyMultiplier(float multiplier) {
        TapjoyLog.i("TapjoyConnect", "setVirtualCurrencyMultiplier: " + multiplier);
        currencyMultiplier = multiplier;
    }

    public float getCurrencyMultiplier() {
        return currencyMultiplier;
    }
}
