package com.sessionm.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.sessionm.logging.Logger;
import com.tapjoy.TapjoyConstants;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class c {
    private static final String TAG = "DeviceUudidFactory";
    protected static final String U = "device_id.xml";
    protected static final String V = "device_id";
    protected static UUID W;

    public c(Context context) {
        if (W == null) {
            synchronized (c.class) {
                if (W == null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(U, 0);
                    String string = sharedPreferences.getString(V, null);
                    if (string != null) {
                        W = UUID.fromString(string);
                    } else {
                        String string2 = Settings.Secure.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
                        try {
                            if ("9774d56d682e549c".equals(string2)) {
                                String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                                W = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            } else {
                                W = UUID.nameUUIDFromBytes(string2.getBytes("utf8"));
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        } catch (SecurityException e2) {
                            Logger.info(TAG, "the permission android.permission.READ_PHONE_STATE has not been defined in the manifest. Skipping network info...");
                            W = UUID.randomUUID();
                        }
                        sharedPreferences.edit().putString(V, W.toString()).commit();
                    }
                }
            }
        }
    }

    public UUID a() {
        return W;
    }
}
