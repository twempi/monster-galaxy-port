package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;

/* JADX INFO: loaded from: classes.dex */
public class BackpackMenu extends Group {
    boolean isOpen;
    private BackpackListener listener;
    private float scaleFactor = ResolutionManager.getScaleFactor();
    private BlueCoffeeMenuItem blueCoffeeMenuItem = new BlueCoffeeMenuItem();
    private StarSeedMenuItem starSeedMenuItem = new StarSeedMenuItem();

    interface BackpackListener {
        public static final int MENU_BLUE_COFFEE = 1;
        public static final int MENU_STAR_SEED = 2;

        void onMenuItemSelected(int i);
    }

    public BackpackMenu(BackpackListener listener) {
        this.listener = listener;
        setX(Gdx.graphics.getWidth() - this.blueCoffeeMenuItem.getWidth());
        setY(15.0f * this.scaleFactor);
        SoundManager.loadSound(SoundManager.SOUND_MENU_SLIDE);
        addActor(this.blueCoffeeMenuItem);
        addActor(this.starSeedMenuItem);
    }

    public void open() {
        this.isOpen = true;
        this.blueCoffeeMenuItem.setY(getY());
        this.starSeedMenuItem.setY(getY());
        this.blueCoffeeMenuItem.clearActions();
        this.starSeedMenuItem.clearActions();
        float backpackButtonHeight = Assets.loadTexture("items.off").getRegionHeight() * this.scaleFactor;
        float blueCoffeeY = backpackButtonHeight + this.starSeedMenuItem.getHeight();
        MoveToAction blueCoffeeMove = new MoveToAction();
        blueCoffeeMove.setPosition(0.0f, blueCoffeeY);
        blueCoffeeMove.setDuration(0.5f);
        AlphaAction blueCoffeeFade = new AlphaAction();
        blueCoffeeFade.setAlpha(1.0f);
        blueCoffeeFade.setDuration(0.0f);
        MoveToAction starSeedMove = new MoveToAction();
        starSeedMove.setPosition(0.0f, backpackButtonHeight);
        starSeedMove.setDuration(0.5f);
        AlphaAction starSeedFade = new AlphaAction();
        starSeedFade.setAlpha(1.0f);
        starSeedFade.setDuration(0.0f);
        ParallelAction parallelBlueCoffee = new ParallelAction();
        parallelBlueCoffee.addAction(blueCoffeeMove);
        parallelBlueCoffee.addAction(blueCoffeeFade);
        ParallelAction parallelStarseed = new ParallelAction();
        parallelStarseed.addAction(starSeedMove);
        parallelStarseed.addAction(starSeedFade);
        this.blueCoffeeMenuItem.addAction(parallelBlueCoffee);
        this.starSeedMenuItem.addAction(parallelStarseed);
    }

    public void close() {
        this.isOpen = false;
        this.blueCoffeeMenuItem.clearActions();
        this.starSeedMenuItem.clearActions();
        MoveToAction blueCoffeeMove = new MoveToAction();
        blueCoffeeMove.setPosition(0.0f, 0.0f);
        blueCoffeeMove.setDuration(0.5f);
        AlphaAction blueCoffeeFade = new AlphaAction();
        blueCoffeeFade.setAlpha(0.0f);
        blueCoffeeFade.setDuration(0.0f);
        MoveToAction starSeedMove = new MoveToAction();
        starSeedMove.setPosition(0.0f, 0.0f);
        starSeedMove.setDuration(0.5f);
        AlphaAction starSeedFade = new AlphaAction();
        starSeedFade.setAlpha(0.0f);
        starSeedFade.setDuration(0.0f);
        ParallelAction parallelBlueCoffee = new ParallelAction();
        parallelBlueCoffee.addAction(blueCoffeeMove);
        parallelBlueCoffee.addAction(blueCoffeeFade);
        ParallelAction parallelStarseed = new ParallelAction();
        parallelStarseed.addAction(starSeedMove);
        parallelStarseed.addAction(starSeedFade);
        this.blueCoffeeMenuItem.addAction(parallelBlueCoffee);
        this.starSeedMenuItem.addAction(parallelStarseed);
    }

    boolean isTouched(Vector2 touchPoint) {
        boolean justTouched = false;
        for (Actor actor : getChildren()) {
            if (actor instanceof Group) {
                Group group = (Group) actor;
                justTouched = isTouched(touchPoint, group);
                if (justTouched) {
                    break;
                }
            }
        }
        return justTouched;
    }

