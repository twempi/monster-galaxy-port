package com.gaiaonline.monstergalaxy.desktop;

import com.gaiaonline.monstergalaxy.screen.TextFieldScreenElement;

/**
 * The game's desktop text field ignores the length limit and disallowed
 * characters that the Android field enforced. This builds one that honours them.
 */
public final class DesktopTextFields {
    private DesktopTextFields() {
    }

    public static TextFieldScreenElement create(int width, final Integer maxChars, final String disallowedChars) {
        return new TextFieldScreenElement(width) {
            @Override
            public boolean keyTyped(char c) {
                if (c >= ' ' && c != 127) { // printable; editing keys always pass
                    if (disallowedChars != null && disallowedChars.indexOf(c) >= 0) {
                        return true;
                    }
                    if (maxChars != null && getText().length() >= maxChars.intValue()) {
                        return true;
                    }
                }
                return super.keyTyped(c);
            }
        };
    }
}
