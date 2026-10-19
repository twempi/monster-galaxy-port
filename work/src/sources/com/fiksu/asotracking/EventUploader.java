package com.fiksu.asotracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.tapjoy.TapjoyConstants;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import oauth.signpost.OAuth;

/* JADX INFO: loaded from: classes.dex */
class EventUploader implements Runnable {
    private static final String FIKSU_SEPARATOR = "<FIKSU>";
    private static final int MAX_FAILED_URLS = 10;
    private final Context mContext;
    private final Map<String, String> mParameters;

    EventUploader(Context context, Map<String, String> parameters) {
        this.mParameters = parameters;
        this.mContext = context;
    }

    private String encodeParameter(String parameter) throws UnsupportedEncodingException {
        return URLEncoder.encode(parameter, OAuth.ENCODING);
    }

    private boolean launchedFromNotification() {
        synchronized ("shared preferences lock") {
            SharedPreferences preferences = EventTracker.getOurSharedPreferences(this.mContext);
            long now = new Date().getTime();
            long then = preferences.getLong("Fiksu.cd2MessageTime", 0L);
            return now - then < 180000;
        }
    }

    private String buildURL() {
        String deviceId;
        if (this.mContext == null) {
            Log.e("FiksuTracking", "Could not find context to use.  Please set it in your main Activity class using EventTracking.setContext().");
            return null;
        }
        String event = this.mParameters.get("event");
        Log.d("FiksuTracking", "Event: " + event);
        String hostname = String.valueOf("https://") + "asotrack1.fluentmobile.com/";
        if ((event.equals("Launch") || event.equals("Resume")) && launchedFromNotification()) {
            hostname = String.valueOf("https://") + "asotrack2.fluentmobile.com/";
            event = "Notification" + event;
        }
        String packageName = this.mContext.getPackageName();
        String url = String.valueOf(hostname) + "$Rev: 28663 $".split(" ")[1] + "/android/" + packageName + "/event?";
        try {
            String url2 = String.valueOf(url) + "appid=" + this.mContext.getPackageName();
            String aid = Settings.Secure.getString(this.mContext.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
            if (aid == null) {
                Log.e("FiksuTracking", "Could not retrieve android_id.  The android_id is not available on emulators running Android 2.1 or below.  Run the code on emulator 2.2 or better or an a device.");
                aid = "";
            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
                if (telephonyManager == null) {
                    Log.e("FiksuTracking", "Could not access telephonyManager.");
                    deviceId = "";
                } else {
                    deviceId = telephonyManager.getDeviceId();
                    if (deviceId == null || deviceId.length() == 0) {
                        Log.e("FiksuTracking", "Could not retrieve deviceId. ");
                        deviceId = "";
                    }
                }
            } catch (SecurityException e) {
                Log.e("FiksuTracking", "READ_PHONE_STATE permission not granted. Could not retrieve deviceId. ");
                deviceId = "";
            }
            String url3 = String.valueOf(String.valueOf(String.valueOf(url2) + "&deviceid=" + deviceId) + "&udid=" + aid) + "&device=" + encodeParameter(Build.MODEL);
            try {
                PackageManager packageManager = this.mContext.getPackageManager();
                url3 = String.valueOf(url3) + "&app_version=" + encodeParameter(packageManager.getPackageInfo(packageName, 0).versionName);
                String appName = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager).toString();
                if (appName != null) {
                    url3 = String.valueOf(url3) + "&app_name=" + encodeParameter(appName);
                }
            } catch (PackageManager.NameNotFoundException e2) {
                Log.e("FiksuTracking", "Could not access package: " + packageName);
            }
            String url4 = String.valueOf(String.valueOf(url3) + "&system_version=" + Build.VERSION.RELEASE) + "&system_name=" + encodeParameter(Build.PRODUCT);
            Locale locale = this.mContext.getResources().getConfiguration().locale;
            String url5 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(url4) + "&country=" + encodeParameter(locale.getCountry())) + "&lang=" + encodeParameter(locale.getLanguage())) + "&timezone=" + encodeParameter(TimeZone.getDefault().getDisplayName())) + "&gmtoffset=" + (TimeZone.getDefault().getRawOffset() / 1000)) + "&event=" + event;
            if (this.mParameters.get("username") != null) {
                url5 = String.valueOf(url5) + "&username=" + encodeParameter(this.mParameters.get("username"));
            }
            if (this.mParameters.get("tvalue") != null) {
                url5 = String.valueOf(url5) + "&tvalue=" + encodeParameter(this.mParameters.get("tvalue"));
            }
            if (this.mParameters.get("fvalue") != null) {
                url5 = String.valueOf(url5) + "&fvalue=" + encodeParameter(this.mParameters.get("fvalue"));
            }
            if (this.mParameters.get("ivalue") != null) {
                return String.valueOf(url5) + "&ivalue=" + this.mParameters.get("ivalue");
            }
            return url5;
        } catch (UnsupportedEncodingException e3) {
            Log.e("FiksuTracking", "Problem creating URL", e3);
            return null;
        }
    }

    private List<String> getSavedUrls() {
        String savedUrls;
        List<String> urls = new ArrayList<>();
        SharedPreferences preferences = EventTracker.getOurSharedPreferences(this.mContext);
        if (preferences != null && (savedUrls = preferences.getString("Fiksu.savedUrls", "")) != null && !savedUrls.equals("")) {
            String[] urlArray = savedUrls.split(FIKSU_SEPARATOR);
            for (String tempUrl : urlArray) {
                urls.add(tempUrl);
            }
        }
        return urls;
    }

    private void saveFailedUrls(List<String> failedUrls) {
        if (failedUrls.size() > 10) {
            failedUrls = new ArrayList<>(failedUrls.subList(failedUrls.size() - 10, failedUrls.size()));
        }
        String urlsToSave = "";
        if (failedUrls.size() > 0) {
            urlsToSave = String.valueOf("") + failedUrls.get(0);
            for (int i = 1; i < failedUrls.size(); i++) {
                urlsToSave = String.valueOf(urlsToSave) + FIKSU_SEPARATOR + failedUrls.get(i);
            }
        }
        SharedPreferences preferences = EventTracker.getOurSharedPreferences(this.mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Fiksu.savedUrls", urlsToSave);
        editor.commit();
    }

    private void uploadToTracking() {
        if (this.mContext == null) {
            Log.e("FiksuTracking", "Could not find context to use.  Please set it in your main Activity class using EventTracking.setContext().");
            return;
        }
        String url = buildURL();
        synchronized ("shared preferences lock") {
            List<String> urls = getSavedUrls();
            if (url != null) {
                urls.add(url);
                if (this.mParameters.get("event").equals("Conversion")) {
                    saveFailedUrls(urls);
                }
            }
            List<String> failedUrls = new ArrayList<>();
            for (String tempUrl : urls) {
                try {
                    if (!doUpload(tempUrl)) {
                        Log.e("FiksuTracking", "Upload failed for url.  Saving it for retry later: " + tempUrl);
                        failedUrls.add(tempUrl);
                    }
                } catch (MalformedURLException e) {
                    Log.e("FiksuTracking", tempUrl);
                    Log.e("FiksuTracking", e.toString());
                }
            }
            saveFailedUrls(failedUrls);
        }
    }

    private boolean doUpload(String url) throws MalformedURLException {
        URL fiksuUrl = new URL(url);
        try {
            HttpURLConnection fiksuConnection = (HttpURLConnection) fiksuUrl.openConnection();
            int responseCode = fiksuConnection.getResponseCode();
            if (responseCode == 200) {
                Log.d("FiksuTracking", "Successfully uploaded tracking information.");
                return true;
            }
            Log.e("FiksuTracking", "Failed to upload tracking information, bad response: " + responseCode);
            return responseCode < 500 || responseCode > 599;
        } catch (Exception e) {
            Log.e("FiksuTracking", "Failed to upload tracking information.");
            return false;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            uploadToTracking();
            synchronized (this) {
                notifyAll();
            }
        } catch (Throwable th) {
            synchronized (this) {
                notifyAll();
                throw th;
            }
        }
    }
}
