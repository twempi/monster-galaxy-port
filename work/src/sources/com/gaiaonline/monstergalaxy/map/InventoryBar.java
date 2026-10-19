package com.gaiaonline.monstergalaxy.map;

import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;

/* JADX INFO: loaded from: classes.dex */
public class InventoryBar extends ScreenGroup {
    private static final float FONT_SCALE_OVER_MAX_VALUE = 0.42f;
    private static final float FONT_SCALE_REGULAR = 0.52f;
    private ShadowTextElement blueCoffeesText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
    private ShadowTextElement coinsText;
    private ShadowTextElement starseedsText;

    public InventoryBar() {
        this.blueCoffeesText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        float offsetx = ResolutionManager.getHorizontalAR() / ResolutionManager.getVerticalAR();
        float xPos = 113.0f - (113.0f * offsetx);
        this.blueCoffeesText.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, (-136.0f) - xPos, 0.0f);
        add(this.blueCoffeesText);
        this.starseedsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.starseedsText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        float xPos2 = 67.0f - (67.0f * offsetx);
        this.starseedsText.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, (-90.0f) - xPos2, 0.0f);
        add(this.starseedsText);
        this.coinsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.coinsText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        float xPos3 = 8.0f - (8.0f * offsetx);
        this.coinsText.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, (-35.0f) - xPos3, 0.0f);
        add(this.coinsText);
        setSize(169.0f, 20.0f);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        super.update(deltaTime);
        Trainer trainer = Game.getTrainer();
        updateField(this.blueCoffeesText, trainer.getBlueCoffeeCups(), 999);
        updateField(this.starseedsText, trainer.getStarSeeds(), 999);
        updateField(this.coinsText, trainer.getCoins(), Constants.MAX_DISPLAY_VALUE_MOGA_CASH);
    }

    private void updateField(TextElement field, int value, int maxDisplayValue) {
        if (value <= maxDisplayValue) {
            field.setText(String.valueOf(value));
            field.setFontScale(FONT_SCALE_REGULAR);
        } else {
            field.setText("+" + maxDisplayValue);
            field.setFontScale(FONT_SCALE_OVER_MAX_VALUE);
        }
    }
}
