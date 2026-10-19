package com.kontagent.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import oauth.signpost.OAuth;

/* JADX INFO: loaded from: classes.dex */
public class MapUtil {
    public static String mapToString(Map<String, String> map) {
        String strEncode;
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            if (key == null) {
                strEncode = "";
            } else {
                try {
                    strEncode = URLEncoder.encode(key, OAuth.ENCODING);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("This method requires UTF-8 encoding support", e);
                }
            }
            stringBuilder.append(strEncode);
            stringBuilder.append("=");
            stringBuilder.append(value != null ? URLEncoder.encode(value, OAuth.ENCODING) : "");
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> stringToMap(String input) {
        Map<String, String> map = new HashMap<>();
        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], OAuth.ENCODING), nameValue.length > 1 ? URLDecoder.decode(nameValue[1], OAuth.ENCODING) : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return map;
    }
}
