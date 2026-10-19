package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;

/* JADX INFO: compiled from: WorldMenu.java */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class WorldMenuItems extends Group implements Button.ClickListener {
    private boolean isOpen;
    private Image menuConnector;
    private Button optionsItem;
    private Button questItem;
    private float scaleFactor;
    private Button shopItem;
    private Button tamerItem;

    public WorldMenuItems(String name) {
        super(name);
        this.scaleFactor = ResolutionManager.getScaleFactor();
        addMenuConnector();
        addQuestButton();
        addTamerButton();
        addShopButton();
        addOptionsButton();
        this.x = (Gdx.graphics.getWidth() - this.tamerItem.width) - (6.0f * this.scaleFactor);
        this.y = 15.0f * this.scaleFactor;
        SoundManager.loadSound(SoundManager.SOUND_MENU_SLIDE);
    }

    private void addQuestButton() {
        TextureRegion normalButton = Assets.loadTexture("quest.off");
        TextureRegion selectedButton = Assets.loadTexture("quest.on");
        this.questItem = new Button("quest", normalButton, selectedButton);
        this.questItem.clickListener = this;
        ResolutionManager.scaleActor(this.questItem);
        addActor(this.questItem);
    }

    private void addTamerButton() {
        if (Game.getTrainer().getGender() == Trainer.Gender.MALE) {
            this.tamerItem = new Button("tamer", Assets.loadTexture("trainer.male.off"), Assets.loadTexture("trainer.male.on"));
        } else {
            this.tamerItem = new Button("tamer", Assets.loadTexture("trainer.female.off"), Assets.loadTexture("trainer.female.on"));
        }
        this.tamerItem.clickListener = this;
        ResolutionManager.scaleActor(this.tamerItem);
        addActor(this.tamerItem);
    }

    private void addShopButton() {
        TextureRegion normalButton = Assets.loadTexture("shop.off");
        TextureRegion selectedButton = Assets.loadTexture("shop.on");
        this.shopItem = new Button("shop", normalButton, selectedButton);
        this.shopItem.clickListener = this;
        ResolutionManager.scaleActor(this.shopItem);
        addActor(this.shopItem);
    }

    private void addOptionsButton() {
        TextureRegion normalButton = Assets.loadTexture("options.off");
        TextureRegion selectedButton = Assets.loadTexture("options.on");
        this.optionsItem = new Button("options", normalButton, selectedButton);
        this.optionsItem.clickListener = this;
        ResolutionManager.scaleActor(this.optionsItem);
        addActor(this.optionsItem);
    }

    private void addMenuConnector() {
        TextureRegion connector = Assets.loadTexture("menu.connector");
        this.menuConnector = new Image("connector", connector);
        this.menuConnector.x = 85.0f * this.scaleFactor;
        this.menuConnector.y = 40.0f * this.scaleFactor;
        ResolutionManager.scaleActor(this.menuConnector);
        addActor(this.menuConnector);
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void open() {
        this.isOpen = true;
        this.questItem.action(getOpenAction(0));
        this.tamerItem.action(getOpenAction(1));
        this.shopItem.action(getOpenAction(2));
        this.optionsItem.action(getOpenAction(3));
        this.menuConnector.visible = true;
        this.menuConnector.action(getMenuConnectorOpenAction());
    }

    public void close() {
        this.isOpen = false;
        this.menuConnector.visible = false;
        this.questItem.action(getCloseAction());
        this.tamerItem.action(getCloseAction());
        this.shopItem.action(getCloseAction());
        this.optionsItem.action(getCloseAction());
    }

    public Action getOpenAction(int index) {
        clearActions();
        this.touchable = true;
        float openedPositionY = (index + 1) * 48 * this.scaleFactor;
        MoveTo itemMove = MoveTo.$(0.0f, openedPositionY, 1.0f);
        FadeOut itemFadeOut = FadeOut.$(0.0f);
        FadeIn itemFadeIn = FadeIn.$(1.0f);
        return Sequence.$(itemFadeOut, Parallel.$(itemFadeIn, itemMove));
    }

    public Action getCloseAction() {
        clearActions();
        this.touchable = false;
        MoveTo itemMove = MoveTo.$(0.0f, 0.0f, 1.0f);
        FadeOut itemFadeOut = FadeOut.$(1.0f);
        return Parallel.$(itemMove, itemFadeOut);
    }

    public Action getMenuConnectorOpenAction() {
        clearActions();
        this.touchable = true;
        FadeTo fadeOut = FadeTo.$(0.0f, 0.0f);
        FadeTo fadeIn = FadeTo.$(1.0f, 0.2f);
        Delay delay = Delay.$(fadeIn, 0.8f);
        return Sequence.$(fadeOut, delay);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener
    public void clicked(Button button) {
        close();
        if (button.name.equals("tamer")) {
            MonsterGalaxy.showScreen(ScreenCode.TAMER, false, true);
            SoundManager.playScreenChangeSound();
            return;
        }
        if (button.name.equals("quest")) {
            MonsterGalaxy.showScreen(ScreenCode.QUEST, false, true);
            SoundManager.playScreenChangeSound();
        } else if (button.name.equals("shop")) {
            MonsterGalaxy.showScreen(ScreenCode.SHOP, false, true);
            SoundManager.playScreenChangeSound();
        } else if (button.name.equals("options")) {
            MonsterGalaxy.showScreen(ScreenCode.SETTINGS, false, true);
            SoundManager.playScreenChangeSound();
        }
    }
}
