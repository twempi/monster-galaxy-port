package com.gaiaonline.monstergalaxy.battle.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gaiaonline.monstergalaxy.app.Assets;

/* JADX INFO: loaded from: classes.dex */
public class Effect {
    private static final String TAG = "Effect";
    private boolean complete;
    private EffectData effectData;
    private String name;
    private OnCompleteListener onCompleteListener;
    protected CCParticleEffect particleEffect = new CCParticleEffect();

    public interface OnCompleteListener {
        void onComplete();
    }

    protected Effect(String name) {
        this.name = name;
    }

    public Effect(EffectData effectData) {
        this.effectData = effectData;
        CCParticleEmitter emitter = setupEmitter(effectData);
        loadImage(emitter);
        addEmitter(emitter);
        this.name = effectData.name;
    }

    public Effect getCopy() {
        return new Effect(this.effectData);
    }

    public String getName() {
        return this.name;
    }

    public void setPosition(float x, float y) {
        this.particleEffect.setPosition(x, y);
    }

    public void setTint(Color tint) {
        for (CCParticleEmitter e : this.particleEffect.getEmitters()) {
            e.setTint(tint);
        }
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void start() {
        this.complete = false;
        this.particleEffect.start();
    }

    public void draw(SpriteBatch batch, float deltaTime) {
        if (!isComplete()) {
            boolean allComplete = true;
            for (CCParticleEmitter emitter : this.particleEffect.getEmitters()) {
                if (emitter.getSprite() != null) {
                    try {
                        emitter.draw(batch, deltaTime);
                    } catch (NullPointerException e) {
                        Gdx.app.log(TAG, "Failed to draw particle emitter", e);
                        e.printStackTrace();
                    }
                }
                if (emitter.isContinuous()) {
                    allComplete = false;
                }
                if (!emitter.isComplete()) {
                    allComplete = false;
                }
            }
            if (allComplete) {
                this.particleEffect.start();
            }
            this.complete = allComplete;
            if (this.complete) {
                if (this.onCompleteListener != null) {
                    this.onCompleteListener.onComplete();
                }
                dispose();
            }
        }
    }

    public boolean isComplete() {
        return this.complete;
    }

    protected void addEmitter(CCParticleEmitter emitter) {
        this.particleEffect.getEmitters().add(emitter);
    }

    protected CCParticleEmitter setupEmitter(EffectData data) {
        CCParticleEmitter emitter = new CCParticleEmitter();
        emitter.setMaxParticleCount(data.maxParticleCount);
        emitter.setAngle(data.angle);
        emitter.setAngleVar(data.angleVar);
        emitter.setSpeed(data.speed);
        emitter.setSpeedVar(data.speedVar);
        emitter.setDuration(data.duration);
        emitter.setLife(data.life);
        emitter.setLifeVar(data.lifeVar);
        emitter.setPosVar(new Vector2(data.sourcePositionVarx, data.sourcePositionVary));
        emitter.setStartSize(data.startParticleSize);
        emitter.setFinishSize(data.finishParticleSize);
        emitter.setGravity(new Vector2(data.gravityx, data.gravityy));
        emitter.setStartColor(data.startColor);
        emitter.setFinishColor(data.finishColor);
        emitter.setStartColorVar(data.startColorVar);
        emitter.setFinishColorVar(data.finishColorVar);
        emitter.setImagePath(data.assetPath);
        return emitter;
    }

    public Effect getFlippedHorizontalCopy() {
        EffectData data = this.effectData;
        this.effectData.angle = 180.0f - this.effectData.angle;
        this.effectData.gravityx = -this.effectData.gravityx;
        return new Effect(data);
    }

    private void loadImage(CCParticleEmitter emitter) {
        String imagePath = emitter.getImagePath();
        try {
            String spriteName = emitter.getImagePath().substring(0, emitter.getImagePath().indexOf(".png"));
            Sprite sprite = Assets.loadSprite(spriteName);
            if (sprite == null) {
                Gdx.app.log(TAG, "Sprite: " + spriteName + " not found");
            }
            emitter.setSprite(sprite);
        } catch (GdxRuntimeException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error loading image:\n" + imagePath);
        }
    }

    public void dispose() {
        this.effectData = null;
    }
}
