package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateInterpolator;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.PausableGroup;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ShadowLabel;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.anim.AnimationManager;
import com.gaiaonline.monstergalaxy.battle.anim.MogaAction;
import com.gaiaonline.monstergalaxy.battle.anim.MogaActor;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.battle.Battle;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
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
    private OnActionCompleted starSeedAnimationCompleted = new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.1
        @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
        public void completed(Action action) {
            final MogaActor enteringMoga;
            action.setCompletionListener(null);
            MogaStage.this.currentAnimations--;
            Actor actor = action.getTarget();
            actor.clearActions();
            actor.remove();
            if (MogaStage.this.currentEnteringMoga == 1) {
                enteringMoga = MogaStage.this.playerMogaActor;
            } else {
                enteringMoga = MogaStage.this.cpuMogaActor;
            }
            MogaStage.this.screen.doEffect(EffectType.PUFF_SMOKE_GLOW, enteringMoga.x + (enteringMoga.width / 2.0f), enteringMoga.y + (enteringMoga.height / 5.0f));
            enteringMoga.addToStage(actor.parent);
            Action fadeIn = FadeIn.$(1.0f);
            Action scaleTo = ScaleTo.$(1.0f, 1.0f, 0.8f);
            scaleTo.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.1.1
                @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
                public void completed(Action action2) {
                    enteringMoga.originY = Math.abs(enteringMoga.height / 2.0f);
                }
            });
            Action mogaEntrance = Parallel.$(fadeIn, scaleTo);
            enteringMoga.originY = 0.0f;
            enteringMoga.scaleX = 0.0f;
            enteringMoga.scaleY = 0.0f;
            enteringMoga.color.a = 0.0f;
            MogaStage.this.addAction(enteringMoga, mogaEntrance);
        }
    };
    private int currentAnimations = 0;
    private MogaListener mogaListener = new MogaListener(this, null);
    private boolean showRarity = true;
    private PausableGroup group = new PausableGroup();

    public MogaStage(BattleScreen screen) {
        this.screen = screen;
        SoundManager.loadSound(SoundManager.SOUND_EXPLODE);
    }

    public PausableGroup getGroup() {
        return this.group;
    }

    public boolean isMogaEntering() {
        return this.currentEnteringMoga != 0;
    }

    public void setPlayerMoga(Moga playerMoga) {
        createPlayerMoga(playerMoga);
    }

    public void setCpuMoga(Moga cpuMoga) {
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
        this.cpuMogaActor.showRarity(false);
    }

    public void attack(final int who, final boolean isZodiac) {
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
        final Action hitMove = attack.getHitMove(mirrored);
        Action move = Sequence.$(chargeEffect, charge, startAttack, endAttack);
        startAttack.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.2
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                MogaActor mogaActor;
                MogaStage.this.screen.onUIEvent(MogaStage.attackEvent[who]);
                MogaStage mogaStage = MogaStage.this;
                if (who != 0) {
                    mogaActor = MogaStage.this.playerMogaActor;
                } else {
                    mogaActor = MogaStage.this.cpuMogaActor;
                }
                mogaStage.hit(mogaActor, hitMove, attack.getHitSound());
            }
        });
        hitMove.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.3
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                if (isZodiac) {
                    moga.glowWithZodiacColor();
                }
            }
        });
        endAttack.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.4
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                moga.stopGlowing();
            }
        });
        addAction(moga, move);
    }

    public void capture(final boolean success) {
        this.captureStarSeed = new Image("", Assets.starSeed);
        ResolutionManager.scaleActor(this.captureStarSeed);
        this.captureStarSeed.y = 70.0f * ResolutionManager.getScaleFactor();
        this.group.addActor(this.captureStarSeed);
        final Action captureAction = AnimationManager.getInstance().getAnimation(success ? "capture_moga" : "capture_moga_unsuccessful", false);
        captureAction.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.5
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                MogaStage.this.currentAnimations--;
                if (!success) {
                    MogaStage.this.group.removeActor(MogaStage.this.captureStarSeed);
                    MogaStage.this.screen.onUIEvent(UIEvent.CAPTURE_FAIL_COMPLETE);
                } else {
                    MoveTo dropStarseed = MoveTo.$(MogaStage.this.captureStarSeed.x, MogaStage.this.captureStarSeed.y - 60.0f, 0.4f);
                    dropStarseed.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.5.1
                        @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
                        public void completed(Action action2) {
                            MogaStage.this.screen.onUIEvent(UIEvent.CAPTURE_SUCCESS_COMPLETE);
                            MogaStage.this.group.removeActor(MogaStage.this.cpuMogaActor);
                            MogaStage.this.group.removeActor(MogaStage.this.captureStarSeed);
                        }
                    });
                    MogaStage.this.captureStarSeed.action(dropStarseed);
                }
            }
        });
        Action starseedAction = AnimationManager.getInstance().getAnimation(success ? "capture_seed_start" : "capture_seed_start_unsuccessful", false);
        try {
            Sequence aSeq = (Sequence) starseedAction;
            aSeq.getActions().get(1).setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.6
                @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
                public void completed(Action action) {
                    MogaStage.this.cpuMogaActor.clearActions();
                    MogaStage.this.cpuMogaActor.action(captureAction);
                    EffectActor aEffect = new EffectActor("sparkcapture");
                    aEffect.setEffect(EffectManager.getInstance().getEffect(EffectType.STARS).getCopy());
                    MogaStage.this.group.addActor(aEffect);
                    aEffect.x = MogaStage.this.captureStarSeed.x + 15.0f;
                    aEffect.y = MogaStage.this.captureStarSeed.y + 15.0f;
                    aEffect.getEffect().start();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("Error", "MogaStage", e);
        }
        SoundManager.loadCaptureSounds();
        SoundManager.playSound(SoundManager.SOUND_SEED_THROWN);
        Action soundAction = new AnimationAction() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.7
            boolean playedSound;

            @Override // com.badlogic.gdx.scenes.scene2d.Action
            public void setTarget(Actor actor) {
            }

            @Override // com.badlogic.gdx.scenes.scene2d.Action
            public Action copy() {
                return null;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.Action
            public void act(float delta) {
                if (!this.playedSound) {
                    this.playedSound = true;
                    SoundManager.playSound(success ? SoundManager.SOUND_CAPTURE_SUCCESS : SoundManager.SOUND_CAPTURE_FAIL);
                }
            }
        };
        this.currentAnimations++;
        this.captureStarSeed.action(Delay.$(soundAction, 1.0f));
        this.captureStarSeed.action(starseedAction);
    }

    public void zodiacGlow() {
        this.playerMogaActor.glowWithZodiacColor();
    }

    public void stopZodiacGlow() {
        this.playerMogaActor.stopGlowing();
    }

    public void freezeMogas() {
        this.playerMogaActor.clearActions();
        this.cpuMogaActor.clearActions();
    }

    public void makePlayerMogaIdle() {
        this.playerMogaActor.beIdle();
    }

    public void runAway() {
        Action runAway = AnimationManager.getInstance().getAnimation("run_away", false);
        this.playerMogaActor.stopGlowing();
        this.playerMogaActor.action(runAway);
        runAway.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.8
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                MogaStage.this.playerMogaActor.visible = false;
                MogaStage.this.screen.onUIEvent(UIEvent.ANIMATIONS_ENDED);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void explode() {
        this.playerMogaActor.clearActions();
        this.cpuMogaActor.clearActions();
        this.cpuMogaActor.rotation = 0.0f;
        SoundManager.playSound(SoundManager.SOUND_EXPLODE);
        addAction(this.cpuMogaActor, AnimationManager.getInstance().getAnimation("dead", true));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hit(MogaActor moga, Action hitMove, String hitSound) {
        moga.clearActions();
        moga.rotation = 0.0f;
        Action[] actionArr = new Action[2];
        actionArr[0] = hitMove;
        actionArr[1] = AnimationManager.getInstance().getAnimation("hit", moga == this.cpuMogaActor);
        Action hitSequence = Sequence.$(actionArr);
        addAction(moga, hitSequence);
        showDamage(moga);
        SoundManager.playSound(hitSound);
    }

    private void showDamage(MogaActor hitMoga) {
        Fonts.regular.setScale(ResolutionManager.getScaleFactor() * 1.0f);
        final ShadowLabel damageLabel = new ShadowLabel("damageLabel", Fonts.regular, String.valueOf(hitMoga.getMoga().getLastDamage()), ColorConstants.DAMAGE_COLOR, ColorConstants.DAMAGE_SHADOW_COLOR);
        damageLabel.x = hitMoga.x + (hitMoga.width / 2.0f);
        damageLabel.y = hitMoga.y + (hitMoga.height / 2.0f);
        MoveBy moveUp = MoveBy.$(0.0f, 50.0f, 1.0f);
        moveUp.setInterpolator(AccelerateInterpolator.$(1.0f));
        FadeOut fadeOut = FadeOut.$(0.2f);
        Sequence damageAction = Sequence.$(moveUp, fadeOut);
        damageAction.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.MogaStage.9
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action) {
                damageLabel.clearActions();
                MogaStage.this.group.removeActor(damageLabel);
            }
        });
        damageLabel.action(damageAction);
        this.group.addActor(damageLabel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAction(Actor actor, Action move) {
        this.currentAnimations++;
        move.setCompletionListener(this.mogaListener);
        actor.action(move);
    }

    private void createPlayerMoga(Moga playerMoga) {
        float scaleFactor = ResolutionManager.getScaleFactor();
        removePlayerMoga();
        this.playerMogaActor = new MogaActor(this.screen, playerMoga, BattleAssets.getTexture(playerMoga, false), false);
        this.playerMogaActor.width *= scaleFactor;
        this.playerMogaActor.height *= scaleFactor;
        this.playerMogaActor.x = (Gdx.graphics.getWidth() / 4) - (this.playerMogaActor.width / 2.0f);
        this.playerMogaActor.y = 70.0f * scaleFactor;
        this.playerMogaStartX = this.playerMogaActor.x;
        this.playerMogaStartY = this.playerMogaActor.y;
    }

    public void removePlayerMoga() {
        if (this.playerMogaActor != null) {
            this.playerMogaActor.stopGlowing();
            this.group.removeActor(this.playerMogaActor);
        }
    }

    private void removeCpuMoga() {
        if (this.cpuMogaActor != null) {
            this.group.removeActor(this.cpuMogaActor);
        }
    }

    private void removeCaptureStarseed() {
        if (this.captureStarSeed != null) {
            this.group.removeActor(this.captureStarSeed);
            this.captureStarSeed = null;
        }
    }

    public void removeMogas() {
        removePlayerMoga();
        removeCpuMoga();
        removeCaptureStarseed();
    }

    private void createCpuMoga(Moga cpuMoga) {
        float scaleFactor = ResolutionManager.getScaleFactor();
        removeCpuMoga();
        this.cpuMogaActor = new MogaActor(this.screen, cpuMoga, BattleAssets.getTexture(cpuMoga, true), true);
        this.cpuMogaActor.width *= scaleFactor;
        this.cpuMogaActor.height *= scaleFactor;
        this.cpuMogaActor.x = (Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4)) - (this.cpuMogaActor.width / 2.0f);
        this.cpuMogaActor.y = 70.0f * scaleFactor;
        if (this.screen.getBattle().getState() != Battle.State.PLAYER_ATTACK) {
            this.cpuMogaActor.beIdle();
            if (this.showRarity) {
                this.cpuMogaActor.showRarity(true);
                this.showRarity = false;
            }
            this.cpuMogaActor.addToStage(this.group);
        }
        this.cpuMogaStartX = this.cpuMogaActor.x;
        this.cpuMogaStartY = this.cpuMogaActor.y;
    }

    public void enterPlayerMoga() {
        enterMoga(1);
    }

    public void enterComputerMoga() {
        enterMoga(2);
    }

    private void enterMoga(int enteringMoga) {
        SoundManager.playSound(SoundManager.SOUND_MOGA_ENTRANCE);
        this.currentEnteringMoga = enteringMoga;
        this.mogaListener.exploded = false;
        this.currentAnimations = 0;
        Image starSeed = new Image("starSeed", Assets.starSeed);
        ResolutionManager.scaleActor(starSeed);
        starSeed.y = 70.0f * ResolutionManager.getScaleFactor();
        if (this.currentEnteringMoga == 1) {
            starSeed.x = 0.0f;
        } else {
            starSeed.x = Gdx.graphics.getWidth();
        }
        boolean isMirrored = this.currentEnteringMoga == 2;
        Action starSeedAnimation = AnimationManager.getInstance().getAnimation("glowEntrance", isMirrored);
        starSeedAnimation.setCompletionListener(this.starSeedAnimationCompleted);
        starSeed.action(starSeedAnimation);
        this.currentAnimations++;
        this.group.addActor(starSeed);
    }

    private class MogaListener implements OnActionCompleted {
        boolean exploded;

        private MogaListener() {
            this.exploded = false;
        }

        /* synthetic */ MogaListener(MogaStage mogaStage, MogaListener mogaListener) {
            this();
        }

        @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
        public void completed(Action action) {
            action.setCompletionListener(null);
            MogaStage.this.currentAnimations--;
            if (MogaStage.this.currentAnimations == 0) {
                MogaStage.this.currentEnteringMoga = 0;
                if (this.exploded || MogaStage.this.cpuMogaActor.getMoga().getHitPoints() != 0) {
                    MogaStage.this.cpuMogaActor.x = MogaStage.this.cpuMogaStartX;
                    MogaStage.this.cpuMogaActor.y = MogaStage.this.cpuMogaStartY;
                    MogaStage.this.playerMogaActor.x = MogaStage.this.playerMogaStartX;
                    MogaStage.this.playerMogaActor.y = MogaStage.this.playerMogaStartY;
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
        removeMogas();
    }
}
