package com.badlogic.gdx.scenes.scene2d.actions;

import com.gaiaonline.monstergalaxy.app.Game;

/* JADX INFO: loaded from: classes.dex */
public class DelayedRunnableAction extends RunnableAction {
    @Override // com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
    public void run() {
        Game.scheduleToGameThread(getRunnable());
    }
}
