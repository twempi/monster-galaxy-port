package org.codehaus.jackson.map.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

/* JADX INFO: loaded from: classes.dex */
public final class TypeFactory {
    protected final TypeModifier[] _modifiers;
    protected final TypeParser _parser;

    @Deprecated
    public static final TypeFactory instance = new TypeFactory();
    private static final JavaType[] NO_TYPES = new JavaType[0];

    private TypeFactory() {
        this._parser = new TypeParser(this);
        this._modifiers = null;
    }

    protected TypeFactory(TypeParser p, TypeModifier[] mods) {
        this._parser = p;
        this._modifiers = mods;
    }

    public TypeFactory withModifier(TypeModifier mod) {
        return this._modifiers == null ? new TypeFactory(this._parser, new TypeModifier[]{mod}) : new TypeFactory(this._parser, (TypeModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, mod));
    }

    public static TypeFactory defaultInstance() {
        return instance;
    }

    public static JavaType unknownType() {
        return defaultInstance()._unknownType();
    }

    public static Class<?> rawClass(Type t) {
        return t instanceof Class ? (Class) t : defaultInstance().constructType(t).getRawClass();
    }

    @Deprecated
    public static JavaType type(Type t) {
        return instance._constructType(t, null);
    }

    @Deprecated
    public static JavaType type(Type type, Class<?> context) {
        return instance.constructType(type, context);
    }

    @Deprecated
    public static JavaType type(Type type, JavaType context) {
        return instance.constructType(type, context);
    }

    @Deprecated
    public static JavaType type(Type type, TypeBindings bindings) {
        return instance._constructType(type, bindings);
    }

    @Deprecated
    public static JavaType type(TypeReference<?> ref) {
        return instance.constructType(ref.getType());
    }

    @Deprecated
    public static JavaType arrayType(Class<?> elementType) {
        return instance.constructArrayType(instance.constructType(elementType));
    }

    @Deprecated
    public static JavaType arrayType(JavaType elementType) {
        return instance.constructArrayType(elementType);
    }

    @Deprecated
    public static JavaType collectionType(Class<? extends Collection> collectionType, Class<?> elementType) {
        return instance.constructCollectionType(collectionType, instance.constructType(elementType));
    }

    @Deprecated
    public static JavaType collectionType(Class<? extends Collection> collectionType, JavaType elementType) {
        return instance.constructCollectionType(collectionType, elementType);
    }

    @Deprecated
    public static JavaType mapType(Class<? extends Map> mapClass, Class<?> keyType, Class<?> valueType) {
        return instance.constructMapType(mapClass, type(keyType), instance.constructType(valueType));
    }

    @Deprecated
    public static JavaType mapType(Class<? extends Map> mapType, JavaType keyType, JavaType valueType) {
        return instance.constructMapType(mapType, keyType, valueType);
    }

