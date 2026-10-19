package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
abstract class Creator {
    private Creator() {
    }

    static final class StringBased {
        protected final Constructor<?> _ctor;
        protected final Method _factoryMethod;
        protected final Class<?> _valueClass;

        public StringBased(Class<?> valueClass, AnnotatedConstructor ctor, AnnotatedMethod factoryMethod) {
            this._valueClass = valueClass;
            this._ctor = ctor == null ? null : ctor.getAnnotated();
            this._factoryMethod = factoryMethod != null ? factoryMethod.getAnnotated() : null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0010, code lost:
        
            r1 = null;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Object construct(java.lang.String r6) throws java.lang.Throwable {
            /*
                r5 = this;
                java.lang.reflect.Constructor<?> r1 = r5._ctor     // Catch: java.lang.Exception -> L24
                if (r1 == 0) goto L11
                java.lang.reflect.Constructor<?> r1 = r5._ctor     // Catch: java.lang.Exception -> L24
                r2 = 1
                java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> L24
                r3 = 0
                r2[r3] = r6     // Catch: java.lang.Exception -> L24
                java.lang.Object r1 = r1.newInstance(r2)     // Catch: java.lang.Exception -> L24
            L10:
                return r1
            L11:
                java.lang.reflect.Method r1 = r5._factoryMethod     // Catch: java.lang.Exception -> L24
                if (r1 == 0) goto L28
                java.lang.reflect.Method r1 = r5._factoryMethod     // Catch: java.lang.Exception -> L24
                java.lang.Class<?> r2 = r5._valueClass     // Catch: java.lang.Exception -> L24
                r3 = 1
                java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Exception -> L24
                r4 = 0
                r3[r4] = r6     // Catch: java.lang.Exception -> L24
                java.lang.Object r1 = r1.invoke(r2, r3)     // Catch: java.lang.Exception -> L24
                goto L10
            L24:
                r0 = move-exception
                org.codehaus.jackson.map.util.ClassUtil.unwrapAndThrowAsIAE(r0)
            L28:
                r1 = 0
                goto L10
            */
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.Creator.StringBased.construct(java.lang.String):java.lang.Object");
        }
    }

    static final class NumberBased {
        protected final Constructor<?> _intCtor;
        protected final Method _intFactoryMethod;
        protected final Constructor<?> _longCtor;
        protected final Method _longFactoryMethod;
        protected final Class<?> _valueClass;

