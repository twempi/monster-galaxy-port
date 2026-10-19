package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class BitmapFontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontParameter> {
    BitmapFont.BitmapFontData data;

    public static class BitmapFontParameter extends AssetLoaderParameters<BitmapFont> {
        public boolean flip = false;
        public Texture.TextureFilter minFitler = Texture.TextureFilter.Nearest;
        public Texture.TextureFilter maxFilter = Texture.TextureFilter.Nearest;
        public BitmapFont.BitmapFontData bitmapFontData = null;
    }

    public BitmapFontLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, BitmapFontParameter parameter) {
        Array<AssetDescriptor> deps = new Array<>();
        if (parameter != null && parameter.bitmapFontData != null) {
            this.data = parameter.bitmapFontData;
        } else {
            FileHandle handle = resolve(fileName);
            this.data = new BitmapFont.BitmapFontData(handle, parameter != null ? parameter.flip : false);
            deps.add(new AssetDescriptor(this.data.getImagePath(), Texture.class));
        }
        return deps;
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public void loadAsync(AssetManager manager, String fileName, BitmapFontParameter parameter) {
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public BitmapFont loadSync(AssetManager manager, String fileName, BitmapFontParameter parameter) {
        resolve(fileName);
        TextureRegion region = new TextureRegion((Texture) manager.get(this.data.getImagePath(), Texture.class));
        if (parameter != null) {
            region.getTexture().setFilter(parameter.minFitler, parameter.maxFilter);
        }
        return new BitmapFont(this.data, region, true);
    }
}
