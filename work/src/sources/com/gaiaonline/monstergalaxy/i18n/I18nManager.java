package com.gaiaonline.monstergalaxy.i18n;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/* JADX INFO: loaded from: classes.dex */
public class I18nManager {
    public static final List<Locale> AVAILABLE_LOCALES;
    public static final String DEFAULT_LOCALE = "en";
    public static Locale cachedLocale;
    static ResourceBundle resourceBundle;

    public enum I18nKey {
        NEXT,
        DONE,
        BACK,
        FINISH,
        NAME_YOUR_TAMER,
        CHOOSE_YOUR_TAMER,
        NOT_READY_FOR_ISLAND,
        HIDE,
        SHOW,
        MORE_MOGAS,
        ONE_MORE_MOGA,
        CLOSE_TO_DEFEAT,
        KEEP_PLAYING,
        NO_MOGAS_ON_TEAM,
        MOGAS_TIRED,
        MANAGE_TEAM,
        TAMER_OUT_ENERGY,
        MANAGE_TAMER,
        REMEMBER_CAPTURE,
        MORE,
        YOU_CAN_SEE_STATUS,
        YOU_CAN_USE_MENU,
        YOU_HAVE_A_TEAM,
        MUST_VISIT_LOCATION,
        THERE_ARE_SECRET_MOGAS,
        GET_LUCKY,
        THANK_YOU_FEEDBACK,
        VISIT_SHOP,
        DONT_HAVE_STARSEED,
        MOGA_TIRED,
        RECHARGE,
        DONT_HAVE_BLUECOFFE,
        LEVEL_CAPS,
        NEW_HIGHSCORE,
        ITEMS_FOUND,
        PREFECT_ATTACK,
        VERY_GOOD_ATTACK,
        WIN,
        REACHED_MAX_LEVEL,
        LEVEL_UP,
        LUCKY_MOGA,
        MOGAS_CAUGHT_COLON,
        SHARE,
        GET_YOUR_DAYLY_PRIZE,
        DAILY_PRIZE,
        CHECKING,
        CANT_GET_DAILY_PRIZE_INFO,
        TOMORROW,
        CLAIM_PRIZE,
        CLAIMING,
        DAILY_PRIZE_NOT_AVAILABLE,
        LOGOUT,
        CONNECT,
        CANT_GET_DAILY_PRIZE_INFO_GAIA,
        RETRY,
        MOGA_CASH,
        ITEMS,
        DEALS,
        LABEL_BAD,
        LABEL_GOOD,
        PLAY_FOR_PRIZES,
        NO_ENOUGH_MOGACASH,
        NO_LUCKYMOGA_AVAILABLE,
        OK,
        MOGAS_TIRED_NOBLUECOFFE,
        SOME_MOGAS_TIRED,
        NAP_ALL,
        IF_MOGAS_TIRED,
        GJOB_COMPLETION_NODE,
        GJOB_WON_STARS_ON_NODE,
        GJOB_CAPTURED_QTY_MOGAS,
        GJOB_KILLED_QTY_MOGAS,
        RECHARGE_DIALOG_TXT,
        RECHARGE_DIALOG_BTN,
        CAPTURE_TOO_WILD,
        CAPTURE_ALREADY_CAPTURED,
        CAPTURE_TOO_STRONG,
        CAPTURE_CAN_BE_CAPTURED,
        FACEBOOK_SHARE_MSG,
        FACEBOOK_NOT_CONNECTED,
        TWITTER_SHARE_MSG,
        SMS_SHARE_MSG,
        EMAIL_SHARE_MSG,
        EMAIL_SHARE_SUBJECT,
        SHARE_DESCRIPTION,
        RETRIEVING_REIMBURSEMENT,
        CLAIM,
        SOUND_EFFECTS,
        MUSIC,
        TAMER_ALERTS,
        TAMER_RECHARGED,
        DAILY_PRIZE_ALERT,
        DONT_MISS_DAILYPRIZE,
        MOGA_RESTED,
        FIND_OUT_MOGAS,
        STORY_ALERTS,
        ISLAND_ALERTS,
        DESIGNED_BY,
        GAIA_INFO,
        CHOOSE_YOUR_MOGA,
        NAME_YOUR_MOGA,
        BUY,
        PLAY,
        NO_QUESTS_NOW,
        CANCEL,
        ACCEPT,
        SKIP,
        YOU_GOT_COLON,
        REWARDS_COLON,
        THROW_YOUR_FIRST_ATTACK,
        TUTORIAL_SK1_SCREEN1_TEXT,
        TUTORIAL_SK1_SCREEN2_TEXT,
        TUTORIAL_SK1_SCREEN3_TEXT,
        GO,
        ZODIAC,
        PAUSED,
        RESUME,
        RUN_AWAY,
        ATTACK,
        YOU_VE_CAUGHT,
        YOU_WON,
        XP_EARNED,
        MOGAS_CAUGHT,
        BEGINNER,
        COMMON,
        UNCOMMON,
        RARE,
        SUPER_RARE,
        EPIC,
        LEGENDARY,
        ARIES,
        TAURUS,
        GEMINI,
        CANCER,
        LEO,
        VIRGO,
        LIBRA,
        SCORPIO,
        SAGITTARIUS,
        CAPRICORN,
        AQUARIUS,
        PISCES,
        YOU_JUST_WON,
        YOU_JUST_WON_BUNDLE,
        PLAY_AGAIN,
        TUTORIAL_SK2_SCREEN1_TEXT,
        TUTORIAL_SK2_SCREEN2_TEXT,
        TUTORIAL_SK2_SCREEN3_TEXT,
        STARSEED,
        MASTER_STARSEED,
        YOU_HAVE_COLON,
        SUCCESS,
        CAPTURE_MOGA_QUESTION,
        USING_BLUE_COFFEES,
        BLUE_COFFEES_TUTORIAL_STEPS,
        DURING_BATTLE_COLON,
        USING_STARSEEDS,
        STARSEEDS_TUTORIAL_STEPS,
        LEVEL,
        STRENGTH_STRONG,
        STRENGTH_NORMAL,
        STRENGTH_WEAK,
        UPDATE_AVAILABLE,
        UPDATE_YES,
        UPDATE_NO,
        LEVEL_ABBREV,
        NOT_CAPTURED,
        DONT_HAVE_MOGA_CASH,
        VISIT_URL_FOR_MORE_INFO_1,
        VISIT_URL_FOR_MORE_INFO_2,
        INFO,
        SKY_SHOP,
        DAILY_PRIZE_GET_IT_AT,
        DAILY_PRIZE_GET_IT_TOMORROW_AT,
        TOTAL,
        DEALS_FREE_STARSEEDS,
        DEALS_SEE_DEAL,
        DEALS_NO_APP_TRAILERS_AVAILABLE,
        DEALS_EARN_STARSEEDS_BY,
        PLEASE_WAIT,
        DO_YOU_LIKE_THIS_GAME,
        YES,
        NO;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static I18nKey[] valuesCustom() {
            I18nKey[] i18nKeyArrValuesCustom = values();
            int length = i18nKeyArrValuesCustom.length;
            I18nKey[] i18nKeyArr = new I18nKey[length];
            System.arraycopy(i18nKeyArrValuesCustom, 0, i18nKeyArr, 0, length);
            return i18nKeyArr;
        }
    }

