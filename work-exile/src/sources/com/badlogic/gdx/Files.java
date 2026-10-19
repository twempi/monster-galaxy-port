package com.badlogic.gdx;

import com.badlogic.gdx.files.FileHandle;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Files {

    public enum FileType {
        Classpath,
        Internal,
        External,
        Absolute
    }

    FileHandle absolute(String str);

    FileHandle classpath(String str);

    FileHandle external(String str);

    String getExternalStoragePath();

    FileHandle getFileHandle(String str, FileType fileType);

    FileHandle internal(String str);

    boolean isExternalStorageAvailable();
}
