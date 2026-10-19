package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Disposable;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public interface VertexData extends Disposable {
    void bind();

    void bind(ShaderProgram shaderProgram);

    @Override // com.badlogic.gdx.utils.Disposable
    void dispose();

    VertexAttributes getAttributes();

    FloatBuffer getBuffer();

    int getNumMaxVertices();

    int getNumVertices();

    void setVertices(float[] fArr, int i, int i2);

    void unbind();

    void unbind(ShaderProgram shaderProgram);
}
