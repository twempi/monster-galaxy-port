package com.badlogic.gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Game implements ApplicationListener {
    private Screen screen;

    @Override // com.badlogic.gdx.ApplicationListener
    public void dispose() {
        if (this.screen != null) {
            this.screen.hide();
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void pause() {
        if (this.screen != null) {
            this.screen.pause();
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void resume() {
        if (this.screen != null) {
            this.screen.resume();
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void render() {
        if (this.screen != null) {
            this.screen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void resize(int width, int height) {
        if (this.screen != null) {
            this.screen.resize(width, height);
        }
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.hide();
        }
        this.screen = screen;
        screen.show();
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Screen getScreen() {
        return this.screen;
    }
}
