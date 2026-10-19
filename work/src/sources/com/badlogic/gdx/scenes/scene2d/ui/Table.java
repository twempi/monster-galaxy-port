package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Toolkit;
import com.esotericsoftware.tablelayout.Value;

/* JADX INFO: loaded from: classes.dex */
public class Table extends WidgetGroup {
    private Drawable background;
    private boolean clip;
    private final TableLayout layout;
    private Rectangle scissorBounds;
    private Skin skin;

    static {
        Toolkit.instance = new TableToolkit();
    }

    public Table() {
        this(null);
    }

    public Table(Skin skin) {
        this.skin = skin;
        this.layout = new TableLayout();
        this.layout.setTable(this);
        setTransform(false);
        setTouchable(Touchable.childrenOnly);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        validate();
        drawBackground(batch, parentAlpha);
        if (isTransform()) {
            applyTransform(batch, computeTransform());
            if (this.clip) {
                if (ScissorStack.pushScissors(calculateScissors(batch.getTransformMatrix()))) {
                    drawChildren(batch, parentAlpha);
                    ScissorStack.popScissors();
                }
            } else {
                drawChildren(batch, parentAlpha);
            }
            resetTransform(batch);
            return;
        }
        super.draw(batch, parentAlpha);
    }

    protected void drawBackground(SpriteBatch batch, float parentAlpha) {
        if (this.background != null) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            this.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }

