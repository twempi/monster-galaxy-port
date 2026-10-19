package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: classes.dex */
public class ActorScreenElement extends ScreenElement {
    protected Actor actor;
    protected Group dummyGroup;

    public void setActorDeprecated(Actor actor) {
        this.actor = actor;
        this.dummyGroup = new Group();
        this.dummyGroup.addActor(actor);
        setSize(actor.getWidth(), actor.getHeight());
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        this.dummyGroup = new Group();
        this.dummyGroup.addActor(actor);
        setSize(actor.getWidth() / ResolutionManager.getScaleFactor(), actor.getHeight() / ResolutionManager.getScaleFactor());
    }

    public Actor getActor() {
        return this.actor;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batch) {
        Color c = batch.getColor();
        if (this.color != null) {
            this.actor.setColor(this.actor.getColor().set(this.color));
        }
        this.actor.setX(pos.x);
        this.actor.setY(pos.y);
        this.actor.draw(batch, 1.0f);
        if (c != null) {
            batch.setColor(c);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        this.actor.act(deltaTime);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public Vector2 getSize() {
        return super.getSize().mul(getActor().getScaleX());
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        this.actor = null;
        this.dummyGroup = null;
    }
}
