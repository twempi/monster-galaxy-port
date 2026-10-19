package com.gaiaonline.monstergalaxy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.crittercism.app.Crittercism;
import com.fiksu.asotracking.FiksuTrackingManager;
import com.flurry.android.FlurryAgent;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.kontagent.Kontagent;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyVideoNotifier;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Analytics implements com.gaiaonline.monstergalaxy.analytics.Analytics {
    private static final String ANALYTICS_PREFS = "Analytics_Prefs";
    private static final String FIRST_PURCHASE_KEY = "firstPurchaseDone";
    private String deviceId;
    private boolean fiskuInitialized;
    private Activity mainActivity = null;
    private boolean startedSession;

    public void setActivity(Activity activity) throws JSONException {
        String Tapjoy_App_ID;
        String TapJoy_Secret_Key;
        this.mainActivity = activity;
        Crittercism.init(activity.getApplicationContext(), getCrittercismAppId(), new JSONObject[0]);
        if (!this.fiskuInitialized) {
            try {
                FiksuTrackingManager.initialize((Application) activity.getApplicationContext());
                this.fiskuInitialized = true;
            } catch (Exception e) {
                Log.e(Game.TAG, "Error while trying to initialize FiksuTrackingManager");
            }
        }
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            Tapjoy_App_ID = "94d1a85d-fe7f-414b-9d1d-2175b37d0ebe";
            TapJoy_Secret_Key = "p7pTe3cO7lzsXwGmGcMf";
        } else {
            Tapjoy_App_ID = "d1d47252-7800-43c3-a660-03d0153f4e64";
            TapJoy_Secret_Key = "vRukEVKHAQAj1mE6B5ov";
        }
        TapjoyConnect.requestTapjoyConnect(activity.getApplicationContext(), Tapjoy_App_ID, TapJoy_Secret_Key);
        TapjoyVideoNotifier tapjoyVideoNotifier = new TapjoyVideoNotifier() { // from class: com.gaiaonline.monstergalaxy.app.Analytics.1
            @Override // com.tapjoy.TapjoyVideoNotifier
            public void videoReady() {
            }

            @Override // com.tapjoy.TapjoyVideoNotifier
            public void videoError(int statusCode) {
            }

            @Override // com.tapjoy.TapjoyVideoNotifier
            public void videoComplete() {
            }
        };
        TapjoyConnect.getTapjoyConnectInstance().initVideoAd(tapjoyVideoNotifier);
        startSession(activity);
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMLogEvent(String event) {
        Gdx.app.log("SessionM", "LogEvent: " + event);
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMPresentPortal() {
        Game.getAndroidPlatformHelper().showNativeProgressDialog();
        this.mainActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.Analytics.2
            @Override // java.lang.Runnable
            public void run() {
                Gdx.app.log("SessionM", "PresentPortal");
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMPresentActivity() {
        this.mainActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.Analytics.3
            @Override // java.lang.Runnable
            public void run() {
                Gdx.app.log("SessionM", "PresentActiviyty");
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMStartActivity() {
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMStopActivity() {
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMPauseActivity() {
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void sessionMResumeActivity() {
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void uploadPurchaseEvent(String username, double price) {
        FiksuTrackingManager.uploadPurchaseEvent(MonsterGalaxyApp.getInstance(), username, price, "USD");
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void uploadRegistrationEvent(String username) {
        FiksuTrackingManager.uploadRegistrationEvent(MonsterGalaxyApp.getInstance(), username);
    }

    public void startSession(Context context) {
        if (!this.startedSession) {
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
                FlurryAgent.onStartSession(context, "DPSDFBBY92BXB8GJ88BM");
            } else {
                FlurryAgent.onStartSession(context, "XT7GQYXCHYMBCD4U7ZIQ");
            }
            boolean isKindle = false;
            try {
                Class<?> build = Class.forName("android.os.Build");
                if (build != null) {
                    Field aField = build.getDeclaredField("MODEL");
                    String model = (String) aField.get(null);
                    if (model.equalsIgnoreCase("Kindle Fire")) {
                        isKindle = true;
                    }
                }
            } catch (Exception e) {
            }
            if (isKindle) {
                Kontagent.startSession("93a8d0bfd5e046be8fadbb27e4a4f39d", context, Kontagent.PRODUCTION_MODE);
            } else if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
                Kontagent.startSession("d3dc59ee95ab4bf297c8e7e724804ded", context, Kontagent.PRODUCTION_MODE);
            } else {
                Kontagent.startSession("eae4f1b006194c6fb81e8b87bd2c6afc", context, Kontagent.PRODUCTION_MODE);
            }
            sendUserInformation();
            sendDeviceInformation(context);
            Kontagent.pageRequest(null);
        }
        this.startedSession = true;
    }

    public void endSession(Context context) {
        try {
            FlurryAgent.onEndSession(context);
            Kontagent.stopSession();
        } catch (Exception e) {
            Log.e(Game.TAG, e.getMessage());
        }
        this.startedSession = false;
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void trackRevenue(String st3, String n, int v) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tu", "direct");
        parameters.put("st1", "monetization");
        parameters.put("st2", "in_app_purchase");
        parameters.put("st3", st3);
        parameters.put("s", this.deviceId);
        int netRevenue = (int) Math.round(((double) v) * 0.7d);
        Kontagent.revenueTracking(Integer.valueOf(netRevenue), parameters);
        if (isFirstPurchase()) {
            if (st3.equals("blue_coffee")) {
                trackEvent("monetization", "in_app_purchase", st3, "bc_first_purchase", "", this.deviceId);
                setFirstPurchaseDone();
            } else if (st3.equals("starseed")) {
                trackEvent("monetization", "in_app_purchase", st3, "ss_first_purchase", "", this.deviceId);
                setFirstPurchaseDone();
            } else if (st3.equals("moga_cash")) {
                trackEvent("monetization", "in_app_purchase", st3, "mc_first_purchase", "", this.deviceId);
                setFirstPurchaseDone();
            }
        }
    }

    private boolean isFirstPurchase() {
        Preferences prefs = Gdx.app.getPreferences(ANALYTICS_PREFS);
        return prefs == null || !prefs.contains(FIRST_PURCHASE_KEY);
    }

    public static boolean hasPurchases() {
        Preferences prefs = Gdx.app.getPreferences(ANALYTICS_PREFS);
        return prefs != null && prefs.contains(FIRST_PURCHASE_KEY);
    }

    private void setFirstPurchaseDone() {
        Preferences prefs = Gdx.app.getPreferences(ANALYTICS_PREFS);
        if (prefs != null) {
            prefs.putBoolean(FIRST_PURCHASE_KEY, true);
            prefs.flush();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.analytics.Analytics
    public void trackEvent(String st1, String st2, String st3, String n, String v, String i) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("st1", st1);
        parameters.put("st2", st2);
        parameters.put("st3", st3);
        parameters.put("n", n);
        parameters.put("v", v);
        parameters.put("i", i);
        parameters.put("s", this.deviceId);
        FlurryAgent.onEvent(n, parameters);
        Kontagent.customEvent(n, parameters);
    }

    private void sendUserInformation() {
        Trainer.Gender gender;
        String genderName;
        if (Game.getTrainer() != null && (gender = Game.getTrainer().getGender()) != null) {
            Map<String, String> userInfo = new HashMap<>();
            String genderName2 = gender.getName();
            if (genderName2 != null) {
                genderName = genderName2.substring(0, 1).toLowerCase();
            } else {
                genderName = "u";
            }
            userInfo.put("g", genderName);
            Kontagent.userInformation(userInfo);
        }
    }

    private void sendDeviceInformation(Context context) {
        Map<String, String> deviceInfo = new HashMap<>();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            String[] version = versionName.split(".");
            if (version.length > 0) {
                deviceInfo.put("v_maj", version[0]);
            }
            if (version.length > 1) {
                deviceInfo.put("v_min", version[1]);
            }
            if (version.length > 2) {
                deviceInfo.put("v_rev", version[2]);
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            String carrierName = tm.getNetworkOperatorName();
            if (carrierName != null && !carrierName.equals("")) {
                deviceInfo.put("c", carrierName);
            }
            this.deviceId = null;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Game.TAG, "Application version could not be retrieved.");
        }
        Kontagent.sendDeviceInformation(deviceInfo);
    }

    private String getCrittercismAppId() {
        if (PlatformHelper.getDeviceModel() == PlatformHelper.Model.KINDLE_FIRE) {
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                return "506edf3c01ed854ed5000007";
            }
            return "506edde7c7db5f4e44000004";
        }
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            return "4efc97cab093157fe3000080";
        }
        return "506dd41b067e7c591d00000a";
    }
}
