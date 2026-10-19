package twitter4j.internal.http;

import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class RequestMethod implements Serializable {
    private static final long serialVersionUID = -4399222582680270381L;
    private final String name;
    private static final Map<String, RequestMethod> instances = new HashMap(5);
    public static final RequestMethod GET = new RequestMethod(HttpEngine.GET);
    public static final RequestMethod POST = new RequestMethod(HttpEngine.POST);
    public static final RequestMethod DELETE = new RequestMethod(HttpEngine.DELETE);
    public static final RequestMethod HEAD = new RequestMethod(HttpEngine.HEAD);
    public static final RequestMethod PUT = new RequestMethod(HttpEngine.PUT);

    private RequestMethod(String name) {
        this.name = name;
        instances.put(name, this);
    }

    public final String name() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestMethod)) {
            return false;
        }
        RequestMethod that = (RequestMethod) o;
        return this.name.equals(that.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return new StringBuffer().append("RequestMethod{name='").append(this.name).append('\'').append('}').toString();
    }

    private static RequestMethod getInstance(String name) {
        return instances.get(name);
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(this.name);
    }
}
