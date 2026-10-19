package com.badlogic.gdx.assets.loaders.resolvers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/* JADX INFO: loaded from: classes.dex */
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
        Resolution bestDesc = choose(this.descriptors);
        FileHandle originalHandle = new FileHandle(fileName);
        FileHandle handle = this.baseResolver.resolve(resolve(originalHandle, bestDesc.suffix));
        return !handle.exists() ? this.baseResolver.resolve(fileName) : handle;
    }

    protected String resolve(FileHandle originalHandle, String suffix) {
        return originalHandle.parent() + "/" + suffix + "/" + originalHandle.name();
    }

    public static Resolution choose(Resolution... descriptors) {
        if (descriptors == null) {
            throw new IllegalArgumentException("descriptors cannot be null.");
        }
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        Resolution best = descriptors[0];
        if (w < h) {
            int n = descriptors.length;
            for (int i = 0; i < n; i++) {
                Resolution other = descriptors[i];
                if (w >= other.portraitWidth && other.portraitWidth >= best.portraitWidth && h >= other.portraitHeight && other.portraitHeight >= best.portraitHeight) {
                    best = descriptors[i];
                }
            }
        } else {
            int n2 = descriptors.length;
            for (int i2 = 0; i2 < n2; i2++) {
                Resolution other2 = descriptors[i2];
                if (w >= other2.portraitHeight && other2.portraitHeight >= best.portraitHeight && h >= other2.portraitWidth && other2.portraitWidth >= best.portraitWidth) {
                    best = descriptors[i2];
                }
            }
        }
        return best;
    }
}
