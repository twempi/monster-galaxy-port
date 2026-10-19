package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ComboBox extends Widget {
    final BitmapFont.TextBounds bounds;
    String[] entries;
    ComboList list;
    SelectionListener listener;
    final Vector2 screenCoords;
    int selection;
    final Stage stage;
    Vector2 stageCoords;
    final ComboBoxStyle style;

    public interface SelectionListener {
        void selected(ComboBox comboBox, int i, String str);
    }

    public ComboBox(String name, String[] entries, Stage stage, ComboBoxStyle style) {
        super(name, 0.0f, 0.0f);
        this.selection = 0;
        this.bounds = new BitmapFont.TextBounds();
        this.screenCoords = new Vector2();
        this.list = null;
        this.stageCoords = new Vector2();
        this.entries = entries;
        this.style = style;
        this.stage = stage;
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        NinePatch background = this.style.background;
        BitmapFont font = this.style.font;
        this.prefHeight = Math.max(font.getLineHeight() + (-font.getDescent()), background.getTotalHeight());
        float max = 0.0f;
        for (int i = 0; i < this.entries.length; i++) {
            max = Math.max(font.getBounds(this.entries[i]).width, max);
        }
        this.prefWidth = background.getLeftWidth() + background.getRightWidth() + max;
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        NinePatch background = this.style.background;
        BitmapFont font = this.style.font;
        Color fontColor = this.style.fontColor;
        if (this.invalidated) {
            layout();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        background.draw(batch, this.x, this.y, this.width, this.height);
        if (this.entries.length > 0) {
            float availableWidth = (this.width - background.getLeftWidth()) - background.getRightWidth();
            int numGlyphs = font.computeVisibleGlyphs(this.entries[this.selection], 0, this.entries[this.selection].length(), availableWidth);
            this.bounds.set(font.getBounds(this.entries[this.selection]));
            float textY = ((int) (this.height / 2.0f)) + ((int) (this.bounds.height / 2.0f));
            font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
            font.draw(batch, this.entries[this.selection], this.x + background.getLeftWidth(), this.y + textY, 0, numGlyphs);
        }
        ScissorStack.toWindowCoordinates(this.stage.getCamera(), batch.getTransformMatrix(), this.screenCoords.set(this.x, this.y));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0 || hit(x, y) == null) {
            return false;
        }
        if (this.list != null) {
            this.stage.removeActor(this.list);
        }
        this.stage.toStageCoordinates((int) this.screenCoords.x, (int) this.screenCoords.y, this.stageCoords);
        this.list = new ComboList(this.name + "-list", this.stageCoords.x, this.stageCoords.y);
        this.stage.addActor(this.list);
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

    public static class ComboBoxStyle {
        public final NinePatch background;
        public final BitmapFont font;
        public final Color fontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        public final NinePatch listBackground;
        public final NinePatch listSelection;

        public ComboBoxStyle(BitmapFont font, Color fontColor, NinePatch background, NinePatch listBackground, NinePatch listSelection) {
            this.background = background;
            this.listBackground = listBackground;
            this.listSelection = listSelection;
            this.font = font;
            this.fontColor.set(fontColor);
        }
    }

    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }

    protected class ComboList extends Actor {
        float entryHeight;
        boolean firstUp;
        Vector2 oldScreenCoords;
        int selected;
        float textOffsetX;
        float textOffsetY;

        public ComboList(String name, float x, float y) {
            super(name);
            this.oldScreenCoords = new Vector2();
            this.entryHeight = 0.0f;
            this.textOffsetX = 0.0f;
            this.textOffsetY = 0.0f;
            this.selected = ComboBox.this.selection;
            this.firstUp = false;
            this.x = x;
            this.y = y;
            this.width = ComboBox.this.width;
            this.height = 100.0f;
            this.oldScreenCoords.set(ComboBox.this.screenCoords);
            ComboBox.this.stage.getRoot().focus(this, 0);
            layout();
        }

        private void layout() {
            BitmapFont font = ComboBox.this.style.font;
            NinePatch listSelection = ComboBox.this.style.listSelection;
            float prefWidth = 0.0f;
            for (int i = 0; i < ComboBox.this.entries.length; i++) {
                String entry = ComboBox.this.entries[i];
                BitmapFont.TextBounds bounds = font.getBounds(entry);
                prefWidth = Math.max(bounds.width, prefWidth);
            }
            this.entryHeight = font.getCapHeight() + ((-font.getDescent()) * 2.0f);
            this.entryHeight += listSelection.getTopHeight() + listSelection.getBottomHeight();
            this.entryHeight *= ComboBox.this.parent.scaleY;
            float prefWidth2 = prefWidth + listSelection.getLeftWidth() + listSelection.getRightWidth();
            float prefHeight = ComboBox.this.entries.length * this.entryHeight;
            this.textOffsetX = listSelection.getLeftWidth();
            this.textOffsetY = listSelection.getTopHeight() + (-font.getDescent());
            this.width = Math.max(prefWidth2, ComboBox.this.width);
            this.width *= ComboBox.this.parent.scaleX;
            this.height = prefHeight;
            this.y -= this.height;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public void draw(SpriteBatch batch, float parentAlpha) {
            NinePatch listBackground = ComboBox.this.style.listBackground;
            NinePatch listSelection = ComboBox.this.style.listSelection;
            BitmapFont font = ComboBox.this.style.font;
            Color fontColor = ComboBox.this.style.fontColor;
            batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
            listBackground.draw(batch, this.x, this.y, this.width, this.height);
            float posY = this.height;
            for (int i = 0; i < ComboBox.this.entries.length; i++) {
                if (this.selected == i) {
                    listSelection.draw(batch, this.x, (this.y + posY) - this.entryHeight, this.width, this.entryHeight);
                }
                font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
                font.setScale(ComboBox.this.parent.scaleX, ComboBox.this.parent.scaleY);
                font.draw(batch, ComboBox.this.entries[i], this.x + this.textOffsetX, (this.y + posY) - this.textOffsetY);
                font.setScale(1.0f, 1.0f);
                posY -= this.entryHeight;
            }
            if (ComboBox.this.screenCoords.x != this.oldScreenCoords.x || ComboBox.this.screenCoords.y != this.oldScreenCoords.y) {
                ComboBox.this.stage.removeActor(this);
            }
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public boolean touchDown(float x, float y, int pointer) {
            if (pointer != 0) {
                return false;
            }
            if (hit(x, y) != null) {
                this.selected = (int) ((this.height - y) / this.entryHeight);
                this.selected = Math.max(0, this.selected);
                this.selected = Math.min(ComboBox.this.entries.length - 1, this.selected);
                ComboBox.this.selection = this.selected;
                if (ComboBox.this.entries.length > 0 && ComboBox.this.listener != null) {
                    ComboBox.this.listener.selected(ComboBox.this, this.selected, ComboBox.this.entries[this.selected]);
                }
            }
            return true;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public boolean touchUp(float x, float y, int pointer) {
            if (this.firstUp) {
                ComboBox.this.stage.removeActor(this);
            } else {
                this.firstUp = true;
            }
            return true;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public boolean touchDragged(float x, float y, int pointer) {
            return true;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public boolean touchMoved(float x, float y) {
            if (hit(x, y) != null) {
                this.selected = (int) ((this.height - y) / this.entryHeight);
                this.selected = Math.max(0, this.selected);
                this.selected = Math.min(ComboBox.this.entries.length - 1, this.selected);
                return true;
            }
            return true;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public Actor hit(float x, float y) {
            if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
                return null;
            }
            return this;
        }
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getSelectionIndex() {
        return this.selection;
    }

    public String getSelection() {
        return this.entries[this.selection];
    }

    public void setEntries(String[] entries) {
        this.entries = entries;
        invalidateHierarchy();
    }
}
