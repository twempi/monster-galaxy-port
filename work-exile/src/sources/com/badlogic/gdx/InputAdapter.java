package com.badlogic.gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class InputAdapter implements InputProcessor {
    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        return false;
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
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchMoved(int x, int y) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        return false;
    }
}
