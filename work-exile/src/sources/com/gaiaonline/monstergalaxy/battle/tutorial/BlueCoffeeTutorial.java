package com.gaiaonline.monstergalaxy.battle.tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BlueCoffeeTutorial extends Screen implements ScreenListener {
    private TextureElement dialogBackground;

    public BlueCoffeeTutorial() {
        this.root.addBackground(Assets.loadTexture("choosezodiacmoga.bg"));
        this.dialogBackground = this.root.add(Assets.loadTexture("bg.widePopup"), 0.0f, 0.0f);
        addButtons();
        buildBlueCoffeeScreen();
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeTextView();
        }
    }

    private void addButtons() {
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        ButtonElement okButton = new ButtonElement(this, UIEvent.OK_BUTTON, normalButton, selectedButton);
        okButton.setPosition(ScreenElement.RelPoint.CENTER, 150.0f, -130.0f);
        Assets.loadTexture("green.btn.small");
        Assets.loadTexture("green.btn.press");
        this.root.add(okButton);
        this.root.addLabel("Ok", 150.0f, -125.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
    }

    private void buildBlueCoffeeScreen() {
        TextElement titleText = this.root.addLabel("Using Blue Coffees.", 0.0f, 130.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.root.add(Assets.loadTexture("blue.coffee.icon"), -20.0f, -5.0f, ScreenElement.RelPoint.LEFT_TOP, titleText, ScreenElement.RelPoint.CENTER);
        TextureElement blueCoffeeScreen = this.root.add(Assets.loadTexture("bluecoffee.screenshot"), 72.0f, 6.0f);
        this.root.addLabel("During Battle:", 20.0f, -10.0f, ScreenElement.RelPoint.LEFT_TOP, this.dialogBackground, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.SECONARY_FONT_COLOR, true, false);
        TextElement descriptionText = new TextElement("", 0.48f, ColorConstants.PRIMARY_FONT_COLOR, true, 135.0f);
        descriptionText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        descriptionText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 25.0f, -35.0f);
        descriptionText.setAlign(this.dialogBackground);
        descriptionText.setText("1) Expand your team by tapping the\ngreen arrow.\n2) Tap the        Blue Coffee icon.\n3) Your Moga's health is restored.");
        this.root.add(descriptionText);
        TextureElement greenArrow = this.root.add(Assets.loadTexture("btn.arrow.right"), 0.0f, 0.0f, ScreenElement.RelPoint.LEFT_TOP);
        TextureElement blueCoffeeIcon = this.root.add(Assets.loadTexture("blue.coffee.icon"), 0.0f, 0.0f, ScreenElement.RelPoint.CENTER);
        greenArrow.setPosition(ScreenElement.RelPoint.LEFT_CENTER, -42.0f, 27.0f);
        greenArrow.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        greenArrow.setAlign(blueCoffeeScreen);
        greenArrow.setScale(0.8f);
        blueCoffeeIcon.setPosition(ScreenElement.RelPoint.LEFT_CENTER, -63.0f, 0.0f);
        blueCoffeeIcon.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        blueCoffeeIcon.setAlign(blueCoffeeScreen);
        blueCoffeeIcon.setScale(0.8f);
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
