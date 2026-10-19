package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BoundGroup extends Group {
    public BoundGroup(String name, float width, float height) {
        super(name);
        this.width = width;
        this.height = height;
        this.originX = width / 2.0f;
        this.originY = height / 2.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (this.focusedActor != null) {
            return super.touchDown(x, y, pointer);
        }
        if (x <= 0.0f || y <= 0.0f || x >= this.width || y >= this.height) {
            return false;
        }
        return super.touchDown(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (this.focusedActor != null) {
            return super.touchUp(x, y, pointer);
        }
        if (x <= 0.0f || y <= 0.0f || x >= this.width || y >= this.height) {
            return false;
        }
        return super.touchUp(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        if (this.focusedActor != null) {
            return super.touchDragged(x, y, pointer);
        }
        if (x <= 0.0f || y <= 0.0f || x >= this.width || y >= this.height) {
            return false;
        }
        return super.touchDragged(x, y, pointer);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || y <= 0.0f || x >= this.width || y >= this.height) {
            return null;
        }
        return this;
    }
}
