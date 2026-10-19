package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SpriteElement extends ScreenElement {
    private float alpha;
    private Sprite sprite;

    public SpriteElement(Sprite sprite) {
        this(sprite, 1.0f);
    }

    public SpriteElement(Sprite sprite, float alpha) {
        this.sprite = sprite;
        this.alpha = alpha;
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.sprite.setSize(this.size.x, this.size.y);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        this.sprite.setPosition(pos.x, pos.y);
        this.sprite.draw(batcher, this.alpha);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        this.sprite = null;
    }
}
