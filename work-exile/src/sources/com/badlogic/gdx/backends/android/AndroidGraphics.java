package com.badlogic.gdx.backends.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceView20;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewCupcake;
import com.badlogic.gdx.backends.android.surfaceview.GdxEglConfigChooser;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.GLU;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.WindowedMean;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AndroidGraphics implements Graphics, GLSurfaceView.Renderer {
    AndroidApplication app;
    private final AndroidApplicationConfiguration config;
    String extensions;
    private int fps;
    GLCommon gl;
    GL10 gl10;
    GL11 gl11;
    GL20 gl20;
    GLU glu;
    int height;
    final View view;
    int width;
    private long lastFrameTime = System.nanoTime();
    private float deltaTime = 0.0f;
    private long frameStart = System.nanoTime();
    private int frames = 0;
    private WindowedMean mean = new WindowedMean(5);
    volatile boolean created = false;
    volatile boolean running = false;
    volatile boolean pause = false;
    volatile boolean resume = false;
    volatile boolean destroy = false;
    private float ppiX = 0.0f;
    private float ppiY = 0.0f;
    private float ppcX = 0.0f;
    private float ppcY = 0.0f;
    private float density = 1.0f;
    private Graphics.BufferFormat bufferFormat = new Graphics.BufferFormat(5, 6, 5, 0, 16, 0, 0, false);
    int[] value = new int[1];
    Object synch = new Object();

    public AndroidGraphics(AndroidApplication activity, AndroidApplicationConfiguration config, ResolutionStrategy resolutionStrategy) {
        this.config = config;
        this.view = createGLSurfaceView(activity, config.useGL20, resolutionStrategy);
        this.view.setFocusable(true);
        this.view.setFocusableInTouchMode(true);
        this.app = activity;
    }

    private View createGLSurfaceView(Activity activity, boolean useGL2, ResolutionStrategy resolutionStrategy) {
        getEglConfigChooser();
        this.config.useGL20 = false;
        GLSurfaceView.EGLConfigChooser configChooser = getEglConfigChooser();
        GLSurfaceViewCupcake view = new GLSurfaceViewCupcake(activity, resolutionStrategy);
        if (configChooser != null) {
            view.setEGLConfigChooser(configChooser);
        } else {
            view.setEGLConfigChooser(this.config.r, this.config.g, this.config.b, this.config.a, this.config.depth, this.config.stencil);
        }
        view.setRenderer(this);
        return view;
    }

    private GLSurfaceView.EGLConfigChooser getEglConfigChooser() {
        return new GdxEglConfigChooser(this.config.r, this.config.g, this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.numSamples, this.config.useGL20);
    }

    private void updatePpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.app.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.ppiX = metrics.xdpi;
        this.ppiY = metrics.ydpi;
        this.ppcX = metrics.xdpi / 2.54f;
        this.ppcY = metrics.ydpi / 2.54f;
        this.density = metrics.density;
    }

    private boolean checkGL20() {
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

    private static boolean isPowerOfTwo(int value) {
        return value != 0 && ((value + (-1)) & value) == 0;
    }

    private void setupGL(javax.microedition.khronos.opengles.GL10 gl) {
        String renderer;
        if (this.gl10 == null && this.gl20 == null) {
            if (this.view instanceof GLSurfaceView20) {
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
        if (!this.created) {
            this.app.listener.create();
            this.created = true;
            synchronized (this) {
                this.running = true;
            }
        }
        this.app.listener.resize(width, height);
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
        Gdx.app.log("AndroidGraphics", Mesh.getManagedStatus());
        Gdx.app.log("AndroidGraphics", Texture.getManagedStatus());
        Gdx.app.log("AndroidGraphics", ShaderProgram.getManagedStatus());
        Gdx.app.log("AndroidGraphics", FrameBuffer.getManagedStatus());
        Display display = this.app.getWindowManager().getDefaultDisplay();
        this.width = display.getWidth();
        this.height = display.getHeight();
        this.mean = new WindowedMean(5);
        this.lastFrameTime = System.nanoTime();
        gl.glViewport(0, 0, this.width, this.height);
    }

    private void logConfig(EGLConfig config) {
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
            if (this.running) {
                this.running = false;
                this.pause = true;
                while (this.pause) {
                    try {
                        this.synch.wait();
                    } catch (InterruptedException e) {
                    }
                }
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
                }
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
            this.app.listener.resume();
            Gdx.app.log("AndroidGraphics", "resumed");
        }
        if (lrunning) {
            synchronized (this.app.runnables) {
                for (int i = 0; i < this.app.runnables.size(); i++) {
                    this.app.runnables.get(i).run();
                }
                this.app.runnables.clear();
            }
            this.app.input.processEvents();
            this.app.listener.render();
        }
        if (lpause) {
            this.app.listener.pause();
            this.app.audio.pause();
            Gdx.app.log("AndroidGraphics", "paused");
        }
        if (ldestroy) {
            this.app.listener.dispose();
            this.app.audio.dispose();
            this.app.audio = null;
            Gdx.app.log("AndroidGraphics", "destroyed");
        }
        if (time - this.frameStart > 1000000000) {
            this.fps = this.frames;
            this.frames = 0;
            this.frameStart = time;
        }
        this.frames++;
    }

    @Override // com.badlogic.gdx.Graphics
    public float getDeltaTime() {
        return this.mean.getMean() == 0.0f ? this.deltaTime : this.mean.getMean();
    }

    @Override // com.badlogic.gdx.Graphics
    public Graphics.GraphicsType getType() {
        return Graphics.GraphicsType.AndroidGL;
    }

    @Override // com.badlogic.gdx.Graphics
    public int getFramesPerSecond() {
        return this.fps;
    }

    public void clearManagedCaches() {
        Mesh.clearAllMeshes(this.app);
        Texture.clearAllTextures(this.app);
        ShaderProgram.clearAllShaderPrograms(this.app);
        FrameBuffer.clearAllFrameBuffers(this.app);
        Gdx.app.log("AndroidGraphics", Mesh.getManagedStatus());
        Gdx.app.log("AndroidGraphics", Texture.getManagedStatus());
        Gdx.app.log("AndroidGraphics", ShaderProgram.getManagedStatus());
        Gdx.app.log("AndroidGraphics", FrameBuffer.getManagedStatus());
    }

    public View getView() {
        return this.view;
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
    public float getDensity() {
        return this.density;
    }

    @Override // com.badlogic.gdx.Graphics
    public GLU getGLU() {
        return this.glu;
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
    public Graphics.DisplayMode[] getDisplayModes() {
        return new Graphics.DisplayMode[]{getDesktopDisplayMode()};
    }

    @Override // com.badlogic.gdx.Graphics
    public boolean setDisplayMode(int width, int height, boolean fullscreen) {
        return false;
    }

    @Override // com.badlogic.gdx.Graphics
    public void setTitle(String title) {
    }

    public void setIcon(Pixmap[] pixmap) {
    }

    private class AndroidDisplayMode extends Graphics.DisplayMode {
        protected AndroidDisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
            super(width, height, refreshRate, bitsPerPixel);
        }
    }

    @Override // com.badlogic.gdx.Graphics
    public Graphics.DisplayMode getDesktopDisplayMode() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.app.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new AndroidDisplayMode(metrics.widthPixels, metrics.heightPixels, 0, 0);
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
}
