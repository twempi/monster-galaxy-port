package com.sessionm.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class CookieUtils {
    static Map<String, CookieAttributeAssignor> assignors;

    /* JADX INFO: compiled from: ProGuard */
    interface CookieAttributeAssignor {
        void assign(BasicClientCookie basicClientCookie, String str, String str2);
    }

    public static List<Cookie> parse(String str) {
        ArrayList arrayList = new ArrayList();
        Cookie internal = parseInternal(str);
        ((BasicClientCookie) internal).setVersion(0);
        arrayList.add(internal);
        return arrayList;
    }

    private static Cookie parseInternal(String str) {
        String strTrim;
        String strTrim2;
        StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
        try {
            String strNextToken = stringTokenizer.nextToken();
            int iIndexOf = strNextToken.indexOf(61);
            if (iIndexOf != -1) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(strNextToken.substring(0, iIndexOf).trim(), stripOffSurroundingQuote(strNextToken.substring(iIndexOf + 1).trim()));
                while (stringTokenizer.hasMoreTokens()) {
                    String strNextToken2 = stringTokenizer.nextToken();
                    int iIndexOf2 = strNextToken2.indexOf(61);
                    if (iIndexOf2 != -1) {
                        strTrim = strNextToken2.substring(0, iIndexOf2).trim();
                        strTrim2 = strNextToken2.substring(iIndexOf2 + 1).trim();
                    } else {
                        strTrim = strNextToken2.trim();
                        strTrim2 = null;
                    }
                    assignAttribute(basicClientCookie, strTrim, strTrim2);
                }
                return basicClientCookie;
            }
            throw new IllegalArgumentException("Invalid cookie name-value pair");
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Empty cookie header string");
        }
    }

    private static String stripOffSurroundingQuote(String str) {
        if (str != null && str.length() > 0 && str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    private static void assignAttribute(BasicClientCookie basicClientCookie, String str, String str2) {
        String strStripOffSurroundingQuote = stripOffSurroundingQuote(str2);
        CookieAttributeAssignor cookieAttributeAssignor = assignors.get(str.toLowerCase());
        if (cookieAttributeAssignor != null) {
            cookieAttributeAssignor.assign(basicClientCookie, str, strStripOffSurroundingQuote);
            return;
        }
        throw new IllegalArgumentException("Illegal cookie attribute " + str);
    }

    static {
        assignors = null;
        assignors = new HashMap();
        assignors.put("comment", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.1
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
                if (basicClientCookie.getComment() == null) {
                    basicClientCookie.setComment(str2);
                }
            }
        });
        assignors.put("domain", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.2
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
                if (basicClientCookie.getDomain() == null) {
                    basicClientCookie.setDomain(str2);
                }
            }
        });
        assignors.put("path", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.3
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
                if (basicClientCookie.getPath() == null) {
                    basicClientCookie.setPath(str2);
                }
            }
        });
        assignors.put("secure", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.4
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
                basicClientCookie.setSecure(true);
            }
        });
        assignors.put("version", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.5
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
                try {
                    basicClientCookie.setVersion(Integer.parseInt(str2));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Illegal cookie version attribute");
                }
            }
        });
        assignors.put("expires", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.6
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
                if (basicClientCookie.getExpiryDate() == null) {
                    basicClientCookie.setExpiryDate(new Date(str2));
                }
            }
        });
        assignors.put("max-age", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.7
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
            }
        });
        assignors.put("visitor", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.8
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
            }
        });
        assignors.put("httponly", new CookieAttributeAssignor() { // from class: com.sessionm.net.CookieUtils.9
            @Override // com.sessionm.net.CookieUtils.CookieAttributeAssignor
            public void assign(BasicClientCookie basicClientCookie, String str, String str2) {
            }
        });
    }
}
