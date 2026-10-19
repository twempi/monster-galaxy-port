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
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.shop.ProductType;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Assets {
    public static TextureRegion addButton;
    public static TextureRegion addPressedButton;
    public static TextureRegion arrowDownBtn;
    public static TextureRegion arrowRight;
    private static TextureAtlas atlas;
    public static TextureRegion attackBtn;
    public static TextureRegion attackPressedBtn;
    public static TextureRegion blueBtn;
    public static TextureRegion blueCoffeePortraitOn;
    public static TextureRegion bluePressedBtn;
    public static TextureRegion captureBtn;
    public static TextureRegion captureDisabledBtn;
    public static TextureRegion capturePressedBtn;
    public static Map<ZodiacPowerType, TextureRegion> circles;
    public static TextureRegion closeBtn;
    public static TextureRegion closeBtnPress;
    public static TextureRegion congrazzles;
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
    public static TextureRegion tapOnZodiac;
    public static TextureRegion timerFull;
    public static TextureRegion vs;
    public static Map<ZodiacSign, TextureRegion> zodiacAttackFills;
    public static Map<ZodiacSign, TextureRegion> zodiacAttackOutlines;
    public static TextureRegion zodiacBtn;
    public static TextureRegion zodiacDisabledBtn;
    public static TextureRegion zodiacPressedBtn;

    public static Sprite loadSprite(String spriteName) {
        return effects.createSprite(spriteName);
    }

    public static TextureRegion loadTexture(String textureName) {
        return atlas.findRegion(textureName);
    }

    public static TextureRegion load(Asset asset) {
        Pixmap pm = new Pixmap(AssetsDownloader.getAsset(asset));
        int width = pm.getWidth();
        int height = pm.getHeight();
        Texture texture = new Texture(MathUtils.nextPowerOfTwo(width), MathUtils.nextPowerOfTwo(height), pm.getFormat());
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture.draw(pm, 0, 0);
        TextureRegion newRegion = new TextureRegion(texture, 0, 0, width, height);
        pm.dispose();
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
        attackBtn = loadTexture("btn.attack");
        attackPressedBtn = loadTexture("btn.attack.press");
        captureBtn = loadTexture("btn.capture");
        capturePressedBtn = loadTexture("btn.capture.press");
        captureDisabledBtn = loadTexture("btn.capture.disabled");
        zodiacBtn = loadTexture("btn.zodiac");
        zodiacPressedBtn = loadTexture("btn.zodiac.press");
        zodiacDisabledBtn = loadTexture("btn.zodiac.disabled");
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
        congrazzles = loadTexture("congrazzles");
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
        vs = loadTexture("vs");
        tapOnZodiac = loadTexture("tap");
        arrowRight = loadTexture("arrow");
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
        p.dispose();
        return tr;
    }
}
