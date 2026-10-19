package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.SnapshotArray;

/* JADX INFO: loaded from: classes.dex */
public class WidgetGroup extends Group implements Layout {
    private boolean fillParent;
    private boolean needsLayout = true;
    private boolean layoutEnabled = true;

    public float getMinWidth() {
        return getPrefWidth();
    }

    public float getMinHeight() {
        return getPrefHeight();
    }

    public float getPrefWidth() {
        return 0.0f;
    }

    public float getPrefHeight() {
        return 0.0f;
    }

    public float getMaxWidth() {
        return 0.0f;
    }

    public float getMaxHeight() {
        return 0.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void setLayoutEnabled(boolean enabled) {
        if (this.layoutEnabled != enabled) {
            this.layoutEnabled = enabled;
            setLayoutEnabled(this, enabled);
        }
    }

    private void setLayoutEnabled(Group parent, boolean enabled) {
        SnapshotArray<Actor> children = getChildren();
        int n = children.size;
        for (int i = 0; i < n; i++) {
            Object obj = (Actor) children.get(i);
            if (obj instanceof Layout) {
                ((Layout) obj).setLayoutEnabled(enabled);
            } else if (obj instanceof Group) {
                setLayoutEnabled((Group) obj, enabled);
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void validate() {
        float parentWidth;
        float parentHeight;
        if (this.layoutEnabled) {
            Group parent = getParent();
            if (this.fillParent && parent != null) {
                Stage stage = getStage();
                if (stage != null && parent == stage.getRoot()) {
                    parentWidth = stage.getWidth();
                    parentHeight = stage.getHeight();
                } else {
                    parentWidth = parent.getWidth();
                    parentHeight = parent.getHeight();
                }
                if (getWidth() != parentWidth || getHeight() != parentHeight) {
                    setWidth(parentWidth);
                    setHeight(parentHeight);
                    invalidate();
                }
            }
            if (this.needsLayout) {
                this.needsLayout = false;
                layout();
            }
        }
    }

    public boolean needsLayout() {
        return this.needsLayout;
    }

    public void invalidate() {
        this.needsLayout = true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void invalidateHierarchy() {
        invalidate();
        Object parent = getParent();
        if (parent instanceof Layout) {
            ((Layout) parent).invalidateHierarchy();
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    protected void childrenChanged() {
        invalidateHierarchy();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void pack() {
        float newWidth = getPrefWidth();
        float newHeight = getPrefHeight();
        if (newWidth != getWidth() || newHeight != getHeight()) {
            setWidth(newWidth);
            setHeight(newHeight);
            invalidate();
        }
        validate();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void setFillParent(boolean fillParent) {
        this.fillParent = fillParent;
    }

    public void layout() {
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        validate();
        super.draw(batch, parentAlpha);
    }
}
