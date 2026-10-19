package com.gaiaonline.monstergalaxy.slotmachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.team.MogaPortraitDialog;
import java.util.EnumSet;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class SlotMachineBaseScreen extends Screen {
    protected static final float FONT_SCALE_OVER_MAX_VALUE = 0.42f;
    protected static final float FONT_SCALE_REGULAR = 0.75f;
    private static EnumSet<ScreenCode> slotMachineScreens = EnumSet.of(ScreenCode.SLOTMACHINE, ScreenCode.SLOTMACHINE_RESULTS);
    private ShadowTextElement coinsText;
    protected ButtonElement mainButton;

    public abstract void addUIComponents();

    public abstract void onMainButtonPressed();

    public SlotMachineBaseScreen(String title, String backgroundAssetName) {
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(this.stage);
        this.root.addBackground(Assets.loadTexture(backgroundAssetName));
        TextureElement luckyMogaText = new TextureElement(Assets.loadTexture("lucky.moga"));
        luckyMogaText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        luckyMogaText.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 130.0f);
        this.root.add(luckyMogaText);
        TextureElement mcashIcon = new TextureElement(Assets.loadTexture("capture.mcash"));
        mcashIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        mcashIcon.setPosition(ScreenElement.RelPoint.LEFT_TOP, 20.0f, -20.0f);
        this.root.add(mcashIcon);
        this.coinsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.coinsText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.coinsText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 55.0f, -17.0f);
        this.root.add(this.coinsText);
        TextElement titleText = new TextElement(title, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
        titleText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        titleText.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -55.0f);
        this.root.add(titleText);
        this.mainButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                SlotMachineBaseScreen.this.onMainButtonPressed();
            }
        }, null, Assets.greenBtn, Assets.greenBtnPress);
        this.mainButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.mainButton.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 35.0f);
        this.root.add(this.mainButton);
        addUIComponents();
        ButtonElement closeButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen.2
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                MonsterGalaxy.showPreviousScreen();
            }
        }, null, Assets.closeBtn, Assets.closeBtnPress);
        closeButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        closeButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 453.0f, 293.0f);
        this.root.add(closeButton);
        MonsterGalaxy.updateMiniGameShownDate();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        if (prevScreen != null && !slotMachineScreens.contains(prevScreen.getCode())) {
            pushMusic();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onExit(Screen nextScreen) {
        if (nextScreen != null && !slotMachineScreens.contains(nextScreen.getCode())) {
            popMusic();
        }
    }

    private void pushMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.pushMusic(SoundManager.MUSIC_SHOP_SCREEN);
        }
    }

    private void popMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.popMusic();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateField(this.coinsText, Game.getTrainer().getCoins(), Constants.MAX_DISPLAY_VALUE_MOGA_CASH);
    }

    public void showMogaInfoDialog(MogaType mogaType) {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen.3
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                SlotMachineBaseScreen.this.hideDialog();
            }
        };
        MogaPortraitDialog mogaInfoDialog = new MogaPortraitDialog(listener, Moga.newMogaFromType(mogaType));
        showDialog(mogaInfoDialog);
    }

    private void updateField(TextElement field, int value, int maxDisplayValue) {
        if (value <= maxDisplayValue) {
            field.setText(String.valueOf(value));
            field.setFontScale(FONT_SCALE_REGULAR);
        } else {
            field.setText("+" + maxDisplayValue);
            field.setFontScale(FONT_SCALE_OVER_MAX_VALUE);
        }
    }
}
