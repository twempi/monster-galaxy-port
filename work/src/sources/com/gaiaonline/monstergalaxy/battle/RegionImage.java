package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/* JADX INFO: loaded from: classes.dex */
public class RegionImage extends Image {
    private TextureRegion region;
    public int srcHeight;
    public int srcWidth;
    public int srcX;
    public int srcY;

    public RegionImage(String name, TextureRegion region) {
        super(region);
        this.region = region;
    }

    public void setRegion(int srcX, int srcY, int srcWidth, int srcHeight) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Image, com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.region.getTexture() != null) {
            batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
            batch.draw(this.region.getTexture(), getX(), getY(), getWidth(), getHeight(), this.srcX + this.region.getRegionX(), this.srcY + this.region.getRegionY(), this.srcWidth, this.srcHeight, false, false);
        }
    }
}