        public NumberBased(Class<?> valueClass, AnnotatedConstructor intCtor, AnnotatedMethod ifm, AnnotatedConstructor longCtor, AnnotatedMethod lfm) {
            this._valueClass = valueClass;
            this._intCtor = intCtor == null ? null : intCtor.getAnnotated();
            this._longCtor = longCtor == null ? null : longCtor.getAnnotated();
            this._intFactoryMethod = ifm == null ? null : ifm.getAnnotated();
            this._longFactoryMethod = lfm != null ? lfm.getAnnotated() : null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0014, code lost:
        
            r1 = construct(r7);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Object construct(int r7) throws java.lang.Throwable {
            /*
                r6 = this;
                java.lang.reflect.Constructor<?> r1 = r6._intCtor     // Catch: java.lang.Exception -> L2c
                if (r1 == 0) goto L15
                java.lang.reflect.Constructor<?> r1 = r6._intCtor     // Catch: java.lang.Exception -> L2c
                r2 = 1
                java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> L2c
                r3 = 0
                java.lang.Integer r4 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Exception -> L2c
                r2[r3] = r4     // Catch: java.lang.Exception -> L2c
                java.lang.Object r1 = r1.newInstance(r2)     // Catch: java.lang.Exception -> L2c
            L14:
                return r1
            L15:
                java.lang.reflect.Method r1 = r6._intFactoryMethod     // Catch: java.lang.Exception -> L2c
                if (r1 == 0) goto L30
                java.lang.reflect.Method r1 = r6._intFactoryMethod     // Catch: java.lang.Exception -> L2c
                java.lang.Class<?> r2 = r6._valueClass     // Catch: java.lang.Exception -> L2c
                r3 = 1
                java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Exception -> L2c
                r4 = 0
                java.lang.Integer r5 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Exception -> L2c
                r3[r4] = r5     // Catch: java.lang.Exception -> L2c
                java.lang.Object r1 = r1.invoke(r2, r3)     // Catch: java.lang.Exception -> L2c
                goto L14
            L2c:
                r0 = move-exception
                org.codehaus.jackson.map.util.ClassUtil.unwrapAndThrowAsIAE(r0)
            L30:
                long r1 = (long) r7
                java.lang.Object r1 = r6.construct(r1)
                goto L14
            */
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.Creator.NumberBased.construct(int):java.lang.Object");
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0014, code lost:
        
            r1 = null;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Object construct(long r7) throws java.lang.Throwable {
            /*
                r6 = this;
                java.lang.reflect.Constructor<?> r1 = r6._longCtor     // Catch: java.lang.Exception -> L2c
                if (r1 == 0) goto L15
                java.lang.reflect.Constructor<?> r1 = r6._longCtor     // Catch: java.lang.Exception -> L2c
                r2 = 1
                java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> L2c
                r3 = 0
                java.lang.Long r4 = java.lang.Long.valueOf(r7)     // Catch: java.lang.Exception -> L2c
                r2[r3] = r4     // Catch: java.lang.Exception -> L2c
                java.lang.Object r1 = r1.newInstance(r2)     // Catch: java.lang.Exception -> L2c
            L14:
                return r1
            L15:
                java.lang.reflect.Method r1 = r6._longFactoryMethod     // Catch: java.lang.Exception -> L2c
                if (r1 == 0) goto L30
                java.lang.reflect.Method r1 = r6._longFactoryMethod     // Catch: java.lang.Exception -> L2c
                java.lang.Class<?> r2 = r6._valueClass     // Catch: java.lang.Exception -> L2c
                r3 = 1
                java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Exception -> L2c
                r4 = 0
                java.lang.Long r5 = java.lang.Long.valueOf(r7)     // Catch: java.lang.Exception -> L2c
                r3[r4] = r5     // Catch: java.lang.Exception -> L2c
                java.lang.Object r1 = r1.invoke(r2, r3)     // Catch: java.lang.Exception -> L2c
                goto L14
            L2c:
                r0 = move-exception
                org.codehaus.jackson.map.util.ClassUtil.unwrapAndThrowAsIAE(r0)
            L30:
                r1 = 0
                goto L14
            */
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.Creator.NumberBased.construct(long):java.lang.Object");
        }
    }

    static final class Delegating {
        protected final AnnotatedMember _creator;
        protected final Constructor<?> _ctor;
        protected JsonDeserializer<Object> _deserializer;
        protected final Method _factoryMethod;
        protected final JavaType _valueType;

        public Delegating(BasicBeanDescription beanDesc, AnnotatedConstructor ctor, AnnotatedMethod factory) {
            TypeBindings bindings = beanDesc.bindingsForBeanType();
            if (ctor != null) {
                this._creator = ctor;
                this._ctor = ctor.getAnnotated();
                this._factoryMethod = null;
                this._valueType = bindings.resolveType(ctor.getParameterType(0));
                return;
            }
            if (factory != null) {
                this._creator = factory;
                this._ctor = null;
                this._factoryMethod = factory.getAnnotated();
                this._valueType = bindings.resolveType(factory.getParameterType(0));
                return;
            }
            throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
        }

        public JavaType getValueType() {
            return this._valueType;
        }

        public AnnotatedMember getCreator() {
            return this._creator;
        }

        public void setDeserializer(JsonDeserializer<Object> deser) {
            this._deserializer = deser;
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws Throwable {
            Object objInvoke = null;
            Object value = this._deserializer.deserialize(jp, ctxt);
            try {
                if (this._ctor != null) {
                    objInvoke = this._ctor.newInstance(value);
                } else {
                    objInvoke = this._factoryMethod.invoke(null, value);
                }
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
            }
            return objInvoke;
        }
    }

    static final class PropertyBased {
        protected final Constructor<?> _ctor;
        protected final Object[] _defaultValues;
        protected final Method _factoryMethod;
        protected final HashMap<String, SettableBeanProperty> _properties;

        public PropertyBased(AnnotatedConstructor ctor, SettableBeanProperty[] ctorProps, AnnotatedMethod factory, SettableBeanProperty[] factoryProps) {
            SettableBeanProperty[] props;
            if (ctor != null) {
                this._ctor = ctor.getAnnotated();
                this._factoryMethod = null;
                props = ctorProps;
            } else if (factory != null) {
                this._ctor = null;
                this._factoryMethod = factory.getAnnotated();
                props = factoryProps;
            } else {
                throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
            }
            this._properties = new HashMap<>();
            Object[] defValues = null;
            int len = props.length;
            for (int i = 0; i < len; i++) {
                SettableBeanProperty prop = props[i];
                this._properties.put(prop.getName(), prop);
                if (prop.getType().isPrimitive()) {
                    defValues = defValues == null ? new Object[len] : defValues;
                    defValues[i] = ClassUtil.defaultValue(prop.getType().getRawClass());
                }
            }
            this._defaultValues = defValues;
        }

        public Collection<SettableBeanProperty> properties() {
            return this._properties.values();
        }

        public SettableBeanProperty findCreatorProperty(String name) {
            return this._properties.get(name);
        }

        public PropertyValueBuffer startBuilding(JsonParser jp, DeserializationContext ctxt) {
            return new PropertyValueBuffer(jp, ctxt, this._properties.size());
        }

        public Object build(PropertyValueBuffer buffer) throws Exception {
            Object bean = null;
            try {
                if (this._ctor != null) {
                    bean = this._ctor.newInstance(buffer.getParameters(this._defaultValues));
                } else {
                    bean = this._factoryMethod.invoke(null, buffer.getParameters(this._defaultValues));
                }
                for (PropertyValue pv = buffer.buffered(); pv != null; pv = pv.next) {
                    pv.assign(bean);
                }
            } catch (Exception e) {
                ClassUtil.throwRootCause(e);
            }
            return bean;
        }
    }
}
