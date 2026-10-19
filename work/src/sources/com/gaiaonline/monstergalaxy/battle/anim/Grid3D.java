package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class Grid3D {
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

    protected abstract void updateVertices(float f);

    public void setTexture(Texture texture) {
        this.texture = texture;
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
        this.vertices = new float[(this.gridSizeX + 1) * (this.gridSizeY + 1) * 5];
        this.mesh = new Mesh(false, (this.gridSizeX + 1) * (this.gridSizeY + 1), this.gridSizeX * this.gridSizeY * 6, new VertexAttribute(0, 3, "a_Position"), new VertexAttribute(3, 2, "a_texCoords"));
        float width = this.texture.getWidth();
        float height = this.texture.getHeight();
        float stepX = width / this.gridSizeX;
        float stepY = height / this.gridSizeY;
        short[] indices = new short[this.gridSizeX * this.gridSizeY * 6];
        float scale = ResolutionManager.getScaleFactor();
        this.offsetX = this.posX - (((1.0f + width) * scale) - this.imgWidth);
        this.offsetY = this.posY - (((1.0f + height) * scale) - this.imgHeight);
        for (int x = 0; x < this.gridSizeX; x++) {
            for (int y = 0; y < this.gridSizeY; y++) {
                int idx = (this.gridSizeX * y) + x;
                float x1 = x * stepX;
                float x2 = x1 + stepX;
                float y1 = y * stepY;
                float y2 = y1 + stepY;
                short a = (short) (((this.gridSizeY + 1) * x) + y);
                short b = (short) (((x + 1) * (this.gridSizeY + 1)) + y);
                short c = (short) (((x + 1) * (this.gridSizeY + 1)) + y + 1);
                short d = (short) (((this.gridSizeY + 1) * x) + y + 1);
                indices[idx * 6] = a;
                indices[(idx * 6) + 1] = b;
                indices[(idx * 6) + 2] = d;
                indices[(idx * 6) + 3] = b;
                indices[(idx * 6) + 4] = c;
                indices[(idx * 6) + 5] = d;
                int[] l1 = {a * 5, b * 5, c * 5, d * 5};
                Vector3 e = new Vector3(x1, y1, 0.0f);
                Vector3 f = new Vector3(x2, y1, 0.0f);
                Vector3 g = new Vector3(x2, y2, 0.0f);
                Vector3 h = new Vector3(x1, y2, 0.0f);
                Vector3[] l2 = {e, f, g, h};
                for (int i = 0; i < 4; i++) {
                    this.vertices[l1[i]] = (l2[i].x * scale) + this.offsetX;
                    this.vertices[l1[i] + 1] = (l2[i].y * scale) + this.offsetY;
                    this.vertices[l1[i] + 2] = l2[i].z;
                    this.vertices[l1[i] + 3] = (width - l2[i].x) / width;
                    this.vertices[l1[i] + 4] = (height - l2[i].y) / height;
                }
            }
        }
        this.originalVertices = (float[]) this.vertices.clone();
        this.mesh.setIndices(indices);
        this.mesh.setVertices(this.vertices);
    }

    protected Vector3 getOriginalVertex(int x, int y) {
        int index = (((this.gridSizeY + 1) * x) + y) * 5;
        Vector3 vert = new Vector3(this.originalVertices[index], this.originalVertices[index + 1], this.originalVertices[index + 2]);
        return vert;
    }

    protected void setVertex(int x, int y, Vector3 vertex) {
        int index = (((this.gridSizeY + 1) * x) + y) * 5;
        this.vertices[index] = vertex.x;
        this.vertices[index + 1] = vertex.y;
        this.vertices[index + 2] = vertex.z;
    }

    public void render() {
        GL10 gl = Gdx.graphics.getGL10();
        updateVertices(Gdx.graphics.getDeltaTime());
        this.mesh.setVertices(this.vertices);
        gl.glEnable(3042);
        this.texture.bind();
        this.mesh.render(4);
    }
}
