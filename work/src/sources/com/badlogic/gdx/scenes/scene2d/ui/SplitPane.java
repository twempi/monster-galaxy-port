package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: classes.dex */
public class SplitPane extends WidgetGroup {
    private Rectangle firstScissors;
    private Actor firstWidget;
    private Rectangle firstWidgetBounds;
    Rectangle handleBounds;
    Vector2 handlePosition;
    Vector2 lastPoint;
    float maxAmount;
    float minAmount;
    private float oldSplitAmount;
    private Rectangle secondScissors;
    private Actor secondWidget;
    private Rectangle secondWidgetBounds;
    float splitAmount;
    SplitPaneStyle style;
    boolean vertical;

    public SplitPane(Actor firstWidget, Actor secondWidget, boolean vertical, Skin skin) {
        this(firstWidget, secondWidget, vertical, skin, "default-" + (vertical ? "vertical" : "horizontal"));
    }

    public SplitPane(Actor firstWidget, Actor secondWidget, boolean vertical, Skin skin, String styleName) {
        this(firstWidget, secondWidget, vertical, (SplitPaneStyle) skin.get(styleName, SplitPaneStyle.class));
    }

    public SplitPane(Actor firstWidget, Actor secondWidget, boolean vertical, SplitPaneStyle style) {
        this.splitAmount = 0.5f;
        this.maxAmount = 1.0f;
        this.firstWidgetBounds = new Rectangle();
        this.secondWidgetBounds = new Rectangle();
        this.handleBounds = new Rectangle();
        this.firstScissors = new Rectangle();
        this.secondScissors = new Rectangle();
        this.lastPoint = new Vector2();
        this.handlePosition = new Vector2();
        this.firstWidget = firstWidget;
        this.secondWidget = secondWidget;
        this.vertical = vertical;
        setStyle(style);
        setFirstWidget(firstWidget);
        setSecondWidget(secondWidget);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        initialize();
    }

