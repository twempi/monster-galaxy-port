package com.gaiaonline.monstergalaxy.team;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.LifeElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.SpriteElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.TouchReleasedListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Portrait extends ButtonUI implements ScreenListener {
    private static final float PORTRAIT_MAX_TOUCH_DISTANCE = 30.0f;
    private ButtonElement addButton;
    private ButtonElement blueCoffeeButton;
    private ButtonElement buyButton;
    private TextElement buyLabel;
    private TextureElement buyMCashIcon;
    private TextElement buyPriceLabel;
    private SpriteElement cardOverlay;
    private NumberFormat formatter = new DecimalFormat("00");
    private LifeElement healthBar;
    private TextureElement healthBarBg;
    private Moga moga;
    private TextElement mogaNameLabel;
    private TextureElement mogaPortrait;
    private TextElement mogaStatusAndLevelLabel;
    private ButtonElement napButton;
    private TextElement napTimerLabel;
    private TextElement rarity;
    private ActorScreenElement rarityGradient;
    private TeamScreen screen;
    private Trainer trainer;
    private ButtonElement wakeUpButton;
    private TextureElement zodiacSign;

    public Portrait(TeamScreen screen) {
        this.screen = screen;
        setScreenListener(this);
        setStretchable(false);
        this.trainer = Game.getTrainer();
        TextureRegion cardBackground = Assets.loadTexture("card.bg");
        addBackground(cardBackground);
        Image glowAlpha = new Image("glow.alpha", Assets.loadTexture("glow.alpha"));
        ResolutionManager.scaleActor(glowAlpha);
        this.rarityGradient = new ActorScreenElement();
        this.rarityGradient.setActorDeprecated(glowAlpha);
        this.rarityGradient.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.rarityGradient.setPosition(ScreenElement.RelPoint.CENTER, -72.0f, -109.0f);
        add(this.rarityGradient);
        TextureRegion normalInfoButton = Assets.infoButton;
        TextureRegion selectedInfoButton = Assets.infoPressedButton;
        addButton(UIEvent.TEAM_MOGA_INFO, normalInfoButton, selectedInfoButton, -50.0f, 90.0f, ScreenElement.RelPoint.CENTER);
        TextureRegion normalAddButton = Assets.addButton;
        TextureRegion selectedAddButton = Assets.addPressedButton;
        this.addButton = addButton(UIEvent.TEAM_ADD_MOGA, normalAddButton, selectedAddButton, 55.0f, 90.0f, ScreenElement.RelPoint.CENTER);
        this.buyButton = addButton(UIEvent.TEAM_BUY_MOGA, Assets.loadTexture("green.btn.small"), Assets.loadTexture("green.btn.small.press"), 0.0f, 32.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        TouchReleasedListener buyButtonListener = new TouchReleasedListener() { // from class: com.gaiaonline.monstergalaxy.team.Portrait.1
            @Override // com.gaiaonline.monstergalaxy.screen.TouchReleasedListener
            public void touchReleased(Vector2 touchPoint, Vector2 releasePoint) {
                if (touchPoint.dst(releasePoint) >= Portrait.PORTRAIT_MAX_TOUCH_DISTANCE) {
                    return;
                }
                Portrait.this.buyMoga();
            }
        };
        this.buyButton.setTouchReleasedListener(buyButtonListener);
        this.buyMCashIcon = new TextureElement(Assets.loadTexture("capture.mcash"));
        this.buyMCashIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.buyMCashIcon.setAlign(this.buyButton);
        this.buyMCashIcon.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 3.0f);
        add(this.buyMCashIcon);
        this.buyLabel = new TextElement("Buy", 0.58f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.buyLabel.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        this.buyLabel.setAlign(this.buyButton);
        this.buyLabel.setPosition(ScreenElement.RelPoint.CENTER, -16.0f, 5.0f);
        add(this.buyLabel);
        this.buyPriceLabel = new TextElement("0", 0.58f, ColorConstants.PRIMARY_FONT_COLOR, true);
        this.buyPriceLabel.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.buyPriceLabel.setAlign(this.buyButton);
        this.buyPriceLabel.setPosition(ScreenElement.RelPoint.CENTER, 16.0f, 5.0f);
        add(this.buyPriceLabel);
        this.rarity = new TextElement("", 0.5f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.rarity.setPosition(ScreenElement.RelPoint.CENTER, 5.0f, 90.0f);
        add(this.rarity);
        add(Assets.mogaBackground, 0.0f, 35.0f);
        this.mogaPortrait = add(normalAddButton, 0.0f, 35.0f);
        add(Assets.littleBlueCircle, -19.0f, 53.0f);
        add(Assets.circles.get(ZodiacPowerType.NORMAL), -20.0f, 55.0f);
        this.healthBarBg = add(Assets.loadTexture("empty.bar.card"), 0.0f, -5.0f);
        this.healthBarBg.setScale(0.65f);
        this.healthBar = new LifeElement(Assets.loadTexture("full.hp.bar.card"));
        this.healthBar.setPosition(ScreenElement.RelPoint.CENTER, -44.0f, -4.0f);
        this.healthBar.setScale(0.65f);
        this.healthBar.setMaxLife(1);
        this.healthBar.setLife(1);
        add(this.healthBar);
        this.zodiacSign = add(Assets.loadTexture("sign.aquarius"), -20.0f, 55.0f);
        this.mogaNameLabel = addLabel("", 0.0f, -22.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        this.mogaStatusAndLevelLabel = addLabel("", 0.0f, -42.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false);
        TextureRegion napNormalButton = Assets.loadTexture("nap.button");
        TextureRegion napSelectedButton = Assets.loadTexture("nap.button.press");
        this.napButton = addButton(UIEvent.TEAM_NAP_MOGA, napNormalButton, napSelectedButton, 45.0f, -80.0f, ScreenElement.RelPoint.CENTER);
        this.cardOverlay = add(new Sprite(Assets.loadTexture("card.overlay")), 0.0f, 0.0f, 158.0f, 232.0f, 0.8f);
        this.napTimerLabel = addLabel("3:00", 0.0f, 0.0f, 0.85f, ColorConstants.SECONARY_FONT_COLOR, true);
        this.cardOverlay.setVisible(false);
        this.napTimerLabel.setVisible(false);
        TextureRegion blueCoffeeNormal = Assets.loadTexture("blue.coffee.portrait.off");
        TextureRegion blueCoffeeSelected = Assets.loadTexture("blue.coffee.portrait.on");
        this.blueCoffeeButton = addButton(UIEvent.DRINK_BLUE_COFFEE, blueCoffeeNormal, blueCoffeeSelected, -40.0f, -80.0f, ScreenElement.RelPoint.CENTER);
        TextureRegion normalWakeButton = Assets.loadTexture("wake.button");
        TextureRegion selectedWakeButton = Assets.loadTexture("wake.button.press");
        this.wakeUpButton = addButton(UIEvent.TEAM_WAKE_UP_MOGA, normalWakeButton, selectedWakeButton, 45.0f, -80.0f, ScreenElement.RelPoint.CENTER);
        this.wakeUpButton.setVisible(false);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        boolean isInTeamList = this.trainer.getTeam().getMogas().contains(this.moga);
        boolean isInPlayerList = this.trainer.getMogas().contains(this.moga);
        boolean isSleeping = this.moga.isSleeping();
        if (isSleeping) {
            int remainingNapTime = (int) Math.floor(this.moga.getNapEnd() - Clock.getCurrentTimeSec());
            if (remainingNapTime > this.moga.getRecoveryTime()) {
                remainingNapTime = this.moga.getRecoveryTime();
                this.moga.startNap();
            }
            int minutes = (int) Math.floor(((long) remainingNapTime) / 60);
            int seconds = remainingNapTime - (minutes * 60);
            this.napTimerLabel.setText(String.valueOf(minutes) + ":" + this.formatter.format(seconds));
            this.addButton.setVisible(false);
        } else {
            this.addButton.setVisible(isInPlayerList && !isInTeamList && this.trainer.getTeam().getMogas().size() < 3);
        }
        this.wakeUpButton.setVisible(isSleeping);
        this.cardOverlay.setVisible(isSleeping);
        this.napTimerLabel.setVisible(isSleeping);
        this.wakeUpButton.setVisible(isSleeping);
        this.napButton.setVisible(!isSleeping && this.moga.isWounded());
        this.blueCoffeeButton.setVisible(this.moga.isWounded());
        this.healthBarBg.setVisible(isInPlayerList);
        this.healthBar.setVisible(isInPlayerList);
        if (isInPlayerList) {
            this.healthBar.setMaxLife(this.moga.getMaxHitPoints());
            this.healthBar.setLife(this.moga.getHitPoints());
        }
    }

    public void setMoga(Moga moga) {
        this.moga = moga;
        if (moga != null) {
            this.rarityGradient.setColor(moga.getType().getRarityColor());
            this.mogaPortrait.setTexture(moga.getType().getPortrait());
            this.rarity.setText(moga.getType().getRarity().getCamelCaseName());
            this.mogaNameLabel.setText(moga.getName());
            boolean inPlayerMogasList = this.trainer.getMogas().contains(moga);
            if (inPlayerMogasList) {
                this.mogaStatusAndLevelLabel.setText("Lv. " + moga.getLevel());
            } else {
                this.mogaStatusAndLevelLabel.setText("Not Captured");
            }
            this.zodiacSign.setTexture(Assets.getZodiacSignAsset(moga.getType().getZodiacSign()));
            this.healthBar.setMaxLife(moga.getMaxHitPoints());
            this.healthBar.setLife(moga.getHitPoints());
            this.buyButton.setVisible(moga.getType().isPurchasable() && !inPlayerMogasList);
            this.buyLabel.setVisible(this.buyButton.isVisible());
            this.buyMCashIcon.setVisible(this.buyButton.isVisible());
            this.buyPriceLabel.setVisible(this.buyButton.isVisible());
            this.buyPriceLabel.setText(String.valueOf(moga.getType().getMogaCashPrice()));
        }
    }

    private void wakeUp() {
        this.napButton.setVisible(true);
        this.wakeUpButton.setVisible(false);
        this.cardOverlay.setVisible(false);
        this.napTimerLabel.setVisible(false);
        this.moga.interruptNap();
    }

    private void putMogaToSleep() {
        this.cardOverlay.setVisible(true);
        this.napTimerLabel.setVisible(true);
        this.wakeUpButton.setVisible(true);
        this.napButton.setVisible(false);
        this.moga.startNap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buyMoga() {
        if (this.trainer.consumeCoins(this.moga.getType().getMogaCashPrice())) {
            this.moga = Moga.newMogaFromType(this.moga.getType());
            this.moga.setTrainerMogasMaxLevel();
            this.moga.fullyRechargeHealth();
            this.moga.fullyRechargeZodiac();
            this.trainer.getMogas().add(this.moga);
            setMoga(this.moga);
            Game.save();
            this.screen.showDialog(NameMogaDialog.newInstance(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.team.Portrait.2
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    Portrait.this.screen.hideDialog();
                    Portrait.this.setMoga(Portrait.this.moga);
                    Game.save();
                }
            }, this.moga));
            return;
        }
        this.screen.showDialog(new AlertDialog(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.team.Portrait.3
            private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent;

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

            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent()[event.ordinal()]) {
                    case 21:
                        Portrait.this.screen.hideDialog();
                        break;
                    case Input.Keys.Q /* 45 */:
                        MonsterGalaxy.showScreen(ScreenCode.SHOP, false, true);
                        Portrait.this.screen.hideDialog();
                        break;
                }
            }
        }, "You don't have enough Moga Cash! Visit the Shop to get more.", "Visit Shop", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON));
    }

    private void applyBlueCoffe() {
        if (this.moga.isWounded()) {
            if (this.trainer.applyBlueCoffee(this.moga)) {
                this.mogaPortrait.playEffect(EffectType.BLUE_COFFEE, ScreenElement.RelPoint.CENTER);
                this.healthBar.setMaxLife(this.moga.getHitPoints());
                this.healthBar.setLife(this.moga.getHitPoints());
                String currentIslandId = String.valueOf(this.trainer.getCurrentIsland().getId());
                if (this.moga.isSleeping()) {
                    Game.getAnalytics().trackEvent("monetization", "use_item", "blue_coffee", "blue_coffee_team", "", currentIslandId);
                } else {
                    Game.getAnalytics().trackEvent("monetization", "use_item", "blue_coffee", "blue_coffee_team_nap", "", currentIslandId);
                }
                wakeUp();
                return;
            }
            this.screen.showShopDialog();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (!this.screen.isInhibitClick()) {
            if (event == UIEvent.TEAM_ADD_MOGA) {
                this.trainer.getTeam().addMoga(this.moga);
                return;
            }
            if (event == UIEvent.TEAM_NAP_MOGA) {
                putMogaToSleep();
                return;
            }
            if (event == UIEvent.TEAM_WAKE_UP_MOGA) {
                wakeUp();
                return;
            }
            if (event == UIEvent.DRINK_BLUE_COFFEE) {
                applyBlueCoffe();
                return;
            }
            if (event == UIEvent.TEAM_MOGA_INFO) {
                if (!this.moga.isSleeping()) {
                    this.screen.showDialog(new MogaPortraitDialog(this, this.moga));
                }
            } else if (event == UIEvent.CLOSE_BUTTON) {
                this.screen.hideDialog();
            }
        }
    }
}
