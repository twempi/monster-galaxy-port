package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.battle.dialogs.QuitGameDialog;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.Root;

/* JADX INFO: loaded from: classes.dex */
public abstract class Screen {
    protected SpriteBatch batcher;
    private float deltaTimeSinceResume;
    protected Dialog dialog;
    public boolean dispose;
    protected Effect effect;
    private int frameNumberSinceResume;
    private int resumeCount;
    protected Root root;
    protected boolean showingQuitDialog;
    protected Stage stage;
    protected boolean wasTouched;
    protected Vector2 startTouchPoint = new Vector2();
    protected Vector2 lastTouchPoint = new Vector2();
    protected OrthographicCamera guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    public Screen() {
        this.guiCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0.0f);
        this.guiCam.update();
        this.batcher = new SpriteBatch();
        this.batcher.setProjectionMatrix(this.guiCam.combined);
        this.root = new Root();
    }

    public ScreenCode getCode() {
        return ScreenFactory.getScreenClassScreenCode(getClass());
    }

    public void dispose() {
        this.effect = null;
        this.root = null;
        this.stage = null;
        EffectManager.getInstance().removeParticles();
    }

    public void update(float deltaTime) {
        this.frameNumberSinceResume++;
        this.deltaTimeSinceResume += deltaTime;
        boolean touchJustReleased = this.wasTouched && !Gdx.input.isTouched();
        if (Gdx.input.justTouched()) {
            updateTouchPoint(this.startTouchPoint);
            if (this.dialog == null) {
                this.root.touched(this.startTouchPoint);
            } else {
                this.dialog.touched(this.startTouchPoint);
            }
        } else if (touchJustReleased) {
            updateTouchPoint(this.lastTouchPoint);
            if (this.dialog == null) {
                this.root.touchReleased(this.startTouchPoint, this.lastTouchPoint);
            } else {
                this.dialog.touchReleased(this.startTouchPoint, this.lastTouchPoint);
            }
        } else if (this.dialog == null) {
            this.root.update(deltaTime);
            if (this.stage != null) {
                this.stage.act(deltaTime);
            }
        } else if (this.dialog.isKeepingAnimation()) {
            this.root.update(deltaTime);
            if (this.stage != null) {
                this.stage.act(deltaTime);
            }
        }
        this.wasTouched = Gdx.input.isTouched();
    }

    private void updateTouchPoint(Vector2 touchPoint) {
        touchPoint.x = Gdx.input.getX();
        touchPoint.y = Gdx.graphics.getHeight() - Gdx.input.getY();
    }

    public void present(float deltaTime) {
        this.root.drawBackground(this.batcher);
        if (this.stage != null) {
            this.stage.draw();
        }
        this.root.present(this.batcher);
        if (this.effect != null) {
            this.batcher.begin();
            this.effect.draw(this.batcher, deltaTime);
            this.batcher.end();
        }
        this.root.drawEffects(this.batcher);
        if (this.dialog != null) {
            this.dialog.present(this.batcher);
        }
    }

    public void present(float deltaTime, ScreenTransition transition) {
        present(deltaTime);
        this.batcher.begin();
        transition.render(this.batcher);
        this.batcher.end();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showDialog(Dialog dialog) {
        this.dialog = dialog;
        dialog.show();
    }

    protected void showQuitDialog() {
        this.showingQuitDialog = true;
        showDialog(new QuitGameDialog(this));
    }

    public void hideDialog() {
        if (this.dialog != null) {
            this.showingQuitDialog = false;
            this.dialog.hide();
            this.dialog = null;
        }
    }

    public void onMenuKeyPressed() {
    }

    public void onBackKeyPressed() {
        if (this.dialog != null) {
            hideDialog();
        } else {
            MonsterGalaxy.showPreviousScreen();
        }
    }

    public final void applicationPause() {
        onApplicationPause();
    }

    public void onApplicationPause() {
    }

    public final void applicationResume() {
        onApplicationResume();
    }

    public void onApplicationResume() {
    }

    public final void pause() {
        onPause();
    }

    public void onPause() {
    }

    public final void enter(Screen prevScreen) {
        onEnter(prevScreen);
    }

    public void onEnter(Screen prevScreen) {
    }

    public final void exit(Screen nextScreen) {
        onExit(nextScreen);
    }

    public void onExit(Screen nextScreen) {
    }

    public final void resume() {
        this.resumeCount++;
        this.frameNumberSinceResume = 0;
        this.deltaTimeSinceResume = 0.0f;
        onResume();
    }

    public void onResume() {
    }

    public int getFrameNumberSinceResume() {
        return this.frameNumberSinceResume;
    }

    public float getDeltaTimeSinceResume() {
        return this.deltaTimeSinceResume;
    }

    public int getResumeCount() {
        return this.resumeCount;
    }
}
