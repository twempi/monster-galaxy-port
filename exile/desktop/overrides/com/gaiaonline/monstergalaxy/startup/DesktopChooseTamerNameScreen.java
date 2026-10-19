package com.gaiaonline.monstergalaxy.startup;

import com.gaiaonline.monstergalaxy.desktop.DesktopTextFields;
import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextFieldScreenElement;

/* Overrides the game class: length-limited desktop text field (see DesktopTextFields). */
public class DesktopChooseTamerNameScreen extends ChooseTamerNameScreen {
    boolean textFieldAdded;
    TextFieldScreenElement textFieldElement;

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected void addTamerNameTextField(int x, int y, int width, int height, Integer maxChars, String disallowedChars) {
        if (!this.textFieldAdded) {
            this.textFieldAdded = true;
            this.textFieldElement = DesktopTextFields.create(width, maxChars, disallowedChars);
            Gdx.input.setInputProcessor(this.textFieldElement);
            this.textFieldElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            this.textFieldElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, x, y);
            this.root.add(this.textFieldElement);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected void removeTamerNameTextField() {
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected String getTamerName() {
        return this.textFieldElement.getText();
    }

    @Override // com.gaiaonline.monstergalaxy.startup.ChooseTamerNameScreen
    protected void setTamerName(String tamerName) {
        this.textFieldElement.setText(tamerName);
    }
}
