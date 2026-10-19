package com.gaiaonline.monstergalaxy.startup;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: classes.dex */
public class AndroidChooseTamerNameScreen extends ChooseTamerNameScreen {
    private static final String FIELD_NAME = "TamerNameTextField";

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected void addTamerNameTextField(int x, int y, int width, int height, Integer maxChars, String disallowedChars) {
        float screenX = x * ResolutionManager.getHorizontalAR();
        float screenY = ((320.0f - y) - (height / 2)) * ResolutionManager.getVerticalAR();
        float screenW = width * ResolutionManager.getHorizontalAR();
        float screenH = height * ResolutionManager.getVerticalAR();
        Game.getAndroidPlatformHelper().addTextField(FIELD_NAME, (int) screenX, (int) screenY, (int) screenW, (int) screenH, false, false, maxChars, disallowedChars, true, true);
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected void removeTamerNameTextField() {
        Game.getAndroidPlatformHelper().removeTextField(FIELD_NAME);
        Game.getAndroidPlatformHelper().switchKeyboard(false);
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected String getTamerName() {
        return Game.getAndroidPlatformHelper().getTextfieldValue(FIELD_NAME);
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected void setTamerName(String tamerName) {
        Game.getAndroidPlatformHelper().setTextfieldValue(FIELD_NAME, tamerName);
    }
}
