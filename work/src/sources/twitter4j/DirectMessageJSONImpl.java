package twitter4j;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
final class DirectMessageJSONImpl extends TwitterResponseImpl implements DirectMessage, Serializable {
    private static final long serialVersionUID = -7104233663827757577L;
    private Date createdAt;
    private long id;
    private User recipient;
    private long recipientId;
    private String recipientScreenName;
    private User sender;
    private long senderId;
    private String senderScreenName;
    private String text;

    DirectMessageJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    DirectMessageJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        this.id = ParseUtil.getLong("id", json);
        this.text = ParseUtil.getUnescapedString("text", json);
        this.senderId = ParseUtil.getLong("sender_id", json);
        this.recipientId = ParseUtil.getLong("recipient_id", json);
        this.createdAt = ParseUtil.getDate("created_at", json);
        this.senderScreenName = ParseUtil.getUnescapedString("sender_screen_name", json);
        this.recipientScreenName = ParseUtil.getUnescapedString("recipient_screen_name", json);
        try {
            this.sender = new UserJSONImpl(json.getJSONObject("sender"));
            this.recipient = new UserJSONImpl(json.getJSONObject("recipient"));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.DirectMessage
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.DirectMessage
    public String getText() {
        return this.text;
    }

    @Override // twitter4j.DirectMessage
    public long getSenderId() {
        return this.senderId;
    }

    @Override // twitter4j.DirectMessage
    public long getRecipientId() {
        return this.recipientId;
    }

    @Override // twitter4j.DirectMessage
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override // twitter4j.DirectMessage
    public String getSenderScreenName() {
        return this.senderScreenName;
    }

    @Override // twitter4j.DirectMessage
    public String getRecipientScreenName() {
        return this.recipientScreenName;
    }

    @Override // twitter4j.DirectMessage
    public User getSender() {
        return this.sender;
    }

    @Override // twitter4j.DirectMessage
    public User getRecipient() {
        return this.recipient;
    }

    static ResponseList<DirectMessage> createDirectMessageList(HttpResponse res, Configuration conf) throws Throwable {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<DirectMessage> directMessages = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                DirectMessageJSONImpl directMessageJSONImpl = new DirectMessageJSONImpl(json);
                directMessages.add(directMessageJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(directMessageJSONImpl, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(directMessages, list);
            }
            return directMessages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int hashCode() {
        return (int) this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this != obj) {
            return (obj instanceof DirectMessage) && ((DirectMessage) obj).getId() == this.id;
        }
        return true;
    }

    public String toString() {
        return new StringBuffer().append("DirectMessageJSONImpl{id=").append(this.id).append(", text='").append(this.text).append('\'').append(", sender_id=").append(this.senderId).append(", recipient_id=").append(this.recipientId).append(", created_at=").append(this.createdAt).append(", sender_screen_name='").append(this.senderScreenName).append('\'').append(", recipient_screen_name='").append(this.recipientScreenName).append('\'').append(", sender=").append(this.sender).append(", recipient=").append(this.recipient).append('}').toString();
    }
}
