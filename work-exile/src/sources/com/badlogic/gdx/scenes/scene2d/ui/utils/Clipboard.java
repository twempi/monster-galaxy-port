package com.badlogic.gdx.scenes.scene2d.ui.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Clipboard {
    public abstract String getContents();

    public abstract void setContents(String str);

    public static Clipboard getDefaultClipboard() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            return new AndroidClipboard();
        }
        try {
            return (Clipboard) Class.forName("com.badlogic.gdx.scenes.scene2d.ui.utils.DesktopClipboard").newInstance();
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't instantiate desktop clipboard", e);
        }
    }
}
