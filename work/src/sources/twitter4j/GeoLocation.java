package twitter4j;

import com.millennialmedia.android.MMAdViewSDK;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: classes.dex */
public class GeoLocation implements Serializable {
    private static final long serialVersionUID = -4847567157651889935L;
    protected double latitude;
    protected double longitude;

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    static GeoLocation getInstance(JSONObject json) throws TwitterException {
        try {
            if (!json.isNull(MMAdViewSDK.Event.INTENT_MAPS)) {
                String coordinates = json.getJSONObject(MMAdViewSDK.Event.INTENT_MAPS).getString("coordinates");
                String[] point = T4JInternalStringUtil.split(coordinates.substring(1, coordinates.length() - 1), ",");
                return new GeoLocation(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
            }
            return null;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    static GeoLocation[][] coordinatesAsGeoLocationArray(JSONArray coordinates) throws TwitterException {
        try {
            GeoLocation[][] boundingBox = new GeoLocation[coordinates.length()][];
            for (int i = 0; i < coordinates.length(); i++) {
                JSONArray array = coordinates.getJSONArray(i);
                boundingBox[i] = new GeoLocation[array.length()];
                for (int j = 0; j < array.length(); j++) {
                    JSONArray coordinate = array.getJSONArray(j);
                    boundingBox[i][j] = new GeoLocation(coordinate.getDouble(1), coordinate.getDouble(0));
                }
            }
            return boundingBox;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoLocation)) {
            return false;
        }
        GeoLocation that = (GeoLocation) o;
        return Double.compare(that.getLatitude(), this.latitude) == 0 && Double.compare(that.getLongitude(), this.longitude) == 0;
    }

    public int hashCode() {
        long temp = this.latitude != 0.0d ? Double.doubleToLongBits(this.latitude) : 0L;
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = this.longitude != 0.0d ? Double.doubleToLongBits(this.longitude) : 0L;
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public String toString() {
        return new StringBuffer().append("GeoLocation{latitude=").append(this.latitude).append(", longitude=").append(this.longitude).append('}').toString();
    }
}
