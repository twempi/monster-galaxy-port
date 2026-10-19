package com.badlogic.gdx.backends.android;

import com.badlogic.gdx.graphics.GL10;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class AndroidGL10 implements GL10 {
    final javax.microedition.khronos.opengles.GL10 gl;

    public AndroidGL10(javax.microedition.khronos.opengles.GL10 gl) {
        this.gl = gl;
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glActiveTexture(int texture) {
        this.gl.glActiveTexture(texture);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glAlphaFunc(int func, float ref) {
        this.gl.glAlphaFunc(func, ref);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glBindTexture(int target, int texture) {
        this.gl.glBindTexture(target, texture);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glBlendFunc(int sfactor, int dfactor) {
        this.gl.glBlendFunc(sfactor, dfactor);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glClear(int mask) {
        this.gl.glClear(mask);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glClearColor(float red, float green, float blue, float alpha) {
        this.gl.glClearColor(red, green, blue, alpha);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glClearDepthf(float depth) {
        this.gl.glClearDepthf(depth);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glClearStencil(int s) {
        this.gl.glClearStencil(s);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glClientActiveTexture(int texture) {
        this.gl.glClientActiveTexture(texture);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glColor4f(float red, float green, float blue, float alpha) {
        this.gl.glColor4f(red, green, blue, alpha);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        this.gl.glColorMask(red, green, blue, alpha);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glColorPointer(int size, int type, int stride, Buffer pointer) {
        this.gl.glColorPointer(size, type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
        this.gl.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
        this.gl.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
        this.gl.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
        this.gl.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glCullFace(int mode) {
        this.gl.glCullFace(mode);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDeleteTextures(int n, IntBuffer textures) {
        this.gl.glDeleteTextures(n, textures);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDepthFunc(int func) {
        this.gl.glDepthFunc(func);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDepthMask(boolean flag) {
        this.gl.glDepthMask(flag);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDepthRangef(float zNear, float zFar) {
        this.gl.glDepthRangef(zNear, zFar);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDisable(int cap) {
        this.gl.glDisable(cap);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glDisableClientState(int array) {
        this.gl.glDisableClientState(array);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDrawArrays(int mode, int first, int count) {
        this.gl.glDrawArrays(mode, first, count);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glDrawElements(int mode, int count, int type, Buffer indices) {
        this.gl.glDrawElements(mode, count, type, indices);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glEnable(int cap) {
        this.gl.glEnable(cap);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glEnableClientState(int array) {
        this.gl.glEnableClientState(array);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glFinish() {
        this.gl.glFinish();
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glFlush() {
        this.gl.glFlush();
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glFogf(int pname, float param) {
        this.gl.glFogf(pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glFogfv(int pname, FloatBuffer params) {
        this.gl.glFogfv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glFrontFace(int mode) {
        this.gl.glFrontFace(mode);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar) {
        this.gl.glFrustumf(left, right, bottom, top, zNear, zFar);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glGenTextures(int n, IntBuffer textures) {
        this.gl.glGenTextures(n, textures);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final int glGetError() {
        return this.gl.glGetError();
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glGetIntegerv(int pname, IntBuffer params) {
        this.gl.glGetIntegerv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final String glGetString(int name) {
        return this.gl.glGetString(name);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glHint(int target, int mode) {
        this.gl.glHint(target, mode);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLightModelf(int pname, float param) {
        this.gl.glLightModelf(pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLightModelfv(int pname, FloatBuffer params) {
        this.gl.glLightModelfv(pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLightf(int light, int pname, float param) {
        this.gl.glLightf(light, pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLightfv(int light, int pname, FloatBuffer params) {
        this.gl.glLightfv(light, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glLineWidth(float width) {
        this.gl.glLineWidth(width);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLoadIdentity() {
        this.gl.glLoadIdentity();
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLoadMatrixf(FloatBuffer m) {
        this.gl.glLoadMatrixf(m);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLogicOp(int opcode) {
        this.gl.glLogicOp(opcode);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMaterialf(int face, int pname, float param) {
        this.gl.glMaterialf(face, pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMaterialfv(int face, int pname, FloatBuffer params) {
        this.gl.glMaterialfv(face, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMatrixMode(int mode) {
        this.gl.glMatrixMode(mode);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMultMatrixf(FloatBuffer m) {
        this.gl.glMultMatrixf(m);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
        this.gl.glMultiTexCoord4f(target, s, t, r, q);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glNormal3f(float nx, float ny, float nz) {
        this.gl.glNormal3f(nx, ny, nz);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glNormalPointer(int type, int stride, Buffer pointer) {
        this.gl.glNormalPointer(type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
        this.gl.glOrthof(left, right, bottom, top, zNear, zFar);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glPixelStorei(int pname, int param) {
        this.gl.glPixelStorei(pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glPointSize(float size) {
        this.gl.glPointSize(size);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glPolygonOffset(float factor, float units) {
        this.gl.glPolygonOffset(factor, units);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glPopMatrix() {
        this.gl.glPopMatrix();
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glPushMatrix() {
        this.gl.glPushMatrix();
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
        this.gl.glReadPixels(x, y, width, height, format, type, pixels);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glRotatef(float angle, float x, float y, float z) {
        this.gl.glRotatef(angle, x, y, z);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glSampleCoverage(float value, boolean invert) {
        this.gl.glSampleCoverage(value, invert);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glScalef(float x, float y, float z) {
        this.gl.glScalef(x, y, z);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glScissor(int x, int y, int width, int height) {
        this.gl.glScissor(x, y, width, height);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glShadeModel(int mode) {
        this.gl.glShadeModel(mode);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glStencilFunc(int func, int ref, int mask) {
        this.gl.glStencilFunc(func, ref, mask);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glStencilMask(int mask) {
        this.gl.glStencilMask(mask);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glStencilOp(int fail, int zfail, int zpass) {
        this.gl.glStencilOp(fail, zfail, zpass);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
        this.gl.glTexCoordPointer(size, type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glTexEnvf(int target, int pname, float param) {
        this.gl.glTexEnvf(target, pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glTexEnvfv(int target, int pname, FloatBuffer params) {
        this.gl.glTexEnvfv(target, pname, params);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
        this.gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glTexParameterf(int target, int pname, float param) {
        this.gl.glTexParameterf(target, pname, param);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
        this.gl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glTranslatef(float x, float y, float z) {
        this.gl.glTranslatef(x, y, z);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glVertexPointer(int size, int type, int stride, Buffer pointer) {
        this.gl.glVertexPointer(size, type, stride, pointer);
    }

    @Override // com.badlogic.gdx.graphics.GLCommon
    public final void glViewport(int x, int y, int width, int height) {
        this.gl.glViewport(x, y, width, height);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glDeleteTextures(int n, int[] textures, int offset) {
        this.gl.glDeleteTextures(n, textures, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glFogfv(int pname, float[] params, int offset) {
        this.gl.glFogfv(pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glGenTextures(int n, int[] textures, int offset) {
        this.gl.glGenTextures(n, textures, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glGetIntegerv(int pname, int[] params, int offset) {
        this.gl.glGetIntegerv(pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLightModelfv(int pname, float[] params, int offset) {
        this.gl.glLightModelfv(pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLightfv(int light, int pname, float[] params, int offset) {
        this.gl.glLightfv(light, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glLoadMatrixf(float[] m, int offset) {
        this.gl.glLoadMatrixf(m, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMaterialfv(int face, int pname, float[] params, int offset) {
        this.gl.glMaterialfv(face, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glMultMatrixf(float[] m, int offset) {
        this.gl.glMultMatrixf(m, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public final void glTexEnvfv(int target, int pname, float[] params, int offset) {
        this.gl.glTexEnvfv(target, pname, params, offset);
    }

    @Override // com.badlogic.gdx.graphics.GL10
    public void glPolygonMode(int face, int mode) {
    }
}
