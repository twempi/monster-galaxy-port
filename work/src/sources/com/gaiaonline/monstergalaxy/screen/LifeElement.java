package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: classes.dex */
public class LifeElement extends TextureElement {
    private float lifeUnitLength;
    private int maxLife;
    private float maxWidth;
    private float minWidth;
    private float origin;

    public LifeElement(TextureRegion region, float minWidth) {
        super(region);
        setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.maxWidth = this.size.x;
        this.minWidth = ResolutionManager.getHorizontalAR() * minWidth;
    }

    public LifeElement(TextureRegion region) {
        this(region, 0.0f);
    }

    public void setMaxLife(int life) {
        this.maxLife = life;
        this.lifeUnitLength = (this.maxWidth - this.minWidth) / life;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void setScale(float scale) {
        super.setScale(scale);
        this.maxWidth *= scale;
        this.minWidth *= scale;
    }

    public void setLife(int life) {
        if (this.visible) {
            if (life == 0) {
                this.visible = false;
            } else {
                this.visible = true;
            }
        }
        this.origin = (this.lifeUnitLength * (this.maxLife - life)) + this.minWidth;
        this.size.x = (this.lifeUnitLength * life) + this.minWidth;
        this.needsRecalc = true;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextureElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        batcher.draw(this.region, pos.x, pos.y, this.origin, 0.0f, this.size.x, this.size.y, 1.0f, 1.0f, 0.0f);
    }
}
