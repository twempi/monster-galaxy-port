package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.battle.BattleScreen;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class MogaActor extends Image {
    private final String TAG;
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
        super(mogaImage);
        this.TAG = "MogaActor";
        this.screenRef = new WeakReference<>(screen);
        this.moga = moga;
        this.mogaImage = mogaImage;
        this.faceLeft = faceLeft;
        this.glow = new Image(moga.getType().getGlowImage());
        this.glow.setVisible(false);
        if (faceLeft) {
            ((TextureRegionDrawable) this.glow.getDrawable()).getRegion().flip(true, false);
        }
        float scaleFactor = ResolutionManager.getScaleFactor();
        this.rarity = Assets.loadLocalizedTexture(moga.getType().getRarityAssetName());
        this.rarityWidth = this.rarity.getRegionWidth() * scaleFactor;
        this.rarityHeight = this.rarity.getRegionHeight() * scaleFactor;
        setOrigin(getWidth() / 2.0f, getHeight() / 2.0f);
    }

    public Moga getMoga() {
        return this.moga;
    }

    public void doEffect(EffectType effect, ScreenElement.RelPoint where) {
        if (this.screenRef.get() != null) {
            float ex = getX() + (getWidth() / 2.0f);
            float ey = where == ScreenElement.RelPoint.CENTER_BOTTOM ? getY() : getY() + (getHeight() / 2.0f);
            this.screenRef.get().doEffect(effect, ex, ey);
        }
    }

    public void beIdle() {
        getColor().set(1.0f, 1.0f, 1.0f, 1.0f);
        addAction(AnimationManager.getInstance().getAnimation("idle-circuit", this.faceLeft));
    }

    public void addToStage(Group group) {
        group.addActor(this.glow);
        group.addActor(this);
    }

    public boolean moveBehind(MogaActor otherMogaActor) {
        if (getParent() == null || getParent().getChildren().indexOf(this, false) <= getParent().getChildren().indexOf(otherMogaActor, false)) {
            return false;
        }
        getParent().swapActor(this, otherMogaActor);
        getParent().swapActor(this.glow, otherMogaActor.glow);
        return true;
    }

    public void reloadTextureRegion(TextureRegion newTextureRegion) {
        ((TextureRegionDrawable) getDrawable()).getRegion().setRegion(newTextureRegion);
        this.mogaImage.setRegion(newTextureRegion);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Image, com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.action3D != null) {
            this.action3D.render(batch);
            return;
        }
        super.draw(batch, parentAlpha);
        this.glow.setX(getX());
        this.glow.setY(getY());
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
            this.rarityPositionX = (getX() + (getWidth() / 2.0f)) - (this.rarityWidth / 2.0f);
            this.rarityPositionY = getY() + (getHeight() / 4.0f) + (100.0f * ResolutionManager.getScaleFactor());
            this.moga.getType().setRarityColor(this.glow.getColor());
            startGlowing();
            return;
        }
        stopGlowing();
    }

    public void glowYellow() {
        this.glow.getColor().set(255.0f, 255.0f, 0.0f, 255.0f);
        startGlowing();
    }

    public void glowWithZodiacColor() {
        this.moga.getType().setZodiacColor(this.glow.getColor());
        startGlowing();
    }

    public void stopGlowing() {
        this.glow.clearActions();
        this.glow.setVisible(false);
    }

    private void startGlowing() {
        this.glow.setScale(getWidth(), getHeight());
        this.glow.setPosition(getX(), getY());
        this.glow.setVisible(true);
        AlphaAction fadeIn = new AlphaAction();
        fadeIn.setAlpha(1.0f);
        fadeIn.setDuration(0.5f);
        AlphaAction fadeOut = new AlphaAction();
        fadeOut.setAlpha(0.0f);
        fadeOut.setDuration(0.5f);
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(fadeIn);
        sequence.addAction(fadeOut);
        RepeatAction forever = new RepeatAction();
        forever.setCount(-1);
        forever.setAction(sequence);
        this.glow.addAction(forever);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public String toString() {
        return String.valueOf(super.toString()) + ", {" + this.moga + "}";
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void act(float delta) {
        super.act(delta);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void clearActions() {
        Gdx.app.debug("MogaActor", "clearActions() on " + this);
        super.clearActions();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void removeAction(Action action) {
        Gdx.app.debug("MogaActor", "removeAction(" + action + ") on " + this);
        super.removeAction(action);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void addAction(Action action) {
        Gdx.app.debug("MogaActor", "addAction(" + action + ") on " + this);
        super.addAction(action);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Array<Action> getActions() {
        Gdx.app.debug("MogaActor", "getActions() on " + this);
        return super.getActions();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    protected void setParent(Group parent) {
        Gdx.app.debug("MogaActor", "setParent(" + parent + ") on " + this);
        super.setParent(parent);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    protected void setStage(Stage stage) {
        Gdx.app.debug("MogaActor", "setStage(" + stage + ") on " + this);
        super.setStage(stage);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
}
