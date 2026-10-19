package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

/* JADX INFO: loaded from: classes.dex */
public class Button extends Table {
    ButtonGroup buttonGroup;
    private ClickListener clickListener;
    boolean isChecked;
    boolean isDisabled;
    private ButtonStyle style;

    public Button(Skin skin) {
        super(skin);
        initialize();
        setStyle((ButtonStyle) skin.get(ButtonStyle.class));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    public Button(Skin skin, String styleName) {
        super(skin);
        initialize();
        setStyle((ButtonStyle) skin.get(styleName, ButtonStyle.class));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    public Button(Actor child, Skin skin, String styleName) {
        this(child, (ButtonStyle) skin.get(styleName, ButtonStyle.class));
    }

    public Button(Actor child, ButtonStyle style) {
        initialize();
        add(child);
        setStyle(style);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    public Button(ButtonStyle style) {
        initialize();
        setStyle(style);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    private void initialize() {
        setTouchable(Touchable.enabled);
        ClickListener clickListener = new ClickListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Button.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener
            public void clicked(InputEvent event, float x, float y) {
                if (!Button.this.isDisabled) {
                    boolean z = Button.this.isChecked;
                    Button.this.setChecked(!Button.this.isChecked);
                }
            }
        };
        this.clickListener = clickListener;
        addListener(clickListener);
    }

    public Button(Drawable up) {
        this(new ButtonStyle(up, null, null));
    }

    public Button(Drawable up, Drawable down) {
        this(new ButtonStyle(up, down, null));
    }

    public Button(Drawable up, Drawable down, Drawable checked) {
        this(new ButtonStyle(up, down, checked));
    }

    public Button(Actor child, Skin skin) {
        this(child, (ButtonStyle) skin.get(ButtonStyle.class));
    }

    public void setChecked(boolean isChecked) {
        if (this.isChecked != isChecked) {
            if (this.buttonGroup == null || this.buttonGroup.canCheck(this, isChecked)) {
                this.isChecked = isChecked;
                if (!this.isDisabled) {
                    ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
                    if (fire(changeEvent)) {
                        this.isChecked = !isChecked;
                    }
                    Pools.free(changeEvent);
                }
            }
        }
    }

    public void toggle() {
        setChecked(!this.isChecked);
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public boolean isPressed() {
        return this.clickListener.isPressed();
    }

    public boolean isOver() {
        return this.clickListener.isOver();
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public void setStyle(ButtonStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.style = style;
        Drawable background = style.up;
        if (background == null && (background = style.down) == null) {
            background = style.checked;
        }
        if (background != null) {
            padBottom(background.getBottomHeight());
            padTop(background.getTopHeight());
            padLeft(background.getLeftWidth());
            padRight(background.getRightWidth());
        }
        invalidateHierarchy();
    }

    public ButtonStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Drawable background;
        float offsetX;
        float offsetY;
        validate();
        if (isPressed() && !this.isDisabled) {
            background = this.style.down == null ? this.style.up : this.style.down;
            offsetX = this.style.pressedOffsetX;
            offsetY = this.style.pressedOffsetY;
        } else {
            if (this.isDisabled && this.style.disabled != null) {
                background = this.style.disabled;
            } else if (this.isChecked && this.style.checked != null) {
                background = (!isOver() || this.style.checkedOver == null) ? this.style.checked : this.style.checkedOver;
            } else if (isOver() && this.style.over != null) {
                background = this.style.over;
            } else {
                background = this.style.up;
            }
            offsetX = this.style.unpressedOffsetX;
            offsetY = this.style.unpressedOffsetY;
        }
        if (background != null) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
        Array<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            children.get(i).translate(offsetX, offsetY);
        }
        super.draw(batch, parentAlpha);
        for (int i2 = 0; i2 < children.size; i2++) {
            children.get(i2).translate(-offsetX, -offsetY);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table
    protected void drawBackground(SpriteBatch batch, float parentAlpha) {
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        float width = super.getPrefWidth();
        if (this.style.up != null) {
            width = Math.max(width, this.style.up.getMinWidth());
        }
        if (this.style.down != null) {
            width = Math.max(width, this.style.down.getMinWidth());
        }
        return this.style.checked != null ? Math.max(width, this.style.checked.getMinWidth()) : width;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        float height = super.getPrefHeight();
        if (this.style.up != null) {
            height = Math.max(height, this.style.up.getMinHeight());
        }
        if (this.style.down != null) {
            height = Math.max(height, this.style.down.getMinHeight());
        }
        return this.style.checked != null ? Math.max(height, this.style.checked.getMinHeight()) : height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinWidth() {
        return getPrefWidth();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinHeight() {
        return getPrefHeight();
    }

    public static class ButtonStyle {
        public Drawable checked;
        public Drawable checkedOver;
        public Drawable disabled;
        public Drawable down;
        public Drawable over;
        public float pressedOffsetX;
        public float pressedOffsetY;
        public float unpressedOffsetX;
        public float unpressedOffsetY;
        public Drawable up;

        public ButtonStyle() {
        }

        public ButtonStyle(Drawable up, Drawable down, Drawable checked) {
            this.up = up;
            this.down = down;
            this.checked = checked;
        }

        public ButtonStyle(ButtonStyle style) {
            this.up = style.up;
            this.down = style.down;
            this.over = style.over;
            this.checked = style.checked;
            this.checkedOver = style.checkedOver;
            this.disabled = style.disabled;
            this.pressedOffsetX = style.pressedOffsetX;
            this.pressedOffsetY = style.pressedOffsetY;
            this.unpressedOffsetX = style.unpressedOffsetX;
            this.unpressedOffsetY = style.unpressedOffsetY;
        }
    }
}
