package com.badlogic.gdx.scenes.scene2d.ui.tablelayout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.tablelayout.Cell;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Table extends Group implements Layout {
    private final TableLayout layout;

    public Table() {
        this((String) null, new TableLayout());
    }

    public Table(float width, float height) {
        this((String) null, new TableLayout());
        this.width = width;
        this.height = height;
    }

    public Table(TableLayout layout) {
        this((String) null, layout);
    }

    public Table(String name) {
        this(name, new TableLayout());
    }

    public Table(String name, float width, float height) {
        this(name, new TableLayout());
        this.width = width;
        this.height = height;
    }

    public Table(String name, TableLayout layout) {
        super(name);
        this.layout = layout;
        layout.setTable(this);
    }

    public Cell add(Actor actor) {
        return this.layout.add(actor);
    }

    public Cell row() {
        return this.layout.row();
    }

    public void parse(String tableDescription) {
        this.layout.parse(tableDescription);
    }

    public Cell columnDefaults(int column) {
        return this.layout.columnDefaults(column);
    }

    public Cell defaults() {
        return this.layout.defaults();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.visible) {
            if (this.layout.needsLayout) {
                this.layout.layout();
            }
            super.draw(batch, parentAlpha);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        this.layout.layout();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
        this.layout.invalidate();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        this.layout.setLayoutSize(0, 0, 0, 0);
        this.layout.layout();
        return this.layout.getMinWidth();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        this.layout.setLayoutSize(0, 0, 0, 0);
        this.layout.layout();
        return this.layout.getMinHeight();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void clear() {
        super.clear();
        this.layout.clear();
    }

    public static void drawDebug(Stage stage) {
        drawDebug(stage.getActors(), stage.getSpriteBatch());
    }

    private static void drawDebug(List<Actor> actors, SpriteBatch batch) {
        int n = actors.size();
        for (int i = 0; i < n; i++) {
            Actor actor = actors.get(i);
            if (actor instanceof Table) {
                ((Table) actor).layout.drawDebug(batch);
            }
            if (actor instanceof Group) {
                drawDebug(((Group) actor).getActors(), batch);
            }
        }
    }

    public TableLayout getTableLayout() {
        return this.layout;
    }
}
