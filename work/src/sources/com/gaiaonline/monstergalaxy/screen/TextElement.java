package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.tweak.Tweak;

/* JADX INFO: loaded from: classes.dex */
public class TextElement extends ScreenElement {
    private static TextureRegion solidRect;
    private BitmapFont.HAlignment alignment;
    private BitmapFont.TextBounds bounds;
    protected final Color color;
    protected BitmapFont font;
    protected float fontScale;
    private boolean horizontalAutoSize;
    private float horizontalAutoSizeFontScale;
    private float horizontalAutoSizeMaxWidth;
    protected String msg;
    private boolean regularFont;
    private boolean verticalAutoSize;
    private float verticalAutoSizeFontScale;
    private float verticalAutoSizeMaxHeight;
    protected float wrapWidth;

    public TextElement(String msg, float fontScale, Color color, boolean isRegular) {
        this(msg, fontScale, color, isRegular, 0.0f);
    }

    public TextElement(String msg, float fontScale, Color color, boolean isRegular, float wrapWidth) {
        this.regularFont = isRegular;
        this.font = Fonts.getFont(!this.regularFont, msg);
        this.color = color;
        this.fontScale = Fonts.getLocaleScaleFactor() * fontScale;
        this.wrapWidth = wrapWidth;
        this.alignment = BitmapFont.HAlignment.LEFT;
        setText(msg);
        setBounds();
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        setBounds();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batch) {
        present(batch, this.color, pos.x, pos.y + this.size.y);
    }

    private static TextureRegion getSolidRect(boolean red) {
        if (solidRect == null) {
            solidRect = AssetsCache.newSolidRectangle(1, 1, new Color(red ? 1.0f : 0.0f, red ? 0.0f : 1.0f, 0.0f, 0.5f));
        }
        return solidRect;
    }

    private void resetFontScale(boolean scaledByFactor) {
        float scaleFactor = scaledByFactor ? ResolutionManager.getScaleFactor() : 1.0f;
        if (this.horizontalAutoSize) {
            this.font.setScale(this.horizontalAutoSizeFontScale * scaleFactor);
        } else if (this.verticalAutoSize) {
            this.font.setScale(this.verticalAutoSizeFontScale * scaleFactor);
        } else {
            this.font.setScale(this.fontScale * scaleFactor);
        }
    }

    protected synchronized void present(SpriteBatch batch, Color color, float x, float y) {
        float scaleFactor;
        synchronized (this) {
            this.font.setColor(color);
            resetFontScale(true);
            if (this.wrapWidth == 0.0f) {
                if (Tweak.showTextSizeHints && Tweak.isEnabled()) {
                    TextureRegion solidRect2 = getSolidRect(false);
                    float scaleFactor2 = y - ((this.verticalAutoSizeMaxHeight == 0.0f ? 5.0f : this.verticalAutoSizeMaxHeight) * ResolutionManager.getScaleFactor());
                    if (this.horizontalAutoSizeMaxWidth == 0.0f) {
                        scaleFactor = 5.0f;
                    } else {
                        scaleFactor = ResolutionManager.getScaleFactor() * this.horizontalAutoSizeMaxWidth;
                    }
                    batch.draw(solidRect2, x, scaleFactor2, scaleFactor, (this.verticalAutoSizeMaxHeight != 0.0f ? this.verticalAutoSizeMaxHeight : 5.0f) * ResolutionManager.getScaleFactor());
                }
                this.font.draw(batch, this.msg, x, y);
            } else {
                if (Tweak.showTextSizeHints && Tweak.isEnabled()) {
                    batch.draw(getSolidRect(true), x, y - ((this.verticalAutoSizeMaxHeight == 0.0f ? 5.0f : this.verticalAutoSizeMaxHeight) * ResolutionManager.getScaleFactor()), ResolutionManager.getScaleFactor() * this.wrapWidth, (this.verticalAutoSizeMaxHeight != 0.0f ? this.verticalAutoSizeMaxHeight : 5.0f) * ResolutionManager.getScaleFactor());
                }
                this.font.drawWrapped(batch, this.msg, x, y, this.wrapWidth * ResolutionManager.getScaleFactor(), this.alignment);
            }
        }
    }

