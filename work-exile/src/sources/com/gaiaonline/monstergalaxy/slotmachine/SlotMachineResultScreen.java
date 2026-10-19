package com.gaiaonline.monstergalaxy.slotmachine;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.EffectActor;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachinePrize;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SlotMachineResultScreen extends SlotMachineBaseScreen {
    public static boolean featureEnabled = true;

    public SlotMachineResultScreen(SlotMachinePrize prize) {
        super("Congrazzles! You just won " + prize.getName() + (prize.getMogaTypeId() == 0 ? " Bundle" : "") + ":", "slotmachine.bg.win");
        CardActor cardActor = new CardActor(prize, this);
        cardActor.x = (Gdx.graphics.getWidth() / 2) - (86.0f * ResolutionManager.getScaleFactor());
        cardActor.y = (((-15.0f) * ResolutionManager.getVerticalAR()) + (Gdx.graphics.getHeight() / 2)) - (96.0f * ResolutionManager.getScaleFactor());
        this.stage.addActor(cardActor);
        addEffect();
        SoundManager.loadSound(SoundManager.SOUND_MOGA_CASH);
        SoundManager.playSound(SoundManager.SOUND_MOGA_CASH);
    }

    private void addEffect() {
        EffectActor actor = new EffectActor("inventory");
        actor.setEffectWithType(EffectType.BLUE_STARS);
        ActorScreenElement blueStarsEffect = new ActorScreenElement();
        blueStarsEffect.setActor(actor);
        blueStarsEffect.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        blueStarsEffect.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        this.root.add(blueStarsEffect);
        actor.getEffect().start();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        if (this.dialog != null) {
            showDialog(this.dialog);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen
    public void onMainButtonPressed() {
        MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE);
    }

    @Override // com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen
    public void addUIComponents() {
        TextElement playText = new TextElement("Play Again", 0.75f, ColorConstants.PRIMARY_FONT_COLOR, false);
        playText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        playText.setAlign(this.mainButton);
        playText.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 5.0f);
        this.root.add(playText);
    }
}
