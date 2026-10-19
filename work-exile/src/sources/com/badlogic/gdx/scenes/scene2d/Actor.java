package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PooledLinkedList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Actor {
    public float height;
    public final String name;
    public float originX;
    public float originY;
    public Group parent;
    public float rotation;
    private boolean toRemove;
    public float width;
    public float x;
    public float y;
    public boolean touchable = true;
    public boolean visible = true;
    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public final Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    protected PooledLinkedList<Action> actions = new PooledLinkedList<>(10);

    public abstract void draw(SpriteBatch spriteBatch, float f);

    public abstract Actor hit(float f, float f2);

    public abstract boolean touchDown(float f, float f2, int i);

    public abstract boolean touchDragged(float f, float f2, int i);

    public abstract boolean touchUp(float f, float f2, int i);

    public Actor(String name) {
        this.name = name;
    }

    public boolean touchMoved(float x, float y) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public void toLocalCoordinates(Vector2 point) {
        if (this.parent != null) {
            this.parent.toLocalCoordinates(point);
            Group.toChildCoordinates(this, point.x, point.y, point);
        }
    }

    public void remove() {
        this.parent.removeActor(this);
    }

    public void act(float delta) {
        this.actions.iter();
        while (true) {
            Action action = this.actions.next();
            if (action != null) {
                action.act(delta);
                if (action.isDone()) {
                    action.finish();
                    this.actions.remove();
                }
            } else {
                return;
            }
        }
    }

    public void action(Action action) {
        action.setTarget(this);
        this.actions.add(action);
    }

    public void clearActions() {
        this.actions.clear();
    }

    public String toString() {
        return this.name + ": [x=" + this.x + ", y=" + this.y + ", refX=" + this.originX + ", refY=" + this.originY + ", width=" + this.width + ", height=" + this.height + "]";
    }

    public void markToRemove(boolean remove) {
        this.toRemove = remove;
    }

    public boolean isMarkedToRemove() {
        return this.toRemove;
    }
}
