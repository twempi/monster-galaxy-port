package com.badlogic.gdx.backends.android;

import android.media.MediaPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class AndroidMusic implements Music {
    private final AndroidAudio audio;
    private MediaPlayer player;
    private boolean isPrepared = true;
    protected boolean wasPlaying = false;

    AndroidMusic(AndroidAudio audio, MediaPlayer player) {
        this.audio = audio;
        this.player = player;
    }

    @Override // com.badlogic.gdx.audio.Music, com.badlogic.gdx.utils.Disposable
    public void dispose() {
        if (this.player != null) {
            try {
                try {
                    if (this.player.isPlaying()) {
                        this.player.stop();
                    }
                    this.player.release();
                    this.player = null;
                    synchronized (this.audio.musics) {
                        this.audio.musics.remove(this);
                    }
                } catch (Throwable th) {
                    Gdx.app.log("AndroidMusic", "error while disposing AndroidMusic instance, non-fatal");
                    this.player = null;
                    synchronized (this.audio.musics) {
                        this.audio.musics.remove(this);
                    }
                }
            } catch (Throwable th2) {
                this.player = null;
                synchronized (this.audio.musics) {
                    this.audio.musics.remove(this);
                    throw th2;
                }
            }
        }
    }

    @Override // com.badlogic.gdx.audio.Music
    public boolean isLooping() {
        return this.player.isLooping();
    }

    @Override // com.badlogic.gdx.audio.Music
    public boolean isPlaying() {
        return this.player.isPlaying();
    }

    @Override // com.badlogic.gdx.audio.Music
    public void pause() {
        if (this.player.isPlaying()) {
            this.player.pause();
        }
    }

    @Override // com.badlogic.gdx.audio.Music
    public void play() {
        if (!this.player.isPlaying()) {
            try {
                if (!this.isPrepared) {
                    this.player.prepare();
                    this.isPrepared = true;
                }
                this.player.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.badlogic.gdx.audio.Music
    public void setLooping(boolean isLooping) {
        this.player.setLooping(isLooping);
    }

    @Override // com.badlogic.gdx.audio.Music
    public void setVolume(float volume) {
        this.player.setVolume(volume, volume);
    }

    @Override // com.badlogic.gdx.audio.Music
    public void stop() {
        if (this.isPrepared) {
            this.player.seekTo(0);
        }
        this.player.stop();
        this.isPrepared = false;
    }

    @Override // com.badlogic.gdx.audio.Music
    public float getPosition() {
        return this.player.getCurrentPosition() / 1000.0f;
    }
}
