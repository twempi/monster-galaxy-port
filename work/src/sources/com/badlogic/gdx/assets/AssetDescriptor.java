package com.badlogic.gdx.assets;

/* JADX INFO: loaded from: classes.dex */
public class AssetDescriptor<T> {
    public final String fileName;
    public final AssetLoaderParameters params;
    public final Class<T> type;

    public AssetDescriptor(String fileName, Class<T> assetType) {
        this(fileName, assetType, null);
    }

    public AssetDescriptor(String fileName, Class<T> assetType, AssetLoaderParameters<T> params) {
        this.fileName = fileName.replaceAll("\\\\", "/");
        this.type = assetType;
        this.params = params;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.fileName);
        buffer.append(", ");
        buffer.append(this.type.getName());
        return buffer.toString();
    }
}
