package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.battle.BattleScreen;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BattlePausedDialog extends Dialog {
    private ButtonElement runAwayButton;

    public BattlePausedDialog(BattleScreen screen) {
        super(screen);
        addLabel("PAUSED", 0.0f, 225.0f, ScreenElement.RelPoint.CENTER_BOTTOM, null, ScreenElement.RelPoint.CENTER_BOTTOM, 1.5f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        TextureRegion normalButton = Assets.loadTexture("shop.button.off");
        TextureRegion selectedButton = Assets.loadTexture("shop.button.on");
        ButtonElement skyShopButton = addButton(UIEvent.SKY_SHOP_BUTTON, normalButton, selectedButton, 0.0f, 174.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("Sky Shop", 171.0f, -32.0f, ScreenElement.RelPoint.LEFT_TOP, skyShopButton, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        TextureRegion normalButton2 = Assets.loadTexture("red.btn");
        TextureRegion selectedButton2 = Assets.loadTexture("red.btn.press");
        this.runAwayButton = addButton(UIEvent.RUN_AWAY_BUTTON, normalButton2, selectedButton2, -88.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("Run Away", 79.0f, -20.0f, ScreenElement.RelPoint.LEFT_TOP, this.runAwayButton, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        TextureRegion normalButton3 = Assets.loadTexture("green.btn");
        TextureRegion selectedButton3 = Assets.loadTexture("green.btn.press");
        ButtonElement resumeButton = addButton(UIEvent.RESUME_BUTTON, normalButton3, selectedButton3, 90.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("Resume", 79.0f, -20.0f, ScreenElement.RelPoint.LEFT_TOP, resumeButton, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
    }

    public void setRunawayEnabled(boolean enabled) {
        this.runAwayButton.setEnabled(enabled);
        if (enabled) {
            this.runAwayButton.setColor(null);
        } else {
            this.runAwayButton.setColor(ColorConstants.RUNNAWAY_FONT_COLOR);
        }
    }
}
