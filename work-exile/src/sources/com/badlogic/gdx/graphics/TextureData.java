package com.badlogic.gdx.graphics;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface TextureData {

    public enum TextureDataType {
        Pixmap,
        Compressed
    }

    void consumeCompressedData();

    Pixmap consumePixmap();

    boolean disposePixmap();

    Pixmap.Format getFormat();

    int getHeight();

    TextureDataType getType();

    int getWidth();

    boolean isManaged();

    boolean isPrepared();

    void prepare();

    boolean useMipMaps();
}
