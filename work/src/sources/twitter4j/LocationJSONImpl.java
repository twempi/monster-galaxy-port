package twitter4j;

import com.sessionm.ui.SessionMActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
final class LocationJSONImpl implements Location {
    private static final long serialVersionUID = 7095092358530897222L;
    private final String countryCode;
    private final String countryName;
    private final String name;
    private final int placeCode;
    private final String placeName;
    private final String url;
    private final int woeid;

    LocationJSONImpl(JSONObject location) throws TwitterException {
        try {
            this.woeid = ParseUtil.getInt("woeid", location);
            this.countryName = ParseUtil.getUnescapedString("country", location);
            this.countryCode = ParseUtil.getRawString("countryCode", location);
            if (!location.isNull("placeType")) {
                JSONObject placeJSON = location.getJSONObject("placeType");
                this.placeName = ParseUtil.getUnescapedString("name", placeJSON);
                this.placeCode = ParseUtil.getInt("code", placeJSON);
            } else {
                this.placeName = null;
                this.placeCode = -1;
            }
            this.name = ParseUtil.getUnescapedString("name", location);
            this.url = ParseUtil.getUnescapedString(SessionMActivity.INTENT_PARAM_URL, location);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    static ResponseList<Location> createLocationList(HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        return createLocationList(res.asJSONArray(), conf.isJSONStoreEnabled());
    }

    static ResponseList<Location> createLocationList(JSONArray list, boolean storeJSON) throws TwitterException {
        try {
            int size = list.length();
            ResponseList<Location> locations = new ResponseListImpl<>(size, null);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                LocationJSONImpl locationJSONImpl = new LocationJSONImpl(json);
                locations.add(locationJSONImpl);
                if (storeJSON) {
                    DataObjectFactoryUtil.registerJSONObject(locationJSONImpl, json);
                }
            }
            if (storeJSON) {
                DataObjectFactoryUtil.registerJSONObject(locations, list);
            }
            return locations;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override // twitter4j.Location
    public int getWoeid() {
        return this.woeid;
    }

    @Override // twitter4j.Location
    public String getCountryName() {
        return this.countryName;
    }

    @Override // twitter4j.Location
    public String getCountryCode() {
        return this.countryCode;
    }

    @Override // twitter4j.Location
    public String getPlaceName() {
        return this.placeName;
    }

    @Override // twitter4j.Location
    public int getPlaceCode() {
        return this.placeCode;
    }

    @Override // twitter4j.Location
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.Location
    public String getURL() {
        return this.url;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationJSONImpl)) {
            return false;
        }
        LocationJSONImpl that = (LocationJSONImpl) o;
        return this.woeid == that.woeid;
    }

    public int hashCode() {
        return this.woeid;
    }

    public String toString() {
        return new StringBuffer().append("LocationJSONImpl{woeid=").append(this.woeid).append(", countryName='").append(this.countryName).append('\'').append(", countryCode='").append(this.countryCode).append('\'').append(", placeName='").append(this.placeName).append('\'').append(", placeCode='").append(this.placeCode).append('\'').append(", name='").append(this.name).append('\'').append(", url='").append(this.url).append('\'').append('}').toString();
    }
}
