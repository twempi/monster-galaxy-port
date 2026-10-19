package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface TwitterResponse extends Serializable {
    public static final int NONE = 0;
    public static final int READ = 1;
    public static final int READ_WRITE = 2;
    public static final int READ_WRITE_DIRECTMESSAGES = 3;

    int getAccessLevel();

    RateLimitStatus getRateLimitStatus();
}
