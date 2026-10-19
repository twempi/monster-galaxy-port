package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class TiledGrid3D {
    private static final Logger logger = new Logger("TiledGrid3D");
    protected int gridSizeX;
    protected int gridSizeY;
    protected int imgHeight;
    protected int imgWidth;
    protected Mesh mesh;
    protected float offsetX;
    protected float offsetY;
    protected float[] originalVertices;
    protected float posX;
    protected float posY;
    protected Texture texture;
    protected float[] vertices;
    private Quad3 tmpQuad3 = new Quad3();
    private boolean isUpsideDown = false;

    protected abstract void updateVertices(float f);

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setUpsideDown(boolean isUpsideDown) {
        this.isUpsideDown = isUpsideDown;
    }

    public void setGridSize(int gridSizeX, int gridSizeY) {
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;
    }

    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public void setImageSize(int width, int height) {
        this.imgWidth = width;
        this.imgHeight = height;
    }

    public void initialize() {
        float width = this.texture.getWidth();
        float height = this.texture.getHeight();
        float stepX = width / this.gridSizeX;
        float stepY = height / this.gridSizeY;
        int numQuads = this.gridSizeX * this.gridSizeY;
        this.vertices = new float[numQuads * 20];
        short[] indices = new short[numQuads * 6];
        this.mesh = new Mesh(false, this.vertices.length, indices.length, new VertexAttribute(0, 3, "a_Position"), new VertexAttribute(3, 2, "a_texCoords"));
        int idx = 0;
        for (int x = 0; x < this.gridSizeX; x++) {
            for (int y = 0; y < this.gridSizeY; y++) {
                float x1 = x * stepX;
                float x2 = x1 + stepX;
                float y1 = y * stepY;
                float y2 = y1 + stepY;
                float newY1 = this.isUpsideDown ? this.imgHeight - y1 : y1;
                float newY2 = this.isUpsideDown ? this.imgHeight - y2 : y2;
                int idx2 = idx + 1;
                this.vertices[idx] = x1;
                int idx3 = idx2 + 1;
                this.vertices[idx2] = y1;
                int idx4 = idx3 + 1;
                this.vertices[idx3] = 0.0f;
                int idx5 = idx4 + 1;
                this.vertices[idx4] = x1 / width;
                int idx6 = idx5 + 1;
                this.vertices[idx5] = newY1 / height;
                int idx7 = idx6 + 1;
                this.vertices[idx6] = x2;
                int idx8 = idx7 + 1;
                this.vertices[idx7] = y1;
                int idx9 = idx8 + 1;
                this.vertices[idx8] = 0.0f;
                int idx10 = idx9 + 1;
                this.vertices[idx9] = x2 / width;
                int idx11 = idx10 + 1;
                this.vertices[idx10] = newY1 / height;
                int idx12 = idx11 + 1;
                this.vertices[idx11] = x1;
                int idx13 = idx12 + 1;
                this.vertices[idx12] = y2;
                int idx14 = idx13 + 1;
                this.vertices[idx13] = 0.0f;
                int idx15 = idx14 + 1;
                this.vertices[idx14] = x1 / width;
                int idx16 = idx15 + 1;
                this.vertices[idx15] = newY2 / height;
                int idx17 = idx16 + 1;
                this.vertices[idx16] = x2;
                int idx18 = idx17 + 1;
                this.vertices[idx17] = y2;
                int idx19 = idx18 + 1;
                this.vertices[idx18] = 0.0f;
                int idx20 = idx19 + 1;
                this.vertices[idx19] = x2 / width;
                idx = idx20 + 1;
                this.vertices[idx20] = newY2 / height;
            }
        }
        for (short x3 = 0; x3 < numQuads; x3 = (short) (x3 + 1)) {
            indices[(x3 * 6) + 0] = (short) ((x3 * 4) + 0);
            indices[(x3 * 6) + 1] = (short) ((x3 * 4) + 1);
            indices[(x3 * 6) + 2] = (short) ((x3 * 4) + 2);
            indices[(x3 * 6) + 3] = (short) ((x3 * 4) + 1);
            indices[(x3 * 6) + 4] = (short) ((x3 * 4) + 2);
            indices[(x3 * 6) + 5] = (short) ((x3 * 4) + 3);
        }
        this.originalVertices = (float[]) this.vertices.clone();
        this.mesh.setIndices(indices);
        this.mesh.setVertices(this.vertices);
    }

    protected void setTile(int x, int y, Quad3 coords) {
        int idx = ((this.gridSizeY * x) + y) * 4 * 5;
        copyToVertices(idx, coords);
    }

    protected Quad3 getOriginalTile(int x, int y) {
        int idx = ((this.gridSizeY * x) + y) * 4 * 5;
        Quad3 ret = this.tmpQuad3;
        copyFromVertices(this.originalVertices, idx, ret);
        return ret;
    }

    protected Quad3 getTile(int x, int y) {
        int idx = ((this.gridSizeY * x) + y) * 4 * 5;
        Quad3 ret = this.tmpQuad3;
        copyFromVertices(this.vertices, idx, ret);
        return ret;
    }

    public void render() {
        if (this.texture != null) {
            this.texture.bind();
            this.mesh.render(4);
        }
    }

    private void copyToVertices(int idx, Quad3 coords) {
        copyFrom3Dcoord(idx, coords.bl);
        copyFrom3Dcoord(idx + 5, coords.br);
        copyFrom3Dcoord(idx + 10, coords.tl);
        copyFrom3Dcoord(idx + 15, coords.tr);
    }

    private void copyFrom3Dcoord(int idx, Vector3 coord) {
        this.vertices[idx] = coord.x;
        this.vertices[idx + 1] = coord.y;
        this.vertices[idx + 2] = coord.z;
    }

    private void copyFromVertices(float[] vertArray, int idx, Quad3 coords) {
        copyTo3Dcoord(vertArray, idx, coords.bl);
        copyTo3Dcoord(vertArray, idx + 5, coords.br);
        copyTo3Dcoord(vertArray, idx + 10, coords.tl);
        copyTo3Dcoord(vertArray, idx + 15, coords.tr);
    }

    private void copyTo3Dcoord(float[] vertArray, int idx, Vector3 coord) {
        coord.x = vertArray[idx];
        coord.y = vertArray[idx + 1];
        coord.z = vertArray[idx + 2];
    }

    public void dispose() {
        if (this.mesh != null) {
            try {
                this.mesh.dispose();
            } catch (NullPointerException e) {
                logger.log("TiledGrid3D - NullPointerException when disposing mesh");
                e.printStackTrace();
            }
        }
        if (this.texture != null) {
            this.texture.dispose();
        }
    }
}
