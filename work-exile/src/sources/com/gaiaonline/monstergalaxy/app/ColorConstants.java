package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import java.io.IOException;
import java.util.Properties;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ColorConstants {
    public static Color COLOR_BAD;
    public static Color COLOR_GOOD;
    public static Color ENERGY_TEXT_COLOR;
    public static Color FIFTH_FONT_COLOR;
    public static Color FOURTH_FONT_COLOR;
    public static Color HINT_ARROW_COLOR;
    public static Color ISLAND_TEXT_NAME_COLOR;
    public static Color ISLAND_TEXT_PROGRESS_COLOR;
    public static Color PRIMARY_GRAYED_COLOR;
    public static Color RUNNAWAY_FONT_COLOR;
    public static Color SECONARY_FONT_COLOR;
    public static Color SECONDARY_GRAYED_COLOR;
    public static Color SECONDARY_SHADOW_COLOR;
    public static Color SHARE_DESCRIPTION_COLOR;
    public static Color SUGESTED_NODE_COLOR;
    public static Color TERTIARY_FONT_COLOR;
    public static Color PRIMARY_FONT_COLOR = Color.WHITE;
    public static Color PRIMARY_SHADOW_COLOR = Color.BLACK;
    public static Color DAMAGE_SHADOW_COLOR = Color.WHITE;
    public static Color DAMAGE_COLOR = Color.RED;
    private static Properties colors = new Properties();

    static {
        try {
            colors.load(Gdx.files.internal("colors.properties").read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SECONARY_FONT_COLOR = buildColor(colors.getProperty("secondary_font_color"));
        TERTIARY_FONT_COLOR = buildColor(colors.getProperty("tertiary_font_color"));
        FOURTH_FONT_COLOR = buildColor(colors.getProperty("fourth_font_color"));
        TERTIARY_FONT_COLOR = buildColor(colors.getProperty("tertiary_font_color"));
        FIFTH_FONT_COLOR = buildColor(colors.getProperty("fifth_font_color"));
        PRIMARY_GRAYED_COLOR = buildColor(colors.getProperty("primary_grayed_color"));
        SECONDARY_GRAYED_COLOR = buildColor(colors.getProperty("secondary_grayed_color"));
        SECONDARY_SHADOW_COLOR = buildColor(colors.getProperty("secondary_shadow_color"));
        HINT_ARROW_COLOR = buildColor(colors.getProperty("hint_arrow_color"));
        RUNNAWAY_FONT_COLOR = buildColor(colors.getProperty("runnaway_font_color"));
        COLOR_BAD = buildColor(colors.getProperty("color_bad"));
        COLOR_GOOD = buildColor(colors.getProperty("color_good"));
        SUGESTED_NODE_COLOR = buildColor(colors.getProperty("suggested_node_color"));
        ISLAND_TEXT_NAME_COLOR = buildColor(colors.getProperty("island_text_name_color"));
        ISLAND_TEXT_PROGRESS_COLOR = buildColor(colors.getProperty("island_text_progress_color"));
        ENERGY_TEXT_COLOR = buildColor(colors.getProperty("energy_text_color"));
        SHARE_DESCRIPTION_COLOR = buildColor(colors.getProperty("share_description_color"));
    }

    private static Color buildColor(String value) {
        String[] rgb = value.split(",");
        float r = Float.parseFloat(rgb[0]);
        float g = Float.parseFloat(rgb[1]);
        float b = Float.parseFloat(rgb[2]);
        float a = Float.parseFloat(rgb[3]);
        return new Color(r, g, b, a);
    }

    public static Color getColor(String name) {
        return buildColor(colors.getProperty(name));
    }
}
