package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SerializationException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class Skin implements Disposable {
    TextureAtlas atlas;
    ObjectMap<Class, ObjectMap<String, Object>> resources = new ObjectMap<>();

    public static class TintedDrawable {
        public Color color;
        public String name;
    }

    public Skin() {
    }

    public Skin(FileHandle skinFile) {
        FileHandle atlasFile = skinFile.sibling(skinFile.nameWithoutExtension() + ".atlas");
        if (atlasFile.exists()) {
            this.atlas = new TextureAtlas(atlasFile);
            addRegions(this.atlas);
        }
        load(skinFile);
    }

    public Skin(FileHandle skinFile, TextureAtlas atlas) {
        this.atlas = atlas;
        addRegions(atlas);
        load(skinFile);
    }

    public Skin(TextureAtlas atlas) {
        this.atlas = atlas;
        addRegions(atlas);
    }

    public void load(FileHandle skinFile) {
        try {
            getJsonLoader(skinFile).fromJson(Skin.class, skinFile);
        } catch (SerializationException ex) {
            throw new SerializationException("Error reading file: " + skinFile, ex);
        }
    }

    public void addRegions(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        int n = regions.size;
        for (int i = 0; i < n; i++) {
            TextureAtlas.AtlasRegion region = regions.get(i);
            add(region.name, region, TextureRegion.class);
        }
    }

    public void add(String name, Object resource) {
        add(name, resource, resource.getClass());
    }

    public void add(String name, Object resource, Class type) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("resource cannot be null.");
        }
        ObjectMap<String, Object> typeResources = this.resources.get(type);
        if (typeResources == null) {
            typeResources = new ObjectMap<>();
            this.resources.put(type, typeResources);
        }
        typeResources.put(name, resource);
    }

    public <T> T get(Class<T> cls) {
        return (T) get("default", cls);
    }

    public <T> T get(String str, Class<T> cls) {
        if (str == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        if (cls == Drawable.class) {
            return (T) getDrawable(str);
        }
        if (cls == TextureRegion.class) {
            return (T) getRegion(str);
        }
        if (cls == NinePatch.class) {
            return (T) getPatch(str);
        }
        if (cls == Sprite.class) {
            return (T) getSprite(str);
        }
        ObjectMap<String, Object> objectMap = this.resources.get(cls);
        if (objectMap == null) {
            throw new GdxRuntimeException("No " + cls.getName() + " registered with name: " + str);
        }
        T t = (T) objectMap.get(str);
        if (t == null) {
            throw new GdxRuntimeException("No " + cls.getName() + " registered with name: " + str);
        }
        return t;
    }

    public <T> T optional(String str, Class<T> cls) {
        if (str == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        ObjectMap<String, Object> objectMap = this.resources.get(cls);
        if (objectMap == null) {
            return null;
        }
        return (T) objectMap.get(str);
    }

    public boolean has(String name, Class type) {
        ObjectMap<String, Object> typeResources = this.resources.get(type);
        if (typeResources == null) {
            return false;
        }
        return typeResources.containsKey(name);
    }

    public <T> ObjectMap<String, T> getAll(Class<T> type) {
        return (ObjectMap) this.resources.get(type);
    }

    public Color getColor(String name) {
        return (Color) get(name, Color.class);
    }

    public BitmapFont getFont(String name) {
        return (BitmapFont) get(name, BitmapFont.class);
    }

    public TextureRegion getRegion(String name) {
        TextureRegion region = (TextureRegion) optional(name, TextureRegion.class);
        if (region != null) {
            return region;
        }
        Texture texture = (Texture) optional(name, Texture.class);
        if (texture == null) {
            throw new GdxRuntimeException("No TextureRegion or Texture registered with name: " + name);
        }
        TextureRegion region2 = new TextureRegion(texture);
        add(name, region2, Texture.class);
        return region2;
    }

    public TiledDrawable getTiledDrawable(String name) {
        TiledDrawable tiled = (TiledDrawable) optional(name, TiledDrawable.class);
        if (tiled != null) {
            return tiled;
        }
        Drawable drawable = (Drawable) optional(name, Drawable.class);
        if (tiled != null) {
            if (drawable instanceof TiledDrawable) {
                return tiled;
            }
            throw new GdxRuntimeException("Drawable found but is not a TiledDrawable: " + name + ", " + drawable.getClass().getName());
        }
        TiledDrawable tiled2 = new TiledDrawable(getRegion(name));
        add(name, tiled2, TiledDrawable.class);
        return tiled2;
    }

    public NinePatch getPatch(String name) {
        int[] splits;
        NinePatch patch = (NinePatch) optional(name, NinePatch.class);
        if (patch == null) {
            try {
                TextureRegion region = getRegion(name);
                if ((region instanceof TextureAtlas.AtlasRegion) && (splits = ((TextureAtlas.AtlasRegion) region).splits) != null) {
                    NinePatch patch2 = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
                    try {
                        int[] pads = ((TextureAtlas.AtlasRegion) region).pads;
                        if (pads != null) {
                            patch2.setPadding(pads[0], pads[1], pads[2], pads[3]);
                        }
                        patch = patch2;
                    } catch (GdxRuntimeException e) {
                        throw new GdxRuntimeException("No NinePatch, TextureRegion, or Texture registered with name: " + name);
                    }
                }
                NinePatch patch3 = patch == null ? new NinePatch(region) : patch;
                add(name, patch3, NinePatch.class);
                return patch3;
            } catch (GdxRuntimeException e2) {
            }
        } else {
            return patch;
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x005a  */
    public Sprite getSprite(String name) {
        Sprite sprite;
        Sprite sprite2;
        Sprite sprite3 = (Sprite) optional(name, Sprite.class);
        if (sprite3 != null) {
            return sprite3;
        }
        try {
            TextureRegion textureRegion = getRegion(name);
            if (textureRegion instanceof TextureAtlas.AtlasRegion) {
                TextureAtlas.AtlasRegion region = (TextureAtlas.AtlasRegion) textureRegion;
                if (!region.rotate && region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
                    sprite = sprite3;
                } else {
                    sprite = new TextureAtlas.AtlasSprite(region);
                }
            } else {
                sprite = sprite3;
            }
            if (sprite == null) {
                try {
                    sprite2 = new Sprite(textureRegion);
                } catch (GdxRuntimeException e) {
                    throw new GdxRuntimeException("No NinePatch, TextureRegion, or Texture registered with name: " + name);
                }
            } else {
                sprite2 = sprite;
            }
            add(name, sprite2, NinePatch.class);
            return sprite2;
        } catch (GdxRuntimeException e2) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:39:0x009a  */
    public Drawable getDrawable(String name) {
        Drawable drawable;
        Drawable drawable2 = (Drawable) optional(name, Drawable.class);
        if (drawable2 != null) {
            return drawable2;
        }
        Drawable drawable3 = (Drawable) optional(name, TiledDrawable.class);
        if (drawable3 != null) {
            return drawable3;
        }
        try {
            TextureRegion textureRegion = getRegion(name);
            if (textureRegion instanceof TextureAtlas.AtlasRegion) {
                TextureAtlas.AtlasRegion region = (TextureAtlas.AtlasRegion) textureRegion;
                if (region.splits != null) {
                    drawable = new NinePatchDrawable(getPatch(name));
                } else if (!region.rotate && region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
                    drawable = drawable3;
                } else {
                    drawable = new SpriteDrawable(getSprite(name));
                }
            } else {
                drawable = drawable3;
            }
            if (drawable == null) {
                try {
                    drawable3 = new TextureRegionDrawable(textureRegion);
                } catch (GdxRuntimeException e) {
                    drawable3 = drawable;
                }
            } else {
                drawable3 = drawable;
            }
        } catch (GdxRuntimeException e2) {
        }
        if (drawable3 == null) {
            NinePatch patch = (NinePatch) optional(name, NinePatch.class);
            if (patch != null) {
                drawable3 = new NinePatchDrawable(patch);
            } else {
                Sprite sprite = (Sprite) optional(name, Sprite.class);
                if (sprite != null) {
                    drawable3 = new SpriteDrawable(sprite);
                } else {
                    throw new GdxRuntimeException("No Drawable, NinePatch, TextureRegion, Texture, or Sprite registered with name: " + name);
                }
            }
        }
        add(name, drawable3, Drawable.class);
        return drawable3;
    }

    public String find(Object resource) {
        if (resource == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        ObjectMap<String, Object> typeResources = this.resources.get(resource.getClass());
        if (typeResources == null) {
            return null;
        }
        return typeResources.findKey(resource, true);
    }

    public Drawable newDrawable(String name) {
        return newDrawable(getDrawable(name));
    }

    public Drawable newDrawable(String name, float r, float g, float b, float a) {
        return newDrawable(getDrawable(name), new Color(r, g, b, a));
    }

    public Drawable newDrawable(String name, Color tint) {
        return newDrawable(getDrawable(name), tint);
    }

    public Drawable newDrawable(Drawable drawable) {
        if (drawable instanceof TextureRegionDrawable) {
            return new TextureRegionDrawable((TextureRegionDrawable) drawable);
        }
        if (drawable instanceof NinePatchDrawable) {
            return new NinePatchDrawable((NinePatchDrawable) drawable);
        }
        if (drawable instanceof SpriteDrawable) {
            return new SpriteDrawable((SpriteDrawable) drawable);
        }
        throw new GdxRuntimeException("Unable to copy, unknown drawable type: " + drawable.getClass());
    }

    public Drawable newDrawable(Drawable drawable, float r, float g, float b, float a) {
        return newDrawable(drawable, new Color(r, g, b, a));
    }

    public Drawable newDrawable(Drawable drawable, Color tint) {
        Sprite sprite;
        Sprite sprite2;
        if (drawable instanceof TextureRegionDrawable) {
            TextureRegion region = ((TextureRegionDrawable) drawable).getRegion();
            if (region instanceof TextureAtlas.AtlasRegion) {
                sprite2 = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion) region);
            } else {
                sprite2 = new Sprite(region);
            }
            sprite2.setColor(tint);
            return new SpriteDrawable(sprite2);
        }
        if (drawable instanceof NinePatchDrawable) {
            NinePatchDrawable patchDrawable = new NinePatchDrawable((NinePatchDrawable) drawable);
            patchDrawable.setPatch(new NinePatch(patchDrawable.getPatch(), tint));
            return patchDrawable;
        }
        if (drawable instanceof SpriteDrawable) {
            SpriteDrawable spriteDrawable = new SpriteDrawable((SpriteDrawable) drawable);
            Sprite sprite3 = spriteDrawable.getSprite();
            if (sprite3 instanceof TextureAtlas.AtlasSprite) {
                sprite = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasSprite) sprite3);
            } else {
                sprite = new Sprite(sprite3);
            }
            sprite.setColor(tint);
            spriteDrawable.setSprite(sprite);
            return spriteDrawable;
        }
        throw new GdxRuntimeException("Unable to copy, unknown drawable type: " + drawable.getClass());
    }

    public void setEnabled(Actor actor, boolean enabled) {
        Method method = findMethod(actor.getClass(), "getStyle");
        if (method != null) {
            try {
                Object style = method.invoke(actor, new Object[0]);
                String name = find(style);
                if (name != null) {
                    Object style2 = get(name.replace("-disabled", "") + (enabled ? "" : "-disabled"), style.getClass());
                    Method method2 = findMethod(actor.getClass(), "setStyle");
                    if (method2 != null) {
                        try {
                            method2.invoke(actor, style2);
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (Exception e2) {
            }
        }
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        if (this.atlas != null) {
            this.atlas.dispose();
        }
        for (ObjectMap<String, Object> entry : this.resources.values()) {
            for (Object resource : entry.values()) {
                if (resource instanceof Disposable) {
                    ((Disposable) resource).dispose();
                }
            }
        }
    }

    protected Json getJsonLoader(final FileHandle skinFile) {
        Json json = new Json() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Skin.1
            @Override // com.badlogic.gdx.utils.Json
            public <T> T readValue(Class<T> cls, Class cls2, Object obj) {
                return (!(obj instanceof String) || CharSequence.class.isAssignableFrom(cls)) ? (T) super.readValue(cls, cls2, obj) : (T) Skin.this.get((String) obj, cls);
            }
        };
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setSerializer(Skin.class, new Json.ReadOnlySerializer<Skin>() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Skin.2
            @Override // com.badlogic.gdx.utils.Json.ReadOnlySerializer, com.badlogic.gdx.utils.Json.Serializer
            public Skin read(Json json2, Object jsonData, Class ignored) {
                ObjectMap<String, ObjectMap> typeToValueMap = (ObjectMap) jsonData;
                for (ObjectMap.Entry<String, ObjectMap> typeEntry : typeToValueMap.entries()) {
                    String className = typeEntry.key;
                    ObjectMap valueMap = typeEntry.value;
                    try {
                        readNamedObjects(json2, Class.forName(className), valueMap);
                    } catch (ClassNotFoundException ex) {
                        throw new SerializationException(ex);
                    }
                }
                return this;
            }

            private void readNamedObjects(Json json2, Class type, ObjectMap<String, ObjectMap> valueMap) {
                Class addType = type == TintedDrawable.class ? Drawable.class : type;
                for (ObjectMap.Entry<String, ObjectMap> valueEntry : valueMap.entries()) {
                    String name = valueEntry.key;
                    Object object = json2.readValue(type, valueEntry.value);
                    if (object != null) {
                        try {
                            Skin.this.add(name, object, addType);
                        } catch (Exception ex) {
                            throw new SerializationException("Error reading " + type.getSimpleName() + ": " + valueEntry.key, ex);
                        }
                    }
                }
            }
        });
        json.setSerializer(BitmapFont.class, new Json.ReadOnlySerializer<BitmapFont>() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Skin.3
            @Override // com.badlogic.gdx.utils.Json.ReadOnlySerializer, com.badlogic.gdx.utils.Json.Serializer
            public BitmapFont read(Json json2, Object jsonData, Class type) {
                String path = (String) json2.readValue("file", String.class, jsonData);
                FileHandle fontFile = skinFile.parent().child(path);
                if (!fontFile.exists()) {
                    fontFile = Gdx.files.internal(path);
                }
                if (!fontFile.exists()) {
                    throw new SerializationException("Font file not found: " + fontFile);
                }
                String regionName = fontFile.nameWithoutExtension();
                try {
                    TextureRegion region = (TextureRegion) this.optional(regionName, TextureRegion.class);
                    if (region != null) {
                        return new BitmapFont(fontFile, region, false);
                    }
                    FileHandle imageFile = fontFile.parent().child(regionName + ".png");
                    if (imageFile.exists()) {
                        return new BitmapFont(fontFile, imageFile, false);
                    }
                    return new BitmapFont(fontFile, false);
                } catch (RuntimeException ex) {
                    throw new SerializationException("Error loading bitmap font: " + fontFile, ex);
                }
            }
        });
        json.setSerializer(Color.class, new Json.ReadOnlySerializer<Color>() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Skin.4
            @Override // com.badlogic.gdx.utils.Json.ReadOnlySerializer, com.badlogic.gdx.utils.Json.Serializer
            public Color read(Json json2, Object jsonData, Class type) {
                if (jsonData instanceof String) {
                    return (Color) Skin.this.get((String) jsonData, Color.class);
                }
                float r = ((Float) json2.readValue("r", (Class<Float>) Float.TYPE, Float.valueOf(0.0f), jsonData)).floatValue();
                float g = ((Float) json2.readValue("g", (Class<Float>) Float.TYPE, Float.valueOf(0.0f), jsonData)).floatValue();
                float b = ((Float) json2.readValue("b", (Class<Float>) Float.TYPE, Float.valueOf(0.0f), jsonData)).floatValue();
                float a = ((Float) json2.readValue("a", (Class<Float>) Float.TYPE, Float.valueOf(1.0f), jsonData)).floatValue();
                return new Color(r, g, b, a);
            }
        });
        json.setSerializer(TintedDrawable.class, new Json.ReadOnlySerializer() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Skin.5
            @Override // com.badlogic.gdx.utils.Json.ReadOnlySerializer, com.badlogic.gdx.utils.Json.Serializer
            public Object read(Json json2, Object jsonData, Class type) {
                String name = (String) json2.readValue("name", String.class, jsonData);
                Color color = (Color) json2.readValue("color", Color.class, jsonData);
                return Skin.this.newDrawable(name, color);
            }
        });
        return json;
    }

    private static Method findMethod(Class type, String name) {
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
