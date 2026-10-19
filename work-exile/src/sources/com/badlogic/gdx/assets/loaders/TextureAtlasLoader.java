package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextureAtlasLoader extends SynchronousAssetLoader<TextureAtlas, TextureAtlasParameter> {
    TextureAtlas.TextureAtlasData data;

    public TextureAtlasLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
    public TextureAtlas load(AssetManager assetManager, String fileName, TextureAtlasParameter parameter) {
        for (TextureAtlas.TextureAtlasData.Page page : this.data.getPages()) {
            Texture texture = (Texture) assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
            page.texture = texture;
        }
        return new TextureAtlas(this.data);
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, TextureAtlasParameter parameter) {
        FileHandle atlasFile = resolve(fileName);
        FileHandle imgDir = atlasFile.parent();
        if (parameter != null) {
            this.data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, parameter.flip);
        } else {
            this.data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, false);
        }
        Array<AssetDescriptor> dependencies = new Array<>();
        for (TextureAtlas.TextureAtlasData.Page page : this.data.getPages()) {
            FileHandle handle = resolve(page.textureFile.path());
            TextureParameter params = new TextureParameter();
            params.format = page.format;
            params.genMipMaps = page.useMipMaps;
            dependencies.add(new AssetDescriptor(handle.path().replaceAll("\\\\", "/"), Texture.class, params));
        }
        return dependencies;
    }
}
