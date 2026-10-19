package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class ActionResetingPool<T extends Action> extends Pool<T> {
    public ActionResetingPool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override // com.badlogic.gdx.utils.Pool
    public T obtain() {
        T t = (T) super.obtain();
        t.reset();
        return t;
    }
}
