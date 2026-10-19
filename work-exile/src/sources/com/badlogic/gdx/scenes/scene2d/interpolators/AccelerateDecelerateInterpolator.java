package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AccelerateDecelerateInterpolator implements Interpolator {
    private static final float DEFAULT_FACTOR = 1.0f;
    private static final Pool<AccelerateDecelerateInterpolator> pool = new Pool<AccelerateDecelerateInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateDecelerateInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public AccelerateDecelerateInterpolator newObject() {
            return new AccelerateDecelerateInterpolator();
        }
    };
    private double doubledFactor;
    private float factor;

    AccelerateDecelerateInterpolator() {
    }

    public static AccelerateDecelerateInterpolator $(float factor) {
        AccelerateDecelerateInterpolator inter = pool.obtain();
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
        return ((float) (Math.cos(((double) (DEFAULT_FACTOR + input)) * 3.141592653589793d) / 2.0d)) + 0.5f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $(this.factor);
    }
}
