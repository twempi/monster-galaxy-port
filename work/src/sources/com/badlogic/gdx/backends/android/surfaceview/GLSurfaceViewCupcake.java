package com.badlogic.gdx.backends.android.surfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.Writer;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class GLSurfaceViewCupcake extends SurfaceView implements SurfaceHolder.Callback {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    static final Object sEglLock = new Object();
    private int mDebugFlags;
    GLSurfaceView.EGLConfigChooser mEGLConfigChooser;
    private GLThread mGLThread;
    GLWrapper mGLWrapper;
    private boolean mHasSurface;
    private int mRenderMode;
    private GLSurfaceView.Renderer mRenderer;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    final ResolutionStrategy resolutionStrategy;

    public interface GLWrapper {
        GL wrap(GL gl);
    }

    public GLSurfaceViewCupcake(Context context, ResolutionStrategy resolutionStrategy) {
        super(context);
        this.resolutionStrategy = resolutionStrategy;
        init();
    }

    public GLSurfaceViewCupcake(Context context, AttributeSet attrs, ResolutionStrategy resolutionStrategy) {
        super(context, attrs);
        this.resolutionStrategy = resolutionStrategy;
        init();
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ResolutionStrategy.MeasuredDimension measures = this.resolutionStrategy.calcMeasures(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measures.width, measures.height);
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(2);
        this.mRenderMode = 1;
    }

    public void setGLWrapper(GLWrapper glWrapper) {
        this.mGLWrapper = glWrapper;
    }

    public void setDebugFlags(int debugFlags) {
        this.mDebugFlags = debugFlags;
    }

    public int getDebugFlags() {
        return this.mDebugFlags;
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        if (this.mRenderer != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
        this.mRenderer = renderer;
    }

    public void setEGLConfigChooser(GLSurfaceView.EGLConfigChooser configChooser) {
        if (this.mRenderer != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
        this.mEGLConfigChooser = configChooser;
    }

    public void setEGLConfigChooser(boolean needDepth) {
        setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
    }

    public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
        setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize));
    }

    public void setRenderMode(int renderMode) {
        this.mRenderMode = renderMode;
        if (this.mGLThread != null) {
            this.mGLThread.setRenderMode(renderMode);
        }
    }

    public int getRenderMode() {
        return this.mRenderMode;
    }

    public void requestRender() {
        GLThread thread = this.mGLThread;
        if (thread != null) {
            thread.requestRender();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        if (this.mGLThread != null) {
            this.mGLThread.surfaceCreated();
        }
        this.mHasSurface = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (this.mGLThread != null) {
            this.mGLThread.surfaceDestroyed();
        }
        this.mHasSurface = false;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (this.mGLThread != null) {
            this.mGLThread.onWindowResize(w, h);
        }
        this.mSurfaceWidth = w;
        this.mSurfaceHeight = h;
    }

    public void onPause() {
        this.mGLThread.onPause();
        this.mGLThread.requestExitAndWait();
        this.mGLThread = null;
    }

    public void onResume() {
        if (this.mEGLConfigChooser == null) {
            this.mEGLConfigChooser = new SimpleEGLConfigChooser(true);
        }
        this.mGLThread = new GLThread(this.mRenderer);
        this.mGLThread.start();
        this.mGLThread.setRenderMode(this.mRenderMode);
        if (this.mHasSurface) {
            this.mGLThread.surfaceCreated();
        }
        if (this.mSurfaceWidth > 0 && this.mSurfaceHeight > 0) {
            this.mGLThread.onWindowResize(this.mSurfaceWidth, this.mSurfaceHeight);
        }
        this.mGLThread.onResume();
    }

    public void queueEvent(Runnable r) {
        if (this.mGLThread != null) {
            this.mGLThread.queueEvent(r);
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

        @Override // com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewCupcake.BaseConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            EGLConfig closestConfig = null;
            int closestDistance = 1000;
            for (EGLConfig config : configs) {
                int r = findConfigAttrib(egl, display, config, 12324, 0);
                int g = findConfigAttrib(egl, display, config, 12323, 0);
                int b = findConfigAttrib(egl, display, config, 12322, 0);
                int a = findConfigAttrib(egl, display, config, 12321, 0);
                int d = findConfigAttrib(egl, display, config, 12325, 0);
                int s = findConfigAttrib(egl, display, config, 12326, 0);
                int distance = Math.abs(r - this.mRedSize) + Math.abs(g - this.mGreenSize) + Math.abs(b - this.mBlueSize) + Math.abs(a - this.mAlphaSize) + Math.abs(d - this.mDepthSize) + Math.abs(s - this.mStencilSize);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestConfig = config;
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
            this.mEglConfig = GLSurfaceViewCupcake.this.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL10.EGL_NO_CONTEXT, null);
            this.mEglSurface = null;
        }

        public GL createSurface(SurfaceHolder holder) {
            if (this.mEglSurface != null) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
            }
            this.mEglSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, holder, null);
            this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext);
            GL gl = this.mEglContext.getGL();
            if (GLSurfaceViewCupcake.this.mGLWrapper != null) {
                return GLSurfaceViewCupcake.this.mGLWrapper.wrap(gl);
            }
            return gl;
        }

        public boolean swap() {
            this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
            return this.mEgl.eglGetError() != 12302;
        }

        public void finish() {
            if (this.mEglSurface != null) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
                this.mEglSurface = null;
            }
            if (this.mEglContext != null) {
                this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
                this.mEglContext = null;
            }
            if (this.mEglDisplay != null) {
                this.mEgl.eglTerminate(this.mEglDisplay);
                this.mEglDisplay = null;
            }
        }
    }

    class GLThread extends Thread {
        private EglHelper mEglHelper;
        private boolean mHasSurface;
        private boolean mPaused;
        private GLSurfaceView.Renderer mRenderer;
        private ArrayList<Runnable> mEventQueue = new ArrayList<>();
        private boolean mDone = false;
        private int mWidth = 0;
        private int mHeight = 0;
        private boolean mRequestRender = true;
        private int mRenderMode = 1;
        private boolean mSizeChanged = true;

        GLThread(GLSurfaceView.Renderer renderer) {
            this.mRenderer = renderer;
            setName("GLThread");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                synchronized (GLSurfaceViewCupcake.sEglLock) {
                    try {
                        guardedRun();
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            } catch (InterruptedException e) {
            } catch (Throwable th2) {
                throw th2;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:26:0x0050, code lost:
        
            if (r3 == false) goto L28;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0052, code lost:
        
            r10.mEglHelper.start();
            r6 = true;
            r0 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x0059, code lost:
        
            if (r0 == false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x005b, code lost:
        
            r1 = (javax.microedition.khronos.opengles.GL10) r10.mEglHelper.createSurface(r10.this$0.getHolder());
            r5 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x006a, code lost:
        
            if (r6 == false) goto L32;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x006c, code lost:
        
            r10.mRenderer.onSurfaceCreated(r1, r10.mEglHelper.mEglConfig);
            r6 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x0076, code lost:
        
            if (r5 == false) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x0078, code lost:
        
            r10.mRenderer.onSurfaceChanged(r1, r7, r2);
            r5 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x007e, code lost:
        
            if (r7 <= 0) goto L44;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x0080, code lost:
        
            if (r2 <= 0) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x0082, code lost:
        
            r10.mRenderer.onDrawFrame(r1);
            r10.mEglHelper.swap();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void guardedRun() throws InterruptedException {
            this.mEglHelper = GLSurfaceViewCupcake.this.new EglHelper();
            this.mEglHelper.start();
            GL10 gl = null;
            boolean tellRendererSurfaceCreated = true;
            boolean tellRendererSurfaceChanged = true;
            while (!this.mDone) {
                boolean needStart = false;
                synchronized (this) {
                    while (true) {
                        Runnable r = getEvent();
                        if (r == null) {
                            break;
                        } else {
                            r.run();
                        }
                    }
                    if (this.mPaused) {
                        this.mEglHelper.finish();
                        needStart = true;
                    }
                    while (needToWait()) {
                        wait();
                    }
                    if (!this.mDone) {
                        boolean changed = this.mSizeChanged;
                        int w = this.mWidth;
                        int h = this.mHeight;
                        this.mSizeChanged = false;
                        this.mRequestRender = false;
                    }
                }
            }
            this.mEglHelper.finish();
        }

        private boolean needToWait() {
            if (this.mDone) {
                return false;
            }
            if (this.mPaused || !this.mHasSurface) {
                return true;
            }
            return this.mWidth <= 0 || this.mHeight <= 0 || !(this.mRequestRender || this.mRenderMode == 1);
        }

        public void setRenderMode(int renderMode) {
            if (renderMode < 0 || renderMode > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (this) {
                this.mRenderMode = renderMode;
                if (renderMode == 1) {
                    notify();
                }
            }
        }

        public int getRenderMode() {
            int i;
            synchronized (this) {
                i = this.mRenderMode;
            }
            return i;
        }

        public void requestRender() {
            synchronized (this) {
                this.mRequestRender = true;
                notify();
            }
        }

        public void surfaceCreated() {
            synchronized (this) {
                this.mHasSurface = true;
                notify();
            }
        }

        public void surfaceDestroyed() {
            synchronized (this) {
                this.mHasSurface = false;
                notify();
            }
        }

        public void onPause() {
            synchronized (this) {
                this.mPaused = true;
            }
        }

        public void onResume() {
            synchronized (this) {
                this.mPaused = false;
                notify();
            }
        }

        public void onWindowResize(int w, int h) {
            synchronized (this) {
                this.mWidth = w;
                this.mHeight = h;
                this.mSizeChanged = true;
                notify();
            }
        }

        public void requestExitAndWait() {
            synchronized (this) {
                this.mDone = true;
                notify();
            }
            try {
                join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void queueEvent(Runnable r) {
            synchronized (this) {
                this.mEventQueue.add(r);
            }
        }

        private Runnable getEvent() {
            synchronized (this) {
                if (this.mEventQueue.size() > 0) {
                    return this.mEventQueue.remove(0);
                }
                return null;
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
}
