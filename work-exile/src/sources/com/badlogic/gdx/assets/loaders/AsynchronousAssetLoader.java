package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class AsynchronousAssetLoader<T, P> extends AssetLoader<T, P> {
    public abstract void loadAsync(AssetManager assetManager, String str, P p);

    public abstract T loadSync();

    public AsynchronousAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }
}
