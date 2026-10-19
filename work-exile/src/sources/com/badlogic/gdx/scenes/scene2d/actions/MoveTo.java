package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MoveTo extends AnimationAction {
    private static final ActionResetingPool<MoveTo> pool = new ActionResetingPool<MoveTo>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.MoveTo.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public MoveTo newObject() {
            return new MoveTo();
        }
    };
    protected float deltaX;
    protected float deltaY;
    protected float startX;
    protected float startY;
    protected float x;
    protected float y;

    public static MoveTo $(float x, float y, float duration) {
        MoveTo action = (MoveTo) pool.obtain();
        action.x = x;
        action.y = y;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startX = this.target.x;
        this.startY = this.target.y;
        this.deltaX = this.x - this.target.x;
        this.deltaY = this.y - this.target.y;
        this.taken = 0.0f;
        this.done = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            this.target.x = this.x;
            this.target.y = this.y;
            return;
        }
        this.target.x = this.startX + (this.deltaX * alpha);
        this.target.y = this.startY + (this.deltaY * alpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        MoveTo moveTo = $(this.x, this.y, this.duration);
        if (this.interpolator != null) {
            moveTo.setInterpolator(this.interpolator.copy());
        }
        return moveTo;
    }
}
