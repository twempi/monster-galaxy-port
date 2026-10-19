package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Image extends Actor {
    public TextureRegion region;

    public Image(String name) {
        super(name);
        this.region = null;
    }

    public Image(String name, Texture texture) {
        super(name);
        this.region = null;
        this.originX = texture.getWidth() / 2.0f;
        this.originY = texture.getHeight() / 2.0f;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.region = new TextureRegion(texture);
    }

    public Image(String name, TextureRegion region) {
        super(name);
        this.region = null;
        this.width = Math.abs(region.getRegionWidth());
        this.height = Math.abs(region.getRegionHeight());
        this.originX = this.width / 2.0f;
        this.originY = this.height / 2.0f;
        this.region = new TextureRegion(region);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.region.getTexture() != null) {
            batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
            if (this.scaleX == 1.0f && this.scaleY == 1.0f && this.rotation == 0.0f) {
                batch.draw(this.region, this.x, this.y, this.width, this.height);
            } else {
                batch.draw(this.region, this.x, this.y, this.originX, this.originY, this.width, this.height, this.scaleX, this.scaleY, this.rotation);
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        return x > 0.0f && y > 0.0f && x < this.width && y < this.height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
            return null;
        }
        return this;
    }
}
