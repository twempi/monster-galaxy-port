package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;

/* JADX INFO: loaded from: classes.dex */
public class LevelBar extends Group {
    private int actualLevel;
    private int maxLevel;
    private ProgressBarImage progressImage = new ProgressBarImage("aImage2", Assets.loadTexture("full.xp.bar"));
    private ProgressTo progressaction;

    public LevelBar(String name, int initialProgrees, int endProgress, int actuallevel, int maxlevel, float delayTime) {
        this.actualLevel = actuallevel;
        this.maxLevel = maxlevel;
        this.progressImage.setWidth(this.progressImage.getWidth() * ResolutionManager.getHorizontalAR());
        this.progressImage.setHeight(this.progressImage.getHeight() * ResolutionManager.getScaleFactor());
        this.progressImage.setY((this.progressImage.getHeight() / 2.0f) - 1.5f);
        this.progressImage.setX(ResolutionManager.getHorizontalAR() * 2.0f);
        Actor aImage1 = new Image(Assets.loadTexture("empty.xp.bar"));
        aImage1.setWidth(aImage1.getWidth() * ResolutionManager.getHorizontalAR());
        aImage1.setHeight(aImage1.getHeight() * ResolutionManager.getScaleFactor());
        addActor(aImage1);
        if (actuallevel == this.maxLevel || actuallevel > maxlevel) {
            showMaxLevel();
            return;
        }
        this.progressaction = new ProgressTo(initialProgrees, endProgress);
        DelayAction delay = new DelayAction();
        delay.setDuration(delayTime);
        delay.setAction(this.progressaction);
        addActor(this.progressImage);
        addAction(delay);
    }

    public void restartAction(int initialProgrees, int endProgress, int newLevel) {
        if (newLevel < this.maxLevel) {
            this.progressaction = new ProgressTo(initialProgrees, endProgress);
            addAction(this.progressaction);
        } else {
            showMaxLevel();
        }
    }

    public void showMaxLevel() {
        this.progressImage.setRegion(Assets.loadTexture("full.xp.bar.yellow"));
        AutoFontLabel max = new AutoFontLabel("MAX", 0.9f, Color.WHITE, true);
        max.setX((this.progressImage.getWidth() / 2.0f) - (max.getWidth() / 2.0f));
        max.setY((this.progressImage.getHeight() / 2.0f) - 1.5f);
        this.progressImage.setProgress(1.0f);
        addActorAfter(this.progressImage, max);
    }

    public void setOnConpletionListener(Runnable listener) {
        if (this.progressaction != null) {
            this.progressaction.setOnComplete(listener);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    class ProgressTo extends TemporalAction {
        float currentPercent;
        int finalPercent;
        int initialPercent;
        private Runnable onComplete;
        protected LevelBar theBar;
        final float totalTime = 0.5f;
        float accTime = 0.0f;
        private boolean done = false;

        public ProgressTo(int initialPercent, int finalPercent) {
            this.initialPercent = initialPercent;
            this.currentPercent = initialPercent;
            this.finalPercent = finalPercent;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Action
        public void setActor(Actor actor) {
            if (actor != null) {
                this.theBar = (LevelBar) actor;
                this.theBar.progressImage.setProgress(this.initialPercent / 100.0f);
            }
        }

        @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
        protected void update(float delta) {
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
                if (this.done) {
                    end();
                }
            }
        }

        @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
        protected void begin() {
            super.begin();
            this.accTime = 0.0f;
        }

        @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
        protected void end() {
            super.end();
            if (this.onComplete != null) {
                this.onComplete.run();
            }
        }

        public void setOnComplete(Runnable onComplete) {
            this.onComplete = onComplete;
        }

        public boolean isDone() {
            return this.done;
        }
    }
}
