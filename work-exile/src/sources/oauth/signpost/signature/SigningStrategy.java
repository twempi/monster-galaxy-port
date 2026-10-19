package oauth.signpost.signature;

import java.io.Serializable;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface SigningStrategy extends Serializable {
    String writeSignature(String str, HttpRequest httpRequest, HttpParameters httpParameters);
}
