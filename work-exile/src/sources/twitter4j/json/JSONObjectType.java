package twitter4j.json;

import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.internal.logging.Logger;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class JSONObjectType implements Serializable {
    public static final JSONObjectType BLOCK;
    public static final JSONObjectType DELETE;
    public static final JSONObjectType DIRECT_MESSAGE;
    public static final JSONObjectType FAVORITE;
    public static final JSONObjectType FOLLOW;
    public static final JSONObjectType FRIENDS;
    public static final JSONObjectType LIMIT;
    public static final JSONObjectType RETWEET;
    public static final JSONObjectType SCRUB_GEO;
    public static final JSONObjectType SENDER;
    public static final JSONObjectType STATUS;
    public static final JSONObjectType UNBLOCK;
    public static final JSONObjectType UNFAVORITE;
    public static final JSONObjectType UNFOLLOW;
    public static final JSONObjectType USER_LIST_CREATED;
    public static final JSONObjectType USER_LIST_DESTROYED;
    public static final JSONObjectType USER_LIST_MEMBER_ADDED;
    public static final JSONObjectType USER_LIST_MEMBER_DELETED;
    public static final JSONObjectType USER_LIST_SUBSCRIBED;
    public static final JSONObjectType USER_LIST_UNSUBSCRIBED;
    public static final JSONObjectType USER_LIST_UPDATED;
    public static final JSONObjectType USER_UPDATE;
    static Class class$twitter4j$json$JSONObjectType = null;
    private static final Logger logger;
    private static final long serialVersionUID = -4487565183481849892L;
    private final String name;

    static {
        Class clsClass$;
        if (class$twitter4j$json$JSONObjectType == null) {
            clsClass$ = class$("twitter4j.json.JSONObjectType");
            class$twitter4j$json$JSONObjectType = clsClass$;
        } else {
            clsClass$ = class$twitter4j$json$JSONObjectType;
        }
        logger = Logger.getLogger(clsClass$);
        SENDER = new JSONObjectType("SENDER");
        STATUS = new JSONObjectType("STATUS");
        DIRECT_MESSAGE = new JSONObjectType("DIRECT_MESSAGE");
        DELETE = new JSONObjectType(HttpEngine.DELETE);
        LIMIT = new JSONObjectType("LIMIT");
        SCRUB_GEO = new JSONObjectType("SCRUB_GEO");
        FRIENDS = new JSONObjectType("FRIENDS");
        FAVORITE = new JSONObjectType("FAVORITE");
        UNFAVORITE = new JSONObjectType("UNFAVORITE");
        RETWEET = new JSONObjectType("RETWEET");
        FOLLOW = new JSONObjectType("FOLLOW");
        UNFOLLOW = new JSONObjectType("UNFOLLOW");
        USER_LIST_MEMBER_ADDED = new JSONObjectType("USER_LIST_MEMBER_ADDED");
        USER_LIST_MEMBER_DELETED = new JSONObjectType("USER_LIST_MEMBER_DELETED");
        USER_LIST_SUBSCRIBED = new JSONObjectType("USER_LIST_SUBSCRIBED");
        USER_LIST_UNSUBSCRIBED = new JSONObjectType("USER_LIST_UNSUBSCRIBED");
        USER_LIST_CREATED = new JSONObjectType("USER_LIST_CREATED");
        USER_LIST_UPDATED = new JSONObjectType("USER_LIST_UPDATED");
        USER_LIST_DESTROYED = new JSONObjectType("USER_LIST_DESTROYED");
        USER_UPDATE = new JSONObjectType("USER_UPDATE");
        BLOCK = new JSONObjectType("BLOCK");
        UNBLOCK = new JSONObjectType("UNBLOCK");
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private JSONObjectType() {
        throw new AssertionError();
    }

    private JSONObjectType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static JSONObjectType determine(JSONObject json) {
        JSONObjectType jSONObjectType;
        if (!json.isNull("sender")) {
            return SENDER;
        }
        if (!json.isNull("text")) {
            return STATUS;
        }
        if (!json.isNull("direct_message")) {
            return DIRECT_MESSAGE;
        }
        if (!json.isNull("delete")) {
            return DELETE;
        }
        if (!json.isNull("limit")) {
            return LIMIT;
        }
        if (!json.isNull("scrub_geo")) {
            return SCRUB_GEO;
        }
        if (!json.isNull("friends")) {
            return FRIENDS;
        }
        if (!json.isNull("event")) {
            try {
                String event = json.getString("event");
                if ("favorite".equals(event)) {
                    jSONObjectType = FAVORITE;
                } else if ("unfavorite".equals(event)) {
                    jSONObjectType = UNFAVORITE;
                } else if ("retweet".equals(event)) {
                    jSONObjectType = RETWEET;
                } else if ("follow".equals(event)) {
                    jSONObjectType = FOLLOW;
                } else if ("unfollow".equals(event)) {
                    jSONObjectType = UNFOLLOW;
                } else if (event.startsWith("list")) {
                    if ("list_member_added".equals(event)) {
                        jSONObjectType = USER_LIST_MEMBER_ADDED;
                    } else if ("list_member_removed".equals(event)) {
                        jSONObjectType = USER_LIST_MEMBER_DELETED;
                    } else if ("list_user_subscribed".equals(event)) {
                        jSONObjectType = USER_LIST_SUBSCRIBED;
                    } else if ("list_user_unsubscribed".equals(event)) {
                        jSONObjectType = USER_LIST_UNSUBSCRIBED;
                    } else if ("list_created".equals(event)) {
                        jSONObjectType = USER_LIST_CREATED;
                    } else if ("list_updated".equals(event)) {
                        jSONObjectType = USER_LIST_UPDATED;
                    } else if ("list_destroyed".equals(event)) {
                        jSONObjectType = USER_LIST_DESTROYED;
                    }
                } else if ("user_update".equals(event)) {
                    jSONObjectType = USER_UPDATE;
                } else if ("block".equals(event)) {
                    jSONObjectType = BLOCK;
                } else if ("unblock".equals(event)) {
                    jSONObjectType = UNBLOCK;
                }
                return jSONObjectType;
            } catch (JSONException e) {
                try {
                    logger.warn("Failed to get event element: ", json.toString(2));
                } catch (JSONException e2) {
                }
            }
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JSONObjectType that = (JSONObjectType) o;
        if (this.name != null) {
            if (this.name.equals(that.name)) {
                return true;
            }
        } else if (that.name == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.name != null) {
            return this.name.hashCode();
        }
        return 0;
    }

    public String toString() {
        return this.name;
    }
}
