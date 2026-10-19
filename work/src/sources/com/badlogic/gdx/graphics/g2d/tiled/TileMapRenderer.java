package com.badlogic.gdx.graphics.g2d.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: classes.dex */
public class TileMapRenderer implements Disposable {
    private static final int FLAG_FLIP_X = Integer.MIN_VALUE;
    private static final int FLAG_FLIP_Y = 1073741824;
    private static final int FLAG_ROTATE = 536870912;
    private static final int MASK_CLEAR = -536870912;
    private int[] allLayers;
    private TileAtlas atlas;
    private int[][][] blendedCacheId;
    private IntArray blendedTiles;
    private SpriteCache cache;
    private int currentCol;
    private int currentLayer;
    private int currentRow;
    private int initialCol;
    private int initialRow;
    private boolean isSimpleTileAtlas;
    private int lastCol;
    private int lastRow;
    private TiledMap map;
    private int mapHeightUnits;
    private int mapWidthUnits;
    private int[][][] normalCacheId;
    public float overdrawX;
    public float overdrawY;
    private int tileHeight;
    private int tileWidth;
    private int tilesPerBlockX;
    private int tilesPerBlockY;
    Vector3 tmp;
    private float unitsPerBlockX;
    private float unitsPerBlockY;
    private float unitsPerTileX;
    private float unitsPerTileY;

    public TileMapRenderer(TiledMap map, TileAtlas atlas, int tilesPerBlockX, int tilesPerBlockY) {
        this(map, atlas, tilesPerBlockX, tilesPerBlockY, map.tileWidth, map.tileHeight);
    }

    public TileMapRenderer(TiledMap map, TileAtlas atlas, int tilesPerBlockX, int tilesPerBlockY, float unitsPerTileX, float unitsPerTileY) {
        this(map, atlas, tilesPerBlockX, tilesPerBlockY, unitsPerTileX, unitsPerTileY, null);
    }

    public TileMapRenderer(TiledMap map, TileAtlas atlas, int tilesPerBlockX, int tilesPerBlockY, ShaderProgram shader) {
        this(map, atlas, tilesPerBlockX, tilesPerBlockY, map.tileWidth, map.tileHeight, shader);
    }

    public TileMapRenderer(TiledMap map, TileAtlas atlas, int tilesPerBlockX, int tilesPerBlockY, float unitsPerTileX, float unitsPerTileY, ShaderProgram shader) {
        IntArray blendedTilesArray;
        this.isSimpleTileAtlas = false;
        this.tmp = new Vector3();
        int[][][] tileMap = new int[map.layers.size()][][];
        for (int i = 0; i < map.layers.size(); i++) {
            tileMap[i] = map.layers.get(i).tiles;
        }
        for (int i2 = 0; i2 < map.tileSets.size(); i2++) {
            if (map.tileSets.get(i2).tileHeight - map.tileHeight > this.overdrawY * unitsPerTileY) {
                this.overdrawY = (map.tileSets.get(i2).tileHeight - map.tileHeight) / unitsPerTileY;
            }
            if (map.tileSets.get(i2).tileWidth - map.tileWidth > this.overdrawX * unitsPerTileX) {
                this.overdrawX = (map.tileSets.get(i2).tileWidth - map.tileWidth) / unitsPerTileX;
            }
        }
        String blendedTiles = map.properties.get("blended tiles");
        if (blendedTiles != null) {
            blendedTilesArray = createFromCSV(blendedTiles);
        } else {
            blendedTilesArray = new IntArray(0);
        }
        init(tileMap, atlas, map.tileWidth, map.tileHeight, unitsPerTileX, unitsPerTileY, blendedTilesArray, tilesPerBlockX, tilesPerBlockY, shader);
        this.map = map;
    }

    public TileMapRenderer(int[][][] map, TileAtlas atlas, int tileWidth, int tileHeight, float unitsPerTileX, float unitsPerTileY, IntArray blendedTiles, int tilesPerBlockX, int tilesPerBlockY) {
        this.isSimpleTileAtlas = false;
        this.tmp = new Vector3();
        init(map, atlas, tileWidth, tileHeight, unitsPerTileX, unitsPerTileY, blendedTiles, tilesPerBlockX, tilesPerBlockY, null);
    }

