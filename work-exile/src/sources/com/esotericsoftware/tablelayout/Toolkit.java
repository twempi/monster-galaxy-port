package com.esotericsoftware.tablelayout;

import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.tapjoy.TapjoyConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import twitter4j.conf.PropertyConfiguration;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Toolkit<C, T extends C, L extends BaseTableLayout> {
    private static final ArrayList<String> classPrefixes = new ArrayList<>();

    public abstract void addChild(C c, C c2, String str);

    public abstract void addDebugRectangle(L l, int i, int i2, int i3, int i4, int i5);

    public abstract void clearDebugRectangles(L l);

    public abstract L getLayout(T t);

    public abstract int getMaxHeight(C c);

    public abstract int getMaxWidth(C c);

    public abstract int getMinHeight(C c);

    public abstract int getMinWidth(C c);

    public abstract int getPrefHeight(C c);

    public abstract int getPrefWidth(C c);

    public abstract C newStack();

    public abstract void removeChild(C c, C c2);

    public T newTable(T t) {
        try {
            return (T) t.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create an instance of: " + t.getClass().getName());
        }
    }

    public void setWidget(L layout, Cell cell, C widget) {
        if (widget != null) {
            removeChild(layout.table, widget);
            layout.widgetToCell.remove(widget);
        }
        cell.widget = widget;
        layout.nameToWidget.put(cell.name, widget);
        layout.widgetToCell.put(widget, cell);
        addChild(layout.table, widget, null);
    }

    int getWidgetWidth(L layout, C widget, String value) {
        if (value.equals(BaseTableLayout.MIN)) {
            return getMinWidth(widget);
        }
        if (value.equals(BaseTableLayout.PREF)) {
            return getPrefWidth(widget);
        }
        return value.equals(BaseTableLayout.MAX) ? getMaxWidth(widget) : width(layout, value);
    }

    int getWidgetHeight(L layout, C widget, String value) {
        if (value.equals(BaseTableLayout.MIN)) {
            return getMinHeight(widget);
        }
        if (value.equals(BaseTableLayout.PREF)) {
            return getPrefHeight(widget);
        }
        return value.equals(BaseTableLayout.MAX) ? getMaxHeight(widget) : height(layout, value);
    }

    protected String validateSize(String size) {
        return size;
    }

    public C newWidget(L layout, String className) {
        try {
            return wrap(Class.forName(className).newInstance());
        } catch (Exception ex) {
            int n = classPrefixes.size();
            for (int i = 0; i < n; i++) {
                String prefix = classPrefixes.get(i);
                try {
                    return newInstance(prefix + className);
                } catch (Exception e) {
                }
            }
            throw new RuntimeException("Error creating instance of class: " + className, ex);
        }
    }

    protected C newInstance(String str) throws Exception {
        return (C) Class.forName(str).newInstance();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public C wrap(Object obj) {
        return obj instanceof BaseTableLayout ? (C) ((BaseTableLayout) obj).getTable() : obj;
    }

    public void setProperty(L layout, C object, String name, List<String> values) {
        try {
            invokeMethod(object, name, values);
        } catch (NoSuchMethodException e) {
            try {
                invokeMethod(object, "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1), values);
            } catch (NoSuchMethodException e2) {
                try {
                    Field field = object.getClass().getField(name);
                    Object value = convertType(object, field.getType(), name, values.get(0));
                    if (value != null) {
                        field.set(object, value);
                    }
                } catch (Exception e3) {
                    throw new RuntimeException("No method, bean property, or field found: " + name + "\nClass: " + object.getClass() + "\nValues: " + values);
                }
            }
        }
    }

    public void setTableProperty(L layout, String name, List<String> values) {
        String name2 = name.toLowerCase();
        int n = values.size();
        for (int i = 0; i < n; i++) {
            values.set(i, values.get(i).toLowerCase());
        }
        try {
            if (name2.equals(TapjoyConstants.TJC_DISPLAY_AD_SIZE)) {
                switch (values.size()) {
                    case 1:
                        break;
                    case 2:
                        layout.height = validateSize(values.get(1));
                        break;
                    default:
                        return;
                }
                layout.width = validateSize(values.get(0));
                return;
            }
            if (name2.equals("width") || name2.equals("w")) {
                layout.width = validateSize(values.get(0));
                return;
            }
            if (name2.equals("height") || name2.equals("h")) {
                layout.height = validateSize(values.get(0));
                return;
            }
            if (name2.equals("padding") || name2.equals("pad")) {
                switch (values.size()) {
                    case 1:
                        String strValidateSize = validateSize(values.get(0));
                        layout.padRight = strValidateSize;
                        layout.padBottom = strValidateSize;
                        layout.padLeft = strValidateSize;
                        layout.padTop = strValidateSize;
                        return;
                    case 4:
                        String value = values.get(3);
                        if (value.length() > 0) {
                            layout.padRight = validateSize(value);
                            break;
                        }
                    case 3:
                        String value2 = values.get(2);
                        if (value2.length() > 0) {
                            layout.padBottom = validateSize(value2);
                            break;
                        }
                    case 2:
                        String value3 = values.get(0);
                        if (value3.length() > 0) {
                            layout.padTop = validateSize(value3);
                        }
                        String value4 = values.get(1);
                        if (value4.length() > 0) {
                            layout.padLeft = validateSize(value4);
                            return;
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid number of values (" + values.size() + "): " + values);
                }
            } else {
                if (name2.startsWith("padding") || name2.startsWith("pad")) {
                    String name3 = name2.replace("padding", "").replace("pad", "");
                    if (name3.equals("top") || name3.equals("t")) {
                        layout.padTop = validateSize(values.get(0));
                        return;
                    }
                    if (name3.equals("left") || name3.equals("l")) {
                        layout.padLeft = validateSize(values.get(0));
                        return;
                    }
                    if (name3.equals("bottom") || name3.equals("b")) {
                        layout.padBottom = validateSize(values.get(0));
                        return;
                    } else {
                        if (name3.equals("right") || name3.equals("r")) {
                            layout.padRight = validateSize(values.get(0));
                            return;
                        }
                        throw new IllegalArgumentException("Unknown property.");
                    }
                }
                if (name2.equals("align")) {
                    int align = 0;
                    int n2 = values.size();
                    for (int i2 = 0; i2 < n2; i2++) {
                        String value5 = values.get(i2);
                        if (value5.equals("center")) {
                            align |= 1;
                        } else if (value5.equals("left")) {
                            align |= 8;
                        } else if (value5.equals("right")) {
                            align |= 16;
                        } else if (value5.equals("top")) {
                            align |= 2;
                        } else if (value5.equals("bottom")) {
                            align |= 4;
                        } else {
                            throw new IllegalArgumentException("Invalid value: " + value5);
                        }
                    }
                    layout.align = align;
                    return;
                }
                if (name2.equals(PropertyConfiguration.DEBUG)) {
                    int debug = values.size() == 0 ? 1 : 0;
                    int n3 = values.size();
                    for (int i3 = 0; i3 < n3; i3++) {
                        String value6 = values.get(i3);
                        if (value6.equalsIgnoreCase("all") || value6.equalsIgnoreCase("true")) {
                            debug |= 1;
                        }
                        if (value6.equalsIgnoreCase("cell")) {
                            debug |= 4;
                        }
                        if (value6.equalsIgnoreCase("table")) {
                            debug |= 2;
                        }
                        if (value6.equalsIgnoreCase("widget")) {
                            debug |= 8;
                        }
                    }
                    layout.debug = debug;
                    return;
                }
                throw new IllegalArgumentException("Unknown table property: " + name2);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error setting table property: " + name2, ex);
        }
    }

    public void setCellProperty(Cell c, String name, List<String> values) {
        String name2 = name.toLowerCase();
        int n = values.size();
        for (int i = 0; i < n; i++) {
            values.set(i, values.get(i).toLowerCase());
        }
        try {
            if (name2.equals("expand")) {
                switch (values.size()) {
                    case 0:
                        c.expandY = 1;
                        c.expandX = 1;
                        return;
                    case 1:
                        String value = values.get(0);
                        if (value.equals("x")) {
                            c.expandX = 1;
                            return;
                        } else {
                            if (value.equals("y")) {
                                c.expandY = 1;
                                return;
                            }
                            Integer numValueOf = Integer.valueOf(Integer.parseInt(value));
                            c.expandY = numValueOf;
                            c.expandX = numValueOf;
                            return;
                        }
                    case 2:
                        String value2 = values.get(0);
                        if (value2.length() > 0) {
                            c.expandX = Integer.valueOf(Integer.parseInt(value2));
                        }
                        String value3 = values.get(1);
                        if (value3.length() > 0) {
                            c.expandY = Integer.valueOf(Integer.parseInt(value3));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
            if (name2.equals("fill")) {
                switch (values.size()) {
                    case 0:
                        Float fValueOf = Float.valueOf(1.0f);
                        c.fillY = fValueOf;
                        c.fillX = fValueOf;
                        return;
                    case 1:
                        String value4 = values.get(0);
                        if (value4.equals("x")) {
                            c.fillX = Float.valueOf(1.0f);
                            return;
                        } else {
                            if (value4.equals("y")) {
                                c.fillY = Float.valueOf(1.0f);
                                return;
                            }
                            Float fValueOf2 = Float.valueOf(Integer.parseInt(value4) / 100.0f);
                            c.fillY = fValueOf2;
                            c.fillX = fValueOf2;
                            return;
                        }
                    case 2:
                        String value5 = values.get(0);
                        if (value5.length() > 0) {
                            c.fillX = Float.valueOf(Integer.parseInt(value5) / 100.0f);
                        }
                        String value6 = values.get(1);
                        if (value6.length() > 0) {
                            c.fillY = Float.valueOf(Integer.parseInt(value6) / 100.0f);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
            if (name2.equals(TapjoyConstants.TJC_DISPLAY_AD_SIZE)) {
                switch (values.size()) {
                    case 1:
                        String value7 = values.get(0);
                        if (value7.length() > 0) {
                            String strValidateSize = validateSize(value7);
                            c.prefHeight = strValidateSize;
                            c.prefWidth = strValidateSize;
                            c.minHeight = strValidateSize;
                            c.minWidth = strValidateSize;
                            return;
                        }
                        return;
                    case 2:
                        String value8 = values.get(0);
                        if (value8.length() > 0) {
                            String strValidateSize2 = validateSize(value8);
                            c.prefWidth = strValidateSize2;
                            c.minWidth = strValidateSize2;
                        }
                        String value9 = values.get(1);
                        if (value9.length() > 0) {
                            String strValidateSize3 = validateSize(value9);
                            c.prefHeight = strValidateSize3;
                            c.minHeight = strValidateSize3;
                            return;
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid number of values (" + values.size() + "): " + values);
                }
            }
            if (name2.equals("width") || name2.equals("w")) {
                switch (values.size()) {
                    case 3:
                        String value10 = values.get(2);
                        if (value10.length() > 0) {
                            c.maxWidth = validateSize(value10);
                            break;
                        }
                    case 2:
                        String value11 = values.get(1);
                        if (value11.length() > 0) {
                            c.prefWidth = validateSize(value11);
                            break;
                        }
                    case 1:
                        String value12 = values.get(0);
                        if (value12.length() > 0) {
                            c.minWidth = validateSize(value12);
                            return;
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid number of values (" + values.size() + "): " + values);
                }
            } else if (name2.equals("height") || name2.equals("h")) {
                switch (values.size()) {
                    case 3:
                        String value13 = values.get(2);
                        if (value13.length() > 0) {
                            c.maxHeight = validateSize(value13);
                            break;
                        }
                    case 2:
                        String value14 = values.get(1);
                        if (value14.length() > 0) {
                            c.prefHeight = validateSize(value14);
                            break;
                        }
                    case 1:
                        String value15 = values.get(0);
                        if (value15.length() > 0) {
                            c.minHeight = validateSize(value15);
                            return;
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid number of values (" + values.size() + "): " + values);
                }
            } else if (name2.equals("spacing") || name2.equals("space")) {
                switch (values.size()) {
                    case 1:
                        String strValidateSize4 = validateSize(values.get(0));
                        c.spaceRight = strValidateSize4;
                        c.spaceBottom = strValidateSize4;
                        c.spaceLeft = strValidateSize4;
                        c.spaceTop = strValidateSize4;
                        return;
                    case 4:
                        String value16 = values.get(3);
                        if (value16.length() > 0) {
                            c.spaceRight = validateSize(value16);
                            break;
                        }
                    case 3:
                        String value17 = values.get(2);
                        if (value17.length() > 0) {
                            c.spaceBottom = validateSize(value17);
                            break;
                        }
                    case 2:
                        String value18 = values.get(0);
                        if (value18.length() > 0) {
                            c.spaceTop = validateSize(value18);
                        }
                        String value19 = values.get(1);
                        if (value19.length() > 0) {
                            c.spaceLeft = validateSize(value19);
                            return;
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid number of values (" + values.size() + "): " + values);
                }
            } else if (name2.equals("padding") || name2.equals("pad")) {
                switch (values.size()) {
                    case 1:
                        String strValidateSize5 = validateSize(values.get(0));
                        c.padRight = strValidateSize5;
                        c.padBottom = strValidateSize5;
                        c.padLeft = strValidateSize5;
                        c.padTop = strValidateSize5;
                        return;
                    case 4:
                        String value20 = values.get(3);
                        if (value20.length() > 0) {
                            c.padRight = validateSize(value20);
                            break;
                        }
                    case 3:
                        String value21 = values.get(2);
                        if (value21.length() > 0) {
                            c.padBottom = validateSize(value21);
                            break;
                        }
                    case 2:
                        String value22 = values.get(0);
                        if (value22.length() > 0) {
                            c.padTop = validateSize(value22);
                        }
                        String value23 = values.get(1);
                        if (value23.length() > 0) {
                            c.padLeft = validateSize(value23);
                            return;
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid number of values (" + values.size() + "): " + values);
                }
            } else {
                if (name2.startsWith("padding") || name2.startsWith("pad")) {
                    String name3 = name2.replace("padding", "").replace("pad", "");
                    if (name3.equals("top") || name3.equals("t")) {
                        c.padTop = validateSize(values.get(0));
                        return;
                    }
                    if (name3.equals("left") || name3.equals("l")) {
                        c.padLeft = validateSize(values.get(0));
                        return;
                    }
                    if (name3.equals("bottom") || name3.equals("b")) {
                        c.padBottom = validateSize(values.get(0));
                        return;
                    } else {
                        if (name3.equals("right") || name3.equals("r")) {
                            c.padRight = validateSize(values.get(0));
                            return;
                        }
                        throw new IllegalArgumentException("Unknown property.");
                    }
                }
                if (name2.startsWith("spacing") || name2.startsWith("space")) {
                    String name4 = name2.replace("spacing", "").replace("space", "");
                    if (name4.equals("top") || name4.equals("t")) {
                        c.spaceTop = validateSize(values.get(0));
                        return;
                    }
                    if (name4.equals("left") || name4.equals("l")) {
                        c.spaceLeft = validateSize(values.get(0));
                        return;
                    }
                    if (name4.equals("bottom") || name4.equals("b")) {
                        c.spaceBottom = validateSize(values.get(0));
                        return;
                    } else {
                        if (name4.equals("right") || name4.equals("r")) {
                            c.spaceRight = validateSize(values.get(0));
                            return;
                        }
                        throw new IllegalArgumentException("Unknown property.");
                    }
                }
                if (name2.equals("align")) {
                    c.align = 0;
                    int n2 = values.size();
                    for (int i2 = 0; i2 < n2; i2++) {
                        String value24 = values.get(i2);
                        if (value24.equals("center")) {
                            c.align = Integer.valueOf(c.align.intValue() | 1);
                        } else if (value24.equals("left")) {
                            c.align = Integer.valueOf(c.align.intValue() | 8);
                        } else if (value24.equals("right")) {
                            c.align = Integer.valueOf(c.align.intValue() | 16);
                        } else if (value24.equals("top")) {
                            c.align = Integer.valueOf(c.align.intValue() | 2);
                        } else if (value24.equals("bottom")) {
                            c.align = Integer.valueOf(c.align.intValue() | 4);
                        } else {
                            throw new IllegalArgumentException("Invalid value: " + value24);
                        }
                    }
                    return;
                }
                if (!name2.equals("ignore")) {
                    if (name2.equals("colspan")) {
                        c.colspan = Integer.valueOf(Integer.parseInt(values.get(0)));
                        return;
                    }
                    if (name2.equals("uniform")) {
                        if (values.size() == 0) {
                            c.uniformY = true;
                            c.uniformX = true;
                        }
                        int n3 = values.size();
                        for (int i3 = 0; i3 < n3; i3++) {
                            String value25 = values.get(i3);
                            if (value25.equals("x")) {
                                c.uniformX = true;
                            } else if (value25.equals("y")) {
                                c.uniformY = true;
                            } else if (value25.equals("false")) {
                                c.uniformY = null;
                                c.uniformY = null;
                            } else {
                                throw new IllegalArgumentException("Invalid value: " + value25);
                            }
                        }
                        return;
                    }
                    throw new IllegalArgumentException("Unknown cell property.");
                }
                c.ignore = Boolean.valueOf(values.size() == 0 ? true : Boolean.valueOf(values.get(0)).booleanValue());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error setting cell property: " + name2, ex);
        }
    }

    public int width(L layout, String value) {
        int length;
        if (value == null || (length = value.length()) == 0) {
            return width(0.0f);
        }
        if (value.charAt(length - 1) == '%' && length > 1) {
            return (int) ((layout.getLayoutWidth() * Integer.parseInt(value.substring(0, length - 1))) / 100.0f);
        }
        if (value.endsWith("px")) {
            return Integer.parseInt(value.substring(0, value.length() - 2));
        }
        return width(value != null ? Integer.parseInt(value) : 0.0f);
    }

    public int width(float value) {
        return (int) value;
    }

    public int height(L layout, String value) {
        int length;
        if (value == null || (length = value.length()) == 0) {
            return height(0.0f);
        }
        if (value.charAt(length - 1) == '%' && length > 1) {
            return (int) ((layout.getLayoutHeight() * Integer.parseInt(value.substring(0, length - 1))) / 100.0f);
        }
        if (value.endsWith("px")) {
            return Integer.parseInt(value.substring(0, value.length() - 2));
        }
        return height(value != null ? Integer.parseInt(value) : 0.0f);
    }

    public int height(float value) {
        return (int) value;
    }

    private void invokeMethod(Object object, String name, List<String> values) throws NoSuchMethodException {
        Object[] params = values.toArray();
        Class<?>[] clsArr = new Class[params.length];
        Method method = null;
        try {
            int n = params.length;
            for (int i = 0; i < n; i++) {
                clsArr[i] = String.class;
            }
            method = object.getClass().getMethod(name, clsArr);
        } catch (NoSuchMethodException e) {
            try {
                int n2 = params.length;
                for (int i2 = 0; i2 < n2; i2++) {
                    clsArr[i2] = CharSequence.class;
                }
                method = object.getClass().getMethod(name, clsArr);
            } catch (NoSuchMethodException e2) {
            }
        }
        if (method != null) {
            try {
                method.invoke(object, params);
                return;
            } catch (Exception ex) {
                throw new RuntimeException("Error invoking method: " + name, ex);
            }
        }
        Method[] methods = object.getClass().getMethods();
        for (Method method2 : methods) {
            if (method2.getName().equalsIgnoreCase(name)) {
                Class<?>[] parameterTypes = method2.getParameterTypes();
                if (parameterTypes.length == values.size()) {
                    Object[] params2 = values.toArray();
                    int ii = 0;
                    int nn = parameterTypes.length;
                    while (true) {
                        if (ii < nn) {
                            Object value = convertType(object, parameterTypes[ii], name, (String) params2[ii]);
                            if (value == null) {
                                break;
                            }
                            params2[ii] = value;
                            ii++;
                        } else {
                            try {
                                method2.invoke(object, params2);
                                return;
                            } catch (Exception ex2) {
                                throw new RuntimeException("Error invoking method: " + name, ex2);
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        throw new NoSuchMethodException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Object convertType(Object obj, Class cls, String str, String str2) {
        if (cls != String.class && cls != CharSequence.class) {
            try {
                if (cls == Integer.TYPE || cls == Integer.class) {
                    str2 = Integer.valueOf(str2);
                } else if (cls == Float.TYPE || cls == Float.class) {
                    str2 = Float.valueOf(str2);
                } else if (cls == Long.TYPE || cls == Long.class) {
                    str2 = Long.valueOf(str2);
                } else if (cls == Double.TYPE || cls == Double.class) {
                    str2 = Double.valueOf(str2);
                } else {
                    if (cls != Short.TYPE && cls != Short.class) {
                        if (cls == Byte.TYPE || cls == Byte.class) {
                            str2 = Byte.valueOf(str2);
                        }
                        if (cls == Boolean.TYPE || cls == Boolean.class) {
                            return Boolean.valueOf(str2);
                        }
                        if (cls == Character.TYPE || cls == Character.class) {
                            return Character.valueOf(str2.charAt(0));
                        }
                        try {
                            Field field = getField(cls, str2);
                            if (field != null && cls == field.getType()) {
                                return field.get(null);
                            }
                        } catch (Exception e) {
                        }
                        try {
                            Field field2 = getField(obj.getClass(), str2);
                            if (field2 != null && cls == field2.getType()) {
                                return field2.get(null);
                            }
                        } catch (Exception e2) {
                        }
                        return null;
                    }
                    str2 = Short.valueOf(str2);
                }
                return str2;
            } catch (NumberFormatException e3) {
            }
        } else {
            return str2;
        }
    }

    private static Field getField(Class type, String name) {
        try {
            Field field = type.getField(name);
            if (field != null) {
                return field;
            }
        } catch (Exception e) {
        }
        while (type != null && type != Object.class) {
            Field[] fields = type.getDeclaredFields();
            int n = fields.length;
            for (int i = 0; i < n; i++) {
                if (fields[i].getName().equalsIgnoreCase(name)) {
                    return fields[i];
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    public static void addClassPrefix(String prefix) {
        classPrefixes.add(prefix);
    }
}
