package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Delay extends Action {
    static final ActionResetingPool<Delay> pool = new ActionResetingPool<Delay>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.Delay.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Delay newObject() {
            return new Delay();
        }
    };
    protected Action action;
    protected float duration;
    protected float taken;

    public static Delay $(Action action, float duration) {
        Delay delay = (Delay) pool.obtain();
        delay.duration = duration;
        delay.action = action;
        return delay;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void reset() {
        super.reset();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.action.setTarget(actor);
        this.taken = 0.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        this.taken += delta;
        if (this.taken > this.duration) {
            callActionCompletedListener();
            this.action.act(delta);
            if (this.action.isDone()) {
                this.action.callActionCompletedListener();
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return this.taken > this.duration && this.action.isDone();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        pool.free(this);
        this.action.finish();
        super.finish();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        return $(this.action.copy(), this.duration);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.action.getTarget();
    }
}
