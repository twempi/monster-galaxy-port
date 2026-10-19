package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/* JADX INFO: loaded from: classes.dex */
public class FloatFrameBuffer extends FrameBuffer {
    public FloatFrameBuffer(int width, int height, boolean hasDepth) {
        super(null, width, height, hasDepth);
    }

    @Override // com.badlogic.gdx.graphics.glutils.FrameBuffer
    protected void setupTexture() {
        FloatTextureData data = new FloatTextureData(this.width, this.height);
        this.colorTexture = new Texture(data);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop || Gdx.app.getType() == Application.ApplicationType.Applet) {
            this.colorTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } else {
            this.colorTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        this.colorTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    }
}
