package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.graphics.Color;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.screen.ScreenElement.RelPoint;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;

/**
 * In-game replacement for the Android options panel (a native view on the phone):
 * music and sound effect volume, and the original credits lines.
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
        addVolumeRow(root, "Music", 46f, SettingsManager.getMusicVolume(), true);
        addVolumeRow(root, "Sound Effects", 0f, SettingsManager.getSoundVolume(), false);

        // The credits text from the Android options screen (res/layout/settings.xml).
        Color small = Color.WHITE;
        root.addLabel("Designed, developed, and published by Gaia Interactive.", 0f, -52f, RelPoint.CENTER, null,
                RelPoint.CENTER, 0.45f, small, true, false);
        root.addLabel("\u00a9 2011 Gaia Interactive, Inc.   v" + Game.getPlatformHelper().getVersionName()
                + " (" + Game.getPlatformHelper().getVersionCode() + ")", 0f, -70f, RelPoint.CENTER, null,
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

    private static TextElement label(ScreenGroup root, String text, float x, float y, RelPoint anchor) {
        return root.addLabel(text, x, y, RelPoint.CENTER, null, anchor, TEXT_SCALE, Color.WHITE, true, true);
    }

    private static String percent(float value) {
        return Math.round(value * 100) + "%";
    }
}
