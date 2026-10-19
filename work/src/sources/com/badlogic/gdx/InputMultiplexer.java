package com.badlogic.gdx;

import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class InputMultiplexer implements InputProcessor {
    private Array<InputProcessor> processors = new Array<>(4);

    public InputMultiplexer() {
    }

    public InputMultiplexer(InputProcessor... processors) {
        for (InputProcessor inputProcessor : processors) {
            this.processors.add(inputProcessor);
        }
    }

    public void addProcessor(int index, InputProcessor processor) {
        this.processors.insert(index, processor);
    }

    public void removeProcessor(int index) {
        this.processors.removeIndex(index);
    }

    public void addProcessor(InputProcessor processor) {
        this.processors.add(processor);
    }

    public void removeProcessor(InputProcessor processor) {
        this.processors.removeValue(processor, true);
    }

    public int size() {
        return this.processors.size;
    }

    public void clear() {
        this.processors.clear();
    }

    public void setProcessors(Array<InputProcessor> processors) {
        this.processors = processors;
    }

    public Array<InputProcessor> getProcessors() {
        return this.processors;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keycode) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).keyDown(keycode)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keycode) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).keyUp(keycode)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).keyTyped(character)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).touchDragged(screenX, screenY, pointer)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean mouseMoved(int screenX, int screenY) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).mouseMoved(screenX, screenY)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        int n = this.processors.size;
        for (int i = 0; i < n; i++) {
            if (this.processors.get(i).scrolled(amount)) {
                return true;
            }
        }
        return false;
    }
}
