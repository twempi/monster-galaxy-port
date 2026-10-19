package com.badlogic.gdx.utils;

import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Base64Coder {
    private static byte[] map2;
    private static final String systemLineSeparator = System.getProperty("line.separator");
    private static char[] map1 = new char[64];

    static {
        int i;
        int i2 = 0;
        char c = 'A';
        while (true) {
            i = i2;
            if (c > 'Z') {
                break;
            }
            i2 = i + 1;
            map1[i] = c;
            c = (char) (c + 1);
        }
        char c2 = 'a';
        while (c2 <= 'z') {
            map1[i] = c2;
            c2 = (char) (c2 + 1);
            i++;
        }
        char c3 = '0';
        while (c3 <= '9') {
            map1[i] = c3;
            c3 = (char) (c3 + 1);
            i++;
        }
        int i3 = i + 1;
        map1[i] = SignatureVisitor.EXTENDS;
        int i4 = i3 + 1;
        map1[i3] = '/';
        map2 = new byte[128];
        for (int i5 = 0; i5 < map2.length; i5++) {
            map2[i5] = -1;
        }
        for (int i6 = 0; i6 < 64; i6++) {
            map2[map1[i6]] = (byte) i6;
        }
    }

    public static String encodeString(String s) {
        return new String(encode(s.getBytes()));
    }

    public static String encodeLines(byte[] in) {
        return encodeLines(in, 0, in.length, 76, systemLineSeparator);
    }

    public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
        int blockLen = (lineLen * 3) / 4;
        if (blockLen <= 0) {
            throw new IllegalArgumentException();
        }
        int lines = ((iLen + blockLen) - 1) / blockLen;
        int bufLen = (((iLen + 2) / 3) * 4) + (lineSeparator.length() * lines);
        StringBuilder buf = new StringBuilder(bufLen);
        int ip = 0;
        while (ip < iLen) {
            int l = Math.min(iLen - ip, blockLen);
            buf.append(encode(in, iOff + ip, l));
            buf.append(lineSeparator);
            ip += l;
        }
        return buf.toString();
    }

    public static char[] encode(byte[] in) {
        return encode(in, 0, in.length);
    }

    public static char[] encode(byte[] in, int iLen) {
        return encode(in, 0, iLen);
    }

    public static char[] encode(byte[] in, int iOff, int iLen) {
        int i1;
        int ip;
        int i2;
        int ip2;
        int oDataLen = ((iLen * 4) + 2) / 3;
        int oLen = ((iLen + 2) / 3) * 4;
        char[] out = new char[oLen];
        int iEnd = iOff + iLen;
        int op = 0;
        int ip3 = iOff;
        while (ip3 < iEnd) {
            int ip4 = ip3 + 1;
            int i0 = in[ip3] & 255;
            if (ip4 < iEnd) {
                ip = ip4 + 1;
                i1 = in[ip4] & 255;
            } else {
                i1 = 0;
                ip = ip4;
            }
            if (ip < iEnd) {
                ip2 = ip + 1;
                i2 = in[ip] & 255;
            } else {
                i2 = 0;
                ip2 = ip;
            }
            int o0 = i0 >>> 2;
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 15) << 2) | (i2 >>> 6);
            int o3 = i2 & 63;
            int op2 = op + 1;
            out[op] = map1[o0];
            int op3 = op2 + 1;
            out[op2] = map1[o1];
            out[op3] = op3 < oDataLen ? map1[o2] : SignatureVisitor.INSTANCEOF;
            int op4 = op3 + 1;
            out[op4] = op4 < oDataLen ? map1[o3] : SignatureVisitor.INSTANCEOF;
            op = op4 + 1;
            ip3 = ip2;
        }
        return out;
    }

    public static String decodeString(String s) {
        return new String(decode(s));
    }

    public static byte[] decodeLines(String s) {
        char[] buf = new char[s.length()];
        int p = 0;
        for (int ip = 0; ip < s.length(); ip++) {
            char c = s.charAt(ip);
            if (c != ' ' && c != '\r' && c != '\n' && c != '\t') {
                buf[p] = c;
                p++;
            }
        }
        return decode(buf, 0, p);
    }

    public static byte[] decode(String s) {
        return decode(s.toCharArray());
    }

    public static byte[] decode(char[] in) {
        return decode(in, 0, in.length);
    }

    public static byte[] decode(char[] in, int iOff, int iLen) {
        char c;
        char c2;
        int ip;
        int op;
        int op2;
        if (iLen % 4 != 0) {
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        }
        while (iLen > 0 && in[(iOff + iLen) - 1] == '=') {
            iLen--;
        }
        int oLen = (iLen * 3) / 4;
        byte[] out = new byte[oLen];
        int iEnd = iOff + iLen;
        int op3 = 0;
        for (int ip2 = iOff; ip2 < iEnd; ip2 = ip) {
            int ip3 = ip2 + 1;
            char c3 = in[ip2];
            int ip4 = ip3 + 1;
            char c4 = in[ip3];
            if (ip4 < iEnd) {
                c = in[ip4];
                ip4++;
            } else {
                c = 'A';
            }
            if (ip4 < iEnd) {
                ip = ip4 + 1;
                c2 = in[ip4];
            } else {
                c2 = 'A';
                ip = ip4;
            }
            if (c3 > 127 || c4 > 127 || c > 127 || c2 > 127) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            int b0 = map2[c3];
            int b1 = map2[c4];
            int b2 = map2[c];
            int b3 = map2[c2];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            int o0 = (b0 << 2) | (b1 >>> 4);
            int o1 = ((b1 & 15) << 4) | (b2 >>> 2);
            int o2 = ((b2 & 3) << 6) | b3;
            int op4 = op3 + 1;
            out[op3] = (byte) o0;
            if (op4 < oLen) {
                op = op4 + 1;
                out[op4] = (byte) o1;
            } else {
                op = op4;
            }
            if (op < oLen) {
                op2 = op + 1;
                out[op] = (byte) o2;
            } else {
                op2 = op;
            }
            op3 = op2;
        }
        return out;
    }

    private Base64Coder() {
    }
}
