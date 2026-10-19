package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.LifeElement;
import com.gaiaonline.monstergalaxy.screen.RotaryTextureElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class Hud extends ButtonUI {
    private static final float ANIMATED_HP_BAR_UPDATE_TIME = 0.5f;
    private boolean animatingHealth;
    private TextureElement arrowDown;
    private RotaryTextureElement arrowRight;
    private TextElement health;
    private float healthAnimationAccTime;
    private LifeElement healthBar;
    private int lastHealth;
    private TextElement level;
    private Moga moga;
    private TextElement name;
    private ButtonElement portrait;
    private TextureElement signEffectiveness;
    protected TextureElement topHud;
    private TextureElement zodiacSign;

    public Hud(ScreenListener screen) {
        super(screen);
        setStretchable(false);
        setPosition(ScreenElement.RelPoint.LEFT_TOP, 0.0f, 0.0f);
        setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        addBackground(Assets.hudBackground);
        add(Assets.healthBarBackground, 10.0f, -5.0f, ScreenElement.RelPoint.LEFT_CENTER, ScreenElement.RelPoint.LEFT_CENTER);
        this.healthBar = new LifeElement(Assets.healthBar, 75.0f);
        this.healthBar.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 10.0f, -5.0f);
        add(this.healthBar);
        this.health = addLabel("", 30.0f, -10.0f, 0.7f, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.health.setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        add(Assets.mogaBackground, -70.0f, -10.0f);
        this.portrait = addButton(UIEvent.PORTRAIT_BUTTON, Assets.mogaBackground, Assets.mogaBackground, -70.0f, -10.0f);
        this.arrowDown = add(Assets.arrowDownBtn, -80.0f, -35.0f);
        this.arrowDown.setVisible(false);
        this.arrowRight = new RotaryTextureElement(Assets.arrowDownBtn);
        this.arrowRight.setPosition(ScreenElement.RelPoint.CENTER, -80.0f, -35.0f);
        this.arrowRight.setRotation(90.0f);
        this.arrowRight.setVisible(false);
        add(this.arrowRight);
        this.name = addLabel("", 10.0f, 22.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        this.level = addLabel("", 80.0f, 18.0f, ANIMATED_HP_BAR_UPDATE_TIME, ColorConstants.PRIMARY_FONT_COLOR, true, true);
        add(Assets.littleBlueCircle, -94.0f, 12.0f);
        this.signEffectiveness = new TextureElement(Assets.circles.get(ZodiacPowerType.NORMAL));
        TextureRegion sign = Assets.circles.get(ZodiacPowerType.NORMAL);
        this.signEffectiveness.setSize(sign.getRegionWidth(), sign.getRegionHeight());
        this.signEffectiveness.setPosition(ScreenElement.RelPoint.CENTER, -95.0f, 14.0f);
        add(this.signEffectiveness);
        this.zodiacSign = add(Assets.circles.get(ZodiacPowerType.NORMAL), -95.0f, 14.0f);
    }

    public void setMoga(Moga moga, ZodiacPowerType powerType) {
        this.moga = moga;
        if (powerType != null) {
            setZodiacPowerType(powerType);
        }
        this.portrait.setNormal(moga.getType().getPortrait());
        this.portrait.setSelected(moga.getType().getPortrait());
        int textLongitude = this.moga.getName().length();
        float fontScale = 0.6f;
        if (textLongitude > 10) {
            fontScale = (float) (((double) 0.6f) - (0.01d * ((double) textLongitude)));
            this.name.setPositionY(18.0f);
        } else {
            this.name.setPositionY(22.0f);
        }
        this.name.setFontScale(fontScale);
        this.name.setText(moga.getName());
        this.level.setText(I18nManager.getText(I18nManager.I18nKey.LEVEL_ABBREV).replace("%d", String.valueOf(moga.getLevel())));
        this.healthBar.setMaxLife(moga.getMaxHitPoints());
        updateHealth();
        this.zodiacSign.setTexture(Assets.getZodiacSignAsset(moga.getType().getZodiacSign()));
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        if (this.animatingHealth) {
            updateHealthAnimatedProgress(deltaTime);
        }
        super.update(deltaTime);
    }

    public void setZodiacPowerType(ZodiacPowerType powerType) {
        if (this.signEffectiveness != null) {
            this.signEffectiveness.setTexture(Assets.circles.get(powerType));
        }
    }

    public void updateHealthAnimated() {
        this.animatingHealth = true;
        this.healthAnimationAccTime = 0.0f;
    }

    private void updateHealthAnimatedProgress(float deltaTime) {
        this.healthAnimationAccTime += deltaTime;
        int hpIncrement = this.moga.getHitPoints() - this.lastHealth;
        int hitPoints = (int) Math.ceil(this.lastHealth + ((hpIncrement * this.healthAnimationAccTime) / ANIMATED_HP_BAR_UPDATE_TIME));
        if (hitPoints <= this.moga.getHitPoints()) {
            updateHealth();
        } else {
            setHealth(hitPoints);
        }
    }

    private void setHealth(int hitPoints) {
        this.health.setText(String.valueOf(hitPoints) + "/" + this.moga.getMaxHitPoints());
        this.healthBar.setVisible(true);
        this.healthBar.setLife(hitPoints);
    }

    public void updateHealth() {
        this.animatingHealth = false;
        int hitPoints = this.moga.getHitPoints();
        setHealth(hitPoints);
        this.lastHealth = hitPoints;
    }

    public void showArrowDown() {
        this.arrowDown.setVisible(true);
        this.arrowRight.setVisible(false);
    }

    public void showArrowRight() {
        this.arrowDown.setVisible(false);
        this.arrowRight.setVisible(true);
    }

    public void hideArrow() {
        this.arrowDown.setVisible(false);
        this.arrowRight.setVisible(false);
    }
}