    private void initialize() {
        addListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.SplitPane.1
            int draggingPointer = -1;

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (this.draggingPointer != -1) {
                    return false;
                }
                if ((pointer == 0 && button != 0) || !SplitPane.this.handleBounds.contains(x, y)) {
                    return false;
                }
                this.draggingPointer = pointer;
                SplitPane.this.lastPoint.set(x, y);
                SplitPane.this.handlePosition.set(SplitPane.this.handleBounds.x, SplitPane.this.handleBounds.y);
                return true;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == this.draggingPointer) {
                    this.draggingPointer = -1;
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pointer == this.draggingPointer) {
                    Drawable handle = SplitPane.this.style.handle;
                    if (!SplitPane.this.vertical) {
                        float delta = x - SplitPane.this.lastPoint.x;
                        float availWidth = SplitPane.this.getWidth() - handle.getMinWidth();
                        float dragX = SplitPane.this.handlePosition.x + delta;
                        SplitPane.this.handlePosition.x = dragX;
                        SplitPane.this.splitAmount = Math.min(availWidth, Math.max(0.0f, dragX)) / availWidth;
                        if (SplitPane.this.splitAmount < SplitPane.this.minAmount) {
                            SplitPane.this.splitAmount = SplitPane.this.minAmount;
                        }
                        if (SplitPane.this.splitAmount > SplitPane.this.maxAmount) {
                            SplitPane.this.splitAmount = SplitPane.this.maxAmount;
                        }
                        SplitPane.this.lastPoint.set(x, y);
                    } else {
                        float delta2 = y - SplitPane.this.lastPoint.y;
                        float availHeight = SplitPane.this.getHeight() - handle.getMinHeight();
                        float dragY = SplitPane.this.handlePosition.y + delta2;
                        SplitPane.this.handlePosition.y = dragY;
                        SplitPane.this.splitAmount = 1.0f - (Math.min(availHeight, Math.max(0.0f, dragY)) / availHeight);
                        if (SplitPane.this.splitAmount < SplitPane.this.minAmount) {
                            SplitPane.this.splitAmount = SplitPane.this.minAmount;
                        }
                        if (SplitPane.this.splitAmount > SplitPane.this.maxAmount) {
                            SplitPane.this.splitAmount = SplitPane.this.maxAmount;
                        }
                        SplitPane.this.lastPoint.set(x, y);
                    }
                    SplitPane.this.invalidate();
                }
            }
        });
    }

    public void setStyle(SplitPaneStyle style) {
        this.style = style;
        invalidateHierarchy();
    }

    public SplitPaneStyle getStyle() {
        return this.style;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        if (!this.vertical) {
            calculateHorizBoundsAndPositions();
        } else {
            calculateVertBoundsAndPositions();
        }
        Actor actor = this.firstWidget;
        Rectangle firstWidgetBounds = this.firstWidgetBounds;
        if (actor != 0) {
            actor.setX(firstWidgetBounds.x);
            actor.setY(firstWidgetBounds.y);
            if (actor.getWidth() != firstWidgetBounds.width || actor.getHeight() != firstWidgetBounds.height) {
                actor.setWidth(firstWidgetBounds.width);
                actor.setHeight(firstWidgetBounds.height);
                if (actor instanceof Layout) {
                    Layout layout = (Layout) actor;
                    layout.invalidate();
                    layout.validate();
                }
            } else if (actor instanceof Layout) {
                ((Layout) actor).validate();
            }
        }
        Actor actor2 = this.secondWidget;
        Rectangle secondWidgetBounds = this.secondWidgetBounds;
        if (actor2 != 0) {
            actor2.setX(secondWidgetBounds.x);
            actor2.setY(secondWidgetBounds.y);
            if (actor2.getWidth() != secondWidgetBounds.width || actor2.getHeight() != secondWidgetBounds.height) {
                actor2.setWidth(secondWidgetBounds.width);
                actor2.setHeight(secondWidgetBounds.height);
                if (actor2 instanceof Layout) {
                    Layout layout2 = (Layout) actor2;
                    layout2.invalidate();
                    layout2.validate();
                    return;
                }
                return;
            }
            if (actor2 instanceof Layout) {
                ((Layout) actor2).validate();
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        float width = (this.firstWidget instanceof Layout ? ((Layout) this.firstWidget).getPrefWidth() : this.firstWidget.getWidth()) + (this.secondWidget instanceof Layout ? ((Layout) this.secondWidget).getPrefWidth() : this.secondWidget.getWidth());
        return !this.vertical ? width + this.style.handle.getMinWidth() : width;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        float height = (this.firstWidget instanceof Layout ? ((Layout) this.firstWidget).getPrefHeight() : this.firstWidget.getHeight()) + (this.secondWidget instanceof Layout ? ((Layout) this.secondWidget).getPrefHeight() : this.secondWidget.getHeight());
        return this.vertical ? height + this.style.handle.getMinHeight() : height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinWidth() {
        return 0.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinHeight() {
        return 0.0f;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    private void calculateHorizBoundsAndPositions() {
        Drawable handle = this.style.handle;
        float height = getHeight();
        float availWidth = getWidth() - handle.getMinWidth();
        float leftAreaWidth = (int) (this.splitAmount * availWidth);
        float rightAreaWidth = availWidth - leftAreaWidth;
        float handleWidth = handle.getMinWidth();
        this.firstWidgetBounds.set(0.0f, 0.0f, leftAreaWidth, height);
        this.secondWidgetBounds.set(leftAreaWidth + handleWidth, 0.0f, rightAreaWidth, height);
        this.handleBounds.set(leftAreaWidth, 0.0f, handleWidth, height);
    }

    private void calculateVertBoundsAndPositions() {
        Drawable handle = this.style.handle;
        float width = getWidth();
        float height = getHeight();
        float availHeight = height - handle.getMinHeight();
        float topAreaHeight = (int) (this.splitAmount * availHeight);
        float bottomAreaHeight = availHeight - topAreaHeight;
        float handleHeight = handle.getMinHeight();
        this.firstWidgetBounds.set(0.0f, height - topAreaHeight, width, topAreaHeight);
        this.secondWidgetBounds.set(0.0f, 0.0f, width, bottomAreaHeight);
        this.handleBounds.set(0.0f, bottomAreaHeight, width, handleHeight);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        validate();
        Color color = getColor();
        Drawable handle = this.style.handle;
        applyTransform(batch, computeTransform());
        Matrix4 transform = batch.getTransformMatrix();
        if (this.firstWidget != null) {
            ScissorStack.calculateScissors(getStage().getCamera(), transform, this.firstWidgetBounds, this.firstScissors);
            if (ScissorStack.pushScissors(this.firstScissors)) {
                if (this.firstWidget.isVisible()) {
                    this.firstWidget.draw(batch, color.a * parentAlpha);
                }
                batch.flush();
                ScissorStack.popScissors();
            }
        }
        if (this.secondWidget != null) {
            ScissorStack.calculateScissors(getStage().getCamera(), transform, this.secondWidgetBounds, this.secondScissors);
            if (ScissorStack.pushScissors(this.secondScissors)) {
                if (this.secondWidget.isVisible()) {
                    this.secondWidget.draw(batch, color.a * parentAlpha);
                }
                batch.flush();
                ScissorStack.popScissors();
            }
        }
        batch.setColor(color.r, color.g, color.b, color.a);
        handle.draw(batch, this.handleBounds.x, this.handleBounds.y, this.handleBounds.width, this.handleBounds.height);
        resetTransform(batch);
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

    public void setFirstWidget(Actor widget) {
        if (this.firstWidget != null) {
            super.removeActor(this.firstWidget);
        }
        this.firstWidget = widget;
        if (widget != null) {
            super.addActor(widget);
        }
        invalidate();
    }

    public void setSecondWidget(Actor widget) {
        if (this.secondWidget != null) {
            super.removeActor(this.secondWidget);
        }
        this.secondWidget = widget;
        if (widget != null) {
            super.addActor(widget);
        }
        invalidate();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void addActor(Actor actor) {
        throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void addActorAt(int index, Actor actor) {
        throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void addActorBefore(Actor actorBefore, Actor actor) {
        throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public boolean removeActor(Actor actor) {
        throw new UnsupportedOperationException("Use ScrollPane#setWidget(null).");
    }

    public static class SplitPaneStyle {
        public Drawable handle;

        public SplitPaneStyle() {
        }

        public SplitPaneStyle(Drawable handle) {
            this.handle = handle;
        }

        public SplitPaneStyle(SplitPaneStyle style) {
            this.handle = style.handle;
        }
    }
}
