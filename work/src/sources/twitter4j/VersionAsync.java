package twitter4j;

/* JADX INFO: loaded from: classes.dex */
public final class VersionAsync {
    private static final String TITLE = "Twitter4J Async API";
    private static final String VERSION = "2.2.3";

    private VersionAsync() {
        throw new AssertionError();
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void main(String[] args) {
        System.out.println("Twitter4J Async API 2.2.3");
    }
}
