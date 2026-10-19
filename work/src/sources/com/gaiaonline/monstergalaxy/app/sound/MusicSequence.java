package com.gaiaonline.monstergalaxy.app.sound;

import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MusicSequence implements MusicElement {
    private boolean loop;
    private boolean sequencePlaying;
    private float volume;
    private List<MusicElement> musicQueue = new ArrayList();
    private int index = 0;

    public MusicSequence(boolean loop, float volume) {
        this.loop = loop;
        this.volume = volume;
    }

    public void addMusic(FileHandle asset, boolean loop) {
        this.musicQueue.add(new MusicBlock(asset, loop, this.volume));
    }

    public void addMusic(MusicBlock music) {
        this.musicQueue.add(music);
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void play() {
        if (this.musicQueue.size() > this.index) {
            this.musicQueue.get(this.index).play();
            this.sequencePlaying = true;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void stop() {
        if (this.musicQueue.size() > this.index) {
            this.musicQueue.get(this.index).stop();
        }
        this.sequencePlaying = false;
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public boolean isPlaying() {
        return this.sequencePlaying;
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void setVolume(float volume) {
        this.volume = volume;
        for (MusicElement music : this.musicQueue) {
            music.setVolume(volume);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void update() {
        if (this.sequencePlaying && this.musicQueue.size() > this.index && !this.musicQueue.get(this.index).isPlaying()) {
            this.index++;
            if (this.musicQueue.size() > this.index) {
                this.musicQueue.get(this.index).play();
            } else if (this.loop && this.musicQueue.size() > 0) {
                this.index = 0;
                this.musicQueue.get(this.index).play();
            } else {
                this.sequencePlaying = false;
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void destroy() {
        for (MusicElement music : this.musicQueue) {
            music.destroy();
        }
    }
}
