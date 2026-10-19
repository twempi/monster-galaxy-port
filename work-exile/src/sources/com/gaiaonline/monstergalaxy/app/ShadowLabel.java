package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actors.Label;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ShadowLabel extends Label {
    private static final float SHADOW_OFFSET_X = 2.0f;
    private static final float SHADOW_OFFSET_Y = -2.0f;
    private BitmapFont aFont;
    private float initialScale;
    private Color shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private Color textColor;

    public ShadowLabel(String name, BitmapFont font, String text, Color textColor, Color shadowColor) {
        super(name, font);
        setText(text);
        this.textColor = textColor;
        this.aFont = font;
        this.scaleX = font.getScaleX();
        this.scaleY = font.getScaleY();
        this.initialScale = font.getScaleX();
        this.shadowColor = shadowColor;
        this.shadowOffsetX = SHADOW_OFFSET_X * ResolutionManager.getScaleFactor();
        this.shadowOffsetY = SHADOW_OFFSET_Y * ResolutionManager.getScaleFactor();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actors.Label, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        doDraw(batch, parentAlpha);
    }

    public void doDraw(SpriteBatch batch, float parentAlpha) {
        float posx = this.x;
        float f = this.y;
        float posy = this.y + this.cache.getBounds().height;
        if (this.initialScale != this.scaleX) {
            posx = this.x + ((this.cache.getBounds().width - (this.cache.getBounds().width * this.scaleX)) / SHADOW_OFFSET_X);
        }
        String[] lines = this.text.split("\n");
        int lineCount = 0;
        for (String actualLine : lines) {
            if (actualLine.trim().length() != 0) {
                float lineOffSet = lineCount * this.aFont.getLineHeight();
                posy -= lineOffSet;
                this.cache.setPosition(posx, posy);
                if (this.scaleX > 0.0f && this.scaleY > 0.0f) {
                    String actualLine2 = actualLine.trim();
                    this.aFont.setScale(this.scaleX, this.scaleY);
                    this.aFont.setColor(this.shadowColor.r, this.shadowColor.g, this.shadowColor.b, this.shadowColor.a * parentAlpha);
                    this.aFont.draw(batch, actualLine2, this.shadowOffsetX + posx, this.shadowOffsetY + posy);
                    this.aFont.setColor(this.textColor.r, this.textColor.g, this.textColor.b, this.textColor.a * parentAlpha);
                    this.aFont.draw(batch, actualLine2, posx, posy);
                }
                lineCount++;
            }
        }
    }
}
