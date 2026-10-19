package twitter4j;

import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;

/* JADX INFO: loaded from: classes.dex */
public class SimilarPlacesImpl extends ResponseListImpl<Place> implements SimilarPlaces {
    private static final long serialVersionUID = -7897806745732767803L;
    private final String token;

    @Override // twitter4j.ResponseListImpl, twitter4j.TwitterResponse
    public int getAccessLevel() {
        return super.getAccessLevel();
    }

    @Override // twitter4j.ResponseListImpl, twitter4j.ResponseList
    public RateLimitStatus getFeatureSpecificRateLimitStatus() {
        return super.getFeatureSpecificRateLimitStatus();
    }

    @Override // twitter4j.ResponseListImpl, twitter4j.ResponseList, twitter4j.TwitterResponse
    public RateLimitStatus getRateLimitStatus() {
        return super.getRateLimitStatus();
    }

    SimilarPlacesImpl(ResponseList<Place> places, HttpResponse res, String token) {
        super(places.size(), res);
        addAll(places);
        this.token = token;
    }

    @Override // twitter4j.SimilarPlaces
    public String getToken() {
        return this.token;
    }

    static SimilarPlaces createSimilarPlaces(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = null;
        try {
            json = res.asJSONObject();
            JSONObject result = json.getJSONObject("result");
            return new SimilarPlacesImpl(PlaceJSONImpl.createPlaceList(result.getJSONArray("places"), res, conf), res, result.getString("token"));
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }
}
