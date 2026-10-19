package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

/* JADX INFO: loaded from: classes.dex */
public abstract class TemporalAction extends Action {
    private boolean complete;
    private float duration;
    private Interpolation interpolation;
    private boolean reverse;
    private float time;

    protected abstract void update(float f);

    public TemporalAction() {
    }

    public TemporalAction(float duration) {
        this.duration = duration;
    }

    public TemporalAction(float duration, Interpolation interpolation) {
        this.duration = duration;
        this.interpolation = interpolation;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean act(float delta) {
        float percent;
        if (this.complete) {
            return true;
        }
        if (this.time == 0.0f) {
            begin();
        }
        this.time += delta;
        this.complete = this.time >= this.duration;
        if (this.complete) {
            percent = 1.0f;
        } else {
            percent = this.time / this.duration;
            if (this.interpolation != null) {
                percent = this.interpolation.apply(percent);
            }
        }
        if (this.reverse) {
            percent = 1.0f - percent;
        }
        update(percent);
        if (this.complete) {
            end();
        }
        return this.complete;
    }

    protected void begin() {
    }

    protected void end() {
    }

    public void finish() {
        this.time = this.duration;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void restart() {
        this.time = 0.0f;
        this.complete = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action, com.badlogic.gdx.utils.Pool.Poolable
    public void reset() {
        super.reset();
        this.reverse = false;
        this.interpolation = null;
    }

    public float getTime() {
        return this.time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Interpolation getInterpolation() {
        return this.interpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public boolean isReverse() {
        return this.reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
