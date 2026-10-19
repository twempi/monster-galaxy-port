package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.Share;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.BlueCoffeeEffect;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.quest.RewardItem;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ProgressBarElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TamerScreen extends Screen implements ScreenListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$map$TamerScreen$ClaimStatus = null;
    private static final DateFormat CLAIM_TIME_FORMAT = new SimpleDateFormat("h:mm a");
    private static final float FONT_SCALE_OVER_MAX_VALUE = 0.42f;
    private static final float FONT_SCALE_REGULAR = 0.52f;
    private Color blueCoffeeDisabledColor;
    private ShadowTextElement blueCoffeesText;
    private ButtonElement claimPrizeButton;
    private TextElement claimPrizeLabel;
    private TextElement claimPrizeLabelTwoLines;
    private ClaimStatus claimStatus;
    private ButtonElement closeButton;
    private ShadowTextElement coinsText;
    private ButtonElement rechargeButton;
    private ButtonElement shareButton;
    private ShadowTextElement starseedsText;
    private Image tamerActor;
    private Trainer trainer;
    private ProgressBarElement trainerEnergyBar;
    private TextElement trainerEnergyText;
    private TextElement trainerEnergyTimer;

    private enum ClaimStatus {
        CHEKING_TIMESTAMP,
        CLAIMING,
        CAN_CLAIM,
        MUST_WAIT,
        NETWORK_FAILURE;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static ClaimStatus[] valuesCustom() {
            ClaimStatus[] claimStatusArrValuesCustom = values();
            int length = claimStatusArrValuesCustom.length;
            ClaimStatus[] claimStatusArr = new ClaimStatus[length];
            System.arraycopy(claimStatusArrValuesCustom, 0, claimStatusArr, 0, length);
            return claimStatusArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$map$TamerScreen$ClaimStatus() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$map$TamerScreen$ClaimStatus;
        if (iArr == null) {
            iArr = new int[ClaimStatus.valuesCustom().length];
            try {
                iArr[ClaimStatus.CAN_CLAIM.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ClaimStatus.CHEKING_TIMESTAMP.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ClaimStatus.CLAIMING.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ClaimStatus.MUST_WAIT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ClaimStatus.NETWORK_FAILURE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$map$TamerScreen$ClaimStatus = iArr;
        }
        return iArr;
    }

    public TamerScreen() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        this.trainer = Game.getTrainer();
        this.blueCoffeeDisabledColor = ColorConstants.PRIMARY_GRAYED_COLOR;
        this.stage = new Stage(width, height, true);
        Gdx.input.setInputProcessor(this.stage);
        SoundManager.loadSound(SoundManager.SOUND_BLUE_COFFEE);
        addBackground();
        addTamerInfo();
        addInventoryBar();
        addCloseButton();
        addTrainerEnergy();
        addMogasInfo();
        addLuckyMogaButton();
        addPrizeIcons();
        addRechargeButton();
        addShareButton();
    }

    private void addLuckyMogaButton() {
        TextureRegion normalButton = Assets.loadTexture("green.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("green.btn.small.press");
        ButtonElement luckyButton = new ButtonElement(this, UIEvent.LUCKY_MOGA, normalButton, selectedButton);
        luckyButton.setPosition(ScreenElement.RelPoint.CENTER, 155.0f, 13.0f);
        this.root.add(luckyButton);
        TextElement luckyLabel = new TextElement("Lucky Moga", 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        luckyLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        luckyLabel.setAlign(luckyButton);
        luckyLabel.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 5.0f);
        this.root.add(luckyLabel);
    }

    private void addBackground() {
        this.root.addBackground(Assets.loadTexture("trainer.bg"));
    }

    private void addTamerInfo() {
        String tamerName = this.trainer.getName();
        this.root.addLabel(tamerName, 15.0f, -15.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.8f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        TextureRegion tamerAsset = new TextureRegion(Assets.loadTexture(this.trainer.getAssetName()));
        tamerAsset.flip(true, false);
        this.tamerActor = new Image("tamer", tamerAsset);
        addToStage(this.tamerActor, 82.0f, 131.0f, 0.0f);
    }

    private void addInventoryBar() {
        ScreenGroup inventory = new ScreenGroup();
        inventory.setSize(169.0f, 20.0f);
        this.blueCoffeesText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.blueCoffeesText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        TextureElement bluecoffe = new TextureElement(Assets.loadTexture("blue.coffee.icon"));
        this.blueCoffeesText.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 29.0f, -3.0f);
        bluecoffe.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 15.0f, -5.0f);
        inventory.add(this.blueCoffeesText);
        inventory.add(bluecoffe);
        this.starseedsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.starseedsText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        TextureElement starseed = new TextureElement(Assets.loadTexture("capture.starseed.icon"));
        this.starseedsText.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, -3.0f);
        starseed.setPosition(ScreenElement.RelPoint.CENTER, -10.0f, -5.0f);
        inventory.add(this.starseedsText);
        inventory.add(starseed);
        this.coinsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.coinsText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        TextureElement mogacash = new TextureElement(Assets.loadTexture("capture.mcash"));
        this.coinsText.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, -32.0f, -3.0f);
        mogacash.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, -45.0f, -5.0f);
        inventory.add(this.coinsText);
        inventory.add(mogacash);
        inventory.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        inventory.setPosition(ScreenElement.RelPoint.CENTER_TOP, 12.0f, -20.0f);
        this.root.add(inventory);
    }

    private void updateField(TextElement field, int value, int maxDisplayValue) {
        if (value <= maxDisplayValue) {
            if (maxDisplayValue == 9999 && value > 999) {
                field.setText(String.valueOf(value));
                field.setFontScale(FONT_SCALE_OVER_MAX_VALUE);
                return;
            } else {
                field.setText(String.valueOf(value));
                field.setFontScale(FONT_SCALE_REGULAR);
                return;
            }
        }
        field.setText("+" + maxDisplayValue);
        field.setFontScale(FONT_SCALE_OVER_MAX_VALUE);
    }

    private void addCloseButton() {
        TextureRegion normalButton = Assets.loadTexture("close.button");
        TextureRegion selectedButton = Assets.loadTexture("close.button.press");
        this.closeButton = new ButtonElement(this, UIEvent.CLOSE_BUTTON, normalButton, selectedButton);
        this.closeButton.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -25.0f, -30.0f);
        this.root.add(this.closeButton);
    }

    private void addTrainerEnergy() {
        int trainerEnergy = this.trainer.getEnergy();
        int trainerMaxEnergy = Constants.maxTrainerEnergy;
        this.root.add(Assets.loadTexture("trainer.energybar.empty"), -45.0f, 76.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.LEFT_CENTER);
        this.trainerEnergyBar = new ProgressBarElement(Assets.loadTexture("trainer.energybar.full"));
        this.trainerEnergyBar.setMaxValue(Constants.maxTrainerEnergy);
        this.trainerEnergyBar.setValue(this.trainer.getEnergy());
        this.trainerEnergyBar.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.trainerEnergyBar.setPosition(ScreenElement.RelPoint.CENTER, -44.5f, 76.0f);
        this.trainerEnergyBar.setMaxValue(trainerMaxEnergy);
        this.trainerEnergyBar.setValue(trainerEnergy);
        this.root.add(this.trainerEnergyBar);
        this.trainerEnergyText = this.root.addLabel(String.valueOf(trainerEnergy), 255.0f, -77.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.RIGHT_TOP, 0.5f, ColorConstants.SECONARY_FONT_COLOR, true, false);
        this.root.addLabel("/" + trainerMaxEnergy, 255.0f, -77.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, true, false);
        this.root.add(Assets.loadTexture("island.energy.icon"), 295.0f, -84.0f, ScreenElement.RelPoint.LEFT_TOP);
        this.trainerEnergyTimer = this.root.addLabel("0:00", 115.0f, 78.0f, ScreenElement.RelPoint.CENTER, 0.6f, ColorConstants.SECONARY_FONT_COLOR, true, true);
    }

    private void addMogasInfo() {
        this.root.addLabel("Mogas caught: ", 190.0f, -120.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        int numberOfMogas = Game.getStorage().getMogaTypeCount(MogaType.SubType.NORMAL) + Game.getStorage().getMogaTypeCount(MogaType.SubType.LUCKY);
        String text1 = String.valueOf(this.trainer.getMogas().size());
        String text2 = "/ " + numberOfMogas;
        TextElement label1 = this.root.addLabel(text1, 190.0f, -145.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.7f, ColorConstants.SECONARY_FONT_COLOR, true, true);
        this.root.addLabel(text2, 22.0f, 1.0f, ScreenElement.RelPoint.RIGHT_CENTER, label1, ScreenElement.RelPoint.CENTER, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
    }

    private void addShareButton() {
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        this.shareButton = new ButtonElement(this, UIEvent.SHARE, normalButton, selectedButton, selectedButton);
        this.shareButton.setPosition(ScreenElement.RelPoint.CENTER, 10.0f, -114.0f);
        this.root.add(this.shareButton);
        TextElement shareLabel = new TextElement("Share", 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        shareLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        shareLabel.setAlign(this.shareButton);
        shareLabel.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 5.0f);
        this.root.add(shareLabel);
    }

    private void addPrizeIcons() {
        Image starSeedActor = new Image("starSeed", Assets.loadTexture("capture.starseed"));
        Image blueCoffeeActor = new Image("blueCoffee", Assets.loadTexture("blue.coffee.icon"));
        ResolutionManager.scaleActor(blueCoffeeActor);
        starSeedActor.rotation = 23.0f;
        blueCoffeeActor.rotation = -23.0f;
        addToStage(starSeedActor, 193.0f, 131.0f, 0.55f);
        addToStage(blueCoffeeActor, 206.0f, 111.0f, 0.8f);
        TextElement descriptionLabel = new TextElement("Get your daily prize!", 0.45f, ColorConstants.FOURTH_FONT_COLOR, true, 70.0f);
        descriptionLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        descriptionLabel.setPosition(ScreenElement.RelPoint.LEFT_TOP, 265.0f, -205.0f);
        this.root.add(descriptionLabel);
        TextureRegion normalButton = Assets.loadTexture("green.btn");
        TextureRegion selectedButton = Assets.loadTexture("green.btn.press");
        TextureRegion grayed = Assets.loadTexture("gray.btn");
        this.claimPrizeButton = new ButtonElement(this, UIEvent.CLAIM_PRIZE, normalButton, selectedButton, grayed);
        this.claimPrizeButton.setPosition(ScreenElement.RelPoint.CENTER, 155.0f, -47.0f);
        this.root.add(this.claimPrizeButton);
        this.claimPrizeLabel = new TextElement("Daily prize", 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.claimPrizeLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.claimPrizeLabel.setPosition(ScreenElement.RelPoint.CENTER, 155.0f, -38.0f);
        this.root.add(this.claimPrizeLabel);
        this.claimPrizeLabelTwoLines = new TextElement("Daily prize", 0.5f, ColorConstants.PRIMARY_FONT_COLOR, true, 110.0f);
        this.claimPrizeLabelTwoLines.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.claimPrizeLabelTwoLines.setPosition(ScreenElement.RelPoint.LEFT_TOP, 25.0f, -7.0f);
        this.claimPrizeLabelTwoLines.setAlign(this.claimPrizeButton);
        this.claimPrizeLabelTwoLines.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.claimPrizeLabelTwoLines.setVisible(false);
        this.root.add(this.claimPrizeLabelTwoLines);
    }

    private void setClaimPrizeMessage(String message, boolean multiLine, boolean green) {
        this.claimPrizeLabel.setVisible(!multiLine);
        this.claimPrizeLabelTwoLines.setVisible(multiLine);
        TextElement label = multiLine ? this.claimPrizeLabelTwoLines : this.claimPrizeLabel;
        label.setText(message);
        this.claimPrizeButton.setGreyedOut(green ? false : true);
    }

    private void checkServerTimestamp() {
        this.claimStatus = ClaimStatus.CHEKING_TIMESTAMP;
        setClaimPrizeMessage("Checking...", false, false);
        Thread checkPrizeThread = new Thread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.TamerScreen.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Game.getGaiaClient().fetchTimestamp();
                    Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.TamerScreen.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TamerScreen.this.checkServerTimestampDone();
                        }
                    });
                } catch (Exception e) {
                    Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.TamerScreen.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            TamerScreen.this.checkServerTimestampFailed();
                        }
                    });
                }
            }
        });
        checkPrizeThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkServerTimestampFailed() {
        this.claimStatus = ClaimStatus.NETWORK_FAILURE;
        setClaimPrizeMessage("Cannot get daily prize info", true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkServerTimestampDone() {
        long claimAvailableTime = Game.getTrainer().getClaimDailyPrizeAvaiableTime();
        if (Game.getGaiaClient().calculateServerTimestamp() < claimAvailableTime) {
            userMustWaitToClaimPrize();
        } else {
            userCanClaimPrize();
        }
    }

    private void userMustWaitToClaimPrize() {
        String day;
        this.claimStatus = ClaimStatus.MUST_WAIT;
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(Game.getGaiaClient().calculateServerTimestamp() * 1000);
        Calendar claimTime = Calendar.getInstance();
        claimTime.setTimeInMillis(Game.getTrainer().getClaimDailyPrizeAvaiableTime() * 1000);
        if (now.get(6) == claimTime.get(6)) {
            day = "";
        } else {
            day = " tomorrow";
        }
        String getItMessage = "Get it" + day + " at " + CLAIM_TIME_FORMAT.format(claimTime.getTime());
        setClaimPrizeMessage(getItMessage, true, false);
    }

    private void userCanClaimPrize() {
        this.claimStatus = ClaimStatus.CAN_CLAIM;
        setClaimPrizeMessage("Claim Prize", false, true);
    }

    private void addRechargeButton() {
        TextureRegion normalButton = Assets.loadTexture("blue.coffee.portrait.off");
        TextureRegion selectedButton = Assets.loadTexture("blue.coffee.portrait.on");
        this.rechargeButton = new ButtonElement(this, UIEvent.RECHARGE_BUTTON, normalButton, selectedButton);
        this.rechargeButton.setPosition(ScreenElement.RelPoint.CENTER, 165.0f, 73.0f);
        this.root.add(this.rechargeButton);
        updateTrainerEnergy();
    }

    private void addToStage(Actor actor, float x, float y, float scaleFactor) {
        ResolutionManager.scaleActor(actor);
        if (scaleFactor != 0.0f) {
            actor.width *= scaleFactor;
            actor.height *= scaleFactor;
        }
        actor.x = ((x / 480.0f) * this.stage.width()) - (actor.width / 2.0f);
        actor.y = ((y / 320.0f) * this.stage.height()) - (actor.height / 2.0f);
        this.stage.addActor(actor);
    }

    private void updateTrainerEnergy() {
        this.trainerEnergyBar.setValue(this.trainer.getEnergy());
        this.trainerEnergyText.setText(String.valueOf(this.trainer.getEnergy()));
        if (this.trainer.isFullyCharged() || this.trainer.getBlueCoffeeCups() == 0) {
            this.rechargeButton.setColor(this.blueCoffeeDisabledColor);
            this.rechargeButton.setEnabled(false);
        } else {
            this.rechargeButton.setColor(ColorConstants.PRIMARY_FONT_COLOR);
            this.rechargeButton.setEnabled(true);
        }
    }

    private void applyBlueCoffee() {
        if (this.trainer.applyBlueCoffee()) {
            BlueCoffeeEffect blueCoffeeEffect = new BlueCoffeeEffect("bc");
            blueCoffeeEffect.x = this.tamerActor.x + (this.tamerActor.width / 2.0f);
            blueCoffeeEffect.y = this.tamerActor.y + (this.tamerActor.height / 2.0f);
            this.stage.addActor(blueCoffeeEffect);
            blueCoffeeEffect.play();
            updateTrainerEnergy();
            String currentIslandId = String.valueOf(this.trainer.getCurrentIsland().getId());
            Game.getAnalytics().trackEvent("monetization", "use_item", "blue_coffee", "blue_coffee_tamer", "", currentIslandId);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        boolean trainerIsFullyCharged = this.trainer.isEnergyFullyCharged();
        if (!trainerIsFullyCharged) {
            this.trainerEnergyTimer.setText(this.trainer.getEnergyTimerText());
            updateTrainerEnergy();
        }
        this.trainerEnergyTimer.setVisible(!trainerIsFullyCharged);
        if (this.dialog != null) {
            this.dialog.update(deltaTime);
        }
        updateField(this.blueCoffeesText, this.trainer.getBlueCoffeeCups(), 999);
        updateField(this.starseedsText, this.trainer.getStarSeeds(), 999);
        updateField(this.coinsText, this.trainer.getCoins(), Constants.MAX_DISPLAY_VALUE_MOGA_CASH);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void dispose() {
        super.dispose();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        playMusic();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        if (this.trainer.getLastDailyPrizeClaim() != null) {
            checkServerTimestamp();
        } else {
            userCanClaimPrize();
        }
    }

    private void playMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.playMusic(Constants.worldAmbienceAudio);
        }
    }

    private void checkAndClaimPrize() {
        this.claimStatus = ClaimStatus.CLAIMING;
        setClaimPrizeMessage("Claiming...", false, false);
        Thread checkPrizeThread = new Thread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.TamerScreen.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Game.getGaiaClient().fetchTimestamp();
                    Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.TamerScreen.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TamerScreen.this.claimPrize();
                        }
                    });
                } catch (Exception e) {
                    Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.TamerScreen.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            TamerScreen.this.checkServerTimestampFailed();
                        }
                    });
                }
            }
        });
        checkPrizeThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void claimPrize() {
        if (Game.getGaiaClient().calculateServerTimestamp() > this.trainer.getClaimDailyPrizeAvaiableTime()) {
            RewardItem item = this.trainer.claimDailyPrize();
            showDialog(new AlertDialog(this, 5, "Daily prize: " + item.toString() + "!", UIEvent.TAMER_DISMISS_DIALOG));
        } else {
            showDialog(new AlertDialog(this, 5, "Daily prize not available yet!", UIEvent.TAMER_DISMISS_DIALOG));
        }
        userMustWaitToClaimPrize();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (event == UIEvent.CLAIM_PRIZE) {
            switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$map$TamerScreen$ClaimStatus()[this.claimStatus.ordinal()]) {
                case 3:
                    checkAndClaimPrize();
                    break;
                case 5:
                    showDialog(new AlertDialog(this, "Cannot get daily prize info. There is no connection with Gaia Server.", "Retry", UIEvent.TAMER_RETRY_CLAIM, UIEvent.TAMER_DISMISS_DIALOG));
                    break;
            }
            return;
        }
        if (event == UIEvent.SHARE) {
            Share.share();
            return;
        }
        if (event == UIEvent.TAMER_RETRY_CLAIM) {
            hideDialog();
            checkServerTimestamp();
            return;
        }
        if (event == UIEvent.TAMER_DISMISS_DIALOG) {
            hideDialog();
            return;
        }
        if (event == UIEvent.RECHARGE_BUTTON) {
            applyBlueCoffee();
        } else if (event == UIEvent.LUCKY_MOGA) {
            MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE, false, true);
        } else if (event == UIEvent.CLOSE_BUTTON) {
            MonsterGalaxy.showPreviousScreen();
        }
    }
}
