package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class MusicLoader extends SynchronousAssetLoader<Music, MusicParameter> {

    public static class MusicParameter extends AssetLoaderParameters<Music> {
    }

    public MusicLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
    public Music load(AssetManager assetManager, String fileName, MusicParameter parameter) {
        return Gdx.audio.newMusic(resolve(fileName));
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, MusicParameter parameter) {
        return null;
    }
}
