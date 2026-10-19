package com.gaiaonline.monstergalaxy.app.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class Fonts {
    private static Set<BitmapFont> customFonts = new HashSet();
    private static FreeTypeFontGenerator cyberbitFTFGen;
    public static BitmapFont italic;
    public static BitmapFont regular;

    public static void initialize() {
        FileHandle cyberbitFH;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/fonts/pack"));
        TextureRegion italicTexture = atlas.findRegion("VAGRunds");
        italic = new ScaleSafeBitmapFont(Gdx.files.internal("data/fonts/VAGRunds.fnt"), italicTexture, false);
        TextureRegion regularTexture = atlas.findRegion("VAGRundsRegular");
        regular = new ScaleSafeBitmapFont(Gdx.files.internal("data/fonts/VAGRundsRegular.fnt"), regularTexture, false);
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1 && (cyberbitFH = Gdx.files.internal("data/fonts/DroidSansFallback.mp3")) != null) {
            cyberbitFTFGen = new ScaleSafeFontGenerator(cyberbitFH);
        }
    }

    public static float getLocaleScaleFactor() {
        if (I18nManager.isAsianLocale()) {
        }
        return 1.0f;
    }

    private static String getUniqueChars(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Set<String> chars = new HashSet<>();
        for (int i = 0; i < text.length(); i++) {
            String c = text.substring(i, i + 1);
            if (chars.add(c)) {
                sb.append(c);
            }
        }
        if (sb.toString().contains("\u3000") && !sb.toString().contains(" ")) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static BitmapFont getFont(boolean italic2, String text) {
        if (text == null || text.trim().length() == 0) {
            return regular;
        }
        if (I18nManager.needAsianSupport(text)) {
            BitmapFont font = cyberbitFTFGen.generateFont(30, getUniqueChars(text), false);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            customFonts.add(font);
            return font;
        }
        if (italic2) {
            return italic;
        }
        return regular;
    }

    public static boolean isWesternFont(BitmapFont font) {
        return font == italic || font == regular;
    }

    public static void releaseFont(BitmapFont font) {
        if (font != null && customFonts.contains(font)) {
            Gdx.app.debug("Fonts", "Releasing font: " + font);
            font.getRegion().getTexture().dispose();
            font.dispose();
            customFonts.remove(font);
        }
    }
}
