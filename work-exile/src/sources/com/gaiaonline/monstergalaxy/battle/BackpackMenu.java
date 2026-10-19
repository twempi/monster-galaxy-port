package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ShadowLabel;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BackpackMenu extends Group {
    private BlueCoffeeMenuItem blueCoffeeMenuItem;
    boolean isOpen;
    private BackpackListener listener;
    private float scaleFactor;
    private StarSeedMenuItem starSeedMenuItem;

    interface BackpackListener {
        public static final int MENU_BLUE_COFFEE = 1;
        public static final int MENU_STAR_SEED = 2;

        void onMenuItemSelected(int i);
    }

    public BackpackMenu(BackpackListener listener) {
        super("backpackMenu");
        this.listener = listener;
        this.scaleFactor = ResolutionManager.getScaleFactor();
        this.blueCoffeeMenuItem = new BlueCoffeeMenuItem();
        this.starSeedMenuItem = new StarSeedMenuItem();
        this.x = Gdx.graphics.getWidth() - this.blueCoffeeMenuItem.width;
        this.y = 15.0f * this.scaleFactor;
        SoundManager.loadSound(SoundManager.SOUND_MENU_SLIDE);
        addActor(this.blueCoffeeMenuItem);
        addActor(this.starSeedMenuItem);
    }

    public void open() {
        this.isOpen = true;
        this.blueCoffeeMenuItem.y = this.y;
        this.starSeedMenuItem.y = this.y;
        this.blueCoffeeMenuItem.clearActions();
        this.starSeedMenuItem.clearActions();
        this.blueCoffeeMenuItem.touchable = true;
        this.starSeedMenuItem.touchable = true;
        float backpackButtonHeight = Assets.loadTexture("items.off").getRegionHeight() * this.scaleFactor;
        float blueCoffeeY = backpackButtonHeight + this.starSeedMenuItem.height;
        MoveTo blueCoffeeMove = MoveTo.$(0.0f, blueCoffeeY, 0.5f);
        FadeIn blueCoffeeFade = FadeIn.$(0.0f);
        MoveTo starSeedMove = MoveTo.$(0.0f, backpackButtonHeight, 0.5f);
        FadeIn starSeedFade = FadeIn.$(0.0f);
        this.blueCoffeeMenuItem.action(Parallel.$(blueCoffeeMove, blueCoffeeFade));
        this.starSeedMenuItem.action(Parallel.$(starSeedMove, starSeedFade));
    }

    public void close() {
        this.isOpen = false;
        this.blueCoffeeMenuItem.clearActions();
        this.starSeedMenuItem.clearActions();
        this.blueCoffeeMenuItem.touchable = false;
        this.starSeedMenuItem.touchable = false;
        MoveTo blueCoffeeMove = MoveTo.$(0.0f, 0.0f, 0.5f);
        FadeOut blueCoffeeFade = FadeOut.$(0.5f);
        MoveTo starSeedMove = MoveTo.$(0.0f, 0.0f, 0.5f);
        FadeOut starSeedFade = FadeOut.$(0.5f);
        Action starSeedClose = Parallel.$(starSeedMove, starSeedFade);
        this.blueCoffeeMenuItem.action(Parallel.$(blueCoffeeMove, blueCoffeeFade));
        this.starSeedMenuItem.action(starSeedClose);
    }

    boolean isTouched(Vector2 touchPoint) {
        boolean justTouched = false;
        for (Group group : getGroups()) {
            justTouched = isTouched(touchPoint, group);
            if (justTouched) {
                break;
            }
        }
        return justTouched;
    }

    private boolean isTouched(Vector2 touchPoint, Group group) {
        if (touchPoint.x <= this.x || touchPoint.x >= this.x + group.width || touchPoint.y <= group.y || touchPoint.y >= group.y + group.height) {
            return false;
        }
        return true;
    }

    private abstract class AbstractMenuItem extends Group {
        private Button button;
        private int cachedValue;
        private ShadowLabel label;
        private int maxDisplayValue;
        private Image tabImage;

        protected abstract int getValue();

        private AbstractMenuItem() {
            this.cachedValue = -1;
        }

        /* synthetic */ AbstractMenuItem(BackpackMenu backpackMenu, AbstractMenuItem abstractMenuItem) {
            this();
        }

        protected void init(Button button, final int menuId, int maxDisplayValue) {
            this.maxDisplayValue = maxDisplayValue;
            this.button = button;
            this.tabImage = new Image("countTab", Assets.loadTexture("count.tab"));
            button.clickListener = new Button.ClickListener() { // from class: com.gaiaonline.monstergalaxy.battle.BackpackMenu.AbstractMenuItem.1
                @Override // com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener
                public void clicked(Button button2) {
                    BackpackMenu.this.listener.onMenuItemSelected(menuId);
                }
            };
            ResolutionManager.scaleActor(this.tabImage);
            ResolutionManager.scaleActor(button);
            button.x = this.tabImage.width - (this.tabImage.width / 4.0f);
            button.y = this.tabImage.y - (this.tabImage.height / 2.0f);
            Fonts.regular.setScale(0.4f * BackpackMenu.this.scaleFactor);
            this.label = new ShadowLabel("", Fonts.regular, "0", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
            updateValue();
            float rightPadding = 5.0f * BackpackMenu.this.scaleFactor;
            this.height = button.height;
            this.width = button.x + button.width + rightPadding;
            addActor(this.tabImage);
            addActor(button);
            addActor(this.label);
        }

        @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
        public void draw(SpriteBatch batch, float parentAlpha) {
            if (this.visible) {
                updateValue();
                super.draw(batch, parentAlpha);
            }
        }

        protected void updateValue() {
            int value = getValue();
            if (this.cachedValue != value) {
                Fonts.regular.setScale(0.4f * BackpackMenu.this.scaleFactor);
                this.label.setFont(Fonts.regular);
                this.cachedValue = value;
                if (value <= this.maxDisplayValue) {
                    this.label.setText(String.valueOf(value));
                    this.label.x = (this.tabImage.width / 2.0f) - (this.label.width / 2.0f);
                } else {
                    this.label.setText("+" + this.maxDisplayValue);
                    this.label.scaleX = BackpackMenu.this.scaleFactor * 0.38f;
                    this.label.scaleY = BackpackMenu.this.scaleFactor * 0.38f;
                    this.label.x = (this.tabImage.width / 2.0f) - (this.label.width - (this.label.width * 0.38f));
                }
                this.label.y = (this.tabImage.height / 2.0f) - (this.label.height / 4.0f);
                float rightPadding = 5.0f * BackpackMenu.this.scaleFactor;
                this.height = this.button.height;
                this.width = this.button.x + this.button.width + rightPadding;
            }
        }
    }

    private class StarSeedMenuItem extends AbstractMenuItem {
        private Button button;

        public StarSeedMenuItem() {
            super(BackpackMenu.this, null);
            this.button = new Button("starSeed", Assets.loadTexture("starseed.button.on"));
            init(this.button, 2, 999);
            setGreyed(true);
        }

        private void setGreyed(boolean greyed) {
            this.button.color.set(greyed ? ColorConstants.PRIMARY_FONT_COLOR : ColorConstants.PRIMARY_GRAYED_COLOR);
        }

        @Override // com.gaiaonline.monstergalaxy.battle.BackpackMenu.AbstractMenuItem, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
        public void draw(SpriteBatch batch, float parentAlpha) {
            if (this.visible) {
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
            Button button = new Button("blueCoffee", Assets.loadTexture("blue.coffee.portrait.on"));
            init(button, 1, 999);
        }

        @Override // com.gaiaonline.monstergalaxy.battle.BackpackMenu.AbstractMenuItem
        protected int getValue() {
            return Game.getTrainer().getBlueCoffeeCups();
        }
    }
}
