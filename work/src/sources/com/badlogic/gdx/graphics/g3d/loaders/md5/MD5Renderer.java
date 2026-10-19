package com.badlogic.gdx.graphics.g3d.loaders.md5;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: classes.dex */
public class MD5Renderer implements Disposable {
    private final short[][] indices;
    private BoundingBox mBBox = new BoundingBox();
    private final Mesh mesh;
    private final MD5Model model;
    private boolean useJni;
    private boolean useNormals;
    private final float[][] vertices;

    public BoundingBox getBBox() {
        return this.mBBox;
    }

    public float[] getVertices(int idx) {
        return this.vertices[idx];
    }

    public short[] getIndices(int idx) {
        return this.indices[idx];
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public MD5Renderer(MD5Model model, boolean useNormals, boolean useJni) {
        if (useJni && useNormals) {
            throw new GdxRuntimeException("JNI with normals is currently unsupported.");
        }
        int maxVertices = 0;
        int maxIndices = 0;
        int stride = useNormals ? 8 : 5;
        this.model = model;
        this.useJni = useJni;
        this.useNormals = useNormals;
        this.indices = new short[model.meshes.length][];
        this.vertices = new float[model.meshes.length][];
        for (int i = 0; i < model.meshes.length; i++) {
            maxVertices = maxVertices < model.meshes[i].numVertices ? model.meshes[i].numVertices : maxVertices;
            if (maxIndices < model.meshes[i].numTriangles * 3) {
                maxIndices = model.meshes[i].numTriangles * 3;
            }
            this.indices[i] = model.meshes[i].getIndices();
            this.vertices[i] = model.meshes[i].createVertexArray(stride);
        }
        if (useNormals) {
            this.mesh = new Mesh(false, maxVertices, maxIndices, new VertexAttribute(0, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(3, 2, "a_texCoord0"), new VertexAttribute(2, 3, ShaderProgram.NORMAL_ATTRIBUTE));
        } else {
            this.mesh = new Mesh(false, maxVertices, maxIndices, new VertexAttribute(0, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(3, 2, "a_texCoord0"));
        }
    }

    public void setSkeleton(MD5Joints skeleton) {
        this.mBBox.clr();
        for (int i = 0; i < this.model.meshes.length; i++) {
            MD5Mesh mesh = this.model.meshes[i];
            if (this.useJni) {
                if (this.useNormals) {
                    throw new GdxRuntimeException("Can't skin vertices & normals using native code yet");
                }
                mesh.calculateVerticesJni(skeleton, this.vertices[i]);
            } else if (this.useNormals) {
                mesh.calculateVerticesN(skeleton, this.vertices[i], this.mBBox);
            } else {
                mesh.calculateVertices(skeleton, this.vertices[i], this.mBBox);
            }
        }
    }

    public void setUseJni(boolean useJni) {
        this.useJni = useJni;
    }

    public void calculateNormals(MD5Joints bindPoseSkeleton) {
        for (int i = 0; i < this.model.meshes.length; i++) {
            MD5Mesh mesh = this.model.meshes[i];
            mesh.calculateNormalsBind(bindPoseSkeleton, this.vertices[i]);
        }
    }

    public void render() {
        for (int i = 0; i < this.model.meshes.length; i++) {
            this.mesh.setIndices(this.indices[i]);
            this.mesh.setVertices(this.vertices[i]);
            this.mesh.render(4, 0, this.indices[i].length);
        }
    }

    public void render(Material[] materials) {
        for (int i = 0; i < this.model.meshes.length; i++) {
            if (materials[i] != null) {
                if (materials[i].Texture != null) {
                    materials[i].Texture.bind();
                }
                materials[i].set(1028);
            }
            this.mesh.setIndices(this.indices[i]);
            this.mesh.setVertices(this.vertices[i]);
            this.mesh.render(4, 0, this.indices[i].length);
        }
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.mesh.dispose();
    }

    public boolean isJniUsed() {
        return this.useJni;
    }
}
