package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.ReferenceCountedAsset;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.ETC1TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextureLoader extends AsynchronousAssetLoader<Texture, TextureParameter> {
    TextureData data;
    Texture texture;

    public TextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public void loadAsync(AssetManager manager, String fileName, TextureParameter parameter) {
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
                Pixmap pixmap = new Pixmap(handle);
                this.data = new FileTextureData(handle, pixmap, format, genMipMaps);
                return;
            } else {
                this.data = new ETC1TextureData(handle, genMipMaps);
                return;
            }
        }
        this.data = parameter.textureData;
        this.texture = parameter.texture;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
    public Texture loadSync() {
        if (this.texture == null) {
            return new ReferenceCountedTexture(this.data);
        }
        this.texture.load(this.data);
        return this.texture;
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, TextureParameter parameter) {
        return null;
    }

    public static class ReferenceCountedTexture extends Texture implements ReferenceCountedAsset {
        private int refCount;

        public ReferenceCountedTexture(TextureData data) {
            super(data);
            this.refCount = 1;
        }

        @Override // com.badlogic.gdx.assets.ReferenceCountedAsset
        public void incRefCount() {
            this.refCount++;
        }

        @Override // com.badlogic.gdx.assets.ReferenceCountedAsset
        public int getRefCount() {
            return this.refCount;
        }

        @Override // com.badlogic.gdx.graphics.Texture, com.badlogic.gdx.utils.Disposable
        public void dispose() {
            this.refCount--;
            if (this.refCount <= 0) {
                super.dispose();
            }
        }

        @Override // com.badlogic.gdx.assets.ReferenceCountedAsset
        public void setRefCount(int refCount) {
            this.refCount = refCount;
        }
    }
}
