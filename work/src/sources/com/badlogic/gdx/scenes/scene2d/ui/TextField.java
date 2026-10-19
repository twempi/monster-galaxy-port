package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

/* JADX INFO: loaded from: classes.dex */
public class TextField extends Widget {
    private static final char BACKSPACE = '\b';
    private static final char BULLET = 149;
    private static final char DELETE = 127;
    private static final char ENTER_ANDROID = '\n';
    private static final char ENTER_DESKTOP = '\r';
    private static final char TAB = '\t';
    private float blinkTime;
    private Clipboard clipboard;
    int cursor;
    boolean cursorOn;
    boolean disabled;
    private CharSequence displayText;
    private final Rectangle fieldBounds;
    TextFieldFilter filter;
    boolean focusTraversal;
    private final FloatArray glyphAdvances;
    final FloatArray glyphPositions;
    boolean hasSelection;
    InputListener inputListener;
    float keyRepeatInitialTime;
    KeyRepeatTask keyRepeatTask;
    float keyRepeatTime;
    OnscreenKeyboard keyboard;
    long lastBlink;
    TextFieldListener listener;
    String messageText;
    private StringBuilder passwordBuffer;
    private char passwordCharacter;
    private boolean passwordMode;
    float renderOffset;
    boolean rightAligned;
    private final Rectangle scissor;
    int selectionStart;
    private float selectionWidth;
    private float selectionX;
    TextFieldStyle style;
    String text;
    private final BitmapFont.TextBounds textBounds;
    float textOffset;
    private int visibleTextEnd;
    private int visibleTextStart;

    public interface OnscreenKeyboard {
        void show(boolean z);
    }

    public interface TextFieldListener {
        void keyTyped(TextField textField, char c);
    }

    public TextField(String text, Skin skin) {
        this(text, (TextFieldStyle) skin.get(TextFieldStyle.class));
    }

    public TextField(String text, Skin skin, String styleName) {
        this(text, (TextFieldStyle) skin.get(styleName, TextFieldStyle.class));
    }

    public TextField(String text, TextFieldStyle style) {
        this.keyboard = new DefaultOnscreenKeyboard();
        this.focusTraversal = true;
        this.fieldBounds = new Rectangle();
        this.textBounds = new BitmapFont.TextBounds();
        this.scissor = new Rectangle();
        this.glyphAdvances = new FloatArray();
        this.glyphPositions = new FloatArray();
        this.cursorOn = true;
        this.blinkTime = 0.32f;
        this.passwordCharacter = BULLET;
        this.keyRepeatTask = new KeyRepeatTask();
        this.keyRepeatInitialTime = 0.4f;
        this.keyRepeatTime = 0.1f;
        setStyle(style);
        this.clipboard = Gdx.app.getClipboard();
        setText(text);
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        initialize();
    }

