package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement.RelPoint;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import java.util.Locale;

/**
 * In-game replacement for the Android options panel (a native view on the phone):
 * music and sound effect volume, language, and the original credits line.
 * Coordinates are in the game's 480x320 base units, relative to the screen centre.
 */
public final class DesktopOptions {
    private static final float LABEL_X = -195f;
    private static final float CONTROL_X = 45f;
    private static final float VALUE_X = 178f;
    private static final float TEXT_SCALE = 0.7f;

    private DesktopOptions() {
    }

    public static void addTo(final ScreenGroup root) {
        TextureElement panel = new TextureElement(Assets.loadTexture("bg.widePopup"));
        panel.setAnchorPoint(RelPoint.CENTER);
        panel.setPosition(RelPoint.CENTER, 0f, -22f);
        root.add(panel);

        SoundManager.loadSound(SoundManager.SOUND_MENU_SLIDE);
        addVolumeRow(root, I18nManager.getText(I18nManager.I18nKey.MUSIC), 52f, SettingsManager.getMusicVolume(), true);
        addVolumeRow(root, I18nManager.getText(I18nManager.I18nKey.SOUND_EFFECTS), 10f, SettingsManager.getSoundVolume(),
                false);
        addLanguageRow(root, -32f);

        Color small = Color.WHITE;
        root.addLabel(I18nManager.getText(I18nManager.I18nKey.DESIGNED_BY), 0f, -82f, RelPoint.CENTER, null,
                RelPoint.CENTER, 0.45f, small, true, false);
        root.addLabel(I18nManager.getText(I18nManager.I18nKey.GAIA_INFO) + "   v" + Game.getPlatformHelper().getVersionName()
                + " (" + Game.getPlatformHelper().getVersionCode() + ")", 0f, -100f, RelPoint.CENTER, null,
                RelPoint.CENTER, 0.45f, small, true, false);
    }

    private static void addVolumeRow(ScreenGroup root, String name, float y, float initial, final boolean music) {
        label(root, name, LABEL_X, y, RelPoint.LEFT_CENTER);
        final TextElement percent = label(root, percent(initial), VALUE_X, y, RelPoint.CENTER);
        VolumeSlider slider = new VolumeSlider(initial, new VolumeSlider.Listener() {
            public void onValue(float value, boolean done) {
                percent.setText(percent(value));
                if (music) {
                    SoundManager.setMusicVolume(value);
                    if (done) {
                        SettingsManager.setMusicVolume(value);
                    }
                } else {
                    SoundManager.setSoundsVolume(value);
                    if (done) {
                        SettingsManager.setSoundVolume(value);
                        SoundManager.playSound(SoundManager.SOUND_MENU_SLIDE);
                    }
                }
            }
        });
        slider.setAnchorPoint(RelPoint.CENTER);
        slider.setPosition(RelPoint.CENTER, CONTROL_X, y);
        root.add(slider);
    }

    private static void addLanguageRow(ScreenGroup root, float y) {
        label(root, "Language", LABEL_X, y, RelPoint.LEFT_CENTER);

        final String current = I18nManager.getLocale();
        final int[] selected = { Math.max(0, indexOf(current)) };

        TextureElement box = new TextureElement(Assets.loadTexture("name.bg"));
        box.setAnchorPoint(RelPoint.CENTER);
        box.setSize(120f, 32f);
        box.setPosition(RelPoint.CENTER, CONTROL_X, y);
        root.add(box);
        final TextElement name = label(root, displayName(selected[0]), CONTROL_X, y, RelPoint.CENTER);

        final ButtonElement apply = new ButtonElement(new ScreenListener() {
            public void onUIEvent(UIEvent event) {
                String language = I18nManager.AVAILABLE_LOCALES.get(selected[0]).getLanguage();
                if (!language.equals(current)) {
                    // Like the Android version: store the choice and restart the game.
                    I18nManager.setLocale(language);
                    DesktopLauncher.restart();
                }
            }
        }, null, Assets.loadTexture("green.btn.small"), Assets.loadTexture("green.btn.small.press"));
        apply.setAnchorPoint(RelPoint.CENTER);
        apply.setSize(62f, 32f);
        apply.setPosition(RelPoint.CENTER, VALUE_X, y);
        root.add(apply); // before its label, so the label draws on top
        final TextElement applyLabel = root.addLabel("Apply", 0f, 3f,
                RelPoint.CENTER, apply, RelPoint.CENTER, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true, false);
        apply.setVisible(false);
        applyLabel.setVisible(false);

        for (final int direction : new int[] { -1, 1 }) {
            ScreenListener step = new ScreenListener() {
                public void onUIEvent(UIEvent event) {
                    int n = I18nManager.AVAILABLE_LOCALES.size();
                    selected[0] = (selected[0] + direction + n) % n;
                    name.setText(displayName(selected[0]));
                    boolean changed = !I18nManager.AVAILABLE_LOCALES.get(selected[0]).getLanguage().equals(current);
                    apply.setVisible(changed);
                    applyLabel.setVisible(changed);
                    SoundManager.playSound(SoundManager.SOUND_UI_TAP);
                }
            };
            arrow(root, step, direction < 0 ? "arrow.left" : "arrow.right", CONTROL_X + direction * 82f, y);
        }
    }

    private static void arrow(ScreenGroup root, ScreenListener listener, String texture, float x, float y) {
        ButtonElement button = new ButtonElement(listener, null, Assets.loadTexture(texture));
        button.setAnchorPoint(RelPoint.CENTER);
        button.setSize(36f, 31f);
        button.setPosition(RelPoint.CENTER, x, y);
        root.add(button);
    }

    private static TextElement label(ScreenGroup root, String text, float x, float y, RelPoint anchor) {
        return root.addLabel(text, x, y, RelPoint.CENTER, null, anchor, TEXT_SCALE, Color.WHITE, true, true);
    }

    private static String percent(float value) {
        return Math.round(value * 100) + "%";
    }

    private static int indexOf(String language) {
        for (int i = 0; i < I18nManager.AVAILABLE_LOCALES.size(); i++) {
            if (I18nManager.AVAILABLE_LOCALES.get(i).getLanguage().equals(language)) {
                return i;
            }
        }
        return -1;
    }

    /** Native names for Latin-script languages; the menu font has no CJK glyphs. */
    private static String displayName(int index) {
        Locale locale = I18nManager.AVAILABLE_LOCALES.get(index);
        String language = locale.getLanguage();
        Locale shownIn = language.equals("ja") || language.equals("ko") || language.equals("zh") ? Locale.ENGLISH : locale;
        String name = locale.getDisplayLanguage(shownIn);
        return name.substring(0, 1).toUpperCase(shownIn) + name.substring(1);
    }
}
