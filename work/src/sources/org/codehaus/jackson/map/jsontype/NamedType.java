package org.codehaus.jackson.map.jsontype;

/* JADX INFO: loaded from: classes.dex */
public final class NamedType {
    protected final Class<?> _class;
    protected final int _hashCode;
    protected String _name;

    public NamedType(Class<?> c) {
        this(c, null);
    }

    public NamedType(Class<?> c, String name) {
        this._class = c;
        this._hashCode = c.getName().hashCode();
        setName(name);
    }

    public Class<?> getType() {
        return this._class;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        if (name == null || name.length() == 0) {
            name = null;
        }
        this._name = name;
    }

    public boolean hasName() {
        return this._name != null;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return o != null && o.getClass() == getClass() && this._class == ((NamedType) o)._class;
    }

    public int hashCode() {
        return this._hashCode;
    }

    public String toString() {
        return "[NamedType, class " + this._class.getName() + ", name: " + (this._name == null ? "null" : "'" + this._name + "'") + "]";
    }
}
