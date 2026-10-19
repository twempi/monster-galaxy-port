package com.gaiaonline.monstergalaxy.battle;

import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;

/* JADX INFO: loaded from: classes.dex */
public class BlueCoffeeEffect extends EffectActor {
    private Effect.OnCompleteListener listener;

    public BlueCoffeeEffect(String name) {
        super(name);
    }

    public void setOnCompleteListener(Effect.OnCompleteListener listener) {
        this.listener = listener;
    }

    public void play() {
        SoundManager.playSound(SoundManager.SOUND_BLUE_COFFEE);
        Effect effect1 = EffectManager.getInstance().getEffect(EffectType.BLUE_COFFEE_1).getCopy();
        final Effect effect2 = EffectManager.getInstance().getEffect(EffectType.BLUE_COFFEE_2).getCopy();
        effect1.setOnCompleteListener(new Effect.OnCompleteListener() { // from class: com.gaiaonline.monstergalaxy.battle.BlueCoffeeEffect.1
            @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect.OnCompleteListener
            public void onComplete() {
                BlueCoffeeEffect.this.setEffect(effect2);
                effect2.start();
            }
        });
        effect2.setOnCompleteListener(new Effect.OnCompleteListener() { // from class: com.gaiaonline.monstergalaxy.battle.BlueCoffeeEffect.2
            @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect.OnCompleteListener
            public void onComplete() {
                BlueCoffeeEffect.this.setEffect(null);
                if (BlueCoffeeEffect.this.listener != null) {
                    BlueCoffeeEffect.this.listener.onComplete();
                }
            }
        });
        setEffect(effect1);
        effect1.start();
    }
}