    public TileMapRenderer(int[][][] map, TileAtlas atlas, int tileWidth, int tileHeight, float unitsPerTileX, float unitsPerTileY, IntArray blendedTiles, int tilesPerBlockX, int tilesPerBlockY, ShaderProgram shader) {
        this.isSimpleTileAtlas = false;
        this.tmp = new Vector3();
        init(map, atlas, tileWidth, tileHeight, unitsPerTileX, unitsPerTileY, blendedTiles, tilesPerBlockX, tilesPerBlockY, shader);
    }

    private void init(int[][][] map, TileAtlas atlas, int tileWidth, int tileHeight, float unitsPerTileX, float unitsPerTileY, IntArray blendedTiles, int tilesPerBlockX, int tilesPerBlockY, ShaderProgram shader) {
        this.atlas = atlas;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.unitsPerTileX = unitsPerTileX;
        this.unitsPerTileY = unitsPerTileY;
        this.blendedTiles = blendedTiles;
        this.tilesPerBlockX = tilesPerBlockX;
        this.tilesPerBlockY = tilesPerBlockY;
        this.unitsPerBlockX = tilesPerBlockX * unitsPerTileX;
        this.unitsPerBlockY = tilesPerBlockY * unitsPerTileY;
        this.isSimpleTileAtlas = atlas instanceof SimpleTileAtlas;
        this.allLayers = new int[map.length];
        int maxCacheSize = 0;
        int maxHeight = 0;
        int maxWidth = 0;
        for (int layer = 0; layer < map.length; layer++) {
            this.allLayers[layer] = layer;
            if (map[layer].length > maxHeight) {
                maxHeight = map[layer].length;
            }
            for (int row = 0; row < map[layer].length; row++) {
                if (map[layer][row].length > maxWidth) {
                    maxWidth = map[layer][row].length;
                }
                for (int col = 0; col < map[layer][row].length; col++) {
                    if (map[layer][row][col] != 0) {
                        maxCacheSize++;
                    }
                }
            }
        }
        this.mapHeightUnits = (int) (maxHeight * unitsPerTileY);
        this.mapWidthUnits = (int) (maxWidth * unitsPerTileX);
        if (shader == null) {
            this.cache = new SpriteCache(maxCacheSize, false);
        } else {
            this.cache = new SpriteCache(maxCacheSize, shader, false);
        }
        this.normalCacheId = new int[map.length][][];
        this.blendedCacheId = new int[map.length][][];
        for (int layer2 = 0; layer2 < map.length; layer2++) {
            this.normalCacheId[layer2] = new int[MathUtils.ceil(map[layer2].length / tilesPerBlockY)][];
            this.blendedCacheId[layer2] = new int[MathUtils.ceil(map[layer2].length / tilesPerBlockY)][];
            for (int row2 = 0; row2 < this.normalCacheId[layer2].length; row2++) {
                this.normalCacheId[layer2][row2] = new int[MathUtils.ceil(map[layer2][row2].length / tilesPerBlockX)];
                this.blendedCacheId[layer2][row2] = new int[MathUtils.ceil(map[layer2][row2].length / tilesPerBlockX)];
                for (int col2 = 0; col2 < this.normalCacheId[layer2][row2].length; col2++) {
                    if (this.isSimpleTileAtlas) {
                        this.blendedCacheId[layer2][row2][col2] = addBlock(map[layer2], row2, col2, false);
                    } else {
                        this.normalCacheId[layer2][row2][col2] = addBlock(map[layer2], row2, col2, false);
                        this.blendedCacheId[layer2][row2][col2] = addBlock(map[layer2], row2, col2, true);
                    }
                }
            }
        }
    }

