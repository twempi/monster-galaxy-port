package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pools;

/* JADX INFO: loaded from: classes.dex */
public class Actor {
    private float height;
    private String name;
    private float originX;
    private float originY;
    private Group parent;
    private float rotation;
    private Stage stage;
    private float width;
    private float x;
    private float y;
    private final DelayedRemovalArray<EventListener> listeners = new DelayedRemovalArray<>(0);
    private final DelayedRemovalArray<EventListener> captureListeners = new DelayedRemovalArray<>(0);
    private final Array<Action> actions = new Array<>(0);
    private Touchable touchable = Touchable.enabled;
    private boolean visible = true;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private final Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public void draw(SpriteBatch batch, float parentAlpha) {
    }

    public void act(float delta) {
        int i = 0;
        int n = this.actions.size;
        while (i < n) {
            Action action = this.actions.get(i);
            if (action.act(delta)) {
                this.actions.removeIndex(i);
                action.setActor(null);
                i--;
                n--;
            }
            i++;
        }
    }

    public boolean fire(Event event) {
        boolean zIsCancelled;
        if (event.getStage() == null) {
            event.setStage(getStage());
        }
        event.setTarget(this);
        Array<Group> ancestors = (Array) Pools.obtain(Array.class);
        for (Group parent = getParent(); parent != null; parent = parent.getParent()) {
            ancestors.add(parent);
        }
        try {
            for (int i = ancestors.size - 1; i >= 0; i--) {
                Group currentTarget = ancestors.get(i);
                currentTarget.notify(event, true);
                if (event.isStopped()) {
                    zIsCancelled = event.isCancelled();
                    ancestors.clear();
                    Pools.free(ancestors);
                    return zIsCancelled;
                }
            }
            notify(event, true);
            if (event.isStopped()) {
                zIsCancelled = event.isCancelled();
                ancestors.clear();
                Pools.free(ancestors);
            } else {
                notify(event, false);
                if (!event.getBubbles()) {
                    zIsCancelled = event.isCancelled();
                    ancestors.clear();
                    Pools.free(ancestors);
                } else if (event.isStopped()) {
                    zIsCancelled = event.isCancelled();
                    ancestors.clear();
                    Pools.free(ancestors);
                } else {
                    int n = ancestors.size;
                    for (int i2 = 0; i2 < n; i2++) {
                        ancestors.get(i2).notify(event, false);
                        if (event.isStopped()) {
                            zIsCancelled = event.isCancelled();
                            ancestors.clear();
                            Pools.free(ancestors);
                        }
                    }
                    zIsCancelled = event.isCancelled();
                    ancestors.clear();
                    Pools.free(ancestors);
                }
            }
            return zIsCancelled;
        } catch (Throwable th) {
            ancestors.clear();
            Pools.free(ancestors);
            throw th;
        }
    }

