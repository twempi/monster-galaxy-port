# Build tools and runtime libraries for the desktop port.
# build.sh / run.sh enter this automatically on NixOS.
{ pkgs ? import <nixpkgs> { } }:

let
  # nixpkgs renamed the xorg.* packages; fall back to the old names on older channels.
  x = new: old: pkgs.${new} or pkgs.xorg.${old};
  runtimeLibs = [
    pkgs.libGL
    pkgs.stdenv.cc.cc.lib # libstdc++ for the older libGDX natives (Exile)
    pkgs.openal
    (x "libx11" "libX11")
    (x "libxcursor" "libXcursor")
    (x "libxext" "libXext")
    (x "libxi" "libXi")
    (x "libxrandr" "libXrandr")
    (x "libxrender" "libXrender")
    (x "libxxf86vm" "libXxf86vm")
  ];
in
pkgs.mkShell {
  packages = [
    pkgs.jdk21
    pkgs.dex2jar
    pkgs.unzip
    pkgs.curl
    pkgs.git
    (x "xrandr" "xrandr") # LWJGL 2 shells out to xrandr for display modes
  ];

  # The prebuilt libGDX/LWJGL natives dlopen these; /run/opengl-driver has the GPU driver.
  LD_LIBRARY_PATH = "/run/opengl-driver/lib:" + pkgs.lib.makeLibraryPath runtimeLibs;
  MONSTER_GALAXY_SHELL = "1";
}