    private int addBlock(int[][] layer, int blockRow, int blockCol, boolean blended) {
        TextureRegion reg;
        this.cache.beginCache();
        int firstCol = blockCol * this.tilesPerBlockX;
        int firstRow = blockRow * this.tilesPerBlockY;
        int lastCol = firstCol + this.tilesPerBlockX;
        int lastRow = firstRow + this.tilesPerBlockY;
        float offsetX = (this.tileWidth - this.unitsPerTileX) / 2.0f;
        float offsetY = (this.tileHeight - this.unitsPerTileY) / 2.0f;
        for (int row = firstRow; row < lastRow && row < layer.length; row++) {
            for (int col = firstCol; col < lastCol && col < layer[row].length; col++) {
                int tile = layer[row][col];
                boolean flipX = (FLAG_FLIP_X & tile) != 0;
                boolean flipY = (FLAG_FLIP_Y & tile) != 0;
                boolean rotate = (FLAG_ROTATE & tile) != 0;
                int tile2 = tile & 536870911;
                if (tile2 != 0 && blended == this.blendedTiles.contains(tile2) && (reg = this.atlas.getRegion(tile2)) != null) {
                    float x = (col * this.unitsPerTileX) - offsetX;
                    float y = (((layer.length - row) - 1) * this.unitsPerTileY) - offsetY;
                    float width = reg.getRegionWidth();
                    float height = reg.getRegionHeight();
                    float originX = width * 0.5f;
                    float originY = height * 0.5f;
                    float scaleX = this.unitsPerTileX / this.tileWidth;
                    float scaleY = this.unitsPerTileY / this.tileHeight;
                    float rotation = 0.0f;
                    int sourceX = reg.getRegionX();
                    int sourceY = reg.getRegionY();
                    int sourceWidth = reg.getRegionWidth();
                    int sourceHeight = reg.getRegionHeight();
                    if (rotate) {
                        if (flipX && flipY) {
                            rotation = -90.0f;
                            sourceX += sourceWidth;
                            sourceWidth = -sourceWidth;
                        } else if (flipX && !flipY) {
                            rotation = -90.0f;
                        } else if (flipY && !flipX) {
                            rotation = 90.0f;
                        } else if (!flipY && !flipX) {
                            rotation = -90.0f;
                            sourceY += sourceHeight;
                            sourceHeight = -sourceHeight;
                        }
                    } else {
                        if (flipX) {
                            sourceX += sourceWidth;
                            sourceWidth = -sourceWidth;
                        }
                        if (flipY) {
                            sourceY += sourceHeight;
                            sourceHeight = -sourceHeight;
                        }
                    }
                    this.cache.add(reg.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation, sourceX, sourceY, sourceWidth, sourceHeight, false, false);
                }
            }
        }
        return this.cache.endCache();
    }

    public void render() {
        render(0.0f, 0.0f, getMapWidthUnits(), getMapHeightUnits());
    }

    public void render(float x, float y, float width, float height) {
        render(x, y, width, height, this.allLayers);
    }

    public void render(OrthographicCamera cam) {
        render(cam, this.allLayers);
    }

    public void render(OrthographicCamera cam, int[] layers) {
        getProjectionMatrix().set(cam.combined);
        this.tmp.set(0.0f, 0.0f, 0.0f);
        cam.unproject(this.tmp);
        render(this.tmp.x, this.tmp.y, cam.zoom * cam.viewportWidth, cam.zoom * cam.viewportHeight, layers);
    }

