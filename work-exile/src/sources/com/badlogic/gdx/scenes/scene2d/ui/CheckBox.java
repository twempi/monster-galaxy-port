package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CheckBox extends Widget {
    final Rectangle bounds;
    final Vector2 boxPos;
    float checkHeight;
    float checkWidth;
    boolean isChecked;
    String label;
    CheckedListener listener;
    final CheckBoxStyle style;
    final BitmapFont.TextBounds textBounds;
    final Vector2 textPos;

    public interface CheckedListener {
        void checked(CheckBox checkBox, boolean z);
    }

    public CheckBox(String name, String label, CheckBoxStyle style) {
        super(name, 0.0f, 0.0f);
        this.isChecked = false;
        this.listener = null;
        this.boxPos = new Vector2();
        this.textPos = new Vector2();
        this.textBounds = new BitmapFont.TextBounds();
        this.bounds = new Rectangle();
        this.checkWidth = 0.0f;
        this.checkHeight = 0.0f;
        this.style = style;
        this.label = label;
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        BitmapFont font = this.style.font;
        TextureRegion checkedRegion = this.style.checked;
        TextureRegion uncheckedRegion = this.style.unchecked;
        this.textBounds.set(font.getBounds(this.label));
        this.textBounds.height -= font.getDescent();
        this.checkWidth = Math.max(checkedRegion.getRegionWidth(), uncheckedRegion.getRegionWidth());
        this.checkHeight = Math.max(checkedRegion.getRegionHeight(), uncheckedRegion.getRegionHeight());
        if (this.textBounds.height > this.checkHeight) {
            this.prefHeight = this.textBounds.height;
            this.boxPos.y = (int) ((this.textBounds.height - checkedRegion.getRegionHeight()) / 2.0f);
            this.textPos.y = this.textBounds.height;
        } else {
            this.prefHeight = this.checkHeight;
            this.boxPos.y = 0.0f;
            this.textPos.y = ((int) ((this.checkHeight - this.textBounds.height) / 2.0f)) + this.textBounds.height;
        }
        this.boxPos.x = 0.0f;
        this.textPos.x = this.checkWidth + 5.0f;
        this.prefWidth = this.checkWidth + 5.0f + this.textBounds.width;
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        BitmapFont font = this.style.font;
        TextureRegion checkedRegion = this.style.checked;
        TextureRegion uncheckedRegion = this.style.unchecked;
        Color fontColor = this.style.fontColor;
        if (this.invalidated) {
            layout();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        if (this.isChecked) {
            batch.draw(checkedRegion, this.x + this.boxPos.x, this.y + this.boxPos.y);
        } else {
            batch.draw(uncheckedRegion, this.x + this.boxPos.x, this.y + this.boxPos.y);
        }
        font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
        font.draw(batch, this.label, this.x + this.textPos.x, this.y + this.textPos.y);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (x >= 0.0f && x < this.width && y >= 0.0f && y < this.height) {
            this.isChecked = !this.isChecked;
            if (this.listener != null) {
                this.listener.checked(this, this.isChecked);
            }
        }
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

    public CheckBox setCheckedListener(CheckedListener listener) {
        this.listener = listener;
        return this;
    }

    public static class CheckBoxStyle {
        public final TextureRegion checked;
        public final BitmapFont font;
        public final Color fontColor;
        public final TextureRegion unchecked;

        public CheckBoxStyle(BitmapFont font, Color fontColor, TextureRegion checked, TextureRegion unchecked) {
            this.font = font;
            this.fontColor = fontColor;
            this.checked = checked;
            this.unchecked = unchecked;
        }
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        invalidateHierarchy();
    }

    public boolean isChecked() {
        return this.isChecked;
    }
}
