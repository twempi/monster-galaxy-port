package com.sessionm.integralblue.httpresponsecache.compat.javax.net.ssl;

import com.sessionm.integralblue.httpresponsecache.compat.System;
import com.sessionm.integralblue.httpresponsecache.compat.java.net.InetAddress;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class DefaultHostnameVerifier implements HostnameVerifier {
    private static final String[] BAD_COUNTRY_2LDS = {"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};

    static {
        Arrays.sort(BAD_COUNTRY_2LDS);
    }

    @Override // javax.net.ssl.HostnameVerifier
    public final boolean verify(String str, SSLSession sSLSession) {
        try {
            X509Certificate x509Certificate = (X509Certificate) sSLSession.getPeerCertificates()[0];
            String lowerCase = str.trim().toLowerCase(Locale.ENGLISH);
            if (matches(lowerCase, getFirstCn(x509Certificate))) {
                return true;
            }
            Iterator<String> it = getDNSSubjectAlts(x509Certificate).iterator();
            while (it.hasNext()) {
                if (matches(lowerCase, it.next())) {
                    return true;
                }
            }
            return false;
        } catch (SSLException e) {
            return false;
        }
    }

    private boolean matches(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        String lowerCase = str2.toLowerCase(Locale.ENGLISH);
        if (lowerCase.startsWith("*.")) {
            int length = lowerCase.length() - 1;
            return str.regionMatches(str.length() - length, lowerCase, 1, length) && lowerCase.indexOf(46, 2) != -1 && acceptableCountryWildcard(lowerCase) && !InetAddress.isNumeric(str);
        }
        return str.equals(lowerCase);
    }

    private boolean acceptableCountryWildcard(String str) {
        int length = str.length();
        if (length < 7 || length > 9 || str.charAt(length - 3) != '.') {
            return true;
        }
        return Arrays.binarySearch(BAD_COUNTRY_2LDS, str.substring(2, length + (-3))) < 0;
    }

    private String getFirstCn(X509Certificate x509Certificate) {
        for (String str : x509Certificate.getSubjectX500Principal().toString().split(",")) {
            int iIndexOf = str.indexOf("CN=");
            if (iIndexOf >= 0) {
                return str.substring(iIndexOf + 3);
            }
        }
        return null;
    }

    private List<String> getDNSSubjectAlts(X509Certificate x509Certificate) {
        try {
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            for (List<?> list : subjectAlternativeNames) {
                if (((Integer) list.get(0)).intValue() == 2) {
                    arrayList.add((String) list.get(1));
                }
            }
            return arrayList;
        } catch (CertificateParsingException e) {
            System.logI("Error parsing certificate", e);
            return Collections.emptyList();
        }
    }
}
