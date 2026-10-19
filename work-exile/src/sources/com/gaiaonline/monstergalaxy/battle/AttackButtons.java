package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.PieMeshElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AttackButtons extends ButtonUI {
    private ActorScreenElement arrowElement;
    private ButtonElement attackButton;
    private boolean attackHint;
    private TextElement attackText;
    private ButtonElement captureButton;
    private boolean captureHint;
    private Moga computerMoga;
    private Moga playerMoga;
    private PieMeshElement progressMesh;
    private Timer timer;
    private ButtonElement zodiacButton;
    private TextureElement zodiacSign;
    private TextElement zodiacText;

    public AttackButtons(BattleScreen screen) {
        super(screen);
        setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        setStretchable(false);
        setSize(480.0f, 320.0f);
        int currentEncounterId = Game.getTrainer().getCurrentNode().getEncounter().getId();
        this.captureHint = currentEncounterId == 456 || currentEncounterId == 458;
        this.attackHint = currentEncounterId == 504;
        this.captureButton = addButton(UIEvent.CAPTURE_BUTTON, Assets.captureBtn, Assets.capturePressedBtn, Assets.captureDisabledBtn, 0.0f, 42.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        this.attackButton = addButton(UIEvent.ATTACK_BUTTON, Assets.attackBtn, Assets.attackPressedBtn, -104.0f, 30.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        this.attackText = addLabel("Attack", 55.0f, -23.0f, ScreenElement.RelPoint.LEFT_TOP, this.attackButton, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        this.zodiacButton = addButton(UIEvent.ZODIAC_BUTTON, Assets.zodiacBtn, Assets.zodiacPressedBtn, Assets.zodiacDisabledBtn, 104.0f, 30.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        this.zodiacText = addLabel("Zodiac", 94.0f, -23.0f, ScreenElement.RelPoint.LEFT_TOP, this.zodiacButton, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        this.progressMesh = new PieMeshElement(33.0f);
        this.progressMesh.setPosition(ScreenElement.RelPoint.LEFT_TOP, 37.0f, -24.5f);
        this.progressMesh.setAlign(this.zodiacButton);
        add(this.progressMesh);
        this.zodiacSign = add(Assets.littleBlueCircle, 65.0f, 33.5f, ScreenElement.RelPoint.CENTER_BOTTOM, ScreenElement.RelPoint.CENTER);
        add(Assets.loadTexture("attackturn"), 0.0f, 40.0f);
        this.timer = new Timer(getSecondsForTimer(currentEncounterId));
        this.timer.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 260.0f);
        add(this.timer);
        if (this.attackHint) {
            this.arrowElement = getArrowActor("arrow.downleft");
            this.arrowElement.setPosition(ScreenElement.RelPoint.CENTER, -30.0f, -85.0f);
            add(this.arrowElement);
        } else if (this.captureHint) {
            this.arrowElement = getArrowActor("down.arrow");
            this.arrowElement.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, -47.0f);
            this.arrowElement.setVisible(false);
            add(this.arrowElement);
        }
        boolean hideZodiacButton = currentEncounterId == 456 || currentEncounterId == 458 || currentEncounterId == 460 || currentEncounterId == 504;
        if (hideZodiacButton) {
            hideZodiacAttackButton();
        }
    }

    private int getSecondsForTimer(int encounterId) {
        if (encounterId == 456 || encounterId == 504) {
            return 10;
        }
        if (encounterId == 458 || encounterId == 460) {
            return 8;
        }
        if (encounterId == 462) {
            return 6;
        }
        return 4;
    }

    private ActorScreenElement getArrowActor(String arrowAsset) {
        ActorScreenElement arrowElement = new ActorScreenElement();
        Image attackArrow = new Image("attackArrow", Assets.loadTexture(arrowAsset));
        ScaleTo scaleUp = ScaleTo.$(1.2f, 1.2f, 0.5f);
        ScaleTo scaleDown = ScaleTo.$(1.0f, 1.0f, 0.5f);
        attackArrow.action(Forever.$(Sequence.$(scaleUp, scaleDown)));
        ResolutionManager.scaleActor(attackArrow);
        arrowElement.setActor(attackArrow);
        return arrowElement;
    }

    public void startTimer() {
        this.visible = true;
        this.timer.start();
    }

    public void setPlayerMoga(Moga playerMoga) {
        this.playerMoga = playerMoga;
        this.zodiacSign.setTexture(Assets.getZodiacSignAsset(playerMoga.getType().getZodiacSign()));
        updatePowerType();
    }

    public void setComputerMoga(Moga computerMoga) {
        this.computerMoga = computerMoga;
        updatePowerType();
    }

    private void updatePowerType() {
        if (this.playerMoga != null && this.computerMoga != null) {
            ZodiacPowerType zodiacPowerType = ZodiacHelper.getInstance().getPowerTypeAgainst(this.playerMoga.getType().getZodiacSign(), this.computerMoga.getType().getZodiacSign());
            this.progressMesh.setTexture(new Texture(Gdx.files.internal(zodiacPowerType.getAssetName())));
        }
    }

    public void updatePowerType(ZodiacPowerType powertype) {
        if (this.playerMoga != null && this.computerMoga != null) {
            this.progressMesh.setTexture(new Texture(Gdx.files.internal(powertype.getAssetName())));
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        boolean z = false;
        super.update(deltaTime);
        Trainer trainer = Game.getTrainer();
        boolean canCapture = trainer.canCaptureMoga(this.computerMoga);
        this.captureButton.setGreyedOut(!canCapture);
        ButtonElement buttonElement = this.zodiacButton;
        if (this.playerMoga != null && this.playerMoga.getZodiacCharge() >= 100.0f && trainer.isNodeCompleted(2)) {
            z = true;
        }
        buttonElement.setEnabled(z);
        this.progressMesh.setCompletionRate(this.playerMoga.getZodiacCharge() / 100.0f);
        if (this.timer.isCompleted()) {
            this.screenListener.onUIEvent(UIEvent.ATTACK_TIMEOUT);
        }
        if (canCapture && this.captureHint) {
            this.arrowElement.setVisible(true);
            hideAttackButton();
        }
    }

    private void hideAttackButton() {
        this.attackButton.setVisible(false);
        this.attackText.setVisible(false);
    }

    private void hideZodiacAttackButton() {
        this.zodiacButton.setVisible(false);
        this.zodiacText.setVisible(false);
        this.zodiacSign.setVisible(false);
        this.progressMesh.setVisible(false);
    }
}