    public void render(float x, float y, float width, float height, int[] layers) {
        this.lastRow = (int) ((this.mapHeightUnits - ((y - height) + this.overdrawY)) / this.unitsPerBlockY);
        this.initialRow = (int) ((this.mapHeightUnits - (y - this.overdrawY)) / this.unitsPerBlockY);
        this.initialRow = this.initialRow > 0 ? this.initialRow : 0;
        this.lastCol = (int) (((x + width) + this.overdrawX) / this.unitsPerBlockX);
        this.initialCol = (int) ((x - this.overdrawX) / this.unitsPerBlockX);
        this.initialCol = this.initialCol > 0 ? this.initialCol : 0;
        Gdx.gl.glBlendFunc(770, 771);
        this.cache.begin();
        if (this.isSimpleTileAtlas) {
            Gdx.gl.glEnable(3042);
            this.currentLayer = 0;
            while (this.currentLayer < layers.length) {
                this.currentRow = this.initialRow;
                while (this.currentRow <= this.lastRow && this.currentRow < getLayerHeightInBlocks(this.currentLayer)) {
                    this.currentCol = this.initialCol;
                    while (this.currentCol <= this.lastCol && this.currentCol < getLayerWidthInBlocks(this.currentLayer, this.currentRow)) {
                        this.cache.draw(this.blendedCacheId[layers[this.currentLayer]][this.currentRow][this.currentCol]);
                        this.currentCol++;
                    }
                    this.currentRow++;
                }
                this.currentLayer++;
            }
        } else {
            this.currentLayer = 0;
            while (this.currentLayer < layers.length) {
                this.currentRow = this.initialRow;
                while (this.currentRow <= this.lastRow && this.currentRow < getLayerHeightInBlocks(this.currentLayer)) {
                    this.currentCol = this.initialCol;
                    while (this.currentCol <= this.lastCol && this.currentCol < getLayerWidthInBlocks(this.currentLayer, this.currentRow)) {
                        Gdx.gl.glDisable(3042);
                        this.cache.draw(this.normalCacheId[layers[this.currentLayer]][this.currentRow][this.currentCol]);
                        Gdx.gl.glEnable(3042);
                        this.cache.draw(this.blendedCacheId[layers[this.currentLayer]][this.currentRow][this.currentCol]);
                        this.currentCol++;
                    }
                    this.currentRow++;
                }
                this.currentLayer++;
            }
        }
        this.cache.end();
        Gdx.gl.glDisable(3042);
    }

    private int getLayerWidthInBlocks(int layer, int row) {
        int normalCacheWidth = 0;
        if (this.normalCacheId != null && this.normalCacheId[layer] != null && this.normalCacheId[layer][row] != null) {
            normalCacheWidth = this.normalCacheId[layer][row].length;
        }
        int blendedCacheWidth = 0;
        if (this.blendedCacheId != null && this.blendedCacheId[layer] != null && this.blendedCacheId[layer][row] != null) {
            blendedCacheWidth = this.blendedCacheId[layer][row].length;
        }
        return Math.max(normalCacheWidth, blendedCacheWidth);
    }

    private int getLayerHeightInBlocks(int layer) {
        int normalCacheHeight = 0;
        if (this.normalCacheId != null && this.normalCacheId[layer] != null) {
            normalCacheHeight = this.normalCacheId[layer].length;
        }
        int blendedCacheHeight = 0;
        if (this.blendedCacheId != null && this.blendedCacheId[layer] != null) {
            blendedCacheHeight = this.blendedCacheId[layer].length;
        }
        return Math.max(normalCacheHeight, blendedCacheHeight);
    }

    public Matrix4 getProjectionMatrix() {
        return this.cache.getProjectionMatrix();
    }

    public Matrix4 getTransformMatrix() {
        return this.cache.getTransformMatrix();
    }

    public int getRow(int worldY) {
        return (int) (worldY / this.unitsPerTileY);
    }

    public int getCol(int worldX) {
        return (int) (worldX / this.unitsPerTileX);
    }

    public int getInitialRow() {
        return this.initialRow;
    }

    public int getInitialCol() {
        return this.initialCol;
    }

    public int getLastRow() {
        return this.lastRow;
    }

    public int getLastCol() {
        return this.lastCol;
    }

    public float getUnitsPerTileX() {
        return this.unitsPerTileX;
    }

    public float getUnitsPerTileY() {
        return this.unitsPerTileY;
    }

    public int getMapHeightUnits() {
        return this.mapHeightUnits;
    }

    public int getMapWidthUnits() {
        return this.mapWidthUnits;
    }

    private static int parseIntWithDefault(String string, int defaultValue) {
        if (string != null) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.cache.dispose();
    }

    public TiledMap getMap() {
        return this.map;
    }

    public TileAtlas getAtlas() {
        return this.atlas;
    }

    private static IntArray createFromCSV(String values) {
        IntArray list = new IntArray(false, (values.length() + 1) / 2);
        StringTokenizer st = new StringTokenizer(values, ",");
        while (st.hasMoreTokens()) {
            list.add(Integer.parseInt(st.nextToken()));
        }
        list.shrink();
        return list;
    }
}
