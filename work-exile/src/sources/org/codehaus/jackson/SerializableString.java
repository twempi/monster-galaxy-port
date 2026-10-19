package org.codehaus.jackson;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface SerializableString {
    char[] asQuotedChars();

    byte[] asQuotedUTF8();

    byte[] asUnquotedUTF8();

    int charLength();

    String getValue();
}
