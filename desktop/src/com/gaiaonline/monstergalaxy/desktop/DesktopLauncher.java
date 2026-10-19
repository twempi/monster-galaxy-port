package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Desktop entry point. Usage:
 *   DesktopLauncher <assets-dir> [--width N] [--height N]
 * The size is the game's logical resolution and the initial window size; the
 * window can then be resized or fullscreened and the picture scales to fit.
 * Must be started with the assets directory as the working directory, because the
 * game loads files via Gdx.files.internal("data/...") like on Android.
 */
public class DesktopLauncher {
    private static String[] launchArgs = new String[0];

    public static void main(String[] args) {
        launchArgs = args.clone();
        if (args.length < 1) {
            System.err.println("usage: DesktopLauncher <assets-dir> [--width N] [--height N]");
            System.exit(2);
        }
        File assetsDir = new File(args[0]).getAbsoluteFile();
        int width = 1280;
        int height = 720;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--width")) {
                width = Integer.parseInt(args[++i]);
            } else if (args[i].equals("--height")) {
                height = Integer.parseInt(args[++i]);
            }
        }

        File dataDir = dataDir();
        dataDir.mkdirs();
        // libGDX puts Preferences (~/.prefs) and "external" files under user.home;
        // point it at our data dir so saves and settings stay in one place.
        System.setProperty("user.home", dataDir.getAbsolutePath());

        loadNatives();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Monster Galaxy";
        config.useGL20 = false; // the Android build ran on GLES 1.x
        config.resizable = true; // the backend's VirtualScreen scales the fixed-size game
        config.vSyncEnabled = true;
        config.width = width;
        config.height = height;

        MonsterGalaxy game = new MonsterGalaxy(dataDir.getAbsolutePath(),
                new DesktopPlatformHelper(assetsDir, dataDir),
                new OfflineGaiaClient(dataDir.getAbsolutePath()),
                new NoopAnalytics());
        new LwjglApplication(new BackKeyListener(game), config);
    }

    /**
     * Saves, starts a fresh copy of the game with the same JVM options and
     * arguments, and exits. Used to apply a language change, as on Android.
     */
    public static void restart() {
        Game.save();
        List<String> command = new ArrayList<String>();
        command.add(new File(System.getProperty("java.home"), "bin/java").getPath());
        command.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(DesktopLauncher.class.getName());
        command.addAll(Arrays.asList(launchArgs));
        try {
            new ProcessBuilder(command).inheritIO().start();
        } catch (IOException e) {
            Gdx.app.error("MonsterGalaxy", "Restart failed; the language applies on next launch", e);
        }
        Gdx.app.exit();
    }

    /** Natives are extracted at build time; -Dorg.lwjgl.librarypath points at them. */
    private static void loadNatives() {
        String dir = System.getProperty("org.lwjgl.librarypath");
        if (dir == null) {
            return;
        }
        GdxNativesLoader.disableNativesLoading = true;
        System.load(new File(dir, "libgdx64.so").getAbsolutePath());
    }

    private static File dataDir() {
        String xdg = System.getenv("XDG_DATA_HOME");
        File base = xdg != null && !xdg.isEmpty()
                ? new File(xdg)
                : new File(System.getProperty("user.home"), ".local/share");
        return new File(base, "monster-galaxy");
    }

    /** Maps Escape to the Android back button. */
    static final class BackKeyListener implements ApplicationListener {
        private final ApplicationListener game;
        private boolean backWasDown;

        BackKeyListener(ApplicationListener game) {
            this.game = game;
        }

        public void create() {
            game.create();
        }

        public void render() {
            boolean backDown = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
            if (backDown && !backWasDown) {
                Game.backKeyPressed();
            }
            backWasDown = backDown;
            game.render();
        }

        public void resize(int width, int height) {
            game.resize(width, height);
        }

        public void pause() {
            game.pause();
        }

        public void resume() {
            game.resume();
        }

        public void dispose() {
            game.dispose();
        }
    }
}
