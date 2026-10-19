package com.badlogic.gdx;

import java.util.ArrayList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class InputMultiplexer implements InputProcessor {
    private ArrayList<InputProcessor> processors = new ArrayList<>(4);

    public InputMultiplexer() {
    }

    public InputMultiplexer(InputProcessor... processors) {
        for (InputProcessor inputProcessor : processors) {
            this.processors.add(inputProcessor);
        }
    }

    public void addProcessor(InputProcessor processor) {
        this.processors.add(processor);
    }

    public void removeProcessor(InputProcessor processor) {
        this.processors.remove(processor);
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keycode) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).keyDown(keycode)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keycode) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).keyUp(keycode)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).keyTyped(character)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int button) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchDown(x, y, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int button) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchUp(x, y, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchDragged(x, y, pointer)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchMoved(int x, int y) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchMoved(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        int n = this.processors.size();
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).scrolled(amount)) {
                return true;
            }
        }
        return false;
    }
}
