package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Widget extends Actor implements Layout {
    protected boolean invalidated;
    public float prefHeight;
    public float prefWidth;

    public Widget(String name, float prefWidth, float prefHeight) {
        super(name);
        this.invalidated = false;
        this.prefWidth = prefWidth;
        this.prefHeight = prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        return this.prefWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        return this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
        this.invalidated = true;
    }

    public void invalidateHierarchy() {
        invalidate();
        for (Group parent = this.parent; parent != null; parent = parent.parent) {
            if (parent instanceof Layout) {
                ((Layout) parent).invalidate();
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
            return null;
        }
        return this;
    }

    public void setPrefSize(int prefWidth, int prefHeight) {
        float f = prefWidth;
        this.width = f;
        this.prefWidth = f;
        float f2 = prefHeight;
        this.height = f2;
        this.prefHeight = f2;
        invalidateHierarchy();
    }
}
