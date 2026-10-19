package com.badlogic.gdx.graphics.g2d.tiled;

/* JADX INFO: loaded from: classes.dex */
public class TileSet {
    public int firstgid;
    public String imageName;
    public int margin;
    public String name;
    public int spacing;
    public int tileHeight;
    public int tileWidth;

    protected TileSet() {
        this.margin = 0;
        this.spacing = 0;
    }

    protected TileSet(TileSet set) {
        this.margin = 0;
        this.spacing = 0;
        this.firstgid = set.firstgid;
        this.tileWidth = set.tileWidth;
        this.tileHeight = set.tileHeight;
        this.margin = set.margin;
        this.spacing = set.spacing;
        this.imageName = set.imageName;
        this.name = set.name;
    }
}
