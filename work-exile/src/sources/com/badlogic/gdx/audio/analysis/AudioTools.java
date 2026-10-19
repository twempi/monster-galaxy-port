package com.badlogic.gdx.audio.analysis;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AudioTools {
    public static native void convertToFloat(ShortBuffer shortBuffer, FloatBuffer floatBuffer, int i);

    public static native void convertToMono(FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    public static native void convertToMono(ShortBuffer shortBuffer, ShortBuffer shortBuffer2, int i);

    public static native void convertToShort(FloatBuffer floatBuffer, ShortBuffer shortBuffer, int i);

    public static native float spectralFlux(FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    public static FloatBuffer allocateFloatBuffer(int numSamples, int numChannels) {
        ByteBuffer b = ByteBuffer.allocateDirect(numSamples * numChannels * 4);
        b.order(ByteOrder.nativeOrder());
        return b.asFloatBuffer();
    }

    public static ShortBuffer allocateShortBuffer(int numSamples, int numChannels) {
        ByteBuffer b = ByteBuffer.allocateDirect(numSamples * numChannels * 2);
        b.order(ByteOrder.nativeOrder());
        return b.asShortBuffer();
    }
}
