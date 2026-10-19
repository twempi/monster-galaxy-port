package com.badlogic.gdx.backends.android.surfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.Writer;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class GLBaseSurfaceView extends GLSurfaceView implements SurfaceHolder.Callback {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    private static final boolean DRAW_TWICE_AFTER_SIZE_CHANGED = true;
    private static final boolean LOG_RENDERER = false;
    private static final boolean LOG_SURFACE = false;
    private static final boolean LOG_THREADS = false;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    static final GLThreadManager sGLThreadManager = new GLThreadManager();
    int mDebugFlags;
    GLSurfaceView.EGLConfigChooser mEGLConfigChooser;
    EGLContextFactory mEGLContextFactory;
    EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private GLThread mGLThread;
    GLWrapper mGLWrapper;
    boolean mSizeChanged;

    public interface EGLContextFactory {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    public interface EGLWindowSurfaceFactory {
        EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    public interface GLWrapper {
        GL wrap(GL gl);
    }

    public GLBaseSurfaceView(Context context) {
        super(context);
        this.mSizeChanged = true;
        init();
    }

    public GLBaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSizeChanged = true;
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    public void setGLWrapper(GLWrapper glWrapper) {
        this.mGLWrapper = glWrapper;
    }

    @Override // android.opengl.GLSurfaceView
    public void setDebugFlags(int debugFlags) {
        this.mDebugFlags = debugFlags;
    }

    @Override // android.opengl.GLSurfaceView
    public int getDebugFlags() {
        return this.mDebugFlags;
    }

    @Override // android.opengl.GLSurfaceView
    public void setRenderer(GLSurfaceView.Renderer renderer) {
        checkRenderThreadState();
        if (this.mEGLConfigChooser == null) {
            this.mEGLConfigChooser = new SimpleEGLConfigChooser(true);
        }
        if (this.mEGLContextFactory == null) {
            this.mEGLContextFactory = new DefaultContextFactory();
        }
        if (this.mEGLWindowSurfaceFactory == null) {
            this.mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        this.mGLThread = new GLThread(renderer);
        this.mGLThread.start();
    }

    public void setEGLContextFactory(EGLContextFactory factory) {
        checkRenderThreadState();
        this.mEGLContextFactory = factory;
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory factory) {
        checkRenderThreadState();
        this.mEGLWindowSurfaceFactory = factory;
    }

    @Override // android.opengl.GLSurfaceView
    public void setEGLConfigChooser(GLSurfaceView.EGLConfigChooser configChooser) {
        checkRenderThreadState();
        this.mEGLConfigChooser = configChooser;
    }

    @Override // android.opengl.GLSurfaceView
    public void setEGLConfigChooser(boolean needDepth) {
        setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
    }

    @Override // android.opengl.GLSurfaceView
    public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
        setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize));
    }

    @Override // android.opengl.GLSurfaceView
    public void setRenderMode(int renderMode) {
        this.mGLThread.setRenderMode(renderMode);
    }

    @Override // android.opengl.GLSurfaceView
    public int getRenderMode() {
        return this.mGLThread.getRenderMode();
    }

    @Override // android.opengl.GLSurfaceView
    public void requestRender() {
        this.mGLThread.requestRender();
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        this.mGLThread.surfaceCreated();
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mGLThread.surfaceDestroyed();
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        this.mGLThread.onWindowResize(w, h);
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        this.mGLThread.onPause();
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        this.mGLThread.onResume();
    }

    @Override // android.opengl.GLSurfaceView
    public void queueEvent(Runnable r) {
        this.mGLThread.queueEvent(r);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mGLThread.requestExitAndWait();
    }

    static class DefaultContextFactory implements EGLContextFactory {
        DefaultContextFactory() {
        }

        @Override // com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceView.EGLContextFactory
        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, null);
        }

        @Override // com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceView.EGLContextFactory
        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            egl.eglDestroyContext(display, context);
        }
    }

    static class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
        DefaultWindowSurfaceFactory() {
        }

        @Override // com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceView.EGLWindowSurfaceFactory
        public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
            return egl.eglCreateWindowSurface(display, config, nativeWindow, null);
        }

        @Override // com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceView.EGLWindowSurfaceFactory
        public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
            egl.eglDestroySurface(display, surface);
        }
    }

    private static abstract class BaseConfigChooser implements GLSurfaceView.EGLConfigChooser {
        protected int[] mConfigSpec;

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] configSpec) {
            this.mConfigSpec = configSpec;
        }

        @Override // android.opengl.GLSurfaceView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            egl.eglChooseConfig(display, this.mConfigSpec, null, 0, num_config);
            int numConfigs = num_config[0];
            if (numConfigs <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] configs = new EGLConfig[numConfigs];
            egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config);
            EGLConfig config = chooseConfig(egl, display, configs);
            if (config == null) {
                throw new IllegalArgumentException("No config chosen");
            }
            return config;
        }
    }

    private static class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue;

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344});
            this.mValue = new int[1];
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        @Override // com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceView.BaseConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            EGLConfig closestConfig = null;
            int closestDistance = 1000;
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, 12325, 0);
                int s = findConfigAttrib(egl, display, config, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int r = findConfigAttrib(egl, display, config, 12324, 0);
                    int g = findConfigAttrib(egl, display, config, 12323, 0);
                    int b = findConfigAttrib(egl, display, config, 12322, 0);
                    int a = findConfigAttrib(egl, display, config, 12321, 0);
                    int distance = Math.abs(r - this.mRedSize) + Math.abs(g - this.mGreenSize) + Math.abs(b - this.mBlueSize) + Math.abs(a - this.mAlphaSize);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestConfig = config;
                    }
                }
            }
            return closestConfig;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                int defaultValue2 = this.mValue[0];
                return defaultValue2;
            }
            return defaultValue;
        }
    }

    private static class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean withDepthBuffer) {
            super(4, 4, 4, 0, withDepthBuffer ? 16 : 0, 0);
            this.mRedSize = 5;
            this.mGreenSize = 6;
            this.mBlueSize = 5;
        }
    }

    private class EglHelper {
        EGL10 mEgl;
        EGLConfig mEglConfig;
        EGLContext mEglContext;
        EGLDisplay mEglDisplay;
        EGLSurface mEglSurface;

        public EglHelper() {
        }

        public void start() {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            int[] version = new int[2];
            this.mEgl.eglInitialize(this.mEglDisplay, version);
            this.mEglConfig = GLBaseSurfaceView.this.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
            this.mEglContext = GLBaseSurfaceView.this.mEGLContextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
            if (this.mEglContext == null || this.mEglContext == EGL10.EGL_NO_CONTEXT) {
                throw new RuntimeException("createContext failed");
            }
            this.mEglSurface = null;
        }

        public GL createSurface(SurfaceHolder holder) {
            if (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                GLBaseSurfaceView.this.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
            }
            this.mEglSurface = GLBaseSurfaceView.this.mEGLWindowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, holder);
            if (this.mEglSurface == null || this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                throwEglException("createWindowSurface");
            }
            if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                throwEglException("eglMakeCurrent");
            }
            GL gl = this.mEglContext.getGL();
            if (GLBaseSurfaceView.this.mGLWrapper != null) {
                gl = GLBaseSurfaceView.this.mGLWrapper.wrap(gl);
            }
            if ((GLBaseSurfaceView.this.mDebugFlags & 3) != 0) {
                int configFlags = 0;
                Writer log = null;
                if ((GLBaseSurfaceView.this.mDebugFlags & 1) != 0) {
                    configFlags = 0 | 1;
                }
                if ((GLBaseSurfaceView.this.mDebugFlags & 2) != 0) {
                    log = new LogWriter();
                }
                return GLDebugHelper.wrap(gl, configFlags, log);
            }
            return gl;
        }

        public boolean swap() {
            this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
            return this.mEgl.eglGetError() != 12302;
        }

        public void destroySurface() {
            if (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                GLBaseSurfaceView.this.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
                this.mEglSurface = null;
            }
        }

        public void finish() {
            if (this.mEglContext != null) {
                GLBaseSurfaceView.this.mEGLContextFactory.destroyContext(this.mEgl, this.mEglDisplay, this.mEglContext);
                this.mEglContext = null;
            }
            if (this.mEglDisplay != null) {
                this.mEgl.eglTerminate(this.mEglDisplay);
                this.mEglDisplay = null;
            }
        }

        private void throwEglException(String function) {
            throw new RuntimeException(function + " failed: " + this.mEgl.eglGetError());
        }
    }

    class GLThread extends Thread {
        private EglHelper mEglHelper;
        boolean mExited;
        private boolean mHasSurface;
        private boolean mHaveEgl;
        private boolean mPaused;
        private boolean mRenderComplete;
        private GLSurfaceView.Renderer mRenderer;
        private boolean mShouldExit;
        private boolean mWaitingForSurface;
        private ArrayList<Runnable> mEventQueue = new ArrayList<>();
        private int mWidth = 0;
        private int mHeight = 0;
        private boolean mRequestRender = true;
        private int mRenderMode = 1;

        GLThread(GLSurfaceView.Renderer renderer) {
            this.mRenderer = renderer;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                guardedRun();
            } catch (InterruptedException e) {
            } finally {
                GLBaseSurfaceView.sGLThreadManager.threadExiting(this);
            }
        }

        private void stopEglLocked() {
            if (this.mHaveEgl) {
                this.mHaveEgl = false;
                this.mEglHelper.destroySurface();
                this.mEglHelper.finish();
                GLBaseSurfaceView.sGLThreadManager.releaseEglSurfaceLocked(this);
            }
        }

        private void guardedRun() throws InterruptedException {
            this.mEglHelper = GLBaseSurfaceView.this.new EglHelper();
            GL10 gl = null;
            boolean createEglSurface = false;
            boolean sizeChanged = false;
            boolean wantRenderNotification = false;
            boolean doRenderNotification = false;
            int w = 0;
            int h = 0;
            Runnable event = null;
            while (true) {
                try {
                    synchronized (GLBaseSurfaceView.sGLThreadManager) {
                        while (true) {
                            if (!this.mShouldExit) {
                                if (!this.mEventQueue.isEmpty()) {
                                    event = this.mEventQueue.remove(0);
                                    break;
                                }
                                if (this.mHaveEgl && this.mPaused) {
                                    stopEglLocked();
                                }
                                if (!this.mHasSurface && !this.mWaitingForSurface) {
                                    if (this.mHaveEgl) {
                                        stopEglLocked();
                                    }
                                    this.mWaitingForSurface = true;
                                    GLBaseSurfaceView.sGLThreadManager.notifyAll();
                                }
                                if (this.mHasSurface && this.mWaitingForSurface) {
                                    this.mWaitingForSurface = false;
                                    GLBaseSurfaceView.sGLThreadManager.notifyAll();
                                }
                                if (doRenderNotification) {
                                    wantRenderNotification = false;
                                    doRenderNotification = false;
                                    this.mRenderComplete = true;
                                    GLBaseSurfaceView.sGLThreadManager.notifyAll();
                                }
                                if (!this.mPaused && this.mHasSurface && this.mWidth > 0 && this.mHeight > 0 && (this.mRequestRender || this.mRenderMode == 1)) {
                                    if (!this.mHaveEgl && GLBaseSurfaceView.sGLThreadManager.tryAcquireEglSurfaceLocked(this)) {
                                        this.mHaveEgl = true;
                                        this.mEglHelper.start();
                                        createEglSurface = true;
                                        sizeChanged = true;
                                        GLBaseSurfaceView.sGLThreadManager.notifyAll();
                                    }
                                    if (this.mHaveEgl) {
                                        if (GLBaseSurfaceView.this.mSizeChanged) {
                                            sizeChanged = true;
                                            w = this.mWidth;
                                            h = this.mHeight;
                                            wantRenderNotification = true;
                                            GLBaseSurfaceView.this.mSizeChanged = false;
                                        } else {
                                            this.mRequestRender = false;
                                        }
                                        GLBaseSurfaceView.sGLThreadManager.notifyAll();
                                        break;
                                    }
                                }
                                GLBaseSurfaceView.sGLThreadManager.wait();
                            } else {
                                synchronized (GLBaseSurfaceView.sGLThreadManager) {
                                    stopEglLocked();
                                }
                                return;
                            }
                        }
                    }
                    if (event != null) {
                        event.run();
                        event = null;
                    } else {
                        if (createEglSurface) {
                            gl = (GL10) this.mEglHelper.createSurface(GLBaseSurfaceView.this.getHolder());
                            this.mRenderer.onSurfaceCreated(gl, this.mEglHelper.mEglConfig);
                            createEglSurface = false;
                        }
                        if (sizeChanged) {
                            this.mRenderer.onSurfaceChanged(gl, w, h);
                            sizeChanged = false;
                        }
                        this.mRenderer.onDrawFrame(gl);
                        if (!this.mEglHelper.swap()) {
                        }
                        if (wantRenderNotification) {
                            doRenderNotification = true;
                        }
                    }
                } catch (Throwable th) {
                    synchronized (GLBaseSurfaceView.sGLThreadManager) {
                        stopEglLocked();
                        throw th;
                    }
                }
            }
        }

        public void setRenderMode(int renderMode) {
            if (renderMode < 0 || renderMode > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mRenderMode = renderMode;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public int getRenderMode() {
            int i;
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                i = this.mRenderMode;
            }
            return i;
        }

        public void requestRender() {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mRequestRender = true;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void surfaceCreated() {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mHasSurface = true;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void surfaceDestroyed() {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mHasSurface = false;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mWaitingForSurface && !this.mExited) {
                    try {
                        GLBaseSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onPause() {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mPaused = true;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void onResume() {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mPaused = false;
                this.mRequestRender = true;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void onWindowResize(int w, int h) {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mWidth = w;
                this.mHeight = h;
                GLBaseSurfaceView.this.mSizeChanged = true;
                this.mRequestRender = true;
                this.mRenderComplete = false;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited && !this.mPaused && !this.mRenderComplete) {
                    try {
                        GLBaseSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestExitAndWait() {
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mShouldExit = true;
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited) {
                    try {
                        GLBaseSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void queueEvent(Runnable r) {
            if (r == null) {
                throw new IllegalArgumentException("r must not be null");
            }
            synchronized (GLBaseSurfaceView.sGLThreadManager) {
                this.mEventQueue.add(r);
                GLBaseSurfaceView.sGLThreadManager.notifyAll();
            }
        }
    }

    static class LogWriter extends Writer {
        private StringBuilder mBuilder = new StringBuilder();

        LogWriter() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            flushBuilder();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            flushBuilder();
        }

        @Override // java.io.Writer
        public void write(char[] buf, int offset, int count) {
            for (int i = 0; i < count; i++) {
                char c = buf[offset + i];
                if (c == '\n') {
                    flushBuilder();
                } else {
                    this.mBuilder.append(c);
                }
            }
        }

        private void flushBuilder() {
            if (this.mBuilder.length() > 0) {
                Log.v("GLSurfaceView", this.mBuilder.toString());
                this.mBuilder.delete(0, this.mBuilder.length());
            }
        }
    }

    private void checkRenderThreadState() {
        if (this.mGLThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }

    static class GLThreadManager {
        private GLThread mEglOwner;

        GLThreadManager() {
        }

        public synchronized void threadExiting(GLThread thread) {
            thread.mExited = true;
            if (this.mEglOwner == thread) {
                this.mEglOwner = null;
            }
            notifyAll();
        }

        public boolean tryAcquireEglSurfaceLocked(GLThread thread) {
            if (this.mEglOwner != thread && this.mEglOwner != null) {
                return false;
            }
            this.mEglOwner = thread;
            notifyAll();
            return true;
        }

        public void releaseEglSurfaceLocked(GLThread thread) {
            if (this.mEglOwner == thread) {
                this.mEglOwner = null;
            }
            notifyAll();
        }
    }
}
