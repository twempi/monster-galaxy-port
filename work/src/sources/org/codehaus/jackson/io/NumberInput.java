package org.codehaus.jackson.io;

/* JADX INFO: loaded from: classes.dex */
public final class NumberInput {
    static final long L_BILLION = 1000000000;
    public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";
    static final String MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
    static final String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);

    public static final int parseInt(char[] digitChars, int offset, int len) {
        int num = digitChars[offset] - '0';
        int len2 = len + offset;
        int offset2 = offset + 1;
        if (offset2 < len2) {
            int num2 = (num * 10) + (digitChars[offset2] - '0');
            int offset3 = offset2 + 1;
            if (offset3 < len2) {
                int num3 = (num2 * 10) + (digitChars[offset3] - '0');
                int offset4 = offset3 + 1;
                if (offset4 < len2) {
                    int num4 = (num3 * 10) + (digitChars[offset4] - '0');
                    int offset5 = offset4 + 1;
                    if (offset5 < len2) {
                        int num5 = (num4 * 10) + (digitChars[offset5] - '0');
                        int offset6 = offset5 + 1;
                        if (offset6 < len2) {
                            int num6 = (num5 * 10) + (digitChars[offset6] - '0');
                            int offset7 = offset6 + 1;
                            if (offset7 < len2) {
                                int num7 = (num6 * 10) + (digitChars[offset7] - '0');
                                int offset8 = offset7 + 1;
                                if (offset8 < len2) {
                                    int num8 = (num7 * 10) + (digitChars[offset8] - '0');
                                    int offset9 = offset8 + 1;
                                    if (offset9 < len2) {
                                        return (num8 * 10) + (digitChars[offset9] - '0');
                                    }
                                    return num8;
                                }
                                return num7;
                            }
                            return num6;
                        }
                        return num5;
                    }
                    return num4;
                }
                return num3;
            }
            return num2;
        }
        return num;
    }

    public static final int parseInt(String str) {
        int offset;
        char c = str.charAt(0);
        int length = str.length();
        boolean negative = c == '-';
        if (negative) {
            if (length == 1 || length > 10) {
                return Integer.parseInt(str);
            }
            offset = 1 + 1;
            c = str.charAt(1);
        } else if (length <= 9) {
            offset = 1;
        } else {
            return Integer.parseInt(str);
        }
        if (c > '9' || c < '0') {
            return Integer.parseInt(str);
        }
        int num = c - '0';
        if (offset < length) {
            int offset2 = offset + 1;
            char c2 = str.charAt(offset);
            if (c2 > '9' || c2 < '0') {
                return Integer.parseInt(str);
            }
            num = (num * 10) + (c2 - '0');
            if (offset2 < length) {
                offset = offset2 + 1;
                char c3 = str.charAt(offset2);
                if (c3 > '9' || c3 < '0') {
                    return Integer.parseInt(str);
                }
                num = (num * 10) + (c3 - '0');
                if (offset < length) {
                    do {
                        int offset3 = offset;
                        offset = offset3 + 1;
                        char c4 = str.charAt(offset3);
                        if (c4 > '9' || c4 < '0') {
                            return Integer.parseInt(str);
                        }
                        num = (num * 10) + (c4 - '0');
                    } while (offset < length);
                }
            }
        }
        return negative ? -num : num;
    }

    public static final long parseLong(char[] digitChars, int offset, int len) {
        int len1 = len - 9;
        long val = ((long) parseInt(digitChars, offset, len1)) * L_BILLION;
        return ((long) parseInt(digitChars, offset + len1, 9)) + val;
    }

    public static final long parseLong(String str) {
        int length = str.length();
        return length <= 9 ? parseInt(str) : Long.parseLong(str);
    }

    public static final boolean inLongRange(char[] digitChars, int offset, int len, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        if (len < cmpLen) {
            return true;
        }
        if (len > cmpLen) {
            return false;
        }
        for (int i = 0; i < cmpLen; i++) {
            int diff = digitChars[offset + i] - cmpStr.charAt(i);
            if (diff != 0) {
                return diff < 0;
            }
        }
        return true;
    }

    public static final boolean inLongRange(String numberStr, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        int actualLen = numberStr.length();
        if (actualLen < cmpLen) {
            return true;
        }
        if (actualLen > cmpLen) {
            return false;
        }
        for (int i = 0; i < cmpLen; i++) {
            int diff = numberStr.charAt(i) - cmpStr.charAt(i);
            if (diff != 0) {
                return diff < 0;
            }
        }
        return true;
    }

    public static int parseAsInt(String input, int defaultValue) {
        String input2;
        int len;
        if (input != null && (len = (input2 = input.trim()).length()) != 0) {
            int i = 0;
            if (0 < len) {
                char c = input2.charAt(0);
                if (c == '+') {
                    input2 = input2.substring(1);
                    len = input2.length();
                } else if (c == '-') {
                    i = 0 + 1;
                }
            }
            while (i < len) {
                char c2 = input2.charAt(i);
                if (c2 <= '9' && c2 >= '0') {
                    i++;
                } else {
                    try {
                        return (int) parseDouble(input2);
                    } catch (NumberFormatException e) {
                        return defaultValue;
                    }
                }
            }
            try {
                return Integer.parseInt(input2);
            } catch (NumberFormatException e2) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static long parseAsLong(String input, long defaultValue) {
        String input2;
        int len;
        if (input != null && (len = (input2 = input.trim()).length()) != 0) {
            int i = 0;
            if (0 < len) {
                char c = input2.charAt(0);
                if (c == '+') {
                    input2 = input2.substring(1);
                    len = input2.length();
                } else if (c == '-') {
                    i = 0 + 1;
                }
            }
            while (i < len) {
                char c2 = input2.charAt(i);
                if (c2 <= '9' && c2 >= '0') {
                    i++;
                } else {
                    try {
                        return (long) parseDouble(input2);
                    } catch (NumberFormatException e) {
                        return defaultValue;
                    }
                }
            }
            try {
                return Long.parseLong(input2);
            } catch (NumberFormatException e2) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static double parseAsDouble(String input, double defaultValue) {
        if (input != null) {
            String input2 = input.trim();
            int len = input2.length();
            if (len != 0) {
                try {
                    return parseDouble(input2);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            return defaultValue;
        }
        return defaultValue;
    }

    public static final double parseDouble(String numStr) throws NumberFormatException {
        if (NASTY_SMALL_DOUBLE.equals(numStr)) {
            return Double.MIN_NORMAL;
        }
        return Double.parseDouble(numStr);
    }
}
