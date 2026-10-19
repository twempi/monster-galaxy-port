package twitter4j;

import java.io.Serializable;
import org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class StatusDeletionNoticeImpl implements StatusDeletionNotice, Serializable {
    private static final long serialVersionUID = 1723338404242596062L;
    private long statusId;
    private long userId;

    @Override // java.lang.Comparable
    public int compareTo(StatusDeletionNotice statusDeletionNotice) {
        return compareTo2(statusDeletionNotice);
    }

    StatusDeletionNoticeImpl(JSONObject status) {
        this.statusId = ParseUtil.getLong("id", status);
        this.userId = ParseUtil.getLong("user_id", status);
    }

    @Override // twitter4j.StatusDeletionNotice
    public long getStatusId() {
        return this.statusId;
    }

    @Override // twitter4j.StatusDeletionNotice
    public long getUserId() {
        return this.userId;
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(StatusDeletionNotice that) {
        long delta = this.statusId - that.getStatusId();
        if (delta < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (delta > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusDeletionNoticeImpl that = (StatusDeletionNoticeImpl) o;
        return this.statusId == that.statusId && this.userId == that.userId;
    }

    public int hashCode() {
        int result = (int) (this.statusId ^ (this.statusId >>> 32));
        return (result * 31) + ((int) (this.userId ^ (this.userId >>> 32)));
    }

    public String toString() {
        return new StringBuffer().append("StatusDeletionNoticeImpl{statusId=").append(this.statusId).append(", userId=").append(this.userId).append('}').toString();
    }
}
