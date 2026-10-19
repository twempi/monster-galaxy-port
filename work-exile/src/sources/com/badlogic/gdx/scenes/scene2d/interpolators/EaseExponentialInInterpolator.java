package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class EaseExponentialInInterpolator implements Interpolator {
    private static final Pool<EaseExponentialInInterpolator> pool = new Pool<EaseExponentialInInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.EaseExponentialInInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public EaseExponentialInInterpolator newObject() {
            return new EaseExponentialInInterpolator();
        }
    };

    public static EaseExponentialInInterpolator $() {
        EaseExponentialInInterpolator inter = pool.obtain();
        return inter;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float input) {
        if (input == 0.0f) {
            return 0.0f;
        }
        return ((float) Math.pow(2.0d, 10.0f * ((input / 1.0f) - 1.0f))) - 0.001f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $();
    }
}
