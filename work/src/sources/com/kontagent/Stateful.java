package com.kontagent;

import java.util.Observable;

/* JADX INFO: loaded from: classes.dex */
public class Stateful extends Observable {
    private final Object mStateSync = new Object();
    private State mState = State.STOPPED;

    public enum State {
        STOPPED,
        PAUSED,
        RUNNING
    }

    private void setState(State state) {
        synchronized (this.mStateSync) {
            if (this.mState != state) {
                this.mState = state;
                this.mStateSync.notifyAll();
                setChanged();
                notifyObservers();
            }
        }
    }

    public State getState() {
        State state;
        synchronized (this.mStateSync) {
            state = this.mState;
        }
        return state;
    }

    public boolean isStarted() {
        State state = getState();
        return state == State.PAUSED || state == State.RUNNING;
    }

    public boolean isResumed() {
        return getState() == State.RUNNING;
    }

    public boolean isPaused() {
        return getState() == State.PAUSED;
    }

    public boolean isStopped() {
        return getState() == State.STOPPED;
    }

    public State waitStates(State... states) throws InterruptedException {
        State state;
        do {
            state = waitStates(Long.MAX_VALUE, states);
        } while (state == null);
        return state;
    }

    public State waitStates(long time, State... states) throws InterruptedException {
        State state;
        synchronized (this.mStateSync) {
            long endTime = System.currentTimeMillis() + time;
            loop0: while (true) {
                for (State state2 : states) {
                    if (this.mState.equals(state2)) {
                        state = this.mState;
                        break loop0;
                    }
                }
                long delta = endTime - System.currentTimeMillis();
                if (delta < 0) {
                    state = null;
                    break;
                }
                this.mStateSync.wait(delta);
            }
        }
        return state;
    }

    public void start() {
        switch (getState()) {
            case PAUSED:
                resume();
                break;
            case STOPPED:
                onStart();
                setState(State.PAUSED);
                onResume();
                setState(State.RUNNING);
                break;
        }
    }

    public boolean resume() {
        switch (getState()) {
            case RUNNING:
                return true;
            case PAUSED:
                onResume();
                setState(State.RUNNING);
                return true;
            case STOPPED:
                return false;
            default:
                return false;
        }
    }

    public boolean pause() {
        switch (getState()) {
            case RUNNING:
                onPause();
                setState(State.PAUSED);
                return true;
            case PAUSED:
                return true;
            case STOPPED:
                return false;
            default:
                return false;
        }
    }

    public void stop() {
        switch (getState()) {
            case RUNNING:
                onPause();
                setState(State.PAUSED);
                onStop();
                setState(State.STOPPED);
                break;
            case PAUSED:
                onStop();
                setState(State.STOPPED);
                break;
        }
    }

    private void assertState(State... validStates) {
        boolean valid = false;
        State curState = getState();
        for (State validState : validStates) {
            if (curState == validState) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            String msg = String.format("Operation can not be exuected in %s state.", curState.name());
            throw new IllegalStateException(msg);
        }
    }

    protected void assertStarted() {
        assertState(State.PAUSED, State.RUNNING);
    }

    protected void assertResumed() {
        assertState(State.RUNNING);
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }
}
