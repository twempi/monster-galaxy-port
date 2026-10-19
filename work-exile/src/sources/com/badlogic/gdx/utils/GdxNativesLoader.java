package com.badlogic.gdx.utils;

import com.badlogic.gdx.Version;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class GdxNativesLoader {
    public static String path;
    public static boolean disableNativesLoading = false;
    private static boolean nativesLoaded = false;
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");
    public static boolean isMac = System.getProperty("os.name").contains("Mac");
    public static boolean is64Bit = System.getProperty("os.arch").equals("amd64");
    public static File nativesDir = new File(System.getProperty("java.io.tmpdir") + "/libgdx/" + crc("gdx.dll"));

    private static String crc(String nativeFile) {
        InputStream input = GdxNativesLoader.class.getResourceAsStream("/" + nativeFile);
        if (input == null) {
            return Version.VERSION;
        }
        CRC32 crc = new CRC32();
        byte[] buffer = new byte[Opcodes.ACC_SYNTHETIC];
        while (true) {
            try {
                int length = input.read(buffer);
                if (length == -1) {
                    break;
                }
                crc.update(buffer, 0, length);
            } catch (Exception e) {
                try {
                    input.close();
                } catch (Exception e2) {
                }
            }
        }
        return Long.toString(crc.getValue());
    }

    public static boolean loadLibrary(String nativeFile32, String nativeFile64) {
        path = extractLibrary(nativeFile32, nativeFile64);
        if (path != null) {
            System.load(path);
        }
        return path != null;
    }

    public static String extractLibrary(String native32, String native64) {
        String nativeFileName = is64Bit ? native64 : native32;
        File nativeFile = new File(nativesDir, nativeFileName);
        try {
            InputStream input = GdxNativesLoader.class.getResourceAsStream("/" + nativeFileName);
            if (input == null) {
                return null;
            }
            nativesDir.mkdirs();
            FileOutputStream output = new FileOutputStream(nativeFile);
            byte[] buffer = new byte[Opcodes.ACC_SYNTHETIC];
            while (true) {
                int length = input.read(buffer);
                if (length == -1) {
                    break;
                }
                output.write(buffer, 0, length);
            }
            input.close();
            output.close();
        } catch (IOException e) {
        }
        if (nativeFile.exists()) {
            return nativeFile.getAbsolutePath();
        }
        return null;
    }

    public static void load() {
        if (disableNativesLoading) {
            System.out.println("So you don't like our native lib loading? Good, you are on your own now. We don't give support from here on out");
            return;
        }
        if (!nativesLoaded) {
            String vm = System.getProperty("java.vm.name");
            if (vm == null || !vm.contains("Dalvik")) {
                if (isWindows) {
                    nativesLoaded = loadLibrary("gdx.dll", "gdx-64.dll");
                } else if (isMac) {
                    nativesLoaded = loadLibrary("libgdx.dylib", "libgdx.dylib");
                } else if (isLinux) {
                    nativesLoaded = loadLibrary("libgdx.so", "libgdx-64.so");
                }
                if (nativesLoaded) {
                    return;
                }
            }
            if (!is64Bit || isMac) {
                System.loadLibrary("gdx");
            } else {
                System.loadLibrary("gdx-64");
            }
            nativesLoaded = true;
        }
    }
}
