package twitter4j.api;

import java.io.File;
import java.io.InputStream;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface AccountMethodsAsync {
    void getAccountSettings();

    void getAccountTotals();

    void getRateLimitStatus();

    void updateProfile(String str, String str2, String str3, String str4);

    void updateProfileBackgroundImage(File file, boolean z);

    void updateProfileBackgroundImage(InputStream inputStream, boolean z);

    void updateProfileColors(String str, String str2, String str3, String str4, String str5);

    void updateProfileImage(File file);

    void updateProfileImage(InputStream inputStream);

    void verifyCredentials();
}
