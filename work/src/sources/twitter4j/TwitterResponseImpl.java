package twitter4j;

import com.badlogic.gdx.Input;
import java.io.Serializable;
import twitter4j.internal.http.HttpResponse;

/* JADX INFO: loaded from: classes.dex */
abstract class TwitterResponseImpl implements TwitterResponse, Serializable {
    private static final long serialVersionUID = -7284708239736552059L;
    private transient int accessLevel;
    private transient RateLimitStatus rateLimitStatus;

    public TwitterResponseImpl() {
        this.rateLimitStatus = null;
        this.accessLevel = 0;
    }

    public TwitterResponseImpl(HttpResponse res) {
        this.rateLimitStatus = null;
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        this.accessLevel = toAccessLevel(res);
    }

    static int toAccessLevel(HttpResponse res) {
        if (res == null) {
            return -1;
        }
        String xAccessLevel = res.getResponseHeader("X-Access-Level");
        if (xAccessLevel == null) {
            return 0;
        }
        switch (xAccessLevel.length()) {
            case 4:
                return 1;
            case 10:
                return 2;
            case 25:
                return 3;
            case Input.Keys.POWER /* 26 */:
                return 3;
            default:
                return 0;
        }
    }

    @Override // twitter4j.TwitterResponse
    public RateLimitStatus getRateLimitStatus() {
        return this.rateLimitStatus;
    }

    @Override // twitter4j.TwitterResponse
    public int getAccessLevel() {
        return this.accessLevel;
    }
}
