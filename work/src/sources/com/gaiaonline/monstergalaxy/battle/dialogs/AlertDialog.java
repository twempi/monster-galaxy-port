package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class AlertDialog extends Dialog {
    private static final float MAX_MESSAGE_HEIGHT = 95.0f;
    private static final float MAX_MESSAGE_WIDTH = 335.0f;
    private UIEvent closeEvent;
    private long dialogTimeOut;
    private float elapsedTime;
    private ScreenListener listener;
    private TextElement messageTextElement;

    public AlertDialog(ScreenListener listener, int seconds, String message, UIEvent closeEvent) {
        super(listener, false);
        this.listener = listener;
        this.closeEvent = closeEvent;
        this.dialogTimeOut = seconds;
        add(Assets.loadTexture("alert.bg"), 0.0f, 0.0f);
        this.messageTextElement = addLabel("", 0.0f, 0.0f, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.messageTextElement.setPosition(ScreenElement.RelPoint.CENTER, -10.0f, 0.0f);
        this.messageTextElement.setText(message);
        this.messageTextElement.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.messageTextElement.setWrapWidth(MAX_MESSAGE_WIDTH);
        this.messageTextElement.enableVerticalAutoSize(MAX_MESSAGE_HEIGHT);
    }

    public void setText(String message) {
        if (this.messageTextElement != null) {
            this.messageTextElement.setText(message);
        }
    }

    public void setTimeOut(int seconds) {
        this.dialogTimeOut = seconds;
        this.elapsedTime = 0.0f;
    }

    public AlertDialog(ScreenListener listener, String message, String okBtnText, UIEvent okEvent, UIEvent closeEvent) {
        this(listener, message, okBtnText, okEvent, closeEvent, true);
    }

    public AlertDialog(ScreenListener listener, String message, String yesBtnText, UIEvent yesEvent, UIEvent closeEvent, boolean addOverlay) {
        this(listener, message, yesBtnText, yesEvent, null, null, closeEvent, addOverlay);
    }

    public AlertDialog(ScreenListener listener, String message, String yesBtnText, UIEvent yesEvent, String noBtnText, UIEvent noEvent, UIEvent closeEvent, boolean addOverlay) {
        super(listener, addOverlay);
        TextureElement bg = add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f);
        this.messageTextElement = new TextElement(message, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.messageTextElement.setText(message);
        this.messageTextElement.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.messageTextElement.setWrapWidth(MAX_MESSAGE_WIDTH);
        this.messageTextElement.enableVerticalAutoSize(MAX_MESSAGE_HEIGHT);
        this.messageTextElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        this.messageTextElement.setPosition(ScreenElement.RelPoint.CENTER_TOP, -10.0f, -15.0f);
        this.messageTextElement.setAlign(bg);
        add(this.messageTextElement);
        if (noBtnText == null) {
            ButtonElement yesBtn = addButton(yesEvent, Assets.greenBtnLarge, Assets.greenBtnLargePress, 0.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
            TextElement yesTextElement = addLabel(yesBtnText, 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, yesBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
            yesTextElement.enableHorizontalAutoSize(180.0f);
        } else {
            ButtonElement yesBtn2 = addButton(yesEvent, Assets.greenBtn, Assets.greenBtnPress, -90.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
            addLabel(yesBtnText, 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, yesBtn2, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
            ButtonElement noBtn = addButton(noEvent, Assets.redBtn, Assets.redBtnPressed, 90.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
            addLabel(noBtnText, 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, noBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        }
        addButton(closeEvent, Assets.closeBtn, Assets.closeBtnPress, 200.0f, 256.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
    }

    public void setWrapWidth(float wrapWidth) {
        this.messageTextElement.setWrapWidth(wrapWidth);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog, com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.dialogTimeOut > 0) {
            this.elapsedTime += deltaTime;
            if (this.elapsedTime >= this.dialogTimeOut) {
                this.listener.onUIEvent(this.closeEvent);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void present(SpriteBatch batcher) {
        super.present(batcher);
    }
}
