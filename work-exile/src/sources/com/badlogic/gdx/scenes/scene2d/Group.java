package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Group extends Actor {
    public static Texture debugTexture;
    protected final List<Actor> children;
    public Actor[] focusedActor;
    protected final List<Group> groups;
    protected final List<Actor> immutableChildren;
    protected final List<Group> immutableGroups;
    public Actor keyboardFocusedActor;
    public Actor lastTouchedChild;
    protected final ObjectMap<String, Actor> namesToActors;
    protected final Matrix4 oldBatchTransform;
    final Vector2 point;
    protected final Matrix3 scenetransform;
    public Actor scrollFocusedActor;
    protected final Matrix4 tmp4;
    protected final Matrix3 transform;
    public static boolean debug = false;
    static final Vector2 xAxis = new Vector2();
    static final Vector2 yAxis = new Vector2();
    static final Vector2 p = new Vector2();
    static final Vector2 ref = new Vector2();

    public Group() {
        this(null);
    }

    public Group(String name) {
        super(name);
        this.tmp4 = new Matrix4();
        this.oldBatchTransform = new Matrix4();
        this.scenetransform = new Matrix3();
        this.focusedActor = new Actor[20];
        this.keyboardFocusedActor = null;
        this.scrollFocusedActor = null;
        this.point = new Vector2();
        this.transform = new Matrix3();
        this.children = new ArrayList();
        this.immutableChildren = Collections.unmodifiableList(this.children);
        this.groups = new ArrayList();
        this.immutableGroups = Collections.unmodifiableList(this.groups);
        this.namesToActors = new ObjectMap<>();
    }

    protected void updateTransform() {
        if (this.originX != 0.0f || this.originY != 0.0f) {
            this.transform.setToTranslation(this.originX, this.originY);
        } else {
            this.transform.idt();
        }
        if (this.rotation != 0.0f) {
            this.transform.mul(this.scenetransform.setToRotation(this.rotation));
        }
        if (this.scaleX != 1.0f || this.scaleY != 1.0f) {
            this.transform.mul(this.scenetransform.setToScaling(this.scaleX, this.scaleY));
        }
        if (this.originX != 0.0f || this.originY != 0.0f) {
            this.transform.mul(this.scenetransform.setToTranslation(-this.originX, -this.originY));
        }
        this.transform.trn(this.x, this.y);
        if (this.parent != null) {
            this.scenetransform.set(this.parent.scenetransform);
            this.scenetransform.mul(this.transform);
        } else {
            this.scenetransform.set(this.transform);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void act(float delta) {
        super.act(delta);
        int i = 0;
        while (i < this.children.size()) {
            Actor child = this.children.get(i);
            child.act(delta);
            if (child.isMarkedToRemove()) {
                child.markToRemove(false);
                removeActor(child);
                i--;
            }
            i++;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (this.visible) {
            setupTransform(batch);
            drawChildren(batch, parentAlpha);
            resetTransform(batch);
        }
    }

    protected void setupTransform(SpriteBatch batch) {
        updateTransform();
        this.tmp4.set(this.scenetransform);
        if (debug && debugTexture != null && this.parent != null) {
            batch.draw(debugTexture, this.x, this.y, this.originX, this.originY, this.width == 0.0f ? 200.0f : this.width, this.height == 0.0f ? 200.0f : this.height, this.scaleX, this.scaleY, this.rotation, 0, 0, debugTexture.getWidth(), debugTexture.getHeight(), false, false);
        }
        batch.end();
        this.oldBatchTransform.set(batch.getTransformMatrix());
        batch.setTransformMatrix(this.tmp4);
        batch.begin();
    }

    protected void drawChildren(SpriteBatch batch, float parentAlpha) {
        int len = this.children.size();
        for (int i = 0; i < len; i++) {
            Actor child = this.children.get(i);
            if (child.visible) {
                child.draw(batch, this.color.a * parentAlpha);
            }
        }
        batch.flush();
    }

    protected void drawChild(Actor child, SpriteBatch batch, float parentAlpha) {
        if (child.visible) {
            child.draw(batch, this.color.a * parentAlpha);
        }
        batch.flush();
    }

    protected void resetTransform(SpriteBatch batch) {
        batch.end();
        batch.setTransformMatrix(this.oldBatchTransform);
        batch.begin();
    }

    public static void toChildCoordinates(Actor child, float x, float y, Vector2 out) {
        if (child.rotation == 0.0f) {
            if (child.scaleX == 1.0f && child.scaleY == 1.0f) {
                out.x = x - child.x;
                out.y = y - child.y;
                return;
            } else if (child.originX == 0.0f && child.originY == 0.0f) {
                out.x = (x - child.x) / child.scaleX;
                out.y = (y - child.y) / child.scaleY;
                return;
            } else {
                out.x = (((x - child.x) - child.originX) / child.scaleX) + child.originX;
                out.y = (((y - child.y) - child.originY) / child.scaleY) + child.originY;
                return;
            }
        }
        float cos = (float) Math.cos((float) Math.toRadians(child.rotation));
        float sin = (float) Math.sin((float) Math.toRadians(child.rotation));
        if (child.scaleX == 1.0f && child.scaleY == 1.0f) {
            if (child.originX == 0.0f && child.originY == 0.0f) {
                float tox = x - child.x;
                float toy = y - child.y;
                out.x = (tox * cos) + (toy * sin);
                out.y = ((-sin) * tox) + (toy * cos);
                return;
            }
            float worldOriginX = child.x + child.originX;
            float worldOriginY = child.y + child.originY;
            float fx = -child.originX;
            float fy = -child.originY;
            float x1 = (cos * fx) - (sin * fy);
            float y1 = (sin * fx) + (cos * fy);
            float tox2 = x - (x1 + worldOriginX);
            float toy2 = y - (y1 + worldOriginY);
            out.x = (tox2 * cos) + (toy2 * sin);
            out.y = ((-sin) * tox2) + (toy2 * cos);
            return;
        }
        if (child.originX == 0.0f && child.originY == 0.0f) {
            float tox3 = x - child.x;
            float toy3 = y - child.y;
            out.x = (tox3 * cos) + (toy3 * sin);
            out.y = ((-sin) * tox3) + (toy3 * cos);
            out.x /= child.scaleX;
            out.y /= child.scaleY;
            return;
        }
        float srefX = child.originX * child.scaleX;
        float srefY = child.originY * child.scaleY;
        float worldOriginX2 = child.x + child.originX;
        float worldOriginY2 = child.y + child.originY;
        float fx2 = -srefX;
        float fy2 = -srefY;
        float x2 = (cos * fx2) - (sin * fy2);
        float y2 = (sin * fx2) + (cos * fy2);
        float tox4 = x - (x2 + worldOriginX2);
        float toy4 = y - (y2 + worldOriginY2);
        out.x = (tox4 * cos) + (toy4 * sin);
        out.y = ((-sin) * tox4) + (toy4 * cos);
        out.x /= child.scaleX;
        out.y /= child.scaleY;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (!this.touchable) {
            return false;
        }
        if (debug) {
            Gdx.app.log("Group", this.name + ": " + x + ", " + y);
        }
        if (this.focusedActor[pointer] != null) {
            this.point.x = x;
            this.point.y = y;
            this.focusedActor[pointer].toLocalCoordinates(this.point);
            this.focusedActor[pointer].touchDown(this.point.x, this.point.y, pointer);
            return true;
        }
        int len = this.children.size() - 1;
        for (int i = len; i >= 0; i--) {
            Actor child = this.children.get(i);
            if (child.touchable) {
                toChildCoordinates(child, x, y, this.point);
                if (child.touchDown(this.point.x, this.point.y, pointer)) {
                    if (child instanceof Group) {
                        this.lastTouchedChild = ((Group) child).lastTouchedChild;
                    } else {
                        this.lastTouchedChild = child;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (!this.touchable) {
            return false;
        }
        if (this.focusedActor[pointer] != null) {
            this.point.x = x;
            this.point.y = y;
            this.focusedActor[pointer].toLocalCoordinates(this.point);
            this.focusedActor[pointer].touchUp(this.point.x, this.point.y, pointer);
            return true;
        }
        int len = this.children.size() - 1;
        for (int i = len; i >= 0; i--) {
            Actor child = this.children.get(i);
            if (child.touchable) {
                toChildCoordinates(child, x, y, this.point);
                if (child.touchUp(this.point.x, this.point.y, pointer)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchMoved(float x, float y) {
        if (!this.touchable) {
            return false;
        }
        int len = this.children.size() - 1;
        for (int i = len; i >= 0; i--) {
            Actor child = this.children.get(i);
            if (child.touchable) {
                toChildCoordinates(child, x, y, this.point);
                if (child.touchMoved(this.point.x, this.point.y)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        if (!this.touchable) {
            return false;
        }
        if (this.focusedActor[pointer] != null) {
            this.point.x = x;
            this.point.y = y;
            this.focusedActor[pointer].toLocalCoordinates(this.point);
            this.focusedActor[pointer].touchDragged(this.point.x, this.point.y, pointer);
            return true;
        }
        int len = this.children.size() - 1;
        for (int i = len; i >= 0; i--) {
            Actor child = this.children.get(i);
            if (child.touchable) {
                toChildCoordinates(child, x, y, this.point);
                if (child.touchDragged(this.point.x, this.point.y, pointer)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean scrolled(int amount) {
        if (this.scrollFocusedActor != null) {
            this.scrollFocusedActor.scrolled(amount);
            return false;
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean keyDown(int keycode) {
        if (this.keyboardFocusedActor != null) {
            return this.keyboardFocusedActor.keyDown(keycode);
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean keyUp(int keycode) {
        if (this.keyboardFocusedActor != null) {
            return this.keyboardFocusedActor.keyUp(keycode);
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean keyTyped(char character) {
        if (this.keyboardFocusedActor != null) {
            return this.keyboardFocusedActor.keyTyped(character);
        }
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        int len = this.children.size() - 1;
        for (int i = len; i >= 0; i--) {
            Actor child = this.children.get(i);
            toChildCoordinates(child, x, y, this.point);
            Actor hit = child.hit(this.point.x, this.point.y);
            if (hit != null) {
                return hit;
            }
        }
        return null;
    }

    public void addActor(Actor actor) {
        this.children.add(actor);
        if (actor instanceof Group) {
            this.groups.add((Group) actor);
        }
        if (actor.name != null) {
            this.namesToActors.put(actor.name, actor);
        }
        actor.parent = this;
    }

    public void addActorAt(int index, Actor actor) {
        this.children.add(index, actor);
        if (actor instanceof Group) {
            this.groups.add((Group) actor);
        }
        if (actor.name != null) {
            this.namesToActors.put(actor.name, actor);
        }
        actor.parent = this;
    }

    public void addActorBefore(Actor actorBefore, Actor actor) {
        int index = this.children.indexOf(actorBefore);
        this.children.add(index, actor);
        if (actor instanceof Group) {
            this.groups.add((Group) actor);
        }
        if (actor.name != null) {
            this.namesToActors.put(actor.name, actor);
        }
        actor.parent = this;
    }

    public void addActorAfter(Actor actorAfter, Actor actor) {
        int index = this.children.indexOf(actorAfter);
        if (index == this.children.size()) {
            this.children.add(actor);
        } else {
            this.children.add(index + 1, actor);
        }
        if (actor instanceof Group) {
            this.groups.add((Group) actor);
        }
        if (actor.name != null) {
            this.namesToActors.put(actor.name, actor);
        }
        actor.parent = this;
    }

    public void removeActor(Actor actor) {
        this.children.remove(actor);
        if (actor instanceof Group) {
            this.groups.remove((Group) actor);
        }
        if (actor.name != null) {
            this.namesToActors.remove(actor.name);
        }
        unfocusAll(actor);
    }

    public void removeActorRecursive(Actor actor) {
        if (this.children.remove(actor)) {
            if (actor instanceof Group) {
                this.groups.remove((Group) actor);
            }
            if (actor.name != null) {
                this.namesToActors.remove(actor.name);
            }
            unfocusAll(actor);
            return;
        }
        for (int i = 0; i < this.groups.size(); i++) {
            this.groups.get(i).removeActorRecursive(actor);
        }
    }

    public Actor findActor(String name) {
        Actor actor = this.namesToActors.get(name);
        if (actor == null) {
            int len = this.groups.size();
            for (int i = 0; i < len; i++) {
                actor = this.groups.get(i).findActor(name);
                if (actor != null) {
                    return actor;
                }
            }
        }
        return actor;
    }

    public boolean swapActor(int first, int second) {
        int maxIndex = this.children.size();
        if (first < 0 || first >= maxIndex || second < 0 || second >= maxIndex) {
            return false;
        }
        Collections.swap(this.children, first, second);
        return true;
    }

    public boolean swapActor(Actor first, Actor second) {
        int firstIndex = this.children.indexOf(first);
        int secondIndex = this.children.indexOf(second);
        if (firstIndex == -1 || secondIndex == -1) {
            return false;
        }
        Collections.swap(this.children, firstIndex, secondIndex);
        return true;
    }

    public List<Actor> getActors() {
        return this.immutableChildren;
    }

    public List<Group> getGroups() {
        return this.immutableGroups;
    }

    public void focus(Actor actor, int pointer) {
        this.focusedActor[pointer] = actor;
        if (this.parent != null) {
            this.parent.focus(actor, pointer);
        }
    }

    public void keyboardFocus(Actor actor) {
        this.keyboardFocusedActor = actor;
        if (this.parent != null) {
            this.parent.keyboardFocus(actor);
        }
    }

    public void scrollFocus(Actor actor) {
        this.scrollFocusedActor = actor;
        if (this.parent != null) {
            this.parent.scrollFocus(actor);
        }
    }

    public static void enableDebugging(String debugTextureFile) {
        debugTexture = new Texture(Gdx.files.internal(debugTextureFile), false);
        debug = true;
    }

    public static void disableDebugging() {
        if (debugTexture != null) {
            debugTexture.dispose();
        }
        debug = false;
    }

    public void clear() {
        this.children.clear();
        this.groups.clear();
        this.namesToActors.clear();
    }

    public void sortChildren(Comparator<Actor> comparator) {
        Collections.sort(this.children, comparator);
    }

    public void unfocusAll() {
        for (int i = 0; i < this.focusedActor.length; i++) {
            this.focusedActor[i] = null;
        }
        for (int i2 = 0; i2 < this.groups.size(); i2++) {
            this.groups.get(i2).unfocusAll();
        }
        this.keyboardFocusedActor = null;
        this.scrollFocusedActor = null;
    }

    public void unfocusAll(Actor actor) {
        for (int i = 0; i < this.focusedActor.length; i++) {
            if (this.focusedActor[i] == actor) {
                focus(null, i);
            }
        }
        if (this.keyboardFocusedActor == actor) {
            keyboardFocus(null);
        }
        if (this.scrollFocusedActor == actor) {
            scrollFocus(null);
        }
    }
}
