package com.gaiaonline.monstergalaxy.app.sound;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.gaiaonline.monstergalaxy.app.Game;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class MusicBlock implements MusicElement {
    private static Logger logger = new Logger("MusicBlock");
    private FileHandle asset;
    private boolean loop;
    private Music music;
    private float volume;

    public MusicBlock(FileHandle asset, boolean loop, float volume) {
        this.asset = asset;
        this.volume = volume;
        this.loop = loop;
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void play() {
        try {
            if (this.asset.type() == Files.FileType.Internal) {
                logger.info("Playing internal storage audio: " + this.asset.toString());
                this.music = Gdx.audio.newMusic(this.asset);
            } else {
                String path = new File(Game.getPlatformHelper().getExternalStoragePath(), this.asset.path()).getAbsolutePath();
                logger.info("Playing external storage audio: " + path);
                this.music = Gdx.audio.newMusic(Gdx.files.external(path));
            }
            this.music.setLooping(this.loop);
            this.music.setVolume(this.volume);
            this.music.play();
        } catch (Exception e) {
            logger.info("Error: Failed to play music: " + e.getMessage());
            e.printStackTrace();
            this.music = null;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void stop() {
        if (this.music != null) {
            try {
                this.music.stop();
            } catch (Exception e) {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public boolean isPlaying() {
        if (this.music != null) {
            try {
                return this.music.isPlaying();
            } catch (Exception e) {
                logger.error("Exception: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
        }
        return false;
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void setVolume(float volume) {
        this.volume = volume;
        if (this.music != null) {
            try {
                this.music.setVolume(volume);
            } catch (Exception e) {
                logger.error("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void update() {
    }

    @Override // com.gaiaonline.monstergalaxy.app.sound.MusicElement
    public void destroy() {
        if (this.music != null) {
            this.music = null;
        }
    }
}
