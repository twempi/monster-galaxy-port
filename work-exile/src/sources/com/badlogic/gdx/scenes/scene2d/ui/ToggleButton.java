package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ToggleButton extends Widget {
    final BitmapFont.TextBounds bounds;
    boolean isPressed;
    ClickListener listener;
    final ToggleButtonStyle style;
    String text;

    public interface ClickListener {
        void click(ToggleButton toggleButton, boolean z);
    }

    public ToggleButton(String name, String label, ToggleButtonStyle style) {
        super(name, 0.0f, 0.0f);
        this.bounds = new BitmapFont.TextBounds();
        this.isPressed = false;
        this.listener = null;
        this.style = style;
        this.text = label;
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        BitmapFont font = this.style.font;
        NinePatch downPatch = this.style.down;
        this.bounds.set(font.getMultiLineBounds(this.text));
        this.prefHeight = downPatch.getBottomHeight() + downPatch.getTopHeight() + this.bounds.height + ((-font.getDescent()) * 2.0f);
        this.prefWidth = downPatch.getLeftWidth() + downPatch.getRightWidth() + this.bounds.width;
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        BitmapFont font = this.style.font;
        Color fontColor = this.style.fontColor;
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
        float textY = ((int) (this.height / 2.0f)) + ((int) (this.bounds.height / 2.0f));
        font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
        font.drawMultiLine(batch, this.text, this.x + ((int) (this.width / 2.0f)), this.y + textY, 0.0f, BitmapFont.HAlignment.CENTER);
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

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
            return null;
        }
        return this;
    }

    public static class ToggleButtonStyle {
        public final NinePatch down;
        public final BitmapFont font;
        public final Color fontColor;
        public final NinePatch up;

        public ToggleButtonStyle(BitmapFont font, Color fontColor, NinePatch down, NinePatch up) {
            this.font = font;
            this.fontColor = fontColor;
            this.down = down;
            this.up = up;
        }
    }

    public void setText(String text) {
        this.text = text;
        invalidateHierarchy();
    }

    public String getText() {
        return this.text;
    }

    public ToggleButton setClickListener(ClickListener listener) {
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
