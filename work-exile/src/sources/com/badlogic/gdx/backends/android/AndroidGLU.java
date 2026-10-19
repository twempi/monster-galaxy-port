package com.badlogic.gdx.backends.android;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLU;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidGLU implements GLU {
    @Override // com.badlogic.gdx.graphics.GLU
    public void gluLookAt(GL10 gl, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        android.opengl.GLU.gluLookAt(((AndroidGL10) gl).gl, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    @Override // com.badlogic.gdx.graphics.GLU
    public void gluOrtho2D(GL10 gl, float left, float right, float bottom, float top) {
        android.opengl.GLU.gluOrtho2D(((AndroidGL10) gl).gl, left, right, bottom, top);
    }

    @Override // com.badlogic.gdx.graphics.GLU
    public void gluPerspective(GL10 gl, float fovy, float aspect, float zNear, float zFar) {
        android.opengl.GLU.gluPerspective(((AndroidGL10) gl).gl, fovy, aspect, zNear, zFar);
    }

    @Override // com.badlogic.gdx.graphics.GLU
    public boolean gluProject(float objX, float objY, float objZ, float[] model, int modelOffset, float[] project, int projectOffset, int[] view, int viewOffset, float[] win, int winOffset) {
        int result = android.opengl.GLU.gluProject(objX, objY, objZ, model, modelOffset, project, projectOffset, view, viewOffset, win, winOffset);
        return result == 1;
    }

    @Override // com.badlogic.gdx.graphics.GLU
    public boolean gluUnProject(float winX, float winY, float winZ, float[] model, int modelOffset, float[] project, int projectOffset, int[] view, int viewOffset, float[] obj, int objOffset) {
        int result = android.opengl.GLU.gluUnProject(winX, winY, winZ, model, modelOffset, project, projectOffset, view, viewOffset, obj, objOffset);
        return result == 1;
    }
}
