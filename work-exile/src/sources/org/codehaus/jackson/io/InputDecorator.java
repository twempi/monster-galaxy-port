package org.codehaus.jackson.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class InputDecorator {
    public abstract InputStream decorate(IOContext iOContext, InputStream inputStream) throws IOException;

    public abstract InputStream decorate(IOContext iOContext, byte[] bArr, int i, int i2) throws IOException;

    public abstract Reader decorate(IOContext iOContext, Reader reader) throws IOException;
}