    @Deprecated
    public static JavaType parametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return instance.constructParametricType(parametrized, parameterClasses);
    }

    @Deprecated
    public static JavaType parametricType(Class<?> parametrized, JavaType... parameterTypes) {
        return instance.constructParametricType(parametrized, parameterTypes);
    }

    public static JavaType fromCanonical(String canonical) throws IllegalArgumentException {
        return instance.constructFromCanonical(canonical);
    }

    @Deprecated
    public static JavaType specialize(JavaType baseType, Class<?> subclass) {
        return instance.constructSpecializedType(baseType, subclass);
    }

    @Deprecated
    public static JavaType fastSimpleType(Class<?> cls) {
        return instance.uncheckedSimpleType(cls);
    }

    @Deprecated
    public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType) {
        return instance.findTypeParameters(clz, expType);
    }

    @Deprecated
    public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType, TypeBindings bindings) {
        return instance.findTypeParameters(clz, expType, bindings);
    }

    @Deprecated
    public static JavaType[] findParameterTypes(JavaType type, Class<?> expType) {
        return instance.findTypeParameters(type, expType);
    }

    @Deprecated
    public static JavaType fromClass(Class<?> clz) {
        return instance._fromClass(clz, null);
    }

    @Deprecated
    public static JavaType fromTypeReference(TypeReference<?> ref) {
        return type(ref.getType());
    }

    @Deprecated
    public static JavaType fromType(Type type) {
        return instance._constructType(type, null);
    }

    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        if (!(baseType instanceof SimpleType) || (!subclass.isArray() && !Map.class.isAssignableFrom(subclass) && !Collection.class.isAssignableFrom(subclass))) {
            return baseType.narrowBy(subclass);
        }
        if (!baseType.getRawClass().isAssignableFrom(subclass)) {
            throw new IllegalArgumentException("Class " + subclass.getClass().getName() + " not subtype of " + baseType);
        }
        JavaType subtype = instance._fromClass(subclass, new TypeBindings(this, baseType.getRawClass()));
        Object h = baseType.getValueHandler();
        if (h != null) {
            subtype.setValueHandler(h);
        }
        Object h2 = baseType.getTypeHandler();
        if (h2 != null) {
            return subtype.withTypeHandler(h2);
        }
        return subtype;
    }

    public JavaType constructFromCanonical(String canonical) throws IllegalArgumentException {
        return this._parser.parse(canonical);
    }

    public JavaType[] findTypeParameters(JavaType type, Class<?> expType) {
        Class<?> raw = type.getRawClass();
        if (raw == expType) {
            int count = type.containedTypeCount();
            if (count == 0) {
                return null;
            }
            JavaType[] result = new JavaType[count];
            for (int i = 0; i < count; i++) {
                result[i] = type.containedType(i);
            }
            return result;
        }
        return findTypeParameters(raw, expType, new TypeBindings(this, type));
    }

    public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType) {
        return findTypeParameters(clz, expType, new TypeBindings(this, clz));
    }

    public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType, TypeBindings bindings) {
        HierarchicType subType = _findSuperTypeChain(clz, expType);
        if (subType == null) {
            throw new IllegalArgumentException("Class " + clz.getName() + " is not a subtype of " + expType.getName());
        }
        HierarchicType superType = subType;
        while (superType.getSuperType() != null) {
            superType = superType.getSuperType();
            Class<?> raw = superType.getRawClass();
            TypeBindings newBindings = new TypeBindings(this, raw);
            if (superType.isGeneric()) {
                ParameterizedType pt = superType.asGeneric();
                Type[] actualTypes = pt.getActualTypeArguments();
                TypeVariable<?>[] vars = raw.getTypeParameters();
                int len = actualTypes.length;
                for (int i = 0; i < len; i++) {
                    String name = vars[i].getName();
                    JavaType type = instance._constructType(actualTypes[i], bindings);
                    newBindings.addBinding(name, type);
                }
            }
            bindings = newBindings;
        }
        if (superType.isGeneric()) {
            return bindings.typesAsArray();
        }
        return null;
    }

    public JavaType constructType(Type type) {
        return _constructType(type, null);
    }

    public JavaType constructType(Type type, TypeBindings bindings) {
        return _constructType(type, bindings);
    }

    public JavaType constructType(TypeReference<?> typeRef) {
        return _constructType(typeRef.getType(), null);
    }

    public JavaType constructType(Type type, Class<?> context) {
        return _constructType(type, new TypeBindings(this, context));
    }

    public JavaType constructType(Type type, JavaType context) {
        return _constructType(type, new TypeBindings(this, context));
    }

    public JavaType _constructType(Type type, TypeBindings context) {
        JavaType resultType;
        if (type instanceof Class) {
            Class<?> cls = (Class) type;
            if (context == null) {
                context = new TypeBindings(this, cls);
            }
            resultType = _fromClass(cls, context);
        } else if (type instanceof ParameterizedType) {
            resultType = _fromParamType((ParameterizedType) type, context);
        } else if (type instanceof GenericArrayType) {
            resultType = _fromArrayType((GenericArrayType) type, context);
        } else if (type instanceof TypeVariable) {
            resultType = _fromVariable((TypeVariable) type, context);
        } else if (type instanceof WildcardType) {
            resultType = _fromWildcard((WildcardType) type, context);
        } else {
            throw new IllegalArgumentException("Unrecognized Type: " + type.toString());
        }
        if (this._modifiers != null && !resultType.isContainerType()) {
            TypeModifier[] arr$ = this._modifiers;
            for (TypeModifier mod : arr$) {
                resultType = mod.modifyType(resultType, type, context, this);
            }
        }
        return resultType;
    }

    protected ArrayType constructArrayType(Class<?> elementType) {
        return ArrayType.construct(_constructType(elementType, null));
    }

    protected ArrayType constructArrayType(JavaType elementType) {
        return ArrayType.construct(elementType);
    }

    public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return CollectionType.construct((Class<?>) collectionClass, constructType(elementClass));
    }

    public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, JavaType elementType) {
        return CollectionType.construct((Class<?>) collectionClass, elementType);
    }

    public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, Class<?> elementClass) {
        return CollectionLikeType.construct(collectionClass, constructType(elementClass));
    }

    public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, JavaType elementType) {
        return CollectionLikeType.construct(collectionClass, elementType);
    }

    public MapType constructMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType) {
        return MapType.construct((Class<?>) mapClass, keyType, valueType);
    }

    public MapType constructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return MapType.construct((Class<?>) mapClass, constructType(keyClass), constructType(valueClass));
    }

    public MapLikeType constructMapLikeType(Class<?> mapClass, JavaType keyType, JavaType valueType) {
        return MapLikeType.construct(mapClass, keyType, valueType);
    }

    public MapLikeType constructMapLikeType(Class<?> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return MapType.construct(mapClass, constructType(keyClass), constructType(valueClass));
    }

    public JavaType constructSimpleType(Class<?> rawType, JavaType[] parameterTypes) {
        TypeVariable<?>[] typeVars = rawType.getTypeParameters();
        if (typeVars.length != parameterTypes.length) {
            throw new IllegalArgumentException("Parameter type mismatch for " + rawType.getName() + ": expected " + typeVars.length + " parameters, was given " + parameterTypes.length);
        }
        String[] names = new String[typeVars.length];
        int len = typeVars.length;
        for (int i = 0; i < len; i++) {
            names[i] = typeVars[i].getName();
        }
        JavaType resultType = new SimpleType(rawType, names, parameterTypes);
        return resultType;
    }

    public JavaType uncheckedSimpleType(Class<?> cls) {
        return new SimpleType(cls, null, null);
    }

    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        int len = parameterClasses.length;
        JavaType[] pt = new JavaType[len];
        for (int i = 0; i < len; i++) {
            pt[i] = _fromClass(parameterClasses[i], null);
        }
        return constructParametricType(parametrized, pt);
    }

    public JavaType constructParametricType(Class<?> parametrized, JavaType... parameterTypes) {
        if (parametrized.isArray()) {
            if (parameterTypes.length != 1) {
                throw new IllegalArgumentException("Need exactly 1 parameter type for arrays (" + parametrized.getName() + ")");
            }
            JavaType resultType = constructArrayType(parameterTypes[0]);
            return resultType;
        }
        if (Map.class.isAssignableFrom(parametrized)) {
            if (parameterTypes.length != 2) {
                throw new IllegalArgumentException("Need exactly 2 parameter types for Map types (" + parametrized.getName() + ")");
            }
            JavaType resultType2 = constructMapType((Class<? extends Map>) parametrized, parameterTypes[0], parameterTypes[1]);
            return resultType2;
        }
        if (Collection.class.isAssignableFrom(parametrized)) {
            if (parameterTypes.length != 1) {
                throw new IllegalArgumentException("Need exactly 1 parameter type for Collection types (" + parametrized.getName() + ")");
            }
            JavaType resultType3 = constructCollectionType((Class<? extends Collection>) parametrized, parameterTypes[0]);
            return resultType3;
        }
        JavaType resultType4 = constructSimpleType(parametrized, parameterTypes);
        return resultType4;
    }

    protected JavaType _fromClass(Class<?> clz, TypeBindings context) {
        if (clz.isArray()) {
            return ArrayType.construct(_constructType(clz.getComponentType(), null));
        }
        if (clz.isEnum()) {
            return new SimpleType(clz);
        }
        if (Map.class.isAssignableFrom(clz)) {
            return _mapType(clz);
        }
        if (Collection.class.isAssignableFrom(clz)) {
            return _collectionType(clz);
        }
        return new SimpleType(clz);
    }

    protected JavaType _fromParameterizedClass(Class<?> clz, List<JavaType> paramTypes) {
        if (clz.isArray()) {
            return ArrayType.construct(_constructType(clz.getComponentType(), null));
        }
        if (clz.isEnum()) {
            return new SimpleType(clz);
        }
        if (Map.class.isAssignableFrom(clz)) {
            if (paramTypes.size() > 0) {
                JavaType keyType = paramTypes.get(0);
                JavaType contentType = paramTypes.size() >= 2 ? paramTypes.get(1) : _unknownType();
                return MapType.construct(clz, keyType, contentType);
            }
            return _mapType(clz);
        }
        if (Collection.class.isAssignableFrom(clz)) {
            if (paramTypes.size() >= 1) {
                return CollectionType.construct(clz, paramTypes.get(0));
            }
            return _collectionType(clz);
        }
        if (paramTypes.size() == 0) {
            return new SimpleType(clz);
        }
        JavaType[] pt = (JavaType[]) paramTypes.toArray(new JavaType[paramTypes.size()]);
        return constructSimpleType(clz, pt);
    }

    protected JavaType _fromParamType(ParameterizedType type, TypeBindings context) {
        JavaType[] pt;
        Class<?> rawType = (Class) type.getRawType();
        Type[] args = type.getActualTypeArguments();
        int paramCount = args == null ? 0 : args.length;
        if (paramCount == 0) {
            pt = NO_TYPES;
        } else {
            pt = new JavaType[paramCount];
            for (int i = 0; i < paramCount; i++) {
                pt[i] = _constructType(args[i], context);
            }
        }
        if (Map.class.isAssignableFrom(rawType)) {
            JavaType subtype = constructSimpleType(rawType, pt);
            JavaType[] mapParams = findTypeParameters(subtype, Map.class);
            if (mapParams.length != 2) {
                throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + rawType.getName() + " (found " + mapParams.length + ")");
            }
            return MapType.construct(rawType, mapParams[0], mapParams[1]);
        }
        if (Collection.class.isAssignableFrom(rawType)) {
            JavaType subtype2 = constructSimpleType(rawType, pt);
            JavaType[] collectionParams = findTypeParameters(subtype2, Collection.class);
            if (collectionParams.length != 1) {
                throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + rawType.getName() + " (found " + collectionParams.length + ")");
            }
            return CollectionType.construct(rawType, collectionParams[0]);
        }
        if (paramCount == 0) {
            return new SimpleType(rawType);
        }
        return constructSimpleType(rawType, pt);
    }

    protected JavaType _fromArrayType(GenericArrayType type, TypeBindings context) {
        JavaType compType = _constructType(type.getGenericComponentType(), context);
        return ArrayType.construct(compType);
    }

    protected JavaType _fromVariable(TypeVariable<?> type, TypeBindings context) {
        if (context == null) {
            return _unknownType();
        }
        String name = type.getName();
        JavaType actualType = context.findType(name);
        if (actualType == null) {
            Type[] bounds = type.getBounds();
            context._addPlaceholder(name);
            return _constructType(bounds[0], context);
        }
        return actualType;
    }

    protected JavaType _fromWildcard(WildcardType type, TypeBindings context) {
        return _constructType(type.getUpperBounds()[0], context);
    }

    private JavaType _mapType(Class<?> rawClass) {
        JavaType[] typeParams = findTypeParameters(rawClass, Map.class);
        if (typeParams == null) {
            return MapType.construct(rawClass, _unknownType(), _unknownType());
        }
        if (typeParams.length != 2) {
            throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": can not determine type parameters");
        }
        return MapType.construct(rawClass, typeParams[0], typeParams[1]);
    }

    private JavaType _collectionType(Class<?> rawClass) {
        JavaType[] typeParams = findTypeParameters(rawClass, Collection.class);
        if (typeParams == null) {
            return CollectionType.construct(rawClass, _unknownType());
        }
        if (typeParams.length != 1) {
            throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": can not determine type parameters");
        }
        return CollectionType.construct(rawClass, typeParams[0]);
    }

    protected JavaType _resolveVariableViaSubTypes(HierarchicType leafType, String variableName, TypeBindings bindings) {
        if (leafType != null && leafType.isGeneric()) {
            TypeVariable<?>[] typeVariables = leafType.getRawClass().getTypeParameters();
            int len = typeVariables.length;
            for (int i = 0; i < len; i++) {
                TypeVariable<?> tv = typeVariables[i];
                if (variableName.equals(tv.getName())) {
                    Type type = leafType.asGeneric().getActualTypeArguments()[i];
                    if (type instanceof TypeVariable) {
                        return _resolveVariableViaSubTypes(leafType.getSubType(), ((TypeVariable) type).getName(), bindings);
                    }
                    return _constructType(type, bindings);
                }
            }
        }
        return _unknownType();
    }

    protected JavaType _unknownType() {
        return new SimpleType(Object.class, null, null);
    }

    protected static HierarchicType _findSuperTypeChain(Class<?> subtype, Class<?> supertype) {
        return supertype.isInterface() ? _findSuperInterfaceChain(subtype, supertype) : _findSuperClassChain(subtype, supertype);
    }

    protected static HierarchicType _findSuperClassChain(Type currentType, Class<?> target) {
        HierarchicType sup;
        HierarchicType current = new HierarchicType(currentType);
        Class<?> raw = current.getRawClass();
        if (raw != target) {
            Type parent = raw.getGenericSuperclass();
            if (parent != null && (sup = _findSuperClassChain(parent, target)) != null) {
                sup.setSubType(current);
                current.setSuperType(sup);
                return current;
            }
            return null;
        }
        return current;
    }

    protected static HierarchicType _findSuperInterfaceChain(Type currentType, Class<?> target) {
        HierarchicType sup;
        HierarchicType current = new HierarchicType(currentType);
        Class<?> raw = current.getRawClass();
        if (raw != target) {
            Type[] parents = raw.getGenericInterfaces();
            if (parents != null) {
                for (Type type : parents) {
                    HierarchicType sup2 = _findSuperInterfaceChain(type, target);
                    if (sup2 != null) {
                        sup2.setSubType(current);
                        current.setSuperType(sup2);
                        return current;
                    }
                }
            }
            Type parent = raw.getGenericSuperclass();
            if (parent != null && (sup = _findSuperInterfaceChain(parent, target)) != null) {
                sup.setSubType(current);
                current.setSuperType(sup);
                return current;
            }
            return null;
        }
        return current;
    }
}
