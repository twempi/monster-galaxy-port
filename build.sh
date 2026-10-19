#!/usr/bin/env bash
# Builds a native Linux desktop version of Monster Galaxy from the Android APK.
#
#   ./build.sh [path/to/monster-galaxy.apk]
#
# Needs: java/javac (8+), unzip, curl, sha1sum, d2j-dex2jar (dex2jar).
# On NixOS these come from shell.nix automatically.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# On NixOS, re-run inside shell.nix (tools + library paths) unless already there.
if [ -e /etc/NIXOS ] && [ -z "${MONSTER_GALAXY_SHELL:-}" ]; then
  exec nix-shell "$ROOT/shell.nix" --run "$(printf '%q ' "$0" "$@")"
fi
APK="$(realpath "${1:-$ROOT/monster-galaxy-1-3-en-android.apk}")"
GDX="$ROOT/vendor/libgdx-0.9.7"
OUT="${MONSTER_GALAXY_BUILD_DIR:-$ROOT/build}" # override to build elsewhere (e.g. for testing)
CACHE="$ROOT/.cache/maven"
TMP="$OUT/tmp"

LWJGL_VERSION=2.9.3
SQLITE_VERSION=3.46.1.3

[ -f "$APK" ] || { echo "APK not found: $APK" >&2; exit 1; }
if [ ! -d "$GDX" ]; then
  echo "Fetching libGDX 0.9.7 (desktop backend sources and prebuilt natives)..."
  git clone -q --depth 1 --branch 0.9.7 --filter=blob:none --sparse https://github.com/libgdx/libgdx "$GDX" 2>/dev/null
  git -C "$GDX" sparse-checkout set --no-cone \
    /backends/gdx-backend-lwjgl/src /backends/gdx-openal/src \
    /backends/gdx-openal/libs/jlayer-1.0.1-libgdx.jar /backends/gdx-openal/libs/jorbis.jar \
    /gdx/libs/gdx-natives.jar /extensions/gdx-freetype/libs/gdx-freetype-natives.jar
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

# Rebuilding replaces build/ under a running game, which can crash it mid-session.
if pgrep -f "DesktopLauncher $OUT/assets" >/dev/null; then
  echo "Monster Galaxy is running from this build; close the game first, then rebuild." >&2
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

echo "[3/5] Compiling the libGDX 0.9.7 desktop (LWJGL) backend..."
# Patched copy of the backend (desktop/backend/*.patch): VirtualScreen renders at
# a fixed logical size and scales to the window; OpenALMusic.dispose leak fix.
mkdir -p "$TMP/backend-src"
cp -r "$GDX/backends/gdx-backend-lwjgl/src/." "$GDX/backends/gdx-openal/src/." "$TMP/backend-src/"
for p in "$ROOT"/desktop/backend/*.patch; do
  patch -s -p1 -d "$TMP/backend-src" < "$p"
done
cp "$ROOT/desktop/backend/com/badlogic/gdx/backends/lwjgl/VirtualScreen.java" "$TMP/backend-src/com/badlogic/gdx/backends/lwjgl/"
find "$TMP/backend-src" -name '*.java' \
  ! -name 'LwjglApplet.java' ! -name 'LwjglAWT*.java' ! -name 'LwjglCanvas.java' ! -name 'LwjglFrame.java' \
  > "$TMP/backend-sources.txt"
CP_BACKEND="$OUT/lib/game.jar:$LWJGL_JAR:$LWJGL_UTIL_JAR:$OUT/lib/jlayer-1.0.1-libgdx.jar:$OUT/lib/jorbis.jar"
javac -nowarn -encoding UTF-8 --release 8 -Xlint:-options -cp "$CP_BACKEND" -d "$TMP/backend" @"$TMP/backend-sources.txt"
jar cf "$OUT/lib/gdx-backend-lwjgl.jar" -C "$TMP/backend" .

echo "[4/5] Compiling the Monster Galaxy desktop layer..."
# desktop/overrides holds patched copies of a few game classes; desktop.jar goes
# first on the classpath so they take precedence over the originals in game.jar.
find "$ROOT/desktop/src" "$ROOT/desktop/overrides" -name '*.java' > "$TMP/desktop-sources.txt"
javac -encoding UTF-8 --release 8 -Xlint:-options -cp "$CP_BACKEND:$OUT/lib/gdx-backend-lwjgl.jar" \
  -d "$TMP/desktop" @"$TMP/desktop-sources.txt"
jar cf "$OUT/lib/desktop.jar" -C "$TMP/desktop" .

echo "[5/5] Extracting game assets and native libraries..."
unzip -q "$APK" 'assets/*' -d "$TMP/apk"
mv "$TMP/apk/assets" "$OUT/assets"
unzip -q -j -o "$GDX/gdx/libs/gdx-natives.jar" libgdx64.so -d "$OUT/natives"
unzip -q -j -o "$GDX/extensions/gdx-freetype/libs/gdx-freetype-natives.jar" libgdx-freetype64.so -d "$OUT/natives"
unzip -q -j -o "$LWJGL_NATIVES" liblwjgl64.so -d "$OUT/natives"
# The freetype loader looks the library up on the classpath.
cp "$GDX/extensions/gdx-freetype/libs/gdx-freetype-natives.jar" "$OUT/lib/"

rm -rf "$TMP"
echo "Done. Run ./run.sh"
