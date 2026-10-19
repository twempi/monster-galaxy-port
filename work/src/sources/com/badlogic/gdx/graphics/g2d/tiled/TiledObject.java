package com.badlogic.gdx.graphics.g2d.tiled;

import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class TiledObject {
    public String name;
    public String polygon;
    public String polyline;
    public String type;
    public int x;
    public int y;
    public int width = 0;
    public int height = 0;
    public int gid = 0;
    public HashMap<String, String> properties = new HashMap<>();
}
