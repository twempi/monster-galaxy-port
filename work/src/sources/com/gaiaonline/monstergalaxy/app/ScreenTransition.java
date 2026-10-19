package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.gaiaonline.monstergalaxy.battle.anim.FadeOutTiles;

/* JADX INFO: loaded from: classes.dex */
public class ScreenTransition {
    public static final int START_LOWER_LEFT = 1;
    public static final int START_UPPER_RIGHT = 0;
    private static final float TRANSITION_DURATION = 0.5f;
    private Texture texture;
    private final FadeOutTiles transition;
    private float progress = 0.0f;
    private Boolean isReady = Boolean.FALSE;

    public ScreenTransition(int start) {
        if (start != 0 && start != 1) {
            throw new IllegalArgumentException("Unknown transition");
        }
        this.transition = new FadeOutTiles(start);
    }

    public void captureScreen() {
        this.isReady = Boolean.FALSE;
        synchronized (this.isReady) {
            int w = Gdx.graphics.getWidth();
            int h = Gdx.graphics.getHeight();
            int potW = MathUtils.nextPowerOfTwo(w);
            int potH = MathUtils.nextPowerOfTwo(h);
            Pixmap pixmap = new Pixmap(potW, potH, Pixmap.Format.RGBA8888);
            Gdx.gl.glReadPixels(0, 0, potW, potH, 6408, 5121, pixmap.getPixels());
            this.texture = new Texture(pixmap);
            this.transition.setTexture(this.texture);
            this.transition.setGridSize(18, 12);
            this.transition.setImageSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.transition.initialize();
            this.isReady = Boolean.TRUE;
        }
    }

    public void update(float deltaTime) {
        this.progress += deltaTime / TRANSITION_DURATION;
        if (this.transition != null && this.texture != null && this.isReady.booleanValue()) {
            this.transition.updateVertices(this.progress);
        }
    }

    public void render(SpriteBatch batcher) {
        if (this.progress < 1.0f) {
            this.transition.render();
        }
    }

    public boolean isFinished() {
        return this.progress >= 1.0f;
    }

    public void dispose() {
    }
}
