package com.gaiaonline.monstergalaxy.graphics.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;

/*
 * Overrides the game class. Two desktop fixes:
 *  - Drag movement is consumed when read, so it is applied once. Originally the
 *    last drag delta stuck, and screens that never call update() (shop, world
 *    map) kept scrolling after release. With a finger the last delta is usually
 *    ~0; with a mouse the list ran away to the end.
 *  - The mouse wheel scrolls (vertically for the shop, horizontally elsewhere;
 *    each screen reads only its own axis).
 */
public class ScrollInputProcessor extends InputAdapter {
    private int directionX;
    private int directionY;
    private static final float MAX_VELOCITY = 200.0f * ResolutionManager.getScaleFactor();
    private static final float DECCEL = 4000.0f * ResolutionManager.getScaleFactor();
    private static final float WHEEL_STEP = 30.0f * ResolutionManager.getScaleFactor();
    private final VelocityTracker tracker = new VelocityTracker(null);
    private boolean isDragging = false;
    private Vector2 delta = new Vector2(0.0f, 0.0f);
    private Vector2 gestureOrigin = new Vector2(-1.0f, -1.0f);
    private Vector2 velocity = new Vector2(0.0f, 0.0f);
    private Vector2 maxVelocity = new Vector2(MAX_VELOCITY, MAX_VELOCITY);
    private final Vector2 deccel = new Vector2(DECCEL, DECCEL);

    public float getDeltaX() {
        float value = this.delta.x;
        this.delta.x = 0.0f;
        return value;
    }

    public float getDeltaY() {
        float value = this.delta.y;
        this.delta.y = 0.0f;
        return value;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean scrolled(int amount) {
        // Wheel down (+1) acts like dragging up/left: reveals what is below/right.
        this.delta.x -= amount * WHEEL_STEP;
        this.delta.y -= amount * WHEEL_STEP;
        return true;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (pointer == 0) {
            this.isDragging = true;
            this.gestureOrigin.set(x, y);
            this.tracker.start(x, y, Gdx.input.getCurrentEventTime());
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        if (pointer == 0) {
            this.delta.x += x - this.tracker.lastX;
            this.delta.y += y - this.tracker.lastY;
            this.tracker.update(x, y, Gdx.input.getCurrentEventTime());
        }
        return super.touchDragged(x, y, pointer);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (pointer == 0 && this.gestureOrigin.x != -1.0f) {
            this.isDragging = false;
            long time = Gdx.input.getCurrentEventTime();
            this.tracker.update(x, y, time);
            this.velocity.x = Math.max(Math.abs(this.tracker.getVelocityX()), this.maxVelocity.x);
            this.velocity.y = Math.max(Math.abs(this.tracker.getVelocityY()), this.maxVelocity.y);
            this.directionX = (int) Math.signum(this.tracker.getVelocityX());
            this.directionY = (int) Math.signum(this.tracker.getVelocityY());
        }
        return super.touchUp(x, y, pointer, button);
    }

    public void setMaxVelocity(float max) {
        this.maxVelocity.set(max, max);
    }

    public void update(float deltaTime) {
        if (!this.isDragging) {
            this.velocity.x -= this.deccel.x * deltaTime;
            this.velocity.y -= this.deccel.y * deltaTime;
            if (this.velocity.x < 0.0f) {
                this.velocity.x = 0.0f;
            }
            if (this.velocity.y < 0.0f) {
                this.velocity.y = 0.0f;
            }
            this.delta.x += this.velocity.x * deltaTime * this.directionX;
            this.delta.y += this.velocity.y * deltaTime * this.directionY;
        }
    }

    private static class VelocityTracker {
        int deltaX;
        int deltaY;
        long lastTime;
        int lastX;
        int lastY;
        long[] meanTime;
        float[] meanX;
        float[] meanY;
        int numSamples;
        int sampleSize;

        private VelocityTracker() {
            this.sampleSize = 10;
            this.meanX = new float[this.sampleSize];
            this.meanY = new float[this.sampleSize];
            this.meanTime = new long[this.sampleSize];
        }

        /* synthetic */ VelocityTracker(VelocityTracker velocityTracker) {
            this();
        }

        public void start(int x, int y, long timeStamp) {
            this.lastX = x;
            this.lastY = y;
            this.deltaX = 0;
            this.deltaY = 0;
            this.numSamples = 0;
            for (int i = 0; i < this.sampleSize; i++) {
                this.meanX[i] = 0.0f;
                this.meanY[i] = 0.0f;
                this.meanTime[i] = 0;
            }
            this.lastTime = timeStamp;
        }

        public void update(int x, int y, long timeStamp) {
            this.deltaX = x - this.lastX;
            this.deltaY = y - this.lastY;
            this.lastX = x;
            this.lastY = y;
            long deltaTime = timeStamp - this.lastTime;
            this.lastTime = timeStamp;
            int index = this.numSamples % this.sampleSize;
            this.meanX[index] = this.deltaX;
            this.meanY[index] = this.deltaY;
            this.meanTime[index] = deltaTime;
            this.numSamples++;
        }

        public float getVelocityX() {
            float meanX = getAverage(this.meanX, this.numSamples);
            float meanTime = getAverage(this.meanTime, this.numSamples) / 1.0E9f;
            if (meanTime == 0.0f) {
                return 0.0f;
            }
            return meanX / meanTime;
        }

        public float getVelocityY() {
            float meanY = getAverage(this.meanY, this.numSamples);
            float meanTime = getAverage(this.meanTime, this.numSamples) / 1.0E9f;
            if (meanTime == 0.0f) {
                return 0.0f;
            }
            return meanY / meanTime;
        }

        private float getAverage(float[] values, int numSamples) {
            int numSamples2 = Math.min(this.sampleSize, numSamples);
            float sum = 0.0f;
            for (int i = 0; i < numSamples2; i++) {
                sum += values[i];
            }
            return sum / numSamples2;
        }

        private long getAverage(long[] values, int numSamples) {
            int numSamples2 = Math.min(this.sampleSize, numSamples);
            long sum = 0;
            for (int i = 0; i < numSamples2; i++) {
                sum += values[i];
            }
            if (numSamples2 == 0) {
                return 0L;
            }
            return sum / ((long) numSamples2);
        }
    }
}
