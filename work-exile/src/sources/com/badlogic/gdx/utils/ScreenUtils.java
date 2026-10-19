package com.badlogic.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ScreenUtils {
    public static TextureRegion getFrameBufferTexture() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        return getFrameBufferTexture(0, 0, w, h);
    }

    public static TextureRegion getFrameBufferTexture(int x, int y, int w, int h) {
        int potW = MathUtils.nextPowerOfTwo(w);
        int potH = MathUtils.nextPowerOfTwo(h);
        Pixmap pixmap = new Pixmap(potW, potH, Pixmap.Format.RGBA8888);
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, potW, potH, 6408, 5121, pixels);
        Texture texture = new Texture(pixmap);
        TextureRegion textureRegion = new TextureRegion(texture, 0, h, w, -h);
        pixmap.dispose();
        return textureRegion;
    }

    public static byte[] getFrameBufferPixels(boolean flipY) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        return getFrameBufferPixels(0, 0, w, h, flipY);
    }

    public static byte[] getFrameBufferPixels(int x, int y, int w, int h, boolean flipY) {
        ByteBuffer pixels = BufferUtils.newByteBuffer(w * h * 4);
        Gdx.gl.glReadPixels(x, y, w, h, 6408, 5121, pixels);
        int numBytes = w * h * 4;
        byte[] lines = new byte[numBytes];
        if (flipY) {
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position(((h - i) - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
        } else {
            pixels.clear();
            pixels.get(lines);
        }
        return lines;
    }
}
