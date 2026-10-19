package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;

/* JADX INFO: loaded from: classes.dex */
public class Stage extends InputAdapter implements Disposable {
    private final SpriteBatch batch;
    private Camera camera;
    private float centerX;
    private float centerY;
    private float gutterHeight;
    private float gutterWidth;
    private float height;
    private Actor keyboardFocus;
    private Actor mouseOverActor;
    private int mouseScreenX;
    private int mouseScreenY;
    private final boolean ownsBatch;
    private Actor[] pointerOverActors;
    private int[] pointerScreenX;
    private int[] pointerScreenY;
    private boolean[] pointerTouched;
    private Group root;
    private Actor scrollFocus;
    private final Vector2 stageCoords;
    private SnapshotArray<TouchFocus> touchFocuses;
    private float width;

    public Stage() {
        this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    public Stage(float width, float height, boolean keepAspectRatio) {
        this.stageCoords = new Vector2();
        this.pointerOverActors = new Actor[20];
        this.pointerTouched = new boolean[20];
        this.pointerScreenX = new int[20];
        this.pointerScreenY = new int[20];
        this.touchFocuses = new SnapshotArray<>(true, 4, TouchFocus.class);
        this.batch = new SpriteBatch();
        this.ownsBatch = true;
        initialize(width, height, keepAspectRatio);
    }

    public Stage(float width, float height, boolean keepAspectRatio, SpriteBatch batch) {
        this.stageCoords = new Vector2();
        this.pointerOverActors = new Actor[20];
        this.pointerTouched = new boolean[20];
        this.pointerScreenX = new int[20];
        this.pointerScreenY = new int[20];
        this.touchFocuses = new SnapshotArray<>(true, 4, TouchFocus.class);
        this.batch = batch;
        this.ownsBatch = false;
        initialize(width, height, keepAspectRatio);
    }

    private void initialize(float width, float height, boolean keepAspectRatio) {
        this.width = width;
        this.height = height;
        this.root = new Group();
        this.root.setStage(this);
        this.camera = new OrthographicCamera();
        setViewport(width, height, keepAspectRatio);
    }

    public void setViewport(float width, float height, boolean keepAspectRatio) {
        if (keepAspectRatio) {
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            if (screenHeight / screenWidth < height / width) {
                float toScreenSpace = screenHeight / height;
                float toViewportSpace = height / screenHeight;
                float deviceWidth = width * toScreenSpace;
                float lengthen = (screenWidth - deviceWidth) * toViewportSpace;
                this.width = width + lengthen;
                this.height = height;
                this.gutterWidth = lengthen / 2.0f;
                this.gutterHeight = 0.0f;
            } else {
                float toScreenSpace2 = screenWidth / width;
                float toViewportSpace2 = width / screenWidth;
                float deviceHeight = height * toScreenSpace2;
                float lengthen2 = (screenHeight - deviceHeight) * toViewportSpace2;
                this.height = height + lengthen2;
                this.width = width;
                this.gutterWidth = 0.0f;
                this.gutterHeight = lengthen2 / 2.0f;
            }
        } else {
            this.width = width;
            this.height = height;
            this.gutterWidth = 0.0f;
            this.gutterHeight = 0.0f;
        }
        this.centerX = this.width / 2.0f;
        this.centerY = this.height / 2.0f;
        this.camera.position.set(this.centerX, this.centerY, 0.0f);
        this.camera.viewportWidth = this.width;
        this.camera.viewportHeight = this.height;
    }

    public void draw() {
        this.camera.update();
        if (this.root.isVisible()) {
            this.batch.setProjectionMatrix(this.camera.combined);
            this.batch.begin();
            this.root.draw(this.batch, 1.0f);
            this.batch.end();
        }
    }

    public void act() {
        act(Math.min(Gdx.graphics.getDeltaTime(), 0.033333335f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void act(float delta) {
        int n = this.pointerOverActors.length;
        for (int pointer = 0; pointer < n; pointer++) {
            Actor actor = this.pointerOverActors[pointer];
            if (!this.pointerTouched[pointer]) {
                if (actor != null) {
                    this.pointerOverActors[pointer] = null;
                    screenToStageCoordinates(this.stageCoords.set(this.pointerScreenX[pointer], this.pointerScreenY[pointer]));
                    InputEvent inputEvent = (InputEvent) Pools.obtain(InputEvent.class);
                    inputEvent.setType(InputEvent.Type.exit);
                    inputEvent.setStage(this);
                    inputEvent.setStageX(this.stageCoords.x);
                    inputEvent.setStageY(this.stageCoords.y);
                    inputEvent.setRelatedActor(actor);
                    inputEvent.setPointer(pointer);
                    actor.fire(inputEvent);
                    Pools.free(inputEvent);
                }
            } else {
                this.pointerOverActors[pointer] = fireEnterAndExit(actor, this.pointerScreenX[pointer], this.pointerScreenY[pointer], pointer);
            }
        }
        Application.ApplicationType type = Gdx.app.getType();
        if (type == Application.ApplicationType.Desktop || type == Application.ApplicationType.Applet || type == Application.ApplicationType.WebGL) {
            this.mouseOverActor = fireEnterAndExit(this.mouseOverActor, this.mouseScreenX, this.mouseScreenY, -1);
        }
        this.root.act(delta);
    }

    private Actor fireEnterAndExit(Actor overLast, int screenX, int screenY, int pointer) {
        screenToStageCoordinates(this.stageCoords.set(screenX, screenY));
        Actor over = hit(this.stageCoords.x, this.stageCoords.y, true);
        if (over != overLast) {
            InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
            event.setStage(this);
            event.setStageX(this.stageCoords.x);
            event.setStageY(this.stageCoords.y);
            event.setPointer(pointer);
            if (overLast != null) {
                event.setType(InputEvent.Type.exit);
                event.setRelatedActor(over);
                overLast.fire(event);
            }
            if (over != null) {
                event.setType(InputEvent.Type.enter);
                event.setRelatedActor(overLast);
                over.fire(event);
            }
            Pools.free(event);
            return over;
        }
        return overLast;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.pointerTouched[pointer] = true;
        this.pointerScreenX[pointer] = screenX;
        this.pointerScreenY[pointer] = screenY;
        screenToStageCoordinates(this.stageCoords.set(screenX, screenY));
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setType(InputEvent.Type.touchDown);
        event.setStage(this);
        event.setStageX(this.stageCoords.x);
        event.setStageY(this.stageCoords.y);
        event.setPointer(pointer);
        event.setButton(button);
        Actor target = hit(this.stageCoords.x, this.stageCoords.y, true);
        if (target == null) {
            target = this.root;
        }
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.pointerScreenX[pointer] = screenX;
        this.pointerScreenY[pointer] = screenY;
        if (this.touchFocuses.size == 0) {
            return false;
        }
        screenToStageCoordinates(this.stageCoords.set(screenX, screenY));
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setType(InputEvent.Type.touchDragged);
        event.setStage(this);
        event.setStageX(this.stageCoords.x);
        event.setStageY(this.stageCoords.y);
        event.setPointer(pointer);
        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        TouchFocus[] focuses = touchFocuses.begin();
        int n = touchFocuses.size;
        for (int i = 0; i < n; i++) {
            TouchFocus focus = focuses[i];
            if (focus.pointer == pointer) {
                event.setTarget(focus.target);
                event.setListenerActor(focus.listenerActor);
                if (focus.listener.handle(event)) {
                    event.handle();
                }
            }
        }
        touchFocuses.end();
        boolean zIsHandled = event.isHandled();
        Pools.free(event);
        return zIsHandled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.pointerTouched[pointer] = false;
        this.pointerScreenX[pointer] = screenX;
        this.pointerScreenY[pointer] = screenY;
        if (this.touchFocuses.size == 0) {
            return false;
        }
        screenToStageCoordinates(this.stageCoords.set(screenX, screenY));
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setType(InputEvent.Type.touchUp);
        event.setStage(this);
        event.setStageX(this.stageCoords.x);
        event.setStageY(this.stageCoords.y);
        event.setPointer(pointer);
        event.setButton(button);
        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        TouchFocus[] focuses = touchFocuses.begin();
        int n = touchFocuses.size;
        for (int i = 0; i < n; i++) {
            TouchFocus focus = focuses[i];
            if (focus.pointer == pointer && focus.button == button && touchFocuses.removeValue(focus, true)) {
                event.setTarget(focus.target);
                event.setListenerActor(focus.listenerActor);
                if (focus.listener.handle(event)) {
                    event.handle();
                }
                Pools.free(focus);
            }
        }
        touchFocuses.end();
        boolean zIsHandled = event.isHandled();
        Pools.free(event);
        return zIsHandled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean mouseMoved(int screenX, int screenY) {
        this.mouseScreenX = screenX;
        this.mouseScreenY = screenY;
        screenToStageCoordinates(this.stageCoords.set(screenX, screenY));
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.mouseMoved);
        event.setStageX(this.stageCoords.x);
        event.setStageY(this.stageCoords.y);
        Actor target = hit(this.stageCoords.x, this.stageCoords.y, true);
        if (target == null) {
            target = this.root;
        }
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        Actor target = this.scrollFocus == null ? this.root : this.scrollFocus;
        screenToStageCoordinates(this.stageCoords.set(this.mouseScreenX, this.mouseScreenY));
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.scrolled);
        event.setScrollAmount(amount);
        event.setStageX(this.stageCoords.x);
        event.setStageY(this.stageCoords.y);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keyCode) {
        Actor target = this.keyboardFocus == null ? this.root : this.keyboardFocus;
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.keyDown);
        event.setKeyCode(keyCode);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keyCode) {
        Actor target = this.keyboardFocus == null ? this.root : this.keyboardFocus;
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.keyUp);
        event.setKeyCode(keyCode);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        Actor target = this.keyboardFocus == null ? this.root : this.keyboardFocus;
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.keyTyped);
        event.setCharacter(character);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    public void addTouchFocus(EventListener listener, Actor listenerActor, Actor target, int pointer, int button) {
        TouchFocus focus = (TouchFocus) Pools.obtain(TouchFocus.class);
        focus.listenerActor = listenerActor;
        focus.target = target;
        focus.listener = listener;
        focus.pointer = pointer;
        focus.button = button;
        this.touchFocuses.add(focus);
    }

    public void removeTouchFocus(EventListener listener, Actor listenerActor, Actor target, int pointer, int button) {
        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        for (int i = touchFocuses.size - 1; i >= 0; i--) {
            TouchFocus focus = touchFocuses.get(i);
            if (focus.listener == listener && focus.listenerActor == listenerActor && focus.target == target && focus.pointer == pointer && focus.button == button) {
                touchFocuses.removeIndex(i);
                Pools.free(focus);
            }
        }
    }

    public void cancelTouchFocus() {
        cancelTouchFocus(null, null);
    }

    public void cancelTouchFocus(EventListener listener, Actor actor) {
        InputEvent event = (InputEvent) Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.touchUp);
        event.setStageX(-2.1474836E9f);
        event.setStageY(-2.1474836E9f);
        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        TouchFocus[] items = touchFocuses.begin();
        int n = touchFocuses.size;
        for (int i = 0; i < n; i++) {
            TouchFocus focus = items[i];
            if ((focus.listener != listener || focus.listenerActor != actor) && touchFocuses.removeValue(focus, true)) {
                event.setTarget(focus.target);
                event.setListenerActor(focus.listenerActor);
                event.setPointer(focus.pointer);
                event.setButton(focus.button);
                focus.listener.handle(event);
            }
        }
        touchFocuses.end();
        Pools.free(event);
    }

