package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextureAtlas implements Disposable {
    private final ArrayList<AtlasRegion> regions;
    private final HashSet<Texture> textures;
    static final String[] tuple = new String[2];
    static final Comparator<TextureAtlasData.Region> indexComparator = new Comparator<TextureAtlasData.Region>() { // from class: com.badlogic.gdx.graphics.g2d.TextureAtlas.1
        @Override // java.util.Comparator
        public int compare(TextureAtlasData.Region region1, TextureAtlasData.Region region2) {
            int i1 = region1.index;
            if (i1 == -1) {
                i1 = Integer.MAX_VALUE;
            }
            int i2 = region2.index;
            if (i2 == -1) {
                i2 = Integer.MAX_VALUE;
            }
            return i1 - i2;
        }
    };

    public static class TextureAtlasData {
        final Array<Page> pages = new Array<>();
        final Array<Region> regions = new Array<>();

        public static class Region {
            public boolean flip;
            public int height;
            public int index;
            public int left;
            public String name;
            public float offsetX;
            public float offsetY;
            public int originalHeight;
            public int originalWidth;
            public Page page;
            public boolean rotate;
            public int top;
            public int width;
        }

        public static class Page {
            public final Pixmap.Format format;
            public final Texture.TextureFilter magFilter;
            public final Texture.TextureFilter minFilter;
            public Texture texture;
            public final FileHandle textureFile;
            public final Texture.TextureWrap uWrap;
            public final boolean useMipMaps;
            public final Texture.TextureWrap vWrap;

            public Page(FileHandle handle, boolean useMipMaps, Pixmap.Format format, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) {
                this.textureFile = handle;
                this.useMipMaps = useMipMaps;
                this.format = format;
                this.minFilter = minFilter;
                this.magFilter = magFilter;
                this.uWrap = uWrap;
                this.vWrap = vWrap;
            }
        }

        public TextureAtlasData(FileHandle packFile, FileHandle imagesDir, boolean flip) {
            Page pageImage;
            PriorityQueue<Region> sortedRegions = new PriorityQueue<>(16, TextureAtlas.indexComparator);
            BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
            Page pageImage2 = null;
            while (true) {
                try {
                    try {
                        String line = reader.readLine();
                        if (line == null) {
                            try {
                                break;
                            } catch (IOException e) {
                            }
                        } else {
                            if (line.trim().length() == 0) {
                                pageImage = null;
                            } else if (pageImage2 == null) {
                                FileHandle file = imagesDir.child(line);
                                Pixmap.Format format = Pixmap.Format.valueOf(TextureAtlas.readValue(reader));
                                TextureAtlas.readTuple(reader);
                                Texture.TextureFilter min = Texture.TextureFilter.valueOf(TextureAtlas.tuple[0]);
                                Texture.TextureFilter max = Texture.TextureFilter.valueOf(TextureAtlas.tuple[1]);
                                String direction = TextureAtlas.readValue(reader);
                                Texture.TextureWrap repeatX = Texture.TextureWrap.ClampToEdge;
                                Texture.TextureWrap repeatY = Texture.TextureWrap.ClampToEdge;
                                if (direction.equals("x")) {
                                    repeatX = Texture.TextureWrap.Repeat;
                                } else if (direction.equals("y")) {
                                    repeatY = Texture.TextureWrap.Repeat;
                                } else if (direction.equals("xy")) {
                                    repeatX = Texture.TextureWrap.Repeat;
                                    repeatY = Texture.TextureWrap.Repeat;
                                }
                                pageImage = new Page(file, min.isMipMap(), format, min, max, repeatX, repeatY);
                                try {
                                    this.pages.add(pageImage);
                                } catch (IOException e2) {
                                    throw new GdxRuntimeException("Error reading pack file: " + packFile);
                                }
                            } else {
                                boolean rotate = Boolean.valueOf(TextureAtlas.readValue(reader)).booleanValue();
                                TextureAtlas.readTuple(reader);
                                int left = Integer.parseInt(TextureAtlas.tuple[0]);
                                int top = Integer.parseInt(TextureAtlas.tuple[1]);
                                TextureAtlas.readTuple(reader);
                                int width = Integer.parseInt(TextureAtlas.tuple[0]);
                                int height = Integer.parseInt(TextureAtlas.tuple[1]);
                                Region region = new Region();
                                region.page = pageImage2;
                                region.left = left;
                                region.top = top;
                                region.width = width;
                                region.height = height;
                                region.name = line;
                                region.rotate = rotate;
                                TextureAtlas.readTuple(reader);
                                region.originalWidth = Integer.parseInt(TextureAtlas.tuple[0]);
                                region.originalHeight = Integer.parseInt(TextureAtlas.tuple[1]);
                                TextureAtlas.readTuple(reader);
                                region.offsetX = Integer.parseInt(TextureAtlas.tuple[0]);
                                region.offsetY = Integer.parseInt(TextureAtlas.tuple[1]);
                                region.index = Integer.parseInt(TextureAtlas.readValue(reader));
                                if (flip) {
                                    region.flip = true;
                                }
                                sortedRegions.add(region);
                                pageImage = pageImage2;
                            }
                            pageImage2 = pageImage;
                        }
                    } catch (IOException e3) {
                    }
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                    }
                    throw th;
                }
            }
            reader.close();
            int n = sortedRegions.size();
            for (int i = 0; i < n; i++) {
                this.regions.add(sortedRegions.poll());
            }
        }

        public Array<Page> getPages() {
            return this.pages;
        }

        public Array<Region> getRegion() {
            return this.regions;
        }
    }

    public TextureAtlas() {
        this.textures = new HashSet<>(4);
        this.regions = new ArrayList<>();
    }

    public TextureAtlas(String internalPackFile) {
        this(Gdx.files.internal(internalPackFile));
    }

    public TextureAtlas(FileHandle packFile) {
        this(packFile, packFile.parent());
    }

    public TextureAtlas(FileHandle packFile, boolean flip) {
        this(packFile, packFile.parent(), flip);
    }

    public TextureAtlas(FileHandle packFile, FileHandle imagesDir) {
        this(packFile, imagesDir, false);
    }

    public TextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip) {
        this(new TextureAtlasData(packFile, imagesDir, flip));
    }

    public TextureAtlas(TextureAtlasData data) {
        this.textures = new HashSet<>(4);
        this.regions = new ArrayList<>();
        load(data);
    }

    private void load(TextureAtlasData data) {
        Texture texture;
        ObjectMap<TextureAtlasData.Page, Texture> pageToTexture = new ObjectMap<>();
        for (TextureAtlasData.Page page : data.pages) {
            if (page.texture == null) {
                texture = new Texture(page.textureFile, page.format, page.useMipMaps);
                texture.setFilter(page.minFilter, page.magFilter);
                texture.setWrap(page.uWrap, page.vWrap);
            } else {
                texture = page.texture;
                texture.setFilter(page.minFilter, page.magFilter);
                texture.setWrap(page.uWrap, page.vWrap);
            }
            this.textures.add(texture);
            pageToTexture.put(page, texture);
        }
        for (TextureAtlasData.Region region : data.regions) {
            AtlasRegion atlasRegion = new AtlasRegion(pageToTexture.get(region.page), region.left, region.top, region.width, region.height);
            atlasRegion.index = region.index;
            atlasRegion.name = region.name;
            atlasRegion.offsetX = region.offsetX;
            atlasRegion.offsetY = region.offsetY;
            atlasRegion.originalHeight = region.originalHeight;
            atlasRegion.originalWidth = region.originalWidth;
            atlasRegion.rotate = region.rotate;
            this.regions.add(atlasRegion);
        }
    }

    public AtlasRegion addRegion(String name, Texture texture, int x, int y, int width, int height) {
        this.textures.add(texture);
        AtlasRegion region = new AtlasRegion(texture, x, y, width, height);
        region.name = name;
        region.originalWidth = width;
        region.originalHeight = height;
        region.index = -1;
        this.regions.add(region);
        return region;
    }

    public AtlasRegion addRegion(String name, TextureRegion textureRegion) {
        return addRegion(name, textureRegion.texture, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public List<AtlasRegion> getRegions() {
        return this.regions;
    }

    public AtlasRegion findRegion(String name) {
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            if (this.regions.get(i).name.equals(name)) {
                return this.regions.get(i);
            }
        }
        return null;
    }

    public AtlasRegion findRegion(String name, int index) {
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name) && region.index == index) {
                return region;
            }
        }
        return null;
    }

    public List<AtlasRegion> findRegions(String name) {
        ArrayList<AtlasRegion> matched = new ArrayList<>();
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name)) {
                matched.add(new AtlasRegion(region));
            }
        }
        return matched;
    }

    public List<Sprite> createSprites() {
        ArrayList sprites = new ArrayList(this.regions.size());
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            sprites.add(newSprite(this.regions.get(i)));
        }
        return sprites;
    }

    public Sprite createSprite(String name) {
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            if (this.regions.get(i).name.equals(name)) {
                return newSprite(this.regions.get(i));
            }
        }
        return null;
    }

    public Sprite createSprite(String name, int index) {
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name) && region.index == index) {
                return newSprite(this.regions.get(i));
            }
        }
        return null;
    }

    public List<Sprite> createSprites(String name) {
        ArrayList<Sprite> matched = new ArrayList<>();
        int n = this.regions.size();
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name)) {
                matched.add(newSprite(region));
            }
        }
        return matched;
    }

    private Sprite newSprite(AtlasRegion region) {
        if (region.packedWidth != region.originalWidth || region.packedHeight != region.originalHeight) {
            return new AtlasSprite(region);
        }
        Sprite sprite = new Sprite(region);
        if (region.rotate) {
            sprite.rotate90(true);
            return sprite;
        }
        return sprite;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        for (Texture texture : this.textures) {
            texture.dispose();
        }
        this.textures.clear();
    }

    static String readValue(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int colon = line.indexOf(58);
        if (colon == -1) {
            throw new GdxRuntimeException("Invalid line: " + line);
        }
        return line.substring(colon + 1).trim();
    }

    static void readTuple(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int colon = line.indexOf(58);
        int comma = line.indexOf(44);
        if (colon == -1 || comma == -1 || comma < colon + 1) {
            throw new GdxRuntimeException("Invalid line: " + line);
        }
        tuple[0] = line.substring(colon + 1, comma).trim();
        tuple[1] = line.substring(comma + 1).trim();
    }

    public static class AtlasRegion extends TextureRegion {
        public int index;
        public String name;
        public float offsetX;
        public float offsetY;
        public int originalHeight;
        public int originalWidth;
        public int packedHeight;
        public int packedWidth;
        public boolean rotate;

        public AtlasRegion(Texture texture, int x, int y, int width, int height) {
            super(texture, x, y, width, height);
            this.packedWidth = width;
            this.packedHeight = height;
        }

        public AtlasRegion(AtlasRegion region) {
            setRegion(region);
            this.index = region.index;
            this.name = region.name;
            this.offsetX = region.offsetX;
            this.offsetY = region.offsetY;
            this.packedWidth = region.packedWidth;
            this.packedHeight = region.packedHeight;
            this.originalWidth = region.originalWidth;
            this.originalHeight = region.originalHeight;
            this.rotate = region.rotate;
            setRegionWidth(this.packedWidth);
            setRegionHeight(this.packedHeight);
        }

        @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
        public void flip(boolean x, boolean y) {
            super.flip(x, y);
            if (x) {
                this.offsetX = (this.originalWidth - this.offsetX) - this.packedWidth;
            }
            if (y) {
                this.offsetY = (this.originalHeight - this.offsetY) - this.packedHeight;
            }
        }
    }

    public static class AtlasSprite extends Sprite {
        final AtlasRegion region;

        public AtlasSprite(AtlasRegion region) {
            this.region = new AtlasRegion(region);
            setRegion(region);
            if (region.rotate) {
                rotate90(true);
            }
            setOrigin(region.originalWidth / 2.0f, region.originalHeight / 2.0f);
            super.setBounds(region.offsetX, region.offsetY, Math.abs(region.getRegionWidth()), Math.abs(region.getRegionHeight()));
            setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setPosition(float x, float y) {
            super.setPosition(this.region.offsetX + x, this.region.offsetY + y);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setBounds(float x, float y, float width, float height) {
            super.setBounds(this.region.offsetX + x, this.region.offsetY + y, width, height);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setOrigin(float originX, float originY) {
            super.setOrigin(this.region.offsetX + originX, this.region.offsetY + originY);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite, com.badlogic.gdx.graphics.g2d.TextureRegion
        public void flip(boolean x, boolean y) {
            super.flip(x, y);
            float oldOffsetX = this.region.offsetX;
            float oldOffsetY = this.region.offsetY;
            this.region.flip(x, y);
            translate(this.region.offsetX - oldOffsetX, this.region.offsetY - oldOffsetY);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getX() {
            return super.getX() - this.region.offsetX;
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getY() {
            return super.getY() - this.region.offsetY;
        }

        public AtlasRegion getAtlasRegion() {
            return this.region;
        }
    }
}
