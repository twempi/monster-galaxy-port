package org.codehaus.jackson.mrbean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.FieldVisitor;
import org.codehaus.jackson.org.objectweb.asm.MethodVisitor;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BeanBuilder {
    protected final Class<?> _implementedType;
    protected final TypeFactory _typeFactory;
    protected Map<String, Property> _beanProperties = new LinkedHashMap();
    protected LinkedHashMap<String, Method> _unsupportedMethods = new LinkedHashMap<>();

    public BeanBuilder(DeserializationConfig config, Class<?> implType) {
        this._implementedType = implType;
        this._typeFactory = config.getTypeFactory();
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0061  */
    /* JADX WARN: Code duplicated, block: B:29:0x0090  */
    /* JADX WARN: Code duplicated, block: B:32:0x0071 A[SYNTHETIC] */
    public BeanBuilder implement(boolean failOnUnrecognized) {
        ArrayList<Class<?>> implTypes = new ArrayList<>();
        implTypes.add(this._implementedType);
        BeanUtil.findSuperTypes(this._implementedType, Object.class, implTypes);
        for (Class<?> impl : implTypes) {
            Method[] arr$ = impl.getDeclaredMethods();
            for (Method m : arr$) {
                String methodName = m.getName();
                int argCount = m.getParameterTypes().length;
                if (argCount == 0) {
                    if (methodName.startsWith("get") || (methodName.startsWith("is") && returnsBoolean(m))) {
                        addGetter(m);
                    } else if (!BeanUtil.isConcrete(m) && !this._unsupportedMethods.containsKey(methodName)) {
                        if (failOnUnrecognized) {
                            throw new IllegalArgumentException("Unrecognized abstract method '" + methodName + "' (not a getter or setter) -- to avoid exception, disable AbstractTypeMaterializer.Feature.FAIL_ON_UNMATERIALIZED_METHOD");
                        }
                        this._unsupportedMethods.put(methodName, m);
                    }
                } else if (argCount == 1 && methodName.startsWith("set")) {
                    addSetter(m);
                } else if (!BeanUtil.isConcrete(m)) {
                    if (failOnUnrecognized) {
                        throw new IllegalArgumentException("Unrecognized abstract method '" + methodName + "' (not a getter or setter) -- to avoid exception, disable AbstractTypeMaterializer.Feature.FAIL_ON_UNMATERIALIZED_METHOD");
                    }
                    this._unsupportedMethods.put(methodName, m);
                }
            }
        }
        return this;
    }

    public byte[] build(String className) {
        String superName;
        ClassWriter cw = new ClassWriter(1);
        String internalClass = getInternalClassName(className);
        String implName = getInternalClassName(this._implementedType.getName());
        if (this._implementedType.isInterface()) {
            superName = "java/lang/Object";
            cw.visit(49, 33, internalClass, null, "java/lang/Object", new String[]{implName});
        } else {
            superName = implName;
            cw.visit(49, 33, internalClass, null, implName, null);
        }
        cw.visitSource(className + ".java", null);
        generateDefaultConstructor(cw, superName);
        for (Property prop : this._beanProperties.values()) {
            TypeDescription type = prop.selectType(this._typeFactory);
            createField(cw, prop, type);
            if (!prop.hasConcreteGetter()) {
                createGetter(cw, internalClass, prop, type);
            }
            if (!prop.hasConcreteSetter()) {
                createSetter(cw, internalClass, prop, type);
            }
        }
        for (Method m : this._unsupportedMethods.values()) {
            createUnimplementedMethod(cw, internalClass, m);
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    private static String getPropertyName(String methodName) {
        int prefixLen = methodName.startsWith("is") ? 2 : 3;
        String body = methodName.substring(prefixLen);
        StringBuilder sb = new StringBuilder(body);
        sb.setCharAt(0, Character.toLowerCase(body.charAt(0)));
        return sb.toString();
    }

    private static String buildGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String buildSetterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String getInternalClassName(String className) {
        return className.replace(".", "/");
    }

    private void addGetter(Method m) {
        Property prop = findProperty(getPropertyName(m.getName()));
        if (prop.getGetter() == null) {
            prop.setGetter(m);
        }
    }

    private void addSetter(Method m) {
        Property prop = findProperty(getPropertyName(m.getName()));
        if (prop.getSetter() == null) {
            prop.setSetter(m);
        }
    }

    private Property findProperty(String propName) {
        Property prop = this._beanProperties.get(propName);
        if (prop == null) {
            Property prop2 = new Property(propName);
            this._beanProperties.put(propName, prop2);
            return prop2;
        }
        return prop;
    }

    private static final boolean returnsBoolean(Method m) {
        Class<?> rt = m.getReturnType();
        return rt == Boolean.class || rt == Boolean.TYPE;
    }

    private static void generateDefaultConstructor(ClassWriter cw, String superName) {
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superName, "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createField(ClassWriter cw, Property prop, TypeDescription type) {
        String sig = type.hasGenerics() ? type.genericSignature() : null;
        String desc = type.erasedSignature();
        FieldVisitor fv = cw.visitField(1, prop.getFieldName(), desc, sig, null);
        fv.visitEnd();
    }

    private static void createSetter(ClassWriter cw, String internalClassName, Property prop, TypeDescription propertyType) {
        String desc;
        String methodName;
        Method setter = prop.getSetter();
        if (setter != null) {
            desc = Type.getMethodDescriptor(setter);
            methodName = setter.getName();
        } else {
            desc = "(" + propertyType.erasedSignature() + ")V";
            methodName = buildSetterName(prop.getName());
        }
        String sig = propertyType.hasGenerics() ? "(" + propertyType.genericSignature() + ")V" : null;
        MethodVisitor mv = cw.visitMethod(1, methodName, desc, sig, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitVarInsn(propertyType.getLoadOpcode(), 1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, internalClassName, prop.getFieldName(), propertyType.erasedSignature());
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createGetter(ClassWriter cw, String internalClassName, Property prop, TypeDescription propertyType) {
        String desc;
        String methodName;
        Method getter = prop.getGetter();
        if (getter != null) {
            desc = Type.getMethodDescriptor(getter);
            methodName = getter.getName();
        } else {
            desc = "()" + propertyType.erasedSignature();
            methodName = buildGetterName(prop.getName());
        }
        String sig = propertyType.hasGenerics() ? "()" + propertyType.genericSignature() : null;
        MethodVisitor mv = cw.visitMethod(1, methodName, desc, sig, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, internalClassName, prop.getFieldName(), propertyType.erasedSignature());
        mv.visitInsn(propertyType.getReturnOpcode());
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createUnimplementedMethod(ClassWriter cw, String internalClassName, Method method) {
        String exceptionName = getInternalClassName(UnsupportedOperationException.class.getName());
        String sig = Type.getMethodDescriptor(method);
        String name = method.getName();
        MethodVisitor mv = cw.visitMethod(1, name, sig, null, null);
        mv.visitTypeInsn(Opcodes.NEW, exceptionName);
        mv.visitInsn(89);
        mv.visitLdcInsn("Unimplemented method '" + name + "' (not a setter/getter, could not materialize)");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, exceptionName, "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static class Property {
        protected final String _fieldName;
        protected Method _getter;
        protected final String _name;
        protected Method _setter;

        public Property(String name) {
            this._name = name;
            this._fieldName = "_" + name;
        }

        public String getName() {
            return this._name;
        }

        public void setGetter(Method m) {
            this._getter = m;
        }

        public void setSetter(Method m) {
            this._setter = m;
        }

        public Method getGetter() {
            return this._getter;
        }

        public Method getSetter() {
            return this._setter;
        }

        public String getFieldName() {
            return this._fieldName;
        }

        public boolean hasConcreteGetter() {
            return this._getter != null && BeanUtil.isConcrete(this._getter);
        }

        public boolean hasConcreteSetter() {
            return this._setter != null && BeanUtil.isConcrete(this._setter);
        }

        private TypeDescription getterType(TypeFactory tf) {
            Class<?> context = this._getter.getDeclaringClass();
            return new TypeDescription(tf.constructType(this._getter.getGenericReturnType(), context));
        }

        private TypeDescription setterType(TypeFactory tf) {
            Class<?> context = this._setter.getDeclaringClass();
            return new TypeDescription(tf.constructType(this._setter.getGenericParameterTypes()[0], context));
        }

        public TypeDescription selectType(TypeFactory tf) {
            if (this._getter == null) {
                return setterType(tf);
            }
            if (this._setter == null) {
                return getterType(tf);
            }
            TypeDescription st = setterType(tf);
            TypeDescription gt = getterType(tf);
            TypeDescription specificType = TypeDescription.moreSpecificType(st, gt);
            if (specificType == null) {
                throw new IllegalArgumentException("Invalid property '" + getName() + "': incompatible types for getter/setter (" + gt + " vs " + st + ")");
            }
            return specificType;
        }
    }

    private static class TypeDescription {
        private final Type _asmType;
        private JavaType _jacksonType;

        public TypeDescription(JavaType type) {
            this._jacksonType = type;
            this._asmType = Type.getType(type.getRawClass());
        }

        public Class<?> getRawClass() {
            return this._jacksonType.getRawClass();
        }

        public String erasedSignature() {
            return this._jacksonType.getErasedSignature();
        }

        public String genericSignature() {
            return this._jacksonType.getGenericSignature();
        }

        public boolean hasGenerics() {
            return this._jacksonType.hasGenericTypes();
        }

        public int getLoadOpcode() {
            return this._asmType.getOpcode(21);
        }

        public int getReturnOpcode() {
            return this._asmType.getOpcode(172);
        }

        public String toString() {
            return this._jacksonType.toString();
        }

        public static TypeDescription moreSpecificType(TypeDescription desc1, TypeDescription desc2) {
            Class<?> c1 = desc1.getRawClass();
            Class<?> c2 = desc2.getRawClass();
            if (!c1.isAssignableFrom(c2)) {
                if (c2.isAssignableFrom(c1)) {
                    return desc1;
                }
                return null;
            }
            return desc2;
        }
    }
}
