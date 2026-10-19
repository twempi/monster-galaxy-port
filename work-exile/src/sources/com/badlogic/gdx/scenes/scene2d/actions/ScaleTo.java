package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ScaleTo extends AnimationAction {
    private static final ActionResetingPool<ScaleTo> pool = new ActionResetingPool<ScaleTo>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.ScaleTo.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public ScaleTo newObject() {
            return new ScaleTo();
        }
    };
    protected float deltaScaleX;
    protected float deltaScaleY;
    protected float scaleX;
    protected float scaleY;
    protected float startScaleX;
    protected float startScaleY;

    public static ScaleTo $(float scaleX, float scaleY, float duration) {
        ScaleTo action = (ScaleTo) pool.obtain();
        action.scaleX = scaleX;
        action.scaleY = scaleY;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startScaleX = this.target.scaleX;
        this.deltaScaleX = this.scaleX - this.target.scaleX;
        this.startScaleY = this.target.scaleY;
        this.deltaScaleY = this.scaleY - this.target.scaleY;
        this.taken = 0.0f;
        this.done = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            this.target.scaleX = this.scaleX;
            this.target.scaleY = this.scaleY;
            return;
        }
        this.target.scaleX = this.startScaleX + (this.deltaScaleX * alpha);
        this.target.scaleY = this.startScaleY + (this.deltaScaleY * alpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        ScaleTo scaleTo = $(this.scaleX, this.scaleY, this.duration);
        if (this.interpolator != null) {
            scaleTo.setInterpolator(this.interpolator.copy());
        }
        return scaleTo;
    }
}
