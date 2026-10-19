package com.gaiaonline.monstergalaxy.screen;

import com.gaiaonline.monstergalaxy.screen.ScreenGroup;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class GalleryAdapter<T extends ScreenGroup> {
    public abstract void configureItem(T t, int i);

    public abstract int getCount();

    public abstract T newItem();
}
