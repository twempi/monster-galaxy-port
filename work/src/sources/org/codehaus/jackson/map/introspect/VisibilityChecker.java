package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.introspect.VisibilityChecker;

/* JADX INFO: loaded from: classes.dex */
public interface VisibilityChecker<T extends VisibilityChecker<T>> {
    boolean isCreatorVisible(Member member);

    boolean isCreatorVisible(AnnotatedMember annotatedMember);

    boolean isFieldVisible(Field field);

    boolean isFieldVisible(AnnotatedField annotatedField);

    boolean isGetterVisible(Method method);

    boolean isGetterVisible(AnnotatedMethod annotatedMethod);

    boolean isIsGetterVisible(Method method);

    boolean isIsGetterVisible(AnnotatedMethod annotatedMethod);

    boolean isSetterVisible(Method method);

    boolean isSetterVisible(AnnotatedMethod annotatedMethod);

    T with(JsonAutoDetect jsonAutoDetect);

    T withCreatorVisibility(JsonAutoDetect.Visibility visibility);

    T withFieldVisibility(JsonAutoDetect.Visibility visibility);

    T withGetterVisibility(JsonAutoDetect.Visibility visibility);

    T withIsGetterVisibility(JsonAutoDetect.Visibility visibility);

    T withSetterVisibility(JsonAutoDetect.Visibility visibility);

