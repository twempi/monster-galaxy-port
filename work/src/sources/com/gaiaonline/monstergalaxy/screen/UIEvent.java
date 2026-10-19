package com.gaiaonline.monstergalaxy.screen;

/* JADX INFO: loaded from: classes.dex */
public enum UIEvent {
    PAUSE_BUTTON,
    ATTACK_BUTTON,
    CAPTURE_BUTTON,
    THROW_STARSEED,
    THROW_MASTERSEED,
    ZODIAC_BUTTON,
    BACKPACK_BUTTON,
    ATTACK_TIMEOUT,
    ATTACK_DIALER_PRESSED,
    ATTACK_DIALER_TIMEOUT,
    FACEBOOK_CONNECT,
    TWITTER,
    SHARE,
    SMS_SHARE,
    EMAIL_SHARE,
    ZODIAC_DIALER_COMPLETED,
    PERFORM_PLAYER_ATTACK,
    PERFORM_CPU_ATTACK,
    DONE_BUTTON,
    RECHARGE_BUTTON,
    CLOSE_BUTTON,
    ANIMATIONS_ENDED,
    SKY_SHOP_BUTTON,
    RUN_AWAY_BUTTON,
    RESUME_BUTTON,
    PORTRAIT_BUTTON,
    TEAM_MOGA_INFO,
    TEAM_ADD_MOGA,
    TEAM_DONE,
    TEAM_REMOVE_MOGA,
    TEAM_NAP_MOGA,
    TEAM_WAKE_UP_MOGA,
    TEAM_BUY_MOGA,
    TAMER_RETRY_CLAIM,
    TAMER_DISMISS_DIALOG,
    MANAGE_TEAM,
    DRINK_BLUE_COFFEE,
    CAPTURE_SUCCESS_COMPLETE,
    CAPTURE_FAIL_COMPLETE,
    NEXT_BUTTON,
    BACK_BUTTON,
    SKIP_BUTTON,
    ACCEPT_BUTTON,
    CANCEL_BUTTON,
    OK_BUTTON,
    MENU_BUTTON,
    CLAIM_PRIZE,
    LUCKY_MOGA;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static UIEvent[] valuesCustom() {
        UIEvent[] uIEventArrValuesCustom = values();
        int length = uIEventArrValuesCustom.length;
        UIEvent[] uIEventArr = new UIEvent[length];
        System.arraycopy(uIEventArrValuesCustom, 0, uIEventArr, 0, length);
        return uIEventArr;
    }
}
