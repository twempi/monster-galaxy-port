package com.badlogic.gdx.backends.android;

import android.content.res.AssetManager;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidFileHandle extends FileHandle {
    final AssetManager assets;

    AndroidFileHandle(AssetManager assets, String fileName, Files.FileType type) {
        super(fileName, type);
        this.assets = assets;
    }

    AndroidFileHandle(AssetManager assets, File file, Files.FileType type) {
        super(file, type);
        this.assets = assets;
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public FileHandle child(String name) {
        return this.file.getPath().length() == 0 ? new AndroidFileHandle(this.assets, new File(name), this.type) : new AndroidFileHandle(this.assets, new File(this.file, name), this.type);
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public FileHandle parent() {
        File parent = this.file.getParentFile();
        if (parent == null) {
            if (this.type == Files.FileType.Absolute) {
                parent = new File("/");
            } else {
                parent = new File("");
            }
        }
        return new AndroidFileHandle(this.assets, parent, this.type);
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public InputStream read() {
        if (this.type == Files.FileType.Internal) {
            try {
                return this.assets.open(this.file.getPath());
            } catch (IOException ex) {
                throw new GdxRuntimeException("Error reading file: " + this.file + " (" + this.type + ")", ex);
            }
        }
        return super.read();
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public FileHandle[] list() {
        if (this.type == Files.FileType.Internal) {
            try {
                String[] relativePaths = this.assets.list(this.file.getPath());
                FileHandle[] handles = new FileHandle[relativePaths.length];
                int n = handles.length;
                for (int i = 0; i < n; i++) {
                    handles[i] = new AndroidFileHandle(this.assets, new File(this.file, relativePaths[i]), this.type);
                }
                return handles;
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error listing children: " + this.file + " (" + this.type + ")", ex);
            }
        }
        return super.list();
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public FileHandle[] list(String suffix) {
        if (this.type == Files.FileType.Internal) {
            try {
                String[] relativePaths = this.assets.list(this.file.getPath());
                FileHandle[] handles = new FileHandle[relativePaths.length];
                int count = 0;
                int n = handles.length;
                for (int i = 0; i < n; i++) {
                    String path = relativePaths[i];
                    if (path.endsWith(suffix)) {
                        handles[count] = new AndroidFileHandle(this.assets, new File(this.file, path), this.type);
                        count++;
                    }
                }
                if (count < relativePaths.length) {
                    FileHandle[] newHandles = new FileHandle[count];
                    System.arraycopy(handles, 0, newHandles, 0, count);
                    return newHandles;
                }
                return handles;
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error listing children: " + this.file + " (" + this.type + ")", ex);
            }
        }
        return super.list();
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public boolean isDirectory() {
        if (this.type != Files.FileType.Internal) {
            return super.isDirectory();
        }
        try {
            return this.assets.list(this.file.getPath()).length > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public boolean exists() {
        if (this.type == Files.FileType.Internal) {
            String fileName = this.file.getPath();
            try {
                this.assets.open(fileName).close();
                return true;
            } catch (Exception e) {
                try {
                    return this.assets.list(fileName).length > 0;
                } catch (Exception e2) {
                    return false;
                }
            }
        }
        return super.exists();
    }

    @Override // com.badlogic.gdx.files.FileHandle
    public long length() {
        if (this.type == Files.FileType.Internal) {
            try {
                return this.assets.openFd(this.file.getPath()).getLength();
            } catch (IOException e) {
            }
        }
        return super.length();
    }
}
