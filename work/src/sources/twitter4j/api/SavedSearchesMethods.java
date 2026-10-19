package twitter4j.api;

import java.util.List;
import twitter4j.SavedSearch;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: classes.dex */
public interface SavedSearchesMethods {
    SavedSearch createSavedSearch(String str) throws TwitterException;

    SavedSearch destroySavedSearch(int i) throws TwitterException;

    List<SavedSearch> getSavedSearches() throws TwitterException;

    SavedSearch showSavedSearch(int i) throws TwitterException;
}
