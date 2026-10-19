package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.StageScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.Hashtable;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ZodiacAttackMeter extends ScreenGroup {
    private static final int TIMER_SECONDS = 5;
    private TextureElement arrow;
    private Color baseColor;
    private Map<ZodiacPowerType, Color> colors;
    private ZodiacSign computerSign;
    private Effect effect;
    private StageScreenElement introScreenElement;
    private ZodiacSign playerSign;
    private TextureRegion playerSignFill;
    private TextureRegion playerSignOutline;
    private boolean playingIntro;
    private ScreenListener screenListener;
    private ActorScreenElement tapOnZodiac;
    private Timer timer;
    private float damageMultiplier = 0.0f;
    private float completionRate = 0.0f;
    private boolean delayAndDone = false;
    int frameCount = 0;

    public ZodiacAttackMeter(ScreenListener screenListener) {
        this.screenListener = screenListener;
        setStretchable(true);
        setSize(480.0f, 320.0f);
        setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        addOverlay();
        this.colors = new Hashtable();
        this.colors.put(ZodiacPowerType.BAD, ColorConstants.COLOR_BAD);
        this.colors.put(ZodiacPowerType.GOOD, ColorConstants.COLOR_GOOD);
        this.arrow = add(Assets.arrowRight, 120.0f, 160.0f);
        this.arrow.setVisible(false);
        this.timer = new Timer(5);
        this.timer.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 60.0f);
        this.timer.setOnCompleteListener(new Timer.CompleteListener() { // from class: com.gaiaonline.monstergalaxy.battle.ZodiacAttackMeter.1
            @Override // com.gaiaonline.monstergalaxy.battle.Timer.CompleteListener
            public void onTimerComplete() {
                ZodiacAttackMeter.this.done();
            }
        });
        this.timer.setVisible(false);
        add(this.timer);
        this.effect = EffectManager.getInstance().getEffect(EffectType.ZODIAC_WHILE_TAPPING);
        float[] effectCoords = {Gdx.graphics.getWidth() * 0.75f, 0.0f};
        this.effect.setPosition(effectCoords[0], effectCoords[1]);
        Image tapOnZodiacActor = new Image("tapOnZodiac", Assets.tapOnZodiac);
        ResolutionManager.scaleActor(tapOnZodiacActor);
        this.tapOnZodiac = add(tapOnZodiacActor);
        this.tapOnZodiac.setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        this.tapOnZodiac.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 120.0f, 160.0f);
        this.tapOnZodiac.setVisible(false);
        this.introScreenElement = new StageScreenElement();
        add(this.introScreenElement);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void present(Vector2 pos, SpriteBatch batcher) {
        super.present(pos, batcher);
        if (!this.playingIntro) {
            drawMeter(batcher, this.completionRate);
            this.effect.draw(batcher, Gdx.graphics.getDeltaTime());
        }
    }

    public void resume() {
        addOverlay();
    }

    private void addOverlay() {
        addOverlay(0.5f);
    }

    private void drawMeter(SpriteBatch batcher, float completionRate) {
        float signScale = (Gdx.graphics.getHeight() * 0.5f) / this.playerSignOutline.getRegionHeight();
        int outlineW = (int) (this.playerSignOutline.getRegionWidth() * signScale);
        int outlineH = (int) (this.playerSignOutline.getRegionHeight() * signScale);
        int fullFillW = (int) (this.playerSignFill.getRegionWidth() * signScale);
        int fullFillH = (int) (this.playerSignFill.getRegionHeight() * signScale);
        float completionHeight = this.playerSignFill.getRegionHeight() * completionRate;
        float outlineX = (Gdx.graphics.getWidth() * 0.75f) - (outlineW / 2);
        float outlineY = (Gdx.graphics.getHeight() * 0.5f) - (outlineH / 2);
        int fillX = (int) (((outlineW - fullFillW) / 2) + outlineX);
        int fillY = (int) (((outlineH - fullFillH) / 2) + outlineY);
        int fillH = (int) (completionHeight * signScale);
        int fillSrcX = this.playerSignFill.getRegionX();
        int fillSrcY = (int) ((this.playerSignFill.getRegionY() + this.playerSignFill.getRegionHeight()) - completionHeight);
        int fillSrcW = this.playerSignFill.getRegionWidth();
        int fillSrcH = (int) completionHeight;
        Color color = this.baseColor;
        if (color == null) {
            color = ColorConstants.PRIMARY_FONT_COLOR;
        }
        Color transparent = new Color(color);
        transparent.a *= 0.75f;
        batcher.setColor(transparent);
        batcher.draw(this.playerSignFill.getTexture(), fillX, fillY, fullFillW, fillH, fillSrcX, fillSrcY, fillSrcW, fillSrcH, false, false);
        if (this.baseColor != null) {
            batcher.setColor(this.baseColor);
        }
        batcher.draw(this.playerSignOutline, outlineX, outlineY, outlineW, outlineH);
        batcher.setColor(ColorConstants.PRIMARY_FONT_COLOR);
    }

    public void start() {
        SoundManager.loadZodiacSounds();
        SoundManager.pushMusic(SoundManager.MUSIC_ZODIAC_BG);
        this.delayAndDone = false;
        this.visible = true;
        this.playingIntro = true;
        Trainer trainer = Game.getTrainer();
        this.playerSign = trainer.getTeam().getDeployedMoga().getType().getZodiacSign();
        this.computerSign = trainer.getCurrentNode().getTeam().getDeployedMoga().getType().getZodiacSign();
        ZodiacPowerType powerType = ZodiacHelper.getInstance().getPowerTypeAgainst(this.playerSign, this.computerSign);
        this.baseColor = this.colors.get(powerType);
        if (this.baseColor != null) {
            this.effect.setTint(this.baseColor);
        }
        ZodiacIntro intro = new ZodiacIntro(this.playerSign, this.computerSign, this.baseColor, powerType);
        this.introScreenElement.setStage(intro);
        intro.setListener(new ZodiacIntro.Listener() { // from class: com.gaiaonline.monstergalaxy.battle.ZodiacAttackMeter.2
            @Override // com.gaiaonline.monstergalaxy.battle.ZodiacIntro.Listener
            public void onZodiacIntroFinished() {
                ZodiacAttackMeter.this.introScreenElement.setVisible(false);
                ZodiacAttackMeter.this.startMeter();
            }
        });
        this.introScreenElement.setVisible(true);
        intro.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMeter() {
        this.playingIntro = false;
        this.playerSignOutline = Assets.zodiacAttackOutlines.get(this.playerSign);
        this.playerSignFill = Assets.zodiacAttackFills.get(this.playerSign);
        this.tapOnZodiac.setVisible(true);
        this.timer.start();
        this.effect.start();
        this.completionRate = 0.0f;
        this.damageMultiplier = 0.0f;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public boolean isSuccess() {
        return this.damageMultiplier >= Constants.zodiacAttackSuccessPercentage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void done() {
        SoundManager.popMusic();
        this.tapOnZodiac.setVisible(false);
        this.tapOnZodiac.getActor().clearActions();
        this.damageMultiplier = this.completionRate;
        this.delayAndDone = true;
        this.frameCount = 0;
    }

    private void onZodiacTap() {
        SoundManager.playSound(SoundManager.SOUND_ZODIAC_TAP);
        this.tapOnZodiac.getActor().clearActions();
        Action tapAction = Sequence.$(ScaleTo.$(1.5f, 1.5f, 0.05f), ScaleTo.$(1.0f, 1.0f, 0.05f));
        this.tapOnZodiac.getActor().action(tapAction);
        this.completionRate = Math.min(1.0f, this.completionRate + Constants.zodiacAttackTapPercentage);
        if (this.completionRate >= 1.0f) {
            done();
            this.timer.stop();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        if (this.delayAndDone) {
            if (this.frameCount > ((double) Gdx.app.getGraphics().getFramesPerSecond()) * 0.75d) {
                this.screenListener.onUIEvent(UIEvent.ZODIAC_DIALER_COMPLETED);
                this.timer.stop();
            }
            this.frameCount++;
        }
        super.update(deltaTime);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.Touchable
    public boolean touched(Vector2 touchPoint) {
        if (this.playingIntro || this.timer.isCompleted()) {
            return false;
        }
        if (touchPoint.x > Gdx.graphics.getWidth() / 2 && !this.delayAndDone) {
            onZodiacTap();
        }
        return true;
    }
}
