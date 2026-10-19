package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.System;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class RawHeaders {
    private static final Comparator<String> FIELD_NAME_COMPARATOR = new Comparator<String>() { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.RawHeaders.1
        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            if (str == str2) {
                return 0;
            }
            if (str == null) {
                return -1;
            }
            if (str2 == null) {
                return 1;
            }
            return String.CASE_INSENSITIVE_ORDER.compare(str, str2);
        }
    };
    private int httpMinorVersion;
    private final List<String> namesAndValues;
    private int responseCode;
    private String responseMessage;
    private String statusLine;

    public RawHeaders() {
        this.namesAndValues = new ArrayList(20);
        this.httpMinorVersion = 1;
        this.responseCode = -1;
    }

    public RawHeaders(RawHeaders rawHeaders) {
        this.namesAndValues = new ArrayList(20);
        this.httpMinorVersion = 1;
        this.responseCode = -1;
        this.namesAndValues.addAll(rawHeaders.namesAndValues);
        this.statusLine = rawHeaders.statusLine;
        this.httpMinorVersion = rawHeaders.httpMinorVersion;
        this.responseCode = rawHeaders.responseCode;
        this.responseMessage = rawHeaders.responseMessage;
    }

    public void setStatusLine(String str) {
        String strTrim = str.trim();
        this.statusLine = strTrim;
        if (strTrim != null && strTrim.startsWith("HTTP/")) {
            String strTrim2 = strTrim.trim();
            int iIndexOf = strTrim2.indexOf(" ") + 1;
            if (iIndexOf != 0) {
                if (strTrim2.charAt(iIndexOf - 2) != '1') {
                    this.httpMinorVersion = 0;
                }
                int length = iIndexOf + 3;
                if (length > strTrim2.length()) {
                    length = strTrim2.length();
                }
                this.responseCode = Integer.parseInt(strTrim2.substring(iIndexOf, length));
                if (length + 1 <= strTrim2.length()) {
                    this.responseMessage = strTrim2.substring(length + 1);
                }
            }
        }
    }

    public String getStatusLine() {
        return this.statusLine;
    }

    public int getHttpMinorVersion() {
        if (this.httpMinorVersion != -1) {
            return this.httpMinorVersion;
        }
        return 1;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void addLine(String str) {
        int iIndexOf = str.indexOf(":");
        if (iIndexOf == -1) {
            add("", str);
        } else {
            add(str.substring(0, iIndexOf), str.substring(iIndexOf + 1));
        }
    }

    public void add(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("fieldName == null");
        }
        if (str2 == null) {
            System.logW("Ignoring HTTP header field '" + str + "' because its value is null");
        } else {
            this.namesAndValues.add(str);
            this.namesAndValues.add(str2.trim());
        }
    }

    public void removeAll(String str) {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.namesAndValues.size()) {
                if (str.equalsIgnoreCase(this.namesAndValues.get(i2))) {
                    this.namesAndValues.remove(i2);
                    this.namesAndValues.remove(i2);
                }
                i = i2 + 2;
            } else {
                return;
            }
        }
    }

    public void addAll(String str, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            add(str, it.next());
        }
    }

    public void set(String str, String str2) {
        removeAll(str);
        add(str, str2);
    }

    public int length() {
        return this.namesAndValues.size() / 2;
    }

    public String getFieldName(int i) {
        int i2 = i * 2;
        if (i2 < 0 || i2 >= this.namesAndValues.size()) {
            return null;
        }
        return this.namesAndValues.get(i2);
    }

    public String getValue(int i) {
        int i2 = (i * 2) + 1;
        if (i2 < 0 || i2 >= this.namesAndValues.size()) {
            return null;
        }
        return this.namesAndValues.get(i2);
    }

    public String get(String str) {
        for (int size = this.namesAndValues.size() - 2; size >= 0; size -= 2) {
            if (str.equalsIgnoreCase(this.namesAndValues.get(size))) {
                return this.namesAndValues.get(size + 1);
            }
        }
        return null;
    }

    public RawHeaders getAll(Set<String> set) {
        RawHeaders rawHeaders = new RawHeaders();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.namesAndValues.size()) {
                String str = this.namesAndValues.get(i2);
                if (set.contains(str)) {
                    rawHeaders.add(str, this.namesAndValues.get(i2 + 1));
                }
                i = i2 + 2;
            } else {
                return rawHeaders;
            }
        }
    }

    public String toHeaderString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append(this.statusLine).append("\r\n");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.namesAndValues.size()) {
                sb.append(this.namesAndValues.get(i2)).append(": ").append(this.namesAndValues.get(i2 + 1)).append("\r\n");
                i = i2 + 2;
            } else {
                sb.append("\r\n");
                return sb.toString();
            }
        }
    }

    public Map<String, List<String>> toMultimap() {
        TreeMap treeMap = new TreeMap(FIELD_NAME_COMPARATOR);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.namesAndValues.size()) {
                break;
            }
            String str = this.namesAndValues.get(i2);
            String str2 = this.namesAndValues.get(i2 + 1);
            ArrayList arrayList = new ArrayList();
            List list = (List) treeMap.get(str);
            if (list != null) {
                arrayList.addAll(list);
            }
            arrayList.add(str2);
            treeMap.put(str, Collections.unmodifiableList(arrayList));
            i = i2 + 2;
        }
        if (this.statusLine != null) {
            treeMap.put(null, Collections.unmodifiableList(Collections.singletonList(this.statusLine)));
        }
        return Collections.unmodifiableMap(treeMap);
    }

    public static RawHeaders fromMultimap(Map<String, List<String>> map) {
        RawHeaders rawHeaders = new RawHeaders();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (key != null) {
                rawHeaders.addAll(key, value);
            } else if (!value.isEmpty()) {
                rawHeaders.setStatusLine(value.get(value.size() - 1));
            }
        }
        return rawHeaders;
    }
}
