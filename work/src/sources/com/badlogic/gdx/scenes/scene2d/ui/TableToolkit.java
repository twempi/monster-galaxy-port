package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Toolkit;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
class TableToolkit extends Toolkit<Actor, Table, TableLayout> {
    static boolean drawDebug;

    TableToolkit() {
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void addChild(Actor parent, Actor child) {
        child.remove();
        try {
            parent.getClass().getMethod("setWidget", Actor.class).invoke(parent, child);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("Error calling setWidget.", ex);
        } catch (Exception e) {
            ((Group) parent).addActor(child);
        }
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void removeChild(Actor parent, Actor child) {
        ((Group) parent).removeActor(child);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getMinWidth(Actor actor) {
        return actor instanceof Layout ? ((Layout) actor).getMinWidth() : actor.getWidth();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getMinHeight(Actor actor) {
        return actor instanceof Layout ? ((Layout) actor).getMinHeight() : actor.getHeight();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getPrefWidth(Actor actor) {
        return actor instanceof Layout ? ((Layout) actor).getPrefWidth() : actor.getWidth();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getPrefHeight(Actor actor) {
        return actor instanceof Layout ? ((Layout) actor).getPrefHeight() : actor.getHeight();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getMaxWidth(Actor actor) {
        if (actor instanceof Layout) {
            return ((Layout) actor).getMaxWidth();
        }
        return 0.0f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getMaxHeight(Actor actor) {
        if (actor instanceof Layout) {
            return ((Layout) actor).getMaxHeight();
        }
        return 0.0f;
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getWidth(Actor widget) {
        return widget.getWidth();
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public float getHeight(Actor widget) {
        return widget.getHeight();
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void clearDebugRectangles(TableLayout layout) {
        if (layout.debugRects != null) {
            layout.debugRects.clear();
        }
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void addDebugRectangle(TableLayout layout, BaseTableLayout.Debug type, float x, float y, float w, float h) {
        drawDebug = true;
        if (layout.debugRects == null) {
            layout.debugRects = new Array<>();
        }
        layout.debugRects.add(new DebugRect(type, x, layout.getTable().getHeight() - y, w, h));
    }

    static class DebugRect extends Rectangle {
        final BaseTableLayout.Debug type;

        public DebugRect(BaseTableLayout.Debug type, float x, float y, float width, float height) {
            super(x, y, width, height);
            this.type = type;
        }
    }
}
