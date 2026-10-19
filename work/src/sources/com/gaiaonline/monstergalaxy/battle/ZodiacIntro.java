package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayedRunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.graphics.util.ActorUtil;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;

/* JADX INFO: loaded from: classes.dex */
public class ZodiacIntro extends Stage {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType;
    private ZodiacSign attackerSign;
    private Image attackerSignImage;
    private Color baseColor;
    private ZodiacSign defenderSign;
    private Image defenderSignImage;
    private EffectActor effect;
    private AutoFontLabel effectivenessLabel;
    private String effectivenessText;
    private Listener listener;
    private ZodiacPowerType powerType;
    private Image vsImage;

    public interface Listener {
        void onZodiacIntroFinished();
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType;
        if (iArr == null) {
            iArr = new int[ZodiacPowerType.valuesCustom().length];
            try {
                iArr[ZodiacPowerType.BAD.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ZodiacPowerType.GOOD.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ZodiacPowerType.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType = iArr;
        }
        return iArr;
    }

    public ZodiacIntro(ZodiacSign attackerSign, ZodiacSign defenderSign, Color baseColor, ZodiacPowerType powerType) {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.attackerSign = attackerSign;
        this.defenderSign = defenderSign;
        this.baseColor = baseColor;
        this.powerType = powerType;
        this.effect = new EffectActor("ZodiacIntroEffect");
        this.effect.setX(Gdx.graphics.getWidth() * 0.5f);
        this.effect.setY(Gdx.graphics.getHeight() * 0.5f);
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType()[powerType.ordinal()]) {
            case 1:
                this.effectivenessText = I18nManager.getText(I18nManager.I18nKey.LABEL_GOOD);
                this.effect.setEffectWithType(EffectType.ZODIAC_STRONG);
                break;
            case 2:
                this.effect.setEffectWithType(EffectType.ZODIAC_NEUTRAL);
                break;
            case 3:
                this.effectivenessText = I18nManager.getText(I18nManager.I18nKey.LABEL_BAD);
                this.effect.setEffectWithType(EffectType.ZODIAC_WEAK);
                break;
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void start() {
        TextureRegion attackerSignTR = Assets.zodiacAttackOutlines.get(this.attackerSign);
        TextureRegion defenderSignTR = Assets.zodiacAttackOutlines.get(this.defenderSign);
        this.attackerSignImage = new Image(Assets.zodiacAttackOutlines.get(this.attackerSign));
        this.defenderSignImage = new Image(Assets.zodiacAttackOutlines.get(this.defenderSign));
        this.vsImage = ResolutionManager.getScaledImage("vs", Assets.loadLocalizedTexture("vs"));
        ActorUtil.centerOrigin(this.attackerSignImage);
        ActorUtil.centerOrigin(this.defenderSignImage);
        ActorUtil.centerOrigin(this.vsImage);
        this.attackerSignImage.setScale((Gdx.graphics.getHeight() * 0.5f) / attackerSignTR.getRegionHeight());
        this.defenderSignImage.setScale((Gdx.graphics.getHeight() * 0.5f) / defenderSignTR.getRegionHeight());
        this.attackerSignImage.setX(0.0f - this.attackerSignImage.getWidth());
        this.attackerSignImage.setY((Gdx.graphics.getHeight() - this.attackerSignImage.getHeight()) / 2.0f);
        this.defenderSignImage.setX(Gdx.graphics.getWidth());
        this.defenderSignImage.setY((Gdx.graphics.getHeight() - this.defenderSignImage.getHeight()) / 2.0f);
        this.vsImage.setX((Gdx.graphics.getWidth() - this.vsImage.getWidth()) / 2.0f);
        this.vsImage.setY((Gdx.graphics.getHeight() - this.vsImage.getHeight()) / 2.0f);
        if (this.powerType != ZodiacPowerType.NORMAL) {
            BitmapFont font = Fonts.regular;
            font.setScale(1.0f * ResolutionManager.getScaleFactor());
            this.effectivenessLabel = new AutoFontLabel(this.effectivenessText, 1.0f * ResolutionManager.getScaleFactor(), this.baseColor, false, ColorConstants.PRIMARY_SHADOW_COLOR);
            ActorUtil.centerOrigin(this.effectivenessLabel);
            this.effectivenessLabel.setX((Gdx.graphics.getWidth() - this.effectivenessLabel.getWidth()) / 2.0f);
            this.effectivenessLabel.setY((Gdx.graphics.getHeight() / 3) - (this.effectivenessLabel.getHeight() / 2.0f));
            ScaleToAction scaleTo = new ScaleToAction();
            scaleTo.setScale(ResolutionManager.getScaleFactor());
            scaleTo.setDuration(0.25f);
            AlphaAction fadeOut = new AlphaAction();
            fadeOut.setAlpha(0.0f);
            fadeOut.setDuration(0.25f);
            DelayAction delay = new DelayAction();
            delay.setAction(fadeOut);
            delay.setDuration(1.5f);
            SequenceAction sequence = new SequenceAction();
            sequence.addAction(scaleTo);
            sequence.addAction(delay);
            this.effectivenessLabel.addAction(sequence);
        }
        if (this.baseColor != null) {
            this.attackerSignImage.setColor(this.baseColor);
            this.defenderSignImage.setColor(this.baseColor);
            this.vsImage.setColor(this.baseColor);
        }
        addActor(this.effect);
        addActor(this.defenderSignImage);
        addActor(this.attackerSignImage);
        addActor(this.vsImage);
        if (this.effectivenessLabel != null) {
            addActor(this.effectivenessLabel);
        }
        this.vsImage.setScale(4.0f);
        ScaleToAction scaleTo1 = new ScaleToAction();
        scaleTo1.setScale(1.0f);
        scaleTo1.setDuration(0.25f);
        ScaleToAction scaleTo4 = new ScaleToAction();
        scaleTo4.setScale(4.0f);
        scaleTo4.setDuration(0.25f);
        DelayAction delay2 = new DelayAction();
        delay2.setAction(scaleTo4);
        delay2.setDuration(1.5f);
        RunnableAction runnableAction = new DelayedRunnableAction();
        runnableAction.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.ZodiacIntro.1
            @Override // java.lang.Runnable
            public void run() {
                ZodiacIntro.this.done();
            }
        });
        SequenceAction vsSequence = new SequenceAction();
        vsSequence.addAction(scaleTo1);
        vsSequence.addAction(delay2);
        vsSequence.addAction(runnableAction);
        this.vsImage.addAction(vsSequence);
        Interpolation easeOut = Interpolation.exp10Out;
        Interpolation easeIn = Interpolation.exp10In;
        MoveToAction attackerMoveToSpot = new MoveToAction();
        attackerMoveToSpot.setX((Gdx.graphics.getWidth() * 0.25f) - (this.attackerSignImage.getWidth() / 2.0f));
        attackerMoveToSpot.setY(this.attackerSignImage.getY());
        attackerMoveToSpot.setDuration(1.5f);
        attackerMoveToSpot.setInterpolation(easeOut);
        MoveToAction attackerMoveOutOfScene = new MoveToAction();
        attackerMoveOutOfScene.setX(Gdx.graphics.getWidth());
        attackerMoveOutOfScene.setY(this.attackerSignImage.getY());
        attackerMoveOutOfScene.setDuration(0.5f);
        attackerMoveOutOfScene.setInterpolation(easeIn);
        SequenceAction attackerSignSequence = new SequenceAction();
        attackerSignSequence.addAction(attackerMoveToSpot);
        attackerSignSequence.addAction(attackerMoveOutOfScene);
        this.attackerSignImage.addAction(attackerSignSequence);
        MoveToAction defenderMoveToSpot = new MoveToAction();
        defenderMoveToSpot.setX((Gdx.graphics.getWidth() * 0.75f) - (this.defenderSignImage.getWidth() / 2.0f));
        defenderMoveToSpot.setY(this.defenderSignImage.getY());
        defenderMoveToSpot.setDuration(1.5f);
        defenderMoveToSpot.setInterpolation(easeOut);
        MoveToAction defenderMoveOutOfScene = new MoveToAction();
        defenderMoveOutOfScene.setX(-this.defenderSignImage.getWidth());
        defenderMoveOutOfScene.setY(this.defenderSignImage.getY());
        defenderMoveOutOfScene.setDuration(0.5f);
        defenderMoveOutOfScene.setInterpolation(easeIn);
        SequenceAction defenderSignSequence = new SequenceAction();
        defenderSignSequence.addAction(defenderMoveToSpot);
        defenderSignSequence.addAction(defenderMoveOutOfScene);
        this.defenderSignImage.addAction(defenderSignSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void done() {
        this.attackerSignImage.remove();
        this.defenderSignImage.remove();
        this.vsImage.remove();
        if (this.effectivenessLabel != null) {
            this.effectivenessLabel.remove();
        }
        this.listener.onZodiacIntroFinished();
    }
}
