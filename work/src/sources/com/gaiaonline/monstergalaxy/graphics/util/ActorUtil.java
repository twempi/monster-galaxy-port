package com.gaiaonline.monstergalaxy.graphics.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

/* JADX INFO: loaded from: classes.dex */
public class ActorUtil {
    public static void disposeActorRecursive(Actor actor) {
        if (actor instanceof Group) {
            Group group = (Group) actor;
            for (int i = group.getChildren().size - 1; i >= 0; i--) {
                disposeActorRecursive(group.getChildren().get(i));
            }
        }
        actor.clearActions();
        if (actor.getParent() != null) {
            actor.getParent().removeActor(actor);
        }
    }

    public static void disposeStageRecursive(Stage stage) {
        disposeActorRecursive(stage.getRoot());
    }

    public static void centerOrigin(Actor actor) {
        actor.setOriginX(actor.getWidth() / 2.0f);
        actor.setOriginY(actor.getHeight() / 2.0f);
    }
}
