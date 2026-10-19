package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/* JADX INFO: loaded from: classes.dex */
public abstract class ButtonUI extends ScreenGroup {
    protected ScreenListener screenListener;

    public ButtonUI() {
    }

    public ButtonUI(ScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public ButtonElement addButton(UIEvent eventCode, TextureRegion normal, TextureRegion selected, float x, float y) {
        return addButton(eventCode, normal, selected, (TextureRegion) null, x, y, ScreenElement.RelPoint.CENTER);
    }

    public ButtonElement addButton(UIEvent eventCode, TextureRegion normal, TextureRegion selected, float x, float y, ScreenElement.RelPoint reference) {
        return addButton(eventCode, normal, selected, (TextureRegion) null, x, y, reference);
    }

    public ButtonElement addButton(UIEvent eventCode, TextureRegion normal, TextureRegion selected, TextureRegion disabled, float x, float y, ScreenElement.RelPoint reference) {
        ButtonElement button = new ButtonElement(this.screenListener, eventCode, normal, selected, disabled);
        button.setSize(normal.getRegionWidth(), normal.getRegionHeight());
        button.setPosition(reference, x, y);
        add(button);
        return button;
    }

    public void setScreenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public ButtonElement addButton(UIEvent eventCode, TextureRegion texture, float x, float y, float width, float height, ScreenElement.RelPoint reference) {
        ButtonElement button = new ButtonElement(this.screenListener, eventCode, texture);
        button.setSize(width, height);
        button.setPosition(reference, x, y);
        button.visible = true;
        add(button);
        return button;
    }
}
