package com.badlogic.gdx.backends.android;

import android.content.Context;
import android.view.MotionEvent;
import com.badlogic.gdx.Gdx;

/* JADX INFO: loaded from: classes.dex */
public class AndroidMultiTouchHandler implements AndroidTouchHandler {
    @Override // com.badlogic.gdx.backends.android.AndroidTouchHandler
    public void onTouch(MotionEvent event, AndroidInput input) {
        int action = event.getAction() & 255;
        int pointerIndex = (event.getAction() & 65280) >> 8;
        int pointerId = event.getPointerId(pointerIndex);
        long timeStamp = System.nanoTime();
        synchronized (input) {
            try {
                switch (action) {
                    case 0:
                    case 5:
                        int realPointerIndex = input.getFreePointerIndex();
                        input.realId[realPointerIndex] = pointerId;
                        int x = (int) event.getX(pointerIndex);
                        int y = (int) event.getY(pointerIndex);
                        postTouchEvent(input, 0, x, y, realPointerIndex, timeStamp);
                        input.touchX[realPointerIndex] = x;
                        input.touchY[realPointerIndex] = y;
                        input.deltaX[realPointerIndex] = 0;
                        input.deltaY[realPointerIndex] = 0;
                        input.touched[realPointerIndex] = true;
                        break;
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                        int realPointerIndex2 = input.lookUpPointerIndex(pointerId);
                        if (realPointerIndex2 != -1) {
                            input.realId[realPointerIndex2] = -1;
                            int x2 = (int) event.getX(pointerIndex);
                            int y2 = (int) event.getY(pointerIndex);
                            postTouchEvent(input, 1, x2, y2, realPointerIndex2, timeStamp);
                            input.touchX[realPointerIndex2] = x2;
                            input.touchY[realPointerIndex2] = y2;
                            input.deltaX[realPointerIndex2] = 0;
                            input.deltaY[realPointerIndex2] = 0;
                            input.touched[realPointerIndex2] = false;
                        }
                        break;
                    case 2:
                        int pointerCount = event.getPointerCount();
                        for (int i = 0; i < pointerCount; i++) {
                            int pointerIndex2 = i;
                            int pointerId2 = event.getPointerId(pointerIndex2);
                            int x3 = (int) event.getX(pointerIndex2);
                            int y3 = (int) event.getY(pointerIndex2);
                            int realPointerIndex3 = input.lookUpPointerIndex(pointerId2);
                            if (realPointerIndex3 != -1) {
                                postTouchEvent(input, 2, x3, y3, realPointerIndex3, timeStamp);
                                input.deltaX[realPointerIndex3] = x3 - input.touchX[realPointerIndex3];
                                input.deltaY[realPointerIndex3] = y3 - input.touchY[realPointerIndex3];
                                input.touchX[realPointerIndex3] = x3;
                                input.touchY[realPointerIndex3] = y3;
                            }
                        }
                        break;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Gdx.app.getGraphics().requestRendering();
    }

    private void logAction(int action, int pointer) {
        String actionStr;
        if (action == 0) {
            actionStr = "DOWN";
        } else if (action == 5) {
            actionStr = "POINTER DOWN";
        } else if (action == 1) {
            actionStr = "UP";
        } else if (action == 6) {
            actionStr = "POINTER UP";
        } else if (action == 4) {
            actionStr = "OUTSIDE";
        } else if (action == 3) {
            actionStr = "CANCEL";
        } else if (action == 2) {
            actionStr = "MOVE";
        } else {
            actionStr = "UNKNOWN (" + action + ")";
        }
        Gdx.app.log("AndroidMultiTouchHandler", "action " + actionStr + ", Android pointer id: " + pointer);
    }

    private void postTouchEvent(AndroidInput input, int type, int x, int y, int pointer, long timeStamp) {
        AndroidInput.TouchEvent event = input.usedTouchEvents.obtain();
        event.timeStamp = timeStamp;
        event.pointer = pointer;
        event.x = x;
        event.y = y;
        event.type = type;
        input.touchEvents.add(event);
    }

    @Override // com.badlogic.gdx.backends.android.AndroidTouchHandler
    public boolean supportsMultitouch(Context activity) {
        return activity.getPackageManager().hasSystemFeature("android.hardware.touchscreen.multitouch");
    }
}
