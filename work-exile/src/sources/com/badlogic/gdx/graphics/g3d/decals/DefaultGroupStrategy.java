package com.badlogic.gdx.graphics.g3d.decals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DefaultGroupStrategy implements GroupStrategy {
    private static final int GROUP_BLEND = 1;
    private static final int GROUP_OPAQUE = 0;

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public int decideGroup(Decal decal) {
        return decal.getMaterial().isOpaque() ? 0 : 1;
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void beforeGroup(int group, Array<Decal> contents) {
        if (group == 1) {
            Gdx.gl10.glEnable(3042);
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void afterGroup(int group) {
        if (group == 1) {
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

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public ShaderProgram getGroupShader(int group) {
        return null;
    }
}
