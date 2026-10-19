package com.badlogic.gdx.audio.io;

import java.nio.ShortBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Mpg123Decoder implements Decoder {
    public final long handle;

    private native void closeFile(long j);

    private native float getLength(long j);

    private native int getNumChannels(long j);

    private native int getRate(long j);

    private native long openFile(String str);

    private native int readSamples(long j, ShortBuffer shortBuffer, int i);

    private native int skipSamples(long j, int i);

    public Mpg123Decoder(String filename) {
        this.handle = openFile(filename);
        if (this.handle == -1) {
            throw new IllegalArgumentException("couldn't open file");
        }
    }

    @Override // com.badlogic.gdx.audio.io.Decoder
    public int readSamples(ShortBuffer samples) {
        int read = readSamples(this.handle, samples, samples.capacity());
        samples.position(0);
        return read;
    }

    @Override // com.badlogic.gdx.audio.io.Decoder
    public int skipSamples(int numSamples) {
        return skipSamples(this.handle, numSamples);
    }

    @Override // com.badlogic.gdx.audio.io.Decoder
    public int getNumChannels() {
        return getNumChannels(this.handle);
    }

    @Override // com.badlogic.gdx.audio.io.Decoder
    public int getRate() {
        return getRate(this.handle);
    }

    @Override // com.badlogic.gdx.audio.io.Decoder
    public float getLength() {
        return getLength(this.handle);
    }

    @Override // com.badlogic.gdx.audio.io.Decoder, com.badlogic.gdx.utils.Disposable
    public void dispose() {
        closeFile(this.handle);
    }
}
