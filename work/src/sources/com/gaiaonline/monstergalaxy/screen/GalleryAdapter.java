package com.gaiaonline.monstergalaxy.screen;

import com.gaiaonline.monstergalaxy.screen.ScreenGroup;

/* JADX INFO: loaded from: classes.dex */
public abstract class GalleryAdapter<T extends ScreenGroup> {
    public abstract void configureItem(T t, int i);

    public abstract int getCount();

    public abstract T newItem();
}
