package com.badlogic.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

/* JADX INFO: loaded from: classes.dex */
public class GestureDetector extends InputAdapter {
    private long gestureStartTime;
    private boolean inTapSquare;
    private final Vector2 initialPointer1;
    private final Vector2 initialPointer2;
    private long lastTapTime;
    private float lastTapX;
    private float lastTapY;
    final GestureListener listener;
    boolean longPressFired;
    private float longPressSeconds;
    private final Timer.Task longPressTask;
    private long maxFlingDelay;
    private boolean panning;
    private boolean pinching;
    Vector2 pointer1;
    private final Vector2 pointer2;
    private int tapCount;
    private long tapCountInterval;
    private float tapSquareCenterX;
    private float tapSquareCenterY;
    private float tapSquareSize;
    private final VelocityTracker tracker;

    public interface GestureListener {
        boolean fling(float f, float f2, int i);

        boolean longPress(float f, float f2);

        boolean pan(float f, float f2, float f3, float f4);

        boolean pinch(Vector2 vector2, Vector2 vector3, Vector2 vector4, Vector2 vector5);

        boolean tap(float f, float f2, int i, int i2);

        boolean touchDown(float f, float f2, int i, int i2);

        boolean zoom(float f, float f2);
    }

    public GestureDetector(GestureListener listener) {
        this(20.0f, 0.4f, 1.1f, 0.15f, listener);
    }

    public GestureDetector(float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay, GestureListener listener) {
        this.tracker = new VelocityTracker();
        this.pointer1 = new Vector2();
        this.pointer2 = new Vector2();
        this.initialPointer1 = new Vector2();
        this.initialPointer2 = new Vector2();
        this.longPressTask = new Timer.Task() { // from class: com.badlogic.gdx.input.GestureDetector.1
            @Override // com.badlogic.gdx.utils.Timer.Task, java.lang.Runnable
            public void run() {
                if (GestureDetector.this.listener.longPress(GestureDetector.this.pointer1.x, GestureDetector.this.pointer1.y)) {
                    GestureDetector.this.longPressFired = true;
                }
            }
        };
        this.tapSquareSize = halfTapSquareSize;
        this.tapCountInterval = (long) (tapCountInterval * 1.0E9f);
        this.longPressSeconds = longPressDuration;
        this.maxFlingDelay = (long) (maxFlingDelay * 1.0E9f);
        this.listener = listener;
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int button) {
        return touchDown(x, y, pointer, button);
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        if (pointer > 1) {
            return false;
        }
        if (pointer == 0) {
            this.pointer1.set(x, y);
            this.gestureStartTime = Gdx.input.getCurrentEventTime();
            this.tracker.start(x, y, this.gestureStartTime);
            if (Gdx.input.isTouched(1)) {
                this.inTapSquare = false;
                this.pinching = true;
                this.initialPointer1.set(this.pointer1);
                this.initialPointer2.set(this.pointer2);
                this.longPressTask.cancel();
            } else {
                this.inTapSquare = true;
                this.pinching = false;
                this.longPressFired = false;
                this.tapSquareCenterX = x;
                this.tapSquareCenterY = y;
                if (!this.longPressTask.isScheduled()) {
                    Timer.schedule(this.longPressTask, this.longPressSeconds);
                }
            }
        } else {
            this.pointer2.set(x, y);
            this.inTapSquare = false;
            this.pinching = true;
            this.initialPointer1.set(this.pointer1);
            this.initialPointer2.set(this.pointer2);
            this.longPressTask.cancel();
        }
        return this.listener.touchDown(x, y, pointer, button);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        return touchDragged(x, y, pointer);
    }

    public boolean touchDragged(float x, float y, int pointer) {
        if (pointer > 1 || this.longPressFired) {
            return false;
        }
        if (this.pinching) {
            if (pointer == 0) {
                this.pointer1.set(x, y);
            } else {
                this.pointer2.set(x, y);
            }
            if (this.listener == null) {
                return false;
            }
            boolean result = this.listener.pinch(this.initialPointer1, this.initialPointer2, this.pointer1, this.pointer2);
            return this.listener.zoom(this.initialPointer1.dst(this.initialPointer2), this.pointer1.dst(this.pointer2)) || result;
        }
        this.tracker.update(x, y, Gdx.input.getCurrentEventTime());
        if (this.inTapSquare && !isWithinTapSquare(x, y, this.tapSquareCenterX, this.tapSquareCenterY)) {
            this.longPressTask.cancel();
            this.inTapSquare = false;
        }
        if (this.inTapSquare) {
            return false;
        }
        this.panning = true;
        return this.listener.pan(x, y, this.tracker.deltaX, this.tracker.deltaY);
    }

