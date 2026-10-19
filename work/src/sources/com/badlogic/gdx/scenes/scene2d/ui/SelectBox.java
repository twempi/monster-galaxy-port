package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

/* JADX INFO: loaded from: classes.dex */
public class SelectBox extends Widget {
    private final BitmapFont.TextBounds bounds;
    private ClickListener clickListener;
    String[] items;
    SelectList list;
    private float prefHeight;
    private float prefWidth;
    final Vector2 screenCoords;
    int selectedIndex;
    SelectBoxStyle style;

    public SelectBox(Object[] items, Skin skin) {
        this(items, (SelectBoxStyle) skin.get(SelectBoxStyle.class));
    }

    public SelectBox(Object[] items, Skin skin, String styleName) {
        this(items, (SelectBoxStyle) skin.get(styleName, SelectBoxStyle.class));
    }

    public SelectBox(Object[] items, SelectBoxStyle style) {
        this.selectedIndex = 0;
        this.bounds = new BitmapFont.TextBounds();
        this.screenCoords = new Vector2();
        setStyle(style);
        setItems(items);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        ClickListener clickListener = new ClickListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.SelectBox.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener, com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && button != 0) {
                    return false;
                }
                if (SelectBox.this.list != null && SelectBox.this.list.getParent() != null) {
                    SelectBox.this.hideList();
                    return true;
                }
                Stage stage = SelectBox.this.getStage();
                Vector2 stageCoords = Vector2.tmp;
                stage.screenToStageCoordinates(stageCoords.set(SelectBox.this.screenCoords.x, SelectBox.this.screenCoords.y));
                SelectBox.this.list = SelectBox.this.new SelectList(stageCoords.x, stageCoords.y);
                stage.addActor(SelectBox.this.list);
                return true;
            }
        };
        this.clickListener = clickListener;
        addListener(clickListener);
    }

    public void setStyle(SelectBoxStyle style) {
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

    public SelectBoxStyle getStyle() {
        return this.style;
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
            objects = strings;
        }
        this.items = (String[]) objects;
        this.selectedIndex = 0;
        Drawable bg = this.style.background;
        BitmapFont font = this.style.font;
        this.prefHeight = Math.max(((bg.getTopHeight() + bg.getBottomHeight()) + font.getCapHeight()) - (font.getDescent() * 2.0f), bg.getMinHeight());
        float max = 0.0f;
        for (int i2 = 0; i2 < this.items.length; i2++) {
            max = Math.max(font.getBounds(this.items[i2]).width, max);
        }
        this.prefWidth = bg.getLeftWidth() + bg.getRightWidth() + max;
        this.prefWidth = Math.max(this.prefWidth, this.style.listBackground.getLeftWidth() + max + this.style.listBackground.getRightWidth() + (this.style.itemSpacing * 2.0f));
        invalidateHierarchy();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Drawable background;
        if (this.list != null && this.list.getParent() != null && this.style.backgroundOpen != null) {
            background = this.style.backgroundOpen;
        } else if (this.clickListener.isOver() && this.style.backgroundOver != null) {
            background = this.style.backgroundOver;
        } else {
            background = this.style.background;
        }
        BitmapFont font = this.style.font;
        Color fontColor = this.style.fontColor;
        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        background.draw(batch, x, y, width, height);
        if (this.items.length > 0) {
            float availableWidth = (width - background.getLeftWidth()) - background.getRightWidth();
            int numGlyphs = font.computeVisibleGlyphs(this.items[this.selectedIndex], 0, this.items[this.selectedIndex].length(), availableWidth);
            this.bounds.set(font.getBounds(this.items[this.selectedIndex]));
            float textY = (int) (((height - (background.getBottomHeight() + background.getTopHeight())) / 2.0f) + background.getBottomHeight() + (this.bounds.height / 2.0f));
            font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
            font.draw(batch, this.items[this.selectedIndex], x + background.getLeftWidth(), y + textY, 0, numGlyphs);
        }
        getStage().toScreenCoordinates(this.screenCoords.set(x, y), batch.getTransformMatrix());
    }

    public void setSelection(int selection) {
        this.selectedIndex = selection;
    }

    public void setSelection(String item) {
        for (int i = 0; i < this.items.length; i++) {
            if (this.items[i].equals(item)) {
                this.selectedIndex = i;
            }
        }
    }

    public int getSelectionIndex() {
        return this.selectedIndex;
    }

    public String getSelection() {
        return this.items[this.selectedIndex];
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        return this.prefWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        return this.prefHeight;
    }

    public void hideList() {
        if (this.list.getParent() != null) {
            this.list.addAction(Actions.sequence(Actions.fadeOut(0.15f, Interpolation.fade), Actions.removeActor()));
        }
    }

    class SelectList extends Actor {
        float itemHeight;
        int listSelectedIndex;
        Vector2 oldScreenCoords = new Vector2();
        InputListener stageListener = new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectList.1
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && button != 0) {
                    return false;
                }
                SelectList.this.stageToLocalCoordinates(Vector2.tmp);
                float x2 = Vector2.tmp.x;
                float y2 = Vector2.tmp.y;
                if (x2 > 0.0f && x2 < SelectList.this.getWidth() && y2 > 0.0f && y2 < SelectList.this.getHeight()) {
                    SelectList.this.listSelectedIndex = (int) ((SelectList.this.getHeight() - y2) / SelectList.this.itemHeight);
                    SelectList.this.listSelectedIndex = Math.max(0, SelectList.this.listSelectedIndex);
                    SelectList.this.listSelectedIndex = Math.min(SelectBox.this.items.length - 1, SelectList.this.listSelectedIndex);
                    SelectBox.this.selectedIndex = SelectList.this.listSelectedIndex;
                    if (SelectBox.this.items.length > 0) {
                        ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
                        SelectBox.this.fire(changeEvent);
                        Pools.free(changeEvent);
                    }
                }
                return true;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SelectBox.this.hideList();
                event.getStage().removeCaptureListener(SelectList.this.stageListener);
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean mouseMoved(InputEvent event, float x, float y) {
                SelectList.this.stageToLocalCoordinates(Vector2.tmp);
                float x2 = Vector2.tmp.x;
                float y2 = Vector2.tmp.y;
                if (x2 > 0.0f && x2 < SelectList.this.getWidth() && y2 > 0.0f && y2 < SelectList.this.getHeight()) {
                    SelectList.this.listSelectedIndex = (int) (((SelectList.this.getHeight() - SelectBox.this.style.listBackground.getTopHeight()) - y2) / SelectList.this.itemHeight);
                    SelectList.this.listSelectedIndex = Math.max(0, SelectList.this.listSelectedIndex);
                    SelectList.this.listSelectedIndex = Math.min(SelectBox.this.items.length - 1, SelectList.this.listSelectedIndex);
                    return true;
                }
                return true;
            }
        };
        float textOffsetX;
        float textOffsetY;

        public SelectList(float x, float y) {
            this.listSelectedIndex = SelectBox.this.selectedIndex;
            setBounds(x, 0.0f, SelectBox.this.getWidth(), 100.0f);
            this.oldScreenCoords.set(SelectBox.this.screenCoords);
            layout();
            Stage stage = SelectBox.this.getStage();
            float height = getHeight();
            if (y - height < 0.0f && SelectBox.this.getHeight() + y + height < stage.getCamera().viewportHeight) {
                setY(SelectBox.this.getHeight() + y);
            } else {
                setY(y - height);
            }
            stage.addCaptureListener(this.stageListener);
            getColor().a = 0.0f;
            addAction(Actions.fadeIn(0.3f, Interpolation.fade));
        }

        private void layout() {
            BitmapFont font = SelectBox.this.style.font;
            Drawable listSelection = SelectBox.this.style.listSelection;
            Drawable listBackground = SelectBox.this.style.listBackground;
            this.itemHeight = font.getCapHeight() + ((-font.getDescent()) * 2.0f) + SelectBox.this.style.itemSpacing;
            this.itemHeight += listSelection.getTopHeight() + listSelection.getBottomHeight();
            this.textOffsetX = listSelection.getLeftWidth() + SelectBox.this.style.itemSpacing;
            this.textOffsetY = listSelection.getTopHeight() + (-font.getDescent()) + (SelectBox.this.style.itemSpacing / 2.0f);
            setWidth(SelectBox.this.getWidth());
            setHeight((SelectBox.this.items.length * this.itemHeight) + listBackground.getTopHeight() + listBackground.getBottomHeight());
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public void draw(SpriteBatch batch, float parentAlpha) {
            Drawable listBackground = SelectBox.this.style.listBackground;
            Drawable listSelection = SelectBox.this.style.listSelection;
            BitmapFont font = SelectBox.this.style.font;
            Color fontColor = SelectBox.this.style.fontColor;
            float x = getX();
            float y = getY();
            float width = getWidth();
            float height = getHeight();
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            listBackground.draw(batch, x, y, width, height);
            float width2 = width - (listBackground.getLeftWidth() + listBackground.getRightWidth());
            float x2 = x + listBackground.getLeftWidth();
            float posY = height - listBackground.getTopHeight();
            for (int i = 0; i < SelectBox.this.items.length; i++) {
                if (this.listSelectedIndex == i) {
                    listSelection.draw(batch, x2, (y + posY) - this.itemHeight, width2, this.itemHeight);
                }
                font.setColor(fontColor.r, fontColor.g, fontColor.b, color.a * fontColor.a * parentAlpha);
                font.draw(batch, SelectBox.this.items[i], this.textOffsetX + x2, (y + posY) - this.textOffsetY);
                posY -= this.itemHeight;
            }
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public Actor hit(float x, float y, boolean touchable) {
            return this;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public void act(float delta) {
            super.act(delta);
            if (SelectBox.this.screenCoords.x != this.oldScreenCoords.x || SelectBox.this.screenCoords.y != this.oldScreenCoords.y) {
                SelectBox.this.hideList();
            }
        }
    }

    public static class SelectBoxStyle {
        public Drawable background;
        public Drawable backgroundOpen;
        public Drawable backgroundOver;
        public BitmapFont font;
        public Color fontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        public float itemSpacing = 10.0f;
        public Drawable listBackground;
        public Drawable listSelection;

        public SelectBoxStyle() {
        }

        public SelectBoxStyle(BitmapFont font, Color fontColor, Drawable background, Drawable listBackground, Drawable listSelection) {
            this.background = background;
            this.listBackground = listBackground;
            this.listSelection = listSelection;
            this.font = font;
            this.fontColor.set(fontColor);
        }

        public SelectBoxStyle(SelectBoxStyle style) {
            this.background = style.background;
            this.listBackground = style.listBackground;
            this.listSelection = style.listSelection;
            this.font = style.font;
            this.fontColor.set(style.fontColor);
        }
    }
}
