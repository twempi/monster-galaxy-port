package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/* JADX INFO: loaded from: classes.dex */
public class CheckBox extends TextButton {
    private Image image;
    private CheckBoxStyle style;

    public CheckBox(String text, Skin skin) {
        this(text, (CheckBoxStyle) skin.get(CheckBoxStyle.class));
    }

    public CheckBox(String text, Skin skin, String styleName) {
        this(text, (CheckBoxStyle) skin.get(styleName, CheckBoxStyle.class));
    }

    public CheckBox(String text, CheckBoxStyle style) {
        super(text, style);
        clear();
        Image image = new Image(style.checkboxOff);
        this.image = image;
        add(image);
        Label label = getLabel();
        add(label);
        label.setAlignment(8);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.TextButton, com.badlogic.gdx.scenes.scene2d.ui.Button
    public void setStyle(Button.ButtonStyle style) {
        if (!(style instanceof CheckBoxStyle)) {
            throw new IllegalArgumentException("style must be a CheckBoxStyle.");
        }
        super.setStyle(style);
        this.style = (CheckBoxStyle) style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.TextButton, com.badlogic.gdx.scenes.scene2d.ui.Button
    public CheckBoxStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.TextButton, com.badlogic.gdx.scenes.scene2d.ui.Button, com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Drawable checkbox;
        if (this.isChecked && this.style.checkboxOn != null) {
            checkbox = this.style.checkboxOn;
        } else if (isOver() && this.style.checkboxOver != null) {
            checkbox = this.style.checkboxOver;
        } else {
            checkbox = this.style.checkboxOff;
        }
        this.image.setDrawable(checkbox);
        super.draw(batch, parentAlpha);
    }

    public Image getImage() {
        return this.image;
    }

    public static class CheckBoxStyle extends TextButton.TextButtonStyle {
        public Drawable checkboxOff;
        public Drawable checkboxOn;
        public Drawable checkboxOver;

        public CheckBoxStyle() {
        }

        public CheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
            this.checkboxOff = checkboxOff;
            this.checkboxOn = checkboxOn;
            this.font = font;
            this.fontColor = fontColor;
        }

        public CheckBoxStyle(CheckBoxStyle style) {
            this.checkboxOff = style.checkboxOff;
            this.checkboxOn = style.checkboxOn;
            this.font = style.font;
            this.fontColor = new Color(style.fontColor);
        }
    }
}
