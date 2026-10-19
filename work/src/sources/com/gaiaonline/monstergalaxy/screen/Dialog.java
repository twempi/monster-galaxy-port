package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: classes.dex */
public abstract class Dialog extends ButtonUI {
    protected boolean keepAnimation;
    private Vector2 origin;
    private boolean overlay;

    public Dialog(ScreenListener screen) {
        this(screen, true);
        this.keepAnimation = false;
    }

    public boolean isKeepingAnimation() {
        return this.keepAnimation;
    }

    public Dialog(ScreenListener screen, boolean overlay) {
        this(screen, overlay, false);
    }

    public Dialog(ScreenListener screen, boolean overlay, boolean stretchable) {
        super(screen);
        this.keepAnimation = false;
        this.keepAnimation = false;
        this.overlay = overlay;
        this.origin = new Vector2(0.0f, 0.0f);
        setStretchable(stretchable);
        this.size.x = Gdx.graphics.getWidth();
        this.size.y = Gdx.graphics.getHeight();
        setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        setAnchorPoint(ScreenElement.RelPoint.CENTER);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void present(SpriteBatch batcher) {
        batcher.begin();
        super.present(this.origin, batcher);
        batcher.end();
    }

    public void show() {
        if (this.overlay) {
            addOverlay(0.5f);
        }
    }

    public void hide() {
        removeOverlay();
    }

    public void pause() {
    }

    public void resume() {
    }
}
