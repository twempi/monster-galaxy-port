package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class ButtonGroup {
    private final Array<Button> buttons;
    private Array<Button> checkedButtons;
    private Button lastChecked;
    private int maxCheckCount;
    private int minCheckCount;
    private boolean uncheckLast;

    public ButtonGroup() {
        this.buttons = new Array<>();
        this.checkedButtons = new Array<>(1);
        this.maxCheckCount = 1;
        this.uncheckLast = true;
        this.minCheckCount = 1;
    }

    public ButtonGroup(Button... buttons) {
        this.buttons = new Array<>();
        this.checkedButtons = new Array<>(1);
        this.maxCheckCount = 1;
        this.uncheckLast = true;
        this.minCheckCount = 0;
        add(buttons);
        this.minCheckCount = 1;
    }

    public void add(Button button) {
        if (button == null) {
            throw new IllegalArgumentException("button cannot be null.");
        }
        button.buttonGroup = null;
        boolean shouldCheck = button.isChecked() || this.buttons.size < this.minCheckCount;
        button.setChecked(false);
        button.buttonGroup = this;
        this.buttons.add(button);
        if (shouldCheck) {
            button.setChecked(true);
        }
    }

    public void add(Button... buttons) {
        if (buttons == null) {
            throw new IllegalArgumentException("buttons cannot be null.");
        }
        for (Button button : buttons) {
            add(button);
        }
    }

    public void remove(Button button) {
        if (button == null) {
            throw new IllegalArgumentException("button cannot be null.");
        }
        button.buttonGroup = null;
        this.buttons.removeValue(button, true);
    }

    public void remove(Button... buttons) {
        if (buttons == null) {
            throw new IllegalArgumentException("buttons cannot be null.");
        }
        for (Button button : buttons) {
            remove(button);
        }
    }

    public void setChecked(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        int n = this.buttons.size;
        for (int i = 0; i < n; i++) {
            Button button = this.buttons.get(i);
            if ((button instanceof TextButton) && text.equals(((TextButton) button).getText())) {
                button.setChecked(true);
                return;
            }
        }
    }

    protected boolean canCheck(Button button, boolean newState) {
        if (button.isChecked == newState) {
            return false;
        }
        if (!newState) {
            if (this.checkedButtons.size <= this.minCheckCount) {
                return false;
            }
            this.checkedButtons.removeValue(button, true);
        } else {
            if (this.maxCheckCount != -1 && this.checkedButtons.size >= this.maxCheckCount) {
                if (!this.uncheckLast) {
                    return false;
                }
                int old = this.minCheckCount;
                this.minCheckCount = 0;
                this.lastChecked.setChecked(false);
                this.minCheckCount = old;
            }
            this.checkedButtons.add(button);
            this.lastChecked = button;
        }
        return true;
    }

    public void uncheckAll() {
        int old = this.minCheckCount;
        this.minCheckCount = 0;
        int n = this.buttons.size;
        for (int i = 0; i < n; i++) {
            Button button = this.buttons.get(i);
            button.setChecked(false);
        }
        this.minCheckCount = old;
    }

    public Button getChecked() {
        if (this.checkedButtons.size > 0) {
            return this.checkedButtons.get(0);
        }
        return null;
    }

    public Array<Button> getAllChecked() {
        return this.checkedButtons;
    }

    public Array<Button> getButtons() {
        return this.buttons;
    }

    public void setMinCheckCount(int minCheckCount) {
        this.minCheckCount = minCheckCount;
    }

    public void setMaxCheckCount(int maxCheckCount) {
        this.maxCheckCount = maxCheckCount;
    }

    public void setUncheckLast(boolean uncheckLast) {
        this.uncheckLast = uncheckLast;
    }
}
