package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ShadowLabel;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ZodiacIntro extends Stage {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType = null;
    private static final String LABEL_BAD = "Less effective";
    private static final String LABEL_GOOD = "More effective";
    private ZodiacSign attackerSign;
    private Image attackerSignImage;
    private Color baseColor;
    private ZodiacSign defenderSign;
    private Image defenderSignImage;
    private EffectActor effect;
    private ShadowLabel effectivenessLabel;
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
        this.effect.x = Gdx.graphics.getWidth() * 0.5f;
        this.effect.y = Gdx.graphics.getHeight() * 0.5f;
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType()[powerType.ordinal()]) {
            case 1:
                this.effectivenessText = LABEL_GOOD;
                this.effect.setEffectWithType(EffectType.ZODIAC_STRONG);
                break;
            case 2:
                this.effect.setEffectWithType(EffectType.ZODIAC_NEUTRAL);
                break;
            case 3:
                this.effectivenessText = LABEL_BAD;
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
        this.attackerSignImage = new Image("attackerSignImage", Assets.zodiacAttackOutlines.get(this.attackerSign));
        this.defenderSignImage = new Image("defenderSignImage", Assets.zodiacAttackOutlines.get(this.defenderSign));
        this.vsImage = ResolutionManager.getScaledImage("vs", Assets.vs);
        Image image = this.attackerSignImage;
        Image image2 = this.attackerSignImage;
        float height = (Gdx.graphics.getHeight() * 0.5f) / attackerSignTR.getRegionHeight();
        image2.scaleY = height;
        image.scaleX = height;
        Image image3 = this.defenderSignImage;
        Image image4 = this.defenderSignImage;
        float height2 = (Gdx.graphics.getHeight() * 0.5f) / defenderSignTR.getRegionHeight();
        image4.scaleY = height2;
        image3.scaleX = height2;
        this.attackerSignImage.x = 0.0f - this.attackerSignImage.width;
        this.attackerSignImage.y = (Gdx.graphics.getHeight() - this.attackerSignImage.height) / 2.0f;
        this.defenderSignImage.x = Gdx.graphics.getWidth();
        this.defenderSignImage.y = (Gdx.graphics.getHeight() - this.defenderSignImage.height) / 2.0f;
        this.vsImage.x = (Gdx.graphics.getWidth() - this.vsImage.width) / 2.0f;
        this.vsImage.y = (Gdx.graphics.getHeight() - this.vsImage.height) / 2.0f;
        if (this.powerType != ZodiacPowerType.NORMAL) {
            BitmapFont font = Fonts.regular;
            font.setScale(1.0f * ResolutionManager.getScaleFactor());
            this.effectivenessLabel = new ShadowLabel("effectivenessLabel", font, this.effectivenessText, this.baseColor, ColorConstants.PRIMARY_SHADOW_COLOR);
            this.effectivenessLabel.x = (Gdx.graphics.getWidth() - this.effectivenessLabel.width) / 2.0f;
            this.effectivenessLabel.y = (Gdx.graphics.getHeight() / 3) - (this.effectivenessLabel.height / 2.0f);
            this.effectivenessLabel.action(Sequence.$(ScaleTo.$(1.0f * ResolutionManager.getScaleFactor(), 1.0f * ResolutionManager.getScaleFactor(), 0.25f), Delay.$(FadeOut.$(0.25f), 1.5f)));
        }
        if (this.baseColor != null) {
            this.attackerSignImage.color.set(this.baseColor);
            this.defenderSignImage.color.set(this.baseColor);
            this.vsImage.color.set(this.baseColor);
        }
        addActor(this.effect);
        addActor(this.defenderSignImage);
        addActor(this.attackerSignImage);
        addActor(this.vsImage);
        if (this.effectivenessLabel != null) {
            addActor(this.effectivenessLabel);
        }
        Image image5 = this.vsImage;
        this.vsImage.scaleY = 4.0f;
        image5.scaleX = 4.0f;
        Sequence vsSequence = Sequence.$(ScaleTo.$(1.0f, 1.0f, 0.25f), Delay.$(ScaleTo.$(4.0f, 4.0f, 0.25f), 1.5f));
        vsSequence.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.ZodiacIntro.1
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                ZodiacIntro.this.done();
            }
        });
        Interpolator easeOut = DecelerateInterpolator.$(5.0f);
        Interpolator easeIn = AccelerateInterpolator.$(2.0f);
        this.vsImage.action(vsSequence);
        this.attackerSignImage.action(Sequence.$(MoveTo.$((Gdx.graphics.getWidth() * 0.25f) - (this.attackerSignImage.width / 2.0f), this.attackerSignImage.y, 1.5f).setInterpolator(easeOut), MoveTo.$(Gdx.graphics.getWidth(), this.attackerSignImage.y, 0.5f).setInterpolator(easeIn)));
        this.defenderSignImage.action(Sequence.$(MoveTo.$((Gdx.graphics.getWidth() * 0.75f) - (this.defenderSignImage.width / 2.0f), this.defenderSignImage.y, 1.5f).setInterpolator(easeOut), MoveTo.$(-this.defenderSignImage.width, this.defenderSignImage.y, 0.5f).setInterpolator(easeIn)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void done() {
        removeActor(this.attackerSignImage);
        removeActor(this.defenderSignImage);
        removeActor(this.vsImage);
        if (this.effectivenessLabel != null) {
            removeActor(this.effectivenessLabel);
        }
        this.listener.onZodiacIntroFinished();
    }
}
