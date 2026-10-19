package com.esotericsoftware.tablelayout;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ParseException extends RuntimeException {
    public int column;
    public int line;

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
