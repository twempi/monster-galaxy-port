package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Window extends Table {
    final Vector2 initial;
    boolean isModal;
    boolean isMovable;
    boolean move;
    final Rectangle scissors;
    final Stage stage;
    final WindowStyle style;
    final BitmapFont.TextBounds textBounds;
    String title;
    final Rectangle titleBounds;
    final Rectangle widgetBounds;

    public Window(String name, Stage stage, String title, int prefWidth, int prefHeight, WindowStyle style) {
        super(name);
        this.widgetBounds = new Rectangle();
        this.titleBounds = new Rectangle();
        this.textBounds = new BitmapFont.TextBounds();
        this.scissors = new Rectangle();
        this.move = false;
        this.isMovable = true;
        this.initial = new Vector2();
        this.isModal = false;
        this.stage = stage;
        this.title = title;
        this.width = prefWidth;
        this.height = prefHeight;
        this.style = style;
        NinePatch background = style.background;
        TableLayout layout = getTableLayout();
        layout.padBottom(Integer.toString(((int) background.getBottomHeight()) + 1));
        layout.padTop(Integer.toString(((int) background.getTopHeight()) + 1));
        layout.padLeft(Integer.toString(((int) background.getLeftWidth()) + 1));
        layout.padRight(Integer.toString(((int) background.getRightWidth()) + 1));
    }

    private void calculateBoundsAndScissors(Matrix4 transform) {
        NinePatch background = this.style.background;
        BitmapFont titleFont = this.style.titleFont;
        this.widgetBounds.x = background.getLeftWidth();
        this.widgetBounds.y = background.getBottomHeight();
        this.widgetBounds.width = (this.width - background.getLeftWidth()) - background.getRightWidth();
        this.widgetBounds.height = (this.height - background.getTopHeight()) - background.getBottomHeight();
        ScissorStack.calculateScissors(this.stage.getCamera(), transform, this.widgetBounds, this.scissors);
        this.titleBounds.x = 0.0f;
        this.titleBounds.y = this.height - background.getTopHeight();
        this.titleBounds.width = this.width;
        this.titleBounds.height = background.getTopHeight();
        this.textBounds.set(titleFont.getBounds(this.title));
        this.textBounds.height -= titleFont.getDescent();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        NinePatch backgroundPatch = this.style.background;
        BitmapFont titleFont = this.style.titleFont;
        Color titleFontColor = this.style.titleFontColor;
        setupTransform(batch);
        layout();
        calculateBoundsAndScissors(batch.getTransformMatrix());
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        backgroundPatch.draw(batch, 0.0f, 0.0f, this.width, this.height);
        float textY = (this.height - ((int) (backgroundPatch.getTopHeight() / 2.0f))) + ((int) (this.textBounds.height / 2.0f));
        titleFont.setColor(titleFontColor.r, titleFontColor.g, titleFontColor.b, titleFontColor.a * parentAlpha);
        titleFont.drawMultiLine(batch, this.title, (int) (this.width / 2.0f), textY, 0.0f, BitmapFont.HAlignment.CENTER);
        batch.flush();
        ScissorStack.pushScissors(this.scissors);
        super.drawChildren(batch, parentAlpha);
        ScissorStack.popScissors();
        resetTransform(batch);
    }

    public static class WindowStyle {
        public final NinePatch background;
        public final BitmapFont titleFont;
        public final Color titleFontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

        public WindowStyle(BitmapFont titleFont, Color titleFontColor, NinePatch backgroundPatch) {
            this.background = backgroundPatch;
            this.titleFont = titleFont;
            this.titleFontColor.set(titleFontColor);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (hit(x, y) != null) {
            if (this.parent.getActors().size() > 1) {
                this.parent.swapActor(this, this.parent.getActors().get(this.parent.getActors().size() - 1));
            }
            if (this.titleBounds.contains(x, y)) {
                focus(this, 0);
                if (this.isMovable) {
                    this.move = true;
                }
                this.initial.set(x, y);
            } else if (!super.touchDown(x, y, pointer)) {
                focus(this, 0);
            }
            return true;
        }
        if (this.isModal && this.parent.getActors().size() > 1) {
            this.parent.swapActor(this, this.parent.getActors().get(this.parent.getActors().size() - 1));
        }
        return this.isModal;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        if (pointer != 0) {
            return false;
        }
        if (this.parent.focusedActor[0] == this) {
            focus(null, 0);
        }
        this.move = false;
        return super.touchUp(x, y, pointer) || this.isModal;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        if (this.move) {
            this.x += x - this.initial.x;
            this.y += y - this.initial.y;
            return true;
        }
        if (this.parent.focusedActor[0] != this) {
            return super.touchDragged(x, y, pointer) || this.isModal;
        }
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if ((x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) && !this.isModal) {
            return null;
        }
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public boolean isMovable() {
        return this.isMovable;
    }

    public void setModal(boolean isModal) {
        this.isModal = isModal;
    }

    public boolean isModal() {
        return this.isModal;
    }
}
