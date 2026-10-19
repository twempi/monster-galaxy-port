package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextElement extends ScreenElement {
    private BitmapFont.HAlignment alignment;
    private BitmapFont.TextBounds bounds;
    protected final Color color;
    protected final BitmapFont font;
    protected float fontScale;
    protected String msg;
    protected float wrapWidth;

    public TextElement(String msg, float fontScale, Color color, boolean isRegular) {
        this(msg, fontScale, color, isRegular, 0.0f);
    }

    public TextElement(String msg, float fontScale, Color color, boolean isRegular, float wrapWidth) {
        this.msg = msg;
        this.font = isRegular ? Fonts.regular : Fonts.italic;
        this.color = color;
        this.fontScale = fontScale;
        this.wrapWidth = ResolutionManager.getScaleFactor() * wrapWidth;
        this.alignment = BitmapFont.HAlignment.LEFT;
        setBounds();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batch) {
        present(batch, this.color, pos.x, pos.y + this.size.y);
    }

    protected synchronized void present(SpriteBatch batch, Color color, float x, float y) {
        this.font.setColor(color);
        this.font.setScale(this.fontScale * ResolutionManager.getScaleFactor());
        if (this.wrapWidth == 0.0f) {
            this.font.draw(batch, this.msg, x, y);
        } else {
            this.font.drawWrapped(batch, this.msg, x, y, this.wrapWidth, this.alignment);
        }
    }

    public void setTextAlignment(BitmapFont.HAlignment alignment) {
        this.alignment = alignment;
    }

    public void setText(String text) {
        this.msg = text;
        setBounds();
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public void setWrapWidth(float wrapWidth) {
        this.wrapWidth = ResolutionManager.getScaleFactor() * wrapWidth;
        setBounds();
    }

    private void setBounds() {
        if (this.msg != null && !"".equals(this.msg)) {
            this.font.setScale(this.fontScale * ResolutionManager.getScaleFactor());
            if (this.wrapWidth != 0.0f) {
                this.bounds = this.font.getWrappedBounds(this.msg, this.wrapWidth);
                this.size.x = this.wrapWidth;
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
    }
}
