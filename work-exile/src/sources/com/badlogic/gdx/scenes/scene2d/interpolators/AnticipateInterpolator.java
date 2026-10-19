package com.badlogic.gdx.scenes.scene2d.interpolators;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.utils.Pool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AnticipateInterpolator implements Interpolator {
    private static final float DEFAULT_TENSION = 2.0f;
    private static final Pool<AnticipateInterpolator> pool = new Pool<AnticipateInterpolator>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.interpolators.AnticipateInterpolator.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public AnticipateInterpolator newObject() {
            return new AnticipateInterpolator();
        }
    };
    private float tension;

    AnticipateInterpolator() {
    }

    public static AnticipateInterpolator $(float tension) {
        AnticipateInterpolator inter = pool.obtain();
        inter.tension = tension;
        return inter;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public void finished() {
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public float getInterpolation(float t) {
        return t * t * (((this.tension + 1.0f) * t) - this.tension);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Interpolator
    public Interpolator copy() {
        return $(this.tension);
    }
}
