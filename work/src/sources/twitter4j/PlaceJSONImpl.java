package twitter4j;

import com.sessionm.ui.SessionMActivity;
import com.tapjoy.TapjoyConstants;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
final class PlaceJSONImpl extends TwitterResponseImpl implements Place, Serializable {
    private static final long serialVersionUID = -2873364341474633812L;
    private GeoLocation[][] boundingBoxCoordinates;
    private String boundingBoxType;
    private Place[] containedWithIn;
    private String country;
    private String countryCode;
    private String fullName;
    private GeoLocation[][] geometryCoordinates;
    private String geometryType;
    private String id;
    private String name;
    private String placeType;
    private String streetAddress;
    private String url;

    @Override // java.lang.Comparable
    public int compareTo(Place place) {
        return compareTo2(place);
    }

    PlaceJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    PlaceJSONImpl(JSONObject json, HttpResponse res) throws TwitterException {
        super(res);
        init(json);
    }

    PlaceJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.name = ParseUtil.getUnescapedString("name", json);
            this.streetAddress = ParseUtil.getUnescapedString("street_address", json);
            this.countryCode = ParseUtil.getRawString(TapjoyConstants.TJC_DEVICE_COUNTRY_CODE, json);
            this.id = ParseUtil.getRawString("id", json);
            this.country = ParseUtil.getRawString("country", json);
            if (!json.isNull("place_type")) {
                this.placeType = ParseUtil.getRawString("place_type", json);
            } else {
                this.placeType = ParseUtil.getRawString(SessionMActivity.INTENT_PARAM_TYPE, json);
            }
            this.url = ParseUtil.getRawString(SessionMActivity.INTENT_PARAM_URL, json);
            this.fullName = ParseUtil.getRawString("full_name", json);
            if (!json.isNull("bounding_box")) {
                JSONObject boundingBoxJSON = json.getJSONObject("bounding_box");
                this.boundingBoxType = ParseUtil.getRawString(SessionMActivity.INTENT_PARAM_TYPE, boundingBoxJSON);
                this.boundingBoxCoordinates = GeoLocation.coordinatesAsGeoLocationArray(boundingBoxJSON.getJSONArray("coordinates"));
            } else {
                this.boundingBoxType = null;
                this.boundingBoxCoordinates = (GeoLocation[][]) null;
            }
            if (!json.isNull("geometry")) {
                JSONObject geometryJSON = json.getJSONObject("geometry");
                this.geometryType = ParseUtil.getRawString(SessionMActivity.INTENT_PARAM_TYPE, geometryJSON);
                JSONArray array = geometryJSON.getJSONArray("coordinates");
                if (this.geometryType.equals("Point")) {
                    this.geometryCoordinates = (GeoLocation[][]) Array.newInstance((Class<?>) GeoLocation.class, 1, 1);
                    this.geometryCoordinates[0][0] = new GeoLocation(array.getDouble(0), array.getDouble(1));
                } else if (this.geometryType.equals("Polygon")) {
                    this.geometryCoordinates = GeoLocation.coordinatesAsGeoLocationArray(array);
                } else {
                    this.geometryType = null;
                    this.geometryCoordinates = (GeoLocation[][]) null;
                }
            } else {
                this.geometryType = null;
                this.geometryCoordinates = (GeoLocation[][]) null;
            }
            if (!json.isNull("contained_within")) {
                JSONArray containedWithInJSON = json.getJSONArray("contained_within");
                this.containedWithIn = new Place[containedWithInJSON.length()];
                for (int i = 0; i < containedWithInJSON.length(); i++) {
                    this.containedWithIn[i] = new PlaceJSONImpl(containedWithInJSON.getJSONObject(i));
                }
                return;
            }
            this.containedWithIn = null;
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(Place that) {
        return this.id.compareTo(that.getId());
    }

    static ResponseList<Place> createPlaceList(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = null;
        try {
            json = res.asJSONObject();
            return createPlaceList(json.getJSONObject("result").getJSONArray("places"), res, conf);
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    static ResponseList<Place> createPlaceList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        try {
            int size = list.length();
            ResponseList<Place> places = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                PlaceJSONImpl placeJSONImpl = new PlaceJSONImpl(json);
                places.add(placeJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(placeJSONImpl, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(places, list);
            }
            return places;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override // twitter4j.Place
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.Place
    public String getStreetAddress() {
        return this.streetAddress;
    }

    @Override // twitter4j.Place
    public String getCountryCode() {
        return this.countryCode;
    }

    @Override // twitter4j.Place
    public String getId() {
        return this.id;
    }

    @Override // twitter4j.Place
    public String getCountry() {
        return this.country;
    }

    @Override // twitter4j.Place
    public String getPlaceType() {
        return this.placeType;
    }

    @Override // twitter4j.Place
    public String getURL() {
        return this.url;
    }

    @Override // twitter4j.Place
    public String getFullName() {
        return this.fullName;
    }

    @Override // twitter4j.Place
    public String getBoundingBoxType() {
        return this.boundingBoxType;
    }

    @Override // twitter4j.Place
    public GeoLocation[][] getBoundingBoxCoordinates() {
        return this.boundingBoxCoordinates;
    }

    @Override // twitter4j.Place
    public String getGeometryType() {
        return this.geometryType;
    }

    @Override // twitter4j.Place
    public GeoLocation[][] getGeometryCoordinates() {
        return this.geometryCoordinates;
    }

    @Override // twitter4j.Place
    public Place[] getContainedWithIn() {
        return this.containedWithIn;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this != obj) {
            return (obj instanceof Place) && ((Place) obj).getId().equals(this.id);
        }
        return true;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return new StringBuffer().append("PlaceJSONImpl{name='").append(this.name).append('\'').append(", streetAddress='").append(this.streetAddress).append('\'').append(", countryCode='").append(this.countryCode).append('\'').append(", id='").append(this.id).append('\'').append(", country='").append(this.country).append('\'').append(", placeType='").append(this.placeType).append('\'').append(", url='").append(this.url).append('\'').append(", fullName='").append(this.fullName).append('\'').append(", boundingBoxType='").append(this.boundingBoxType).append('\'').append(", boundingBoxCoordinates=").append(this.boundingBoxCoordinates == null ? null : Arrays.asList(this.boundingBoxCoordinates)).append(", geometryType='").append(this.geometryType).append('\'').append(", geometryCoordinates=").append(this.geometryCoordinates == null ? null : Arrays.asList(this.geometryCoordinates)).append(", containedWithIn=").append(this.containedWithIn != null ? Arrays.asList(this.containedWithIn) : null).append('}').toString();
    }
}
