package twitter4j;

import java.io.Serializable;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class AccountTotalsJSONImpl extends TwitterResponseImpl implements AccountTotals, Serializable {
    private static final long serialVersionUID = -2291419345865627123L;
    private final int favorites;
    private final int followers;
    private final int friends;
    private final int updates;

    private AccountTotalsJSONImpl(HttpResponse res, JSONObject json) {
        super(res);
        this.updates = ParseUtil.getInt("updates", json);
        this.followers = ParseUtil.getInt("followers", json);
        this.favorites = ParseUtil.getInt("favorites", json);
        this.friends = ParseUtil.getInt("friends", json);
    }

    AccountTotalsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    AccountTotalsJSONImpl(JSONObject json) throws TwitterException {
        this((HttpResponse) null, json);
    }

    @Override // twitter4j.AccountTotals
    public int getUpdates() {
        return this.updates;
    }

    @Override // twitter4j.AccountTotals
    public int getFollowers() {
        return this.followers;
    }

    @Override // twitter4j.AccountTotals
    public int getFavorites() {
        return this.favorites;
    }

    @Override // twitter4j.AccountTotals
    public int getFriends() {
        return this.friends;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTotalsJSONImpl that = (AccountTotalsJSONImpl) o;
        return this.favorites == that.favorites && this.followers == that.followers && this.friends == that.friends && this.updates == that.updates;
    }

    public int hashCode() {
        int result = this.updates;
        return (((((result * 31) + this.followers) * 31) + this.favorites) * 31) + this.friends;
    }

    public String toString() {
        return new StringBuffer().append("AccountTotalsJSONImpl{updates=").append(this.updates).append(", followers=").append(this.followers).append(", favorites=").append(this.favorites).append(", friends=").append(this.friends).append('}').toString();
    }
}
