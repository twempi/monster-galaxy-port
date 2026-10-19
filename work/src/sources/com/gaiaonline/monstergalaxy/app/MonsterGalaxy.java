package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;
import com.gaiaonline.monstergalaxy.battle.BattleScreen;
import com.gaiaonline.monstergalaxy.map.WorldScreen;
import com.gaiaonline.monstergalaxy.model.battle.Battle;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachineReel;
import com.gaiaonline.monstergalaxy.model.minigame.Slotmachine;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.service.AbstractGaiaClient;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import com.gaiaonline.monstergalaxy.slotmachine.SlotMachineScreen;
import com.gaiaonline.monstergalaxy.splash.SplashScreen;
import com.gaiaonline.monstergalaxy.startup.SendInstallInfoRetryScreen;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class MonsterGalaxy extends Game {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode;
    private static String currentDate;
    private static WeakReference<MonsterGalaxy> instance;
    private static boolean isInitialized;
    private static ScreenCode pastScreenCode;
    private static ScreenCode startScreenCode;
    private static LinkedList<ScreenHistoryEntry> screenStack = new LinkedList<>();
    private static EnumSet<ScreenCode> tweakMenuEnabledScreens = EnumSet.of(ScreenCode.ISLAND, ScreenCode.WORLD);
    private static Object sessionLock = new Object();
    private static Logger log = new Logger(Game.TAG);

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode;
        if (iArr == null) {
            iArr = new int[ScreenCode.valuesCustom().length];
            try {
                iArr[ScreenCode.CHOOSE_MOGA.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ScreenCode.CHOOSE_MOGA_NAME.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ScreenCode.CHOOSE_TAMER.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ScreenCode.CHOOSE_TAMER_NAME.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ScreenCode.ISLAND.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ScreenCode.QUEST.ordinal()] = 9;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ScreenCode.SETTINGS.ordinal()] = 10;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ScreenCode.SHARE.ordinal()] = 11;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[ScreenCode.SHOP.ordinal()] = 12;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[ScreenCode.SLOTMACHINE.ordinal()] = 13;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[ScreenCode.SLOTMACHINE_RESULTS.ordinal()] = 14;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[ScreenCode.SPLASH.ordinal()] = 15;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[ScreenCode.TAMER.ordinal()] = 8;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[ScreenCode.TEAM.ordinal()] = 6;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[ScreenCode.WORLD.ordinal()] = 7;
            } catch (NoSuchFieldError e15) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode = iArr;
        }
        return iArr;
    }

    public MonsterGalaxy(String storageFolder, PlatformHelper platformHelper, AbstractGaiaClient gaiaClient, com.gaiaonline.monstergalaxy.analytics.Analytics analytics) {
        super(storageFolder, platformHelper, gaiaClient, analytics);
        if (instance != null && instance.get() != null) {
            throw new RuntimeException("Multiple instances of MonsterGalaxy are not supported");
        }
        instance = new WeakReference<>(this);
    }

    public static void updateMiniGameShownDate() {
        SettingsManager.setValue("dailySlotMachine", currentDate);
    }

    public static boolean isMiniGameAvailable() {
        if (!SlotMachineScreen.available) {
            return false;
        }
        if (getPlatformHelper().isAndroid() && getPlatformHelper().getAdsService().isShowingInterstitial()) {
            return false;
        }
        String dailySlotMachine = SettingsManager.getString("dailySlotMachine");
        Slotmachine slotMachine = Game.getStorage().getSlotMachine();
        SlotMachineReel bestPrizes = slotMachine.getReels().get(0);
        boolean isAvailable = !dailySlotMachine.equals(currentDate) && bestPrizes.getPrizes().size() > 0 && Game.getTrainer().getCompletedNodesIds().contains(14);
        if (isAvailable) {
            updateMiniGameShownDate();
            return isAvailable;
        }
        return isAvailable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkDownloadedMogaAssets() {
        SlotMachineScreen.available = AssetsDownloader.getInstance().areLuckyMogaAssetsDownloaded();
        Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxy.1
            @Override // java.lang.Runnable
            public void run() {
                Screen currentScreen = MonsterGalaxy.getInstance().screen;
                boolean showMiniGame = currentScreen != null && (currentScreen.getCode() == ScreenCode.WORLD || currentScreen.getCode() == ScreenCode.ISLAND);
                if (showMiniGame && MonsterGalaxy.isMiniGameAvailable()) {
                    MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE, false, true);
                }
            }
        });
        AssetsDownloader.getInstance().downloadMogaAssetsBySubType(MogaType.SubType.LUCKY);
        AssetsDownloader.getInstance().downloadMogaAssetsBySubType(MogaType.SubType.NORMAL);
    }

    public static void startGaiaSession() {
        Gdx.app.debug(Game.TAG, "startGaiaSession()");
        getGaiaClient().loadPassportIdAndGuestPass();
        if (getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            synchronized (getTrainer().sentDeviceUniqueId) {
                synchronized (sessionLock) {
                    if (sendInstallInfoIfUnsent() && !getGaiaClient().isSessionStarted()) {
                        Thread t = new Thread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxy.2
                            @Override // java.lang.Runnable
                            public void run() {
                                MonsterGalaxy.startSessionIfNotStarted();
                            }
                        }, "StartSession");
                        t.start();
                    }
                }
            }
            return;
        }
        Gdx.app.debug(Game.TAG, "startGaiaSession() #2");
        synchronized (getTrainer().sentDeviceUniqueId) {
            Gdx.app.debug(Game.TAG, "startGaiaSession() #3");
            if (!getTrainer().sentDeviceUniqueId.booleanValue()) {
                Gdx.app.debug(Game.TAG, "startGaiaSession() #4");
                Thread t2 = new Thread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxy.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Gdx.app.debug(Game.TAG, "startGaiaSession(): run() {sendInstallInfoIfUnsent()}");
                        MonsterGalaxy.sendInstallInfoIfUnsent();
                    }
                }, "SendInstallInfo");
                t2.start();
            }
        }
    }

    public static boolean sendInstallInfoIfUnsent() {
        Gdx.app.debug(Game.TAG, "sendInstallInfoIfUnsent()");
        int maxTries = getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2 ? 3 : 1;
        int tries = 0;
        while (tries < maxTries) {
            tries++;
            Gdx.app.debug(Game.TAG, "sendInstallInfoIfUnsent() try #" + tries);
            if (tries > 1) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (getTrainer().sentDeviceUniqueId) {
                if (getTrainer().sentDeviceUniqueId.booleanValue()) {
                    log.info("Install info already sent to server");
                    return true;
                }
                log.info("Sending install info attempt " + tries + " of " + maxTries);
                if (getGaiaClient().sendInstallInfo()) {
                    log.info("Install info successfuly sent to server");
                    getTrainer().sentDeviceUniqueId = true;
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean startSessionIfNotStarted() {
        synchronized (sessionLock) {
            log.info("Starting Gaia session");
            if (getGaiaClient().startGaiaSession()) {
                log.info("Gaia session started");
                return true;
            }
            log.info("Failed to start Gaia session");
            return false;
        }
    }

    public static synchronized void gameInitialized() {
        boolean installInfoSent;
        Gdx.app.debug(Game.TAG, "Game initialized");
        isInitialized = true;
        if (getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            Gdx.app.debug(Game.TAG, "Starting Gaia Session (SK2)");
            startGaiaSession();
            synchronized (getTrainer().sentDeviceUniqueId) {
                installInfoSent = getTrainer().sentDeviceUniqueId.booleanValue();
                Gdx.app.debug(Game.TAG, "Install info sent: " + installInfoSent);
                if (!installInfoSent) {
                    Gdx.app.debug(Game.TAG, "Redirecting to send install info retry screen");
                    showScreen(new SendInstallInfoRetryScreen());
                }
            }
            if (installInfoSent) {
                Gdx.app.debug(Game.TAG, "Install info was sent");
                gaiaSessionStarted();
            }
        } else {
            Gdx.app.debug(Game.TAG, "Starting Gaia Session (SK1)");
            startGaiaSession();
            gaiaSessionStarted();
        }
    }

    public static void gaiaSessionStarted() {
        Gdx.app.debug(Game.TAG, "gaiaSessionStarted()");
        Gdx.app.debug(Game.TAG, "gaiaSessionStarted(): fetchServerTimestamp()");
        fetchServerTimestamp();
        Calendar calendar = Calendar.getInstance();
        currentDate = String.valueOf(String.valueOf(calendar.get(1))) + String.valueOf(calendar.get(2) + 1) + String.valueOf(calendar.get(5));
        Gdx.app.debug(Game.TAG, "gaiaSessionStarted(): showNextScreen()");
        showNextScreen();
        Gdx.app.debug(Game.TAG, "gaiaSessionStarted(): gaiaClient.verifyReimbursement()");
        gaiaClient.verifyReimbursement();
        Gdx.app.debug(Game.TAG, "gaiaSessionStarted(): startBackgroundTasks()");
        startBackgroundTasks();
        if (getPlatformHelper().isAndroid()) {
            Gdx.app.debug(Game.TAG, "gaiaSessionStarted(): doCacheInterstitials()");
            getPlatformHelper().getAdsService().doCacheInterstitials();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.gaiaonline.monstergalaxy.app.MonsterGalaxy$1BackgroundTask] */
    private static void startBackgroundTasks() {
        new Thread() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxy.1BackgroundTask
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                MonsterGalaxy.verifyNewVersionAvailability();
                MonsterGalaxy.checkDownloadedMogaAssets();
            }
        }.start();
    }

    public static void showNextScreen() {
        showScreen(getNextScreen());
    }

    public static ScreenCode getNextScreen() {
        Trainer trainer = Game.getTrainer();
        if (trainer.getGender() == null) {
            return ScreenCode.CHOOSE_TAMER;
        }
        if (trainer.getName() == null) {
            return ScreenCode.CHOOSE_TAMER_NAME;
        }
        if (trainer.getMogas().size() == 0) {
            return ScreenCode.CHOOSE_MOGA;
        }
        if (trainer.getMogas().size() == 1 && trainer.getMogas().get(0).getName() == null) {
            return ScreenCode.CHOOSE_MOGA_NAME;
        }
        if (startScreenCode != null) {
            return startScreenCode;
        }
        if (isMiniGameAvailable()) {
            return ScreenCode.SLOTMACHINE;
        }
        if (trainer.getCurrentIsland().getId() == 2) {
            return ScreenCode.ISLAND;
        }
        return ScreenCode.WORLD;
    }

    public static synchronized boolean isGameInitialized() {
        return isInitialized;
    }

    public static void showBattleScreen() {
        AssetsDownloader.getInstance().pauseDownloading();
        Battle battle = getInstance().createBattle();
        BattleScreen bs = new BattleScreen(battle);
        showScreen(bs);
    }

    public static Battle staticCreateBattle() {
        return getInstance().createBattle();
    }

    public static void showScreen(ScreenCode screenCode, boolean replaceCurrent, boolean destroyCurrent) {
        showScreen(ScreenFactory.createScreen(screenCode), replaceCurrent, destroyCurrent);
    }

    public static void showScreen(Screen newScreen, boolean replaceCurrent, boolean destroyCurrent) {
        pastScreenCode = getInstance().screen.getCode();
        getInstance().setTransition(getTransition(getInstance().screen, newScreen));
        if (!replaceCurrent) {
            if (destroyCurrent) {
                screenStack.add(new ScreenHistoryEntry(getInstance().screen.getCode()));
            } else {
                screenStack.add(new ScreenHistoryEntry(getInstance().screen));
            }
        }
        getInstance().screen.dispose = destroyCurrent;
        getInstance().setScreen(newScreen);
    }

    public static ScreenCode getPastScreen() {
        return pastScreenCode;
    }

    private static ScreenTransition getTransition(Screen fromScreen, Screen toScreen) {
        ScreenCode fromScreenCode = fromScreen == null ? null : fromScreen.getCode();
        ScreenCode toScreenCode = toScreen == null ? null : toScreen.getCode();
        if (toScreenCode == null) {
            return null;
        }
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode()[toScreenCode.ordinal()]) {
            case 13:
            case 14:
                return new ScreenTransition(0);
            default:
                if (fromScreenCode != null) {
                    switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode()[fromScreenCode.ordinal()]) {
                        case 13:
                        case 14:
                            return new ScreenTransition(1);
                        default:
                            if (fromScreenCode == ScreenCode.ISLAND) {
                                switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode()[toScreenCode.ordinal()]) {
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    case 10:
                                    case 12:
                                        return new ScreenTransition(0);
                                }
                            }
                            break;
                    }
                }
                if (toScreenCode != ScreenCode.ISLAND || fromScreenCode == null) {
                    return null;
                }
                switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode()[fromScreenCode.ordinal()]) {
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                        return new ScreenTransition(1);
                    case 11:
                    default:
                        return null;
                }
        }
    }

    public static void showPreviousScreen() {
        getInstance().screen.dispose = true;
        if (screenStack.isEmpty()) {
            showScreen(ScreenCode.ISLAND);
        } else {
            showScreen(screenStack.removeLast().getScreen());
        }
    }

    public static void showScreen(ScreenCode screenCode) {
        showScreen(screenCode, true, true);
    }

    public static void backToIslandFromBattleFlow() {
        if (Game.getPlatformHelper().shouldShowAds()) {
            Game.getPlatformHelper().getAdsService().setAfterBattleAdPending(true);
        }
        showScreen(ScreenCode.ISLAND);
    }

    public static void backToWorldFromBattleFlow(int unlockedIslandId) {
        if (Game.getPlatformHelper().shouldShowAds()) {
            Game.getPlatformHelper().getAdsService().setAfterBattleAdPending(true);
        }
        showScreen(new WorldScreen(unlockedIslandId));
    }

    public static void showScreen(Screen screen) {
        showScreen(screen, true, true);
    }

    public static synchronized void showScreenWhenPossible(final ScreenCode screenCode) {
        if (!isInitialized) {
            startScreenCode = screenCode;
        } else {
            runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxy.4
                @Override // java.lang.Runnable
                public void run() {
                    MonsterGalaxy.showScreen(screenCode);
                }
            });
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Game
    public Screen getStartScreen() {
        return new SplashScreen();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Game, com.badlogic.gdx.ApplicationListener
    public void create() {
        super.create();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Game, com.badlogic.gdx.ApplicationListener
    public void dispose() {
        instance = null;
    }

    public static MonsterGalaxy getInstance() {
        if (instance == null) {
            return null;
        }
        return instance.get();
    }

    public static boolean canShowTweaksMenu() {
        Screen nextScreen = getInstance().nextScreen;
        return tweakMenuEnabledScreens.contains(getInstance().screen.getCode()) && (nextScreen == null || tweakMenuEnabledScreens.contains(nextScreen));
    }

    public static void loadAndSetTrainer(File file) {
        getInstance().internalLoadAndSetTrainer(file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void verifyNewVersionAvailability() {
        if (gaiaClient != null) {
            newVersionAvailable = gaiaClient.verifyNewVersionAvailability();
        }
    }
}
