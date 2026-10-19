package twitter4j;

import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class AccountSettingsJSONImpl extends TwitterResponseImpl implements AccountSettings, Serializable {
    private static final long serialVersionUID = 7983363611306383416L;
    private final boolean GEO_ENABLED;
    private final String SLEEP_END_TIME;
    private final String SLEEP_START_TIME;
    private final boolean SLEEP_TIME_ENABLED;
    private final Location[] TREND_LOCATION;

    private AccountSettingsJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        try {
            JSONObject sleepTime = json.getJSONObject("sleep_time");
            this.SLEEP_TIME_ENABLED = ParseUtil.getBoolean("enabled", sleepTime);
            this.SLEEP_START_TIME = sleepTime.getString("start_time");
            this.SLEEP_END_TIME = sleepTime.getString("end_time");
            if (json.isNull("trend_location")) {
                this.TREND_LOCATION = new Location[0];
            } else {
                JSONArray locations = json.getJSONArray("trend_location");
                this.TREND_LOCATION = new Location[locations.length()];
                for (int i = 0; i < locations.length(); i++) {
                    this.TREND_LOCATION[i] = new LocationJSONImpl(locations.getJSONObject(i));
                }
            }
            this.GEO_ENABLED = ParseUtil.getBoolean("geo_enabled", json);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    AccountSettingsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    AccountSettingsJSONImpl(JSONObject json) throws TwitterException {
        this((HttpResponse) null, json);
    }

    @Override // twitter4j.AccountSettings
    public boolean isSleepTimeEnabled() {
        return this.SLEEP_TIME_ENABLED;
    }

    @Override // twitter4j.AccountSettings
    public String getSleepStartTime() {
        return this.SLEEP_START_TIME;
    }

    @Override // twitter4j.AccountSettings
    public String getSleepEndTime() {
        return this.SLEEP_END_TIME;
    }

    @Override // twitter4j.AccountSettings
    public Location[] getTrendLocations() {
        return this.TREND_LOCATION;
    }

    @Override // twitter4j.AccountSettings
    public boolean isGeoEnabled() {
        return this.GEO_ENABLED;
    }
}
