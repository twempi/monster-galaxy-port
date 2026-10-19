package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class SkinLoader extends AsynchronousAssetLoader<Skin, SkinParameter> {
    public SkinLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, SkinParameter parameter) {
        Array<AssetDescriptor> deps = new Array<>();
        if (parameter == null) {
            deps.add(new AssetDescriptor(resolve(fileName).pathWithoutExtension() + ".atlas", TextureAtlas.class));
        } else {
            deps.add(new AssetDescriptor(parameter.textureAtlasPath, TextureAtlas.class));
        }
        return deps;
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public void loadAsync(AssetManager manager, String fileName, SkinParameter parameter) {
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public Skin loadSync(AssetManager manager, String fileName, SkinParameter parameter) {
        String textureAtlasPath;
        if (parameter == null) {
            textureAtlasPath = resolve(fileName).pathWithoutExtension() + ".atlas";
        } else {
            textureAtlasPath = parameter.textureAtlasPath;
        }
        TextureAtlas atlas = (TextureAtlas) manager.get(textureAtlasPath, TextureAtlas.class);
        return new Skin(resolve(fileName), atlas);
    }

    public static class SkinParameter extends AssetLoaderParameters<Skin> {
        public final String textureAtlasPath;

        public SkinParameter(String textureAtlasPath) {
            this.textureAtlasPath = textureAtlasPath;
        }
    }
}
