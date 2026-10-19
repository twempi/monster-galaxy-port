package com.badlogic.gdx.graphics.g2d.tiled;

import java.util.HashMap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TiledLayer {
    public String name;
    public HashMap<String, String> properties = new HashMap<>(0);
    public int[][] tiles;

    TiledLayer() {
    }

    public int getWidth() {
        if (this.tiles[0] == null) {
            return 0;
        }
        return this.tiles[0].length;
    }

    public int getHeight() {
        return this.tiles.length;
    }
}
