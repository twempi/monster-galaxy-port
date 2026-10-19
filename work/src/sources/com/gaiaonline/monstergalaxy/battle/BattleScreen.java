package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.analytics.Analytics;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.MusicManager;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.battle.dialer.AttackMeter;
import com.gaiaonline.monstergalaxy.battle.dialer.LinealAttackMeter;
import com.gaiaonline.monstergalaxy.battle.dialer.RoundedAttackMeter;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.dialogs.BattleEndedDialog;
import com.gaiaonline.monstergalaxy.battle.dialogs.BattlePausedDialog;
import com.gaiaonline.monstergalaxy.battle.dialogs.CaptureDialog;
import com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.battle.tutorial.AttackTutorialSkin1;
import com.gaiaonline.monstergalaxy.battle.tutorial.AttackTutorialSkin2;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.AttackType;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.battle.Battle;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.map.Encounter;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class BattleScreen extends Screen implements Battle.Listener, TeamHud.Listener, ScreenListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$battle$Battle$EventType = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent = null;
    private static final String METHOD_TAG = "BattleScreen-method";
    private static BattleScreen instance;
    private AttackButtons attackButtons;
    private AttackMeter attackMetter;
    private BackpackButton backpackButton;
    private Battle battle;
    private AttackType cpuAttackType;
    private TeamHud cpuTeamHud;
    private int currentEncounterId;
    private boolean drinkingBlueCoffee;
    private boolean isPlayerTurn;
    private Hud leftHud;
    private float meterStrength;
    private MogaStage mogaStage;
    private PauseButton pauseButton;
    private TeamHud playerTeamHud;
    private Battle.EventType queuedEvent;
    private Hud rightHud;
    private boolean showAttackTutorial;
    private ActorScreenElement teamHudHintArrow;
    private Trainer trainer;
    private ZodiacAttackMeter zodiacAttackMeter;
    private boolean needToReloadAssets = false;
    boolean isWaitingForAnimation = false;

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$battle$Battle$EventType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$battle$Battle$EventType;
        if (iArr == null) {
            iArr = new int[Battle.EventType.valuesCustom().length];
            try {
                iArr[Battle.EventType.ATTACK_CHOICE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Battle.EventType.CPU_ATTACK.ordinal()] = 7;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Battle.EventType.CPU_KILLED.ordinal()] = 8;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Battle.EventType.DEPLOY_MOGA.ordinal()] = 9;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Battle.EventType.MOGA_CAPTURE_FAILED.ordinal()] = 12;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Battle.EventType.MOGA_CAPTURE_SUCCESS.ordinal()] = 11;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[Battle.EventType.NOT_ENOUGH_MATERSEEDS.ordinal()] = 14;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[Battle.EventType.NOT_ENOUGH_STARSEEDS.ordinal()] = 13;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[Battle.EventType.PLAYER_ATTACK.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[Battle.EventType.PLAYER_KILLED.ordinal()] = 4;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[Battle.EventType.PLAYER_LOST.ordinal()] = 10;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[Battle.EventType.PLAYER_RAN_AWAY.ordinal()] = 5;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[Battle.EventType.REGULAR_ATTACK_STRENGTH_METER.ordinal()] = 2;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[Battle.EventType.ZODIAC_ATTACK_STRENGTH_METER.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$battle$Battle$EventType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent;
        if (iArr == null) {
            iArr = new int[UIEvent.valuesCustom().length];
            try {
                iArr[UIEvent.ACCEPT_BUTTON.ordinal()] = 43;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[UIEvent.ANIMATIONS_ENDED.ordinal()] = 22;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[UIEvent.ATTACK_BUTTON.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[UIEvent.ATTACK_DIALER_PRESSED.ordinal()] = 9;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[UIEvent.ATTACK_DIALER_TIMEOUT.ordinal()] = 10;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[UIEvent.ATTACK_TIMEOUT.ordinal()] = 8;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[UIEvent.BACKPACK_BUTTON.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[UIEvent.BACK_BUTTON.ordinal()] = 41;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[UIEvent.CANCEL_BUTTON.ordinal()] = 44;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[UIEvent.CAPTURE_BUTTON.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[UIEvent.CAPTURE_FAIL_COMPLETE.ordinal()] = 39;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[UIEvent.CAPTURE_SUCCESS_COMPLETE.ordinal()] = 38;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[UIEvent.CLAIM_PRIZE.ordinal()] = 47;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[UIEvent.CLOSE_BUTTON.ordinal()] = 21;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[UIEvent.DONE_BUTTON.ordinal()] = 19;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[UIEvent.DRINK_BLUE_COFFEE.ordinal()] = 37;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[UIEvent.EMAIL_SHARE.ordinal()] = 15;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[UIEvent.FACEBOOK_CONNECT.ordinal()] = 11;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[UIEvent.LUCKY_MOGA.ordinal()] = 48;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[UIEvent.MANAGE_TEAM.ordinal()] = 36;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[UIEvent.MENU_BUTTON.ordinal()] = 46;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[UIEvent.NEXT_BUTTON.ordinal()] = 40;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[UIEvent.OK_BUTTON.ordinal()] = 45;
            } catch (NoSuchFieldError e23) {
            }
            try {
                iArr[UIEvent.PAUSE_BUTTON.ordinal()] = 1;
            } catch (NoSuchFieldError e24) {
            }
            try {
                iArr[UIEvent.PERFORM_CPU_ATTACK.ordinal()] = 18;
            } catch (NoSuchFieldError e25) {
            }
            try {
                iArr[UIEvent.PERFORM_PLAYER_ATTACK.ordinal()] = 17;
            } catch (NoSuchFieldError e26) {
            }
            try {
                iArr[UIEvent.PORTRAIT_BUTTON.ordinal()] = 26;
            } catch (NoSuchFieldError e27) {
            }
            try {
                iArr[UIEvent.RECHARGE_BUTTON.ordinal()] = 20;
            } catch (NoSuchFieldError e28) {
            }
            try {
                iArr[UIEvent.RESUME_BUTTON.ordinal()] = 25;
            } catch (NoSuchFieldError e29) {
            }
            try {
                iArr[UIEvent.RUN_AWAY_BUTTON.ordinal()] = 24;
            } catch (NoSuchFieldError e30) {
            }
            try {
                iArr[UIEvent.SHARE.ordinal()] = 13;
            } catch (NoSuchFieldError e31) {
            }
            try {
                iArr[UIEvent.SKIP_BUTTON.ordinal()] = 42;
            } catch (NoSuchFieldError e32) {
            }
            try {
                iArr[UIEvent.SKY_SHOP_BUTTON.ordinal()] = 23;
            } catch (NoSuchFieldError e33) {
            }
            try {
                iArr[UIEvent.SMS_SHARE.ordinal()] = 14;
            } catch (NoSuchFieldError e34) {
            }
            try {
                iArr[UIEvent.TAMER_DISMISS_DIALOG.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                iArr[UIEvent.TAMER_RETRY_CLAIM.ordinal()] = 34;
            } catch (NoSuchFieldError e36) {
            }
            try {
                iArr[UIEvent.TEAM_ADD_MOGA.ordinal()] = 28;
            } catch (NoSuchFieldError e37) {
            }
            try {
                iArr[UIEvent.TEAM_BUY_MOGA.ordinal()] = 33;
            } catch (NoSuchFieldError e38) {
            }
            try {
                iArr[UIEvent.TEAM_DONE.ordinal()] = 29;
            } catch (NoSuchFieldError e39) {
            }
            try {
                iArr[UIEvent.TEAM_MOGA_INFO.ordinal()] = 27;
            } catch (NoSuchFieldError e40) {
            }
            try {
                iArr[UIEvent.TEAM_NAP_MOGA.ordinal()] = 31;
            } catch (NoSuchFieldError e41) {
            }
            try {
                iArr[UIEvent.TEAM_REMOVE_MOGA.ordinal()] = 30;
            } catch (NoSuchFieldError e42) {
            }
            try {
                iArr[UIEvent.TEAM_WAKE_UP_MOGA.ordinal()] = 32;
            } catch (NoSuchFieldError e43) {
            }
            try {
                iArr[UIEvent.THROW_MASTERSEED.ordinal()] = 5;
            } catch (NoSuchFieldError e44) {
            }
            try {
                iArr[UIEvent.THROW_STARSEED.ordinal()] = 4;
            } catch (NoSuchFieldError e45) {
            }
            try {
                iArr[UIEvent.TWITTER.ordinal()] = 12;
            } catch (NoSuchFieldError e46) {
            }
            try {
                iArr[UIEvent.ZODIAC_BUTTON.ordinal()] = 6;
            } catch (NoSuchFieldError e47) {
            }
            try {
                iArr[UIEvent.ZODIAC_DIALER_COMPLETED.ordinal()] = 16;
            } catch (NoSuchFieldError e48) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent = iArr;
        }
        return iArr;
    }

    public BattleScreen(Battle battle) {
        instance = this;
        this.battle = battle;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        super.onEnter(prevScreen);
        this.trainer = Game.getTrainer();
        BattleAssets.load();
        this.root.addBackground(this.trainer.getCurrentNode().getBackgroundAsset());
        initialize();
    }

    public static BattleScreen getInstance() {
        return instance;
    }

    private void initialize() {
        Gdx.app.debug(METHOD_TAG, "initialize()");
        this.battle.addEventListener(this);
        this.battle.init();
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.playerTeamHud = new TeamHud(true);
        this.playerTeamHud.setListener(this);
        this.playerTeamHud.setTeam(this.trainer.getTeam());
        this.playerTeamHud.setRival(this.battle.getComputerMoga());
        this.cpuTeamHud = new TeamHud(false);
        this.cpuTeamHud.setTeam(this.trainer.getCurrentNode().getTeam());
        this.cpuTeamHud.close(false, 0);
        this.leftHud = new Hud(this);
        this.root.add(this.leftHud);
        this.rightHud = new Hud(null);
        this.rightHud.flip();
        this.rightHud.setMoga(this.battle.getComputerMoga(), null);
        this.root.add(this.rightHud);
        this.attackMetter = getAttackMeter();
        this.attackMetter.setVisible(false);
        this.root.add(this.attackMetter);
        this.zodiacAttackMeter = new ZodiacAttackMeter(this);
        this.zodiacAttackMeter.setVisible(false);
        this.root.add(this.zodiacAttackMeter);
        this.attackButtons = new AttackButtons(this);
        this.attackButtons.setComputerMoga(this.battle.getComputerMoga());
        this.attackButtons.setVisible(false);
        this.root.add(this.attackButtons);
        this.pauseButton = new PauseButton(this);
        this.root.add(this.pauseButton);
        this.backpackButton = new BackpackButton(this);
        this.root.add(this.backpackButton);
        this.mogaStage = new MogaStage(this);
        this.mogaStage.setCpuMoga(this.battle.getComputerMoga());
        this.stage.addActor(this.mogaStage.getGroup());
        this.stage.addActor(this.playerTeamHud);
        this.stage.addActor(this.cpuTeamHud);
        Encounter currentEncounter = this.trainer.getCurrentNode().getEncounter();
        this.currentEncounterId = currentEncounter.getId();
        this.showAttackTutorial = this.currentEncounterId == 504;
        SoundManager.playMusic(MusicManager.getAvailableRandomMusic(currentEncounter.getMusicType()));
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeTextView();
        }
        showTeamHud(true);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
        Gdx.app.debug(METHOD_TAG, "onBackKeyPressed()");
        if (!this.isWaitingForAnimation && !this.showAttackTutorial) {
            if (isBattlePaused()) {
                resumeBattle();
            } else if (canPause()) {
                pauseBattle();
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onMenuKeyPressed() {
        Gdx.app.debug(METHOD_TAG, "onMenuKeyPressed()");
        if (!this.isWaitingForAnimation) {
            if (isBattlePaused()) {
                resumeBattle();
            } else if (canPause()) {
                pauseBattle();
            }
        }
    }

    private AttackMeter getAttackMeter() {
        Gdx.app.debug(METHOD_TAG, "getAttackMeter()");
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            AttackMeter attackMeter = new RoundedAttackMeter(this);
            return attackMeter;
        }
        AttackMeter attackMeter2 = new LinealAttackMeter(this, this.stage);
        return attackMeter2;
    }

    private Dialog getAttackTutorial() {
        Gdx.app.debug(METHOD_TAG, "getAttackTutorial()");
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.battle.BattleScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                BattleScreen.this.hideDialog();
                BattleScreen.this.attackButtons.startTimer();
                BattleScreen.this.showAttackTutorial = false;
            }
        };
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            Dialog attackTutorial = new AttackTutorialSkin1(listener);
            return attackTutorial;
        }
        Dialog attackTutorial2 = new AttackTutorialSkin2(listener);
        return attackTutorial2;
    }

    private void showTeamHud(boolean showAll) {
        Gdx.app.debug(METHOD_TAG, "showTeamHud(" + showAll + ")");
        this.pauseButton.setVisible(false);
        this.playerTeamHud.open(true, showAll ? -1 : getMogaIndex(this.battle.getPlayerMoga()));
        this.playerTeamHud.setVisible(true);
        this.leftHud.setVisible(!showAll);
        if (this.playerTeamHud.getMogaCount() > 1) {
            this.leftHud.showArrowDown();
        } else {
            this.leftHud.hideArrow();
        }
        this.attackButtons.setVisible(false);
        this.backpackButton.setVisible(false);
    }

    private void hideTeamHud(int deployedMogaIndex) {
        Gdx.app.debug(METHOD_TAG, "hideTeamHud(" + deployedMogaIndex + ")");
        this.playerTeamHud.close(true, deployedMogaIndex);
        if (this.trainer.getTeam().getMogas().size() > 1) {
            if (this.playerTeamHud.getMogaCount() > 1) {
                this.leftHud.showArrowRight();
            } else {
                this.leftHud.hideArrow();
            }
        }
    }

    private int getMogaIndex(Moga moga) {
        Gdx.app.debug(METHOD_TAG, "getMogaIndex(Moga {" + moga + "})");
        return this.trainer.getTeam().getMogas().indexOf(moga);
    }

    private void chooseMoga(Moga moga) {
        Gdx.app.debug(METHOD_TAG, "chooseMoga(Moga {" + moga + "})");
        if (moga.getHitPoints() > 0) {
            hideTeamHud(getMogaIndex(moga));
            SoundManager.loadSound(SoundManager.SOUND_BATLE_LOST);
        }
    }

    private void pauseBattle() {
        Gdx.app.debug(METHOD_TAG, "pauseBattle()");
        BattlePausedDialog battlePausedDialog = new BattlePausedDialog(this);
        battlePausedDialog.setRunawayEnabled(this.isPlayerTurn);
        showDialog(battlePausedDialog);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.TeamHud.Listener
    public void onTeamHudClose(boolean finished, Integer selectedMogaIndex) {
        Gdx.app.debug(METHOD_TAG, "onTeamHudClose(" + finished + ", " + selectedMogaIndex + ")");
        if (this.teamHudHintArrow != null) {
            this.teamHudHintArrow.setVisible(false);
        }
        if (finished) {
            this.leftHud.setVisible(true);
            if (selectedMogaIndex == null) {
                this.pauseButton.setVisible(true);
                this.attackButtons.setVisible(true);
                this.backpackButton.setVisible(true);
            }
            if (selectedMogaIndex != null) {
                Moga moga = this.trainer.getTeam().getMogas().get(selectedMogaIndex.intValue());
                this.mogaStage.setPlayerMoga(moga);
                ZodiacSign playerSign = moga.getType().getZodiacSign();
                ZodiacSign computerSign = this.battle.getComputerMoga().getType().getZodiacSign();
                ZodiacPowerType leftType = ZodiacHelper.getInstance().getPowerTypeAgainst(playerSign, computerSign);
                ZodiacPowerType rightType = ZodiacHelper.getInstance().getPowerTypeAgainst(computerSign, playerSign);
                this.leftHud.setMoga(moga, leftType);
                this.cpuTeamHud.setRival(moga);
                this.rightHud.setZodiacPowerType(rightType);
                this.attackButtons.setPlayerMoga(moga);
                this.attackButtons.updatePowerType(leftType);
                this.mogaStage.enterPlayerMoga();
                this.battle.deployPlayerMoga(moga);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.battle.TeamHud.Listener
    public void onTeamHudOpen(boolean finished) {
        Gdx.app.debug(METHOD_TAG, "onTeamHudOpen(" + finished + ")");
        boolean showArrow = (this.trainer.isNodeCompleted(2) && this.trainer.isNodeCompleted(4)) ? false : true;
        if (finished && this.teamHudHintArrow == null && showArrow) {
            addTeamHudHintArrow();
        }
    }

    private void addTeamHudHintArrow() {
        Gdx.app.debug(METHOD_TAG, "addTeamHudHintArrow()");
        Image arrow = new Image(Assets.loadTexture("arrow.up"));
        arrow.setOrigin(arrow.getWidth() / 2.0f, arrow.getHeight() / 2.0f);
        float scaleFactor = ResolutionManager.getScaleFactor();
        ScaleToAction scaleUp = new ScaleToAction();
        scaleUp.setScale(1.2f);
        scaleUp.setDuration(0.5f);
        ScaleToAction scaleDown = new ScaleToAction();
        scaleDown.setScale(1.0f);
        scaleDown.setDuration(0.5f);
        this.teamHudHintArrow = new ActorScreenElement();
        arrow.setScale(scaleFactor);
        arrow.setAlign(1);
        this.teamHudHintArrow.setActor(arrow);
        this.teamHudHintArrow.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.teamHudHintArrow.setPosition(ScreenElement.RelPoint.LEFT_TOP, (41.0f / ResolutionManager.getHorizontalAR()) * scaleFactor, -177.0f);
        this.root.add(this.teamHudHintArrow);
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(scaleUp);
        sequence.addAction(scaleDown);
        RepeatAction forever = new RepeatAction();
        forever.setCount(-1);
        forever.setAction(sequence);
        arrow.addAction(forever);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.mogaStage.getGroup().paused = isBattlePaused();
        if (this.dialog != null) {
            this.root.update(deltaTime);
            this.dialog.update(deltaTime);
        }
    }

    private boolean isBattlePaused() {
        boolean dialogPause = (this.dialog == null || this.dialog.isKeepingAnimation()) ? false : true;
        return (dialogPause || this.playerTeamHud.isOpen()) && this.battle.getState() != Battle.State.MOGA_CHOICE;
    }

    private void resumeBattle() {
        Gdx.app.debug(METHOD_TAG, "resumeBattle()");
        boolean visible = this.battle.getState() == Battle.State.ATTACK_CHOICE && this.queuedEvent == null && !this.mogaStage.isAnimationRunning();
        this.attackButtons.setVisible(visible);
        this.backpackButton.setVisible(visible);
        hideDialog();
    }

    private boolean canPause() {
        Gdx.app.debug(METHOD_TAG, "canPause()");
        return (!this.pauseButton.isVisible() || isBattlePaused() || this.drinkingBlueCoffee) ? false : true;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onApplicationPause() {
        Gdx.app.debug(METHOD_TAG, "onApplicationPause()");
        if (canPause()) {
            pauseBattle();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        Gdx.app.debug(METHOD_TAG, "onPause()");
        this.needToReloadAssets = true;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        Gdx.app.debug(METHOD_TAG, "onResume()");
        Gdx.input.setInputProcessor(this.stage);
        if (this.needToReloadAssets) {
            BattleAssets.load();
            this.needToReloadAssets = false;
        }
        this.root.addBackground(Game.getTrainer().getCurrentNode().getBackgroundAsset());
        this.mogaStage.reloadMogaTextures();
        if (this.dialog != null) {
            this.dialog.show();
        }
        if (this.zodiacAttackMeter != null && this.zodiacAttackMeter.isVisible()) {
            this.zodiacAttackMeter.resume();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void dispose() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void showDialog(Dialog dialog) {
        Gdx.app.debug(METHOD_TAG, "showDialog(" + dialog + ")");
        this.attackButtons.setVisible(false);
        this.backpackButton.setVisible(false);
        super.showDialog(dialog);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        Gdx.app.debug(METHOD_TAG, "onUIEvent(" + event + ")");
        this.pauseButton.setVisible(true);
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent()[event.ordinal()]) {
            case 1:
                if (canPause() && !this.isWaitingForAnimation) {
                    pauseBattle();
                    break;
                }
                break;
            case 2:
                playerTurnEnded();
                this.pauseButton.setVisible(false);
                this.backpackButton.setVisible(false);
                this.attackButtons.setVisible(false);
                this.battle.setAttackType(AttackType.REGULAR);
                break;
            case 3:
                Moga cpuMoga = this.battle.getComputerMoga();
                if (this.trainer.canCaptureMoga(cpuMoga)) {
                    showDialog(new CaptureDialog(this, cpuMoga, this.battle.getCaptureRate(), this.currentEncounterId));
                } else {
                    String message = null;
                    if (this.trainer.getCurrentNode().getEncounter().isCaptureEnabled()) {
                        message = I18nManager.getText(I18nManager.I18nKey.CAPTURE_TOO_WILD);
                    } else if (this.trainer.hasMogasOfType(cpuMoga.getType())) {
                        message = I18nManager.getText(I18nManager.I18nKey.CAPTURE_ALREADY_CAPTURED);
                    } else if (this.trainer.getTeam().getLevel() < cpuMoga.getLevel()) {
                        message = I18nManager.getText(I18nManager.I18nKey.CAPTURE_TOO_STRONG);
                    } else if (cpuMoga.canBeCaptured()) {
                        message = I18nManager.getText(I18nManager.I18nKey.CAPTURE_CAN_BE_CAPTURED);
                    }
                    if (message != null) {
                        showDialog(new AlertDialog(this, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.RESUME_BUTTON, UIEvent.RESUME_BUTTON));
                    }
                }
                break;
            case 4:
                playerTurnEnded();
                this.isWaitingForAnimation = true;
                hideDialog();
                this.battle.requestThrowSeed();
                break;
            case 5:
                playerTurnEnded();
                this.isWaitingForAnimation = true;
                hideDialog();
                this.battle.requestThrowMasterSeed();
                break;
            case 6:
                playerTurnEnded();
                this.pauseButton.setVisible(false);
                this.backpackButton.setVisible(false);
                this.attackButtons.setVisible(false);
                this.battle.setAttackType(AttackType.ZODIAC);
                break;
            case 7:
                showOrHideBackPackMenu();
                break;
            case 8:
                playerTurnEnded();
                this.backpackButton.setVisible(false);
                this.attackButtons.setVisible(false);
                this.battle.playerTimedOut();
                break;
            case 9:
                this.attackMetter.setVisible(false);
                this.meterStrength = this.attackMetter.getDamageMultiplier();
                this.mogaStage.attack(0, false);
                break;
            case 10:
                playerTurnEnded();
                this.attackMetter.setVisible(false);
                this.battle.playerTimedOut();
                break;
            case 16:
                this.zodiacAttackMeter.setVisible(false);
                if (this.zodiacAttackMeter.isSuccess()) {
                    this.meterStrength = this.zodiacAttackMeter.getDamageMultiplier();
                    this.mogaStage.attack(0, true);
                } else {
                    this.battle.playerTimedOut();
                    this.mogaStage.stopZodiacGlow();
                }
                break;
            case 17:
                this.battle.playerAttack(this.meterStrength);
                if (this.battle.getAttackType().equals(AttackType.REGULAR) && 1.0f - this.meterStrength < Constants.physicalPerfectRange) {
                    this.root.doShake();
                }
                this.rightHud.updateHealthAnimated();
                break;
            case 18:
                this.battle.computerAttack(this.cpuAttackType, 1.0f);
                this.leftHud.updateHealthAnimated();
                break;
            case 19:
                finish();
                break;
            case 20:
                recharge();
                break;
            case 21:
                showDialog(new BattleEndedDialog(this, false, null));
                break;
            case 22:
                if (this.queuedEvent != null) {
                    Gdx.app.debug(METHOD_TAG, "ANIMATIONS_ENDED -> queued event to process: " + this.queuedEvent);
                    onBattleEvent(this.queuedEvent);
                }
                break;
            case 23:
                MonsterGalaxy.showScreen(ScreenCode.SHOP, false, false);
                break;
            case 24:
                this.root.add(Assets.loadLocalizedTexture("run.away"), 110.0f, -100.0f, ScreenElement.RelPoint.LEFT_TOP);
                playerTurnEnded();
                this.backpackButton.setVisible(false);
                this.attackButtons.setVisible(false);
                hideDialog();
                this.battle.requestRunaway();
                String currentIslandId = String.valueOf(this.trainer.getCurrentIsland().getId());
                Game.getAnalytics().trackEvent("encounters", "run", String.valueOf(this.currentEncounterId), "encounter_run", "", currentIslandId);
                break;
            case 25:
                resumeBattle();
                break;
            case Input.Keys.POWER /* 26 */:
                if (!this.attackMetter.isVisible() && this.playerTeamHud.getMogaCount() > 1 && this.battle.getState() == Battle.State.ATTACK_CHOICE && !this.mogaStage.isMogaEntering()) {
                    if (this.playerTeamHud.isOpen()) {
                        hideTeamHud(getMogaIndex(this.battle.getPlayerMoga()));
                    } else if (this.isPlayerTurn) {
                        showTeamHud(false);
                    }
                    break;
                }
                break;
            case Input.Keys.I /* 37 */:
                if (this.battle.getPlayerMoga().isWounded()) {
                    if (this.trainer.getBlueCoffeeCups() > 0) {
                        onBlueCoffee(this.trainer.getTeam().getMogas().indexOf(this.battle.getPlayerMoga()));
                    } else {
                        showShopDialog();
                    }
                }
                break;
            case Input.Keys.J /* 38 */:
                this.isWaitingForAnimation = false;
                showDialog(NameMogaDialog.newInstance(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.battle.BattleScreen.2
                    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                    public void onUIEvent(UIEvent event2) {
                        BattleScreen.this.hideDialog();
                        BattleScreen.this.battle.mogaWasCaptured();
                    }
                }, this.battle.getComputerMoga()));
                MonsterGalaxy.getAnalytics().sessionMLogEvent(Analytics.CAPTURE_MOGA_SESSIONM_EVENT);
                break;
            case Input.Keys.K /* 39 */:
                this.isWaitingForAnimation = false;
                playerTurnEnded();
                this.battle.setComputerTurn();
                break;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.model.battle.Battle.Listener
    public void onBattleEvent(Battle.EventType event) {
        Gdx.app.debug(METHOD_TAG, "onBattleEvent(" + event + ")");
        if (this.mogaStage.isAnimationRunning() || this.mogaStage.isMogaEntering()) {
            Gdx.app.debug(METHOD_TAG, "queuing event: " + event);
            this.queuedEvent = event;
        }
        this.queuedEvent = null;
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$battle$Battle$EventType()[event.ordinal()]) {
            case 1:
                if (this.showAttackTutorial) {
                    showDialog(getAttackTutorial());
                } else {
                    this.attackButtons.startTimer();
                }
                playerTurnStarted();
                this.pauseButton.setVisible(true);
                this.backpackButton.setVisible(true);
                this.mogaStage.hideRarity();
                break;
            case 2:
                this.pauseButton.setVisible(false);
                this.attackButtons.setVisible(false);
                this.attackMetter.setVisible(true);
                this.attackMetter.start();
                break;
            case 3:
                this.pauseButton.setVisible(false);
                this.attackButtons.setVisible(false);
                this.zodiacAttackMeter.setVisible(true);
                this.mogaStage.zodiacGlow();
                this.zodiacAttackMeter.start();
                break;
            case 4:
                if (this.battle.isRunningAway()) {
                    finish();
                } else {
                    battleEnded();
                }
                break;
            case 5:
                playerTurnEnded();
                this.mogaStage.runAway();
                this.queuedEvent = Battle.EventType.PLAYER_KILLED;
                break;
            case 7:
                this.cpuAttackType = this.battle.getRandomComputerAttackType();
                this.mogaStage.attack(1, this.cpuAttackType == AttackType.ZODIAC);
                break;
            case 8:
                battleEnded();
                break;
            case 9:
                Moga moga = this.trainer.getCurrentNode().getTeam().getNextMoga();
                this.mogaStage.setCpuMoga(moga);
                ZodiacSign playerSign = this.battle.getPlayerMoga().getType().getZodiacSign();
                ZodiacSign computerSign = moga.getType().getZodiacSign();
                ZodiacPowerType rightType = ZodiacHelper.getInstance().getPowerTypeAgainst(computerSign, playerSign);
                ZodiacPowerType leftType = ZodiacHelper.getInstance().getPowerTypeAgainst(playerSign, computerSign);
                this.attackButtons.updatePowerType(leftType);
                this.leftHud.setZodiacPowerType(leftType);
                this.rightHud.setMoga(moga, rightType);
                this.playerTeamHud.setRival(moga);
                this.cpuTeamHud.setTeam(this.trainer.getCurrentNode().getTeam());
                this.cpuTeamHud.close(false, 0);
                this.mogaStage.enterComputerMoga();
                this.battle.deployComputerMoga(moga);
                this.attackButtons.setComputerMoga(moga);
                break;
            case 11:
                this.mogaStage.capture(true);
                break;
            case 12:
                this.mogaStage.capture(false);
                break;
            case 13:
                showDialog(new AlertDialog(this, I18nManager.getText(I18nManager.I18nKey.DONT_HAVE_STARSEED), I18nManager.getText(I18nManager.I18nKey.VISIT_SHOP), UIEvent.SKY_SHOP_BUTTON, UIEvent.RESUME_BUTTON));
                break;
            case 14:
                showDialog(new AlertDialog(this, I18nManager.getText(I18nManager.I18nKey.DONT_HAVE_STARSEED), I18nManager.getText(I18nManager.I18nKey.VISIT_SHOP), UIEvent.SKY_SHOP_BUTTON, UIEvent.RESUME_BUTTON));
                break;
        }
    }

    private void playerTurnStarted() {
        Gdx.app.debug(METHOD_TAG, "playerTurnStarted()");
        this.isPlayerTurn = true;
    }

    private void playerTurnEnded() {
        Gdx.app.debug(METHOD_TAG, "playerTurnEnded()");
        this.isPlayerTurn = false;
    }

    private void showOrHideBackPackMenu() {
        Gdx.app.debug(METHOD_TAG, "showOrHideBackPackMenu()");
        if (this.backpackButton.isMenuOpen()) {
            this.backpackButton.closeMenu();
        } else {
            this.backpackButton.showMenu();
        }
        this.attackButtons.setVisible(!this.backpackButton.isMenuOpen());
    }

    private void recharge() {
        Gdx.app.debug(METHOD_TAG, "recharge()");
        hideDialog();
        Moga playerMoga = this.battle.getPlayerMoga();
        if (this.trainer.applyBlueCoffee(playerMoga)) {
            SoundManager.loadSound(SoundManager.SOUND_BLUE_COFFEE);
            SoundManager.playSound(SoundManager.SOUND_BLUE_COFFEE);
            this.mogaStage.removePlayerMoga();
            this.mogaStage.setCpuMoga(this.battle.getComputerMoga());
            this.mogaStage.setPlayerMoga(playerMoga);
            this.mogaStage.enterPlayerMoga();
            this.leftHud.updateHealth();
            this.battle.deployPlayerMoga(playerMoga);
            String currentIslandId = String.valueOf(this.trainer.getCurrentIsland().getId());
            Game.getAnalytics().trackEvent("monetization", "use_item", "blue_coffee", "blue_coffee_popup", "", currentIslandId);
        }
    }

    private void battleEnded() {
        Gdx.app.debug(METHOD_TAG, "battleEnded()");
        this.pauseButton.setVisible(false);
        this.mogaStage.freezeMogas();
        BattleReport report = this.trainer.getBattleReport();
        if (!report.playerHasWon() && this.trainer.getTeam().hasHealtyMogas()) {
            this.mogaStage.removePlayerMoga();
            showTeamHud(true);
            return;
        }
        if (!report.playerHasWon() && !this.trainer.getTeam().hasHealtyMogas() && this.trainer.getBlueCoffeeCups() > 0) {
            showRechargeDialog();
            return;
        }
        this.trainer.battleOver();
        if (report.playerHasWon()) {
            this.mogaStage.makePlayerMogaIdle();
        }
        int capturedMogaCount = report.getCapturedMogas().size();
        Moga capturedMoga = capturedMogaCount == 0 ? null : report.getCapturedMogas().get(capturedMogaCount - 1);
        showDialog(new BattleEndedDialog(this, report.playerHasWon(), capturedMoga));
    }

    private void finish() {
        Gdx.app.debug(METHOD_TAG, "finish()");
        if (this.battle.isRunningAway()) {
            MonsterGalaxy.backToIslandFromBattleFlow();
        } else if (!this.trainer.getTeam().hasHealtyMogas()) {
            MonsterGalaxy.showScreen(ScreenCode.TEAM);
        } else {
            this.trainer.getBattleReport().update();
            MonsterGalaxy.showScreen(new BattleReportScreen());
        }
    }

    private void showRechargeDialog() {
        Gdx.app.debug(METHOD_TAG, "showRechargeDialog()");
        String message = I18nManager.getText(I18nManager.I18nKey.MOGA_TIRED);
        AlertDialog dialog = new AlertDialog(this, message, I18nManager.getText(I18nManager.I18nKey.RECHARGE), UIEvent.RECHARGE_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    public void showShopDialog() {
        Gdx.app.debug(METHOD_TAG, "showShopDialog()");
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.battle.BattleScreen.3
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                Gdx.app.debug(BattleScreen.METHOD_TAG, "showShopDialog() ScreenListener.onUIEvent(" + event + ")");
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.SHOP, false, false);
                }
                BattleScreen.this.hideDialog();
                BattleScreen.this.attackButtons.setVisible(true);
                BattleScreen.this.backpackButton.setVisible(true);
                BattleScreen.this.backpackButton.closeMenu();
            }
        };
        String message = I18nManager.getText(I18nManager.I18nKey.DONT_HAVE_BLUECOFFE);
        showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.VISIT_SHOP), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON));
    }

    public void doEffect(EffectType effectType, float x, float y) {
        Gdx.app.debug(METHOD_TAG, "doEffect(" + effectType + ", " + x + ", " + y + ")");
        this.effect = EffectManager.getInstance().getEffect(effectType);
        this.effect.setPosition(x, y);
        this.effect.start();
    }

    public Stage getStage() {
        return this.stage;
    }

    public Battle getBattle() {
        return this.battle;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void hideDialog() {
        Gdx.app.debug(METHOD_TAG, "hideDialog()");
        super.hideDialog();
    }

    @Override // com.gaiaonline.monstergalaxy.battle.TeamHud.Listener
    public void onMogaSelected(int mogaIndex) {
        Gdx.app.debug(METHOD_TAG, "onMogaSelected(" + mogaIndex + ")");
        chooseMoga(this.trainer.getTeam().getMogas().get(mogaIndex));
    }

    @Override // com.gaiaonline.monstergalaxy.battle.TeamHud.Listener
    public void onBlueCoffee(int mogaIndex) {
        Gdx.app.debug(METHOD_TAG, "onBlueCoffee(" + mogaIndex + ")");
        Moga moga = this.trainer.getTeam().getMogas().get(mogaIndex);
        if (moga.getHitPoints() < moga.getMaxHitPoints() && Game.getTrainer().applyBlueCoffee(moga)) {
            if (this.playerTeamHud.isOpen()) {
                this.playerTeamHud.blueCoffeeApplied(mogaIndex);
                return;
            }
            Actor mogaActor = this.mogaStage.getGroup().findActor(moga.getName());
            if (mogaActor != null) {
                this.drinkingBlueCoffee = true;
                BlueCoffeeEffect blueCoffeeEffect = new BlueCoffeeEffect("bc");
                blueCoffeeEffect.setX(mogaActor.getX() + (mogaActor.getWidth() / 2.0f));
                blueCoffeeEffect.setY(mogaActor.getY() + (mogaActor.getHeight() / 2.0f));
                blueCoffeeEffect.setOnCompleteListener(new Effect.OnCompleteListener() { // from class: com.gaiaonline.monstergalaxy.battle.BattleScreen.4
                    @Override // com.gaiaonline.monstergalaxy.battle.effects.Effect.OnCompleteListener
                    public void onComplete() {
                        BattleScreen.this.attackButtons.setVisible(!BattleScreen.this.backpackButton.isMenuOpen());
                        BattleScreen.this.backpackButton.setVisible(BattleScreen.this.backpackButton.isMenuOpen() ? false : true);
                        BattleScreen.this.drinkingBlueCoffee = false;
                    }
                });
                this.stage.addActor(blueCoffeeEffect);
                blueCoffeeEffect.play();
            }
            this.backpackButton.closeMenu();
            this.backpackButton.setVisible(false);
            this.leftHud.updateHealth();
        }
    }
}
