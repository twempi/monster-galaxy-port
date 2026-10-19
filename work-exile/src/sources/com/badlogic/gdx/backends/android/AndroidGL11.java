package com.badlogic.gdx.backends.android;

import com.badlogic.gdx.graphics.GL11;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class AndroidGL11 extends AndroidGL10 implements GL11 {
    private final javax.microedition.khronos.opengles.GL11 gl;

    public AndroidGL11(GL10 gl) {
        super(gl);
        this.gl = (javax.microedition.khronos.opengles.GL11) gl;
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glBindBuffer(int target, int buffer) {
        this.gl.glBindBuffer(target, buffer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glBufferData(int target, int size, Buffer data, int usage) {
        this.gl.glBufferData(target, size, data, usage);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glBufferSubData(int target, int offset, int size, Buffer data) {
        this.gl.glBufferSubData(target, offset, size, data);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glClipPlanef(int plane, FloatBuffer equation) {
        this.gl.glClipPlanef(plane, equation);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
        this.gl.glColor4ub(red, green, blue, alpha);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glDeleteBuffers(int n, IntBuffer buffers) {
        this.gl.glDeleteBuffers(n, buffers);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGenBuffers(int n, IntBuffer buffers) {
        this.gl.glGenBuffers(n, buffers);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetBooleanv(int pname, IntBuffer params) {
        this.gl.glGetBooleanv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
        this.gl.glGetBufferParameteriv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetClipPlanef(int pname, FloatBuffer eqn) {
        this.gl.glGetClipPlanef(pname, eqn);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetFloatv(int pname, FloatBuffer params) {
        this.gl.glGetFloatv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetLightfv(int light, int pname, FloatBuffer params) {
        this.gl.glGetLightfv(light, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
        this.gl.glGetMaterialfv(face, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetPointerv(int pname, Buffer[] params) {
        this.gl.glGetPointerv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetTexEnviv(int env, int pname, IntBuffer params) {
        this.gl.glGetTexEnviv(env, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
        this.gl.glGetTexParameterfv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
        this.gl.glGetTexParameteriv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public boolean glIsBuffer(int buffer) {
        return this.gl.glIsBuffer(buffer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public boolean glIsEnabled(int cap) {
        return this.gl.glIsEnabled(cap);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public boolean glIsTexture(int texture) {
        return this.gl.glIsTexture(texture);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glPointParameterf(int pname, float param) {
        this.gl.glPointParameterf(pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glPointParameterfv(int pname, FloatBuffer params) {
        this.gl.glPointParameterfv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
        this.gl.glPointSizePointerOES(type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexEnvi(int target, int pname, int param) {
        this.gl.glTexEnvi(target, pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexEnviv(int target, int pname, IntBuffer params) {
        this.gl.glTexEnviv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexParameterfv(int target, int pname, FloatBuffer params) {
        this.gl.glTexParameterfv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexParameteri(int target, int pname, int param) {
        this.gl.glTexParameteri(target, pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexParameteriv(int target, int pname, IntBuffer params) {
        this.gl.glTexParameteriv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glClipPlanef(int plane, float[] equation, int offset) {
        this.gl.glClipPlanef(plane, equation, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glDeleteBuffers(int n, int[] buffers, int offset) {
        this.gl.glDeleteBuffers(n, buffers, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGenBuffers(int n, int[] buffers, int offset) {
        this.gl.glGenBuffers(n, buffers, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetBooleanv(int pname, boolean[] params, int offset) {
        this.gl.glGetBooleanv(pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
        this.gl.glGetBufferParameteriv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetClipPlanef(int pname, float[] eqn, int offset) {
        this.gl.glGetClipPlanef(pname, eqn, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetFloatv(int pname, float[] params, int offset) {
        this.gl.glGetFloatv(pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetLightfv(int light, int pname, float[] params, int offset) {
        this.gl.glGetLightfv(light, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetMaterialfv(int face, int pname, float[] params, int offset) {
        this.gl.glGetMaterialfv(face, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
        this.gl.glGetTexEnviv(env, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
        this.gl.glGetTexParameterfv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
        this.gl.glGetTexParameteriv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glPointParameterfv(int pname, float[] params, int offset) {
        this.gl.glPointParameterfv(pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexEnviv(int target, int pname, int[] params, int offset) {
        this.gl.glTexEnviv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexParameterfv(int target, int pname, float[] params, int offset) {
        this.gl.glTexParameterfv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexParameteriv(int target, int pname, int[] params, int offset) {
        this.gl.glTexParameteriv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glColorPointer(int size, int type, int stride, int pointer) {
        this.gl.glColorPointer(size, type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glNormalPointer(int type, int stride, int pointer) {
        this.gl.glNormalPointer(type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glTexCoordPointer(int size, int type, int stride, int pointer) {
        this.gl.glTexCoordPointer(size, type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glVertexPointer(int size, int type, int stride, int pointer) {
        this.gl.glVertexPointer(size, type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL11
    public void glDrawElements(int mode, int count, int type, int indices) {
        this.gl.glDrawElements(mode, count, type, indices);
    }
}
