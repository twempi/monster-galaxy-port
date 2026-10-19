package com.gaiaonline.monstergalaxy.tweak;

import com.badlogic.gdx.graphics.Color;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AutoPlayerScreen extends Screen {
    AutoPlayer autoPlayer = new AutoPlayer();

    public AutoPlayerScreen(int islandIndex) {
        this.autoPlayer.setIslandToComplete(islandIndex);
        this.root.addBackground(Assets.loadTexture("startup.choosezodiacmoga.bg"));
    }

    public static String[] getIslandNames() {
        List<Island> islands = Game.getStorage().getAllIslands();
        String[] islandNames = new String[islands.size()];
        for (int i = 0; i < islands.size(); i++) {
            islandNames[i] = islands.get(i).getName();
        }
        return islandNames;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        Game.getAndroidPlatformHelper().removeTextView();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        Game.getAndroidPlatformHelper().addTextView(450, 30, 100, "", Color.WHITE);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        Node node = this.autoPlayer.playNextNode();
        if (node != null) {
            Game.getAndroidPlatformHelper().setTextViewText("Completed node " + node.getIsland().getName() + " - " + node.getName());
        } else {
            Game.getAndroidPlatformHelper().setTextViewText("Done");
            MonsterGalaxy.showScreen(ScreenCode.WORLD);
        }
    }
}
