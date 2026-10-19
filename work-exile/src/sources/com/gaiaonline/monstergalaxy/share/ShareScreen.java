package com.gaiaonline.monstergalaxy.share;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.service.FacebookService;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ShareScreen extends Screen implements ScreenListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent = null;
    private static final String EMAIL_SHARE_SUBJECT = "Free Android Game";
    private static final String FACEBOOK_NOT_CONNECTED = "You are not connected to Facebook.\nPlease try connecting from Tamer Screen first.";
    private static final int TWEET_MAX_LENGTH = 140;
    private boolean showTweetDialogOnNextFrame;
    private InputTextDialog tweetDialog;
    private static String FACEBOOK_SHARE_MSG = "I'm playing Monster Galaxy and you should too! You battle against awesome monsters and train them to fight for you--and it's FREE. Get it on your phone now so we can start playing together!";
    private static String TWITTER_SHARE_MSG = "I'm playing Monster G and you should too! You battle against awesome monsters and train them to fight and it's FREE - ";
    private static String SMS_SHARE_MSG = "I'm playing Monster Galaxy and you should too! You battle against awesome monsters and train them to fight for you--and it's FREE. Get it on your phone now so we can start playing together! ";
    private static String EMAIL_SHARE_MSG = "I'm playing Monster Galaxy and you should too! You battle against awesome monsters and train them to fight for you--and it's FREE. Get it on your phone now so we can start playing together!\n\nGoogle Play Store: https://play.google.com/store/apps/details?id=com.gaiaonline.monstergalaxy";
    private static String SHARE_DESCRIPTION = "Please share Monster G with your friends if you enjoy playing - thanks!";
    private static String SHORTEN_LINK = "";

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

    public ShareScreen() {
        SHORTEN_LINK = PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON ? "http://amzn.to/MfUA0l" : "http://bit.ly/GIQfzt";
        loadBackground();
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SHORTEN_LINK = PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON ? "http://amzn.to/SUDnG4" : "http://bit.ly/OFlkbm";
            FACEBOOK_SHARE_MSG = "I'm playing Monster Galaxy: Exile the sequel to the most popular monster-battling game on iOS! Get it on your phone now so we can start playing together!";
            TWITTER_SHARE_MSG = "I'm playing Monster X the sequel to the most popular monster-battling game on iOS! ";
            SMS_SHARE_MSG = "I'm playing Monster Galaxy: Exile and you should too! the sequel to the most popular monster-battling game on iOS! Get it on your phone now so we can start playing together!";
            EMAIL_SHARE_MSG = "I'm playing Monster Galaxy: Exile  and you should too! the sequel to the most popular monster-battling game on iOS! Get it on your phone now so we can start playing together!" + SHORTEN_LINK;
            SHARE_DESCRIPTION = "Please share Monster Galaxy: Exile with your friends if you enjoy playing - thanks! ";
        }
        TextureElement title = new TextureElement(Assets.loadTexture("share.title"));
        title.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        title.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 270.0f);
        this.root.add(title);
        TextElement description = new TextElement(SHARE_DESCRIPTION, 0.65f, ColorConstants.SHARE_DESCRIPTION_COLOR, true, 400.0f);
        description.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        description.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 235.0f);
        this.root.add(description);
        addFacebookButton();
        addTwitterButton();
        if (Game.getAndroidPlatformHelper().isSMSCapable()) {
            addSmsButton();
        }
        addEmailButton();
        addDoneButton();
        this.tweetDialog = new InputTextDialog(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.share.ShareScreen.1
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
                        ShareScreen.this.hideDialog();
                        break;
                    case Input.Keys.O /* 43 */:
                        ShareScreen.this.shareOnTwitter(ShareScreen.this.tweetDialog.getText());
                        ShareScreen.this.hideDialog();
                        break;
                }
            }
        }, String.valueOf(TWITTER_SHARE_MSG) + SHORTEN_LINK, "Tweet", 140);
    }

    private void loadBackground() {
        this.root.addBackground(Assets.loadTexture("startup.choosezodiacmoga.bg"));
    }

    private void addDoneButton() {
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        ButtonElement button = new ButtonElement(this, UIEvent.CLOSE_BUTTON, normalButton, selectedButton, selectedButton);
        button.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        button.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 420.0f, 270.0f);
        this.root.add(button);
        TextElement label = new TextElement("Done", 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        label.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        label.setAlign(button);
        label.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 5.0f);
        this.root.add(label);
    }

    private void addFacebookButton() {
        addShareButton(UIEvent.TWITTER, "share.twitter.icon", "Twitter", 130.0f, 140.0f);
    }

    private void addTwitterButton() {
        addShareButton(UIEvent.FACEBOOK_CONNECT, "trainer.fb.icon", "Facebook", 350.0f, 140.0f);
    }

    private void addSmsButton() {
        addShareButton(UIEvent.SMS_SHARE, "share.sms.icon", "Text Msg", 130.0f, 50.0f);
    }

    private void addEmailButton() {
        addShareButton(UIEvent.EMAIL_SHARE, "share.email.icon", "Email", 350.0f, 50.0f);
    }

    private void addShareButton(UIEvent event, String icon, String labelText, float x, float y) {
        TextureRegion normalButton = Assets.loadTexture("blue.btn");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.press");
        ButtonElement button = new ButtonElement(this, event, normalButton, selectedButton, selectedButton);
        button.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        button.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, x, y);
        this.root.add(button);
        TextureElement fbIcon = new TextureElement(Assets.loadTexture(icon));
        fbIcon.setAlign(button);
        fbIcon.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        fbIcon.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, -15.0f, 3.0f);
        this.root.add(fbIcon);
        TextElement labelElement = new TextElement(labelText, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        labelElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        labelElement.setAlign(button);
        labelElement.setPosition(ScreenElement.RelPoint.CENTER, -15.0f, 5.0f);
        this.root.add(labelElement);
    }

    private void shareOnFacebook(String message) {
        Game.getFacebookService().postMessage(new FacebookService.OnCompletedHandler() { // from class: com.gaiaonline.monstergalaxy.share.ShareScreen.2
            @Override // com.gaiaonline.monstergalaxy.service.FacebookService.OnCompletedHandler
            public void onCompleted(boolean success) {
            }
        }, message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareOnTwitter(String message) {
        Game.getTwitterService().updateStatus(message);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.showTweetDialogOnNextFrame) {
            loadBackground();
            showDialog(this.tweetDialog);
            this.showTweetDialogOnNextFrame = false;
        }
        if (this.dialog != null) {
            this.dialog.update(deltaTime);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
        if (Game.getTwitterService().isViewOpen()) {
            Game.getTwitterService().closeView();
        } else {
            super.onBackKeyPressed();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent()[event.ordinal()]) {
            case 11:
                if (Game.getFacebookService().isSessionValid()) {
                    shareOnFacebook(FACEBOOK_SHARE_MSG);
                } else {
                    showDialog(new AlertDialog(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.share.ShareScreen.3
                        @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                        public void onUIEvent(UIEvent event2) {
                            ShareScreen.this.hideDialog();
                        }
                    }, 0, FACEBOOK_NOT_CONNECTED, UIEvent.CLOSE_BUTTON));
                    ((AlertDialog) this.dialog).setTimeOut(2);
                }
                break;
            case 12:
                if (Game.getTwitterService().isAuthorized()) {
                    showDialog(this.tweetDialog);
                } else {
                    Game.getTwitterService().authorize(new Runnable() { // from class: com.gaiaonline.monstergalaxy.share.ShareScreen.4
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareScreen.this.showTweetDialogOnNextFrame = true;
                        }
                    });
                }
                break;
            case 14:
                if (Game.getPlatformHelper().isAndroid()) {
                    Game.getAndroidPlatformHelper().composeSms(String.valueOf(SMS_SHARE_MSG) + SHORTEN_LINK);
                }
                break;
            case 15:
                if (Game.getPlatformHelper().isAndroid()) {
                    Game.getAndroidPlatformHelper().composeEmail(EMAIL_SHARE_SUBJECT, EMAIL_SHARE_MSG);
                }
                break;
            case 21:
                MonsterGalaxy.showPreviousScreen();
                break;
        }
    }
}
