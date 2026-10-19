package twitter4j;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public interface DirectMessage extends TwitterResponse, Serializable {
    Date getCreatedAt();

    long getId();

    User getRecipient();

    long getRecipientId();

    String getRecipientScreenName();

    User getSender();

    long getSenderId();

    String getSenderScreenName();

    String getText();
}
