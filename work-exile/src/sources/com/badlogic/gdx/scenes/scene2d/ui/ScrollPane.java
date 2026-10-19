package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ScrollPane extends Group implements Layout {
    float hScrollAmount;
    Rectangle hScrollBounds;
    Rectangle hScrollKnobBounds;
    float handlePos;
    boolean hasHScroll;
    boolean hasVScroll;
    boolean invalidated;
    Vector2 lastPoint;
    float prefHeight;
    float prefWidth;
    Rectangle scissorBounds;
    Stage stage;
    final ScrollPaneStyle style;
    Vector3 tmp;
    boolean touchScrollH;
    boolean touchScrollV;
    float vScrollAmount;
    Rectangle vScrollBounds;
    Rectangle vScrollKnobBounds;
    Actor widget;
    Rectangle widgetAreaBounds;

    public ScrollPane(String name, Stage stage, Actor widget, int prefWidth, int prefHeight, ScrollPaneStyle style) {
        super(name);
        this.invalidated = true;
        this.hScrollBounds = new Rectangle();
        this.vScrollBounds = new Rectangle();
        this.hScrollKnobBounds = new Rectangle();
        this.vScrollKnobBounds = new Rectangle();
        this.widgetAreaBounds = new Rectangle();
        this.scissorBounds = new Rectangle();
        this.hScrollAmount = 0.0f;
        this.vScrollAmount = 0.0f;
        this.hasHScroll = false;
        this.hasVScroll = false;
        this.touchScrollH = false;
        this.touchScrollV = false;
        this.lastPoint = new Vector2();
        this.tmp = new Vector3();
        this.handlePos = 0.0f;
        this.style = style;
        float f = prefWidth;
        this.width = f;
        this.prefWidth = f;
        float f2 = prefHeight;
        this.height = f2;
        this.prefHeight = f2;
        this.stage = stage;
        this.widget = widget;
        addActor(widget);
        layout();
    }

    private void calculateBoundsAndPositions(Matrix4 batchTransform) {
        NinePatch background = this.style.background;
        NinePatch hScrollKnob = this.style.hScrollKnob;
        NinePatch vScrollKnob = this.style.vScrollKnob;
        float areaWidth = (this.width - background.getLeftWidth()) - background.getRightWidth();
        float areaHeight = (this.height - background.getTopHeight()) - background.getBottomHeight();
        this.hasHScroll = false;
        this.hasVScroll = false;
        if (this.widget.width > areaWidth) {
            this.hasHScroll = true;
        }
        if (this.widget.height > areaHeight) {
            this.hasVScroll = true;
        }
        if (this.hasVScroll && this.widget.width > areaWidth - vScrollKnob.getTotalWidth()) {
            this.hasHScroll = true;
            areaWidth -= vScrollKnob.getTotalWidth();
        }
        if (this.hasHScroll && this.widget.height > areaHeight - hScrollKnob.getTotalHeight()) {
            this.hasVScroll = true;
            areaHeight -= hScrollKnob.getTotalHeight();
        }
        if (this.hasHScroll) {
            this.hScrollBounds.set(background.getLeftWidth(), background.getBottomHeight(), areaWidth, hScrollKnob.getTotalHeight());
            this.hScrollKnobBounds.width = Math.max(hScrollKnob.getTotalWidth(), (int) ((this.hScrollBounds.width * areaWidth) / this.widget.width));
            this.hScrollKnobBounds.height = hScrollKnob.getTotalHeight();
            this.hScrollKnobBounds.x = this.hScrollBounds.x + ((int) ((this.hScrollBounds.width - this.hScrollKnobBounds.width) * this.hScrollAmount));
            this.hScrollKnobBounds.y = this.hScrollBounds.y;
        }
        if (this.hasVScroll) {
            this.vScrollBounds.set((this.width - background.getRightWidth()) - vScrollKnob.getTotalWidth(), (this.height - background.getTopHeight()) - areaHeight, vScrollKnob.getTotalWidth(), areaHeight);
            this.vScrollKnobBounds.width = vScrollKnob.getTotalWidth();
            this.vScrollKnobBounds.height = Math.max(vScrollKnob.getTotalHeight(), (int) ((this.vScrollBounds.height * areaHeight) / this.widget.height));
            this.vScrollKnobBounds.x = this.vScrollBounds.x;
            this.vScrollKnobBounds.y = this.vScrollBounds.y + ((int) ((this.vScrollBounds.height - this.vScrollKnobBounds.height) * (1.0f - this.vScrollAmount)));
        }
        this.widgetAreaBounds.set(background.getLeftWidth(), (this.hasHScroll ? hScrollKnob.getTotalHeight() : 0.0f) + background.getBottomHeight(), areaWidth, areaHeight);
        this.widget.y = (this.widgetAreaBounds.y - (!this.hasVScroll ? (int) (this.widget.height - areaHeight) : 0)) - (this.hasVScroll ? (int) ((this.widget.height - areaHeight) * (1.0f - this.vScrollAmount)) : 0);
        this.widget.x = this.widgetAreaBounds.x - (this.hasHScroll ? (int) ((this.widget.width - areaWidth) * this.hScrollAmount) : 0);
        ScissorStack.calculateScissors(this.stage.getCamera(), batchTransform, this.widgetAreaBounds, this.scissorBounds);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        NinePatch background = this.style.background;
        NinePatch hScrollKnob = this.style.hScrollKnob;
        NinePatch hScroll = this.style.hScroll;
        NinePatch vScrollKnob = this.style.vScrollKnob;
        NinePatch vScroll = this.style.vScroll;
        setupTransform(batch);
        if (this.invalidated) {
            layout();
        }
        calculateBoundsAndPositions(batch.getTransformMatrix());
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        background.draw(batch, 0.0f, 0.0f, this.width, this.height);
        ScissorStack.pushScissors(this.scissorBounds);
        drawChildren(batch, parentAlpha);
        ScissorStack.popScissors();
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        if (this.hasHScroll) {
            hScroll.draw(batch, this.hScrollBounds.x, this.hScrollBounds.y, this.hScrollBounds.width, this.hScrollBounds.height);
            hScrollKnob.draw(batch, this.hScrollKnobBounds.x, this.hScrollKnobBounds.y, this.hScrollKnobBounds.width, this.hScrollKnobBounds.height);
        }
        if (this.hasVScroll) {
            vScroll.draw(batch, this.vScrollBounds.x, this.vScrollBounds.y, this.vScrollBounds.width, this.vScrollBounds.height);
            vScrollKnob.draw(batch, this.vScrollKnobBounds.x, this.vScrollKnobBounds.y, this.vScrollKnobBounds.width, this.vScrollKnobBounds.height);
        }
        resetTransform(batch);
    }

    public static class ScrollPaneStyle {
        public final NinePatch background;
        public final NinePatch hScroll;
        public final NinePatch hScrollKnob;
        public final NinePatch vScroll;
        public final NinePatch vScrollKnob;

        public ScrollPaneStyle(NinePatch backgroundPatch, NinePatch hScroll, NinePatch hScrollKnob, NinePatch vScroll, NinePatch vScrollKnob) {
            this.background = backgroundPatch;
            this.hScroll = hScroll;
            this.hScrollKnob = hScrollKnob;
            this.vScroll = vScroll;
            this.vScrollKnob = vScrollKnob;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        if (this.widget instanceof Layout) {
            Layout layout = (Layout) this.widget;
            this.widget.width = Math.max(this.width, layout.getPrefWidth());
            this.widget.height = Math.max(this.height, layout.getPrefHeight());
            layout.invalidate();
            layout.layout();
        }
        this.invalidated = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
        if (this.widget instanceof Layout) {
            ((Layout) this.widget).invalidate();
        }
        this.invalidated = true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        return this.prefWidth;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        return this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (this.hasHScroll && this.hScrollBounds.contains(x, y)) {
            if (this.hScrollKnobBounds.contains(x, y)) {
                this.lastPoint.set(x, y);
                this.handlePos = this.hScrollKnobBounds.x;
                this.touchScrollH = true;
                focus(this, 0);
            } else if (x < this.hScrollKnobBounds.x) {
                this.hScrollAmount = Math.max(0.0f, this.hScrollAmount - 0.1f);
            } else {
                this.hScrollAmount = Math.min(1.0f, this.hScrollAmount + 0.1f);
            }
            return true;
        }
        if (this.hasVScroll && this.vScrollBounds.contains(x, y)) {
            if (this.vScrollKnobBounds.contains(x, y)) {
                this.lastPoint.set(x, y);
                this.handlePos = this.vScrollKnobBounds.y;
                this.touchScrollV = true;
                focus(this, 0);
            } else if (y < this.vScrollKnobBounds.y) {
                this.vScrollAmount = Math.min(1.0f, this.vScrollAmount + 0.1f);
            } else {
                this.vScrollAmount = Math.max(0.0f, this.vScrollAmount - 0.1f);
            }
            return true;
        }
        if (this.widgetAreaBounds.contains(x, y)) {
            return super.touchDown(x, y, pointer);
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (this.touchScrollH || this.touchScrollV) {
            focus(null, 0);
            this.touchScrollH = false;
            this.touchScrollV = false;
            return true;
        }
        return super.touchUp(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (this.touchScrollH) {
            float delta = x - this.lastPoint.x;
            float scrollH = this.handlePos + delta;
            this.handlePos = scrollH;
            this.hScrollAmount = (Math.min((this.hScrollBounds.x + this.hScrollBounds.width) - this.hScrollKnobBounds.width, Math.max(this.hScrollBounds.x, scrollH)) - this.hScrollBounds.x) / (this.hScrollBounds.width - this.hScrollKnobBounds.width);
            this.lastPoint.set(x, y);
            return true;
        }
        if (this.touchScrollV) {
            float delta2 = y - this.lastPoint.y;
            float scrollV = this.handlePos + delta2;
            this.handlePos = scrollV;
            this.vScrollAmount = 1.0f - ((Math.min((this.vScrollBounds.y + this.vScrollBounds.height) - this.vScrollKnobBounds.height, Math.max(this.vScrollBounds.y, scrollV)) - this.vScrollBounds.y) / (this.vScrollBounds.height - this.vScrollKnobBounds.height));
            this.lastPoint.set(x, y);
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

    public void setWidget(Actor widget) {
        if (widget == null) {
            throw new IllegalArgumentException("widget must not be null");
        }
        removeActor(this.widget);
        this.widget = widget;
        addActor(widget);
        invalidate();
    }
}
