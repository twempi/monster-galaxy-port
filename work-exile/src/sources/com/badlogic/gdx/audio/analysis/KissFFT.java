package com.badlogic.gdx.audio.analysis;

import com.badlogic.gdx.utils.Disposable;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class KissFFT implements Disposable {
    private final long handle;

    private native long create(int i);

    private native void destroy(long j);

    private native void getImagPart(long j, ShortBuffer shortBuffer);

    private native void getRealPart(long j, ShortBuffer shortBuffer);

    private native void spectrum(long j, ShortBuffer shortBuffer, FloatBuffer floatBuffer);

    public KissFFT(int numSamples) {
        this.handle = create(numSamples);
    }

    public void spectrum(ShortBuffer samples, FloatBuffer spectrum) {
        spectrum(this.handle, samples, spectrum);
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        destroy(this.handle);
    }

    public void getRealPart(ShortBuffer real) {
        getRealPart(this.handle, real);
    }

    public void getImagPart(ShortBuffer imag) {
        getImagPart(this.handle, imag);
    }
}
