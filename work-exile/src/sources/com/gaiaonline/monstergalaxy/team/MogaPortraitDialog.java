package com.gaiaonline.monstergalaxy.team;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.DownloadableElement;
import com.gaiaonline.monstergalaxy.screen.LifeElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MogaPortraitDialog extends Dialog {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType;
    private DownloadableElement mogaImage;

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType;
        if (iArr == null) {
            iArr = new int[ZodiacPowerType.valuesCustom().length];
            try {
                iArr[ZodiacPowerType.BAD.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ZodiacPowerType.GOOD.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ZodiacPowerType.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType = iArr;
        }
        return iArr;
    }

    public MogaPortraitDialog(ScreenListener listener, Moga moga) {
        super(listener);
        ZodiacHelper zodiacHelper = ZodiacHelper.getInstance();
        ZodiacSign mogaZodiac = moga.getType().getZodiacSign();
        List<ZodiacSign> strong = new ArrayList<>();
        List<ZodiacSign> normal = new ArrayList<>();
        List<ZodiacSign> weak = new ArrayList<>();
        for (ZodiacSign sign : ZodiacSign.valuesCustom()) {
            switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacPowerType()[zodiacHelper.getPowerTypeAgainst(mogaZodiac, sign).ordinal()]) {
                case 1:
                    strong.add(sign);
                    break;
                case 2:
                    normal.add(sign);
                    break;
                case 3:
                    weak.add(sign);
                    break;
            }
        }
        add(Assets.loadTexture("card.view.bg"), 0.0f, 0.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.CENTER);
        addButton(UIEvent.CLOSE_BUTTON, Assets.closeBtn, Assets.closeBtnPress, 212.0f, 133.0f, ScreenElement.RelPoint.CENTER);
        addLabel(moga.getName(), -200.0f, 145.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.75f, ColorConstants.getColor("team_color_01"), false, true);
        ScreenElement descriptionLabel = new TextElement(moga.getType().getDescription(), 0.5f, ColorConstants.getColor("team_color_02"), true, 233.0f);
        descriptionLabel.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        descriptionLabel.setPosition(ScreenElement.RelPoint.CENTER, -45.0f, 100.0f);
        add(descriptionLabel);
        this.mogaImage = new DownloadableElement(moga.getType().getAsset());
        this.mogaImage.setPosition(ScreenElement.RelPoint.CENTER, -140.0f, 25.0f);
        add(this.mogaImage);
        TextureRegion rarity = Assets.loadTexture(moga.getType().getRarityAssetName());
        add(rarity, -140.0f, -40.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.CENTER);
        TextureRegion zodiacSign = Assets.getZodiacSignAsset(moga.getType().getZodiacSign());
        zodiacSign = zodiacSign == null ? Assets.loadTexture("sign.aries") : zodiacSign;
        float zodiacSignPosY = -88.0f;
        float zodiacLabelPosY = -85.0f;
        if (Game.getTrainer().getMogas().contains(moga)) {
            zodiacSignPosY = -125.0f;
            addLabel("Level " + moga.getLevel(), 115.0f, 145.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
            add(Assets.loadTexture("empty.bar.card"), -141.0f, -70.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.CENTER);
            LifeElement healthBar = new LifeElement(Assets.loadTexture("full.hp.bar.card"));
            healthBar.setPosition(ScreenElement.RelPoint.CENTER, -209.0f, -68.0f);
            add(healthBar);
            healthBar.setMaxLife(moga.getMaxHitPoints());
            healthBar.setLife(moga.getHitPoints());
            String healthText = String.valueOf(moga.getHitPoints()) + "/" + moga.getMaxHitPoints() + " HP";
            addLabel(healthText, -136.0f, -68.0f, 0.4f, ColorConstants.FOURTH_FONT_COLOR, true);
            add(Assets.loadTexture("empty.bar.card"), -141.0f, -95.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.CENTER);
            ScreenElement experienceBar = new LifeElement(Assets.loadTexture("full.xp.bar.card"));
            experienceBar.setPosition(ScreenElement.RelPoint.CENTER, -209.0f, -93.0f);
            add(experienceBar);
            String experienceText = String.valueOf(moga.getCurrentXPForNextLevel()) + "/" + moga.getNeededXPForNextLevel() + " XP";
            addLabel(experienceText, -136.0f, -93.0f, 0.4f, ColorConstants.FOURTH_FONT_COLOR, true);
            zodiacLabelPosY = -122.0f;
        }
        add(Assets.littleBlueCircle, 1.0f - 180.0f, zodiacSignPosY - 1.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.NORMAL), -180.0f, zodiacSignPosY, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, -180.0f, zodiacSignPosY, ScreenElement.RelPoint.CENTER);
        addLabel(moga.getType().getZodiacSign().getCamelCaseName(), -160.0f, zodiacLabelPosY, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.getColor("team_color_02"), false, false);
        Color zodiacSignTextColor = ColorConstants.getColor("team_color_02");
        add(Assets.littleBlueCircle, -30.0f, 0.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.GOOD), -30.0f, 1.0f, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, -30.0f, 1.0f, ScreenElement.RelPoint.CENTER);
        addLabel("Strong", -50.0f, -10.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        TextureRegion zodiacBg = Assets.loadTexture("zodiac.bg");
        if (strong.size() == 4) {
            add(zodiacBg, 50.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(0)), 20.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(0).getCamelCaseName(), 35.0f, 17.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 50.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(1)), 20.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(1).getCamelCaseName(), 35.0f, -8.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(2)), 120.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(2).getCamelCaseName(), 135.0f, 17.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(3)), 120.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(3).getCamelCaseName(), 135.0f, -8.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
        }
        add(Assets.littleBlueCircle, -30.0f, -60.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.NORMAL), -30.0f, -59.0f, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, -30.0f, -59.0f, ScreenElement.RelPoint.CENTER);
        addLabel("Normal", -54.0f, -68.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        if (normal.size() == 4) {
            add(zodiacBg, 50.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(0)), 20.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(0).getCamelCaseName(), 35.0f, -42.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 50.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(1)), 20.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(1).getCamelCaseName(), 35.0f, -67.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(2)), 120.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(2).getCamelCaseName(), 135.0f, -42.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(3)), 120.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(3).getCamelCaseName(), 135.0f, -67.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
        }
        add(Assets.littleBlueCircle, -30.0f, -116.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.BAD), -30.0f, -115.0f, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, -30.0f, -115.0f, ScreenElement.RelPoint.CENTER);
        addLabel("Weak", -50.0f, -125.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
        if (weak.size() == 4) {
            add(zodiacBg, 50.0f, -108.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(0)), 20.0f, -108.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(0).getCamelCaseName(), 35.0f, -102.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 50.0f, -133.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(1)), 20.0f, -133.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(1).getCamelCaseName(), 35.0f, -127.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -108.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(2)), 120.0f, -108.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(2).getCamelCaseName(), 135.0f, -102.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -133.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(3)), 120.0f, -133.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(3).getCamelCaseName(), 135.0f, -127.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
        }
    }

    private TextureRegion getZodiacSignAsset(ZodiacSign sign) {
        return Assets.loadTexture("team.sign." + sign.getName().toLowerCase());
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void pause() {
        this.mogaImage.dispose();
    }
}
