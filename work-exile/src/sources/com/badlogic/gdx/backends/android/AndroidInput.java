package com.badlogic.gdx.backends.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import java.util.ArrayList;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AndroidInput implements Input, View.OnKeyListener, View.OnTouchListener, SensorEventListener {
    final AndroidApplication app;
    private final AndroidApplicationConfiguration config;
    private Handler handle;
    final boolean hasMultitouch;
    boolean keyboardAvailable;
    private SensorManager manager;
    private final Input.Orientation nativeOrientation;
    private InputProcessor processor;
    private int sleepTime;
    private final AndroidTouchHandler touchHandler;
    private final Vibrator vibrator;
    Pool<KeyEvent> usedKeyEvents = new Pool<KeyEvent>(16, 1000) { // from class: com.badlogic.gdx.backends.android.AndroidInput.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public KeyEvent newObject() {
            return AndroidInput.this.new KeyEvent();
        }
    };
    Pool<TouchEvent> usedTouchEvents = new Pool<TouchEvent>(16, 1000) { // from class: com.badlogic.gdx.backends.android.AndroidInput.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public TouchEvent newObject() {
            return AndroidInput.this.new TouchEvent();
        }
    };
    ArrayList<KeyEvent> keyEvents = new ArrayList<>();
    ArrayList<TouchEvent> touchEvents = new ArrayList<>();
    int[] touchX = new int[20];
    int[] touchY = new int[20];
    int[] deltaX = new int[20];
    int[] deltaY = new int[20];
    boolean[] touched = new boolean[20];
    int[] realId = new int[10];
    private IntMap<Object> keys = new IntMap<>();
    public boolean accelerometerAvailable = false;
    private final float[] accelerometerValues = new float[3];
    private String text = null;
    private Input.TextInputListener textListener = null;
    private boolean catchBack = false;
    private boolean catchMenu = false;
    private boolean compassAvailable = false;
    private final float[] magneticFieldValues = new float[3];
    private float azimuth = 0.0f;
    private float pitch = 0.0f;
    private float roll = 0.0f;
    private float inclination = 0.0f;
    private boolean justTouched = false;
    private long currentEventTimeStamp = System.nanoTime();
    boolean requestFocus = true;
    final float[] R = new float[9];
    final float[] orientation = new float[3];

    class KeyEvent {
        static final int KEY_DOWN = 0;
        static final int KEY_TYPED = 2;
        static final int KEY_UP = 1;
        char keyChar;
        int keyCode;
        long timeStamp;
        int type;

        KeyEvent() {
        }
    }

    class TouchEvent {
        static final int TOUCH_DOWN = 0;
        static final int TOUCH_DRAGGED = 2;
        static final int TOUCH_UP = 1;
        int pointer;
        long timeStamp;
        int type;
        int x;
        int y;

        TouchEvent() {
        }
    }

    public AndroidInput(AndroidApplication activity, View view, AndroidApplicationConfiguration config) {
        this.sleepTime = 0;
        view.setOnKeyListener(this);
        view.setOnTouchListener(this);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
        this.config = config;
        for (int i = 0; i < this.realId.length; i++) {
            this.realId[i] = -1;
        }
        this.handle = new Handler();
        this.app = activity;
        this.sleepTime = config.touchSleepTime;
        int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
        if (sdkVersion >= 5) {
            this.touchHandler = new AndroidMultiTouchHandler();
        } else {
            this.touchHandler = new AndroidSingleTouchHandler();
        }
        this.hasMultitouch = this.touchHandler.supportsMultitouch(this.app);
        this.vibrator = (Vibrator) activity.getSystemService("vibrator");
        int rotation = getRotation();
        Graphics.DisplayMode mode = this.app.graphics.getDesktopDisplayMode();
        if (((rotation == 0 || rotation == 180) && mode.width >= mode.height) || ((rotation == 90 || rotation == 270) && mode.width <= mode.height)) {
            this.nativeOrientation = Input.Orientation.Landscape;
        } else {
            this.nativeOrientation = Input.Orientation.Portrait;
        }
    }

    @Override // com.badlogic.gdx.Input
    public float getAccelerometerX() {
        return this.accelerometerValues[0];
    }

    @Override // com.badlogic.gdx.Input
    public float getAccelerometerY() {
        return this.accelerometerValues[1];
    }

    @Override // com.badlogic.gdx.Input
    public float getAccelerometerZ() {
        return this.accelerometerValues[2];
    }

    @Override // com.badlogic.gdx.Input
    public void getTextInput(final Input.TextInputListener listener, final String title, final String text) {
        this.handle.post(new Runnable() { // from class: com.badlogic.gdx.backends.android.AndroidInput.3
            @Override // java.lang.Runnable
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(AndroidInput.this.app);
                alert.setTitle(title);
                final EditText input = new EditText(AndroidInput.this.app);
                input.setText(text);
                input.setSingleLine();
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() { // from class: com.badlogic.gdx.backends.android.AndroidInput.3.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.input(input.getText().toString());
                    }
                });
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.badlogic.gdx.backends.android.AndroidInput.3.2
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface arg0) {
                        listener.canceled();
                    }
                });
                alert.show();
            }
        });
    }

    @Override // com.badlogic.gdx.Input
    public int getX() {
        int i;
        synchronized (this) {
            i = this.touchX[0];
        }
        return i;
    }

    @Override // com.badlogic.gdx.Input
    public int getY() {
        int i;
        synchronized (this) {
            i = this.touchY[0];
        }
        return i;
    }

    @Override // com.badlogic.gdx.Input
    public int getX(int pointer) {
        int i;
        synchronized (this) {
            i = this.touchX[pointer];
        }
        return i;
    }

    @Override // com.badlogic.gdx.Input
    public int getY(int pointer) {
        int i;
        synchronized (this) {
            i = this.touchY[pointer];
        }
        return i;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isTouched(int pointer) {
        boolean z;
        synchronized (this) {
            z = this.touched[pointer];
        }
        return z;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isKeyPressed(int key) {
        boolean zContainsKey;
        synchronized (this) {
            try {
                if (key == -1) {
                    zContainsKey = this.keys.size > 0;
                } else {
                    zContainsKey = this.keys.containsKey(key);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return zContainsKey;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isTouched() {
        boolean z;
        synchronized (this) {
            z = this.touched[0];
        }
        return z;
    }

    @Override // com.badlogic.gdx.Input
    public void setInputProcessor(InputProcessor processor) {
        synchronized (this) {
            this.processor = processor;
        }
    }

    void processEvents() {
        synchronized (this) {
            this.justTouched = false;
            if (this.processor != null) {
                InputProcessor processor = this.processor;
                int len = this.keyEvents.size();
                for (int i = 0; i < len; i++) {
                    KeyEvent e = this.keyEvents.get(i);
                    this.currentEventTimeStamp = e.timeStamp;
                    switch (e.type) {
                        case 0:
                            processor.keyDown(e.keyCode);
                            break;
                        case 1:
                            processor.keyUp(e.keyCode);
                            break;
                        case 2:
                            processor.keyTyped(e.keyChar);
                            break;
                    }
                    this.usedKeyEvents.free(e);
                }
                int len2 = this.touchEvents.size();
                for (int i2 = 0; i2 < len2; i2++) {
                    TouchEvent e2 = this.touchEvents.get(i2);
                    this.currentEventTimeStamp = e2.timeStamp;
                    switch (e2.type) {
                        case 0:
                            processor.touchDown(e2.x, e2.y, e2.pointer, 0);
                            this.justTouched = true;
                            break;
                        case 1:
                            processor.touchUp(e2.x, e2.y, e2.pointer, 0);
                            break;
                        case 2:
                            processor.touchDragged(e2.x, e2.y, e2.pointer);
                            break;
                    }
                    this.usedTouchEvents.free(e2);
                }
            } else {
                int len3 = this.touchEvents.size();
                for (int i3 = 0; i3 < len3; i3++) {
                    TouchEvent e3 = this.touchEvents.get(i3);
                    if (e3.type == 0) {
                        this.justTouched = true;
                    }
                    this.usedTouchEvents.free(e3);
                }
                int len4 = this.keyEvents.size();
                for (int i4 = 0; i4 < len4; i4++) {
                    this.usedKeyEvents.free(this.keyEvents.get(i4));
                }
            }
            if (this.touchEvents.size() == 0) {
                for (int i5 = 0; i5 < this.deltaX.length; i5++) {
                    this.deltaX[0] = 0;
                    this.deltaY[0] = 0;
                }
            }
            this.keyEvents.clear();
            this.touchEvents.clear();
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent event) {
        if (this.requestFocus) {
            view.requestFocus();
            view.requestFocusFromTouch();
            this.requestFocus = false;
        }
        this.touchHandler.onTouch(event, this);
        if (this.sleepTime != 0) {
            try {
                Thread.sleep(this.sleepTime);
                return true;
            } catch (InterruptedException e) {
                return true;
            }
        }
        return true;
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View v, int keyCode, android.view.KeyEvent e) {
        synchronized (this) {
            char character = (char) e.getUnicodeChar();
            if (keyCode == 67) {
                character = '\b';
            }
            switch (e.getAction()) {
                case 0:
                    KeyEvent event = this.usedKeyEvents.obtain();
                    event.keyChar = (char) 0;
                    event.keyCode = e.getKeyCode();
                    event.type = 0;
                    if (keyCode == 4 && e.isAltPressed()) {
                        keyCode = 255;
                        event.keyCode = 255;
                    }
                    this.keyEvents.add(event);
                    this.keys.put(event.keyCode, null);
                    break;
                case 1:
                    KeyEvent event2 = this.usedKeyEvents.obtain();
                    event2.keyChar = (char) 0;
                    event2.keyCode = e.getKeyCode();
                    event2.type = 1;
                    if (keyCode == 4 && e.isAltPressed()) {
                        keyCode = 255;
                        event2.keyCode = 255;
                    }
                    this.keyEvents.add(event2);
                    KeyEvent event3 = this.usedKeyEvents.obtain();
                    event3.keyChar = character;
                    event3.keyCode = 0;
                    event3.type = 2;
                    this.keyEvents.add(event3);
                    if (keyCode == 255) {
                        this.keys.remove(255);
                    } else {
                        this.keys.remove(e.getKeyCode());
                    }
                    break;
            }
        }
        if (keyCode == 255) {
            return true;
        }
        if (this.catchBack && keyCode == 4) {
            return true;
        }
        return this.catchMenu && keyCode == 82;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 1) {
            if (this.nativeOrientation == Input.Orientation.Portrait) {
                System.arraycopy(event.values, 0, this.accelerometerValues, 0, this.accelerometerValues.length);
            } else {
                this.accelerometerValues[0] = event.values[1];
                this.accelerometerValues[1] = -event.values[0];
                this.accelerometerValues[2] = event.values[2];
            }
        }
        if (event.sensor.getType() == 2) {
            System.arraycopy(event.values, 0, this.magneticFieldValues, 0, this.magneticFieldValues.length);
        }
    }

    @Override // com.badlogic.gdx.Input
    public void setOnscreenKeyboardVisible(final boolean visible) {
        this.handle.post(new Runnable() { // from class: com.badlogic.gdx.backends.android.AndroidInput.4
            @Override // java.lang.Runnable
            public void run() {
                InputMethodManager manager = (InputMethodManager) AndroidInput.this.app.getSystemService("input_method");
                if (visible) {
                    View view = ((AndroidGraphics) AndroidInput.this.app.getGraphics()).getView();
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    manager.showSoftInput(((AndroidGraphics) AndroidInput.this.app.getGraphics()).getView(), 0);
                    return;
                }
                manager.hideSoftInputFromWindow(((AndroidGraphics) AndroidInput.this.app.getGraphics()).getView().getWindowToken(), 0);
            }
        });
    }

    @Override // com.badlogic.gdx.Input
    public void setCatchBackKey(boolean catchBack) {
        this.catchBack = catchBack;
    }

    @Override // com.badlogic.gdx.Input
    public void setCatchMenuKey(boolean catchMenu) {
        this.catchMenu = catchMenu;
    }

    @Override // com.badlogic.gdx.Input
    public void vibrate(int milliseconds) {
        this.vibrator.vibrate(milliseconds);
    }

    @Override // com.badlogic.gdx.Input
    public void vibrate(long[] pattern, int repeat) {
        this.vibrator.vibrate(pattern, repeat);
    }

    @Override // com.badlogic.gdx.Input
    public void cancelVibrate() {
        this.vibrator.cancel();
    }

    @Override // com.badlogic.gdx.Input
    public boolean justTouched() {
        return this.justTouched;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isButtonPressed(int button) {
        if (button == 0) {
            return isTouched();
        }
        return false;
    }

    private void updateOrientation() {
        if (SensorManager.getRotationMatrix(this.R, null, this.accelerometerValues, this.magneticFieldValues)) {
            SensorManager.getOrientation(this.R, this.orientation);
            this.azimuth = (float) Math.toDegrees(this.orientation[0]);
            this.pitch = (float) Math.toDegrees(this.orientation[1]);
            this.roll = (float) Math.toDegrees(this.orientation[2]);
        }
    }

    @Override // com.badlogic.gdx.Input
    public float getAzimuth() {
        if (!this.compassAvailable) {
            return 0.0f;
        }
        updateOrientation();
        return this.azimuth;
    }

    @Override // com.badlogic.gdx.Input
    public float getPitch() {
        if (!this.compassAvailable) {
            return 0.0f;
        }
        updateOrientation();
        return this.pitch;
    }

    @Override // com.badlogic.gdx.Input
    public float getRoll() {
        if (!this.compassAvailable) {
            return 0.0f;
        }
        updateOrientation();
        return this.roll;
    }

    void registerSensorListeners() {
        if (this.config.useAccelerometer) {
            this.manager = (SensorManager) this.app.getSystemService("sensor");
            if (this.manager.getSensorList(1).size() == 0) {
                this.accelerometerAvailable = false;
            } else {
                Sensor accelerometer = this.manager.getSensorList(1).get(0);
                this.accelerometerAvailable = this.manager.registerListener(this, accelerometer, 1);
            }
        } else {
            this.accelerometerAvailable = false;
        }
        if (this.config.useCompass) {
            if (this.manager == null) {
                this.manager = (SensorManager) this.app.getSystemService("sensor");
            }
            Sensor sensor = this.manager.getDefaultSensor(2);
            if (sensor != null) {
                this.compassAvailable = this.accelerometerAvailable;
                if (this.compassAvailable) {
                    this.compassAvailable = this.manager.registerListener(this, sensor, 1);
                }
            } else {
                this.compassAvailable = false;
            }
        } else {
            this.compassAvailable = false;
        }
        Gdx.app.log("AndroidInput", "sensor listener setup");
    }

    void unregisterSensorListeners() {
        if (this.manager != null) {
            this.manager.unregisterListener(this);
            this.manager = null;
        }
        Gdx.app.log("AndroidInput", "sensor listener tear down");
    }

    @Override // com.badlogic.gdx.Input
    public InputProcessor getInputProcessor() {
        return this.processor;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isPeripheralAvailable(Input.Peripheral peripheral) {
        if (peripheral == Input.Peripheral.Accelerometer) {
            return this.accelerometerAvailable;
        }
        if (peripheral == Input.Peripheral.Compass) {
            return this.compassAvailable;
        }
        if (peripheral == Input.Peripheral.HardwareKeyboard) {
            return this.keyboardAvailable;
        }
        if (peripheral == Input.Peripheral.OnscreenKeyboard) {
            return true;
        }
        if (peripheral == Input.Peripheral.Vibrator) {
            return this.vibrator != null;
        }
        if (peripheral == Input.Peripheral.MultitouchScreen) {
            return this.hasMultitouch;
        }
        return false;
    }

    public int getFreePointerIndex() {
        int len = this.realId.length;
        for (int i = 0; i < len; i++) {
            if (this.realId[i] == -1) {
                return i;
            }
        }
        int[] tmp = new int[this.realId.length + 1];
        System.arraycopy(this.realId, 0, tmp, 0, this.realId.length);
        this.realId = tmp;
        int i2 = tmp.length - 1;
        return i2;
    }

    public int lookUpPointerIndex(int pointerId) {
        int len = this.realId.length;
        for (int i = 0; i < len; i++) {
            if (this.realId[i] == pointerId) {
                return i;
            }
        }
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < len; i2++) {
            buf.append(i2 + ":" + this.realId[i2] + " ");
        }
        Gdx.app.log("AndroidInput", "Pointer ID lookup failed: " + pointerId + ", " + buf.toString());
        return -1;
    }

    @Override // com.badlogic.gdx.Input
    public int getRotation() {
        int orientation = this.app.getWindowManager().getDefaultDisplay().getOrientation();
        switch (orientation) {
            case 0:
            default:
                return 0;
            case 1:
                return 90;
            case 2:
                return Opcodes.GETFIELD;
            case 3:
                return 270;
        }
    }

    @Override // com.badlogic.gdx.Input
    public Input.Orientation getNativeOrientation() {
        return this.nativeOrientation;
    }

    @Override // com.badlogic.gdx.Input
    public void setCursorCatched(boolean catched) {
    }

    @Override // com.badlogic.gdx.Input
    public boolean isCursorCatched() {
        return false;
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaX() {
        return this.deltaX[0];
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaX(int pointer) {
        return this.deltaX[pointer];
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaY() {
        return this.deltaY[0];
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaY(int pointer) {
        return this.deltaY[pointer];
    }

    @Override // com.badlogic.gdx.Input
    public void setCursorPosition(int x, int y) {
    }

    @Override // com.badlogic.gdx.Input
    public long getCurrentEventTime() {
        return this.currentEventTimeStamp;
    }
}
