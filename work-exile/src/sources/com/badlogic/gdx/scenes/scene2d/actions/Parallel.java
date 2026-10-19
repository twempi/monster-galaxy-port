package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.CompositeAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Parallel extends CompositeAction {
    static final ActionResetingPool<Parallel> pool = new ActionResetingPool<Parallel>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.Parallel.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Parallel newObject() {
            return new Parallel();
        }
    };
    protected boolean[] finished;
    protected Actor target = null;

    public static Parallel $(Action... actions) {
        Parallel parallel = (Parallel) pool.obtain();
        parallel.actions.clear();
        if (parallel.finished == null || parallel.finished.length < actions.length) {
            parallel.finished = new boolean[actions.length];
        }
        int len = actions.length;
        for (int i = 0; i < len; i++) {
            parallel.finished[i] = false;
        }
        for (Action action : actions) {
            parallel.actions.add(action);
        }
        return parallel;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        int len = this.actions.size();
        for (int i = 0; i < len; i++) {
            this.actions.get(i).setTarget(actor);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        int len = this.actions.size();
        boolean allDone = true;
        for (int i = 0; i < len; i++) {
            Action action = this.actions.get(i);
            if (!action.isDone()) {
                action.act(delta);
                allDone = false;
            } else if (!this.finished[i]) {
                action.finish();
                this.finished[i] = true;
                allDone &= this.finished[i];
            }
        }
        if (allDone) {
            callActionCompletedListener();
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        int len = this.actions.size();
        for (int i = 0; i < len; i++) {
            if (!this.actions.get(i).isDone()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        pool.free(this);
        int len = this.actions.size();
        for (int i = 0; i < len; i++) {
            if (!this.finished[i]) {
                this.actions.get(i).finish();
            }
        }
        super.finish();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        Parallel parallel = (Parallel) pool.obtain();
        parallel.actions.clear();
        if (parallel.finished == null || parallel.finished.length < this.actions.size()) {
            parallel.finished = new boolean[this.actions.size()];
        }
        int len = this.actions.size();
        for (int i = 0; i < len; i++) {
            parallel.finished[i] = false;
        }
        int len2 = this.actions.size();
        for (int i2 = 0; i2 < len2; i2++) {
            parallel.actions.add(this.actions.get(i2).copy());
        }
        return parallel;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }
}
