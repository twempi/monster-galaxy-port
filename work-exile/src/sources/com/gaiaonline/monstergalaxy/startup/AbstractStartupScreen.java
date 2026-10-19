package com.gaiaonline.monstergalaxy.startup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class AbstractStartupScreen extends Screen implements ScreenListener {
    public AbstractStartupScreen() {
        if (!SoundManager.isPlayingMusic()) {
            SoundManager.playMusic(Constants.worldAmbienceAudio);
        }
    }

    protected void addTitle(String title) {
        ShadowTextElement label = new ShadowTextElement(title, 1.0f, Color.WHITE, Color.BLACK, false);
        label.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        label.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -20.0f);
        this.root.add(label);
    }

    protected void addNextButton() {
        addNavigationButton(UIEvent.NEXT_BUTTON, ScreenElement.RelPoint.RIGHT_BOTTOM, new Vector2(-60.0f, 30.0f), "Next");
    }

    protected void addDoneButton() {
        addNavigationButton(UIEvent.NEXT_BUTTON, ScreenElement.RelPoint.LEFT_BOTTOM, new Vector2(280.0f, 240.0f), "Done");
    }

    protected void addBackButton() {
        addNavigationButton(UIEvent.BACK_BUTTON, ScreenElement.RelPoint.LEFT_BOTTOM, new Vector2(70.0f, 30.0f), "Back");
    }

    protected void addFinishButton() {
        addNavigationButton(UIEvent.OK_BUTTON, ScreenElement.RelPoint.RIGHT_BOTTOM, new Vector2(-60.0f, 30.0f), "Finish");
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        SoundManager.playScreenChangeSound();
    }

    private void addNavigationButton(UIEvent eventCode, ScreenElement.RelPoint relPoint, Vector2 position, String text) {
        TextureRegion normal = Assets.loadTexture("blue.btn.small");
        TextureRegion selected = Assets.loadTexture("blue.btn.small.press");
        ButtonElement button = new ButtonElement(this, eventCode, normal, selected);
        button.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        button.setPosition(relPoint, position.x, position.y);
        this.root.add(button);
        TextElement label = new TextElement(text, 0.7f, Color.WHITE, true);
        label.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        label.setPosition(relPoint, position.x, position.y + 5.0f);
        this.root.add(label);
    }
}
