package com.gaiaonline.monstergalaxy.team;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.LifeElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.TextureElement;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class HealthBarElement extends ScreenGroup {
    Moga moga;
    LifeElement progressBarElement;

    public HealthBarElement() {
        TextureElement background = new TextureElement(Assets.loadTexture("playerhud.hp.bar.empty"));
        TextureRegion healthBarTexture = Assets.loadTexture("playerhud.hp.bar.full");
        this.progressBarElement = new LifeElement(healthBarTexture);
        float width = Math.max(background.getSize().x + 1.0f, this.progressBarElement.getSize().x) / ResolutionManager.getHorizontalAR();
        float height = Math.max(background.getSize().y, this.progressBarElement.getSize().y + 1.0f) / ResolutionManager.getVerticalAR();
        setSize(width, height);
        background.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        background.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        this.progressBarElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.progressBarElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 2.0f, 3.0f);
        add(background);
        add(this.progressBarElement);
        this.progressBarElement.setMaxLife(1);
        this.progressBarElement.setLife(0);
    }

    public void setMoga(Moga moga) {
        this.moga = moga;
        if (moga != null) {
            this.progressBarElement.setMaxLife(moga.getMaxHitPoints());
            this.progressBarElement.setLife(moga.getHitPoints());
        } else {
            this.progressBarElement.setMaxLife(1);
            this.progressBarElement.setLife(0);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void update(float deltaTime) {
        if (this.moga != null) {
            this.progressBarElement.setLife(this.moga.getHitPoints());
            this.progressBarElement.setVisible(this.moga.getHitPoints() > 0);
        }
        super.update(deltaTime);
    }
}
