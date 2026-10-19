# Monster Galaxy for Linux

A native Linux desktop build of **Monster Galaxy 1.3** (Gaia Online, Android, 2013),
made from the original APK. No emulator. The original game code runs on the JVM
with a desktop libGDX backend.

```sh
./build.sh      # once: turns the APK into a desktop build in ./build
./run.sh        # play
```

Close the game before running `build.sh` again. It replaces `build/`, so it
refuses to run while the game is open.

For the sequel, Monster Galaxy: The Exile, see [below](#monster-galaxy-the-exile).

On NixOS both scripts enter `shell.nix` on their own, which provides the JDK,
dex2jar and the X11/OpenGL/OpenAL libraries. On other distros, install a JDK (8+),
`dex2jar`, `unzip`, `curl` and `git` first.

## Playing

- Mouse = touch. **Escape** = the Android back button (pause, close dialogs, quit).
- Scroll lists (like the shop) with the **mouse wheel** or by click-dragging.
- Typing works in the name fields.
- The window can be resized or made fullscreen (sway: `$mod+f`). The picture
  scales with black bars. `./run.sh --width 1920 --height 1080` sets the render
  resolution (default 1280x720).
- **Options** (Menu → Options): music and sound effect volume, and language.
  Changing the language restarts the game, as it did on Android.
- Saves and settings are in `~/.local/share/monster-galaxy/` (the `monsterGalaxy`
  file is your save).
- **Insert** opens the mod menu (`desktop/src/.../ModMenu.java`): set Moga Cash,
  Starseeds and Blue Coffee, refill or unlimited energy, always win the "Best"
  Lucky Moga prize, free Lucky Moga spins, set any Moga's level (1–50), heal and
  wake all Mogas, and give yourself any Moga. Up/Down select, Left/Right change
  (Shift ×10, Ctrl ×100), type digits + Enter for an exact number. The mouse works
  too. Changes are saved right away; the toggles are remembered between launches.
  While the menu is open the game gets no input. Exile has the same menu.
- If the game quits right away with "No OpenGL context found" (or an X error
  about `X_GLXCreateNewContext`) after a system update, reboot. The NVIDIA
  driver was updated but the old kernel module is still loaded, which breaks
  every new OpenGL program, not just these games.

## How it works

This APK is a libGDX 0.9.7 game. All art, music and game data are bundled in it,
and progress is saved locally. The Gaia servers were only used for analytics,
install tracking and a time check. `build.sh`:

1. converts the APK's Dalvik bytecode into a normal jar (`dex2jar`). The game
   code is not recompiled.
2. compiles libGDX 0.9.7's own LWJGL desktop backend (sparse-cloned into
   `vendor/`) against the game's bundled libGDX core, with the patches in
   `desktop/backend/`. `virtual-screen.patch` is described below;
   `openal-music-dispose.patch` stops freed music streams piling up.
3. compiles the desktop layer in `desktop/src`, which replaces the Android-only
   pieces:
   - `DesktopPlatformHelper`: reports "not Android", which switches the game to
     the desktop code paths its developers left in (in-game text fields and
     dialogue rendering). Ads, billing, Facebook and Twitter are no-ops.
   - `DesktopStorageHelper`: the game's SQLite queries, ported from
     `android.database` to JDBC. Reads the bundled per-language databases.
   - `OfflineGaiaClient`: stands in for the dead gaiaonline.com API. Server
     time is the local clock.
   - `NoopAnalytics`: drops Flurry, Kontagent and SessionM.
   - `DesktopOptions` / `VolumeSlider`: the Options panel. Android drew it with
     native widgets; this rebuilds it in-engine from the game's own art.
4. `desktop/overrides`: patched copies of a few game classes. They fix the
   size and length limits of the name text field, add the Options panel to
   `SettingsScreen`, fix scrolling in `ScrollInputProcessor` (mouse wheel
   support; a drag no longer keeps scrolling after you let go), and fix
   `MusicBlock`, which never stopped the previous track. Before that fix,
   songs stacked up and the music slider only reached the newest one.
5. extracts the assets and the native libraries.

`VirtualScreen` exists because the game reads the screen size once at startup.
The game renders into an offscreen buffer of fixed size, which is scaled into
the window, and mouse coordinates are mapped back.

## Known differences from the phone version

- **Shop purchases are free.** Without Google Play billing, the game's desktop
  code path grants items directly. This also applies to the paid starter Mogas.
- Bold text in quest dialogue shows as plain text (Android rendered it as HTML).
- Facebook/Twitter sharing, ads, offer walls and push notifications are gone.
  That includes the notification toggles from the phone's Options screen.

`work/` holds the decompiled sources (jadx) used for the reverse engineering.
The build does not need it.

## Monster Galaxy: The Exile

`exile/` ports the sequel, **Monster Galaxy: The Exile 1.0.3**
(`monster-galaxy-exile-1-0-3-en-android.apk`), the same way:

```sh
exile/build.sh  # once: builds into exile/build
exile/run.sh    # play
```

Controls, Options (music and sound effect volume), scrolling, free purchases
and resizing work as above. Saves are kept separately in
`~/.local/share/monster-galaxy-exile/`. Close the game before running
`exile/build.sh` again.

Exile is built on an older libGDX (0.9.2, from 2011), so `exile/build.sh`
compiles that version's desktop backend with its own patches in
`exile/desktop/backend/` (the same VirtualScreen and music fixes, plus
`core-compat.patch` for the slightly older engine core bundled in the game).
`exile/desktop/` is the desktop layer adapted to Exile. Two differences:

- Exile reports skin 2 (`SK2`), as the Android build did for its package
  `com.gaiaonline.mge`. That selects Exile's own UI, such as the straight
  attack meter in battles.
- Exile streamed most of its music from Gaia's CDN, which is gone and was never
  archived. All art ships in the APK, but 12 island themes and 16 battle themes
  were never bundled. The downloader is switched off (`DownloadTask` override),
  and the game falls back to the bundled tracks of the same kind on its own.
  Every island and battle has music, but there is less variety than there was
  in 2012.

`work-exile/` holds Exile's decompiled sources.
