package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FadeTo extends AnimationAction {
    private static final ActionResetingPool<FadeTo> pool = new ActionResetingPool<FadeTo>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.FadeTo.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public FadeTo newObject() {
            return new FadeTo();
        }
    };
    protected float startAlpha;
    protected float toAlpha = 0.0f;
    protected float deltaAlpha = 0.0f;

    public static FadeTo $(float alpha, float duration) {
        FadeTo action = (FadeTo) pool.obtain();
        action.toAlpha = Math.min(Math.max(alpha, 0.0f), 1.0f);
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startAlpha = this.target.color.a;
        this.deltaAlpha = this.toAlpha - this.target.color.a;
        this.taken = 0.0f;
        this.done = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            this.target.color.a = this.toAlpha;
        } else {
            float val = this.startAlpha + (this.deltaAlpha * alpha);
            this.target.color.a = Math.min(Math.max(val, 0.0f), 1.0f);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        FadeTo fadeTo = $(this.toAlpha, this.duration);
        if (this.interpolator != null) {
            fadeTo.setInterpolator(this.interpolator.copy());
        }
        return fadeTo;
    }
}
