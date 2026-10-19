package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: classes.dex */
public class FileTextureData implements TextureData {
    public static boolean copyToPOT;
    final FileHandle file;
    Pixmap.Format format;
    int height;
    boolean isPrepared = false;
    Pixmap pixmap;
    boolean useMipMaps;
    int width;

    public FileTextureData(FileHandle file, Pixmap preloadedPixmap, Pixmap.Format format, boolean useMipMaps) {
        this.width = 0;
        this.height = 0;
        this.file = file;
        this.pixmap = preloadedPixmap;
        this.format = format;
        this.useMipMaps = useMipMaps;
        if (this.pixmap != null) {
            this.pixmap = ensurePot(this.pixmap);
            this.width = this.pixmap.getWidth();
            this.height = this.pixmap.getHeight();
            if (format == null) {
                this.format = this.pixmap.getFormat();
            }
        }
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
        if (this.pixmap == null) {
            if (this.file.extension().equals("cim")) {
                this.pixmap = PixmapIO.readCIM(this.file);
            } else {
                this.pixmap = ensurePot(new Pixmap(this.file));
            }
            this.width = this.pixmap.getWidth();
            this.height = this.pixmap.getHeight();
            if (this.format == null) {
                this.format = this.pixmap.getFormat();
            }
        }
        this.isPrepared = true;
    }

    private Pixmap ensurePot(Pixmap pixmap) {
        if (Gdx.gl20 == null && copyToPOT) {
            int pixmapWidth = pixmap.getWidth();
            int pixmapHeight = pixmap.getHeight();
            int potWidth = MathUtils.nextPowerOfTwo(pixmapWidth);
            int potHeight = MathUtils.nextPowerOfTwo(pixmapHeight);
            if (pixmapWidth != potWidth || pixmapHeight != potHeight) {
                Pixmap tmp = new Pixmap(potWidth, potHeight, pixmap.getFormat());
                tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmapWidth, pixmapHeight);
                pixmap.dispose();
                return tmp;
            }
        }
        return pixmap;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public Pixmap consumePixmap() {
        if (!this.isPrepared) {
            throw new GdxRuntimeException("Call prepare() before calling getPixmap()");
        }
        this.isPrepared = false;
        Pixmap pixmap = this.pixmap;
        this.pixmap = null;
        return pixmap;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean disposePixmap() {
        return true;
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
        return this.format;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean useMipMaps() {
        return this.useMipMaps;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public boolean isManaged() {
        return true;
    }

    public FileHandle getFileHandle() {
        return this.file;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public TextureData.TextureDataType getType() {
        return TextureData.TextureDataType.Pixmap;
    }

    @Override // com.badlogic.gdx.graphics.TextureData
    public void consumeCompressedData() {
        throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
    }
}
