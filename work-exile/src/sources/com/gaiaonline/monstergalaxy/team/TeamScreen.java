package com.gaiaonline.monstergalaxy.team;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Team;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.Gallery;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TeamScreen extends Screen {
    protected static final int DISTANCE_BETWEEN_PORTRAITS = 178;
    protected static final int GALLERY_MARGIN = 25;
    private boolean inhibitClick = false;
    private Gallery<Portrait> mogaGallery;
    private Trainer trainer;

    public void setInhibitClick(boolean inhibitClick) {
        this.inhibitClick = inhibitClick;
    }

    public boolean isInhibitClick() {
        return this.inhibitClick;
    }

    public TeamScreen() {
        ScreenListener doneListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.team.TeamScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (!TeamScreen.this.hasUndeployedTiredMogas()) {
                    MonsterGalaxy.showPreviousScreen();
                } else {
                    TeamScreen.this.showNapDialog();
                }
            }
        };
        this.root.add(new TeamHeader(doneListener));
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.trainer = Game.getTrainer();
        this.mogaGallery = new Gallery<>();
        this.mogaGallery.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        this.mogaGallery.setSize(480.0f, 238.0f);
        this.mogaGallery.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.mogaGallery.setAdapter(new TeamGalleryAdapter(this));
        this.mogaGallery.setDistance(178);
        this.mogaGallery.setMargin(25.0f);
        this.root.addBackground(Assets.loadTexture("team.bg"));
        this.root.add(this.mogaGallery);
        TextureElement bluecoffe = new TextureElement(Assets.loadTexture("blue.coffee.icon"));
        bluecoffe.setPosition(ScreenElement.RelPoint.CENTER_TOP, 95.0f, -33.0f);
        this.root.add(bluecoffe);
        boolean knockedOutTutorialShown = SettingsManager.getBoolean("knockedOutTutorialShown");
        if (!knockedOutTutorialShown && !this.trainer.getTeam().hasHealtyMogas()) {
            showSleepTutorial();
            SettingsManager.setValue("knockedOutTutorialShown", true);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        if (this.dialog != null) {
            this.dialog.pause();
            this.dialog.hide();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        playMusic();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        this.mogaGallery.setScrollEnabled(this.dialog == null);
        if (this.dialog != null) {
            this.dialog.resume();
            this.dialog.show();
        }
        Gdx.input.setInputProcessor(this.mogaGallery.getInputProcessor());
    }

    private void playMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.playMusic(Constants.worldAmbienceAudio);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasUndeployedTiredMogas() {
        Team team = this.trainer.getTeam();
        for (Moga moga : this.trainer.getMogas()) {
            if (!team.getMogas().contains(moga) && moga.canGoToSleep()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putUndeployedTiredMogasToSleep() {
        Team team = this.trainer.getTeam();
        for (Moga moga : this.trainer.getMogas()) {
            if (!team.getMogas().contains(moga) && moga.canGoToSleep()) {
                moga.startNap();
            }
        }
        Game.save();
    }

    public void showShopDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.team.TeamScreen.2
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                TeamScreen.this.hideDialog();
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.SHOP, false, true);
                }
            }
        };
        showDialog(new AlertDialog(listener, "Your Mogas are tired but you don't have any Blue Coffee! Visit the Shop to get more.", "Visit Shop", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNapDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.team.TeamScreen.3
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    TeamScreen.this.putUndeployedTiredMogasToSleep();
                }
                MonsterGalaxy.showPreviousScreen();
            }
        };
        showDialog(new AlertDialog(listener, "Some of your Mogas are tired! Would you like to nap all your tired Mogas?", "Nap All", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON));
    }

    private void showSleepTutorial() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.team.TeamScreen.4
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                TeamScreen.this.hideDialog();
            }
        };
        String message = "If your Mogas are tired, try putting them to \n sleep by tapping the Nap button. You can \n also use a Blue Coffee to immediately \n recharge them.";
        showDialog(new AlertDialog(listener, message, "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON) { // from class: com.gaiaonline.monstergalaxy.team.TeamScreen.1MyAlertDialog
            {
                ActorScreenElement arrowElement = new ActorScreenElement();
                Image attackArrow = new Image("downArrow", Assets.loadTexture("down.arrow"));
                ScaleTo scaleUp = ScaleTo.$(1.2f, 1.2f, 0.5f);
                ScaleTo scaleDown = ScaleTo.$(1.0f, 1.0f, 0.5f);
                attackArrow.action(Forever.$(Sequence.$(scaleUp, scaleDown)));
                ResolutionManager.scaleActor(attackArrow);
                arrowElement.setActorDeprecated(attackArrow);
                arrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
                arrowElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 103.0f, 80.0f);
                add(arrowElement);
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.mogaGallery.getDeltaScroll() == 0.0f) {
            this.inhibitClick = false;
        } else {
            this.inhibitClick = true;
        }
        if (this.dialog != null) {
            this.root.update(deltaTime);
            if (this.stage != null) {
                this.stage.act(deltaTime);
            }
            this.dialog.update(deltaTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void showDialog(Dialog dialog) {
        this.mogaGallery.setScrollEnabled(false);
        super.showDialog(dialog);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void hideDialog() {
        super.hideDialog();
        Gdx.input.setInputProcessor(this.mogaGallery.getInputProcessor());
        this.mogaGallery.setScrollEnabled(true);
    }
}
