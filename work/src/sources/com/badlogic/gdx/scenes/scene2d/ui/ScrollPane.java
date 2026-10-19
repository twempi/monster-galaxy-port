package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

/* JADX INFO: loaded from: classes.dex */
public class ScrollPane extends WidgetGroup {
    float amountX;
    float amountY;
    float areaHeight;
    float areaWidth;
    boolean cancelTouchFocus;
    private boolean clamp;
    private boolean disableX;
    private boolean disableY;
    int draggingPointer;
    float fadeAlpha;
    float fadeAlphaSeconds;
    float fadeDelay;
    float fadeDelaySeconds;
    private boolean fadeScrollBars;
    boolean flickScroll;
    private ActorGestureListener flickScrollListener;
    float flingTime;
    float flingTimer;
    private boolean forceOverscrollX;
    private boolean forceOverscrollY;
    final Rectangle hKnobBounds;
    final Rectangle hScrollBounds;
    final Vector2 lastPoint;
    float maxX;
    float maxY;
    private float overscrollDistance;
    private float overscrollSpeedMax;
    private float overscrollSpeedMin;
    private boolean overscrollX;
    private boolean overscrollY;
    private final Rectangle scissorBounds;
    boolean scrollX;
    boolean scrollY;
    private boolean scrollbarsOnTop;
    private boolean smoothScrolling;
    private ScrollPaneStyle style;
    boolean touchScrollH;
    boolean touchScrollV;
    final Rectangle vKnobBounds;
    final Rectangle vScrollBounds;
    float velocityX;
    float velocityY;
    float visualAmountX;
    float visualAmountY;
    private Actor widget;
    private final Rectangle widgetAreaBounds;
    private final Rectangle widgetCullingArea;

    public ScrollPane(Actor widget) {
        this(widget, new ScrollPaneStyle());
    }

    public ScrollPane(Actor widget, Skin skin) {
        this(widget, (ScrollPaneStyle) skin.get(ScrollPaneStyle.class));
    }

    public ScrollPane(Actor widget, Skin skin, String styleName) {
        this(widget, (ScrollPaneStyle) skin.get(styleName, ScrollPaneStyle.class));
    }

