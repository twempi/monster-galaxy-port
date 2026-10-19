package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Game;

/* JADX INFO: loaded from: classes.dex */
public class TextureElement extends ScreenElement {
    protected boolean disposeTexture;
    protected TextureRegion region;

    public TextureElement(TextureRegion region, boolean disposeTexture) {
        this(region);
        this.disposeTexture = disposeTexture;
    }

    public TextureElement(TextureRegion region) {
        setTexture(region);
    }

    public TextureRegion getRegion() {
        return this.region;
    }

    public void setTexture(TextureRegion region) {
        this.region = region;
        if (region != null) {
            setSize(region.getRegionWidth(), region.getRegionHeight());
        }
        this.needsRecalc = true;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void flip() {
        super.flip();
        this.region = new TextureRegion(this.region);
        this.region.flip(true, false);
    }

    public TextureRegion getTexture() {
        return this.region;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        try {
            batcher.draw(this.region, pos.x, pos.y, this.size.x, this.size.y);
        } catch (Exception e) {
            Gdx.app.log(Game.TAG, toString(), e);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        if (this.region != null) {
        }
        this.region = null;
    }
}
