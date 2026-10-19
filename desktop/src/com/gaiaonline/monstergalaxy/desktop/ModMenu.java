package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.model.minigame.Slotmachine;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Cheat menu, toggled with Insert: edit currencies, Moga levels, give Mogas, and
 * rig Lucky Moga. Drawn on top of the game each frame. While it is open, a proxy
 * for Gdx.input hides touches and keys from the game (its screens poll Gdx.input
 * as well as using input processors).
 */
public final class ModMenu {
    private static final int MAX_LEVEL = 50;
    private static final int MAX_AMOUNT = 999999999;
    private static final Color DIM = new Color(0f, 0f, 0f, 0.55f);
    private static final Color PANEL = new Color(0.07f, 0.09f, 0.17f, 0.95f);
    private static final Color ACCENT = new Color(1f, 0.78f, 0.25f, 1f);
    private static final Color HIGHLIGHT = new Color(0.25f, 0.45f, 0.85f, 0.45f);
    private static final Color TEXT = new Color(0.93f, 0.95f, 1f, 1f);
    private static final Color MUTED = new Color(0.6f, 0.65f, 0.75f, 1f);
    private static final Color ON = new Color(0.45f, 0.95f, 0.45f, 1f);

    private static ModMenu instance;

    private final Input input; // the real backend input
    private final Preferences prefs;
    private InputProcessor gameProcessor;
    private boolean open;

    private SpriteBatch batch;
    private BitmapFont font;
    private Texture white;

    private final List<Row> rows = new ArrayList<Row>();
    private int selected;
    private String editBuffer; // digits typed into a number row, or null
    private String status;
    private float statusTime;
    private float panelX, panelWidth, rowHeight, valueCenterX, arrowOffset;
    private final float[] rowBottoms;

    // Lucky Moga
    private boolean alwaysBest, freeSpins, unlimitedEnergy;
    private Slotmachine slotMachine;
    private int slotCost;
    private Random slotRandom;
    private static final Random PICK_FIRST_REEL = new Random() {
        @Override
        public double nextDouble() {
            return 0.0; // Slotmachine.getRandomReel then picks reel 0, the "Best" prizes
        }
    };

    // Mogas
    private int mogaIndex;
    private int giveIndex;
    private List<MogaType> giveable = new ArrayList<MogaType>();

    private ModMenu(Input input) {
        this.input = input;
        prefs = Gdx.app.getPreferences("mod-menu");
        alwaysBest = prefs.getBoolean("alwaysBest", false);
        freeSpins = prefs.getBoolean("freeSpins", false);
        unlimitedEnergy = prefs.getBoolean("unlimitedEnergy", false);
        buildRows();
        rowBottoms = new float[rows.size()];
        selected = nextSelectable(-1, 1);
    }

    /** Call after the game's create(): takes over Gdx.input. */
    public static void install() {
        Input real = Gdx.input;
        instance = new ModMenu(real);
        instance.gameProcessor = real.getInputProcessor();
        real.setInputProcessor(instance.new Router());
        Gdx.input = instance.new GameInput();
    }

    /** Call after the game's render(): applies cheats and draws the menu. */
    public static void render() {
        if (instance != null) {
            instance.frame();
        }
    }

    // ---------------------------------------------------------------- rows

    private abstract class Row {
        final String label;

        Row(String label) {
            this.label = label;
        }

        boolean selectable() {
            return trainer() != null;
        }

        abstract String value();

        void adjust(int delta) {
        }

        void activate() {
        }

        boolean numeric() {
            return false;
        }

        /** Has < > arrows (stepped with Left/Right or a click). */
        boolean arrows() {
            return false;
        }

        void set(int value) {
        }
    }

    private final class Header extends Row {
        Header(String label) {
            super(label);
        }

        @Override
        boolean selectable() {
            return false;
        }

        @Override
        String value() {
            return "";
        }
    }

    private abstract class NumberRow extends Row {
        final int min, max;

        NumberRow(String label, int min, int max) {
            super(label);
            this.min = min;
            this.max = max;
        }

        abstract int get();

        abstract void put(int value);

        @Override
        boolean selectable() {
            return super.selectable() && enabled();
        }

        boolean enabled() {
            return true;
        }

        @Override
        String value() {
            return String.valueOf(get());
        }

