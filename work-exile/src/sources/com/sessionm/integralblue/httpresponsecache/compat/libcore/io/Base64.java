package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

import com.sessionm.integralblue.httpresponsecache.compat.Charsets;
import com.sessionm.integralblue.httpresponsecache.compat.Strings;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.util.EmptyArray;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Base64 {
    private static final byte[] map = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 54, 55, 56, 57, 43, 47};

    private Base64() {
    }

    public static byte[] decode(byte[] bArr) {
        return decode(bArr, bArr.length);
    }

    public static byte[] decode(byte[] bArr, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = (i / 4) * 3;
        if (i7 == 0) {
            return EmptyArray.BYTE;
        }
        byte[] bArr2 = new byte[i7];
        int i8 = 0;
        while (true) {
            byte b = bArr[i - 1];
            if (b != 10 && b != 13 && b != 32 && b != 9) {
                if (b != 61) {
                    break;
                }
                i8++;
            }
            i--;
        }
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        while (i9 < i) {
            byte b2 = bArr[i9];
            if (b2 == 10 || b2 == 13 || b2 == 32) {
                i2 = i10;
                i3 = i12;
                i4 = i11;
            } else if (b2 == 9) {
                i2 = i10;
                i3 = i12;
                i4 = i11;
            } else {
                if (b2 >= 65 && b2 <= 90) {
                    i5 = b2 - 65;
                } else if (b2 >= 97 && b2 <= 122) {
                    i5 = b2 - 71;
                } else if (b2 >= 48 && b2 <= 57) {
                    i5 = b2 + 4;
                } else if (b2 == 43) {
                    i5 = 62;
                } else if (b2 == 47) {
                    i5 = 63;
                } else {
                    return null;
                }
                int i13 = (i10 << 6) | ((byte) i5);
                if (i11 % 4 == 3) {
                    int i14 = i12 + 1;
                    bArr2[i12] = (byte) (i13 >> 16);
                    int i15 = i14 + 1;
                    bArr2[i14] = (byte) (i13 >> 8);
                    i6 = i15 + 1;
                    bArr2[i15] = (byte) i13;
                } else {
                    i6 = i12;
                }
                i4 = i11 + 1;
                i3 = i6;
                i2 = i13;
            }
            i9++;
            i11 = i4;
            i12 = i3;
            i10 = i2;
        }
        if (i8 > 0) {
            int i16 = i10 << (i8 * 6);
            int i17 = i12 + 1;
            bArr2[i12] = (byte) (i16 >> 16);
            if (i8 == 1) {
                i12 = i17 + 1;
                bArr2[i17] = (byte) (i16 >> 8);
            } else {
                i12 = i17;
            }
        }
        byte[] bArr3 = new byte[i12];
        System.arraycopy(bArr2, 0, bArr3, 0, i12);
        return bArr3;
    }

    public static String encode(byte[] bArr) {
        byte[] bArr2 = new byte[((bArr.length + 2) * 4) / 3];
        int length = bArr.length - (bArr.length % 3);
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            bArr2[i2] = map[(bArr[i] & 255) >> 2];
            int i4 = i3 + 1;
            bArr2[i3] = map[((bArr[i] & 3) << 4) | ((bArr[i + 1] & 255) >> 4)];
            int i5 = i4 + 1;
            bArr2[i4] = map[((bArr[i + 1] & 15) << 2) | ((bArr[i + 2] & 255) >> 6)];
            bArr2[i5] = map[bArr[i + 2] & 63];
            i += 3;
            i2 = i5 + 1;
        }
        switch (bArr.length % 3) {
            case 1:
                int i6 = i2 + 1;
                bArr2[i2] = map[(bArr[length] & 255) >> 2];
                int i7 = i6 + 1;
                bArr2[i6] = map[(bArr[length] & 3) << 4];
                int i8 = i7 + 1;
                bArr2[i7] = 61;
                i2 = i8 + 1;
                bArr2[i8] = 61;
                break;
            case 2:
                int i9 = i2 + 1;
                bArr2[i2] = map[(bArr[length] & 255) >> 2];
                int i10 = i9 + 1;
                bArr2[i9] = map[((bArr[length] & 3) << 4) | ((bArr[length + 1] & 255) >> 4)];
                int i11 = i10 + 1;
                bArr2[i10] = map[(bArr[length + 1] & 15) << 2];
                i2 = i11 + 1;
                bArr2[i11] = 61;
                break;
        }
        return Strings.construct(bArr2, 0, i2, Charsets.US_ASCII);
    }
}
