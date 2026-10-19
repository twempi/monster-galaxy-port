package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.battle.BattleScreen;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MogaActor extends Image {
    private Action3D action3D;
    private boolean faceLeft;
    private Image glow;
    private Moga moga;
    protected TextureRegion mogaImage;
    private TextureRegion rarity;
    private float rarityHeight;
    private float rarityPositionX;
    private float rarityPositionY;
    private float rarityWidth;
    private WeakReference<BattleScreen> screenRef;
    private boolean showRarity;

    public MogaActor(BattleScreen screen, Moga moga, TextureRegion mogaImage, boolean faceLeft) {
        super(moga.getName(), mogaImage);
        this.screenRef = new WeakReference<>(screen);
        this.moga = moga;
        this.mogaImage = mogaImage;
        this.faceLeft = faceLeft;
        this.glow = new Image(String.valueOf(moga.getName()) + "_glow", moga.getType().getGlowImage());
        this.glow.visible = false;
        if (faceLeft) {
            this.glow.region.flip(true, false);
        }
        float scaleFactor = ResolutionManager.getScaleFactor();
        this.rarity = Assets.loadTexture(moga.getType().getRarityAssetName());
        this.rarityWidth = this.rarity.getRegionWidth() * scaleFactor;
        this.rarityHeight = this.rarity.getRegionHeight() * scaleFactor;
    }

    public Moga getMoga() {
        return this.moga;
    }

    public void doEffect(EffectType effect, ScreenElement.RelPoint where) {
        if (this.screenRef.get() != null) {
            float ex = this.x + (this.width / 2.0f);
            float ey = where == ScreenElement.RelPoint.CENTER_BOTTOM ? this.y : this.y + (this.height / 2.0f);
            this.screenRef.get().doEffect(effect, ex, ey);
        }
    }

    public void beIdle() {
        action(AnimationManager.getInstance().getAnimation("idle-circuit", this.faceLeft));
    }

    public void addToStage(Group group) {
        group.addActor(this.glow);
        group.addActor(this);
    }

    public boolean moveBehind(MogaActor otherMogaActor) {
        if (this.parent == null || this.parent.getActors().indexOf(this) <= this.parent.getActors().indexOf(otherMogaActor)) {
            return false;
        }
        this.parent.swapActor(this, otherMogaActor);
        this.parent.swapActor(this.glow, otherMogaActor.glow);
        return true;
    }

    public void reloadTextureRegion(TextureRegion newTextureRegion) {
        this.region.setRegion(newTextureRegion);
        this.mogaImage.setRegion(newTextureRegion);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actors.Image, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.action3D != null) {
            this.action3D.render(batch);
            return;
        }
        super.draw(batch, parentAlpha);
        this.glow.x = this.x;
        this.glow.y = this.y;
        if (this.showRarity) {
            batch.draw(this.rarity, this.rarityPositionX, this.rarityPositionY, this.rarityWidth, this.rarityHeight);
        }
    }

    public void setAction3D(Action3D action3D) {
        this.action3D = action3D;
    }

    public void showRarity(boolean showRarity) {
        this.showRarity = showRarity;
        if (showRarity) {
            this.rarityPositionX = (this.x + (this.width / 2.0f)) - (this.rarityWidth / 2.0f);
            this.rarityPositionY = this.y + (this.height / 4.0f) + (100.0f * ResolutionManager.getScaleFactor());
            this.moga.getType().setRarityColor(this.glow.color);
            startGlowing();
            return;
        }
        stopGlowing();
    }

    public void glowYellow() {
        this.glow.color.r = 255.0f;
        this.glow.color.g = 255.0f;
        this.glow.color.b = 0.0f;
        startGlowing();
    }

    public void glowWithZodiacColor() {
        this.moga.getType().setZodiacColor(this.glow.color);
        startGlowing();
    }

    public void stopGlowing() {
        this.glow.clearActions();
        this.glow.visible = false;
    }

    private void startGlowing() {
        this.glow.width = this.width;
        this.glow.height = this.height;
        this.glow.x = this.x;
        this.glow.y = this.y;
        this.glow.visible = true;
        this.glow.action(Forever.$(Sequence.$(FadeIn.$(0.5f), FadeOut.$(0.5f))));
    }
}
