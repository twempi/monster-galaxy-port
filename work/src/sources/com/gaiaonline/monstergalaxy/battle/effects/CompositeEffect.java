package com.gaiaonline.monstergalaxy.battle.effects;

/* JADX INFO: loaded from: classes.dex */
public class CompositeEffect extends Effect {
    public CompositeEffect(String name, Effect... effects) {
        super(name);
        for (Effect ef : effects) {
            for (CCParticleEmitter em : ef.particleEffect.getEmitters()) {
                addEmitter(em);
            }
        }
    }
}
