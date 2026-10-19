package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.CompositeAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Sequence extends CompositeAction {
    static final ActionResetingPool<Sequence> pool = new ActionResetingPool<Sequence>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.Sequence.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Sequence newObject() {
            return new Sequence();
        }
    };
    protected int currAction = 0;
    protected Actor target;

    public static Sequence $(Action... actions) {
        Sequence sequence = (Sequence) pool.obtain();
        sequence.actions.clear();
        for (Action action : actions) {
            sequence.actions.add(action);
        }
        return sequence;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        if (this.actions.size() > 0) {
            this.actions.get(0).setTarget(this.target);
        }
        this.currAction = 0;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        if (this.actions.size() == 0) {
            this.currAction = 1;
            return;
        }
        if (this.currAction >= this.actions.size()) {
            callActionCompletedListener();
            return;
        }
        this.actions.get(this.currAction).act(delta);
        if (this.actions.get(this.currAction).isDone()) {
            this.actions.get(this.currAction).callActionCompletedListener();
            this.currAction++;
            if (this.currAction < this.actions.size()) {
                this.actions.get(this.currAction).setTarget(this.target);
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return this.currAction >= this.actions.size();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        pool.free(this);
        super.finish();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        Sequence action = (Sequence) pool.obtain();
        action.actions.clear();
        int len = this.actions.size();
        for (int i = 0; i < len; i++) {
            action.actions.add(this.actions.get(i).copy());
        }
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }
}
