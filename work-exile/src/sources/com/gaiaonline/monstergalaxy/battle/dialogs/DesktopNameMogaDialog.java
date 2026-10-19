package com.gaiaonline.monstergalaxy.battle.dialogs;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextFieldScreenElement;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
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
        this.textFieldElement = new TextFieldScreenElement(width);
        Gdx.input.setInputProcessor(this.textFieldElement);
        this.textFieldElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.textFieldElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, x, y);
        add(this.textFieldElement);
    }
}
