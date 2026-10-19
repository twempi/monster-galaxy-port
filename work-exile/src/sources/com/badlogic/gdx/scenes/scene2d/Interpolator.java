package com.badlogic.gdx.scenes.scene2d;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Interpolator {
    Interpolator copy();

    void finished();

    float getInterpolation(float f);
}