        @Override
        boolean numeric() {
            return true;
        }

        @Override
        boolean arrows() {
            return true;
        }

        @Override
        void adjust(int delta) {
            set((int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, (long) get() + delta)));
        }

        @Override
        void set(int value) {
            put(Math.max(min, Math.min(max, value)));
            Game.save();
        }
    }

    private abstract class Toggle extends Row {
        Toggle(String label) {
            super(label);
        }

        abstract boolean on();

        abstract void flip();

        @Override
        String value() {
            return on() ? "ON" : "OFF";
        }

        @Override
        void adjust(int delta) {
            activate();
        }

        @Override
        void activate() {
            flip();
            prefs.putBoolean("alwaysBest", alwaysBest);
            prefs.putBoolean("freeSpins", freeSpins);
            prefs.putBoolean("unlimitedEnergy", unlimitedEnergy);
            prefs.flush();
        }
    }

    private abstract class Action extends Row {
        Action(String label) {
            super(label);
        }

        @Override
        String value() {
            return "[ Enter ]";
        }

        abstract void run(Trainer trainer);

        @Override
        void activate() {
            run(trainer());
            Game.save();
        }
    }

    private void buildRows() {
        rows.add(new Header("Currencies"));
        rows.add(new NumberRow("Moga Cash", 0, MAX_AMOUNT) {
            int get() {
                return trainer().getCoins();
            }

            void put(int value) {
                trainer().setCoins(value);
            }
        });
        rows.add(new NumberRow("Starseeds", 0, MAX_AMOUNT) {
            int get() {
                return trainer().getStarSeeds();
            }

            void put(int value) {
                trainer().setStarSeeds(value);
            }
        });
        rows.add(new NumberRow("Blue Coffee", 0, MAX_AMOUNT) {
            int get() {
                return trainer().getBlueCoffeeCups();
            }

            void put(int value) {
                trainer().setBlueCoffeeCups(value);
            }
        });
        rows.add(new Action("Refill energy") {
            void run(Trainer trainer) {
                trainer.rechargeEnergy();
                flash("Energy refilled");
            }
        });
        rows.add(new Toggle("Unlimited energy") {
            boolean on() {
                return unlimitedEnergy;
            }

            void flip() {
                unlimitedEnergy = !unlimitedEnergy;
            }
        });

        rows.add(new Header("Lucky Moga"));
        rows.add(new Toggle("Always win the Best prize") {
            boolean on() {
                return alwaysBest;
            }

            void flip() {
                alwaysBest = !alwaysBest;
            }
        });
        rows.add(new Toggle("Free spins") {
            boolean on() {
                return freeSpins;
            }

            void flip() {
                freeSpins = !freeSpins;
                if (freeSpins) {
                    flash("Free from the next visit to Lucky Moga");
                }
            }
        });

        rows.add(new Header("Mogas"));
        rows.add(new Row("Moga") {
            @Override
            boolean selectable() {
                return super.selectable() && !trainer().getMogas().isEmpty();
            }

            String value() {
                Moga moga = selectedMoga();
                return moga == null ? "none" : moga.getName() + "  (" + (mogaIndex + 1) + "/"
                        + trainer().getMogas().size() + ")";
            }

            @Override
            boolean arrows() {
                return true;
            }

            @Override
            void adjust(int delta) {
                int n = trainer().getMogas().size();
                mogaIndex = ((mogaIndex + Integer.signum(delta)) % n + n) % n;
            }
        });
        rows.add(new NumberRow("    Level", 1, MAX_LEVEL) {
            @Override
            boolean enabled() {
                return selectedMoga() != null;
            }

            @Override
            String value() {
                return selectedMoga() == null ? "-" : super.value();
            }

            int get() {
                return selectedMoga().getLevel();
            }

            void put(int value) {
                setLevel(selectedMoga(), value);
            }
        });
        rows.add(new Action("Set all Mogas to level 50") {
            void run(Trainer trainer) {
                for (Moga moga : trainer.getMogas()) {
                    setLevel(moga, MAX_LEVEL);
                }
                flash("All Mogas are level 50");
            }
        });
        rows.add(new Action("Heal and wake all Mogas") {
            void run(Trainer trainer) {
                for (Moga moga : trainer.getMogas()) {
                    moga.interruptNap();
                    moga.fullyRechargeHealth();
                    moga.fullyRechargeZodiac();
                }
                flash("All Mogas healed");
            }
        });
        rows.add(new Row("Give Moga") {
            @Override
            boolean selectable() {
                return super.selectable() && !giveable.isEmpty();
            }

            String value() {
                return giveable.isEmpty() ? "you have them all" : giveable.get(giveIndex).getLocalizedName();
            }

            @Override
            boolean arrows() {
                return true;
            }

            @Override
            void adjust(int delta) {
                int n = giveable.size();
                giveIndex = ((giveIndex + delta) % n + n) % n;
            }

            @Override
            void activate() {
                MogaType type = giveable.get(giveIndex);
                Trainer trainer = trainer();
                Moga moga = Moga.newMogaFromType(type);
                moga.setTrainerMogasMaxLevel();
                moga.fullyRechargeHealth();
                moga.fullyRechargeZodiac();
                trainer.getMogas().add(moga);
                if (trainer.getTeam().getMogas().size() < 3) {
                    trainer.getTeam().addMoga(moga);
                }
                Game.save();
                flash("Added " + type.getLocalizedName());
                refreshGiveable();
            }
        });
    }

    private static Trainer trainer() {
        return Game.getTrainer();
    }

    private Moga selectedMoga() {
        Trainer trainer = trainer();
        if (trainer == null || trainer.getMogas().isEmpty()) {
            return null;
        }
        mogaIndex = Math.min(mogaIndex, trainer.getMogas().size() - 1);
        return trainer.getMogas().get(mogaIndex);
    }

    /** Sets a Moga's experience to the start of the given level and heals it (max HP depends on level). */
    private static void setLevel(Moga moga, int level) {
        int xp = Moga.getCumulativeExperienceForLevel(level);
        moga.setExperience(xp);
        // getLevel inverts the XP curve in floats; nudge past rounding at the boundary.
        for (int i = 0; i < 100000 && moga.getLevel() < level; i++) {
            moga.setExperience(++xp);
        }
        moga.fullyRechargeHealth();
    }

    private void refreshGiveable() {
        giveable = new ArrayList<MogaType>();
        Trainer trainer = trainer();
        if (trainer == null) {
            return;
        }
        for (MogaType.SubType subType : MogaType.SubType.values()) {
            for (MogaType type : Game.getStorage().getMogaTypesBySubType(subType)) {
                if (!trainer.hasMogasOfType(type)) {
                    giveable.add(type);
                }
            }
        }
        Collections.sort(giveable, new Comparator<MogaType>() {
            public int compare(MogaType a, MogaType b) {
                return a.getLocalizedName().compareTo(b.getLocalizedName());
            }
        });
        giveIndex = Math.min(giveIndex, Math.max(0, giveable.size() - 1));
    }

    // ---------------------------------------------------------------- frame

    private void frame() {
        applyCheats();
        if (open) {
            draw();
        }
    }

    private void applyCheats() {
        Trainer trainer = trainer();
        if (trainer != null && unlimitedEnergy) {
            trainer.rechargeEnergy();
        }
        // Only touch the slot machine once the game has loaded it: creating it early
        // would roll its cards at the wrong time.
        Slotmachine machine = Game.getStorage() instanceof DesktopStorageHelper
                ? ((DesktopStorageHelper) Game.getStorage()).loadedSlotMachine()
                : null;
        if (machine == null) {
            return;
        }
        try {
            Field random = Slotmachine.class.getDeclaredField("random");
            random.setAccessible(true);
            if (machine != slotMachine) {
                slotMachine = machine;
                slotCost = machine.getCost();
                slotRandom = (Random) random.get(machine);
            }
            random.set(machine, alwaysBest ? PICK_FIRST_REEL : slotRandom);
            machine.setCost(freeSpins ? 0 : slotCost);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setOpen(boolean open) {
        this.open = open;
        editBuffer = null;
        if (open) {
            refreshGiveable();
            if (!rows.get(selected).selectable()) {
                selected = nextSelectable(selected, 1);
            }
        }
    }

    private void flash(String message) {
        status = message;
        statusTime = 3f;
    }

    private void draw() {
        if (batch == null) {
            batch = new SpriteBatch();
            font = new BitmapFont(Gdx.files.internal("data/fonts/VAGRundsRegular.fnt"), Fonts.regular.getRegion(), false);
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            white = new Texture(pixmap);
            pixmap.dispose();
        }
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float u = h / 720f;
        statusTime -= Gdx.graphics.getDeltaTime();

        rowHeight = 31 * u;
        panelWidth = 660 * u;
        float top = 78 * u, bottom = 62 * u;
        float panelHeight = top + rows.size() * rowHeight + bottom;
        panelX = (w - panelWidth) / 2;
        float panelY = (h - panelHeight) / 2;
        valueCenterX = panelX + panelWidth - 170 * u;
        arrowOffset = 140 * u;

        batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
        batch.begin();
        batch.enableBlending();
        rect(0, 0, w, h, DIM);
        rect(panelX - 2 * u, panelY - 2 * u, panelWidth + 4 * u, panelHeight + 4 * u, ACCENT);
        rect(panelX, panelY, panelWidth, panelHeight, PANEL);

        text("MOD MENU", w / 2, panelY + panelHeight - 18 * u, 0.95f * u, ACCENT, true);
        text("Insert: close     Up/Down: select     Left/Right: change (Shift x10, Ctrl x100)",
                w / 2, panelY + panelHeight - 54 * u, 0.42f * u, MUTED, true);

        boolean noGame = trainer() == null;
        float y = panelY + panelHeight - top;
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            y -= rowHeight;
            rowBottoms[i] = y;
            float textY = y + rowHeight / 2 + 9 * u;
            if (row instanceof Header) {
                text(row.label.toUpperCase(), panelX + 20 * u, textY, 0.55f * u, ACCENT, false);
                rect(panelX + 20 * u, y + 3 * u, panelWidth - 40 * u, 1.5f * u, ACCENT);
                continue;
            }
            boolean enabled = row.selectable();
            if (i == selected && open && enabled) {
                rect(panelX + 10 * u, y + 2 * u, panelWidth - 20 * u, rowHeight - 4 * u, HIGHLIGHT);
            }
            text(row.label, panelX + 34 * u, textY, 0.6f * u, enabled ? TEXT : MUTED, false);
            if (noGame) {
                continue;
            }
            String value = row.value();
            Color color = enabled ? TEXT : MUTED;
            if (row instanceof Toggle) {
                color = ((Toggle) row).on() ? ON : MUTED;
            } else if (row instanceof Action) {
                color = i == selected ? ACCENT : MUTED;
            }
            if (row.numeric() && i == selected && editBuffer != null) {
                value = editBuffer + "_";
                color = ACCENT;
            }
            text(value, valueCenterX, textY, 0.6f * u, color, true);
            if (row.arrows() && enabled) {
                Color arrowColor = i == selected ? ACCENT : MUTED;
                text("<", valueCenterX - arrowOffset, textY, 0.7f * u, arrowColor, true);
                text(">", valueCenterX + arrowOffset, textY, 0.7f * u, arrowColor, true);
            }
        }

        String footer;
        if (noGame) {
            footer = "Start or continue a game first.";
        } else if (statusTime > 0 && status != null) {
            footer = status;
        } else if (rows.get(selected).numeric()) {
            footer = editBuffer != null ? "Enter: apply     Esc: cancel" : "Type a number and press Enter to set it exactly.";
        } else {
            footer = "Enter or click to use.";
        }
        text(footer, w / 2, panelY + 40 * u, 0.5f * u, statusTime > 0 ? ON : MUTED, true);
        batch.end();
    }

    private void rect(float x, float y, float width, float height, Color color) {
        batch.setColor(color);
        batch.draw(white, x, y, width, height);
        batch.setColor(Color.WHITE);
    }

    private void text(String s, float x, float y, float scale, Color color, boolean centered) {
        font.setScale(scale);
        font.setColor(color);
        if (centered) {
            x -= font.getBounds(s).width / 2;
        }
        font.draw(batch, s, x, y);
    }

    // ---------------------------------------------------------------- input

    private int nextSelectable(int from, int direction) {
        int n = rows.size();
        for (int step = 1; step <= n; step++) {
            int i = ((from + direction * step) % n + n) % n;
            if (rows.get(i).selectable()) {
                return i;
            }
        }
        return Math.max(0, from);
    }

    private int step() {
        if (input.isKeyPressed(Keys.CONTROL_LEFT) || input.isKeyPressed(Keys.CONTROL_RIGHT)) {
            return 100;
        }
        if (input.isKeyPressed(Keys.SHIFT_LEFT) || input.isKeyPressed(Keys.SHIFT_RIGHT)) {
            return 10;
        }
        return 1;
    }

    private void menuKeyDown(int key) {
        Row row = rows.get(selected);
        boolean usable = row.selectable();
        if (editBuffer != null) {
            if (key == Keys.ENTER) {
                if (editBuffer.length() > 0) {
                    row.set((int) Math.min(Integer.MAX_VALUE, Long.parseLong(editBuffer)));
                }
                editBuffer = null;
            } else if (key == Keys.ESCAPE) {
                editBuffer = null;
            } else if (key == Keys.DEL || key == Keys.FORWARD_DEL) {
                editBuffer = editBuffer.substring(0, Math.max(0, editBuffer.length() - 1));
            } else if (key == Keys.UP || key == Keys.DOWN) {
                editBuffer = null;
                menuKeyDown(key);
            }
            return;
        }
        switch (key) {
        case Keys.ESCAPE:
            setOpen(false);
            break;
        case Keys.UP:
            selected = nextSelectable(selected, -1);
            break;
        case Keys.DOWN:
            selected = nextSelectable(selected, 1);
            break;
        case Keys.LEFT:
        case Keys.RIGHT:
            if (usable) {
                row.adjust(key == Keys.LEFT ? -step() : step());
            }
            break;
        case Keys.ENTER:
        case Keys.SPACE:
            if (usable) {
                if (row.numeric()) {
                    editBuffer = "";
                } else {
                    row.activate();
                }
            }
            break;
        default:
            break;
        }
    }

    private void menuKeyTyped(char c) {
        Row row = rows.get(selected);
        if (c >= '0' && c <= '9' && row.numeric() && row.selectable()) {
            if (editBuffer == null) {
                editBuffer = "";
            }
            if (editBuffer.length() < 9) {
                editBuffer = editBuffer.equals("0") ? String.valueOf(c) : editBuffer + c;
            }
        }
    }

    /** x, y in logical screen pixels, y from the top. */
    private void menuTouchDown(int x, int screenY) {
        float y = Gdx.graphics.getHeight() - screenY;
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            if (y < rowBottoms[i] || y >= rowBottoms[i] + rowHeight || x < panelX || x > panelX + panelWidth) {
                continue;
            }
            if (!row.selectable()) {
                return;
            }
            if (selected != i) {
                editBuffer = null;
            }
            selected = i;
            float hit = arrowOffset * 0.25f;
            boolean leftArrow = row.arrows() && Math.abs(x - (valueCenterX - arrowOffset)) < hit;
            boolean rightArrow = row.arrows() && Math.abs(x - (valueCenterX + arrowOffset)) < hit;
            if (leftArrow || rightArrow) {
                editBuffer = null;
                row.adjust(leftArrow ? -step() : step());
            } else if (row.numeric()) {
                editBuffer = ""; // click on a number: type a new one
            } else {
                row.activate();
            }
            return;
        }
    }

    /** Sits in the backend; sends events to the menu while it is open, else to the game. */
    private final class Router implements InputProcessor {
        public boolean keyDown(int keycode) {
            if (keycode == Keys.INSERT) {
                setOpen(!open);
                return true;
            }
            if (open) {
                menuKeyDown(keycode);
                return true;
            }
            if (keycode == Keys.ESCAPE) {
                Game.backKeyPressed(); // Escape is the Android back button
            }
            return gameProcessor != null && gameProcessor.keyDown(keycode);
        }

        public boolean keyUp(int keycode) {
            return !open && gameProcessor != null && gameProcessor.keyUp(keycode);
        }

        public boolean keyTyped(char character) {
            if (open) {
                menuKeyTyped(character);
                return true;
            }
            return gameProcessor != null && gameProcessor.keyTyped(character);
        }

        public boolean touchDown(int x, int y, int pointer, int button) {
            if (open) {
                menuTouchDown(x, y);
                return true;
            }
            return gameProcessor != null && gameProcessor.touchDown(x, y, pointer, button);
        }

        public boolean touchUp(int x, int y, int pointer, int button) {
            // Always let the game see releases, so nothing stays pressed after the menu opens.
            return gameProcessor != null && gameProcessor.touchUp(x, y, pointer, button);
        }

        public boolean touchDragged(int x, int y, int pointer) {
            return !open && gameProcessor != null && gameProcessor.touchDragged(x, y, pointer);
        }

        public boolean mouseMoved(int x, int y) {
            return !open && gameProcessor != null && gameProcessor.mouseMoved(x, y);
        }

        public boolean scrolled(int amount) {
            if (open) {
                Row row = rows.get(selected);
                if (row.selectable() && !(row instanceof Action)) {
                    editBuffer = null;
                    row.adjust(-amount * step());
                }
                return true;
            }
            return gameProcessor != null && gameProcessor.scrolled(amount);
        }
    }

    /** What the game sees as Gdx.input: the real input, minus touches and keys while the menu is open. */
    private final class GameInput implements Input {
        public boolean isTouched() {
            return !open && input.isTouched();
        }

        public boolean isTouched(int pointer) {
            return !open && input.isTouched(pointer);
        }

        public boolean justTouched() {
            return !open && input.justTouched();
        }

        public boolean isButtonPressed(int button) {
            return !open && input.isButtonPressed(button);
        }

        public boolean isKeyPressed(int key) {
            return !open && input.isKeyPressed(key);
        }

        public void setInputProcessor(InputProcessor processor) {
            gameProcessor = processor;
        }

        public InputProcessor getInputProcessor() {
            return gameProcessor;
        }

        public float getAccelerometerX() {
            return input.getAccelerometerX();
        }

        public float getAccelerometerY() {
            return input.getAccelerometerY();
        }

        public float getAccelerometerZ() {
            return input.getAccelerometerZ();
        }

        public float getAzimuth() {
            return input.getAzimuth();
        }

        public float getPitch() {
            return input.getPitch();
        }

        public float getRoll() {
            return input.getRoll();
        }

        public void getRotationMatrix(float[] matrix) {
            input.getRotationMatrix(matrix);
        }

        public int getRotation() {
            return input.getRotation();
        }

        public Orientation getNativeOrientation() {
            return input.getNativeOrientation();
        }

        public long getCurrentEventTime() {
            return input.getCurrentEventTime();
        }

        public int getX() {
            return input.getX();
        }

        public int getX(int pointer) {
            return input.getX(pointer);
        }

        public int getY() {
            return input.getY();
        }

        public int getY(int pointer) {
            return input.getY(pointer);
        }

        public int getDeltaX() {
            return input.getDeltaX();
        }

        public int getDeltaX(int pointer) {
            return input.getDeltaX(pointer);
        }

        public int getDeltaY() {
            return input.getDeltaY();
        }

        public int getDeltaY(int pointer) {
            return input.getDeltaY(pointer);
        }

        public void getTextInput(TextInputListener listener, String title, String text) {
            input.getTextInput(listener, title, text);
        }

        public void getPlaceholderTextInput(TextInputListener listener, String title, String placeholder) {
            input.getPlaceholderTextInput(listener, title, placeholder);
        }

        public void setOnscreenKeyboardVisible(boolean visible) {
            input.setOnscreenKeyboardVisible(visible);
        }

        public void vibrate(int milliseconds) {
            input.vibrate(milliseconds);
        }

        public void vibrate(long[] pattern, int repeat) {
            input.vibrate(pattern, repeat);
        }

        public void cancelVibrate() {
            input.cancelVibrate();
        }

        public void setCatchBackKey(boolean catchBack) {
            input.setCatchBackKey(catchBack);
        }

        public void setCatchMenuKey(boolean catchMenu) {
            input.setCatchMenuKey(catchMenu);
        }

        public boolean isPeripheralAvailable(Peripheral peripheral) {
            return input.isPeripheralAvailable(peripheral);
        }

        public void setCursorCatched(boolean catched) {
            input.setCursorCatched(catched);
        }

        public boolean isCursorCatched() {
            return input.isCursorCatched();
        }

        public void setCursorPosition(int x, int y) {
            input.setCursorPosition(x, y);
        }
    }
}
