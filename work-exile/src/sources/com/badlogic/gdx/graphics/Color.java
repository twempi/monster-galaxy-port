package com.badlogic.gdx.graphics;

import com.badlogic.gdx.utils.NumberUtils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Color {
    public float a;
    public float b;
    public float g;
    public float r;
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);

    public Color() {
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    public Color(Color color) {
        set(color);
    }

    public Color set(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        clamp();
        return this;
    }

    public Color mul(Color color) {
        this.r *= color.r;
        this.g *= color.g;
        this.b *= color.b;
        this.a *= color.a;
        clamp();
        return this;
    }

    public Color mul(float value) {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
        clamp();
        return this;
    }

    public Color add(Color color) {
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        this.a += color.a;
        clamp();
        return this;
    }

    public Color sub(Color color) {
        this.r -= color.r;
        this.g -= color.g;
        this.b -= color.b;
        this.a -= color.a;
        clamp();
        return this;
    }

    public void clamp() {
        if (this.r < 0.0f) {
            this.r = 0.0f;
        } else if (this.r > 1.0f) {
            this.r = 1.0f;
        }
        if (this.g < 0.0f) {
            this.g = 0.0f;
        } else if (this.g > 1.0f) {
            this.g = 1.0f;
        }
        if (this.b < 0.0f) {
            this.b = 0.0f;
        } else if (this.b > 1.0f) {
            this.b = 1.0f;
        }
        if (this.a < 0.0f) {
            this.a = 0.0f;
        } else if (this.a > 1.0f) {
            this.a = 1.0f;
        }
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Color color = (Color) o;
        return Float.compare(color.a, this.a) == 0 && Float.compare(color.b, this.b) == 0 && Float.compare(color.g, this.g) == 0 && Float.compare(color.r, this.r) == 0;
    }

    public int hashCode() {
        int result = this.r != 0.0f ? NumberUtils.floatToIntBits(this.r) : 0;
        return (((((result * 31) + (this.g != 0.0f ? NumberUtils.floatToIntBits(this.g) : 0)) * 31) + (this.b != 0.0f ? NumberUtils.floatToIntBits(this.b) : 0)) * 31) + (this.a != 0.0f ? NumberUtils.floatToIntBits(this.a) : 0);
    }

    public String toString() {
        return Integer.toHexString(toIntBits());
    }

    public static float toFloatBits(int r, int g, int b, int a) {
        int color = (a << 24) | (b << 16) | (g << 8) | r;
        float floatColor = NumberUtils.intBitsToFloat((-16777217) & color);
        return floatColor;
    }

    public static int toIntBits(int r, int g, int b, int a) {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public float toFloatBits() {
        int color = (((int) (this.a * 255.0f)) << 24) | (((int) (this.b * 255.0f)) << 16) | (((int) (this.g * 255.0f)) << 8) | ((int) (this.r * 255.0f));
        return NumberUtils.intBitsToFloat((-16777217) & color);
    }

    public int toIntBits() {
        int color = (((int) (this.a * 255.0f)) << 24) | (((int) (this.b * 255.0f)) << 16) | (((int) (this.g * 255.0f)) << 8) | ((int) (this.r * 255.0f));
        return color;
    }

    public static float toFloatBits(float r, float g, float b, float a) {
        int color = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        return NumberUtils.intBitsToFloat((-16777217) & color);
    }

    public static int alpha(float alpha) {
        return (int) (255.0f * alpha);
    }

    public static int luminanceAlpha(float luminance, float alpha) {
        return (((int) (luminance * 255.0f)) << 8) | ((int) (255.0f * alpha));
    }

    public static int rgb565(float r, float g, float b) {
        return (((int) (r * 31.0f)) << 11) | (((int) (63.0f * g)) << 5) | ((int) (b * 31.0f));
    }

    public static int rgba4444(float r, float g, float b, float a) {
        return (((int) (r * 15.0f)) << 12) | (((int) (g * 15.0f)) << 8) | (((int) (b * 15.0f)) << 4) | ((int) (a * 15.0f));
    }

    public static int rgb888(float r, float g, float b) {
        return (((int) (r * 255.0f)) << 16) | (((int) (g * 255.0f)) << 8) | ((int) (b * 255.0f));
    }

    public static int rgba8888(float r, float g, float b, float a) {
        return (((int) (r * 255.0f)) << 24) | (((int) (g * 255.0f)) << 16) | (((int) (b * 255.0f)) << 8) | ((int) (a * 255.0f));
    }
}
