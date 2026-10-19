package com.gaiaonline.monstergalaxy.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.desktop.DesktopOptions;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.service.AbstractGaiaClient;

/* Overrides the game class: adds the desktop options panel (Android used a native view). */
public class SettingsScreen extends Screen implements ScreenListener {
    private int showNativeUICountDown = -1;
    ScreenListener doneListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.settings.SettingsScreen.1
        @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
        public void onUIEvent(UIEvent event) {
            MonsterGalaxy.showPreviousScreen();
        }
    };

    public SettingsScreen() {
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(this.stage);
        makeUI();
        SoundManager.playMusic(Constants.worldAmbienceAudio);
    }

    private void makeUI() {
        TextureElement bg = new TextureElement(Assets.loadTexture("startup.choosezodiacmoga.bg"));
        bg.setSize(Gdx.graphics.getWidth() / ResolutionManager.getScaleFactor(), Gdx.graphics.getHeight() / ResolutionManager.getScaleFactor());
        bg.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        bg.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        this.root.add(bg);
        TextureElement optionsLabel = new TextureElement(Assets.loadTexture("options"));
        optionsLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        optionsLabel.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -10.0f);
        this.root.add(optionsLabel);
        TextureRegion normalAddButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedAddButton = Assets.loadTexture("blue.btn.small.press");
        ButtonElement doneButton = new ButtonElement(this.doneListener, null, normalAddButton, selectedAddButton);
        doneButton.setAnchorPoint(ScreenElement.RelPoint.RIGHT_TOP);
        doneButton.setSize(88.0f, 56.0f);
        doneButton.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -12.0f, -7.0f);
        doneButton.setScale(1.0f);
        this.root.add(doneButton);
        if (Game.getGaiaClient().hasReimbursement()) {
            ScreenListener claimListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.settings.SettingsScreen.2
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    if (Game.getPlatformHelper().isAndroid()) {
                        Game.getAndroidPlatformHelper().removeOptionsView();
                    }
                    SettingsScreen.this.showDialog(new AlertDialog(SettingsScreen.this, 0, "Retrieving reimbursement from Gaia servers", UIEvent.TAMER_DISMISS_DIALOG));
                    AbstractGaiaClient.GaiaHttpListener listener = new AbstractGaiaClient.GaiaHttpListener() { // from class: com.gaiaonline.monstergalaxy.settings.SettingsScreen.2.1
                        @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient.GaiaHttpListener
                        public void onHttpDone(String message) {
                            ((AlertDialog) SettingsScreen.this.dialog).setText(message);
                            ((AlertDialog) SettingsScreen.this.dialog).setTimeOut(2);
                        }
                    };
                    Game.getGaiaClient().claimReimbursement(listener);
                }
            };
            ButtonElement claimButton = new ButtonElement(claimListener, UIEvent.CLAIM_PRIZE, normalAddButton, selectedAddButton);
            claimButton.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
            claimButton.setSize(88.0f, 56.0f);
            claimButton.setPosition(ScreenElement.RelPoint.LEFT_TOP, 12.0f, -7.0f);
            claimButton.setScale(1.0f);
            this.root.add(claimButton);
            this.root.addLabel("Claim", 0.0f, 5.0f, ScreenElement.RelPoint.CENTER, claimButton, ScreenElement.RelPoint.CENTER, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, true, false);
        }
        this.root.addLabel("Done", 0.0f, 5.0f, ScreenElement.RelPoint.CENTER, doneButton, ScreenElement.RelPoint.CENTER, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, true, false);

        DesktopOptions.addTo(this.root);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeOptionsView();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        this.showNativeUICountDown = 1;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.dialog != null) {
            this.dialog.update(deltaTime);
        }
        if (this.showNativeUICountDown > -1) {
            if (this.showNativeUICountDown == 0 && Game.getPlatformHelper().isAndroid()) {
                Game.getAndroidPlatformHelper().addOptionsView(Game.getGaiaClient().getDeviceUniqueId());
            }
            this.showNativeUICountDown--;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        hideDialog();
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().addOptionsView(Game.getGaiaClient().getDeviceUniqueId());
        }
    }
}
