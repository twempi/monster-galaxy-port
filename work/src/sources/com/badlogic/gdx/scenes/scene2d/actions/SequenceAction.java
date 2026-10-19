package com.badlogic.gdx.scenes.scene2d.actions;

/* JADX INFO: loaded from: classes.dex */
public class SequenceAction extends ParallelAction {
    private int index;

    @Override // com.badlogic.gdx.scenes.scene2d.actions.ParallelAction, com.badlogic.gdx.scenes.scene2d.Action
    public boolean act(float delta) {
        if (this.index >= this.actions.size) {
            return true;
        }
        if (this.actions.get(this.index).act(delta)) {
            this.index++;
            if (this.index > this.actions.size) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.ParallelAction, com.badlogic.gdx.scenes.scene2d.Action
    public void restart() {
        super.restart();
        this.index = 0;
    }
}
