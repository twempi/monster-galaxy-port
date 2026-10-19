package com.badlogic.gdx.assets.loaders.resolvers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ResolutionFileResolver implements FileHandleResolver {
    final FileHandleResolver baseResolver;
    final Resolution[] descriptors;

    public static class Resolution {
        int portraitHeight;
        int portraitWidth;
        String suffix;

        public Resolution(int portraitWidth, int portraitHeight, String suffix) {
            this.portraitWidth = portraitWidth;
            this.portraitHeight = portraitHeight;
            this.suffix = suffix;
        }
    }

    public ResolutionFileResolver(FileHandleResolver baseResolver, Resolution... descriptors) {
        this.baseResolver = baseResolver;
        this.descriptors = descriptors;
    }

    @Override // com.badlogic.gdx.assets.loaders.FileHandleResolver
    public FileHandle resolve(String fileName) {
        int width;
        int height;
        if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight()) {
            width = Gdx.graphics.getHeight();
            height = Gdx.graphics.getWidth();
        } else {
            width = Gdx.graphics.getWidth();
            height = Gdx.graphics.getHeight();
        }
        Resolution bestDesc = null;
        int bestDistance = Integer.MAX_VALUE;
        int n = this.descriptors.length;
        for (int i = 0; i < n; i++) {
            int distance = Math.abs(width - this.descriptors[i].portraitWidth) + Math.abs(height - this.descriptors[i].portraitHeight);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestDesc = this.descriptors[i];
            }
        }
        FileHandle handle = this.baseResolver.resolve(fileName + bestDesc.suffix);
        return !handle.exists() ? this.baseResolver.resolve(fileName) : handle;
    }
}
