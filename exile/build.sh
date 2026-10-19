#!/usr/bin/env bash
# Builds a native Linux desktop version of Monster Galaxy: The Exile from the Android APK.
#
#   exile/build.sh [path/to/monster-galaxy-exile.apk]
#
# Needs: java/javac (8+), unzip, curl, sha1sum, d2j-dex2jar (dex2jar).
# On NixOS these come from shell.nix automatically.
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(dirname "$HERE")" # shares shell.nix, vendor/ and .cache/ with the Monster Galaxy port
# On NixOS, re-run inside shell.nix (tools + library paths) unless already there.
if [ -e /etc/NIXOS ] && [ -z "${MONSTER_GALAXY_SHELL:-}" ]; then
  exec nix-shell "$ROOT/shell.nix" --run "$(printf '%q ' "$0" "$@")"
fi
APK="$(realpath "${1:-$ROOT/monster-galaxy-exile-1-0-3-en-android.apk}")"
GDX="$ROOT/vendor/libgdx-0.9.2"
OUT="${MONSTER_GALAXY_BUILD_DIR:-$HERE/build}" # override to build elsewhere (e.g. for testing)
CACHE="$ROOT/.cache/maven"
TMP="$OUT/tmp"

LWJGL_VERSION=2.9.3
SQLITE_VERSION=3.46.1.3

[ -f "$APK" ] || { echo "APK not found: $APK" >&2; exit 1; }
if [ ! -d "$GDX" ]; then
  echo "Fetching libGDX 0.9.2 (desktop backend sources and prebuilt natives)..."
  git clone -q --depth 1 --branch 0.9.2 --filter=blob:none --sparse https://github.com/libgdx/libgdx "$GDX" 2>/dev/null
  git -C "$GDX" sparse-checkout set --no-cone \
    /backends/gdx-backend-lwjgl/src /backends/gdx-openal/src \
    /backends/gdx-openal/libs/jlayer-1.0.1-libgdx.jar /backends/gdx-openal/libs/jorbis.jar \
    /gdx/libs/linux64/libgdx-64.so
fi

# fetch <group/path> <artifact> <version> [classifier] -> prints local path
fetch() {
  local group="$1" artifact="$2" version="$3" classifier="${4:-}"
  local file="$artifact-$version${classifier:+-$classifier}.jar"
  local url="https://repo1.maven.org/maven2/$group/$artifact/$version/$file"
  mkdir -p "$CACHE"
  if [ ! -f "$CACHE/$file" ]; then
    echo "Downloading $file" >&2
    curl -fsSL -o "$CACHE/$file.part" "$url"
    local want; want="$(curl -fsSL "$url.sha1" | cut -c1-40)"
    echo "$want  $CACHE/$file.part" | sha1sum -c --quiet - >&2
    mv "$CACHE/$file.part" "$CACHE/$file"
  fi
  echo "$CACHE/$file"
}

# Rebuilding replaces the build under a running game, which can crash it mid-session.
if pgrep -f "DesktopLauncher $OUT/assets" >/dev/null; then
  echo "Monster Galaxy: The Exile is running from this build; close the game first, then rebuild." >&2
  exit 1
fi
rm -rf "$OUT"
mkdir -p "$OUT/lib" "$OUT/natives" "$TMP"

echo "[1/5] Converting Android bytecode (dex) to a Java jar..."
d2j-dex2jar -f -o "$OUT/lib/game.jar" "$APK" >/dev/null 2>&1

echo "[2/5] Fetching LWJGL $LWJGL_VERSION and sqlite-jdbc..."
LWJGL_JAR="$(fetch org/lwjgl/lwjgl lwjgl "$LWJGL_VERSION")"
LWJGL_UTIL_JAR="$(fetch org/lwjgl/lwjgl lwjgl_util "$LWJGL_VERSION")"
LWJGL_NATIVES="$(fetch org/lwjgl/lwjgl lwjgl-platform "$LWJGL_VERSION" natives-linux)"
SQLITE_JAR="$(fetch org/xerial sqlite-jdbc "$SQLITE_VERSION")"
cp "$LWJGL_JAR" "$LWJGL_UTIL_JAR" "$SQLITE_JAR" "$OUT/lib/"
cp "$GDX/backends/gdx-openal/libs/jlayer-1.0.1-libgdx.jar" \
   "$GDX/backends/gdx-openal/libs/jorbis.jar" "$OUT/lib/"

echo "[3/5] Compiling the libGDX 0.9.2 desktop (LWJGL) backend..."
# Patched copy of the backend (desktop/backend/*.patch): VirtualScreen renders at
# a fixed logical size and scales to the window; OpenALMusic.dispose leak fix.
mkdir -p "$TMP/backend-src"
cp -r "$GDX/backends/gdx-backend-lwjgl/src/." "$GDX/backends/gdx-openal/src/." "$TMP/backend-src/"
for p in "$HERE"/desktop/backend/*.patch; do
  patch -s -p1 -d "$TMP/backend-src" < "$p"
done
# VirtualScreen only talks to LWJGL, so it is shared with the Monster Galaxy port.
cp "$ROOT/desktop/backend/com/badlogic/gdx/backends/lwjgl/VirtualScreen.java" "$TMP/backend-src/com/badlogic/gdx/backends/lwjgl/"
find "$TMP/backend-src" -name '*.java' \
  ! -name 'LwjglApplet.java' ! -name 'LwjglCanvas.java' ! -name 'LwjglFrame.java' \
  > "$TMP/backend-sources.txt"
CP_BACKEND="$OUT/lib/game.jar:$LWJGL_JAR:$LWJGL_UTIL_JAR:$OUT/lib/jlayer-1.0.1-libgdx.jar:$OUT/lib/jorbis.jar"
javac -nowarn -encoding UTF-8 --release 8 -Xlint:-options -cp "$CP_BACKEND" -d "$TMP/backend" @"$TMP/backend-sources.txt"
jar cf "$OUT/lib/gdx-backend-lwjgl.jar" -C "$TMP/backend" .

echo "[4/5] Compiling the Exile desktop layer..."
# desktop/overrides holds patched copies of a few game classes; desktop.jar goes
# first on the classpath so they take precedence over the originals in game.jar.
find "$HERE/desktop/src" "$HERE/desktop/overrides" -name '*.java' > "$TMP/desktop-sources.txt"
javac -encoding UTF-8 --release 8 -Xlint:-options -cp "$CP_BACKEND:$OUT/lib/gdx-backend-lwjgl.jar" \
  -d "$TMP/desktop" @"$TMP/desktop-sources.txt"
jar cf "$OUT/lib/desktop.jar" -C "$TMP/desktop" .

echo "[5/5] Extracting game assets and native libraries..."
unzip -q "$APK" 'assets/*' -d "$TMP/apk"
mv "$TMP/apk/assets" "$OUT/assets"
cp "$GDX/gdx/libs/linux64/libgdx-64.so" "$OUT/natives/"
unzip -q -j -o "$LWJGL_NATIVES" liblwjgl64.so -d "$OUT/natives"

rm -rf "$TMP"
echo "Done. Run exile/run.sh"
