package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;

/**
 * A 0..1 slider drawn with the game's HP bar art. Game screens only deliver
 * tap-down/up to elements, so dragging is handled by polling the pointer.
 */
public class VolumeSlider extends ScreenElement {
    public interface Listener {
        /** Called while dragging (done=false) and once on release (done=true). */
        void onValue(float value, boolean done);
    }

    private static final float TRACK_INSET_X = 4f; // fill inset inside the track, in atlas pixels
    private static final float KNOB_SIZE = 26f;

    private final TextureRegion track = Assets.loadTexture("hpbarbackground");
    private final TextureRegion fill = Assets.loadTexture("hpbarfull");
    private final TextureRegion knob = Assets.loadTexture("white.circle");
    private final Listener listener;
    private final Vector2 drawnAt = new Vector2(-1, -1);
    private float value;
    private boolean pointerWasDown;
    private boolean dragging;

    public VolumeSlider(float value, Listener listener) {
        this.value = clamp(value);
        this.listener = listener;
        setSize(track.getRegionWidth(), track.getRegionHeight());
    }

    public float getValue() {
        return value;
    }

    @Override
    protected void present(Vector2 pos, SpriteBatch batch) {
        drawnAt.set(pos);
        float scale = size.y / track.getRegionHeight();
        batch.draw(track, pos.x, pos.y, size.x, size.y);

        float inset = TRACK_INSET_X * scale;
        float fillMaxWidth = size.x - 2 * inset;
        float fillHeight = fill.getRegionHeight() * scale;
        float fillY = pos.y + (size.y - fillHeight) / 2;
        int regionWidth = Math.round(fill.getRegionWidth() * value);
        if (regionWidth > 0) {
            TextureRegion part = new TextureRegion(fill, 0, 0, regionWidth, fill.getRegionHeight());
            batch.draw(part, pos.x + inset, fillY, fillMaxWidth * value, fillHeight);
        }

        float knobSize = KNOB_SIZE * scale;
        float knobX = pos.x + inset + fillMaxWidth * value - knobSize / 2;
        batch.draw(knob, knobX, pos.y + (size.y - knobSize) / 2, knobSize, knobSize);
    }

    @Override
    protected void update(float deltaTime) {
        boolean down = Gdx.input.isTouched();
        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (down && !pointerWasDown && contains(x, y)) {
            dragging = true;
        }
        if (dragging) {
            if (down) {
                setFromPointer(x);
                listener.onValue(value, false);
            } else {
                dragging = false;
                listener.onValue(value, true);
            }
        }
        pointerWasDown = down;
    }

    private boolean contains(float x, float y) {
        if (drawnAt.x < 0) {
            return false;
        }
        float slack = 10 * ResolutionManager.getScaleFactor(); // easier to grab than the thin bar
        return x >= drawnAt.x - slack && x <= drawnAt.x + size.x + slack
            && y >= drawnAt.y - slack && y <= drawnAt.y + size.y + slack;
    }

    private void setFromPointer(float x) {
        float inset = TRACK_INSET_X * size.y / track.getRegionHeight();
        float raw = clamp((x - drawnAt.x - inset) / (size.x - 2 * inset));
        value = Math.round(raw * 20) / 20f; // 5% steps
    }

    private static float clamp(float v) {
        return v < 0 ? 0 : v > 1 ? 1 : v;
    }

    @Override
    protected void dispose() {
    }
}