    public void addActor(Actor actor) {
        this.root.addActor(actor);
    }

    public void addAction(Action action) {
        this.root.addAction(action);
    }

    public Array<Actor> getActors() {
        return this.root.getChildren();
    }

    public boolean addListener(EventListener listener) {
        return this.root.addListener(listener);
    }

    public boolean removeListener(EventListener listener) {
        return this.root.removeListener(listener);
    }

    public boolean addCaptureListener(EventListener listener) {
        return this.root.addCaptureListener(listener);
    }

    public boolean removeCaptureListener(EventListener listener) {
        return this.root.removeCaptureListener(listener);
    }

    public void clear() {
        unfocusAll();
        this.root.clear();
    }

    public void unfocusAll() {
        this.scrollFocus = null;
        this.keyboardFocus = null;
        cancelTouchFocus();
    }

    public void unfocus(Actor actor) {
        if (this.scrollFocus == actor) {
            this.scrollFocus = null;
        }
        if (this.keyboardFocus == actor) {
            this.keyboardFocus = null;
        }
    }

    public void setKeyboardFocus(Actor actor) {
        if (this.keyboardFocus != actor) {
            FocusListener.FocusEvent event = (FocusListener.FocusEvent) Pools.obtain(FocusListener.FocusEvent.class);
            event.setStage(this);
            event.setType(FocusListener.FocusEvent.Type.keyboard);
            if (this.keyboardFocus != null) {
                event.setFocused(false);
                this.keyboardFocus.fire(event);
            }
            this.keyboardFocus = actor;
            if (this.keyboardFocus != null) {
                event.setFocused(true);
                this.keyboardFocus.fire(event);
            }
            Pools.free(event);
        }
    }

