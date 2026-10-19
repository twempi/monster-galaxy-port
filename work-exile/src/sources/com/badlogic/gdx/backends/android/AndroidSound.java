package com.badlogic.gdx.backends.android;

import android.media.AudioManager;
import android.media.SoundPool;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.IntArray;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class AndroidSound implements Sound {
    final AudioManager manager;
    final int soundId;
    final SoundPool soundPool;
    final IntArray streamIds = new IntArray(8);

    AndroidSound(SoundPool pool, AudioManager manager, int soundId) {
        this.soundPool = pool;
        this.manager = manager;
        this.soundId = soundId;
    }

    @Override // com.badlogic.gdx.audio.Sound, com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.soundPool.unload(this.soundId);
    }

    @Override // com.badlogic.gdx.audio.Sound
    public void play() {
        play(1.0f);
    }

    @Override // com.badlogic.gdx.audio.Sound
    public void play(float volume) {
        if (this.streamIds.size == 8) {
            this.streamIds.pop();
        }
        this.streamIds.add(this.soundPool.play(this.soundId, volume, volume, 1, 0, 1.0f));
    }

    @Override // com.badlogic.gdx.audio.Sound
    public void stop() {
        int n = this.streamIds.size;
        for (int i = 0; i < n; i++) {
            this.soundPool.stop(this.streamIds.get(i));
        }
    }
}
