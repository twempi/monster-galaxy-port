package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class RotaryTextureElement extends TextureElement {
    private Vector2 absSize;
    private float rotation;

    public RotaryTextureElement(TextureRegion region) {
        super(region);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void setSize(float x, float y) {
        super.setSize(x, y);
        this.absSize = new Vector2(this.size.x, this.size.y);
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        if (rotation != 0.0f) {
            if (rotation % 90.0f != 0.0f) {
                float rad = (float) Math.sqrt((this.size.x / 2.0f) + (this.size.y / 2.0f));
                float angle = (float) Math.toRadians(rotation);
                this.size.x = ((float) (((double) rad) * Math.cos(angle))) * 2.0f;
                this.size.y = ((float) (((double) rad) * Math.sin(angle))) * 2.0f;
            } else if ((rotation / 90.0f) % 2.0f == 1.0f) {
                this.size.x = this.absSize.y;
                this.size.y = this.absSize.x;
            }
            this.rotation = rotation;
            this.needsRecalc = true;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextureElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void present(Vector2 pos, SpriteBatch batcher) {
        batcher.draw(this.region, pos.x, pos.y, 0.0f, 0.0f, this.absSize.x, this.absSize.y, 1.0f, 1.0f, this.rotation);
    }
}
