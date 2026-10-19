package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class EffectActor extends Actor {
    private Effect effect;

    public EffectActor(String name) {
        super(name);
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void setEffectWithType(EffectType effectType) {
        setEffect(EffectManager.getInstance().getEffect(effectType).getCopy());
        this.effect.start();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.effect != null) {
            this.effect.setPosition(this.x, this.y);
            this.effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        return null;
    }
}
