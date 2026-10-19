package org.codehaus.jackson.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import oauth.signpost.OAuth;
import org.codehaus.jackson.Version;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class VersionUtil {
    public static final String VERSION_FILE = "VERSION.txt";
    private static final Pattern VERSION_SEPARATOR = Pattern.compile("[-_./;:]");

    public static Version versionFor(Class<?> cls) {
        Version version = null;
        try {
            InputStream in = cls.getResourceAsStream(VERSION_FILE);
            if (in != null) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, OAuth.ENCODING));
                    version = parseVersion(br.readLine());
                    try {
                        in.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Throwable th) {
                    try {
                        in.close();
                        throw th;
                    } catch (IOException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
        } catch (IOException e3) {
        }
        return version == null ? Version.unknownVersion() : version;
    }

    public static Version parseVersion(String versionStr) {
        if (versionStr == null) {
            return null;
        }
        String versionStr2 = versionStr.trim();
        if (versionStr2.length() == 0) {
            return null;
        }
        String[] parts = VERSION_SEPARATOR.split(versionStr2);
        if (parts.length < 2) {
            return null;
        }
        int major = parseVersionPart(parts[0]);
        int minor = parseVersionPart(parts[1]);
        int patch = parts.length > 2 ? parseVersionPart(parts[2]) : 0;
        String snapshot = parts.length > 3 ? parts[3] : null;
        return new Version(major, minor, patch, snapshot);
    }

    protected static int parseVersionPart(String partStr) {
        String partStr2 = partStr.toString();
        int len = partStr2.length();
        int number = 0;
        for (int i = 0; i < len; i++) {
            char c = partStr2.charAt(i);
            if (c > '9' || c < '0') {
                break;
            }
            number = (number * 10) + (c - '0');
        }
        return number;
    }
}
