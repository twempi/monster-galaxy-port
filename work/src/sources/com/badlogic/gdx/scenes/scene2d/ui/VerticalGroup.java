package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.SnapshotArray;

/* JADX INFO: loaded from: classes.dex */
public class VerticalGroup extends WidgetGroup {
    private int alignment;
    private float prefHeight;
    private float prefWidth;
    private boolean reverse;
    private boolean sizeInvalid = true;

    public VerticalGroup() {
        setTouchable(Touchable.childrenOnly);
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void invalidate() {
        super.invalidate();
        this.sizeInvalid = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void computeSize() {
        this.sizeInvalid = false;
        this.prefWidth = 0.0f;
        this.prefHeight = 0.0f;
        SnapshotArray<Actor> children = getChildren();
        int n = children.size;
        for (int i = 0; i < n; i++) {
            Actor actor = children.get(i);
            if (actor instanceof Layout) {
                Layout layout = (Layout) actor;
                this.prefWidth = Math.max(this.prefWidth, layout.getPrefWidth());
                this.prefHeight += layout.getPrefHeight();
            } else {
                this.prefWidth = Math.max(this.prefWidth, actor.getWidth());
                this.prefHeight += actor.getHeight();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        float width;
        float height;
        float x;
        float groupWidth = getWidth();
        float y = this.reverse ? 0.0f : getHeight();
        float dir = this.reverse ? 1.0f : -1.0f;
        SnapshotArray<Actor> children = getChildren();
        int n = children.size;
        for (int i = 0; i < n; i++) {
            Actor actor = children.get(i);
            if (actor instanceof Layout) {
                Layout layout = (Layout) actor;
                width = layout.getPrefWidth();
                height = layout.getPrefHeight();
            } else {
                width = actor.getWidth();
                height = actor.getHeight();
            }
            if ((this.alignment & 8) != 0) {
                x = 0.0f;
            } else if ((this.alignment & 16) != 0) {
                x = groupWidth - width;
            } else {
                x = (groupWidth - width) / 2.0f;
            }
            if (!this.reverse) {
                y += height * dir;
            }
            actor.setBounds(x, y, width, height);
            if (this.reverse) {
                y += height * dir;
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.prefWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.prefHeight;
    }
}
