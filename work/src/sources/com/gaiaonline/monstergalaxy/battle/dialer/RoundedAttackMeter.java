package com.gaiaonline.monstergalaxy.battle.dialer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
public class RoundedAttackMeter extends AttackMeter {
    private static final int INITIAL_ROTATION_ANGLE = -130;
    private static final int RADIUS = 95;
    private float damageMultiplier;
    int frameCount = 0;
    private boolean isRunning;
    private TextureElement meter;
    private float meterAngle;
    private Sprite meterArrow;
    private int meterStruckStrength;
    private float meterTextureTime;
    private float previousMeterAngle;
    private ScreenListener screenListener;
    private float speed;
    private TextureElement strengthLabel;
    private TextureElement tapToAttack;
    private Trainer trainer;
    private static final float SCALE = ResolutionManager.getScaleFactor();
    private static final float HOR_AR = ResolutionManager.getHorizontalAR();

    public RoundedAttackMeter(ScreenListener screenListener) {
        this.screenListener = screenListener;
        setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        setStretchable(true);
        this.speed = getMeterSpeed();
        setSize(480.0f, 320.0f);
        this.trainer = Game.getTrainer();
        this.meter = add(Assets.loadTexture("attack.strength.meter"), 100.0f, 160.0f, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.tapToAttack = add(Assets.loadLocalizedTexture("tap.to.attack"), 200.0f, 160.0f, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.strengthLabel = add(Assets.loadLocalizedTexture("perfect"), 135.0f, 185.0f, ScreenElement.RelPoint.LEFT_BOTTOM);
        this.strengthLabel.setVisible(false);
        this.meterArrow = new Sprite(Assets.loadTexture("black.arrow"));
        this.meterArrow.setSize(this.meterArrow.getWidth() * SCALE, this.meterArrow.getHeight() * SCALE);
        this.previousMeterAngle = -130.0f;
        this.meterAngle = -130.0f;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialer.AttackMeter
    public void start() {
        this.isRunning = true;
        this.visible = true;
        this.damageMultiplier = 0.0f;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialer.AttackMeter
    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    private void onDialerFinished() {
        this.meterStruckStrength = 0;
        this.meterTextureTime = 0.0f;
        this.strengthLabel.setVisible(false);
        this.meter.setVisible(true);
        this.tapToAttack.setVisible(true);
        this.previousMeterAngle = -130.0f;
        this.meterAngle = -130.0f;
        this.screenListener.onUIEvent(UIEvent.ATTACK_DIALER_PRESSED);
    }

    private float getMeterSpeed() {
        int encounterId = Game.getTrainer().getCurrentNode().getEncounter().getId();
        if (encounterId == 456 || encounterId == 458 || encounterId == 460 || encounterId == 462 || encounterId == 502 || encounterId == 504) {
            return 0.8f;
        }
        return 1.5f;
    }

    private void applyTouch() {
        this.isRunning = false;
        this.tapToAttack.setVisible(false);
        this.meterAngle = getOptimizedAngle();
        if (this.meterAngle < 0.0f) {
            this.damageMultiplier = (this.meterAngle + 130.0f) / 130.0f;
            this.damageMultiplier *= 0.09f;
            this.damageMultiplier += 0.01f;
        } else if (this.meterAngle < 90.0f) {
            this.damageMultiplier = this.meterAngle / 90.0f;
            this.damageMultiplier *= 0.9f;
            this.damageMultiplier += 0.1f;
        } else {
            this.damageMultiplier = (130.0f - this.meterAngle) / 40.0f;
        }
        if (1.0f - this.damageMultiplier < Constants.physicalPerfectRange) {
            this.meterStruckStrength = 1;
            this.damageMultiplier = Constants.physicalPerfectMultiplier;
            this.trainer.getBattleReport().setScoredPerfect(true);
        } else if (this.damageMultiplier > Constants.accuracyAllGreen) {
            this.meterStruckStrength = 2;
            this.trainer.getBattleReport().setScoredVeryGood(true);
        } else {
            this.meterStruckStrength = 0;
            this.trainer.getBattleReport().setScoredNormal(true);
        }
    }

    private float getOptimizedAngle() {
        float minAngle = Math.min(this.previousMeterAngle, this.meterAngle);
        float maxAngle = Math.max(this.previousMeterAngle, this.meterAngle);
        if (minAngle > 90.0f || maxAngle < 90.0f) {
            return maxAngle > 90.0f ? minAngle : maxAngle;
        }
        return 90.0f;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        if (this.isRunning) {
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(-1)) {
                applyTouch();
                this.frameCount = 0;
            }
        } else {
            if (this.frameCount > Gdx.app.getGraphics().getFramesPerSecond() / 5) {
                this.meter.setVisible(false);
            }
            this.frameCount++;
        }
        if (this.isRunning) {
            float angSpeed = 240.0f - Math.abs(this.meterAngle - 90.0f);
            float angSpeed2 = angSpeed * this.speed;
            this.previousMeterAngle = this.meterAngle;
            this.meterAngle += angSpeed2 * deltaTime;
            if (this.meterAngle > 130.0f) {
                this.meterAngle = 130.0f;
                onDialerFinished();
                return;
            } else {
                this.meterArrow.setRotation(-this.meterAngle);
                float arrowX = (108.0f * HOR_AR) - ((((float) Math.cos(Math.toRadians(this.meterAngle))) * 95.0f) * SCALE);
                float arrowY = 150.0f + (((float) Math.sin(Math.toRadians(this.meterAngle))) * 95.0f);
                this.meterArrow.setPosition(arrowX, arrowY * SCALE);
            }
        }
        if (this.meterStruckStrength == 1 || this.meterStruckStrength == 2) {
            this.meterTextureTime += deltaTime;
            if (this.meterTextureTime > 0.5f) {
                onDialerFinished();
            }
        } else if (!this.isRunning) {
            this.meterTextureTime += deltaTime;
            if (this.meterTextureTime > 0.5f) {
                onDialerFinished();
            }
        }
        if (this.meterTextureTime < 0.5f) {
            if (this.meterStruckStrength == 1) {
                this.strengthLabel.setTexture(Assets.loadLocalizedTexture("perfect"));
                this.strengthLabel.setVisible(true);
            } else if (this.meterStruckStrength == 2) {
                this.strengthLabel.setTexture(Assets.loadLocalizedTexture("verygood"));
                this.strengthLabel.setVisible(true);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void present(Vector2 origin, SpriteBatch batcher) {
        super.present(origin, batcher);
        if (this.isRunning || this.meter.isVisible()) {
            this.meterArrow.draw(batcher);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        this.meterArrow = null;
        this.screenListener = null;
        this.strengthLabel = null;
        this.meter = null;
        this.tapToAttack = null;
    }
}