    public ScrollPane(Actor widget, ScrollPaneStyle style) {
        this.hScrollBounds = new Rectangle();
        this.vScrollBounds = new Rectangle();
        this.hKnobBounds = new Rectangle();
        this.vKnobBounds = new Rectangle();
        this.widgetAreaBounds = new Rectangle();
        this.widgetCullingArea = new Rectangle();
        this.scissorBounds = new Rectangle();
        this.lastPoint = new Vector2();
        this.fadeScrollBars = true;
        this.smoothScrolling = true;
        this.fadeAlphaSeconds = 1.0f;
        this.fadeDelaySeconds = 1.0f;
        this.cancelTouchFocus = true;
        this.flickScroll = true;
        this.overscrollX = true;
        this.overscrollY = true;
        this.flingTime = 1.0f;
        this.overscrollDistance = 50.0f;
        this.overscrollSpeedMin = 30.0f;
        this.overscrollSpeedMax = 200.0f;
        this.clamp = true;
        this.draggingPointer = -1;
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.widget = widget;
        this.style = style;
        if (widget != null) {
            setWidget(widget);
        }
        setWidth(150.0f);
        setHeight(150.0f);
        addCaptureListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.1
            private float handlePosition;

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (ScrollPane.this.draggingPointer != -1) {
                    return false;
                }
                if (pointer == 0 && button != 0) {
                    return false;
                }
                ScrollPane.this.getStage().setScrollFocus(ScrollPane.this);
                if (!ScrollPane.this.flickScroll) {
                    ScrollPane.this.resetFade();
                }
                if (ScrollPane.this.fadeAlpha == 0.0f) {
                    return false;
                }
                if (ScrollPane.this.scrollX && ScrollPane.this.hScrollBounds.contains(x, y)) {
                    event.stop();
                    ScrollPane.this.resetFade();
                    if (ScrollPane.this.hKnobBounds.contains(x, y)) {
                        ScrollPane.this.lastPoint.set(x, y);
                        this.handlePosition = ScrollPane.this.hKnobBounds.x;
                        ScrollPane.this.touchScrollH = true;
                        ScrollPane.this.draggingPointer = pointer;
                        return true;
                    }
                    ScrollPane.this.setScrollX(((x >= ScrollPane.this.hKnobBounds.x ? 1 : -1) * Math.max(ScrollPane.this.areaWidth * 0.9f, ScrollPane.this.maxX * 0.1f)) + ScrollPane.this.amountX);
                    return true;
                }
                if (!ScrollPane.this.scrollY || !ScrollPane.this.vScrollBounds.contains(x, y)) {
                    return false;
                }
                event.stop();
                ScrollPane.this.resetFade();
                if (ScrollPane.this.vKnobBounds.contains(x, y)) {
                    ScrollPane.this.lastPoint.set(x, y);
                    this.handlePosition = ScrollPane.this.vKnobBounds.y;
                    ScrollPane.this.touchScrollV = true;
                    ScrollPane.this.draggingPointer = pointer;
                    return true;
                }
                ScrollPane.this.setScrollY(((y < ScrollPane.this.vKnobBounds.y ? 1 : -1) * Math.max(ScrollPane.this.areaHeight * 0.9f, ScrollPane.this.maxY * 0.1f)) + ScrollPane.this.amountY);
                return true;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == ScrollPane.this.draggingPointer) {
                    ScrollPane.this.draggingPointer = -1;
                    ScrollPane.this.touchScrollH = false;
                    ScrollPane.this.touchScrollV = false;
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pointer == ScrollPane.this.draggingPointer) {
                    if (ScrollPane.this.touchScrollH) {
                        float delta = x - ScrollPane.this.lastPoint.x;
                        float scrollH = this.handlePosition + delta;
                        this.handlePosition = scrollH;
                        ScrollPane.this.setScrollPercentX((Math.min((ScrollPane.this.hScrollBounds.x + ScrollPane.this.hScrollBounds.width) - ScrollPane.this.hKnobBounds.width, Math.max(ScrollPane.this.hScrollBounds.x, scrollH)) - ScrollPane.this.hScrollBounds.x) / (ScrollPane.this.hScrollBounds.width - ScrollPane.this.hKnobBounds.width));
                        ScrollPane.this.lastPoint.set(x, y);
                        return;
                    }
                    if (ScrollPane.this.touchScrollV) {
                        float delta2 = y - ScrollPane.this.lastPoint.y;
                        float scrollV = this.handlePosition + delta2;
                        this.handlePosition = scrollV;
                        ScrollPane.this.setScrollPercentY(1.0f - ((Math.min((ScrollPane.this.vScrollBounds.y + ScrollPane.this.vScrollBounds.height) - ScrollPane.this.vKnobBounds.height, Math.max(ScrollPane.this.vScrollBounds.y, scrollV)) - ScrollPane.this.vScrollBounds.y) / (ScrollPane.this.vScrollBounds.height - ScrollPane.this.vKnobBounds.height)));
                        ScrollPane.this.lastPoint.set(x, y);
                    }
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean mouseMoved(InputEvent event, float x, float y) {
                if (!ScrollPane.this.flickScroll) {
                    ScrollPane.this.resetFade();
                    return false;
                }
                return false;
            }
        });
        this.flickScrollListener = new ActorGestureListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.2
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                ScrollPane.this.resetFade();
                ScrollPane.this.amountX -= deltaX;
                ScrollPane.this.amountY += deltaY;
                ScrollPane.this.clamp();
                ScrollPane.this.cancelTouchFocusedChild(event);
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
            public void fling(InputEvent event, float x, float y, int button) {
                if (Math.abs(x) > 150.0f) {
                    ScrollPane.this.flingTimer = ScrollPane.this.flingTime;
                    ScrollPane.this.velocityX = x;
                    ScrollPane.this.cancelTouchFocusedChild(event);
                }
                if (Math.abs(y) > 150.0f) {
                    ScrollPane.this.flingTimer = ScrollPane.this.flingTime;
                    ScrollPane.this.velocityY = -y;
                    ScrollPane.this.cancelTouchFocusedChild(event);
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener, com.badlogic.gdx.scenes.scene2d.EventListener
            public boolean handle(Event event) {
                if (!super.handle(event)) {
                    return false;
                }
                if (((InputEvent) event).getType() == InputEvent.Type.touchDown) {
                    ScrollPane.this.flingTimer = 0.0f;
                }
                return true;
            }
        };
        addListener(this.flickScrollListener);
        addListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.3
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                ScrollPane.this.resetFade();
                if (ScrollPane.this.scrollY) {
                    ScrollPane.this.setScrollY(ScrollPane.this.amountY + ((Math.max(ScrollPane.this.areaHeight * 0.9f, ScrollPane.this.maxY * 0.1f) / 4.0f) * amount));
                    return true;
                }
                if (ScrollPane.this.scrollX) {
                    ScrollPane.this.setScrollX(ScrollPane.this.amountX + ((Math.max(ScrollPane.this.areaWidth * 0.9f, ScrollPane.this.maxX * 0.1f) / 4.0f) * amount));
                    return true;
                }
                return true;
            }
        });
    }

    void resetFade() {
        this.fadeAlpha = this.fadeAlphaSeconds;
        this.fadeDelay = this.fadeDelaySeconds;
    }

    void cancelTouchFocusedChild(InputEvent event) {
        Stage stage;
        if (this.cancelTouchFocus && (stage = getStage()) != null) {
            stage.cancelTouchFocus(this.flickScrollListener, this);
        }
    }

    void clamp() {
        if (this.clamp) {
            scrollX(this.overscrollX ? MathUtils.clamp(this.amountX, -this.overscrollDistance, this.maxX + this.overscrollDistance) : MathUtils.clamp(this.amountX, 0.0f, this.maxX));
            scrollY(this.overscrollY ? MathUtils.clamp(this.amountY, -this.overscrollDistance, this.maxY + this.overscrollDistance) : MathUtils.clamp(this.amountY, 0.0f, this.maxY));
        }
    }

    public void setStyle(ScrollPaneStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.style = style;
        invalidateHierarchy();
    }

    public ScrollPaneStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void act(float delta) {
        super.act(delta);
        boolean panning = this.flickScrollListener.getGestureDetector().isPanning();
        if (this.fadeAlpha > 0.0f && this.fadeScrollBars && !panning && !this.touchScrollH && !this.touchScrollV) {
            this.fadeDelay -= this.fadeDelaySeconds * delta;
            if (this.fadeDelay <= 0.0f) {
                this.fadeAlpha = Math.max(0.0f, this.fadeAlpha - (this.fadeAlphaSeconds * delta));
            }
        }
        if (this.flingTimer > 0.0f) {
            resetFade();
            float alpha = this.flingTimer / this.flingTime;
            this.amountX -= (this.velocityX * alpha) * delta;
            this.amountY -= (this.velocityY * alpha) * delta;
            clamp();
            if (this.amountX == (-this.overscrollDistance)) {
                this.velocityX = 0.0f;
            }
            if (this.amountX >= this.maxX + this.overscrollDistance) {
                this.velocityX = 0.0f;
            }
            if (this.amountY == (-this.overscrollDistance)) {
                this.velocityY = 0.0f;
            }
            if (this.amountY >= this.maxY + this.overscrollDistance) {
                this.velocityY = 0.0f;
            }
            this.flingTimer -= delta;
            if (this.flingTimer <= 0.0f) {
                this.velocityX = 0.0f;
                this.velocityY = 0.0f;
            }
        }
        if (!this.smoothScrolling || this.flingTimer > 0.0f || this.touchScrollH || this.touchScrollV || panning) {
            if (this.visualAmountX != this.amountX) {
                visualScrollX(this.amountX);
            }
            if (this.visualAmountY != this.amountY) {
                visualScrollY(this.amountY);
            }
        } else {
            if (this.visualAmountX != this.amountX) {
                if (this.visualAmountX < this.amountX) {
                    visualScrollX(Math.min(this.amountX, this.visualAmountX + Math.max(150.0f * delta, (this.amountX - this.visualAmountX) * 5.0f * delta)));
                } else {
                    visualScrollX(Math.max(this.amountX, this.visualAmountX - Math.max(150.0f * delta, ((this.visualAmountX - this.amountX) * 5.0f) * delta)));
                }
            }
            if (this.visualAmountY != this.amountY) {
                if (this.visualAmountY < this.amountY) {
                    visualScrollY(Math.min(this.amountY, this.visualAmountY + Math.max(150.0f * delta, (this.amountY - this.visualAmountY) * 5.0f * delta)));
                } else {
                    visualScrollY(Math.max(this.amountY, this.visualAmountY - Math.max(150.0f * delta, ((this.visualAmountY - this.amountY) * 5.0f) * delta)));
                }
            }
        }
        if (!panning) {
            if (this.overscrollX && this.scrollX) {
                if (this.amountX < 0.0f) {
                    resetFade();
                    this.amountX += (this.overscrollSpeedMin + (((this.overscrollSpeedMax - this.overscrollSpeedMin) * (-this.amountX)) / this.overscrollDistance)) * delta;
                    if (this.amountX > 0.0f) {
                        scrollX(0.0f);
                    }
                } else if (this.amountX > this.maxX) {
                    resetFade();
                    this.amountX -= (this.overscrollSpeedMin + (((this.overscrollSpeedMax - this.overscrollSpeedMin) * (-(this.maxX - this.amountX))) / this.overscrollDistance)) * delta;
                    if (this.amountX < this.maxX) {
                        scrollX(this.maxX);
                    }
                }
            }
            if (this.overscrollY && this.scrollY) {
                if (this.amountY < 0.0f) {
                    resetFade();
                    this.amountY += (this.overscrollSpeedMin + (((this.overscrollSpeedMax - this.overscrollSpeedMin) * (-this.amountY)) / this.overscrollDistance)) * delta;
                    if (this.amountY > 0.0f) {
                        scrollY(0.0f);
                        return;
                    }
                    return;
                }
                if (this.amountY > this.maxY) {
                    resetFade();
                    this.amountY -= (this.overscrollSpeedMin + (((this.overscrollSpeedMax - this.overscrollSpeedMin) * (-(this.maxY - this.amountY))) / this.overscrollDistance)) * delta;
                    if (this.amountY < this.maxY) {
                        scrollY(this.maxY);
                    }
                }
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public void layout() {
        float widgetWidth;
        float widgetHeight;
        Drawable bg = this.style.background;
        Drawable hScrollKnob = this.style.hScrollKnob;
        Drawable vScrollKnob = this.style.vScrollKnob;
        float bgLeftWidth = 0.0f;
        float bgRightWidth = 0.0f;
        float bgTopHeight = 0.0f;
        float bgBottomHeight = 0.0f;
        if (bg != null) {
            bgLeftWidth = bg.getLeftWidth();
            bgRightWidth = bg.getRightWidth();
            bgTopHeight = bg.getTopHeight();
            bgBottomHeight = bg.getBottomHeight();
        }
        float width = getWidth();
        float height = getHeight();
        float scrollbarHeight = hScrollKnob != null ? hScrollKnob.getMinHeight() : 0.0f;
        if (this.style.hScroll != null) {
            scrollbarHeight = Math.max(scrollbarHeight, this.style.hScroll.getMinHeight());
        }
        float scrollbarWidth = vScrollKnob != null ? vScrollKnob.getMinWidth() : 0.0f;
        if (this.style.vScroll != null) {
            scrollbarWidth = Math.max(scrollbarWidth, this.style.vScroll.getMinWidth());
        }
        this.areaWidth = (width - bgLeftWidth) - bgRightWidth;
        this.areaHeight = (height - bgTopHeight) - bgBottomHeight;
        if (this.widget != null) {
            if (this.widget instanceof Layout) {
                Layout layout = (Layout) this.widget;
                widgetWidth = layout.getPrefWidth();
                widgetHeight = layout.getPrefHeight();
            } else {
                widgetWidth = this.widget.getWidth();
                widgetHeight = this.widget.getHeight();
            }
            this.scrollX = this.forceOverscrollX || (widgetWidth > this.areaWidth && !this.disableX);
            this.scrollY = this.forceOverscrollY || (widgetHeight > this.areaHeight && !this.disableY);
            boolean fade = this.fadeScrollBars;
            if (!fade) {
                if (this.scrollY) {
                    this.areaWidth -= scrollbarWidth;
                    if (!this.scrollX && widgetWidth > this.areaWidth && !this.disableX) {
                        this.scrollX = true;
                    }
                }
                if (this.scrollX) {
                    this.areaHeight -= scrollbarHeight;
                    if (!this.scrollY && widgetHeight > this.areaHeight && !this.disableY) {
                        this.scrollY = true;
                        this.areaWidth -= scrollbarWidth;
                    }
                }
            }
            this.widgetAreaBounds.set(bgLeftWidth, bgBottomHeight, this.areaWidth, this.areaHeight);
            if (fade) {
                if (this.scrollX) {
                    this.areaHeight -= scrollbarHeight;
                }
                if (this.scrollY) {
                    this.areaWidth -= scrollbarWidth;
                }
            } else if (this.scrollbarsOnTop) {
                if (this.scrollX) {
                    this.widgetAreaBounds.height += scrollbarHeight;
                }
                if (this.scrollY) {
                    this.widgetAreaBounds.width += scrollbarWidth;
                }
            } else if (this.scrollX) {
                this.widgetAreaBounds.y += scrollbarHeight;
            }
            float widgetWidth2 = this.disableX ? width : Math.max(this.areaWidth, widgetWidth);
            float widgetHeight2 = this.disableY ? height : Math.max(this.areaHeight, widgetHeight);
            this.maxX = widgetWidth2 - this.areaWidth;
            this.maxY = widgetHeight2 - this.areaHeight;
            if (fade) {
                if (this.scrollX) {
                    this.maxY -= scrollbarHeight;
                }
                if (this.scrollY) {
                    this.maxX -= scrollbarWidth;
                }
            }
            scrollX(MathUtils.clamp(this.amountX, 0.0f, this.maxX));
            scrollY(MathUtils.clamp(this.amountY, 0.0f, this.maxY));
            if (this.scrollX) {
                if (hScrollKnob != null) {
                    float hScrollHeight = this.style.hScroll != null ? this.style.hScroll.getMinHeight() : hScrollKnob.getMinHeight();
                    this.hScrollBounds.set(bgLeftWidth, bgBottomHeight, this.areaWidth, hScrollHeight);
                    this.hKnobBounds.width = Math.max(hScrollKnob.getMinWidth(), (int) ((this.hScrollBounds.width * this.areaWidth) / widgetWidth2));
                    this.hKnobBounds.height = hScrollKnob.getMinHeight();
                    this.hKnobBounds.x = this.hScrollBounds.x + ((int) ((this.hScrollBounds.width - this.hKnobBounds.width) * getScrollPercentX()));
                    this.hKnobBounds.y = this.hScrollBounds.y;
                } else {
                    this.hScrollBounds.set(0.0f, 0.0f, 0.0f, 0.0f);
                    this.hKnobBounds.set(0.0f, 0.0f, 0.0f, 0.0f);
                }
            }
            if (this.scrollY) {
                if (vScrollKnob != null) {
                    float vScrollWidth = this.style.vScroll != null ? this.style.vScroll.getMinWidth() : vScrollKnob.getMinWidth();
                    this.vScrollBounds.set((width - bgRightWidth) - vScrollWidth, (height - bgTopHeight) - this.areaHeight, vScrollWidth, this.areaHeight);
                    this.vKnobBounds.width = vScrollKnob.getMinWidth();
                    this.vKnobBounds.height = Math.max(vScrollKnob.getMinHeight(), (int) ((this.vScrollBounds.height * this.areaHeight) / widgetHeight2));
                    this.vKnobBounds.x = (width - bgRightWidth) - vScrollKnob.getMinWidth();
                    this.vKnobBounds.y = this.vScrollBounds.y + ((int) ((this.vScrollBounds.height - this.vKnobBounds.height) * (1.0f - getScrollPercentY())));
                } else {
                    this.vScrollBounds.set(0.0f, 0.0f, 0.0f, 0.0f);
                    this.vKnobBounds.set(0.0f, 0.0f, 0.0f, 0.0f);
                }
            }
            if (this.widget.getWidth() != widgetWidth2 || this.widget.getHeight() != widgetHeight2) {
                this.widget.setWidth(widgetWidth2);
                this.widget.setHeight(widgetHeight2);
                if (this.widget instanceof Layout) {
                    Layout layout2 = (Layout) this.widget;
                    layout2.invalidate();
                    layout2.validate();
                    return;
                }
                return;
            }
            if (this.widget instanceof Layout) {
                ((Layout) this.widget).validate();
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        float y;
        if (this.widget != null) {
            validate();
            applyTransform(batch, computeTransform());
            if (this.scrollX) {
                this.hKnobBounds.x = this.hScrollBounds.x + ((int) ((this.hScrollBounds.width - this.hKnobBounds.width) * getScrollPercentX()));
            }
            if (this.scrollY) {
                this.vKnobBounds.y = this.vScrollBounds.y + ((int) ((this.vScrollBounds.height - this.vKnobBounds.height) * (1.0f - getScrollPercentY())));
            }
            float y2 = this.widgetAreaBounds.y;
            if (!this.scrollY) {
                y = y2 - ((int) this.maxY);
            } else {
                y = y2 - ((int) (this.maxY - this.visualAmountY));
            }
            if (!this.fadeScrollBars && this.scrollbarsOnTop && this.scrollX) {
                float scrollbarHeight = this.style.hScrollKnob != null ? this.style.hScrollKnob.getMinHeight() : 0.0f;
                if (this.style.hScroll != null) {
                    scrollbarHeight = Math.max(scrollbarHeight, this.style.hScroll.getMinHeight());
                }
                y += scrollbarHeight;
            }
            float x = this.widgetAreaBounds.x;
            if (this.scrollX) {
                x -= (int) this.visualAmountX;
            }
            this.widget.setPosition(x, y);
            if (this.widget instanceof Cullable) {
                this.widgetCullingArea.x = (-this.widget.getX()) + this.widgetAreaBounds.x;
                this.widgetCullingArea.y = (-this.widget.getY()) + this.widgetAreaBounds.y;
                this.widgetCullingArea.width = this.widgetAreaBounds.width;
                this.widgetCullingArea.height = this.widgetAreaBounds.height;
                ((Cullable) this.widget).setCullingArea(this.widgetCullingArea);
            }
            ScissorStack.calculateScissors(getStage().getCamera(), batch.getTransformMatrix(), this.widgetAreaBounds, this.scissorBounds);
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            if (this.style.background != null) {
                this.style.background.draw(batch, 0.0f, 0.0f, getWidth(), getHeight());
            }
            batch.flush();
            if (ScissorStack.pushScissors(this.scissorBounds)) {
                drawChildren(batch, parentAlpha);
                ScissorStack.popScissors();
            }
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha * Interpolation.fade.apply(this.fadeAlpha / this.fadeAlphaSeconds));
            if (this.scrollX && this.scrollY && this.style.corner != null) {
                this.style.corner.draw(batch, this.hScrollBounds.width + this.hScrollBounds.x, this.hScrollBounds.y, this.vScrollBounds.width, this.vScrollBounds.y);
            }
            if (this.scrollX) {
                if (this.style.hScroll != null) {
                    this.style.hScroll.draw(batch, this.hScrollBounds.x, this.hScrollBounds.y, this.hScrollBounds.width, this.hScrollBounds.height);
                }
                if (this.style.hScrollKnob != null) {
                    this.style.hScrollKnob.draw(batch, this.hKnobBounds.x, this.hKnobBounds.y, this.hKnobBounds.width, this.hKnobBounds.height);
                }
            }
            if (this.scrollY) {
                if (this.style.vScroll != null) {
                    this.style.vScroll.draw(batch, this.vScrollBounds.x, this.vScrollBounds.y, this.vScrollBounds.width, this.vScrollBounds.height);
                }
                if (this.style.vScrollKnob != null) {
                    this.style.vScrollKnob.draw(batch, this.vKnobBounds.x, this.vKnobBounds.y, this.vKnobBounds.width, this.vKnobBounds.height);
                }
            }
            resetTransform(batch);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        if (this.widget instanceof Layout) {
            return ((Layout) this.widget).getPrefWidth();
        }
        return 150.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        if (this.widget instanceof Layout) {
            return ((Layout) this.widget).getPrefHeight();
        }
        return 150.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinWidth() {
        return 0.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getMinHeight() {
        return 0.0f;
    }

    public void setWidget(Actor widget) {
        if (this.widget != null) {
            super.removeActor(this.widget);
        }
        this.widget = widget;
        if (widget != null) {
            super.addActor(widget);
        }
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
        if (actor != this.widget) {
            return false;
        }
        setWidget(null);
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y, boolean touchable) {
        if (x < 0.0f || x >= getWidth() || y < 0.0f || y >= getHeight()) {
            return null;
        }
        if (this.scrollX && this.hScrollBounds.contains(x, y)) {
            return this;
        }
        return (this.scrollY && this.vScrollBounds.contains(x, y)) ? this : super.hit(x, y, touchable);
    }

    protected void scrollX(float pixelsX) {
        this.amountX = pixelsX;
    }

    protected void scrollY(float pixelsY) {
        this.amountY = pixelsY;
    }

    protected void visualScrollX(float pixelsX) {
        this.visualAmountX = pixelsX;
    }

    protected void visualScrollY(float pixelsY) {
        this.visualAmountY = pixelsY;
    }

    public void setScrollX(float pixels) {
        scrollX(MathUtils.clamp(pixels, 0.0f, this.maxX));
    }

    public float getScrollX() {
        return this.amountX;
    }

    public void setScrollY(float pixels) {
        scrollY(MathUtils.clamp(pixels, 0.0f, this.maxY));
    }

    public float getScrollY() {
        return this.amountY;
    }

    public float getVisualScrollX() {
        if (this.scrollX) {
            return this.visualAmountX;
        }
        return 0.0f;
    }

    public float getVisualScrollY() {
        if (this.scrollY) {
            return this.visualAmountY;
        }
        return 0.0f;
    }

    public float getScrollPercentX() {
        return MathUtils.clamp(this.amountX / this.maxX, 0.0f, 1.0f);
    }

    public void setScrollPercentX(float percentX) {
        scrollX(this.maxX * MathUtils.clamp(percentX, 0.0f, 1.0f));
    }

    public float getScrollPercentY() {
        return MathUtils.clamp(this.amountY / this.maxY, 0.0f, 1.0f);
    }

    public void setScrollPercentY(float percentY) {
        scrollY(this.maxY * MathUtils.clamp(percentY, 0.0f, 1.0f));
    }

    public void setFlickScroll(boolean flickScroll) {
        if (this.flickScroll != flickScroll) {
            this.flickScroll = flickScroll;
            if (flickScroll) {
                addListener(this.flickScrollListener);
            } else {
                removeListener(this.flickScrollListener);
            }
            invalidate();
        }
    }

    public void scrollTo(float x, float y, float width, float height) {
        float amountX = this.amountX;
        if (x + width > this.areaWidth + amountX) {
            amountX = (x + width) - this.areaWidth;
        }
        if (x < amountX) {
            amountX = x;
        }
        scrollX(MathUtils.clamp(amountX, 0.0f, this.maxX));
        float amountY = this.amountY;
        if (amountY > ((this.maxY - y) - height) + this.areaHeight) {
            amountY = ((this.maxY - y) - height) + this.areaHeight;
        }
        if (amountY < this.maxY - y) {
            amountY = this.maxY - y;
        }
        scrollY(MathUtils.clamp(amountY, 0.0f, this.maxY));
    }

    public void scrollToCenter(float x, float y, float width, float height) {
        float amountX = this.amountX;
        if (x + width > this.areaWidth + amountX) {
            amountX = (x + width) - this.areaWidth;
        }
        if (x < amountX) {
            amountX = x;
        }
        scrollX(MathUtils.clamp(amountX, 0.0f, this.maxX));
        float amountY = this.amountY;
        float centerY = ((this.maxY - y) + (this.areaHeight / 2.0f)) - (height / 2.0f);
        if (amountY < centerY - (this.areaHeight / 4.0f) || amountY > (this.areaHeight / 4.0f) + centerY) {
            amountY = centerY;
        }
        scrollY(MathUtils.clamp(amountY, 0.0f, this.maxY));
    }

    public float getMaxX() {
        return this.maxX;
    }

    public float getMaxY() {
        return this.maxY;
    }

    public float getScrollBarHeight() {
        if (this.style.hScrollKnob == null || !this.scrollX) {
            return 0.0f;
        }
        return this.style.hScrollKnob.getMinHeight();
    }

    public float getScrollBarWidth() {
        if (this.style.vScrollKnob == null || !this.scrollY) {
            return 0.0f;
        }
        return this.style.vScrollKnob.getMinWidth();
    }

    public boolean isScrollX() {
        return this.scrollX;
    }

    public boolean isScrollY() {
        return this.scrollY;
    }

    public void setScrollingDisabled(boolean x, boolean y) {
        this.disableX = x;
        this.disableY = y;
    }

    public boolean isDragging() {
        return this.draggingPointer != -1;
    }

    public boolean isPanning() {
        return this.flickScrollListener.getGestureDetector().isPanning();
    }

    public boolean isFlinging() {
        return this.flingTimer > 0.0f;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityX() {
        if (this.flingTimer <= 0.0f) {
            return 0.0f;
        }
        float alpha = this.flingTimer / this.flingTime;
        float alpha2 = alpha * alpha * alpha;
        return this.velocityX * alpha2 * alpha2 * alpha2;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityY() {
        return this.velocityY;
    }

    public void setOverscroll(boolean overscrollX, boolean overscrollY) {
        this.overscrollX = overscrollX;
        this.overscrollY = overscrollY;
    }

    public void setupOverscroll(float distance, float speedMin, float speedMax) {
        this.overscrollDistance = distance;
        this.overscrollSpeedMin = speedMin;
        this.overscrollSpeedMax = speedMax;
    }

    public void setForceOverscroll(boolean x, boolean y) {
        this.forceOverscrollX = x;
        this.forceOverscrollY = y;
    }

    public void setFlingTime(float flingTime) {
        this.flingTime = flingTime;
    }

    public void setClamp(boolean clamp) {
        this.clamp = clamp;
    }

    public void setFadeScrollBars(boolean fadeScrollBars) {
        if (this.fadeScrollBars != fadeScrollBars) {
            this.fadeScrollBars = fadeScrollBars;
            if (!fadeScrollBars) {
                this.fadeAlpha = this.fadeAlphaSeconds;
            }
            invalidate();
        }
    }

    public void setupFadeScrollBars(float fadeAlphaSeconds, float fadeDelaySeconds) {
        this.fadeAlphaSeconds = fadeAlphaSeconds;
        this.fadeDelaySeconds = fadeDelaySeconds;
    }

    public void setSmoothScrolling(boolean smoothScrolling) {
        this.smoothScrolling = smoothScrolling;
    }

    public void setScrollbarsOnTop(boolean scrollbarsOnTop) {
        this.scrollbarsOnTop = scrollbarsOnTop;
        invalidate();
    }

    public void setCancelTouchFocus(boolean cancelTouchFocus) {
        this.cancelTouchFocus = cancelTouchFocus;
    }

    public static class ScrollPaneStyle {
        public Drawable background;
        public Drawable corner;
        public Drawable hScroll;
        public Drawable hScrollKnob;
        public Drawable vScroll;
        public Drawable vScrollKnob;

        public ScrollPaneStyle() {
        }

        public ScrollPaneStyle(Drawable background, Drawable hScroll, Drawable hScrollKnob, Drawable vScroll, Drawable vScrollKnob) {
            this.background = background;
            this.hScroll = hScroll;
            this.hScrollKnob = hScrollKnob;
            this.vScroll = vScroll;
            this.vScrollKnob = vScrollKnob;
        }

        public ScrollPaneStyle(ScrollPaneStyle style) {
            this.background = style.background;
            this.hScroll = style.hScroll;
            this.hScrollKnob = style.hScrollKnob;
            this.vScroll = style.vScroll;
            this.vScrollKnob = style.vScrollKnob;
        }
    }
}
