package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.StringBuilder;

/* JADX INFO: loaded from: classes.dex */
public class Label extends Widget {
    private final BitmapFont.TextBounds bounds;
    private BitmapFontCache cache;
    private float fontScaleX;
    private float fontScaleY;
    private int labelAlign;
    private float lastPrefHeight;
    private BitmapFont.HAlignment lineAlign;
    private boolean sizeInvalid;
    private LabelStyle style;
    private final StringBuilder text;
    private boolean wrap;

    public Label(CharSequence text, Skin skin) {
        this(text, (LabelStyle) skin.get(LabelStyle.class));
    }

    public Label(CharSequence text, Skin skin, String styleName) {
        this(text, (LabelStyle) skin.get(styleName, LabelStyle.class));
    }

    public Label(CharSequence text, Skin skin, String fontName, Color color) {
        this(text, new LabelStyle(skin.getFont(fontName), color));
    }

    public Label(CharSequence text, Skin skin, String fontName, String colorName) {
        this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
    }

    public Label(CharSequence text, LabelStyle style) {
        this.bounds = new BitmapFont.TextBounds();
        this.text = new StringBuilder();
        this.labelAlign = 8;
        this.lineAlign = BitmapFont.HAlignment.LEFT;
        this.sizeInvalid = true;
        this.fontScaleX = 1.0f;
        this.fontScaleY = 1.0f;
        if (text != null) {
            this.text.append(text);
        }
        setStyle(style);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
    }

    public void setStyle(LabelStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        if (style.font == null) {
            throw new IllegalArgumentException("Missing LabelStyle font.");
        }
        this.style = style;
        this.cache = new BitmapFontCache(style.font, style.font.usesIntegerPositions());
        invalidateHierarchy();
    }

    public LabelStyle getStyle() {
        return this.style;
    }

    public void setText(CharSequence newText) {
        if (newText instanceof StringBuilder) {
            if (!this.text.equals(newText)) {
                this.text.setLength(0);
                this.text.append((StringBuilder) newText);
            } else {
                return;
            }
        } else {
            if (newText == null) {
                newText = "";
            }
            if (!textEquals(newText)) {
                this.text.setLength(0);
                this.text.append(newText);
            } else {
                return;
            }
        }
        invalidateHierarchy();
    }

