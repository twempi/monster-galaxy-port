package org.codehaus.jackson.map.type;

import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class SimpleType extends TypeBase {
    protected final String[] _typeNames;
    protected final JavaType[] _typeParameters;

    protected SimpleType(Class<?> cls) {
        this(cls, null, null);
    }

    protected SimpleType(Class<?> cls, String[] typeNames, JavaType[] typeParams) {
        super(cls, 0);
        if (typeNames == null || typeNames.length == 0) {
            this._typeNames = null;
            this._typeParameters = null;
        } else {
            this._typeNames = typeNames;
            this._typeParameters = typeParams;
        }
    }

    public static SimpleType constructUnsafe(Class<?> raw) {
        return new SimpleType(raw, null, null);
    }

    @Override // org.codehaus.jackson.type.JavaType
    protected JavaType _narrow(Class<?> subclass) {
        return new SimpleType(subclass, this._typeNames, this._typeParameters);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType narrowContentsBy(Class<?> subclass) {
        throw new IllegalArgumentException("Internal error: SimpleType.narrowContentsBy() should never be called");
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType widenContentsBy(Class<?> subclass) {
        throw new IllegalArgumentException("Internal error: SimpleType.widenContentsBy() should never be called");
    }

    public static SimpleType construct(Class<?> cls) {
        if (Map.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: " + cls.getName() + ")");
        }
        if (Collection.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: " + cls.getName() + ")");
        }
        if (cls.isArray()) {
            throw new IllegalArgumentException("Can not construct SimpleType for an array (class: " + cls.getName() + ")");
        }
        return new SimpleType(cls);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public SimpleType withTypeHandler(Object h) {
        SimpleType newInstance = new SimpleType(this._class, this._typeNames, this._typeParameters);
        newInstance._typeHandler = h;
        return newInstance;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType withContentTypeHandler(Object h) {
        throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
    }

    @Override // org.codehaus.jackson.map.type.TypeBase
    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._typeParameters != null && this._typeParameters.length > 0) {
            sb.append('<');
            boolean first = true;
            JavaType[] arr$ = this._typeParameters;
            for (JavaType t : arr$) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(t.toCanonical());
            }
            sb.append('>');
        }
        return sb.toString();
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isContainerType() {
        return false;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public int containedTypeCount() {
        if (this._typeParameters == null) {
            return 0;
        }
        return this._typeParameters.length;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType containedType(int index) {
        if (index < 0 || this._typeParameters == null || index >= this._typeParameters.length) {
            return null;
        }
        return this._typeParameters[index];
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String containedTypeName(int index) {
        if (index < 0 || this._typeNames == null || index >= this._typeNames.length) {
            return null;
        }
        return this._typeNames[index];
    }

    @Override // org.codehaus.jackson.map.type.TypeBase, org.codehaus.jackson.type.JavaType
    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(this._class, sb, true);
    }

    @Override // org.codehaus.jackson.map.type.TypeBase, org.codehaus.jackson.type.JavaType
    public StringBuilder getGenericSignature(StringBuilder sb) {
        _classSignature(this._class, sb, false);
        if (this._typeParameters != null) {
            sb.append('<');
            JavaType[] arr$ = this._typeParameters;
            for (JavaType param : arr$) {
                sb = param.getGenericSignature(sb);
            }
            sb.append('>');
        }
        sb.append(';');
        return sb;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
        return sb.toString();
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        SimpleType other = (SimpleType) o;
        if (other._class != this._class) {
            return false;
        }
        JavaType[] p1 = this._typeParameters;
        JavaType[] p2 = other._typeParameters;
        if (p1 == null) {
            return p2 == null || p2.length == 0;
        }
        if (p2 == null || p1.length != p2.length) {
            return false;
        }
        int len = p1.length;
        for (int i = 0; i < len; i++) {
            if (!p1[i].equals(p2[i])) {
                return false;
            }
        }
        return true;
    }
}
