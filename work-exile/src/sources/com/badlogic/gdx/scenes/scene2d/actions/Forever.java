package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.TemporalAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Forever extends TemporalAction {
    static final ActionResetingPool<Forever> pool = new ActionResetingPool<Forever>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.Forever.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Forever newObject() {
            return new Forever();
        }
    };

    public static Forever $(Action action) {
        Forever forever = (Forever) pool.obtain();
        forever.action = action;
        return forever;
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
            Action oldAction = this.action;
            this.action = this.action.copy();
            oldAction.finish();
            this.action.setTarget(this.target);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        pool.free(this);
        this.action.finish();
        super.finish();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        return $(this.action.copy());
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }
}
