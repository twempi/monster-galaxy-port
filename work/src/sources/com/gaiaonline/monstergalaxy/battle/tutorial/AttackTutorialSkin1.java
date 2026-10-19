package com.gaiaonline.monstergalaxy.battle.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class AttackTutorialSkin1 extends Dialog implements ScreenListener {
    private TextureElement attackStrengthMeter;
    private ButtonElement backButton;
    private TextElement backText;
    private TextureRegion background;
    private ActorScreenElement blackArrow;
    private int currentScreen;
    private TextureElement curvedArrow;
    private TextElement descriptionText;
    private TextureElement fingerStarbust;
    private ButtonElement goButton;
    private TextElement goText;
    private float height;
    private ScreenListener listener;
    private ButtonElement nextButton;
    private TextElement nextText;
    private TextureElement tapTutorial;
    private TextElement titleText;
    private float width;

    public AttackTutorialSkin1(ScreenListener listener) {
        super(null, false);
        this.listener = listener;
        this.background = Assets.loadTexture("bg.attackTutorial");
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        setScreenListener(this);
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        this.nextButton = addButton(UIEvent.NEXT_BUTTON, normalButton, selectedButton, 170.0f, -130.0f);
        this.backButton = addButton(UIEvent.BACK_BUTTON, normalButton, selectedButton, -170.0f, -130.0f);
        this.nextText = addLabel(I18nManager.getText(I18nManager.I18nKey.NEXT), 170.0f, -125.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.backText = addLabel(I18nManager.getText(I18nManager.I18nKey.BACK), -170.0f, -125.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        buildFirstScreen();
        buildSecondScreen();
        buildThirdScreen();
        showScreen();
    }

    private void buildFirstScreen() {
        this.attackStrengthMeter = add(Assets.loadTexture("attack.strength.meter"), 152.0f, -158.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        this.curvedArrow = add(Assets.loadTexture("curved.arrow"), 125.0f, -160.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        Image blackArrowActor = new Image(Assets.loadTexture("black.arrow"));
        ResolutionManager.scaleActor(blackArrowActor);
        this.blackArrow = new ActorScreenElement();
        this.blackArrow.setActor(blackArrowActor);
        this.blackArrow.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        add(this.blackArrow);
        this.descriptionText = new TextElement("", 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true, 140.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, 30.0f, 80.0f);
        add(this.descriptionText);
    }

    private void buildSecondScreen() {
        this.fingerStarbust = add(Assets.loadTexture("finger.starburst"), 180.0f, -200.0f, ScreenElement.RelPoint.LEFT_TOP);
        this.tapTutorial = add(Assets.loadLocalizedTexture("tap.tutorial"), 160.0f, -160.0f, ScreenElement.RelPoint.LEFT_TOP);
    }

    private void buildThirdScreen() {
        this.titleText = addLabel(I18nManager.getText(I18nManager.I18nKey.THROW_YOUR_FIRST_ATTACK), 0.0f, 80.0f, 0.6f, ColorConstants.SECONARY_FONT_COLOR, false);
        TextureRegion normalButton = Assets.loadTexture("green.btn");
        TextureRegion selectedButton = Assets.loadTexture("green.btn.press");
        this.goButton = addButton(UIEvent.OK_BUTTON, normalButton, selectedButton, 0.0f, -130.0f);
        this.goText = addLabel(I18nManager.getText(I18nManager.I18nKey.GO), 0.0f, -125.0f, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
    }

    private void showFirstScreen() {
        setElementsVisibility();
        this.blackArrow.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        this.blackArrow.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 35.0f, -65.0f);
        this.blackArrow.setAlign(this.attackStrengthMeter);
        this.blackArrow.getActor().setRotation(40.0f);
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, 33.0f, 68.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setWrapWidth(180.0f);
        this.descriptionText.setText(I18nManager.getText(I18nManager.I18nKey.TUTORIAL_SK1_SCREEN1_TEXT));
        this.descriptionText.enableVerticalAutoSize(160.0f);
    }

    private void showSecondScreen() {
        setElementsVisibility();
        this.blackArrow.setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        this.blackArrow.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 82.0f);
        this.blackArrow.getActor().setRotation(-80.0f);
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, 33.0f, 68.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setWrapWidth(180.0f);
        this.descriptionText.setText(I18nManager.getText(I18nManager.I18nKey.TUTORIAL_SK1_SCREEN2_TEXT));
        this.descriptionText.enableVerticalAutoSize(160.0f);
    }

    private void showThirdScreen() {
        setElementsVisibility();
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, -130.0f, 43.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setWrapWidth(250.0f);
        this.descriptionText.setText(I18nManager.getText(I18nManager.I18nKey.TUTORIAL_SK1_SCREEN3_TEXT));
        this.descriptionText.enableVerticalAutoSize(120.0f);
        addOverlay(0.5f);
    }

    private void setElementsVisibility() {
        this.nextButton.setVisible(this.currentScreen != 2);
        this.nextText.setVisible(this.currentScreen != 2);
        this.curvedArrow.setVisible(this.currentScreen == 0);
        this.attackStrengthMeter.setVisible(this.currentScreen != 2);
        this.blackArrow.setVisible(this.currentScreen != 2);
        this.fingerStarbust.setVisible(this.currentScreen == 1);
        this.tapTutorial.setVisible(this.currentScreen == 1);
        this.backButton.setVisible(this.currentScreen == 1);
        this.backText.setVisible(this.currentScreen == 1);
        this.goButton.setVisible(this.currentScreen == 2);
        this.goText.setVisible(this.currentScreen == 2);
        this.titleText.setVisible(this.currentScreen == 2);
    }

    private void showScreen() {
        if (this.currentScreen == 0) {
            showFirstScreen();
        } else if (this.currentScreen == 1) {
            showSecondScreen();
        } else {
            showThirdScreen();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void present(SpriteBatch batcher) {
        if (this.currentScreen != 2) {
            batcher.begin();
            batcher.draw(this.background, 0.0f, 0.0f, this.width, this.height);
            batcher.end();
        }
        super.present(batcher);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (event == UIEvent.NEXT_BUTTON) {
            this.currentScreen++;
            showScreen();
        } else if (event == UIEvent.BACK_BUTTON) {
            this.currentScreen--;
            showScreen();
        } else if (event == UIEvent.OK_BUTTON) {
            this.listener.onUIEvent(event);
            Game.getAnalytics().trackEvent("acquisition", "", "", "complete_attack_howto", "", "");
        }
    }
}
