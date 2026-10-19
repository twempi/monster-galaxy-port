package com.badlogic.gdx.assets;

/* JADX INFO: loaded from: classes.dex */
public class AssetLoaderParameters<T> {
    public LoadedCallback loadedCallback;

    public interface LoadedCallback {
        void finishedLoading(AssetManager assetManager, String str, Class cls);
    }
}
