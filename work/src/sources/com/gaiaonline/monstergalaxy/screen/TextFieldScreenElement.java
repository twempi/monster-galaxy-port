package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;

/* JADX INFO: loaded from: classes.dex */
public class TextFieldScreenElement extends ActorScreenElement implements InputProcessor {
    private static final int NINEPATCH_CORNER_SIZE = 12;
    TextField textField;

    private class StageWrapper extends Stage {
        Actor actor;

        private StageWrapper() {
        }

        /* synthetic */ StageWrapper(TextFieldScreenElement textFieldScreenElement, StageWrapper stageWrapper) {
            this();
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Stage
        public void setKeyboardFocus(Actor actor) {
            this.actor = actor;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Stage
        public Actor getKeyboardFocus() {
            return this.actor;
        }
    }

    private class TextFieldWrapper extends TextField {
        Stage stage;

        public TextFieldWrapper(String s, TextField.TextFieldStyle style) {
            super(s, style);
            this.stage = new StageWrapper(TextFieldScreenElement.this, null);
            this.stage.setKeyboardFocus(this);
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Actor
        public Stage getStage() {
            return this.stage;
        }
    }

    public TextFieldScreenElement(float prefWidth) {
        NinePatch bg = getNinePatch(Assets.loadTexture("name.bg"), 12, 12, 12, 12);
        NinePatch cursor = getNinePatch(Assets.loadTexture("cursor"), 3, 3, 1, 1);
        Drawable cursorDrawable = new NinePatchDrawable(cursor);
        Drawable nameBgDrawable = new TextureRegionDrawable(Assets.loadTexture("name.bg"));
        Drawable bgDrawable = new NinePatchDrawable(bg);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle(Fonts.regular, Color.WHITE, cursorDrawable, nameBgDrawable, bgDrawable);
        this.textField = new TextFieldWrapper("", style);
        setActor(this.textField);
    }

    public void setKeyboardFocus() {
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
        InputEvent event = new InputEvent();
        event.setKeyCode(132);
        event.setType(InputEvent.Type.keyDown);
        this.textField.fire(event);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keycode) {
        InputEvent event = new InputEvent();
        event.setKeyCode(keycode);
        event.setType(InputEvent.Type.keyDown);
        return this.textField.fire(event);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keycode) {
        InputEvent event = new InputEvent();
        event.setKeyCode(keycode);
        event.setType(InputEvent.Type.keyUp);
        return this.textField.fire(event);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        InputEvent event = new InputEvent();
        event.setCharacter(character);
        event.setType(InputEvent.Type.keyTyped);
        return this.textField.fire(event);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        InputEvent event = new InputEvent();
        event.setStageX(x);
        event.setStageY(y);
        event.setPointer(pointer);
        event.setType(InputEvent.Type.touchDragged);
        return this.textField.fire(event);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.scrolled);
        event.setScrollAmount(amount);
        return this.textField.fire(event);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean mouseMoved(int arg0, int arg1) {
        return false;
    }
}
