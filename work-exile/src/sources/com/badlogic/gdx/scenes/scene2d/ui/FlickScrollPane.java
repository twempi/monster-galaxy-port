package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FlickScrollPane extends Group implements Layout {
    private static final int SCROLLTIMEDEFAULT = 350;
    float hScrollAmount;
    boolean hasHScroll;
    boolean hasVScroll;
    DecelerateInterpolator interpolator;
    boolean invalidated;
    Vector2 lastPoint;
    public Listener listener;
    public boolean overscrolls;
    float prefHeight;
    float prefWidth;
    Rectangle scissorBounds;
    private Vector2 scrollStartPoint;
    float scrolledtime;
    private boolean scrolling;
    private long scrollingStarted;
    float scrollstarth;
    float scrollstartv;
    int scrolltime;
    float smoothscrolldestx;
    float smoothscrolldesty;
    Stage stage;
    Vector3 tmp;
    float vScrollAmount;
    Actor widget;
    Rectangle widgetAreaBounds;

    public interface Listener {
        void onFlinged();

        void onSmoothScrollComplete();
    }

    public FlickScrollPane(String name, Stage stage, Actor widget, int prefWidth, int prefHeight) {
        super(name);
        this.invalidated = true;
        this.widgetAreaBounds = new Rectangle();
        this.scissorBounds = new Rectangle();
        this.hScrollAmount = 0.0f;
        this.vScrollAmount = 0.0f;
        this.hasHScroll = false;
        this.hasVScroll = false;
        this.lastPoint = new Vector2();
        this.scrolling = false;
        this.scrollStartPoint = new Vector2();
        this.overscrolls = false;
        this.tmp = new Vector3();
        this.scrolledtime = 0.0f;
        this.scrolltime = SCROLLTIMEDEFAULT;
        this.smoothscrolldesty = -1.0f;
        this.smoothscrolldestx = -1.0f;
        this.interpolator = DecelerateInterpolator.$(DecelerateInterpolator.DEFAULT_FACTOR);
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
        this.hasHScroll = false;
        this.hasVScroll = false;
        if (this.widget.width > this.width) {
            this.hasHScroll = true;
        }
        if (this.widget.height > this.height) {
            this.hasVScroll = true;
        }
        this.widgetAreaBounds.set(0.0f, 0.0f, this.width, this.height);
        this.widget.y = (-(!this.hasVScroll ? (int) (this.widget.height - this.height) : 0)) - (this.hasVScroll ? (int) ((this.widget.height - this.height) * (1.0f - this.vScrollAmount)) : 0);
        this.widget.x = -(this.hasHScroll ? (int) ((this.widget.width - this.width) * this.hScrollAmount) : 0);
        ScissorStack.calculateScissors(this.stage.getCamera(), batchTransform, this.widgetAreaBounds, this.scissorBounds);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        setupTransform(batch);
        if (this.invalidated) {
            layout();
        }
        calculateBoundsAndPositions(batch.getTransformMatrix());
        ScissorStack.pushScissors(this.scissorBounds);
        drawChildren(batch, parentAlpha);
        ScissorStack.popScissors();
        resetTransform(batch);
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
        if (pointer == 0) {
            if (!((!this.touchable) | (hit(x, y) == null))) {
                this.lastPoint.set(x, y);
                this.scrolling = true;
                this.scrollingStarted = System.currentTimeMillis();
                this.scrollStartPoint.x = x;
                this.scrollStartPoint.y = y;
                focus(this, 0);
                return true;
            }
        }
        Gdx.app.log("", "HERE d");
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        focus(null, 0);
        if (pointer != 0) {
            return false;
        }
        if (this.scrolling) {
            this.scrolling = false;
            long timetaken = System.currentTimeMillis() - this.scrollingStarted;
            float diffx = Math.max(this.scrollStartPoint.x, x) - Math.min(this.scrollStartPoint.x, x);
            float diffy = Math.max(this.scrollStartPoint.y, y) - Math.min(this.scrollStartPoint.y, y);
            System.out.println(String.format("Scrolled for %dms, %f on x, %f on y ", Long.valueOf(timetaken), Float.valueOf(diffx), Float.valueOf(diffy)));
            if (this.vScrollAmount > 1.0f) {
                smoothScrollTo(0.0f, 1.0f);
                return true;
            }
            if (this.vScrollAmount < 0.0f) {
                smoothScrollTo(0.0f, 0.0f);
                return true;
            }
            if (diffx >= 10.0f || diffy >= 10.0f) {
                float yaccel = diffy / timetaken;
                float xaccel = diffx / timetaken;
                System.out.println(String.format("y accel %f x accel %f", Float.valueOf(yaccel), Float.valueOf(xaccel)));
                if (yaccel > 1.0f || xaccel > 1.0f) {
                    System.out.println("fling");
                    if (this.scrollStartPoint.y > y) {
                        smoothScrollTo(this.hScrollAmount, Math.max(this.vScrollAmount + ((1.0f / this.widget.height) * (-(4.0f * diffy))), 0.0f), (int) (timetaken * 1.5d));
                    } else {
                        smoothScrollTo(this.hScrollAmount, Math.min(this.vScrollAmount + ((1.0f / this.widget.height) * 4.0f * diffy), 1.0f), (int) (timetaken * 1.5d));
                    }
                    if (this.listener != null) {
                        this.listener.onFlinged();
                    }
                }
            } else if (timetaken < 190) {
                System.out.println("User has clicked");
                for (Actor child : this.children) {
                    Vector2 point = new Vector2();
                    toChildCoordinates(child, x, y, point);
                    Actor hitchild = child.hit(point.x, point.y);
                    if (hitchild != null) {
                        hitchild.touchDown(point.x, point.y, pointer);
                        hitchild.touchUp(point.x, point.y, pointer);
                        Gdx.app.log("flick", hitchild.toString());
                        return true;
                    }
                }
            }
        }
        return super.touchUp(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        if (pointer != 0 || !this.touchable) {
            return false;
        }
        if (this.scrolling) {
            if (this.hasHScroll) {
                this.hScrollAmount -= (x - this.lastPoint.x) / (this.widget.width - this.width);
                if (this.overscrolls) {
                    this.hScrollAmount = Math.max(0.0f, this.hScrollAmount);
                    this.hScrollAmount = Math.min(1.0f, this.hScrollAmount);
                }
            }
            if (this.hasVScroll) {
                this.vScrollAmount += (y - this.lastPoint.y) / (this.widget.height - this.height);
                if (this.overscrolls) {
                    this.vScrollAmount = Math.max(0.0f, this.vScrollAmount);
                    this.vScrollAmount = Math.min(1.0f, this.vScrollAmount);
                }
            }
            this.lastPoint.set(x, y);
            return true;
        }
        Gdx.app.log("", "HERE dr!");
        return false;
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

    public void scrollHorizontalTo(float scroll) {
        Gdx.app.log("", "scrollHorizontalTo");
        this.hScrollAmount = scroll;
    }

    public float getHorizontalScrollAmount() {
        return this.hScrollAmount;
    }

    public void scrollVerticalTo(float scroll) {
        this.vScrollAmount = scroll;
    }

    public float getVerticalScrollAmount() {
        return this.vScrollAmount;
    }

    public void smoothScrollTo(float horizontal, float vertical) {
        smoothScrollTo(horizontal, vertical, SCROLLTIMEDEFAULT);
    }

    public void smoothScrollTo(float horizontal, float vertical, int duration) {
        this.smoothscrolldestx = horizontal;
        this.smoothscrolldesty = vertical;
        this.scrollstarth = this.hScrollAmount;
        this.scrollstartv = this.vScrollAmount;
        this.scrolledtime = 0.0f;
        this.scrolltime = duration;
    }

    public void cancelAnimation() {
        this.smoothscrolldestx = -1.0f;
        this.smoothscrolldesty = -1.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void act(float delta) {
        super.act(delta);
        if (this.smoothscrolldestx != -1.0f && this.smoothscrolldesty != -1.0f) {
            this.touchable = false;
            this.scrolledtime += 1000.0f * delta;
            float relscrolltime = (1.0f / this.scrolltime) * this.scrolledtime;
            if (this.smoothscrolldestx > this.scrollstarth) {
                this.hScrollAmount = this.scrollstarth + ((this.smoothscrolldestx - this.scrollstarth) * this.interpolator.getInterpolation(relscrolltime));
            } else {
                this.hScrollAmount = this.scrollstarth - ((this.scrollstarth - this.smoothscrolldestx) * this.interpolator.getInterpolation(relscrolltime));
            }
            if (this.smoothscrolldesty > this.scrollstartv) {
                this.vScrollAmount = this.scrollstartv + ((this.smoothscrolldesty - this.scrollstartv) * this.interpolator.getInterpolation(relscrolltime));
            } else {
                this.vScrollAmount = this.scrollstartv - ((this.scrollstartv - this.smoothscrolldesty) * this.interpolator.getInterpolation(relscrolltime));
            }
            if (this.scrolledtime >= this.scrolltime) {
                this.hScrollAmount = this.smoothscrolldestx;
                this.vScrollAmount = this.smoothscrolldesty;
                this.smoothscrolldestx = -1.0f;
                this.smoothscrolldesty = -1.0f;
                this.touchable = true;
                this.scrolledtime = 0.0f;
                Gdx.app.log("", "SCROLL OVER");
                if (this.listener != null) {
                    this.listener.onSmoothScrollComplete();
                }
            }
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
