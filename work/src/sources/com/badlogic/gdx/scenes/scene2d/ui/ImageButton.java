package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.esotericsoftware.tablelayout.Cell;

/* JADX INFO: loaded from: classes.dex */
public class ImageButton extends Button {
    private final Image image;
    private ImageButtonStyle style;

    public ImageButton(Skin skin) {
        this((ImageButtonStyle) skin.get(ImageButtonStyle.class));
    }

    public ImageButton(Skin skin, String styleName) {
        this((ImageButtonStyle) skin.get(styleName, ImageButtonStyle.class));
    }

    public ImageButton(ImageButtonStyle style) {
        super(style);
        this.image = new Image();
        this.image.setScaling(Scaling.fit);
        add(this.image);
        setStyle(style);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    public ImageButton(Drawable imageUp) {
        this(new ImageButtonStyle(null, null, null, imageUp, null, null));
    }

    public ImageButton(Drawable imageUp, Drawable imageDown) {
        this(new ImageButtonStyle(null, null, null, imageUp, imageDown, null));
    }

    public ImageButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
        this(new ImageButtonStyle(null, null, null, imageUp, imageDown, imageChecked));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Button
    public void setStyle(Button.ButtonStyle style) {
        if (!(style instanceof ImageButtonStyle)) {
            throw new IllegalArgumentException("style must be an ImageButtonStyle.");
        }
        super.setStyle(style);
        this.style = (ImageButtonStyle) style;
        if (this.image != null) {
            updateImage();
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Button
    public ImageButtonStyle getStyle() {
        return this.style;
    }

    private void updateImage() {
        boolean isPressed = isPressed();
        if (this.isDisabled && this.style.imageDisabled != null) {
            this.image.setDrawable(this.style.imageDisabled);
            return;
        }
        if (isPressed && this.style.imageDown != null) {
            this.image.setDrawable(this.style.imageDown);
            return;
        }
        if (this.isChecked && this.style.imageChecked != null) {
            this.image.setDrawable((this.style.imageCheckedOver == null || !isOver()) ? this.style.imageChecked : this.style.imageCheckedOver);
            return;
        }
        if (isOver() && this.style.imageOver != null) {
            this.image.setDrawable(this.style.imageOver);
        } else if (this.style.imageUp != null) {
            this.image.setDrawable(this.style.imageUp);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Button, com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        updateImage();
        super.draw(batch, parentAlpha);
    }

    public Image getImage() {
        return this.image;
    }

    public Cell getImageCell() {
        return getCell(this.image);
    }

    public static class ImageButtonStyle extends Button.ButtonStyle {
        public Drawable imageChecked;
        public Drawable imageCheckedOver;
        public Drawable imageDisabled;
        public Drawable imageDown;
        public Drawable imageOver;
        public Drawable imageUp;

        public ImageButtonStyle() {
        }

        public ImageButtonStyle(Drawable up, Drawable down, Drawable checked, Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
            super(up, down, checked);
            this.imageUp = imageUp;
            this.imageDown = imageDown;
            this.imageChecked = imageChecked;
        }

        public ImageButtonStyle(ImageButtonStyle style) {
            super(style);
            this.imageUp = style.imageUp;
            this.imageDown = style.imageDown;
            this.imageOver = style.imageOver;
            this.imageChecked = style.imageChecked;
            this.imageCheckedOver = style.imageCheckedOver;
            this.imageDisabled = style.imageDisabled;
        }

        public ImageButtonStyle(Button.ButtonStyle style) {
            super(style);
        }
    }
}
