package oauth.signpost.http;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface HttpResponse {
    InputStream getContent() throws IOException;

    String getReasonPhrase() throws Exception;

    int getStatusCode() throws IOException;

    Object unwrap();
}
