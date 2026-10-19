package twitter4j;

import org.json.JSONObject;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class PagableResponseListImpl<T> extends ResponseListImpl implements PagableResponseList {
    private static final long serialVersionUID = 1531950333538983361L;
    private final long nextCursor;
    private final long previousCursor;

    PagableResponseListImpl(int size, JSONObject json, HttpResponse res) {
        super(size, res);
        this.previousCursor = ParseUtil.getLong("previous_cursor", json);
        this.nextCursor = ParseUtil.getLong("next_cursor", json);
    }

    @Override // twitter4j.PagableResponseList, twitter4j.CursorSupport
    public boolean hasPrevious() {
        return 0 != this.previousCursor;
    }

    @Override // twitter4j.PagableResponseList, twitter4j.CursorSupport
    public long getPreviousCursor() {
        return this.previousCursor;
    }

    @Override // twitter4j.PagableResponseList, twitter4j.CursorSupport
    public boolean hasNext() {
        return 0 != this.nextCursor;
    }

    @Override // twitter4j.PagableResponseList, twitter4j.CursorSupport
    public long getNextCursor() {
        return this.nextCursor;
    }
}
