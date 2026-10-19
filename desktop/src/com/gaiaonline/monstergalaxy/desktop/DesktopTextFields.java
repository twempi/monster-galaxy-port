package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.screen.TextFieldScreenElement;

/**
 * The game's desktop text field ignores the requested width and length limits
 * (only noticeable above 480x320). This builds one that honours them.
 */
public final class DesktopTextFields {
    private DesktopTextFields() {
    }

    public static TextFieldScreenElement create(int width, final Integer maxChars, final String disallowedChars) {
        TextFieldScreenElement element = new TextFieldScreenElement(width);
        TextField field = (TextField) element.getActor();
        field.setWidth(width * ResolutionManager.getScaleFactor());
        field.setTextFieldFilter(new TextField.TextFieldFilter() {
            public boolean acceptChar(TextField textField, char c) {
                if (disallowedChars != null && disallowedChars.indexOf(c) >= 0) {
                    return false;
                }
                return maxChars == null || textField.getText().length() < maxChars.intValue();
            }
        });
        element.setActor(field); // re-sync the element's size with the actor
        return element;
    }
}
