package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/* JADX INFO: loaded from: classes.dex */
public abstract class FilteredBeanPropertyWriter {
    public static BeanPropertyWriter constructViewBased(BeanPropertyWriter base, Class<?>[] viewsToIncludeIn) {
        return viewsToIncludeIn.length == 1 ? new SingleView(base, viewsToIncludeIn[0]) : new MultiView(base, viewsToIncludeIn);
    }

    private static final class SingleView extends BeanPropertyWriter {
        protected final BeanPropertyWriter _delegate;
        protected final Class<?> _view;

        protected SingleView(BeanPropertyWriter delegate, Class<?> view) {
            super(delegate);
            this._delegate = delegate;
            this._view = view;
        }

        @Override // org.codehaus.jackson.map.ser.BeanPropertyWriter
        public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser) {
            return new SingleView(this._delegate.withSerializer(ser), this._view);
        }

        @Override // org.codehaus.jackson.map.ser.BeanPropertyWriter
        public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
            Class<?> activeView = prov.getSerializationView();
            if (activeView == null || this._view.isAssignableFrom(activeView)) {
                this._delegate.serializeAsField(bean, jgen, prov);
            }
        }
    }

    private static final class MultiView extends BeanPropertyWriter {
        protected final BeanPropertyWriter _delegate;
        protected final Class<?>[] _views;

        protected MultiView(BeanPropertyWriter delegate, Class<?>[] views) {
            super(delegate);
            this._delegate = delegate;
            this._views = views;
        }

        @Override // org.codehaus.jackson.map.ser.BeanPropertyWriter
        public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser) {
            return new MultiView(this._delegate.withSerializer(ser), this._views);
        }

        @Override // org.codehaus.jackson.map.ser.BeanPropertyWriter
        public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
            Class<?> activeView = prov.getSerializationView();
            if (activeView != null) {
                int i = 0;
                int len = this._views.length;
                while (i < len && !this._views[i].isAssignableFrom(activeView)) {
                    i++;
                }
                if (i == len) {
                    return;
                }
            }
            this._delegate.serializeAsField(bean, jgen, prov);
        }
    }
}
