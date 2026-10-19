package com.gaiaonline.monstergalaxy.battle;

import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class PauseButton extends ButtonUI {
    public PauseButton(BattleScreen screen) {
        super(screen);
        setStretchable(false);
        setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        setSize(Assets.pauseOff.getRegionWidth(), Assets.pauseOff.getRegionHeight());
        addButton(UIEvent.PAUSE_BUTTON, Assets.pauseOff, Assets.pauseOn, 0.0f, 0.0f);
    }
}
