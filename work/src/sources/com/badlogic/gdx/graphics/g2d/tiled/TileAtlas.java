package com.badlogic.gdx.graphics.g2d.tiled;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import java.util.HashSet;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: classes.dex */
public class TileAtlas implements Disposable {
    protected IntMap<TextureRegion> regionsMap = new IntMap<>();
    protected final HashSet<Texture> textures = new HashSet<>(1);

    protected TileAtlas() {
    }

    public TileAtlas(TiledMap map, FileHandle inputDir) {
        for (TileSet set : map.tileSets) {
            FileHandle packfile = getRelativeFileHandle(inputDir, removeExtension(set.imageName) + " packfile");
            TextureAtlas textureAtlas = new TextureAtlas(packfile, packfile.parent(), false);
            Array<TextureAtlas.AtlasRegion> atlasRegions = textureAtlas.findRegions(removeExtension(removePath(set.imageName)));
            for (TextureAtlas.AtlasRegion reg : atlasRegions) {
                this.regionsMap.put(reg.index + set.firstgid, reg);
                if (!this.textures.contains(reg.getTexture())) {
                    this.textures.add(reg.getTexture());
                }
            }
        }
    }

    public TextureRegion getRegion(int id) {
        return this.regionsMap.get(id);
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        for (Texture texture : this.textures) {
            texture.dispose();
        }
        this.textures.clear();
    }

    private static String removeExtension(String s) {
        int extensionIndex = s.lastIndexOf(".");
        return extensionIndex == -1 ? s : s.substring(0, extensionIndex);
    }

    private static String removePath(String s) {
        String temp;
        int index = s.lastIndexOf(92);
        if (index != -1) {
            temp = s.substring(index + 1);
        } else {
            temp = s;
        }
        int index2 = temp.lastIndexOf(47);
        if (index2 != -1) {
            return s.substring(index2 + 1);
        }
        return s;
    }

    private static FileHandle getRelativeFileHandle(FileHandle path, String relativePath) {
        if (relativePath.trim().length() != 0) {
            FileHandle child = path;
            StringTokenizer tokenizer = new StringTokenizer(relativePath, "\\/");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.equals("..")) {
                    child = child.parent();
                } else {
                    child = child.child(token);
                }
            }
            return child;
        }
        return path;
    }

    public void flipRegions(boolean x, boolean y) {
        for (TextureRegion region : this.regionsMap.values()) {
            region.flip(x, y);
        }
    }
}
