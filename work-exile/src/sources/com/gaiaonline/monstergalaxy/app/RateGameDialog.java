package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class RateGameDialog extends Dialog {
    private static final float MAX_MESSAGE_WIDTH = 335.0f;
    private TextElement messageTextElement;

    public RateGameDialog(ScreenListener listener) {
        super(listener, true);
        TextureElement bg = add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f);
        this.messageTextElement = new TextElement("Do you like this game?", 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.messageTextElement.setWrapWidth(MAX_MESSAGE_WIDTH);
        this.messageTextElement.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.messageTextElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        this.messageTextElement.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -15.0f);
        this.messageTextElement.setAlign(bg);
        add(this.messageTextElement);
        ButtonElement noBtn = addButton(UIEvent.CANCEL_BUTTON, Assets.redBtnSmall, Assets.redBtnSmallPressed, -70.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("No", 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, noBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        TextureRegion normalButton = Assets.loadTexture("green.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("green.btn.small.press");
        ButtonElement yesBtn = addButton(UIEvent.ACCEPT_BUTTON, normalButton, selectedButton, 80.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("Yes", 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, yesBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
    }

    public void setWrapWidth(float wrapWidth) {
        this.messageTextElement.setWrapWidth(wrapWidth);
    }
}
