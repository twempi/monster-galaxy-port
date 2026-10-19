package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.TintTo;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ShadowLabel;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
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
        portraitBg.x = (-3.0f) * scaleFactor;
        Image signEffectivenessBg = ResolutionManager.getScaledImage("signBg", Assets.littleBlueCircle);
        signEffectivenessBg.x = 1.0f * scaleFactor;
        signEffectivenessBg.y = portraitBg.height - signEffectivenessBg.height;
        this.signEffectiveness = ResolutionManager.getScaledImage("sign", Assets.circles.get(ZodiacPowerType.NORMAL));
        this.signEffectiveness.x = signEffectivenessBg.x + ((signEffectivenessBg.width - this.signEffectiveness.width) * 0.4f);
        this.signEffectiveness.y = signEffectivenessBg.y + ((signEffectivenessBg.height - this.signEffectiveness.height) * 0.75f);
        Image zodiacSign = ResolutionManager.getScaledImage("", Assets.getZodiacSignAsset(moga.getType().getZodiacSign()));
        if (zodiacSign.region != null) {
            zodiacSign.x = this.signEffectiveness.x + ((this.signEffectiveness.width - zodiacSign.width) / 2.0f);
            zodiacSign.y = this.signEffectiveness.y + ((this.signEffectiveness.height - zodiacSign.height) / 2.0f);
        }
        this.levelBg = ResolutionManager.getScaledImage("hud_level_bg_" + hudName, Assets.sidePortraitBg);
        this.levelBg.x = portraitBg.x + (portraitBg.width * 0.85f);
        this.levelBg.y = portraitBg.y + (portraitBg.height / 2.0f);
        float hbScale = scaleFactor * 0.7f;
        int regionSize = (int) ((this.levelBg.width / hbScale) + 6.0f);
        TextureRegion hbBg = new TextureRegion(Assets.healthBarBackground);
        hbBg.setRegionX((Assets.healthBarBackground.getRegionX() + Assets.healthBarBackground.getRegionWidth()) - regionSize);
        this.healthBarBg = new Image("", hbBg);
        this.healthBarBg.width = regionSize * hbScale;
        this.healthBarBg.height = Assets.healthBarBackground.getRegionHeight() * hbScale;
        this.healthBarBg.y = (portraitBg.y + (portraitBg.height / 2.0f)) - (this.healthBarBg.height * 0.8f);
        this.healthBarBg.x = this.levelBg.x;
        int regionSize2 = (int) ((this.levelBg.width / hbScale) - 2.0f);
        this.hb = new TextureRegion(Assets.healthBar);
        this.hb.setRegionX((Assets.healthBar.getRegionX() + Assets.healthBar.getRegionWidth()) - regionSize2);
        this.healthBar = new RegionImage("", this.hb);
        this.healthBar.height = Assets.healthBar.getRegionHeight() * hbScale;
        this.healthBar.y = this.healthBarBg.y + ((this.healthBarBg.height - this.healthBar.height) / 2.0f);
        this.healthBar.x = this.healthBarBg.x;
        updateHealthbar();
        Button blueCoffee = new Button("blue_coffee_on", Assets.blueCoffeePortraitOn);
        blueCoffee.clickListener = new Button.ClickListener() { // from class: com.gaiaonline.monstergalaxy.battle.MogaMiniHud.1
            @Override // com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener
            public void clicked(Button button) {
                MogaMiniHud.this.listener.onBlueCoffee(MogaMiniHud.this.mogaIndex);
            }
        };
        blueCoffee.x = this.healthBarBg.x + this.healthBarBg.width;
        blueCoffee.y = this.healthBarBg.y;
        Button portrait = new Button("", moga.getType().getPortrait());
        portrait.clickListener = new Button.ClickListener() { // from class: com.gaiaonline.monstergalaxy.battle.MogaMiniHud.2
            @Override // com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener
            public void clicked(Button button) {
                MogaMiniHud.this.onMogaSelected();
            }
        };
        portrait.x = 12.0f * scaleFactor;
        portrait.y = 11.0f * scaleFactor;
        ResolutionManager.scaleActor(portrait);
        ResolutionManager.scaleActor(blueCoffee);
        Fonts.regular.setScale(0.5f * scaleFactor);
        ShadowLabel levelLabel = new ShadowLabel("hud_level_" + hudName, Fonts.regular, "Lv. " + moga.getLevel(), ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
        levelLabel.x = (this.levelBg.x + (this.levelBg.width / 2.0f)) - (levelLabel.width / 2.0f);
        levelLabel.y = (this.levelBg.y + (this.levelBg.height / 2.0f)) - (levelLabel.height / 2.0f);
        this.width = portraitBg.width;
        this.height = portraitBg.height;
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
        if (zodiacSign.region != null) {
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
            this.underPortraitGroup.color.a = 1.0f;
            this.overPortraitGroup.color.a = 1.0f;
            this.underPortraitGroup.action(TintTo.$(1.0f, 1.0f, 1.0f, Float.valueOf(0.0f), 0.5f));
            this.overPortraitGroup.action(TintTo.$(1.0f, 1.0f, 1.0f, Float.valueOf(0.0f), 0.5f));
            return;
        }
        this.underPortraitGroup.color.a = 0.0f;
        this.overPortraitGroup.color.a = 0.0f;
    }

    public void showAll(boolean animate) {
        if (animate) {
            this.underPortraitGroup.color.a = 0.0f;
            this.overPortraitGroup.color.a = 0.0f;
            this.underPortraitGroup.action(TintTo.$(1.0f, 1.0f, 1.0f, Float.valueOf(1.0f), 0.5f));
            this.overPortraitGroup.action(TintTo.$(1.0f, 1.0f, 1.0f, Float.valueOf(1.0f), 0.5f));
            return;
        }
        this.underPortraitGroup.color.a = 1.0f;
        this.overPortraitGroup.color.a = 1.0f;
    }

    public void setRival(Moga rival) {
        this.signEffectiveness.region = Assets.circles.get(ZodiacHelper.getInstance().getPowerTypeAgainst(this.moga.getType().getZodiacSign(), rival.getType().getZodiacSign()));
    }

    public void updateHealthbar() {
        float hbScale = ResolutionManager.getScaleFactor() * 0.7f;
        float barCompletion = this.moga.getHitPoints() / this.moga.getMaxHitPoints();
        float regionSize = (int) ((this.levelBg.width / hbScale) - 2.0f);
        this.hb.setRegionX((int) ((Assets.healthBar.getRegionX() + Assets.healthBar.getRegionWidth()) - regionSize));
        this.healthBar.width = regionSize * barCompletion * hbScale;
        this.healthBar.setRegion((int) (this.hb.getRegionWidth() * (1.0f - barCompletion)), 0, (int) (this.hb.getRegionWidth() * barCompletion), this.hb.getRegionHeight());
    }

    public void blueCoffeeApplied() {
        updateHealthbar();
        this.effectActor.x = this.healthBar.x + (this.healthBar.width / 2.0f);
        this.effectActor.y = this.healthBar.y + (this.healthBar.height / 2.0f);
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
