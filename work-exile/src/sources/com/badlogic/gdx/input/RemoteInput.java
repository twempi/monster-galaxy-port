package com.badlogic.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class RemoteInput implements Runnable, Input {
    public static int DEFAULT_PORT = 8190;
    private float[] accel;
    private float[] compass;
    public final String[] ips;
    boolean[] isTouched;
    boolean justTouched;
    Set<Integer> keys;
    private boolean multiTouch;
    private final int port;
    InputProcessor processor;
    private float remoteHeight;
    private float remoteWidth;
    private ServerSocket serverSocket;
    int[] touchX;
    int[] touchY;

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

    class EventTrigger implements Runnable {
        KeyEvent keyEvent;
        TouchEvent touchEvent;

        public EventTrigger(TouchEvent touchEvent, KeyEvent keyEvent) {
            this.touchEvent = touchEvent;
            this.keyEvent = keyEvent;
        }

        @Override // java.lang.Runnable
        public void run() {
            RemoteInput.this.justTouched = false;
            if (RemoteInput.this.processor != null) {
                if (this.touchEvent != null) {
                    RemoteInput.this.touchX[this.touchEvent.pointer] = this.touchEvent.x;
                    RemoteInput.this.touchY[this.touchEvent.pointer] = this.touchEvent.y;
                    switch (this.touchEvent.type) {
                        case 0:
                            RemoteInput.this.processor.touchDown(this.touchEvent.x, this.touchEvent.y, this.touchEvent.pointer, 0);
                            RemoteInput.this.isTouched[this.touchEvent.pointer] = true;
                            RemoteInput.this.justTouched = true;
                            break;
                        case 1:
                            RemoteInput.this.processor.touchUp(this.touchEvent.x, this.touchEvent.y, this.touchEvent.pointer, 0);
                            RemoteInput.this.isTouched[this.touchEvent.pointer] = false;
                            break;
                        case 2:
                            RemoteInput.this.processor.touchDragged(this.touchEvent.x, this.touchEvent.y, this.touchEvent.pointer);
                            break;
                    }
                }
                if (this.keyEvent != null) {
                    switch (this.keyEvent.type) {
                        case 0:
                            RemoteInput.this.processor.keyDown(this.keyEvent.keyCode);
                            RemoteInput.this.keys.add(Integer.valueOf(this.keyEvent.keyCode));
                            break;
                        case 1:
                            RemoteInput.this.processor.keyUp(this.keyEvent.keyCode);
                            RemoteInput.this.keys.remove(Integer.valueOf(this.keyEvent.keyCode));
                            break;
                        case 2:
                            RemoteInput.this.processor.keyTyped(this.keyEvent.keyChar);
                            break;
                    }
                    return;
                }
                return;
            }
            if (this.touchEvent != null) {
                RemoteInput.this.touchX[this.touchEvent.pointer] = this.touchEvent.x;
                RemoteInput.this.touchY[this.touchEvent.pointer] = this.touchEvent.y;
                if (this.touchEvent.type == 0) {
                    RemoteInput.this.isTouched[this.touchEvent.pointer] = true;
                    RemoteInput.this.justTouched = true;
                }
                if (this.touchEvent.type == 1) {
                    RemoteInput.this.isTouched[this.touchEvent.pointer] = false;
                }
            }
            if (this.keyEvent != null) {
                if (this.keyEvent.type == 0) {
                    RemoteInput.this.keys.add(Integer.valueOf(this.keyEvent.keyCode));
                }
                if (this.keyEvent.type == 1) {
                    RemoteInput.this.keys.remove(Integer.valueOf(this.keyEvent.keyCode));
                }
            }
        }
    }

    public RemoteInput() {
        this(DEFAULT_PORT);
    }

    public RemoteInput(int port) {
        this.accel = new float[3];
        this.compass = new float[3];
        this.multiTouch = false;
        this.remoteWidth = 0.0f;
        this.remoteHeight = 0.0f;
        this.keys = new HashSet();
        this.touchX = new int[20];
        this.touchY = new int[20];
        this.isTouched = new boolean[20];
        this.justTouched = false;
        this.processor = null;
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
            InetAddress[] allByName = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            this.ips = new String[allByName.length];
            for (int i = 0; i < allByName.length; i++) {
                this.ips[i] = allByName[i].getHostAddress();
            }
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't open listening socket at port '" + port + "'", e);
        }
    }

    /* JADX INFO: Infinite loop detected, blocks: 27, insns: 0 */
    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                System.out.println("listening, port " + this.port);
                Socket socket = this.serverSocket.accept();
                socket.setTcpNoDelay(true);
                socket.setSoTimeout(3000);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                this.multiTouch = in.readBoolean();
                while (true) {
                    int event = in.readInt();
                    KeyEvent keyEvent = null;
                    TouchEvent touchEvent = null;
                    switch (event) {
                        case 0:
                            keyEvent = new KeyEvent();
                            keyEvent.keyCode = in.readInt();
                            keyEvent.type = 0;
                            break;
                        case 1:
                            keyEvent = new KeyEvent();
                            keyEvent.keyCode = in.readInt();
                            keyEvent.type = 1;
                            break;
                        case 2:
                            keyEvent = new KeyEvent();
                            keyEvent.keyChar = in.readChar();
                            keyEvent.type = 2;
                            break;
                        case 3:
                            touchEvent = new TouchEvent();
                            touchEvent.x = (int) ((in.readInt() / this.remoteWidth) * Gdx.graphics.getWidth());
                            touchEvent.y = (int) ((in.readInt() / this.remoteHeight) * Gdx.graphics.getHeight());
                            touchEvent.pointer = in.readInt();
                            touchEvent.type = 0;
                            break;
                        case 4:
                            touchEvent = new TouchEvent();
                            touchEvent.x = (int) ((in.readInt() / this.remoteWidth) * Gdx.graphics.getWidth());
                            touchEvent.y = (int) ((in.readInt() / this.remoteHeight) * Gdx.graphics.getHeight());
                            touchEvent.pointer = in.readInt();
                            touchEvent.type = 1;
                            break;
                        case 5:
                            touchEvent = new TouchEvent();
                            touchEvent.x = (int) ((in.readInt() / this.remoteWidth) * Gdx.graphics.getWidth());
                            touchEvent.y = (int) ((in.readInt() / this.remoteHeight) * Gdx.graphics.getHeight());
                            touchEvent.pointer = in.readInt();
                            touchEvent.type = 2;
                            break;
                        case 6:
                            this.accel[0] = in.readFloat();
                            this.accel[1] = in.readFloat();
                            this.accel[2] = in.readFloat();
                            break;
                        case 7:
                            this.compass[0] = in.readFloat();
                            this.compass[1] = in.readFloat();
                            this.compass[2] = in.readFloat();
                            break;
                        case 8:
                            this.remoteWidth = in.readFloat();
                            this.remoteHeight = in.readFloat();
                            break;
                    }
                    Gdx.app.postRunnable(new EventTrigger(touchEvent, keyEvent));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.badlogic.gdx.Input
    public float getAccelerometerX() {
        return this.accel[0];
    }

    @Override // com.badlogic.gdx.Input
    public float getAccelerometerY() {
        return this.accel[1];
    }

    @Override // com.badlogic.gdx.Input
    public float getAccelerometerZ() {
        return this.accel[2];
    }

    @Override // com.badlogic.gdx.Input
    public int getX() {
        return this.touchX[0];
    }

    @Override // com.badlogic.gdx.Input
    public int getX(int pointer) {
        return this.touchX[pointer];
    }

    @Override // com.badlogic.gdx.Input
    public int getY() {
        return this.touchY[0];
    }

    @Override // com.badlogic.gdx.Input
    public int getY(int pointer) {
        return this.touchY[pointer];
    }

    @Override // com.badlogic.gdx.Input
    public boolean isTouched() {
        return this.isTouched[0];
    }

    @Override // com.badlogic.gdx.Input
    public boolean justTouched() {
        return this.justTouched;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isTouched(int pointer) {
        return this.isTouched[pointer];
    }

    @Override // com.badlogic.gdx.Input
    public boolean isButtonPressed(int button) {
        if (button != 0) {
            return false;
        }
        for (int i = 0; i < this.isTouched.length; i++) {
            if (this.isTouched[i]) {
                return true;
            }
        }
        return false;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isKeyPressed(int key) {
        return this.keys.contains(Integer.valueOf(key));
    }

    @Override // com.badlogic.gdx.Input
    public void getTextInput(Input.TextInputListener listener, String title, String text) {
        Gdx.app.getInput().getTextInput(listener, title, text);
    }

    @Override // com.badlogic.gdx.Input
    public void setOnscreenKeyboardVisible(boolean visible) {
    }

    @Override // com.badlogic.gdx.Input
    public void vibrate(int milliseconds) {
    }

    @Override // com.badlogic.gdx.Input
    public void vibrate(long[] pattern, int repeat) {
    }

    @Override // com.badlogic.gdx.Input
    public void cancelVibrate() {
    }

    @Override // com.badlogic.gdx.Input
    public float getAzimuth() {
        return this.compass[0];
    }

    @Override // com.badlogic.gdx.Input
    public float getPitch() {
        return this.compass[1];
    }

    @Override // com.badlogic.gdx.Input
    public float getRoll() {
        return this.compass[2];
    }

    @Override // com.badlogic.gdx.Input
    public void setCatchBackKey(boolean catchBack) {
    }

    @Override // com.badlogic.gdx.Input
    public void setInputProcessor(InputProcessor processor) {
        this.processor = processor;
    }

    @Override // com.badlogic.gdx.Input
    public InputProcessor getInputProcessor() {
        return this.processor;
    }

    public String[] getIPs() {
        return this.ips;
    }

    @Override // com.badlogic.gdx.Input
    public boolean isPeripheralAvailable(Input.Peripheral peripheral) {
        if (peripheral == Input.Peripheral.Accelerometer || peripheral == Input.Peripheral.Compass) {
            return true;
        }
        if (peripheral == Input.Peripheral.MultitouchScreen) {
            return this.multiTouch;
        }
        return false;
    }

    @Override // com.badlogic.gdx.Input
    public int getRotation() {
        return 0;
    }

    @Override // com.badlogic.gdx.Input
    public Input.Orientation getNativeOrientation() {
        return Input.Orientation.Landscape;
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
        return 0;
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaX(int pointer) {
        return 0;
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaY() {
        return 0;
    }

    @Override // com.badlogic.gdx.Input
    public int getDeltaY(int pointer) {
        return 0;
    }

    @Override // com.badlogic.gdx.Input
    public void setCursorPosition(int x, int y) {
    }

    @Override // com.badlogic.gdx.Input
    public void setCatchMenuKey(boolean catchMenu) {
    }

    @Override // com.badlogic.gdx.Input
    public long getCurrentEventTime() {
        return 0L;
    }
}
