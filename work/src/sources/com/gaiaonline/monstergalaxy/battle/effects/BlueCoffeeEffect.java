package com.gaiaonline.monstergalaxy.battle.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gaiaonline.monstergalaxy.app.SoundManager;

/* JADX INFO: loaded from: classes.dex */
public class BlueCoffeeEffect extends Effect {
    private Effect currentEffect;
    private final Effect effect1;
    private final Effect effect2;
    private Effect.OnCompleteListener listener;

    public BlueCoffeeEffect(String name) {
        super(name);
        this.effect1 = EffectManager.getInstance().getEffect(EffectType.BLUE_COFFEE_1).getCopy();
        this.effect2 = EffectManager.getInstance().getEffect(EffectType.BLUE_COFFEE_2).getCopy();
        this.effect1.setOnCompleteListener(new Effect.OnCompleteListener() { // from class: com.gaiaonline.monstergalaxy.battle.effects.BlueCoffeeEffect.1
            @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect.OnCompleteListener
            public void onComplete() {
                BlueCoffeeEffect.this.currentEffect = BlueCoffeeEffect.this.effect2;
                BlueCoffeeEffect.this.effect2.start();
            }
        });
        this.effect2.setOnCompleteListener(new Effect.OnCompleteListener() { // from class: com.gaiaonline.monstergalaxy.battle.effects.BlueCoffeeEffect.2
            @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect.OnCompleteListener
            public void onComplete() {
                if (BlueCoffeeEffect.this.listener != null) {
                    BlueCoffeeEffect.this.listener.onComplete();
                }
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect
    public void setPosition(float x, float y) {
        this.effect1.setPosition(x, y);
        this.effect2.setPosition(x, y);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect
    public void setOnCompleteListener(Effect.OnCompleteListener listener) {
        this.listener = listener;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect
    public void start() {
        SoundManager.playSound(SoundManager.SOUND_BLUE_COFFEE);
        this.currentEffect = this.effect1;
        this.currentEffect.start();
    }

    @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect
    public void draw(SpriteBatch batch, float deltaTime) {
        this.currentEffect.draw(batch, deltaTime);
    }
}
