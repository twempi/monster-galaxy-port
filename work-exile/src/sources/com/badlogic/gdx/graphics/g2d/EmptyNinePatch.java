package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class EmptyNinePatch extends NinePatch {
    private static TextureRegion[] emptyPatches;
    private static EmptyNinePatch instance;
    private static TextureRegion region;

    public static EmptyNinePatch getInstance() {
        if (instance == null) {
            Texture texture = new Texture(2, 2, Pixmap.Format.RGBA8888);
            region = new TextureRegion(texture) { // from class: com.badlogic.gdx.graphics.g2d.EmptyNinePatch.1
                @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
                public int getRegionWidth() {
                    return 0;
                }

                @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
                public int getRegionHeight() {
                    return 0;
                }
            };
            emptyPatches = new TextureRegion[]{region, region, region, region, region, region, region, region, region};
            instance = new EmptyNinePatch();
        }
        return instance;
    }

    public static TextureRegion getRegion() {
        getInstance();
        return region;
    }

    private EmptyNinePatch() {
        super(emptyPatches);
    }

    @Override // com.badlogic.gdx.graphics.g2d.NinePatch
    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
    }
}
