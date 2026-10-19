package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Label extends Widget {
    final BitmapFont.TextBounds bounds;
    String label;
    final LabelStyle style;
    final Vector2 textPos;

    public Label(String name, String label, LabelStyle style) {
        super(name, 0.0f, 0.0f);
        this.bounds = new BitmapFont.TextBounds();
        this.textPos = new Vector2();
        this.style = style;
        this.label = label;
        this.touchable = false;
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        BitmapFont font = this.style.font;
        this.bounds.set(font.getMultiLineBounds(this.label));
        this.bounds.height -= font.getDescent();
        this.prefWidth = this.bounds.width;
        this.prefHeight = this.bounds.height;
        this.textPos.x = 0.0f;
        this.textPos.y = this.prefHeight;
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        BitmapFont font = this.style.font;
        Color fontColor = this.style.fontColor;
        if (this.invalidated) {
            layout();
        }
        font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
        font.drawMultiLine(batch, this.label, this.x + this.textPos.x, this.y + this.height);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        return false;
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
        return null;
    }

    public static class LabelStyle {
        public BitmapFont font;
        public Color fontColor;

        public LabelStyle(BitmapFont font, Color fontColor) {
            this.font = font;
            this.fontColor = fontColor;
        }
    }

    public void setText(String text) {
        this.label = text;
        layout();
        invalidateHierarchy();
    }
}
