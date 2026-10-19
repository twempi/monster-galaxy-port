package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;
import com.gaiaonline.monstergalaxy.battle.anim.AnimationManager;
import com.gaiaonline.monstergalaxy.model.battle.Battle;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.quest.QuestProgress;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.service.AbstractGaiaClient;
import com.gaiaonline.monstergalaxy.service.AdsService;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public abstract class Game implements ApplicationListener {
    public static final String SALT_SK1 = "s89-dsg0*F&G0ksgd9yF9mfgew-9";
    public static final String SALT_SK2 = "8vUo-Phi_g!ts7GNF.Soayu4";
    private static final String SAVE_DATA_FILE_NAME = "monsterGalaxy";
    public static final String TAG = "MonsterGalaxy";
    private static com.gaiaonline.monstergalaxy.analytics.Analytics analytics;
    protected static AbstractGaiaClient gaiaClient;
    private static Thread gameThread;
    private static ConcurrentLinkedQueue<Runnable> gameThreadTaskQueue;
    protected static boolean newVersionAvailable;
    private static PlatformHelper platformHelper;
    protected static String storageFolder;
    private static Trainer trainer;
    protected Screen nextScreen;
    protected Screen screen;
    private ScreenTransition transition;
    private Object transitionLock = new Object();
    public static boolean testing = false;
    private static boolean fetchServerTimestamp = true;

    public abstract Screen getStartScreen();

    public Game(String storageFolder2, PlatformHelper platformHelper2, AbstractGaiaClient gaiaClient2, com.gaiaonline.monstergalaxy.analytics.Analytics analytics2) {
        gameThreadTaskQueue = new ConcurrentLinkedQueue<>();
        storageFolder = storageFolder2;
        gaiaClient = gaiaClient2;
        platformHelper = platformHelper2;
        analytics = analytics2;
        Constants.load();
        setupTrainer(loadTrainer());
    }

    public void setPlatformHelper(PlatformHelper platformHelper2) {
        platformHelper = platformHelper2;
    }

    private void setupTrainer(Trainer newTrainer) {
        trainer = newTrainer;
        if (trainer == null) {
            trainer = new Trainer(null);
            trainer.addCoins(7);
            trainer.addStarSeeds(3);
            trainer.rechargeEnergy();
        }
        if (trainer.getCurrentIsland() == null) {
            trainer.setCurrentIsland(getFirstIsland());
        } else {
            refreshSavedData();
        }
        if (trainer.sentDeviceUniqueId == null) {
            trainer.sentDeviceUniqueId = false;
        }
    }

    public static void initialize() {
        Gdx.app.setLogLevel(3);
        Gdx.app.debug(TAG, "initialize()");
        Gdx.app.debug(TAG, "initialize(): SoundManager.initialize()");
        SoundManager.initialize();
        Gdx.app.debug(TAG, "initialize(): MusicManager.initialize();");
        MusicManager.initialize();
        Gdx.app.debug(TAG, "initialize(): Assets.load();");
        Assets.load();
        Gdx.app.debug(TAG, "initialize(): Fonts.initialize();");
        Fonts.initialize();
        Gdx.app.debug(TAG, "initialize(): AnimationManager.initialize();");
        AnimationManager.initialize();
        Gdx.app.debug(TAG, "initialize(): ZodiacHelper.getInstance().initialize();");
        ZodiacHelper.getInstance().initialize();
        Gdx.app.debug(TAG, "initialize(): SettingsManager.initialize();");
        SettingsManager.initialize();
        Gdx.app.debug(TAG, "initialize(): Clock.initialize();");
        Clock.initialize();
        Gdx.app.debug(TAG, "initialize(): AssetsDownloader.getInstance().downloadMusic();");
        AssetsDownloader.getInstance().downloadMusic();
        Gdx.app.debug(TAG, "initialize(): DONE");
    }

    public static boolean areTrainerAndMogaReady() {
        return (getTrainer() == null || getTrainer().getName() == null || getTrainer().getMogas() == null || getTrainer().getMogas().size() <= 0 || getTrainer().getMogas().get(0).getName() == null || getTrainer().getMogas().get(0).getName().length() <= 0) ? false : true;
    }

    public static PlatformHelper getPlatformHelper() {
        return platformHelper;
    }

    public static AndroidPlatformHelper getAndroidPlatformHelper() {
        return (AndroidPlatformHelper) platformHelper;
    }

    public static AbstractGaiaClient getGaiaClient() {
        return gaiaClient;
    }

    public static com.gaiaonline.monstergalaxy.analytics.Analytics getAnalytics() {
        return analytics;
    }

    public static void menuKeyPressed() {
        runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.Game.1
            @Override // java.lang.Runnable
            public void run() {
                if (MonsterGalaxy.getInstance().screen != null) {
                    MonsterGalaxy.getInstance().screen.onMenuKeyPressed();
                }
            }
        });
    }

    public static void backKeyPressed() {
        if (getPlatformHelper() != null && getPlatformHelper().getAdsService() != null && getPlatformHelper().getAdsService().isShowingInterstitial()) {
            getPlatformHelper().getAdsService().dismissInterstitial();
        } else {
            runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.Game.2
                @Override // java.lang.Runnable
                public void run() {
                    if (MonsterGalaxy.getInstance().screen != null) {
                        MonsterGalaxy.getInstance().screen.onBackKeyPressed();
                    }
                }
            });
        }
    }

    private void refreshSavedData() {
        StorageHelper storage = getStorage();
        trainer.setCurrentIsland(storage.getIsland(trainer.getCurrentIsland().getId()));
        for (QuestProgress questProgress : trainer.getQuestsProgressions()) {
            Quest quest = questProgress.getQuest();
            questProgress.setQuest(storage.getQuest(quest.getId()));
        }
        for (Moga moga : trainer.getMogas()) {
            moga.setType(storage.getMogaType(moga.getType().getId()));
        }
    }

    public void setScreen(Screen nextScreen) {
        this.nextScreen = nextScreen;
    }

    public void setTransition(ScreenTransition aTransition) {
        synchronized (this.transitionLock) {
            this.transition = aTransition;
        }
    }

    protected Battle createBattle() {
        Battle currentBattle = new Battle();
        currentBattle.setComputerMoga(trainer.getCurrentNode().getTeam().getFirstMoga());
        return currentBattle;
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void create() {
        this.screen = getStartScreen();
        resumeScreen();
    }

    public static synchronized void fetchServerTimestamp() {
        if (fetchServerTimestamp) {
            try {
                Clock.setServerTime(getGaiaClient().fetchTimestamp());
            } catch (Exception e) {
                Gdx.app.log(TAG, "Failure while trying to connect to gaia", e);
            }
            fetchServerTimestamp = false;
        } else {
            fetchServerTimestamp = false;
        }
        throw th;
    }

    public static void fetchServerTimestampAsync() {
        Thread t = new Thread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.Game.3
            @Override // java.lang.Runnable
            public void run() {
                Game.fetchServerTimestamp();
            }
        });
        t.start();
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void resume() {
        Clock.saveLocalClockDelta();
        MonsterGalaxy.startGaiaSession();
        fetchServerTimestampAsync();
        resumeScreen();
        if (this.nextScreen == null && getPlatformHelper().shouldShowAds() && !getPlatformHelper().getAdsService().isShowingInterstitial() && ((this.screen.getCode() == ScreenCode.ISLAND || this.screen.getCode() == ScreenCode.WORLD) && trainer != null && trainer.getMogas().size() > 0)) {
            getPlatformHelper().getAdsService().showInterstitial(AdsService.Location.APP_START, null);
        }
        if (MonsterGalaxy.isGameInitialized()) {
            AssetsDownloader.getInstance().downloadMusic();
        }
    }

    private void resumeScreen() {
        this.screen.applicationResume();
        this.screen.resume();
        if (getPlatformHelper().isAndroid()) {
            getAndroidPlatformHelper().hideNativeProgressDialog();
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (this.screen == null || this.screen.getCode() != ScreenCode.SPLASH) {
            Gdx.gl.glClear(16384);
        }
        processGameThreadQueue();
        SoundManager.update();
        if (this.transition == null || this.nextScreen != null) {
            this.screen.update(deltaTime);
            this.screen.present(deltaTime);
        } else {
            this.screen.update(deltaTime);
            synchronized (this.transitionLock) {
                if (this.transition != null) {
                    this.transition.update(deltaTime);
                    this.screen.present(deltaTime, this.transition);
                    if (this.transition.isFinished()) {
                        this.transition = null;
                    }
                } else {
                    this.screen.present(deltaTime);
                }
            }
        }
        if (this.nextScreen != null) {
            this.screen.pause();
            this.screen.exit(this.nextScreen);
            boolean z = this.screen.dispose;
            Screen prevScreen = this.screen;
            this.screen = this.nextScreen;
            this.nextScreen = null;
            System.gc();
            this.screen.enter(prevScreen);
            this.screen.resume();
            synchronized (this.transitionLock) {
                if (this.transition != null) {
                    this.transition.captureScreen();
                }
            }
        }
    }

    private void processGameThreadQueue() {
        if (gameThread == null) {
            gameThread = Thread.currentThread();
        }
        while (true) {
            Runnable task = gameThreadTaskQueue.poll();
            if (task != null) {
                task.run();
            } else {
                return;
            }
        }
    }

    public static void runOnGameThread(Runnable task) {
        if (Thread.currentThread() == gameThread) {
            task.run();
        } else {
            scheduleToGameThread(task);
        }
    }

    public static void scheduleToGameThread(Runnable task) {
        gameThreadTaskQueue.add(task);
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void resize(int width, int height) {
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void pause() {
        Clock.saveLocalClockDelta();
        this.screen.pause();
        this.screen.applicationPause();
        synchronized (this.transitionLock) {
            if (this.transition != null) {
                this.transition = null;
            }
        }
        save();
        AssetsCache.clearTextures();
        fetchServerTimestamp = true;
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void dispose() {
    }

    public static Trainer getTrainer() {
        return trainer;
    }

    public static StorageHelper getStorage() {
        return platformHelper.getStorageHelper();
    }

    protected void internalLoadAndSetTrainer(File file) {
        setupTrainer(loadTrainer(file));
    }

    private Trainer loadTrainer() {
        return loadTrainer(new File(String.valueOf(storageFolder) + "/monsterGalaxy"));
    }

    private Trainer loadTrainer(File file) {
        Trainer trainer2 = null;
        if (storageFolder != null && file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                try {
                    ObjectInputStream in = new ObjectInputStream(fis);
                    try {
                        trainer2 = (Trainer) in.readObject();
                        in.close();
                    } catch (IOException e) {
                        ex = e;
                        ex.printStackTrace();
                    } catch (ClassNotFoundException e2) {
                        ex = e2;
                        ex.printStackTrace();
                    }
                } catch (IOException e3) {
                    ex = e3;
                } catch (ClassNotFoundException e4) {
                    ex = e4;
                }
            } catch (IOException e5) {
                ex = e5;
            } catch (ClassNotFoundException e6) {
                ex = e6;
            }
        }
        return trainer2;
    }

    public static boolean save() {
        return save(new File(String.valueOf(storageFolder) + "/monsterGalaxy"));
    }

    public static boolean save(File file) {
        boolean zRenameTo = false;
        if (storageFolder != null) {
            try {
                File tmpFile = new File(String.valueOf(file.getAbsolutePath()) + ".tmp");
                FileOutputStream fos = new FileOutputStream(tmpFile);
                try {
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    try {
                        out.writeObject(trainer);
                        out.flush();
                        out.close();
                        zRenameTo = tmpFile.renameTo(file);
                    } catch (IOException e) {
                        ex = e;
                        ex.printStackTrace();
                    }
                } catch (IOException e2) {
                    ex = e2;
                }
            } catch (IOException e3) {
                ex = e3;
            }
        }
        return zRenameTo;
    }

    private static void buildRandomPlayerTeam() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int player = (random.nextInt(Opcodes.L2I) + 1) * 2;
            MogaType mogaType = getStorage().getMogaType(player);
            Moga moga = Moga.newMogaFromType(mogaType);
            trainer.addMoga(moga);
            trainer.getTeam().addMoga(moga);
        }
    }

    private static Island getFirstIsland() {
        return getStorage().getIsland(2);
    }

    public static void quit() {
        getPlatformHelper().quit();
    }

    public static boolean isNewVersionAvailable() {
        return newVersionAvailable;
    }

    public static void setNewVersionAvailable(boolean value) {
        newVersionAvailable = value;
    }
}
