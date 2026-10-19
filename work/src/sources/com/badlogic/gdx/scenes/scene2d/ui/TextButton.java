package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.esotericsoftware.tablelayout.Cell;

/* JADX INFO: loaded from: classes.dex */
public class TextButton extends Button {
    private final Label label;
    private TextButtonStyle style;

    public TextButton(String text, Skin skin) {
        this(text, (TextButtonStyle) skin.get(TextButtonStyle.class));
        setSkin(skin);
    }

    public TextButton(String text, Skin skin, String styleName) {
        this(text, (TextButtonStyle) skin.get(styleName, TextButtonStyle.class));
        setSkin(skin);
    }

    public TextButton(String text, TextButtonStyle style) {
        super(style);
        this.style = style;
        this.label = new Label(text, new Label.LabelStyle(style.font, style.fontColor));
        this.label.setAlignment(1);
        add(this.label).expand().fill();
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Button
    public void setStyle(Button.ButtonStyle style) {
        if (!(style instanceof TextButtonStyle)) {
            throw new IllegalArgumentException("style must be a TextButtonStyle.");
        }
        super.setStyle(style);
        this.style = (TextButtonStyle) style;
        if (this.label != null) {
            TextButtonStyle textButtonStyle = (TextButtonStyle) style;
            Label.LabelStyle labelStyle = this.label.getStyle();
            labelStyle.font = textButtonStyle.font;
            labelStyle.fontColor = textButtonStyle.fontColor;
            this.label.setStyle(labelStyle);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Button
    public TextButtonStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Button, com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Color fontColor;
        if (this.isDisabled && this.style.disabledFontColor != null) {
            fontColor = this.style.disabledFontColor;
        } else if (isPressed() && this.style.downFontColor != null) {
            fontColor = this.style.downFontColor;
        } else if (this.isChecked && this.style.checkedFontColor != null) {
            fontColor = (!isOver() || this.style.checkedOverFontColor == null) ? this.style.checkedFontColor : this.style.checkedOverFontColor;
        } else if (isOver() && this.style.overFontColor != null) {
            fontColor = this.style.overFontColor;
        } else {
            fontColor = this.style.fontColor;
        }
        if (fontColor != null) {
            this.label.getStyle().fontColor = fontColor;
        }
        super.draw(batch, parentAlpha);
    }

    public Label getLabel() {
        return this.label;
    }

    public Cell getLabelCell() {
        return getCell(this.label);
    }

    public void setText(String text) {
        this.label.setText(text);
    }

    public CharSequence getText() {
        return this.label.getText();
    }

    public static class TextButtonStyle extends Button.ButtonStyle {
        public Color checkedFontColor;
        public Color checkedOverFontColor;
        public Color disabledFontColor;
        public Color downFontColor;
        public BitmapFont font;
        public Color fontColor;
        public Color overFontColor;

        public TextButtonStyle() {
        }

        public TextButtonStyle(Drawable up, Drawable down, Drawable checked) {
            super(up, down, checked);
        }

        public TextButtonStyle(TextButtonStyle style) {
            super(style);
            this.font = style.font;
            if (style.fontColor != null) {
                this.fontColor = new Color(style.fontColor);
            }
            if (style.downFontColor != null) {
                this.downFontColor = new Color(style.downFontColor);
            }
            if (style.overFontColor != null) {
                this.overFontColor = new Color(style.overFontColor);
            }
            if (style.checkedFontColor != null) {
                this.checkedFontColor = new Color(style.checkedFontColor);
            }
            if (style.checkedOverFontColor != null) {
                this.checkedFontColor = new Color(style.checkedOverFontColor);
            }
            if (style.disabledFontColor != null) {
                this.disabledFontColor = new Color(style.disabledFontColor);
            }
        }
    }
}
