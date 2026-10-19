package com.badlogic.gdx.graphics.g3d.decals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

/* JADX INFO: loaded from: classes.dex */
public class SimpleOrthoGroupStrategy implements GroupStrategy {
    private static final int GROUP_BLEND = 1;
    private static final int GROUP_OPAQUE = 0;
    private Comparator comparator = new Comparator();

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public int decideGroup(Decal decal) {
        return decal.getMaterial().isOpaque() ? 0 : 1;
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void beforeGroup(int group, Array<Decal> contents) {
        if (group == 1) {
            Sort.instance().sort(contents, this.comparator);
            Gdx.gl10.glEnable(3042);
            Gdx.gl10.glDepthMask(false);
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void afterGroup(int group) {
        if (group == 1) {
            Gdx.gl10.glDepthMask(true);
            Gdx.gl10.glDisable(3042);
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void beforeGroups() {
        Gdx.gl10.glEnable(3553);
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void afterGroups() {
        Gdx.gl10.glDisable(3553);
    }

    class Comparator implements java.util.Comparator<Decal> {
        Comparator() {
        }

        @Override // java.util.Comparator
        public int compare(Decal a, Decal b) {
            return a.getZ() - b.getZ() < 0.0f ? -1 : 1;
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public ShaderProgram getGroupShader(int group) {
        return null;
    }
}
