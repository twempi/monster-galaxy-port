package com.gaiaonline.monstergalaxy.app;

import com.gaiaonline.monstergalaxy.map.IslandScreen;
import com.gaiaonline.monstergalaxy.map.TamerScreen;
import com.gaiaonline.monstergalaxy.map.WorldScreen;
import com.gaiaonline.monstergalaxy.quest.QuestScreen;
import com.gaiaonline.monstergalaxy.settings.SettingsScreen;
import com.gaiaonline.monstergalaxy.share.ShareScreen;
import com.gaiaonline.monstergalaxy.shop.ShopScreen;
import com.gaiaonline.monstergalaxy.slotmachine.SlotMachineResultScreen;
import com.gaiaonline.monstergalaxy.slotmachine.SlotMachineScreen;
import com.gaiaonline.monstergalaxy.splash.SplashScreen;
import com.gaiaonline.monstergalaxy.startup.ChooseMogaNameScreen;
import com.gaiaonline.monstergalaxy.startup.ChooseMogaScreen;
import com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen;
import com.gaiaonline.monstergalaxy.startup.ChooseTamerScreen;
import com.gaiaonline.monstergalaxy.team.TeamScreen;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ScreenFactory {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode;
    private static Map<Class<? extends Screen>, ScreenCode> screenCodes = new HashMap();

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

    static {
        screenCodes.put(ChooseTamerScreen.class, ScreenCode.CHOOSE_TAMER);
        screenCodes.put(ChooseTamerNameScreen.class, ScreenCode.CHOOSE_TAMER_NAME);
        screenCodes.put(ChooseMogaScreen.class, ScreenCode.CHOOSE_MOGA);
        screenCodes.put(ChooseMogaNameScreen.class, ScreenCode.CHOOSE_MOGA_NAME);
        screenCodes.put(IslandScreen.class, ScreenCode.ISLAND);
        screenCodes.put(TeamScreen.class, ScreenCode.TEAM);
        screenCodes.put(TamerScreen.class, ScreenCode.TAMER);
        screenCodes.put(WorldScreen.class, ScreenCode.WORLD);
        screenCodes.put(QuestScreen.class, ScreenCode.QUEST);
        screenCodes.put(SettingsScreen.class, ScreenCode.SETTINGS);
        screenCodes.put(ShopScreen.class, ScreenCode.SHOP);
        screenCodes.put(ShareScreen.class, ScreenCode.SHARE);
        screenCodes.put(SlotMachineScreen.class, ScreenCode.SLOTMACHINE);
        screenCodes.put(SlotMachineResultScreen.class, ScreenCode.SLOTMACHINE_RESULTS);
        screenCodes.put(SplashScreen.class, ScreenCode.SPLASH);
    }

    public static Screen createScreen(ScreenCode screenCode) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$app$ScreenCode()[screenCode.ordinal()]) {
            case 1:
                return new ChooseTamerScreen();
            case 2:
                return ChooseTamerNameScreen.newInstance();
            case 3:
                return new ChooseMogaScreen();
            case 4:
                return ChooseMogaNameScreen.newInstance();
            case 5:
                return new IslandScreen();
            case 6:
                return new TeamScreen();
            case 7:
                return new WorldScreen();
            case 8:
                return new TamerScreen();
            case 9:
                return new QuestScreen();
            case 10:
                return new SettingsScreen();
            case 11:
                return new ShareScreen();
            case 12:
                return new ShopScreen();
            case 13:
                return new SlotMachineScreen();
            default:
                throw new RuntimeException("No screen by that name: " + screenCode);
        }
    }

    public static ScreenCode getScreenClassScreenCode(Class<? extends Screen> clazz) {
        return screenCodes.get(clazz);
    }
}
