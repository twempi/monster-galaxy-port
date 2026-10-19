#!/usr/bin/env bash
# Runs the Monster Galaxy: The Exile desktop build produced by exile/build.sh.
#
#   exile/run.sh [--width N --height N]
#
# The size (default 1280x720) is the game's render resolution and initial window
# size. Resize or fullscreen the window freely (sway: $mod+f); the picture scales.
# Saves live in ${XDG_DATA_HOME:-~/.local/share}/monster-galaxy-exile.
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(dirname "$HERE")" # shell.nix is shared with the Monster Galaxy port
# On NixOS, re-run inside shell.nix (tools + library paths) unless already there.
if [ -e /etc/NIXOS ] && [ -z "${MONSTER_GALAXY_SHELL:-}" ]; then
  exec nix-shell "$ROOT/shell.nix" --run "$(printf '%q ' "$0" "$@")"
fi
OUT="$HERE/build"
[ -d "$OUT/lib" ] || { echo "Not built yet: run exile/build.sh first" >&2; exit 1; }

# desktop.jar first: it overrides a few classes in game.jar.
CP="$OUT/lib/desktop.jar"
for jar in "$OUT"/lib/*.jar; do
  [ "$jar" = "$OUT/lib/desktop.jar" ] || CP="$CP:$jar"
done
cd "$OUT/assets"
exec java \
  -Dorg.lwjgl.librarypath="$OUT/natives" \
  -Djava.library.path="$OUT/natives" \
  -cp "$CP" \
  com.gaiaonline.monstergalaxy.desktop.DesktopLauncher "$OUT/assets" "$@"
