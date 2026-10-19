package twitter4j.conf;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class PropertyConfigurationFactory implements ConfigurationFactory {
    private static final PropertyConfiguration ROOT_CONFIGURATION = new PropertyConfiguration();

    PropertyConfigurationFactory() {
    }

    @Override // twitter4j.conf.ConfigurationFactory
    public Configuration getInstance() {
        return ROOT_CONFIGURATION;
    }

    @Override // twitter4j.conf.ConfigurationFactory
    public Configuration getInstance(String configTreePath) {
        return new PropertyConfiguration(configTreePath);
    }

    @Override // twitter4j.conf.ConfigurationFactory
    public void dispose() {
    }
}
