package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: classes.dex */
public enum Scaling {
    fit,
    fill,
    fillX,
    fillY,
    stretch,
    stretchX,
    stretchY,
    none;

    private static Vector2 temp = new Vector2();

    public Vector2 apply(float sourceWidth, float sourceHeight, float targetWidth, float targetHeight) {
        switch (this) {
            case fit:
                float targetRatio = targetHeight / targetWidth;
                float sourceRatio = sourceHeight / sourceWidth;
                float scale = targetRatio > sourceRatio ? targetWidth / sourceWidth : targetHeight / sourceHeight;
                temp.x = sourceWidth * scale;
                temp.y = sourceHeight * scale;
                break;
            case fill:
                float targetRatio2 = targetHeight / targetWidth;
                float sourceRatio2 = sourceHeight / sourceWidth;
                float scale2 = targetRatio2 < sourceRatio2 ? targetWidth / sourceWidth : targetHeight / sourceHeight;
                temp.x = sourceWidth * scale2;
                temp.y = sourceHeight * scale2;
                break;
            case fillX:
                float targetRatio3 = targetHeight / targetWidth;
                float f = sourceHeight / sourceWidth;
                float scale3 = targetWidth / sourceWidth;
                temp.x = sourceWidth * scale3;
                temp.y = sourceHeight * scale3;
                break;
            case fillY:
                float targetRatio4 = targetHeight / targetWidth;
                float f2 = sourceHeight / sourceWidth;
                float scale4 = targetHeight / sourceHeight;
                temp.x = sourceWidth * scale4;
                temp.y = sourceHeight * scale4;
                break;
            case stretch:
                temp.x = targetWidth;
                temp.y = targetHeight;
                break;
            case stretchX:
                temp.x = targetWidth;
                temp.y = sourceHeight;
                break;
            case stretchY:
                temp.x = sourceWidth;
                temp.y = targetHeight;
                break;
            case none:
                temp.x = sourceWidth;
                temp.y = sourceHeight;
                break;
        }
        return temp;
    }
}