    private Rectangle calculateScissors(Matrix4 transform) {
        Rectangle tableBounds = Rectangle.tmp;
        tableBounds.width = getWidth();
        tableBounds.height = getHeight();
        if (this.background == null) {
            tableBounds.x = 0.0f;
            tableBounds.y = 0.0f;
        } else {
            tableBounds.x = this.layout.getPadLeft();
            tableBounds.y = this.layout.getPadBottom();
            tableBounds.width -= tableBounds.x + this.layout.getPadRight();
            tableBounds.height -= tableBounds.y + this.layout.getPadTop();
        }
        if (this.scissorBounds == null) {
            this.scissorBounds = new Rectangle();
        }
        ScissorStack.calculateScissors(getStage().getCamera(), transform, tableBounds, this.scissorBounds);
        return this.scissorBounds;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void invalidate() {
        this.layout.invalidate();
        super.invalidate();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        return this.background != null ? Math.max(this.layout.getPrefWidth(), this.background.getMinWidth()) : this.layout.getPrefWidth();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        return this.background != null ? Math.max(this.layout.getPrefHeight(), this.background.getMinHeight()) : this.layout.getPrefHeight();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinWidth() {
        return this.layout.getMinWidth();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinHeight() {
        return this.layout.getMinHeight();
    }

    public void setBackground(Drawable background) {
        if (this.background != background) {
            this.background = background;
            if (background == null) {
                pad((Value) null);
                return;
            }
            padBottom(background.getBottomHeight());
            padTop(background.getTopHeight());
            padLeft(background.getLeftWidth());
            padRight(background.getRightWidth());
            invalidate();
        }
    }

    public Drawable getBackground() {
        return this.background;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y, boolean touchable) {
        if (!this.clip || (!(touchable && getTouchable() == Touchable.disabled) && x >= 0.0f && x < getWidth() && y >= 0.0f && y < getHeight())) {
            return super.hit(x, y, touchable);
        }
        return null;
    }

    public void setClip(boolean enabled) {
        this.clip = enabled;
        setTransform(enabled);
        invalidate();
    }

    public int getRow(float y) {
        return this.layout.getRow(y);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void clear() {
        super.clear();
        this.layout.clear();
        invalidate();
    }

    public Cell add(String text) {
        if (this.skin == null) {
            throw new IllegalStateException("Table must have a skin set to use this method.");
        }
        return add(new Label(text, this.skin));
    }

    public Cell add(String text, String labelStyleName) {
        if (this.skin == null) {
            throw new IllegalStateException("Table must have a skin set to use this method.");
        }
        return add(new Label(text, (Label.LabelStyle) this.skin.get(labelStyleName, Label.LabelStyle.class)));
    }

    public Cell add(String text, String fontName, Color color) {
        if (this.skin == null) {
            throw new IllegalStateException("Table must have a skin set to use this method.");
        }
        return add(new Label(text, new Label.LabelStyle(this.skin.getFont(fontName), color)));
    }

    public Cell add(String text, String fontName, String colorName) {
        if (this.skin == null) {
            throw new IllegalStateException("Table must have a skin set to use this method.");
        }
        return add(new Label(text, new Label.LabelStyle(this.skin.getFont(fontName), this.skin.getColor(colorName))));
    }

    public Cell add() {
        return this.layout.add(null);
    }

    public Cell add(Actor actor) {
        return this.layout.add(actor);
    }

    public Cell stack(Actor... actors) {
        Stack stack = new Stack();
        if (actors != null) {
            for (Actor actor : actors) {
                stack.addActor(actor);
            }
        }
        return add(stack);
    }

    public Cell row() {
        return this.layout.row();
    }

    public Cell columnDefaults(int column) {
        return this.layout.columnDefaults(column);
    }

    public Cell defaults() {
        return this.layout.defaults();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        this.layout.layout();
    }

    public void reset() {
        this.layout.reset();
    }

    public Cell getCell(Actor actor) {
        return this.layout.getCell(actor);
    }

    public java.util.List<Cell> getCells() {
        return this.layout.getCells();
    }

    public Table pad(Value pad) {
        this.layout.pad(pad);
        return this;
    }

    public Table pad(Value top, Value left, Value bottom, Value right) {
        this.layout.pad(top, left, bottom, right);
        return this;
    }

    public Table padTop(Value padTop) {
        this.layout.padTop(padTop);
        return this;
    }

    public Table padLeft(Value padLeft) {
        this.layout.padLeft(padLeft);
        return this;
    }

    public Table padBottom(Value padBottom) {
        this.layout.padBottom(padBottom);
        return this;
    }

    public Table padRight(Value padRight) {
        this.layout.padRight(padRight);
        return this;
    }

    public Table pad(float pad) {
        this.layout.pad(pad);
        return this;
    }

    public Table pad(float top, float left, float bottom, float right) {
        this.layout.pad(top, left, bottom, right);
        return this;
    }

    public Table padTop(float padTop) {
        this.layout.padTop(padTop);
        return this;
    }

    public Table padLeft(float padLeft) {
        this.layout.padLeft(padLeft);
        return this;
    }

    public Table padBottom(float padBottom) {
        this.layout.padBottom(padBottom);
        return this;
    }

    public Table padRight(float padRight) {
        this.layout.padRight(padRight);
        return this;
    }

    public Table align(int align) {
        this.layout.align(align);
        return this;
    }

    public Table center() {
        this.layout.center();
        return this;
    }

    public Table top() {
        this.layout.top();
        return this;
    }

    public Table left() {
        this.layout.left();
        return this;
    }

    public Table bottom() {
        this.layout.bottom();
        return this;
    }

    public Table right() {
        this.layout.right();
        return this;
    }

    public Table debug() {
        this.layout.debug();
        return this;
    }

    public Table debugTable() {
        this.layout.debugTable();
        return this;
    }

    public Table debugCell() {
        this.layout.debugCell();
        return this;
    }

    public Table debugWidget() {
        this.layout.debugWidget();
        return this;
    }

    public Table debug(BaseTableLayout.Debug debug) {
        this.layout.debug(debug);
        return this;
    }

    public BaseTableLayout.Debug getDebug() {
        return this.layout.getDebug();
    }

    public Value getPadTopValue() {
        return this.layout.getPadTopValue();
    }

    public float getPadTop() {
        return this.layout.getPadTop();
    }

    public Value getPadLeftValue() {
        return this.layout.getPadLeftValue();
    }

    public float getPadLeft() {
        return this.layout.getPadLeft();
    }

    public Value getPadBottomValue() {
        return this.layout.getPadBottomValue();
    }

    public float getPadBottom() {
        return this.layout.getPadBottom();
    }

    public Value getPadRightValue() {
        return this.layout.getPadRightValue();
    }

    public float getPadRight() {
        return this.layout.getPadRight();
    }

    public float getPadX() {
        return this.layout.getPadLeft() + this.layout.getPadRight();
    }

    public float getPadY() {
        return this.layout.getPadTop() + this.layout.getPadBottom();
    }

    public int getAlign() {
        return this.layout.getAlign();
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public void setRound(boolean round) {
        this.layout.round = round;
    }

    public static void drawDebug(Stage stage) {
        if (TableToolkit.drawDebug) {
            drawDebug(stage.getActors(), stage.getSpriteBatch());
        }
    }

    private static void drawDebug(Array<Actor> actors, SpriteBatch batch) {
        int n = actors.size;
        for (int i = 0; i < n; i++) {
            Actor actor = actors.get(i);
            if (actor.isVisible()) {
                if (actor instanceof Table) {
                    ((Table) actor).layout.drawDebug(batch);
                }
                if (actor instanceof Group) {
                    drawDebug(((Group) actor).getChildren(), batch);
                }
            }
        }
    }
}
