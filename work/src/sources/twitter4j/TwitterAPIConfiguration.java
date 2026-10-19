package twitter4j;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface TwitterAPIConfiguration extends TwitterResponse {
    int getCharactersReservedPerMedia();

    int getMaxMediaPerUpload();

    String[] getNonUsernamePaths();

    int getPhotoSizeLimit();

    Map<Integer, MediaEntity.Size> getPhotoSizes();

    int getShortURLLength();

    int getShortURLLengthHttps();
}