    private void initialize() {
        ClickListener clickListener = new ClickListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.TextField.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() > 1) {
                    TextField.this.setSelection(0, TextField.this.text.length());
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener, com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!super.touchDown(event, x, y, pointer, button)) {
                    return false;
                }
                if (pointer == 0 && button != 0) {
                    return false;
                }
                if (TextField.this.disabled) {
                    return true;
                }
                TextField.this.keyboard.show(true);
                TextField.this.clearSelection();
                setCursorPosition(x);
                TextField.this.selectionStart = TextField.this.cursor;
                Stage stage = TextField.this.getStage();
                if (stage != null) {
                    stage.setKeyboardFocus(TextField.this);
                }
                return true;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.utils.ClickListener, com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                TextField.this.lastBlink = 0L;
                TextField.this.cursorOn = false;
                setCursorPosition(x);
                TextField.this.hasSelection = true;
            }

            private void setCursorPosition(float x) {
                TextField.this.lastBlink = 0L;
                TextField.this.cursorOn = false;
                float x2 = x - (TextField.this.renderOffset + TextField.this.textOffset);
                for (int i = 0; i < TextField.this.glyphPositions.size; i++) {
                    if (TextField.this.glyphPositions.items[i] > x2) {
                        TextField.this.cursor = Math.max(0, i - 1);
                        return;
                    }
                }
                TextField.this.cursor = Math.max(0, TextField.this.glyphPositions.size - 1);
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyDown(InputEvent event, int keycode) {
                if (TextField.this.disabled) {
                    return false;
                }
                BitmapFont bitmapFont = TextField.this.style.font;
                TextField.this.lastBlink = 0L;
                TextField.this.cursorOn = false;
                Stage stage = TextField.this.getStage();
                if (stage != null && stage.getKeyboardFocus() == TextField.this) {
                    boolean repeat = false;
                    boolean ctrl = Gdx.input.isKeyPressed(129) || Gdx.input.isKeyPressed(130);
                    if (ctrl) {
                        if (keycode == 50) {
                            TextField.this.paste();
                            return true;
                        }
                        if (keycode == 31 || keycode == 133) {
                            TextField.this.copy();
                            return true;
                        }
                        if (keycode == 52 || keycode == 67) {
                            TextField.this.cut();
                            return true;
                        }
                    }
                    if (Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60)) {
                        if (keycode == 133) {
                            TextField.this.paste();
                        }
                        if (keycode == 112 && TextField.this.hasSelection) {
                            TextField.this.copy();
                            TextField.this.delete();
                        }
                        if (keycode == 21) {
                            if (!TextField.this.hasSelection) {
                                TextField.this.selectionStart = TextField.this.cursor;
                                TextField.this.hasSelection = true;
                            }
                            while (true) {
                                TextField textField = TextField.this;
                                int i = textField.cursor - 1;
                                textField.cursor = i;
                                if (i <= 0 || !ctrl) {
                                    break;
                                }
                                char c = TextField.this.text.charAt(TextField.this.cursor);
                                if (c < 'A' || c > 'Z') {
                                    if (c < 'a' || c > 'z') {
                                        if (c < '0' || c > '9') {
                                            break;
                                        }
                                    }
                                }
                            }
                            repeat = true;
                        }
                        if (keycode == 22) {
                            if (!TextField.this.hasSelection) {
                                TextField.this.selectionStart = TextField.this.cursor;
                                TextField.this.hasSelection = true;
                            }
                            int length = TextField.this.text.length();
                            while (true) {
                                TextField textField2 = TextField.this;
                                int i2 = textField2.cursor + 1;
                                textField2.cursor = i2;
                                if (i2 >= length || !ctrl) {
                                    break;
                                }
                                char c2 = TextField.this.text.charAt(TextField.this.cursor - 1);
                                if (c2 < 'A' || c2 > 'Z') {
                                    if (c2 < 'a' || c2 > 'z') {
                                        if (c2 < '0' || c2 > '9') {
                                            break;
                                        }
                                    }
                                }
                            }
                            repeat = true;
                        }
                        if (keycode == 3) {
                            if (!TextField.this.hasSelection) {
                                TextField.this.selectionStart = TextField.this.cursor;
                                TextField.this.hasSelection = true;
                            }
                            TextField.this.cursor = 0;
                        }
                        if (keycode == 132) {
                            if (!TextField.this.hasSelection) {
                                TextField.this.selectionStart = TextField.this.cursor;
                                TextField.this.hasSelection = true;
                            }
                            TextField.this.cursor = TextField.this.text.length();
                        }
                        TextField.this.cursor = Math.max(0, TextField.this.cursor);
                        TextField.this.cursor = Math.min(TextField.this.text.length(), TextField.this.cursor);
                    } else {
                        if (keycode == 21) {
                            while (true) {
                                TextField textField3 = TextField.this;
                                int i3 = textField3.cursor;
                                textField3.cursor = i3 - 1;
                                if (i3 <= 1 || !ctrl) {
                                    break;
                                }
                                char c3 = TextField.this.text.charAt(TextField.this.cursor - 1);
                                if (c3 < 'A' || c3 > 'Z') {
                                    if (c3 < 'a' || c3 > 'z') {
                                        if (c3 < '0' || c3 > '9') {
                                            break;
                                        }
                                    }
                                }
                            }
                            TextField.this.clearSelection();
                            repeat = true;
                        }
                        if (keycode == 22) {
                            int length2 = TextField.this.text.length();
                            while (true) {
                                TextField textField4 = TextField.this;
                                int i4 = textField4.cursor + 1;
                                textField4.cursor = i4;
                                if (i4 >= length2 || !ctrl) {
                                    break;
                                }
                                char c4 = TextField.this.text.charAt(TextField.this.cursor - 1);
                                if (c4 < 'A' || c4 > 'Z') {
                                    if (c4 < 'a' || c4 > 'z') {
                                        if (c4 < '0' || c4 > '9') {
                                            break;
                                        }
                                    }
                                }
                            }
                            TextField.this.clearSelection();
                            repeat = true;
                        }
                        if (keycode == 3) {
                            TextField.this.cursor = 0;
                            TextField.this.clearSelection();
                        }
                        if (keycode == 132) {
                            TextField.this.cursor = TextField.this.text.length();
                            TextField.this.clearSelection();
                        }
                        TextField.this.cursor = Math.max(0, TextField.this.cursor);
                        TextField.this.cursor = Math.min(TextField.this.text.length(), TextField.this.cursor);
                    }
                    if (repeat && (!TextField.this.keyRepeatTask.isScheduled() || TextField.this.keyRepeatTask.keycode != keycode)) {
                        TextField.this.keyRepeatTask.keycode = keycode;
                        TextField.this.keyRepeatTask.cancel();
                        Timer.schedule(TextField.this.keyRepeatTask, TextField.this.keyRepeatInitialTime, TextField.this.keyRepeatTime);
                    }
                    return true;
                }
                return false;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyUp(InputEvent event, int keycode) {
                if (TextField.this.disabled) {
                    return false;
                }
                TextField.this.keyRepeatTask.cancel();
                return true;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyTyped(InputEvent event, char character) {
                if (TextField.this.disabled) {
                    return false;
                }
                BitmapFont font = TextField.this.style.font;
                Stage stage = TextField.this.getStage();
                if (stage == null || stage.getKeyboardFocus() != TextField.this) {
                    return false;
                }
                if (character == '\b' && (TextField.this.cursor > 0 || TextField.this.hasSelection)) {
                    if (!TextField.this.hasSelection) {
                        TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor - 1) + TextField.this.text.substring(TextField.this.cursor);
                        TextField.this.updateDisplayText();
                        TextField.this.cursor--;
                        TextField.this.renderOffset = 0.0f;
                    } else {
                        TextField.this.delete();
                    }
                }
                if (character == 127) {
                    if (TextField.this.cursor >= TextField.this.text.length() && !TextField.this.hasSelection) {
                        return true;
                    }
                    if (!TextField.this.hasSelection) {
                        TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor) + TextField.this.text.substring(TextField.this.cursor + 1);
                        TextField.this.updateDisplayText();
                        return true;
                    }
                    TextField.this.delete();
                    return true;
                }
                if (character != '\r' && character != '\n' && TextField.this.filter != null && !TextField.this.filter.acceptChar(TextField.this, character)) {
                    return true;
                }
                if ((character == '\t' || character == '\n') && TextField.this.focusTraversal) {
                    TextField.this.next(Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60));
                }
                if (font.containsCharacter(character)) {
                    if (!TextField.this.hasSelection) {
                        TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor) + character + TextField.this.text.substring(TextField.this.cursor, TextField.this.text.length());
                        TextField.this.updateDisplayText();
                        TextField.this.cursor++;
                    } else {
                        int minIndex = Math.min(TextField.this.cursor, TextField.this.selectionStart);
                        int maxIndex = Math.max(TextField.this.cursor, TextField.this.selectionStart);
                        TextField.this.text = (minIndex > 0 ? TextField.this.text.substring(0, minIndex) : "") + (maxIndex < TextField.this.text.length() ? TextField.this.text.substring(maxIndex, TextField.this.text.length()) : "");
                        TextField.this.cursor = minIndex;
                        TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor) + character + TextField.this.text.substring(TextField.this.cursor, TextField.this.text.length());
                        TextField.this.updateDisplayText();
                        TextField.this.cursor++;
                        TextField.this.clearSelection();
                    }
                }
                if (TextField.this.listener == null) {
                    return true;
                }
                TextField.this.listener.keyTyped(TextField.this, character);
                return true;
            }
        };
        this.inputListener = clickListener;
        addListener(clickListener);
    }

    public void setStyle(TextFieldStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.style = style;
        invalidateHierarchy();
    }

    public void setPasswordCharacter(char passwordCharacter) {
        this.passwordCharacter = passwordCharacter;
    }

    public TextFieldStyle getStyle() {
        return this.style;
    }

    private void calculateOffsets() {
        float visibleWidth = getWidth();
        if (this.style.background != null) {
            visibleWidth -= this.style.background.getLeftWidth() + this.style.background.getRightWidth();
        }
        float position = this.glyphPositions.get(this.cursor);
        float distance = position - Math.abs(this.renderOffset);
        if (distance <= 0.0f) {
            if (this.cursor > 0) {
                this.renderOffset = -this.glyphPositions.get(this.cursor - 1);
            } else {
                this.renderOffset = 0.0f;
            }
        } else if (distance > visibleWidth) {
            this.renderOffset -= distance - visibleWidth;
        }
        this.visibleTextStart = 0;
        this.textOffset = 0.0f;
        float start = Math.abs(this.renderOffset);
        int len = this.glyphPositions.size;
        float startPos = 0.0f;
        for (int i = 0; i < len; i++) {
            if (this.glyphPositions.items[i] >= start) {
                this.visibleTextStart = i;
                startPos = this.glyphPositions.items[i];
                this.textOffset = startPos - start;
                break;
            }
        }
        this.visibleTextEnd = Math.min(this.displayText.length(), this.cursor + 1);
        while (this.visibleTextEnd <= this.displayText.length() && this.glyphPositions.items[this.visibleTextEnd] - startPos <= visibleWidth) {
            this.visibleTextEnd++;
        }
        this.visibleTextEnd = Math.max(0, this.visibleTextEnd - 1);
        if (this.hasSelection) {
            int minIndex = Math.min(this.cursor, this.selectionStart);
            int maxIndex = Math.max(this.cursor, this.selectionStart);
            float minX = Math.max(this.glyphPositions.get(minIndex), startPos);
            float maxX = Math.min(this.glyphPositions.get(maxIndex), this.glyphPositions.get(this.visibleTextEnd));
            this.selectionX = minX;
            this.selectionWidth = maxX - minX;
        }
        if (this.rightAligned) {
            this.textOffset = visibleWidth - (this.glyphPositions.items[this.visibleTextEnd] - startPos);
            if (this.hasSelection) {
                this.selectionX += this.textOffset;
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        float textY;
        BitmapFont font = this.style.font;
        Color fontColor = this.disabled ? this.style.disabledFontColor : this.style.fontColor;
        Drawable selection = this.style.selection;
        Drawable cursorPatch = this.style.cursor;
        Drawable background = this.style.background;
        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        float textY2 = (this.textBounds.height / 2.0f) + font.getDescent();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        float bgLeftWidth = 0.0f;
        if (background != null) {
            background.draw(batch, x, y, width, height);
            bgLeftWidth = background.getLeftWidth();
            float bottom = background.getBottomHeight();
            textY = (int) ((((height - background.getTopHeight()) - bottom) / 2.0f) + textY2 + bottom);
        } else {
            textY = (int) ((height / 2.0f) + textY2);
        }
        calculateOffsets();
        Stage stage = getStage();
        boolean focused = stage != null && stage.getKeyboardFocus() == this;
        if (focused && this.hasSelection && selection != null) {
            selection.draw(batch, this.selectionX + x + bgLeftWidth + this.renderOffset, ((y + textY) - this.textBounds.height) - font.getDescent(), this.selectionWidth, this.textBounds.height + (font.getDescent() / 2.0f));
        }
        float yOffset = font.isFlipped() ? -this.textBounds.height : 0.0f;
        if (this.displayText.length() == 0) {
            if (!focused && this.messageText != null) {
                if (this.style.messageFontColor != null) {
                    font.setColor(this.style.messageFontColor.r, this.style.messageFontColor.g, this.style.messageFontColor.b, this.style.messageFontColor.a * parentAlpha);
                } else {
                    font.setColor(0.7f, 0.7f, 0.7f, parentAlpha);
                }
                if (this.style.messageFont != null) {
                    BitmapFont bitmapFont = this.style.messageFont;
                }
                font.draw(batch, this.messageText, x + bgLeftWidth, y + textY + yOffset);
            }
        } else {
            font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
            font.draw(batch, this.displayText, x + bgLeftWidth + this.textOffset, y + textY + yOffset, this.visibleTextStart, this.visibleTextEnd);
        }
        if (focused && !this.disabled) {
            blink();
            if (this.cursorOn && cursorPatch != null) {
                cursorPatch.draw(batch, ((((x + bgLeftWidth) + this.textOffset) + this.glyphPositions.get(this.cursor)) - this.glyphPositions.items[this.visibleTextStart]) - 1.0f, ((y + textY) - this.textBounds.height) - font.getDescent(), cursorPatch.getMinWidth(), this.textBounds.height + (font.getDescent() / 2.0f));
            }
        }
    }

    void updateDisplayText() {
        if (this.passwordMode && this.style.font.containsCharacter(this.passwordCharacter)) {
            if (this.passwordBuffer == null) {
                this.passwordBuffer = new StringBuilder(this.text.length());
            }
            if (this.passwordBuffer.length() > this.text.length()) {
                this.passwordBuffer.setLength(this.text.length());
            } else {
                int n = this.text.length();
                for (int i = this.passwordBuffer.length(); i < n; i++) {
                    this.passwordBuffer.append(this.passwordCharacter);
                }
            }
            this.displayText = this.passwordBuffer;
        } else {
            this.displayText = this.text;
        }
        this.style.font.computeGlyphAdvancesAndPositions(this.displayText, this.glyphAdvances, this.glyphPositions);
        if (this.selectionStart > this.text.length()) {
            this.selectionStart = this.text.length();
        }
    }

    private void blink() {
        long time = TimeUtils.nanoTime();
        if ((time - this.lastBlink) / 1.0E9f > this.blinkTime) {
            this.cursorOn = !this.cursorOn;
            this.lastBlink = time;
        }
    }

    public void copy() {
        if (this.hasSelection) {
            int minIndex = Math.min(this.cursor, this.selectionStart);
            int maxIndex = Math.max(this.cursor, this.selectionStart);
            this.clipboard.setContents(this.text.substring(minIndex, maxIndex));
        }
    }

    public void cut() {
        if (this.hasSelection) {
            copy();
            delete();
        }
    }

    void paste() {
        String content = this.clipboard.getContents();
        if (content != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (this.style.font.containsCharacter(c)) {
                    builder.append(c);
                }
            }
            String content2 = builder.toString();
            if (!this.hasSelection) {
                this.text = this.text.substring(0, this.cursor) + content2 + this.text.substring(this.cursor, this.text.length());
                updateDisplayText();
                this.cursor += content2.length();
                return;
            }
            int minIndex = Math.min(this.cursor, this.selectionStart);
            int maxIndex = Math.max(this.cursor, this.selectionStart);
            this.text = (minIndex > 0 ? this.text.substring(0, minIndex) : "") + (maxIndex < this.text.length() ? this.text.substring(maxIndex, this.text.length()) : "");
            this.cursor = minIndex;
            this.text = this.text.substring(0, this.cursor) + content2 + this.text.substring(this.cursor, this.text.length());
            updateDisplayText();
            this.cursor = content2.length() + minIndex;
            clearSelection();
        }
    }

    void delete() {
        int minIndex = Math.min(this.cursor, this.selectionStart);
        int maxIndex = Math.max(this.cursor, this.selectionStart);
        this.text = (minIndex > 0 ? this.text.substring(0, minIndex) : "") + (maxIndex < this.text.length() ? this.text.substring(maxIndex, this.text.length()) : "");
        updateDisplayText();
        this.cursor = minIndex;
        clearSelection();
    }

    public void next(boolean up) {
        Stage stage = getStage();
        if (stage != null) {
            getParent().localToStageCoordinates(Vector2.tmp.set(getX(), getY()));
            TextField textField = findNextTextField(stage.getActors(), null, Vector2.tmp2, Vector2.tmp, up);
            if (textField == null) {
                if (up) {
                    Vector2.tmp.set(Float.MIN_VALUE, Float.MIN_VALUE);
                } else {
                    Vector2.tmp.set(Float.MAX_VALUE, Float.MAX_VALUE);
                }
                textField = findNextTextField(getStage().getActors(), null, Vector2.tmp2, Vector2.tmp, up);
            }
            if (textField != null) {
                stage.setKeyboardFocus(textField);
            } else {
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0064  */
    private TextField findNextTextField(Array<Actor> actors, TextField best, Vector2 bestCoords, Vector2 currentCoords, boolean up) {
        int n = actors.size;
        for (int i = 0; i < n; i++) {
            Actor actor = actors.get(i);
            if (actor != this) {
                if (actor instanceof TextField) {
                    Vector2 actorCoords = actor.getParent().localToStageCoordinates(Vector2.tmp3.set(actor.getX(), actor.getY()));
                    if ((actorCoords.y < currentCoords.y || (actorCoords.y == currentCoords.y && actorCoords.x > currentCoords.x)) ^ up) {
                        if (best == null) {
                            best = (TextField) actor;
                            bestCoords.set(actorCoords);
                        } else if ((actorCoords.y > bestCoords.y || (actorCoords.y == bestCoords.y && actorCoords.x < bestCoords.x)) ^ up) {
                            best = (TextField) actor;
                            bestCoords.set(actorCoords);
                        }
                    }
                }
                if (actor instanceof Group) {
                    best = findNextTextField(((Group) actor).getChildren(), best, bestCoords, currentCoords, up);
                }
            }
        }
        return best;
    }

    public void setTextFieldListener(TextFieldListener listener) {
        this.listener = listener;
    }

    public void setTextFieldFilter(TextFieldFilter filter) {
        this.filter = filter;
    }

    public void setFocusTraversal(boolean focusTraversal) {
        this.focusTraversal = focusTraversal;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        BitmapFont font = this.style.font;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (font.containsCharacter(c)) {
                buffer.append(c);
            }
        }
        this.text = buffer.toString();
        updateDisplayText();
        this.cursor = 0;
        clearSelection();
        this.textBounds.set(font.getBounds(this.displayText));
        this.textBounds.height -= font.getDescent() * 2.0f;
        font.computeGlyphAdvancesAndPositions(this.displayText, this.glyphAdvances, this.glyphPositions);
    }

    public String getText() {
        return this.text;
    }

    public void setSelection(int selectionStart, int selectionEnd) {
        if (selectionStart < 0) {
            throw new IllegalArgumentException("selectionStart must be >= 0");
        }
        if (selectionEnd < 0) {
            throw new IllegalArgumentException("selectionEnd must be >= 0");
        }
        int selectionStart2 = Math.min(this.text.length(), selectionStart);
        int selectionEnd2 = Math.min(this.text.length(), selectionEnd);
        if (selectionEnd2 == selectionStart2) {
            clearSelection();
            return;
        }
        if (selectionEnd2 < selectionStart2) {
            selectionEnd2 = selectionStart2;
            selectionStart2 = selectionEnd2;
        }
        this.hasSelection = true;
        this.selectionStart = selectionStart2;
        this.cursor = selectionEnd2;
    }

    public void selectAll() {
        setSelection(0, this.text.length());
    }

    public void clearSelection() {
        this.hasSelection = false;
    }

    public void setCursorPosition(int cursorPosition) {
        if (cursorPosition < 0) {
            throw new IllegalArgumentException("cursorPosition must be >= 0");
        }
        clearSelection();
        this.cursor = Math.min(cursorPosition, this.text.length());
    }

    public int getCursorPosition() {
        return this.cursor;
    }

    public OnscreenKeyboard getOnscreenKeyboard() {
        return this.keyboard;
    }

    public void setOnscreenKeyboard(OnscreenKeyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        return 150.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefHeight() {
        float prefHeight = this.textBounds.height;
        if (this.style.background != null) {
            return Math.max(this.style.background.getBottomHeight() + prefHeight + this.style.background.getTopHeight(), this.style.background.getMinHeight());
        }
        return prefHeight;
    }

    public void setRightAligned(boolean rightAligned) {
        this.rightAligned = rightAligned;
    }

    public void setPasswordMode(boolean passwordMode) {
        this.passwordMode = passwordMode;
    }

    public void setBlinkTime(float blinkTime) {
        this.blinkTime = blinkTime;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    class KeyRepeatTask extends Timer.Task {
        int keycode;

        KeyRepeatTask() {
        }

        @Override // com.badlogic.gdx.utils.Timer.Task, java.lang.Runnable
        public void run() {
            TextField.this.inputListener.keyDown(null, this.keycode);
        }
    }

    public interface TextFieldFilter {
        boolean acceptChar(TextField textField, char c);

        public static class DigitsOnlyFilter implements TextFieldFilter {
            @Override // com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter
            public boolean acceptChar(TextField textField, char key) {
                return Character.isDigit(key);
            }
        }
    }

    public static class DefaultOnscreenKeyboard implements OnscreenKeyboard {
        @Override // com.badlogic.gdx.scenes.scene2d.ui.TextField.OnscreenKeyboard
        public void show(boolean visible) {
            Gdx.input.setOnscreenKeyboardVisible(visible);
        }
    }

    public static class TextFieldStyle {
        public Drawable background;
        public Drawable cursor;
        public Color disabledFontColor;
        public BitmapFont font;
        public Color fontColor;
        public BitmapFont messageFont;
        public Color messageFontColor;
        public Drawable selection;

        public TextFieldStyle() {
        }

        public TextFieldStyle(BitmapFont font, Color fontColor, Drawable cursor, Drawable selection, Drawable background) {
            this.background = background;
            this.cursor = cursor;
            this.font = font;
            this.fontColor = fontColor;
            this.selection = selection;
        }

        public TextFieldStyle(TextFieldStyle style) {
            this.messageFont = style.messageFont;
            if (style.messageFontColor != null) {
                this.messageFontColor = new Color(style.messageFontColor);
            }
            this.background = style.background;
            this.cursor = style.cursor;
            this.font = style.font;
            if (style.fontColor != null) {
                this.fontColor = new Color(style.fontColor);
            }
            if (style.disabledFontColor != null) {
                this.disabledFontColor = new Color(style.disabledFontColor);
            }
            this.selection = style.selection;
        }
    }
}
