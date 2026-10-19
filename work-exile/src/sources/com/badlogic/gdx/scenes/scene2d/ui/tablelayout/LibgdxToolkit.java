package com.badlogic.gdx.scenes.scene2d.ui.tablelayout;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Toolkit;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LibgdxToolkit extends Toolkit<Actor, Table, TableLayout> {
    public static BitmapFont defaultFont;
    private static HashMap<String, BitmapFont> fonts;
    public static LibgdxToolkit instance;

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public /* bridge */ /* synthetic */ void setProperty(BaseTableLayout x0, Actor actor, String x2, List x3) {
        setProperty((TableLayout) x0, actor, x2, (List<String>) x3);
    }

    static {
        addClassPrefix("com.badlogic.gdx.scenes.scene2d.");
        addClassPrefix("com.badlogic.gdx.scenes.scene2d.actors.");
        instance = new LibgdxToolkit();
        fonts = new HashMap<>();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public Actor wrap(Object object) {
        if (!(object instanceof String)) {
            return object == null ? new Group() : (Actor) super.wrap(object);
        }
        if (defaultFont == null) {
            throw new IllegalStateException("No default font has been set.");
        }
        return new Label(null, defaultFont, (String) object);
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public Actor newWidget(TableLayout layout, String className) {
        TextureAtlas.AtlasRegion region;
        if (layout.atlas == null || (region = layout.atlas.findRegion(className)) == null) {
            return className.equals("button") ? new Button(null) : (Actor) super.newWidget(layout, className);
        }
        return new Image(className, region);
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public TableLayout getLayout(Table table) {
        return table.getTableLayout();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public Actor newStack() {
        return new Stack();
    }

    public void setProperty(TableLayout layout, Actor object, String name, List<String> values) {
        if (object instanceof Label) {
            Label label = (Label) object;
            String value = values.get(0);
            if (name.equals("font")) {
                label.setFont(getFont(value));
                return;
            }
        }
        super.setProperty(layout, object, name, values);
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void addChild(Actor parent, Actor child, String layoutString) {
        if (child.parent != null) {
            child.remove();
        }
        ((Group) parent).addActor(child);
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void removeChild(Actor parent, Actor child) {
        ((Group) parent).removeActor(child);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public int getMinWidth(Actor actor) {
        return actor instanceof Layout ? (int) ((Layout) actor).getPrefWidth() : (int) actor.width;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public int getMinHeight(Actor actor) {
        return actor instanceof Layout ? (int) ((Layout) actor).getPrefHeight() : (int) actor.height;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public int getPrefWidth(Actor actor) {
        return actor instanceof Layout ? (int) ((Layout) actor).getPrefWidth() : (int) actor.width;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.esotericsoftware.tablelayout.Toolkit
    public int getPrefHeight(Actor actor) {
        return actor instanceof Layout ? (int) ((Layout) actor).getPrefHeight() : (int) actor.height;
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public int getMaxWidth(Actor actor) {
        return 0;
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public int getMaxHeight(Actor actor) {
        return 0;
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void clearDebugRectangles(TableLayout layout) {
        if (layout.debugRects != null) {
            layout.debugRects.clear();
        }
    }

    @Override // com.esotericsoftware.tablelayout.Toolkit
    public void addDebugRectangle(TableLayout layout, int type, int x, int y, int w, int h) {
        if (layout.debugRects == null) {
            layout.debugRects = new Array<>();
        }
        layout.debugRects.add(new DebugRect(type, x, y, w, h));
    }

    public static void registerFont(String name, BitmapFont font) {
        fonts.put(name, font);
        if (defaultFont == null) {
            defaultFont = font;
        }
    }

    public static BitmapFont getFont(String name) {
        BitmapFont font = fonts.get(name);
        if (font == null) {
            throw new IllegalArgumentException("Font not found: " + name);
        }
        return font;
    }

    static class DebugRect extends Rectangle {
        final int type;

        public DebugRect(int type, int x, int y, int width, int height) {
            super(x, y, width, height);
            this.type = type;
        }
    }
}
