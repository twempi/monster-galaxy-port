package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class BattleEndedDialog extends Dialog {
    public BattleEndedDialog(ScreenListener screen, boolean playerWon) {
        this(screen, playerWon, null);
        this.keepAnimation = true;
    }

    public BattleEndedDialog(ScreenListener screen, boolean playerWon, Moga lastCapturedMoga) {
        super(screen, false);
        if (playerWon) {
            this.keepAnimation = true;
        }
        Vector2 position = new Vector2(240.0f, 210.0f);
        add(playerWon ? Assets.loadLocalizedTexture("congrazzles") : Assets.loadLocalizedTexture("you.lose"), 0.0f, position.y, ScreenElement.RelPoint.CENTER_BOTTOM);
        if (playerWon) {
            if (lastCapturedMoga != null) {
                position.y -= 30.0f;
                addLabel(I18nManager.getText(I18nManager.I18nKey.YOU_VE_CAUGHT), 0.0f, position.y, ScreenElement.RelPoint.CENTER_BOTTOM, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
                position.y -= 22.0f;
                addLabel(String.valueOf(lastCapturedMoga.getType().getLocalizedName()) + "!", 0.0f, position.y, ScreenElement.RelPoint.CENTER_BOTTOM, 0.9f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
                TextureElement starseedBig = new TextureElement(Assets.loadTexture("starseed.big"));
                starseedBig.setScale(0.5f * ResolutionManager.getScaleFactor());
                starseedBig.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                float proportionalOffset = 120.0f * ResolutionManager.getScaleFactor();
                starseedBig.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, position.x + proportionalOffset, position.y - 26.0f);
                add(starseedBig);
            } else {
                position.y -= 30.0f;
                addLabel(I18nManager.getText(I18nManager.I18nKey.YOU_WON), 0.0f, position.y, ScreenElement.RelPoint.CENTER_BOTTOM, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
            }
            SoundManager.playMusic(SoundManager.MUSIC_BATLE_WON);
        } else {
            SoundManager.playSound(SoundManager.SOUND_BATLE_LOST);
        }
        position.y -= 120.0f;
        addButton(UIEvent.DONE_BUTTON, Assets.blueBtn, Assets.bluePressedBtn, 0.0f, position.y, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel(I18nManager.getText(I18nManager.I18nKey.DONE), 0.0f, 5.0f + position.y, ScreenElement.RelPoint.CENTER_BOTTOM, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, false);
    }
}
