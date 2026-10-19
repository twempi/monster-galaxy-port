package com.sessionm.cpi;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.tapjoy.TapjoyConstants;
import java.security.MessageDigest;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class ODIN {
    private static final String CHAR_SET = "iso-8859-1";
    private static final String SHA1_ALGORITHM = "SHA-1";
    private static final String TAG = "ODIN";

    public static String getODIN1(Context context) {
        String string;
        try {
            string = Settings.System.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
        } catch (Exception e) {
            try {
                string = Settings.System.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
            } catch (Exception e2) {
                Log.i(TAG, "Error generating ODIN-1: ", e2);
                return null;
            }
        }
        return SHA1(string);
    }

    private static String convertToHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            int i = (b >>> 4) & 15;
            int i2 = 0;
            while (true) {
                if (i < 0 || i > 9) {
                    stringBuffer.append((char) ((i - 10) + 97));
                } else {
                    stringBuffer.append((char) (i + 48));
                }
                int i3 = b & 15;
                int i4 = i2 + 1;
                if (i2 >= 1) {
                    break;
                }
                i2 = i4;
                i = i3;
            }
        }
        return stringBuffer.toString();
    }

    private static String SHA1(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1_ALGORITHM);
            byte[] bArr = new byte[40];
            messageDigest.update(str.getBytes(CHAR_SET), 0, str.length());
            return convertToHex(messageDigest.digest());
        } catch (Exception e) {
            Log.i(TAG, "Error generating generating SHA-1: ", e);
            return null;
        }
    }
}
