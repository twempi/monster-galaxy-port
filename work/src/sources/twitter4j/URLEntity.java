package twitter4j;

import java.io.Serializable;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
public interface URLEntity extends Serializable {
    String getDisplayURL();

    int getEnd();

    URL getExpandedURL();

    int getStart();

    URL getURL();
}
