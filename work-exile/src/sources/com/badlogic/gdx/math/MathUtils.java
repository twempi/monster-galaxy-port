package com.badlogic.gdx.math;

import com.badlogic.gdx.utils.NumberUtils;
import java.util.Random;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MathUtils {
    private static final int ATAN2_BITS = 7;
    private static final int ATAN2_BITS2 = 14;
    private static final int ATAN2_COUNT = 16384;
    private static final int ATAN2_DIM;
    private static final int ATAN2_MASK = 16383;
    private static final double BIG_ENOUGH_CEIL;
    private static final double BIG_ENOUGH_FLOOR = 16384.0d;
    private static final int BIG_ENOUGH_INT = 16384;
    private static final double BIG_ENOUGH_ROUND = 16384.5d;
    private static final double CEIL = 0.9999999d;
    private static final float INV_ATAN2_DIM_MINUS_1;
    public static final float PI = 3.1415927f;
    private static final int SIN_BITS = 13;
    private static final int SIN_COUNT = 8192;
    private static final int SIN_MASK = 8191;
    private static final float[] atan2;
    private static final float degFull = 360.0f;
    private static final float degToIndex = 22.755556f;
    public static final float degreesToRadians = 0.017453292f;
    private static final float radFull = 6.2831855f;
    private static final float radToIndex = 1303.7972f;
    public static final float radiansToDegrees = 57.295776f;
    public static Random random;
    public static final float[] sin = new float[8192];
    public static final float[] cos = new float[8192];

    static {
        for (int i = 0; i < 8192; i++) {
            float a = ((i + 0.5f) / 8192.0f) * radFull;
            sin[i] = (float) Math.sin(a);
            cos[i] = (float) Math.cos(a);
        }
        for (int i2 = 0; i2 < 360; i2 += 90) {
            sin[((int) (i2 * degToIndex)) & SIN_MASK] = (float) Math.sin(i2 * 0.017453292f);
            cos[((int) (i2 * degToIndex)) & SIN_MASK] = (float) Math.cos(i2 * 0.017453292f);
        }
        ATAN2_DIM = (int) Math.sqrt(BIG_ENOUGH_FLOOR);
        INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);
        atan2 = new float[16384];
        for (int i3 = 0; i3 < ATAN2_DIM; i3++) {
            for (int j = 0; j < ATAN2_DIM; j++) {
                float x0 = i3 / ATAN2_DIM;
                float y0 = j / ATAN2_DIM;
                atan2[(ATAN2_DIM * j) + i3] = (float) Math.atan2(y0, x0);
            }
        }
        random = new Random();
        BIG_ENOUGH_CEIL = NumberUtils.longBitsToDouble(NumberUtils.doubleToLongBits(16385.0d) - 1);
    }

    public static final float sin(float rad) {
        return sin[((int) (radToIndex * rad)) & SIN_MASK];
    }

    public static final float cos(float rad) {
        return cos[((int) (radToIndex * rad)) & SIN_MASK];
    }

    public static final float sinDeg(float deg) {
        return sin[((int) (degToIndex * deg)) & SIN_MASK];
    }

    public static final float cosDeg(float deg) {
        return cos[((int) (degToIndex * deg)) & SIN_MASK];
    }

    public static final float atan2(float y, float x) {
        float mul;
        float add;
        if (x < 0.0f) {
            if (y < 0.0f) {
                y = -y;
                mul = 1.0f;
            } else {
                mul = -1.0f;
            }
            x = -x;
            add = -3.1415927f;
        } else {
            if (y < 0.0f) {
                y = -y;
                mul = -1.0f;
            } else {
                mul = 1.0f;
            }
            add = 0.0f;
        }
        float invDiv = 1.0f / ((x < y ? y : x) * INV_ATAN2_DIM_MINUS_1);
        int xi = (int) (x * invDiv);
        int yi = (int) (y * invDiv);
        return (atan2[(ATAN2_DIM * yi) + xi] + add) * mul;
    }

    public static final int random(int range) {
        return random.nextInt(range + 1);
    }

    public static final int random(int start, int end) {
        return random.nextInt((end - start) + 1) + start;
    }

    public static final boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static final float random() {
        return random.nextFloat();
    }

    public static final float random(float range) {
        return random.nextFloat() * range;
    }

    public static final float random(float start, float end) {
        return (random.nextFloat() * (end - start)) + start;
    }

    public static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        int value2 = value - 1;
        int value3 = value2 | (value2 >> 1);
        int value4 = value3 | (value3 >> 2);
        int value5 = value4 | (value4 >> 4);
        int value6 = value5 | (value5 >> 8);
        return (value6 | (value6 >> 16)) + 1;
    }

    public static boolean isPowerOfTwo(int value) {
        return value != 0 && ((value + (-1)) & value) == 0;
    }

    public static int floor(float x) {
        return ((int) (((double) x) + BIG_ENOUGH_FLOOR)) - 16384;
    }

    public static int floorPositive(float x) {
        return (int) x;
    }

    public static int ceil(float x) {
        return ((int) (((double) x) + BIG_ENOUGH_CEIL)) - 16384;
    }

    public static int ceilPositive(float x) {
        return (int) (((double) x) + CEIL);
    }

    public static int round(float x) {
        return ((int) (((double) x) + BIG_ENOUGH_ROUND)) - 16384;
    }

    public static int roundPositive(float x) {
        return (int) (0.5f + x);
    }
}
