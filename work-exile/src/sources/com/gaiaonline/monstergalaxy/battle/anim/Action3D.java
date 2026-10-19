package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Action3D extends AnimationAction {
    protected int gridSizeX;
    protected int gridSizeY;
    protected Grid3D transformation;

    public abstract void render(SpriteBatch spriteBatch);

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        ((MogaActor) this.target).setAction3D(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        createInterpolatedAlpha(delta);
        if (this.done) {
            ((MogaActor) this.target).setAction3D(null);
        }
    }
}
