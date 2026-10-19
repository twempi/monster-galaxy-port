package org.codehaus.jackson.util;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class InternCache extends LinkedHashMap<String, String> {
    private static final int MAX_ENTRIES = 192;
    public static final InternCache instance = new InternCache();

    private InternCache() {
        super(192, 0.8f, true);
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return size() > 192;
    }

    public synchronized String intern(String input) {
        String result;
        result = get(input);
        if (result == null) {
            result = input.intern();
            put(result, result);
        }
        return result;
    }
}
