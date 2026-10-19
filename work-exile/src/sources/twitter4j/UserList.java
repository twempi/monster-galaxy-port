package twitter4j;

import java.io.Serializable;
import java.net.URI;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface UserList extends Comparable<UserList>, TwitterResponse, Serializable {
    String getDescription();

    String getFullName();

    int getId();

    int getMemberCount();

    String getName();

    String getSlug();

    int getSubscriberCount();

    URI getURI();

    User getUser();

    boolean isFollowing();

    boolean isPublic();
}