    private boolean textEquals(CharSequence other) {
        int length = this.text.length;
        char[] chars = this.text.chars;
        if (length != other.length()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (chars[i] != other.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public CharSequence getText() {
        return this.text;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void invalidate() {
        super.invalidate();
        this.sizeInvalid = true;
    }

    private void computeSize() {
        this.sizeInvalid = false;
        if (this.wrap) {
            this.bounds.set(this.cache.getFont().getWrappedBounds(this.text, getWidth()));
        } else {
            this.bounds.set(this.cache.getFont().getMultiLineBounds(this.text));
        }
        this.bounds.width *= this.fontScaleX;
        this.bounds.height *= this.fontScaleY;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        float y;
        if (this.sizeInvalid) {
            computeSize();
        }
        if (this.wrap) {
            float prefHeight = getPrefHeight();
            if (prefHeight != this.lastPrefHeight) {
                this.lastPrefHeight = prefHeight;
                invalidateHierarchy();
            }
        }
        BitmapFont font = this.cache.getFont();
        float oldScaleX = font.getScaleX();
        float oldScaleY = font.getScaleY();
        if (this.fontScaleX != 1.0f || this.fontScaleY != 1.0f) {
            font.setScale(this.fontScaleX, this.fontScaleY);
        }
        Drawable background = this.style.background;
        float width = getWidth();
        float height = getHeight();
        float x = 0.0f;
        float y2 = 0.0f;
        if (background != null) {
            x = background.getLeftWidth();
            y2 = background.getBottomHeight();
            width -= background.getLeftWidth() + background.getRightWidth();
            height -= background.getBottomHeight() + background.getTopHeight();
        }
        if ((this.labelAlign & 2) != 0) {
            y = y2 + (this.cache.getFont().isFlipped() ? 0.0f : height - this.bounds.height) + this.style.font.getDescent();
        } else if ((this.labelAlign & 4) != 0) {
            y = (y2 + (this.cache.getFont().isFlipped() ? height - this.bounds.height : 0.0f)) - this.style.font.getDescent();
        } else {
            y = y2 + ((int) ((height - this.bounds.height) / 2.0f));
        }
        if (!this.cache.getFont().isFlipped()) {
            y += this.bounds.height;
        }
        if ((this.labelAlign & 8) == 0) {
            if ((this.labelAlign & 16) != 0) {
                x += width - this.bounds.width;
            } else {
                x += (int) ((width - this.bounds.width) / 2.0f);
            }
        }
        if (this.wrap) {
            this.cache.setWrappedText(this.text, x, y, this.bounds.width, this.lineAlign);
        } else {
            this.cache.setMultiLineText(this.text, x, y, this.bounds.width, this.lineAlign);
        }
        if (this.fontScaleX != 1.0f || this.fontScaleY != 1.0f) {
            font.setScale(oldScaleX, oldScaleY);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        validate();
        Color color = getColor();
        if (this.style.background != null) {
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            this.style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
        this.cache.setColor(this.style.fontColor == null ? color : Color.tmp.set(color).mul(this.style.fontColor));
        this.cache.setPosition(getX(), getY());
        this.cache.draw(batch, color.a * parentAlpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        if (this.wrap) {
            return 0.0f;
        }
        if (this.sizeInvalid) {
            computeSize();
        }
        float width = this.bounds.width;
        Drawable background = this.style.background;
        return background != null ? width + background.getLeftWidth() + background.getRightWidth() : width;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        if (this.sizeInvalid) {
            computeSize();
        }
        float height = this.bounds.height - (this.style.font.getDescent() * 2.0f);
        Drawable background = this.style.background;
        return background != null ? height + background.getTopHeight() + background.getBottomHeight() : height;
    }

    public BitmapFont.TextBounds getTextBounds() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.bounds;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
        invalidateHierarchy();
    }

    public void setAlignment(int wrapAlign) {
        setAlignment(wrapAlign, wrapAlign);
    }

    public void setAlignment(int labelAlign, int lineAlign) {
        this.labelAlign = labelAlign;
        if ((lineAlign & 8) != 0) {
            this.lineAlign = BitmapFont.HAlignment.LEFT;
        } else if ((lineAlign & 16) != 0) {
            this.lineAlign = BitmapFont.HAlignment.RIGHT;
        } else {
            this.lineAlign = BitmapFont.HAlignment.CENTER;
        }
        invalidate();
    }

    public void setFontScale(float fontScale) {
        this.fontScaleX = fontScale;
        this.fontScaleY = fontScale;
        invalidateHierarchy();
    }

    public void setFontScale(float fontScaleX, float fontScaleY) {
        this.fontScaleX = fontScaleX;
        this.fontScaleY = fontScaleY;
        invalidateHierarchy();
    }

    public float getFontScaleX() {
        return this.fontScaleX;
    }

    public void setFontScaleX(float fontScaleX) {
        this.fontScaleX = fontScaleX;
        invalidateHierarchy();
    }

    public float getFontScaleY() {
        return this.fontScaleY;
    }

    public void setFontScaleY(float fontScaleY) {
        this.fontScaleY = fontScaleY;
        invalidateHierarchy();
    }

    public static class LabelStyle {
        public Drawable background;
        public BitmapFont font;
        public Color fontColor;

        public LabelStyle() {
        }

        public LabelStyle(BitmapFont font, Color fontColor) {
            this.font = font;
            this.fontColor = fontColor;
        }

        public LabelStyle(LabelStyle style) {
            this.font = style.font;
            if (style.fontColor != null) {
                this.fontColor = new Color(style.fontColor);
            }
        }
    }
}
