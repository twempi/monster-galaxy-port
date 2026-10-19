package twitter4j.api;

import java.io.File;
import java.io.InputStream;
import twitter4j.AccountSettings;
import twitter4j.AccountTotals;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.User;

/* JADX INFO: loaded from: classes.dex */
public interface AccountMethods {
    AccountSettings getAccountSettings() throws TwitterException;

    AccountTotals getAccountTotals() throws TwitterException;

    RateLimitStatus getRateLimitStatus() throws TwitterException;

    User updateProfile(String str, String str2, String str3, String str4) throws TwitterException;

    User updateProfileBackgroundImage(File file, boolean z) throws TwitterException;

    User updateProfileBackgroundImage(InputStream inputStream, boolean z) throws TwitterException;

    User updateProfileColors(String str, String str2, String str3, String str4, String str5) throws TwitterException;

    User updateProfileImage(File file) throws TwitterException;

    User updateProfileImage(InputStream inputStream) throws TwitterException;

    User verifyCredentials() throws TwitterException;
}
