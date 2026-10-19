package com.gaiaonline.monstergalaxy.slotmachine;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.DelayedRunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TintToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.graphics.util.ActorUtil;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachinePrize;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;

/* JADX INFO: loaded from: classes.dex */
public class CardActor extends Group {
    public static final int CARD_HEIGHT = 193;
    public static final int CARD_WIDTH = 172;
    private Image cardBg;
    private boolean glowing;
    private ImageButton infoButton;
    private SlotMachinePrize prize;
    private Image prizeImage;
    private Group group = new Group();
    private Image glowingCardBg = new Image(Assets.loadTexture("slotmachine.card.selected"));

    public interface GlowCompletedListener {
        void cardGlowCompleted();
    }

    public CardActor(SlotMachinePrize prize, SlotMachineBaseScreen screen) {
        this.prize = prize;
        this.group.setWidth(this.glowingCardBg.getWidth());
        this.group.setHeight(this.glowingCardBg.getHeight());
        this.glowingCardBg.setY(8.0f);
        this.group.addActor(this.glowingCardBg);
        this.cardBg = new Image(Assets.loadTexture("slotmachine.card.unselected"));
        this.cardBg.setX((this.group.getWidth() - this.cardBg.getWidth()) / 2.0f);
        this.cardBg.setY((this.group.getHeight() - this.cardBg.getHeight()) / 2.0f);
        this.group.addActor(this.cardBg);
        addMogaInfoButton(screen);
        addPrize();
        this.glowingCardBg.setColor(1.0f, 1.0f, 1.0f, 0.0f);
        this.group.setScale(ResolutionManager.getScaleFactor());
        setWidth(this.group.getWidth() * this.group.getScaleX());
        setHeight(this.group.getHeight() * this.group.getScaleY());
        addActor(this.group);
    }

    private void addMogaInfoButton(final SlotMachineBaseScreen screen) {
        if (this.prize.getMogaTypeId() > 0) {
            TextureRegionDrawable normalInfoButton = new TextureRegionDrawable(Assets.infoButton);
            TextureRegionDrawable selectedInfoButton = new TextureRegionDrawable(Assets.infoPressedButton);
            this.infoButton = new ImageButton(normalInfoButton, selectedInfoButton);
            this.infoButton.addListener(new EventListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.CardActor.1
                @Override // com.badlogic.gdx.scenes.scene2d.EventListener
                public boolean handle(Event event) {
                    if (!(event instanceof InputEvent) || ((InputEvent) event).getType() != InputEvent.Type.touchDown) {
                        return false;
                    }
                    MogaType mogaType = Game.getStorage().getMogaType(CardActor.this.prize.getMogaTypeId());
                    screen.showMogaInfoDialog(mogaType);
                    return true;
                }
            });
            this.infoButton.setX(18.0f);
            this.infoButton.setY((-(this.infoButton.getHeight() / 2.0f)) + 47.0f);
            this.group.addActor(this.infoButton);
        }
    }

    private void addPrize() {
        int prizeIndex = -1;
        if (this.prize.getMogaTypeId() > 0) {
            MogaType mogaType = Game.getStorage().getMogaType(this.prize.getMogaTypeId());
            if (mogaType.getAsset() != null) {
                this.prizeImage = new Image(Assets.load(mogaType.getAsset()));
                ActorUtil.centerOrigin(this.prizeImage);
                this.prizeImage.setX((this.group.getWidth() - this.prizeImage.getWidth()) / 2.0f);
                this.prizeImage.setY(((this.group.getHeight() - this.prizeImage.getHeight()) / 2.0f) + 20.0f);
                this.prizeImage.setScale(0.72f);
                this.group.addActor(this.prizeImage);
            }
            Image rarityElement = new Image(Assets.loadLocalizedTexture(mogaType.getRarityAssetName()));
            ActorUtil.centerOrigin(rarityElement);
            rarityElement.setX((this.group.getWidth() - rarityElement.getWidth()) / 2.0f);
            rarityElement.setY((this.group.getHeight() - (rarityElement.getHeight() / 2.0f)) - 30.0f);
            rarityElement.setScale(0.75f);
            this.group.addActor(rarityElement);
        } else {
            if (this.prize.getBlueCoffees() > 0) {
                prizeIndex = (-1) + 1;
                addPrizeItem(prizeIndex, this.prize.getBlueCoffees(), "blue.coffee.icon");
            }
            if (this.prize.getStarSeeds() > 0) {
                prizeIndex++;
                addPrizeItem(prizeIndex, this.prize.getStarSeeds(), "capture.starseed.icon");
            }
            if (this.prize.getMogaCash() > 0) {
                addPrizeItem(prizeIndex + 1, this.prize.getMogaCash(), "capture.mcash");
            }
        }
        AutoFontLabel displayName = new AutoFontLabel(this.prize.getDisplayName(), 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        float offset = 0.0f;
        float availableWidth = this.group.getWidth();
        if (this.infoButton != null) {
            offset = 45.0f + 5.0f;
            availableWidth = ((this.group.getWidth() - offset) - 23.0f) - 5.0f;
        }
        displayName.setX(((availableWidth - displayName.getWidth()) / 2.0f) + offset);
        displayName.setY(((-displayName.getHeight()) / 2.0f) + 50.0f);
        this.group.addActor(displayName);
    }

    private void addPrizeItem(int prizeIndex, int amount, String iconName) {
        int y = (prizeIndex * (-35)) - 50;
        this.prizeImage = new Image(Assets.loadTexture(iconName));
        this.prizeImage.setX(45.0f - (this.prizeImage.getWidth() / 2.0f));
        this.prizeImage.setY(this.group.getHeight() + y);
        this.group.addActor(this.prizeImage);
        AutoFontLabel prizeLabel = new AutoFontLabel("x " + amount, 0.8f, ColorConstants.FOURTH_FONT_COLOR, false);
        prizeLabel.setX(75.0f);
        prizeLabel.setY(this.group.getHeight() + y);
        this.group.addActor(prizeLabel);
    }

    private void loadMogaAsset() {
        MogaType mogaType = Game.getStorage().getMogaType(this.prize.getMogaTypeId());
        if (this.prizeImage != null && mogaType != null) {
            this.prizeImage.setDrawable(new TextureRegionDrawable(Assets.load(mogaType.getAsset())));
        }
    }

    public void glow(final GlowCompletedListener listener) {
        this.glowing = true;
        SequenceAction sequence = new SequenceAction();
        TintToAction tint1 = new TintToAction();
        tint1.setTint(1.0f, 1.0f, 1.0f, Float.valueOf(1.0f));
        tint1.setDuration(0.2f);
        TintToAction tint2 = new TintToAction();
        tint2.setTint(1.0f, 1.0f, 1.0f, Float.valueOf(0.0f));
        tint2.setDuration(0.2f);
        RunnableAction runnableAction = new DelayedRunnableAction();
        runnableAction.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.slotmachine.CardActor.2
            @Override // java.lang.Runnable
            public void run() {
                CardActor.this.glowing = false;
                if (listener != null) {
                    listener.cardGlowCompleted();
                }
            }
        });
        sequence.addAction(tint1);
        sequence.addAction(tint2);
        sequence.addAction(runnableAction);
        this.glowingCardBg.addAction(sequence);
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public void resume() {
        if (this.prize.getMogaTypeId() > 0) {
            loadMogaAsset();
        }
    }
}
