package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.TintToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;

/* JADX INFO: loaded from: classes.dex */
public class MogaMiniHud extends Group {
    private static final float HIDE_DURATION = 0.5f;
    private static final float SHOW_DURATION = 0.5f;
    private BlueCoffeeEffect effectActor;
    private TextureRegion hb;
    private RegionImage healthBar;
    private Image healthBarBg;
    private Image levelBg;
    private Listener listener;
    private Moga moga;
    private int mogaIndex;
    private Group overPortraitGroup;
    private Group portraitGroup;
    private Image signEffectiveness;
    private Group underPortraitGroup;

    public interface Listener {
        void onBlueCoffee(int i);

        void onMogaSelected(int i);
    }

    public MogaMiniHud(Moga moga, int mogaIndex, Listener listener) {
        this.moga = moga;
        this.mogaIndex = mogaIndex;
        this.listener = listener;
        String hudName = "minihud" + mogaIndex;
        float scaleFactor = ResolutionManager.getScaleFactor();
        Image portraitBg = ResolutionManager.getScaledImage("hud_bg_" + hudName, Assets.mogaBackground);
        portraitBg.setX((-3.0f) * scaleFactor);
        Image signEffectivenessBg = ResolutionManager.getScaledImage("signBg", Assets.littleBlueCircle);
        signEffectivenessBg.setX(1.0f * scaleFactor);
        signEffectivenessBg.setY(portraitBg.getHeight() - signEffectivenessBg.getHeight());
        this.signEffectiveness = ResolutionManager.getScaledImage("sign", Assets.circles.get(ZodiacPowerType.NORMAL));
        this.signEffectiveness.setX(signEffectivenessBg.getX() + ((signEffectivenessBg.getWidth() - this.signEffectiveness.getWidth()) * 0.4f));
        this.signEffectiveness.setY(signEffectivenessBg.getY() + ((signEffectivenessBg.getHeight() - this.signEffectiveness.getHeight()) * 0.75f));
        Image zodiacSign = ResolutionManager.getScaledImage("", Assets.getZodiacSignAsset(moga.getType().getZodiacSign()));
        if (zodiacSign.getDrawable() != null) {
            zodiacSign.setX(this.signEffectiveness.getX() + ((this.signEffectiveness.getWidth() - zodiacSign.getWidth()) / 2.0f));
            zodiacSign.setY(this.signEffectiveness.getY() + ((this.signEffectiveness.getHeight() - zodiacSign.getHeight()) / 2.0f));
        }
        this.levelBg = ResolutionManager.getScaledImage("hud_level_bg_" + hudName, Assets.sidePortraitBg);
        this.levelBg.setX(portraitBg.getX() + (portraitBg.getWidth() * 0.85f));
        this.levelBg.setY(portraitBg.getY() + (portraitBg.getHeight() / 2.0f));
        float hbScale = scaleFactor * 0.7f;
        int regionSize = (int) ((this.levelBg.getWidth() / hbScale) + 6.0f);
        TextureRegion hbBg = new TextureRegion(Assets.healthBarBackground);
        hbBg.setRegionX((Assets.healthBarBackground.getRegionX() + Assets.healthBarBackground.getRegionWidth()) - regionSize);
        this.healthBarBg = new Image(hbBg);
        this.healthBarBg.setWidth(regionSize * hbScale);
        this.healthBarBg.setHeight(Assets.healthBarBackground.getRegionHeight() * hbScale);
        this.healthBarBg.setY((portraitBg.getY() + (portraitBg.getHeight() / 2.0f)) - (this.healthBarBg.getHeight() * 0.8f));
        this.healthBarBg.setX(this.levelBg.getX());
        int regionSize2 = (int) ((this.levelBg.getWidth() / hbScale) - 2.0f);
        this.hb = new TextureRegion(Assets.healthBar);
        this.hb.setRegionX((Assets.healthBar.getRegionX() + Assets.healthBar.getRegionWidth()) - regionSize2);
        this.healthBar = new RegionImage("", this.hb);
        this.healthBar.setHeight(Assets.healthBar.getRegionHeight() * hbScale);
        this.healthBar.setY(this.healthBarBg.getY() + ((this.healthBarBg.getHeight() - this.healthBar.getHeight()) / 2.0f));
        this.healthBar.setX(this.healthBarBg.getX());
        updateHealthbar();
        Button blueCoffee = new Button(new TextureRegionDrawable(Assets.blueCoffeePortraitOn));
        blueCoffee.addListener(new ClickListener() { // from class: com.gaiaonline.monstergalaxy.battle.MogaMiniHud.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener
            public void clicked(InputEvent event, float x, float y) {
                MogaMiniHud.this.listener.onBlueCoffee(MogaMiniHud.this.mogaIndex);
            }
        });
        blueCoffee.setX(this.healthBarBg.getX() + this.healthBarBg.getWidth());
        blueCoffee.setY(this.healthBarBg.getY());
        Button portrait = new Button(new TextureRegionDrawable(moga.getType().getPortrait()));
        portrait.addListener(new ClickListener() { // from class: com.gaiaonline.monstergalaxy.battle.MogaMiniHud.2
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener
            public void clicked(InputEvent event, float x, float y) {
                MogaMiniHud.this.onMogaSelected();
            }
        });
        portrait.setX(12.0f * scaleFactor);
        portrait.setY(11.0f * scaleFactor);
        ResolutionManager.scaleActor(portrait);
        ResolutionManager.scaleActor(blueCoffee);
        String levelText = I18nManager.getText(I18nManager.I18nKey.LEVEL_ABBREV).replace("%d", String.valueOf(moga.getLevel()));
        AutoFontLabel levelLabel = new AutoFontLabel(levelText, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, false, ColorConstants.PRIMARY_SHADOW_COLOR);
        levelLabel.setX((this.levelBg.getX() + (this.levelBg.getWidth() / 2.0f)) - (levelLabel.getWidth() / 2.0f));
        levelLabel.setY((this.levelBg.getY() + (this.levelBg.getHeight() / 2.0f)) - (levelLabel.getHeight() / 2.0f));
        setWidth(portraitBg.getWidth());
        setHeight(portraitBg.getHeight());
        this.effectActor = new BlueCoffeeEffect("effect");
        this.underPortraitGroup = new Group();
        this.portraitGroup = new Group();
        this.overPortraitGroup = new Group();
        this.underPortraitGroup.addActor(this.healthBarBg);
        this.underPortraitGroup.addActor(this.healthBar);
        this.underPortraitGroup.addActor(this.levelBg);
        this.underPortraitGroup.addActor(levelLabel);
        this.portraitGroup.addActor(portraitBg);
        this.portraitGroup.addActor(portrait);
        this.overPortraitGroup.addActor(signEffectivenessBg);
        this.overPortraitGroup.addActor(this.signEffectiveness);
        if (zodiacSign.getDrawable() != null) {
            this.overPortraitGroup.addActor(zodiacSign);
        }
        this.overPortraitGroup.addActor(blueCoffee);
        this.overPortraitGroup.addActor(this.effectActor);
        addActor(this.underPortraitGroup);
        addActor(this.portraitGroup);
        addActor(this.overPortraitGroup);
    }

