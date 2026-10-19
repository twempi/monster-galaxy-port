package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class RegionImage extends Image {
    public int srcHeight;
    public int srcWidth;
    public int srcX;
    public int srcY;

    public RegionImage(String name, TextureRegion region) {
        super(name, region);
    }

    public void setRegion(int srcX, int srcY, int srcWidth, int srcHeight) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actors.Image, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.region.getTexture() != null) {
            batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
            batch.draw(this.region.getTexture(), this.x, this.y, this.width, this.height, this.srcX + this.region.getRegionX(), this.srcY + this.region.getRegionY(), this.srcWidth, this.srcHeight, false, false);
        }
    }
}
