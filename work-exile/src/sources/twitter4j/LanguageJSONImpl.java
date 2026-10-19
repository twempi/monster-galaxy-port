package twitter4j;

import com.sessionm.a.b;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.api.HelpMethods;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LanguageJSONImpl implements HelpMethods.Language {
    private String code;
    private String name;
    private String status;

    LanguageJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.name = json.getString("name");
            this.code = json.getString("code");
            this.status = json.getString(b.K);
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    @Override // twitter4j.api.HelpMethods.Language
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.api.HelpMethods.Language
    public String getCode() {
        return this.code;
    }

    @Override // twitter4j.api.HelpMethods.Language
    public String getStatus() {
        return this.status;
    }

    static ResponseList<HelpMethods.Language> createLanguageList(HttpResponse res, Configuration conf) throws TwitterException {
        return createLanguageList(res.asJSONArray(), res, conf);
    }

    static ResponseList<HelpMethods.Language> createLanguageList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        try {
            int size = list.length();
            ResponseList<HelpMethods.Language> languages = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                LanguageJSONImpl languageJSONImpl = new LanguageJSONImpl(json);
                languages.add(languageJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(languageJSONImpl, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(languages, list);
            }
            return languages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }
}
