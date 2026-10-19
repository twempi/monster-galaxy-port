package com.sessionm.cpi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import com.sessionm.a.b;
import com.sessionm.core.Session;
import com.sessionm.core.SessionMAndroidConfig;
import com.sessionm.json.JSONObject;
import com.sessionm.logging.Logger;
import com.sessionm.ui.ActivityController;
import twitter4j.conf.PropertyConfiguration;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class PackageReceiver extends BroadcastReceiver {
    private JSONObject activePackage;
    private String clicked;
    private String engagementId;
    private String packageName;

    public PackageReceiver() {
    }

    public PackageReceiver(JSONObject jSONObject) {
        this.activePackage = jSONObject.getJSONObject("cpc_info");
        this.engagementId = this.activePackage.getString("engagementId");
        this.packageName = this.activePackage.getString("scheme");
        this.activePackage.put("id", this.engagementId);
        this.clicked = String.valueOf(System.currentTimeMillis());
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Uri data;
        String action = intent.getAction();
        if (action != null && action.equalsIgnoreCase("android.intent.action.PACKAGE_ADDED") && (data = intent.getData()) != null) {
            String schemeSpecificPart = data.getSchemeSpecificPart();
            SharedPreferences sharedPreferences = context.getSharedPreferences("smInstallsFile", 3);
            Intent intent2 = new Intent();
            intent2.setAction("com.sessionm.TRANSACTION_RECEIVED");
            intent2.putExtra("id", this.engagementId);
            intent2.putExtra("clicked", this.clicked);
            for (String str : this.activePackage.names()) {
                intent2.putExtra(str, this.activePackage.getString(str));
            }
            sharedPreferences.edit().putString(schemeSpecificPart, this.activePackage.toString()).commit();
            ActivityController currentActivityController = Session.getSession().getCurrentActivityController();
            if (currentActivityController != null) {
                Logger.debug(getClass().getName(), "installed application " + schemeSpecificPart);
                if (this.engagementId == null) {
                    this.engagementId = "";
                }
                if (schemeSpecificPart.equalsIgnoreCase(this.packageName)) {
                    try {
                        context.unregisterReceiver(this);
                    } catch (IllegalArgumentException e) {
                        if (PropertyConfiguration.DEBUG.equalsIgnoreCase((String) SessionMAndroidConfig.getInstance().getConfigurationByKey(b.B))) {
                            e.printStackTrace();
                        }
                    }
                    currentActivityController.notifyApplicationInstalled(schemeSpecificPart, this.engagementId);
                }
            }
            context.sendBroadcast(intent2);
        }
    }
}
