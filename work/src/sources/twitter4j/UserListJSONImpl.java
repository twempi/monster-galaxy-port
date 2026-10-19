package twitter4j;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class UserListJSONImpl extends TwitterResponseImpl implements UserList, Serializable {
    private static final long serialVersionUID = -6345893237975349030L;
    private String description;
    private boolean following;
    private String fullName;
    private int id;
    private int memberCount;
    private boolean mode;
    private String name;
    private String slug;
    private int subscriberCount;
    private String uri;
    private User user;

    @Override // java.lang.Comparable
    public int compareTo(UserList userList) {
        return compareTo2(userList);
    }

    UserListJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    UserListJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        this.id = ParseUtil.getInt("id", json);
        this.name = ParseUtil.getRawString("name", json);
        this.fullName = ParseUtil.getRawString("full_name", json);
        this.slug = ParseUtil.getRawString("slug", json);
        this.description = ParseUtil.getRawString("description", json);
        this.subscriberCount = ParseUtil.getInt("subscriber_count", json);
        this.memberCount = ParseUtil.getInt("member_count", json);
        this.uri = ParseUtil.getRawString("uri", json);
        this.mode = "public".equals(ParseUtil.getRawString("mode", json));
        this.following = ParseUtil.getBoolean("following", json);
        try {
            if (!json.isNull(PropertyConfiguration.USER)) {
                this.user = new UserJSONImpl(json.getJSONObject(PropertyConfiguration.USER));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(UserList that) {
        return this.id - that.getId();
    }

    @Override // twitter4j.UserList
    public int getId() {
        return this.id;
    }

    @Override // twitter4j.UserList
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.UserList
    public String getFullName() {
        return this.fullName;
    }

    @Override // twitter4j.UserList
    public String getSlug() {
        return this.slug;
    }

    @Override // twitter4j.UserList
    public String getDescription() {
        return this.description;
    }

    @Override // twitter4j.UserList
    public int getSubscriberCount() {
        return this.subscriberCount;
    }

    @Override // twitter4j.UserList
    public int getMemberCount() {
        return this.memberCount;
    }

    @Override // twitter4j.UserList
    public URI getURI() {
        try {
            return new URI(this.uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override // twitter4j.UserList
    public boolean isPublic() {
        return this.mode;
    }

    @Override // twitter4j.UserList
    public boolean isFollowing() {
        return this.following;
    }

    @Override // twitter4j.UserList
    public User getUser() {
        return this.user;
    }

    static PagableResponseList<UserList> createPagableUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("lists");
            int size = list.length();
            PagableResponseList<UserList> users = new PagableResponseListImpl<>(size, json, res);
            for (int i = 0; i < size; i++) {
                JSONObject userListJson = list.getJSONObject(i);
                UserList userList = new UserListJSONImpl(userListJson);
                users.add(userList);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    static ResponseList<UserList> createUserListList(HttpResponse res, Configuration conf) throws Throwable {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<UserList> users = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject userListJson = list.getJSONObject(i);
                UserListJSONImpl userListJSONImpl = new UserListJSONImpl(userListJson);
                users.add(userListJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userListJSONImpl, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, list);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this != obj) {
            return (obj instanceof UserList) && ((UserList) obj).getId() == this.id;
        }
        return true;
    }

    public String toString() {
        return new StringBuffer().append("UserListJSONImpl{id=").append(this.id).append(", name='").append(this.name).append('\'').append(", fullName='").append(this.fullName).append('\'').append(", slug='").append(this.slug).append('\'').append(", description='").append(this.description).append('\'').append(", subscriberCount=").append(this.subscriberCount).append(", memberCount=").append(this.memberCount).append(", uri='").append(this.uri).append('\'').append(", mode=").append(this.mode).append(", user=").append(this.user).append(", following=").append(this.following).append('}').toString();
    }
}
