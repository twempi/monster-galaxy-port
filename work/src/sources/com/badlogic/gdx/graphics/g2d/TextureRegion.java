package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
public class TextureRegion {
    int regionHeight;
    int regionWidth;
    Texture texture;
    float u;
    float u2;
    float v;
    float v2;

    public TextureRegion() {
    }

    public TextureRegion(Texture texture) {
        if (texture == null) {
            throw new IllegalArgumentException("texture cannot be null.");
        }
        this.texture = texture;
        setRegion(0, 0, texture.getWidth(), texture.getHeight());
    }

    public TextureRegion(Texture texture, int width, int height) {
        this.texture = texture;
        setRegion(0, 0, width, height);
    }

    public TextureRegion(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;
        setRegion(x, y, width, height);
    }

    public TextureRegion(Texture texture, float u, float v, float u2, float v2) {
        this.texture = texture;
        setRegion(u, v, u2, v2);
    }

    public TextureRegion(TextureRegion region) {
        setRegion(region);
    }

    public TextureRegion(TextureRegion region, int x, int y, int width, int height) {
        setRegion(region, x, y, width, height);
    }

    public void setRegion(Texture texture) {
        this.texture = texture;
        setRegion(0, 0, texture.getWidth(), texture.getHeight());
    }

    public void setRegion(int x, int y, int width, int height) {
        float invTexWidth = 1.0f / this.texture.getWidth();
        float invTexHeight = 1.0f / this.texture.getHeight();
        setRegion(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
        this.regionWidth = Math.abs(width);
        this.regionHeight = Math.abs(height);
    }

    public void setRegion(float u, float v, float u2, float v2) {
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        this.regionWidth = Math.round(Math.abs(u2 - u) * this.texture.getWidth());
        this.regionHeight = Math.round(Math.abs(v2 - v) * this.texture.getHeight());
    }

    public void setRegion(TextureRegion region) {
        this.texture = region.texture;
        setRegion(region.u, region.v, region.u2, region.v2);
    }

    public void setRegion(TextureRegion region, int x, int y, int width, int height) {
        this.texture = region.texture;
        setRegion(region.getRegionX() + x, region.getRegionY() + y, width, height);
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public float getU() {
        return this.u;
    }

    public void setU(float u) {
        this.u = u;
        this.regionWidth = Math.round(Math.abs(this.u2 - u) * this.texture.getWidth());
    }

    public float getV() {
        return this.v;
    }

    public void setV(float v) {
        this.v = v;
        this.regionHeight = Math.round(Math.abs(this.v2 - v) * this.texture.getHeight());
    }

    public float getU2() {
        return this.u2;
    }

    public void setU2(float u2) {
        this.u2 = u2;
        this.regionWidth = Math.round(Math.abs(u2 - this.u) * this.texture.getWidth());
    }

    public float getV2() {
        return this.v2;
    }

    public void setV2(float v2) {
        this.v2 = v2;
        this.regionHeight = Math.round(Math.abs(v2 - this.v) * this.texture.getHeight());
    }

    public int getRegionX() {
        return Math.round(this.u * this.texture.getWidth());
    }

    public void setRegionX(int x) {
        setU(x / this.texture.getWidth());
    }

    public int getRegionY() {
        return Math.round(this.v * this.texture.getHeight());
    }

    public void setRegionY(int y) {
        setV(y / this.texture.getHeight());
    }

    public int getRegionWidth() {
        return this.regionWidth;
    }

    public void setRegionWidth(int width) {
        setU2(this.u + (width / this.texture.getWidth()));
    }

    public int getRegionHeight() {
        return this.regionHeight;
    }

    public void setRegionHeight(int height) {
        setV2(this.v + (height / this.texture.getHeight()));
    }

    public void flip(boolean x, boolean y) {
        if (x) {
            float temp = this.u;
            this.u = this.u2;
            this.u2 = temp;
        }
        if (y) {
            float temp2 = this.v;
            this.v = this.v2;
            this.v2 = temp2;
        }
    }

    public boolean isFlipX() {
        return this.u > this.u2;
    }

    public boolean isFlipY() {
        return this.v > this.v2;
    }

    public void scroll(float xAmount, float yAmount) {
        if (xAmount != 0.0f) {
            float width = (this.u2 - this.u) * this.texture.getWidth();
            this.u = (this.u + xAmount) % 1.0f;
            this.u2 = this.u + (width / this.texture.getWidth());
        }
        if (yAmount != 0.0f) {
            float height = (this.v2 - this.v) * this.texture.getHeight();
            this.v = (this.v + yAmount) % 1.0f;
            this.v2 = this.v + (height / this.texture.getHeight());
        }
    }

    public TextureRegion[][] split(int tileWidth, int tileHeight) {
        int x;
        int x2 = getRegionX();
        int y = getRegionY();
        int width = this.regionWidth;
        int height = this.regionHeight;
        int rows = height / tileHeight;
        int cols = width / tileWidth;
        TextureRegion[][] tiles = (TextureRegion[][]) Array.newInstance((Class<?>) TextureRegion.class, rows, cols);
        int row = 0;
        while (row < rows) {
            int col = 0;
            while (col < cols) {
                x = x2;
                tiles[row][col] = new TextureRegion(this.texture, x, y, tileWidth, tileHeight);
                col++;
                x += tileWidth;
            }
            x = x2;
            row++;
            y += tileHeight;
        }
        return tiles;
    }

    public static TextureRegion[][] split(Texture texture, int tileWidth, int tileHeight) {
        TextureRegion region = new TextureRegion(texture);
        return region.split(tileWidth, tileHeight);
    }
}
