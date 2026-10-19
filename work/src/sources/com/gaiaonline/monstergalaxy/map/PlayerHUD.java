package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ProgressBarElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.team.HealthBarElement;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PlayerHUD extends ScreenGroup {
    private ShadowTextElement energyText;
    private ProgressBarElement tamerEnergyProgressBar;
    private TextureElement teamBg;
    private Trainer trainer;
    private TextElement trainerEnergyTimer;

    public PlayerHUD() {
        setStretchable(false);
        this.size.x = Gdx.graphics.getWidth();
        this.size.y = Gdx.graphics.getHeight();
        this.trainer = Game.getTrainer();
        this.teamBg = new TextureElement(Assets.loadTexture("playerhud.topleft.hud"));
        this.teamBg.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.teamBg.setPosition(ScreenElement.RelPoint.LEFT_TOP, 0.0f, 0.0f);
        add(this.teamBg);
        ScreenListener teamButtonListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.PlayerHUD.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                SoundManager.playScreenChangeSound();
                MonsterGalaxy.showScreen(ScreenCode.TEAM, false, true);
            }
        };
        ButtonElement teamButton = new ButtonElement(teamButtonListener, UIEvent.MANAGE_TEAM, Assets.loadLocalizedTexture("playerhud.team.off"), Assets.loadLocalizedTexture("playerhud.team.on"));
        teamButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        teamButton.setPosition(ScreenElement.RelPoint.LEFT_TOP, 40.0f, -33.0f);
        add(teamButton);
        addTeamMogasPortraits();
        TextureElement trainerBg = new TextureElement(Assets.loadTexture("playerhud.topright.hud"));
        trainerBg.setAnchorPoint(ScreenElement.RelPoint.RIGHT_TOP);
        trainerBg.setPosition(ScreenElement.RelPoint.RIGHT_TOP, 0.0f, 0.0f);
        add(trainerBg);
        addTrainerEnergy();
        InventoryBar inventoryBar = new InventoryBar();
        inventoryBar.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        inventoryBar.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -4.0f, -44.0f);
        add(inventoryBar);
    }

    private void addTrainerEnergy() {
        TextureElement progressBarBg = new TextureElement(Assets.loadTexture("trainer.energybar.empty"));
        progressBarBg.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        progressBarBg.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -76.0f, -17.0f);
        this.tamerEnergyProgressBar = new ProgressBarElement(Assets.loadTexture("trainer.energybar.full"));
        this.tamerEnergyProgressBar.setMaxValue(Constants.maxTrainerEnergy);
        this.tamerEnergyProgressBar.setValue(this.trainer.getEnergy());
        this.tamerEnergyProgressBar.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.tamerEnergyProgressBar.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -76.0f, -17.0f);
        this.energyText = new ShadowTextElement("100", 0.6f, ColorConstants.ENERGY_TEXT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.energyText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.energyText.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -80.0f, -15.0f);
        ShadowTextElement maxEnergyText = new ShadowTextElement("/" + Constants.maxTrainerEnergy, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        maxEnergyText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        maxEnergyText.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -48.0f, -15.0f);
        TextureElement energyIcon = new TextureElement(Assets.loadTexture("island.energy.icon"));
        energyIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        energyIcon.setPosition(ScreenElement.RelPoint.RIGHT_TOP, -102.0f, -17.0f);
        add(progressBarBg);
        add(this.tamerEnergyProgressBar);
        add(energyIcon);
        add(this.energyText);
        add(maxEnergyText);
        this.trainerEnergyTimer = addLabel("0:00", -150.0f, -7.0f, ScreenElement.RelPoint.RIGHT_TOP, 0.6f, ColorConstants.SECONARY_FONT_COLOR, true, true);
        this.trainerEnergyTimer.setAnchorPoint(ScreenElement.RelPoint.RIGHT_TOP);
        updateTamerEnergy();
    }

    private void updateTamerEnergy() {
        this.tamerEnergyProgressBar.setValue(this.trainer.getEnergy());
        this.energyText.setText(String.valueOf(this.trainer.getEnergy()));
    }

    private void addTeamMogasPortraits() {
        List<Moga> mogas = Game.getTrainer().getTeam().getMogas();
        for (int i = 0; i < 3; i++) {
            Moga moga = null;
            if (i < mogas.size()) {
                Moga moga2 = mogas.get(i);
                moga = moga2;
            }
            addMogaPortrait(moga, i);
        }
    }

    private void addMogaPortrait(Moga moga, int index) {
        Vector2 position = new Vector2((index * 42) + 96, -26.0f);
        ScreenElement mogaBg = new TextureElement(Assets.loadTexture("playerhud.moga.bg.small"));
        mogaBg.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        mogaBg.setPosition(ScreenElement.RelPoint.LEFT_TOP, position.x, position.y);
        add(mogaBg);
        if (moga != null) {
            ScreenElement portrait = new TextureElement(moga.getType().getPortrait());
            portrait.setScale(0.5f);
            portrait.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            portrait.setPosition(ScreenElement.RelPoint.LEFT_TOP, position.x, position.y);
            add(portrait);
            HealthBarElement healthBarElement = new HealthBarElement();
            healthBarElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
            healthBarElement.setPosition(ScreenElement.RelPoint.LEFT_TOP, position.x, position.y - 22.0f);
            healthBarElement.setMoga(moga);
            add(healthBarElement);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        super.update(deltaTime);
        boolean trainerIsFullyCharged = this.trainer.isEnergyFullyCharged();
        if (!trainerIsFullyCharged) {
            this.trainerEnergyTimer.setText(this.trainer.getEnergyTimerText());
        }
        updateTamerEnergy();
        this.trainerEnergyTimer.setVisible(!trainerIsFullyCharged);
    }
}
