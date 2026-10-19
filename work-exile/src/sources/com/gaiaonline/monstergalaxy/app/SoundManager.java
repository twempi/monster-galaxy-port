package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.gaiaonline.monstergalaxy.app.sound.MusicBlock;
import com.gaiaonline.monstergalaxy.app.sound.MusicElement;
import com.gaiaonline.monstergalaxy.app.sound.MusicSequence;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SoundManager {
    public static final String MUSIC_BATLE_WON = "m_b_win.mp3";
    public static final String MUSIC_BATTLE_BACKGROUND_DEFAULT = "m_battle_adventure3.mp3";
    public static final String MUSIC_SHOP_SCREEN = "m_shop.mp3";
    public static final String MUSIC_ZODIAC_BG = "b_z_background.mp3";
    public static final String SOUND_BATLE_LOST = "b_lose";
    public static final String SOUND_BLUE_COFFEE = "ui_coffee";
    public static final String SOUND_CAPTURE_FAIL = "b_seed_capture_fail";
    public static final String SOUND_CAPTURE_SUCCESS = "b_seed_capture_success";
    public static final String SOUND_EXPLODE = "b_ko";
    public static final String SOUND_ISLAND_UNLOCK = "ui_unlock_island";
    public static final String SOUND_LEVELUP = "b_level_up";
    public static final String SOUND_MENU_SLIDE = "ui_menu_slide";
    public static final String SOUND_MOGA_CASH = "ui_shop_moga_cash";
    public static final String SOUND_MOGA_ENTRANCE = "b_moga_entrance";
    public static final String SOUND_SCREEN_CHANGE = "ScreenChange";
    public static final String SOUND_SEED_THROWN = "b_seed_thrown";
    public static final String SOUND_UI_TAP = "ui_tap";
    public static final String SOUND_ZODIAC_TAP = "b_z_tap";
    private static Set<String> loadedMogaTypeSounds;
    private static MusicElement music;
    private static LinkedList<MusicElement> musicStack;
    private static Map<String, Sound> sounds;
    private static String TAG = "SoundManager";
    private static Logger logger = new Logger(TAG);
    private static float soundsVolume = 0.5f;
    private static float musicVolume = 0.5f;

    public static void initialize() {
        if (loadedMogaTypeSounds == null) {
            loadedMogaTypeSounds = new HashSet();
        } else {
            loadedMogaTypeSounds.clear();
        }
        if (sounds == null) {
            sounds = new HashMap();
        } else {
            for (Map.Entry<String, Sound> entry : sounds.entrySet()) {
                entry.getValue().dispose();
            }
            sounds.clear();
        }
        if (musicStack == null) {
            musicStack = new LinkedList<>();
        } else {
            for (MusicElement music2 : musicStack) {
                music2.destroy();
            }
        }
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            playMusic(Constants.worldStingAudio, Constants.worldAmbienceAudio);
        }
        loadSound(SOUND_UI_TAP);
    }

    public static void setMusicVolume(float volume) {
        Gdx.app.log(TAG, "Music Volume: " + volume);
        musicVolume = volume;
        try {
            if (isPlayingMusic()) {
                music.setVolume(volume);
            }
        } catch (NullPointerException e) {
            Gdx.app.log(TAG, "libgdx. Music not yet initialized.");
        }
    }

    public static void setSoundsVolume(float volume) {
        Gdx.app.log(TAG, "Sound Volume: " + volume);
        soundsVolume = volume;
    }

    public static void loadSound(String assetName) {
        if (assetName != null && !assetName.equals("")) {
            Sound sound = sounds.get(assetName);
            if (sound == null) {
                Sound sound2 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/" + assetName + ".mp3"));
                sounds.put(assetName, sound2);
            }
        }
    }

    public static void loadCaptureSounds() {
        loadSound(SOUND_SEED_THROWN);
        loadSound(SOUND_CAPTURE_SUCCESS);
        loadSound(SOUND_CAPTURE_FAIL);
    }

    public static void loadZodiacSounds() {
        loadSound(SOUND_ZODIAC_TAP);
    }

    public static void loadMogaTypeSounds(MogaType type) {
        AttackMove move = type.getBasicAttack();
        if (!Game.testing) {
            if (move != null) {
                loadSound(move.getAttackSound());
                loadSound(move.getHitSound());
                loadedMogaTypeSounds.add(move.getAttackSound());
                loadedMogaTypeSounds.add(move.getHitSound());
            }
            AttackMove move2 = type.getZodiacAttack();
            if (move2 != null) {
                loadSound(type.getZodiacAttack().getAttackSound());
                loadSound(type.getZodiacAttack().getHitSound());
                loadedMogaTypeSounds.add(move2.getAttackSound());
                loadedMogaTypeSounds.add(move2.getHitSound());
            }
        }
    }

    public static void playScreenChangeSound() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            loadSound(SOUND_SCREEN_CHANGE);
            playSound(SOUND_SCREEN_CHANGE);
        }
    }

    public static void playSound(String soundName) {
        if (soundName != null && sounds != null && !soundName.equals("")) {
            Sound sound = sounds.get(soundName);
            if (sound != null) {
                logger.log("Playing sound: " + soundName);
                sound.play(soundsVolume);
                return;
            } else {
                Gdx.app.log(TAG, "Sound Not Found: " + soundName);
                return;
            }
        }
        Gdx.app.log(TAG, "Playing Sound: invalid sound name! ");
    }

    public static void unloadSound(String name) {
        Sound sound = sounds.remove(name);
        if (sound != null) {
            sound.dispose();
        }
    }

    public static void unloadMogaTypeSounds() {
        for (String soundName : loadedMogaTypeSounds) {
            unloadSound(soundName);
        }
        loadedMogaTypeSounds.clear();
    }

    public static void playMusic(String assetName) {
        playMusic(false, null, AssetsDownloader.getMusic(assetName));
    }

    public static void playMusic(FileHandle asset) {
        playMusic(false, null, asset);
    }

    public static void playMusic(FileHandle stingAsset, FileHandle musicAssetName) {
        playMusic(false, stingAsset, musicAssetName);
    }

    public static void playMusic(String stingAssetName, FileHandle musicAssetName) {
        playMusic(false, AssetsDownloader.getMusic(stingAssetName), musicAssetName);
    }

    public static void playMusic(String stingAssetName, String musicAssetName) {
        playMusic(false, AssetsDownloader.getMusic(stingAssetName), AssetsDownloader.getMusic(musicAssetName));
    }

    private static void playMusic(boolean push, FileHandle stingAssetName, FileHandle musicAssetName) {
        if (push) {
            if (isPlayingMusic()) {
                music.stop();
                musicStack.addFirst(music);
            }
        } else if (music != null) {
            music.destroy();
        }
        try {
            if (stingAssetName == null) {
                music = new MusicBlock(musicAssetName, true, musicVolume);
            } else {
                MusicSequence sequence = new MusicSequence(false, musicVolume);
                sequence.addMusic(stingAssetName, false);
                sequence.addMusic(musicAssetName, true);
                music = sequence;
            }
            music.play();
        } catch (Exception e) {
            logger.log("Error: Failed to play music: " + e.getMessage());
            e.printStackTrace();
            music = null;
        }
    }

    public static void pushMusic(String musicAssetName) {
        playMusic(true, null, AssetsDownloader.getMusic(musicAssetName));
    }

    public static void pushMusic(String stingAssetName, String musicAssetName) {
        playMusic(true, AssetsDownloader.getMusic(stingAssetName), AssetsDownloader.getMusic(musicAssetName));
    }

    public static void popMusic() {
        if (isPlayingMusic()) {
            music.stop();
        }
        if (music != null) {
            music.destroy();
            music = null;
        }
        if (!musicStack.isEmpty()) {
            music = musicStack.removeFirst();
            music.play();
        }
    }

    public static void stopPlayingMusic() {
        if (isPlayingMusic()) {
            music.stop();
        }
    }

    public static boolean isPlayingMusic() {
        if (music == null) {
            return false;
        }
        try {
            return music.isPlaying();
        } catch (Exception e) {
            logger.log("Failed to check if music is playing: " + e.getMessage());
            return false;
        }
    }

    public static void stopSound(String soundName) {
        sounds.get(soundName).stop();
    }

    public static void stopPlayingSounds() {
        for (Map.Entry<String, Sound> entry : sounds.entrySet()) {
            entry.getValue().stop();
        }
    }

    public static void update() {
        if (music != null) {
            music.update();
        }
    }
}
