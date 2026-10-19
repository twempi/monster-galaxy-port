package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.math.Matrix4;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ImmediateModeRenderer {
    void begin(Matrix4 matrix4, int i);

    void color(float f, float f2, float f3, float f4);

    void end();

    int getNumVertices();

    void normal(float f, float f2, float f3);

    void texCoord(float f, float f2);

    void vertex(float f, float f2, float f3);
}
