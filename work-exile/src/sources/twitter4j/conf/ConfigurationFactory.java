package twitter4j.conf;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ConfigurationFactory {
    void dispose();

    Configuration getInstance();

    Configuration getInstance(String str);
}