    public void setTextAlignment(BitmapFont.HAlignment alignment) {
        this.alignment = alignment;
    }

    public void setText(String text) {
        if (text.contains("\u3000")) {
            text = text.replace("\u3000", "   ");
        }
        if (this.msg != text) {
            if (this.msg == null || !this.msg.equals(text)) {
                this.msg = text;
                boolean needAsianSupport = I18nManager.needAsianSupport(text);
                if (needAsianSupport || (!needAsianSupport && !Fonts.isWesternFont(this.font))) {
                    BitmapFont oldFont = this.font;
                    this.font = Fonts.getFont(!this.regularFont, text);
                    this.font.setColor(oldFont.getColor());
                    this.font.setScale(oldFont.getScaleX(), oldFont.getScaleY());
                    this.font.setUseIntegerPositions(true);
                    Fonts.releaseFont(oldFont);
                }
                horizontalAutoSize();
                verticalAutoSize();
                setBounds();
            }
        }
    }

    public void enableHorizontalAutoSize(float maxWidth) {
        this.horizontalAutoSize = true;
        this.horizontalAutoSizeMaxWidth = maxWidth;
        horizontalAutoSize();
    }

    public void enableVerticalAutoSize(float maxHeight) {
        this.verticalAutoSize = true;
        this.verticalAutoSizeMaxHeight = maxHeight;
        verticalAutoSize();
    }

    private BitmapFont.TextBounds getUnscaledVerticalBoundsHeight() {
        if (this.msg == null) {
            return null;
        }
        if (this.wrapWidth > 0.0f) {
            return this.font.getWrappedBounds(this.msg, this.wrapWidth);
        }
        return this.font.getMultiLineBounds(this.msg);
    }

    private void verticalAutoSize() {
        if (this.verticalAutoSize && this.msg != null && this.msg.trim().length() > 0 && this.verticalAutoSizeMaxHeight > 1.0f) {
            float lowerLimit = 0.0f;
            float upperLimit = this.fontScale;
            this.verticalAutoSizeFontScale = upperLimit;
            this.font.setScale(this.verticalAutoSizeFontScale);
            BitmapFont.TextBounds tb = getUnscaledVerticalBoundsHeight();
            if (Float.isNaN(tb.height)) {
                Gdx.app.error("TextElement", "verticalAutoSize(): TextBounds height is NaN. Text: " + (this.msg.length() > 50 ? this.msg.substring(0, 50) : this.msg));
                return;
            }
            float newHeight = tb.height;
            Gdx.app.debug("TextElement", "VAS - vasfs = " + this.verticalAutoSizeFontScale + ", nh=" + newHeight + ", nw=" + tb.width + ", text=" + this.msg);
            if (newHeight > this.verticalAutoSizeMaxHeight) {
                boolean isTooTall = true;
                while (upperLimit > lowerLimit && (upperLimit - lowerLimit > 0.001f || isTooTall)) {
                    float middleValue = (lowerLimit + upperLimit) / 2.0f;
                    this.verticalAutoSizeFontScale = middleValue;
                    this.font.setScale(this.verticalAutoSizeFontScale);
                    BitmapFont.TextBounds tb2 = getUnscaledVerticalBoundsHeight();
                    float newHeight2 = tb2.height;
                    Gdx.app.debug("TextElement", "VAS - vasfs = " + this.verticalAutoSizeFontScale + ", nh=" + newHeight2 + ", nw=" + tb2.width + ", text=" + this.msg);
                    if (newHeight2 == this.verticalAutoSizeMaxHeight) {
                        return;
                    }
                    if (newHeight2 < this.verticalAutoSizeMaxHeight) {
                        isTooTall = false;
                        lowerLimit = middleValue;
                    } else if (newHeight2 > this.verticalAutoSizeMaxHeight) {
                        isTooTall = true;
                        upperLimit = middleValue;
                    }
                }
                setBounds();
            }
        }
    }

