package twitter4j.conf;

/* JADX INFO: loaded from: classes.dex */
public interface ConfigurationFactory {
    void dispose();

    Configuration getInstance();

    Configuration getInstance(String str);
}
