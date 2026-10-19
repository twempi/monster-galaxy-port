package com.badlogic.gdx;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.GLU;
import com.badlogic.gdx.graphics.Pixmap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Graphics {

    public enum GraphicsType {
        AndroidGL,
        JoglGL,
        LWJGL,
        Angle,
        WebGL
    }

    BufferFormat getBufferFormat();

    float getDeltaTime();

    float getDensity();

    DisplayMode getDesktopDisplayMode();

    DisplayMode[] getDisplayModes();

    int getFramesPerSecond();

    GL10 getGL10();

    GL11 getGL11();

    GL20 getGL20();

    GLCommon getGLCommon();

    GLU getGLU();

    int getHeight();

    float getPpcX();

    float getPpcY();

    float getPpiX();

    float getPpiY();

    GraphicsType getType();

    int getWidth();

    boolean isGL11Available();

    boolean isGL20Available();

    boolean setDisplayMode(int i, int i2, boolean z);

    boolean setDisplayMode(DisplayMode displayMode);

    void setIcon(Pixmap pixmap);

    void setTitle(String str);

    void setVSync(boolean z);

    boolean supportsDisplayModeChange();

    boolean supportsExtension(String str);

    public static class DisplayMode {
        public final int bitsPerPixel;
        public final int height;
        public final int refreshRate;
        public final int width;

        protected DisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
            this.width = width;
            this.height = height;
            this.refreshRate = refreshRate;
            this.bitsPerPixel = bitsPerPixel;
        }

        public String toString() {
            return this.width + "x" + this.height + ", bpp: " + this.bitsPerPixel + ", hz: " + this.refreshRate;
        }
    }

    public static class BufferFormat {
        public final int a;
        public final int b;
        public final boolean coverageSampling;
        public final int depth;
        public final int g;
        public final int r;
        public final int samples;
        public final int stencil;

        public BufferFormat(int r, int g, int b, int a, int depth, int stencil, int samples, boolean coverageSampling) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.depth = depth;
            this.stencil = stencil;
            this.samples = samples;
            this.coverageSampling = coverageSampling;
        }

        public String toString() {
            return "r: " + this.r + ", g: " + this.g + ", b: " + this.b + ", a: " + this.a + ", depth: " + this.depth + ", stencil: " + this.stencil + ", num samples: " + this.samples + ", coverage sampling: " + this.coverageSampling;
        }
    }
}
