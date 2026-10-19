package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ProgressBarElement extends TextureElement {
    private float maxValue;
    private float progress;

    public ProgressBarElement(TextureRegion textureRegion) {
        super(textureRegion);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setValue(float value) {
        this.progress = Math.max(0.0f, Math.min(this.maxValue, value / this.maxValue));
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextureElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        batcher.draw(this.region.getTexture(), pos.x, pos.y, this.progress * this.size.x, this.size.y, this.region.getRegionX(), this.region.getRegionY(), Math.round(this.region.getRegionWidth() * this.progress), this.region.getRegionHeight(), false, false);
    }
}
