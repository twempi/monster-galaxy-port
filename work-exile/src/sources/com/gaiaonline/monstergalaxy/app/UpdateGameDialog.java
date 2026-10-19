package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class UpdateGameDialog extends Dialog {
    private static final float MAX_MESSAGE_WIDTH = 335.0f;
    private TextElement messageTextElement;

    public UpdateGameDialog(ScreenListener listener) {
        super(listener, true);
        TextureElement bg = add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f);
        this.messageTextElement = new TextElement("Hey Tamer! An update for Monster Galaxy is now available! Would you like to get it now?", 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.messageTextElement.setWrapWidth(MAX_MESSAGE_WIDTH);
        this.messageTextElement.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.messageTextElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        this.messageTextElement.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -15.0f);
        this.messageTextElement.setAlign(bg);
        add(this.messageTextElement);
        ButtonElement yesBtn = addButton(UIEvent.OK_BUTTON, Assets.greenBtn, Assets.greenBtnPress, -80.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("Yes, update now!", 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, yesBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        ButtonElement noBtn = addButton(UIEvent.CANCEL_BUTTON, Assets.greenBtn, Assets.greenBtnPress, 90.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel("Not now, laterz", 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, noBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
    }

    public void setWrapWidth(float wrapWidth) {
        this.messageTextElement.setWrapWidth(wrapWidth);
    }
}
