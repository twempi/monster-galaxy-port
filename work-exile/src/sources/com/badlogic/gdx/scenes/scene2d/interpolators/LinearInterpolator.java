package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LinearInterpolator implements Interpolator {
    private static final Pool<LinearInterpolator> pool = new Pool<LinearInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.LinearInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public LinearInterpolator newObject() {
            return new LinearInterpolator();
        }
    };

    LinearInterpolator() {
    }

    public static LinearInterpolator $() {
        return pool.obtain();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float input) {
        return input;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $();
    }
}
