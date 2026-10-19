package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BackpackButton extends ButtonUI implements BackpackMenu.BackpackListener {
    private ButtonElement button;
    private BackpackMenu menu;
    private BattleScreen screen;

    public BackpackButton(BattleScreen screen) {
        super(screen);
        setStretchable(true);
        setSize(480.0f, 320.0f);
        setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        this.screen = screen;
        this.button = addButton(UIEvent.BACKPACK_BUTTON, Assets.loadTexture("items.off"), Assets.loadTexture("items.on"), 212.0f, 30.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        this.menu = new BackpackMenu(this);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.Touchable
    public boolean touched(Vector2 touchPoint) {
        if (!this.menu.isOpen) {
            return super.touched(touchPoint);
        }
        if (!this.menu.isTouched(touchPoint) && !contains(this.button, touchPoint)) {
            this.screen.onUIEvent(UIEvent.BACKPACK_BUTTON);
        }
        return true;
    }

    public void showMenu() {
        this.menu.open();
        if (this.screen.getStage().findActor(this.menu.name) == null) {
            this.screen.getStage().addActor(this.menu);
        }
        SoundManager.playSound(SoundManager.SOUND_MENU_SLIDE);
    }

    public boolean isMenuOpen() {
        return this.menu.isOpen;
    }

    public void closeMenu() {
        this.menu.close();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            this.menu.close();
            Actor menuActor = this.screen.getStage().findActor(this.menu.name);
            if (menuActor != null) {
                this.screen.getStage().removeActor(menuActor);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.battle.BackpackMenu.BackpackListener
    public void onMenuItemSelected(int index) {
        if (index == 1) {
            this.screen.onUIEvent(UIEvent.DRINK_BLUE_COFFEE);
            String currentIslandId = String.valueOf(Game.getTrainer().getCurrentIsland().getId());
            Game.getAnalytics().trackEvent("monetization", "use_item", "blue_coffee", "blue_coffee_backpack", "", currentIslandId);
        } else if (index == 2) {
            closeMenu();
            this.screen.onUIEvent(UIEvent.CAPTURE_BUTTON);
        }
    }
}
