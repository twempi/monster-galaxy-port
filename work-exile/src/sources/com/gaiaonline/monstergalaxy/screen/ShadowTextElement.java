package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ShadowTextElement extends TextElement {
    private static final float SHADOW_OFFSET_X = 2.0f;
    private static final float SHADOW_OFFSET_Y = -2.0f;
    private Color shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;

    public ShadowTextElement(String msg, float scale, Color textColor, Color shadowColor, boolean isRegular) {
        super(msg, scale, textColor, isRegular);
        this.shadowColor = shadowColor;
        this.shadowOffsetX = SHADOW_OFFSET_X * ResolutionManager.getScaleFactor();
        this.shadowOffsetY = SHADOW_OFFSET_Y * ResolutionManager.getScaleFactor();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batch) {
        present(batch, this.shadowColor, pos.x + this.shadowOffsetX, pos.y + this.size.y + this.shadowOffsetY);
        super.present(pos, batch);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        super.dispose();
    }
}
