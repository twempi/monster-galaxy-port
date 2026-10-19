package com.badlogic.gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface InputProcessor {
    boolean keyDown(int i);

    boolean keyTyped(char c);

    boolean keyUp(int i);

    boolean scrolled(int i);

    boolean touchDown(int i, int i2, int i3, int i4);

    boolean touchDragged(int i, int i2, int i3);

    boolean touchMoved(int i, int i2);

    boolean touchUp(int i, int i2, int i3, int i4);
}
