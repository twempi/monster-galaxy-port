package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;

/* JADX INFO: loaded from: classes.dex */
public class FreetypeBuild {
    public static void main(String[] args) throws Exception {
        String[] headers = {"freetype-2.4.10/include"};
        String[] sources = {"freetype-2.4.10/src/base/ftsystem.c", "freetype-2.4.10/src/base/ftinit.c", "freetype-2.4.10/src/base/ftdebug.c", "freetype-2.4.10/src/base/ftbase.c", "freetype-2.4.10/src/base/ftbbox.c", "freetype-2.4.10/src/base/ftglyph.c", "freetype-2.4.10/src/base/ftbdf.c", "freetype-2.4.10/src/base/ftbitmap.c", "freetype-2.4.10/src/base/ftcid.c", "freetype-2.4.10/src/base/ftfstype.c", "freetype-2.4.10/src/base/ftgasp.c", "freetype-2.4.10/src/base/ftgxval.c", "freetype-2.4.10/src/base/ftlcdfil.c", "freetype-2.4.10/src/base/ftmm.c", "freetype-2.4.10/src/base/ftotval.c", "freetype-2.4.10/src/base/ftpatent.c", "freetype-2.4.10/src/base/ftpfr.c", "freetype-2.4.10/src/base/ftstroke.c", "freetype-2.4.10/src/base/ftsynth.c", "freetype-2.4.10/src/base/fttype1.c", "freetype-2.4.10/src/base/ftwinfnt.c", "freetype-2.4.10/src/base/ftxf86.c", "freetype-2.4.10/src/bdf/bdf.c", "freetype-2.4.10/src/cff/cff.c", "freetype-2.4.10/src/cid/type1cid.c", "freetype-2.4.10/src/pcf/pcf.c", "freetype-2.4.10/src/pfr/pfr.c", "freetype-2.4.10/src/sfnt/sfnt.c", "freetype-2.4.10/src/truetype/truetype.c", "freetype-2.4.10/src/type1/type1.c", "freetype-2.4.10/src/type42/type42.c", "freetype-2.4.10/src/winfonts/winfnt.c", "freetype-2.4.10/src/raster/raster.c", "freetype-2.4.10/src/smooth/smooth.c", "freetype-2.4.10/src/autofit/autofit.c", "freetype-2.4.10/src/cache/ftcache.c", "freetype-2.4.10/src/gzip/ftgzip.c", "freetype-2.4.10/src/lzw/ftlzw.c", "freetype-2.4.10/src/bzip2/ftbzip2.c", "freetype-2.4.10/src/gxvalid/gxvalid.c", "freetype-2.4.10/src/otvalid/otvalid.c", "freetype-2.4.10/src/psaux/psaux.c", "freetype-2.4.10/src/pshinter/pshinter.c", "freetype-2.4.10/src/psnames/psnames.c"};
        BuildTarget win32home = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
        win32home.compilerPrefix = "";
        win32home.buildFileName = "build-windows32home.xml";
        win32home.excludeFromMasterBuildFile = true;
        win32home.headerDirs = headers;
        win32home.cIncludes = sources;
        win32home.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        win32home.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        BuildTarget win32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
        win32.headerDirs = headers;
        win32.cIncludes = sources;
        win32.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        win32.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        BuildTarget win64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, true);
        win64.headerDirs = headers;
        win64.cIncludes = sources;
        win64.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        win64.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        BuildTarget lin32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, false);
        lin32.headerDirs = headers;
        lin32.cIncludes = sources;
        lin32.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        lin32.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        BuildTarget lin64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, true);
        lin64.headerDirs = headers;
        lin64.cIncludes = sources;
        lin64.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        lin64.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        BuildTarget mac = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, false);
        mac.headerDirs = headers;
        mac.cIncludes = sources;
        mac.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        mac.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        mac.linkerFlags += " -framework CoreServices -framework Carbon";
        BuildTarget android2 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Android, false);
        android2.headerDirs = headers;
        android2.cIncludes = sources;
        android2.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        android2.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
        new NativeCodeGenerator().generate();
        new AntScriptGenerator().generate(new BuildConfig("gdx-freetype"), new BuildTarget[]{win32home, win32, win64, lin32, lin64, mac, android2});
    }
}
