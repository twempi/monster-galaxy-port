package twitter4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class FriendshipJSONImpl implements Friendship {
    private static final long serialVersionUID = 7724410837770709741L;
    private boolean followedBy;
    private boolean following;
    private final long id;
    private final String name;
    private final String screenName;

    FriendshipJSONImpl(JSONObject json) throws TwitterException {
        this.following = false;
        this.followedBy = false;
        try {
            this.id = ParseUtil.getLong("id", json);
            this.name = json.getString("name");
            this.screenName = json.getString("screen_name");
            JSONArray connections = json.getJSONArray("connections");
            for (int i = 0; i < connections.length(); i++) {
                String connection = connections.getString(i);
                if ("following".equals(connection)) {
                    this.following = true;
                } else if ("followed_by".equals(connection)) {
                    this.followedBy = true;
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    static ResponseList<Friendship> createFriendshipList(HttpResponse res, Configuration conf) throws Throwable {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Friendship> friendshipList = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                FriendshipJSONImpl friendshipJSONImpl = new FriendshipJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(friendshipJSONImpl, json);
                }
                friendshipList.add(friendshipJSONImpl);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(friendshipList, list);
            }
            return friendshipList;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override // twitter4j.Friendship
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.Friendship
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.Friendship
    public String getScreenName() {
        return this.screenName;
    }

    @Override // twitter4j.Friendship
    public boolean isFollowing() {
        return this.following;
    }

    @Override // twitter4j.Friendship
    public boolean isFollowedBy() {
        return this.followedBy;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendshipJSONImpl that = (FriendshipJSONImpl) o;
        return this.followedBy == that.followedBy && this.following == that.following && this.id == that.id && this.name.equals(that.name) && this.screenName.equals(that.screenName);
    }

    public int hashCode() {
        int result = (int) (this.id ^ (this.id >>> 32));
        return (((((((result * 31) + (this.name != null ? this.name.hashCode() : 0)) * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + (this.following ? 1 : 0)) * 31) + (this.followedBy ? 1 : 0);
    }

    public String toString() {
        return new StringBuffer().append("FriendshipJSONImpl{id=").append(this.id).append(", name='").append(this.name).append('\'').append(", screenName='").append(this.screenName).append('\'').append(", following=").append(this.following).append(", followedBy=").append(this.followedBy).append('}').toString();
    }
}
