package twitter4j;

import twitter4j.internal.http.HttpResponse;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class ProfileImageImpl extends TwitterResponseImpl implements ProfileImage {
    private static final long serialVersionUID = -3710458112877311569L;
    private String url;

    ProfileImageImpl(HttpResponse res) {
        super(res);
        this.url = res.getResponseHeader("Location");
    }

    @Override // twitter4j.ProfileImage
    public String getURL() {
        return this.url;
    }
}
