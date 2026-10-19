package twitter4j.internal.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import oauth.signpost.OAuth;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.TwitterException;
import twitter4j.internal.http.HTMLEntity;

/* JADX INFO: loaded from: classes.dex */
public final class ParseUtil {
    private static ThreadLocal<Map<String, SimpleDateFormat>> formatMap = new ThreadLocal<Map<String, SimpleDateFormat>>() { // from class: twitter4j.internal.util.ParseUtil.1
        @Override // java.lang.ThreadLocal
        protected Map<String, SimpleDateFormat> initialValue() {
            return initialValue2();
        }

        @Override // java.lang.ThreadLocal
        /* JADX INFO: renamed from: initialValue, reason: avoid collision after fix types in other method */
        protected Map<String, SimpleDateFormat> initialValue2() {
            return new HashMap();
        }
    };

    private ParseUtil() {
        throw new AssertionError();
    }

    public static String getUnescapedString(String str, JSONObject json) {
        return HTMLEntity.unescape(getRawString(str, json));
    }

    public static String getRawString(String name, JSONObject json) {
        try {
            if (json.isNull(name)) {
                return null;
            }
            return json.getString(name);
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getURLDecodedString(String name, JSONObject json) {
        String returnValue = getRawString(name, json);
        if (returnValue != null) {
            try {
                return URLDecoder.decode(returnValue, OAuth.ENCODING);
            } catch (UnsupportedEncodingException e) {
                return returnValue;
            }
        }
        return returnValue;
    }

    public static Date getDate(String name, JSONObject json) throws TwitterException {
        return getDate(name, json, "EEE MMM d HH:mm:ss z yyyy");
    }

    public static Date getDate(String name, JSONObject json, String format) throws TwitterException {
        String dateStr = getUnescapedString(name, json);
        if ("null".equals(dateStr) || dateStr == null) {
            return null;
        }
        return getDate(dateStr, format);
    }

    public static Date getDate(String name, String format) throws TwitterException {
        SimpleDateFormat sdf = formatMap.get().get(format);
        if (sdf == null) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.get().put(format, sdf);
        }
        try {
            return sdf.parse(name);
        } catch (ParseException pe) {
            throw new TwitterException(new StringBuffer().append("Unexpected date format(").append(name).append(") returned from twitter.com").toString(), pe);
        }
    }

    public static int getInt(String name, JSONObject json) {
        return getInt(getRawString(name, json));
    }

    public static int getInt(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return -1;
        }
        try {
            return Integer.valueOf(str).intValue();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static long getLong(String name, JSONObject json) {
        return getLong(getRawString(name, json));
    }

    public static long getLong(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return -1L;
        }
        if (str.endsWith("+")) {
            return Long.valueOf(str.substring(0, str.length() - 1)).longValue() + 1;
        }
        return Long.valueOf(str).longValue();
    }

    public static double getDouble(String name, JSONObject json) {
        String str2 = getRawString(name, json);
        if (str2 == null || "".equals(str2) || "null".equals(str2)) {
            return -1.0d;
        }
        return Double.valueOf(str2).doubleValue();
    }

    public static boolean getBoolean(String name, JSONObject json) {
        String str = getRawString(name, json);
        if (str == null || "null".equals(str)) {
            return false;
        }
        return Boolean.valueOf(str).booleanValue();
    }
}
