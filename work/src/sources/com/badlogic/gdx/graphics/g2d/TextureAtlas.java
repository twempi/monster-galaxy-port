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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class TextureAtlas implements Disposable {
    private final Array<AtlasRegion> regions;
    private final HashSet<Texture> textures;
    static final String[] tuple = new String[4];
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
            public int[] pads;
            public Page page;
            public boolean rotate;
            public int[] splits;
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
                                } catch (Exception e2) {
                                    ex = e2;
                                    throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
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
                                if (TextureAtlas.readTuple(reader) == 4) {
                                    region.splits = new int[]{Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3])};
                                    if (TextureAtlas.readTuple(reader) == 4) {
                                        region.pads = new int[]{Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3])};
                                        TextureAtlas.readTuple(reader);
                                    }
                                }
                                region.originalWidth = Integer.parseInt(TextureAtlas.tuple[0]);
                                region.originalHeight = Integer.parseInt(TextureAtlas.tuple[1]);
                                TextureAtlas.readTuple(reader);
                                region.offsetX = Integer.parseInt(TextureAtlas.tuple[0]);
                                region.offsetY = Integer.parseInt(TextureAtlas.tuple[1]);
                                region.index = Integer.parseInt(TextureAtlas.readValue(reader));
                                if (flip) {
                                    region.flip = true;
                                }
                                this.regions.add(region);
                                pageImage = pageImage2;
                            }
                            pageImage2 = pageImage;
                        }
                    } catch (Exception e3) {
                        ex = e3;
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
            this.regions.sort(TextureAtlas.indexComparator);
        }

        public Array<Page> getPages() {
            return this.pages;
        }

        public Array<Region> getRegions() {
            return this.regions;
        }
    }

    public TextureAtlas() {
        this.textures = new HashSet<>(4);
        this.regions = new Array<>();
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
        this.regions = new Array<>();
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
            int width = region.width;
            int height = region.height;
            AtlasRegion atlasRegion = new AtlasRegion(pageToTexture.get(region.page), region.left, region.top, region.rotate ? height : width, region.rotate ? width : height);
            atlasRegion.index = region.index;
            atlasRegion.name = region.name;
            atlasRegion.offsetX = region.offsetX;
            atlasRegion.offsetY = region.offsetY;
            atlasRegion.originalHeight = region.originalHeight;
            atlasRegion.originalWidth = region.originalWidth;
            atlasRegion.rotate = region.rotate;
            atlasRegion.splits = region.splits;
            atlasRegion.pads = region.pads;
            if (region.flip) {
                atlasRegion.flip(false, true);
            }
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

    public Array<AtlasRegion> getRegions() {
        return this.regions;
    }

    public AtlasRegion findRegion(String name) {
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            if (this.regions.get(i).name.equals(name)) {
                return this.regions.get(i);
            }
        }
        return null;
    }

    public AtlasRegion findRegion(String name, int index) {
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name) && region.index == index) {
                return region;
            }
        }
        return null;
    }

    public Array<AtlasRegion> findRegions(String name) {
        Array<AtlasRegion> matched = new Array<>();
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name)) {
                matched.add(new AtlasRegion(region));
            }
        }
        return matched;
    }

    public Array<Sprite> createSprites() {
        Array<Sprite> array = new Array<>(this.regions.size);
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            array.add(newSprite(this.regions.get(i)));
        }
        return array;
    }

    public Sprite createSprite(String name) {
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            if (this.regions.get(i).name.equals(name)) {
                return newSprite(this.regions.get(i));
            }
        }
        return null;
    }

    public Sprite createSprite(String name, int index) {
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name) && region.index == index) {
                return newSprite(this.regions.get(i));
            }
        }
        return null;
    }

    public Array<Sprite> createSprites(String name) {
        Array<Sprite> matched = new Array<>();
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name)) {
                matched.add(newSprite(region));
            }
        }
        return matched;
    }

    private Sprite newSprite(AtlasRegion region) {
        if (region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
            if (region.rotate) {
                Sprite sprite = new Sprite(region);
                sprite.setBounds(0.0f, 0.0f, region.getRegionHeight(), region.getRegionWidth());
                sprite.rotate90(true);
                return sprite;
            }
            Sprite sprite2 = new Sprite(region);
            return sprite2;
        }
        Sprite sprite3 = new AtlasSprite(region);
        return sprite3;
    }

    public NinePatch createPatch(String name) {
        int n = this.regions.size;
        for (int i = 0; i < n; i++) {
            AtlasRegion region = this.regions.get(i);
            if (region.name.equals(name)) {
                int[] splits = region.splits;
                if (splits == null) {
                    throw new IllegalArgumentException("Region does not have ninepatch splits: " + name);
                }
                NinePatch patch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
                if (region.pads != null) {
                    patch.setPadding(region.pads[0], region.pads[1], region.pads[2], region.pads[3]);
                    return patch;
                }
                return patch;
            }
        }
        return null;
    }

    public Set<Texture> getTextures() {
        return this.textures;
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

    static int readTuple(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int colon = line.indexOf(58);
        if (colon == -1) {
            throw new GdxRuntimeException("Invalid line: " + line);
        }
        int lastMatch = colon + 1;
        int i = 0;
        while (i < 3) {
            int comma = line.indexOf(44, lastMatch);
            if (comma == -1) {
                if (i != 0) {
                    break;
                }
                throw new GdxRuntimeException("Invalid line: " + line);
            }
            tuple[i] = line.substring(lastMatch, comma).trim();
            lastMatch = comma + 1;
            i++;
        }
        tuple[i] = line.substring(lastMatch).trim();
        return i + 1;
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
        public int[] pads;
        public boolean rotate;
        public int[] splits;

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
            this.splits = region.splits;
        }

        @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
        public void flip(boolean x, boolean y) {
            super.flip(x, y);
            if (x) {
                this.offsetX = (this.originalWidth - this.offsetX) - getRotatedPackedWidth();
            }
            if (y) {
                this.offsetY = (this.originalHeight - this.offsetY) - getRotatedPackedHeight();
            }
        }

        public float getRotatedPackedWidth() {
            return this.rotate ? this.packedHeight : this.packedWidth;
        }

        public float getRotatedPackedHeight() {
            return this.rotate ? this.packedWidth : this.packedHeight;
        }
    }

    public static class AtlasSprite extends Sprite {
        float originalOffsetX;
        float originalOffsetY;
        final AtlasRegion region;

        public AtlasSprite(AtlasRegion region) {
            this.region = new AtlasRegion(region);
            this.originalOffsetX = region.offsetX;
            this.originalOffsetY = region.offsetY;
            setRegion(region);
            setOrigin(region.originalWidth / 2.0f, region.originalHeight / 2.0f);
            int width = region.getRegionWidth();
            int height = region.getRegionHeight();
            if (region.rotate) {
                super.rotate90(true);
                super.setBounds(region.offsetX, region.offsetY, height, width);
            } else {
                super.setBounds(region.offsetX, region.offsetY, width, height);
            }
            setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        public AtlasSprite(AtlasSprite sprite) {
            this.region = sprite.region;
            this.originalOffsetX = sprite.originalOffsetX;
            this.originalOffsetY = sprite.originalOffsetY;
            set(sprite);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setPosition(float x, float y) {
            super.setPosition(this.region.offsetX + x, this.region.offsetY + y);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setBounds(float x, float y, float width, float height) {
            float widthRatio = width / this.region.originalWidth;
            float heightRatio = height / this.region.originalHeight;
            this.region.offsetX = this.originalOffsetX * widthRatio;
            this.region.offsetY = this.originalOffsetY * heightRatio;
            int packedWidth = this.region.rotate ? this.region.packedHeight : this.region.packedWidth;
            int packedHeight = this.region.rotate ? this.region.packedWidth : this.region.packedHeight;
            super.setBounds(this.region.offsetX + x, this.region.offsetY + y, packedWidth * widthRatio, packedHeight * heightRatio);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setSize(float width, float height) {
            setBounds(getX(), getY(), width, height);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void setOrigin(float originX, float originY) {
            super.setOrigin(originX - this.region.offsetX, originY - this.region.offsetY);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite, com.badlogic.gdx.graphics.g2d.TextureRegion
        public void flip(boolean x, boolean y) {
            super.flip(x, y);
            float oldOriginX = getOriginX();
            float oldOriginY = getOriginY();
            float oldOffsetX = this.region.offsetX;
            float oldOffsetY = this.region.offsetY;
            float widthRatio = getWidthRatio();
            float heightRatio = getHeightRatio();
            this.region.offsetX = this.originalOffsetX;
            this.region.offsetY = this.originalOffsetY;
            this.region.flip(x, y);
            this.originalOffsetX = this.region.offsetX;
            this.originalOffsetY = this.region.offsetY;
            this.region.offsetX *= widthRatio;
            this.region.offsetY *= heightRatio;
            translate(this.region.offsetX - oldOffsetX, this.region.offsetY - oldOffsetY);
            setOrigin(oldOriginX, oldOriginY);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public void rotate90(boolean clockwise) {
            super.rotate90(clockwise);
            float oldOriginX = getOriginX();
            float oldOriginY = getOriginY();
            float oldOffsetX = this.region.offsetX;
            float oldOffsetY = this.region.offsetY;
            float widthRatio = getWidthRatio();
            float heightRatio = getHeightRatio();
            if (clockwise) {
                this.region.offsetX = oldOffsetY;
                this.region.offsetY = ((this.region.originalHeight * heightRatio) - oldOffsetX) - (this.region.packedWidth * widthRatio);
            } else {
                this.region.offsetX = ((this.region.originalWidth * widthRatio) - oldOffsetY) - (this.region.packedHeight * heightRatio);
                this.region.offsetY = oldOffsetX;
            }
            translate(this.region.offsetX - oldOffsetX, this.region.offsetY - oldOffsetY);
            setOrigin(oldOriginX, oldOriginY);
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getX() {
            return super.getX() - this.region.offsetX;
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getY() {
            return super.getY() - this.region.offsetY;
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getOriginX() {
            return super.getOriginX() + this.region.offsetX;
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getOriginY() {
            return super.getOriginY() + this.region.offsetY;
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getWidth() {
            return (super.getWidth() / this.region.getRotatedPackedWidth()) * this.region.originalWidth;
        }

        @Override // com.badlogic.gdx.graphics.g2d.Sprite
        public float getHeight() {
            return (super.getHeight() / this.region.getRotatedPackedHeight()) * this.region.originalHeight;
        }

        public float getWidthRatio() {
            return super.getWidth() / this.region.getRotatedPackedWidth();
        }

        public float getHeightRatio() {
            return super.getHeight() / this.region.getRotatedPackedHeight();
        }

        public AtlasRegion getAtlasRegion() {
            return this.region;
        }
    }
}
