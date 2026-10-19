package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class FloatTextureData implements TextureData {
    FloatBuffer buffer;
    int height;
    boolean isPrepared = false;
    int width;

    public FloatTextureData(int w, int h) {
        this.width = 0;
        this.height = 0;
        this.width = w;
        this.height = h;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public TextureData.TextureDataType getType() {
        return TextureData.TextureDataType.Float;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean isPrepared() {
        return this.isPrepared;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public void prepare() {
        if (this.isPrepared) {
            throw new GdxRuntimeException("Already prepared");
        }
        this.buffer = BufferUtils.newFloatBuffer(this.width * this.height * 4);
        this.isPrepared = true;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public void consumeCompressedData() {
        if (!Gdx.graphics.supportsExtension("texture_float")) {
            throw new GdxRuntimeException("Extension OES_TEXTURE_FLOAT not supported!");
        }
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS || Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Gdx.gl.glTexImage2D(3553, 0, 6408, this.width, this.height, 0, 6408, 5126, this.buffer);
        } else {
            Gdx.gl.glTexImage2D(3553, 0, 34836, this.width, this.height, 0, 6408, 5126, this.buffer);
        }
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public Pixmap consumePixmap() {
        throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean disposePixmap() {
        throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public int getWidth() {
        return this.width;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public int getHeight() {
        return this.height;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public Pixmap.Format getFormat() {
        return Pixmap.Format.RGBA8888;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean useMipMaps() {
        return false;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean isManaged() {
        return true;
    }
}
