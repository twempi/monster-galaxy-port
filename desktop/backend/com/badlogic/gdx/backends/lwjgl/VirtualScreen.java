package com.badlogic.gdx.backends.lwjgl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

/**
 * Monster Galaxy reads the screen size once at startup and lays everything out
 * from it, so it cannot handle a resized window. This renders the game into an
 * offscreen framebuffer of a fixed logical size and draws that, letterboxed, into
 * the real window. The game (including its glScissor/glReadPixels calls) only ever
 * sees the logical size; mouse coordinates are mapped back in LwjglInput.
 */
final class VirtualScreen {
	static boolean active;
	static int width, height;

	private static int fbo, colorTexture, depthBuffer;
	// Placement of the logical screen inside the window, in window pixels.
	private static int viewX, viewY, viewWidth, viewHeight;

	private VirtualScreen () {
	}

	static void init (int logicalWidth, int logicalHeight) {
		width = logicalWidth;
		height = logicalHeight;
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.err.println("VirtualScreen: EXT_framebuffer_object unavailable; window resizing disabled");
			return;
		}

		colorTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
			(ByteBuffer)null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		depthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthBuffer);
		EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL11.GL_DEPTH_COMPONENT, width,
			height);
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, 0);

		fbo = EXTFramebufferObject.glGenFramebuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
			EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTexture, 0);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
			EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthBuffer);
		int status = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		if (status != EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT) {
			System.err.println("VirtualScreen: framebuffer incomplete (" + status + "); window resizing disabled");
			return;
		}
		active = true;
		layout(Display.getWidth(), Display.getHeight());
	}

	/** Redirects the game's rendering into the logical-size framebuffer. */
	static void begin () {
		if (!active) return;
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo);
		GL11.glViewport(0, 0, width, height);
	}

	/** Draws the framebuffer into the window, scaled to fit with black bars. */
	static void end () {
		if (!active) return;
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		layout(Display.getWidth(), Display.getHeight());

		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glViewport(viewX, viewY, viewWidth, viewHeight);

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(-1, -1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(1, -1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(1, 1);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(-1, 1);
		GL11.glEnd();

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	private static void layout (int windowWidth, int windowHeight) {
		float scale = Math.min(windowWidth / (float)width, windowHeight / (float)height);
		viewWidth = Math.round(width * scale);
		viewHeight = Math.round(height * scale);
		viewX = (windowWidth - viewWidth) / 2;
		viewY = (windowHeight - viewHeight) / 2;
	}

	/** Window mouse x (left origin) to logical x. */
	static int mouseX (int windowX) {
		if (!active) return windowX;
		return clamp((int)((windowX - viewX) * (width / (float)viewWidth)), width);
	}

	/** Window mouse y (LWJGL: bottom origin) to logical y (top origin, as libGDX expects). */
	static int mouseY (int windowYFromBottom) {
		if (!active) return Display.getHeight() - 1 - windowYFromBottom;
		int fromBottom = (int)((windowYFromBottom - viewY) * (height / (float)viewHeight));
		return clamp(height - 1 - fromBottom, height);
	}

	/** Window mouse delta to logical delta. */
	static int mouseDelta (int windowDelta) {
		if (!active) return windowDelta;
		return Math.round(windowDelta * (width / (float)viewWidth));
	}

	private static int clamp (int value, int size) {
		return value < 0 ? 0 : value >= size ? size - 1 : value;
	}
}
