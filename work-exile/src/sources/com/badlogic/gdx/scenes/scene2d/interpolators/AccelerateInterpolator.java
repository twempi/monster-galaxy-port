package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AccelerateInterpolator implements Interpolator {
    private static final float DEFAULT_FACTOR = 1.0f;
    private static final Pool<AccelerateInterpolator> pool = new Pool<AccelerateInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public AccelerateInterpolator newObject() {
            return new AccelerateInterpolator();
        }
    };
    private double doubledFactor;
    private float factor;

    AccelerateInterpolator() {
    }

    public static AccelerateInterpolator $(float factor) {
        AccelerateInterpolator inter = pool.obtain();
        inter.factor = factor;
        inter.doubledFactor = 2.0f * factor;
        return inter;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float input) {
        return this.factor == DEFAULT_FACTOR ? input * input : (float) Math.pow(input, this.doubledFactor);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $(this.factor);
    }
}
