package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.ETC1TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class TextureLoader extends AsynchronousAssetLoader<Texture, TextureParameter> {
    TextureData data;
    Texture texture;

    public static class TextureParameter extends AssetLoaderParameters<Texture> {
        public Pixmap.Format format = null;
        public boolean genMipMaps = false;
        public Texture texture = null;
        public TextureData textureData = null;
        public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
        public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
        public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
        public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
    }

    public TextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public void loadAsync(AssetManager manager, String fileName, TextureParameter parameter) {
        Pixmap pixmap;
        if (parameter == null || (parameter != null && parameter.textureData == null)) {
            Pixmap.Format format = null;
            boolean genMipMaps = false;
            this.texture = null;
            if (parameter != null) {
                format = parameter.format;
                genMipMaps = parameter.genMipMaps;
                this.texture = parameter.texture;
            }
            FileHandle handle = resolve(fileName);
            if (!fileName.contains(".etc1")) {
                if (fileName.contains(".cim")) {
                    pixmap = PixmapIO.readCIM(handle);
                } else {
                    pixmap = new Pixmap(handle);
                }
                this.data = new FileTextureData(handle, pixmap, format, genMipMaps);
                return;
            }
            this.data = new ETC1TextureData(handle, genMipMaps);
            return;
        }
        this.data = parameter.textureData;
        if (!this.data.isPrepared()) {
            this.data.prepare();
        }
        this.texture = parameter.texture;
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public Texture loadSync(AssetManager manager, String fileName, TextureParameter parameter) {
        Texture texture = this.texture;
        if (texture != null) {
            texture.load(this.data);
        } else {
            texture = new Texture(this.data);
        }
        if (parameter != null) {
            texture.setFilter(parameter.minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }
        return texture;
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, TextureParameter parameter) {
        return null;
    }
}
