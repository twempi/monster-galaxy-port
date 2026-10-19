package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;

/* JADX INFO: loaded from: classes.dex */
public class AndroidNameMogaDialog extends NameMogaDialog {
    private static final String FIELD_NAME = "MogaNameTextField";

    protected AndroidNameMogaDialog(ScreenListener screenListener, Moga moga) {
        super(screenListener, moga);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog
    protected void addMogaNameTextField(int x, int y, int width, int height, Integer maxChars, String disallowedChars) {
        float screenX = x * ResolutionManager.getHorizontalAR();
        float screenY = ((320.0f - y) - (height / 2)) * ResolutionManager.getVerticalAR();
        float screenW = width * ResolutionManager.getHorizontalAR();
        float screenH = height * ResolutionManager.getVerticalAR();
        Game.getAndroidPlatformHelper().addTextField(FIELD_NAME, (int) screenX, (int) screenY, (int) screenW, (int) screenH, false, false, maxChars, disallowedChars, true, true);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog
    protected String getMogaName() {
        return Game.getAndroidPlatformHelper().getTextfieldValue(FIELD_NAME);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog
    protected void setMogaName(String mogaName) {
        Game.getAndroidPlatformHelper().setTextfieldValue(FIELD_NAME, mogaName);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void hide() {
        super.hide();
        Game.getAndroidPlatformHelper().removeTextField(FIELD_NAME);
        Game.getAndroidPlatformHelper().switchKeyboard(false);
    }
}
