package com.gaiaonline.monstergalaxy.battle.dialer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayedRunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class LinealAttackMeter extends AttackMeter {
    private float damageMultiplier;
    private boolean isRunning;
    private TextureElement maxArrow;
    private TextureElement meter;
    private int meterStruckStrength;
    private float meterTextureTime;
    private Image pointer;
    private float pointerSpeed;
    private ScreenListener screenListener;
    private Stage stage;
    private TextureElement strengthIndicator;
    private Trainer trainer = Game.getTrainer();

    public LinealAttackMeter(ScreenListener screenListener, Stage stage) {
        this.screenListener = screenListener;
        this.stage = stage;
        setSize(480.0f, 74.0f);
        setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 0.0f);
        float scaleFactor = ResolutionManager.getScaleFactor();
        float width = Gdx.graphics.getWidth() / scaleFactor;
        this.meter = add(Assets.loadTexture("attack.strength.meter"), 0.0f, 0.0f, width, 74.0f, ScreenElement.RelPoint.LEFT_BOTTOM, null, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.maxArrow = add(Assets.loadLocalizedTexture("tap.to.attack"), 355.0f, 50.0f, ScreenElement.RelPoint.LEFT_BOTTOM, (ScreenElement) null, ScreenElement.RelPoint.CENTER);
        TextureRegion pointerTexture = Assets.loadTexture("black.arrow");
        this.pointer = new Image(pointerTexture);
        this.pointer.setWidth(pointerTexture.getRegionWidth() * scaleFactor);
        this.pointer.setHeight(pointerTexture.getRegionHeight() * scaleFactor);
        this.strengthIndicator = add(Assets.loadLocalizedTexture("perfect"), 135.0f, 185.0f, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.strengthIndicator.setVisible(false);
        this.pointerSpeed = getPointerSpeed();
    }

    private void addPointer() {
        this.pointer.setX(-50.0f);
        this.pointer.setY(0.0f);
        MoveToAction move = new MoveToAction();
        move.setPosition(Gdx.graphics.getWidth(), 0.0f);
        Interpolation interpolation = new Interpolation.ElasticIn(1.0f, 1.1f);
        move.setInterpolation(interpolation);
        move.setDuration(this.pointerSpeed);
        RunnableAction runnableAction = new DelayedRunnableAction();
        runnableAction.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.dialer.LinealAttackMeter.1
            @Override // java.lang.Runnable
            public void run() {
                LinealAttackMeter.this.applyTouch();
                LinealAttackMeter.this.onDialerFinished();
            }
        });
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(move);
        sequence.addAction(runnableAction);
        this.pointer.addAction(sequence);
        this.stage.addActor(this.pointer);
    }

    private float getPointerSpeed() {
        int encounterId = Game.getTrainer().getCurrentNode().getEncounter().getId();
        if (encounterId != 456 && encounterId != 458 && encounterId != 460 && encounterId != 462 && encounterId != 502 && encounterId != 504) {
            return 1.0f;
        }
        return 1.5f;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialer.AttackMeter
    public void start() {
        this.isRunning = true;
        this.visible = true;
        this.damageMultiplier = 0.0f;
        this.pointer.setVisible(true);
        this.meter.setVisible(true);
        this.maxArrow.setVisible(true);
        addPointer();
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialer.AttackMeter
    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void present(Vector2 origin, SpriteBatch batcher) {
        super.present(origin, batcher);
        if (this.pointer.isVisible()) {
            this.pointer.draw(batcher, 1.0f);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        if (this.isRunning && Gdx.input.isTouched()) {
            applyTouch();
        }
        if (this.meterTextureTime > 0.5f) {
            onDialerFinished();
        }
        if (this.meterTextureTime < 0.5f && (this.meterStruckStrength == 1 || this.meterStruckStrength == 2)) {
            this.strengthIndicator.setVisible(true);
        }
        if (this.meterStruckStrength == 1 || this.meterStruckStrength == 2) {
            this.meterTextureTime += deltaTime;
            if (this.meterTextureTime > 0.5f) {
                onDialerFinished();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDialerFinished() {
        this.meterStruckStrength = 0;
        this.meterTextureTime = 0.0f;
        this.strengthIndicator.setVisible(false);
        this.screenListener.onUIEvent(UIEvent.ATTACK_DIALER_PRESSED);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        this.meter = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTouch() {
        float scaleFactor = ResolutionManager.getScaleFactor();
        float kMGAttackPowerMeterPosition = 460.0f * scaleFactor;
        float kMGAttackPowerTagPosition = 355.0f * scaleFactor;
        this.maxArrow.setVisible(false);
        this.pointer.remove();
        this.meter.setVisible(false);
        this.pointer.setVisible(false);
        this.pointer.clearActions();
        float width = Gdx.graphics.getWidth();
        float limit1 = width * 0.1f;
        float limit2 = width * 0.6f;
        if (this.pointer.getX() < limit1) {
            this.damageMultiplier = (this.pointer.getX() + kMGAttackPowerMeterPosition) / kMGAttackPowerMeterPosition;
            this.damageMultiplier *= 0.09f;
            this.damageMultiplier += 0.01f;
        } else if (this.pointer.getX() < limit2) {
            this.damageMultiplier = this.pointer.getX() / (kMGAttackPowerTagPosition - 40.0f);
            this.damageMultiplier *= 0.9f;
            this.damageMultiplier += 0.1f;
        } else {
            this.damageMultiplier = ((kMGAttackPowerMeterPosition - this.pointer.getX()) / 140.0f) * scaleFactor;
        }
        if (1.0f - this.damageMultiplier < Constants.physicalPerfectRange) {
            this.meterStruckStrength = 1;
            this.damageMultiplier = Constants.physicalPerfectMultiplier;
            this.trainer.getBattleReport().setScoredPerfect(true);
            this.strengthIndicator.setTexture(Assets.loadLocalizedTexture("perfect"));
            return;
        }
        if (this.damageMultiplier > Constants.accuracyAllGreen) {
            this.meterStruckStrength = 2;
            this.trainer.getBattleReport().setScoredVeryGood(true);
            this.strengthIndicator.setTexture(Assets.loadLocalizedTexture("verygood"));
        } else {
            this.meterStruckStrength = 0;
            this.trainer.getBattleReport().setScoredNormal(true);
            this.meterTextureTime = 1.5f;
        }
    }
}
