package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.PieMeshElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class CaptureDialog extends Dialog {
    private static final int STARSEED_CAPTURE_COST = 1;
    private ButtonElement captureButton;

    public CaptureDialog(ScreenListener screen, Moga cpuMoga, float captureRate, int currentEncounterId) {
        super(screen);
        add(Assets.loadTexture("capture.bg"), 0.0f, 0.0f);
        addLabel(I18nManager.getText(I18nManager.I18nKey.CAPTURE_MOGA_QUESTION).replace("%moganame", cpuMoga.getName()), 0.0f, 280.0f, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        add(Assets.loadLocalizedTexture(cpuMoga.getType().getRarityAssetName()), 0.0f, 252.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        boolean showHintArrow = currentEncounterId == 456 || currentEncounterId == 458;
        Color color = showHintArrow ? ColorConstants.HINT_ARROW_COLOR : null;
        addCaptureButton(false, UIEvent.THROW_MASTERSEED, 100, I18nManager.getText(I18nManager.I18nKey.MASTER_STARSEED), Assets.loadTexture("capture.mcash"), Assets.loadLocalizedTexture("capture.masterseed"), cpuMoga.getType().getCaptureCost(), color);
        addCaptureButton(true, UIEvent.THROW_STARSEED, Math.round(100.0f * captureRate), I18nManager.getText(I18nManager.I18nKey.STARSEED), Assets.loadTexture("capture.starseed.icon"), Assets.loadTexture("capture.starseed"), 1, null);
        ButtonElement cancelButton = addButton(UIEvent.RESUME_BUTTON, Assets.redBtnSmall, Assets.redBtnSmallPressed, -114.0f, 60.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel(I18nManager.getText(I18nManager.I18nKey.CANCEL), 0.0f, 7.0f, ScreenElement.RelPoint.CENTER, cancelButton, ScreenElement.RelPoint.CENTER, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        TextElement youHaveLabel = addLabel(I18nManager.getText(I18nManager.I18nKey.YOU_HAVE_COLON), 5.0f, 52.0f, ScreenElement.RelPoint.CENTER_BOTTOM, null, ScreenElement.RelPoint.CENTER_BOTTOM, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        add(Assets.loadTexture("capture.mcash"), 50.0f, 4.0f, ScreenElement.RelPoint.CENTER_BOTTOM, youHaveLabel);
        addLabel(String.valueOf(Game.getTrainer().getCoins()), 70.0f, 0.0f, ScreenElement.RelPoint.CENTER_BOTTOM, youHaveLabel, ScreenElement.RelPoint.LEFT_BOTTOM, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        add(Assets.loadTexture("capture.starseed.icon"), 115.0f, 4.0f, ScreenElement.RelPoint.CENTER_BOTTOM, youHaveLabel);
        addLabel(String.valueOf(Game.getTrainer().getStarSeeds()), 128.0f, 0.0f, ScreenElement.RelPoint.CENTER_BOTTOM, youHaveLabel, ScreenElement.RelPoint.LEFT_BOTTOM, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        if (showHintArrow) {
            addArrow();
        }
    }

    private void addArrow() {
        Image arrow = new Image(Assets.loadTexture("arrow.left"));
        ScaleToAction scaleUp = new ScaleToAction();
        scaleUp.setScale(1.2f);
        scaleUp.setDuration(0.5f);
        ScaleToAction scaleDown = new ScaleToAction();
        scaleDown.setScale(1.0f);
        scaleDown.setDuration(0.5f);
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(scaleUp);
        sequence.addAction(scaleDown);
        RepeatAction forever = new RepeatAction();
        forever.setCount(-1);
        forever.setAction(sequence);
        arrow.addAction(forever);
        ResolutionManager.scaleActor(arrow);
        ActorScreenElement arrowElement = new ActorScreenElement();
        arrowElement.setActor(arrow);
        arrowElement.setAlign(this.captureButton);
        arrowElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        arrowElement.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, 20.0f, 0.0f);
        add(arrowElement);
    }

    private void addCaptureButton(boolean isAtRight, UIEvent captureEvent, int successRate, String text, TextureRegion icon, TextureRegion starseed, int captureCost, Color color) {
        this.captureButton = addButton(captureEvent, Assets.loadTexture("capture.seed.btn"), Assets.loadTexture("capture.seed.btn.press"), isAtRight ? 90 : -90, 150.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        TextureElement starseedElement = add(starseed, 120.0f, 50.0f, ScreenElement.RelPoint.LEFT_BOTTOM, this.captureButton);
        TextureElement iconElement = add(icon, 20.0f, 25.0f, ScreenElement.RelPoint.LEFT_BOTTOM, this.captureButton);
        addLabel(String.valueOf(captureCost), 40.0f, 22.0f, ScreenElement.RelPoint.LEFT_BOTTOM, this.captureButton, ScreenElement.RelPoint.LEFT_BOTTOM, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        ShadowTextElement seedText = new ShadowTextElement(text, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, Color.BLACK, true);
        seedText.setTextAlignment(BitmapFont.HAlignment.CENTER);
        seedText.setWrapWidth(78.0f);
        seedText.enableVerticalAutoSize(53.0f);
        seedText.setAlign(this.captureButton);
        seedText.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        seedText.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 120.0f, 110.0f);
        add(seedText);
        TextureElement percenCircleElement = add(Assets.loadTexture("capture.percent.circle"), 45.0f, 75.0f, ScreenElement.RelPoint.LEFT_BOTTOM, this.captureButton);
        PieMeshElement pie = new PieMeshElement(63.0f, new Texture(Gdx.files.internal("data/capture.100percent.png")));
        pie.setCompletionRate(successRate / 100.0f);
        pie.setAlign(this.captureButton);
        pie.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 45.0f, 75.0f);
        add(pie);
        addLabel(String.valueOf(successRate) + "%", 45.0f, 85.0f, ScreenElement.RelPoint.LEFT_BOTTOM, this.captureButton, ScreenElement.RelPoint.CENTER, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        addLabel(I18nManager.getText(I18nManager.I18nKey.SUCCESS), 45.0f, 63.0f, ScreenElement.RelPoint.LEFT_BOTTOM, this.captureButton, ScreenElement.RelPoint.CENTER, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        if (color != null) {
            starseedElement.setColor(color);
            iconElement.setColor(color);
            percenCircleElement.setColor(color);
            this.captureButton.setColor(color);
            this.captureButton.setEnabled(false);
        }
    }
}
