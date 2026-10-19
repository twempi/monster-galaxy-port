package com.badlogic.gdx.scenes.scene2d;

import java.util.ArrayList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class CompositeAction extends Action {
    protected final ArrayList<Action> actions = new ArrayList<>();

    public ArrayList<Action> getActions() {
        return this.actions;
    }
}
