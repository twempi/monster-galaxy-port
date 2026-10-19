package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class OvershootInterpolator implements Interpolator {
    private static final float DEFAULT_FACTOR = 1.0f;
    private static final Pool<OvershootInterpolator> pool = new Pool<OvershootInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.OvershootInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public OvershootInterpolator newObject() {
            return new OvershootInterpolator();
        }
    };
    private double doubledFactor;
    private float factor;

    OvershootInterpolator() {
    }

    public static OvershootInterpolator $(float factor) {
        OvershootInterpolator inter = pool.obtain();
        inter.factor = factor;
        inter.doubledFactor = 2.0f * factor;
        return inter;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float t) {
        float t2 = t - DEFAULT_FACTOR;
        return (t2 * t2 * (((this.factor + DEFAULT_FACTOR) * t2) + this.factor)) + DEFAULT_FACTOR;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $(this.factor);
    }
}
