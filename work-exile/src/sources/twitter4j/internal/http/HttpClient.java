package twitter4j.internal.http;

import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface HttpClient {
    HttpResponse request(HttpRequest httpRequest) throws TwitterException;

    void shutdown();
}
