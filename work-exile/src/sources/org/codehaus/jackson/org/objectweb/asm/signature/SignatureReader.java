package org.codehaus.jackson.org.objectweb.asm.signature;

import com.badlogic.gdx.Input;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SignatureReader {
    private final String a;

    public SignatureReader(String str) {
        this.a = str;
    }

    private static int a(String str, int i, SignatureVisitor signatureVisitor) {
        int i2 = i + 1;
        char cCharAt = str.charAt(i);
        switch (cCharAt) {
            case Input.Keys.ENTER /* 66 */:
            case 'C':
            case Input.Keys.GRAVE /* 68 */:
            case Input.Keys.EQUALS /* 70 */:
            case Input.Keys.BACKSLASH /* 73 */:
            case Input.Keys.SEMICOLON /* 74 */:
            case 'S':
            case 'V':
            case 'Z':
                signatureVisitor.visitBaseType(cCharAt);
                return i2;
            case Input.Keys.MINUS /* 69 */:
            case Input.Keys.LEFT_BRACKET /* 71 */:
            case Input.Keys.RIGHT_BRACKET /* 72 */:
            case 'K':
            case Input.Keys.SLASH /* 76 */:
            case Input.Keys.AT /* 77 */:
            case Input.Keys.NUM /* 78 */:
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                boolean z = false;
                int i3 = i2;
                int i4 = i2;
                boolean z2 = false;
                while (true) {
                    int i5 = i4 + 1;
                    char cCharAt2 = str.charAt(i4);
                    switch (cCharAt2) {
                        case '.':
                        case Input.Keys.SHIFT_LEFT /* 59 */:
                            if (!z) {
                                String strSubstring = str.substring(i3, i5 - 1);
                                if (z2) {
                                    signatureVisitor.visitInnerClassType(strSubstring);
                                } else {
                                    signatureVisitor.visitClassType(strSubstring);
                                }
                            }
                            if (cCharAt2 == ';') {
                                signatureVisitor.visitEnd();
                                return i5;
                            }
                            z2 = true;
                            z = false;
                            i3 = i5;
                            i4 = i5;
                            break;
                        case Input.Keys.SHIFT_RIGHT /* 60 */:
                            String strSubstring2 = str.substring(i3, i5 - 1);
                            if (z2) {
                                signatureVisitor.visitInnerClassType(strSubstring2);
                            } else {
                                signatureVisitor.visitClassType(strSubstring2);
                            }
                            int iA = i5;
                            while (true) {
                                char cCharAt3 = str.charAt(iA);
                                switch (cCharAt3) {
                                    case Input.Keys.N /* 42 */:
                                        iA++;
                                        signatureVisitor.visitTypeArgument();
                                        break;
                                    case Input.Keys.O /* 43 */:
                                    case Input.Keys.Q /* 45 */:
                                        iA = a(str, iA + 1, signatureVisitor.visitTypeArgument(cCharAt3));
                                        break;
                                    case Input.Keys.SPACE /* 62 */:
                                        i4 = iA;
                                        z = true;
                                        continue;
                                    default:
                                        iA = a(str, iA, signatureVisitor.visitTypeArgument(SignatureVisitor.INSTANCEOF));
                                        break;
                                }
                            }
                            break;
                        default:
                            i4 = i5;
                            break;
                    }
                }
                break;
            case 'T':
                int iIndexOf = str.indexOf(59, i2);
                signatureVisitor.visitTypeVariable(str.substring(i2, iIndexOf));
                return iIndexOf + 1;
            case '[':
                return a(str, i2, signatureVisitor.visitArrayType());
        }
    }

    public void accept(SignatureVisitor signatureVisitor) {
        char cCharAt;
        int i = 0;
        String str = this.a;
        int length = str.length();
        if (str.charAt(0) == '<') {
            i = 2;
            do {
                int iIndexOf = str.indexOf(58, i);
                signatureVisitor.visitFormalTypeParameter(str.substring(i - 1, iIndexOf));
                int iA = iIndexOf + 1;
                char cCharAt2 = str.charAt(iA);
                if (cCharAt2 == 'L' || cCharAt2 == '[' || cCharAt2 == 'T') {
                    iA = a(str, iA, signatureVisitor.visitClassBound());
                }
                while (true) {
                    int i2 = iA;
                    i = i2 + 1;
                    cCharAt = str.charAt(i2);
                    if (cCharAt != ':') {
                        break;
                    } else {
                        iA = a(str, i, signatureVisitor.visitInterfaceBound());
                    }
                }
            } while (cCharAt != '>');
        }
        if (str.charAt(i) != '(') {
            int iA2 = a(str, i, signatureVisitor.visitSuperclass());
            while (iA2 < length) {
                iA2 = a(str, iA2, signatureVisitor.visitInterface());
            }
        } else {
            int iA3 = i + 1;
            while (str.charAt(iA3) != ')') {
                iA3 = a(str, iA3, signatureVisitor.visitParameterType());
            }
            int iA4 = a(str, iA3 + 1, signatureVisitor.visitReturnType());
            while (iA4 < length) {
                iA4 = a(str, iA4 + 1, signatureVisitor.visitExceptionType());
            }
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        a(this.a, 0, signatureVisitor);
    }
}
