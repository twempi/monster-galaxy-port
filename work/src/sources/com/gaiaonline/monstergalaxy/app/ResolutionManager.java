package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;

/* JADX INFO: loaded from: classes.dex */
public class ResolutionManager {
    public static final float BASE_SCREEN_HEIGHT = 320.0f;
    public static final float BASE_SCREEN_WIDTH = 480.0f;
    public static final String RESOLUTION_SUFFIX;
    private static final float arx;
    private static float ary;
    private static float imgScale;
    private static float screenHeight;
    private static final float screenWidth = Gdx.graphics.getWidth();

    static {
        switch (Gdx.graphics.getWidth()) {
            case 480:
                RESOLUTION_SUFFIX = "";
                break;
            case 800:
                RESOLUTION_SUFFIX = ".800";
                break;
            default:
                RESOLUTION_SUFFIX = ".854";
                break;
        }
        screenHeight = Gdx.graphics.getHeight();
        if (PlatformHelper.getDeviceModel() == PlatformHelper.Model.KINDLE_FIRE) {
            screenHeight = 580.0f;
        }
        arx = screenWidth / 480.0f;
        ary = screenHeight / 320.0f;
        imgScale = ary;
    }

    public static float getScreenHeight() {
        return screenHeight;
    }

    public static float getScreenWidth() {
        return screenWidth;
    }

    public static float getHorizontalAR() {
        return arx;
    }

    public static float getVerticalAR() {
        return ary;
    }

    public static float getScaleFactor() {
        return imgScale;
    }

    public static void overrideHeightForKindle(int height) {
        screenHeight = height;
        ary = screenHeight / 320.0f;
        imgScale = ary;
    }

    public static float[] getCoords(float x, float y) {
        float[] coords = {arx * x, ary * y};
        return coords;
    }

    public static Image getScaledImage(String name, TextureRegion textureRegion) {
        if (textureRegion != null) {
            Image image = new Image(textureRegion);
            image.setWidth(textureRegion.getRegionWidth() * imgScale);
            image.setHeight(textureRegion.getRegionHeight() * imgScale);
            return image;
        }
        return new Image();
    }

    public static void scaleActor(Actor actor) {
        actor.setWidth(imgScale * actor.getWidth());
        actor.setHeight(imgScale * actor.getHeight());
    }

    public static void scale(TextureRegionDrawable drawable) {
        drawable.setMinWidth(imgScale * drawable.getRegion().getRegionWidth());
        drawable.setMinHeight(imgScale * drawable.getRegion().getRegionHeight());
    }
}
