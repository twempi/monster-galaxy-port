package com.gaiaonline.monstergalaxy.assets;

import java.io.File;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Asset {
    private final Integer cropWidth;
    private final String extension;
    private final String filename;
    private final String folder;

    public Asset(String folder, String filename) {
        this(folder, filename, "png");
    }

    public Asset(String folder, String filename, String extension) {
        this(folder, filename, extension, null);
    }

    public Asset(String folder, String filename, String extension, Integer cropWidth) {
        this.folder = folder;
        this.filename = filename;
        this.extension = extension;
        this.cropWidth = cropWidth;
    }

    public String getFolder() {
        return this.folder;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getCompletePath() {
        return "data/downloaded/" + this.folder + File.separator + this.filename + "." + this.extension;
    }

    public String getFolderPath() {
        return "data/downloaded/" + this.folder;
    }

    public Integer getCropWidth() {
        return this.cropWidth;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Asset a = (Asset) o;
        if (a.folder.equals(this.folder) && a.filename.equals(this.filename) && a.extension.equals(this.extension)) {
            if (a.cropWidth == null && this.cropWidth == null) {
                return true;
            }
            if (a.cropWidth != null && a.cropWidth.equals(this.cropWidth)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.folder.hashCode() + this.filename.hashCode() + this.extension.hashCode();
    }
}