    public boolean notify(Event event, boolean capture) {
        if (event.getTarget() == null) {
            throw new IllegalArgumentException("The event target cannot be null.");
        }
        DelayedRemovalArray<EventListener> listeners = capture ? this.captureListeners : this.listeners;
        if (listeners.size == 0) {
            return event.isCancelled();
        }
        event.setListenerActor(this);
        event.setCapture(capture);
        if (event.getStage() == null) {
            event.setStage(this.stage);
        }
        listeners.begin();
        int n = listeners.size;
        for (int i = 0; i < n; i++) {
            EventListener listener = listeners.get(i);
            if (listener.handle(event)) {
                event.handle();
                if (event instanceof InputEvent) {
                    InputEvent inputEvent = (InputEvent) event;
                    if (inputEvent.getType() == InputEvent.Type.touchDown) {
                        event.getStage().addTouchFocus(listener, this, inputEvent.getTarget(), inputEvent.getPointer(), inputEvent.getButton());
                    }
                }
            }
        }
        listeners.end();
        return event.isCancelled();
    }

    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && this.touchable != Touchable.enabled) {
            return null;
        }
        if (x < 0.0f || x >= this.width || y < 0.0f || y >= this.height) {
            this = null;
        }
        return this;
    }

    public boolean remove() {
        if (this.parent != null) {
            return this.parent.removeActor(this);
        }
        return false;
    }

    public boolean addListener(EventListener listener) {
        if (this.listeners.contains(listener, true)) {
            return false;
        }
        this.listeners.add(listener);
        return true;
    }

    public boolean removeListener(EventListener listener) {
        return this.listeners.removeValue(listener, true);
    }

    public Array<EventListener> getListeners() {
        return this.listeners;
    }

    public boolean addCaptureListener(EventListener listener) {
        if (!this.captureListeners.contains(listener, true)) {
            this.captureListeners.add(listener);
        }
        return true;
    }

    public boolean removeCaptureListener(EventListener listener) {
        return this.captureListeners.removeValue(listener, true);
    }

    public Array<EventListener> getCaptureListeners() {
        return this.captureListeners;
    }

    public void addAction(Action action) {
        action.setActor(this);
        this.actions.add(action);
    }

    public void removeAction(Action action) {
        if (this.actions.removeValue(action, true)) {
            action.setActor(null);
        }
    }

    public Array<Action> getActions() {
        return this.actions;
    }

    public void clearActions() {
        for (int i = this.actions.size - 1; i >= 0; i--) {
            this.actions.get(i).setActor(null);
        }
        this.actions.clear();
    }

    public Stage getStage() {
        return this.stage;
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isDescendantOf(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("actor cannot be null.");
        }
        for (Actor parent = this; parent != null; parent = parent.getParent()) {
            if (parent == actor) {
                return true;
            }
        }
        return false;
    }

    public boolean isAscendantOf(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("actor cannot be null.");
        }
        while (actor != null) {
            if (actor == this) {
                return true;
            }
            actor = actor.getParent();
        }
        return false;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public Group getParent() {
        return this.parent;
    }

    protected void setParent(Group parent) {
        this.parent = parent;
    }

    public Touchable getTouchable() {
        return this.touchable;
    }

    public void setTouchable(Touchable touchable) {
        this.touchable = touchable;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void translate(float x, float y) {
        setX(this.x + x);
        setY(this.y + y);
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getTop() {
        return getY() + getHeight();
    }

    public float getRight() {
        return getX() + getWidth();
    }

    public void setSize(float width, float height) {
        setWidth(width);
        setHeight(height);
    }

    public void size(float size) {
        setWidth(this.width + size);
        setHeight(this.height + size);
    }

    public void size(float width, float height) {
        setWidth(this.width + width);
        setHeight(this.height + height);
    }

    public void setBounds(float x, float y, float width, float height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public float getOriginX() {
        return this.originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return this.originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public void setOrigin(float originX, float originY) {
        setOriginX(originX);
        setOriginY(originY);
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
    }

    public void setScale(float scaleX, float scaleY) {
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    public void scale(float scale) {
        setScaleX(this.scaleX + scale);
        setScaleY(this.scaleY + scale);
    }

    public void scale(float scaleX, float scaleY) {
        setScaleX(this.scaleX + scaleX);
        setScaleY(this.scaleY + scaleY);
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float degrees) {
        this.rotation = degrees;
    }

    public void rotate(float amountInDegrees) {
        setRotation(this.rotation + amountInDegrees);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void toFront() {
        setZIndex(Integer.MAX_VALUE);
    }

    public void toBack() {
        setZIndex(0);
    }

    public void setZIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("ZIndex cannot be < 0.");
        }
        Group parent = getParent();
        if (parent != null) {
            Array<Actor> children = parent.getChildren();
            if (children.size != 1 && children.removeValue(this, true)) {
                if (index >= children.size) {
                    children.add(this);
                } else {
                    children.insert(index, this);
                }
            }
        }
    }

    public int getZIndex() {
        Group parent = getParent();
        if (parent == null) {
            return -1;
        }
        return parent.getChildren().indexOf(this, true);
    }

    public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
        Stage stage = getStage();
        return stage == null ? screenCoords : stageToLocalCoordinates(stage.screenToStageCoordinates(screenCoords));
    }

    public Vector2 stageToLocalCoordinates(Vector2 stageCoords) {
        if (this.parent != null) {
            this.parent.stageToLocalCoordinates(stageCoords);
            parentToLocalCoordinates(stageCoords);
        }
        return stageCoords;
    }

    public Vector2 localToStageCoordinates(Vector2 localCoords) {
        for (Actor actor = this; actor != null; actor = actor.getParent()) {
            if (actor.getRotation() != 0.0f || actor.getScaleX() != 1.0f || actor.getScaleY() != 1.0f) {
                throw new GdxRuntimeException("Only unrotated and unscaled actors may use this method.");
            }
            localCoords.x += actor.getX();
            localCoords.y += actor.getY();
        }
        return localCoords;
    }

    public Vector2 localToParentCoordinates(Vector2 localCoords) {
        if (getRotation() != 0.0f || getScaleX() != 1.0f || getScaleY() != 1.0f) {
            throw new GdxRuntimeException("Only unrotated and unscaled actors may use this method.");
        }
        localCoords.x += getX();
        localCoords.y += getY();
        return localCoords;
    }

    public Vector2 localToAscendantCoordinates(Actor ascendant, Vector2 localCoords) {
        Actor actor = this;
        while (actor.getParent() != null) {
            actor.localToParentCoordinates(localCoords);
            actor = actor.getParent();
            if (actor == ascendant) {
                break;
            }
        }
        return localCoords;
    }

    public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
        float rotation = getRotation();
        float scaleX = getScaleX();
        float scaleY = getScaleY();
        float childX = getX();
        float childY = getY();
        if (rotation != 0.0f) {
            float cos = (float) Math.cos(0.017453292f * rotation);
            float sin = (float) Math.sin(0.017453292f * rotation);
            float originX = getOriginX();
            float originY = getOriginY();
            if (scaleX == 1.0f && scaleY == 1.0f) {
                if (originX == 0.0f && originY == 0.0f) {
                    float tox = parentCoords.x - childX;
                    float toy = parentCoords.y - childY;
                    parentCoords.x = (tox * cos) + (toy * sin);
                    parentCoords.y = ((-sin) * tox) + (toy * cos);
                } else {
                    float worldOriginX = childX + originX;
                    float worldOriginY = childY + originY;
                    float fx = -originX;
                    float fy = -originY;
                    float x1 = ((cos * fx) - (sin * fy)) + worldOriginX;
                    float y1 = (sin * fx) + (cos * fy) + worldOriginY;
                    float tox2 = parentCoords.x - x1;
                    float toy2 = parentCoords.y - y1;
                    parentCoords.x = (tox2 * cos) + (toy2 * sin);
                    parentCoords.y = ((-sin) * tox2) + (toy2 * cos);
                }
            } else if (originX == 0.0f && originY == 0.0f) {
                float tox3 = parentCoords.x - childX;
                float toy3 = parentCoords.y - childY;
                parentCoords.x = ((tox3 * cos) + (toy3 * sin)) / scaleX;
                parentCoords.y = (((-sin) * tox3) + (toy3 * cos)) / scaleY;
            } else {
                float worldOriginX2 = childX + originX;
                float worldOriginY2 = childY + originY;
                float fx2 = (-originX) * scaleX;
                float fy2 = (-originY) * scaleY;
                float x2 = ((cos * fx2) - (sin * fy2)) + worldOriginX2;
                float y2 = (sin * fx2) + (cos * fy2) + worldOriginY2;
                float tox4 = parentCoords.x - x2;
                float toy4 = parentCoords.y - y2;
                parentCoords.x = ((tox4 * cos) + (toy4 * sin)) / scaleX;
                parentCoords.y = (((-sin) * tox4) + (toy4 * cos)) / scaleY;
            }
        } else if (scaleX == 1.0f && scaleY == 1.0f) {
            parentCoords.x -= childX;
            parentCoords.y -= childY;
        } else {
            float originX2 = getOriginX();
            float originY2 = getOriginY();
            if (originX2 == 0.0f && originY2 == 0.0f) {
                parentCoords.x = (parentCoords.x - childX) / scaleX;
                parentCoords.y = (parentCoords.y - childY) / scaleY;
            } else {
                parentCoords.x = (((parentCoords.x - childX) - originX2) / scaleX) + originX2;
                parentCoords.y = (((parentCoords.y - childY) - originY2) / scaleY) + originY2;
            }
        }
        return parentCoords;
    }

    public String toString() {
        int dotIndex;
        String name = this.name;
        if (name == null && (dotIndex = (name = getClass().getName()).lastIndexOf(46)) != -1) {
            name = name.substring(dotIndex + 1);
        }
        return name + " " + this.x + "," + this.y + " " + this.width + "x" + this.height;
    }
}
