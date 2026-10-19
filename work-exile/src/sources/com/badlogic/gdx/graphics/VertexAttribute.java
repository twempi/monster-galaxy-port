package com.badlogic.gdx.graphics;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class VertexAttribute {
    public String alias;
    public final int numComponents;
    public int offset;
    public final int usage;

    public VertexAttribute(int usage, int numComponents, String alias) {
        this.usage = usage;
        this.numComponents = numComponents;
        this.alias = alias;
    }
}
