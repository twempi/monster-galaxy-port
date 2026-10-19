package com.gaiaonline.monstergalaxy.graphics.util;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/* JADX INFO: loaded from: classes.dex */
public class PieMesh extends Mesh {
    int numPieces;
    private float[] pieceEndRanges;
    private float[] pieceEndVertices;
    private float radius;
    private float x;
    private float y;

    public PieMesh(float x, float y, float radius) {
        super(true, 7, 8, new VertexAttribute(0, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(3, 2, ShaderProgram.TEXCOORD_ATTRIBUTE));
        this.numPieces = 0;
        this.pieceEndVertices = new float[]{0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.0f, 0.5f};
        this.pieceEndRanges = new float[]{0.125f, 0.375f, 0.625f, 0.875f, 1.0f};
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override // com.badlogic.gdx.graphics.Mesh
    public void render(int primitiveType) {
        if (getNumIndices() > 0) {
            super.render(primitiveType);
        }
    }

    public void setCompletionRate(float completionRate) {
        this.numPieces = getNumPieces(completionRate);
        if (this.numPieces == 0) {
            setVertices(new float[0]);
            setIndices(new short[0]);
            return;
        }
        float[] vertices = new float[(this.numPieces + 2) * 5];
        vertices[0] = this.x;
        vertices[1] = this.y;
        vertices[2] = 0.0f;
        vertices[3] = 0.5f;
        vertices[4] = 0.5f;
        float zeroVertexX = this.pieceEndVertices[(this.pieceEndRanges.length - 1) * 2];
        float zeroVertexY = this.pieceEndVertices[((this.pieceEndRanges.length - 1) * 2) + 1];
        vertices[5] = this.x + (this.radius * zeroVertexX);
        vertices[6] = this.y + (this.radius * zeroVertexY);
        vertices[7] = 0.0f;
        vertices[8] = 0.5f + zeroVertexX;
        vertices[9] = 0.5f + zeroVertexY;
        for (int i = 0; i < this.numPieces - 1; i++) {
            vertices[((i + 3) * 5) + 0] = this.x + (this.pieceEndVertices[i * 2] * this.radius);
            vertices[((i + 3) * 5) + 1] = this.y + (this.pieceEndVertices[(i * 2) + 1] * this.radius);
            vertices[((i + 3) * 5) + 2] = 0.0f;
            vertices[((i + 3) * 5) + 3] = this.pieceEndVertices[i * 2] + 0.5f;
            vertices[((i + 3) * 5) + 4] = this.pieceEndVertices[(i * 2) + 1] + 0.5f;
        }
        float[] lastVertex = getLastVertex(this.numPieces - 1, completionRate);
        vertices[10] = this.x + (this.radius * lastVertex[0]);
        vertices[11] = this.y + (this.radius * lastVertex[1]);
        vertices[12] = 0.0f;
        vertices[13] = 0.5f + lastVertex[0];
        vertices[14] = 0.5f + lastVertex[1];
        setVertices(vertices);
        short[] indices = new short[this.numPieces + 2];
        indices[0] = 0;
        indices[1] = 1;
        indices[indices.length - 1] = 2;
        for (int i2 = 2; i2 < indices.length - 1; i2++) {
            indices[i2] = (short) (i2 + 1);
        }
        setIndices(indices);
    }

    private float[] getLastVertex(int vertexIndex, float completionRate) {
        int prevIndex = vertexIndex == 0 ? this.pieceEndRanges.length - 1 : vertexIndex - 1;
        float endRange = this.pieceEndRanges[vertexIndex];
        float startRange = vertexIndex == 0 ? 0.0f : this.pieceEndRanges[prevIndex];
        float pieceCompletionRate = (completionRate - startRange) / (endRange - startRange);
        float endX = this.pieceEndVertices[vertexIndex * 2];
        float endY = this.pieceEndVertices[(vertexIndex * 2) + 1];
        float startX = this.pieceEndVertices[prevIndex * 2];
        float startY = this.pieceEndVertices[(prevIndex * 2) + 1];
        float vertexX = startX + ((endX - startX) * pieceCompletionRate);
        float vertexY = startY + ((endY - startY) * pieceCompletionRate);
        return new float[]{vertexX, vertexY};
    }

    private int getNumPieces(float completionRate) {
        if (completionRate == 0.0f) {
            return 0;
        }
        for (int i = 0; i < this.pieceEndRanges.length; i++) {
            if (completionRate <= this.pieceEndRanges[i]) {
                return i + 1;
            }
        }
        return this.pieceEndRanges.length;
    }
}
