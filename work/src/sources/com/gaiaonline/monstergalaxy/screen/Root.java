package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.tweak.Tweak;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Root extends ScreenGroup {
    private TextureRegion background;
    private float deltaTime;
    private List<Effect> effects;
    private Vector2 origin;
    private boolean shake = false;
    int frameCount = 0;

    public Root() {
        this.size.x = Gdx.graphics.getWidth();
        this.size.y = Gdx.graphics.getHeight();
        this.origin = new Vector2(0.0f, 0.0f);
        this.effects = new LinkedList();
    }

    public void doShake() {
        this.shake = true;
    }

    public void drawBackground(SpriteBatch batcher) {
        if (this.background != null) {
            float pos = 0.0f;
            if (this.shake) {
                this.frameCount++;
                if (this.frameCount < Gdx.app.getGraphics().getFramesPerSecond() / 8) {
                    pos = this.frameCount % 2 == 0 ? 0.0f : 30.0f;
                    this.origin.x = pos;
                } else {
                    this.frameCount = 0;
                    this.origin.x = 0.0f;
                    this.shake = false;
                }
            }
            batcher.disableBlending();
            batcher.begin();
            batcher.draw(this.background, pos, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batcher.end();
        }
    }

    public void drawEffects(SpriteBatch batcher) {
        if (this.effects.size() != 0) {
            batcher.begin();
            for (Effect e : this.effects) {
                e.draw(batcher, this.deltaTime);
            }
            batcher.end();
        }
    }

    public void present(SpriteBatch batcher) {
        batcher.enableBlending();
        batcher.begin();
        super.present(this.origin, batcher);
        if (Tweak.isShowGameClock()) {
            ClockElement.getInstance().present(batcher);
        }
        batcher.end();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        this.deltaTime = deltaTime;
        super.update(deltaTime);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void setSize(float width, float height) {
        throw new RuntimeException("Root group can not be resized");
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup
    public void addBackground(TextureRegion background) {
        this.background = background;
    }

    public void addBackground(Asset asset) {
        this.background = AssetsCache.get(this, asset);
    }

    public void addBackground(Texture backgroundTexture) {
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.background = new TextureRegion(backgroundTexture, 0, 0, 480, 320);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        this.effects.clear();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup
    protected Vector2 getAbsolutePosition(ScreenElement e) {
        Vector2 pos = new Vector2(0.0f, 0.0f);
        pos.add(this.elements.get(e));
        return pos;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup
    protected void addEffect(Effect effect) {
        this.effects.add(effect);
    }
}
