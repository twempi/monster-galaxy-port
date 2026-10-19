package com.badlogic.gdx.scenes.scene2d;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class AnimationAction extends Action {
    protected boolean done;
    protected float duration;
    protected Interpolator interpolator;
    protected float invDuration;
    protected float taken;
    protected Actor target;

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return this.done;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        if (this.interpolator != null) {
            this.interpolator.finished();
        }
    }

    public AnimationAction setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    protected float createInterpolatedAlpha(float delta) {
        this.taken += delta;
        if (this.taken >= this.duration) {
            this.taken = this.duration;
            this.done = true;
            return this.taken;
        }
        if (this.interpolator == null) {
            return this.taken * this.invDuration;
        }
        float interpolatedTime = this.interpolator.getInterpolation(this.taken / this.duration) * this.duration;
        return this.invDuration * interpolatedTime;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void reset() {
        super.reset();
        this.interpolator = null;
    }
}
