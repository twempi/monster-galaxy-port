package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/* JADX INFO: loaded from: classes.dex */
public class TiledDrawable extends TextureRegionDrawable {
    public TiledDrawable() {
    }

    public TiledDrawable(TextureRegion region) {
        super(region);
    }

    public TiledDrawable(TextureRegionDrawable drawable) {
        super(drawable);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable, com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable, com.badlogic.gdx.scenes.scene2d.utils.Drawable
    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        TextureRegion region = getRegion();
        float regionWidth = region.getRegionWidth();
        float regionHeight = region.getRegionHeight();
        float remainingX = width % regionWidth;
        float remainingY = height % regionHeight;
        float endX = (x + width) - remainingX;
        float endY = (y + height) - remainingY;
        while (x < endX) {
            y = y;
            while (y < endY) {
                batch.draw(region, x, y, regionWidth, regionHeight);
                y += regionHeight;
            }
            x += regionWidth;
        }
        Texture texture = region.getTexture();
        float u = region.getU();
        float v2 = region.getV2();
        if (remainingX > 0.0f) {
            float u2 = u + (remainingX / texture.getWidth());
            float v = region.getV();
            y = y;
            while (y < endY) {
                batch.draw(texture, x, y, remainingX, regionHeight, u, v2, u2, v);
                y += regionHeight;
            }
            if (remainingY > 0.0f) {
                float v3 = v2 - (remainingY / texture.getHeight());
                batch.draw(texture, x, y, remainingX, remainingY, u, v2, u2, v3);
            }
        }
        if (remainingY > 0.0f) {
            float u3 = region.getU2();
            float v4 = v2 - (remainingY / texture.getHeight());
            for (float x2 = x; x2 < endX; x2 += regionWidth) {
                batch.draw(texture, x2, y, regionWidth, remainingY, u, v2, u3, v4);
            }
        }
    }
}
