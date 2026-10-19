package org.codehaus.jackson.org.objectweb.asm;

import com.badlogic.gdx.Input;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ClassReader {
    public static final int EXPAND_FRAMES = 8;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    private final int[] a;
    public final byte[] b;
    private final String[] c;
    private final int d;
    public final int header;

    public ClassReader(InputStream inputStream) throws IOException {
        this(a(inputStream));
    }

    public ClassReader(String str) throws IOException {
        this(ClassLoader.getSystemResourceAsStream(new StringBuffer().append(str.replace('.', '/')).append(".class").toString()));
    }

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i, int i2) {
        int unsignedShort;
        this.b = bArr;
        this.a = new int[readUnsignedShort(i + 8)];
        int length = this.a.length;
        this.c = new String[length];
        int i3 = 0;
        int i4 = 1;
        int i5 = i + 10;
        while (i4 < length) {
            this.a[i4] = i5 + 1;
            switch (bArr[i5]) {
                case 1:
                    unsignedShort = readUnsignedShort(i5 + 1) + 3;
                    if (unsignedShort > i3) {
                        i3 = unsignedShort;
                    }
                    break;
                case 2:
                case 7:
                case 8:
                default:
                    unsignedShort = 3;
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    unsignedShort = 5;
                    break;
                case 5:
                case 6:
                    unsignedShort = 9;
                    i4++;
                    break;
            }
            i4++;
            i5 = unsignedShort + i5;
        }
        this.d = i3;
        this.header = i5;
    }

    private int a(int i, char[] cArr, String str, AnnotationVisitor annotationVisitor) {
        int i2 = 0;
        if (annotationVisitor == null) {
            switch (this.b[i] & 255) {
                case 64:
                    return a(i + 3, cArr, true, (AnnotationVisitor) null);
                case 91:
                    return a(i + 1, cArr, false, (AnnotationVisitor) null);
                case 101:
                    return i + 5;
                default:
                    return i + 3;
            }
        }
        int i3 = i + 1;
        switch (this.b[i] & 255) {
            case 64:
                return a(i3 + 2, cArr, true, annotationVisitor.visitAnnotation(str, readUTF8(i3, cArr)));
            case Input.Keys.ENTER /* 66 */:
                annotationVisitor.visit(str, new Byte((byte) readInt(this.a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 67:
                annotationVisitor.visit(str, new Character((char) readInt(this.a[readUnsignedShort(i3)])));
                return i3 + 2;
            case Input.Keys.GRAVE /* 68 */:
            case Input.Keys.EQUALS /* 70 */:
            case Input.Keys.BACKSLASH /* 73 */:
            case Input.Keys.SEMICOLON /* 74 */:
                annotationVisitor.visit(str, readConst(readUnsignedShort(i3), cArr));
                return i3 + 2;
            case 83:
                annotationVisitor.visit(str, new Short((short) readInt(this.a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 90:
                annotationVisitor.visit(str, readInt(this.a[readUnsignedShort(i3)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                return i3 + 2;
            case 91:
                int unsignedShort = readUnsignedShort(i3);
                int i4 = i3 + 2;
                if (unsignedShort == 0) {
                    return a(i4 - 2, cArr, false, annotationVisitor.visitArray(str));
                }
                int i5 = i4 + 1;
                switch (this.b[i4] & 255) {
                    case Input.Keys.ENTER /* 66 */:
                        byte[] bArr = new byte[unsignedShort];
                        while (i2 < unsignedShort) {
                            bArr[i2] = (byte) readInt(this.a[readUnsignedShort(i5)]);
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, bArr);
                        return i5 - 1;
                    case 67:
                        char[] cArr2 = new char[unsignedShort];
                        while (i2 < unsignedShort) {
                            cArr2[i2] = (char) readInt(this.a[readUnsignedShort(i5)]);
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, cArr2);
                        return i5 - 1;
                    case Input.Keys.GRAVE /* 68 */:
                        double[] dArr = new double[unsignedShort];
                        while (i2 < unsignedShort) {
                            dArr[i2] = Double.longBitsToDouble(readLong(this.a[readUnsignedShort(i5)]));
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, dArr);
                        return i5 - 1;
                    case Input.Keys.EQUALS /* 70 */:
                        float[] fArr = new float[unsignedShort];
                        while (i2 < unsignedShort) {
                            fArr[i2] = Float.intBitsToFloat(readInt(this.a[readUnsignedShort(i5)]));
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, fArr);
                        return i5 - 1;
                    case Input.Keys.BACKSLASH /* 73 */:
                        int[] iArr = new int[unsignedShort];
                        while (i2 < unsignedShort) {
                            iArr[i2] = readInt(this.a[readUnsignedShort(i5)]);
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, iArr);
                        return i5 - 1;
                    case Input.Keys.SEMICOLON /* 74 */:
                        long[] jArr = new long[unsignedShort];
                        while (i2 < unsignedShort) {
                            jArr[i2] = readLong(this.a[readUnsignedShort(i5)]);
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, jArr);
                        return i5 - 1;
                    case 83:
                        short[] sArr = new short[unsignedShort];
                        while (i2 < unsignedShort) {
                            sArr[i2] = (short) readInt(this.a[readUnsignedShort(i5)]);
                            i5 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, sArr);
                        return i5 - 1;
                    case 90:
                        boolean[] zArr = new boolean[unsignedShort];
                        int i6 = i5;
                        for (int i7 = 0; i7 < unsignedShort; i7++) {
                            zArr[i7] = readInt(this.a[readUnsignedShort(i6)]) != 0;
                            i6 += 3;
                        }
                        annotationVisitor.visit(str, zArr);
                        return i6 - 1;
                    default:
                        return a(i5 - 3, cArr, false, annotationVisitor.visitArray(str));
                }
            case 99:
                annotationVisitor.visit(str, Type.getType(readUTF8(i3, cArr)));
                return i3 + 2;
            case 101:
                annotationVisitor.visitEnum(str, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                return i3 + 4;
            case Opcodes.DREM /* 115 */:
                annotationVisitor.visit(str, readUTF8(i3, cArr));
                return i3 + 2;
            default:
                return i3;
        }
    }

    private int a(int i, char[] cArr, boolean z, AnnotationVisitor annotationVisitor) {
        int iA;
        int unsignedShort = readUnsignedShort(i);
        int i2 = i + 2;
        if (z) {
            iA = i2;
            int i3 = unsignedShort;
            while (i3 > 0) {
                i3--;
                iA = a(iA + 2, cArr, readUTF8(iA, cArr), annotationVisitor);
            }
        } else {
            iA = i2;
            int i4 = unsignedShort;
            while (i4 > 0) {
                i4--;
                iA = a(iA, cArr, (String) null, annotationVisitor);
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return iA;
    }

    private int a(Object[] objArr, int i, int i2, char[] cArr, Label[] labelArr) {
        int i3 = i2 + 1;
        switch (this.b[i2] & 255) {
            case 0:
                objArr[i] = Opcodes.TOP;
                return i3;
            case 1:
                objArr[i] = Opcodes.INTEGER;
                return i3;
            case 2:
                objArr[i] = Opcodes.FLOAT;
                return i3;
            case 3:
                objArr[i] = Opcodes.DOUBLE;
                return i3;
            case 4:
                objArr[i] = Opcodes.LONG;
                return i3;
            case 5:
                objArr[i] = Opcodes.NULL;
                return i3;
            case 6:
                objArr[i] = Opcodes.UNINITIALIZED_THIS;
                return i3;
            case 7:
                objArr[i] = readClass(i3, cArr);
                return i3 + 2;
            default:
                objArr[i] = readLabel(readUnsignedShort(i3), labelArr);
                return i3 + 2;
        }
    }

    private String a(int i, int i2, char[] cArr) {
        int i3;
        int i4 = i + i2;
        byte[] bArr = this.b;
        char c = 0;
        char c2 = 0;
        int i5 = 0;
        while (i < i4) {
            int i6 = i + 1;
            byte b = bArr[i];
            switch (c2) {
                case 0:
                    int i7 = b & 255;
                    if (i7 < 128) {
                        i3 = i5 + 1;
                        cArr[i5] = (char) i7;
                    } else if (i7 < 224 && i7 > 191) {
                        c = (char) (i7 & 31);
                        c2 = 1;
                        i3 = i5;
                    } else {
                        c = (char) (i7 & 15);
                        c2 = 2;
                        i3 = i5;
                    }
                    break;
                case 1:
                    cArr[i5] = (char) ((b & 63) | (c << 6));
                    i3 = i5 + 1;
                    c2 = 0;
                    break;
                case 2:
                    c = (char) ((c << 6) | (b & 63));
                    c2 = 1;
                    i3 = i5;
                    break;
                default:
                    i3 = i5;
                    break;
            }
            i5 = i3;
            i = i6;
        }
        return new String(cArr, 0, i5);
    }

    private Attribute a(Attribute[] attributeArr, String str, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        for (int i4 = 0; i4 < attributeArr.length; i4++) {
            if (attributeArr[i4].type.equals(str)) {
                return attributeArr[i4].read(this, i, i2, cArr, i3, labelArr);
            }
        }
        return new Attribute(str).read(this, i, i2, null, -1, null);
    }

    private void a(int i, String str, char[] cArr, boolean z, MethodVisitor methodVisitor) {
        int iA = i + 1;
        int i2 = this.b[i] & 255;
        int length = Type.getArgumentTypes(str).length - i2;
        int i3 = 0;
        while (i3 < length) {
            AnnotationVisitor annotationVisitorVisitParameterAnnotation = methodVisitor.visitParameterAnnotation(i3, "Ljava/lang/Synthetic;", false);
            if (annotationVisitorVisitParameterAnnotation != null) {
                annotationVisitorVisitParameterAnnotation.visitEnd();
            }
            i3++;
        }
        while (true) {
            int i4 = i3;
            if (i4 >= i2 + length) {
                return;
            }
            iA += 2;
            for (int unsignedShort = readUnsignedShort(iA); unsignedShort > 0; unsignedShort--) {
                iA = a(iA + 2, cArr, true, methodVisitor.visitParameterAnnotation(i4, readUTF8(iA, cArr), z));
            }
            i3 = i4 + 1;
        }
    }

    private static byte[] a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        byte[] bArr = new byte[inputStream.available()];
        int i = 0;
        while (true) {
            int i2 = inputStream.read(bArr, i, bArr.length - i);
            if (i2 == -1) {
                if (i >= bArr.length) {
                    return bArr;
                }
                byte[] bArr2 = new byte[i];
                System.arraycopy(bArr, 0, bArr2, 0, i);
                return bArr2;
            }
            int i3 = i2 + i;
            if (i3 == bArr.length) {
                int i4 = inputStream.read();
                if (i4 < 0) {
                    return bArr;
                }
                byte[] bArr3 = new byte[bArr.length + 1000];
                System.arraycopy(bArr, 0, bArr3, 0, i3);
                i = i3 + 1;
                bArr3[i3] = (byte) i4;
                bArr = bArr3;
            } else {
                i = i3;
            }
        }
    }

    void a(ClassWriter classWriter) {
        int i;
        char[] cArr = new char[this.d];
        int length = this.a.length;
        Item[] itemArr = new Item[length];
        int i2 = 1;
        while (i2 < length) {
            int i3 = this.a[i2];
            byte b = this.b[i3 - 1];
            Item item = new Item(i2);
            switch (b) {
                case 1:
                    String strA = this.c[i2];
                    if (strA == null) {
                        int i4 = this.a[i2];
                        String[] strArr = this.c;
                        strA = a(i4 + 2, readUnsignedShort(i4), cArr);
                        strArr[i2] = strA;
                    }
                    item.a(b, strA, null, null);
                    i = i2;
                    break;
                case 2:
                case 7:
                case 8:
                default:
                    item.a(b, readUTF8(i3, cArr), null, null);
                    i = i2;
                    break;
                case 3:
                    item.a(readInt(i3));
                    i = i2;
                    break;
                case 4:
                    item.a(Float.intBitsToFloat(readInt(i3)));
                    i = i2;
                    break;
                case 5:
                    item.a(readLong(i3));
                    i = i2 + 1;
                    break;
                case 6:
                    item.a(Double.longBitsToDouble(readLong(i3)));
                    i = i2 + 1;
                    break;
                case 9:
                case 10:
                case 11:
                    int i5 = this.a[readUnsignedShort(i3 + 2)];
                    item.a(b, readClass(i3, cArr), readUTF8(i5, cArr), readUTF8(i5 + 2, cArr));
                    i = i2;
                    break;
                case 12:
                    item.a(b, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr), null);
                    i = i2;
                    break;
            }
            int length2 = item.j % itemArr.length;
            item.k = itemArr[length2];
            itemArr[length2] = item;
            i2 = i + 1;
        }
        int i6 = this.a[1] - 1;
        classWriter.d.putByteArray(this.b, i6, this.header - i6);
        classWriter.e = itemArr;
        classWriter.f = (int) (0.75d * ((double) length));
        classWriter.c = length;
    }

    public void accept(ClassVisitor classVisitor, int i) {
        accept(classVisitor, new Attribute[0], i);
    }

    /* JADX WARN: Code duplicated, block: B:257:0x07c2  */
    /* JADX WARN: Code duplicated, block: B:259:0x07cf  */
    /* JADX WARN: Code duplicated, block: B:261:0x07d3  */
    /* JADX WARN: Code duplicated, block: B:262:0x07d6  */
    /* JADX WARN: Code duplicated, block: B:263:0x07e9  */
    /* JADX WARN: Code duplicated, block: B:264:0x07fc  */
    /* JADX WARN: Code duplicated, block: B:266:0x0806  */
    /* JADX WARN: Code duplicated, block: B:267:0x0809  */
    /* JADX WARN: Code duplicated, block: B:268:0x080c  */
    /* JADX WARN: Code duplicated, block: B:270:0x083c A[LOOP:22: B:269:0x083a->B:270:0x083c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:271:0x0851  */
    /* JADX WARN: Code duplicated, block: B:273:0x0876 A[LOOP:23: B:272:0x0874->B:273:0x0876, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:274:0x088d  */
    /* JADX WARN: Code duplicated, block: B:275:0x0891  */
    /* JADX WARN: Code duplicated, block: B:276:0x0895  */
    /* JADX WARN: Code duplicated, block: B:279:0x08a3  */
    /* JADX WARN: Code duplicated, block: B:281:0x08db  */
    /* JADX WARN: Code duplicated, block: B:283:0x08e4  */
    /* JADX WARN: Code duplicated, block: B:286:0x091b  */
    /* JADX WARN: Code duplicated, block: B:288:0x092d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:289:0x092f  */
    /* JADX WARN: Code duplicated, block: B:291:0x093d  */
    /* JADX WARN: Code duplicated, block: B:293:0x0947  */
    /* JADX WARN: Code duplicated, block: B:296:0x0966  */
    /* JADX WARN: Code duplicated, block: B:298:0x0979  */
    /* JADX WARN: Code duplicated, block: B:300:0x0983  */
    /* JADX WARN: Code duplicated, block: B:302:0x09aa  */
    /* JADX WARN: Code duplicated, block: B:304:0x09b4 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:305:0x09b6  */
    /* JADX WARN: Code duplicated, block: B:307:0x09c2  */
    /* JADX WARN: Code duplicated, block: B:309:0x09cc  */
    /* JADX WARN: Code duplicated, block: B:311:0x09eb  */
    /* JADX WARN: Code duplicated, block: B:313:0x09f5  */
    /* JADX WARN: Code duplicated, block: B:315:0x09f9  */
    /* JADX WARN: Code duplicated, block: B:316:0x0a16  */
    /* JADX WARN: Code duplicated, block: B:318:0x0a20  */
    /* JADX WARN: Code duplicated, block: B:320:0x0a24  */
    /* JADX WARN: Code duplicated, block: B:321:0x0a3d  */
    /* JADX WARN: Code duplicated, block: B:324:0x0a46  */
    /* JADX WARN: Code duplicated, block: B:331:0x0a73  */
    /* JADX WARN: Code duplicated, block: B:333:0x0a7f  */
    /* JADX WARN: Code duplicated, block: B:335:0x0a84  */
    /* JADX WARN: Code duplicated, block: B:337:0x0a8c  */
    /* JADX WARN: Code duplicated, block: B:344:0x0aa4  */
    /* JADX WARN: Code duplicated, block: B:354:0x0acc  */
    /* JADX WARN: Code duplicated, block: B:361:0x0b08 A[LOOP:42: B:359:0x0b00->B:361:0x0b08, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:364:0x0b13  */
    /* JADX WARN: Code duplicated, block: B:367:0x0b1d A[LOOP:43: B:365:0x0b15->B:367:0x0b1d, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:372:0x0b37 A[LOOP:44: B:370:0x0b2f->B:372:0x0b37, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:378:0x0b60  */
    /* JADX WARN: Code duplicated, block: B:380:0x0b66  */
    /* JADX WARN: Code duplicated, block: B:393:0x0b83  */
    /* JADX WARN: Code duplicated, block: B:396:0x0b8b  */
    /* JADX WARN: Code duplicated, block: B:399:0x0b98  */
    /* JADX WARN: Code duplicated, block: B:404:0x0bc4  */
    /* JADX WARN: Code duplicated, block: B:405:0x0bcc  */
    /* JADX WARN: Code duplicated, block: B:407:0x0bd0  */
    /* JADX WARN: Code duplicated, block: B:408:0x0bdf  */
    /* JADX WARN: Code duplicated, block: B:410:0x0bed  */
    /* JADX WARN: Code duplicated, block: B:411:0x0bfe  */
    /* JADX WARN: Code duplicated, block: B:416:0x0c11  */
    /* JADX WARN: Code duplicated, block: B:418:0x0c15  */
    /* JADX WARN: Code duplicated, block: B:419:0x0c1c  */
    /* JADX WARN: Code duplicated, block: B:421:0x0c20 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:422:0x0c22  */
    /* JADX WARN: Code duplicated, block: B:425:0x0c2b A[LOOP:35: B:424:0x0c29->B:425:0x0c2b, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:426:0x0c3a  */
    /* JADX WARN: Code duplicated, block: B:428:0x0c4a  */
    /* JADX WARN: Code duplicated, block: B:430:0x0c59 A[LOOP:36: B:429:0x0c57->B:430:0x0c59, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:433:0x0c75 A[LOOP:37: B:432:0x0c73->B:433:0x0c75, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:437:0x0c92  */
    /* JADX WARN: Code duplicated, block: B:439:0x0cab  */
    /* JADX WARN: Code duplicated, block: B:440:0x0cb1  */
    /* JADX WARN: Code duplicated, block: B:442:0x0cb5  */
    /* JADX WARN: Code duplicated, block: B:444:0x0cc3  */
    /* JADX WARN: Code duplicated, block: B:445:0x0ccf  */
    /* JADX WARN: Code duplicated, block: B:446:0x0ce1  */
    /* JADX WARN: Code duplicated, block: B:447:0x0cf5  */
    /* JADX WARN: Code duplicated, block: B:449:0x0cff  */
    /* JADX WARN: Code duplicated, block: B:450:0x0d15  */
    /* JADX WARN: Code duplicated, block: B:451:0x0d23  */
    /* JADX WARN: Code duplicated, block: B:454:0x0d54 A[LOOP:33: B:452:0x0d4b->B:454:0x0d54, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:456:0x0d6e  */
    /* JADX WARN: Code duplicated, block: B:459:0x0d91 A[LOOP:34: B:457:0x0d8a->B:459:0x0d91, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:461:0x0db5  */
    /* JADX WARN: Code duplicated, block: B:462:0x0dc2  */
    /* JADX WARN: Code duplicated, block: B:463:0x0dcd  */
    /* JADX WARN: Code duplicated, block: B:464:0x0ddc  */
    /* JADX WARN: Code duplicated, block: B:465:0x0def  */
    /* JADX WARN: Code duplicated, block: B:466:0x0e04  */
    /* JADX WARN: Code duplicated, block: B:468:0x0e16  */
    /* JADX WARN: Code duplicated, block: B:471:0x0e2a  */
    /* JADX WARN: Code duplicated, block: B:476:0x0e35  */
    /* JADX WARN: Code duplicated, block: B:477:0x0e39  */
    /* JADX WARN: Code duplicated, block: B:478:0x0e4e  */
    /* JADX WARN: Code duplicated, block: B:480:0x0e56  */
    /* JADX WARN: Code duplicated, block: B:481:0x0e65  */
    /* JADX WARN: Code duplicated, block: B:484:0x0e7c  */
    /* JADX WARN: Code duplicated, block: B:489:0x0e86  */
    /* JADX WARN: Code duplicated, block: B:491:0x0e96 A[LOOP:38: B:490:0x0e94->B:491:0x0e96, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:494:0x0ec3  */
    /* JADX WARN: Code duplicated, block: B:496:0x0edc  */
    /* JADX WARN: Code duplicated, block: B:499:0x0ee0  */
    /* JADX WARN: Code duplicated, block: B:507:0x0f1a A[LOOP:41: B:506:0x0f18->B:507:0x0f1a, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:510:0x0f34  */
    /* JADX WARN: Code duplicated, block: B:514:0x0f45  */
    /* JADX WARN: Code duplicated, block: B:515:0x0f48  */
    /* JADX WARN: Code duplicated, block: B:516:0x0f4c  */
    /* JADX WARN: Code duplicated, block: B:517:0x0f54  */
    /* JADX WARN: Code duplicated, block: B:519:0x0f69  */
    /* JADX WARN: Code duplicated, block: B:523:0x0f7f  */
    /* JADX WARN: Code duplicated, block: B:573:0x06cb A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:621:0x0974 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:624:0x09da A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:628:0x0a9c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:629:0x0ae5 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:630:0x0aee A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:631:0x0af7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:632:0x0b00 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:633:0x0b2f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:634:0x0adc A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:669:0x0c84 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:670:0x0b89 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:682:0x0ef6 A[SYNTHETIC] */
    public void accept(ClassVisitor classVisitor, Attribute[] attributeArr, int i) {
        int i2;
        int i3;
        String[] strArr;
        int unsignedShort;
        int unsignedShort2;
        int i4;
        int i5;
        Label[] labelArr;
        int i6;
        int unsignedShort3;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        boolean z;
        Attribute attribute;
        int unsignedShort4;
        int i14;
        Object[] objArr;
        Object[] objArr2;
        int i15;
        Object[] objArr3;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        Label label;
        int[] iArr;
        int i24;
        int unsignedShort5;
        int unsignedShort6;
        int i25;
        int unsignedShort7;
        int i26;
        int i27;
        Label label2;
        int i28;
        int i29;
        int i30;
        int i31;
        String str;
        String utf8;
        String utf9;
        int[] iArr2;
        Label[] labelArr2;
        int i32;
        Label[] labelArr3;
        int i33;
        int i34;
        int i35;
        int i36;
        int iA;
        int i37;
        int unsignedShort8;
        int iA2;
        int i38;
        int i39;
        int i40;
        int i41;
        int i42;
        int i43;
        int i44;
        int i45;
        int iA3;
        int i46;
        int i47;
        Object[] objArr4;
        int i48;
        int i49;
        int unsignedShort9;
        int i50;
        int i51;
        int i52;
        String utf10;
        int i53;
        Attribute attribute2;
        boolean z2;
        int i54;
        int i55;
        int unsignedShort10;
        int i56;
        int i57;
        Attribute attribute3;
        int unsignedShort11;
        int i58;
        int unsignedShort12;
        int unsignedShort13;
        int i59;
        int unsignedShort14;
        int unsignedShort15;
        Label label3;
        Label label4;
        Label label5;
        int unsignedShort16;
        int i60;
        int i61;
        int i62;
        int i63;
        boolean z3;
        int i64;
        int i65;
        int i66;
        int i67;
        int i68;
        int i69;
        int i70;
        Attribute attribute4;
        int i71;
        int unsignedShort17;
        int i72;
        Attribute attribute5;
        int i73;
        int i74;
        String utf11;
        String utf12;
        String str2;
        String strA;
        String utf13;
        int i75;
        Attribute attribute6;
        int i76;
        int i77;
        byte[] bArr = this.b;
        char[] cArr = new char[this.d];
        int i78 = 0;
        int i79 = 0;
        Attribute attribute7 = null;
        int i80 = this.header;
        int unsignedShort18 = readUnsignedShort(i80);
        String str3 = readClass(i80 + 2, cArr);
        int i81 = this.a[readUnsignedShort(i80 + 4)];
        String utf14 = i81 == 0 ? null : readUTF8(i81, cArr);
        String[] strArr2 = new String[readUnsignedShort(i80 + 6)];
        int i82 = 0;
        int i83 = i80 + 8;
        int i84 = 0;
        while (true) {
            i2 = i83;
            if (i84 >= strArr2.length) {
                break;
            }
            strArr2[i84] = readClass(i2, cArr);
            i83 = i2 + 2;
            i84++;
        }
        boolean z4 = (i & 1) != 0;
        boolean z5 = (i & 2) != 0;
        boolean z6 = (i & 8) != 0;
        int i85 = i2 + 2;
        for (int unsignedShort19 = readUnsignedShort(i2); unsignedShort19 > 0; unsignedShort19--) {
            i85 += 8;
            for (int unsignedShort20 = readUnsignedShort(i85 + 6); unsignedShort20 > 0; unsignedShort20--) {
                i85 += readInt(i85 + 2) + 6;
            }
        }
        int unsignedShort21 = readUnsignedShort(i85);
        int i86 = i85 + 2;
        for (int i87 = unsignedShort21; i87 > 0; i87--) {
            i86 += 8;
            for (int unsignedShort22 = readUnsignedShort(i86 + 6); unsignedShort22 > 0; unsignedShort22--) {
                i86 += readInt(i86 + 2) + 6;
            }
        }
        String utf15 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        int unsignedShort23 = readUnsignedShort(i86);
        int i88 = i86 + 2;
        while (unsignedShort23 > 0) {
            String utf16 = readUTF8(i88, cArr);
            if ("SourceFile".equals(utf16)) {
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = readUTF8(i88 + 6, cArr);
                i75 = i82;
                attribute6 = attribute7;
                utf11 = str8;
                i77 = i78;
                i76 = i79;
            } else if ("InnerClasses".equals(utf16)) {
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = str4;
                i75 = i88 + 6;
                attribute6 = attribute7;
                utf11 = str8;
                i77 = i78;
                i76 = i79;
            } else if ("EnclosingMethod".equals(utf16)) {
                str2 = readClass(i88 + 6, cArr);
                int unsignedShort24 = readUnsignedShort(i88 + 8);
                if (unsignedShort24 != 0) {
                    utf12 = readUTF8(this.a[unsignedShort24], cArr);
                    utf11 = readUTF8(this.a[unsignedShort24] + 2, cArr);
                } else {
                    utf11 = str8;
                    utf12 = str7;
                }
                strA = str5;
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                i76 = i79;
                i77 = i78;
            } else if ("Signature".equals(utf16)) {
                utf15 = readUTF8(i88 + 6, cArr);
                utf11 = str8;
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                i76 = i79;
                i77 = i78;
            } else if ("RuntimeVisibleAnnotations".equals(utf16)) {
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                i77 = i88 + 6;
                utf11 = str8;
                i76 = i79;
            } else if ("Deprecated".equals(utf16)) {
                unsignedShort18 |= Opcodes.ACC_DEPRECATED;
                utf11 = str8;
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                i76 = i79;
                i77 = i78;
            } else if ("Synthetic".equals(utf16)) {
                unsignedShort18 |= 266240;
                utf11 = str8;
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                i76 = i79;
                i77 = i78;
            } else if ("SourceDebugExtension".equals(utf16)) {
                int i89 = readInt(i88 + 2);
                utf12 = str7;
                str2 = str6;
                strA = a(i88 + 6, i89, new char[i89]);
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                utf11 = str8;
                i77 = i78;
                i76 = i79;
            } else if ("RuntimeInvisibleAnnotations".equals(utf16)) {
                utf12 = str7;
                str2 = str6;
                strA = str5;
                utf13 = str4;
                i75 = i82;
                attribute6 = attribute7;
                i77 = i78;
                utf11 = str8;
                i76 = i88 + 6;
            } else {
                Attribute attributeA = a(attributeArr, utf16, i88 + 6, readInt(i88 + 2), cArr, -1, null);
                if (attributeA != null) {
                    attributeA.a = attribute7;
                    utf12 = str7;
                    str2 = str6;
                    strA = str5;
                    utf13 = str4;
                    i75 = i82;
                    attribute6 = attributeA;
                    utf11 = str8;
                    i77 = i78;
                    i76 = i79;
                } else {
                    utf11 = str8;
                    utf12 = str7;
                    str2 = str6;
                    strA = str5;
                    utf13 = str4;
                    i75 = i82;
                    attribute6 = attribute7;
                    i76 = i79;
                    i77 = i78;
                }
            }
            str4 = utf13;
            unsignedShort23--;
            i82 = i75;
            i88 += readInt(i88 + 2) + 6;
            attribute7 = attribute6;
            i79 = i76;
            i78 = i77;
            str5 = strA;
            str8 = utf11;
            str7 = utf12;
            str6 = str2;
        }
        classVisitor.visit(readInt(4), unsignedShort18, str3, utf15, utf14, strArr2);
        if (!z5 && (str4 != null || str5 != null)) {
            classVisitor.visitSource(str4, str5);
        }
        if (str6 != null) {
            classVisitor.visitOuterClass(str6, str7, str8);
        }
        int i90 = 1;
        while (i90 >= 0) {
            int i91 = i90 == 0 ? i79 : i78;
            if (i91 != 0) {
                int iA4 = i91 + 2;
                int unsignedShort25 = readUnsignedShort(i91);
                while (unsignedShort25 > 0) {
                    unsignedShort25--;
                    iA4 = a(iA4 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(iA4, cArr), i90 != 0));
                }
            }
            i90--;
        }
        while (attribute7 != null) {
            Attribute attribute8 = attribute7.a;
            attribute7.a = null;
            classVisitor.visitAttribute(attribute7);
            attribute7 = attribute8;
        }
        if (i82 != 0) {
            int unsignedShort26 = readUnsignedShort(i82);
            int i92 = i82 + 2;
            while (unsignedShort26 > 0) {
                classVisitor.visitInnerClass(readUnsignedShort(i92) == 0 ? null : readClass(i92, cArr), readUnsignedShort(i92 + 2) == 0 ? null : readClass(i92 + 2, cArr), readUnsignedShort(i92 + 4) == 0 ? null : readUTF8(i92 + 4, cArr), readUnsignedShort(i92 + 6));
                unsignedShort26--;
                i92 += 8;
            }
        }
        int i93 = i2 + 2;
        for (int unsignedShort27 = readUnsignedShort(i2); unsignedShort27 > 0; unsignedShort27--) {
            int unsignedShort28 = readUnsignedShort(i93);
            String utf17 = readUTF8(i93 + 2, cArr);
            String utf18 = readUTF8(i93 + 4, cArr);
            int i94 = 0;
            String utf19 = null;
            int i95 = 0;
            int i96 = 0;
            Attribute attribute9 = null;
            int unsignedShort29 = readUnsignedShort(i93 + 6);
            i93 += 8;
            while (unsignedShort29 > 0) {
                String utf20 = readUTF8(i93, cArr);
                if ("ConstantValue".equals(utf20)) {
                    unsignedShort17 = readUnsignedShort(i93 + 6);
                    i72 = unsignedShort28;
                    attribute5 = attribute9;
                    i73 = i96;
                    i74 = i95;
                } else if ("Signature".equals(utf20)) {
                    utf19 = readUTF8(i93 + 6, cArr);
                    unsignedShort17 = i94;
                    i72 = unsignedShort28;
                    attribute5 = attribute9;
                    i73 = i96;
                    i74 = i95;
                } else if ("Deprecated".equals(utf20)) {
                    i72 = 131072 | unsignedShort28;
                    attribute5 = attribute9;
                    i73 = i96;
                    i74 = i95;
                    unsignedShort17 = i94;
                } else if ("Synthetic".equals(utf20)) {
                    i72 = 266240 | unsignedShort28;
                    attribute5 = attribute9;
                    i73 = i96;
                    i74 = i95;
                    unsignedShort17 = i94;
                } else if ("RuntimeVisibleAnnotations".equals(utf20)) {
                    i72 = unsignedShort28;
                    attribute5 = attribute9;
                    i73 = i96;
                    i74 = i93 + 6;
                    unsignedShort17 = i94;
                } else if ("RuntimeInvisibleAnnotations".equals(utf20)) {
                    i72 = unsignedShort28;
                    attribute5 = attribute9;
                    i73 = i93 + 6;
                    i74 = i95;
                    unsignedShort17 = i94;
                } else {
                    Attribute attributeA2 = a(attributeArr, utf20, i93 + 6, readInt(i93 + 2), cArr, -1, null);
                    if (attributeA2 != null) {
                        attributeA2.a = attribute9;
                        i72 = unsignedShort28;
                        attribute5 = attributeA2;
                        i73 = i96;
                        i74 = i95;
                        unsignedShort17 = i94;
                    } else {
                        unsignedShort17 = i94;
                        i72 = unsignedShort28;
                        attribute5 = attribute9;
                        i73 = i96;
                        i74 = i95;
                    }
                }
                i94 = unsignedShort17;
                unsignedShort29--;
                unsignedShort28 = i72;
                i93 += readInt(i93 + 2) + 6;
                attribute9 = attribute5;
                i96 = i73;
                i95 = i74;
            }
            FieldVisitor fieldVisitorVisitField = classVisitor.visitField(unsignedShort28, utf17, utf18, utf19, i94 == 0 ? null : readConst(i94, cArr));
            if (fieldVisitorVisitField != null) {
                int i97 = 1;
                while (i97 >= 0) {
                    int i98 = i97 == 0 ? i96 : i95;
                    if (i98 != 0) {
                        int iA5 = i98 + 2;
                        int unsignedShort30 = readUnsignedShort(i98);
                        while (unsignedShort30 > 0) {
                            unsignedShort30--;
                            iA5 = a(iA5 + 2, cArr, true, fieldVisitorVisitField.visitAnnotation(readUTF8(iA5, cArr), i97 != 0));
                        }
                    }
                    i97--;
                }
                while (attribute9 != null) {
                    Attribute attribute10 = attribute9.a;
                    attribute9.a = null;
                    fieldVisitorVisitField.visitAttribute(attribute9);
                    attribute9 = attribute10;
                }
                fieldVisitorVisitField.visitEnd();
            }
        }
        int i99 = i93 + 2;
        for (int unsignedShort31 = readUnsignedShort(i93); unsignedShort31 > 0; unsignedShort31--) {
            int i100 = i99 + 6;
            int unsignedShort32 = readUnsignedShort(i99);
            String utf21 = readUTF8(i99 + 2, cArr);
            String utf22 = readUTF8(i99 + 4, cArr);
            String utf23 = null;
            int i101 = 0;
            int i102 = 0;
            int i103 = 0;
            int i104 = 0;
            int i105 = 0;
            Attribute attribute11 = null;
            int i106 = 0;
            int i107 = 0;
            int unsignedShort33 = readUnsignedShort(i99 + 6);
            i99 += 8;
            while (unsignedShort33 > 0) {
                String utf24 = readUTF8(i99, cArr);
                int i108 = readInt(i99 + 2);
                int i109 = i99 + 6;
                if ("Code".equals(utf24)) {
                    if (z4) {
                        i71 = i105;
                        i64 = i104;
                        i65 = i103;
                        i66 = i107;
                        i67 = i106;
                        i68 = unsignedShort32;
                        attribute4 = attribute11;
                        i69 = i102;
                        i70 = i101;
                    } else {
                        i71 = i105;
                        i64 = i104;
                        i65 = i103;
                        i66 = i107;
                        i67 = i109;
                        i68 = unsignedShort32;
                        attribute4 = attribute11;
                        i69 = i102;
                        i70 = i101;
                    }
                } else if ("Exceptions".equals(utf24)) {
                    i71 = i105;
                    i64 = i104;
                    i65 = i103;
                    i66 = i109;
                    i67 = i106;
                    i68 = unsignedShort32;
                    attribute4 = attribute11;
                    i69 = i102;
                    i70 = i101;
                } else if ("Signature".equals(utf24)) {
                    utf23 = readUTF8(i109, cArr);
                    i71 = i105;
                    i64 = i104;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    i68 = unsignedShort32;
                    attribute4 = attribute11;
                    i69 = i102;
                    i70 = i101;
                } else if ("Deprecated".equals(utf24)) {
                    i64 = i104;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    i68 = 131072 | unsignedShort32;
                    i69 = i102;
                    i70 = i101;
                    i71 = i105;
                    attribute4 = attribute11;
                } else if ("RuntimeVisibleAnnotations".equals(utf24)) {
                    i71 = i105;
                    i64 = i104;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    i68 = unsignedShort32;
                    attribute4 = attribute11;
                    i69 = i102;
                    i70 = i109;
                } else if ("AnnotationDefault".equals(utf24)) {
                    i71 = i105;
                    i64 = i104;
                    i65 = i109;
                    i66 = i107;
                    i67 = i106;
                    i70 = i101;
                    attribute4 = attribute11;
                    i69 = i102;
                    i68 = unsignedShort32;
                } else if ("Synthetic".equals(utf24)) {
                    i64 = i104;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    i68 = 266240 | unsignedShort32;
                    i69 = i102;
                    i70 = i101;
                    i71 = i105;
                    attribute4 = attribute11;
                } else if ("RuntimeInvisibleAnnotations".equals(utf24)) {
                    i71 = i105;
                    i64 = i104;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    i68 = unsignedShort32;
                    attribute4 = attribute11;
                    i69 = i109;
                    i70 = i101;
                } else if ("RuntimeVisibleParameterAnnotations".equals(utf24)) {
                    i71 = i105;
                    i64 = i109;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    i69 = i102;
                    attribute4 = attribute11;
                    i68 = unsignedShort32;
                    i70 = i101;
                } else if ("RuntimeInvisibleParameterAnnotations".equals(utf24)) {
                    i71 = i109;
                    i64 = i104;
                    i65 = i103;
                    i66 = i107;
                    i67 = i106;
                    attribute4 = attribute11;
                    i68 = unsignedShort32;
                    i69 = i102;
                    i70 = i101;
                } else {
                    Attribute attributeA3 = a(attributeArr, utf24, i109, i108, cArr, -1, null);
                    if (attributeA3 != null) {
                        attributeA3.a = attribute11;
                        i64 = i104;
                        i65 = i103;
                        i66 = i107;
                        i67 = i106;
                        i68 = unsignedShort32;
                        i69 = i102;
                        i70 = i101;
                        int i110 = i105;
                        attribute4 = attributeA3;
                        i71 = i110;
                    } else {
                        i71 = i105;
                        i64 = i104;
                        i65 = i103;
                        i66 = i107;
                        i67 = i106;
                        i68 = unsignedShort32;
                        attribute4 = attribute11;
                        i69 = i102;
                        i70 = i101;
                    }
                }
                unsignedShort33--;
                i106 = i67;
                unsignedShort32 = i68;
                i99 = i108 + i109;
                attribute11 = attribute4;
                i102 = i69;
                i101 = i70;
                i105 = i71;
                i107 = i66;
                i104 = i64;
                i103 = i65;
            }
            if (i107 == 0) {
                i3 = i107;
                strArr = null;
            } else {
                String[] strArr3 = new String[readUnsignedShort(i107)];
                i3 = i107 + 2;
                for (int i111 = 0; i111 < strArr3.length; i111++) {
                    strArr3[i111] = readClass(i3, cArr);
                    i3 += 2;
                }
                strArr = strArr3;
            }
            MethodVisitor methodVisitorVisitMethod = classVisitor.visitMethod(unsignedShort32, utf21, utf22, utf23, strArr);
            if (methodVisitorVisitMethod == null) {
                if (methodVisitorVisitMethod != null && i106 != 0) {
                    unsignedShort = readUnsignedShort(i106);
                    unsignedShort2 = readUnsignedShort(i106 + 2);
                    int i112 = readInt(i106 + 4);
                    i4 = i106 + 8;
                    i5 = i4 + i112;
                    methodVisitorVisitMethod.visitCode();
                    labelArr = new Label[i112 + 2];
                    readLabel(i112 + 1, labelArr);
                    i6 = i4;
                    while (i6 < i5) {
                        i60 = i6 - i4;
                        switch (ClassWriter.a[bArr[i6] & 255]) {
                            case 0:
                            case 4:
                                i61 = i6 + 1;
                                break;
                            case 1:
                            case 3:
                            case 10:
                                i61 = i6 + 2;
                                break;
                            case 2:
                            case 5:
                            case 6:
                            case 11:
                            case 12:
                                i61 = i6 + 3;
                                break;
                            case 7:
                                i61 = i6 + 5;
                                break;
                            case 8:
                                readLabel(readShort(i6 + 1) + i60, labelArr);
                                i61 = i6 + 3;
                                break;
                            case 9:
                                readLabel(readInt(i6 + 1) + i60, labelArr);
                                i61 = i6 + 5;
                                break;
                            case 13:
                                int i113 = (i6 + 4) - (i60 & 3);
                                readLabel(readInt(i113) + i60, labelArr);
                                i61 = i113 + 12;
                                i63 = (readInt(i113 + 8) - readInt(i113 + 4)) + 1;
                                while (i63 > 0) {
                                    readLabel(readInt(i61) + i60, labelArr);
                                    i63--;
                                    i61 += 4;
                                }
                                break;
                            case 14:
                                int i114 = (i6 + 4) - (i60 & 3);
                                readLabel(readInt(i114) + i60, labelArr);
                                i61 = i114 + 8;
                                i62 = readInt(i114 + 4);
                                while (i62 > 0) {
                                    readLabel(readInt(i61 + 4) + i60, labelArr);
                                    i62--;
                                    i61 += 8;
                                }
                                break;
                            case 15:
                            default:
                                i61 = i6 + 4;
                                break;
                            case 16:
                                if ((bArr[i6 + 1] & 255) == 132) {
                                    i61 = i6 + 6;
                                } else {
                                    i61 = i6 + 4;
                                }
                                break;
                        }
                        i6 = i61;
                    }
                    i7 = i6 + 2;
                    for (unsignedShort3 = readUnsignedShort(i6); unsignedShort3 > 0; unsignedShort3--) {
                        label3 = readLabel(readUnsignedShort(i7), labelArr);
                        label4 = readLabel(readUnsignedShort(i7 + 2), labelArr);
                        label5 = readLabel(readUnsignedShort(i7 + 4), labelArr);
                        unsignedShort16 = readUnsignedShort(i7 + 6);
                        if (unsignedShort16 == 0) {
                            methodVisitorVisitMethod.visitTryCatchBlock(label3, label4, label5, null);
                        } else {
                            methodVisitorVisitMethod.visitTryCatchBlock(label3, label4, label5, readUTF8(this.a[unsignedShort16], cArr));
                        }
                        i7 += 8;
                    }
                    i8 = 0;
                    i9 = 0;
                    i10 = 0;
                    i11 = 0;
                    i12 = 0;
                    i13 = 0;
                    z = true;
                    attribute = null;
                    unsignedShort4 = readUnsignedShort(i7);
                    i14 = i7 + 2;
                    while (unsignedShort4 > 0) {
                        utf10 = readUTF8(i14, cArr);
                        if ("LocalVariableTable".equals(utf10)) {
                            if (z5) {
                                z2 = z;
                                unsignedShort10 = i12;
                                i54 = i11;
                                i56 = i10;
                                i55 = i9;
                                i57 = i8;
                            } else {
                                int i115 = i14 + 6;
                                i59 = i14 + 8;
                                for (unsignedShort13 = readUnsignedShort(i14 + 6); unsignedShort13 > 0; unsignedShort13--) {
                                    unsignedShort14 = readUnsignedShort(i59);
                                    if (labelArr[unsignedShort14] == null) {
                                        readLabel(unsignedShort14, labelArr).a |= 1;
                                    }
                                    unsignedShort15 = unsignedShort14 + readUnsignedShort(i59 + 2);
                                    if (labelArr[unsignedShort15] == null) {
                                        readLabel(unsignedShort15, labelArr).a |= 1;
                                    }
                                    i59 += 10;
                                }
                                unsignedShort10 = i12;
                                i54 = i11;
                                i56 = i10;
                                i55 = i9;
                                i57 = i115;
                                z2 = z;
                            }
                        } else if ("LocalVariableTypeTable".equals(utf10)) {
                            unsignedShort10 = i12;
                            i54 = i11;
                            i56 = i10;
                            i55 = i14 + 6;
                            z2 = z;
                            i57 = i8;
                        } else {
                            if ("LineNumberTable".equals(utf10)) {
                                if (!z5) {
                                    i58 = i14 + 8;
                                    for (unsignedShort11 = readUnsignedShort(i14 + 6); unsignedShort11 > 0; unsignedShort11--) {
                                        unsignedShort12 = readUnsignedShort(i58);
                                        if (labelArr[unsignedShort12] == null) {
                                            readLabel(unsignedShort12, labelArr).a |= 1;
                                        }
                                        labelArr[unsignedShort12].b = readUnsignedShort(i58 + 2);
                                        i58 += 4;
                                    }
                                }
                            } else if ("StackMapTable".equals(utf10)) {
                                if ((i & 4) == 0) {
                                    i56 = i14 + 8;
                                    i55 = i9;
                                    i57 = i8;
                                    i54 = readInt(i14 + 2);
                                    unsignedShort10 = readUnsignedShort(i14 + 6);
                                    z2 = z;
                                }
                            } else if ("StackMap".equals(utf10)) {
                                i53 = 0;
                                attribute2 = attribute;
                                while (i53 < attributeArr.length) {
                                    if (attributeArr[i53].type.equals(utf10) || (attribute3 = attributeArr[i53].read(this, i14 + 6, readInt(i14 + 2), cArr, i4 - 8, labelArr)) == null) {
                                        attribute3 = attribute2;
                                    } else {
                                        attribute3.a = attribute2;
                                    }
                                    i53++;
                                    attribute2 = attribute3;
                                }
                                z2 = z;
                                i54 = i11;
                                attribute = attribute2;
                                i55 = i9;
                                unsignedShort10 = i12;
                                i56 = i10;
                                i57 = i8;
                            } else if ((i & 4) == 0) {
                                i56 = i14 + 8;
                                i54 = readInt(i14 + 2);
                                unsignedShort10 = readUnsignedShort(i14 + 6);
                                z2 = false;
                                i55 = i9;
                                i57 = i8;
                            }
                            z2 = z;
                            unsignedShort10 = i12;
                            i54 = i11;
                            i56 = i10;
                            i55 = i9;
                            i57 = i8;
                        }
                        z = z2;
                        i9 = i55;
                        i8 = i57;
                        unsignedShort4--;
                        i14 += readInt(i14 + 2) + 6;
                        i11 = i54;
                        i10 = i56;
                        i12 = unsignedShort10;
                    }
                    if (i10 != 0) {
                        objArr4 = new Object[unsignedShort2];
                        objArr = new Object[unsignedShort];
                        if (z6) {
                            if ((unsignedShort32 & 8) == 0) {
                                i48 = 0;
                            } else if ("<init>".equals(utf21)) {
                                i48 = 1;
                                objArr4[0] = Opcodes.UNINITIALIZED_THIS;
                            } else {
                                i48 = 1;
                                objArr4[0] = readClass(this.header + 2, cArr);
                            }
                            i50 = 1;
                            while (true) {
                                i51 = i50 + 1;
                                switch (utf22.charAt(i50)) {
                                    case Input.Keys.ENTER /* 66 */:
                                    case 'C':
                                    case Input.Keys.BACKSLASH /* 73 */:
                                    case 'S':
                                    case 'Z':
                                        objArr4[i48] = Opcodes.INTEGER;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.GRAVE /* 68 */:
                                        objArr4[i48] = Opcodes.DOUBLE;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.EQUALS /* 70 */:
                                        objArr4[i48] = Opcodes.FLOAT;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.SEMICOLON /* 74 */:
                                        objArr4[i48] = Opcodes.LONG;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.SLASH /* 76 */:
                                        while (utf22.charAt(i51) != ';') {
                                            i51++;
                                        }
                                        int i116 = i50 + 1;
                                        i50 = i51 + 1;
                                        objArr4[i48] = utf22.substring(i116, i51);
                                        i48++;
                                        break;
                                    case '[':
                                        while (utf22.charAt(i51) == '[') {
                                            i51++;
                                        }
                                        if (utf22.charAt(i51) == 'L') {
                                            i52 = i51 + 1;
                                            while (utf22.charAt(i52) != ';') {
                                                i52++;
                                            }
                                            i51 = i52;
                                        }
                                        int i117 = i51 + 1;
                                        objArr4[i48] = utf22.substring(i50, i117);
                                        i48++;
                                        i50 = i117;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else {
                            i48 = 0;
                        }
                        i15 = -1;
                        for (i49 = i10; i49 < (i10 + i11) - 2; i49++) {
                            if (bArr[i49] != 8 && (unsignedShort9 = readUnsignedShort(i49 + 1)) >= 0 && unsignedShort9 < i112 && (bArr[i4 + unsignedShort9] & 255) == 187) {
                                readLabel(unsignedShort9, labelArr);
                            }
                        }
                        i13 = i48;
                        objArr2 = objArr4;
                    } else {
                        objArr = null;
                        objArr2 = null;
                        i15 = 0;
                    }
                    objArr3 = objArr2;
                    i16 = 0;
                    i17 = 0;
                    i18 = i13;
                    i19 = i15;
                    i20 = 0;
                    i21 = i12;
                    i22 = i10;
                    for (i23 = i4; i23 < i5; i23 = i30) {
                        i27 = i23 - i4;
                        label2 = labelArr[i27];
                        if (label2 != null) {
                            methodVisitorVisitMethod.visitLabel(label2);
                            if (!z5 && label2.b > 0) {
                                methodVisitorVisitMethod.visitLineNumber(label2.b, label2);
                            }
                        }
                        i28 = i21;
                        while (objArr3 != null && (i19 == i27 || i19 == -1)) {
                            if (z || z6) {
                                methodVisitorVisitMethod.visitFrame(-1, i18, objArr3, i16, objArr);
                            } else if (i19 != -1) {
                                methodVisitorVisitMethod.visitFrame(i20, i17, objArr3, i16, objArr);
                            }
                            if (i28 > 0) {
                                if (z) {
                                    iA = i22 + 1;
                                    i35 = bArr[i22] & 255;
                                    i36 = i19;
                                } else {
                                    i35 = 255;
                                    i36 = -1;
                                    iA = i22;
                                }
                                i37 = 0;
                                if (i35 < 64) {
                                    i43 = 3;
                                    i42 = 0;
                                    i44 = i35;
                                } else if (i35 < 128) {
                                    i44 = i35 - 64;
                                    iA = a(objArr, 0, iA, cArr, labelArr);
                                    i43 = 4;
                                    i42 = 1;
                                } else {
                                    unsignedShort8 = readUnsignedShort(iA);
                                    iA += 2;
                                    if (i35 == 247) {
                                        iA = a(objArr, 0, iA, cArr, labelArr);
                                        i43 = 4;
                                        i42 = 1;
                                        i44 = unsignedShort8;
                                    } else if (i35 < 248 && i35 < 251) {
                                        int i118 = 251 - i35;
                                        i18 -= i118;
                                        i43 = 2;
                                        i37 = i118;
                                        i42 = 0;
                                        i44 = unsignedShort8;
                                    } else if (i35 == 251) {
                                        i43 = 3;
                                        i42 = 0;
                                        i44 = unsignedShort8;
                                    } else if (i35 < 255) {
                                        if (z6) {
                                            i45 = i18;
                                        } else {
                                            i45 = 0;
                                        }
                                        iA3 = iA;
                                        i46 = i45;
                                        i47 = i35 - 251;
                                        while (i47 > 0) {
                                            iA3 = a(objArr3, i46, iA3, cArr, labelArr);
                                            i47--;
                                            i46++;
                                        }
                                        int i119 = i35 - 251;
                                        i18 += i119;
                                        i43 = 1;
                                        iA = iA3;
                                        i37 = i119;
                                        i42 = 0;
                                        i44 = unsignedShort8;
                                    } else {
                                        int unsignedShort34 = readUnsignedShort(iA);
                                        iA2 = iA + 2;
                                        i38 = 0;
                                        i39 = unsignedShort34;
                                        while (i39 > 0) {
                                            iA2 = a(objArr3, i38, iA2, cArr, labelArr);
                                            i39--;
                                            i38++;
                                        }
                                        int unsignedShort35 = readUnsignedShort(iA2);
                                        iA = iA2 + 2;
                                        i40 = 0;
                                        i41 = unsignedShort35;
                                        while (i41 > 0) {
                                            iA = a(objArr, i40, iA, cArr, labelArr);
                                            i41--;
                                            i40++;
                                        }
                                        i42 = unsignedShort35;
                                        i37 = unsignedShort34;
                                        i43 = 0;
                                        i18 = unsignedShort34;
                                        i44 = unsignedShort8;
                                    }
                                }
                                int i120 = i44 + 1 + i36;
                                readLabel(i120, labelArr);
                                i16 = i42;
                                i17 = i37;
                                i20 = i43;
                                i28--;
                                i19 = i120;
                                i22 = iA;
                            } else {
                                objArr3 = null;
                            }
                        }
                        i29 = bArr[i23] & 255;
                        switch (ClassWriter.a[i29]) {
                            case 0:
                                methodVisitorVisitMethod.visitInsn(i29);
                                i30 = i23 + 1;
                                break;
                            case 1:
                                methodVisitorVisitMethod.visitIntInsn(i29, bArr[i23 + 1]);
                                i30 = i23 + 2;
                                break;
                            case 2:
                                methodVisitorVisitMethod.visitIntInsn(i29, readShort(i23 + 1));
                                i30 = i23 + 3;
                                break;
                            case 3:
                                methodVisitorVisitMethod.visitVarInsn(i29, bArr[i23 + 1] & 255);
                                i30 = i23 + 2;
                                break;
                            case 4:
                                if (i29 > 54) {
                                    int i121 = i29 - 59;
                                    methodVisitorVisitMethod.visitVarInsn((i121 >> 2) + 54, i121 & 3);
                                } else {
                                    int i122 = i29 - 26;
                                    methodVisitorVisitMethod.visitVarInsn((i122 >> 2) + 21, i122 & 3);
                                }
                                i30 = i23 + 1;
                                break;
                            case 5:
                                methodVisitorVisitMethod.visitTypeInsn(i29, readClass(i23 + 1, cArr));
                                i30 = i23 + 3;
                                break;
                            case 6:
                            case 7:
                                i31 = this.a[readUnsignedShort(i23 + 1)];
                                if (i29 == 186) {
                                    str = Opcodes.INVOKEDYNAMIC_OWNER;
                                } else {
                                    str = readClass(i31, cArr);
                                    i31 = this.a[readUnsignedShort(i31 + 2)];
                                }
                                utf8 = readUTF8(i31, cArr);
                                utf9 = readUTF8(i31 + 2, cArr);
                                if (i29 < 182) {
                                    methodVisitorVisitMethod.visitFieldInsn(i29, str, utf8, utf9);
                                } else {
                                    methodVisitorVisitMethod.visitMethodInsn(i29, str, utf8, utf9);
                                }
                                if (i29 == 185 && i29 != 186) {
                                    i30 = i23 + 3;
                                } else {
                                    i30 = i23 + 5;
                                }
                                break;
                            case 8:
                                methodVisitorVisitMethod.visitJumpInsn(i29, labelArr[readShort(i23 + 1) + i27]);
                                i30 = i23 + 3;
                                break;
                            case 9:
                                methodVisitorVisitMethod.visitJumpInsn(i29 - 33, labelArr[readInt(i23 + 1) + i27]);
                                i30 = i23 + 5;
                                break;
                            case 10:
                                methodVisitorVisitMethod.visitLdcInsn(readConst(bArr[i23 + 1] & 255, cArr));
                                i30 = i23 + 2;
                                break;
                            case 11:
                                methodVisitorVisitMethod.visitLdcInsn(readConst(readUnsignedShort(i23 + 1), cArr));
                                i30 = i23 + 3;
                                break;
                            case 12:
                                methodVisitorVisitMethod.visitIincInsn(bArr[i23 + 1] & 255, bArr[i23 + 2]);
                                i30 = i23 + 3;
                                break;
                            case 13:
                                int i123 = (i23 + 4) - (i27 & 3);
                                int i124 = i27 + readInt(i123);
                                int i125 = readInt(i123 + 4);
                                int i126 = readInt(i123 + 8);
                                i30 = i123 + 12;
                                labelArr3 = new Label[(i126 - i125) + 1];
                                for (i33 = 0; i33 < labelArr3.length; i33++) {
                                    labelArr3[i33] = labelArr[readInt(i30) + i27];
                                    i30 += 4;
                                }
                                methodVisitorVisitMethod.visitTableSwitchInsn(i125, i126, labelArr[i124], labelArr3);
                                break;
                            case 14:
                                int i127 = (i23 + 4) - (i27 & 3);
                                int i128 = i27 + readInt(i127);
                                int i129 = readInt(i127 + 4);
                                i30 = i127 + 8;
                                iArr2 = new int[i129];
                                labelArr2 = new Label[i129];
                                for (i32 = 0; i32 < iArr2.length; i32++) {
                                    iArr2[i32] = readInt(i30);
                                    labelArr2[i32] = labelArr[readInt(i30 + 4) + i27];
                                    i30 += 8;
                                }
                                methodVisitorVisitMethod.visitLookupSwitchInsn(labelArr[i128], iArr2, labelArr2);
                                break;
                            case 15:
                            default:
                                methodVisitorVisitMethod.visitMultiANewArrayInsn(readClass(i23 + 1, cArr), bArr[i23 + 3] & 255);
                                i30 = i23 + 4;
                                break;
                            case 16:
                                i34 = bArr[i23 + 1] & 255;
                                if (i34 == 132) {
                                    methodVisitorVisitMethod.visitIincInsn(readUnsignedShort(i23 + 2), readShort(i23 + 4));
                                    i30 = i23 + 6;
                                } else {
                                    methodVisitorVisitMethod.visitVarInsn(i34, readUnsignedShort(i23 + 2));
                                    i30 = i23 + 4;
                                }
                                break;
                        }
                        i21 = i28;
                    }
                    label = labelArr[i5 - i4];
                    if (label != null) {
                        methodVisitorVisitMethod.visitLabel(label);
                    }
                    if (!z5 && i8 != 0) {
                        iArr = null;
                        if (i9 != 0) {
                            unsignedShort7 = readUnsignedShort(i9) * 3;
                            i26 = i9 + 2;
                            iArr = new int[unsignedShort7];
                            while (unsignedShort7 > 0) {
                                int i130 = unsignedShort7 - 1;
                                iArr[i130] = i26 + 6;
                                int i131 = i130 - 1;
                                iArr[i131] = readUnsignedShort(i26 + 8);
                                unsignedShort7 = i131 - 1;
                                iArr[unsignedShort7] = readUnsignedShort(i26);
                                i26 += 10;
                            }
                        }
                        i24 = i8 + 2;
                        for (unsignedShort5 = readUnsignedShort(i8); unsignedShort5 > 0; unsignedShort5--) {
                            unsignedShort6 = readUnsignedShort(i24);
                            int unsignedShort36 = readUnsignedShort(i24 + 2);
                            int unsignedShort37 = readUnsignedShort(i24 + 8);
                            String utf25 = null;
                            if (iArr != null) {
                                for (i25 = 0; i25 < iArr.length; i25 += 3) {
                                    if (iArr[i25] != unsignedShort6 && iArr[i25 + 1] == unsignedShort37) {
                                        utf25 = readUTF8(iArr[i25 + 2], cArr);
                                        break;
                                    }
                                }
                            }
                            methodVisitorVisitMethod.visitLocalVariable(readUTF8(i24 + 4, cArr), readUTF8(i24 + 6, cArr), utf25, labelArr[unsignedShort6], labelArr[unsignedShort6 + unsignedShort36], unsignedShort37);
                            i24 += 10;
                        }
                    }
                    while (attribute != null) {
                        Attribute attribute12 = attribute.a;
                        attribute.a = null;
                        methodVisitorVisitMethod.visitAttribute(attribute);
                        attribute = attribute12;
                    }
                    methodVisitorVisitMethod.visitMaxs(unsignedShort, unsignedShort2);
                }
                if (methodVisitorVisitMethod != null) {
                    methodVisitorVisitMethod.visitEnd();
                }
            } else {
                if (methodVisitorVisitMethod instanceof MethodWriter) {
                    MethodWriter methodWriter = (MethodWriter) methodVisitorVisitMethod;
                    if (methodWriter.b.J == this && utf23 == methodWriter.g) {
                        boolean z7 = false;
                        if (strArr == null) {
                            z3 = methodWriter.j == 0;
                        } else if (strArr.length == methodWriter.j) {
                            z7 = true;
                            int i132 = i3;
                            int length = strArr.length - 1;
                            while (true) {
                                if (length >= 0) {
                                    i132 -= 2;
                                    if (methodWriter.k[length] != readUnsignedShort(i132)) {
                                        z3 = false;
                                    } else {
                                        length--;
                                    }
                                } else {
                                    z3 = z7;
                                }
                            }
                        } else {
                            z3 = z7;
                        }
                        if (z3) {
                            methodWriter.h = i100;
                            methodWriter.i = i99 - i100;
                        }
                    }
                }
                if (i103 != 0) {
                    AnnotationVisitor annotationVisitorVisitAnnotationDefault = methodVisitorVisitMethod.visitAnnotationDefault();
                    a(i103, cArr, (String) null, annotationVisitorVisitAnnotationDefault);
                    if (annotationVisitorVisitAnnotationDefault != null) {
                        annotationVisitorVisitAnnotationDefault.visitEnd();
                    }
                }
                int i133 = 1;
                while (i133 >= 0) {
                    int i134 = i133 == 0 ? i102 : i101;
                    if (i134 != 0) {
                        int iA6 = i134 + 2;
                        int unsignedShort38 = readUnsignedShort(i134);
                        while (unsignedShort38 > 0) {
                            unsignedShort38--;
                            iA6 = a(iA6 + 2, cArr, true, methodVisitorVisitMethod.visitAnnotation(readUTF8(iA6, cArr), i133 != 0));
                        }
                    }
                    i133--;
                }
                if (i104 != 0) {
                    a(i104, utf22, cArr, true, methodVisitorVisitMethod);
                }
                if (i105 != 0) {
                    a(i105, utf22, cArr, false, methodVisitorVisitMethod);
                }
                while (attribute11 != null) {
                    Attribute attribute13 = attribute11.a;
                    attribute11.a = null;
                    methodVisitorVisitMethod.visitAttribute(attribute11);
                    attribute11 = attribute13;
                }
                if (methodVisitorVisitMethod != null) {
                    unsignedShort = readUnsignedShort(i106);
                    unsignedShort2 = readUnsignedShort(i106 + 2);
                    int i1110 = readInt(i106 + 4);
                    i4 = i106 + 8;
                    i5 = i4 + i1110;
                    methodVisitorVisitMethod.visitCode();
                    labelArr = new Label[i1110 + 2];
                    readLabel(i1110 + 1, labelArr);
                    i6 = i4;
                    while (i6 < i5) {
                        i60 = i6 - i4;
                        switch (ClassWriter.a[bArr[i6] & 255]) {
                            case 0:
                            case 4:
                                i61 = i6 + 1;
                                break;
                            case 1:
                            case 3:
                            case 10:
                                i61 = i6 + 2;
                                break;
                            case 2:
                            case 5:
                            case 6:
                            case 11:
                            case 12:
                                i61 = i6 + 3;
                                break;
                            case 7:
                                i61 = i6 + 5;
                                break;
                            case 8:
                                readLabel(readShort(i6 + 1) + i60, labelArr);
                                i61 = i6 + 3;
                                break;
                            case 9:
                                readLabel(readInt(i6 + 1) + i60, labelArr);
                                i61 = i6 + 5;
                                break;
                            case 13:
                                int i1111 = (i6 + 4) - (i60 & 3);
                                readLabel(readInt(i1111) + i60, labelArr);
                                i61 = i1111 + 12;
                                i63 = (readInt(i1111 + 8) - readInt(i1111 + 4)) + 1;
                                while (i63 > 0) {
                                    readLabel(readInt(i61) + i60, labelArr);
                                    i63--;
                                    i61 += 4;
                                }
                                break;
                            case 14:
                                int i1112 = (i6 + 4) - (i60 & 3);
                                readLabel(readInt(i1112) + i60, labelArr);
                                i61 = i1112 + 8;
                                i62 = readInt(i1112 + 4);
                                while (i62 > 0) {
                                    readLabel(readInt(i61 + 4) + i60, labelArr);
                                    i62--;
                                    i61 += 8;
                                }
                                break;
                            case 15:
                            default:
                                i61 = i6 + 4;
                                break;
                            case 16:
                                if ((bArr[i6 + 1] & 255) == 132) {
                                    i61 = i6 + 6;
                                } else {
                                    i61 = i6 + 4;
                                }
                                break;
                        }
                        i6 = i61;
                    }
                    i7 = i6 + 2;
                    while (unsignedShort3 > 0) {
                        label3 = readLabel(readUnsignedShort(i7), labelArr);
                        label4 = readLabel(readUnsignedShort(i7 + 2), labelArr);
                        label5 = readLabel(readUnsignedShort(i7 + 4), labelArr);
                        unsignedShort16 = readUnsignedShort(i7 + 6);
                        if (unsignedShort16 == 0) {
                            methodVisitorVisitMethod.visitTryCatchBlock(label3, label4, label5, null);
                        } else {
                            methodVisitorVisitMethod.visitTryCatchBlock(label3, label4, label5, readUTF8(this.a[unsignedShort16], cArr));
                        }
                        i7 += 8;
                    }
                    i8 = 0;
                    i9 = 0;
                    i10 = 0;
                    i11 = 0;
                    i12 = 0;
                    i13 = 0;
                    z = true;
                    attribute = null;
                    unsignedShort4 = readUnsignedShort(i7);
                    i14 = i7 + 2;
                    while (unsignedShort4 > 0) {
                        utf10 = readUTF8(i14, cArr);
                        if ("LocalVariableTable".equals(utf10)) {
                            if (z5) {
                                int i1113 = i14 + 6;
                                i59 = i14 + 8;
                                while (unsignedShort13 > 0) {
                                    unsignedShort14 = readUnsignedShort(i59);
                                    if (labelArr[unsignedShort14] == null) {
                                        readLabel(unsignedShort14, labelArr).a |= 1;
                                    }
                                    unsignedShort15 = unsignedShort14 + readUnsignedShort(i59 + 2);
                                    if (labelArr[unsignedShort15] == null) {
                                        readLabel(unsignedShort15, labelArr).a |= 1;
                                    }
                                    i59 += 10;
                                }
                                unsignedShort10 = i12;
                                i54 = i11;
                                i56 = i10;
                                i55 = i9;
                                i57 = i1113;
                                z2 = z;
                            } else {
                                z2 = z;
                                unsignedShort10 = i12;
                                i54 = i11;
                                i56 = i10;
                                i55 = i9;
                                i57 = i8;
                            }
                        } else if ("LocalVariableTypeTable".equals(utf10)) {
                            unsignedShort10 = i12;
                            i54 = i11;
                            i56 = i10;
                            i55 = i14 + 6;
                            z2 = z;
                            i57 = i8;
                        } else {
                            if ("LineNumberTable".equals(utf10)) {
                                if (!z5) {
                                    i58 = i14 + 8;
                                    while (unsignedShort11 > 0) {
                                        unsignedShort12 = readUnsignedShort(i58);
                                        if (labelArr[unsignedShort12] == null) {
                                            readLabel(unsignedShort12, labelArr).a |= 1;
                                        }
                                        labelArr[unsignedShort12].b = readUnsignedShort(i58 + 2);
                                        i58 += 4;
                                    }
                                }
                            } else if ("StackMapTable".equals(utf10)) {
                                if ((i & 4) == 0) {
                                    i56 = i14 + 8;
                                    i55 = i9;
                                    i57 = i8;
                                    i54 = readInt(i14 + 2);
                                    unsignedShort10 = readUnsignedShort(i14 + 6);
                                    z2 = z;
                                }
                            } else if ("StackMap".equals(utf10)) {
                                i53 = 0;
                                attribute2 = attribute;
                                while (i53 < attributeArr.length) {
                                    if (attributeArr[i53].type.equals(utf10)) {
                                        attribute3 = attribute2;
                                    } else {
                                        attribute3 = attribute2;
                                    }
                                    i53++;
                                    attribute2 = attribute3;
                                }
                                z2 = z;
                                i54 = i11;
                                attribute = attribute2;
                                i55 = i9;
                                unsignedShort10 = i12;
                                i56 = i10;
                                i57 = i8;
                            } else if ((i & 4) == 0) {
                                i56 = i14 + 8;
                                i54 = readInt(i14 + 2);
                                unsignedShort10 = readUnsignedShort(i14 + 6);
                                z2 = false;
                                i55 = i9;
                                i57 = i8;
                            }
                            z2 = z;
                            unsignedShort10 = i12;
                            i54 = i11;
                            i56 = i10;
                            i55 = i9;
                            i57 = i8;
                        }
                        z = z2;
                        i9 = i55;
                        i8 = i57;
                        unsignedShort4--;
                        i14 += readInt(i14 + 2) + 6;
                        i11 = i54;
                        i10 = i56;
                        i12 = unsignedShort10;
                    }
                    if (i10 != 0) {
                        objArr4 = new Object[unsignedShort2];
                        objArr = new Object[unsignedShort];
                        if (z6) {
                            if ((unsignedShort32 & 8) == 0) {
                                i48 = 0;
                            } else if ("<init>".equals(utf21)) {
                                i48 = 1;
                                objArr4[0] = Opcodes.UNINITIALIZED_THIS;
                            } else {
                                i48 = 1;
                                objArr4[0] = readClass(this.header + 2, cArr);
                            }
                            i50 = 1;
                            while (true) {
                                i51 = i50 + 1;
                                switch (utf22.charAt(i50)) {
                                    case Input.Keys.ENTER /* 66 */:
                                    case 'C':
                                    case Input.Keys.BACKSLASH /* 73 */:
                                    case 'S':
                                    case 'Z':
                                        objArr4[i48] = Opcodes.INTEGER;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.GRAVE /* 68 */:
                                        objArr4[i48] = Opcodes.DOUBLE;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.EQUALS /* 70 */:
                                        objArr4[i48] = Opcodes.FLOAT;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.SEMICOLON /* 74 */:
                                        objArr4[i48] = Opcodes.LONG;
                                        i48++;
                                        i50 = i51;
                                        break;
                                    case Input.Keys.SLASH /* 76 */:
                                        while (utf22.charAt(i51) != ';') {
                                            i51++;
                                        }
                                        int i1114 = i50 + 1;
                                        i50 = i51 + 1;
                                        objArr4[i48] = utf22.substring(i1114, i51);
                                        i48++;
                                        break;
                                    case '[':
                                        while (utf22.charAt(i51) == '[') {
                                            i51++;
                                        }
                                        if (utf22.charAt(i51) == 'L') {
                                            i52 = i51 + 1;
                                            while (utf22.charAt(i52) != ';') {
                                                i52++;
                                            }
                                            i51 = i52;
                                        }
                                        int i1115 = i51 + 1;
                                        objArr4[i48] = utf22.substring(i50, i1115);
                                        i48++;
                                        i50 = i1115;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else {
                            i48 = 0;
                        }
                        i15 = -1;
                        while (i49 < (i10 + i11) - 2) {
                            if (bArr[i49] != 8) {
                            }
                        }
                        i13 = i48;
                        objArr2 = objArr4;
                    } else {
                        objArr = null;
                        objArr2 = null;
                        i15 = 0;
                    }
                    objArr3 = objArr2;
                    i16 = 0;
                    i17 = 0;
                    i18 = i13;
                    i19 = i15;
                    i20 = 0;
                    i21 = i12;
                    i22 = i10;
                    while (i23 < i5) {
                        i27 = i23 - i4;
                        label2 = labelArr[i27];
                        if (label2 != null) {
                            methodVisitorVisitMethod.visitLabel(label2);
                            if (!z5) {
                                methodVisitorVisitMethod.visitLineNumber(label2.b, label2);
                            }
                        }
                        i28 = i21;
                        while (objArr3 != null) {
                            if (z) {
                                methodVisitorVisitMethod.visitFrame(-1, i18, objArr3, i16, objArr);
                            } else {
                                methodVisitorVisitMethod.visitFrame(-1, i18, objArr3, i16, objArr);
                            }
                            if (i28 > 0) {
                                if (z) {
                                    iA = i22 + 1;
                                    i35 = bArr[i22] & 255;
                                    i36 = i19;
                                } else {
                                    i35 = 255;
                                    i36 = -1;
                                    iA = i22;
                                }
                                i37 = 0;
                                if (i35 < 64) {
                                    i43 = 3;
                                    i42 = 0;
                                    i44 = i35;
                                } else if (i35 < 128) {
                                    i44 = i35 - 64;
                                    iA = a(objArr, 0, iA, cArr, labelArr);
                                    i43 = 4;
                                    i42 = 1;
                                } else {
                                    unsignedShort8 = readUnsignedShort(iA);
                                    iA += 2;
                                    if (i35 == 247) {
                                        iA = a(objArr, 0, iA, cArr, labelArr);
                                        i43 = 4;
                                        i42 = 1;
                                        i44 = unsignedShort8;
                                    } else if (i35 < 248) {
                                        if (i35 == 251) {
                                            i43 = 3;
                                            i42 = 0;
                                            i44 = unsignedShort8;
                                        } else if (i35 < 255) {
                                            if (z6) {
                                                i45 = i18;
                                            } else {
                                                i45 = 0;
                                            }
                                            iA3 = iA;
                                            i46 = i45;
                                            i47 = i35 - 251;
                                            while (i47 > 0) {
                                                iA3 = a(objArr3, i46, iA3, cArr, labelArr);
                                                i47--;
                                                i46++;
                                            }
                                            int i1116 = i35 - 251;
                                            i18 += i1116;
                                            i43 = 1;
                                            iA = iA3;
                                            i37 = i1116;
                                            i42 = 0;
                                            i44 = unsignedShort8;
                                        } else {
                                            int unsignedShort39 = readUnsignedShort(iA);
                                            iA2 = iA + 2;
                                            i38 = 0;
                                            i39 = unsignedShort39;
                                            while (i39 > 0) {
                                                iA2 = a(objArr3, i38, iA2, cArr, labelArr);
                                                i39--;
                                                i38++;
                                            }
                                            int unsignedShort310 = readUnsignedShort(iA2);
                                            iA = iA2 + 2;
                                            i40 = 0;
                                            i41 = unsignedShort310;
                                            while (i41 > 0) {
                                                iA = a(objArr, i40, iA, cArr, labelArr);
                                                i41--;
                                                i40++;
                                            }
                                            i42 = unsignedShort310;
                                            i37 = unsignedShort39;
                                            i43 = 0;
                                            i18 = unsignedShort39;
                                            i44 = unsignedShort8;
                                        }
                                    } else if (i35 == 251) {
                                        i43 = 3;
                                        i42 = 0;
                                        i44 = unsignedShort8;
                                    } else if (i35 < 255) {
                                        if (z6) {
                                            i45 = i18;
                                        } else {
                                            i45 = 0;
                                        }
                                        iA3 = iA;
                                        i46 = i45;
                                        i47 = i35 - 251;
                                        while (i47 > 0) {
                                            iA3 = a(objArr3, i46, iA3, cArr, labelArr);
                                            i47--;
                                            i46++;
                                        }
                                        int i1117 = i35 - 251;
                                        i18 += i1117;
                                        i43 = 1;
                                        iA = iA3;
                                        i37 = i1117;
                                        i42 = 0;
                                        i44 = unsignedShort8;
                                    } else {
                                        int unsignedShort311 = readUnsignedShort(iA);
                                        iA2 = iA + 2;
                                        i38 = 0;
                                        i39 = unsignedShort311;
                                        while (i39 > 0) {
                                            iA2 = a(objArr3, i38, iA2, cArr, labelArr);
                                            i39--;
                                            i38++;
                                        }
                                        int unsignedShort312 = readUnsignedShort(iA2);
                                        iA = iA2 + 2;
                                        i40 = 0;
                                        i41 = unsignedShort312;
                                        while (i41 > 0) {
                                            iA = a(objArr, i40, iA, cArr, labelArr);
                                            i41--;
                                            i40++;
                                        }
                                        i42 = unsignedShort312;
                                        i37 = unsignedShort311;
                                        i43 = 0;
                                        i18 = unsignedShort311;
                                        i44 = unsignedShort8;
                                    }
                                }
                                int i1210 = i44 + 1 + i36;
                                readLabel(i1210, labelArr);
                                i16 = i42;
                                i17 = i37;
                                i20 = i43;
                                i28--;
                                i19 = i1210;
                                i22 = iA;
                            } else {
                                objArr3 = null;
                            }
                        }
                        i29 = bArr[i23] & 255;
                        switch (ClassWriter.a[i29]) {
                            case 0:
                                methodVisitorVisitMethod.visitInsn(i29);
                                i30 = i23 + 1;
                                break;
                            case 1:
                                methodVisitorVisitMethod.visitIntInsn(i29, bArr[i23 + 1]);
                                i30 = i23 + 2;
                                break;
                            case 2:
                                methodVisitorVisitMethod.visitIntInsn(i29, readShort(i23 + 1));
                                i30 = i23 + 3;
                                break;
                            case 3:
                                methodVisitorVisitMethod.visitVarInsn(i29, bArr[i23 + 1] & 255);
                                i30 = i23 + 2;
                                break;
                            case 4:
                                if (i29 > 54) {
                                    int i1211 = i29 - 59;
                                    methodVisitorVisitMethod.visitVarInsn((i1211 >> 2) + 54, i1211 & 3);
                                } else {
                                    int i1212 = i29 - 26;
                                    methodVisitorVisitMethod.visitVarInsn((i1212 >> 2) + 21, i1212 & 3);
                                }
                                i30 = i23 + 1;
                                break;
                            case 5:
                                methodVisitorVisitMethod.visitTypeInsn(i29, readClass(i23 + 1, cArr));
                                i30 = i23 + 3;
                                break;
                            case 6:
                            case 7:
                                i31 = this.a[readUnsignedShort(i23 + 1)];
                                if (i29 == 186) {
                                    str = Opcodes.INVOKEDYNAMIC_OWNER;
                                } else {
                                    str = readClass(i31, cArr);
                                    i31 = this.a[readUnsignedShort(i31 + 2)];
                                }
                                utf8 = readUTF8(i31, cArr);
                                utf9 = readUTF8(i31 + 2, cArr);
                                if (i29 < 182) {
                                    methodVisitorVisitMethod.visitFieldInsn(i29, str, utf8, utf9);
                                } else {
                                    methodVisitorVisitMethod.visitMethodInsn(i29, str, utf8, utf9);
                                }
                                if (i29 == 185) {
                                    i30 = i23 + 5;
                                } else {
                                    i30 = i23 + 5;
                                }
                                break;
                            case 8:
                                methodVisitorVisitMethod.visitJumpInsn(i29, labelArr[readShort(i23 + 1) + i27]);
                                i30 = i23 + 3;
                                break;
                            case 9:
                                methodVisitorVisitMethod.visitJumpInsn(i29 - 33, labelArr[readInt(i23 + 1) + i27]);
                                i30 = i23 + 5;
                                break;
                            case 10:
                                methodVisitorVisitMethod.visitLdcInsn(readConst(bArr[i23 + 1] & 255, cArr));
                                i30 = i23 + 2;
                                break;
                            case 11:
                                methodVisitorVisitMethod.visitLdcInsn(readConst(readUnsignedShort(i23 + 1), cArr));
                                i30 = i23 + 3;
                                break;
                            case 12:
                                methodVisitorVisitMethod.visitIincInsn(bArr[i23 + 1] & 255, bArr[i23 + 2]);
                                i30 = i23 + 3;
                                break;
                            case 13:
                                int i1213 = (i23 + 4) - (i27 & 3);
                                int i1214 = i27 + readInt(i1213);
                                int i1215 = readInt(i1213 + 4);
                                int i1216 = readInt(i1213 + 8);
                                i30 = i1213 + 12;
                                labelArr3 = new Label[(i1216 - i1215) + 1];
                                while (i33 < labelArr3.length) {
                                    labelArr3[i33] = labelArr[readInt(i30) + i27];
                                    i30 += 4;
                                }
                                methodVisitorVisitMethod.visitTableSwitchInsn(i1215, i1216, labelArr[i1214], labelArr3);
                                break;
                            case 14:
                                int i1217 = (i23 + 4) - (i27 & 3);
                                int i1218 = i27 + readInt(i1217);
                                int i1219 = readInt(i1217 + 4);
                                i30 = i1217 + 8;
                                iArr2 = new int[i1219];
                                labelArr2 = new Label[i1219];
                                while (i32 < iArr2.length) {
                                    iArr2[i32] = readInt(i30);
                                    labelArr2[i32] = labelArr[readInt(i30 + 4) + i27];
                                    i30 += 8;
                                }
                                methodVisitorVisitMethod.visitLookupSwitchInsn(labelArr[i1218], iArr2, labelArr2);
                                break;
                            case 15:
                            default:
                                methodVisitorVisitMethod.visitMultiANewArrayInsn(readClass(i23 + 1, cArr), bArr[i23 + 3] & 255);
                                i30 = i23 + 4;
                                break;
                            case 16:
                                i34 = bArr[i23 + 1] & 255;
                                if (i34 == 132) {
                                    methodVisitorVisitMethod.visitIincInsn(readUnsignedShort(i23 + 2), readShort(i23 + 4));
                                    i30 = i23 + 6;
                                } else {
                                    methodVisitorVisitMethod.visitVarInsn(i34, readUnsignedShort(i23 + 2));
                                    i30 = i23 + 4;
                                }
                                break;
                        }
                        i21 = i28;
                    }
                    label = labelArr[i5 - i4];
                    if (label != null) {
                        methodVisitorVisitMethod.visitLabel(label);
                    }
                    if (!z5) {
                        iArr = null;
                        if (i9 != 0) {
                            unsignedShort7 = readUnsignedShort(i9) * 3;
                            i26 = i9 + 2;
                            iArr = new int[unsignedShort7];
                            while (unsignedShort7 > 0) {
                                int i135 = unsignedShort7 - 1;
                                iArr[i135] = i26 + 6;
                                int i136 = i135 - 1;
                                iArr[i136] = readUnsignedShort(i26 + 8);
                                unsignedShort7 = i136 - 1;
                                iArr[unsignedShort7] = readUnsignedShort(i26);
                                i26 += 10;
                            }
                        }
                        i24 = i8 + 2;
                        while (unsignedShort5 > 0) {
                            unsignedShort6 = readUnsignedShort(i24);
                            int unsignedShort313 = readUnsignedShort(i24 + 2);
                            int unsignedShort314 = readUnsignedShort(i24 + 8);
                            String utf26 = null;
                            if (iArr != null) {
                                while (i25 < iArr.length) {
                                    if (iArr[i25] != unsignedShort6) {
                                    }
                                }
                            }
                            methodVisitorVisitMethod.visitLocalVariable(readUTF8(i24 + 4, cArr), readUTF8(i24 + 6, cArr), utf26, labelArr[unsignedShort6], labelArr[unsignedShort6 + unsignedShort313], unsignedShort314);
                            i24 += 10;
                        }
                    }
                    while (attribute != null) {
                        Attribute attribute14 = attribute.a;
                        attribute.a = null;
                        methodVisitorVisitMethod.visitAttribute(attribute);
                        attribute = attribute14;
                    }
                    methodVisitorVisitMethod.visitMaxs(unsignedShort, unsignedShort2);
                }
                if (methodVisitorVisitMethod != null) {
                    methodVisitorVisitMethod.visitEnd();
                }
            }
        }
        classVisitor.visitEnd();
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.d]);
    }

    public String[] getInterfaces() {
        int i = this.header + 6;
        int unsignedShort = readUnsignedShort(i);
        String[] strArr = new String[unsignedShort];
        if (unsignedShort > 0) {
            char[] cArr = new char[this.d];
            for (int i2 = 0; i2 < unsignedShort; i2++) {
                i += 2;
                strArr[i2] = readClass(i, cArr);
            }
        }
        return strArr;
    }

    public int getItem(int i) {
        return this.a[i];
    }

    public String getSuperName() {
        int i = this.a[readUnsignedShort(this.header + 4)];
        if (i == 0) {
            return null;
        }
        return readUTF8(i, new char[this.d]);
    }

    public int readByte(int i) {
        return this.b[i] & 255;
    }

    public String readClass(int i, char[] cArr) {
        return readUTF8(this.a[readUnsignedShort(i)], cArr);
    }

    public Object readConst(int i, char[] cArr) {
        int i2 = this.a[i];
        switch (this.b[i2 - 1]) {
            case 3:
                return new Integer(readInt(i2));
            case 4:
                return new Float(Float.intBitsToFloat(readInt(i2)));
            case 5:
                return new Long(readLong(i2));
            case 6:
                return new Double(Double.longBitsToDouble(readLong(i2)));
            case 7:
                return Type.getObjectType(readUTF8(i2, cArr));
            default:
                return readUTF8(i2, cArr);
        }
    }

    public int readInt(int i) {
        byte[] bArr = this.b;
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }

    protected Label readLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            labelArr[i] = new Label();
        }
        return labelArr[i];
    }

    public long readLong(int i) {
        return (((long) readInt(i)) << 32) | (((long) readInt(i + 4)) & 4294967295L);
    }

    public short readShort(int i) {
        byte[] bArr = this.b;
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    public String readUTF8(int i, char[] cArr) {
        int unsignedShort = readUnsignedShort(i);
        String str = this.c[unsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.a[unsignedShort];
        String[] strArr = this.c;
        String strA = a(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[unsignedShort] = strA;
        return strA;
    }

    public int readUnsignedShort(int i) {
        byte[] bArr = this.b;
        return (bArr[i + 1] & 255) | ((bArr[i] & 255) << 8);
    }
}
