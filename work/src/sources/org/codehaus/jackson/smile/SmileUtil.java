package org.codehaus.jackson.smile;

/* JADX INFO: loaded from: classes.dex */
public class SmileUtil {
    public static int zigzagEncode(int input) {
        return input < 0 ? (input << 1) ^ (-1) : input << 1;
    }

    public static int zigzagDecode(int encoded) {
        return (encoded & 1) == 0 ? encoded >>> 1 : (encoded >>> 1) ^ (-1);
    }

    public static long zigzagEncode(long input) {
        return input < 0 ? (input << 1) ^ (-1) : input << 1;
    }

    public static long zigzagDecode(long encoded) {
        return (1 & encoded) == 0 ? encoded >>> 1 : (encoded >>> 1) ^ (-1);
    }
}
