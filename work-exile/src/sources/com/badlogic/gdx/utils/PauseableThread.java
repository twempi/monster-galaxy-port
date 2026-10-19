package com.badlogic.gdx.utils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class PauseableThread extends Thread {
    final Runnable runnable;
    boolean paused = false;
    boolean exit = false;

    public PauseableThread(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            synchronized (this) {
                while (this.paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.exit) {
                return;
            } else {
                this.runnable.run();
            }
        }
    }

    public void onPause() {
        this.paused = true;
    }

    public void onResume() {
        synchronized (this) {
            this.paused = false;
            notifyAll();
        }
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void stopThread() {
        this.exit = true;
        if (this.paused) {
            onResume();
        }
    }
}
