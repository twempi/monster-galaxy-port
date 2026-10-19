package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class VertexArray implements VertexData {
    final VertexAttributes attributes;
    final FloatBuffer buffer;
    final ByteBuffer byteBuffer;
    boolean isBound;

    public VertexArray(int numVertices, VertexAttribute... attributes) {
        this(numVertices, new VertexAttributes(attributes));
    }

    public VertexArray(int numVertices, VertexAttributes attributes) {
        this.isBound = false;
        this.attributes = attributes;
        this.byteBuffer = ByteBuffer.allocateDirect(this.attributes.vertexSize * numVertices);
        this.byteBuffer.order(ByteOrder.nativeOrder());
        this.buffer = this.byteBuffer.asFloatBuffer();
        this.buffer.flip();
        this.byteBuffer.flip();
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData, com.badlogic.gdx.utils.Disposable
    public void dispose() {
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public FloatBuffer getBuffer() {
        return this.buffer;
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public int getNumVertices() {
        return (this.buffer.limit() * 4) / this.attributes.vertexSize;
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public int getNumMaxVertices() {
        return this.byteBuffer.capacity() / this.attributes.vertexSize;
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public void setVertices(float[] vertices, int offset, int count) {
        BufferUtils.copy(vertices, this.byteBuffer, count, offset);
        this.buffer.position(0);
        this.buffer.limit(count);
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public void bind() {
        GL10 gl = Gdx.gl10;
        int textureUnit = 0;
        int numAttributes = this.attributes.size();
        this.byteBuffer.limit(this.buffer.limit() * 4);
        for (int i = 0; i < numAttributes; i++) {
            VertexAttribute attribute = this.attributes.get(i);
            switch (attribute.usage) {
                case 0:
                    this.byteBuffer.position(attribute.offset);
                    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                    gl.glVertexPointer(attribute.numComponents, 5126, this.attributes.vertexSize, this.byteBuffer);
                    break;
                case 1:
                case 5:
                    int colorType = attribute.usage == 5 ? 5121 : 5126;
                    this.byteBuffer.position(attribute.offset);
                    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
                    gl.glColorPointer(attribute.numComponents, colorType, this.attributes.vertexSize, this.byteBuffer);
                    break;
                case 2:
                    this.byteBuffer.position(attribute.offset);
                    gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
                    gl.glNormalPointer(5126, this.attributes.vertexSize, this.byteBuffer);
                    break;
                case 3:
                    gl.glClientActiveTexture(33984 + textureUnit);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    this.byteBuffer.position(attribute.offset);
                    gl.glTexCoordPointer(attribute.numComponents, 5126, this.attributes.vertexSize, this.byteBuffer);
                    textureUnit++;
                    break;
            }
        }
        this.isBound = true;
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public void unbind() {
        GL10 gl = Gdx.gl10;
        int textureUnit = 0;
        int numAttributes = this.attributes.size();
        for (int i = 0; i < numAttributes; i++) {
            VertexAttribute attribute = this.attributes.get(i);
            switch (attribute.usage) {
                case 1:
                case 5:
                    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
                    break;
                case 2:
                    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
                    break;
                case 3:
                    gl.glClientActiveTexture(33984 + textureUnit);
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    textureUnit++;
                    break;
            }
        }
        this.byteBuffer.position(0);
        this.isBound = false;
    }

    @Override // com.badlogic.gdx.graphics.glutils.VertexData
    public VertexAttributes getAttributes() {
        return this.attributes;
    }
}
