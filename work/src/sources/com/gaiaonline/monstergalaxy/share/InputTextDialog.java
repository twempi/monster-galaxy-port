package com.gaiaonline.monstergalaxy.share;

import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class InputTextDialog extends Dialog implements ScreenListener {
    private static final String FIELD_NAME = "inputText";
    private String defaultText;
    private ScreenListener externalScreenListener;
    private int maxChars;
    private String text;

    public InputTextDialog(ScreenListener listener, String defaultText, String okButtonText, int maxChars) {
        super(null, true);
        this.externalScreenListener = listener;
        this.defaultText = defaultText;
        this.maxChars = maxChars;
        setScreenListener(this);
        add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f);
        ButtonElement okBtn = addButton(UIEvent.ACCEPT_BUTTON, Assets.greenBtnLarge, Assets.greenBtnLargePress, 0.0f, 100.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        addLabel(okButtonText, 0.0f, -30.0f, ScreenElement.RelPoint.CENTER_TOP, okBtn, ScreenElement.RelPoint.CENTER_BOTTOM, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        addButton(UIEvent.CLOSE_BUTTON, Assets.closeBtn, Assets.closeBtnPress, 196.0f, 254.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void show() {
        super.show();
        if (Game.getPlatformHelper().isAndroid()) {
            int x = (int) ((480.0f - 320) / 2.0f);
            float screenX = x * ResolutionManager.getHorizontalAR();
            float screenY = ((320.0f - Opcodes.INVOKEINTERFACE) - 55) * ResolutionManager.getVerticalAR();
            float screenW = 320 * ResolutionManager.getHorizontalAR();
            float screenH = 110 * ResolutionManager.getVerticalAR();
            Game.getAndroidPlatformHelper().addTextField(FIELD_NAME, (int) screenX, (int) screenY, (int) screenW, (int) screenH, true, true, Integer.valueOf(this.maxChars), null, false, false);
            Game.getAndroidPlatformHelper().setTextfieldValue(FIELD_NAME, this.defaultText);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void hide() {
        if (Game.getPlatformHelper().isAndroid()) {
            this.text = Game.getAndroidPlatformHelper().getTextfieldValue(FIELD_NAME);
            Game.getAndroidPlatformHelper().removeTextField(FIELD_NAME);
        }
        super.hide();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (Game.getPlatformHelper().isAndroid()) {
            this.text = Game.getAndroidPlatformHelper().getTextfieldValue(FIELD_NAME);
        }
        this.externalScreenListener.onUIEvent(event);
    }

    public String getText() {
        return this.text;
    }
}
