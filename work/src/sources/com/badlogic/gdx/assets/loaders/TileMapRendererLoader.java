package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class TileMapRendererLoader extends SynchronousAssetLoader<TileMapRenderer, TileMapParameter> {

    public static class TileMapParameter extends AssetLoaderParameters<TileMapRenderer> {
        public final String imageDirectory;
        public final int tilesPerBlockX;
        public final int tilesPerBlockY;
        public final float unitsPerTileX;
        public final float unitsPerTileY;

        public TileMapParameter(String imageDirectory, int tilesPerBlockX, int tilesPerBlockY) {
            this.imageDirectory = imageDirectory;
            this.tilesPerBlockX = tilesPerBlockX;
            this.tilesPerBlockY = tilesPerBlockY;
            this.unitsPerTileX = 0.0f;
            this.unitsPerTileY = 0.0f;
        }

        public TileMapParameter(String imageDirectory, int tilesPerBlockX, int tilesPerBlockY, float unitsPerTileX, float unitsPerTileY) {
            this.imageDirectory = imageDirectory;
            this.tilesPerBlockX = tilesPerBlockX;
            this.tilesPerBlockY = tilesPerBlockY;
            this.unitsPerTileX = unitsPerTileX;
            this.unitsPerTileY = unitsPerTileY;
        }
    }

    public TileMapRendererLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override // com.badlogic.gdx.assets.loaders.AssetLoader
    public Array<AssetDescriptor> getDependencies(String fileName, TileMapParameter parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("Missing TileMapRendererParameter: " + fileName);
        }
        return null;
    }

    @Override // com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
    public TileMapRenderer load(AssetManager assetManager, String fileName, TileMapParameter parameter) {
        TiledMap map = TiledLoader.createMap(resolve(fileName));
        TileAtlas atlas = new TileAtlas(map, resolve(parameter.imageDirectory));
        return (parameter.unitsPerTileX == 0.0f || parameter.unitsPerTileY == 0.0f) ? new TileMapRenderer(map, atlas, parameter.tilesPerBlockX, parameter.tilesPerBlockY) : new TileMapRenderer(map, atlas, parameter.tilesPerBlockX, parameter.tilesPerBlockY, parameter.unitsPerTileX, parameter.unitsPerTileY);
    }
}
