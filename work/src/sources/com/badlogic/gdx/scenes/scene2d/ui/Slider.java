package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

/* JADX INFO: loaded from: classes.dex */
public class Slider extends Widget {
    int draggingPointer;
    private float max;
    private float min;
    private float sliderPos;
    private float steps;
    private SliderStyle style;
    private float value;
    private boolean vertical;

    public Slider(float min, float max, float steps, boolean vertical, Skin skin) {
        this(min, max, steps, vertical, (SliderStyle) skin.get("default-" + (vertical ? "vertical" : "horizontal"), SliderStyle.class));
    }

    public Slider(float min, float max, float steps, boolean vertical, Skin skin, String styleName) {
        this(min, max, steps, vertical, (SliderStyle) skin.get(styleName, SliderStyle.class));
    }

    public Slider(float min, float max, float steps, boolean vertical, SliderStyle style) {
        this.draggingPointer = -1;
        if (min > max) {
            throw new IllegalArgumentException("min must be > max: " + min + " > " + max);
        }
        if (steps <= 0.0f) {
            throw new IllegalArgumentException("steps must be > 0: " + steps);
        }
        setStyle(style);
        this.min = min;
        this.max = max;
        this.steps = steps;
        this.vertical = vertical;
        this.value = min;
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        addListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Slider.1
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Slider.this.draggingPointer != -1) {
                    return false;
                }
                Slider.this.draggingPointer = pointer;
                Slider.this.calculatePositionAndValue(x, y);
                return true;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == Slider.this.draggingPointer) {
                    Slider.this.draggingPointer = -1;
                    Slider.this.calculatePositionAndValue(x, y);
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Slider.this.calculatePositionAndValue(x, y);
            }
        });
    }

    public void setStyle(SliderStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.style = style;
        invalidateHierarchy();
    }

    public SliderStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Drawable knob = this.style.knob;
        Drawable bg = this.style.background;
        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (this.vertical) {
            bg.draw(batch, x + ((int) ((width - bg.getMinWidth()) * 0.5f)), y, bg.getMinWidth(), height);
            float height2 = height - (bg.getTopHeight() + bg.getBottomHeight());
            this.sliderPos = ((this.value - this.min) / (this.max - this.min)) * (height2 - knob.getMinHeight());
            this.sliderPos = Math.max(0.0f, this.sliderPos);
            this.sliderPos = Math.min(height2 - knob.getMinHeight(), this.sliderPos) + bg.getBottomHeight();
            knob.draw(batch, x + ((int) ((width - knob.getMinWidth()) * 0.5f)), y + this.sliderPos, knob.getMinWidth(), knob.getMinHeight());
            return;
        }
        bg.draw(batch, x, y + ((int) ((height - bg.getMinHeight()) * 0.5f)), width, bg.getMinHeight());
        float width2 = width - (bg.getLeftWidth() + bg.getRightWidth());
        this.sliderPos = ((this.value - this.min) / (this.max - this.min)) * (width2 - knob.getMinWidth());
        this.sliderPos = Math.max(0.0f, this.sliderPos);
        this.sliderPos = Math.min(width2 - knob.getMinWidth(), this.sliderPos) + bg.getLeftWidth();
        knob.draw(batch, x + this.sliderPos, y + ((int) ((height - knob.getMinHeight()) * 0.5f)), knob.getMinWidth(), knob.getMinHeight());
    }

    void calculatePositionAndValue(float x, float y) {
        float value;
        Drawable knob = this.style.knob;
        Drawable bg = this.style.background;
        float oldPosition = this.sliderPos;
        if (this.vertical) {
            float height = (getHeight() - bg.getTopHeight()) - bg.getBottomHeight();
            this.sliderPos = (y - bg.getBottomHeight()) - (knob.getMinHeight() * 0.5f);
            this.sliderPos = Math.max(0.0f, this.sliderPos);
            this.sliderPos = Math.min(height - knob.getMinHeight(), this.sliderPos);
            value = this.min + ((this.max - this.min) * (this.sliderPos / (height - knob.getMinHeight())));
        } else {
            float width = (getWidth() - bg.getLeftWidth()) - bg.getRightWidth();
            this.sliderPos = (x - bg.getLeftWidth()) - (knob.getMinWidth() * 0.5f);
            this.sliderPos = Math.max(0.0f, this.sliderPos);
            this.sliderPos = Math.min(width - knob.getMinWidth(), this.sliderPos);
            value = this.min + ((this.max - this.min) * (this.sliderPos / (width - knob.getMinWidth())));
        }
        float oldValue = value;
        setValue(value);
        if (value == oldValue) {
            this.sliderPos = oldPosition;
        }
    }

    public boolean isDragging() {
        return this.draggingPointer != -1;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        if (value < this.min || value > this.max) {
            throw new IllegalArgumentException("value must be >= min and <= max: " + value);
        }
        float value2 = MathUtils.clamp(Math.round(value / this.steps) * this.steps, this.min, this.max);
        float oldValue = this.value;
        if (value2 != oldValue) {
            this.value = value2;
            ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
            if (fire(changeEvent)) {
                this.value = oldValue;
            }
            Pools.free(changeEvent);
        }
    }

    public void setRange(float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be < max");
        }
        this.min = min;
        this.max = max;
        setValue(min);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        if (this.vertical) {
            return Math.max(this.style.knob.getMinWidth(), this.style.background.getMinWidth());
        }
        return 140.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        if (this.vertical) {
            return 140.0f;
        }
        return Math.max(this.style.knob.getMinHeight(), this.style.background.getMinHeight());
    }

    public float getMinValue() {
        return this.min;
    }

    public float getMaxValue() {
        return this.max;
    }

    public static class SliderStyle {
        public Drawable background;
        public Drawable knob;

        public SliderStyle() {
        }

        public SliderStyle(Drawable background, Drawable knob) {
            this.background = background;
            this.knob = knob;
        }

        public SliderStyle(SliderStyle style) {
            this.background = style.background;
            this.knob = style.knob;
        }
    }
}
