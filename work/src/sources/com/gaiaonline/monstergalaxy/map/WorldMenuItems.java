package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayedRunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;

/* JADX INFO: compiled from: WorldMenu.java */
/* JADX INFO: loaded from: classes.dex */
class WorldMenuItems extends Group {
    private boolean enabled;
    private boolean isOpen;
    private Image menuConnector;
    private ImageButton optionsItem;
    private ImageButton questItem;
    private float scaleFactor = ResolutionManager.getScaleFactor();
    private ImageButton shopItem;
    private ImageButton tamerItem;

    public WorldMenuItems(String name) {
        addMenuConnector();
        addQuestButton();
        addTamerButton();
        addShopButton();
        addOptionsButton();
        setX((Gdx.graphics.getWidth() - this.tamerItem.getWidth()) - (6.0f * this.scaleFactor));
        setY(15.0f * this.scaleFactor);
        SoundManager.loadSound(SoundManager.SOUND_MENU_SLIDE);
    }

    private void addQuestButton() {
        TextureRegionDrawable normalButton = new TextureRegionDrawable(Assets.loadLocalizedTexture("quest.off"));
        TextureRegionDrawable selectedButton = new TextureRegionDrawable(Assets.loadLocalizedTexture("quest.on"));
        ResolutionManager.scale(normalButton);
        ResolutionManager.scale(selectedButton);
        this.questItem = new ImageButton(normalButton, selectedButton);
        this.questItem.addListener(new EventListener() { // from class: com.gaiaonline.monstergalaxy.map.WorldMenuItems.1
            @Override // com.badlogic.gdx.scenes.scene2d.EventListener
            public boolean handle(Event event) {
                WorldMenuItems.this.onInputEvent("quest", event);
                return true;
            }
        });
        addActor(this.questItem);
    }

    private void addTamerButton() {
        TextureRegionDrawable off;
        TextureRegionDrawable on;
        if (Game.getTrainer().getGender() == Trainer.Gender.MALE) {
            off = new TextureRegionDrawable(Assets.loadLocalizedTexture("trainer.male.off"));
            on = new TextureRegionDrawable(Assets.loadLocalizedTexture("trainer.male.on"));
        } else {
            off = new TextureRegionDrawable(Assets.loadLocalizedTexture("trainer.female.off"));
            on = new TextureRegionDrawable(Assets.loadLocalizedTexture("trainer.female.on"));
        }
        ResolutionManager.scale(off);
        ResolutionManager.scale(on);
        this.tamerItem = new ImageButton(off, on);
        this.tamerItem.addListener(new EventListener() { // from class: com.gaiaonline.monstergalaxy.map.WorldMenuItems.2
            @Override // com.badlogic.gdx.scenes.scene2d.EventListener
            public boolean handle(Event event) {
                WorldMenuItems.this.onInputEvent("tamer", event);
                return false;
            }
        });
        addActor(this.tamerItem);
    }

    private void addShopButton() {
        TextureRegionDrawable normalButton = new TextureRegionDrawable(Assets.loadLocalizedTexture("shop.off"));
        TextureRegionDrawable selectedButton = new TextureRegionDrawable(Assets.loadLocalizedTexture("shop.on"));
        ResolutionManager.scale(normalButton);
        ResolutionManager.scale(selectedButton);
        this.shopItem = new ImageButton(normalButton, selectedButton);
        this.shopItem.addListener(new EventListener() { // from class: com.gaiaonline.monstergalaxy.map.WorldMenuItems.3
            @Override // com.badlogic.gdx.scenes.scene2d.EventListener
            public boolean handle(Event event) {
                WorldMenuItems.this.onInputEvent("shop", event);
                return false;
            }
        });
        addActor(this.shopItem);
    }

    private void addOptionsButton() {
        TextureRegionDrawable normalButton = new TextureRegionDrawable(Assets.loadLocalizedTexture("options.off"));
        TextureRegionDrawable selectedButton = new TextureRegionDrawable(Assets.loadLocalizedTexture("options.on"));
        ResolutionManager.scale(normalButton);
        ResolutionManager.scale(selectedButton);
        this.optionsItem = new ImageButton(normalButton, selectedButton);
        this.optionsItem.addListener(new EventListener() { // from class: com.gaiaonline.monstergalaxy.map.WorldMenuItems.4
            @Override // com.badlogic.gdx.scenes.scene2d.EventListener
            public boolean handle(Event event) {
                WorldMenuItems.this.onInputEvent("options", event);
                return false;
            }
        });
        addActor(this.optionsItem);
    }

