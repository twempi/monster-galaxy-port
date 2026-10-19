package com.millennialmedia.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import oauth.signpost.OAuth;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMDemographic {
    public static final String EDUCATION_ASSOCIATE = "associate";
    public static final String EDUCATION_BACHELORS = "bachelors";
    public static final String EDUCATION_HIGH_SCHOOL = "highschool";
    public static final String EDUCATION_IN_COLLEGE = "incollege";
    public static final String EDUCATION_MASTERS = "masters";
    public static final String EDUCATION_PHD = "phd";
    public static final String EDUCATION_PROFESSIONAL = "professional";
    public static final String EDUCATION_SOME_COLLEGE = "somecollege";
    public static final String ETHNICITY_ASIAN = "asian";
    public static final String ETHNICITY_BLACK = "black";
    public static final String ETHNICITY_HISPANIC = "hispanic";
    public static final String ETHNICITY_INDIAN = "indian";
    public static final String ETHNICITY_MIDDLE_EASTERN = "middleeastern";
    public static final String ETHNICITY_NATIVE_AMERICAN = "nativeamerican";
    public static final String ETHNICITY_OTHER = "other";
    public static final String ETHNICITY_PACIFIC_ISLANDER = "pacificislander";
    public static final String ETHNICITY_WHITE = "white";
    public static final String GENDER_FEMALE = "female";
    public static final String GENDER_MALE = "male";
    public static final String KEY_AGE = "age";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_INCOME = "income";
    public static final String KEY_KEYWORDS = "keywords";
    public static final String KEY_POLITICS = "politics";
    public static final String KEY_VENDOR = "vendor";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_ZIP = "zip";
    public static final String MARITAL_DIVORCED = "divorced";
    public static final String MARITAL_ENGAGED = "engaged";
    public static final String MARITAL_MARRIED = "married";
    public static final String MARITAL_RELATIONSHIP = "relationship";
    public static final String MARITAL_SINGLE = "single";
    public static final String ORIENTATION_BISEXUAL = "bisexual";
    public static final String ORIENTATION_GAY = "gay";
    public static final String ORIENTATION_STRAIGHT = "straight";
    public String education;
    public String ethnicity;
    public String gender;
    public String marital;
    public String orientation;
    private HashMap<String, String> values = new HashMap<>();

    MMDemographic() {
    }

    public void put(String key, String value) {
        if (key.equals(MMAdView.KEY_GENDER)) {
            this.gender = value;
            return;
        }
        if (key.equals(MMAdView.KEY_ETHNICITY)) {
            this.ethnicity = value;
            return;
        }
        if (key.equals(MMAdView.KEY_MARITAL_STATUS)) {
            this.marital = value;
            return;
        }
        if (key.equals(MMAdView.KEY_ORIENTATION)) {
            this.orientation = value;
            return;
        }
        if (key.equals(MMAdView.KEY_EDUCATION)) {
            this.education = value;
        } else if (value != null) {
            this.values.put(key, value);
        } else {
            this.values.remove(key);
        }
    }

    String toQueryString() throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.gender != null && (this.gender.equals(GENDER_MALE) || this.gender.equals(GENDER_FEMALE))) {
            stringBuilder.append("&gender=" + URLEncoder.encode(this.gender, OAuth.ENCODING));
        }
        if (this.ethnicity != null && (this.ethnicity.equals(ETHNICITY_WHITE) || this.ethnicity.equals(ETHNICITY_BLACK) || this.ethnicity.equals(ETHNICITY_HISPANIC) || this.ethnicity.equals(ETHNICITY_ASIAN) || this.ethnicity.equals(ETHNICITY_INDIAN) || this.ethnicity.equals(ETHNICITY_MIDDLE_EASTERN) || this.ethnicity.equals(ETHNICITY_NATIVE_AMERICAN) || this.ethnicity.equals(ETHNICITY_PACIFIC_ISLANDER) || this.ethnicity.equals(ETHNICITY_OTHER))) {
            stringBuilder.append("&ethnicity=" + URLEncoder.encode(this.ethnicity, OAuth.ENCODING));
        }
        if (this.marital != null && (this.marital.equals(MARITAL_SINGLE) || this.marital.equals(MARITAL_MARRIED) || this.marital.equals(MARITAL_DIVORCED) || this.marital.equals(MARITAL_ENGAGED) || this.marital.equals(MARITAL_RELATIONSHIP))) {
            stringBuilder.append("&marital=" + this.marital);
        }
        if (this.orientation != null && (this.orientation.equals(ORIENTATION_STRAIGHT) || this.orientation.equals(ORIENTATION_GAY) || this.orientation.equals(ORIENTATION_BISEXUAL))) {
            stringBuilder.append("&orientation=" + this.orientation);
        }
        if (this.education != null && (this.education.equals(EDUCATION_HIGH_SCHOOL) || this.education.equals(EDUCATION_IN_COLLEGE) || this.education.equals(EDUCATION_SOME_COLLEGE) || this.education.equals(EDUCATION_ASSOCIATE) || this.education.equals(EDUCATION_BACHELORS) || this.education.equals(EDUCATION_MASTERS) || this.education.equals(EDUCATION_PHD) || this.education.equals(EDUCATION_PROFESSIONAL))) {
            stringBuilder.append("&edu=" + URLEncoder.encode(this.education, OAuth.ENCODING));
        }
        for (Map.Entry<String, String> entry : this.values.entrySet()) {
            stringBuilder.append(String.format("&%s=%s", URLEncoder.encode(entry.getKey()), URLEncoder.encode(entry.getValue())));
        }
        return stringBuilder.toString();
    }

    void getUrlParams(Map<String, String> paramsMap) {
        for (Map.Entry<String, String> entry : this.values.entrySet()) {
            paramsMap.put(entry.getKey(), entry.getValue());
        }
        if (this.gender != null) {
            paramsMap.put(MMAdView.KEY_GENDER, this.gender);
        }
        if (this.marital != null && (this.marital.equals(MARITAL_SINGLE) || this.marital.equals(MARITAL_MARRIED) || this.marital.equals(MARITAL_DIVORCED) || this.marital.equals(MARITAL_ENGAGED) || this.marital.equals(MARITAL_RELATIONSHIP))) {
            paramsMap.put(MMAdView.KEY_MARITAL_STATUS, this.marital);
        }
        if (this.ethnicity != null) {
            paramsMap.put(MMAdView.KEY_ETHNICITY, this.ethnicity);
        }
        if (this.orientation != null && (this.orientation.equals(ORIENTATION_STRAIGHT) || this.orientation.equals(ORIENTATION_GAY) || this.orientation.equals(ORIENTATION_BISEXUAL))) {
            paramsMap.put(MMAdView.KEY_ORIENTATION, this.orientation);
        }
        if (this.education != null) {
            paramsMap.put("edu", this.education);
        }
    }
}
