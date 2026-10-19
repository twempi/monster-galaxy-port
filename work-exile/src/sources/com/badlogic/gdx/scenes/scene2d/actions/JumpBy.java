package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JumpBy extends AnimationAction {
    private static final ActionResetingPool<JumpBy> pool = new ActionResetingPool<JumpBy>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.JumpBy.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public JumpBy newObject() {
            return new JumpBy();
        }
    };
    protected float deltaX;
    protected float deltaY;
    private float height;
    protected float initialX;
    protected float initialY;
    private int jumps;
    protected float startX;
    protected float startY;
    protected float x;
    protected float y;

    public static JumpBy $(float x, float y, float height, int jumps, float duration) {
        JumpBy action = (JumpBy) pool.obtain();
        action.initialX = x;
        action.x = x;
        action.initialY = y;
        action.y = y;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        action.height = height;
        action.jumps = jumps;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startX = this.target.x;
        this.startY = this.target.y;
        this.deltaX = this.x;
        this.deltaY = this.y;
        this.x = this.target.x + this.x;
        this.y = this.target.y + this.y;
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
        this.target.y = this.startY + (this.height * ((float) Math.abs(Math.sin(3.1415927f * alpha * this.jumps))));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        JumpBy jumpBy = $(this.initialX, this.initialY, this.height, this.jumps, this.duration);
        if (this.interpolator != null) {
            jumpBy.setInterpolator(this.interpolator.copy());
        }
        return jumpBy;
    }
}
