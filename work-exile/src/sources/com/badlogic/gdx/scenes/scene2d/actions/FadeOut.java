package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FadeOut extends AnimationAction {
    private static final ActionResetingPool<FadeOut> pool = new ActionResetingPool<FadeOut>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.FadeOut.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public FadeOut newObject() {
            return new FadeOut();
        }
    };
    protected float startAlpha = 0.0f;
    protected float deltaAlpha = 0.0f;

    public static FadeOut $(float duration) {
        FadeOut action = (FadeOut) pool.obtain();
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startAlpha = this.target.color.a;
        this.deltaAlpha = -this.target.color.a;
        this.taken = 0.0f;
        this.done = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            this.target.color.a = 0.0f;
        } else {
            this.target.color.a = this.startAlpha + (this.deltaAlpha * alpha);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        FadeOut fadeOut = $(this.duration);
        if (this.interpolator != null) {
            fadeOut.setInterpolator(this.interpolator.copy());
        }
        return fadeOut;
    }
}
