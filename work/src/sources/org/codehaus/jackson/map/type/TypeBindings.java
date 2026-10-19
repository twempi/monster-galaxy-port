package org.codehaus.jackson.map.type;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class TypeBindings {
    private static final JavaType[] NO_TYPES = new JavaType[0];
    public static final JavaType UNBOUND = new SimpleType(Object.class);
    protected Map<String, JavaType> _bindings;
    protected final Class<?> _contextClass;
    protected final JavaType _contextType;
    private final TypeBindings _parentBindings;
    protected HashSet<String> _placeholders;
    protected final TypeFactory _typeFactory;

    public TypeBindings(TypeFactory typeFactory, Class<?> cc) {
        this(typeFactory, null, cc, null);
    }

    public TypeBindings(TypeFactory typeFactory, JavaType type) {
        this(typeFactory, null, type.getRawClass(), type);
    }

    public TypeBindings childInstance() {
        return new TypeBindings(this._typeFactory, this, this._contextClass, this._contextType);
    }

    private TypeBindings(TypeFactory tf, TypeBindings parent, Class<?> cc, JavaType type) {
        this._typeFactory = tf;
        this._parentBindings = parent;
        this._contextClass = cc;
        this._contextType = type;
    }

    public JavaType resolveType(Class<?> cls) {
        return this._typeFactory._constructType(cls, this);
    }

    public JavaType resolveType(Type type) {
        return this._typeFactory._constructType(type, this);
    }

    public int getBindingCount() {
        if (this._bindings == null) {
            _resolve();
        }
        return this._bindings.size();
    }

    public JavaType findType(String name) {
        String className;
        if (this._bindings == null) {
            _resolve();
        }
        JavaType t = this._bindings.get(name);
        if (t == null) {
            if (this._placeholders != null && this._placeholders.contains(name)) {
                return UNBOUND;
            }
            if (this._parentBindings != null) {
                return this._parentBindings.findType(name);
            }
            if (this._contextClass != null) {
                Class<?> enclosing = this._contextClass.getEnclosingClass();
                if (enclosing != null && !Modifier.isStatic(this._contextClass.getModifiers())) {
                    return UNBOUND;
                }
            }
            if (this._contextClass != null) {
                className = this._contextClass.getName();
            } else if (this._contextType != null) {
                className = this._contextType.toString();
            } else {
                className = "UNKNOWN";
            }
            throw new IllegalArgumentException("Type variable '" + name + "' can not be resolved (with context of class " + className + ")");
        }
        return t;
    }

    public void addBinding(String name, JavaType type) {
        if (this._bindings == null || this._bindings.size() == 0) {
            this._bindings = new LinkedHashMap();
        }
        this._bindings.put(name, type);
    }

    public JavaType[] typesAsArray() {
        if (this._bindings == null) {
            _resolve();
        }
        return this._bindings.size() == 0 ? NO_TYPES : (JavaType[]) this._bindings.values().toArray(new JavaType[this._bindings.size()]);
    }

    protected void _resolve() {
        int count;
        _resolveBindings(this._contextClass);
        if (this._contextType != null && (count = this._contextType.containedTypeCount()) > 0) {
            if (this._bindings == null) {
                this._bindings = new LinkedHashMap();
            }
            for (int i = 0; i < count; i++) {
                String name = this._contextType.containedTypeName(i);
                JavaType type = this._contextType.containedType(i);
                this._bindings.put(name, type);
            }
        }
        if (this._bindings == null) {
            this._bindings = Collections.emptyMap();
        }
    }

    public void _addPlaceholder(String name) {
        if (this._placeholders == null) {
            this._placeholders = new HashSet<>();
        }
        this._placeholders.add(name);
    }

    protected void _resolveBindings(Type t) {
        Class<?> raw;
        if (t != null) {
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) t;
                Type[] args = pt.getActualTypeArguments();
                if (args != null && args.length > 0) {
                    Class<?> rawType = (Class) pt.getRawType();
                    TypeVariable<?>[] vars = rawType.getTypeParameters();
                    if (vars.length != args.length) {
                        throw new IllegalArgumentException("Strange parametrized type (in class " + rawType.getName() + "): number of type arguments != number of type parameters (" + args.length + " vs " + vars.length + ")");
                    }
                    int len = args.length;
                    for (int i = 0; i < len; i++) {
                        String name = vars[i].getName();
                        if (this._bindings == null) {
                            this._bindings = new LinkedHashMap();
                        } else {
                            if (this._bindings.containsKey(name)) {
                            }
                        }
                        _addPlaceholder(name);
                        this._bindings.put(name, this._typeFactory._constructType(args[i], this));
                    }
                }
                raw = (Class) pt.getRawType();
            } else if (t instanceof Class) {
                raw = (Class) t;
                TypeVariable<?>[] vars2 = raw.getTypeParameters();
                if (vars2 != null && vars2.length > 0) {
                    for (TypeVariable<?> var : vars2) {
                        String name2 = var.getName();
                        Type varType = var.getBounds()[0];
                        if (varType != null) {
                            if (this._bindings == null) {
                                this._bindings = new LinkedHashMap();
                            } else if (this._bindings.containsKey(name2)) {
                            }
                            _addPlaceholder(name2);
                            this._bindings.put(name2, this._typeFactory._constructType(varType, this));
                        }
                    }
                }
            } else {
                return;
            }
            _resolveBindings(raw.getGenericSuperclass());
            Type[] arr$ = raw.getGenericInterfaces();
            for (Type intType : arr$) {
                _resolveBindings(intType);
            }
        }
    }

    public String toString() {
        if (this._bindings == null) {
            _resolve();
        }
        StringBuilder sb = new StringBuilder("[TypeBindings for ");
        if (this._contextType != null) {
            sb.append(this._contextType.toString());
        } else {
            sb.append(this._contextClass.getName());
        }
        sb.append(": ").append(this._bindings).append("]");
        return sb.toString();
    }
}
