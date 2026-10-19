package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;

/* JADX INFO: loaded from: classes.dex */
public class AutoFontLabel extends Actor {
    private static final float SHADOW_OFFSET_X = 2.0f;
    private static final float SHADOW_OFFSET_Y = -2.0f;
    private BitmapFont font;
    private Color fontColor;
    private float fontScale;
    private boolean italic;
    private boolean multiline;
    private Color shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private String text;
    private Float wrapWidth;

    public AutoFontLabel(String text, float scale, Color fontColor, boolean italic, Color shadowColor) {
        this.shadowColor = null;
        this.fontScale = scale;
        this.fontColor = fontColor;
        this.italic = italic;
        this.shadowOffsetX = SHADOW_OFFSET_X * ResolutionManager.getScaleFactor();
        this.shadowOffsetY = SHADOW_OFFSET_Y * ResolutionManager.getScaleFactor();
        setShadowColor(shadowColor);
        setText(text);
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public AutoFontLabel(String text, float scale, Color color, boolean italic) {
        this(text, scale, color, italic, null);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.font != null) {
            float scaledHeight = getHeight() * getScaleY();
            float scaledOriginX = getOriginX() * (1.0f - getScaleX());
            float scaledOriginY = getOriginY() * (1.0f - getScaleY());
            if (this.shadowColor != null) {
                Color tmpColor = new Color(this.shadowColor);
                tmpColor.a = this.shadowColor.a * getColor().a * parentAlpha;
                tmpColor.r = this.shadowColor.r * getColor().r;
                tmpColor.g = this.shadowColor.g * getColor().g;
                tmpColor.b = this.shadowColor.b * getColor().b;
                this.font.setColor(tmpColor);
                if (this.fontScale > 0.0f && getScaleX() > 0.0f && getScaleY() > 0.0f) {
                    this.font.setScale(this.fontScale * getScaleX(), this.fontScale * getScaleY());
                    this.font.draw(batch, this.text, getX() + (this.shadowOffsetX * getScaleX()) + scaledOriginX, getY() + (this.shadowOffsetY * getScaleY()) + scaledHeight + scaledOriginY);
                }
            }
            Color tmpColor2 = new Color(this.fontColor);
            tmpColor2.a = this.fontColor.a * getColor().a * parentAlpha;
            tmpColor2.r = this.fontColor.r * getColor().r;
            tmpColor2.g = this.fontColor.g * getColor().g;
            tmpColor2.b = this.fontColor.b * getColor().b;
            this.font.setColor(tmpColor2);
            this.font.setScale(this.fontScale * getScaleX(), this.fontScale * getScaleY());
            this.font.draw(batch, this.text, getX() + scaledOriginX, getY() + scaledHeight + scaledOriginY);
        }
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setText(String text) {
        String oldText = this.text;
        this.text = text;
        if (text != null && !text.equals(oldText)) {
            setFont();
        }
    }

    private void setFont() {
        if (I18nManager.isAsianLocale()) {
            BitmapFont oldFont = this.font;
            this.font = Fonts.getFont(false, getText().toString());
            Fonts.releaseFont(oldFont);
        } else if (this.italic) {
            this.font = Fonts.italic;
        } else {
            this.font = Fonts.regular;
        }
    }

    public String getText() {
        return this.text;
    }

    public void dispose() {
        Fonts.releaseFont(this.font);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public float getHeight() {
        if (this.font == null || this.text == null || this.text.length() == 0) {
            return 0.0f;
        }
        this.font.setScale(this.fontScale);
        if (this.wrapWidth != null) {
            return this.font.getWrappedBounds(this.text, this.wrapWidth.floatValue()).height;
        }
        if (this.multiline) {
            return this.font.getMultiLineBounds(this.text).height;
        }
        return this.font.getBounds(this.text).height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public float getWidth() {
        if (this.font == null || this.text == null || this.text.length() == 0) {
            return 0.0f;
        }
        this.font.setScale(this.fontScale);
        if (this.wrapWidth != null) {
            return this.font.getWrappedBounds(this.text, this.wrapWidth.floatValue()).width;
        }
        if (this.multiline) {
            return this.font.getMultiLineBounds(this.text).width;
        }
        return this.font.getBounds(this.text).width;
    }

    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }
}
