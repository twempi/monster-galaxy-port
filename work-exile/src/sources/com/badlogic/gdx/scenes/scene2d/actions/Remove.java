package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Remove extends Action {
    private static final ActionResetingPool<Remove> pool = new ActionResetingPool<Remove>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.Remove.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Remove newObject() {
            return new Remove();
        }
    };
    protected boolean removed = false;
    protected Actor target;

    public static Remove $() {
        Remove remove = (Remove) pool.obtain();
        remove.removed = false;
        remove.target = null;
        return remove;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        if (!this.removed) {
            this.target.markToRemove(true);
            this.removed = true;
            callActionCompletedListener();
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return this.removed;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        return $();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }
}
