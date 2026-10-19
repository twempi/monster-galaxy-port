package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.files.FileHandle;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class MusicManager {
    private static Map<String, List<String>> musicByType;
    private static Random random;

    public static void initialize() {
        random = new Random();
        musicByType = Game.getStorage().getBattleAudio();
    }

    public static FileHandle getAvailableRandomMusic(String type) {
        if (type == null) {
            return AssetsDownloader.getMusic(SoundManager.MUSIC_BATTLE_BACKGROUND_DEFAULT);
        }
        List<String> musicList = new ArrayList<>(musicByType.get(type));
        while (musicList.size() > 0) {
            int index = random.nextInt(musicList.size());
            String musicAssetName = musicList.get(index);
            FileHandle music = AssetsDownloader.getMusic(musicAssetName);
            if (music == null) {
                musicList.remove(index);
            } else {
                return music;
            }
        }
        return AssetsDownloader.getMusic(SoundManager.MUSIC_BATTLE_BACKGROUND_DEFAULT);
    }

    public static FileHandle getAvailableIslandMusic(String intendedMusic) {
        FileHandle f = AssetsDownloader.getMusic(intendedMusic);
        if (f == null) {
            return AssetsDownloader.getMusic(Constants.defaultIslandAmbienceAudio);
        }
        return f;
    }

    public static FileHandle getAvailableQuestMusic(String intendedMusic) {
        FileHandle f = AssetsDownloader.getMusic(intendedMusic);
        if (f == null) {
            return AssetsDownloader.getMusic(Constants.defaultQuestAmbienceAudio);
        }
        return f;
    }
}
