package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;

/* JADX INFO: loaded from: classes.dex */
public class Dialog extends Window {
    public static float fadeDuration = 0.4f;
    Table buttonTable;
    Table contentTable;
    private Skin skin;
    ObjectMap<Actor, Object> values;

    public Dialog(String title, Skin skin) {
        super(title, (Window.WindowStyle) skin.get(Window.WindowStyle.class));
        this.values = new ObjectMap<>();
        this.skin = skin;
        initialize();
    }

    public Dialog(String title, Skin skin, String windowStyleName) {
        super(title, (Window.WindowStyle) skin.get(windowStyleName, Window.WindowStyle.class));
        this.values = new ObjectMap<>();
        this.skin = skin;
        initialize();
    }

    public Dialog(String title, Window.WindowStyle windowStyle) {
        super(title, windowStyle);
        this.values = new ObjectMap<>();
        initialize();
    }

    private void initialize() {
        setModal(true);
        defaults().space(6.0f);
        Table table = new Table(this.skin);
        this.contentTable = table;
        add(table).expand();
        row();
        Table table2 = new Table(this.skin);
        this.buttonTable = table2;
        add(table2);
        this.contentTable.defaults().space(6.0f);
        this.buttonTable.defaults().space(6.0f);
        this.buttonTable.addListener(new ChangeListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Dialog.1
            @Override // com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                while (actor.getParent() != Dialog.this.buttonTable) {
                    actor = actor.getParent();
                }
                Dialog.this.result(Dialog.this.values.get(actor));
                Dialog.this.hide();
            }
        });
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Window, com.badlogic.gdx.scenes.scene2d.ui.Table, com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup, com.badlogic.gdx.scenes.scene2d.Group, com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        Stage stage = getStage();
        if (stage.getKeyboardFocus() == null) {
            stage.setKeyboardFocus(this);
        }
        super.draw(batch, parentAlpha);
    }

    public Table getContentTable() {
        return this.contentTable;
    }

    public Table getButtonTable() {
        return this.buttonTable;
    }

    public Dialog text(String text) {
        if (this.skin == null) {
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        }
        return text(text, (Label.LabelStyle) this.skin.get(Label.LabelStyle.class));
    }

    public Dialog text(String text, Label.LabelStyle labelStyle) {
        this.contentTable.add(new Label(text, labelStyle));
        return this;
    }

    public Dialog button(String text) {
        return button(text, null);
    }

    public Dialog button(String text, Object object) {
        if (this.skin == null) {
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        }
        return button(text, object, (TextButton.TextButtonStyle) this.skin.get(TextButton.TextButtonStyle.class));
    }

    public Dialog button(String text, Object object, TextButton.TextButtonStyle buttonStyle) {
        TextButton button = new TextButton(text, buttonStyle);
        this.buttonTable.add(button);
        setObject(button, object);
        return this;
    }

    public Dialog show(Stage stage) {
        stage.setKeyboardFocus(this);
        stage.setScrollFocus(this);
        pack();
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2.0f), Math.round((stage.getHeight() - getHeight()) / 2.0f));
        stage.addActor(this);
        if (fadeDuration > 0.0f) {
            getColor().a = 0.0f;
            addAction(Actions.fadeIn(fadeDuration, Interpolation.fade));
        }
        return this;
    }

    public void hide() {
        addAction(Actions.sequence(Actions.fadeOut(fadeDuration, Interpolation.fade), Actions.removeActor()));
    }

    public void setObject(Actor actor, Object object) {
        this.values.put(actor, object);
    }

    public Dialog key(final int keycode, final Object object) {
        addListener(new InputListener() { // from class: com.badlogic.gdx.scenes.scene2d.ui.Dialog.2
            @Override // com.badlogic.gdx.scenes.scene2d.InputListener
            public boolean keyDown(InputEvent event, int keycode2) {
                if (keycode == keycode2) {
                    Dialog.this.result(object);
                    Dialog.this.hide();
                    return false;
                }
                return false;
            }
        });
        return this;
    }

    protected void result(Object object) {
    }
}
