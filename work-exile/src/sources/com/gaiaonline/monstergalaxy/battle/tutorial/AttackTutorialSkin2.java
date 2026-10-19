package com.gaiaonline.monstergalaxy.battle.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AttackTutorialSkin2 extends Dialog implements ScreenListener {
    private TextureElement attackStrengthMeter;
    private ButtonElement backButton;
    private TextElement backText;
    private TextureRegion background;
    private TextureElement blackArrow;
    private int currentScreen;
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
    private TextureElement tutorialArrow;
    private float width;

    public AttackTutorialSkin2(ScreenListener listener) {
        super(null, false);
        this.listener = listener;
        this.background = Assets.loadTexture("bg.attackTutorial");
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        setScreenListener(this);
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        this.nextButton = addButton(UIEvent.NEXT_BUTTON, normalButton, selectedButton, 200.0f, -50.0f);
        this.backButton = addButton(UIEvent.BACK_BUTTON, normalButton, selectedButton, -200.0f, -50.0f);
        this.nextText = addLabel("Next", 200.0f, -45.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.backText = addLabel("Back", -200.0f, -45.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        buildFirstScreen();
        buildSecondScreen();
        buildThirdScreen();
        showScreen();
    }

    private void buildFirstScreen() {
        float scaleFactor = ResolutionManager.getScaleFactor();
        float width = Gdx.graphics.getWidth() / scaleFactor;
        this.attackStrengthMeter = add(Assets.loadTexture("attack.strength.meter"), 0.0f, 0.0f, width, 74.0f, ScreenElement.RelPoint.LEFT_BOTTOM, null, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.tutorialArrow = add(Assets.loadTexture("straight.arrow"), 70.0f, 60.0f, ScreenElement.RelPoint.LEFT_BOTTOM, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.blackArrow = add(Assets.loadTexture("black.arrow"), -20.0f, 0.0f, ScreenElement.RelPoint.LEFT_BOTTOM, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.descriptionText = new TextElement("", 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true, 140.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, 30.0f, 80.0f);
        add(this.descriptionText);
    }

    private void buildSecondScreen() {
        this.fingerStarbust = add(Assets.loadTexture("finger.starburst"), 0.0f, 60.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        this.tapTutorial = add(Assets.loadTexture("tap.tutorial"), -20.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
    }

    private void buildThirdScreen() {
        this.titleText = addLabel("Throw your first attack!", 0.0f, 80.0f, 0.6f, ColorConstants.SECONARY_FONT_COLOR, false);
        TextureRegion normalButton = Assets.loadTexture("green.btn");
        TextureRegion selectedButton = Assets.loadTexture("green.btn.press");
        this.goButton = addButton(UIEvent.OK_BUTTON, normalButton, selectedButton, 0.0f, -130.0f);
        this.goText = addLabel("Go", 0.0f, -125.0f, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
    }

    private void showFirstScreen() {
        setElementsVisibility();
        this.blackArrow.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.blackArrow.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, -20.0f, 0.0f);
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, -80.0f, 90.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setWrapWidth(220.0f);
        this.descriptionText.setText("Let's learn how to fight! \nWe want to stop the arrow when it's in the green area.");
    }

    private void showSecondScreen() {
        setElementsVisibility();
        this.blackArrow.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 355.0f, 0.0f);
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, -80.0f, 90.0f);
        this.descriptionText.setText("Tap anywhere on the screen to stop the moving arrow and launch your attack.");
    }

    private void showThirdScreen() {
        setElementsVisibility();
        this.descriptionText.setPosition(ScreenElement.RelPoint.CENTER, -95.0f, 48.0f);
        this.descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.descriptionText.setWrapWidth(230.0f);
        this.descriptionText.setText("1) Tap the Attack button.\n\n2) Tap to stop the arrow when it's in the green area.");
        addOverlay(0.5f);
    }

    private void setElementsVisibility() {
        this.nextButton.setVisible(this.currentScreen != 2);
        this.nextText.setVisible(this.currentScreen != 2);
        this.tutorialArrow.setVisible(this.currentScreen == 0);
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
