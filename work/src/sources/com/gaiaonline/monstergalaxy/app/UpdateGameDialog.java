package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class UpdateGameDialog extends Dialog {
    private static final float MAX_MESSAGE_HEIGHT = 95.0f;
    private static final float MAX_MESSAGE_WIDTH = 335.0f;
    private TextElement messageTextElement;

    public UpdateGameDialog(ScreenListener listener) {
        super(listener, true);
        TextureElement bg = add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f);
        this.messageTextElement = new TextElement(I18nManager.getText(I18nManager.I18nKey.UPDATE_AVAILABLE), 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.messageTextElement.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.messageTextElement.setWrapWidth(MAX_MESSAGE_WIDTH);
        this.messageTextElement.enableVerticalAutoSize(MAX_MESSAGE_HEIGHT);
        this.messageTextElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        this.messageTextElement.setPosition(ScreenElement.RelPoint.CENTER_TOP, -10.0f, -15.0f);
        this.messageTextElement.setAlign(bg);
        add(this.messageTextElement);
        ButtonElement yesBtn = addButton(UIEvent.OK_BUTTON, Assets.greenBtn, Assets.greenBtnPress, -80.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        TextElement yesLabel = addLabel(I18nManager.getText(I18nManager.I18nKey.UPDATE_YES), 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, yesBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        yesLabel.enableHorizontalAutoSize(130.0f);
        ButtonElement noBtn = addButton(UIEvent.CANCEL_BUTTON, Assets.greenBtn, Assets.greenBtnPress, 90.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        TextElement noLabel = addLabel(I18nManager.getText(I18nManager.I18nKey.UPDATE_NO), 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, noBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        noLabel.enableHorizontalAutoSize(130.0f);
    }

    public void setWrapWidth(float wrapWidth) {
        this.messageTextElement.setWrapWidth(wrapWidth);
    }
}
