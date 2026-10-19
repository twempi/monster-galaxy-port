package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SpriteBatch implements Disposable {
    private int blendDstFunc;
    private int blendSrcFunc;
    private boolean blendingDisabled;
    private Mesh[] buffers;
    float color;
    private final Matrix4 combinedMatrix;
    private int currBufferIdx;
    private ShaderProgram customShader;
    private boolean drawing;
    private int idx;
    private float invTexHeight;
    private float invTexWidth;
    private Texture lastTexture;
    public int maxSpritesInBatch;
    private Mesh mesh;
    private final Matrix4 projectionMatrix;
    public int renderCalls;
    private ShaderProgram shader;
    private Color tempColor;
    private final Matrix4 transformMatrix;
    private final float[] vertices;

    public SpriteBatch() {
        this(1000);
    }

    public SpriteBatch(int size) {
        this.lastTexture = null;
        this.invTexWidth = 0.0f;
        this.invTexHeight = 0.0f;
        this.idx = 0;
        this.currBufferIdx = 0;
        this.transformMatrix = new Matrix4();
        this.projectionMatrix = new Matrix4();
        this.combinedMatrix = new Matrix4();
        this.drawing = false;
        this.blendingDisabled = false;
        this.blendSrcFunc = 770;
        this.blendDstFunc = 771;
        this.color = Color.WHITE.toFloatBits();
        this.tempColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderCalls = 0;
        this.maxSpritesInBatch = 0;
        this.customShader = null;
        this.buffers = new Mesh[1];
        this.buffers[0] = new Mesh(Mesh.VertexDataType.VertexArray, false, size * 4, size * 6, new VertexAttribute(0, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(5, 4, ShaderProgram.COLOR_ATTRIBUTE), new VertexAttribute(3, 2, "a_texCoord0"));
        this.projectionMatrix.setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.vertices = new float[size * 20];
        int len = size * 6;
        short[] indices = new short[len];
        short j = 0;
        int i = 0;
        while (i < len) {
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 0);
            i += 6;
            j = (short) (j + 4);
        }
        this.buffers[0].setIndices(indices);
        this.mesh = this.buffers[0];
        if (Gdx.graphics.isGL20Available()) {
            createShader();
        }
    }

    public SpriteBatch(int size, int buffers) {
        this.lastTexture = null;
        this.invTexWidth = 0.0f;
        this.invTexHeight = 0.0f;
        this.idx = 0;
        this.currBufferIdx = 0;
        this.transformMatrix = new Matrix4();
        this.projectionMatrix = new Matrix4();
        this.combinedMatrix = new Matrix4();
        this.drawing = false;
        this.blendingDisabled = false;
        this.blendSrcFunc = 770;
        this.blendDstFunc = 771;
        this.color = Color.WHITE.toFloatBits();
        this.tempColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderCalls = 0;
        this.maxSpritesInBatch = 0;
        this.customShader = null;
        this.buffers = new Mesh[buffers];
        for (int i = 0; i < buffers; i++) {
            this.buffers[i] = new Mesh(false, size * 4, size * 6, new VertexAttribute(0, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(5, 4, ShaderProgram.COLOR_ATTRIBUTE), new VertexAttribute(3, 2, "a_texCoord0"));
        }
        this.projectionMatrix.setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.vertices = new float[size * 20];
        int len = size * 6;
        short[] indices = new short[len];
        short j = 0;
        int i2 = 0;
        while (i2 < len) {
            indices[i2 + 0] = (short) (j + 0);
            indices[i2 + 1] = (short) (j + 1);
            indices[i2 + 2] = (short) (j + 2);
            indices[i2 + 3] = (short) (j + 2);
            indices[i2 + 4] = (short) (j + 3);
            indices[i2 + 5] = (short) (j + 0);
            i2 += 6;
            j = (short) (j + 4);
        }
        for (int i3 = 0; i3 < buffers; i3++) {
            this.buffers[i3].setIndices(indices);
        }
        this.mesh = this.buffers[0];
        if (Gdx.graphics.isGL20Available()) {
            createShader();
        }
    }

    private void createShader() {
        this.shader = new ShaderProgram("attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}");
        if (!this.shader.isCompiled()) {
            throw new IllegalArgumentException("couldn't compile shader: " + this.shader.getLog());
        }
    }

    public void begin() {
        if (this.drawing) {
            throw new IllegalStateException("you have to call SpriteBatch.end() first");
        }
        this.renderCalls = 0;
        if (!Gdx.graphics.isGL20Available()) {
            GL10 gl = Gdx.gl10;
            gl.glDepthMask(false);
            gl.glEnable(3553);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadMatrixf(this.projectionMatrix.val, 0);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadMatrixf(this.transformMatrix.val, 0);
        } else {
            this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
            GL20 gl2 = Gdx.gl20;
            gl2.glDepthMask(false);
            gl2.glEnable(3553);
            if (this.customShader != null) {
                this.customShader.begin();
                this.customShader.setUniformMatrix("u_proj", this.projectionMatrix);
                this.customShader.setUniformMatrix("u_trans", this.transformMatrix);
                this.customShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
                this.customShader.setUniformi("u_texture", 0);
            } else {
                this.shader.begin();
                this.shader.setUniformMatrix("u_projectionViewMatrix", this.combinedMatrix);
                this.shader.setUniformi("u_texture", 0);
            }
        }
        this.idx = 0;
        this.lastTexture = null;
        this.drawing = true;
    }

    public void end() {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before end.");
        }
        if (this.idx > 0) {
            renderMesh();
        }
        this.lastTexture = null;
        this.idx = 0;
        this.drawing = false;
        GLCommon gl = Gdx.gl;
        gl.glDepthMask(true);
        if (isBlendingEnabled()) {
            gl.glDisable(3042);
        }
        gl.glDisable(3553);
        if (Gdx.graphics.isGL20Available()) {
            if (this.customShader != null) {
                this.customShader.end();
            } else {
                this.shader.end();
            }
        }
    }

    public void setColor(Color tint) {
        this.color = tint.toFloatBits();
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        this.color = NumberUtils.intBitsToFloat((-16777217) & intBits);
    }

    public void setColor(float color) {
        this.color = color;
    }

    public Color getColor() {
        int intBits = NumberUtils.floatToRawIntBits(this.color);
        Color color = this.tempColor;
        color.r = (intBits & 255) / 255.0f;
        color.g = ((intBits >>> 8) & 255) / 255.0f;
        color.b = ((intBits >>> 16) & 255) / 255.0f;
        color.a = ((intBits >>> 24) & 255) / 255.0f;
        return color;
    }

    public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float worldOriginX = x + originX;
        float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;
        if (scaleX != 1.0f || scaleY != 1.0f) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }
        float p1x = fx;
        float p1y = fy;
        float p2x = fx;
        float p2y = fy2;
        float p3x = fx2;
        float p3y = fy2;
        float p4x = fx2;
        float p4y = fy;
        if (rotation != 0.0f) {
            float cos = MathUtils.cosDeg(rotation);
            float sin = MathUtils.sinDeg(rotation);
            x1 = (cos * p1x) - (sin * p1y);
            y1 = (sin * p1x) + (cos * p1y);
            x2 = (cos * p2x) - (sin * p2y);
            y2 = (sin * p2x) + (cos * p2y);
            x3 = (cos * p3x) - (sin * p3y);
            y3 = (sin * p3x) + (cos * p3y);
            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;
            x2 = p2x;
            y2 = p2y;
            x3 = p3x;
            y3 = p3y;
            x4 = p4x;
            y4 = p4y;
        }
        float x5 = x1 + worldOriginX;
        float y5 = y1 + worldOriginY;
        float x6 = x2 + worldOriginX;
        float y6 = y2 + worldOriginY;
        float x7 = x3 + worldOriginX;
        float y7 = y3 + worldOriginY;
        float x8 = x4 + worldOriginX;
        float y8 = y4 + worldOriginY;
        float u = srcX * this.invTexWidth;
        float v = (srcY + srcHeight) * this.invTexHeight;
        float u2 = (srcX + srcWidth) * this.invTexWidth;
        float v2 = srcY * this.invTexHeight;
        if (flipX) {
            u = u2;
            u2 = u;
        }
        if (flipY) {
            v = v2;
            v2 = v;
        }
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x5;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y5;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x6;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = y6;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = x7;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = y7;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u2;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v2;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = x8;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y8;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u2;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v;
    }

    public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float u = srcX * this.invTexWidth;
        float v = (srcY + srcHeight) * this.invTexHeight;
        float u2 = (srcX + srcWidth) * this.invTexWidth;
        float v2 = srcY * this.invTexHeight;
        float fx2 = x + width;
        float fy2 = y + height;
        if (flipX) {
            u = u2;
            u2 = u;
        }
        if (flipY) {
            v = v2;
            v2 = v;
        }
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = fy2;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = fx2;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = fy2;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u2;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v2;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = fx2;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u2;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v;
    }

    public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float u = srcX * this.invTexWidth;
        float v = (srcY + srcHeight) * this.invTexHeight;
        float u2 = (srcX + srcWidth) * this.invTexWidth;
        float v2 = srcY * this.invTexHeight;
        float fx2 = x + srcWidth;
        float fy2 = y + srcHeight;
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = fy2;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = fx2;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = fy2;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u2;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v2;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = fx2;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u2;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v;
    }

    public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float fx2 = x + width;
        float fy2 = y + height;
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = fy2;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = fx2;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = fy2;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u2;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v2;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = fx2;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u2;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v;
    }

    public void draw(Texture texture, float x, float y) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float fx2 = x + texture.getWidth();
        float fy2 = y + texture.getHeight();
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = 0.0f;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = 1.0f;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = fy2;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = 0.0f;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = 0.0f;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = fx2;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = fy2;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = 1.0f;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = 0.0f;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = fx2;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = 1.0f;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = 1.0f;
    }

    public void draw(Texture texture, float x, float y, float width, float height) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float fx2 = x + width;
        float fy2 = y + height;
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = 0.0f;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = 1.0f;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = fy2;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = 0.0f;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = 0.0f;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = fx2;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = fy2;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = 1.0f;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = 0.0f;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = fx2;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = 1.0f;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = 1.0f;
    }

    public void draw(Texture texture, float[] spriteVertices, int offset, int length) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx + length >= this.vertices.length) {
            renderMesh();
        }
        System.arraycopy(spriteVertices, offset, this.vertices, this.idx, length);
        this.idx += length;
    }

    public void draw(TextureRegion region, float x, float y) {
        draw(region, x, y, Math.abs(region.getRegionWidth()), Math.abs(region.getRegionHeight()));
    }

    public void draw(TextureRegion region, float x, float y, float width, float height) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        Texture texture = region.texture;
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float fx2 = x + width;
        float fy2 = y + height;
        float u = region.u;
        float v = region.v2;
        float u2 = region.u2;
        float v2 = region.v;
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = fy2;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = fx2;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = fy2;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u2;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v2;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = fx2;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u2;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v;
    }

    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        Texture texture = region.texture;
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float worldOriginX = x + originX;
        float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;
        if (scaleX != 1.0f || scaleY != 1.0f) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }
        float p1x = fx;
        float p1y = fy;
        float p2x = fx;
        float p2y = fy2;
        float p3x = fx2;
        float p3y = fy2;
        float p4x = fx2;
        float p4y = fy;
        if (rotation != 0.0f) {
            float cos = MathUtils.cosDeg(rotation);
            float sin = MathUtils.sinDeg(rotation);
            x1 = (cos * p1x) - (sin * p1y);
            y1 = (sin * p1x) + (cos * p1y);
            x2 = (cos * p2x) - (sin * p2y);
            y2 = (sin * p2x) + (cos * p2y);
            x3 = (cos * p3x) - (sin * p3y);
            y3 = (sin * p3x) + (cos * p3y);
            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;
            x2 = p2x;
            y2 = p2y;
            x3 = p3x;
            y3 = p3y;
            x4 = p4x;
            y4 = p4y;
        }
        float u = region.u;
        float v = region.v2;
        float u2 = region.u2;
        float v2 = region.v;
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x1 + worldOriginX;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y1 + worldOriginY;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x2 + worldOriginX;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = y2 + worldOriginY;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = x3 + worldOriginX;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = y3 + worldOriginY;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u2;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v2;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = x4 + worldOriginX;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y4 + worldOriginY;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u2;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v;
    }

    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;
        float u1;
        float v1;
        float u2;
        float v2;
        float u3;
        float v3;
        float u4;
        float v4;
        if (!this.drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
        Texture texture = region.texture;
        if (texture != this.lastTexture) {
            renderMesh();
            this.lastTexture = texture;
            this.invTexWidth = 1.0f / texture.getWidth();
            this.invTexHeight = 1.0f / texture.getHeight();
        } else if (this.idx == this.vertices.length) {
            renderMesh();
        }
        float worldOriginX = x + originX;
        float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;
        if (scaleX != 1.0f || scaleY != 1.0f) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }
        float p1x = fx;
        float p1y = fy;
        float p2x = fx;
        float p2y = fy2;
        float p3x = fx2;
        float p3y = fy2;
        float p4x = fx2;
        float p4y = fy;
        if (rotation != 0.0f) {
            float cos = MathUtils.cosDeg(rotation);
            float sin = MathUtils.sinDeg(rotation);
            x1 = (cos * p1x) - (sin * p1y);
            y1 = (sin * p1x) + (cos * p1y);
            x2 = (cos * p2x) - (sin * p2y);
            y2 = (sin * p2x) + (cos * p2y);
            x3 = (cos * p3x) - (sin * p3y);
            y3 = (sin * p3x) + (cos * p3y);
            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;
            x2 = p2x;
            y2 = p2y;
            x3 = p3x;
            y3 = p3y;
            x4 = p4x;
            y4 = p4y;
        }
        float x5 = x1 + worldOriginX;
        float y5 = y1 + worldOriginY;
        float x6 = x2 + worldOriginX;
        float y6 = y2 + worldOriginY;
        float x7 = x3 + worldOriginX;
        float y7 = y3 + worldOriginY;
        float x8 = x4 + worldOriginX;
        float y8 = y4 + worldOriginY;
        if (clockwise) {
            u1 = region.u2;
            v1 = region.v2;
            u2 = region.u;
            v2 = region.v2;
            u3 = region.u;
            v3 = region.v;
            u4 = region.u2;
            v4 = region.v;
        } else {
            u1 = region.u;
            v1 = region.v;
            u2 = region.u2;
            v2 = region.v;
            u3 = region.u2;
            v3 = region.v2;
            u4 = region.u;
            v4 = region.v2;
        }
        float[] fArr = this.vertices;
        int i = this.idx;
        this.idx = i + 1;
        fArr[i] = x5;
        float[] fArr2 = this.vertices;
        int i2 = this.idx;
        this.idx = i2 + 1;
        fArr2[i2] = y5;
        float[] fArr3 = this.vertices;
        int i3 = this.idx;
        this.idx = i3 + 1;
        fArr3[i3] = this.color;
        float[] fArr4 = this.vertices;
        int i4 = this.idx;
        this.idx = i4 + 1;
        fArr4[i4] = u1;
        float[] fArr5 = this.vertices;
        int i5 = this.idx;
        this.idx = i5 + 1;
        fArr5[i5] = v1;
        float[] fArr6 = this.vertices;
        int i6 = this.idx;
        this.idx = i6 + 1;
        fArr6[i6] = x6;
        float[] fArr7 = this.vertices;
        int i7 = this.idx;
        this.idx = i7 + 1;
        fArr7[i7] = y6;
        float[] fArr8 = this.vertices;
        int i8 = this.idx;
        this.idx = i8 + 1;
        fArr8[i8] = this.color;
        float[] fArr9 = this.vertices;
        int i9 = this.idx;
        this.idx = i9 + 1;
        fArr9[i9] = u2;
        float[] fArr10 = this.vertices;
        int i10 = this.idx;
        this.idx = i10 + 1;
        fArr10[i10] = v2;
        float[] fArr11 = this.vertices;
        int i11 = this.idx;
        this.idx = i11 + 1;
        fArr11[i11] = x7;
        float[] fArr12 = this.vertices;
        int i12 = this.idx;
        this.idx = i12 + 1;
        fArr12[i12] = y7;
        float[] fArr13 = this.vertices;
        int i13 = this.idx;
        this.idx = i13 + 1;
        fArr13[i13] = this.color;
        float[] fArr14 = this.vertices;
        int i14 = this.idx;
        this.idx = i14 + 1;
        fArr14[i14] = u3;
        float[] fArr15 = this.vertices;
        int i15 = this.idx;
        this.idx = i15 + 1;
        fArr15[i15] = v3;
        float[] fArr16 = this.vertices;
        int i16 = this.idx;
        this.idx = i16 + 1;
        fArr16[i16] = x8;
        float[] fArr17 = this.vertices;
        int i17 = this.idx;
        this.idx = i17 + 1;
        fArr17[i17] = y8;
        float[] fArr18 = this.vertices;
        int i18 = this.idx;
        this.idx = i18 + 1;
        fArr18[i18] = this.color;
        float[] fArr19 = this.vertices;
        int i19 = this.idx;
        this.idx = i19 + 1;
        fArr19[i19] = u4;
        float[] fArr20 = this.vertices;
        int i20 = this.idx;
        this.idx = i20 + 1;
        fArr20[i20] = v4;
    }

    public void flush() {
        renderMesh();
    }

    private void renderMesh() {
        if (this.idx != 0) {
            this.renderCalls++;
            int spritesInBatch = this.idx / 20;
            if (spritesInBatch > this.maxSpritesInBatch) {
                this.maxSpritesInBatch = spritesInBatch;
            }
            this.lastTexture.bind();
            this.mesh.setVertices(this.vertices, 0, this.idx);
            if (Gdx.graphics.isGL20Available()) {
                if (this.blendingDisabled) {
                    Gdx.gl20.glDisable(3042);
                } else {
                    GL20 gl20 = Gdx.gl20;
                    gl20.glEnable(3042);
                    gl20.glBlendFunc(this.blendSrcFunc, this.blendDstFunc);
                }
                if (this.customShader != null) {
                    this.mesh.render(this.customShader, 4, 0, spritesInBatch * 6);
                } else {
                    this.mesh.render(this.shader, 4, 0, spritesInBatch * 6);
                }
            } else {
                if (this.blendingDisabled) {
                    Gdx.gl10.glDisable(3042);
                } else {
                    GL10 gl10 = Gdx.gl10;
                    gl10.glEnable(3042);
                    gl10.glBlendFunc(this.blendSrcFunc, this.blendDstFunc);
                }
                this.mesh.render(4, 0, spritesInBatch * 6);
            }
            this.idx = 0;
            this.currBufferIdx++;
            if (this.currBufferIdx == this.buffers.length) {
                this.currBufferIdx = 0;
            }
            this.mesh = this.buffers[this.currBufferIdx];
        }
    }

    public void disableBlending() {
        renderMesh();
        this.blendingDisabled = true;
    }

    public void enableBlending() {
        renderMesh();
        this.blendingDisabled = false;
    }

    public void setBlendFunction(int srcFunc, int dstFunc) {
        renderMesh();
        this.blendSrcFunc = srcFunc;
        this.blendDstFunc = dstFunc;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        for (int i = 0; i < this.buffers.length; i++) {
            this.buffers[i].dispose();
        }
        if (this.shader != null) {
            this.shader.dispose();
        }
    }

    public Matrix4 getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4 getTransformMatrix() {
        return this.transformMatrix;
    }

    public void setProjectionMatrix(Matrix4 projection) {
        if (this.drawing) {
            throw new GdxRuntimeException("Can't set the matrix within begin()/end() block");
        }
        this.projectionMatrix.set(projection);
    }

    public void setTransformMatrix(Matrix4 transform) {
        if (this.drawing) {
            throw new GdxRuntimeException("Can't set the matrix within begin()/end() block");
        }
        this.transformMatrix.set(transform);
    }

    public void setShader(ShaderProgram shader) {
        this.customShader = shader;
    }

    public boolean isBlendingEnabled() {
        return !this.blendingDisabled;
    }
}
