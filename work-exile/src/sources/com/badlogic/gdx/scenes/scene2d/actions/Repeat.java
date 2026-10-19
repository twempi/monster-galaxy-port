package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.TemporalAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Repeat extends TemporalAction {
    static final ActionResetingPool<Repeat> pool = new ActionResetingPool<Repeat>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.Repeat.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Repeat newObject() {
            return new Repeat();
        }
    };
    protected int finishedTimes;
    protected int times;

    public static Repeat $(Action action, int times) {
        Repeat repeat = (Repeat) pool.obtain();
        repeat.action = action;
        repeat.times = times;
        return repeat;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void reset() {
        super.reset();
        this.finishedTimes = 0;
        this.listener = null;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.action.setTarget(actor);
        this.target = actor;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        this.action.act(delta);
        if (this.action.isDone()) {
            this.finishedTimes++;
            if (this.finishedTimes < this.times) {
                Action oldAction = this.action;
                this.action = this.action.copy();
                oldAction.finish();
                this.action.setTarget(this.target);
                return;
            }
            callActionCompletedListener();
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return this.finishedTimes >= this.times;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        pool.free(this);
        this.action.finish();
        super.finish();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        return $(this.action.copy(), this.times);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }
}
