package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public abstract class NameMogaDialog extends Dialog {
    private ButtonElement button;
    private Moga moga;
    private ScreenListener screenListener;

    protected abstract void addMogaNameTextField(int i, int i2, int i3, int i4, Integer num, String str);

    protected abstract String getMogaName();

    protected abstract void setMogaName(String str);

    public NameMogaDialog(ScreenListener screenListener, Moga moga) {
        super(null, true, true);
        this.screenListener = screenListener;
        this.moga = moga;
        ScreenListener okButtonScreenListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                NameMogaDialog.this.okPressed(event);
            }
        };
        TextElement title = addLabel(I18nManager.getText(I18nManager.I18nKey.NAME_YOUR_MOGA), 20.0f, 285.0f, ScreenElement.RelPoint.LEFT_BOTTOM, null, ScreenElement.RelPoint.LEFT_CENTER, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        title.enableHorizontalAutoSize(450.0f);
        addMogaNameTextField(15, 240, 210, 48, Moga.NAME_MAX_CHARS, "");
        setMogaName(moga.getName());
        Vector2 position = new Vector2(280.0f, 240.0f);
        TextureRegion normal = Assets.loadTexture("blue.btn.small");
        TextureRegion selected = Assets.loadTexture("blue.btn.small.press");
        this.button = new ButtonElement(okButtonScreenListener, UIEvent.DONE_BUTTON, normal, selected);
        this.button.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.button.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, position.x, position.y);
        add(this.button);
        TextElement label = new TextElement(I18nManager.getText(I18nManager.I18nKey.DONE), 0.7f, ColorConstants.PRIMARY_FONT_COLOR, true);
        label.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        label.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, position.x, position.y + 5.0f);
        add(label);
    }

    public static NameMogaDialog newInstance(ScreenListener screenListener, Moga moga) {
        return Game.getPlatformHelper().isAndroid() ? new AndroidNameMogaDialog(screenListener, moga) : new DesktopNameMogaDialog(screenListener, moga);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void okPressed(UIEvent event) {
        this.moga.setName(getMogaName());
        this.screenListener.onUIEvent(event);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ButtonUI
    public void setScreenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog, com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
