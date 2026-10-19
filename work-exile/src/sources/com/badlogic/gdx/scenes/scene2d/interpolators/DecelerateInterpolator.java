package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DecelerateInterpolator implements Interpolator {
    private static final float DEFAULT_FACTOR = 1.0f;
    private static final Pool<DecelerateInterpolator> pool = new Pool<DecelerateInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public DecelerateInterpolator newObject() {
            return new DecelerateInterpolator();
        }
    };
    private double doubledFactor;
    private float factor;

    DecelerateInterpolator() {
    }

    public static DecelerateInterpolator $(float factor) {
        DecelerateInterpolator inter = pool.obtain();
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
        return this.factor == DEFAULT_FACTOR ? DEFAULT_FACTOR - ((DEFAULT_FACTOR - input) * (DEFAULT_FACTOR - input)) : (float) (1.0d - Math.pow(DEFAULT_FACTOR - input, this.doubledFactor));
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $(this.factor);
    }
}
