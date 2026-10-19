package com.gaiaonline.monstergalaxy.startup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ChooseTamerScreen extends AbstractStartupScreen {
    public ChooseTamerScreen() {
        this.root.addBackground(Assets.loadTexture("startup.choosetamer.bg"));
        addTitle("Choose your Tamer");
        addTamer(Trainer.Gender.MALE, "pc.guy", new Vector2(-85.0f, 0.0f), new Vector2(0.0f, 0.0f));
        addTamer(Trainer.Gender.FEMALE, "pc.girl", new Vector2(85.0f, 0.0f), new Vector2(0.0f, -5.0f));
    }

    private void addTamer(final Trainer.Gender gender, String tamerAssetName, Vector2 buttonPosition, Vector2 relativeTamerPosition) {
        TextureRegion normal = Assets.loadTexture("startup.btn.tamer");
        TextureRegion selected = Assets.loadTexture("startup.btn.tamer.press");
        ButtonElement button = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.startup.ChooseTamerScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                ChooseTamerScreen.this.chooseGender(gender);
            }
        }, null, normal, selected);
        button.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        button.setPosition(ScreenElement.RelPoint.CENTER, buttonPosition.x, buttonPosition.y);
        this.root.add(button);
        Vector2 absoluteTamerPosition = relativeTamerPosition.add(buttonPosition);
        TextureElement tamer = new TextureElement(Assets.loadTexture(tamerAssetName));
        tamer.flip();
        tamer.setScale(0.7f);
        tamer.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        tamer.setPosition(ScreenElement.RelPoint.CENTER, absoluteTamerPosition.x, absoluteTamerPosition.y);
        this.root.add(tamer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void chooseGender(Trainer.Gender gender) {
        Game.getAnalytics().trackEvent("acquisition", "", "", "complete_choose_tamer", "", "");
        Game.getTrainer().setGender(gender);
        MonsterGalaxy.showScreen(ScreenCode.CHOOSE_TAMER_NAME);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
    }
}
