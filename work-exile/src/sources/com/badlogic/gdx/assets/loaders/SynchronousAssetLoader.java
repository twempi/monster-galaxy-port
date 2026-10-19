package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class SynchronousAssetLoader<T, P> extends AssetLoader<T, P> {
    public abstract T load(AssetManager assetManager, String str, P p);

    public SynchronousAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }
}
