package twitter4j;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface ProfileImage extends TwitterResponse, Serializable {
    public static final ImageSize BIGGER = new ImageSize("bigger", null);
    public static final ImageSize NORMAL = new ImageSize("normal", null);
    public static final ImageSize MINI = new ImageSize("mini", null);

    /* JADX INFO: renamed from: twitter4j.ProfileImage$1, reason: invalid class name */
    static class AnonymousClass1 {
    }

    String getURL();

    public static class ImageSize implements Serializable {
        private static final Map<String, ImageSize> instances = new HashMap();
        private static final long serialVersionUID = 3363026523372848987L;
        private final String name;

        ImageSize(String x0, AnonymousClass1 x1) {
            this(x0);
        }

        private ImageSize() {
            throw new AssertionError();
        }

        private ImageSize(String name) {
            this.name = name;
            instances.put(name, this);
        }

        public String getName() {
            return this.name;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ImageSize imageSize = (ImageSize) o;
            return this.name.equals(imageSize.name);
        }

        public int hashCode() {
            return this.name.hashCode();
        }

        public String toString() {
            return this.name;
        }

        private static ImageSize getInstance(String name) {
            return instances.get(name);
        }

        private Object readResolve() throws ObjectStreamException {
            return getInstance(this.name);
        }
    }
}
