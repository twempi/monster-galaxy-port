package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/* JADX INFO: loaded from: classes.dex */
public abstract class Action3D extends TemporalAction {
    protected int gridSizeX;
    protected int gridSizeY;
    protected Grid3D transformation;

    public abstract void render(SpriteBatch spriteBatch);

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setActor(Actor actor) {
        super.setActor(actor);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        ((MogaActor) this.actor).setAction3D(this);
        super.begin();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void end() {
        super.end();
        ((MogaActor) getActor()).setAction3D(null);
    }
}
