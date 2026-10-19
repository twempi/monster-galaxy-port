package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Slider extends Widget {
    ValueChangedListener listener;
    float max;
    float min;
    float sliderPos;
    float steps;
    final SliderStyle style;
    float value;

    public interface ValueChangedListener {
        void changed(Slider slider, float f);
    }

    public Slider(String name, float prefWidth, float min, float max, float steps, SliderStyle style) {
        super(name, prefWidth, 0.0f);
        this.listener = null;
        this.style = style;
        if (min > max) {
            throw new IllegalArgumentException("min must be > max");
        }
        if (steps < 0.0f) {
            throw new IllegalArgumentException("unit must be > 0");
        }
        this.min = min;
        this.max = max;
        this.steps = steps;
        this.value = min;
        layout();
        this.width = prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        this.prefHeight = Math.max(this.style.knob.getRegionHeight(), this.style.slider.getTotalHeight());
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        TextureRegion knob = this.style.knob;
        NinePatch slider = this.style.slider;
        if (this.invalidated) {
            layout();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        this.sliderPos = ((this.value - this.min) / (this.max - this.min)) * (this.width - knob.getRegionWidth());
        this.sliderPos = Math.max(0.0f, this.sliderPos);
        this.sliderPos = Math.min(this.width - knob.getRegionWidth(), this.sliderPos);
        float maxHeight = Math.max(knob.getRegionHeight(), slider.getTotalHeight());
        slider.draw(batch, this.x, ((int) ((maxHeight - slider.getTotalHeight()) * 0.5f)) + this.y, this.width, slider.getTotalHeight());
        batch.draw(knob, this.x + this.sliderPos, this.y + ((int) ((maxHeight - knob.getRegionHeight()) * 0.5f)));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0 || hit(x, y) == null) {
            return false;
        }
        calculateSliderPosAndValue(x);
        this.parent.focus(this, pointer);
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (pointer != 0 || this.parent.focusedActor[0] != this) {
            return false;
        }
        calculateSliderPosAndValue(x);
        this.parent.focus(null, pointer);
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        if (pointer != 0 || this.parent.focusedActor[0] != this) {
            return false;
        }
        calculateSliderPosAndValue(x);
        return true;
    }

    private void calculateSliderPosAndValue(float x) {
        TextureRegion knob = this.style.knob;
        this.sliderPos = x - (knob.getRegionWidth() / 2);
        this.sliderPos = Math.max(0.0f, this.sliderPos);
        this.sliderPos = Math.min(this.width - knob.getRegionWidth(), this.sliderPos);
        this.value = this.min + ((this.max - this.min) * (this.sliderPos / (this.width - knob.getRegionWidth())));
        if (this.listener != null) {
            this.listener.changed(this, getValue());
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
            return null;
        }
        return this;
    }

    public static class SliderStyle {
        TextureRegion knob;
        NinePatch slider;

        public SliderStyle(NinePatch sliderPatch, TextureRegion knobRegion) {
            this.slider = sliderPatch;
            this.knob = knobRegion;
        }
    }

    public Slider setValueChangedListener(ValueChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public float getValue() {
        return ((float) Math.floor(this.value / this.steps)) * this.steps;
    }

    public void setValue(float value) {
        if (value < this.min || value > this.max) {
            throw new IllegalArgumentException("value must be >= min && <= max");
        }
        this.value = value;
        if (this.listener != null) {
            this.listener.changed(this, getValue());
        }
    }

    public void setRange(float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be < max");
        }
        this.min = min;
        this.max = max;
        this.value = min;
        if (this.listener != null) {
            this.listener.changed(this, getValue());
        }
    }
}
