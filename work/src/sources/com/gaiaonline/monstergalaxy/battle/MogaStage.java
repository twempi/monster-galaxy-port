package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayedRunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.PausableGroup;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.battle.anim.AnimationManager;
import com.gaiaonline.monstergalaxy.battle.anim.MogaAction;
import com.gaiaonline.monstergalaxy.battle.anim.MogaActor;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.battle.Battle;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class MogaStage {
    public static final int CPU = 1;
    public static final int PLAYER = 0;
    private static final UIEvent[] attackEvent = {UIEvent.PERFORM_PLAYER_ATTACK, UIEvent.PERFORM_CPU_ATTACK};
    private Actor captureStarSeed;
    private MogaActor cpuMogaActor;
    private float cpuMogaStartX;
    private float cpuMogaStartY;
    private int currentEnteringMoga;
    private MogaActor playerMogaActor;
    private float playerMogaStartX;
    private float playerMogaStartY;
    private BattleScreen screen;
    private final int ENTERING_NONE = 0;
    private final int ENTERING_PLAYER_MOGA = 1;
    private final int ENTERING_COMPUTER_MOGA = 2;
    private final float MOGA_POSITION_Y = 70.0f;
    private final String METHOD_TAG = "MogaStage-method";
    private StarseedCompleted starSeedAnimationCompleted = new StarseedCompleted(this, null);
    private int currentAnimations = 0;
    private MogaListener mogaListener = new MogaListener(this, 0 == true ? 1 : 0);
    private boolean showRarity = true;
    private PausableGroup group = new PausableGroup();

    /* JADX WARN: Multi-variable type inference failed */
    public MogaStage(BattleScreen battleScreen) {
        this.screen = battleScreen;
        SoundManager.loadSound(SoundManager.SOUND_EXPLODE);
    }

    public PausableGroup getGroup() {
        return this.group;
    }

    public boolean isMogaEntering() {
        return this.currentEnteringMoga != 0;
    }

    public void setPlayerMoga(Moga playerMoga) {
        Gdx.app.debug("MogaStage-method", "setPlayerMoga(" + playerMoga + ")");
        createPlayerMoga(playerMoga);
    }

    public void setCpuMoga(Moga cpuMoga) {
        Gdx.app.debug("MogaStage-method", "setCpuMoga(" + cpuMoga + ")");
        createCpuMoga(cpuMoga);
    }

    public void reloadMogaTextures() {
        if (this.playerMogaActor != null) {
            this.playerMogaActor.reloadTextureRegion(BattleAssets.getTexture(this.playerMogaActor.getMoga(), false));
        }
        if (this.cpuMogaActor != null) {
            this.cpuMogaActor.reloadTextureRegion(BattleAssets.getTexture(this.cpuMogaActor.getMoga(), true));
        }
    }

    public boolean isAnimationRunning() {
        return this.currentAnimations > 0;
    }

    public void hideRarity() {
        Gdx.app.debug("MogaStage-method", "hideRarity()");
        this.cpuMogaActor.showRarity(false);
    }

    public void attack(final int who, final boolean isZodiac) {
        Gdx.app.debug("MogaStage-method", "attack(" + who + ", " + isZodiac + ")");
        final MogaActor moga = who == 0 ? this.playerMogaActor : this.cpuMogaActor;
        MogaActor otherMoga = who == 0 ? this.cpuMogaActor : this.playerMogaActor;
        otherMoga.moveBehind(moga);
        moga.clearActions();
        moga.stopGlowing();
        boolean mirrored = who == 1;
        final AttackMove attack = isZodiac ? moga.getMoga().getType().getZodiacAttack() : moga.getMoga().getType().getBasicAttack();
        Action chargeEffect = MogaAction.newInstance(attack.getChargeEffect());
        Action charge = attack.getChargeMove(mirrored);
        Action startAttack = attack.getStartMove(mirrored);
        Action endAttack = attack.getEndMove(mirrored);
        Action hitMove = attack.getHitMove(mirrored);
        RunnableAction hitMoveRunnableAction = new DelayedRunnableAction();
        hitMoveRunnableAction.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.1
            @Override // java.lang.Runnable
            public void run() {
                if (isZodiac) {
                    moga.glowWithZodiacColor();
                }
            }
        });
        final SequenceAction hitMoveSequence = new SequenceAction();
        hitMoveSequence.addAction(hitMove);
        hitMoveSequence.addAction(hitMoveRunnableAction);
        RunnableAction startAttackCompleteRunnable = new DelayedRunnableAction();
        startAttackCompleteRunnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.2
            @Override // java.lang.Runnable
            public void run() {
                MogaActor mogaActor;
                MogaStage.this.screen.onUIEvent(MogaStage.attackEvent[who]);
                MogaStage mogaStage = MogaStage.this;
                if (who != 0) {
                    mogaActor = MogaStage.this.playerMogaActor;
                } else {
                    mogaActor = MogaStage.this.cpuMogaActor;
                }
                mogaStage.hit(mogaActor, hitMoveSequence, attack.getHitSound());
            }
        });
        RunnableAction endAttackCompleteRunnable = new DelayedRunnableAction();
        endAttackCompleteRunnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.3
            @Override // java.lang.Runnable
            public void run() {
                System.out.println("Stop glowing");
                moga.stopGlowing();
            }
        });
        SequenceAction move = new SequenceAction();
        move.addAction(chargeEffect);
        move.addAction(charge);
        move.addAction(startAttack);
        move.addAction(startAttackCompleteRunnable);
        move.addAction(endAttack);
        move.addAction(endAttackCompleteRunnable);
        addAction(moga, move);
    }

    public void capture(final boolean success) {
        Gdx.app.debug("MogaStage-method", "capture(" + success + ")");
        this.captureStarSeed = new Image(new TextureRegionDrawable(Assets.starSeed));
        ResolutionManager.scaleActor(this.captureStarSeed);
        this.captureStarSeed.setY(70.0f * ResolutionManager.getScaleFactor());
        this.group.addActor(this.captureStarSeed);
        final SequenceAction captureSequence = new SequenceAction();
        captureSequence.addAction(AnimationManager.getInstance().getAnimation(success ? "capture_moga" : "capture_moga_unsuccessful", false));
        RunnableAction captureRunnableAction = new DelayedRunnableAction();
        captureRunnableAction.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.4
            @Override // java.lang.Runnable
            public void run() {
                MogaStage.this.currentAnimations--;
                if (!success) {
                    MogaStage.this.group.removeActor(MogaStage.this.captureStarSeed);
                    MogaStage.this.screen.onUIEvent(UIEvent.CAPTURE_FAIL_COMPLETE);
                    return;
                }
                MoveToAction dropStarseed = new MoveToAction();
                dropStarseed.setPosition(MogaStage.this.captureStarSeed.getX(), MogaStage.this.captureStarSeed.getY() - 60.0f);
                dropStarseed.setDuration(0.4f);
                RunnableAction dropRunnable = new DelayedRunnableAction();
                dropRunnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MogaStage.this.screen.onUIEvent(UIEvent.CAPTURE_SUCCESS_COMPLETE);
                        MogaStage.this.group.removeActor(MogaStage.this.cpuMogaActor);
                        MogaStage.this.group.removeActor(MogaStage.this.captureStarSeed);
                    }
                });
                SequenceAction dropSequence = new SequenceAction();
                dropSequence.addAction(dropStarseed);
                dropSequence.addAction(dropRunnable);
                MogaStage.this.captureStarSeed.addAction(dropSequence);
            }
        });
        captureSequence.addAction(captureRunnableAction);
        Action starseedAction = AnimationManager.getInstance().getAnimation(success ? "capture_seed_start" : "capture_seed_start_unsuccessful", false);
        try {
            SequenceAction aSeq = (SequenceAction) starseedAction;
            SequenceAction seq2 = new SequenceAction();
            seq2.addAction(aSeq.getActions().get(1));
            RunnableAction runnableAction = new DelayedRunnableAction();
            runnableAction.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.5
                @Override // java.lang.Runnable
                public void run() {
                    MogaStage.this.cpuMogaActor.clearActions();
                    MogaStage.this.cpuMogaActor.addAction(captureSequence);
                    EffectActor aEffect = new EffectActor("sparkcapture");
                    aEffect.setEffect(EffectManager.getInstance().getEffect(EffectType.STARS).getCopy());
                    MogaStage.this.group.addActor(aEffect);
                    aEffect.setX(MogaStage.this.captureStarSeed.getX() + 15.0f);
                    aEffect.setY(MogaStage.this.captureStarSeed.getY() + 15.0f);
                    aEffect.getEffect().start();
                }
            });
            seq2.addAction(aSeq.getActions().get(1));
            seq2.addAction(runnableAction);
            aSeq.getActions().set(1, seq2);
        } catch (Exception e) {
            Gdx.app.log("Error", "MogaStage", e);
        }
        SoundManager.loadCaptureSounds();
        SoundManager.playSound(SoundManager.SOUND_SEED_THROWN);
        Action soundAction = new TemporalAction() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.6
            boolean playedSound;

            @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
            public void update(float delta) {
                if (!this.playedSound) {
                    this.playedSound = true;
                    SoundManager.playSound(success ? SoundManager.SOUND_CAPTURE_SUCCESS : SoundManager.SOUND_CAPTURE_FAIL);
                }
            }
        };
        this.currentAnimations++;
        DelayAction delay = new DelayAction();
        delay.setDuration(1.0f);
        delay.setAction(soundAction);
        this.captureStarSeed.addAction(delay);
        this.captureStarSeed.addAction(starseedAction);
    }

    public void zodiacGlow() {
        Gdx.app.debug("MogaStage-method", "zodiacGlow()");
        this.playerMogaActor.glowWithZodiacColor();
    }

    public void stopZodiacGlow() {
        Gdx.app.debug("MogaStage-method", "stopZodiacGlow()");
        this.playerMogaActor.stopGlowing();
    }

    public void freezeMogas() {
        Gdx.app.debug("MogaStage-method", "freezeMogas()");
        this.playerMogaActor.clearActions();
        this.cpuMogaActor.clearActions();
    }

    public void makePlayerMogaIdle() {
        Gdx.app.debug("MogaStage-method", "makePlayerMogaIdle()");
        this.playerMogaActor.beIdle();
    }

    public void runAway() {
        Gdx.app.debug("MogaStage-method", "runAway()");
        Action runAway = AnimationManager.getInstance().getAnimation("run_away", false);
        RunnableAction runnable = new DelayedRunnableAction();
        runnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.7
            @Override // java.lang.Runnable
            public void run() {
                MogaStage.this.playerMogaActor.setVisible(false);
                MogaStage.this.screen.onUIEvent(UIEvent.ANIMATIONS_ENDED);
            }
        });
        SequenceAction runAwaySequence = new SequenceAction();
        runAwaySequence.addAction(runAway);
        runAwaySequence.addAction(runnable);
        this.playerMogaActor.stopGlowing();
        this.playerMogaActor.addAction(runAwaySequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void explode() {
        Gdx.app.debug("MogaStage-method", "explode()");
        this.playerMogaActor.clearActions();
        this.cpuMogaActor.clearActions();
        this.cpuMogaActor.setRotation(0.0f);
        SoundManager.playSound(SoundManager.SOUND_EXPLODE);
        addAction(this.cpuMogaActor, AnimationManager.getInstance().getAnimation("dead", true));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hit(MogaActor moga, Action hitMove, String hitSound) {
        Gdx.app.debug("MogaStage-method", "hit(" + moga + ", " + hitMove + ", " + hitSound + ")");
        moga.clearActions();
        moga.setRotation(0.0f);
        SequenceAction hitSequence = new SequenceAction();
        hitSequence.addAction(hitMove);
        hitSequence.addAction(AnimationManager.getInstance().getAnimation("hit", moga == this.cpuMogaActor));
        addAction(moga, hitSequence);
        showDamage(moga);
        SoundManager.playSound(hitSound);
    }

    private void showDamage(MogaActor hitMoga) {
        Gdx.app.debug("MogaStage-method", "showDamage(" + hitMoga + ")");
        Fonts.regular.setScale(ResolutionManager.getScaleFactor() * 1.0f);
        final AutoFontLabel damageLabel = new AutoFontLabel(String.valueOf(hitMoga.getMoga().getLastDamage()), ResolutionManager.getScaleFactor() * 1.0f, ColorConstants.DAMAGE_COLOR, false, ColorConstants.DAMAGE_SHADOW_COLOR);
        damageLabel.setX(hitMoga.getX() + (hitMoga.getWidth() / 2.0f));
        damageLabel.setY(hitMoga.getY() + (hitMoga.getHeight() / 2.0f));
        MoveByAction moveUp = new MoveByAction();
        moveUp.setAmount(0.0f, 50.0f);
        moveUp.setDuration(1.0f);
        moveUp.setInterpolation(Interpolation.pow5);
        AlphaAction fadeOut = new AlphaAction();
        fadeOut.setAlpha(0.0f);
        fadeOut.setDuration(0.2f);
        RunnableAction runnable = new DelayedRunnableAction();
        runnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.8
            @Override // java.lang.Runnable
            public void run() {
                damageLabel.clearActions();
                MogaStage.this.group.removeActor(damageLabel);
            }
        });
        SequenceAction damageAction = new SequenceAction();
        damageAction.addAction(moveUp);
        damageAction.addAction(fadeOut);
        damageAction.addAction(runnable);
        damageLabel.addAction(damageAction);
        this.group.addActor(damageLabel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAction(Actor actor, Action move) {
        Gdx.app.debug("MogaStage-method", "addAction(" + actor + ", " + move + ")");
        this.currentAnimations++;
        SequenceAction seq = new SequenceAction();
        RunnableAction runnableAction = new DelayedRunnableAction();
        runnableAction.setRunnable(this.mogaListener);
        seq.addAction(move);
        seq.addAction(runnableAction);
        actor.addAction(seq);
    }

    private void createPlayerMoga(Moga playerMoga) {
        Gdx.app.debug("MogaStage-method", "createPlayerMoga(" + playerMoga + ")");
        float scaleFactor = ResolutionManager.getScaleFactor();
        removePlayerMoga();
        this.playerMogaActor = new MogaActor(this.screen, playerMoga, BattleAssets.getTexture(playerMoga, false), false);
        this.playerMogaActor.setWidth(this.playerMogaActor.getWidth() * scaleFactor);
        this.playerMogaActor.setHeight(this.playerMogaActor.getHeight() * scaleFactor);
        this.playerMogaActor.setX((Gdx.graphics.getWidth() / 4) - (this.playerMogaActor.getWidth() / 2.0f));
        this.playerMogaActor.setY(70.0f * scaleFactor);
        this.playerMogaStartX = this.playerMogaActor.getX();
        this.playerMogaStartY = this.playerMogaActor.getY();
    }

    public void removePlayerMoga() {
        Gdx.app.debug("MogaStage-method", "removePlayerMoga()");
        if (this.playerMogaActor != null) {
            this.playerMogaActor.stopGlowing();
            this.group.removeActor(this.playerMogaActor);
        }
    }

    private void removeCpuMoga() {
        Gdx.app.debug("MogaStage-method", "removeCpuMoga()");
        if (this.cpuMogaActor != null) {
            this.group.removeActor(this.cpuMogaActor);
        }
    }

    private void removeCaptureStarseed() {
        Gdx.app.debug("MogaStage-method", "removeCaptureStarseed()");
        if (this.captureStarSeed != null) {
            this.group.removeActor(this.captureStarSeed);
            this.captureStarSeed = null;
        }
    }

    public void removeMogas() {
        Gdx.app.debug("MogaStage-method", "removeMogas()");
        removePlayerMoga();
        removeCpuMoga();
        removeCaptureStarseed();
    }

    private void createCpuMoga(Moga cpuMoga) {
        Gdx.app.debug("MogaStage-method", "createCpuMoga(" + cpuMoga + ")");
        float scaleFactor = ResolutionManager.getScaleFactor();
        removeCpuMoga();
        this.cpuMogaActor = new MogaActor(this.screen, cpuMoga, BattleAssets.getTexture(cpuMoga, true), true);
        this.cpuMogaActor.setWidth(this.cpuMogaActor.getWidth() * scaleFactor);
        this.cpuMogaActor.setHeight(this.cpuMogaActor.getHeight() * scaleFactor);
        this.cpuMogaActor.setX((Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4)) - (this.cpuMogaActor.getWidth() / 2.0f));
        this.cpuMogaActor.setY(70.0f * scaleFactor);
        if (this.screen.getBattle().getState() != Battle.State.PLAYER_ATTACK) {
            this.cpuMogaActor.beIdle();
            if (this.showRarity) {
                this.cpuMogaActor.showRarity(true);
                this.showRarity = false;
            }
            this.cpuMogaActor.addToStage(this.group);
        }
        this.cpuMogaStartX = this.cpuMogaActor.getX();
        this.cpuMogaStartY = this.cpuMogaActor.getY();
    }

    public void enterPlayerMoga() {
        Gdx.app.debug("MogaStage-method", "enterPlayerMoga()");
        enterMoga(1);
    }

    public void enterComputerMoga() {
        Gdx.app.debug("MogaStage-method", "enterComputerMoga()");
        enterMoga(2);
    }

    private void enterMoga(int enteringMoga) {
        Gdx.app.debug("MogaStage-method", "enterMoga(" + enteringMoga + ")");
        SoundManager.playSound(SoundManager.SOUND_MOGA_ENTRANCE);
        this.currentEnteringMoga = enteringMoga;
        this.mogaListener.exploded = false;
        this.currentAnimations = 0;
        Image starSeed = new Image(Assets.starSeed);
        ResolutionManager.scaleActor(starSeed);
        starSeed.setY(70.0f * ResolutionManager.getScaleFactor());
        if (this.currentEnteringMoga == 1) {
            starSeed.setX(0.0f);
        } else {
            starSeed.setX(Gdx.graphics.getWidth());
        }
        boolean isMirrored = this.currentEnteringMoga == 2;
        Action starSeedAnimation = AnimationManager.getInstance().getAnimation("glowEntrance", isMirrored);
        RunnableAction runnableAction = new DelayedRunnableAction();
        this.starSeedAnimationCompleted.setActor(starSeed);
        runnableAction.setRunnable(this.starSeedAnimationCompleted);
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(starSeedAnimation);
        sequence.addAction(runnableAction);
        starSeed.addAction(sequence);
        this.currentAnimations++;
        this.group.addActor(starSeed);
    }

    private class StarseedCompleted implements Runnable {
        private Actor actor;

        private StarseedCompleted() {
        }

        /* synthetic */ StarseedCompleted(MogaStage mogaStage, StarseedCompleted starseedCompleted) {
            this();
        }

        public void setActor(Actor actor) {
            this.actor = actor;
        }

        @Override // java.lang.Runnable
        public void run() {
            final MogaActor enteringMoga;
            Gdx.app.debug("MogaStage-method", "StarseedCompleted.run()");
            MogaStage.this.currentAnimations--;
            Group parent = this.actor.getParent();
            this.actor.remove();
            if (MogaStage.this.currentEnteringMoga == 1) {
                enteringMoga = MogaStage.this.playerMogaActor;
            } else {
                enteringMoga = MogaStage.this.cpuMogaActor;
            }
            MogaStage.this.screen.doEffect(EffectType.PUFF_SMOKE_GLOW, enteringMoga.getX() + (enteringMoga.getWidth() / 2.0f), enteringMoga.getY() + (enteringMoga.getHeight() / 5.0f));
            enteringMoga.addToStage(parent);
            AlphaAction fadeIn = new AlphaAction();
            fadeIn.setAlpha(1.0f);
            fadeIn.setDuration(1.0f);
            ScaleToAction scaleTo = new ScaleToAction();
            scaleTo.setScale(1.0f);
            scaleTo.setDuration(0.8f);
            SequenceAction scaleToSeq = new SequenceAction();
            RunnableAction scaleToRunnable = new DelayedRunnableAction();
            scaleToRunnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.StarseedCompleted.1
                @Override // java.lang.Runnable
                public void run() {
                    Gdx.app.debug("MogaStage-method", "StarseedCompleted.run.scaleToRunnable.run()");
                    enteringMoga.setOriginY(Math.abs(enteringMoga.getHeight() / 2.0f));
                }
            });
            scaleToSeq.addAction(scaleTo);
            scaleToSeq.addAction(scaleToRunnable);
            ParallelAction mogaEntrance = new ParallelAction();
            mogaEntrance.addAction(fadeIn);
            mogaEntrance.addAction(scaleToSeq);
            enteringMoga.setOriginY(0.0f);
            enteringMoga.setScale(0.0f);
            enteringMoga.getColor().a = 0.0f;
            MogaStage.this.addAction(enteringMoga, mogaEntrance);
        }
    }

    private class MogaListener implements Runnable {
        boolean exploded;

        private MogaListener() {
            this.exploded = false;
        }

        /* synthetic */ MogaListener(MogaStage mogaStage, MogaListener mogaListener) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            MogaStage.this.currentAnimations--;
            Gdx.app.debug("MogaStage-MogaListener", "Action ended. Current animations: " + MogaStage.this.currentAnimations);
            if (MogaStage.this.currentAnimations == 0) {
                MogaStage.this.currentEnteringMoga = 0;
                if (this.exploded || MogaStage.this.cpuMogaActor.getMoga().getHitPoints() != 0) {
                    MogaStage.this.cpuMogaActor.setX(MogaStage.this.cpuMogaStartX);
                    MogaStage.this.cpuMogaActor.setY(MogaStage.this.cpuMogaStartY);
                    MogaStage.this.playerMogaActor.setX(MogaStage.this.playerMogaStartX);
                    MogaStage.this.playerMogaActor.setY(MogaStage.this.playerMogaStartY);
                    MogaStage.this.playerMogaActor.beIdle();
                    MogaStage.this.cpuMogaActor.beIdle();
                    MogaStage.this.screen.onUIEvent(UIEvent.ANIMATIONS_ENDED);
                    return;
                }
                this.exploded = true;
                MogaStage.this.explode();
            }
        }
    }

    public void dispose() {
        Gdx.app.debug("MogaStage-method", "dispose()");
        removeMogas();
    }
}
