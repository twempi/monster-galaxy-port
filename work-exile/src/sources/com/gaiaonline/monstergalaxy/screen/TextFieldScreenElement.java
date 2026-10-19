package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextFieldScreenElement extends ActorScreenElement implements InputProcessor {
    private static final int NINEPATCH_CORNER_SIZE = 12;
    TextField textField;

    public TextFieldScreenElement(float prefWidth) {
        NinePatch bg = getNinePatch(Assets.loadTexture("name.bg"), 12, 12, 12, 12);
        NinePatch cursor = getNinePatch(Assets.loadTexture("cursor"), 3, 3, 1, 1);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle(Fonts.regular, Color.WHITE, cursor, Assets.loadTexture("name.bg"), bg);
        this.textField = new TextField("", (prefWidth - 24.0f) * ResolutionManager.getHorizontalAR(), style);
        setActor(this.textField);
        this.dummyGroup.keyboardFocusedActor = this.actor;
    }

    private NinePatch getNinePatch(TextureRegion region, int left, int right, int top, int bottom) {
        TextureRegion topLeftPatch = new TextureRegion(region.getTexture(), region.getRegionX(), region.getRegionY(), left, top);
        TextureRegion topCenterPatch = new TextureRegion(region.getTexture(), region.getRegionX() + left, region.getRegionY(), (region.getRegionWidth() - left) - right, top);
        TextureRegion topRightPatch = new TextureRegion(region.getTexture(), (region.getRegionX() + region.getRegionWidth()) - right, region.getRegionY(), right, top);
        TextureRegion middleLeftPatch = new TextureRegion(region.getTexture(), region.getRegionX(), region.getRegionY() + top, left, (region.getRegionHeight() - top) - bottom);
        TextureRegion middleCenterPatch = new TextureRegion(region.getTexture(), region.getRegionX() + left, region.getRegionY() + top, (region.getRegionWidth() - left) - right, (region.getRegionHeight() - top) - bottom);
        TextureRegion middleRightPatch = new TextureRegion(region.getTexture(), (region.getRegionX() + region.getRegionWidth()) - right, region.getRegionY() + top, right, (region.getRegionHeight() - top) - bottom);
        TextureRegion bottomLeftPatch = new TextureRegion(region.getTexture(), region.getRegionX(), (region.getRegionY() + region.getRegionHeight()) - bottom, left, bottom);
        TextureRegion bottomCenterPatch = new TextureRegion(region.getTexture(), region.getRegionX() + left, (region.getRegionY() + region.getRegionHeight()) - bottom, (region.getRegionWidth() - left) - right, bottom);
        TextureRegion bottomRightPatch = new TextureRegion(region.getTexture(), (region.getRegionX() + region.getRegionWidth()) - right, (region.getRegionY() + region.getRegionHeight()) - bottom, right, bottom);
        return new NinePatch(topLeftPatch, topCenterPatch, topRightPatch, middleLeftPatch, middleCenterPatch, middleRightPatch, bottomLeftPatch, bottomCenterPatch, bottomRightPatch);
    }

    public String getText() {
        return this.textField.getText();
    }

    public void setText(String text) {
        this.textField.setText(text);
        this.textField.keyDown(132);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keycode) {
        return this.textField.keyDown(keycode);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keycode) {
        return this.textField.keyUp(keycode);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        return this.textField.keyTyped(character);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        return this.textField.touchDragged(x, y, pointer);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchMoved(int x, int y) {
        return this.textField.touchMoved(x, y);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        return this.textField.scrolled(amount);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }
}
