package com.gaiaonline.monstergalaxy.startup;

import com.badlogic.gdx.Input;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public abstract class ChooseMogaNameScreen extends AbstractStartupScreen {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent;

    protected abstract void addMogaNameTextField(int i, int i2, int i3, int i4, Integer num, String str);

    protected abstract String getMogaName();

    protected abstract void removeMogaNameTextField();

    protected abstract void setMogaName(String str);

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

    public ChooseMogaNameScreen() {
        this.root.addBackground(Assets.loadTexture("startup.choosezodiacmoga.bg"));
        addTitle(I18nManager.getText(I18nManager.I18nKey.NAME_YOUR_MOGA));
        addFinishButton();
        addDoneButton();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        super.onResume();
        addMogaNameTextField(15, 240, 210, 48, Moga.NAME_MAX_CHARS, " ");
        if (Game.getTrainer().getMogas().get(0).getName() != null) {
            setMogaName(Game.getTrainer().getMogas().get(0).getName());
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        removeMogaNameTextField();
    }

    public static ChooseMogaNameScreen newInstance() {
        return Game.getPlatformHelper().isAndroid() ? new AndroidChooseMogaNameScreen() : new DesktopChooseMogaNameScreen();
    }

    private boolean saveMogaName() {
        String mogaName = getMogaName().trim();
        if (mogaName.length() <= 0) {
            return false;
        }
        Game.getTrainer().getMogas().get(0).setName(mogaName);
        Game.getAnalytics().trackEvent("acquisition", "", "", "complete_name_moga", "", "");
        return true;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$UIEvent()[event.ordinal()]) {
            case 40:
            case Input.Keys.Q /* 45 */:
                if (saveMogaName()) {
                    MonsterGalaxy.showNextScreen();
                }
                break;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public ScreenCode getCode() {
        return ScreenCode.CHOOSE_MOGA_NAME;
    }
}
