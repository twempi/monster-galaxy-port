package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ImageToggleButton extends Widget {
    final Rectangle bounds;
    TextureRegion image;
    float imageHeight;
    float imageWidth;
    boolean isPressed;
    ClickListener listener;
    final ImageToggleButtonStyle style;

    public interface ClickListener {
        void click(ImageToggleButton imageToggleButton, boolean z);
    }

    public ImageToggleButton(String name, TextureRegion image, ImageToggleButtonStyle style) {
        super(name, 0.0f, 0.0f);
        this.bounds = new Rectangle();
        this.isPressed = false;
        this.listener = null;
        this.style = style;
        this.image = image;
        this.imageWidth = image.getRegionWidth();
        this.imageHeight = image.getRegionHeight();
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    public ImageToggleButton(String name, TextureRegion image, float imageWidth, float imageHeight, ImageToggleButtonStyle style) {
        super(name, 0.0f, 0.0f);
        this.bounds = new Rectangle();
        this.isPressed = false;
        this.listener = null;
        this.style = style;
        this.image = image;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        NinePatch downPatch = this.style.down;
        this.bounds.set(0.0f, 0.0f, this.imageWidth, this.imageHeight);
        this.prefHeight = downPatch.getBottomHeight() + downPatch.getTopHeight() + this.bounds.height;
        this.prefWidth = downPatch.getLeftWidth() + downPatch.getRightWidth() + this.bounds.width;
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        NinePatch downPatch = this.style.down;
        NinePatch upPatch = this.style.up;
        if (this.invalidated) {
            layout();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        if (this.isPressed) {
            downPatch.draw(batch, this.x, this.y, this.width, this.height);
        } else {
            upPatch.draw(batch, this.x, this.y, this.width, this.height);
        }
        float imageX = (this.width - this.bounds.width) * 0.5f;
        float imageY = (this.height - this.bounds.height) * 0.5f;
        batch.draw(this.image, this.x + imageX, this.y + imageY, this.imageWidth, this.imageHeight);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0 || hit(x, y) == null) {
            return false;
        }
        this.isPressed = this.isPressed ? false : true;
        if (this.listener != null) {
            this.listener.click(this, this.isPressed);
        }
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return false;
    }

    public static class ImageToggleButtonStyle {
        public final NinePatch down;
        public final NinePatch up;

        public ImageToggleButtonStyle(NinePatch down, NinePatch up) {
            this.down = down;
            this.up = up;
        }
    }

    public TextureRegion getImage() {
        return this.image;
    }

    public void setImage(TextureRegion image) {
        this.image = image;
        invalidateHierarchy();
    }

    public void setImageSize(float width, float height) {
        this.imageWidth = width;
        this.imageHeight = height;
        invalidateHierarchy();
    }

    public ImageToggleButton setClickListener(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }
}
