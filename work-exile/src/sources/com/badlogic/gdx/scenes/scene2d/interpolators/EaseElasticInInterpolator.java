package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class EaseElasticInInterpolator implements Interpolator {
    private static final Pool<EaseElasticInInterpolator> pool = new Pool<EaseElasticInInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.EaseElasticInInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public EaseElasticInInterpolator newObject() {
            return new EaseElasticInInterpolator();
        }
    };
    private float period;

    public static EaseElasticInInterpolator $(float period) {
        EaseElasticInInterpolator inter = pool.obtain();
        inter.period = period;
        return inter;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float input) {
        if (input == 0.0f || input == 1.0f) {
            return input;
        }
        float s = this.period / 4.0f;
        float localInput = input - 1.0f;
        return (float) ((-Math.pow(2.0d, 10.0f * localInput)) * Math.sin(((((double) (localInput - s)) * 3.141592653589793d) * 2.0d) / ((double) this.period)));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $(this.period);
    }
}
