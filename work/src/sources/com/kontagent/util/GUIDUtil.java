package com.kontagent.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import oauth.signpost.OAuth;

/* JADX INFO: loaded from: classes.dex */
public class GUIDUtil {
    public static String generateTrackingId() {
        return shaUUID().substring(0, 16);
    }

    public static String generateShortTrackingId() {
        return shaUUID().substring(0, 8);
    }

    public static Long generateSenderId() {
        return Long.valueOf(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private static String shaUUID() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(generateUUID().getBytes(OAuth.ENCODING));
            byte[] tempSha = digest.digest();
            return byteArrayToHexString(tempSha);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static String byteArrayToHexString(byte[] b) throws Exception {
        String result = "";
        for (byte b2 : b) {
            result = result + Integer.toString((b2 & 255) + 256, 16).substring(1);
        }
        return result;
    }
}