    private void horizontalAutoSize() {
        this.horizontalAutoSizeFontScale = this.fontScale;
        if (this.horizontalAutoSize) {
            this.horizontalAutoSizeFontScale = getFontScaleForHorizontalAutoSize(this.horizontalAutoSizeMaxWidth);
            setBounds();
        }
    }

    public static void horizontallyAutosize(TextElement[] elements, float maxWidth) {
        float minFontScale = -1.0f;
        for (TextElement element : elements) {
            float fontScale = element.getFontScaleForHorizontalAutoSize(maxWidth);
            if (minFontScale == -1.0f || fontScale < minFontScale) {
                minFontScale = fontScale;
            }
        }
        for (TextElement element2 : elements) {
            element2.setFontScale(minFontScale);
        }
    }

    private float getFontScaleForHorizontalAutoSize(float maxWidth) {
        float newFontScale = this.fontScale;
        if (this.msg != null && this.msg.trim().length() > 0 && maxWidth > 1.0f) {
            float lowerLimit = 0.0f;
            float upperLimit = this.fontScale;
            newFontScale = upperLimit;
            this.font.setScale(ResolutionManager.getScaleFactor() * newFontScale);
            BitmapFont.TextBounds tb = this.font.getBounds(this.msg);
            if (Float.isNaN(tb.width)) {
                Gdx.app.error("TextElement", "verticalAutoSize(): TextBounds height is NaN. Text: " + (this.msg.length() > 50 ? this.msg.substring(0, 50) : this.msg));
                return newFontScale;
            }
            if (tb.width <= ResolutionManager.getScaleFactor() * maxWidth) {
                return newFontScale;
            }
            boolean isTooWide = true;
            while (true) {
                if ((upperLimit > lowerLimit && upperLimit - lowerLimit > 0.01f) || isTooWide) {
                    float middleValue = (lowerLimit + upperLimit) / 2.0f;
                    newFontScale = middleValue;
                    this.font.setScale(ResolutionManager.getScaleFactor() * newFontScale);
                    BitmapFont.TextBounds tb2 = this.font.getBounds(this.msg);
                    if (Float.isNaN(tb2.width)) {
                        Gdx.app.error("TextElement", "getFontScaleForHorizontalAutoSize(): TextBounds width is NaN. Text: " + (this.msg.length() > 50 ? this.msg.substring(0, 50) : this.msg));
                        return newFontScale;
                    }
                    float newWidth = tb2.width;
                    if (newWidth == ResolutionManager.getScaleFactor() * maxWidth) {
                        return newFontScale;
                    }
                    if (newWidth < ResolutionManager.getScaleFactor() * maxWidth) {
                        isTooWide = false;
                        lowerLimit = middleValue;
                    } else if (newWidth > ResolutionManager.getScaleFactor() * maxWidth) {
                        isTooWide = true;
                        upperLimit = middleValue;
                    }
                }
            }
        }
        return newFontScale;
    }

    public void disableAutoSize() {
        this.horizontalAutoSize = false;
        this.verticalAutoSize = false;
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public void setWrapWidth(float wrapWidth) {
        this.wrapWidth = wrapWidth;
        verticalAutoSize();
        setBounds();
    }

    private void setBounds() {
        if (this.msg != null && !"".equals(this.msg)) {
            resetFontScale(true);
            if (this.wrapWidth != 0.0f) {
                this.bounds = this.font.getWrappedBounds(this.msg, this.wrapWidth * ResolutionManager.getScaleFactor());
                this.size.x = this.wrapWidth * ResolutionManager.getScaleFactor();
                this.size.y = this.bounds.height;
            } else {
                this.bounds = this.font.getBounds(this.msg);
                this.size.x = this.bounds.width;
                this.size.y = this.bounds.height;
            }
            this.needsRecalc = true;
        }
    }

    public BitmapFont.TextBounds getBounds() {
        return this.bounds;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        Fonts.releaseFont(this.font);
    }

    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }
}
