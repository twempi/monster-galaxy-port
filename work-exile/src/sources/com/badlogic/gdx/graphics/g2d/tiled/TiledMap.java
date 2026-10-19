package com.badlogic.gdx.graphics.g2d.tiled;

import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TiledMap {
    public int height;
    public String orientation;
    public int tileHeight;
    public int tileWidth;
    public FileHandle tmxFile;
    public int width;
    public ArrayList<TiledLayer> layers = new ArrayList<>(4);
    public ArrayList<TiledObjectGroup> objectGroups = new ArrayList<>(1);
    public ArrayList<TileSet> tileSets = new ArrayList<>(5);
    public HashMap<String, String> properties = new HashMap<>(2);
    private ArrayList<TileProperty> tileProperties = new ArrayList<>(0);

    public void setTileProperty(int id, String name, String value) {
        for (TileProperty tp : this.tileProperties) {
            if (tp.id == id) {
                tp.propertyMap.put(name, value);
                return;
            }
        }
        TileProperty tempProperty = new TileProperty();
        tempProperty.id = id;
        tempProperty.propertyMap.put(name, value);
        this.tileProperties.add(tempProperty);
    }

    public String getTileProperty(int id, String name) {
        for (TileProperty tp : this.tileProperties) {
            if (tp.id == id) {
                return tp.propertyMap.get(name);
            }
        }
        return null;
    }

    class TileProperty {
        int id;
        HashMap<String, String> propertyMap = new HashMap<>();

        TileProperty() {
        }
    }
}
