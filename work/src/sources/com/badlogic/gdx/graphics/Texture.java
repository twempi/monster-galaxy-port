package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ETC1TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Texture implements Disposable {
    private static AssetManager assetManager;
    TextureData data;
    int glHandle;
    TextureFilter magFilter;
    TextureFilter minFilter;
    TextureWrap uWrap;
    TextureWrap vWrap;
    private static boolean enforcePotImages = true;
    static final Map<Application, List<Texture>> managedTextures = new HashMap();
    private static final IntBuffer buffer = BufferUtils.newIntBuffer(1);

    public enum TextureFilter {
        Nearest(9728),
        Linear(9729),
        MipMap(9987),
        MipMapNearestNearest(9984),
        MipMapLinearNearest(9985),
        MipMapNearestLinear(9986),
        MipMapLinearLinear(9987);

        final int glEnum;

        TextureFilter(int glEnum) {
            this.glEnum = glEnum;
        }

        public boolean isMipMap() {
            return (this.glEnum == 9728 || this.glEnum == 9729) ? false : true;
        }

        public int getGLEnum() {
            return this.glEnum;
        }
    }

    public enum TextureWrap {
        ClampToEdge(33071),
        Repeat(10497);

        final int glEnum;

        TextureWrap(int glEnum) {
            this.glEnum = glEnum;
        }

        public int getGLEnum() {
            return this.glEnum;
        }
    }

    public Texture(String internalPath) {
        this(Gdx.files.internal(internalPath));
    }

    public Texture(FileHandle file) {
        this(file, (Pixmap.Format) null, false);
    }

    public Texture(FileHandle file, boolean useMipMaps) {
        this(file, (Pixmap.Format) null, useMipMaps);
    }

    public Texture(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
        this.minFilter = TextureFilter.Nearest;
        this.magFilter = TextureFilter.Nearest;
        this.uWrap = TextureWrap.ClampToEdge;
        this.vWrap = TextureWrap.ClampToEdge;
        if (file.name().contains(".etc1")) {
            create(new ETC1TextureData(file, useMipMaps));
        } else {
            create(new FileTextureData(file, null, format, useMipMaps));
        }
    }

    public Texture(Pixmap pixmap) {
        this(new PixmapTextureData(pixmap, null, false, false));
    }

    public Texture(Pixmap pixmap, boolean useMipMaps) {
        this(new PixmapTextureData(pixmap, null, useMipMaps, false));
    }

    public Texture(Pixmap pixmap, Pixmap.Format format, boolean useMipMaps) {
        this(new PixmapTextureData(pixmap, format, useMipMaps, false));
    }

    public Texture(int width, int height, Pixmap.Format format) {
        this(new PixmapTextureData(new Pixmap(width, height, format), null, false, true));
    }

    public Texture(TextureData data) {
        this.minFilter = TextureFilter.Nearest;
        this.magFilter = TextureFilter.Nearest;
        this.uWrap = TextureWrap.ClampToEdge;
        this.vWrap = TextureWrap.ClampToEdge;
        create(data);
    }

    private void create(TextureData data) {
        this.glHandle = createGLHandle();
        load(data);
        if (data.isManaged()) {
            addManagedTexture(Gdx.app, this);
        }
    }

    public static int createGLHandle() {
        buffer.position(0);
        buffer.limit(buffer.capacity());
        Gdx.gl.glGenTextures(1, buffer);
        return buffer.get(0);
    }

    public void load(TextureData data) {
        if (this.data != null && data.isManaged() != this.data.isManaged()) {
            throw new GdxRuntimeException("New data must have the same managed status as the old data");
        }
        this.data = data;
        if (!data.isPrepared()) {
            data.prepare();
        }
        if (data.getType() == TextureData.TextureDataType.Pixmap) {
            Pixmap pixmap = data.consumePixmap();
            uploadImageData(pixmap);
            if (data.disposePixmap()) {
                pixmap.dispose();
            }
            setFilter(this.minFilter, this.magFilter);
            setWrap(this.uWrap, this.vWrap);
        }
        if (data.getType() == TextureData.TextureDataType.Compressed) {
            Gdx.gl.glBindTexture(3553, this.glHandle);
            data.consumeCompressedData();
            setFilter(this.minFilter, this.magFilter);
            setWrap(this.uWrap, this.vWrap);
        }
        if (data.getType() == TextureData.TextureDataType.Float) {
            Gdx.gl.glBindTexture(3553, this.glHandle);
            data.consumeCompressedData();
            setFilter(this.minFilter, this.magFilter);
            setWrap(this.uWrap, this.vWrap);
        }
        Gdx.gl.glBindTexture(3553, 0);
    }

    private void uploadImageData(Pixmap pixmap) {
        if (enforcePotImages && Gdx.gl20 == null && (!MathUtils.isPowerOfTwo(this.data.getWidth()) || !MathUtils.isPowerOfTwo(this.data.getHeight()))) {
            throw new GdxRuntimeException("Texture width and height must be powers of two: " + this.data.getWidth() + "x" + this.data.getHeight());
        }
        boolean disposePixmap = false;
        if (this.data.getFormat() != pixmap.getFormat()) {
            Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), this.data.getFormat());
            Pixmap.Blending blend = Pixmap.getBlending();
            Pixmap.setBlending(Pixmap.Blending.None);
            tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
            Pixmap.setBlending(blend);
            pixmap = tmp;
            disposePixmap = true;
        }
        Gdx.gl.glBindTexture(3553, this.glHandle);
        Gdx.gl.glPixelStorei(3317, 1);
        if (this.data.useMipMaps()) {
            MipMapGenerator.generateMipMap(pixmap, pixmap.getWidth(), pixmap.getHeight(), disposePixmap);
            return;
        }
        Gdx.gl.glTexImage2D(3553, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
        if (disposePixmap) {
            pixmap.dispose();
        }
    }

    private void reload() {
        if (!this.data.isManaged()) {
            throw new GdxRuntimeException("Tried to reload unmanaged Texture");
        }
        this.glHandle = createGLHandle();
        load(this.data);
    }

    public void bind() {
        Gdx.gl.glBindTexture(3553, this.glHandle);
    }

    public void bind(int unit) {
        Gdx.gl.glActiveTexture(33984 + unit);
        Gdx.gl.glBindTexture(3553, this.glHandle);
    }

    public void draw(Pixmap pixmap, int x, int y) {
        if (this.data.isManaged()) {
            throw new GdxRuntimeException("can't draw to a managed texture");
        }
        Gdx.gl.glBindTexture(3553, this.glHandle);
        Gdx.gl.glTexSubImage2D(3553, 0, x, y, pixmap.getWidth(), pixmap.getHeight(), pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
    }

    public int getWidth() {
        return this.data.getWidth();
    }

    public int getHeight() {
        return this.data.getHeight();
    }

    public TextureFilter getMinFilter() {
        return this.minFilter;
    }

    public TextureFilter getMagFilter() {
        return this.magFilter;
    }

    public TextureWrap getUWrap() {
        return this.uWrap;
    }

    public TextureWrap getVWrap() {
        return this.vWrap;
    }

    public TextureData getTextureData() {
        return this.data;
    }

    public boolean isManaged() {
        return this.data.isManaged();
    }

    public int getTextureObjectHandle() {
        return this.glHandle;
    }

    public void setWrap(TextureWrap u, TextureWrap v) {
        this.uWrap = u;
        this.vWrap = v;
        bind();
        Gdx.gl.glTexParameterf(3553, 10242, u.getGLEnum());
        Gdx.gl.glTexParameterf(3553, 10243, v.getGLEnum());
    }

    public void setFilter(TextureFilter minFilter, TextureFilter magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        bind();
        Gdx.gl.glTexParameterf(3553, 10241, minFilter.getGLEnum());
        Gdx.gl.glTexParameterf(3553, 10240, magFilter.getGLEnum());
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        if (this.glHandle != 0) {
            buffer.put(0, this.glHandle);
            Gdx.gl.glDeleteTextures(1, buffer);
            if (this.data.isManaged() && managedTextures.get(Gdx.app) != null) {
                managedTextures.get(Gdx.app).remove(this);
            }
            this.glHandle = 0;
        }
    }

    public static void setEnforcePotImages(boolean enforcePotImages2) {
        enforcePotImages = enforcePotImages2;
    }

    private static void addManagedTexture(Application app, Texture texture) {
        List<Texture> managedTexureList = managedTextures.get(app);
        if (managedTexureList == null) {
            managedTexureList = new ArrayList<>();
        }
        managedTexureList.add(texture);
        managedTextures.put(app, managedTexureList);
    }

    public static void clearAllTextures(Application app) {
        managedTextures.remove(app);
    }

    public static void invalidateAllTextures(Application app) {
        List<Texture> managedTexureList = managedTextures.get(app);
        if (managedTexureList != null) {
            if (assetManager == null) {
                for (int i = 0; i < managedTexureList.size(); i++) {
                    managedTexureList.get(i).reload();
                }
                return;
            }
            assetManager.finishLoading();
            List<Texture> textures = new ArrayList<>(managedTexureList);
            for (Texture texture : textures) {
                String fileName = assetManager.getAssetFileName(texture);
                if (fileName == null) {
                    texture.reload();
                } else {
                    final int refCount = assetManager.getReferenceCount(fileName);
                    assetManager.setReferenceCount(fileName, 0);
                    texture.glHandle = 0;
                    TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
                    params.textureData = texture.getTextureData();
                    params.minFilter = texture.getMinFilter();
                    params.magFilter = texture.getMagFilter();
                    params.wrapU = texture.getUWrap();
                    params.wrapV = texture.getVWrap();
                    params.genMipMaps = texture.data.useMipMaps();
                    params.texture = texture;
                    params.loadedCallback = new AssetLoaderParameters.LoadedCallback() { // from class: com.badlogic.gdx.graphics.Texture.1
                        @Override // com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback
                        public void finishedLoading(AssetManager assetManager2, String fileName2, Class type) {
                            assetManager2.setReferenceCount(fileName2, refCount);
                        }
                    };
                    assetManager.unload(fileName);
                    texture.glHandle = createGLHandle();
                    assetManager.load(fileName, Texture.class, params);
                }
            }
            managedTexureList.clear();
            managedTexureList.addAll(textures);
        }
    }

    public static void setAssetManager(AssetManager manager) {
        assetManager = manager;
    }

    public static String getManagedStatus() {
        StringBuilder builder = new StringBuilder();
        builder.append("Managed textures/app: { ");
        for (Application app : managedTextures.keySet()) {
            builder.append(managedTextures.get(app).size());
            builder.append(" ");
        }
        builder.append("}");
        return builder.toString();
    }

    public static int getNumManagedTextures() {
        return managedTextures.get(Gdx.app).size();
    }
}
