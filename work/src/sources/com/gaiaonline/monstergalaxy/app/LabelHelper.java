package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/* JADX INFO: loaded from: classes.dex */
public class LabelHelper {
    public static float setFontScaleForHorizontalAutoSize(BitmapFont font, float maxWidth, float fontScale, String msg) {
        float newFontScale = fontScale;
        if (msg != null && msg.trim().length() > 0 && maxWidth > 1.0f) {
            float lowerLimit = 0.0f;
            float upperLimit = fontScale;
            newFontScale = upperLimit;
            font.setScale(newFontScale);
            if (font.getBounds(msg).width <= maxWidth) {
                return newFontScale;
            }
            boolean isTooWide = true;
            while (true) {
                if ((upperLimit <= lowerLimit || upperLimit - lowerLimit <= 0.01f) && !isTooWide) {
                    break;
                }
                float middleValue = (lowerLimit + upperLimit) / 2.0f;
                newFontScale = middleValue;
                font.setScale(newFontScale);
                float newWidth = font.getBounds(msg).width;
                if (newWidth == maxWidth) {
                    return newFontScale;
                }
                if (newWidth < maxWidth) {
                    isTooWide = false;
                    lowerLimit = middleValue;
                } else if (newWidth > maxWidth) {
                    isTooWide = true;
                    upperLimit = middleValue;
                }
            }
        }
        return newFontScale;
    }
}
