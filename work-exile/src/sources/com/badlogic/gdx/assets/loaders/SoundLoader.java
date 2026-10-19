package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SoundLoader extends SynchronousAssetLoader<Sound, SoundParameter> {
    public SoundLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
    public Sound load(AssetManager assetManager, String fileName, SoundParameter parameter) {
        return Gdx.audio.newSound(resolve(fileName));
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, SoundParameter parameter) {
        return null;
    }
}
