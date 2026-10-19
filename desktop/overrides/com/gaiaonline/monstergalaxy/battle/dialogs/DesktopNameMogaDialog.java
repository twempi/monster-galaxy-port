package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.desktop.DesktopTextFields;
import com.gaiaonline.monstergalaxy.screen.TextFieldScreenElement;

/* Overrides the game class: sized, length-limited desktop text field (see DesktopTextFields). */
public class DesktopNameMogaDialog extends NameMogaDialog {
    private TextFieldScreenElement textFieldElement;

    protected DesktopNameMogaDialog(ScreenListener screenListener, Moga moga) {
        super(screenListener, moga);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog
    protected String getMogaName() {
        return this.textFieldElement.getText();
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog
    protected void setMogaName(String mogaName) {
        this.textFieldElement.setText(mogaName);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.dialogs.NameMogaDialog
    protected void addMogaNameTextField(int x, int y, int width, int height, Integer maxChars, String disallowedChars) {
        this.textFieldElement = DesktopTextFields.create(width, maxChars, disallowedChars);
        Gdx.input.setInputProcessor(this.textFieldElement);
        this.textFieldElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.textFieldElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, x, y);
        add(this.textFieldElement);
    }
}
