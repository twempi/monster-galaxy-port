package org.codehaus.jackson.org.objectweb.asm;

import com.badlogic.gdx.Input;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Type {
    public static final int ARRAY = 9;
    public static final int BOOLEAN = 1;
    public static final int BYTE = 3;
    public static final int CHAR = 2;
    public static final int DOUBLE = 8;
    public static final int FLOAT = 6;
    public static final int INT = 5;
    public static final int LONG = 7;
    public static final int OBJECT = 10;
    public static final int SHORT = 4;
    public static final int VOID = 0;
    private final int a;
    private final char[] b;
    private final int c;
    private final int d;
    public static final Type VOID_TYPE = new Type(0, null, 1443168256, 1);
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);

    private Type(int i, char[] cArr, int i2, int i3) {
        this.a = i;
        this.b = cArr;
        this.c = i2;
        this.d = i3;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static Type a(char[] cArr, int i) {
        int i2 = 1;
        switch (cArr[i]) {
            case Input.Keys.ENTER /* 66 */:
                return BYTE_TYPE;
            case 'C':
                return CHAR_TYPE;
            case Input.Keys.GRAVE /* 68 */:
                return DOUBLE_TYPE;
            case Input.Keys.EQUALS /* 70 */:
                return FLOAT_TYPE;
            case Input.Keys.BACKSLASH /* 73 */:
                return INT_TYPE;
            case Input.Keys.SEMICOLON /* 74 */:
                return LONG_TYPE;
            case 'S':
                return SHORT_TYPE;
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case '[':
                while (cArr[i + i2] == '[') {
                    i2++;
                }
                if (cArr[i + i2] == 'L') {
                    do {
                        i2++;
                    } while (cArr[i + i2] != ';');
                }
                return new Type(9, cArr, i, i2 + 1);
            default:
                while (cArr[i + i2] != ';') {
                    i2++;
                }
                return new Type(10, cArr, i + 1, i2 - 1);
        }
    }

    private void a(StringBuffer stringBuffer) {
        if (this.b == null) {
            stringBuffer.append((char) ((this.c & (-16777216)) >>> 24));
        } else {
            if (this.a == 9) {
                stringBuffer.append(this.b, this.c, this.d);
                return;
            }
            stringBuffer.append('L');
            stringBuffer.append(this.b, this.c, this.d);
            stringBuffer.append(';');
        }
    }

    private static void a(StringBuffer stringBuffer, Class cls) {
        char c;
        while (!cls.isPrimitive()) {
            if (!cls.isArray()) {
                stringBuffer.append('L');
                String name = cls.getName();
                int length = name.length();
                for (int i = 0; i < length; i++) {
                    char cCharAt = name.charAt(i);
                    if (cCharAt == '.') {
                        cCharAt = '/';
                    }
                    stringBuffer.append(cCharAt);
                }
                stringBuffer.append(';');
                return;
            }
            stringBuffer.append('[');
            cls = cls.getComponentType();
        }
        if (cls == Integer.TYPE) {
            c = 'I';
        } else if (cls == Void.TYPE) {
            c = 'V';
        } else if (cls == Boolean.TYPE) {
            c = 'Z';
        } else if (cls == Byte.TYPE) {
            c = 'B';
        } else if (cls == Character.TYPE) {
            c = 'C';
        } else if (cls == Short.TYPE) {
            c = 'S';
        } else if (cls == Double.TYPE) {
            c = 'D';
        } else {
            c = cls == Float.TYPE ? 'F' : 'J';
        }
        stringBuffer.append(c);
    }

    public static Type[] getArgumentTypes(String str) {
        int i;
        int i2 = 1;
        char[] charArray = str.toCharArray();
        int i3 = 0;
        int i4 = 1;
        while (true) {
            int i5 = i4 + 1;
            char c = charArray[i4];
            if (c == ')') {
                break;
            }
            if (c == 'L') {
                do {
                    i = i5;
                    i5 = i + 1;
                } while (charArray[i] != ';');
                i3++;
                i4 = i5;
            } else if (c != '[') {
                i3++;
                i4 = i5;
            } else {
                i4 = i5;
            }
        }
        Type[] typeArr = new Type[i3];
        int i6 = 0;
        while (charArray[i2] != ')') {
            typeArr[i6] = a(charArray, i2);
            i2 += (typeArr[i6].a == 10 ? 2 : 0) + typeArr[i6].d;
            i6++;
        }
        return typeArr;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] typeArr = new Type[parameterTypes.length];
        for (int length = parameterTypes.length - 1; length >= 0; length--) {
            typeArr[length] = getType(parameterTypes[length]);
        }
        return typeArr;
    }

    public static int getArgumentsAndReturnSizes(String str) {
        int i;
        char cCharAt;
        int i2;
        int i3 = 1;
        int i4 = 1;
        int i5 = 1;
        while (true) {
            i = i4 + 1;
            char cCharAt2 = str.charAt(i4);
            if (cCharAt2 == ')') {
                break;
            }
            if (cCharAt2 == 'L') {
                do {
                    i2 = i;
                    i = i2 + 1;
                } while (str.charAt(i2) != ';');
                i5++;
                i4 = i;
            } else if (cCharAt2 == '[') {
                while (true) {
                    cCharAt = str.charAt(i);
                    if (cCharAt != '[') {
                        break;
                    }
                    i++;
                }
                if (cCharAt == 'D' || cCharAt == 'J') {
                    i5--;
                    i4 = i;
                } else {
                    i4 = i;
                }
            } else if (cCharAt2 == 'D' || cCharAt2 == 'J') {
                i5 += 2;
                i4 = i;
            } else {
                i5++;
                i4 = i;
            }
        }
        char cCharAt3 = str.charAt(i);
        int i6 = i5 << 2;
        if (cCharAt3 == 'V') {
            i3 = 0;
        } else if (cCharAt3 == 'D' || cCharAt3 == 'J') {
            i3 = 2;
        }
        return i6 | i3;
    }

    public static String getConstructorDescriptor(Constructor constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class<?> cls : parameterTypes) {
            a(stringBuffer, cls);
        }
        return stringBuffer.append(")V").toString();
    }

    public static String getDescriptor(Class cls) {
        StringBuffer stringBuffer = new StringBuffer();
        a(stringBuffer, cls);
        return stringBuffer.toString();
    }

    public static String getInternalName(Class cls) {
        return cls.getName().replace('.', '/');
    }

    public static String getMethodDescriptor(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class<?> cls : parameterTypes) {
            a(stringBuffer, cls);
        }
        stringBuffer.append(')');
        a(stringBuffer, method.getReturnType());
        return stringBuffer.toString();
    }

    public static String getMethodDescriptor(Type type, Type[] typeArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Type type2 : typeArr) {
            type2.a(stringBuffer);
        }
        stringBuffer.append(')');
        type.a(stringBuffer);
        return stringBuffer.toString();
    }

    public static Type getObjectType(String str) {
        char[] charArray = str.toCharArray();
        return new Type(charArray[0] == '[' ? 9 : 10, charArray, 0, charArray.length);
    }

    public static Type getReturnType(String str) {
        return a(str.toCharArray(), str.indexOf(41) + 1);
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    public static Type getType(Class cls) {
        if (!cls.isPrimitive()) {
            return getType(getDescriptor(cls));
        }
        if (cls == Integer.TYPE) {
            return INT_TYPE;
        }
        if (cls == Void.TYPE) {
            return VOID_TYPE;
        }
        if (cls == Boolean.TYPE) {
            return BOOLEAN_TYPE;
        }
        if (cls == Byte.TYPE) {
            return BYTE_TYPE;
        }
        if (cls == Character.TYPE) {
            return CHAR_TYPE;
        }
        if (cls == Short.TYPE) {
            return SHORT_TYPE;
        }
        if (cls == Double.TYPE) {
            return DOUBLE_TYPE;
        }
        return cls == Float.TYPE ? FLOAT_TYPE : LONG_TYPE;
    }

    public static Type getType(String str) {
        return a(str.toCharArray(), 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Type)) {
            return false;
        }
        Type type = (Type) obj;
        if (this.a != type.a) {
            return false;
        }
        if (this.a != 10 && this.a != 9) {
            return true;
        }
        if (this.d != type.d) {
            return false;
        }
        int i = this.c;
        int i2 = type.c;
        int i3 = this.d + i;
        while (i < i3) {
            if (this.b[i] != type.b[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    public String getClassName() {
        switch (this.a) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return "byte";
            case 4:
                return "short";
            case 5:
                return "int";
            case 6:
                return "float";
            case 7:
                return "long";
            case 8:
                return "double";
            case 9:
                StringBuffer stringBuffer = new StringBuffer(getElementType().getClassName());
                for (int dimensions = getDimensions(); dimensions > 0; dimensions--) {
                    stringBuffer.append("[]");
                }
                return stringBuffer.toString();
            default:
                return new String(this.b, this.c, this.d).replace('/', '.');
        }
    }

    public String getDescriptor() {
        StringBuffer stringBuffer = new StringBuffer();
        a(stringBuffer);
        return stringBuffer.toString();
    }

    public int getDimensions() {
        int i = 1;
        while (this.b[this.c + i] == '[') {
            i++;
        }
        return i;
    }

    public Type getElementType() {
        return a(this.b, this.c + getDimensions());
    }

    public String getInternalName() {
        return new String(this.b, this.c, this.d);
    }

    public int getOpcode(int i) {
        if (i == 46 || i == 79) {
            return (this.b == null ? (this.c & 65280) >> 8 : 4) + i;
        }
        return (this.b == null ? (this.c & 16711680) >> 16 : 4) + i;
    }

    public int getSize() {
        if (this.b == null) {
            return this.c & 255;
        }
        return 1;
    }

    public int getSort() {
        return this.a;
    }

    public int hashCode() {
        int i = this.a * 13;
        if (this.a == 10 || this.a == 9) {
            int i2 = this.c;
            int i3 = i2 + this.d;
            while (i2 < i3) {
                int i4 = (i + this.b[i2]) * 17;
                i2++;
                i = i4;
            }
        }
        return i;
    }

    public String toString() {
        return getDescriptor();
    }
}
