package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class NinePatch {
    public static final int BOTTOM_CENTER = 7;
    public static final int BOTTOM_LEFT = 6;
    public static final int BOTTOM_RIGHT = 8;
    public static final int MIDDLE_CENTER = 4;
    public static final int MIDDLE_LEFT = 3;
    public static final int MIDDLE_RIGHT = 5;
    public static final int TOP_CENTER = 1;
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 2;
    final TextureRegion[] patches;

    public NinePatch(Texture texture, int left, int right, int top, int bottom) {
        this(new TextureRegion(texture), left, right, top, bottom);
    }

    public NinePatch(TextureRegion region, int left, int right, int top, int bottom) {
        int middleWidth = (region.getRegionWidth() - left) - right;
        int middleHeight = (region.getRegionHeight() - top) - bottom;
        this.patches = new TextureRegion[]{new TextureRegion(region, 0, 0, left, top), new TextureRegion(region, left, 0, middleWidth, top), new TextureRegion(region, left + middleWidth, 0, right, top), new TextureRegion(region, 0, top, left, middleHeight), new TextureRegion(region, left, top, middleWidth, middleHeight), new TextureRegion(region, left + middleWidth, top, right, middleHeight), new TextureRegion(region, 0, top + middleHeight, left, bottom), new TextureRegion(region, left, top + middleHeight, middleWidth, bottom), new TextureRegion(region, left + middleWidth, top + middleHeight, right, bottom)};
    }

    public NinePatch(TextureRegion... patches) {
        if (patches.length != 9) {
            throw new IllegalArgumentException("NinePatch needs nine TextureRegions");
        }
        this.patches = patches;
        checkValidity();
    }

    private void checkValidity() {
        if (this.patches[6].getRegionWidth() != this.patches[0].getRegionWidth() || this.patches[6].getRegionWidth() != this.patches[3].getRegionWidth()) {
            throw new GdxRuntimeException("Left side patches must have the same width");
        }
        if (this.patches[8].getRegionWidth() != this.patches[2].getRegionWidth() || this.patches[8].getRegionWidth() != this.patches[5].getRegionWidth()) {
            throw new GdxRuntimeException("Right side patches must have the same width");
        }
        if (this.patches[6].getRegionHeight() != this.patches[7].getRegionHeight() || this.patches[6].getRegionHeight() != this.patches[8].getRegionHeight()) {
            throw new GdxRuntimeException("Bottom patches must have the same height");
        }
        if (this.patches[0].getRegionHeight() != this.patches[1].getRegionHeight() || this.patches[0].getRegionHeight() != this.patches[2].getRegionHeight()) {
            throw new GdxRuntimeException("Top patches must have the same height");
        }
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        float widthTopBottom = width - (this.patches[0].getRegionWidth() + this.patches[2].getRegionWidth());
        float heightLeftRight = height - (this.patches[0].getRegionHeight() + this.patches[6].getRegionHeight());
        batch.draw(this.patches[6], x, y, this.patches[6].getRegionWidth(), this.patches[6].getRegionHeight());
        batch.draw(this.patches[7], x + this.patches[6].getRegionWidth(), y, widthTopBottom, this.patches[7].getRegionHeight());
        batch.draw(this.patches[8], this.patches[6].getRegionWidth() + x + widthTopBottom, y, this.patches[8].getRegionWidth(), this.patches[8].getRegionHeight());
        float y2 = y + this.patches[6].getRegionHeight();
        batch.draw(this.patches[3], x, y2, this.patches[3].getRegionWidth(), heightLeftRight);
        batch.draw(this.patches[4], x + this.patches[3].getRegionWidth(), y2, widthTopBottom, heightLeftRight);
        batch.draw(this.patches[5], this.patches[3].getRegionWidth() + x + widthTopBottom, y2, this.patches[5].getRegionWidth(), heightLeftRight);
        float y3 = y2 + heightLeftRight;
        batch.draw(this.patches[0], x, y3, this.patches[0].getRegionWidth(), this.patches[0].getRegionHeight());
        batch.draw(this.patches[1], x + this.patches[0].getRegionWidth(), y3, widthTopBottom, this.patches[1].getRegionHeight());
        batch.draw(this.patches[2], this.patches[0].getRegionWidth() + x + widthTopBottom, y3, this.patches[2].getRegionWidth(), this.patches[2].getRegionHeight());
    }

    public float getLeftWidth() {
        return this.patches[0].getRegionWidth();
    }

    public float getRightWidth() {
        return this.patches[2].getRegionWidth();
    }

    public float getTopHeight() {
        return this.patches[2].getRegionHeight();
    }

    public float getBottomHeight() {
        return this.patches[8].getRegionHeight();
    }

    public float getTotalHeight() {
        return getTopHeight() + getBottomHeight() + this.patches[3].getRegionHeight();
    }

    public float getTotalWidth() {
        return getLeftWidth() + getRightWidth() + this.patches[4].getRegionWidth();
    }
}
