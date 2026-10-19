package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextureRegion {
    Texture texture;
    float u;
    float u2;
    float v;
    float v2;

    public TextureRegion() {
    }

    public TextureRegion(Texture texture) {
        this.texture = texture;
        setRegion(0, 0, texture.getWidth(), texture.getHeight());
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
    }

    public void setRegion(float u, float v, float u2, float v2) {
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
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
    }

    public float getV() {
        return this.v;
    }

    public void setV(float v) {
        this.v = v;
    }

    public float getU2() {
        return this.u2;
    }

    public void setU2(float u2) {
        this.u2 = u2;
    }

    public float getV2() {
        return this.v2;
    }

    public void setV2(float v2) {
        this.v2 = v2;
    }

    public int getRegionX() {
        return (int) (this.u * this.texture.getWidth());
    }

    public void setRegionX(int x) {
        setU(x / this.texture.getWidth());
    }

    public int getRegionY() {
        return (int) (this.v * this.texture.getHeight());
    }

    public void setRegionY(int y) {
        setV(y / this.texture.getHeight());
    }

    public int getRegionWidth() {
        return (int) ((this.u2 - this.u) * this.texture.getWidth());
    }

    public void setRegionWidth(int width) {
        setU2(this.u + (width / this.texture.getWidth()));
    }

    public int getRegionHeight() {
        return (int) ((this.v2 - this.v) * this.texture.getHeight());
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
        int x = getRegionX();
        int y = getRegionY();
        int width = getRegionWidth();
        int height = getRegionHeight();
        if (width < 0) {
            x -= width;
            width = -width;
        }
        if (height < 0) {
            y -= height;
            height = -height;
        }
        int rows = height / tileHeight;
        int cols = width / tileWidth;
        int startX = x;
        TextureRegion[][] tiles = (TextureRegion[][]) Array.newInstance((Class<?>) TextureRegion.class, rows, cols);
        int row = 0;
        while (row < rows) {
            int x2 = startX;
            int col = 0;
            while (col < cols) {
                tiles[row][col] = new TextureRegion(this.texture, x2, y, tileWidth, tileHeight);
                col++;
                x2 += tileWidth;
            }
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
