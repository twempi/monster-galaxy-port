package com.gaiaonline.monstergalaxy.team;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.DownloadableElement;
import com.gaiaonline.monstergalaxy.screen.LifeElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
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
        TextElement descriptionLabel = new TextElement(moga.getType().getDescription(), 0.5f, ColorConstants.getColor("team_color_02"), true, 235.0f);
        descriptionLabel.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        descriptionLabel.setPosition(ScreenElement.RelPoint.CENTER, -45.0f, 100.0f);
        descriptionLabel.setWrapWidth(235.0f);
        descriptionLabel.enableVerticalAutoSize(55.0f);
        add(descriptionLabel);
        this.mogaImage = new DownloadableElement(moga.getType().getAsset());
        this.mogaImage.setPosition(ScreenElement.RelPoint.CENTER, -140.0f, 25.0f);
        add(this.mogaImage);
        TextureRegion rarity = Assets.loadLocalizedTexture(moga.getType().getRarityAssetName());
        ScreenElement rarityElement = new TextureElement(rarity);
        float rarityScale = ((float) rarity.getRegionWidth()) > 140.0f ? 140.0f / rarity.getRegionWidth() : 1.0f;
        rarityElement.setSize(rarity.getRegionWidth() * rarityScale, rarity.getRegionHeight() * rarityScale);
        rarityElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        rarityElement.setPosition(ScreenElement.RelPoint.CENTER, -140.0f, -40.0f);
        add(rarityElement);
        TextureRegion zodiacSign = Assets.getZodiacSignAsset(moga.getType().getZodiacSign());
        zodiacSign = zodiacSign == null ? Assets.loadTexture("sign.aries") : zodiacSign;
        float zodiacSignPosY = -88.0f;
        float zodiacLabelPosY = -85.0f;
        if (Game.getTrainer().getMogas().contains(moga)) {
            zodiacSignPosY = -125.0f;
            addLabel(String.valueOf(I18nManager.getText(I18nManager.I18nKey.LEVEL)) + " " + moga.getLevel(), 115.0f, 145.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
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
        addLabel(moga.getType().getZodiacSign().getLocalizedCamelCaseName(), -160.0f, zodiacLabelPosY, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.getColor("team_color_02"), false, false);
        Color zodiacSignTextColor = ColorConstants.getColor("team_color_02");
        TextureElement circleStrong = add(Assets.littleBlueCircle, -30.0f, 0.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.GOOD), 0.0f, 2.0f, ScreenElement.RelPoint.CENTER, circleStrong, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, 0.0f, 0.0f, ScreenElement.RelPoint.CENTER, circleStrong, ScreenElement.RelPoint.CENTER);
        addLabel(I18nManager.getText(I18nManager.I18nKey.STRENGTH_STRONG), 0.0f, 1.0f, ScreenElement.RelPoint.CENTER_BOTTOM, circleStrong, ScreenElement.RelPoint.CENTER_TOP, 0.45f, I18nManager.isAsianLocale() ? ColorConstants.PRIMARY_SHADOW_COLOR : ColorConstants.PRIMARY_FONT_COLOR, false, !I18nManager.isAsianLocale());
        TextureRegion zodiacBg = Assets.loadTexture("zodiac.bg");
        if (strong.size() == 4) {
            add(zodiacBg, 50.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(0)), 20.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(0).getLocalizedCamelCaseName(), 35.0f, 17.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 50.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(1)), 20.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(1).getLocalizedCamelCaseName(), 35.0f, -8.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(2)), 120.0f, 10.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(2).getLocalizedCamelCaseName(), 135.0f, 17.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(strong.get(3)), 120.0f, -15.0f, ScreenElement.RelPoint.CENTER);
            addLabel(strong.get(3).getLocalizedCamelCaseName(), 135.0f, -8.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
        }
        TextureElement circleNormal = add(Assets.littleBlueCircle, -30.0f, -55.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.NORMAL), 0.0f, 2.0f, ScreenElement.RelPoint.CENTER, circleNormal, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, 0.0f, 0.0f, ScreenElement.RelPoint.CENTER, circleNormal, ScreenElement.RelPoint.CENTER);
        addLabel(I18nManager.getText(I18nManager.I18nKey.STRENGTH_NORMAL), 0.0f, 1.0f, ScreenElement.RelPoint.CENTER_BOTTOM, circleNormal, ScreenElement.RelPoint.CENTER_TOP, 0.45f, I18nManager.isAsianLocale() ? ColorConstants.PRIMARY_SHADOW_COLOR : ColorConstants.PRIMARY_FONT_COLOR, false, !I18nManager.isAsianLocale());
        if (normal.size() == 4) {
            add(zodiacBg, 50.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(0)), 20.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(0).getLocalizedCamelCaseName(), 35.0f, -42.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 50.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(1)), 20.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(1).getLocalizedCamelCaseName(), 35.0f, -67.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(2)), 120.0f, -48.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(2).getLocalizedCamelCaseName(), 135.0f, -42.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(normal.get(3)), 120.0f, -73.0f, ScreenElement.RelPoint.CENTER);
            addLabel(normal.get(3).getLocalizedCamelCaseName(), 135.0f, -67.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
        }
        TextureElement circleBad = add(Assets.littleBlueCircle, -30.0f, -110.0f, ScreenElement.RelPoint.CENTER);
        add(Assets.circles.get(ZodiacPowerType.BAD), 0.0f, 2.0f, ScreenElement.RelPoint.CENTER, circleBad, ScreenElement.RelPoint.CENTER);
        add(zodiacSign, 0.0f, 0.0f, ScreenElement.RelPoint.CENTER, circleBad, ScreenElement.RelPoint.CENTER);
        addLabel(I18nManager.getText(I18nManager.I18nKey.STRENGTH_WEAK), 0.0f, 1.0f, ScreenElement.RelPoint.CENTER_BOTTOM, circleBad, ScreenElement.RelPoint.CENTER_TOP, 0.45f, I18nManager.isAsianLocale() ? ColorConstants.PRIMARY_SHADOW_COLOR : ColorConstants.PRIMARY_FONT_COLOR, false, !I18nManager.isAsianLocale());
        if (weak.size() == 4) {
            add(zodiacBg, 50.0f, -102.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(0)), 20.0f, -102.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(0).getLocalizedCamelCaseName(), 35.0f, -96.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 50.0f, -127.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(1)), 20.0f, -127.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(1).getLocalizedCamelCaseName(), 35.0f, -121.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -102.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(2)), 120.0f, -102.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(2).getLocalizedCamelCaseName(), 135.0f, -96.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
            add(zodiacBg, 150.0f, -127.0f, ScreenElement.RelPoint.CENTER);
            add(getZodiacSignAsset(weak.get(3)), 120.0f, -127.0f, ScreenElement.RelPoint.CENTER);
            addLabel(weak.get(3).getLocalizedCamelCaseName(), 135.0f, -121.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.4f, zodiacSignTextColor, false, false);
        }
    }

    private TextureRegion getZodiacSignAsset(ZodiacSign sign) {
        return Assets.loadTexture("team.sign." + sign.getName().toLowerCase());
    }

    @Override // com.gaiaonline.monstergalaxy.screen.Dialog
    public void pause() {
    }
}
