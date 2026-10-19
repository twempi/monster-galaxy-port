package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LinearGroup extends BoundGroup {
    protected final LinearGroupLayout layout;

    public enum LinearGroupLayout {
        Vertical,
        Horizontal
    }

    public LinearGroup(String name, float width, float height, LinearGroupLayout layout) {
        super(name, width, height);
        this.layout = layout;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void addActor(Actor actor) {
        if ((actor instanceof Group) && !(actor instanceof BoundGroup)) {
            throw new GdxRuntimeException("Can only add Actors and BoundGroup subclasses");
        }
        super.addActor(actor);
        layout();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group
    public void removeActor(Actor actor) {
        super.removeActor(actor);
        layout();
    }

    protected void layout() {
        int len = getActors().size();
        float x = 0.0f;
        float y = 0.0f;
        for (int i = 0; i < len; i++) {
            Actor actor = getActors().get(i);
            actor.x = x;
            actor.y = y;
            if (this.layout == LinearGroupLayout.Horizontal) {
                x += actor.width;
            } else {
                y += actor.height;
            }
        }
    }
}