    private void addMenuConnector() {
        TextureRegion connector = Assets.loadTexture("menu.connector");
        this.menuConnector = new Image(connector);
        this.menuConnector.setX(85.0f * this.scaleFactor);
        this.menuConnector.setY(40.0f * this.scaleFactor);
        ResolutionManager.scaleActor(this.menuConnector);
        addActor(this.menuConnector);
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void open() {
        this.isOpen = true;
        this.questItem.addAction(getOpenAction(0));
        this.tamerItem.addAction(getOpenAction(1));
        this.shopItem.addAction(getOpenAction(2));
        this.optionsItem.addAction(getOpenAction(3));
        this.menuConnector.setVisible(true);
        this.menuConnector.addAction(getMenuConnectorOpenAction());
    }

    public void close() {
        this.enabled = false;
        this.isOpen = false;
        this.menuConnector.setVisible(false);
        this.questItem.addAction(getCloseAction());
        this.tamerItem.addAction(getCloseAction());
        this.shopItem.addAction(getCloseAction());
        this.optionsItem.addAction(getCloseAction());
    }

    public Action getOpenAction(int index) {
        clearActions();
        float openedPositionY = (index + 1) * 48 * this.scaleFactor;
        MoveToAction itemMove = new MoveToAction();
        itemMove.setX(0.0f);
        itemMove.setY(openedPositionY);
        itemMove.setDuration(1.0f);
        AlphaAction itemFadeOut = new AlphaAction();
        itemFadeOut.setAlpha(0.0f);
        itemFadeOut.setDuration(0.0f);
        AlphaAction itemFadeIn = new AlphaAction();
        itemFadeIn.setAlpha(1.0f);
        itemFadeIn.setDuration(1.0f);
        SequenceAction sequence = new SequenceAction();
        ParallelAction parallel = new ParallelAction();
        parallel.addAction(itemFadeIn);
        parallel.addAction(itemMove);
        sequence.addAction(itemFadeOut);
        sequence.addAction(parallel);
        return sequence;
    }

    public Action getCloseAction() {
        clearActions();
        MoveToAction itemMove = new MoveToAction();
        itemMove.setX(0.0f);
        itemMove.setY(0.0f);
        itemMove.setDuration(1.0f);
        AlphaAction itemFadeOut = new AlphaAction();
        itemFadeOut.setAlpha(0.0f);
        itemFadeOut.setDuration(1.0f);
        ParallelAction parallel = new ParallelAction();
        parallel.addAction(itemMove);
        parallel.addAction(itemFadeOut);
        return parallel;
    }

    public Action getMenuConnectorOpenAction() {
        clearActions();
        AlphaAction fadeOut = new AlphaAction();
        fadeOut.setAlpha(0.0f);
        fadeOut.setDuration(0.0f);
        AlphaAction fadeIn = new AlphaAction();
        fadeIn.setAlpha(1.0f);
        fadeIn.setDuration(0.2f);
        DelayAction delay = new DelayAction();
        delay.setAction(fadeIn);
        delay.setDuration(0.8f);
        RunnableAction runnable = new DelayedRunnableAction();
        runnable.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.WorldMenuItems.5
            @Override // java.lang.Runnable
            public void run() {
                WorldMenuItems.this.enabled = true;
            }
        });
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(fadeOut);
        sequence.addAction(delay);
        sequence.addAction(runnable);
        return sequence;
    }

    public void onInputEvent(String buttonName, Event event) {
        if (this.enabled && (event instanceof InputEvent) && ((InputEvent) event).getType() == InputEvent.Type.touchDown) {
            close();
            if (buttonName.equals("tamer")) {
                MonsterGalaxy.showScreen(ScreenCode.TAMER, false, true);
                SoundManager.playScreenChangeSound();
                return;
            }
            if (buttonName.equals("quest")) {
                MonsterGalaxy.showScreen(ScreenCode.QUEST, false, true);
                SoundManager.playScreenChangeSound();
            } else if (buttonName.equals("shop")) {
                MonsterGalaxy.showScreen(ScreenCode.SHOP, false, true);
                SoundManager.playScreenChangeSound();
            } else if (buttonName.equals("options")) {
                MonsterGalaxy.showScreen(ScreenCode.SETTINGS, false, true);
                SoundManager.playScreenChangeSound();
            }
        }
    }
}
