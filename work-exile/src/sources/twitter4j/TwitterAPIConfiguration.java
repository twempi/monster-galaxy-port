package twitter4j;

import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface TwitterAPIConfiguration extends TwitterResponse {
    int getCharactersReservedPerMedia();

    int getMaxMediaPerUpload();

    String[] getNonUsernamePaths();

    int getPhotoSizeLimit();

    Map<Integer, MediaEntity.Size> getPhotoSizes();

    int getShortURLLength();

    int getShortURLLengthHttps();
}
