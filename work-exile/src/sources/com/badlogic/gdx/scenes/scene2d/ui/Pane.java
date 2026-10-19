package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Pane extends Table {
    final Rectangle scissors;
    final Stage stage;
    final PaneStyle style;
    final Rectangle widgetBounds;

    public Pane(String name, Stage stage, int prefWidth, int prefHeight, PaneStyle style) {
        super(name);
        this.widgetBounds = new Rectangle();
        this.scissors = new Rectangle();
        this.stage = stage;
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

    private void calculateScissors(Matrix4 transform) {
        NinePatch background = this.style.background;
        this.widgetBounds.x = background.getLeftWidth();
        this.widgetBounds.y = background.getBottomHeight();
        this.widgetBounds.width = (this.width - background.getLeftWidth()) - background.getRightWidth();
        this.widgetBounds.height = (this.height - background.getTopHeight()) - background.getBottomHeight();
        ScissorStack.calculateScissors(this.stage.getCamera(), transform, this.widgetBounds, this.scissors);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        NinePatch background = this.style.background;
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        background.draw(batch, this.x, this.y, this.width, this.height);
        setupTransform(batch);
        layout();
        calculateScissors(batch.getTransformMatrix());
        ScissorStack.pushScissors(this.scissors);
        super.drawChildren(batch, parentAlpha);
        resetTransform(batch);
        ScissorStack.popScissors();
    }

    public static class PaneStyle {
        public final NinePatch background;

        public PaneStyle(NinePatch backgroundPatch) {
            this.background = backgroundPatch;
        }
    }
}
