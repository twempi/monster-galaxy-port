package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class DragAndDrop {
    private int button;
    Actor dragActor;
    long dragStartTime;
    boolean isValidTarget;
    Payload payload;
    Source source;
    Target target;
    Array<Target> targets = new Array<>();
    private float tapSquareSize = 8.0f;
    float dragActorX = 14.0f;
    float dragActorY = -20.0f;
    int dragTime = Input.Keys.F7;

    public void addSource(final Source source) {
        DragListener listener = new DragListener() { // from class: com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.DragListener
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.this.dragStartTime = System.currentTimeMillis();
                DragAndDrop.this.payload = source.dragStart(event, getTouchDownX(), getTouchDownY(), pointer);
                event.stop();
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.DragListener
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (DragAndDrop.this.payload != null) {
                    Stage stage = event.getStage();
                    Touchable dragActorTouchable = null;
                    if (DragAndDrop.this.dragActor != null) {
                        dragActorTouchable = DragAndDrop.this.dragActor.getTouchable();
                        DragAndDrop.this.dragActor.setTouchable(Touchable.disabled);
                    }
                    Target newTarget = null;
                    DragAndDrop.this.isValidTarget = false;
                    Actor hit = event.getStage().hit(event.getStageX(), event.getStageY(), true);
                    if (hit == null) {
                        hit = event.getStage().hit(event.getStageX(), event.getStageY(), false);
                    }
                    if (hit != null) {
                        int n = DragAndDrop.this.targets.size;
                        for (int i = 0; i < n; i++) {
                            Target target = DragAndDrop.this.targets.get(i);
                            if (target.actor.isAscendantOf(hit)) {
                                newTarget = target;
                                target.actor.stageToLocalCoordinates(Vector2.tmp.set(event.getStageX(), event.getStageY()));
                                DragAndDrop.this.isValidTarget = target.drag(source, DragAndDrop.this.payload, Vector2.tmp.x, Vector2.tmp.y, pointer);
                                break;
                            }
                        }
                    }
                    if (newTarget != DragAndDrop.this.target) {
                        if (DragAndDrop.this.target != null) {
                            DragAndDrop.this.target.reset(source, DragAndDrop.this.payload);
                        }
                        DragAndDrop.this.target = newTarget;
                    }
                    if (DragAndDrop.this.dragActor != null) {
                        DragAndDrop.this.dragActor.setTouchable(dragActorTouchable);
                    }
                    Actor actor = null;
                    if (DragAndDrop.this.target != null) {
                        actor = DragAndDrop.this.isValidTarget ? DragAndDrop.this.payload.validDragActor : DragAndDrop.this.payload.invalidDragActor;
                    }
                    if (actor == null) {
                        actor = DragAndDrop.this.payload.dragActor;
                    }
                    if (actor != null) {
                        if (DragAndDrop.this.dragActor != actor) {
                            if (DragAndDrop.this.dragActor != null) {
                                DragAndDrop.this.dragActor.remove();
                            }
                            DragAndDrop.this.dragActor = actor;
                            stage.addActor(actor);
                        }
                        float actorX = event.getStageX() + DragAndDrop.this.dragActorX;
                        float actorY = (event.getStageY() + DragAndDrop.this.dragActorY) - actor.getHeight();
                        if (actorX < 0.0f) {
                            actorX = 0.0f;
                        }
                        if (actorY < 0.0f) {
                            actorY = 0.0f;
                        }
                        if (actor.getWidth() + actorX > stage.getWidth()) {
                            actorX = stage.getWidth() - actor.getWidth();
                        }
                        if (actor.getHeight() + actorY > stage.getHeight()) {
                            actorY = stage.getHeight() - actor.getHeight();
                        }
                        actor.setPosition(actorX, actorY);
                    }
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.DragListener
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (DragAndDrop.this.payload != null) {
                    if (System.currentTimeMillis() - DragAndDrop.this.dragStartTime < DragAndDrop.this.dragTime) {
                        DragAndDrop.this.isValidTarget = false;
                    }
                    if (DragAndDrop.this.dragActor != null) {
                        DragAndDrop.this.dragActor.remove();
                    }
                    if (DragAndDrop.this.isValidTarget) {
                        DragAndDrop.this.target.actor.stageToLocalCoordinates(Vector2.tmp.set(event.getStageX(), event.getStageY()));
                        DragAndDrop.this.target.drop(source, DragAndDrop.this.payload, Vector2.tmp.x, Vector2.tmp.y, pointer);
                    }
                    source.dragStop(event, x, y, pointer, DragAndDrop.this.isValidTarget ? DragAndDrop.this.target : null);
                    if (DragAndDrop.this.target != null) {
                        DragAndDrop.this.target.reset(source, DragAndDrop.this.payload);
                    }
                    DragAndDrop.this.source = null;
                    DragAndDrop.this.payload = null;
                    DragAndDrop.this.target = null;
                    DragAndDrop.this.isValidTarget = false;
                    DragAndDrop.this.dragActor = null;
                }
            }
        };
        listener.setTapSquareSize(this.tapSquareSize);
        listener.setButton(this.button);
        source.actor.addCaptureListener(listener);
    }

    public void addTarget(Target target) {
        this.targets.add(target);
    }

    public void setTapSquareSize(float halfTapSquareSize) {
        this.tapSquareSize = halfTapSquareSize;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public void setDragActorPosition(float dragActorX, float dragActorY) {
        this.dragActorX = dragActorX;
        this.dragActorY = dragActorY;
    }

    public boolean isDragging() {
        return this.payload != null;
    }

    public Actor getDragActor() {
        return this.dragActor;
    }

    public void setDragTime(int dragMillis) {
        this.dragTime = dragMillis;
    }

    public static abstract class Source {
        final Actor actor;

        public abstract Payload dragStart(InputEvent inputEvent, float f, float f2, int i);

        public Source(Actor actor) {
            if (actor == null) {
                throw new IllegalArgumentException("actor cannot be null.");
            }
            this.actor = actor;
        }

        public void dragStop(InputEvent event, float x, float y, int pointer, Target target) {
        }

        public Actor getActor() {
            return this.actor;
        }
    }

    public static abstract class Target {
        final Actor actor;

        public abstract boolean drag(Source source, Payload payload, float f, float f2, int i);

        public abstract void drop(Source source, Payload payload, float f, float f2, int i);

        public Target(Actor actor) {
            if (actor == null) {
                throw new IllegalArgumentException("actor cannot be null.");
            }
            this.actor = actor;
        }

        public void reset(Source source, Payload payload) {
        }

        public Actor getActor() {
            return this.actor;
        }
    }

    public static class Payload {
        Actor dragActor;
        Actor invalidDragActor;
        Object object;
        Actor validDragActor;

        public void setDragActor(Actor dragActor) {
            this.dragActor = dragActor;
        }

        public Actor getDragActor() {
            return this.dragActor;
        }

        public void setValidDragActor(Actor validDragActor) {
            this.validDragActor = validDragActor;
        }

        public Actor getValidDragActor() {
            return this.validDragActor;
        }

        public void setInvalidDragActor(Actor invalidDragActor) {
            this.invalidDragActor = invalidDragActor;
        }

        public Actor getInvalidDragActor() {
            return this.invalidDragActor;
        }

        public Object getObject() {
            return this.object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
