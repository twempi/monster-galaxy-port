package com.badlogic.gdx.scenes.scene2d.actions;

/* JADX INFO: loaded from: classes.dex */
public class RepeatAction extends DelegateAction {
    public static final int FOREVER = -1;
    private int executedCount;
    private boolean finished;
    private int repeatCount;

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean act(float delta) {
        if (this.executedCount == this.repeatCount) {
            return true;
        }
        if (this.action.act(delta)) {
            if (this.repeatCount > 0) {
                this.executedCount++;
            }
            if (this.executedCount == this.repeatCount) {
                return true;
            }
            this.action.restart();
        }
        return false;
    }

    public void finish() {
        this.finished = true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.DelegateAction, com.badlogic.gdx.scenes.scene2d.Action
    public void restart() {
        super.restart();
        this.executedCount = 0;
        this.finished = false;
    }

    public void setCount(int count) {
        this.repeatCount = count;
    }

    public int getCount() {
        return this.repeatCount;
    }
}
