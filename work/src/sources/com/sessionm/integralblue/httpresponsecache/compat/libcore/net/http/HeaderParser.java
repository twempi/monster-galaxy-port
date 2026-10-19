package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class HeaderParser {

    /* JADX INFO: compiled from: ProGuard */
    public interface CacheControlHandler {
        void handle(String str, String str2);
    }

    HeaderParser() {
    }

    public static void parseCacheControl(String str, CacheControlHandler cacheControlHandler) {
        int iSkipUntil;
        String strTrim;
        int i = 0;
        while (i < str.length()) {
            int iSkipUntil2 = skipUntil(str, i, "=,");
            String strTrim2 = str.substring(i, iSkipUntil2).trim();
            if (iSkipUntil2 == str.length() || str.charAt(iSkipUntil2) == ',') {
                i = iSkipUntil2 + 1;
                cacheControlHandler.handle(strTrim2, null);
            } else {
                int iSkipWhitespace = skipWhitespace(str, iSkipUntil2 + 1);
                if (iSkipWhitespace < str.length() && str.charAt(iSkipWhitespace) == '\"') {
                    int i2 = iSkipWhitespace + 1;
                    int iSkipUntil3 = skipUntil(str, i2, "\"");
                    strTrim = str.substring(i2, iSkipUntil3);
                    iSkipUntil = iSkipUntil3 + 1;
                } else {
                    iSkipUntil = skipUntil(str, iSkipWhitespace, ",");
                    strTrim = str.substring(iSkipWhitespace, iSkipUntil).trim();
                }
                String str2 = strTrim;
                i = iSkipUntil;
                cacheControlHandler.handle(strTrim2, str2);
            }
        }
    }

    private static int skipUntil(String str, int i, String str2) {
        while (i < str.length() && str2.indexOf(str.charAt(i)) == -1) {
            i++;
        }
        return i;
    }

    private static int skipWhitespace(String str, int i) {
        char cCharAt;
        while (i < str.length() && ((cCharAt = str.charAt(i)) == ' ' || cCharAt == '\t')) {
            i++;
        }
        return i;
    }

    public static int parseSeconds(String str) {
        try {
            long j = Long.parseLong(str);
            if (j > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            if (j < 0) {
                return 0;
            }
            return (int) j;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
