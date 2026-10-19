package twitter4j;

import com.sessionm.a.b;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class StatusUpdate implements Serializable {
    private static final long serialVersionUID = -3595502688477609916L;
    private String status;
    private long inReplyToStatusId = -1;
    private GeoLocation location = null;
    private String placeId = null;
    private boolean displayCoordinates = true;
    private Annotations annotations = null;

    public StatusUpdate(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public long getInReplyToStatusId() {
        return this.inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public StatusUpdate inReplyToStatusId(long inReplyToStatusId) {
        setInReplyToStatusId(inReplyToStatusId);
        return this;
    }

    public GeoLocation getLocation() {
        return this.location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public StatusUpdate location(GeoLocation location) {
        setLocation(location);
        return this;
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public StatusUpdate placeId(String placeId) {
        setPlaceId(placeId);
        return this;
    }

    public boolean isDisplayCoordinates() {
        return this.displayCoordinates;
    }

    public void setDisplayCoordinates(boolean displayCoordinates) {
        this.displayCoordinates = displayCoordinates;
    }

    public StatusUpdate displayCoordinates(boolean displayCoordinates) {
        setDisplayCoordinates(displayCoordinates);
        return this;
    }

    public Annotations getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public StatusUpdate annotations(Annotations annotations) {
        setAnnotations(annotations);
        return this;
    }

    public void addAnnotation(Annotation annotation) {
        if (this.annotations == null) {
            this.annotations = new Annotations();
        }
        this.annotations.addAnnotation(annotation);
    }

    public StatusUpdate annotation(Annotation annotation) {
        addAnnotation(annotation);
        return this;
    }

    HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        appendParameter(b.K, this.status, params);
        if (-1 != this.inReplyToStatusId) {
            appendParameter("in_reply_to_status_id", this.inReplyToStatusId, (List<HttpParameter>) params);
        }
        if (this.location != null) {
            appendParameter("lat", this.location.getLatitude(), params);
            appendParameter("long", this.location.getLongitude(), params);
        }
        appendParameter("place_id", this.placeId, params);
        if (!this.displayCoordinates) {
            appendParameter("display_coordinates", "false", params);
        }
        if (this.annotations != null && !this.annotations.isEmpty()) {
            appendParameter("annotations", this.annotations.asParameterValue(), params);
        }
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return (HttpParameter[]) params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusUpdate that = (StatusUpdate) o;
        if (this.displayCoordinates == that.displayCoordinates && this.inReplyToStatusId == that.inReplyToStatusId) {
            if (this.location == null ? that.location != null : !this.location.equals(that.location)) {
                return false;
            }
            if (this.placeId == null ? that.placeId != null : !this.placeId.equals(that.placeId)) {
                return false;
            }
            if (this.annotations == null ? that.annotations != null : !this.annotations.equals(that.annotations)) {
                return false;
            }
            return this.status.equals(that.status);
        }
        return false;
    }

    public int hashCode() {
        int result = this.status.hashCode();
        return (((((((((result * 31) + ((int) (this.inReplyToStatusId ^ (this.inReplyToStatusId >>> 32)))) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.placeId != null ? this.placeId.hashCode() : 0)) * 31) + (this.displayCoordinates ? 1 : 0)) * 31) + (this.annotations != null ? this.annotations.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("StatusUpdate{status='").append(this.status).append('\'').append(", inReplyToStatusId=").append(this.inReplyToStatusId).append(", location=").append(this.location).append(", placeId='").append(this.placeId).append('\'').append(", displayCoordinates=").append(this.displayCoordinates).append(", annotations=").append(this.annotations).append('}').toString();
    }
}
