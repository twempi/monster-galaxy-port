package com.badlogic.gdx.audio.io;

import com.badlogic.gdx.utils.Disposable;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Decoder extends Disposable {
    @Override // com.badlogic.gdx.utils.Disposable
    void dispose();

    float getLength();

    int getNumChannels();

    int getRate();

    int readSamples(ShortBuffer shortBuffer);

    int skipSamples(int i);
}
