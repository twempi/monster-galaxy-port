package com.MdotM;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.tapjoy.TapjoyConstants;
import java.net.URLEncoder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MdotmReceiver extends BroadcastReceiver {
    public String postBackUrl = "";
    public String deviceId = "0";
    public String androidId = "0";
    private Thread makePostBack = new Thread() { // from class: com.MdotM.MdotmReceiver.1
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(MdotmReceiver.this.postBackUrl);
                httpClient.execute(httpGet);
            } catch (Exception e) {
            }
        }
    };

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String referrer;
        String packageName;
        try {
            referrer = intent.getStringExtra("referrer");
            if (referrer == null) {
                referrer = "null_referrer_found";
            }
        } catch (Exception e) {
            referrer = "exception_found_retrieving_referrer";
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            this.deviceId = telephonyManager.getDeviceId();
        } catch (Exception e2) {
            this.deviceId = "0";
        }
        try {
            this.androidId = Settings.Secure.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
        } catch (Exception e3) {
            this.androidId = "0";
        }
        Package packageObj = getClass().getPackage();
        if (packageObj == null) {
            packageName = "null_package";
        } else {
            packageName = packageObj.getName();
        }
        this.postBackUrl = "http://ads.mdotm.com/ads/receiver.php?referrer=" + URLEncoder.encode(referrer) + "&package=" + URLEncoder.encode(packageName) + "&deviceid=" + URLEncoder.encode(this.deviceId) + "&androidid=" + URLEncoder.encode(this.androidId);
        this.makePostBack.start();
    }
}
