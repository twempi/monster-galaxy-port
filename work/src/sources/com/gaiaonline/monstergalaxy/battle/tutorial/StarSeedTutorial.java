package com.gaiaonline.monstergalaxy.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class StarSeedTutorial extends Screen implements ScreenListener {
    private TextElement descriptionText;
    private TextureElement dialogBackground;
    private TextElement titleText;

    public StarSeedTutorial() {
        this.root.addBackground(Assets.loadTexture("choosezodiacmoga.bg"));
        this.dialogBackground = this.root.add(Assets.loadTexture("bg.widePopup"), 0.0f, 0.0f);
        addButtons();
        buildStarSeedScreen();
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeTextView();
        }
    }

    private void addButtons() {
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        ButtonElement okButton = new ButtonElement(this, UIEvent.OK_BUTTON, normalButton, selectedButton);
        okButton.setPosition(ScreenElement.RelPoint.CENTER, 145.0f, -130.0f);
        Assets.loadTexture("green.btn.small");
        Assets.loadTexture("green.btn.press");
        this.root.add(okButton);
        this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.OK), 145.0f, -125.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
    }

    private void buildStarSeedScreen() {
        this.titleText = this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.USING_STARSEEDS), 0.0f, 130.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.root.add(Assets.loadTexture("capture.starseed.icon"), -20.0f, -5.0f, ScreenElement.RelPoint.LEFT_TOP, this.titleText, ScreenElement.RelPoint.CENTER);
        this.titleText.enableHorizontalAutoSize(350.0f);
        TextElement duringBattle = this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.DURING_BATTLE_COLON), 20.0f, -10.0f, ScreenElement.RelPoint.LEFT_TOP, this.dialogBackground, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.SECONARY_FONT_COLOR, true, false);
        duringBattle.enableHorizontalAutoSize(150.0f);
        this.descriptionText = new TextElement("", 0.48f, ColorConstants.PRIMARY_FONT_COLOR, true, 160.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setAlign(this.dialogBackground);
        this.descriptionText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 15.0f, -35.0f);
        this.descriptionText.setText(I18nManager.getText(I18nManager.I18nKey.STARSEEDS_TUTORIAL_STEPS));
        this.descriptionText.setWrapWidth(160.0f);
        this.descriptionText.enableVerticalAutoSize(140.0f);
        this.root.add(this.descriptionText);
        this.root.add(Assets.loadLocalizedTexture("capture.screenshot"), 72.0f, 6.0f);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        Game.getAnalytics().trackEvent("acquisition", "", "", "complete_item_howto", "", "");
        MonsterGalaxy.showBattleScreen();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }
}
