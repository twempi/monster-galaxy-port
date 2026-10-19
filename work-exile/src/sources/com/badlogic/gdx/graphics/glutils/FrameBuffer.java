package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FrameBuffer implements Disposable {
    private static final Map<Application, List<FrameBuffer>> buffers = new HashMap();
    private Texture colorTexture;
    private int depthbufferHandle;
    private final Pixmap.Format format;
    private int framebufferHandle;
    private final boolean hasDepth;
    private final int height;
    private final int width;

    public FrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.hasDepth = hasDepth;
        build();
        addManagedFrameBuffer(Gdx.app, this);
    }

    private void build() {
        this.colorTexture = new Texture(this.width, this.height, this.format);
        this.colorTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.colorTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        GL20 gl = Gdx.graphics.getGL20();
        ByteBuffer tmp = ByteBuffer.allocateDirect(4);
        tmp.order(ByteOrder.nativeOrder());
        IntBuffer handle = tmp.asIntBuffer();
        gl.glGenFramebuffers(1, handle);
        this.framebufferHandle = handle.get(0);
        if (this.hasDepth) {
            gl.glGenRenderbuffers(1, handle);
            this.depthbufferHandle = handle.get(0);
        }
        gl.glBindTexture(3553, this.colorTexture.getTextureObjectHandle());
        if (this.hasDepth) {
            gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, this.depthbufferHandle);
            gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, GL20.GL_DEPTH_COMPONENT16, this.colorTexture.getWidth(), this.colorTexture.getHeight());
        }
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.framebufferHandle);
        gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, 3553, this.colorTexture.getTextureObjectHandle(), 0);
        if (this.hasDepth) {
            gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, GL20.GL_RENDERBUFFER, this.depthbufferHandle);
        }
        int result = gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER);
        gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
        gl.glBindTexture(3553, 0);
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
        if (result != 36053) {
            this.colorTexture.dispose();
            if (this.hasDepth) {
                handle.put(this.depthbufferHandle);
                handle.flip();
                gl.glDeleteRenderbuffers(1, handle);
            }
            this.colorTexture.dispose();
            handle.put(this.framebufferHandle);
            handle.flip();
            gl.glDeleteFramebuffers(1, handle);
            if (result == 36054) {
                throw new IllegalStateException("frame buffer couldn't be constructed: incomplete attachment");
            }
            if (result == 36057) {
                throw new IllegalStateException("frame buffer couldn't be constructed: incomplete dimensions");
            }
            if (result == 36055) {
                throw new IllegalStateException("frame buffer couldn't be constructed: missing attachment");
            }
        }
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        GL20 gl = Gdx.graphics.getGL20();
        ByteBuffer tmp = ByteBuffer.allocateDirect(4);
        tmp.order(ByteOrder.nativeOrder());
        IntBuffer handle = tmp.asIntBuffer();
        this.colorTexture.dispose();
        if (this.hasDepth) {
            handle.put(this.depthbufferHandle);
            handle.flip();
            gl.glDeleteRenderbuffers(1, handle);
        }
        handle.put(this.framebufferHandle);
        handle.flip();
        gl.glDeleteFramebuffers(1, handle);
        if (buffers.get(Gdx.app) != null) {
            buffers.get(Gdx.app).remove(this);
        }
    }

    public void begin() {
        Gdx.graphics.getGL20().glViewport(0, 0, this.colorTexture.getWidth(), this.colorTexture.getHeight());
        Gdx.graphics.getGL20().glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.framebufferHandle);
    }

    public void end() {
        Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.graphics.getGL20().glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
    }

    private void addManagedFrameBuffer(Application app, FrameBuffer frameBuffer) {
        List<FrameBuffer> managedResources = buffers.get(app);
        if (managedResources == null) {
            managedResources = new ArrayList<>();
        }
        managedResources.add(frameBuffer);
        buffers.put(app, managedResources);
    }

    public static void invalidateAllFrameBuffers(Application app) {
        List<FrameBuffer> bufferList;
        if (Gdx.graphics.getGL20() != null && (bufferList = buffers.get(app)) != null) {
            for (int i = 0; i < bufferList.size(); i++) {
                bufferList.get(i).build();
            }
        }
    }

    public static void clearAllFrameBuffers(Application app) {
        buffers.remove(app);
    }

    public static String getManagedStatus() {
        StringBuilder builder = new StringBuilder();
        builder.append("Managed buffers/app: { ");
        for (Application app : buffers.keySet()) {
            builder.append(buffers.get(app).size());
            builder.append(" ");
        }
        builder.append("}");
        return builder.toString();
    }

    public Texture getColorBufferTexture() {
        return this.colorTexture;
    }

    public int getHeight() {
        return this.colorTexture.getHeight();
    }

    public int getWidth() {
        return this.colorTexture.getWidth();
    }
}
