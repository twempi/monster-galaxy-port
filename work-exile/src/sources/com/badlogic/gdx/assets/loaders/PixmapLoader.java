package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class PixmapLoader extends AsynchronousAssetLoader<Pixmap, PixmapParameter> {
    Pixmap pixmap;

    public PixmapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public void loadAsync(AssetManager manager, String fileName, PixmapParameter parameter) {
        this.pixmap = null;
        this.pixmap = new Pixmap(resolve(fileName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public Pixmap loadSync() {
        return this.pixmap;
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, PixmapParameter parameter) {
        return null;
    }
}
