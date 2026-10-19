package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Stage extends InputAdapter implements Disposable {
    protected float centerX;
    protected float centerY;
    protected float height;
    protected boolean stretch;
    protected float width;
    final Vector2 point = new Vector2();
    final Vector2 coords = new Vector2();
    final Vector3 tmp = new Vector3();
    protected final Group root = new Group("root");
    protected final SpriteBatch batch = new SpriteBatch();
    protected Camera camera = new OrthographicCamera();

    public Stage(float width, float height, boolean stretch) {
        this.width = width;
        this.height = height;
        this.stretch = stretch;
        setViewport(width, height, stretch);
    }

    public void setViewport(float width, float height, boolean stretch) {
        if (stretch) {
            this.width = width;
            this.height = height;
        } else if (width > height && width / Gdx.graphics.getWidth() <= height / Gdx.graphics.getHeight()) {
            float toDeviceSpace = Gdx.graphics.getHeight() / height;
            float toViewportSpace = height / Gdx.graphics.getHeight();
            float deviceWidth = width * toDeviceSpace;
            this.width = ((Gdx.graphics.getWidth() - deviceWidth) * toViewportSpace) + width;
            this.height = height;
        } else {
            float toDeviceSpace2 = Gdx.graphics.getWidth() / width;
            float toViewportSpace2 = width / Gdx.graphics.getWidth();
            float deviceHeight = height * toDeviceSpace2;
            this.height = ((Gdx.graphics.getHeight() - deviceHeight) * toViewportSpace2) + height;
            this.width = width;
        }
        this.stretch = stretch;
        this.centerX = width / 2.0f;
        this.centerY = height / 2.0f;
        this.camera.position.set(this.centerX, this.centerY, 0.0f);
        this.camera.viewportWidth = this.width;
        this.camera.viewportHeight = this.height;
    }

    public float width() {
        return this.width;
    }

    public float height() {
        return this.height;
    }

    public int left() {
        return 0;
    }

    public float right() {
        return this.width - 1.0f;
    }

    public float top() {
        return this.height - 1.0f;
    }

    public float bottom() {
        return 0.0f;
    }

    public float centerX() {
        return this.centerX;
    }

    public float centerY() {
        return this.centerY;
    }

    public boolean isStretched() {
        return this.stretch;
    }

    public Actor findActor(String name) {
        return this.root.findActor(name);
    }

    public List<Actor> getActors() {
        return this.root.getActors();
    }

    public List<Group> getGroups() {
        return this.root.getGroups();
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int button) {
        toStageCoordinates(x, y, this.coords);
        Group.toChildCoordinates(this.root, this.coords.x, this.coords.y, this.point);
        return this.root.touchDown(this.point.x, this.point.y, pointer);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int button) {
        toStageCoordinates(x, y, this.coords);
        Group.toChildCoordinates(this.root, this.coords.x, this.coords.y, this.point);
        return this.root.touchUp(this.point.x, this.point.y, pointer);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        toStageCoordinates(x, y, this.coords);
        Group.toChildCoordinates(this.root, this.coords.x, this.coords.y, this.point);
        return this.root.touchDragged(this.point.x, this.point.y, pointer);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchMoved(int x, int y) {
        toStageCoordinates(x, y, this.coords);
        Group.toChildCoordinates(this.root, this.coords.x, this.coords.y, this.point);
        return this.root.touchMoved(this.point.x, this.point.y);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        return this.root.scrolled(amount);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean keyDown(int keycode) {
        return this.root.keyDown(keycode);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean keyUp(int keycode) {
        return this.root.keyUp(keycode);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char character) {
        return this.root.keyTyped(character);
    }

    public void act(float delta) {
        this.root.act(delta);
    }

    public void draw() {
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        this.root.draw(this.batch, 1.0f);
        this.batch.end();
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.batch.dispose();
    }

    public void addActor(Actor actor) {
        this.root.addActor(actor);
    }

    public String graphToString() {
        StringBuilder buffer = new StringBuilder();
        graphToString(buffer, this.root, 0);
        return buffer.toString();
    }

    private void graphToString(StringBuilder buffer, Actor actor, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append(' ');
        }
        buffer.append(actor);
        buffer.append("\n");
        if (actor instanceof Group) {
            Group group = (Group) actor;
            for (int i2 = 0; i2 < group.getActors().size(); i2++) {
                graphToString(buffer, group.getActors().get(i2), level + 1);
            }
        }
    }

    public Group getRoot() {
        return this.root;
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

    public Actor getLastTouchedChild() {
        return this.root.lastTouchedChild;
    }

    public Actor hit(float x, float y) {
        Group.toChildCoordinates(this.root, x, y, this.point);
        return this.root.hit(this.point.x, this.point.y);
    }

    public void toStageCoordinates(int x, int y, Vector2 out) {
        this.camera.unproject(this.tmp.set(x, y, 0.0f));
        out.x = this.tmp.x;
        out.y = this.tmp.y;
    }

    public void clear() {
        this.root.clear();
    }

    public void removeActor(Actor actor) {
        this.root.removeActorRecursive(actor);
    }

    public void unfocusAll() {
        this.root.unfocusAll();
    }
}
