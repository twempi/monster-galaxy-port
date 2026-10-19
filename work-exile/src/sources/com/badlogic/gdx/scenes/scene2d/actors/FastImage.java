package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FastImage extends Actor {
    public final TextureRegion region;
    private float sHeight;
    private float sOriginX;
    private float sOriginY;
    private float sRotation;
    private float sScaleX;
    private float sScaleY;
    private float sWidth;
    private float sX;
    private float sY;
    private Sprite sprite;

    public FastImage(String name) {
        super(name);
        this.sprite = new Sprite();
        this.region = new TextureRegion();
    }

    public FastImage(String name, Texture texture) {
        super(name);
        this.sprite = new Sprite();
        this.originX = texture.getWidth() / 2.0f;
        this.originY = texture.getHeight() / 2.0f;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.region = new TextureRegion(texture);
    }

    public FastImage(String name, TextureRegion region) {
        super(name);
        this.sprite = new Sprite();
        this.width = Math.abs(region.getRegionWidth());
        this.height = Math.abs(region.getRegionHeight());
        this.originX = this.width / 2.0f;
        this.originY = this.height / 2.0f;
        this.region = new TextureRegion(region);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        updateSprite();
        if (this.region.getTexture() != null) {
            this.sprite.draw(batch, parentAlpha);
        }
    }

    private void updateSprite() {
        if (this.sX != this.x || this.sY != this.y) {
            this.sprite.setPosition(this.x, this.y);
            this.sX = this.x;
            this.sY = this.y;
        }
        if (this.sOriginX != this.originX || this.sOriginY != this.originY) {
            this.sprite.setOrigin(this.originX, this.originY);
            this.sOriginX = this.originX;
            this.sOriginY = this.originY;
        }
        if (this.sRotation != this.rotation) {
            this.sprite.setRotation(this.rotation);
            this.sRotation = this.rotation;
        }
        if (this.sScaleX != this.scaleX || this.sScaleY != this.scaleY) {
            this.sprite.setScale(this.scaleX, this.scaleY);
            this.sScaleX = this.scaleX;
            this.sScaleY = this.scaleY;
        }
        if (this.sWidth != this.width || this.sHeight != this.height) {
            this.sprite.setSize(this.width, this.height);
            this.sWidth = this.width;
            this.sHeight = this.height;
        }
        this.sprite.setColor(this.color);
        this.sprite.setRegion(this.region);
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
