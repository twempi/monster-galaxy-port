package com.badlogic.gdx.graphics.g3d.loaders;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class ModelLoaderOld {
    public static Mesh loadObj(InputStream in) {
        return ObjLoader.loadObj(in);
    }
}
