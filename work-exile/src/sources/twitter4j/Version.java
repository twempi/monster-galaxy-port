package twitter4j;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Version {
    private static final String TITLE = "Twitter4J";
    private static final String VERSION = "2.2.3";

    private Version() {
        throw new AssertionError();
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void main(String[] args) {
        System.out.println("Twitter4J 2.2.3");
    }
}
