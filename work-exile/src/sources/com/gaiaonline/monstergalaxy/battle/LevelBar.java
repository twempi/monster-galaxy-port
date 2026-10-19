package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LevelBar extends Group {
    int actualLevel;
    int maxLevel;
    private ProgressBarImage progressImage;
    ProgressTo progressaction;

    public LevelBar(String name, int initialProgrees, int endProgress, int actuallevel, int maxlevel, float delayTime) {
        super(name);
        this.actualLevel = actuallevel;
        this.maxLevel = maxlevel;
        Image aImage1 = new Image("aImage1", Assets.loadTexture("empty.xp.bar"));
        aImage1.width *= ResolutionManager.getHorizontalAR();
        aImage1.height *= ResolutionManager.getScaleFactor();
        addActor(aImage1);
        if (actuallevel == this.maxLevel || actuallevel > maxlevel) {
            showMaxLevel();
            return;
        }
        this.progressImage = new ProgressBarImage("aImage2", Assets.loadTexture("full.xp.bar"));
        this.progressImage.width *= ResolutionManager.getHorizontalAR();
        this.progressImage.height *= ResolutionManager.getScaleFactor();
        this.progressImage.y = (this.progressImage.height / 2.0f) - 1.5f;
        this.progressImage.x = ResolutionManager.getHorizontalAR() * 2.0f;
        this.progressaction = new ProgressTo(initialProgrees, endProgress);
        action(Delay.$(this.progressaction, delayTime));
        addActor(this.progressImage);
    }

    public void restartAction(int initialProgrees, int endProgress, int newLevel) {
        if (newLevel < this.maxLevel) {
            this.progressaction = new ProgressTo(initialProgrees, endProgress);
            action(this.progressaction);
        } else {
            showMaxLevel();
        }
    }

    public void showMaxLevel() {
        if (this.progressImage != null) {
            this.progressImage.region = Assets.loadTexture("full.xp.bar.yellow");
        } else {
            this.progressImage = new ProgressBarImage("aImage2", Assets.loadTexture("full.xp.bar.yellow"));
            addActor(this.progressImage);
            this.progressImage.width *= ResolutionManager.getHorizontalAR();
            this.progressImage.height *= ResolutionManager.getScaleFactor();
            this.progressImage.y = (this.progressImage.height / 2.0f) - 1.5f;
            this.progressImage.x = ResolutionManager.getHorizontalAR() * 2.0f;
        }
        BitmapFont aBmpFont = Fonts.italic;
        aBmpFont.setScale(0.9f);
        Label max = new Label("aText2", aBmpFont, "MAX");
        max.x = (this.progressImage.width / 2.0f) - (max.width / 2.0f);
        max.y = (this.progressImage.height / 2.0f) - 1.5f;
        this.progressImage.setProgress(1.0f);
        addActorAfter(this.progressImage, max);
    }

    public void setOnConpletionListener(OnActionCompleted listener) {
        if (this.progressaction != null) {
            this.progressaction.setCompletionListener(listener);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        return null;
    }

    class ProgressTo extends AnimationAction {
        float currentPercent;
        int finalPercent;
        int initialPercent;
        protected LevelBar theBar;
        final float totalTime = 0.5f;
        float accTime = 0.0f;

        public ProgressTo(int initialPercent, int finalPercent) {
            this.initialPercent = initialPercent;
            this.currentPercent = initialPercent;
            this.finalPercent = finalPercent;
            this.done = false;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Action
        public void setTarget(Actor actor) {
            this.target = actor;
            this.theBar = (LevelBar) this.target;
            this.theBar.progressImage.setProgress(this.initialPercent / 100.0f);
        }

        @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
        public Actor getTarget() {
            return this.target;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Action
        public void act(float delta) {
            if (!this.done) {
                this.accTime += delta;
                this.currentPercent = this.initialPercent + (((this.finalPercent - this.initialPercent) * this.accTime) / 0.5f);
                this.done = this.currentPercent >= ((float) this.finalPercent);
                if (this.done) {
                    this.currentPercent = this.finalPercent;
                    if (LevelBar.this.maxLevel == LevelBar.this.actualLevel) {
                        LevelBar.this.showMaxLevel();
                    }
                }
                this.theBar.progressImage.setProgress(this.currentPercent / 100.0f);
            }
        }

        @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
        public boolean isDone() {
            return this.done;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Action
        public Action copy() {
            return null;
        }
    }
}
