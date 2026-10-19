package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/* JADX INFO: loaded from: classes.dex */
public class ProgressBarImage extends Image {
    private TextureRegionDrawable drawable;
    private float progress;

    public ProgressBarImage(String name) {
    }

    public ProgressBarImage(String string, TextureRegion loadTexture) {
        this(new TextureRegionDrawable(loadTexture));
    }

    public ProgressBarImage(TextureRegionDrawable drawable) {
        super(drawable);
        this.drawable = drawable;
    }

    public void setRegion(TextureRegion region) {
        setDrawable(new TextureRegionDrawable(region));
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Image, com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.drawable.getRegion().getTexture() != null) {
            batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
            batch.draw(this.drawable.getRegion().getTexture(), getX(), getY(), this.progress * getWidth(), getHeight(), this.drawable.getRegion().getRegionX(), this.drawable.getRegion().getRegionY(), Math.round(this.drawable.getRegion().getRegionWidth() * this.progress), this.drawable.getRegion().getRegionHeight(), false, false);
        }
    }
}
