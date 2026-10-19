package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class TypeBase extends JavaType {
    volatile String _canonicalName;

    protected abstract String buildCanonicalName();

    @Override // org.codehaus.jackson.type.JavaType
    public abstract StringBuilder getErasedSignature(StringBuilder sb);

    @Override // org.codehaus.jackson.type.JavaType
    public abstract StringBuilder getGenericSignature(StringBuilder sb);

    protected TypeBase(Class<?> raw, int hash) {
        super(raw, hash);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String toCanonical() {
        String str = this._canonicalName;
        if (str == null) {
            return buildCanonicalName();
        }
        return str;
    }

    protected final JavaType copyHandlers(JavaType fromType) {
        this._valueHandler = fromType.getValueHandler();
        this._typeHandler = fromType.getTypeHandler();
        return this;
    }

    protected static StringBuilder _classSignature(Class<?> cls, StringBuilder sb, boolean trailingSemicolon) {
        if (cls.isPrimitive()) {
            if (cls == Boolean.TYPE) {
                sb.append('Z');
            } else if (cls == Byte.TYPE) {
                sb.append('B');
            } else if (cls == Short.TYPE) {
                sb.append('S');
            } else if (cls == Character.TYPE) {
                sb.append('C');
            } else if (cls == Integer.TYPE) {
                sb.append('I');
            } else if (cls == Long.TYPE) {
                sb.append('J');
            } else if (cls == Float.TYPE) {
                sb.append('F');
            } else if (cls == Double.TYPE) {
                sb.append('D');
            } else if (cls == Void.TYPE) {
                sb.append('V');
            } else {
                throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
            }
        } else {
            sb.append('L');
            String name = cls.getName();
            int len = name.length();
            for (int i = 0; i < len; i++) {
                char c = name.charAt(i);
                if (c == '.') {
                    c = '/';
                }
                sb.append(c);
            }
            if (trailingSemicolon) {
                sb.append(';');
            }
        }
        return sb;
    }
}
