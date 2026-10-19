package com.sessionm.a;

import com.kontagent.Kontagent;
import java.util.HashMap;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public final class b {
    public static final String A = "api.action.events";
    public static final String B = "app.running.mode";
    public static final String C = "app.id";
    public static final String D = "app.cache.path";
    public static final String E = "app.optout.path";
    public static final String F = "app.logging.enable";
    public static final String G = "app.logging.level";
    public static final String H = "app.bugs.orientation";
    public static final String I = "sdk.revision";
    public static final String J = "sdk.version";
    public static final String K = "status";
    public static final String L = "_session";
    public static final String M = "cookie";
    public static final String N = "ok";
    public static final String O = "uuid";
    public static final String P = "forecast events";
    public static final String Q = "sdk.logging.url";
    public static final String R = "sdk.remote.debugger.url";
    public static final int S = 1989;
    public static HashMap<String, String> T = new HashMap<>();
    public static final String b = "api.server.url";
    public static final String c = "api.server.url.test";
    public static final String d = "api.server.url.dev";
    public static final String e = "api.server.url.staging";
    public static final String f = "api.server.url.production";
    public static final String g = "portal.server.url";
    public static final String h = "portal.server.url.test";
    public static final String i = "portal.server.url.dev";
    public static final String j = "portal.server.url.staging";
    public static final String k = "portal.server.url.production";
    public static final String l = "ads.server.url";
    public static final String m = "ads.server.url.test";
    public static final String n = "ads.server.url.dev";
    public static final String o = "ads.server.url.staging";
    public static final String p = "api.server.mode";
    public static final String q = "api.apps.path";
    public static final String r = "api.session.path";
    public static final String s = "api.bugs.path";
    public static final String t = "api.achievements.path";
    public static final String u = "api.awards.path";
    public static final String v = "api.errors.path";
    public static final String w = "api.key";
    public static final String x = "api.version";
    public static final String y = "api.action.session";
    public static final String z = "api.action.awards";

    private b() {
    }

    static {
        String str = String.format("v%s", "4");
        T.put(J, "1.2.2");
        T.put(I, "b6ae6ef0339c8166a6b1a3932025b89d18142ee1");
        T.put(c, "http://m.tb.sessionm.com");
        T.put(b, "https://api.sessionm.com");
        T.put(d, "https://api.tb.sessionm.com");
        T.put(e, "https://m.s.sessionm.com");
        T.put(f, "https://api.sessionm.com");
        T.put(h, "http://portal.tb.sessionm.com");
        T.put(g, "https://portal.sessionm.com");
        T.put(i, "https://portal.tb.sessionm.com");
        T.put(j, "https://m.s.sessionm.com");
        T.put(k, "https://portal.sessionm.com");
        T.put(t, str + "/user.json");
        T.put(E, "optout.json");
        T.put(p, Kontagent.PRODUCTION_MODE);
        T.put(q, str + "/apps");
        T.put(r, str + "/session");
        T.put(l, "https://ads.sessionm.com/transactions.json");
        T.put(m, "https://ads.tb.sessionm.com/transactions.json");
        T.put(o, "https://m.s.sessionm.com/transactions.json");
        T.put(n, "https://ads.tb.sessionm.com/transactions.json");
        T.put(s, str + "/bugs");
        T.put(u, "");
        T.put(w, "");
        T.put(x, "4");
        T.put(y, "session.json");
        T.put(z, str + "/awards.json");
        T.put(A, str + "/events.json");
        T.put(v, str + "/errors.json");
        T.put(B, Kontagent.PRODUCTION_MODE);
        T.put(C, "NONE");
        T.put(D, "/data/data/com.sessionm.sdk/cache");
        T.put(F, "true");
        T.put(G, Kontagent.PRODUCTION_MODE);
        T.put(R, "");
    }
}
