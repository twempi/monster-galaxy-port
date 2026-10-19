package com.gaiaonline.monstergalaxy.app;

/* JADX INFO: loaded from: classes.dex */
public enum ScreenCode {
    CHOOSE_TAMER,
    CHOOSE_TAMER_NAME,
    CHOOSE_MOGA,
    CHOOSE_MOGA_NAME,
    ISLAND,
    TEAM,
    WORLD,
    TAMER,
    QUEST,
    SETTINGS,
    SHARE,
    SHOP,
    SLOTMACHINE,
    SLOTMACHINE_RESULTS,
    SPLASH;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static ScreenCode[] valuesCustom() {
        ScreenCode[] screenCodeArrValuesCustom = values();
        int length = screenCodeArrValuesCustom.length;
        ScreenCode[] screenCodeArr = new ScreenCode[length];
        System.arraycopy(screenCodeArrValuesCustom, 0, screenCodeArr, 0, length);
        return screenCodeArr;
    }
}