    static {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale(DEFAULT_LOCALE));
        locales.add(new Locale("es"));
        locales.add(new Locale("de"));
        locales.add(new Locale("fr"));
        locales.add(new Locale("ja"));
        locales.add(new Locale("ko"));
        locales.add(new Locale("zh"));
        AVAILABLE_LOCALES = Collections.unmodifiableList(locales);
    }

    public static String getText(I18nKey key) {
        if (resourceBundle == null) {
            init();
        }
        return resourceBundle.getString(key.toString());
    }

    public static void init() {
        String locale = getLocale();
        FileHandle rootFileHandle = null;
        try {
            rootFileHandle = Gdx.files.internal("strings_" + locale + ".properties");
        } catch (Exception e1) {
            Gdx.app.log("I18nManager", "Error", e1);
        }
        if (!rootFileHandle.exists()) {
            rootFileHandle = Gdx.files.internal("strings_en.properties");
        }
        try {
            resourceBundle = new PropertyResourceBundle(rootFileHandle.read());
        } catch (IOException e) {
            Gdx.app.log("I18nManager", "Error", e);
        }
    }

    public static synchronized String getLocale() {
        String language;
        if (cachedLocale != null) {
            language = cachedLocale.getLanguage();
        } else if (Game.getPlatformHelper().getSkin() != PlatformHelper.MGSkin.SK1) {
            cachedLocale = new Locale(DEFAULT_LOCALE);
            language = cachedLocale.getLanguage();
        } else {
            String userSelectedLanguage = Game.getPlatformHelper().getUserSelectedLocale();
            if (userSelectedLanguage != null) {
                cachedLocale = new Locale(userSelectedLanguage);
            } else {
                cachedLocale = new Locale(getSystemLocale());
            }
            if (!AVAILABLE_LOCALES.contains(cachedLocale)) {
                cachedLocale = new Locale(DEFAULT_LOCALE);
            }
            language = cachedLocale.getLanguage();
        }
        return language;
    }

    public static boolean isAsianLocale() {
        String locale = getLocale();
        return locale.equals("ja") || locale.equals("ko") || locale.equals("zh");
    }

    public static boolean containsAsianChars(String text) {
        if (text == null || text.trim().length() == 0) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 256 && c <= 65535) {
                return true;
            }
        }
        return false;
    }

    public static boolean needAsianSupport(String text) {
        if (isAsianLocale()) {
            return true;
        }
        return containsAsianChars(text);
    }

    public static String getSystemLocale() {
        return Locale.getDefault().getLanguage();
    }

    public static synchronized void setLocale(String locale) {
        cachedLocale = null;
        Game.getPlatformHelper().setUserSelectedLocale(locale);
    }
}
