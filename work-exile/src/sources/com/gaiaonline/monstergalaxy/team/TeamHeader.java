package com.gaiaonline.monstergalaxy.team;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TeamHeader extends ButtonUI implements ScreenListener {
    private static final int HEADER_HEIGHT = 100;
    private int blueCoffees;
    private TextElement blueCoffeesTextElement;
    private int mogaTypeCount;
    private int numberOfCaughtMogas;
    private TextElement numberOfMogas;
    private List<PortraitView> portraitViews;
    private List<Moga> teamMogas;
    private Trainer trainer;
    private List<Moga> trainerMogas;

    public TeamHeader(ScreenListener doneListener) {
        setScreenListener(this);
        setPosition(ScreenElement.RelPoint.LEFT_TOP, 0.0f, 0.0f);
        setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        setSize(480.0f, 100.0f);
        this.trainer = Game.getTrainer();
        this.portraitViews = new ArrayList();
        this.teamMogas = this.trainer.getTeam().getMogas();
        this.trainerMogas = this.trainer.getMogas();
        float mogaPosition = -40.0f;
        for (int i = 0; i < 3; i++) {
            PortraitView portrait = new PortraitView(this.screenListener, mogaPosition, 0.0f, i);
            add(portrait);
            this.portraitViews.add(portrait);
            mogaPosition += 50.0f;
        }
        this.mogaTypeCount = Game.getStorage().getMogaTypeCount(MogaType.SubType.NORMAL) + Game.getStorage().getMogaTypeCount(MogaType.SubType.LUCKY);
        this.numberOfMogas = addLabel("0/0", 10.0f, 27.0f, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, true);
        addLabel("Mogas Caught", 10.0f, 10.0f, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, true);
        updateNumberOfMogas();
        this.blueCoffeesTextElement = addLabel("000", 125.0f, 16.0f, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
        updateBlueCoffees();
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        ButtonElement doneButton = new ButtonElement(doneListener, UIEvent.TEAM_DONE, normalButton, selectedButton);
        doneButton.setPosition(ScreenElement.RelPoint.CENTER, 195.0f, 15.0f);
        doneButton.setScale(0.9f);
        add(doneButton);
        addLabel("Done", 195.0f, 20.0f, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
    }

    private void updateNumberOfMogas() {
        this.numberOfCaughtMogas = this.trainerMogas.size();
        String text = String.valueOf(this.numberOfCaughtMogas) + "/" + this.mogaTypeCount;
        this.numberOfMogas.setText(text);
    }

    private void updateBlueCoffees() {
        String blueCoffeesLabelText;
        float blueCoffeeFontScale;
        this.blueCoffees = this.trainer.getBlueCoffeeCups();
        if (this.blueCoffees <= 999) {
            blueCoffeesLabelText = String.valueOf(this.blueCoffees);
            blueCoffeeFontScale = 0.55f;
        } else {
            blueCoffeesLabelText = "+999";
            blueCoffeeFontScale = 0.42f;
        }
        this.blueCoffeesTextElement.setFontScale(blueCoffeeFontScale);
        this.blueCoffeesTextElement.setText(blueCoffeesLabelText);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.blueCoffees != this.trainer.getBlueCoffeeCups()) {
            updateBlueCoffees();
        }
        if (this.numberOfCaughtMogas != this.trainerMogas.size()) {
            updateNumberOfMogas();
        }
        for (int n = 0; n < 3; n++) {
            this.portraitViews.get(n).setMogaIndex(n);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeMogaFromTeam(int mogaIndex) {
        if (mogaIndex < this.teamMogas.size()) {
            this.teamMogas.remove(mogaIndex);
            for (int i = 0; i < 3; i++) {
                PortraitView view = this.portraitViews.get(i);
                view.setMogaIndex(i);
            }
        }
    }

    private class PortraitView extends ButtonUI implements ScreenListener {
        private HealthBarElement healthBarElement;
        private int mogaIndex;
        private ButtonElement portraitElement;
        private ButtonElement removeButton;

        public PortraitView(ScreenListener screenListener, float x, float y, int mogaIndex) {
            setScreenListener(this);
            setPosition(ScreenElement.RelPoint.LEFT_TOP, x, y);
            setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
            setSize(100.0f, 100.0f);
            this.mogaIndex = mogaIndex;
            TextureElement mogaBg = add(Assets.mogaBackground, 65.0f, -30.0f, ScreenElement.RelPoint.LEFT_TOP, (ScreenElement) null, (ScreenElement.RelPoint) null);
            mogaBg.setScale(0.6f);
            this.portraitElement = addButton(UIEvent.TEAM_REMOVE_MOGA, Assets.mogaBackground, null, 65.0f, -30.0f, ScreenElement.RelPoint.LEFT_TOP);
            this.portraitElement.setScale(0.6f);
            this.healthBarElement = new HealthBarElement();
            this.healthBarElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
            this.healthBarElement.setPosition(ScreenElement.RelPoint.LEFT_TOP, 65.0f, -54.0f);
            add(this.healthBarElement);
            TextureRegion normalRemoveButton = Assets.loadTexture("remove.button");
            TextureRegion selectedRemoveButton = Assets.loadTexture("remove.button.press");
            this.removeButton = addButton(UIEvent.TEAM_REMOVE_MOGA, normalRemoveButton, selectedRemoveButton, 0.0f, 35.0f, ScreenElement.RelPoint.CENTER);
            this.removeButton.setScale(0.6f);
            if (mogaIndex < TeamHeader.this.teamMogas.size()) {
                this.portraitElement.setNormal(((Moga) TeamHeader.this.teamMogas.get(mogaIndex)).getType().getPortrait());
                return;
            }
            this.portraitElement.setVisible(false);
            this.removeButton.setVisible(false);
            this.healthBarElement.setVisible(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMogaIndex(int mogaIndex) {
            this.mogaIndex = mogaIndex;
            if (mogaIndex < TeamHeader.this.teamMogas.size()) {
                Moga moga = (Moga) TeamHeader.this.teamMogas.get(mogaIndex);
                this.portraitElement.setNormal(moga.getType().getPortrait());
                this.portraitElement.setVisible(true);
                this.removeButton.setVisible(true);
                this.healthBarElement.setVisible(true);
                this.healthBarElement.setMoga(moga);
                return;
            }
            this.portraitElement.setVisible(false);
            this.removeButton.setVisible(false);
            this.healthBarElement.setVisible(false);
            this.portraitElement.setNormal(null);
            this.healthBarElement.setMoga(null);
        }

        @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
        public void onUIEvent(UIEvent event) {
            TeamHeader.this.removeMogaFromTeam(this.mogaIndex);
        }
    }
}