    public void showPortraitOnly(boolean animate) {
        if (animate) {
            this.underPortraitGroup.getColor().a = 1.0f;
            this.overPortraitGroup.getColor().a = 1.0f;
            TintToAction tintTo1 = new TintToAction();
            tintTo1.setTint(1.0f, 1.0f, 1.0f, Float.valueOf(0.0f));
            tintTo1.setDuration(0.5f);
            TintToAction tintTo2 = new TintToAction();
            tintTo2.setTint(1.0f, 1.0f, 1.0f, Float.valueOf(0.0f));
            tintTo2.setDuration(0.5f);
            this.underPortraitGroup.addAction(tintTo1);
            this.overPortraitGroup.addAction(tintTo2);
            return;
        }
        this.underPortraitGroup.getColor().a = 0.0f;
        this.overPortraitGroup.getColor().a = 0.0f;
    }

    public void showAll(boolean animate) {
        if (animate) {
            TintToAction tintTo1 = new TintToAction();
            tintTo1.setTint(1.0f, 1.0f, 1.0f, Float.valueOf(1.0f));
            tintTo1.setDuration(0.5f);
            TintToAction tintTo2 = new TintToAction();
            tintTo2.setTint(1.0f, 1.0f, 1.0f, Float.valueOf(1.0f));
            tintTo2.setDuration(0.5f);
            this.underPortraitGroup.getColor().a = 0.0f;
            this.overPortraitGroup.getColor().a = 0.0f;
            this.underPortraitGroup.addAction(tintTo1);
            this.overPortraitGroup.addAction(tintTo2);
            return;
        }
        this.underPortraitGroup.getColor().a = 1.0f;
        this.overPortraitGroup.getColor().a = 1.0f;
    }

    public void setRival(Moga rival) {
        this.signEffectiveness.setDrawable(new TextureRegionDrawable(Assets.circles.get(ZodiacHelper.getInstance().getPowerTypeAgainst(this.moga.getType().getZodiacSign(), rival.getType().getZodiacSign()))));
    }

    public void updateHealthbar() {
        float hbScale = ResolutionManager.getScaleFactor() * 0.7f;
        float barCompletion = this.moga.getHitPoints() / this.moga.getMaxHitPoints();
        float regionSize = (int) ((this.levelBg.getWidth() / hbScale) - 2.0f);
        this.hb.setRegionX((int) ((Assets.healthBar.getRegionX() + Assets.healthBar.getRegionWidth()) - regionSize));
        this.healthBar.setWidth(regionSize * barCompletion * hbScale);
        this.healthBar.setRegion((int) (this.hb.getRegionWidth() * (1.0f - barCompletion)), 0, (int) (this.hb.getRegionWidth() * barCompletion), this.hb.getRegionHeight());
    }

    public void blueCoffeeApplied() {
        updateHealthbar();
        this.effectActor.setX(this.healthBar.getX() + (this.healthBar.getWidth() / 2.0f));
        this.effectActor.setY(this.healthBar.getY() + (this.healthBar.getHeight() / 2.0f));
        this.effectActor.play();
        String currentIslandId = String.valueOf(Game.getTrainer().getCurrentIsland().getId());
        Game.getAnalytics().trackEvent("monetization", "use_item", "blue_coffee", "blue_coffee_battle_team", "", currentIslandId);
    }

    public void dispose() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMogaSelected() {
        this.listener.onMogaSelected(this.mogaIndex);
    }
}
