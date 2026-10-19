package com.badlogic.gdx.assets;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AssetDescriptor {
    final String fileName;
    final AssetLoaderParameters params;
    final Class type;

    public AssetDescriptor(String fileName, Class assetType, AssetLoaderParameters params) {
        this.fileName = fileName.replaceAll("\\\\", "/");
        this.type = assetType;
        this.params = params;
    }

    public String toString() {
        return this.fileName;
    }
}
