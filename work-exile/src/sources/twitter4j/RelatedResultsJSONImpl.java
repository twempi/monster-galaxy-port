package twitter4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class RelatedResultsJSONImpl extends TwitterResponseImpl implements RelatedResults, Serializable {
    private static final String TWEETS_FROM_USER = "TweetsFromUser";
    private static final String TWEETS_WITH_CONVERSATION = "TweetsWithConversation";
    private static final String TWEETS_WITH_REPLY = "TweetsWithReply";
    private static final long serialVersionUID = -7417061781993004083L;
    private Map<String, ResponseList<Status>> tweetsMap;

    RelatedResultsJSONImpl(HttpResponse res, Configuration conf) throws Throwable {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONArray jsonArray = res.asJSONArray();
        init(jsonArray, res, conf.isJSONStoreEnabled());
    }

    RelatedResultsJSONImpl(JSONArray jsonArray) throws TwitterException {
        init(jsonArray, null, false);
    }

    private void init(JSONArray jsonArray, HttpResponse res, boolean registerRawJSON) throws TwitterException {
        this.tweetsMap = new HashMap(2);
        try {
            int listLen = jsonArray.length();
            for (int i = 0; i < listLen; i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                if ("Tweet".equals(o.getString("resultType"))) {
                    String groupName = o.getString("groupName");
                    if (groupName.length() != 0 && (groupName.equals(TWEETS_WITH_CONVERSATION) || groupName.equals(TWEETS_WITH_REPLY) || groupName.equals(TWEETS_FROM_USER))) {
                        JSONArray results = o.getJSONArray("results");
                        ResponseList<Status> statuses = this.tweetsMap.get(groupName);
                        if (statuses == null) {
                            statuses = new ResponseListImpl<>(results.length(), res);
                            this.tweetsMap.put(groupName, statuses);
                        }
                        int resultsLen = results.length();
                        for (int j = 0; j < resultsLen; j++) {
                            JSONObject json = results.getJSONObject(j).getJSONObject("value");
                            StatusJSONImpl statusJSONImpl = new StatusJSONImpl(json);
                            if (registerRawJSON) {
                                DataObjectFactoryUtil.registerJSONObject(statusJSONImpl, json);
                            }
                            statuses.add(statusJSONImpl);
                        }
                        if (registerRawJSON) {
                            DataObjectFactoryUtil.registerJSONObject(statuses, results);
                        }
                    }
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.RelatedResults
    public ResponseList<Status> getTweetsWithConversation() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_WITH_CONVERSATION);
        return statuses != null ? statuses : new ResponseListImpl<>(0, null);
    }

    @Override // twitter4j.RelatedResults
    public ResponseList<Status> getTweetsWithReply() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_WITH_REPLY);
        return statuses != null ? statuses : new ResponseListImpl<>(0, null);
    }

    @Override // twitter4j.RelatedResults
    public ResponseList<Status> getTweetsFromUser() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_FROM_USER);
        return statuses != null ? statuses : new ResponseListImpl<>(0, null);
    }

    public int hashCode() {
        int result = this.tweetsMap.hashCode() + 31;
        return result;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RelatedResultsJSONImpl)) {
            return false;
        }
        RelatedResultsJSONImpl other = (RelatedResultsJSONImpl) obj;
        if (this.tweetsMap == null) {
            if (other.tweetsMap != null) {
                return false;
            }
        } else if (!this.tweetsMap.equals(other.tweetsMap)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return new StringBuffer().append("RelatedResultsJSONImpl {tweetsMap=").append(this.tweetsMap).append("}").toString();
    }
}
