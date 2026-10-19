package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pools;

/* JADX INFO: loaded from: classes.dex */
public class List extends Widget implements Cullable {
    private Rectangle cullingArea;
    private float itemHeight;
    private String[] items;
    private float prefHeight;
    private float prefWidth;
    private int selectedIndex;
    private ListStyle style;
    private float textOffsetX;
    private float textOffsetY;

    public List(Object[] items, Skin skin) {
        this(items, (ListStyle) skin.get(ListStyle.class));
    }

    public List(Object[] items, Skin skin, String styleName) {
        this(items, (ListStyle) skin.get(styleName, ListStyle.class));
    }

    public List(Object[] items, ListStyle style) {
        setStyle(style);
        setItems(items);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        addListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.List.1
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && button != 0) {
                    return false;
                }
                List.this.touchDown(y);
                return true;
            }
        });
    }

    void touchDown(float y) {
        int oldIndex = this.selectedIndex;
        this.selectedIndex = (int) ((getHeight() - y) / this.itemHeight);
        this.selectedIndex = Math.max(0, this.selectedIndex);
        this.selectedIndex = Math.min(this.items.length - 1, this.selectedIndex);
        ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
        if (fire(changeEvent)) {
            this.selectedIndex = oldIndex;
        }
        Pools.free(changeEvent);
    }

    public void setStyle(ListStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.style = style;
        if (this.items != null) {
            setItems(this.items);
        } else {
            invalidateHierarchy();
        }
    }

    public ListStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        BitmapFont font = this.style.font;
        Drawable selectedDrawable = this.style.selection;
        Color fontColorSelected = this.style.fontColorSelected;
        Color fontColorUnselected = this.style.fontColorUnselected;
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        float x = getX();
        float y = getY();
        font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
        float itemY = getHeight();
        for (int i = 0; i < this.items.length; i++) {
            if (this.cullingArea == null || (itemY - this.itemHeight <= this.cullingArea.y + this.cullingArea.height && itemY >= this.cullingArea.y)) {
                if (this.selectedIndex == i) {
                    selectedDrawable.draw(batch, x, (y + itemY) - this.itemHeight, Math.max(this.prefWidth, getWidth()), this.itemHeight);
                    font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
                }
                font.draw(batch, this.items[i], this.textOffsetX + x, (y + itemY) - this.textOffsetY);
                if (this.selectedIndex == i) {
                    font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
                }
            } else if (itemY < this.cullingArea.y) {
                return;
            }
            itemY -= this.itemHeight;
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setSelectedIndex(int index) {
        if (index < 0 || index >= this.items.length) {
            throw new GdxRuntimeException("index must be >= 0 and < " + this.items.length + ": " + index);
        }
        this.selectedIndex = index;
    }

    public String getSelection() {
        if (this.items.length == 0) {
            return null;
        }
        return this.items[this.selectedIndex];
    }

    public int setSelection(String item) {
        this.selectedIndex = -1;
        int n = this.items.length;
        for (int i = 0; i < n; i++) {
            if (this.items[i].equals(item)) {
                this.selectedIndex = i;
                break;
            }
        }
        return this.selectedIndex;
    }

    public void setItems(Object[] objects) {
        if (objects == null) {
            throw new IllegalArgumentException("items cannot be null.");
        }
        if (!(objects instanceof String[])) {
            String[] strings = new String[objects.length];
            int n = objects.length;
            for (int i = 0; i < n; i++) {
                strings[i] = String.valueOf(objects[i]);
            }
            this.items = strings;
        } else {
            this.items = (String[]) objects;
        }
        this.selectedIndex = 0;
        BitmapFont font = this.style.font;
        Drawable selectedDrawable = this.style.selection;
        this.itemHeight = font.getCapHeight() - (font.getDescent() * 2.0f);
        this.itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();
        this.prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
        this.textOffsetX = selectedDrawable.getLeftWidth();
        this.textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();
        this.prefWidth = 0.0f;
        for (int i2 = 0; i2 < this.items.length; i2++) {
            BitmapFont.TextBounds bounds = font.getBounds(this.items[i2]);
            this.prefWidth = Math.max(bounds.width, this.prefWidth);
        }
        this.prefHeight = this.items.length * this.itemHeight;
        invalidateHierarchy();
    }

    public String[] getItems() {
        return this.items;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        return this.prefWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        return this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.Cullable
    public void setCullingArea(Rectangle cullingArea) {
        this.cullingArea = cullingArea;
    }

    public static class ListStyle {
        public BitmapFont font;
        public Color fontColorSelected = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        public Color fontColorUnselected = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        public Drawable selection;

        public ListStyle() {
        }

        public ListStyle(BitmapFont font, Color fontColorSelected, Color fontColorUnselected, Drawable selection) {
            this.font = font;
            this.fontColorSelected.set(fontColorSelected);
            this.fontColorUnselected.set(fontColorUnselected);
            this.selection = selection;
        }

        public ListStyle(ListStyle style) {
            this.font = style.font;
            this.fontColorSelected.set(style.fontColorSelected);
            this.fontColorUnselected.set(style.fontColorUnselected);
            this.selection = style.selection;
        }
    }
}
