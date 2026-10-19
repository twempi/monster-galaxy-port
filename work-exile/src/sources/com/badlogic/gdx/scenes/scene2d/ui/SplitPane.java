package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SplitPane extends Group implements Layout {
    Actor firstWidget;
    Rectangle firstWidgetBounds;
    Rectangle handleBounds;
    Vector2 handlePos;
    boolean invalidated;
    Vector2 lastPoint;
    float maxAmount;
    float minAmount;
    float oldSplitAmount;
    float prefHeight;
    float prefWidth;
    Rectangle[] scissors;
    Actor secondWidget;
    Rectangle secondWidgetBounds;
    float splitAmount;
    Stage stage;
    final SplitPaneStyle style;
    boolean touchDrag;
    boolean vertical;

    public SplitPane(String name, Stage stage, Actor firstWidget, Actor secondWidget, boolean vertical, int prefWidth, int prefHeight, SplitPaneStyle style) {
        super(name);
        this.invalidated = false;
        this.splitAmount = 0.5f;
        this.minAmount = 0.0f;
        this.maxAmount = 1.0f;
        this.oldSplitAmount = 0.0f;
        this.firstWidgetBounds = new Rectangle();
        this.secondWidgetBounds = new Rectangle();
        this.handleBounds = new Rectangle();
        this.scissors = new Rectangle[]{new Rectangle(), new Rectangle()};
        this.touchDrag = false;
        this.lastPoint = new Vector2();
        this.handlePos = new Vector2();
        this.stage = stage;
        this.style = style;
        this.firstWidget = firstWidget;
        this.secondWidget = secondWidget;
        float f = prefWidth;
        this.width = f;
        this.prefWidth = f;
        float f2 = prefHeight;
        this.height = f2;
        this.prefHeight = f2;
        this.vertical = vertical;
        addActor(firstWidget);
        addActor(secondWidget);
        layout();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        if (this.firstWidget instanceof Layout) {
            Layout layout = (Layout) this.firstWidget;
            layout.layout();
            this.firstWidget.width = layout.getPrefWidth();
            this.firstWidget.height = layout.getPrefHeight();
        }
        if (this.secondWidget instanceof Layout) {
            Layout layout2 = (Layout) this.secondWidget;
            layout2.layout();
            this.secondWidget.width = layout2.getPrefWidth();
            this.secondWidget.height = layout2.getPrefHeight();
        }
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
        if (this.firstWidget instanceof Layout) {
            ((Layout) this.firstWidget).invalidate();
        }
        if (this.secondWidget instanceof Layout) {
            ((Layout) this.secondWidget).invalidate();
        }
        this.invalidated = true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        return this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        return this.prefWidth;
    }

    private void calculateBoundsAndPositions(Matrix4 transform) {
        if (this.oldSplitAmount != this.splitAmount) {
            this.oldSplitAmount = this.splitAmount;
            invalidate();
        }
        if (!this.vertical) {
            calculateHorizBoundsAndPositions();
        } else {
            calculateVertBoundsAndPositions();
        }
        boolean layoutFirst = false;
        boolean layoutSecond = false;
        if (this.firstWidget.width != this.firstWidgetBounds.width || this.firstWidget.height != this.firstWidgetBounds.height) {
            layoutFirst = true;
        }
        if (this.secondWidget.width != this.secondWidgetBounds.width || this.secondWidget.height != this.secondWidgetBounds.height) {
            layoutSecond = true;
        }
        this.firstWidget.x = this.firstWidgetBounds.x;
        this.firstWidget.y = this.firstWidgetBounds.y;
        this.firstWidget.width = this.firstWidgetBounds.width;
        this.firstWidget.height = this.firstWidgetBounds.height;
        this.secondWidget.x = this.secondWidgetBounds.x;
        this.secondWidget.y = this.secondWidgetBounds.y;
        this.secondWidget.width = this.secondWidgetBounds.width;
        this.secondWidget.height = this.secondWidgetBounds.height;
        if (layoutFirst && (this.firstWidget instanceof Layout)) {
            ((Layout) this.firstWidget).invalidate();
        }
        if (layoutSecond && (this.secondWidget instanceof Layout)) {
            ((Layout) this.secondWidget).invalidate();
        }
        ScissorStack.calculateScissors(this.stage.getCamera(), transform, this.firstWidgetBounds, this.scissors[0]);
        ScissorStack.calculateScissors(this.stage.getCamera(), transform, this.secondWidgetBounds, this.scissors[1]);
    }

    private void calculateHorizBoundsAndPositions() {
        NinePatch handle = this.style.handle;
        float availWidth = this.width - handle.getTotalWidth();
        float leftAreaWidth = (int) (this.splitAmount * availWidth);
        float rightAreaWidth = availWidth - leftAreaWidth;
        float handleWidth = handle.getTotalWidth();
        this.firstWidgetBounds.set(0.0f, 0.0f, leftAreaWidth, this.height);
        this.secondWidgetBounds.set(leftAreaWidth + handleWidth, 0.0f, rightAreaWidth, this.height);
        this.handleBounds.set(leftAreaWidth, 0.0f, handleWidth, this.height);
    }

    private void calculateVertBoundsAndPositions() {
        NinePatch handle = this.style.handle;
        float availHeight = this.height - handle.getTotalHeight();
        float topAreaHeight = (int) (this.splitAmount * availHeight);
        float bottomAreaHeight = availHeight - topAreaHeight;
        float handleHeight = handle.getTotalHeight();
        this.firstWidgetBounds.set(0.0f, this.height - topAreaHeight, this.width, topAreaHeight);
        this.secondWidgetBounds.set(0.0f, 0.0f, this.width, bottomAreaHeight);
        this.handleBounds.set(0.0f, bottomAreaHeight, this.width, handleHeight);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        NinePatch handle = this.style.handle;
        setupTransform(batch);
        calculateBoundsAndPositions(batch.getTransformMatrix());
        for (int i = 0; i < this.children.size(); i++) {
            ScissorStack.pushScissors(this.scissors[i]);
            drawChild(this.children.get(i), batch, parentAlpha);
            ScissorStack.popScissors();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a);
        handle.draw(batch, this.handleBounds.x, this.handleBounds.y, this.handleBounds.width, this.handleBounds.height);
        if (this.invalidated) {
            layout();
        }
        resetTransform(batch);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (this.handleBounds.contains(x, y)) {
            this.touchDrag = true;
            this.lastPoint.set(x, y);
            this.handlePos.set(this.handleBounds.x, this.handleBounds.y);
            focus(this, 0);
            return true;
        }
        return super.touchDown(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (this.touchDrag) {
            focus(null, 0);
            this.touchDrag = false;
            return true;
        }
        return super.touchUp(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        NinePatch handle = this.style.handle;
        if (pointer != 0) {
            return false;
        }
        if (this.touchDrag) {
            if (!this.vertical) {
                float delta = x - this.lastPoint.x;
                float availWidth = this.width - handle.getTotalWidth();
                float dragX = this.handlePos.x + delta;
                this.handlePos.x = dragX;
                this.splitAmount = Math.min(availWidth, Math.max(0.0f, dragX)) / availWidth;
                if (this.splitAmount < this.minAmount) {
                    this.splitAmount = this.minAmount;
                }
                if (this.splitAmount > this.maxAmount) {
                    this.splitAmount = this.maxAmount;
                }
                invalidate();
                this.lastPoint.set(x, y);
            } else {
                float delta2 = y - this.lastPoint.y;
                float availHeight = this.height - handle.getTotalHeight();
                float dragY = this.handlePos.y + delta2;
                this.handlePos.y = dragY;
                this.splitAmount = 1.0f - (Math.min(availHeight, Math.max(0.0f, dragY)) / availHeight);
                if (this.splitAmount < this.minAmount) {
                    this.splitAmount = this.minAmount;
                }
                if (this.splitAmount > this.maxAmount) {
                    this.splitAmount = this.maxAmount;
                }
                invalidate();
                this.lastPoint.set(x, y);
            }
            return true;
        }
        return super.touchDragged(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
            return null;
        }
        return this;
    }

    public static class SplitPaneStyle {
        public final NinePatch handle;

        public SplitPaneStyle(NinePatch handle) {
            this.handle = handle;
        }
    }

    public void setSplitAmount(float split) {
        this.splitAmount = Math.max(Math.min(this.maxAmount, split), this.minAmount);
        invalidate();
    }

    public float getSplit() {
        return this.splitAmount;
    }

    public void setMinSplitAmount(float minAmount) {
        if (minAmount < 0.0f) {
            throw new GdxRuntimeException("minAmount has to be >= 0");
        }
        if (minAmount >= this.maxAmount) {
            throw new GdxRuntimeException("minAmount has to be < maxAmount");
        }
        this.minAmount = minAmount;
    }

    public void setMaxSplitAmount(float maxAmount) {
        if (maxAmount > 1.0f) {
            throw new GdxRuntimeException("maxAmount has to be >= 0");
        }
        if (maxAmount <= this.minAmount) {
            throw new GdxRuntimeException("maxAmount has to be > minAmount");
        }
        this.maxAmount = maxAmount;
    }

    public void setWidgets(Actor firstWidget, Actor secondWidget) {
        if (firstWidget == null) {
            throw new IllegalArgumentException("firstWidget must not be null");
        }
        if (secondWidget == null) {
            throw new IllegalArgumentException("secondWidget must not be null");
        }
        removeActor(this.firstWidget);
        removeActor(this.secondWidget);
        this.firstWidget = firstWidget;
        this.secondWidget = secondWidget;
        addActor(firstWidget);
        addActor(secondWidget);
        invalidate();
    }
}