    @JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility = JsonAutoDetect.Visibility.ANY)
    public static class Std implements VisibilityChecker<Std> {
        protected static final Std DEFAULT = new Std((JsonAutoDetect) Std.class.getAnnotation(JsonAutoDetect.class));
        protected final JsonAutoDetect.Visibility _creatorMinLevel;
        protected final JsonAutoDetect.Visibility _fieldMinLevel;
        protected final JsonAutoDetect.Visibility _getterMinLevel;
        protected final JsonAutoDetect.Visibility _isGetterMinLevel;
        protected final JsonAutoDetect.Visibility _setterMinLevel;

        public static Std defaultInstance() {
            return DEFAULT;
        }

        public Std(JsonAutoDetect ann) {
            JsonMethod[] incl = ann.value();
            this._getterMinLevel = hasMethod(incl, JsonMethod.GETTER) ? ann.getterVisibility() : JsonAutoDetect.Visibility.NONE;
            this._isGetterMinLevel = hasMethod(incl, JsonMethod.IS_GETTER) ? ann.isGetterVisibility() : JsonAutoDetect.Visibility.NONE;
            this._setterMinLevel = hasMethod(incl, JsonMethod.SETTER) ? ann.setterVisibility() : JsonAutoDetect.Visibility.NONE;
            this._creatorMinLevel = hasMethod(incl, JsonMethod.CREATOR) ? ann.creatorVisibility() : JsonAutoDetect.Visibility.NONE;
            this._fieldMinLevel = hasMethod(incl, JsonMethod.FIELD) ? ann.fieldVisibility() : JsonAutoDetect.Visibility.NONE;
        }

        public Std(JsonAutoDetect.Visibility getter, JsonAutoDetect.Visibility isGetter, JsonAutoDetect.Visibility setter, JsonAutoDetect.Visibility creator, JsonAutoDetect.Visibility field) {
            this._getterMinLevel = getter;
            this._isGetterMinLevel = isGetter;
            this._setterMinLevel = setter;
            this._creatorMinLevel = creator;
            this._fieldMinLevel = field;
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public Std with(JsonAutoDetect ann) {
            if (ann != null) {
                JsonMethod[] incl = ann.value();
                JsonAutoDetect.Visibility v = hasMethod(incl, JsonMethod.GETTER) ? ann.getterVisibility() : JsonAutoDetect.Visibility.NONE;
                Std curr = withGetterVisibility(v);
                JsonAutoDetect.Visibility v2 = hasMethod(incl, JsonMethod.IS_GETTER) ? ann.isGetterVisibility() : JsonAutoDetect.Visibility.NONE;
                Std curr2 = curr.withIsGetterVisibility(v2);
                JsonAutoDetect.Visibility v3 = hasMethod(incl, JsonMethod.SETTER) ? ann.setterVisibility() : JsonAutoDetect.Visibility.NONE;
                Std curr3 = curr2.withSetterVisibility(v3);
                JsonAutoDetect.Visibility v4 = hasMethod(incl, JsonMethod.CREATOR) ? ann.creatorVisibility() : JsonAutoDetect.Visibility.NONE;
                Std curr4 = curr3.withCreatorVisibility(v4);
                JsonAutoDetect.Visibility v5 = hasMethod(incl, JsonMethod.FIELD) ? ann.fieldVisibility() : JsonAutoDetect.Visibility.NONE;
                return curr4.withFieldVisibility(v5);
            }
            return this;
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public Std withGetterVisibility(JsonAutoDetect.Visibility v) {
            if (v == JsonAutoDetect.Visibility.DEFAULT) {
                v = DEFAULT._getterMinLevel;
            }
            if (this._getterMinLevel == v) {
                return this;
            }
            return new Std(v, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public Std withIsGetterVisibility(JsonAutoDetect.Visibility v) {
            if (v == JsonAutoDetect.Visibility.DEFAULT) {
                v = DEFAULT._isGetterMinLevel;
            }
            if (this._isGetterMinLevel == v) {
                return this;
            }
            return new Std(this._getterMinLevel, v, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public Std withSetterVisibility(JsonAutoDetect.Visibility v) {
            if (v == JsonAutoDetect.Visibility.DEFAULT) {
                v = DEFAULT._setterMinLevel;
            }
            if (this._setterMinLevel == v) {
                return this;
            }
            return new Std(this._getterMinLevel, this._isGetterMinLevel, v, this._creatorMinLevel, this._fieldMinLevel);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public Std withCreatorVisibility(JsonAutoDetect.Visibility v) {
            if (v == JsonAutoDetect.Visibility.DEFAULT) {
                v = DEFAULT._creatorMinLevel;
            }
            if (this._creatorMinLevel == v) {
                return this;
            }
            return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, v, this._fieldMinLevel);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public Std withFieldVisibility(JsonAutoDetect.Visibility v) {
            if (v == JsonAutoDetect.Visibility.DEFAULT) {
                v = DEFAULT._fieldMinLevel;
            }
            return this._fieldMinLevel == v ? this : new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, v);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isCreatorVisible(Member m) {
            return this._creatorMinLevel.isVisible(m);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isCreatorVisible(AnnotatedMember m) {
            return isCreatorVisible(m.getMember());
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isFieldVisible(Field f) {
            return this._fieldMinLevel.isVisible(f);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isFieldVisible(AnnotatedField f) {
            return isFieldVisible(f.getAnnotated());
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isGetterVisible(Method m) {
            return this._getterMinLevel.isVisible(m);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isGetterVisible(AnnotatedMethod m) {
            return isGetterVisible(m.getAnnotated());
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isIsGetterVisible(Method m) {
            return this._isGetterMinLevel.isVisible(m);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isIsGetterVisible(AnnotatedMethod m) {
            return isIsGetterVisible(m.getAnnotated());
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isSetterVisible(Method m) {
            return this._setterMinLevel.isVisible(m);
        }

        @Override // org.codehaus.jackson.map.introspect.VisibilityChecker
        public boolean isSetterVisible(AnnotatedMethod m) {
            return isSetterVisible(m.getAnnotated());
        }

        private static boolean hasMethod(JsonMethod[] methods, JsonMethod method) {
            for (JsonMethod curr : methods) {
                if (curr == method || curr == JsonMethod.ALL) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return "[Visibility: getter: " + this._getterMinLevel + ", isGetter: " + this._isGetterMinLevel + ", setter: " + this._setterMinLevel + ", creator: " + this._creatorMinLevel + ", field: " + this._fieldMinLevel + "]";
        }
    }
}
