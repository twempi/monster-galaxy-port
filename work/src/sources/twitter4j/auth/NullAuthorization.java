package twitter4j.auth;

import java.io.ObjectStreamException;
import java.io.Serializable;
import twitter4j.internal.http.HttpRequest;

/* JADX INFO: loaded from: classes.dex */
public class NullAuthorization implements Authorization, Serializable {
    private static NullAuthorization SINGLETON = new NullAuthorization();
    private static final long serialVersionUID = -8748173338942663960L;

    private NullAuthorization() {
    }

    public static NullAuthorization getInstance() {
        return SINGLETON;
    }

    @Override // twitter4j.auth.Authorization
    public String getAuthorizationHeader(HttpRequest req) {
        return null;
    }

    @Override // twitter4j.auth.Authorization
    public boolean isEnabled() {
        return false;
    }

    public boolean equals(Object o) {
        return SINGLETON == o;
    }

    public String toString() {
        return "NullAuthentication{SINGLETON}";
    }

    private Object readResolve() throws ObjectStreamException {
        return SINGLETON;
    }
}
