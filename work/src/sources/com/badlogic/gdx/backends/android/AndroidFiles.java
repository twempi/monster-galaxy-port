package com.badlogic.gdx.backends.android;

import android.content.res.AssetManager;
import android.os.Environment;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;

/* JADX INFO: loaded from: classes.dex */
public class AndroidFiles implements Files {
    protected final AssetManager assets;
    protected final String localpath;
    protected final String sdcard;

    public AndroidFiles(AssetManager assets) {
        this.sdcard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        this.assets = assets;
        this.localpath = this.sdcard;
    }

    public AndroidFiles(AssetManager assets, String localpath) {
        this.sdcard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        this.assets = assets;
        this.localpath = localpath.endsWith("/") ? localpath : localpath + "/";
    }

    @Override // com.badlogic.gdx.Files
    public FileHandle getFileHandle(String path, Files.FileType type) {
        return new AndroidFileHandle(type == Files.FileType.Internal ? this.assets : null, path, type);
    }

    @Override // com.badlogic.gdx.Files
    public FileHandle classpath(String path) {
        return new AndroidFileHandle((AssetManager) null, path, Files.FileType.Classpath);
    }

    @Override // com.badlogic.gdx.Files
    public FileHandle internal(String path) {
        return new AndroidFileHandle(this.assets, path, Files.FileType.Internal);
    }

    @Override // com.badlogic.gdx.Files
    public FileHandle external(String path) {
        return new AndroidFileHandle((AssetManager) null, path, Files.FileType.External);
    }

    @Override // com.badlogic.gdx.Files
    public FileHandle absolute(String path) {
        return new AndroidFileHandle((AssetManager) null, path, Files.FileType.Absolute);
    }

    @Override // com.badlogic.gdx.Files
    public FileHandle local(String path) {
        return new AndroidFileHandle((AssetManager) null, this.localpath + path, Files.FileType.Local);
    }

    @Override // com.badlogic.gdx.Files
    public String getExternalStoragePath() {
        return this.sdcard;
    }

    @Override // com.badlogic.gdx.Files
    public boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    @Override // com.badlogic.gdx.Files
    public String getLocalStoragePath() {
        return this.localpath;
    }

    @Override // com.badlogic.gdx.Files
    public boolean isLocalStorageAvailable() {
        return true;
    }
}
