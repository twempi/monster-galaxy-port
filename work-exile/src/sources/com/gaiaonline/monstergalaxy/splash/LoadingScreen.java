package com.gaiaonline.monstergalaxy.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.JumpBy;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LoadingScreen extends Screen {
    private Texture loadingTexture = new Texture(Gdx.files.internal("data/loading.png"));

    public LoadingScreen() {
        this.root.addBackground(this.loadingTexture);
        this.root.addLabel("Loading...", 0.0f, -80.0f, ScreenElement.RelPoint.CENTER, 1.0f, Color.WHITE, true, true);
        Actor seed = ResolutionManager.getScaledImage("loading_seed", Assets.starSeed);
        seed.x = (Gdx.graphics.getWidth() / 2) - (seed.width / 2.0f);
        seed.y = (Gdx.graphics.getHeight() / 2) - (seed.height / 2.0f);
        seed.action(Forever.$(JumpBy.$(0.0f, 0.0f, 40.0f, 1, 1.7f)));
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage.addActor(seed);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float delta) {
        super.update(delta);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void dispose() {
        super.dispose();
        if (this.loadingTexture != null) {
            this.loadingTexture.dispose();
        }
    }
}
