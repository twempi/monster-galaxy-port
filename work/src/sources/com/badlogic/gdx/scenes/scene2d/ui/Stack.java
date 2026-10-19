package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

/* JADX INFO: loaded from: classes.dex */
public class Stack extends WidgetGroup {
    private float maxHeight;
    private float maxWidth;
    private float minHeight;
    private float minWidth;
    private float prefHeight;
    private float prefWidth;
    private boolean sizeInvalid = true;

    public Stack() {
        setTransform(false);
        setWidth(150.0f);
        setHeight(150.0f);
        setTouchable(Touchable.childrenOnly);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void invalidate() {
        super.invalidate();
        this.sizeInvalid = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void computeSize() {
        float childMaxWidth;
        float childMaxHeight;
        this.sizeInvalid = false;
        this.prefWidth = 0.0f;
        this.prefHeight = 0.0f;
        this.minWidth = 0.0f;
        this.minHeight = 0.0f;
        this.maxWidth = 0.0f;
        this.maxHeight = 0.0f;
        SnapshotArray<Actor> children = getChildren();
        int n = children.size;
        for (int i = 0; i < n; i++) {
            Actor actor = children.get(i);
            if (actor instanceof Layout) {
                Layout layout = (Layout) actor;
                this.prefWidth = Math.max(this.prefWidth, layout.getPrefWidth());
                this.prefHeight = Math.max(this.prefHeight, layout.getPrefHeight());
                this.minWidth = Math.max(this.minWidth, layout.getMinWidth());
                this.minHeight = Math.max(this.minHeight, layout.getMinHeight());
                childMaxWidth = layout.getMaxWidth();
                childMaxHeight = layout.getMaxHeight();
            } else {
                this.prefWidth = Math.max(this.prefWidth, actor.getWidth());
                this.prefHeight = Math.max(this.prefHeight, actor.getHeight());
                this.minWidth = Math.max(this.minWidth, actor.getWidth());
                this.minHeight = Math.max(this.minHeight, actor.getHeight());
                childMaxWidth = 0.0f;
                childMaxHeight = 0.0f;
            }
            if (childMaxWidth > 0.0f) {
                if (this.maxWidth != 0.0f) {
                    childMaxWidth = Math.min(this.maxWidth, childMaxWidth);
                }
                this.maxWidth = childMaxWidth;
            }
            if (childMaxHeight > 0.0f) {
                if (this.maxHeight != 0.0f) {
                    childMaxHeight = Math.min(this.maxHeight, childMaxHeight);
                }
                this.maxHeight = childMaxHeight;
            }
        }
    }

    public void add(Actor actor) {
        addActor(actor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        if (this.sizeInvalid) {
            computeSize();
        }
        float width = getWidth();
        float height = getHeight();
        Array<Actor> children = getChildren();
        int n = children.size;
        for (int i = 0; i < n; i++) {
            Actor actor = children.get(i);
            actor.setX(0.0f);
            actor.setY(0.0f);
            if (actor.getWidth() != width || actor.getHeight() != height) {
                actor.setWidth(width);
                actor.setHeight(height);
                if (actor instanceof Layout) {
                    Layout layout = (Layout) actor;
                    layout.invalidate();
                    layout.validate();
                }
            } else if (actor instanceof Layout) {
                ((Layout) actor).validate();
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

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinWidth() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.minWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinHeight() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.minHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMaxWidth() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.maxWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMaxHeight() {
        if (this.sizeInvalid) {
            computeSize();
        }
        return this.maxHeight;
    }
}
