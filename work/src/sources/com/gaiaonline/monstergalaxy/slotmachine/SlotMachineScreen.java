package com.gaiaonline.monstergalaxy.slotmachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachinePrize;
import com.gaiaonline.monstergalaxy.model.minigame.Slotmachine;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class SlotMachineScreen extends SlotMachineBaseScreen implements CardActor.GlowCompletedListener {
    private static final int REEL_COUNT = 3;
    private static final int REEL_SPOTTING_COUNT = 9;
    public static boolean available;
    private static Slotmachine slotmachine;
    private List<CardActor> cardActors;
    private int[] cardSpottings;
    private Moga mogaWon;
    private boolean pendingPrizeRedeem;
    private List<SlotMachinePrize> sortedPrizes;
    private int spottingIndex;

    public SlotMachineScreen() {
        super(I18nManager.getText(I18nManager.I18nKey.PLAY_FOR_PRIZES), "slotmachine.bg");
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        if (this.pendingPrizeRedeem) {
            redeemPrize();
        }
    }

    private void redeemPrize() {
        Trainer trainer = Game.getTrainer();
        SlotMachinePrize prize = slotmachine.getChosenPrize();
        trainer.addCoins(prize.getMogaCash());
        trainer.addBlueCoffeeCups(prize.getBlueCoffees());
        trainer.addStarSeeds(prize.getStarSeeds());
        this.pendingPrizeRedeem = false;
        Game.save();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        if (!available) {
            showDownloadingAssets();
        }
        if (this.cardActors != null) {
            for (CardActor prize : this.cardActors) {
                prize.resume();
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen
    public void onMainButtonPressed() {
        if (available) {
            play();
        }
    }

    private void play() {
        if (Game.getTrainer().consumeCoins(slotmachine.getCost())) {
            this.mainButton.setEnabled(false);
            slotmachine.spin();
            this.pendingPrizeRedeem = true;
            this.cardSpottings = new int[9];
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                this.cardSpottings[i] = random.nextInt(3);
            }
            this.cardSpottings[8] = this.sortedPrizes.indexOf(slotmachine.getChosenPrize());
            SlotMachinePrize prize = slotmachine.getChosenPrize();
            if (prize.getMogaTypeId() > 0) {
                MogaType mogaType = Game.getStorage().getMogaType(prize.getMogaTypeId());
                Trainer trainer = Game.getTrainer();
                if (!trainer.hasMogasOfType(mogaType)) {
                    this.mogaWon = Moga.newMogaFromType(mogaType);
                    this.mogaWon.setTrainerMogasMaxLevel();
                    this.mogaWon.fullyRechargeHealth();
                    this.mogaWon.fullyRechargeZodiac();
                    trainer.getMogas().add(this.mogaWon);
                    if (trainer.getTeam().getMogas().size() < 3) {
                        trainer.getTeam().addMoga(this.mogaWon);
                    }
                }
            }
            glowCurrentCard();
            return;
        }
        showShopDialog();
    }

    private void showShopDialog() {
        String message = I18nManager.getText(I18nManager.I18nKey.NO_ENOUGH_MOGACASH);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.SlotMachineScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.SHOP, false, true);
                } else {
                    SlotMachineScreen.this.hideDialog();
                }
            }
        };
        showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.VISIT_SHOP), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showDownloadingAssets() {
        String message = I18nManager.getText(I18nManager.I18nKey.NO_LUCKYMOGA_AVAILABLE);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.SlotMachineScreen.2
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                MonsterGalaxy.showPreviousScreen();
            }
        };
        showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.OK), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    @Override // com.gaiaonline.monstergalaxy.slotmachine.CardActor.GlowCompletedListener
    public void cardGlowCompleted() {
        this.spottingIndex++;
        if (this.spottingIndex >= this.cardSpottings.length) {
            if (this.mogaWon != null) {
                showDialog(NameMogaDialog.newInstance(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.SlotMachineScreen.3
                    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                    public void onUIEvent(UIEvent event) {
                        SlotMachineScreen.this.hideDialog();
                        SlotMachineScreen.this.showResultsScreen();
                    }
                }, this.mogaWon));
                return;
            } else {
                showResultsScreen();
                return;
            }
        }
        glowCurrentCard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showResultsScreen() {
        MonsterGalaxy.showScreen(new SlotMachineResultScreen(slotmachine.getChosenPrize()));
    }

    private void glowCurrentCard() {
        this.cardActors.get(this.cardSpottings[this.spottingIndex]).glow(this);
    }

    @Override // com.gaiaonline.monstergalaxy.slotmachine.SlotMachineBaseScreen
    public void addUIComponents() {
        if (slotmachine == null) {
            slotmachine = Game.getStorage().getSlotMachine();
        }
        if (available) {
            try {
                addCards();
            } catch (GdxRuntimeException e) {
                available = false;
                Gdx.app.log("Lucky Moga", e.getMessage());
            }
        }
        addMainButtonAssets();
    }

    private void addMainButtonAssets() {
        TextElement costText = new TextElement(String.valueOf(slotmachine.getCost()), 0.75f, ColorConstants.PRIMARY_FONT_COLOR, false);
        costText.setTextAlignment(BitmapFont.HAlignment.RIGHT);
        costText.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        costText.setAlign(this.mainButton);
        costText.setPosition(ScreenElement.RelPoint.CENTER, -40.0f, 5.0f);
        this.root.add(costText);
        TextureElement mcashIcon = new TextureElement(Assets.loadTexture("mcash"));
        mcashIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        mcashIcon.setAlign(this.mainButton);
        mcashIcon.setPosition(ScreenElement.RelPoint.CENTER, -20.0f, 3.0f);
        this.root.add(mcashIcon);
        TextElement playText = new TextElement(I18nManager.getText(I18nManager.I18nKey.PLAY), 0.75f, ColorConstants.PRIMARY_FONT_COLOR, false);
        playText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        playText.setAlign(this.mainButton);
        playText.setPosition(ScreenElement.RelPoint.CENTER, 3.0f, 5.0f);
        playText.enableHorizontalAutoSize(55.0f);
        this.root.add(playText);
    }

    private void addCards() {
        this.cardActors = new ArrayList();
        float CARD_PADDING = (-25.0f) * ResolutionManager.getScaleFactor();
        float CARD_MARGIN = (((480.0f * ResolutionManager.getHorizontalAR()) - ((ResolutionManager.getScaleFactor() * 172.0f) * 3.0f)) - (CARD_PADDING * 2.0f)) / 2.0f;
        this.sortedPrizes = new ArrayList();
        Comparator<SlotMachinePrize> comparator = new PrizeComparator(this, null);
        for (int i = 0; i < 3; i++) {
            this.sortedPrizes.add(slotmachine.getReels().get(i).getPrize());
        }
        Collections.sort(this.sortedPrizes, comparator);
        int i2 = 0;
        while (i2 < 3) {
            CardActor cardActor = new CardActor(this.sortedPrizes.get(i2), this);
            this.cardActors.add(cardActor);
            int y = i2 == 0 ? -30 : -15;
            int x = (int) ((i2 * ((ResolutionManager.getScaleFactor() * 172.0f) + CARD_PADDING)) + CARD_MARGIN);
            cardActor.setX(x);
            cardActor.setY(((y * ResolutionManager.getVerticalAR()) + (Gdx.graphics.getHeight() / 2)) - (96.0f * ResolutionManager.getScaleFactor()));
            cardActor.setRotation((1 - i2) * 5);
            this.stage.addActor(cardActor);
            i2++;
        }
    }

    private class PrizeComparator implements Comparator<SlotMachinePrize> {
        private PrizeComparator() {
        }

        /* synthetic */ PrizeComparator(SlotMachineScreen slotMachineScreen, PrizeComparator prizeComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(SlotMachinePrize prize1, SlotMachinePrize prize2) {
            int compare = prize1.getType().compareTo(prize2.getType());
            return compare * (-1);
        }
    }
}
