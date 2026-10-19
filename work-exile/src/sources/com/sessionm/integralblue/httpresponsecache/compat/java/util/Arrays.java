package com.sessionm.integralblue.httpresponsecache.compat.java.util;

import com.sessionm.integralblue.httpresponsecache.compat.java.lang.ArrayIndexOutOfBoundsException;
import java.lang.reflect.Array;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Arrays {
    public static void checkOffsetAndCount(int i, int i2, int i3) {
        if ((i2 | i3) < 0 || i2 > i || i - i2 < i3) {
            throw new ArrayIndexOutOfBoundsException(i, i2, i3);
        }
    }

    public static boolean[] copyOfRange(boolean[] zArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = zArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        boolean[] zArr2 = new boolean[i3];
        System.arraycopy(zArr, i, zArr2, 0, iMin);
        return zArr2;
    }

    public static byte[] copyOfRange(byte[] bArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = bArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i, bArr2, 0, iMin);
        return bArr2;
    }

    public static char[] copyOfRange(char[] cArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = cArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        char[] cArr2 = new char[i3];
        System.arraycopy(cArr, i, cArr2, 0, iMin);
        return cArr2;
    }

    public static double[] copyOfRange(double[] dArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = dArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        double[] dArr2 = new double[i3];
        System.arraycopy(dArr, i, dArr2, 0, iMin);
        return dArr2;
    }

    public static float[] copyOfRange(float[] fArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = fArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        float[] fArr2 = new float[i3];
        System.arraycopy(fArr, i, fArr2, 0, iMin);
        return fArr2;
    }

    public static int[] copyOfRange(int[] iArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = iArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        int[] iArr2 = new int[i3];
        System.arraycopy(iArr, i, iArr2, 0, iMin);
        return iArr2;
    }

    public static long[] copyOfRange(long[] jArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = jArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        long[] jArr2 = new long[i3];
        System.arraycopy(jArr, i, jArr2, 0, iMin);
        return jArr2;
    }

    public static short[] copyOfRange(short[] sArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = sArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        short[] sArr2 = new short[i3];
        System.arraycopy(sArr, i, sArr2, 0, iMin);
        return sArr2;
    }

    public static <T> T[] copyOfRange(T[] tArr, int i, int i2) {
        int length = tArr.length;
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        T[] tArr2 = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i3));
        System.arraycopy(tArr, i, tArr2, 0, iMin);
        return tArr2;
    }

    public static <T, U> T[] copyOfRange(U[] uArr, int i, int i2, Class<? extends T[]> cls) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = uArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        T[] tArr = (T[]) ((Object[]) Array.newInstance(cls.getComponentType(), i3));
        System.arraycopy(uArr, i, tArr, 0, iMin);
        return tArr;
    }
}
