package com.gaiaonline.monstergalaxy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.map.Encounter;
import com.gaiaonline.monstergalaxy.model.map.EncounterAppearance;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AssetsDownloader {
    static final String BASE_DOWNLOAD_DIR = "data/downloaded/";
    private static AssetsDownloader instance;
    private static Map<String, FileHandle> musicFileHandles = new HashMap();
    private boolean allMusicDownloaded;
    private boolean luckyMogaAssetsDownloaded;
    private final DownloaderExecutor threadPool = new DownloaderExecutor();
    private final Map<Asset, Future<Boolean>> downloads = new LinkedHashMap();

    public static AssetsDownloader getInstance() {
        if (instance == null) {
            instance = new AssetsDownloader();
        }
        return instance;
    }

    public static FileHandle getAsset(Asset asset) {
        FileHandle f = Gdx.files.internal(asset.getCompletePath());
        if (f.exists()) {
            return f;
        }
        FileHandle f2 = Gdx.files.external(asset.getCompletePath());
        if (f2.exists()) {
            return f2;
        }
        throw new GdxRuntimeException("Asset not found: " + asset.getCompletePath());
    }

    public void downloadMusic() {
        Thread t = new Thread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.assets.AssetsDownloader.1
            @Override // java.lang.Runnable
            public void run() {
                AssetsDownloader.this.downloadSyncMusic();
            }
        });
        t.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadSyncMusic() {
        if (!this.allMusicDownloaded) {
            Map<String, List<String>> battleMusicByType = Game.getStorage().getBattleAudio();
            Set<String> musics = new HashSet<>();
            for (List<String> list : battleMusicByType.values()) {
                musics.addAll(list);
            }
            for (Island island : Game.getStorage().getAllIslands()) {
                String audio = island.getAmbienceAudio();
                if (audio != null) {
                    musics.add(audio);
                }
            }
            boolean allMusicDownloaded = true;
            Set<Asset> assets = new HashSet<>();
            for (String assetName : musics) {
                if (getMusic(assetName) == null) {
                    allMusicDownloaded = false;
                    assets.add(new Asset("sounds", assetName.substring(0, assetName.lastIndexOf(".")), assetName.substring(assetName.lastIndexOf(".") + 1)));
                }
            }
            if (!allMusicDownloaded) {
                download(assets);
            } else {
                this.allMusicDownloaded = true;
            }
        }
    }

    public void downloadMogaAssetsBySubType(MogaType.SubType subType) {
        if (!areMogaSubTypesAssetsDownloaded(subType)) {
            StorageHelper storage = Game.getStorage();
            downloadMogaAssets(storage.getMogaTypesBySubType(subType));
        }
    }

    private void downloadMogaAssets(List<MogaType> mogaTypes) {
        Set<Asset> mogasAssets = new HashSet<>();
        for (MogaType mogaType : mogaTypes) {
            String mogaAssetName = mogaType.getAssetName();
            String portraitAssetName = mogaType.getPortraitAssetName();
            mogasAssets.add(new Asset("entireMogas", mogaAssetName));
            mogasAssets.add(new Asset("portraitMogas", portraitAssetName));
        }
        download(mogasAssets);
    }

    public boolean areLuckyMogaAssetsDownloaded() {
        if (!this.luckyMogaAssetsDownloaded) {
            this.luckyMogaAssetsDownloaded = areMogaSubTypesAssetsDownloaded(MogaType.SubType.LUCKY);
        }
        return this.luckyMogaAssetsDownloaded;
    }

    public boolean areMogaSubTypesAssetsDownloaded(MogaType.SubType subType) {
        StorageHelper storage = Game.getStorage();
        List<MogaType> mogaTypes = storage.getMogaTypesBySubType(subType);
        for (MogaType mogaType : mogaTypes) {
            if (!exists(new Asset("entireMogas/", mogaType.getAssetName())) || !exists(new Asset("portraitMogas/", mogaType.getPortraitAssetName()))) {
                return false;
            }
        }
        return true;
    }

    public void downloadAssetsForIsland(Island island) {
        Set<Asset> islandPack = getIslandAssets(island);
        download(islandPack);
    }

    public boolean areAssetsDownloaded(Island island) {
        Set<Asset> islandPack = getIslandAssets(island);
        for (Asset a : islandPack) {
            if (!exists(a)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDownloadFinished(Asset asset) {
        Future<Boolean> future = this.downloads.get(asset);
        return future == null || future.isDone();
    }

    public void stopDownloading() {
        this.threadPool.shutdownNow();
    }

    public void pauseDownloading() {
        this.threadPool.pause();
    }

    public void resumeDownloading() {
        this.threadPool.resume();
    }

    public void downloadNow(Asset asset) {
        if (!exists(asset)) {
            Future<Boolean> future = this.threadPool.submit(new DownloadTask(asset, 1));
            this.downloads.put(asset, future);
        }
    }

    private void download(Set<Asset> assets) {
        for (Asset a : assets) {
            if (!exists(a) && !isBeingDownloaded(a)) {
                Future<Boolean> f = this.threadPool.submit(new DownloadTask(a, 2));
                this.downloads.put(a, f);
            }
        }
    }

    private Set<Asset> getIslandAssets(Island island) {
        Set<Asset> islandPack = new HashSet<>();
        islandPack.add(island.getMapAsset());
        for (int qid : island.getQuestIds()) {
            Quest q = Game.getStorage().getQuest(qid);
            islandPack.add(new Asset("storyDialog/npcs", q.getNpcAsset().replace('.', '_')));
        }
        for (Node n : island.getNodes()) {
            islandPack.add(n.getBackgroundAsset());
            islandPack.add(new Asset("island/thumbnails", n.getThumbnailAsset().replace('.', '_')));
            List<Encounter> encounters = Game.getStorage().getEncounters(n.getId());
            for (Encounter encounter : encounters) {
                for (EncounterAppearance ea : encounter.getAppearances()) {
                    MogaType mt = Game.getStorage().getMogaType(ea.getMogaTypeId());
                    islandPack.add(mt.getAsset());
                }
            }
        }
        return islandPack;
    }

    public boolean isBeingDownloaded(Asset asset) {
        Future<Boolean> f = this.downloads.get(asset);
        return (f == null || f.isDone()) ? false : true;
    }

    public static boolean exists(Asset asset) {
        FileHandle f = Gdx.files.internal(asset.getCompletePath());
        if (f.exists()) {
            return true;
        }
        FileHandle f2 = Gdx.files.external(asset.getCompletePath());
        return f2.exists();
    }

    public static FileHandle getMusic(String filename) {
        if (musicFileHandles.get(filename) == null) {
            FileHandle f = Gdx.files.internal("data/sounds/" + filename);
            if (f.exists()) {
                musicFileHandles.put(filename, f);
                return f;
            }
            FileHandle f2 = Gdx.files.internal("data/downloaded/sounds/" + filename);
            if (f2.exists()) {
                musicFileHandles.put(filename, f2);
                return f2;
            }
            FileHandle f3 = Gdx.files.external("data/downloaded/sounds/" + filename);
            if (f3.exists()) {
                musicFileHandles.put(filename, f3);
                return f3;
            }
        }
        return musicFileHandles.get(filename);
    }

    public static boolean exists(String assetFilePath) {
        FileHandle f = Gdx.files.internal(assetFilePath);
        if (f.exists()) {
            return true;
        }
        FileHandle f2 = Gdx.files.external(assetFilePath);
        return f2.exists();
    }

    private AssetsDownloader() {
    }
}
