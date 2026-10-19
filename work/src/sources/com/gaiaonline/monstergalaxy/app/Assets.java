package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.shop.ProductType;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Assets {
    public static TextureRegion addButton;
    public static TextureRegion addPressedButton;
    public static TextureRegion arrowDownBtn;
    private static TextureAtlas atlas;
    public static TextureRegion blueBtn;
    public static TextureRegion blueCoffeePortraitOn;
    public static TextureRegion bluePressedBtn;
    public static Map<ZodiacPowerType, TextureRegion> circles;
    public static TextureRegion closeBtn;
    public static TextureRegion closeBtnPress;
    private static TextureAtlas effects;
    public static TextureRegion greenBtn;
    public static TextureRegion greenBtnLarge;
    public static TextureRegion greenBtnLargePress;
    public static TextureRegion greenBtnPress;
    public static TextureRegion healthBar;
    public static TextureRegion healthBarBackground;
    public static TextureRegion hudBackground;
    public static TextureRegion infoButton;
    public static TextureRegion infoPressedButton;
    public static TextureRegion littleBlueCircle;
    public static TextureRegion mogaBackground;
    public static TextureRegion pauseOff;
    public static TextureRegion pauseOn;
    public static TextureRegion redBtn;
    public static TextureRegion redBtnPressed;
    public static TextureRegion redBtnSmall;
    public static TextureRegion redBtnSmallPressed;
    public static TextureRegion sidePortraitBg;
    public static TextureRegion starSeed;
    public static TextureRegion timerFull;
    public static Map<ZodiacSign, TextureRegion> zodiacAttackFills;
    public static Map<ZodiacSign, TextureRegion> zodiacAttackOutlines;

    public static Sprite loadSprite(String spriteName) {
        return effects.createSprite(spriteName);
    }

    public static TextureRegion loadTexture(String textureName) {
        return atlas.findRegion(textureName);
    }

    public static TextureRegion loadLocalizedTexture(String textureName) {
        String localizedTextureName = null;
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            String localeLangCode = I18nManager.getLocale();
            if (!localeLangCode.equals(I18nManager.DEFAULT_LOCALE)) {
                localizedTextureName = String.valueOf(textureName) + "." + localeLangCode;
            }
        }
        TextureRegion region = atlas.findRegion(localizedTextureName);
        if (region == null) {
            return atlas.findRegion(textureName);
        }
        return region;
    }

    public static TextureRegion load(Asset asset) {
        Pixmap pm = new Pixmap(AssetsDownloader.getAsset(asset));
        int width = pm.getWidth();
        int height = pm.getHeight();
        Texture texture = new Texture(MathUtils.nextPowerOfTwo(width), MathUtils.nextPowerOfTwo(height), pm.getFormat());
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture.draw(pm, 0, 0);
        TextureRegion newRegion = new TextureRegion(texture, 0, 0, width, height);
        return newRegion;
    }

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/hvga/pack"));
        effects = new TextureAtlas(Gdx.files.internal("data/effects/pack"));
        pauseOn = loadTexture("pause.on");
        pauseOff = loadTexture("pause.off");
        for (ProductType productType : ProductType.valuesCustom()) {
            productType.setIconAsset(loadTexture(productType.getIconAssetName()));
        }
        hudBackground = loadTexture("hudbg");
        mogaBackground = loadTexture("mogabg");
        sidePortraitBg = loadTexture("side.portrait.bg");
        healthBarBackground = loadTexture("hpbarbackground");
        healthBar = loadTexture("hpbarfull");
        timerFull = loadTexture("timerfull");
        greenBtnLarge = loadTexture("green.btn.large");
        greenBtnLargePress = loadTexture("green.btn.large.press");
        greenBtn = loadTexture("green.btn");
        greenBtnPress = loadTexture("green.btn.press");
        closeBtn = loadTexture("close.button");
        closeBtnPress = loadTexture("close.button.press");
        blueBtn = loadTexture("blue.btn");
        bluePressedBtn = loadTexture("blue.btn.press");
        arrowDownBtn = loadTexture("btn.arrow.down");
        starSeed = loadTexture("starseed");
        blueCoffeePortraitOn = loadTexture("blue.coffee.portrait.on");
        littleBlueCircle = loadTexture("little.blue.circle");
        redBtnSmall = loadTexture("red.btn.small");
        redBtnSmallPressed = loadTexture("red.btn.small.press");
        redBtn = loadTexture("red.btn");
        redBtnPressed = loadTexture("red.btn.press");
        circles = new HashMap();
        circles.put(ZodiacPowerType.BAD, loadTexture("red.circle"));
        circles.put(ZodiacPowerType.NORMAL, loadTexture("white.circle"));
        circles.put(ZodiacPowerType.GOOD, loadTexture("green.circle"));
        zodiacAttackOutlines = new HashMap();
        zodiacAttackFills = new HashMap();
        for (ZodiacSign sign : ZodiacSign.valuesCustom()) {
            zodiacAttackOutlines.put(sign, loadTexture(sign.name().toLowerCase()));
            zodiacAttackFills.put(sign, loadTexture(String.valueOf(sign.name().toLowerCase()) + ".fill"));
        }
        infoButton = loadTexture("info.button");
        infoPressedButton = loadTexture("info.button.press");
        addButton = loadTexture("add.button");
        addPressedButton = loadTexture("add.button.press");
        SoundManager.loadSound(SoundManager.SOUND_MOGA_ENTRANCE);
    }

    public static TextureRegion getZodiacSignAsset(ZodiacSign sign) {
        return loadTexture("sign." + sign.getName().toLowerCase());
    }

    public static TextureRegion loadFromFile(String assetName) {
        Pixmap p = new Pixmap(Gdx.files.internal(assetName));
        int width = p.getWidth();
        int height = p.getHeight();
        Texture t = new Texture(MathUtils.nextPowerOfTwo(width), MathUtils.nextPowerOfTwo(height), Pixmap.Format.RGBA8888);
        t.draw(p, 0, 0);
        TextureRegion tr = new TextureRegion(t, 0, 0, width, height);
        return tr;
    }
}
