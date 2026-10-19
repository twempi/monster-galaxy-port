package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/* JADX INFO: loaded from: classes.dex */
public class Window extends Table {
    final Vector2 dragOffset;
    boolean dragging;
    boolean isModal;
    boolean isMovable;
    boolean keepWithinParent;
    private WindowStyle style;
    private String title;
    private int titleAlignment;
    private BitmapFontCache titleCache;

    public Window(String title, Skin skin) {
        this(title, (WindowStyle) skin.get(WindowStyle.class));
        setSkin(skin);
    }

    public Window(String title, Skin skin, String styleName) {
        this(title, (WindowStyle) skin.get(styleName, WindowStyle.class));
        setSkin(skin);
    }

    public Window(String title, WindowStyle style) {
        this.isMovable = true;
        this.dragOffset = new Vector2();
        this.titleAlignment = 1;
        this.keepWithinParent = true;
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null.");
        }
        this.title = title;
        setTouchable(Touchable.enabled);
        setClip(true);
        setStyle(style);
        setWidth(150.0f);
        setHeight(150.0f);
        setTitle(title);
        addCaptureListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Window.1
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Window.this.toFront();
                return false;
            }
        });
        addListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Window.2
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == 0) {
                    Window.this.dragging = Window.this.isMovable && Window.this.getHeight() - y <= Window.this.getPadTop() && y < Window.this.getHeight() && x > 0.0f && x < Window.this.getWidth();
                    Window.this.dragOffset.set(x, y);
                }
                return Window.this.dragging || Window.this.isModal;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Window.this.dragging) {
                    Window.this.translate(x - Window.this.dragOffset.x, y - Window.this.dragOffset.y);
                }
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean mouseMoved(InputEvent event, float x, float y) {
                return Window.this.isModal;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                return Window.this.isModal;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyDown(InputEvent event, int keycode) {
                return Window.this.isModal;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyUp(InputEvent event, int keycode) {
                return Window.this.isModal;
            }

            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyTyped(InputEvent event, char character) {
                return Window.this.isModal;
            }
        });
    }

    public void setStyle(WindowStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null.");
        }
        this.style = style;
        setBackground(style.background);
        this.titleCache = new BitmapFontCache(style.titleFont);
        this.titleCache.setColor(style.titleFontColor);
        if (this.title != null) {
            setTitle(this.title);
        }
        invalidateHierarchy();
    }

    public WindowStyle getStyle() {
        return this.style;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        float parentWidth;
        float parentHeight;
        if (this.keepWithinParent) {
            Group parent = getParent();
            Stage stage = getStage();
            if (parent == stage.getRoot()) {
                parentWidth = stage.getWidth();
                parentHeight = stage.getHeight();
            } else {
                parentWidth = parent.getWidth();
                parentHeight = parent.getHeight();
            }
            if (getX() < 0.0f) {
                setX(0.0f);
            }
            if (getRight() > parentWidth) {
                setX(parentWidth - getWidth());
            }
            if (getY() < 0.0f) {
                setY(0.0f);
            }
            if (getTop() > parentHeight) {
                setY(parentHeight - getHeight());
            }
        }
        super.draw(batch, parentAlpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table
    protected void drawBackground(SpriteBatch batch, float parentAlpha) {
        float x;
        if (this.style.stageBackground != null) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            Stage stage = getStage();
            Vector2 position = stageToLocalCoordinates(Vector2.tmp.set(0.0f, 0.0f));
            Vector2 size = stageToLocalCoordinates(Vector2.tmp2.set(stage.getWidth(), stage.getHeight()));
            this.style.stageBackground.draw(batch, position.x + getX(), position.y + getY(), size.x + getX(), size.y + getY());
        }
        super.drawBackground(batch, parentAlpha);
        float x2 = getX();
        float y = getY() + getHeight();
        BitmapFont.TextBounds bounds = this.titleCache.getBounds();
        if ((this.titleAlignment & 8) != 0) {
            x = x2 + getPadLeft();
        } else if ((this.titleAlignment & 16) != 0) {
            x = x2 + ((getWidth() - bounds.width) - getPadRight());
        } else {
            x = x2 + ((getWidth() - bounds.width) / 2.0f);
        }
        if ((this.titleAlignment & 2) == 0) {
            if ((this.titleAlignment & 4) != 0) {
                y -= getPadTop() - bounds.height;
            } else {
                y -= (getPadTop() - bounds.height) / 2.0f;
            }
        }
        this.titleCache.setColor(Color.tmp.set(getColor()).mul(this.style.titleFontColor));
        this.titleCache.setPosition((int) x, (int) y);
        this.titleCache.draw(batch, parentAlpha);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y, boolean touchable) {
        Actor hit = super.hit(x, y, touchable);
        return (hit == null && this.isModal && (!touchable || getTouchable() == Touchable.enabled)) ? this : hit;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleCache.setMultiLineText(title, 0.0f, 0.0f);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitleAlignment(int titleAlignment) {
        this.titleAlignment = titleAlignment;
    }

    public void setMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public void setModal(boolean isModal) {
        this.isModal = isModal;
    }

    public void setKeepWithinParent(boolean keepWithinParent) {
        this.keepWithinParent = keepWithinParent;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.utils.Layout
    public float getPrefWidth() {
        return Math.max(super.getPrefWidth(), this.titleCache.getBounds().width + getPadLeft() + getPadRight());
    }

    public static class WindowStyle {
        public Drawable background;
        public Drawable stageBackground;
        public BitmapFont titleFont;
        public Color titleFontColor;

        public WindowStyle() {
            this.titleFontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        public WindowStyle(BitmapFont titleFont, Color titleFontColor, Drawable background) {
            this.titleFontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
            this.background = background;
            this.titleFont = titleFont;
            this.titleFontColor.set(titleFontColor);
        }

        public WindowStyle(WindowStyle style) {
            this.titleFontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
            this.background = style.background;
            this.titleFont = style.titleFont;
            this.titleFontColor = new Color(style.titleFontColor);
        }
    }
}
