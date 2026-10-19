package com.badlogic.gdx.scenes.scene2d;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Action {
    protected OnActionCompleted listener = null;

    public abstract void act(float f);

    public abstract Action copy();

    public abstract Actor getTarget();

    public abstract boolean isDone();

    public abstract void setTarget(Actor actor);

    public void finish() {
        if (this.listener != null) {
            this.listener.completed(this);
        }
    }

    public void callActionCompletedListener() {
        if (this.listener != null) {
            this.listener.completed(this);
        }
        this.listener = null;
    }

    public Action setCompletionListener(OnActionCompleted listener) {
        this.listener = listener;
        return this;
    }

    public OnActionCompleted getCompletionListener() {
        return this.listener;
    }

    public void reset() {
        this.listener = null;
    }
}
