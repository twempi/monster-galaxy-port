package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Toast;
import com.sessionm.ui.SessionMActivity;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import oauth.signpost.OAuth;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class HandShake {
    private static final String HANDSHAKE_URL = "http://ads.mp.mydas.mobi/";
    private static final String HANDSHAKE_URL_PARMS = "appConfigServlet?apid=";
    static String apid;
    private static boolean forceRefresh;
    private static String overrideHandShakeURL;
    private static HandShake sharedInstance;
    private WeakReference<Context> appContextRef;
    private String[] cachedVideos;
    private WeakReference<Context> contextRef;
    private long lastHandShake;
    String mmdid;
    private String schemesList;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private LinkedHashMap<String, AdTypeHandShake> adTypeHandShakes = new LinkedHashMap<>();
    private ArrayList<Scheme> schemes = new ArrayList<>();
    private long deferredViewTimeout = 3600000;
    boolean kill = false;
    private long handShakeCallback = 86400000;
    boolean hdid = true;
    long creativeCacheTimeout = 259200000;
    private Runnable updateHandShakeRunnable = new Runnable() { // from class: com.millennialmedia.android.HandShake.2
        @Override // java.lang.Runnable
        public void run() {
            Context context = (Context) HandShake.this.contextRef.get();
            if (context == null) {
                context = (Context) HandShake.this.appContextRef.get();
            }
            if (context != null) {
                HandShake.sharedHandShake(context);
            }
        }
    };

    static synchronized HandShake sharedHandShake(Context context) {
        HandShake handShake;
        if (apid == null) {
            MMAdViewSDK.Log.e("No apid set for the handshake.");
            handShake = null;
        } else {
            if (sharedInstance == null) {
                sharedInstance = new HandShake(context);
            } else if (System.currentTimeMillis() - sharedInstance.lastHandShake > sharedInstance.handShakeCallback) {
                MMAdViewSDK.Log.d("Handshake expired, requesting new handshake from the server.");
                sharedInstance = new HandShake(context);
            }
            handShake = sharedInstance;
        }
        return handShake;
    }

    static synchronized void setHandShakeURL(Context context, String url) {
        try {
            if (url != null) {
                overrideHandShakeURL = url + HANDSHAKE_URL_PARMS;
            } else {
                overrideHandShakeURL = "http://ads.mp.mydas.mobi/appConfigServlet?apid=";
            }
            forceRefresh = true;
            sharedInstance = new HandShake(context);
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized boolean canRequestVideo(Context context, String adType) {
        AdTypeHandShake adTypeHandShake;
        adTypeHandShake = this.adTypeHandShakes.get(adType);
        return adTypeHandShake != null ? adTypeHandShake.canRequestVideo(context, adType) : true;
    }

    synchronized boolean canDisplayCachedAd(String adType, long cachedTime) {
        AdTypeHandShake adTypeHandShake;
        adTypeHandShake = this.adTypeHandShakes.get(adType);
        return adTypeHandShake != null ? adTypeHandShake.canDisplayCachedAd(cachedTime) : true;
    }

    synchronized void updateLastVideoViewedTime(Context context, String adType) {
        AdTypeHandShake adTypeHandShake = this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            adTypeHandShake.updateLastVideoViewedTime(context, adType);
        }
    }

    synchronized boolean isAdTypeDownloading(String adType) {
        AdTypeHandShake adTypeHandShake;
        adTypeHandShake = this.adTypeHandShakes.get(adType);
        return adTypeHandShake != null ? adTypeHandShake.downloading : false;
    }

    synchronized void lockAdTypeDownload(String adType) {
        AdTypeHandShake adTypeHandShake = this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            adTypeHandShake.downloading = true;
        }
    }

    synchronized void unlockAdTypeDownload(String adType) {
        AdTypeHandShake adTypeHandShake = this.adTypeHandShakes.get(adType);
        if (adTypeHandShake != null) {
            adTypeHandShake.downloading = false;
        }
    }

    void overrideAdRefresh(MMAdView adView) {
        AdTypeHandShake adTypeHandShake;
        if (adView.adType != null && (adTypeHandShake = this.adTypeHandShakes.get(adView.adType)) != null && adTypeHandShake.refreshInterval != null) {
            adView.refreshInterval = Integer.parseInt(adTypeHandShake.refreshInterval);
        }
    }

    void setMMdid(Context context, String newMMdid) {
        setMMdid(context, newMMdid, true);
    }

    /* JADX WARN: Code duplicated, block: B:11:0x001b A[Catch: all -> 0x0035, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:8:0x0011, B:9:0x0014, B:11:0x001b, B:14:0x0032), top: B:19:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:14:0x0032 A[Catch: all -> 0x0035, TRY_ENTER, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:8:0x0011, B:9:0x0014, B:11:0x001b, B:14:0x0032), top: B:19:0x0003 }] */
    synchronized void setMMdid(Context context, String newMMdid, boolean persist) {
        if (newMMdid == null) {
            this.mmdid = newMMdid;
            MMAdViewSDK.setMMdid(this.mmdid);
            if (persist) {
                SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("handshake_mmdid", this.mmdid);
                editor.commit();
            }
        } else {
            if (newMMdid.length() == 0 || newMMdid.equals("NULL")) {
                this.mmdid = null;
            } else {
                this.mmdid = newMMdid;
            }
            MMAdViewSDK.setMMdid(this.mmdid);
            if (persist) {
                SharedPreferences settings2 = context.getSharedPreferences("MillennialMediaSettings", 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putString("handshake_mmdid", this.mmdid);
                editor2.commit();
            }
        }
        throw th;
    }

    private HandShake() {
    }

    private HandShake(Context context) {
        this.contextRef = new WeakReference<>(context);
        this.appContextRef = new WeakReference<>(context.getApplicationContext());
        if (forceRefresh || !loadHandShake(context) || System.currentTimeMillis() - this.lastHandShake > this.handShakeCallback) {
            forceRefresh = false;
            this.lastHandShake = System.currentTimeMillis();
            new Thread(new Runnable() { // from class: com.millennialmedia.android.HandShake.1
                @Override // java.lang.Runnable
                public void run() {
                    HttpGetRequest getRequest = new HttpGetRequest();
                    Context tempContext = (Context) HandShake.this.contextRef.get();
                    if (tempContext == null) {
                        tempContext = (Context) HandShake.this.appContextRef.get();
                    }
                    if (tempContext != null) {
                        try {
                            StringBuilder handshakeUrlBuilder = new StringBuilder();
                            TreeMap<String, String> deviceValuesMap = new TreeMap<>();
                            deviceValuesMap.put("ua", "Android:" + Build.MODEL);
                            MMAdViewController.getUrlCommonValues(tempContext, deviceValuesMap);
                            for (Map.Entry<String, String> entry : deviceValuesMap.entrySet()) {
                                handshakeUrlBuilder.append(String.format("&%s=%s", entry.getKey(), URLEncoder.encode(entry.getValue(), OAuth.ENCODING)));
                            }
                            String handshakeUrl = (HandShake.overrideHandShakeURL == null ? "http://ads.mp.mydas.mobi/appConfigServlet?apid=" : HandShake.overrideHandShakeURL) + HandShake.apid + handshakeUrlBuilder.toString();
                            MMAdViewSDK.Log.v("Performing handshake: %s", handshakeUrl);
                            HttpResponse httpResponse = getRequest.get(handshakeUrl);
                            if (httpResponse != null) {
                                HandShake.this.deserializeFromObj(HandShake.this.parseJson(HttpGetRequest.convertStreamToString(httpResponse.getEntity().getContent())));
                                HandShake.this.saveHandShake(tempContext);
                                HandShake.this.handler.postDelayed(HandShake.this.updateHandShakeRunnable, HandShake.this.handShakeCallback);
                            }
                        } catch (Exception e) {
                            MMAdViewSDK.Log.e("Could not get a handshake. %s", e.getMessage());
                        }
                    }
                }
            }).start();
        }
    }

    synchronized String getSchemesList(Context context) {
        if (this.schemesList == null && this.schemes.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Scheme scheme : this.schemes) {
                if (scheme.checkAvailability(context)) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("," + scheme.id);
                    } else {
                        stringBuilder.append(Integer.toString(scheme.id));
                    }
                }
            }
            if (stringBuilder.length() > 0) {
                this.schemesList = stringBuilder.toString();
            }
        }
        return this.schemesList;
    }

    synchronized JSONArray getSchemesJSONArray(Context context) {
        JSONArray array;
        array = new JSONArray();
        if (this.schemes.size() > 0) {
            for (Scheme scheme : this.schemes) {
                if (scheme.checkAvailability(context)) {
                    try {
                        JSONObject schemeObject = new JSONObject();
                        schemeObject.put("scheme", scheme.scheme);
                        schemeObject.put("schemeid", scheme.id);
                        array.put(schemeObject);
                    } catch (JSONException e) {
                    }
                }
            }
        }
        return array;
    }

    private class Scheme {
        int id;
        String scheme;

        Scheme() {
        }

        Scheme(String scheme, int id) {
            this.scheme = scheme;
            this.id = id;
        }

        boolean checkAvailability(Context context) {
            Intent intent;
            if (this.scheme.contains("://")) {
                intent = new Intent("android.intent.action.VIEW", Uri.parse(this.scheme));
            } else {
                intent = new Intent("android.intent.action.VIEW", Uri.parse(this.scheme + "://"));
            }
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 65536);
            return list.size() > 0;
        }

        void deserializeFromObj(JSONObject schemeObject) {
            if (schemeObject != null) {
                this.scheme = schemeObject.optString("scheme", null);
                this.id = schemeObject.optInt("schemeid");
            }
        }
    }

    private class AdTypeHandShake {
        boolean downloading;
        String refreshInterval;
        long lastVideo = 0;
        long videoInterval = 0;

        AdTypeHandShake() {
        }

        boolean canRequestVideo(Context context, String adType) {
            MMAdViewSDK.Log.d("canRequestVideo() Current Time: %d Last Video: %d  Diff: %d  Video interval: %d", Long.valueOf(System.currentTimeMillis()), Long.valueOf(this.lastVideo / 1000), Long.valueOf((System.currentTimeMillis() - this.lastVideo) / 1000), Long.valueOf(this.videoInterval / 1000));
            return System.currentTimeMillis() - this.lastVideo > this.videoInterval;
        }

        boolean canDisplayCachedAd(long cachedTime) {
            return System.currentTimeMillis() - cachedTime < HandShake.this.deferredViewTimeout;
        }

        void updateLastVideoViewedTime(Context context, String adType) {
            this.lastVideo = System.currentTimeMillis();
            save(context, adType);
        }

        void deserializeFromObj(JSONObject adTypeObject) {
            if (adTypeObject != null) {
                this.videoInterval = adTypeObject.optLong("videointerval") * 1000;
                this.refreshInterval = adTypeObject.optString("adrefresh", null);
                if (this.refreshInterval != null && this.refreshInterval.equalsIgnoreCase("sdk")) {
                    this.refreshInterval = null;
                }
            }
        }

        boolean load(SharedPreferences settings, String adType) {
            boolean settingsFound = false;
            if (settings.contains("handshake_lastvideo_" + adType)) {
                this.lastVideo = settings.getLong("handshake_lastvideo_" + adType, this.lastVideo);
                settingsFound = true;
            }
            if (settings.contains("handshake_videointerval_" + adType)) {
                this.videoInterval = settings.getLong("handshake_videointerval_" + adType, this.videoInterval);
                settingsFound = true;
            }
            if (settings.contains("handshake_adrefresh_" + adType)) {
                this.refreshInterval = settings.getString("handshake_adrefresh_" + adType, null);
                return true;
            }
            return settingsFound;
        }

        void loadLastVideo(Context context, String adType) {
            SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
            if (settings != null && settings.contains("handshake_lastvideo_" + adType)) {
                this.lastVideo = settings.getLong("handshake_lastvideo_" + adType, this.lastVideo);
            }
        }

        void save(SharedPreferences.Editor editor, String adType) {
            editor.putLong("handshake_lastvideo_" + adType, this.lastVideo);
            editor.putLong("handshake_videointerval_" + adType, this.videoInterval);
            if (this.refreshInterval != null) {
                editor.putString("handshake_adrefresh_" + adType, this.refreshInterval);
            }
        }

        void save(Context context, String adType) {
            SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
            SharedPreferences.Editor editor = settings.edit();
            save(editor, adType);
            editor.commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject parseJson(String jsonString) {
        MMAdViewSDK.Log.d("JSON String: %s", jsonString);
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                MMAdViewSDK.Log.v(jsonObject.toString());
                if (jsonObject.has("mmishake")) {
                    return jsonObject.getJSONObject("mmishake");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deserializeFromObj(JSONObject handShakeObject) {
        final Context context = this.contextRef.get();
        if (handShakeObject != null) {
            try {
                JSONArray jsonArray = handShakeObject.optJSONArray("errors");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        if (jsonObject != null) {
                            final String message = jsonObject.optString("message", null);
                            String type = jsonObject.optString(SessionMActivity.INTENT_PARAM_TYPE, null);
                            if (message != null && type != null) {
                                if (type.equalsIgnoreCase("log")) {
                                    MMAdViewSDK.Log.e(message);
                                } else if (type.equalsIgnoreCase("prompt")) {
                                    this.handler.post(new Runnable() { // from class: com.millennialmedia.android.HandShake.3
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            try {
                                                Toast.makeText(context, "Error: " + message, 1).show();
                                            } catch (WindowManager.BadTokenException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
                JSONObject jsonObject2 = handShakeObject.optJSONObject("adtypes");
                if (jsonObject2 != null) {
                    String[] adTypes = MMAdView.getAdTypes();
                    for (int i2 = 0; i2 < adTypes.length; i2++) {
                        JSONObject adTypeObject = jsonObject2.optJSONObject(adTypes[i2]);
                        if (adTypeObject != null) {
                            AdTypeHandShake adTypeHandShake = new AdTypeHandShake();
                            adTypeHandShake.deserializeFromObj(adTypeObject);
                            adTypeHandShake.loadLastVideo(context, adTypes[i2]);
                            this.adTypeHandShakes.put(adTypes[i2], adTypeHandShake);
                        }
                    }
                }
                synchronized (this) {
                    JSONArray jsonArray2 = handShakeObject.optJSONArray("schemes");
                    if (jsonArray2 != null) {
                        if (this.schemes != null && this.schemes.size() > 0) {
                            this.schemes.removeAll(this.schemes);
                        }
                        for (int i3 = 0; i3 < jsonArray2.length(); i3++) {
                            JSONObject jsonObject3 = jsonArray2.optJSONObject(i3);
                            if (jsonObject3 != null) {
                                Scheme scheme = new Scheme();
                                scheme.deserializeFromObj(jsonObject3);
                                this.schemes.add(scheme);
                            }
                        }
                    }
                }
                this.deferredViewTimeout = handShakeObject.optLong("deferredviewtimeout", 3600L) * 1000;
                this.kill = handShakeObject.optBoolean("kill");
                this.handShakeCallback = handShakeObject.optLong("handshakecallback", 86400L) * 1000;
                this.hdid = handShakeObject.optBoolean("hdid", true);
                this.creativeCacheTimeout = handShakeObject.optLong("creativeCacheTimeout", 259200L) * 1000;
                JSONArray jsonArray3 = handShakeObject.optJSONArray("cachedVideos");
                if (jsonArray3 != null) {
                    this.cachedVideos = new String[jsonArray3.length()];
                    for (int i4 = 0; i4 < jsonArray3.length(); i4++) {
                        this.cachedVideos[i4] = jsonArray3.optString(i4);
                    }
                } else {
                    this.cachedVideos = new String[0];
                }
                if (this.cachedVideos.length > 0) {
                    PreCacheWorker.preCacheVideos(this.cachedVideos, context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean loadHandShake(Context context) {
        boolean settingsFound = false;
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        if (settings == null) {
            return false;
        }
        if (settings.contains("handshake_deferredviewtimeout")) {
            this.deferredViewTimeout = settings.getLong("handshake_deferredviewtimeout", this.deferredViewTimeout);
            settingsFound = true;
        }
        if (settings.contains("handshake_kill")) {
            this.kill = settings.getBoolean("handshake_kill", this.kill);
            settingsFound = true;
        }
        if (settings.contains("handshake_callback")) {
            this.handShakeCallback = settings.getLong("handshake_callback", this.handShakeCallback);
            settingsFound = true;
        }
        if (settings.contains("handshake_hdid")) {
            this.hdid = settings.getBoolean("handshake_hdid", this.hdid);
            settingsFound = true;
        }
        if (settings.contains("handshake_mmdid")) {
            setMMdid(context, settings.getString("handshake_mmdid", this.mmdid), false);
            settingsFound = true;
        }
        if (settings.contains("handshake_creativecachetimeout")) {
            this.creativeCacheTimeout = settings.getLong("handshake_creativecachetimeout", this.creativeCacheTimeout);
            settingsFound = true;
        }
        String[] adTypes = MMAdView.getAdTypes();
        for (int i = 0; i < adTypes.length; i++) {
            AdTypeHandShake adTypeHandShake = new AdTypeHandShake();
            if (adTypeHandShake.load(settings, adTypes[i])) {
                settingsFound = true;
                this.adTypeHandShakes.put(adTypes[i], adTypeHandShake);
            }
        }
        synchronized (this) {
            if (settings.contains("handshake_schemes")) {
                String schemesStr = settings.getString("handshake_schemes", "");
                if (schemesStr.length() > 0) {
                    String[] schemeStrings = schemesStr.split("\n");
                    for (String str : schemeStrings) {
                        String[] parts = str.split("\t");
                        if (parts.length >= 2) {
                            Scheme scheme = new Scheme(parts[0], Integer.parseInt(parts[1]));
                            this.schemes.add(scheme);
                        }
                    }
                    settingsFound = true;
                }
            }
        }
        if (settings.contains("handshake_cachedvideos")) {
            String videos = settings.getString("handshake_cachedvideos", "");
            if (videos.length() > 0) {
                this.cachedVideos = videos.split("\n");
            }
        }
        if (this.cachedVideos == null) {
            this.cachedVideos = new String[0];
        }
        if (settings.contains("handshake_lasthandshake")) {
            this.lastHandShake = settings.getLong("handshake_lasthandshake", this.lastHandShake);
            settingsFound = true;
        }
        if (settingsFound) {
            MMAdViewSDK.Log.d("Handshake successfully loaded from shared preferences.");
            if (System.currentTimeMillis() - this.lastHandShake < this.handShakeCallback) {
                this.handler.postDelayed(this.updateHandShakeRunnable, this.handShakeCallback - (System.currentTimeMillis() - this.lastHandShake));
            }
            if (this.cachedVideos.length > 0) {
                PreCacheWorker.preCacheVideos(this.cachedVideos, context);
            }
        }
        return settingsFound;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveHandShake(Context context) {
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("handshake_deferredviewtimeout", this.deferredViewTimeout);
        editor.putBoolean("handshake_kill", this.kill);
        editor.putLong("handshake_callback", this.handShakeCallback);
        editor.putBoolean("handshake_hdid", this.hdid);
        editor.putLong("handshake_creativecaetimeout", this.creativeCacheTimeout);
        for (String adType : this.adTypeHandShakes.keySet()) {
            AdTypeHandShake adTypeHandShake = this.adTypeHandShakes.get(adType);
            adTypeHandShake.save(editor, adType);
        }
        synchronized (this) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < this.schemes.size(); i++) {
                Scheme scheme = this.schemes.get(i);
                if (i > 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(scheme.scheme + "\t" + scheme.id);
            }
            editor.putString("handshake_schemes", stringBuilder.toString());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i2 = 0; i2 < this.cachedVideos.length; i2++) {
            if (i2 > 0) {
                stringBuilder2.append("\n");
            }
            stringBuilder2.append(this.cachedVideos[i2]);
        }
        if (stringBuilder2.length() > 0) {
            editor.putString("handshake_cachedvideos", stringBuilder2.toString());
        }
        editor.putLong("handshake_lasthandshake", this.lastHandShake);
        editor.commit();
    }
}
