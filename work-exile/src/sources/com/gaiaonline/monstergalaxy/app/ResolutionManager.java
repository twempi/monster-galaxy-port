package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
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
            Image image = new Image(name, textureRegion);
            image.width = textureRegion.getRegionWidth() * imgScale;
            image.height = textureRegion.getRegionHeight() * imgScale;
            return image;
        }
        return new Image(name);
    }

    public static void scaleActor(Actor actor) {
        actor.width *= imgScale;
        actor.height *= imgScale;
    }
}
