package com.gaiaonline.monstergalaxy.startup;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextFieldScreenElement;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DesktopChooseMogaNameScreen extends ChooseMogaNameScreen {
    boolean textFieldAdded;
    TextFieldScreenElement textFieldElement;

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseMogaNameScreen
    protected void addMogaNameTextField(int x, int y, int width, int height, Integer maxChars, String disallowedChars) {
        if (!this.textFieldAdded) {
            this.textFieldAdded = true;
            this.textFieldElement = new TextFieldScreenElement(width);
            Gdx.input.setInputProcessor(this.textFieldElement);
            this.textFieldElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            this.textFieldElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, x, y);
            this.root.add(this.textFieldElement);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseMogaNameScreen
    protected void removeMogaNameTextField() {
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseMogaNameScreen
    protected String getMogaName() {
        return this.textFieldElement.getText();
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseMogaNameScreen
    protected void setMogaName(String mogaName) {
        this.textFieldElement.setText(mogaName);
    }
}
