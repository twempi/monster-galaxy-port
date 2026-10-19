package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Skin implements Disposable {
    Texture texture;
    Map<String, NinePatch> ninePatches = new HashMap();
    Map<String, TextureRegion> regions = new HashMap();
    Map<String, Color> colors = new HashMap();
    Map<String, BitmapFont> fonts = new HashMap();
    Map<String, Button.ButtonStyle> buttonStyles = new HashMap();
    Map<String, CheckBox.CheckBoxStyle> checkBoxStyles = new HashMap();
    Map<String, Slider.SliderStyle> sliderStyles = new HashMap();
    Map<String, Label.LabelStyle> labelStyles = new HashMap();
    Map<String, ToggleButton.ToggleButtonStyle> toggleButtonStyles = new HashMap();
    Map<String, List.ListStyle> listStyles = new HashMap();
    Map<String, Pane.PaneStyle> paneStyles = new HashMap();
    Map<String, ScrollPane.ScrollPaneStyle> scrollPaneStyles = new HashMap();
    Map<String, SplitPane.SplitPaneStyle> splitPaneStyles = new HashMap();
    Map<String, TextField.TextFieldStyle> textFieldStyles = new HashMap();
    Map<String, ComboBox.ComboBoxStyle> comboBoxStyles = new HashMap();
    Map<String, ImageButton.ImageButtonStyle> imageButtonStyles = new HashMap();
    Map<String, ImageToggleButton.ImageToggleButtonStyle> imageToggleButtonStyles = new HashMap();
    Map<String, Window.WindowStyle> windowStyles = new HashMap();

    public Skin(FileHandle skinFile, FileHandle textureFile) {
        this.texture = new Texture(textureFile);
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        parseSkin(skinFile);
    }

    public Skin(Texture texture) {
        this.texture = texture;
    }

    public void parseSkin(FileHandle skinFile) {
        try {
            XmlReader xml = new XmlReader();
            XmlReader.Element skin = xml.parse(skinFile);
            parseLibrary(skin, skinFile);
            parseWidgetStyles(skin.getChildByName("widgetStyles"));
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't parse skinFile", e);
        }
    }

    protected void parseWidgetStyles(XmlReader.Element styles) {
        parseButtonStyles(styles);
        parseCheckBoxStyles(styles);
        parseLabelStyles(styles);
        parseSliderStyles(styles);
        parseToggleButtonStyles(styles);
        parseListStyles(styles);
        parsePaneStyles(styles);
        parseScrollPaneStyles(styles);
        parseSplitPaneStyles(styles);
        parseTextFieldStyles(styles);
        parseComboBoxStyles(styles);
        parseWindowStyles(styles);
        parseImageButtonStyles(styles);
        parseImageToggleButtonStyles(styles);
    }

    private void error(String msg, XmlReader.Element element) {
        throw new GdxRuntimeException(msg + ", element: " + element.toString());
    }

    private void parseWindowStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("window")) {
            String name = style.getAttribute("name");
            NinePatch background = getNinePatch(style.getAttribute("background"));
            BitmapFont font = getFont(style.getAttribute("titleFont"));
            Color fontColor = getColor(style.getAttribute("titleFontColor"));
            if (name == null) {
                error("No name given for window style", style);
            }
            if (background == null) {
                error("No 'background' nine-patch given for window style", style);
            }
            if (font == null) {
                error("No 'font' given for window style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for window style", style);
            }
            this.windowStyles.put(name, new Window.WindowStyle(font, fontColor, background));
        }
    }

    private void parseComboBoxStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("combobox")) {
            String name = style.getAttribute("name");
            NinePatch background = getNinePatch(style.getAttribute("background"));
            NinePatch listBackground = getNinePatch(style.getAttribute("listBackground"));
            NinePatch listSelection = getNinePatch(style.getAttribute("listSelection"));
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColor = getColor(style.getAttribute("fontColor"));
            if (name == null) {
                error("No name given for combobox style", style);
            }
            if (background == null) {
                error("No 'background' nine-patch given for combobox style", style);
            }
            if (listBackground == null) {
                error("No 'listBackground' nine-patch given for combobox style", style);
            }
            if (listSelection == null) {
                error("No 'listSelection' nine-patch given for combobox style", style);
            }
            if (font == null) {
                error("No 'font' given for combobox style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for combobox style", style);
            }
            this.comboBoxStyles.put(name, new ComboBox.ComboBoxStyle(font, fontColor, background, listBackground, listSelection));
        }
    }

    private void parseTextFieldStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("textfield")) {
            String name = style.getAttribute("name");
            NinePatch background = getNinePatch(style.getAttribute("background"));
            NinePatch cursor = getNinePatch(style.getAttribute("cursor"));
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColor = getColor(style.getAttribute("fontColor"));
            TextureRegion selection = getRegion(style.getAttribute("selection"));
            if (name == null) {
                error("No name given for textfield style", style);
            }
            if (background == null) {
                error("No 'background' nine-patch given for textfield style", style);
            }
            if (cursor == null) {
                error("No 'cursor' nine-patch given for textfield style", style);
            }
            if (font == null) {
                error("No 'font' given for textfield style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for textfield stye", style);
            }
            if (selection == null) {
                error("No 'selection' region given for textfield style", style);
            }
            this.textFieldStyles.put(name, new TextField.TextFieldStyle(font, fontColor, cursor, selection, background));
        }
    }

    private void parseSplitPaneStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("splitpane")) {
            String name = style.getAttribute("name");
            NinePatch handle = getNinePatch(style.getAttribute("handle"));
            if (name == null) {
                error("No name given for splitpane style", style);
            }
            if (handle == null) {
                error("No 'handle' given for splitpane style", style);
            }
            this.splitPaneStyles.put(name, new SplitPane.SplitPaneStyle(handle));
        }
    }

    private void parseScrollPaneStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("scrollpane")) {
            String name = style.getAttribute("name");
            NinePatch background = getNinePatch(style.getAttribute("background"));
            NinePatch hScroll = getNinePatch(style.getAttribute("hScroll"));
            NinePatch hScrollKnob = getNinePatch(style.getAttribute("hScrollKnob"));
            NinePatch vScroll = getNinePatch(style.getAttribute("vScroll"));
            NinePatch vScrollKnob = getNinePatch(style.getAttribute("vScrollKnob"));
            if (name == null) {
                error("No name given for scrollpane style", style);
            }
            if (background == null) {
                error("No 'background' given for scrollpane style", style);
            }
            if (hScroll == null) {
                error("No 'hScroll' given for scrollpane style", style);
            }
            if (hScrollKnob == null) {
                error("No 'hScrollKnob' given for scrollpane style", style);
            }
            if (vScroll == null) {
                error("No 'vScroll' given for scrollpane style", style);
            }
            if (vScrollKnob == null) {
                error("No 'vScrollKnob' given for scrollpane style", style);
            }
            this.scrollPaneStyles.put(name, new ScrollPane.ScrollPaneStyle(background, hScroll, hScrollKnob, vScroll, vScrollKnob));
        }
    }

    private void parsePaneStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("pane")) {
            String name = style.getAttribute("name");
            NinePatch patch = getNinePatch(style.getAttribute("background"));
            if (name == null) {
                error("No name given for pane style", style);
            }
            if (patch == null) {
                error("No 'background' given for pane style", style);
            }
            this.paneStyles.put(name, new Pane.PaneStyle(patch));
        }
    }

    private void parseListStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("list")) {
            String name = style.getAttribute("name");
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColorUnselected = getColor(style.getAttribute("fontColorUnselected"));
            Color fontColorSelected = getColor(style.getAttribute("fontColorSelected"));
            NinePatch selectedPatch = getNinePatch(style.getAttribute("selected"));
            if (name == null) {
                error("No name given for list style", style);
            }
            if (font == null) {
                error("No font given for list style", style);
            }
            if (fontColorUnselected == null) {
                error("No 'fontColorUnselected' given for list style", style);
            }
            if (fontColorSelected == null) {
                error("No 'fontColorSelected' given for list style", style);
            }
            if (selectedPatch == null) {
                error("No 'selected' nine-patch given for list style", style);
            }
            this.listStyles.put(name, new List.ListStyle(font, fontColorSelected, fontColorUnselected, selectedPatch));
        }
    }

    private void parseImageToggleButtonStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("imagetogglebutton")) {
            String name = style.getAttribute("name");
            NinePatch down = getNinePatch(style.getAttribute("down"));
            NinePatch up = getNinePatch(style.getAttribute("up"));
            if (name == null) {
                error("No name given for togglebutton style", style);
            }
            if (down == null) {
                error("No 'down' nine-patch given for togglebutton style", style);
            }
            if (up == null) {
                error("No 'up' nine-patch given for togglebutton style", style);
            }
            this.imageToggleButtonStyles.put(name, new ImageToggleButton.ImageToggleButtonStyle(down, up));
        }
    }

    private void parseToggleButtonStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("togglebutton")) {
            String name = style.getAttribute("name");
            NinePatch down = getNinePatch(style.getAttribute("down"));
            NinePatch up = getNinePatch(style.getAttribute("up"));
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColor = getColor(style.getAttribute("fontColor"));
            if (name == null) {
                error("No name given for togglebutton style", style);
            }
            if (down == null) {
                error("No 'down' nine-patch given for togglebutton style", style);
            }
            if (up == null) {
                error("No 'up' nine-patch given for togglebutton style", style);
            }
            if (font == null) {
                error("No 'font' given for togglebutton style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for togglebutton style", style);
            }
            this.toggleButtonStyles.put(name, new ToggleButton.ToggleButtonStyle(font, fontColor, down, up));
        }
    }

    private void parseSliderStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("slider")) {
            String name = style.getAttribute("name");
            NinePatch sliderPatch = getNinePatch(style.getAttribute("slider"));
            TextureRegion knobPatch = getRegion(style.getAttribute("knob"));
            if (name == null) {
                error("No name given for slider style", style);
            }
            if (sliderPatch == null) {
                error("No 'slider' nine-patch given for slider style", style);
            }
            if (knobPatch == null) {
                error("No 'knob' region given for slider style", style);
            }
            this.sliderStyles.put(name, new Slider.SliderStyle(sliderPatch, knobPatch));
        }
    }

    private void parseCheckBoxStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("checkbox")) {
            String name = style.getAttribute("name");
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColor = getColor(style.getAttribute("fontColor"));
            TextureRegion checked = getRegion(style.getAttribute("checked"));
            TextureRegion unchecked = getRegion(style.getAttribute("unchecked"));
            if (name == null) {
                error("No name given for checkbox style", style);
            }
            if (font == null) {
                error("No 'font' given for checkbox style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for checkbox style", style);
            }
            if (checked == null) {
                error("No 'checked' region given for checkbox style", style);
            }
            if (unchecked == null) {
                error("No 'unchecked' region given for checkbox style", style);
            }
            this.checkBoxStyles.put(name, new CheckBox.CheckBoxStyle(font, fontColor, checked, unchecked));
        }
    }

    private void parseImageButtonStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("imagebutton")) {
            String name = style.getAttribute("name");
            NinePatch down = getNinePatch(style.getAttribute("down"));
            NinePatch up = getNinePatch(style.getAttribute("up"));
            if (name == null) {
                error("No name given for button style", style);
            }
            if (down == null) {
                error("No 'down' nine-patch given for button style", style);
            }
            if (up == null) {
                error("No 'up' nine-patch given for button style", style);
            }
            this.imageButtonStyles.put(name, new ImageButton.ImageButtonStyle(down, up));
        }
    }

    private void parseButtonStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("button")) {
            String name = style.getAttribute("name");
            NinePatch down = getNinePatch(style.getAttribute("down"));
            NinePatch up = getNinePatch(style.getAttribute("up"));
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColor = getColor(style.getAttribute("fontColor"));
            if (name == null) {
                error("No name given for button style", style);
            }
            if (down == null) {
                error("No 'down' nine-patch given for button style", style);
            }
            if (up == null) {
                error("No 'up' nine-patch given for button style", style);
            }
            if (font == null) {
                error("No 'font' nine-patch given for button style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for button style", style);
            }
            this.buttonStyles.put(name, new Button.ButtonStyle(font, fontColor, down, up));
        }
    }

    private void parseLabelStyles(XmlReader.Element styles) {
        for (XmlReader.Element style : styles.getChildrenByName("label")) {
            String name = style.getAttribute("name");
            BitmapFont font = getFont(style.getAttribute("font"));
            Color fontColor = getColor(style.getAttribute("fontColor"));
            if (name == null) {
                error("No name given for label style", style);
            }
            if (font == null) {
                error("No 'font' given for label style", style);
            }
            if (fontColor == null) {
                error("No 'fontColor' given for label style", style);
            }
            this.labelStyles.put(style.getAttribute("name"), new Label.LabelStyle(font, fontColor));
        }
    }

    private void parseLibrary(XmlReader.Element skin, FileHandle skinFile) {
        XmlReader.Element library = skin.getChildByName("library");
        parseColors(library);
        parseNinePatches(library);
        parseRegions(library);
        parseFonts(library, skinFile);
    }

    private void parseColors(XmlReader.Element library) {
        for (XmlReader.Element color : library.getChildrenByName("color")) {
            Color col = new Color(color.getFloatAttribute("r"), color.getFloatAttribute("g"), color.getFloatAttribute("b"), color.getFloatAttribute("a"));
            this.colors.put(color.getAttribute("name"), col);
        }
    }

    private void parseFonts(XmlReader.Element library, FileHandle skinFile) {
        for (XmlReader.Element font : library.getChildrenByName("font")) {
            String path = font.getAttribute("file");
            FileHandle file = skinFile.parent().child(path);
            if (!file.exists()) {
                file = Gdx.files.internal(path);
            }
            BitmapFont bitmapFont = new BitmapFont(file, false);
            this.fonts.put(font.getAttribute("name"), bitmapFont);
        }
    }

    private void parseRegions(XmlReader.Element library) {
        for (XmlReader.Element region : library.getChildrenByName("region")) {
            this.regions.put(region.getAttribute("name"), parseRegion(region));
        }
    }

    private void parseNinePatches(XmlReader.Element library) {
        for (XmlReader.Element ninePatch : library.getChildrenByName("ninepatch")) {
            this.ninePatches.put(ninePatch.getAttribute("name"), parseNinePatch(ninePatch));
        }
    }

    private NinePatch parseNinePatch(XmlReader.Element ninePatch) {
        TextureRegion[] regions = new TextureRegion[9];
        Array<XmlReader.Element> patches = ninePatch.getChildrenByName("region");
        if (regions.length != 9) {
            error("Nine-patch definition has to have 9 regions", ninePatch);
        }
        for (int i = 0; i < regions.length; i++) {
            regions[i] = parseRegion(patches.get(i));
        }
        return new NinePatch(regions);
    }

    private TextureRegion parseRegion(XmlReader.Element region) {
        return new TextureRegion(this.texture, region.getIntAttribute("x"), region.getIntAttribute("y"), region.getIntAttribute("width"), region.getIntAttribute("height"));
    }

    public Color getColor(String name) {
        return this.colors.get(name);
    }

    public void addColor(String name, Color color) {
        this.colors.put(name, color);
    }

    public NinePatch getNinePatch(String name) {
        return this.ninePatches.get(name);
    }

    public void addNinePatch(String name, NinePatch ninePatch) {
        this.ninePatches.put(name, ninePatch);
    }

    public TextureRegion getRegion(String name) {
        return this.regions.get(name);
    }

    public void addRegion(String name, TextureRegion region) {
        this.regions.put(name, region);
    }

    public BitmapFont getFont(String name) {
        return this.fonts.get(name);
    }

    public void addFont(String name, BitmapFont font) {
        this.fonts.put(name, font);
    }

    public Button.ButtonStyle getButtonStyle(String name) {
        return this.buttonStyles.get(name);
    }

    public void addButtonStyle(String name, Button.ButtonStyle style) {
        this.buttonStyles.put(name, style);
    }

    public ImageButton.ImageButtonStyle getImageButtonStyle(String name) {
        return this.imageButtonStyles.get(name);
    }

    public void addImageButtonStyle(String name, ImageButton.ImageButtonStyle style) {
        this.imageButtonStyles.put(name, style);
    }

    public ImageToggleButton.ImageToggleButtonStyle getImageToggleButtonStyle(String name) {
        return this.imageToggleButtonStyles.get(name);
    }

    public void addImageToggleButtonStyle(String name, ImageToggleButton.ImageToggleButtonStyle style) {
        this.imageToggleButtonStyles.put(name, style);
    }

    public CheckBox.CheckBoxStyle getCheckBoxStyle(String name) {
        return this.checkBoxStyles.get(name);
    }

    public void addCheckBoxStyle(String name, CheckBox.CheckBoxStyle style) {
        this.checkBoxStyles.put(name, style);
    }

    public ComboBox.ComboBoxStyle getComboBoxStyle(String name) {
        return this.comboBoxStyles.get(name);
    }

    public void addComboBoxStyle(String name, ComboBox.ComboBoxStyle style) {
        this.comboBoxStyles.put(name, style);
    }

    public Label.LabelStyle getLabelStyle(String name) {
        return this.labelStyles.get(name);
    }

    public void addLabelStyle(String name, Label.LabelStyle style) {
        this.labelStyles.put(name, style);
    }

    public List.ListStyle getListStyle(String name) {
        return this.listStyles.get(name);
    }

    public void addListStyle(String name, List.ListStyle style) {
        this.listStyles.put(name, style);
    }

    public Pane.PaneStyle getPaneStyle(String name) {
        return this.paneStyles.get(name);
    }

    public void addPaneStyle(String name, Pane.PaneStyle style) {
        this.paneStyles.put(name, style);
    }

    public ScrollPane.ScrollPaneStyle getScrollPaneStyle(String name) {
        return this.scrollPaneStyles.get(name);
    }

    public void addScrollPaneStyle(String name, ScrollPane.ScrollPaneStyle style) {
        this.scrollPaneStyles.put(name, style);
    }

    public Slider.SliderStyle getSliderStyle(String name) {
        return this.sliderStyles.get(name);
    }

    public void addSliderStyle(String name, Slider.SliderStyle style) {
        this.sliderStyles.put(name, style);
    }

    public SplitPane.SplitPaneStyle getSplitPaneStyle(String name) {
        return this.splitPaneStyles.get(name);
    }

    public void addSplitPaneStyle(String name, SplitPane.SplitPaneStyle style) {
        this.splitPaneStyles.put(name, style);
    }

    public TextField.TextFieldStyle getTextFieldStyle(String name) {
        return this.textFieldStyles.get(name);
    }

    public void addTextFieldStyle(String name, TextField.TextFieldStyle style) {
        this.textFieldStyles.put(name, style);
    }

    public ToggleButton.ToggleButtonStyle getToggleButtonStyle(String name) {
        return this.toggleButtonStyles.get(name);
    }

    public void addToggleButtonStyle(String name, ToggleButton.ToggleButtonStyle style) {
        this.toggleButtonStyles.put(name, style);
    }

    public Window.WindowStyle getWindowStyle(String name) {
        return this.windowStyles.get(name);
    }

    public void addWindowStyle(String name, Window.WindowStyle style) {
        this.windowStyles.put(name, style);
    }

    public ImageButton newImageButton(String name, TextureRegion image) {
        return newImageButton(name, image, "default");
    }

    public ImageButton newImageButton(String name, TextureRegion image, String style) {
        return new ImageButton(name, image, this.imageButtonStyles.get(style));
    }

    public ImageToggleButton newImageToggleButton(String name, TextureRegion image) {
        return newImageToggleButton(name, image, "default");
    }

    public ImageToggleButton newImageToggleButton(String name, TextureRegion image, String style) {
        return new ImageToggleButton(name, image, this.imageToggleButtonStyles.get(style));
    }

    public Button newButton(String name, String label) {
        return newButton(name, label, "default");
    }

    public Button newButton(String name, String label, String style) {
        return new Button(name, label, this.buttonStyles.get(style));
    }

    public ToggleButton newToggleButton(String name, String label) {
        return newToggleButton(name, label, "default");
    }

    public ToggleButton newToggleButton(String name, String label, String style) {
        return new ToggleButton(name, label, this.toggleButtonStyles.get(style));
    }

    public CheckBox newCheckBox(String name, String label) {
        return new CheckBox(name, label, this.checkBoxStyles.get("default"));
    }

    public CheckBox newCheckBox(String name, String label, String style) {
        return new CheckBox(name, label, this.checkBoxStyles.get(style));
    }

    public Label newLabel(String name, String label) {
        return new Label(name, label, this.labelStyles.get("default"));
    }

    public Label newLabel(String name, String label, String style) {
        return new Label(name, label, this.labelStyles.get(style));
    }

    public Slider newSlider(String name, float prefWidth, float min, float max, float step) {
        return newSlider(name, prefWidth, min, max, step, "default");
    }

    public Slider newSlider(String name, float prefWidth, float min, float max, float step, String style) {
        return new Slider(name, prefWidth, min, max, step, this.sliderStyles.get(style));
    }

    public List newList(String name, String[] entries) {
        return newList(name, entries, "default");
    }

    public List newList(String name, String[] entries, String style) {
        return new List(name, entries, this.listStyles.get(style));
    }

    public Pane newPane(String name, Stage stage, int prefWidth, int prefHeight) {
        return newPane(name, stage, prefWidth, prefHeight, "default");
    }

    public Pane newPane(String name, Stage stage, int prefWidth, int prefHeight, String style) {
        return new Pane(name, stage, prefWidth, prefHeight, this.paneStyles.get(style));
    }

    public ScrollPane newScrollPane(String name, Stage stage, Actor widget, int prefWidth, int prefHeight) {
        return newScrollPane(name, stage, widget, prefWidth, prefHeight, "default");
    }

    public ScrollPane newScrollPane(String name, Stage stage, Actor widget, int prefWidth, int prefHeight, String style) {
        return new ScrollPane(name, stage, widget, prefWidth, prefHeight, this.scrollPaneStyles.get(style));
    }

    public SplitPane newSplitPane(String name, Stage stage, Actor firstWidget, Actor secondWidget, boolean vertical, int prefWidth, int prefHeight, String style) {
        return new SplitPane(name, stage, firstWidget, secondWidget, vertical, prefWidth, prefHeight, this.splitPaneStyles.get(style));
    }

    public TextField newTextField(String name, float prefWidth) {
        return newTextField(name, prefWidth, "default");
    }

    public TextField newTextField(String name, float prefWidth, String style) {
        return new TextField(name, prefWidth, this.textFieldStyles.get(style));
    }

    public ComboBox newComboBox(String name, String[] entries, Stage stage) {
        return newComboBox(name, entries, stage, "default");
    }

    public ComboBox newComboBox(String name, String[] entries, Stage stage, String style) {
        return new ComboBox(name, entries, stage, this.comboBoxStyles.get(style));
    }

    public Window newWindow(String name, Stage stage, String title, int width, int height) {
        return newWindow(name, stage, title, width, height, "default");
    }

    public Window newWindow(String name, Stage stage, String title, int width, int height, String style) {
        return new Window(name, stage, title, width, height, this.windowStyles.get(style));
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.texture.dispose();
        for (BitmapFont font : this.fonts.values()) {
            font.dispose();
        }
    }

    public Texture getTexture() {
        return this.texture;
    }
}
