package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

/* JADX INFO: loaded from: classes.dex */
public class RunnableAction extends Action {
    private boolean ran;
    private Runnable runnable;

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean act(float delta) {
        if (!this.ran) {
            run();
            this.ran = true;
        }
        return true;
    }

    public void run() {
        this.runnable.run();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void restart() {
        this.ran = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action, com.badlogic.gdx.utils.Pool.Poolable
    public void reset() {
        super.reset();
        this.runnable = null;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
