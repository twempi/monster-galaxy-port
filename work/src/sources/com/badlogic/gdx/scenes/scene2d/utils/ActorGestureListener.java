package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

/* JADX INFO: loaded from: classes.dex */
public class ActorGestureListener implements EventListener {
    Actor actor;
    private final GestureDetector detector;
    InputEvent event;
    Actor touchDownTarget;

    public ActorGestureListener() {
        this(20.0f, 0.4f, 1.1f, 0.15f);
    }

    public ActorGestureListener(float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay) {
        this.detector = new GestureDetector(halfTapSquareSize, tapCountInterval, longPressDuration, maxFlingDelay, new GestureDetector.GestureAdapter() { // from class: com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener.1
            private final Vector2 initialPointer1 = new Vector2();
            private final Vector2 initialPointer2 = new Vector2();
            private final Vector2 pointer1 = new Vector2();
            private final Vector2 pointer2 = new Vector2();

            @Override // com.badlogic.gdx.input.GestureDetector.GestureAdapter, com.badlogic.gdx.input.GestureDetector.GestureListener
            public boolean tap(float stageX, float stageY, int count, int button) {
                ActorGestureListener.this.actor.stageToLocalCoordinates(Vector2.tmp.set(stageX, stageY));
                ActorGestureListener.this.tap(ActorGestureListener.this.event, Vector2.tmp.x, Vector2.tmp.y, count, button);
                return true;
            }

            @Override // com.badlogic.gdx.input.GestureDetector.GestureAdapter, com.badlogic.gdx.input.GestureDetector.GestureListener
            public boolean longPress(float stageX, float stageY) {
                ActorGestureListener.this.actor.stageToLocalCoordinates(Vector2.tmp.set(stageX, stageY));
                return ActorGestureListener.this.longPress(ActorGestureListener.this.actor, Vector2.tmp.x, Vector2.tmp.y);
            }

            @Override // com.badlogic.gdx.input.GestureDetector.GestureAdapter, com.badlogic.gdx.input.GestureDetector.GestureListener
            public boolean fling(float velocityX, float velocityY, int button) {
                ActorGestureListener.this.fling(ActorGestureListener.this.event, velocityX, velocityY, button);
                return true;
            }

            @Override // com.badlogic.gdx.input.GestureDetector.GestureAdapter, com.badlogic.gdx.input.GestureDetector.GestureListener
            public boolean pan(float stageX, float stageY, float deltaX, float deltaY) {
                ActorGestureListener.this.actor.stageToLocalCoordinates(Vector2.tmp.set(stageX, stageY));
                ActorGestureListener.this.pan(ActorGestureListener.this.event, Vector2.tmp.x, Vector2.tmp.y, deltaX, deltaY);
                return true;
            }

            @Override // com.badlogic.gdx.input.GestureDetector.GestureAdapter, com.badlogic.gdx.input.GestureDetector.GestureListener
            public boolean zoom(float initialDistance, float distance) {
                ActorGestureListener.this.zoom(ActorGestureListener.this.event, initialDistance, distance);
                return true;
            }

            @Override // com.badlogic.gdx.input.GestureDetector.GestureAdapter, com.badlogic.gdx.input.GestureDetector.GestureListener
            public boolean pinch(Vector2 stageInitialPointer1, Vector2 stageInitialPointer2, Vector2 stagePointer1, Vector2 stagePointer2) {
                ActorGestureListener.this.actor.stageToLocalCoordinates(this.initialPointer1.set(stageInitialPointer1));
                ActorGestureListener.this.actor.stageToLocalCoordinates(this.initialPointer2.set(stageInitialPointer2));
                ActorGestureListener.this.actor.stageToLocalCoordinates(this.pointer1.set(stagePointer1));
                ActorGestureListener.this.actor.stageToLocalCoordinates(this.pointer2.set(stagePointer2));
                ActorGestureListener.this.pinch(ActorGestureListener.this.event, this.initialPointer1, this.initialPointer2, this.pointer1, this.pointer2);
                return true;
            }
        });
    }

    @Override // com.badlogic.gdx.scenes.scene2d.EventListener
    public boolean handle(Event e) {
        if (!(e instanceof InputEvent)) {
            return false;
        }
        InputEvent event = (InputEvent) e;
        switch (event.getType()) {
            case touchDown:
                this.actor = event.getListenerActor();
                this.touchDownTarget = event.getTarget();
                this.detector.touchDown(event.getStageX(), event.getStageY(), event.getPointer(), event.getButton());
                this.actor.stageToLocalCoordinates(Vector2.tmp.set(event.getStageX(), event.getStageY()));
                touchDown(event, Vector2.tmp.x, Vector2.tmp.y, event.getPointer(), event.getButton());
                return true;
            case touchUp:
                this.event = event;
                this.actor = event.getListenerActor();
                this.detector.touchUp(event.getStageX(), event.getStageY(), event.getPointer(), event.getButton());
                this.actor.stageToLocalCoordinates(Vector2.tmp.set(event.getStageX(), event.getStageY()));
                touchUp(event, Vector2.tmp.x, Vector2.tmp.y, event.getPointer(), event.getButton());
                return true;
            case touchDragged:
                this.event = event;
                this.actor = event.getListenerActor();
                this.detector.touchDragged(event.getStageX(), event.getStageY(), event.getPointer());
                return true;
            default:
                return false;
        }
    }

    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void tap(InputEvent event, float x, float y, int count, int button) {
    }

    public boolean longPress(Actor actor, float x, float y) {
        return false;
    }

    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
    }

    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
    }

    public void zoom(InputEvent event, float initialDistance, float distance) {
    }

    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    }

    public GestureDetector getGestureDetector() {
        return this.detector;
    }

    public Actor getTouchDownTarget() {
        return this.touchDownTarget;
    }
}
