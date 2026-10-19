package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ButtonUI;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class WorldMenu extends ButtonUI implements ScreenListener {
    private boolean isOpen;
    private Listener listener;
    private WorldMenuItems menu;
    private ButtonElement menuButton;
    private Stage stage;

    public interface Listener {
        void onWorldMenuToggled(boolean z);
    }

    public WorldMenu(Stage stage) {
        this.stage = stage;
        setStretchable(true);
        setSize(480.0f, 320.0f);
        setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        TextureRegion normalButton = Assets.loadTexture("menu.off");
        TextureRegion selectedButton = Assets.loadTexture("menu.on");
        this.menuButton = addButton(UIEvent.MENU_BUTTON, normalButton, selectedButton, 212.0f, 30.0f, ScreenElement.RelPoint.CENTER_BOTTOM);
        this.menu = new WorldMenuItems("menu");
        this.menuButton.setScreenListener(this);
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void toggleMenu() {
        SoundManager.playSound(SoundManager.SOUND_MENU_SLIDE);
        if (this.menu.isOpen()) {
            this.isOpen = false;
            this.menu.close();
            if (this.listener != null) {
                this.listener.onWorldMenuToggled(false);
                return;
            }
            return;
        }
        this.stage.removeActor(this.menu);
        this.isOpen = true;
        this.menu.open();
        if (this.listener != null) {
            this.listener.onWorldMenuToggled(true);
        }
        this.stage.addActor(this.menu);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (event == UIEvent.MENU_BUTTON) {
            toggleMenu();
        }
    }
}
