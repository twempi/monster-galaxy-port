package com.sessionm.core;

import android.content.Context;
import android.util.Log;
import com.kontagent.Kontagent;
import com.sessionm.a.b;
import com.sessionm.api.Config;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import twitter4j.conf.PropertyConfiguration;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public final class SessionMAndroidConfig implements Config {
    public static final int SERVER_DEV = 1;
    public static final int SERVER_PROD = 3;
    public static final int SERVER_STAGE = 2;
    public static final int SERVER_TEST = 0;
    private static final String TAG = "SessionMAndroidConfig";
    private final Properties configImtems;

    public static SessionMAndroidConfig getInstance() {
        return Inner.instance;
    }

    /* JADX INFO: compiled from: ProGuard */
    private static class Inner {
        public static SessionMAndroidConfig instance = new SessionMAndroidConfig();

        private Inner() {
        }
    }

    private SessionMAndroidConfig() {
        this.configImtems = new Properties();
        try {
            Class.forName("com.sessionm.DevConfig");
            Log.i(TAG, "loading com.sessionm.DevConfig");
        } catch (ClassNotFoundException e) {
        }
        this.configImtems.putAll(b.T);
    }

    public static int getResId(String str, Context context, Class<?> cls) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            return declaredField.getInt(declaredField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override // com.sessionm.api.Config
    public void enableDebugMode(boolean z) {
        String str = Kontagent.PRODUCTION_MODE;
        if (z) {
            str = PropertyConfiguration.DEBUG;
        }
        this.configImtems.setProperty(b.B, str);
    }

    public Object getConfigurationByKey(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return this.configImtems.get(str);
    }

    public String getSdkVersion() {
        return this.configImtems.getProperty(b.J);
    }

    public void setServer(int i) {
        String str = "";
        String str2 = "";
        String str3 = "";
        if (i == 1) {
            str = (String) getConfigurationByKey(b.d);
            str2 = (String) getConfigurationByKey(b.i);
            str3 = (String) getConfigurationByKey(b.n);
        } else if (i == 0) {
            str = (String) getConfigurationByKey(b.c);
            str2 = (String) getConfigurationByKey(b.h);
            str3 = (String) getConfigurationByKey(b.m);
        } else if (i == 2) {
            str = (String) getConfigurationByKey(b.e);
            str2 = (String) getConfigurationByKey(b.j);
            str3 = (String) getConfigurationByKey(b.o);
        } else if (i == 3) {
            str = (String) getConfigurationByKey(b.f);
            str2 = (String) getConfigurationByKey(b.k);
            str3 = (String) getConfigurationByKey(b.l);
        }
        if (str != null && str.length() != 0) {
            this.configImtems.setProperty(b.b, str);
            this.configImtems.setProperty(b.g, str2);
            this.configImtems.setProperty(b.l, str3);
        }
    }

    public void loadCustomConfig(Context context, int i) {
        synchronized (this) {
            try {
                InputStream inputStreamOpenRawResource = context.getResources().openRawResource(i);
                if (inputStreamOpenRawResource != null) {
                    try {
                        try {
                            this.configImtems.load(inputStreamOpenRawResource);
                            try {
                                inputStreamOpenRawResource.close();
                            } catch (Exception e) {
                            }
                        } catch (IOException e2) {
                            Log.e(TAG, "could not read custom config file", e2);
                            try {
                                inputStreamOpenRawResource.close();
                            } catch (Exception e3) {
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            inputStreamOpenRawResource.close();
                        } catch (Exception e4) {
                        }
                        throw th;
                    }
                }
            } catch (Exception e5) {
                Log.e(TAG, "could not find custom config file... ignoring...", e5);
            }
        }
    }

    public boolean isSecureAPI() {
        return !getConfigurationByKey(b.x).toString().equals("1");
    }

    public String serverUrl() {
        return (String) getConfigurationByKey(b.b);
    }

    public String portalUrl() {
        return (String) getConfigurationByKey(b.g);
    }

    @Override // com.sessionm.api.Config
    public void setActivityOrientation(int i) {
        String str = "portrait";
        switch (i) {
            case 0:
                str = "portrait";
                break;
            case 1:
                str = "landscape";
                break;
            case 2:
                str = "dynamic";
                break;
        }
        this.configImtems.setProperty(b.H, str);
    }
}
