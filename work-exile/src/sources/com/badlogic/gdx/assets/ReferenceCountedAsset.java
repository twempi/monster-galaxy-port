package com.badlogic.gdx.assets;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ReferenceCountedAsset {
    int getRefCount();

    void incRefCount();

    void setRefCount(int i);
}
