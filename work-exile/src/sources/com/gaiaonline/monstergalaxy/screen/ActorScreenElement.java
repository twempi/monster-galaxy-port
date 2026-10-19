package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.graphics.util.ActorUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ActorScreenElement extends ScreenElement {
    protected Actor actor;
    protected Group dummyGroup;

    public void setActorDeprecated(Actor actor) {
        this.actor = actor;
        this.dummyGroup = new Group();
        this.dummyGroup.addActor(actor);
        setSize(actor.width, actor.height);
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        this.dummyGroup = new Group();
        this.dummyGroup.addActor(actor);
        setSize(actor.width / ResolutionManager.getScaleFactor(), actor.height / ResolutionManager.getScaleFactor());
    }

    public Actor getActor() {
        return this.actor;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batch) {
        Color c = batch.getColor();
        if (this.color != null) {
            this.actor.color.set(this.color);
        }
        this.actor.x = pos.x;
        this.actor.y = pos.y;
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
        return super.getSize().mul(getActor().scaleX);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        ActorUtil.disposeActorRecursive(this.dummyGroup);
        this.actor = null;
        this.dummyGroup = null;
    }
}
