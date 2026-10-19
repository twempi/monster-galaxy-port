package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextureParameter implements AssetLoaderParameters<Texture> {
    public Pixmap.Format format = null;
    public boolean genMipMaps = false;
    public Texture texture = null;
    public TextureData textureData = null;
}