    public Actor getKeyboardFocus() {
        return this.keyboardFocus;
    }

    public void setScrollFocus(Actor actor) {
        if (this.scrollFocus != actor) {
            FocusListener.FocusEvent event = (FocusListener.FocusEvent) Pools.obtain(FocusListener.FocusEvent.class);
            event.setStage(this);
            event.setType(FocusListener.FocusEvent.Type.scroll);
            if (this.scrollFocus != null) {
                event.setFocused(false);
                this.scrollFocus.fire(event);
            }
            this.scrollFocus = actor;
            if (this.scrollFocus != null) {
                event.setFocused(true);
                this.scrollFocus.fire(event);
            }
            Pools.free(event);
        }
    }

    public Actor getScrollFocus() {
        return this.scrollFocus;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getGutterWidth() {
        return this.gutterWidth;
    }

    public float getGutterHeight() {
        return this.gutterHeight;
    }

    public SpriteBatch getSpriteBatch() {
        return this.batch;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Group getRoot() {
        return this.root;
    }

    public Actor hit(float stageX, float stageY, boolean touchable) {
        Vector2 actorCoords = Vector2.tmp;
        this.root.parentToLocalCoordinates(actorCoords.set(stageX, stageY));
        return this.root.hit(actorCoords.x, actorCoords.y, touchable);
    }

    public Vector2 screenToStageCoordinates(Vector2 screenCoords) {
        this.camera.unproject(Vector3.tmp.set(screenCoords.x, screenCoords.y, 0.0f));
        screenCoords.x = Vector3.tmp.x;
        screenCoords.y = Vector3.tmp.y;
        return screenCoords;
    }

    public Vector2 stageToScreenCoordinates(Vector2 stageCoords) {
        Vector3.tmp.set(stageCoords.x, stageCoords.y, 0.0f);
        this.camera.project(Vector3.tmp);
        stageCoords.x = Vector3.tmp.x;
        stageCoords.y = Vector3.tmp.y;
        return stageCoords;
    }

    public Vector2 toScreenCoordinates(Vector2 coords, Matrix4 transformMatrix) {
        ScissorStack.toWindowCoordinates(this.camera, transformMatrix, coords);
        return coords;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        if (this.ownsBatch) {
            this.batch.dispose();
        }
    }

    public static final class TouchFocus implements Pool.Poolable {
        int button;
        EventListener listener;
        Actor listenerActor;
        int pointer;
        Actor target;

        @Override // com.badlogic.gdx.utils.Pool.Poolable
        public void reset() {
            this.listenerActor = null;
            this.listener = null;
        }
    }
}
