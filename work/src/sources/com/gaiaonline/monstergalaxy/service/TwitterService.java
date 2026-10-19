package com.gaiaonline.monstergalaxy.service;

/* JADX INFO: loaded from: classes.dex */
public interface TwitterService {
    public static final String OAUTH_KEY = "DKkEKCGfcMcGMowA7YzDQ";
    public static final String OAUTH_SECRET = "BcHPoyaedzBY7eHoMECjXWJkDFhWAuLS82qb7fcVEQ";

    boolean authorize(Runnable runnable);

    void closeView();

    boolean isAuthorized();

    boolean isViewOpen();

    boolean updateStatus(String str);
}
