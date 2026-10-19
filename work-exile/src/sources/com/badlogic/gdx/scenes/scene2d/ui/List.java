package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class List extends Widget {
    String[] entries;
    float entryHeight;
    boolean invalidated;
    SelectionListener listener;
    int selected;
    final ListStyle style;
    float textOffsetX;
    float textOffsetY;

    public interface SelectionListener {
        void selected(List list, int i, String str);
    }

    public List(String name, String[] entries, ListStyle style) {
        super(name, 0.0f, 0.0f);
        this.invalidated = false;
        this.entryHeight = 0.0f;
        this.textOffsetX = 0.0f;
        this.textOffsetY = 0.0f;
        this.selected = 0;
        this.style = style;
        this.entries = entries;
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        BitmapFont font = this.style.font;
        NinePatch selectedPatch = this.style.selectedPatch;
        this.prefWidth = 0.0f;
        this.prefHeight = 0.0f;
        for (int i = 0; i < this.entries.length; i++) {
            String entry = this.entries[i];
            BitmapFont.TextBounds bounds = font.getBounds(entry);
            this.prefWidth = Math.max(bounds.width, this.prefWidth);
        }
        this.entryHeight = font.getLineHeight() - font.getDescent();
        this.entryHeight += selectedPatch.getTopHeight() + selectedPatch.getBottomHeight();
        this.prefWidth += selectedPatch.getLeftWidth() + selectedPatch.getRightWidth();
        this.prefHeight = this.entries.length * this.entryHeight;
        this.textOffsetX = selectedPatch.getLeftWidth();
        this.textOffsetY = selectedPatch.getTopHeight() - font.getDescent();
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        BitmapFont font = this.style.font;
        NinePatch selectedPatch = this.style.selectedPatch;
        Color fontColorSelected = this.style.fontColorSelected;
        Color fontColorUnselected = this.style.fontColorUnselected;
        if (this.invalidated) {
            layout();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        float posY = this.height;
        for (int i = 0; i < this.entries.length; i++) {
            if (this.selected == i) {
                selectedPatch.draw(batch, this.x, (this.y + posY) - this.entryHeight, Math.max(this.prefWidth, this.width), this.entryHeight);
                font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
            } else {
                font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
            }
            font.draw(batch, this.entries[i], this.x + this.textOffsetX, (this.y + posY) - this.textOffsetY);
            posY -= this.entryHeight;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0 || hit(x, y) == null) {
            return false;
        }
        this.selected = (int) ((this.height - y) / this.entryHeight);
        this.selected = Math.max(0, this.selected);
        this.selected = Math.min(this.entries.length - 1, this.selected);
        if (this.listener != null) {
            this.listener.selected(this, this.selected, this.entries[this.selected]);
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
        if (x < 0.0f || x >= Math.max(this.prefWidth, this.width) || y < 0.0f || y >= this.prefHeight) {
            return null;
        }
        return this;
    }

    public static class ListStyle {
        public final BitmapFont font;
        public final Color fontColorSelected = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        public final Color fontColorUnselected = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        public final NinePatch selectedPatch;

        public ListStyle(BitmapFont font, Color fontColorSelected, Color fontColorUnselected, NinePatch selectedPatch) {
            this.font = font;
            this.fontColorSelected.set(fontColorSelected);
            this.fontColorUnselected.set(this.fontColorUnselected);
            this.selectedPatch = selectedPatch;
        }
    }

    public int getSelectedIndex() {
        return this.selected;
    }

    public String getSelection() {
        return this.entries[this.selected];
    }

    public void setSelection(int index) {
        if (index < 0 || index >= this.entries.length) {
            throw new GdxRuntimeException("Index must be > 0 and < #entries");
        }
        this.selected = index;
        invalidateHierarchy();
    }

    public void setEntries(String[] entries) {
        if (entries == null) {
            throw new IllegalArgumentException("entries must not be null");
        }
        this.entries = entries;
        this.selected = 0;
        invalidateHierarchy();
    }

    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }
}
