package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Layout;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Button extends Actor implements Layout {
    public ClickListener clickListener;
    protected int pointer;
    public boolean pressed;
    public TextureRegion pressedRegion;
    public TextureRegion unpressedRegion;

    public interface ClickListener {
        void clicked(Button button);
    }

    public Button(String name) {
        super(name);
        this.pressed = false;
        this.pointer = -1;
        this.pressedRegion = new TextureRegion();
        this.unpressedRegion = new TextureRegion();
    }

    public Button(String name, Texture texture) {
        super(name);
        this.pressed = false;
        this.pointer = -1;
        this.originX = texture.getWidth() / 2.0f;
        this.originY = texture.getHeight() / 2.0f;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.pressedRegion = new TextureRegion(texture);
        this.unpressedRegion = new TextureRegion(texture);
    }

    public Button(String name, TextureRegion region) {
        this(name, region, region);
    }

    public Button(String name, TextureRegion unpressedRegion, TextureRegion pressedRegion) {
        super(name);
        this.pressed = false;
        this.pointer = -1;
        this.width = Math.abs(unpressedRegion.getRegionWidth());
        this.height = Math.abs(unpressedRegion.getRegionHeight());
        this.originX = this.width / 2.0f;
        this.originY = this.height / 2.0f;
        this.unpressedRegion = new TextureRegion(unpressedRegion);
        this.pressedRegion = new TextureRegion(pressedRegion);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        TextureRegion region = this.pressed ? this.pressedRegion : this.unpressedRegion;
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        if (region.getTexture() != null) {
            if (this.scaleX == 1.0f && this.scaleY == 1.0f && this.rotation == 0.0f) {
                batch.draw(region, this.x, this.y, this.width, this.height);
            } else {
                batch.draw(region, this.x, this.y, this.originX, this.originY, this.width, this.height, this.scaleX, this.scaleY, this.rotation);
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        boolean result = false;
        if (!this.pressed) {
            if (x > 0.0f && y > 0.0f && x < this.width && y < this.height) {
                result = true;
            }
            this.pressed = result;
            if (this.pressed) {
                this.parent.focus(this, pointer);
                this.pointer = pointer;
            }
        }
        return result;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (!this.pressed) {
            return false;
        }
        if (pointer == this.pointer) {
            this.parent.focus(null, pointer);
        }
        this.pressed = false;
        if (this.clickListener != null) {
            this.clickListener.clicked(this);
        }
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return this.pressed;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || y <= 0.0f || x >= this.width || y >= this.height) {
            return null;
        }
        return this;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        return this.unpressedRegion.getRegionWidth() * this.scaleX;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        return this.unpressedRegion.getRegionHeight() * this.scaleY;
    }
}
