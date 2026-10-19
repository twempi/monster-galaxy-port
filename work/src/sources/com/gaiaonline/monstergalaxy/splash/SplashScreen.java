package com.gaiaonline.monstergalaxy.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;

/* JADX INFO: loaded from: classes.dex */
public class SplashScreen extends Screen {
    int height;
    int width;
    private boolean splashShown = false;
    private Texture splash = new Texture(Gdx.files.internal("data/splash.png"));

    public SplashScreen() {
        this.height = Gdx.graphics.getHeight();
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.width = Gdx.graphics.getWidth();
        this.splash.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.batcher.disableBlending();
        this.batcher.begin();
        this.batcher.draw(this.splash, 0.0f, 0.0f, this.width, this.height, 0, 0, 960, 640, false, false);
        this.batcher.end();
        this.batcher.enableBlending();
        if (Game.getTrainer().getName() == null) {
            Game.getAnalytics().trackEvent("acquisition", "", "", "splash_screen", "", "");
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void present(float delta) {
        this.batcher.disableBlending();
        this.batcher.begin();
        this.batcher.draw(this.splash, 0.0f, 0.0f, this.width, this.height, 0, 0, 960, 640, false, false);
        this.batcher.end();
        this.batcher.enableBlending();
        if (this.splashShown) {
            Game.initialize();
            MonsterGalaxy.gameInitialized();
        }
        this.splashShown = true;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void dispose() {
    }
}
