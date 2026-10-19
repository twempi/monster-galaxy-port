package com.badlogic.gdx.graphics.g2d.tiled;

import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class TiledObjectGroup {
    public int height;
    public String name;
    public int width;
    public HashMap<String, String> properties = new HashMap<>();
    public ArrayList<TiledObject> objects = new ArrayList<>();
}
