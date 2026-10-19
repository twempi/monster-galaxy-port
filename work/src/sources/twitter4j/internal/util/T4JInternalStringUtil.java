package twitter4j.internal.util;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class T4JInternalStringUtil {
    private T4JInternalStringUtil() {
        throw new AssertionError();
    }

    public static String maskString(String str) {
        StringBuffer buf = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            buf.append("*");
        }
        return buf.toString();
    }

    public static String[] split(String str, String separator) {
        int index = str.indexOf(separator);
        if (index == -1) {
            String[] returnValue = {str};
            return returnValue;
        }
        List<String> strList = new ArrayList<>();
        int oldIndex = 0;
        while (index != -1) {
            String subStr = str.substring(oldIndex, index);
            strList.add(subStr);
            oldIndex = index + separator.length();
            index = str.indexOf(separator, oldIndex);
        }
        if (oldIndex != str.length()) {
            strList.add(str.substring(oldIndex));
        }
        return (String[]) strList.toArray(new String[strList.size()]);
    }

    public static String join(int[] follows) {
        StringBuffer buf = new StringBuffer(follows.length * 11);
        for (int follow : follows) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    public static String join(long[] follows) {
        StringBuffer buf = new StringBuffer(follows.length * 11);
        for (long follow : follows) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    public static String join(String[] track) {
        StringBuffer buf = new StringBuffer(track.length * 11);
        for (String str : track) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(str);
        }
        return buf.toString();
    }
}
