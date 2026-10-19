package com.badlogic.gdx.scenes.scene2d.ui.tablelayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TableLayout extends BaseTableLayout<Actor, Table, TableLayout, LibgdxToolkit> {
    public TextureAtlas atlas;
    Array<LibgdxToolkit.DebugRect> debugRects;
    private ImmediateModeRenderer debugRenderer;
    boolean needsLayout;

    public TableLayout() {
        super(LibgdxToolkit.instance);
        this.needsLayout = true;
    }

    public TableLayout(LibgdxToolkit toolkit) {
        super(toolkit);
        this.needsLayout = true;
    }

    public void parse(FileHandle file) {
        super.parse(file.readString());
    }

    public Actor register(Actor actor) {
        if (actor.name == null) {
            throw new IllegalArgumentException("Actor must have a name: " + actor.getClass());
        }
        return register(actor.name, actor);
    }

    public Actor registerImage(String name) {
        return register(new Image(name, this.atlas.findRegion(name)));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.esotericsoftware.tablelayout.BaseTableLayout
    public Actor getWidget(String name) {
        Actor actor = (Actor) super.getWidget(name);
        return actor == null ? getTable().findActor(name) : actor;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.BaseTableLayout
    public void layout() {
        if (this.needsLayout) {
            this.needsLayout = false;
            Table table = getTable();
            setLayoutSize(0, 0, (int) table.width, (int) table.height);
            super.layout();
            List<Cell> cells = getCells();
            int n = cells.size();
            for (int i = 0; i < n; i++) {
                Cell c = cells.get(i);
                if (!c.getIgnore()) {
                    Actor actor = (Actor) c.getWidget();
                    actor.x = c.getWidgetX();
                    int widgetHeight = c.getWidgetHeight();
                    actor.y = (table.height - c.getWidgetY()) - widgetHeight;
                    actor.width = c.getWidgetWidth();
                    actor.height = widgetHeight;
                    if (actor instanceof Layout) {
                        Layout layout = (Layout) actor;
                        layout.invalidate();
                        layout.layout();
                    }
                }
            }
        }
    }

    @Override // com.esotericsoftware.tablelayout.BaseTableLayout
    public void invalidate() {
        this.needsLayout = true;
    }

    @Override // com.esotericsoftware.tablelayout.BaseTableLayout
    public void invalidateHierarchy() {
        invalidate();
        for (Actor parent = getTable().parent; parent != null; parent = parent.parent) {
            if (parent instanceof Layout) {
                ((Layout) parent).invalidate();
            }
        }
    }

    public void drawDebug(SpriteBatch batch) {
        if (getDebug() != 0 && this.debugRects != null) {
            if (this.debugRenderer == null) {
                if (Gdx.graphics.isGL20Available()) {
                    this.debugRenderer = new ImmediateModeRenderer20(64, false, true, 0);
                } else {
                    this.debugRenderer = new ImmediateModeRenderer10(64);
                }
            }
            Table table = getTable();
            float x = 0.0f;
            float y = 0.0f;
            for (Actor parent = table.parent; parent != null; parent = parent.parent) {
                if (parent instanceof Group) {
                    x += parent.x;
                    y += parent.y;
                }
            }
            float y2 = (table.y + table.height) - y;
            Gdx.graphics.getHeight();
            this.debugRenderer.begin(batch.getProjectionMatrix(), 1);
            int n = this.debugRects.size;
            for (int i = 0; i < n; i++) {
                LibgdxToolkit.DebugRect rect = this.debugRects.get(i);
                float x1 = x + rect.x;
                float y1 = (y2 - rect.y) - rect.height;
                float x2 = x1 + rect.width;
                float y3 = y1 + rect.height;
                float r = (rect.type & 4) != 0 ? 1.0f : 0.0f;
                float g = (rect.type & 8) != 0 ? 1.0f : 0.0f;
                float b = (rect.type & 2) != 0 ? 1.0f : 0.0f;
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x1, y1, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x1, y3, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x1, y3, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x2, y3, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x2, y3, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x2, y1, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x2, y1, 0.0f);
                this.debugRenderer.color(r, g, b, 1.0f);
                this.debugRenderer.vertex(x1, y1, 0.0f);
                if (this.debugRenderer.getNumVertices() == 64) {
                    this.debugRenderer.end();
                    this.debugRenderer.begin(batch.getProjectionMatrix(), 1);
                }
            }
            this.debugRenderer.end();
        }
    }
}
