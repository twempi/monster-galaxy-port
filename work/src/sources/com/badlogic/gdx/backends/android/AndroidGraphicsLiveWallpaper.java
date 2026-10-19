package com.badlogic.gdx.backends.android;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.android.surfaceview.DefaultGLSurfaceViewLW;
import com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceViewLW;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceView20LW;
import com.badlogic.gdx.backends.android.surfaceview.GdxEglConfigChooser;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.GLU;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.WindowedMean;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* JADX INFO: loaded from: classes.dex */
public final class AndroidGraphicsLiveWallpaper implements Graphics, GLSurfaceView.Renderer {
    AndroidLiveWallpaper app;
    protected EGLContext eglContext;
    protected String extensions;
    protected int fps;
    protected GLCommon gl;
    protected GL10 gl10;
    protected GL11 gl11;
    protected GL20 gl20;
    protected GLU glu;
    int height;
    final GLBaseSurfaceViewLW view;
    int width;
    protected long lastFrameTime = System.nanoTime();
    protected float deltaTime = 0.0f;
    protected long frameStart = System.nanoTime();
    protected int frames = 0;
    protected WindowedMean mean = new WindowedMean(5);
    volatile boolean created = false;
    volatile boolean running = false;
    volatile boolean pause = false;
    volatile boolean resume = false;
    volatile boolean destroy = false;
    protected float ppiX = 0.0f;
    protected float ppiY = 0.0f;
    protected float ppcX = 0.0f;
    protected float ppcY = 0.0f;
    protected float density = 1.0f;
    private Graphics.BufferFormat bufferFormat = new Graphics.BufferFormat(5, 6, 5, 0, 16, 0, 0, false);
    protected boolean isContinuous = true;
    int[] value = new int[1];
    Object synch = new Object();