    @Override // com.badlogic.gdx.InputAdapter, com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int button) {
        return touchUp(x, y, pointer, button);
    }

    public boolean touchUp(float x, float y, int pointer, int button) {
        if (pointer > 1) {
            return false;
        }
        if (this.inTapSquare && !isWithinTapSquare(x, y, this.tapSquareCenterX, this.tapSquareCenterY)) {
            this.inTapSquare = false;
        }
        this.longPressTask.cancel();
        this.panning = false;
        if (this.longPressFired) {
            return false;
        }
        if (this.inTapSquare) {
            if (TimeUtils.nanoTime() - this.lastTapTime > this.tapCountInterval || !isWithinTapSquare(x, y, this.lastTapX, this.lastTapY)) {
                this.tapCount = 0;
            }
            this.tapCount++;
            this.lastTapTime = TimeUtils.nanoTime();
            this.lastTapX = x;
            this.lastTapY = y;
            this.gestureStartTime = 0L;
            return this.listener.tap(x, y, this.tapCount, button);
        }
        if (this.pinching) {
            this.pinching = false;
            this.panning = true;
            if (pointer == 0) {
                this.tracker.start(this.pointer2.x, this.pointer2.y, Gdx.input.getCurrentEventTime());
                return false;
            }
            this.tracker.start(this.pointer1.x, this.pointer1.y, Gdx.input.getCurrentEventTime());
            return false;
        }
        this.gestureStartTime = 0L;
        long time = Gdx.input.getCurrentEventTime();
        if (time - this.tracker.lastTime >= this.maxFlingDelay) {
            return false;
        }
        this.tracker.update(x, y, time);
        return this.listener.fling(this.tracker.getVelocityX(), this.tracker.getVelocityY(), button);
    }

    public boolean isLongPressed() {
        return isLongPressed(this.longPressSeconds);
    }

    public boolean isLongPressed(float duration) {
        return this.gestureStartTime != 0 && TimeUtils.nanoTime() - this.gestureStartTime > ((long) (1.0E9f * duration));
    }

    public boolean isPanning() {
        return this.panning;
    }

    public void reset() {
        this.gestureStartTime = 0L;
        this.panning = false;
        this.inTapSquare = false;
    }

    private boolean isWithinTapSquare(float x, float y, float centerX, float centerY) {
        return Math.abs(x - centerX) < this.tapSquareSize && Math.abs(y - centerY) < this.tapSquareSize;
    }

    public void setTapSquareSize(int tapSquareSize) {
        this.tapSquareSize = tapSquareSize;
    }

    public void setTapCountInterval(float tapCountInterval) {
        this.tapCountInterval = (long) (1.0E9f * tapCountInterval);
    }

    public void setLongPressSeconds(float longPressSeconds) {
        this.longPressSeconds = longPressSeconds;
    }

    public void setMaxFlingDelay(long maxFlingDelay) {
        this.maxFlingDelay = maxFlingDelay;
    }

    public static class GestureAdapter implements GestureListener {
        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override // com.badlogic.gdx.input.GestureDetector.GestureListener
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    }

    static class VelocityTracker {
        float deltaX;
        float deltaY;
        long lastTime;
        float lastX;
        float lastY;
        int numSamples;
        int sampleSize = 10;
        float[] meanX = new float[this.sampleSize];
        float[] meanY = new float[this.sampleSize];
        long[] meanTime = new long[this.sampleSize];

        VelocityTracker() {
        }

        public void start(float x, float y, long timeStamp) {
            this.lastX = x;
            this.lastY = y;
            this.deltaX = 0.0f;
            this.deltaY = 0.0f;
            this.numSamples = 0;
            for (int i = 0; i < this.sampleSize; i++) {
                this.meanX[i] = 0.0f;
                this.meanY[i] = 0.0f;
                this.meanTime[i] = 0;
            }
            this.lastTime = timeStamp;
        }

        public void update(float x, float y, long timeStamp) {
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

        private float getSum(float[] values, int numSamples) {
            int numSamples2 = Math.min(this.sampleSize, numSamples);
            float sum = 0.0f;
            for (int i = 0; i < numSamples2; i++) {
                sum += values[i];
            }
            if (numSamples2 == 0) {
                return 0.0f;
            }
            return sum;
        }
    }
}
