package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class ParallelAction extends Action {
    Array<Action> actions = new Array<>(4);
    private boolean complete;

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean act(float delta) {
        if (this.complete) {
            return true;
        }
        this.complete = true;
        Array<Action> actions = this.actions;
        int n = actions.size;
        for (int i = 0; i < n; i++) {
            if (!actions.get(i).act(delta)) {
                this.complete = false;
            }
        }
        return this.complete;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void restart() {
        this.complete = false;
        Array<Action> actions = this.actions;
        int n = actions.size;
        for (int i = 0; i < n; i++) {
            actions.get(i).restart();
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action, com.badlogic.gdx.utils.Pool.Poolable
    public void reset() {
        super.reset();
        this.actions.clear();
    }

    public void addAction(Action action) {
        this.actions.add(action);
        if (this.actor != null) {
            action.setActor(this.actor);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setActor(Actor actor) {
        Array<Action> actions = this.actions;
        int n = actions.size;
        for (int i = 0; i < n; i++) {
            actions.get(i).setActor(actor);
        }
        super.setActor(actor);
    }

    public Array<Action> getActions() {
        return this.actions;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public String toString() {
        StringBuilder buffer = new StringBuilder(64);
        buffer.append(super.toString());
        buffer.append('(');
        Array<Action> actions = this.actions;
        int n = actions.size;
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(actions.get(i));
        }
        buffer.append(')');
        return buffer.toString();
    }
}
