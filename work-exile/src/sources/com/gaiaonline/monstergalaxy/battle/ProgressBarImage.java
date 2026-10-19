package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ProgressBarImage extends Image {
    private float progress;

    public ProgressBarImage(String name) {
        super(name);
    }

    public ProgressBarImage(String string, TextureRegion loadTexture) {
        super(string, loadTexture);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actors.Image, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.region.getTexture() != null) {
            batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
            batch.draw(this.region.getTexture(), this.x, this.y, this.progress * this.width, this.height, this.region.getRegionX(), this.region.getRegionY(), Math.round(this.region.getRegionWidth() * this.progress), this.region.getRegionHeight(), false, false);
        }
    }
}
