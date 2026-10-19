package org.codehaus.jackson.map.util;

import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public interface Provider<T> {
    Collection<T> provide();
}
