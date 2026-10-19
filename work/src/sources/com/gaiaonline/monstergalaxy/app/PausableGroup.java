package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;

/* JADX INFO: loaded from: classes.dex */
public class PausableGroup extends Group {
    public boolean paused;

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void act(float delta) {
        try {
            if (!this.paused) {
                super.act(delta);
            }
        } catch (Exception e) {
            Gdx.app.log(Game.TAG, "PausableGroup: sorry, could not be paused.", e);
        }
    }
}
