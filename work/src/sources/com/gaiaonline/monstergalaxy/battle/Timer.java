package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.screen.PieMeshElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.TextElement;

/* JADX INFO: loaded from: classes.dex */
public class Timer extends ScreenGroup {
    private CompleteListener completeListener;
    private boolean completed = true;
    private float elapsedTime;
    private int lastSecondsShown;
    private final PieMeshElement pieMesh;
    private final TextElement timerLabel;
    private int timerSeconds;

    public interface CompleteListener {
        void onTimerComplete();
    }

    public Timer(int seconds) {
        this.timerSeconds = seconds;
        addBackground(Assets.loadTexture("timer.background"));
        FileHandle imageFileHandle = Gdx.files.internal("data/timer.full.png");
        this.pieMesh = new PieMeshElement(144.0f, new Texture(imageFileHandle));
        this.pieMesh.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        add(this.pieMesh);
        this.timerLabel = addLabel(String.valueOf(seconds), 0.0f, 0.0f, 1.5f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setOnCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public void start() {
        this.completed = false;
        this.elapsedTime = 0.0f;
        this.visible = true;
    }

    public void stop() {
        this.completed = false;
        this.elapsedTime = 0.0f;
        this.visible = false;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        if (!this.completed) {
            this.elapsedTime += deltaTime;
            if (this.elapsedTime > this.timerSeconds) {
                this.completed = true;
                if (this.completeListener != null) {
                    this.completeListener.onTimerComplete();
                }
            }
            this.pieMesh.setCompletionRate(1.0f - (this.elapsedTime / this.timerSeconds));
            int seconds = this.timerSeconds - ((int) this.elapsedTime);
            if (seconds != this.lastSecondsShown) {
                this.timerLabel.setText(String.valueOf(seconds));
                this.lastSecondsShown = seconds;
            }
        }
    }
}
