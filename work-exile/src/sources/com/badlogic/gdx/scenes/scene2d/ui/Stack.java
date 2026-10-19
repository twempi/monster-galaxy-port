package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.LibgdxToolkit;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Stack extends Group implements Layout {
    private boolean needsLayout = true;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        if (this.needsLayout) {
            this.needsLayout = false;
            int n = this.children.size();
            for (int i = 0; i < n; i++) {
                Actor actor = this.children.get(i);
                actor.x = this.x;
                actor.y = this.y;
                actor.width = this.width;
                actor.height = this.height;
                if (actor instanceof Layout) {
                    Layout layout = (Layout) actor;
                    layout.invalidate();
                    layout.layout();
                }
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.needsLayout) {
            layout();
        }
        super.draw(batch, parentAlpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
        this.needsLayout = true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        float width = 0.0f;
        int n = this.children.size();
        for (int i = 0; i < n; i++) {
            width = Math.max(width, LibgdxToolkit.instance.getPrefWidth(this.children.get(i)));
        }
        return this.scaleX * width;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        float height = 0.0f;
        int n = this.children.size();
        for (int i = 0; i < n; i++) {
            height = Math.max(height, LibgdxToolkit.instance.getPrefHeight(this.children.get(i)));
        }
        return this.scaleY * height;
    }
}