    private boolean isTouched(Vector2 touchPoint, Group group) {
        if (touchPoint.x <= getX() || touchPoint.x >= getX() + group.getWidth() || touchPoint.y <= group.getY() || touchPoint.y >= group.getY() + group.getHeight()) {
            return false;
        }
        return true;
    }

    private abstract class AbstractMenuItem extends Group {
        private Button button;
        private int cachedValue;
        private AutoFontLabel label;
        private int maxDisplayValue;
        private Image tabImage;

        protected abstract int getValue();

        private AbstractMenuItem() {
            this.cachedValue = -1;
        }

        /* synthetic */ AbstractMenuItem(BackpackMenu backpackMenu, AbstractMenuItem abstractMenuItem) {
            this();
        }

        protected void init(Button button, int menuId, int maxDisplayValue) {
            this.maxDisplayValue = maxDisplayValue;
            this.button = button;
            this.tabImage = new Image(Assets.loadTexture("count.tab"));
            ResolutionManager.scaleActor(this.tabImage);
            ResolutionManager.scaleActor(button);
            button.setX(this.tabImage.getWidth() - (this.tabImage.getWidth() / 4.0f));
            button.setY(this.tabImage.getY() - (this.tabImage.getHeight() / 2.0f));
            this.label = new AutoFontLabel("", 0.4f * BackpackMenu.this.scaleFactor, ColorConstants.PRIMARY_FONT_COLOR, false, ColorConstants.PRIMARY_SHADOW_COLOR);
            updateValue();
            float rightPadding = 5.0f * BackpackMenu.this.scaleFactor;
            setHeight(button.getHeight());
            setWidth(button.getX() + button.getWidth() + rightPadding);
            addActor(this.tabImage);
            addActor(button);
            addActor(this.label);
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
        public void draw(SpriteBatch batch, float parentAlpha) {
            if (isVisible()) {
                updateValue();
                super.draw(batch, parentAlpha);
            }
        }

        protected void updateValue() {
            int value = getValue();
            if (this.cachedValue != value) {
                this.cachedValue = value;
                if (value <= this.maxDisplayValue) {
                    this.label.setScale(1.0f);
                    this.label.setText(String.valueOf(value));
                    this.label.setX((this.tabImage.getWidth() / 2.0f) - (this.label.getWidth() / 2.0f));
                } else {
                    this.label.setText("+" + this.maxDisplayValue);
                    this.label.setScale(BackpackMenu.this.scaleFactor * 0.38f);
                    this.label.setX((this.tabImage.getWidth() / 2.0f) - (this.label.getWidth() - (this.label.getWidth() * 0.38f)));
                }
                this.label.setY((this.tabImage.getHeight() / 2.0f) - (this.label.getHeight() / 4.0f));
                float rightPadding = 5.0f * BackpackMenu.this.scaleFactor;
                setHeight(this.button.getHeight());
                setWidth(this.button.getX() + this.button.getWidth() + rightPadding);
            }
        }
    }

    private class StarSeedMenuItem extends AbstractMenuItem {
        private ImageButton button;

        public StarSeedMenuItem() {
            super(BackpackMenu.this, null);
            this.button = new ImageButton(new TextureRegionDrawable(Assets.loadTexture("starseed.button.on")));
            init(this.button, 2, 999);
            setGreyed(true);
        }

        private void setGreyed(boolean greyed) {
            this.button.setColor(greyed ? ColorConstants.PRIMARY_FONT_COLOR : ColorConstants.PRIMARY_GRAYED_COLOR);
        }

        @Override // com.gaiaonline.monstergalaxy.battle.BackpackMenu.AbstractMenuItem, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
        public void draw(SpriteBatch batch, float parentAlpha) {
            if (isVisible()) {
                Trainer trainer = Game.getTrainer();
                setGreyed(trainer.canCaptureMoga(trainer.getCurrentNode().getTeam().getDeployedMoga()));
                super.draw(batch, parentAlpha);
            }
        }

        @Override // com.gaiaonline.monstergalaxy.battle.BackpackMenu.AbstractMenuItem
        protected int getValue() {
            return Game.getTrainer().getStarSeeds();
        }
    }

    private class BlueCoffeeMenuItem extends AbstractMenuItem {
        public BlueCoffeeMenuItem() {
            super(BackpackMenu.this, null);
            ImageButton button = new ImageButton(new TextureRegionDrawable(Assets.loadTexture("blue.coffee.portrait.on")));
            init(button, 1, 999);
        }

        @Override // com.gaiaonline.monstergalaxy.battle.BackpackMenu.AbstractMenuItem
        protected int getValue() {
            return Game.getTrainer().getBlueCoffeeCups();
        }
    }
}
