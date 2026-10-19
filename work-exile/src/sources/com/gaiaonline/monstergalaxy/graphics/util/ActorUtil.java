package com.gaiaonline.monstergalaxy.graphics.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ActorUtil {
    public static void disposeActorRecursive(Actor actor) {
        if (actor instanceof Group) {
            Group group = (Group) actor;
            for (int i = group.getActors().size() - 1; i >= 0; i--) {
                disposeActorRecursive(group.getActors().get(i));
            }
        }
        actor.clearActions();
        if (actor.parent != null) {
            actor.parent.removeActor(actor);
        }
    }

    public static void disposeStageRecursive(Stage stage) {
        disposeActorRecursive(stage.getRoot());
        stage.dispose();
    }
}
