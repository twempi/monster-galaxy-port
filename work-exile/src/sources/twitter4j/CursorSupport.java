package twitter4j;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface CursorSupport {
    public static final long START = -1;

    long getNextCursor();

    long getPreviousCursor();

    boolean hasNext();

    boolean hasPrevious();
}
