package com.sessionm.a;

import android.util.Base64;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class a {
    public static final char[] a = {129, 254, 147, 128, 'l', 188, 'u', 129, 253, 'I', 'Y', 192, 141, 173, 129, '\\', 209, 220, 'k', 204, 'I', 'C', 173, 231, 'Y', 212, 202, 153, ' ', 'C', 204, 6, 190, 202, 'K', 176, 181, 7, 'K', 'M', 6, 'i', 'I', 24, 'J', '\'', 249, 18, 207, 'I', '%', '\t', 226, 143, 222, 241, 237, 225, 'Q', 157, '%', 134, 'A', '*', 218, 'm', 'a', 'g', 137, 'h', '&', 132, 208, 178, 156, '\\', 'z', 148, 211, 'C', 250, 241, '2', '$', SignatureVisitor.SUPER, 128, '6', 244, 237, 158, 6, '>', SignatureVisitor.EXTENDS, 145, 'q', 189, 237, '>', 182, 'n', 131, 'T', 'x', 191, 'V', 'f', 14, 255, 212, SignatureVisitor.INSTANCEOF, 250, '|', 'i', 190, 221, 27, 168, 165, 153, 'S', '\"', 152, 144, 185, SignatureVisitor.SUPER};

    private static byte[] a(char[] cArr) {
        byte[] bArr = new byte[cArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (cArr[i] ^ 1989);
        }
        return bArr;
    }

    public static String a(String str, char[] cArr, char[] cArr2) throws NoSuchAlgorithmException, InvalidKeyException {
        if (str == null) {
            return null;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(a(String.copyValueOf(a).concat(String.copyValueOf(cArr).concat(String.copyValueOf(cArr2))).toCharArray()), "HmacSHA256");
        byte[] bytes = str.getBytes();
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return Base64.encodeToString(mac.doFinal(bytes), 10);
    }
}
