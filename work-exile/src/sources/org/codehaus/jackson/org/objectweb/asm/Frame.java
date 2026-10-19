package org.codehaus.jackson.org.objectweb.asm;

import com.badlogic.gdx.Input;
import com.flurry.android.CallbackEvent;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class Frame {
    static final int[] a;
    Label b;
    int[] c;
    int[] d;
    private int[] e;
    private int[] f;
    private int g;
    private int h;
    private int[] i;

    static {
        int[] iArr = new int[CallbackEvent.ADS_UPDATED];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(i) - 'E';
        }
        a = iArr;
    }

    Frame() {
    }

    private int a() {
        if (this.g > 0) {
            int[] iArr = this.f;
            int i = this.g - 1;
            this.g = i;
            return iArr[i];
        }
        Label label = this.b;
        int i2 = label.f - 1;
        label.f = i2;
        return 50331648 | (-i2);
    }

    private int a(int i) {
        if (this.e == null || i >= this.e.length) {
            return 33554432 | i;
        }
        int i2 = this.e[i];
        if (i2 != 0) {
            return i2;
        }
        int i3 = 33554432 | i;
        this.e[i] = i3;
        return i3;
    }

    private int a(ClassWriter classWriter, int i) {
        int iC;
        if (i == 16777222) {
            iC = classWriter.c(classWriter.F) | 24117248;
        } else {
            if (((-1048576) & i) != 25165824) {
                return i;
            }
            iC = classWriter.c(classWriter.E[1048575 & i].g) | 24117248;
        }
        for (int i2 = 0; i2 < this.h; i2++) {
            int i3 = this.i[i2];
            int i4 = (-268435456) & i3;
            int i5 = 251658240 & i3;
            if (i5 == 33554432) {
                i3 = this.c[i3 & 8388607] + i4;
            } else if (i5 == 50331648) {
                i3 = this.d[this.d.length - (i3 & 8388607)] + i4;
            }
            if (i == i3) {
                return iC;
            }
        }
        return i;
    }

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[10];
        }
        int length = this.e.length;
        if (i >= length) {
            int[] iArr = new int[Math.max(i + 1, length * 2)];
            System.arraycopy(this.e, 0, iArr, 0, length);
            this.e = iArr;
        }
        this.e[i] = i2;
    }

    private void a(String str) {
        char cCharAt = str.charAt(0);
        if (cCharAt == '(') {
            c((Type.getArgumentsAndReturnSizes(str) >> 2) - 1);
        } else if (cCharAt == 'J' || cCharAt == 'D') {
            c(2);
        } else {
            c(1);
        }
    }

    private void a(ClassWriter classWriter, String str) {
        int iB = b(classWriter, str);
        if (iB != 0) {
            b(iB);
            if (iB == 16777220 || iB == 16777219) {
                b(16777216);
            }
        }
    }

    private static boolean a(ClassWriter classWriter, int i, int[] iArr, int i2) {
        int iC;
        int i3 = iArr[i2];
        if (i3 == i) {
            return false;
        }
        if ((268435455 & i) != 16777221) {
            iC = i;
        } else {
            if (i3 == 16777221) {
                return false;
            }
            iC = 16777221;
        }
        if (i3 == 0) {
            iArr[i2] = iC;
            return true;
        }
        if ((i3 & 267386880) == 24117248 || (i3 & (-268435456)) != 0) {
            if (iC == 16777221) {
                return false;
            }
            if (((-1048576) & iC) != ((-1048576) & i3)) {
                iC = ((iC & 267386880) == 24117248 || (iC & (-268435456)) != 0) ? classWriter.c("java/lang/Object") | 24117248 : 16777216;
            } else if ((i3 & 267386880) == 24117248) {
                iC = classWriter.a(iC & 1048575, 1048575 & i3) | (iC & (-268435456)) | 24117248;
            } else {
                iC = classWriter.c("java/lang/Object") | 24117248;
            }
        } else if (i3 != 16777221) {
            iC = 16777216;
        } else if ((iC & 267386880) != 24117248 && (iC & (-268435456)) == 0) {
            iC = 16777216;
        }
        if (i3 == iC) {
            return false;
        }
        iArr[i2] = iC;
        return true;
    }

    private static int b(ClassWriter classWriter, String str) {
        int iC = 16777217;
        int iIndexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(iIndexOf)) {
            case Input.Keys.ENTER /* 66 */:
            case 'C':
            case Input.Keys.BACKSLASH /* 73 */:
            case 'S':
            case 'Z':
                return 16777217;
            case Input.Keys.GRAVE /* 68 */:
                return 16777219;
            case Input.Keys.EQUALS /* 70 */:
                return 16777218;
            case Input.Keys.SEMICOLON /* 74 */:
                return 16777220;
            case Input.Keys.SLASH /* 76 */:
                return 24117248 | classWriter.c(str.substring(iIndexOf + 1, str.length() - 1));
            case 'V':
                return 0;
            default:
                int i = iIndexOf + 1;
                while (str.charAt(i) == '[') {
                    i++;
                }
                switch (str.charAt(i)) {
                    case Input.Keys.ENTER /* 66 */:
                        iC = 16777226;
                        break;
                    case 'C':
                        iC = 16777227;
                        break;
                    case Input.Keys.GRAVE /* 68 */:
                        iC = 16777219;
                        break;
                    case Input.Keys.EQUALS /* 70 */:
                        iC = 16777218;
                        break;
                    case Input.Keys.BACKSLASH /* 73 */:
                        break;
                    case Input.Keys.SEMICOLON /* 74 */:
                        iC = 16777220;
                        break;
                    case 'S':
                        iC = 16777228;
                        break;
                    case 'Z':
                        iC = 16777225;
                        break;
                    default:
                        iC = classWriter.c(str.substring(i + 1, str.length() - 1)) | 24117248;
                        break;
                }
                return ((i - iIndexOf) << 28) | iC;
        }
    }

    private void b(int i) {
        if (this.f == null) {
            this.f = new int[10];
        }
        int length = this.f.length;
        if (this.g >= length) {
            int[] iArr = new int[Math.max(this.g + 1, length * 2)];
            System.arraycopy(this.f, 0, iArr, 0, length);
            this.f = iArr;
        }
        int[] iArr2 = this.f;
        int i2 = this.g;
        this.g = i2 + 1;
        iArr2[i2] = i;
        int i3 = this.b.f + this.g;
        if (i3 > this.b.g) {
            this.b.g = i3;
        }
    }

    private void c(int i) {
        if (this.g >= i) {
            this.g -= i;
            return;
        }
        this.b.f -= i - this.g;
        this.g = 0;
    }

    private void d(int i) {
        if (this.i == null) {
            this.i = new int[2];
        }
        int length = this.i.length;
        if (this.h >= length) {
            int[] iArr = new int[Math.max(this.h + 1, length * 2)];
            System.arraycopy(this.i, 0, iArr, 0, length);
            this.i = iArr;
        }
        int[] iArr2 = this.i;
        int i2 = this.h;
        this.h = i2 + 1;
        iArr2[i2] = i;
    }

    void a(int i, int i2, ClassWriter classWriter, Item item) {
        switch (i) {
            case 0:
            case Opcodes.INEG /* 116 */:
            case Opcodes.LNEG /* 117 */:
            case Opcodes.FNEG /* 118 */:
            case Opcodes.DNEG /* 119 */:
            case Opcodes.I2B /* 145 */:
            case Opcodes.I2C /* 146 */:
            case Opcodes.I2S /* 147 */:
            case Opcodes.GOTO /* 167 */:
            case Opcodes.RETURN /* 177 */:
                return;
            case 1:
                b(16777221);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 21:
                b(16777217);
                return;
            case 9:
            case 10:
            case 22:
                b(16777220);
                b(16777216);
                return;
            case 11:
            case 12:
            case 13:
            case 23:
                b(16777218);
                return;
            case 14:
            case 15:
            case 24:
                b(16777219);
                b(16777216);
                return;
            case 18:
                switch (item.b) {
                    case 3:
                        b(16777217);
                        return;
                    case 4:
                        b(16777218);
                        return;
                    case 5:
                        b(16777220);
                        b(16777216);
                        return;
                    case 6:
                        b(16777219);
                        b(16777216);
                        return;
                    case 7:
                        b(24117248 | classWriter.c("java/lang/Class"));
                        return;
                    default:
                        b(24117248 | classWriter.c("java/lang/String"));
                        return;
                }
            case 19:
            case 20:
            case Input.Keys.POWER /* 26 */:
            case Input.Keys.CAMERA /* 27 */:
            case Input.Keys.CLEAR /* 28 */:
            case Input.Keys.A /* 29 */:
            case Input.Keys.B /* 30 */:
            case Input.Keys.C /* 31 */:
            case 32:
            case Input.Keys.E /* 33 */:
            case Input.Keys.F /* 34 */:
            case Input.Keys.G /* 35 */:
            case 36:
            case Input.Keys.I /* 37 */:
            case Input.Keys.J /* 38 */:
            case Input.Keys.K /* 39 */:
            case 40:
            case Input.Keys.M /* 41 */:
            case Input.Keys.N /* 42 */:
            case Input.Keys.O /* 43 */:
            case Input.Keys.P /* 44 */:
            case Input.Keys.Q /* 45 */:
            case Input.Keys.SHIFT_LEFT /* 59 */:
            case Input.Keys.SHIFT_RIGHT /* 60 */:
            case Input.Keys.TAB /* 61 */:
            case Input.Keys.SPACE /* 62 */:
            case Input.Keys.SYM /* 63 */:
            case 64:
            case 65:
            case Input.Keys.ENTER /* 66 */:
            case 67:
            case Input.Keys.GRAVE /* 68 */:
            case Input.Keys.MINUS /* 69 */:
            case Input.Keys.EQUALS /* 70 */:
            case Input.Keys.LEFT_BRACKET /* 71 */:
            case Input.Keys.RIGHT_BRACKET /* 72 */:
            case Input.Keys.BACKSLASH /* 73 */:
            case Input.Keys.SEMICOLON /* 74 */:
            case 75:
            case Input.Keys.SLASH /* 76 */:
            case Input.Keys.AT /* 77 */:
            case Input.Keys.NUM /* 78 */:
            case SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING /* 196 */:
            case Opcodes.MULTIANEWARRAY /* 197 */:
            default:
                c(i2);
                a(classWriter, item.g);
                return;
            case 25:
                b(a(i2));
                return;
            case 46:
            case 51:
            case 52:
            case 53:
                c(2);
                b(16777217);
                return;
            case 47:
            case Opcodes.D2L /* 143 */:
                c(2);
                b(16777220);
                b(16777216);
                return;
            case 48:
                c(2);
                b(16777218);
                return;
            case 49:
            case Opcodes.L2D /* 138 */:
                c(2);
                b(16777219);
                b(16777216);
                return;
            case 50:
                c(1);
                b(a() - 268435456);
                return;
            case 54:
            case 56:
            case 58:
                a(i2, a());
                if (i2 > 0) {
                    int iA = a(i2 - 1);
                    if (iA == 16777220 || iA == 16777219) {
                        a(i2 - 1, 16777216);
                        return;
                    } else {
                        if ((251658240 & iA) != 16777216) {
                            a(i2 - 1, iA | 8388608);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 55:
            case 57:
                c(1);
                a(i2, a());
                a(i2 + 1, 16777216);
                if (i2 > 0) {
                    int iA2 = a(i2 - 1);
                    if (iA2 == 16777220 || iA2 == 16777219) {
                        a(i2 - 1, 16777216);
                        return;
                    } else {
                        if ((251658240 & iA2) != 16777216) {
                            a(i2 - 1, iA2 | 8388608);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                c(3);
                return;
            case 80:
            case 82:
                c(4);
                return;
            case 87:
            case Opcodes.IFEQ /* 153 */:
            case Opcodes.IFNE /* 154 */:
            case Opcodes.IFLT /* 155 */:
            case Opcodes.IFGE /* 156 */:
            case Opcodes.IFGT /* 157 */:
            case Opcodes.IFLE /* 158 */:
            case Opcodes.TABLESWITCH /* 170 */:
            case Opcodes.LOOKUPSWITCH /* 171 */:
            case 172:
            case Opcodes.FRETURN /* 174 */:
            case Opcodes.ARETURN /* 176 */:
            case Opcodes.ATHROW /* 191 */:
            case Opcodes.MONITORENTER /* 194 */:
            case Opcodes.MONITOREXIT /* 195 */:
            case Opcodes.IFNULL /* 198 */:
            case Opcodes.IFNONNULL /* 199 */:
                c(1);
                return;
            case 88:
            case Opcodes.IF_ICMPEQ /* 159 */:
            case 160:
            case Opcodes.IF_ICMPLT /* 161 */:
            case Opcodes.IF_ICMPGE /* 162 */:
            case Opcodes.IF_ICMPGT /* 163 */:
            case Opcodes.IF_ICMPLE /* 164 */:
            case Opcodes.IF_ACMPEQ /* 165 */:
            case Opcodes.IF_ACMPNE /* 166 */:
            case Opcodes.LRETURN /* 173 */:
            case Opcodes.DRETURN /* 175 */:
                c(2);
                return;
            case 89:
                int iA3 = a();
                b(iA3);
                b(iA3);
                return;
            case 90:
                int iA4 = a();
                int iA5 = a();
                b(iA4);
                b(iA5);
                b(iA4);
                return;
            case 91:
                int iA6 = a();
                int iA7 = a();
                int iA8 = a();
                b(iA6);
                b(iA8);
                b(iA7);
                b(iA6);
                return;
            case 92:
                int iA9 = a();
                int iA10 = a();
                b(iA10);
                b(iA9);
                b(iA10);
                b(iA9);
                return;
            case 93:
                int iA11 = a();
                int iA12 = a();
                int iA13 = a();
                b(iA12);
                b(iA11);
                b(iA13);
                b(iA12);
                b(iA11);
                return;
            case 94:
                int iA14 = a();
                int iA15 = a();
                int iA16 = a();
                int iA17 = a();
                b(iA15);
                b(iA14);
                b(iA17);
                b(iA16);
                b(iA15);
                b(iA14);
                return;
            case 95:
                int iA18 = a();
                int iA19 = a();
                b(iA18);
                b(iA19);
                return;
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case Opcodes.ISHL /* 120 */:
            case Opcodes.ISHR /* 122 */:
            case Opcodes.IUSHR /* 124 */:
            case Opcodes.IAND /* 126 */:
            case 128:
            case 130:
            case Opcodes.L2I /* 136 */:
            case Opcodes.D2I /* 142 */:
            case Opcodes.FCMPL /* 149 */:
            case Opcodes.FCMPG /* 150 */:
                c(2);
                b(16777217);
                return;
            case 97:
            case 101:
            case 105:
            case 109:
            case Opcodes.LREM /* 113 */:
            case Opcodes.LAND /* 127 */:
            case 129:
            case 131:
                c(4);
                b(16777220);
                b(16777216);
                return;
            case 98:
            case 102:
            case 106:
            case 110:
            case Opcodes.FREM /* 114 */:
            case Opcodes.L2F /* 137 */:
            case Opcodes.D2F /* 144 */:
                c(2);
                b(16777218);
                return;
            case 99:
            case 103:
            case 107:
            case Opcodes.DDIV /* 111 */:
            case Opcodes.DREM /* 115 */:
                c(4);
                b(16777219);
                b(16777216);
                return;
            case Opcodes.LSHL /* 121 */:
            case Opcodes.LSHR /* 123 */:
            case Opcodes.LUSHR /* 125 */:
                c(3);
                b(16777220);
                b(16777216);
                return;
            case 132:
                a(i2, 16777217);
                return;
            case 133:
            case Opcodes.F2L /* 140 */:
                c(1);
                b(16777220);
                b(16777216);
                return;
            case Opcodes.I2F /* 134 */:
                c(1);
                b(16777218);
                return;
            case Opcodes.I2D /* 135 */:
            case Opcodes.F2D /* 141 */:
                c(1);
                b(16777219);
                b(16777216);
                return;
            case Opcodes.F2I /* 139 */:
            case Opcodes.ARRAYLENGTH /* 190 */:
            case 193:
                c(1);
                b(16777217);
                return;
            case Opcodes.LCMP /* 148 */:
            case Opcodes.DCMPL /* 151 */:
            case Opcodes.DCMPG /* 152 */:
                c(4);
                b(16777217);
                return;
            case Opcodes.JSR /* 168 */:
            case Opcodes.RET /* 169 */:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case Opcodes.GETSTATIC /* 178 */:
                a(classWriter, item.i);
                return;
            case Opcodes.PUTSTATIC /* 179 */:
                a(item.i);
                return;
            case Opcodes.GETFIELD /* 180 */:
                c(1);
                a(classWriter, item.i);
                return;
            case Opcodes.PUTFIELD /* 181 */:
                a(item.i);
                a();
                return;
            case Opcodes.INVOKEVIRTUAL /* 182 */:
            case Opcodes.INVOKESPECIAL /* 183 */:
            case Opcodes.INVOKESTATIC /* 184 */:
            case Opcodes.INVOKEINTERFACE /* 185 */:
                a(item.i);
                if (i != 184) {
                    int iA20 = a();
                    if (i == 183 && item.h.charAt(0) == '<') {
                        d(iA20);
                    }
                }
                a(classWriter, item.i);
                return;
            case Opcodes.INVOKEDYNAMIC /* 186 */:
                a(item.h);
                a(classWriter, item.h);
                return;
            case Opcodes.NEW /* 187 */:
                b(25165824 | classWriter.a(item.g, i2));
                return;
            case Opcodes.NEWARRAY /* 188 */:
                a();
                switch (i2) {
                    case 4:
                        b(285212681);
                        return;
                    case 5:
                        b(285212683);
                        return;
                    case 6:
                        b(285212674);
                        return;
                    case 7:
                        b(285212675);
                        return;
                    case 8:
                        b(285212682);
                        return;
                    case 9:
                        b(285212684);
                        return;
                    case 10:
                        b(285212673);
                        return;
                    default:
                        b(285212676);
                        return;
                }
            case Opcodes.ANEWARRAY /* 189 */:
                String str = item.g;
                a();
                if (str.charAt(0) == '[') {
                    a(classWriter, new StringBuffer().append('[').append(str).toString());
                    return;
                } else {
                    b(classWriter.c(str) | 292552704);
                    return;
                }
            case 192:
                String str2 = item.g;
                a();
                if (str2.charAt(0) == '[') {
                    a(classWriter, str2);
                    return;
                } else {
                    b(classWriter.c(str2) | 24117248);
                    return;
                }
        }
    }

    void a(ClassWriter classWriter, int i, Type[] typeArr, int i2) {
        int i3 = 1;
        this.c = new int[i2];
        this.d = new int[0];
        if ((i & 8) != 0) {
            i3 = 0;
        } else if ((262144 & i) == 0) {
            this.c[0] = 24117248 | classWriter.c(classWriter.F);
        } else {
            this.c[0] = 16777222;
        }
        for (Type type : typeArr) {
            int iB = b(classWriter, type.getDescriptor());
            int i4 = i3 + 1;
            this.c[i3] = iB;
            if (iB == 16777220 || iB == 16777219) {
                i3 = i4 + 1;
                this.c[i4] = 16777216;
            } else {
                i3 = i4;
            }
        }
        while (i3 < i2) {
            this.c[i3] = 16777216;
            i3++;
        }
    }

    boolean a(ClassWriter classWriter, Frame frame, int i) {
        boolean z;
        int iA;
        boolean z2 = false;
        int length = this.c.length;
        int length2 = this.d.length;
        if (frame.c == null) {
            frame.c = new int[length];
            z2 = true;
        }
        int i2 = 0;
        boolean zA = z2;
        while (i2 < length) {
            if (this.e == null || i2 >= this.e.length || (iA = this.e[i2]) == 0) {
                iA = this.c[i2];
            } else {
                int i3 = (-268435456) & iA;
                int i4 = 251658240 & iA;
                if (i4 != 16777216) {
                    int i5 = i4 == 33554432 ? i3 + this.c[8388607 & iA] : i3 + this.d[length2 - (8388607 & iA)];
                    iA = ((iA & 8388608) == 0 || !(i5 == 16777220 || i5 == 16777219)) ? i5 : 16777216;
                }
            }
            if (this.i != null) {
                iA = a(classWriter, iA);
            }
            zA |= a(classWriter, iA, frame.c, i2);
            i2++;
        }
        if (i > 0) {
            int i6 = 0;
            boolean z3 = zA;
            while (i6 < length) {
                boolean zA2 = a(classWriter, this.c[i6], frame.c, i6) | z3;
                i6++;
                z3 = zA2;
            }
            if (frame.d == null) {
                frame.d = new int[1];
                z3 = true;
            }
            return a(classWriter, i, frame.d, 0) | z3;
        }
        int length3 = this.d.length + this.b.f;
        if (frame.d == null) {
            frame.d = new int[this.g + length3];
            z = true;
        } else {
            z = zA;
        }
        boolean zA3 = z;
        for (int i7 = 0; i7 < length3; i7++) {
            int iA2 = this.d[i7];
            if (this.i != null) {
                iA2 = a(classWriter, iA2);
            }
            zA3 |= a(classWriter, iA2, frame.d, i7);
        }
        for (int i8 = 0; i8 < this.g; i8++) {
            int iA3 = this.f[i8];
            int i9 = (-268435456) & iA3;
            int i10 = 251658240 & iA3;
            if (i10 != 16777216) {
                int i11 = i10 == 33554432 ? i9 + this.c[8388607 & iA3] : i9 + this.d[length2 - (8388607 & iA3)];
                iA3 = ((iA3 & 8388608) == 0 || !(i11 == 16777220 || i11 == 16777219)) ? i11 : 16777216;
            }
            if (this.i != null) {
                iA3 = a(classWriter, iA3);
            }
            zA3 |= a(classWriter, iA3, frame.d, length3 + i8);
        }
        return zA3;
    }
}
