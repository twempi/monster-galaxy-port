package com.badlogic.gdx.audio.analysis;

import com.badlogic.gdx.utils.Disposable;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class NativeFFT implements Disposable {
    private long handle;

    private native long createFFT(int i, int i2);

    private native void destroyFFT(long j);

    private native void nativeSpectrum(long j, FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    public NativeFFT(int timeSize, int sampleRate) {
        this.handle = createFFT(timeSize, sampleRate);
    }

    public void spectrum(FloatBuffer samples, FloatBuffer spectrum, int numSamples) {
        nativeSpectrum(this.handle, samples, spectrum, numSamples);
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        destroyFFT(this.handle);
    }
}
