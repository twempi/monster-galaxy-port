package com.badlogic.gdx.backends.android;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AndroidAudio implements Audio {
    private final AudioManager manager;
    protected final List<AndroidMusic> musics = new ArrayList();
    private SoundPool soundPool = new SoundPool(16, 3, 100);

    public AndroidAudio(Activity context) {
        this.manager = (AudioManager) context.getSystemService("audio");
        context.setVolumeControlStream(3);
    }

    protected void pause() {
        synchronized (this.musics) {
            for (AndroidMusic music : this.musics) {
                if (music.isPlaying()) {
                    music.pause();
                }
            }
        }
    }

    protected void resume() {
        synchronized (this.musics) {
            for (AndroidMusic music : this.musics) {
                if (music.isPaused()) {
                    music.play();
                }
            }
        }
    }

    @Override // com.badlogic.gdx.Audio
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
        return new AndroidAudioDevice(samplingRate, isMono);
    }

    @Override // com.badlogic.gdx.Audio
    public Music newMusic(FileHandle file) {
        AndroidMusic music;
        AndroidFileHandle aHandle = (AndroidFileHandle) file;
        MediaPlayer mediaPlayer = new MediaPlayer();
        if (aHandle.type() == Files.FileType.Internal) {
            try {
                AssetFileDescriptor descriptor = aHandle.assets.openFd(aHandle.path());
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mediaPlayer.prepare();
                music = new AndroidMusic(this, mediaPlayer);
                synchronized (this.musics) {
                    this.musics.add(music);
                }
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error loading audio file: " + file + "\nNote: Internal audio files must be placed in the assets directory.", ex);
            }
        } else {
            try {
                mediaPlayer.setDataSource(aHandle.path());
                mediaPlayer.prepare();
                music = new AndroidMusic(this, mediaPlayer);
                synchronized (this.musics) {
                    this.musics.add(music);
                }
            } catch (Exception ex2) {
                throw new GdxRuntimeException("Error loading audio file: " + file, ex2);
            }
        }
        return music;
    }

    @Override // com.badlogic.gdx.Audio
    public Sound newSound(FileHandle file) {
        AndroidFileHandle aHandle = (AndroidFileHandle) file;
        if (aHandle.type() == Files.FileType.Internal) {
            try {
                AssetFileDescriptor descriptor = aHandle.assets.openFd(aHandle.path());
                AndroidSound sound = new AndroidSound(this.soundPool, this.manager, this.soundPool.load(descriptor, 1));
                descriptor.close();
                return sound;
            } catch (IOException ex) {
                throw new GdxRuntimeException("Error loading audio file: " + file + "\nNote: Internal audio files must be placed in the assets directory.", ex);
            }
        }
        try {
            AndroidSound sound2 = new AndroidSound(this.soundPool, this.manager, this.soundPool.load(aHandle.path(), 1));
            return sound2;
        } catch (Exception ex2) {
            throw new GdxRuntimeException("Error loading audio file: " + file, ex2);
        }
    }

    @Override // com.badlogic.gdx.Audio
    public AudioRecorder newAudioRecoder(int samplingRate, boolean isMono) {
        return new AndroidAudioRecorder(samplingRate, isMono);
    }

    public void dispose() {
        synchronized (this.musics) {
            ArrayList<AndroidMusic> musicsCopy = new ArrayList<>(this.musics);
            for (AndroidMusic music : musicsCopy) {
                music.dispose();
            }
        }
        this.soundPool.release();
    }
}