    protected boolean checkGL20() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(display, version);
        int[] configAttribs = {12324, 4, 12323, 4, 12322, 4, 12352, 4, 12344};
        EGLConfig[] configs = new EGLConfig[10];
        int[] num_config = new int[1];
        egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);
        egl.eglTerminate(display);
        return num_config[0] > 0;
    }

    @Override // com.badlogic.gdx.Graphics
    public GL10 getGL10() {
        return this.gl10;
    }

    @Override // com.badlogic.gdx.Graphics
    public GL11 getGL11() {
        return this.gl11;
    }

    @Override // com.badlogic.gdx.Graphics
    public GL20 getGL20() {
        return this.gl20;
    }

    @Override // com.badlogic.gdx.Graphics
    public int getHeight() {
        return this.height;
    }

    @Override // com.badlogic.gdx.Graphics
    public int getWidth() {
        return this.width;
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean isGL11Available() {
        return this.gl11 != null;
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean isGL20Available() {
        return this.gl20 != null;
    }

    protected void logConfig(EGLConfig config) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int r = getAttrib(egl, display, config, 12324, 0);
        int g = getAttrib(egl, display, config, 12323, 0);
        int b = getAttrib(egl, display, config, 12322, 0);
        int a = getAttrib(egl, display, config, 12321, 0);
        int d = getAttrib(egl, display, config, 12325, 0);
        int s = getAttrib(egl, display, config, 12326, 0);
        int samples = Math.max(getAttrib(egl, display, config, 12337, 0), getAttrib(egl, display, config, GdxEglConfigChooser.EGL_COVERAGE_SAMPLES_NV, 0));
        boolean coverageSample = getAttrib(egl, display, config, GdxEglConfigChooser.EGL_COVERAGE_SAMPLES_NV, 0) != 0;
        Gdx.app.log("AndroidGraphics", "framebuffer: (" + r + ", " + g + ", " + b + ", " + a + ")");
        Gdx.app.log("AndroidGraphics", "depthbuffer: (" + d + ")");
        Gdx.app.log("AndroidGraphics", "stencilbuffer: (" + s + ")");
        Gdx.app.log("AndroidGraphics", "samples: (" + samples + ")");
        Gdx.app.log("AndroidGraphics", "coverage sampling: (" + coverageSample + ")");
        this.bufferFormat = new Graphics.BufferFormat(r, g, b, a, d, s, samples, coverageSample);
    }

    private int getAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attrib, int defValue) {
        if (egl.eglGetConfigAttrib(display, config, attrib, this.value)) {
            int defValue2 = this.value[0];
            return defValue2;
        }
        return defValue;
    }

    void resume() {
        synchronized (this.synch) {
            this.running = true;
            this.resume = true;
        }
    }

    void pause() {
        synchronized (this.synch) {
            try {
                if (this.running) {
                    this.running = false;
                    this.pause = true;
                    while (this.pause) {
                        try {
                            this.synch.wait();
                        } catch (InterruptedException e) {
                            Gdx.app.log("AndroidGraphics", "waiting for pause synchronization failed!");
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void destroy() {
        synchronized (this.synch) {
            this.running = false;
            this.destroy = true;
            while (this.destroy) {
                try {
                    this.synch.wait();
                } catch (InterruptedException e) {
                    Gdx.app.log("AndroidGraphics", "waiting for destroy synchronization failed!");
                }
            }
        }
    }

    @Override // com.badlogic.gdx.Graphics
    public float getDeltaTime() {
        return this.mean.getMean() == 0.0f ? this.deltaTime : this.mean.getMean();
    }

    @Override // com.badlogic.gdx.Graphics
    public float getRawDeltaTime() {
        return this.deltaTime;
    }

    @Override // com.badlogic.gdx.Graphics
    public Graphics.GraphicsType getType() {
        return Graphics.GraphicsType.AndroidGL;
    }

    @Override // com.badlogic.gdx.Graphics
    public int getFramesPerSecond() {
        return this.fps;
    }

    @Override // com.badlogic.gdx.Graphics
    public GLCommon getGLCommon() {
        return this.gl;
    }

    @Override // com.badlogic.gdx.Graphics
    public float getPpiX() {
        return this.ppiX;
    }

    @Override // com.badlogic.gdx.Graphics
    public float getPpiY() {
        return this.ppiY;
    }

    @Override // com.badlogic.gdx.Graphics
    public float getPpcX() {
        return this.ppcX;
    }

    @Override // com.badlogic.gdx.Graphics
    public float getPpcY() {
        return this.ppcY;
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean supportsDisplayModeChange() {
        return false;
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean setDisplayMode(Graphics.DisplayMode displayMode) {
        return false;
    }

    @Override // com.badlogic.gdx.Graphics
    public void setTitle(String title) {
    }

    class AndroidDisplayMode extends Graphics.DisplayMode {
        protected AndroidDisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
            super(width, height, refreshRate, bitsPerPixel);
        }
    }

    @Override // com.badlogic.gdx.Graphics
    public Graphics.BufferFormat getBufferFormat() {
        return this.bufferFormat;
    }

    @Override // com.badlogic.gdx.Graphics
    public void setVSync(boolean vsync) {
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean supportsExtension(String extension) {
        if (this.extensions == null) {
            this.extensions = Gdx.gl.glGetString(7939);
        }
        return this.extensions.contains(extension);
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean isContinuousRendering() {
        return this.isContinuous;
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean isFullscreen() {
        return true;
    }

    public AndroidGraphicsLiveWallpaper(AndroidLiveWallpaper app, boolean useGL2IfAvailable, ResolutionStrategy resolutionStrategy) {
        this.view = createGLSurfaceView(app, useGL2IfAvailable, resolutionStrategy);
        this.app = app;
    }

    private GLBaseSurfaceViewLW createGLSurfaceView(AndroidLiveWallpaper app, boolean useGL2, ResolutionStrategy resolutionStrategy) {
        GLBaseSurfaceViewLW view;
        GLSurfaceView.EGLConfigChooser configChooser = getEglConfigChooser();
        if (useGL2 && checkGL20()) {
            view = new GLSurfaceView20LW(app.getEngine(), resolutionStrategy);
            if (configChooser != null) {
                view.setEGLConfigChooser(configChooser);
            }
            view.setRenderer(this);
        } else {
            view = new DefaultGLSurfaceViewLW(app.getEngine(), resolutionStrategy);
            if (configChooser != null) {
                view.setEGLConfigChooser(configChooser);
            }
            view.setRenderer(this);
        }
        return view;
    }

    private GLSurfaceView.EGLConfigChooser getEglConfigChooser() {
        if (Build.DEVICE.equalsIgnoreCase("GT-I7500")) {
            return new GLSurfaceView.EGLConfigChooser() { // from class: com.badlogic.gdx.backends.android.AndroidGraphicsLiveWallpaper.1
                @Override // android.opengl.GLSurfaceView.EGLConfigChooser
                public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                    int[] attributes = {12325, 16, 12344};
                    EGLConfig[] configs = new EGLConfig[1];
                    int[] result = new int[1];
                    egl.eglChooseConfig(display, attributes, configs, 1, result);
                    return configs[0];
                }
            };
        }
        return null;
    }

    private void updatePpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((WindowManager) this.app.getService().getSystemService("window")).getDefaultDisplay();
        display.getMetrics(metrics);
        this.ppiX = metrics.xdpi;
        this.ppiY = metrics.ydpi;
        this.ppcX = metrics.xdpi / 2.54f;
        this.ppcY = metrics.ydpi / 2.54f;
        this.density = metrics.density;
    }

    private void setupGL(javax.microedition.khronos.opengles.GL10 gl) {
        String renderer;
        if (this.gl10 == null && this.gl20 == null) {
            boolean isGL20 = checkGL20();
            Gdx.app.log("AndroidGraphics", "GL20: " + isGL20);
            if (this.view instanceof GLSurfaceView20LW) {
                this.gl20 = new AndroidGL20();
                this.gl = this.gl20;
            } else {
                this.gl10 = new AndroidGL10(gl);
                this.gl = this.gl10;
                if ((gl instanceof javax.microedition.khronos.opengles.GL11) && (renderer = gl.glGetString(7937)) != null && !renderer.toLowerCase().contains("pixelflinger") && !Build.MODEL.equals("MB200") && !Build.MODEL.equals("MB220") && !Build.MODEL.contains("Behold")) {
                    this.gl11 = new AndroidGL11((javax.microedition.khronos.opengles.GL11) gl);
                    this.gl10 = this.gl11;
                }
            }
            this.glu = new AndroidGLU();
            Gdx.gl = this.gl;
            Gdx.gl10 = this.gl10;
            Gdx.gl11 = this.gl11;
            Gdx.gl20 = this.gl20;
            Gdx.glu = this.glu;
            Gdx.app.log("AndroidGraphics", "OGL renderer: " + gl.glGetString(7937));
            Gdx.app.log("AndroidGraphics", "OGL vendor: " + gl.glGetString(7936));
            Gdx.app.log("AndroidGraphics", "OGL version: " + gl.glGetString(7938));
            Gdx.app.log("AndroidGraphics", "OGL extensions: " + gl.glGetString(7939));
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        updatePpi();
        gl.glViewport(0, 0, this.width, this.height);
        this.app.getListener().resize(width, height);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 gl, EGLConfig config) {
        setupGL(gl);
        logConfig(config);
        updatePpi();
        Mesh.invalidateAllMeshes(this.app);
        Texture.invalidateAllTextures(this.app);
        ShaderProgram.invalidateAllShaderPrograms(this.app);
        FrameBuffer.invalidateAllFrameBuffers(this.app);
        Display display = ((WindowManager) this.app.getService().getSystemService("window")).getDefaultDisplay();
        this.width = display.getWidth();
        this.height = display.getHeight();
        this.mean = new WindowedMean(5);
        this.lastFrameTime = System.nanoTime();
        gl.glViewport(0, 0, this.width, this.height);
        if (!this.created) {
            this.app.getListener().create();
            this.created = true;
            synchronized (this) {
                this.running = true;
            }
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl) {
        boolean lrunning;
        boolean lpause;
        boolean ldestroy;
        boolean lresume;
        long time = System.nanoTime();
        this.deltaTime = (time - this.lastFrameTime) / 1.0E9f;
        this.lastFrameTime = time;
        this.mean.addValue(this.deltaTime);
        synchronized (this.synch) {
            lrunning = this.running;
            lpause = this.pause;
            ldestroy = this.destroy;
            lresume = this.resume;
            if (this.resume) {
                this.resume = false;
            }
            if (this.pause) {
                this.pause = false;
                this.synch.notifyAll();
            }
            if (this.destroy) {
                this.destroy = false;
                this.synch.notifyAll();
            }
        }
        if (lresume) {
            this.app.getListener().resume();
            Gdx.app.log("AndroidGraphics", "resumed");
        }
        if (lrunning && (Gdx.graphics.getGL10() != null || Gdx.graphics.getGL11() != null || Gdx.graphics.getGL20() != null)) {
            synchronized (this.app.runnables) {
                for (int i = 0; i < this.app.runnables.size; i++) {
                    this.app.runnables.get(i).run();
                }
                this.app.runnables.clear();
            }
            this.app.input.processEvents();
            this.app.getListener().render();
        }
        if (lpause) {
            this.app.getListener().pause();
            ((AndroidAudio) this.app.getAudio()).pause();
            Gdx.app.log("AndroidGraphics", "paused");
        }
        if (ldestroy) {
            this.app.getListener().dispose();
            ((AndroidAudio) this.app.getAudio()).dispose();
            Gdx.app.log("AndroidGraphics", "destroyed");
        }
        if (time - this.frameStart > 1000000000) {
            this.fps = this.frames;
            this.frames = 0;
            this.frameStart = time;
        }
        this.frames++;
    }

    protected void clearManagedCaches() {
        Mesh.clearAllMeshes(this.app);
        Texture.clearAllTextures(this.app);
        ShaderProgram.clearAllShaderPrograms(this.app);
        FrameBuffer.clearAllFrameBuffers(this.app);
        Gdx.app.log("AndroidGraphics", Mesh.getManagedStatus());
        Gdx.app.log("AndroidGraphics", Texture.getManagedStatus());
        Gdx.app.log("AndroidGraphics", ShaderProgram.getManagedStatus());
        Gdx.app.log("AndroidGraphics", FrameBuffer.getManagedStatus());
    }

    public GLBaseSurfaceViewLW getView() {
        return this.view;
    }

    @Override // com.badlogic.gdx.Graphics
    public Graphics.DisplayMode[] getDisplayModes() {
        return new Graphics.DisplayMode[0];
    }

    @Override // com.badlogic.gdx.Graphics
    public GLU getGLU() {
        return this.glu;
    }

    @Override // com.badlogic.gdx.Graphics
    public float getDensity() {
        return this.density;
    }

    @Override // com.badlogic.gdx.Graphics
    public Graphics.DisplayMode getDesktopDisplayMode() {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((WindowManager) this.app.getService().getSystemService("window")).getDefaultDisplay();
        display.getMetrics(metrics);
        return new AndroidDisplayMode(metrics.widthPixels, metrics.heightPixels, 0, 0);
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean setDisplayMode(int width, int height, boolean fullscreen) {
        return false;
    }

    @Override // com.badlogic.gdx.Graphics
    public void setContinuousRendering(boolean isContinuous) {
        if (this.view != null) {
            this.isContinuous = isContinuous;
            int renderMode = isContinuous ? 1 : 0;
            this.view.setRenderMode(renderMode);
        }
    }

    @Override // com.badlogic.gdx.Graphics
    public void requestRendering() {
        if (this.view != null) {
            this.view.requestRender();
        }
    }
}
