package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BitmapFontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontParameter> {
    BitmapFont.BitmapFontData data;
    String fileName;
    AssetManager manager;

    public BitmapFontLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, BitmapFontParameter parameter) {
        FileHandle handle = resolve(fileName);
        this.data = new BitmapFont.BitmapFontData(handle, parameter != null ? parameter.flip : false);
        Array<AssetDescriptor> deps = new Array<>();
        deps.add(new AssetDescriptor(this.data.getImageFile(), Texture.class, null));
        return deps;
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public void loadAsync(AssetManager manager, String fileName, BitmapFontParameter parameter) {
        this.manager = manager;
        this.fileName = fileName;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public BitmapFont loadSync() {
        resolve(this.fileName);
        TextureRegion region = new TextureRegion((Texture) this.manager.get(this.data.getImageFile(), Texture.class));
        return new BitmapFont(this.data, region, true);
    }
}
