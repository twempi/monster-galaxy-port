package oauth.signpost.exception;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class OAuthException extends Exception {
    public OAuthException(String message) {
        super(message);
    }

    public OAuthException(Throwable cause) {
        super(cause);
    }

    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
