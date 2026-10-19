package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;

/* JADX INFO: loaded from: classes.dex */
public class EffectActor extends Actor {
    private Effect effect;

    public EffectActor(String name) {
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        setWidth(Gdx.graphics.getWidth());
        setHeight(Gdx.graphics.getHeight());
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
            this.effect.setPosition(getX(), getY());
            this.effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
    }
}
