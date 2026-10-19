package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ClassWriter implements ClassVisitor {
    public static final int COMPUTE_FRAMES = 2;
    public static final int COMPUTE_MAXS = 1;
    static final byte[] a;
    MethodWriter A;
    MethodWriter B;
    private short D;
    Item[] E;
    String F;
    private final boolean G;
    private final boolean H;
    boolean I;
    ClassReader J;
    int b;
    int c;
    final ByteVector d;
    Item[] e;
    int f;
    final Item g;
    final Item h;
    final Item i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int[] o;
    private int p;
    private ByteVector q;
    private int r;
    private int s;
    private AnnotationWriter t;
    private AnnotationWriter u;
    private Attribute v;
    private int w;
    private ByteVector x;
    FieldWriter y;
    FieldWriter z;

    static {
        byte[] bArr = new byte[220];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) ("AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHHFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII".charAt(i) - 'A');
        }
        a = bArr;
    }

    public ClassWriter(int i) {
        this.c = 1;
        this.d = new ByteVector();
        this.e = new Item[256];
        this.f = (int) (0.75d * ((double) this.e.length));
        this.g = new Item();
        this.h = new Item();
        this.i = new Item();
        this.H = (i & 1) != 0;
        this.G = (i & 2) != 0;
    }

    public ClassWriter(ClassReader classReader, int i) {
        this(i);
        classReader.a(this);
        this.J = classReader;
    }

    private Item a(Item item) {
        Item item2 = this.e[item.j % this.e.length];
        while (item2 != null && (item2.b != item.b || !item.a(item2))) {
            item2 = item2.k;
        }
        return item2;
    }

    private void a(int i, int i2, int i3) {
        this.d.b(i, i2).putShort(i3);
    }

    private Item b(String str) {
        this.h.a(8, str, null, null);
        Item itemA = a(this.h);
        if (itemA != null) {
            return itemA;
        }
        this.d.b(8, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        Item item = new Item(i, this.h);
        b(item);
        return item;
    }

    private void b(Item item) {
        if (this.c > this.f) {
            int length = this.e.length;
            int i = (length * 2) + 1;
            Item[] itemArr = new Item[i];
            for (int i2 = length - 1; i2 >= 0; i2--) {
                Item item2 = this.e[i2];
                while (item2 != null) {
                    int length2 = item2.j % itemArr.length;
                    Item item3 = item2.k;
                    item2.k = itemArr[length2];
                    itemArr[length2] = item2;
                    item2 = item3;
                }
            }
            this.e = itemArr;
            this.f = (int) (((double) i) * 0.75d);
        }
        int length3 = item.j % this.e.length;
        item.k = this.e[length3];
        this.e[length3] = item;
    }

    private Item c(Item item) {
        this.D = (short) (this.D + 1);
        Item item2 = new Item(this.D, this.g);
        b(item2);
        if (this.E == null) {
            this.E = new Item[16];
        }
        if (this.D == this.E.length) {
            Item[] itemArr = new Item[this.E.length * 2];
            System.arraycopy(this.E, 0, itemArr, 0, this.E.length);
            this.E = itemArr;
        }
        this.E[this.D] = item2;
        return item2;
    }

    int a(int i, int i2) {
        this.h.b = 15;
        this.h.d = ((long) i) | (((long) i2) << 32);
        this.h.j = Integer.MAX_VALUE & (i + 15 + i2);
        Item itemA = a(this.h);
        if (itemA == null) {
            String str = this.E[i].g;
            String str2 = this.E[i2].g;
            this.h.c = c(getCommonSuperClass(str, str2));
            itemA = new Item(0, this.h);
            b(itemA);
        }
        return itemA.c;
    }

    int a(String str, int i) {
        this.g.b = 14;
        this.g.c = i;
        this.g.g = str;
        this.g.j = Integer.MAX_VALUE & (str.hashCode() + 14 + i);
        Item itemA = a(this.g);
        if (itemA == null) {
            itemA = c(this.g);
        }
        return itemA.a;
    }

    Item a(double d) {
        this.g.a(d);
        Item itemA = a(this.g);
        if (itemA != null) {
            return itemA;
        }
        this.d.putByte(6).putLong(this.g.d);
        Item item = new Item(this.c, this.g);
        b(item);
        this.c += 2;
        return item;
    }

    Item a(float f) {
        this.g.a(f);
        Item itemA = a(this.g);
        if (itemA != null) {
            return itemA;
        }
        this.d.putByte(4).putInt(this.g.c);
        int i = this.c;
        this.c = i + 1;
        Item item = new Item(i, this.g);
        b(item);
        return item;
    }

    Item a(int i) {
        this.g.a(i);
        Item itemA = a(this.g);
        if (itemA != null) {
            return itemA;
        }
        this.d.putByte(3).putInt(i);
        int i2 = this.c;
        this.c = i2 + 1;
        Item item = new Item(i2, this.g);
        b(item);
        return item;
    }

    Item a(long j) {
        this.g.a(j);
        Item itemA = a(this.g);
        if (itemA != null) {
            return itemA;
        }
        this.d.putByte(5).putLong(j);
        Item item = new Item(this.c, this.g);
        b(item);
        this.c += 2;
        return item;
    }

    Item a(Object obj) {
        if (obj instanceof Integer) {
            return a(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return a(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return a((int) ((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return a(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return a(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        if (obj instanceof Float) {
            return a(((Float) obj).floatValue());
        }
        if (obj instanceof Long) {
            return a(((Long) obj).longValue());
        }
        if (obj instanceof Double) {
            return a(((Double) obj).doubleValue());
        }
        if (obj instanceof String) {
            return b((String) obj);
        }
        if (!(obj instanceof Type)) {
            throw new IllegalArgumentException(new StringBuffer().append("value ").append(obj).toString());
        }
        Type type = (Type) obj;
        return a(type.getSort() == 10 ? type.getInternalName() : type.getDescriptor());
    }

    Item a(String str) {
        this.h.a(7, str, null, null);
        Item itemA = a(this.h);
        if (itemA != null) {
            return itemA;
        }
        this.d.b(7, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        Item item = new Item(i, this.h);
        b(item);
        return item;
    }

    Item a(String str, String str2) {
        this.h.a(12, str, str2, null);
        Item itemA = a(this.h);
        if (itemA != null) {
            return itemA;
        }
        a(12, newUTF8(str), newUTF8(str2));
        int i = this.c;
        this.c = i + 1;
        Item item = new Item(i, this.h);
        b(item);
        return item;
    }

    Item a(String str, String str2, String str3) {
        this.i.a(9, str, str2, str3);
        Item itemA = a(this.i);
        if (itemA != null) {
            return itemA;
        }
        a(9, newClass(str), newNameType(str2, str3));
        int i = this.c;
        this.c = i + 1;
        Item item = new Item(i, this.i);
        b(item);
        return item;
    }

    Item a(String str, String str2, String str3, boolean z) {
        int i = z ? 11 : 10;
        this.i.a(i, str, str2, str3);
        Item itemA = a(this.i);
        if (itemA != null) {
            return itemA;
        }
        a(i, newClass(str), newNameType(str2, str3));
        int i2 = this.c;
        this.c = i2 + 1;
        Item item = new Item(i2, this.i);
        b(item);
        return item;
    }

    int c(String str) {
        this.g.a(13, str, null, null);
        Item itemA = a(this.g);
        if (itemA == null) {
            itemA = c(this.g);
        }
        return itemA.a;
    }

    protected String getCommonSuperClass(String str, String str2) {
        try {
            Class<?> cls = Class.forName(str.replace('/', '.'));
            Class<?> cls2 = Class.forName(str2.replace('/', '.'));
            if (cls.isAssignableFrom(cls2)) {
                return str;
            }
            if (cls2.isAssignableFrom(cls)) {
                return str2;
            }
            if (cls.isInterface() || cls2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                cls = cls.getSuperclass();
            } while (!cls.isAssignableFrom(cls2));
            return cls.getName().replace('.', '/');
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int newClass(String str) {
        return a(str).a;
    }

    public int newConst(Object obj) {
        return a(obj).a;
    }

    public int newField(String str, String str2, String str3) {
        return a(str, str2, str3).a;
    }

    public int newMethod(String str, String str2, String str3, boolean z) {
        return a(str, str2, str3, z).a;
    }

    public int newNameType(String str, String str2) {
        return a(str, str2).a;
    }

    public int newUTF8(String str) {
        this.g.a(1, str, null, null);
        Item itemA = a(this.g);
        if (itemA == null) {
            this.d.putByte(1).putUTF8(str);
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.g);
            b(itemA);
        }
        return itemA.a;
    }

    public byte[] toByteArray() {
        int i;
        int iA = (this.n * 2) + 24;
        FieldWriter fieldWriter = this.y;
        int i2 = 0;
        while (fieldWriter != null) {
            iA += fieldWriter.a();
            fieldWriter = fieldWriter.a;
            i2++;
        }
        MethodWriter methodWriter = this.A;
        int i3 = 0;
        while (methodWriter != null) {
            iA += methodWriter.a();
            methodWriter = methodWriter.a;
            i3++;
        }
        if (this.l != 0) {
            i = 1;
            iA += 8;
            newUTF8("Signature");
        } else {
            i = 0;
        }
        if (this.p != 0) {
            i++;
            iA += 8;
            newUTF8("SourceFile");
        }
        if (this.q != null) {
            i++;
            iA += this.q.b + 4;
            newUTF8("SourceDebugExtension");
        }
        if (this.r != 0) {
            i++;
            iA += 10;
            newUTF8("EnclosingMethod");
        }
        if ((this.j & Opcodes.ACC_DEPRECATED) != 0) {
            i++;
            iA += 6;
            newUTF8("Deprecated");
        }
        if ((this.j & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b & 65535) < 49 || (this.j & 262144) != 0)) {
            i++;
            iA += 6;
            newUTF8("Synthetic");
        }
        if (this.x != null) {
            i++;
            iA += this.x.b + 8;
            newUTF8("InnerClasses");
        }
        if (this.t != null) {
            i++;
            iA += this.t.a() + 8;
            newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.u != null) {
            i++;
            iA += this.u.a() + 8;
            newUTF8("RuntimeInvisibleAnnotations");
        }
        int iA2 = iA;
        if (this.v != null) {
            int iA3 = i + this.v.a();
            iA2 += this.v.a(this, null, 0, -1, -1);
            i = iA3;
        }
        ByteVector byteVector = new ByteVector(this.d.b + iA2);
        byteVector.putInt(-889275714).putInt(this.b);
        byteVector.putShort(this.c).putByteArray(this.d.a, 0, this.d.b);
        byteVector.putShort(((393216 | ((this.j & 262144) / 64)) ^ (-1)) & this.j).putShort(this.k).putShort(this.m);
        byteVector.putShort(this.n);
        for (int i4 = 0; i4 < this.n; i4++) {
            byteVector.putShort(this.o[i4]);
        }
        byteVector.putShort(i2);
        for (FieldWriter fieldWriter2 = this.y; fieldWriter2 != null; fieldWriter2 = fieldWriter2.a) {
            fieldWriter2.a(byteVector);
        }
        byteVector.putShort(i3);
        for (MethodWriter methodWriter2 = this.A; methodWriter2 != null; methodWriter2 = methodWriter2.a) {
            methodWriter2.a(byteVector);
        }
        byteVector.putShort(i);
        if (this.l != 0) {
            byteVector.putShort(newUTF8("Signature")).putInt(2).putShort(this.l);
        }
        if (this.p != 0) {
            byteVector.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.p);
        }
        if (this.q != null) {
            int i5 = this.q.b - 2;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i5);
            byteVector.putByteArray(this.q.a, 2, i5);
        }
        if (this.r != 0) {
            byteVector.putShort(newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.r).putShort(this.s);
        }
        if ((this.j & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(newUTF8("Deprecated")).putInt(0);
        }
        if ((this.j & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b & 65535) < 49 || (this.j & 262144) != 0)) {
            byteVector.putShort(newUTF8("Synthetic")).putInt(0);
        }
        if (this.x != null) {
            byteVector.putShort(newUTF8("InnerClasses"));
            byteVector.putInt(this.x.b + 2).putShort(this.w);
            byteVector.putByteArray(this.x.a, 0, this.x.b);
        }
        if (this.t != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            this.t.a(byteVector);
        }
        if (this.u != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            this.u.a(byteVector);
        }
        if (this.v != null) {
            this.v.a(this, null, 0, -1, -1, byteVector);
        }
        if (!this.I) {
            return byteVector.a;
        }
        ClassWriter classWriter = new ClassWriter(2);
        new ClassReader(byteVector.a).accept(classWriter, 4);
        return classWriter.toByteArray();
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        this.b = i;
        this.j = i2;
        this.k = newClass(str);
        this.F = str;
        if (str2 != null) {
            this.l = newUTF8(str2);
        }
        this.m = str3 == null ? 0 : newClass(str3);
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        this.n = strArr.length;
        this.o = new int[this.n];
        for (int i3 = 0; i3 < this.n; i3++) {
            this.o[i3] = newClass(strArr[i3]);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.t;
            this.t = annotationWriter;
        } else {
            annotationWriter.g = this.u;
            this.u = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        attribute.a = this.v;
        this.v = attribute;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i, str, str2, str3, obj);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i) {
        if (this.x == null) {
            this.x = new ByteVector();
        }
        this.w++;
        this.x.putShort(str == null ? 0 : newClass(str));
        this.x.putShort(str2 == null ? 0 : newClass(str2));
        this.x.putShort(str3 != null ? newUTF8(str3) : 0);
        this.x.putShort(i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i, str, str2, str3, strArr, this.H, this.G);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String str, String str2, String str3) {
        this.r = newClass(str);
        if (str2 == null || str3 == null) {
            return;
        }
        this.s = newNameType(str2, str3);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitSource(String str, String str2) {
        if (str != null) {
            this.p = newUTF8(str);
        }
        if (str2 != null) {
            this.q = new ByteVector().putUTF8(str2);
        }
    }
}
