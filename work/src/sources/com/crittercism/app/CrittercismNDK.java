package com.crittercism.app;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class CrittercismNDK {
    private static final String LIBNAME = "libcrittercism-ndk.so";
    private static String LIBPATH = null;
    private static final int LIBRARYVERSION = 2;

    public static native boolean checkLibraryVersion(int i);

    public static boolean installLib(Context context) {
        String str = System.getProperty("os.arch").contains("v7") ? "armeabi-v7a" : "armeabi";
        try {
            new File(LIBPATH).mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(LIBPATH + LIBNAME));
            InputStream inputStreamOpen = context.getAssets().open(str + "/libcrittercism-ndk.so");
            byte[] bArr = new byte[Opcodes.ACC_ANNOTATION];
            while (true) {
                int i = inputStreamOpen.read(bArr);
                if (i < 0) {
                    inputStreamOpen.close();
                    fileOutputStream.close();
                    System.load(LIBPATH + LIBNAME);
                    return true;
                }
                fileOutputStream.write(bArr, 0, i);
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static native boolean installNdk(String str);

    public static void installNdkLib(Context context, String str) {
        boolean zInstallLib;
        LIBPATH = context.getFilesDir().getAbsolutePath() + "/com.crittercism/lib/";
        String str2 = context.getFilesDir().getAbsolutePath() + "/" + str;
        if (new File(LIBPATH + LIBNAME).exists()) {
            try {
                System.load(LIBPATH + LIBNAME);
                zInstallLib = checkLibraryVersion(2);
            } catch (UnsatisfiedLinkError e) {
                zInstallLib = installLib(context);
            }
            if (!zInstallLib) {
                installLib(context);
            }
        } else {
            installLib(context);
        }
        try {
            if (installNdk(str2)) {
                File file = new File(str2);
                file.getAbsolutePath();
                file.mkdirs();
            }
        } catch (Throwable th) {
        }
    }
}
