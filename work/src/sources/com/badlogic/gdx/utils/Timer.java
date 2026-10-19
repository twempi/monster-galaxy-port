package com.badlogic.gdx.utils;

import com.badlogic.gdx.Gdx;

/* JADX INFO: loaded from: classes.dex */
public class Timer {
    private static final int CANCELLED = -1;
    private static final int FOREVER = -2;
    public static final Timer instance = new Timer();
    private boolean posted;
    private boolean stopped;
    private final Array<Task> tasks = new Array<>(false, 8);
    private final Runnable timerRunnable = new Runnable() { // from class: com.badlogic.gdx.utils.Timer.1
        @Override // java.lang.Runnable
        public void run() {
            Timer.this.update();
        }
    };

    public void postTask(Task task) {
        scheduleTask(task, 0.0f, 0.0f, 0);
    }

    public void scheduleTask(Task task) {
        scheduleTask(task, 0.0f, 0.0f, -2);
    }

    public void scheduleTask(Task task, float delaySeconds) {
        scheduleTask(task, delaySeconds, 0.0f, 0);
    }

    public void scheduleTask(Task task, float delaySeconds, float intervalSeconds) {
        scheduleTask(task, delaySeconds, intervalSeconds, -2);
    }

    public void scheduleTask(Task task, float delaySeconds, float intervalSeconds, int repeatCount) {
        if (task.repeatCount != -1) {
            throw new IllegalArgumentException("The same task may not be scheduled twice.");
        }
        task.delaySeconds = delaySeconds;
        task.intervalSeconds = intervalSeconds;
        task.repeatCount = repeatCount;
        this.tasks.add(task);
        postRunnable();
    }

    public void stop() {
        this.stopped = true;
    }

    public void start() {
        this.stopped = false;
        postRunnable();
    }

    public void clear() {
        int n = this.tasks.size;
        for (int i = 0; i < n; i++) {
            this.tasks.get(i).cancel();
        }
        this.tasks.clear();
    }

    private void postRunnable() {
        if (!this.stopped && !this.posted) {
            this.posted = true;
            Gdx.app.postRunnable(this.timerRunnable);
        }
    }

    void update() {
        if (this.stopped) {
            this.posted = false;
            return;
        }
        float delta = Gdx.graphics.getDeltaTime();
        int i = 0;
        int n = this.tasks.size;
        while (i < n) {
            Task task = this.tasks.get(i);
            task.delaySeconds -= delta;
            if (task.delaySeconds <= 0.0f) {
                if (task.repeatCount != -1) {
                    if (task.repeatCount == 0) {
                        task.repeatCount = -1;
                    }
                    task.run();
                }
                if (task.repeatCount == -1) {
                    this.tasks.removeIndex(i);
                    i--;
                    n--;
                } else {
                    task.delaySeconds = task.intervalSeconds;
                    if (task.repeatCount > 0) {
                        task.repeatCount--;
                    }
                }
            }
            i++;
        }
        if (this.tasks.size == 0) {
            this.posted = false;
        } else {
            Gdx.app.postRunnable(this.timerRunnable);
        }
    }

    public static void post(Task task) {
        instance.postTask(task);
    }

    public static void schedule(Task task) {
        instance.scheduleTask(task);
    }

    public static void schedule(Task task, float delaySeconds) {
        instance.scheduleTask(task, delaySeconds);
    }

    public static void schedule(Task task, float delaySeconds, float intervalSeconds) {
        instance.scheduleTask(task, delaySeconds, intervalSeconds);
    }

    public static void schedule(Task task, float delaySeconds, float intervalSeconds, int repeatCount) {
        instance.scheduleTask(task, delaySeconds, intervalSeconds, repeatCount);
    }

    public static abstract class Task implements Runnable {
        float delaySeconds;
        float intervalSeconds;
        int repeatCount = -1;

        @Override // java.lang.Runnable
        public abstract void run();

        public void cancel() {
            this.delaySeconds = 0.0f;
            this.repeatCount = -1;
        }

        public boolean isScheduled() {
            return this.repeatCount != -1;
        }
    }
}
