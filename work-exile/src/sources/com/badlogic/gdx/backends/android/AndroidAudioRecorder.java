package com.badlogic.gdx.backends.android;

import android.media.AudioRecord;
import com.badlogic.gdx.audio.AudioRecorder;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidAudioRecorder implements AudioRecorder {
    private AudioRecord recorder;

    public AndroidAudioRecorder(int samplingRate, boolean isMono) {
        int minBufferSize = AudioRecord.getMinBufferSize(samplingRate, isMono ? 2 : 3, 2);
        this.recorder = new AudioRecord(1, samplingRate, isMono ? 2 : 3, 2, minBufferSize);
        this.recorder.startRecording();
    }

    @Override // com.badlogic.gdx.audio.AudioRecorder, com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.recorder.stop();
        this.recorder.release();
    }

    @Override // com.badlogic.gdx.audio.AudioRecorder
    public void read(short[] samples, int offset, int numSamples) {
        int read = 0;
        while (read != numSamples) {
            read += this.recorder.read(samples, offset + read, numSamples - read);
        }
    }
}
