package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class EaseExponentialOutInterpolator implements Interpolator {
    private static final Pool<EaseExponentialOutInterpolator> pool = new Pool<EaseExponentialOutInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.EaseExponentialOutInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public EaseExponentialOutInterpolator newObject() {
            return new EaseExponentialOutInterpolator();
        }
    };

    public static EaseExponentialOutInterpolator $() {
        EaseExponentialOutInterpolator inter = pool.obtain();
        return inter;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float input) {
        if (input == 1.0f) {
            return 1.0f;
        }
        return 1.0f + ((float) Math.pow(2.0d, ((-10.0f) * input) / 1.0f));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $();
    }
}
