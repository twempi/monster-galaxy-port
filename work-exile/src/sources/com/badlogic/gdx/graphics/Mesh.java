package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.IndexBufferObjectSubData;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectSubData;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Mesh implements Disposable {
    final IndexData indices;
    final boolean isVertexArray;
    final VertexData vertices;
    static final Map<Application, List<Mesh>> meshes = new HashMap();
    public static boolean forceVBO = false;
    boolean autoBind = true;
    int refCount = 0;

    public enum VertexDataType {
        VertexArray,
        VertexBufferObject,
        VertexBufferObjectSubData
    }

    public Mesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        if (Gdx.gl20 != null || Gdx.gl11 != null || forceVBO) {
            this.vertices = new VertexBufferObject(isStatic, maxVertices, attributes);
            this.indices = new IndexBufferObject(isStatic, maxIndices);
            this.isVertexArray = false;
        } else {
            this.vertices = new VertexArray(maxVertices, attributes);
            this.indices = new IndexBufferObject(maxIndices);
            this.isVertexArray = true;
        }
        addManagedMesh(Gdx.app, this);
    }

    public Mesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) {
        if (Gdx.gl20 != null || Gdx.gl11 != null || forceVBO) {
            this.vertices = new VertexBufferObject(isStatic, maxVertices, attributes);
            this.indices = new IndexBufferObject(isStatic, maxIndices);
            this.isVertexArray = false;
        } else {
            this.vertices = new VertexArray(maxVertices, attributes);
            this.indices = new IndexBufferObject(maxIndices);
            this.isVertexArray = true;
        }
        addManagedMesh(Gdx.app, this);
    }

    public Mesh(VertexDataType type, boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        if (type == VertexDataType.VertexArray && Gdx.graphics.isGL20Available()) {
            type = VertexDataType.VertexBufferObject;
        }
        if (type == VertexDataType.VertexBufferObject) {
            this.vertices = new VertexBufferObject(isStatic, maxVertices, attributes);
            this.indices = new IndexBufferObject(isStatic, maxIndices);
            this.isVertexArray = false;
        } else if (type == VertexDataType.VertexBufferObjectSubData) {
            this.vertices = new VertexBufferObjectSubData(isStatic, maxVertices, attributes);
            this.indices = new IndexBufferObjectSubData(isStatic, maxIndices);
            this.isVertexArray = false;
        } else {
            this.vertices = new VertexArray(maxVertices, attributes);
            this.indices = new IndexBufferObject(maxIndices);
            this.isVertexArray = true;
        }
        addManagedMesh(Gdx.app, this);
    }

    public void setVertices(float[] vertices) {
        this.vertices.setVertices(vertices, 0, vertices.length);
    }

    public void setVertices(float[] vertices, int offset, int count) {
        this.vertices.setVertices(vertices, offset, count);
    }

    public void getVertices(float[] vertices) {
        if (vertices.length < (getNumVertices() * getVertexSize()) / 4) {
            throw new IllegalArgumentException("not enough room in vertices array, has " + vertices.length + " floats, needs " + ((getNumVertices() * getVertexSize()) / 4));
        }
        int pos = getVerticesBuffer().position();
        getVerticesBuffer().position(0);
        getVerticesBuffer().get(vertices, 0, (getNumVertices() * getVertexSize()) / 4);
        getVerticesBuffer().position(pos);
    }

    public void setIndices(short[] indices) {
        this.indices.setIndices(indices, 0, indices.length);
    }

    public void setIndices(short[] indices, int offset, int count) {
        this.indices.setIndices(indices, offset, count);
    }

    public void getIndices(short[] indices) {
        if (indices.length < getNumIndices()) {
            throw new IllegalArgumentException("not enough room in indices array, has " + indices.length + " floats, needs " + getNumIndices());
        }
        int pos = getIndicesBuffer().position();
        getIndicesBuffer().position(0);
        getIndicesBuffer().get(indices, 0, getNumIndices());
        getIndicesBuffer().position(pos);
    }

    public int getNumIndices() {
        return this.indices.getNumIndices();
    }

    public int getNumVertices() {
        return this.vertices.getNumVertices();
    }

    public int getMaxVertices() {
        return this.vertices.getNumMaxVertices();
    }

    public int getMaxIndices() {
        return this.indices.getNumMaxIndices();
    }

    public int getVertexSize() {
        return this.vertices.getAttributes().vertexSize;
    }

    public void setAutoBind(boolean autoBind) {
        this.autoBind = autoBind;
    }

    public void bind() {
        if (Gdx.graphics.isGL20Available()) {
            throw new IllegalStateException("can't use this render method with OpenGL ES 2.0");
        }
        this.vertices.bind();
        if (this.isVertexArray || this.indices.getNumIndices() <= 0) {
            return;
        }
        this.indices.bind();
    }

    public void unbind() {
        if (Gdx.graphics.isGL20Available()) {
            throw new IllegalStateException("can't use this render method with OpenGL ES 2.0");
        }
        this.vertices.unbind();
        if (this.isVertexArray || this.indices.getNumIndices() <= 0) {
            return;
        }
        this.indices.unbind();
    }

    public void bind(ShaderProgram shader) {
        if (!Gdx.graphics.isGL20Available()) {
            throw new IllegalStateException("can't use this render method with OpenGL ES 1.x");
        }
        ((VertexBufferObject) this.vertices).bind(shader);
        if (this.indices.getNumIndices() > 0) {
            this.indices.bind();
        }
    }

    public void unbind(ShaderProgram shader) {
        if (!Gdx.graphics.isGL20Available()) {
            throw new IllegalStateException("can't use this render method with OpenGL ES 1.x");
        }
        ((VertexBufferObject) this.vertices).unbind(shader);
        if (this.indices.getNumIndices() > 0) {
            this.indices.unbind();
        }
    }

    public void render(int primitiveType) {
        render(primitiveType, 0, this.indices.getNumMaxIndices() > 0 ? getNumIndices() : getNumVertices());
    }

    public void render(int primitiveType, int offset, int count) {
        if (Gdx.graphics.isGL20Available()) {
            throw new IllegalStateException("can't use this render method with OpenGL ES 2.0");
        }
        if (this.autoBind) {
            bind();
        }
        if (this.isVertexArray) {
            if (this.indices.getNumIndices() > 0) {
                ShortBuffer buffer = this.indices.getBuffer();
                int oldPosition = buffer.position();
                int oldLimit = buffer.limit();
                buffer.position(offset);
                buffer.limit(offset + count);
                Gdx.gl10.glDrawElements(primitiveType, count, 5123, buffer);
                buffer.position(oldPosition);
                buffer.limit(oldLimit);
            } else {
                Gdx.gl10.glDrawArrays(primitiveType, offset, count);
            }
        } else if (this.indices.getNumIndices() > 0) {
            Gdx.gl11.glDrawElements(primitiveType, count, 5123, offset * 2);
        } else {
            Gdx.gl11.glDrawArrays(primitiveType, offset, count);
        }
        if (this.autoBind) {
            unbind();
        }
    }

    public void render(ShaderProgram shader, int primitiveType) {
        render(shader, primitiveType, 0, this.indices.getNumMaxIndices() > 0 ? getNumIndices() : getNumVertices());
    }

    public void render(ShaderProgram shader, int primitiveType, int offset, int count) {
        if (!Gdx.graphics.isGL20Available()) {
            throw new IllegalStateException("can't use this render method with OpenGL ES 1.x");
        }
        if (this.autoBind) {
            bind(shader);
        }
        if (this.indices.getNumIndices() > 0) {
            Gdx.gl20.glDrawElements(primitiveType, count, 5123, offset * 2);
        } else {
            Gdx.gl20.glDrawArrays(primitiveType, offset, count);
        }
        if (this.autoBind) {
            unbind(shader);
        }
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.refCount--;
        if (this.refCount <= 0) {
            if (meshes.get(Gdx.app) != null) {
                meshes.get(Gdx.app).remove(this);
            }
            this.vertices.dispose();
            this.indices.dispose();
        }
    }

    public VertexAttribute getVertexAttribute(int usage) {
        VertexAttributes attributes = this.vertices.getAttributes();
        int len = attributes.size();
        for (int i = 0; i < len; i++) {
            if (attributes.get(i).usage == usage) {
                return attributes.get(i);
            }
        }
        return null;
    }

    public VertexAttributes getVertexAttributes() {
        return this.vertices.getAttributes();
    }

    public FloatBuffer getVerticesBuffer() {
        return this.vertices.getBuffer();
    }

    public BoundingBox calculateBoundingBox() {
        BoundingBox bbox = new BoundingBox();
        calculateBoundingBox(bbox);
        return bbox;
    }

    public void calculateBoundingBox(BoundingBox bbox) {
        int numVertices = getNumVertices();
        if (numVertices == 0) {
            throw new GdxRuntimeException("No vertices defined");
        }
        FloatBuffer verts = this.vertices.getBuffer();
        bbox.inf();
        VertexAttribute posAttrib = getVertexAttribute(0);
        int offset = posAttrib.offset / 4;
        int vertexSize = this.vertices.getAttributes().vertexSize / 4;
        int idx = offset;
        switch (posAttrib.numComponents) {
            case 1:
                for (int i = 0; i < numVertices; i++) {
                    bbox.ext(verts.get(idx), 0.0f, 0.0f);
                    idx += vertexSize;
                }
                return;
            case 2:
                for (int i2 = 0; i2 < numVertices; i2++) {
                    bbox.ext(verts.get(idx), verts.get(idx + 1), 0.0f);
                    idx += vertexSize;
                }
                return;
            case 3:
                for (int i3 = 0; i3 < numVertices; i3++) {
                    bbox.ext(verts.get(idx), verts.get(idx + 1), verts.get(idx + 2));
                    idx += vertexSize;
                }
                return;
            default:
                return;
        }
    }

    public ShortBuffer getIndicesBuffer() {
        return this.indices.getBuffer();
    }

    private static void addManagedMesh(Application app, Mesh mesh) {
        List<Mesh> managedResources = meshes.get(app);
        if (managedResources == null) {
            managedResources = new ArrayList<>();
        }
        managedResources.add(mesh);
        meshes.put(app, managedResources);
    }

    public static void invalidateAllMeshes(Application app) {
        List<Mesh> meshesList = meshes.get(app);
        if (meshesList != null) {
            for (int i = 0; i < meshesList.size(); i++) {
                if (meshesList.get(i).vertices instanceof VertexBufferObject) {
                    ((VertexBufferObject) meshesList.get(i).vertices).invalidate();
                    meshesList.get(i).indices.invalidate();
                }
            }
        }
    }

    public static void clearAllMeshes(Application app) {
        meshes.remove(app);
    }

    public static String getManagedStatus() {
        StringBuilder builder = new StringBuilder();
        builder.append("Managed meshes/app: { ");
        for (Application app : meshes.keySet()) {
            builder.append(meshes.get(app).size());
            builder.append(" ");
        }
        builder.append("}");
        return builder.toString();
    }

    public void scale(float scaleX, float scaleY, float scaleZ) {
        VertexAttribute posAttr = getVertexAttribute(0);
        int offset = posAttr.offset / 4;
        int numComponents = posAttr.numComponents;
        int numVertices = getNumVertices();
        int vertexSize = getVertexSize() / 4;
        float[] vertices = new float[numVertices * vertexSize];
        getVertices(vertices);
        int idx = offset;
        switch (numComponents) {
            case 1:
                for (int i = 0; i < numVertices; i++) {
                    vertices[idx] = vertices[idx] * scaleX;
                    idx += vertexSize;
                }
                break;
            case 2:
                for (int i2 = 0; i2 < numVertices; i2++) {
                    vertices[idx] = vertices[idx] * scaleX;
                    int i3 = idx + 1;
                    vertices[i3] = vertices[i3] * scaleY;
                    idx += vertexSize;
                }
                break;
            case 3:
                for (int i4 = 0; i4 < numVertices; i4++) {
                    vertices[idx] = vertices[idx] * scaleX;
                    int i5 = idx + 1;
                    vertices[i5] = vertices[i5] * scaleY;
                    int i6 = idx + 2;
                    vertices[i6] = vertices[i6] * scaleZ;
                    idx += vertexSize;
                }
                break;
        }
        setVertices(vertices);
    }
}
