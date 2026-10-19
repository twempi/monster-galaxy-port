package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class RotateTo extends AnimationAction {
    private static final ActionResetingPool<RotateTo> pool = new ActionResetingPool<RotateTo>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.RotateTo.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public RotateTo newObject() {
            return new RotateTo();
        }
    };
    protected float deltaRotation;
    protected float rotation;
    protected float startRotation;

    public static RotateTo $(float rotation, float duration) {
        RotateTo action = (RotateTo) pool.obtain();
        action.rotation = rotation;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startRotation = this.target.rotation;
        this.deltaRotation = this.rotation - this.target.rotation;
        this.taken = 0.0f;
        this.done = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            this.target.rotation = this.rotation;
        } else {
            this.target.rotation = this.startRotation + (this.deltaRotation * alpha);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        RotateTo rotateTo = $(this.rotation, this.duration);
        if (this.interpolator != null) {
            rotateTo.setInterpolator(this.interpolator.copy());
        }
        return rotateTo;
    }
}
