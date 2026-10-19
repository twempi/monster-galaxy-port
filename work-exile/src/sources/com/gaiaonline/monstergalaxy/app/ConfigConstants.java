package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import java.io.IOException;
import java.util.Properties;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ConfigConstants {
    public static final String ASSETS_DOWNLOAD_URL;
    public static final String WEBSERVICE_INSTALL_URL;
    public static final String WEBSERVICE_PURCHASE;
    public static final String WEBSERVICE_REIMBURSEMENT_PHASE1;
    public static final String WEBSERVICE_REIMBURSEMENT_PHASE2;
    public static final String WEBSERVICE_REIMBURSEMENT_PHASE3;
    public static final String WEBSERVICE_SESSION;
    public static final String WEBSERVICE_TIME_URL;
    public static final String WEBSERVICE_VERSION;

    static {
        Properties props = new Properties();
        try {
            props.load(Gdx.files.internal("config.properties").read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ASSETS_DOWNLOAD_URL = props.getProperty("assets_download_url");
        WEBSERVICE_TIME_URL = props.getProperty("time_url");
        WEBSERVICE_INSTALL_URL = props.getProperty("install_url");
        WEBSERVICE_REIMBURSEMENT_PHASE1 = props.getProperty("reimbursement_p1");
        WEBSERVICE_REIMBURSEMENT_PHASE2 = props.getProperty("reimbursement_p2");
        WEBSERVICE_REIMBURSEMENT_PHASE3 = props.getProperty("reimbursement_p3");
        WEBSERVICE_PURCHASE = props.getProperty("purchase_url");
        WEBSERVICE_SESSION = props.getProperty("session_url");
        WEBSERVICE_VERSION = props.getProperty("version_url");
    }
}
