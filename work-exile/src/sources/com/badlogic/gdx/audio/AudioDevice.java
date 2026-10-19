package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface AudioDevice extends Disposable {
    @Override // com.badlogic.gdx.utils.Disposable
    void dispose();

    int getLatency();

    boolean isMono();

    void writeSamples(float[] fArr, int i, int i2);

    void writeSamples(short[] sArr, int i, int i2);
}
