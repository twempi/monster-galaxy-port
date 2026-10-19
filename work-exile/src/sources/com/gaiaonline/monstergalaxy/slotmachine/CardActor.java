package com.gaiaonline.monstergalaxy.slotmachine;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actions.TintTo;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachinePrize;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CardActor extends Group {
    public static final int CARD_HEIGHT = 193;
    public static final int CARD_WIDTH = 172;
    private Image cardBg;
    private boolean glowing;
    private Image glowingCardBg;
    private Group group;
    private SlotMachinePrize prize;
    private Image prizeImage;

    public interface GlowCompletedListener {
        void cardGlowCompleted();
    }

    public CardActor(SlotMachinePrize prize, SlotMachineBaseScreen screen) {
        super("");
        this.prize = prize;
        this.group = new Group();
        this.glowingCardBg = new Image("", Assets.loadTexture("slotmachine.card.selected"));
        this.group.width = this.glowingCardBg.width;
        this.group.height = this.glowingCardBg.height;
        this.glowingCardBg.y = 8.0f;
        this.group.addActor(this.glowingCardBg);
        this.cardBg = new Image("", Assets.loadTexture("slotmachine.card.unselected"));
        this.cardBg.x = (this.group.width - this.cardBg.width) / 2.0f;
        this.cardBg.y = (this.group.height - this.cardBg.height) / 2.0f;
        this.group.addActor(this.cardBg);
        addPrize();
        addMogaInfoButton(screen);
        this.glowingCardBg.color.set(1.0f, 1.0f, 1.0f, 0.0f);
        Group group = this.group;
        Group group2 = this.group;
        float scaleFactor = ResolutionManager.getScaleFactor();
        group2.scaleY = scaleFactor;
        group.scaleX = scaleFactor;
        this.width = this.group.width * this.group.scaleX;
        this.height = this.group.height * this.group.scaleY;
        addActor(this.group);
    }

    private void addMogaInfoButton(final SlotMachineBaseScreen screen) {
        if (this.prize.getMogaTypeId() > 0) {
            TextureRegion normalInfoButton = Assets.infoButton;
            TextureRegion selectedInfoButton = Assets.infoPressedButton;
            Button button = new Button("", normalInfoButton, selectedInfoButton);
            button.clickListener = new Button.ClickListener() { // from class: com.gaiaonline.monstergalaxy.slotmachine.CardActor.1
                @Override // com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener
                public void clicked(Button button2) {
                    MogaType mogaType = Game.getStorage().getMogaType(CardActor.this.prize.getMogaTypeId());
                    screen.showMogaInfoDialog(mogaType);
                }
            };
            button.x = 18.0f;
            button.y = (-(button.height / 2.0f)) + 47.0f;
            this.group.addActor(button);
        }
    }

    private void addPrize() {
        int prizeIndex = -1;
        if (this.prize.getMogaTypeId() > 0) {
            MogaType mogaType = Game.getStorage().getMogaType(this.prize.getMogaTypeId());
            if (mogaType.getAsset() != null) {
                this.prizeImage = new Image("moga", Assets.load(mogaType.getAsset()));
                this.prizeImage.x = (this.group.width - this.prizeImage.width) / 2.0f;
                this.prizeImage.y = ((this.group.height - this.prizeImage.height) / 2.0f) + 20.0f;
                this.prizeImage.scaleX = 0.72f;
                this.prizeImage.scaleY = 0.72f;
                this.group.addActor(this.prizeImage);
            }
            Image rarityElement = new Image("", Assets.loadTexture(mogaType.getRarityAssetName()));
            rarityElement.x = (this.group.width - rarityElement.width) / 2.0f;
            rarityElement.y = (this.group.height - (rarityElement.height / 2.0f)) - 30.0f;
            rarityElement.scaleY = 0.75f;
            rarityElement.scaleX = 0.75f;
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
        BitmapFont font = Fonts.regular;
        font.setScale(0.8f);
        Label displayName = new Label("", font, this.prize.getDisplayName());
        displayName.color.set(ColorConstants.PRIMARY_FONT_COLOR);
        displayName.x = (this.group.width - displayName.width) / 2.0f;
        displayName.y = ((-displayName.height) / 2.0f) + 50.0f;
        this.group.addActor(displayName);
    }

    private void addPrizeItem(int prizeIndex, int amount, String iconName) {
        int y = (prizeIndex * (-35)) - 50;
        this.prizeImage = new Image("", Assets.loadTexture(iconName));
        this.prizeImage.x = 45.0f - (this.prizeImage.width / 2.0f);
        this.prizeImage.y = this.group.height + y;
        this.group.addActor(this.prizeImage);
        BitmapFont font = Fonts.regular;
        font.setScale(0.8f);
        Label prizeLabel = new Label("", font, "x " + amount);
        prizeLabel.color.set(ColorConstants.FOURTH_FONT_COLOR);
        prizeLabel.x = 75.0f;
        prizeLabel.y = this.group.height + y;
        this.group.addActor(prizeLabel);
    }

    private void loadMogaAsset() {
        MogaType mogaType = Game.getStorage().getMogaType(this.prize.getMogaTypeId());
        if (this.prizeImage != null && mogaType != null) {
            this.prizeImage.region = Assets.load(mogaType.getAsset());
        }
    }

    public void glow(final GlowCompletedListener listener) {
        this.glowing = true;
        Action action = Sequence.$(TintTo.$(1.0f, 1.0f, 1.0f, Float.valueOf(1.0f), 0.2f), TintTo.$(1.0f, 1.0f, 1.0f, Float.valueOf(0.0f), 0.2f));
        action.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.slotmachine.CardActor.2
            @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
            public void completed(Action action2) {
                CardActor.this.glowing = false;
                if (listener != null) {
                    listener.cardGlowCompleted();
                }
            }
        });
        this.glowingCardBg.action(action);
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
