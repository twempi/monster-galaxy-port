package crittercism.android;

import android.os.Environment;
import android.os.StatFs;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
public final class h {
    public static BigInteger a() {
        BigInteger bigIntegerValueOf = BigInteger.valueOf(-1L);
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return BigInteger.valueOf(statFs.getAvailableBlocks()).multiply(BigInteger.valueOf(statFs.getBlockSize()));
        } catch (Exception e) {
            return bigIntegerValueOf;
        }
    }

    public static BigInteger b() {
        BigInteger bigIntegerValueOf = BigInteger.valueOf(-1L);
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return BigInteger.valueOf(statFs.getBlockCount()).multiply(BigInteger.valueOf(statFs.getBlockSize()));
        } catch (Exception e) {
            return bigIntegerValueOf;
        }
    }

    public static BigInteger c() {
        BigInteger bigIntegerValueOf = BigInteger.valueOf(-1L);
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return BigInteger.valueOf(statFs.getAvailableBlocks()).multiply(BigInteger.valueOf(statFs.getBlockSize()));
        } catch (Exception e) {
            return bigIntegerValueOf;
        }
    }

    public static BigInteger d() {
        BigInteger bigIntegerValueOf = BigInteger.valueOf(-1L);
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return BigInteger.valueOf(statFs.getBlockCount()).multiply(BigInteger.valueOf(statFs.getBlockSize()));
        } catch (Exception e) {
            return bigIntegerValueOf;
        }
    }
}
