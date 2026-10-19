package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.SoundManager;

/* JADX INFO: loaded from: classes.dex */
public class ButtonElement extends ScreenElement implements Touchable {
    private TextureRegion disabled;
    private final UIEvent eventCode;
    private boolean flip;
    protected boolean isEnabled;
    protected boolean isGreyedOut;
    protected boolean isPressed;
    private ScreenListener listener;
    private TextureRegion normal;
    private TextureRegion selected;
    private Color selectedColor;
    private TouchReleasedListener touchReleasedListener;

    public ButtonElement(ScreenListener listener, UIEvent eventCode, TextureRegion normal, TextureRegion selected, TextureRegion disabled) {
        this.listener = listener;
        this.eventCode = eventCode;
        this.normal = normal;
        this.selected = selected;
        setSize(normal.getRegionWidth(), normal.getRegionHeight());
        this.disabled = disabled;
        this.isEnabled = true;
    }

    public ButtonElement(ScreenListener listener, UIEvent eventCode, TextureRegion normal, TextureRegion selected) {
        this(listener, eventCode, normal, selected, null);
    }

    public ButtonElement(ScreenListener listener, UIEvent eventCode, TextureRegion normal) {
        this(listener, eventCode, normal, null, null);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Touchable
    public boolean touched(Vector2 touchPoint) {
        if (!this.isEnabled) {
            return false;
        }
        if (!this.isPressed) {
            SoundManager.playSound(SoundManager.SOUND_UI_TAP);
        }
        this.isPressed = true;
        return true;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Touchable
    public boolean touchReleased(Vector2 touchPoint, Vector2 releasePoint) {
        if (this.isEnabled && this.touchReleasedListener != null) {
            this.touchReleasedListener.touchReleased(touchPoint, releasePoint);
        }
        if (this.isEnabled && this.listener != null) {
            this.listener.onUIEvent(this.eventCode);
            return true;
        }
        return true;
    }

    public void setNormal(TextureRegion normal) {
        this.normal = normal;
        if (normal != null) {
            if (this.flip) {
                this.normal = flip(normal);
            }
            setSize(normal.getRegionWidth(), normal.getRegionHeight());
        }
        this.needsRecalc = true;
    }

    public void setSelected(TextureRegion select) {
        this.selected = select;
        if (this.selected != null) {
            if (this.flip) {
                this.selected = flip(this.selected);
            }
            setSize(this.selected.getRegionWidth(), this.selected.getRegionHeight());
        }
        this.needsRecalc = true;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setGreyedOut(boolean greyedOut) {
        this.isGreyedOut = greyedOut;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setScreenListener(ScreenListener listener) {
        this.listener = listener;
    }

    public void setTouchReleasedListener(TouchReleasedListener listener) {
        this.touchReleasedListener = listener;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void flip() {
        super.flip();
        this.flip = true;
        this.normal = flip(this.normal);
        this.selected = flip(this.selected);
        this.disabled = flip(this.disabled);
    }

    private TextureRegion flip(TextureRegion region) {
        if (region == null) {
            return null;
        }
        TextureRegion region2 = new TextureRegion(this.normal);
        region2.flip(true, false);
        return region2;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        Color originalColor = batcher.getColor();
        Color color = (this.selectedColor != null && this.isPressed && this.isEnabled) ? this.selectedColor : this.color;
        if (color != null) {
            batcher.setColor(color);
        }
        if ((!this.isEnabled || this.isGreyedOut) && this.disabled != null) {
            batcher.draw(this.disabled, pos.x, pos.y, this.size.x, this.size.y);
        } else if (this.selected != null && this.isPressed && this.isEnabled) {
            if (Gdx.input.isTouched()) {
                batcher.draw(this.selected, pos.x, pos.y, this.size.x, this.size.y);
            } else {
                this.isPressed = false;
                batcher.draw(this.normal, pos.x, pos.y, this.size.x, this.size.y);
            }
        } else {
            batcher.draw(this.normal, pos.x, pos.y, this.size.x, this.size.y);
        }
        if (color != null) {
            batcher.setColor(originalColor);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        this.normal = null;
        this.selected = null;
        this.disabled = null;
        this.listener = null;
        this.touchReleasedListener = null;
    }
}
