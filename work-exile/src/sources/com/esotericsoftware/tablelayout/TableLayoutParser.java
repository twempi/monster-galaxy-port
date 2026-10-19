package com.esotericsoftware.tablelayout;

import java.util.ArrayList;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class TableLayoutParser {
    static final int tableLayout_en_main = 1;
    static final int tableLayout_en_main_table = 62;
    static final int tableLayout_en_stack = 91;
    static final int tableLayout_en_widgetSection = 68;
    static final int tableLayout_error = 0;
    static final int tableLayout_first_final = 104;
    static final int tableLayout_start = 1;
    private static final byte[] _tableLayout_actions = init__tableLayout_actions_0();
    private static final short[] _tableLayout_key_offsets = init__tableLayout_key_offsets_0();
    private static final char[] _tableLayout_trans_keys = init__tableLayout_trans_keys_0();
    private static final byte[] _tableLayout_single_lengths = init__tableLayout_single_lengths_0();
    private static final byte[] _tableLayout_range_lengths = init__tableLayout_range_lengths_0();
    private static final short[] _tableLayout_index_offsets = init__tableLayout_index_offsets_0();
    private static final byte[] _tableLayout_trans_targs = init__tableLayout_trans_targs_0();
    private static final short[] _tableLayout_trans_actions = init__tableLayout_trans_actions_0();

    TableLayoutParser() {
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0082  */
    /* JADX WARN: Code duplicated, block: B:125:0x04f7  */
    /* JADX WARN: Code duplicated, block: B:128:0x051c A[Catch: RuntimeException -> 0x0400, TRY_LEAVE, TryCatch #4 {RuntimeException -> 0x0400, blocks: (B:38:0x00f1, B:59:0x016c, B:60:0x0183, B:62:0x01a3, B:63:0x01cd, B:65:0x01eb, B:66:0x0215, B:67:0x0235, B:72:0x024d, B:73:0x0277, B:75:0x028f, B:76:0x0296, B:78:0x02aa, B:79:0x02d4, B:81:0x02ec, B:82:0x0316, B:84:0x032e, B:85:0x0359, B:86:0x0374, B:88:0x038b, B:91:0x03c7, B:93:0x03cd, B:96:0x03e4, B:99:0x03ec, B:103:0x040d, B:108:0x0423, B:109:0x043f, B:110:0x0440, B:111:0x044b, B:113:0x045b, B:115:0x0468, B:116:0x048f, B:118:0x04b2, B:119:0x04ce, B:121:0x04df, B:123:0x04ef, B:126:0x04f9, B:128:0x051c, B:139:0x057c, B:142:0x0584, B:143:0x058b, B:148:0x05b0, B:149:0x05cc, B:151:0x05d9, B:162:0x062d, B:163:0x0634, B:168:0x0659, B:169:0x0660, B:171:0x0674, B:177:0x06b0, B:178:0x06b7, B:183:0x06dc, B:184:0x06e3, B:186:0x06ff, B:187:0x0706, B:189:0x0725, B:190:0x074f, B:192:0x0759, B:193:0x0763, B:105:0x0417), top: B:231:0x00f1, inners: #0 }] */
    /* JADX WARN: Code duplicated, block: B:133:0x0540 A[Catch: RuntimeException -> 0x0840, TRY_LEAVE, TryCatch #3 {RuntimeException -> 0x0840, blocks: (B:131:0x0535, B:133:0x0540, B:154:0x05e8, B:156:0x05f3), top: B:229:0x0535 }] */
    /* JADX WARN: Code duplicated, block: B:13:0x008e  */
    /* JADX WARN: Code duplicated, block: B:15:0x0098  */
    /* JADX WARN: Code duplicated, block: B:202:0x07a6  */
    /* JADX WARN: Code duplicated, block: B:205:0x0805 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:206:0x0807  */
    /* JADX WARN: Code duplicated, block: B:208:0x083f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:220:0x086b  */
    /* JADX WARN: Code duplicated, block: B:250:0x079e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:251:0x079b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:252:0x007a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:294:0x009c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:297:0x0149 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:298:0x00d1 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:299:0x0146 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:29:0x00c8 A[Catch: RuntimeException -> 0x0859, TryCatch #6 {RuntimeException -> 0x0859, blocks: (B:22:0x00ab, B:24:0x00b9, B:26:0x00c0, B:43:0x010f, B:45:0x011b, B:46:0x011e, B:48:0x012a, B:49:0x012d, B:32:0x00d2, B:34:0x00dc, B:27:0x00c2, B:29:0x00c8, B:31:0x00d1, B:50:0x0132, B:52:0x0146, B:53:0x0149), top: B:235:0x00ab }] */
    /* JADX WARN: Code duplicated, block: B:300:0x015b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:50:0x0132 A[Catch: RuntimeException -> 0x0859, TryCatch #6 {RuntimeException -> 0x0859, blocks: (B:22:0x00ab, B:24:0x00b9, B:26:0x00c0, B:43:0x010f, B:45:0x011b, B:46:0x011e, B:48:0x012a, B:49:0x012d, B:32:0x00d2, B:34:0x00dc, B:27:0x00c2, B:29:0x00c8, B:31:0x00d1, B:50:0x0132, B:52:0x0146, B:53:0x0149), top: B:235:0x00ab }] */
    /* JADX WARN: Code duplicated, block: B:56:0x0157  */
    public static void parse(BaseTableLayout baseTableLayout, String str) {
        int i;
        int i2;
        int i3;
        int i4;
        byte b;
        int i5;
        int i6;
        int i7;
        int i8;
        Object layout;
        int length;
        Object objNewWidget;
        K k = baseTableLayout.toolkit;
        char[] charArray = (str + "  ").toCharArray();
        int i9 = 0;
        int length2 = charArray.length;
        int[] iArr = new int[4];
        int i10 = 0;
        String str2 = null;
        String strTrim = null;
        String str3 = null;
        int i11 = 0;
        ArrayList arrayList = new ArrayList(4);
        ArrayList arrayList2 = new ArrayList(8);
        Cell cellAdd = null;
        Cell cellRow = null;
        Cell cellColumnDefaults = null;
        RuntimeException runtimeException = null;
        boolean z = false;
        if (0 != 0) {
            System.out.println();
        }
        int i12 = 1;
        int i13 = 0;
        Object obj = baseTableLayout;
        int i14 = 0;
        Object objWrap = null;
        while (true) {
            switch (i13) {
                case 0:
                    if (i9 == length2) {
                        i13 = 4;
                    } else if (i12 == 0) {
                        i13 = 5;
                    }
                case 1:
                    try {
                        short s = _tableLayout_key_offsets[i12];
                        short s2 = _tableLayout_index_offsets[i12];
                        byte b2 = _tableLayout_single_lengths[i12];
                        int i15 = s;
                        int i16 = s2;
                        if (b2 > 0) {
                            int i17 = s;
                            int i18 = (s + b2) - 1;
                            while (true) {
                                if (i18 >= i17) {
                                    int i19 = i17 + ((i18 - i17) >> 1);
                                    if (charArray[i9] < _tableLayout_trans_keys[i19]) {
                                        i18 = i19 - 1;
                                    } else if (charArray[i9] > _tableLayout_trans_keys[i19]) {
                                        i17 = i19 + 1;
                                    } else {
                                        i8 = s2 + (i19 - s);
                                    }
                                } else {
                                    i15 = s + b2;
                                    i16 = s2 + b2;
                                    b = _tableLayout_range_lengths[i12];
                                    i8 = i16;
                                    if (b > 0) {
                                        i5 = i15;
                                        i6 = ((b << 1) + i15) - 2;
                                        while (true) {
                                            if (i6 < i5) {
                                                i7 = i5 + (((i6 - i5) >> 1) & (-2));
                                                if (charArray[i9] < _tableLayout_trans_keys[i7]) {
                                                    i6 = i7 - 2;
                                                } else if (charArray[i9] > _tableLayout_trans_keys[i7 + 1]) {
                                                    i5 = i7 + 2;
                                                } else {
                                                    i8 = i16 + ((i7 - i15) >> 1);
                                                }
                                            } else {
                                                i8 = i16 + b;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            b = _tableLayout_range_lengths[i12];
                            i8 = i16;
                            if (b > 0) {
                                i5 = i15;
                                i6 = ((b << 1) + i15) - 2;
                                while (true) {
                                    if (i6 < i5) {
                                        i7 = i5 + (((i6 - i5) >> 1) & (-2));
                                        if (charArray[i9] < _tableLayout_trans_keys[i7]) {
                                            i6 = i7 - 2;
                                        } else if (charArray[i9] > _tableLayout_trans_keys[i7 + 1]) {
                                            i5 = i7 + 2;
                                        } else {
                                            i8 = i16 + ((i7 - i15) >> 1);
                                        }
                                    } else {
                                        i8 = i16 + b;
                                    }
                                }
                            }
                        }
                        i12 = _tableLayout_trans_targs[i8];
                        if (_tableLayout_trans_actions[i8] != 0) {
                            short s3 = _tableLayout_trans_actions[i8];
                            int i20 = s3 + 1;
                            int i21 = _tableLayout_actions[s3];
                            while (true) {
                                int i22 = i11;
                                String str4 = str3;
                                String str5 = str2;
                                int i23 = i21 - 1;
                                if (i21 > 0) {
                                    try {
                                        int i24 = i20 + 1;
                                        switch (_tableLayout_actions[i20]) {
                                            case 0:
                                                i10 = i9;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 1:
                                                str2 = new String(charArray, i10, i9 - i10);
                                                i10 = i9;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 2:
                                                arrayList.add(new String(charArray, i10, i9 - i10));
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 3:
                                                if (0 != 0) {
                                                    System.out.println("tableProperty: " + str5 + " = " + arrayList);
                                                }
                                                k.setTableProperty((BaseTableLayout) obj, str5, arrayList);
                                                arrayList.clear();
                                                str2 = null;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 4:
                                                if (0 != 0) {
                                                    System.out.println("cellDefaultProperty: " + str5 + " = " + arrayList);
                                                }
                                                k.setCellProperty(((BaseTableLayout) obj).defaults(), str5, arrayList);
                                                arrayList.clear();
                                                str2 = null;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 5:
                                                i11 = i22 + 1;
                                                try {
                                                    cellColumnDefaults = ((BaseTableLayout) obj).columnDefaults(i22);
                                                    str3 = str4;
                                                    str2 = str5;
                                                    i21 = i23;
                                                    i20 = i24;
                                                } catch (RuntimeException e) {
                                                    e = e;
                                                    i = i14;
                                                    runtimeException = e;
                                                    if (i9 < length2) {
                                                        i2 = 1;
                                                        i3 = 0;
                                                        for (i4 = 0; i4 < i9; i4++) {
                                                            if (charArray[i4] == '\n') {
                                                                i2++;
                                                                i3 = i4;
                                                            }
                                                        }
                                                        ParseException parseException = new ParseException("Error parsing layout on line " + i2 + ":" + (i9 - i3) + " near: " + new String(charArray, i9, Math.min(64, length2 - i9)), runtimeException);
                                                        parseException.line = i2;
                                                        parseException.column = i9 - i3;
                                                        throw parseException;
                                                    }
                                                    if (i > 0) {
                                                        throw new ParseException("Error parsing layout (possibly an unmatched brace or quote): " + new String(charArray, 0, Math.min(64, length2)), runtimeException);
                                                    }
                                                }
                                                break;
                                            case 6:
                                                if (0 != 0) {
                                                    System.out.println("columnDefaultProperty: " + str5 + " = " + arrayList);
                                                }
                                                k.setCellProperty(cellColumnDefaults, str5, arrayList);
                                                arrayList.clear();
                                                str2 = null;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 7:
                                                if (0 != 0) {
                                                    System.out.println("startRow");
                                                }
                                                cellRow = ((BaseTableLayout) obj).row();
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 8:
                                                if (0 != 0) {
                                                    System.out.println("rowDefaultValue: " + str5 + " = " + arrayList);
                                                }
                                                k.setCellProperty(cellRow, str5, arrayList);
                                                arrayList.clear();
                                                str2 = null;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 9:
                                                if (0 != 0) {
                                                    System.out.println("cellProperty: " + str5 + " = " + arrayList);
                                                }
                                                k.setCellProperty(cellAdd, str5, arrayList);
                                                arrayList.clear();
                                                str2 = null;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 10:
                                                if (0 != 0) {
                                                    System.out.println("widgetLayoutString: " + new String(charArray, i10, i9 - i10).trim());
                                                }
                                                strTrim = new String(charArray, i10, i9 - i10).trim();
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 11:
                                                str3 = new String(charArray, i10, i9 - i10);
                                                i11 = i22;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 12:
                                                if (0 != 0) {
                                                    System.out.println("newWidget, name:" + str5 + " class:" + str4 + " widget:" + objWrap);
                                                }
                                                if (objWrap != null) {
                                                    if (str5 == null || str5.length() <= 0) {
                                                        objNewWidget = objWrap;
                                                    } else {
                                                        baseTableLayout.register(str5, objWrap);
                                                        objNewWidget = objWrap;
                                                    }
                                                } else {
                                                    if (str4 == null) {
                                                        if (str5.length() > 0) {
                                                            if (z) {
                                                                objNewWidget = k.wrap(null);
                                                                baseTableLayout.register(str5, objNewWidget);
                                                            } else {
                                                                objNewWidget = baseTableLayout.getWidget(str5);
                                                                if (objNewWidget == null) {
                                                                    try {
                                                                        objNewWidget = k.newWidget(baseTableLayout, str5);
                                                                    } catch (RuntimeException e2) {
                                                                        throw new IllegalArgumentException("Widget not found with name: " + str5);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            objNewWidget = k.wrap(null);
                                                        }
                                                    } else {
                                                        objNewWidget = k.newWidget(baseTableLayout, str4);
                                                        if (str5.length() > 0) {
                                                            baseTableLayout.register(str5, objNewWidget);
                                                        }
                                                    }
                                                    break;
                                                }
                                                str2 = null;
                                                str3 = null;
                                                objWrap = objNewWidget;
                                                i11 = i22;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 13:
                                                if (0 != 0) {
                                                    System.out.println("newLabel: " + new String(charArray, i10, i9 - i10));
                                                }
                                                objWrap = k.wrap(new String(charArray, i10, i9 - i10));
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 14:
                                                if (0 != 0) {
                                                    System.out.println("startTable, name:" + str5);
                                                }
                                                arrayList2.add(obj);
                                                BaseTableLayout baseTableLayout2 = null;
                                                try {
                                                    try {
                                                        for (int size = arrayList2.size() - 1; size >= 0; size--) {
                                                            Object obj2 = arrayList2.get(size);
                                                            if (obj2 instanceof BaseTableLayout) {
                                                                baseTableLayout2 = (BaseTableLayout) obj2;
                                                                if (baseTableLayout2 == null) {
                                                                    baseTableLayout2 = baseTableLayout;
                                                                }
                                                                layout = k.getLayout(k.newTable(baseTableLayout2.getTable()));
                                                                ((BaseTableLayout) layout).setParent(baseTableLayout2);
                                                                if (str5 != null) {
                                                                    baseTableLayout.register(str5, ((BaseTableLayout) layout).getTable());
                                                                    str2 = null;
                                                                } else {
                                                                    str2 = str5;
                                                                }
                                                                cellAdd = null;
                                                                length = iArr.length;
                                                                iArr = iArr;
                                                                if (i14 == length) {
                                                                    int[] iArr2 = new int[iArr.length * 2];
                                                                    System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                                                                    iArr = iArr2;
                                                                }
                                                                i = i14 + 1;
                                                                iArr[i14] = i12;
                                                                i12 = 62;
                                                                i13 = 2;
                                                                obj = layout;
                                                                i11 = i22;
                                                                str3 = str4;
                                                                i14 = i;
                                                                objWrap = null;
                                                            }
                                                            break;
                                                        }
                                                        iArr[i14] = i12;
                                                        i12 = 62;
                                                        i13 = 2;
                                                        obj = layout;
                                                        i11 = i22;
                                                        str3 = str4;
                                                        i14 = i;
                                                        objWrap = null;
                                                    } catch (RuntimeException e3) {
                                                        e = e3;
                                                        runtimeException = e;
                                                        if (i9 < length2) {
                                                            i2 = 1;
                                                            i3 = 0;
                                                            while (i4 < i9) {
                                                                if (charArray[i4] == '\n') {
                                                                    i2++;
                                                                    i3 = i4;
                                                                }
                                                            }
                                                            ParseException parseException2 = new ParseException("Error parsing layout on line " + i2 + ":" + (i9 - i3) + " near: " + new String(charArray, i9, Math.min(64, length2 - i9)), runtimeException);
                                                            parseException2.line = i2;
                                                            parseException2.column = i9 - i3;
                                                            throw parseException2;
                                                        }
                                                        if (i > 0) {
                                                            throw new ParseException("Error parsing layout (possibly an unmatched brace or quote): " + new String(charArray, 0, Math.min(64, length2)), runtimeException);
                                                        }
                                                    }
                                                    length = iArr.length;
                                                    iArr = iArr;
                                                    if (i14 == length) {
                                                        int[] iArr3 = new int[iArr.length * 2];
                                                        System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                                                        iArr = iArr3;
                                                    }
                                                    i = i14 + 1;
                                                } catch (RuntimeException e4) {
                                                    e = e4;
                                                    i = i14;
                                                }
                                                if (baseTableLayout2 == null) {
                                                    baseTableLayout2 = baseTableLayout;
                                                }
                                                layout = k.getLayout(k.newTable(baseTableLayout2.getTable()));
                                                ((BaseTableLayout) layout).setParent(baseTableLayout2);
                                                if (str5 != null) {
                                                    baseTableLayout.register(str5, ((BaseTableLayout) layout).getTable());
                                                    str2 = null;
                                                } else {
                                                    str2 = str5;
                                                }
                                                cellAdd = null;
                                                break;
                                            case 15:
                                                if (arrayList2.isEmpty()) {
                                                    objWrap = obj;
                                                    i11 = i22;
                                                    str3 = str4;
                                                    str2 = str5;
                                                    i21 = i23;
                                                    i20 = i24;
                                                } else {
                                                    if (0 != 0) {
                                                        System.out.println("endTable");
                                                    }
                                                    Object objRemove = arrayList2.remove(arrayList2.size() - 1);
                                                    i = i14 - 1;
                                                    try {
                                                        i12 = iArr[i];
                                                        i13 = 2;
                                                        i11 = i22;
                                                        str3 = str4;
                                                        str2 = str5;
                                                        i14 = i;
                                                        Object obj3 = obj;
                                                        obj = objRemove;
                                                        objWrap = obj3;
                                                    } catch (RuntimeException e5) {
                                                        e = e5;
                                                        runtimeException = e;
                                                        if (i9 < length2) {
                                                            i2 = 1;
                                                            i3 = 0;
                                                            while (i4 < i9) {
                                                                if (charArray[i4] == '\n') {
                                                                    i2++;
                                                                    i3 = i4;
                                                                }
                                                            }
                                                            ParseException parseException3 = new ParseException("Error parsing layout on line " + i2 + ":" + (i9 - i3) + " near: " + new String(charArray, i9, Math.min(64, length2 - i9)), runtimeException);
                                                            parseException3.line = i2;
                                                            parseException3.column = i9 - i3;
                                                            throw parseException3;
                                                        }
                                                        if (i > 0) {
                                                            throw new ParseException("Error parsing layout (possibly an unmatched brace or quote): " + new String(charArray, 0, Math.min(64, length2)), runtimeException);
                                                        }
                                                    }
                                                }
                                                break;
                                            case 16:
                                                if (0 != 0) {
                                                    System.out.println("startStack, name:" + str5);
                                                }
                                                arrayList2.add(obj);
                                                Object objNewStack = k.newStack();
                                                if (str5 != null) {
                                                    baseTableLayout.register(str5, objNewStack);
                                                    str2 = null;
                                                } else {
                                                    str2 = str5;
                                                }
                                                cellAdd = null;
                                                int length3 = iArr.length;
                                                iArr = iArr;
                                                if (i14 == length3) {
                                                    int[] iArr4 = new int[iArr.length * 2];
                                                    System.arraycopy(iArr, 0, iArr4, 0, iArr.length);
                                                    iArr = iArr4;
                                                }
                                                int i25 = i14 + 1;
                                                iArr[i14] = i12;
                                                i12 = 91;
                                                i13 = 2;
                                                obj = objNewStack;
                                                i11 = i22;
                                                str3 = str4;
                                                i14 = i25;
                                                objWrap = null;
                                                break;
                                            case 17:
                                                if (0 != 0) {
                                                    System.out.println("endStack");
                                                }
                                                Object objRemove2 = arrayList2.remove(arrayList2.size() - 1);
                                                int i26 = i14 - 1;
                                                i12 = iArr[i26];
                                                i13 = 2;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i14 = i26;
                                                Object obj4 = obj;
                                                obj = objRemove2;
                                                objWrap = obj4;
                                                break;
                                            case 18:
                                                if (0 != 0) {
                                                    System.out.println("startWidgetSection");
                                                }
                                                arrayList2.add(obj);
                                                int length4 = iArr.length;
                                                iArr = iArr;
                                                if (i14 == length4) {
                                                    int[] iArr5 = new int[iArr.length * 2];
                                                    System.arraycopy(iArr, 0, iArr5, 0, iArr.length);
                                                    iArr = iArr5;
                                                }
                                                int i27 = i14 + 1;
                                                iArr[i14] = i12;
                                                i12 = 68;
                                                i13 = 2;
                                                obj = objWrap;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i14 = i27;
                                                objWrap = null;
                                                break;
                                            case 19:
                                                if (0 != 0) {
                                                    System.out.println("endWidgetSection");
                                                }
                                                Object objRemove3 = arrayList2.remove(arrayList2.size() - 1);
                                                int i28 = i14 - 1;
                                                i12 = iArr[i28];
                                                i13 = 2;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i14 = i28;
                                                Object obj5 = obj;
                                                obj = objRemove3;
                                                objWrap = obj5;
                                                break;
                                            case 20:
                                                if (0 != 0) {
                                                    System.out.println("addCell");
                                                }
                                                cellAdd = ((BaseTableLayout) obj).add(k.wrap(objWrap));
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 21:
                                                if (0 != 0) {
                                                    System.out.println("addWidget");
                                                }
                                                k.addChild(obj, k.wrap(objWrap), strTrim);
                                                strTrim = null;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 22:
                                                if (0 != 0) {
                                                    System.out.println("widgetProperty: " + str5 + " = " + arrayList);
                                                }
                                                Object table = obj;
                                                if (obj instanceof BaseTableLayout) {
                                                    table = ((BaseTableLayout) obj).getTable();
                                                }
                                                k.setProperty(baseTableLayout, table, str5, arrayList);
                                                arrayList.clear();
                                                str2 = null;
                                                i11 = i22;
                                                str3 = str4;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 23:
                                                z = false;
                                                objWrap = null;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            case 24:
                                                z = true;
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                            default:
                                                i11 = i22;
                                                str3 = str4;
                                                str2 = str5;
                                                i21 = i23;
                                                i20 = i24;
                                                break;
                                        }
                                    } catch (RuntimeException e6) {
                                        e = e6;
                                        i = i14;
                                        runtimeException = e;
                                        if (i9 < length2) {
                                            i2 = 1;
                                            i3 = 0;
                                            while (i4 < i9) {
                                                if (charArray[i4] == '\n') {
                                                    i2++;
                                                    i3 = i4;
                                                }
                                            }
                                            ParseException parseException4 = new ParseException("Error parsing layout on line " + i2 + ":" + (i9 - i3) + " near: " + new String(charArray, i9, Math.min(64, length2 - i9)), runtimeException);
                                            parseException4.line = i2;
                                            parseException4.column = i9 - i3;
                                            throw parseException4;
                                        }
                                        if (i > 0) {
                                            throw new ParseException("Error parsing layout (possibly an unmatched brace or quote): " + new String(charArray, 0, Math.min(64, length2)), runtimeException);
                                        }
                                    }
                                } else {
                                    i11 = i22;
                                    str3 = str4;
                                    str2 = str5;
                                }
                                break;
                            }
                        }
                        if (i12 == 0) {
                            i13 = 5;
                        } else {
                            i9++;
                            if (i9 != length2) {
                                i13 = 1;
                            }
                        }
                    } catch (RuntimeException e7) {
                        e = e7;
                        i = i14;
                    }
                    break;
                case 2:
                    if (i12 == 0) {
                        i13 = 5;
                    } else {
                        i9++;
                        if (i9 != length2) {
                            i13 = 1;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        i = i14;
        if (i9 < length2) {
            i2 = 1;
            i3 = 0;
            while (i4 < i9) {
                if (charArray[i4] == '\n') {
                    i2++;
                    i3 = i4;
                }
            }
            ParseException parseException5 = new ParseException("Error parsing layout on line " + i2 + ":" + (i9 - i3) + " near: " + new String(charArray, i9, Math.min(64, length2 - i9)), runtimeException);
            parseException5.line = i2;
            parseException5.column = i9 - i3;
            throw parseException5;
        }
        if (i > 0) {
            throw new ParseException("Error parsing layout (possibly an unmatched brace or quote): " + new String(charArray, 0, Math.min(64, length2)), runtimeException);
        }
    }

    private static byte[] init__tableLayout_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1, 9, 1, 11, 1, 12, 1, 13, 1, 14, 1, 15, 1, 16, 1, 17, 1, 18, 1, 19, 1, 20, 1, 21, 1, 22, 1, 23, 2, 0, 2, 2, 0, 11, 2, 0, 13, 2, 1, 3, 2, 1, 4, 2, 1, 6, 2, 1, 8, 2, 1, 9, 2, 1, 12, 2, 1, 22, 2, 1, 24, 2, 2, 3, 2, 2, 4, 2, 2, 6, 2, 2, 8, 2, 2, 9, 2, 2, 22, 2, 3, 14, 2, 3, 15, 2, 3, 16, 2, 3, 23, 2, 4, 14, 2, 4, 15, 2, 4, 16, 2, 4, 23, 2, 5, 0, 2, 5, 14, 2, 5, 15, 2, 5, 16, 2, 5, 23, 2, 6, 14, 2, 6, 15, 2, 6, 16, 2, 6, 23, 2, 7, 0, 2, 7, 14, 2, 7, 16, 2, 7, 23, 2, 8, 14, 2, 8, 16, 2, 8, 23, 2, 9, 14, 2, 9, 15, 2, 9, 16, 2, 9, 18, 2, 9, 23, 2, 10, 21, 2, 11, 12, 2, 16, 0, 2, 20, 0, 2, 20, 14, 2, 20, 15, 2, 20, 16, 2, 20, 18, 2, 20, 23, 2, 21, 14, 2, 21, 16, 2, 21, 17, 2, 21, 18, 2, 21, 19, 2, 21, 23, 2, 22, 14, 2, 22, 16, 2, 22, 19, 2, 22, 23, 3, 0, 1, 12, 3, 0, 1, 24, 3, 1, 3, 14, 3, 1, 3, 15, 3, 1, 3, 16, 3, 1, 3, 23, 3, 1, 4, 14, 3, 1, 4, 15, 3, 1, 4, 16, 3, 1, 4, 23, 3, 1, 6, 14, 3, 1, 6, 15, 3, 1, 6, 16, 3, 1, 6, 23, 3, 1, 8, 14, 3, 1, 8, 16, 3, 1, 8, 23, 3, 1, 9, 14, 3, 1, 9, 15, 3, 1, 9, 16, 3, 1, 9, 18, 3, 1, 9, 23, 3, 1, 22, 14, 3, 1, 22, 16, 3, 1, 22, 19, 3, 1, 22, 23, 3, 2, 3, 14, 3, 2, 3, 15, 3, 2, 3, 16, 3, 2, 3, 23, 3, 2, 4, 14, 3, 2, 4, 15, 3, 2, 4, 16, 3, 2, 4, 23, 3, 2, 6, 14, 3, 2, 6, 15, 3, 2, 6, 16, 3, 2, 6, 23, 3, 2, 8, 14, 3, 2, 8, 16, 3, 2, 8, 23, 3, 2, 9, 14, 3, 2, 9, 15, 3, 2, 9, 16, 3, 2, 9, 18, 3, 2, 9, 23, 3, 2, 22, 14, 3, 2, 22, 16, 3, 2, 22, 19, 3, 2, 22, 23, 3, 10, 21, 14, 3, 10, 21, 16, 3, 10, 21, 18, 3, 10, 21, 19, 3, 10, 21, 23};
    }

    private static short[] init__tableLayout_key_offsets_0() {
        return new short[]{0, 0, 17, 18, 19, 35, 51, 60, 61, 62, 75, 82, 87, 89, 97, 104, 112, 113, 114, 118, 132, 146, 163, 174, 175, 176, 184, 193, 210, 227, 241, 261, 272, 273, 274, 285, 294, 303, 320, 337, 351, 371, 387, 403, 419, 436, 453, 467, 487, 498, 499, 500, 511, 520, 531, 532, 533, 544, 553, 571, 589, 603, 624, 641, 652, 653, 654, 666, 675, 689, 690, 691, 706, 714, 719, 721, 729, 736, 744, 745, 746, 750, 765, 780, 795, 809, 827, 838, 839, 840, 849, 858, 866, 867, 868, 877, 886, 891, 893, 901, 908, 916, 917, 918, 922, 925, 925};
    }

    private static char[] init__tableLayout_trans_keys_0() {
        return new char[]{' ', '\'', '*', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', '(', SignatureVisitor.SUPER, '<', '[', '{', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '(', SignatureVisitor.SUPER, '<', '[', '{', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, '<', '[', '{', '}', '\t', '\r', SignatureVisitor.SUPER, SignatureVisitor.SUPER, ' ', '\'', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '<', '[', '{', '\t', '\r', ' ', ':', ']', '\t', '\r', ':', ']', ' ', '\'', ':', '<', ']', '{', '\t', '\r', ' ', '\'', ':', ']', '{', '\t', '\r', ' ', '\'', ':', '<', ']', '{', '\t', '\r', '\'', '\'', ' ', ']', '\t', '\r', ' ', '\'', ':', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', SignatureVisitor.SUPER, '.', '_', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', ',', '.', '<', '[', '_', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '%', '\'', SignatureVisitor.SUPER, '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', ',', '<', '[', '{', '\t', '\r', '%', '.', '_', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '(', SignatureVisitor.SUPER, ':', '<', '[', '{', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '(', SignatureVisitor.SUPER, ':', '<', '[', '{', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', SignatureVisitor.SUPER, '.', '_', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', '(', ',', SignatureVisitor.SUPER, '.', '<', '[', '_', '{', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '%', '\'', SignatureVisitor.SUPER, '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', '(', ',', SignatureVisitor.SUPER, '<', '[', '{', '}', '\t', '\r', '%', '.', '_', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, ':', '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, ':', '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', SignatureVisitor.SUPER, '.', '_', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', ',', SignatureVisitor.SUPER, '.', '<', '[', '_', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, ':', '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', SignatureVisitor.SUPER, ':', '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', SignatureVisitor.SUPER, '.', '_', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', ',', SignatureVisitor.SUPER, '.', '<', '[', '_', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '%', '\'', SignatureVisitor.SUPER, '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', ',', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '%', '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '%', '\'', SignatureVisitor.SUPER, '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', ',', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '%', '.', '_', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '*', SignatureVisitor.SUPER, ':', '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '*', SignatureVisitor.SUPER, ':', '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', SignatureVisitor.SUPER, '.', '_', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', '*', ',', SignatureVisitor.SUPER, '.', '<', '[', '_', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '*', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '%', '\'', SignatureVisitor.SUPER, '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', '*', ',', SignatureVisitor.SUPER, '<', '[', '{', '|', '}', '\t', '\r', '%', '.', '_', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', ')', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', '(', ')', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', ')', '<', '[', '{', '\t', '\r', ' ', ':', ']', '\t', '\r', ':', ']', ' ', '\'', ':', '<', ']', '{', '\t', '\r', ' ', '\'', ':', ']', '{', '\t', '\r', ' ', '\'', ':', '<', ']', '{', '\t', '\r', '\'', '\'', ' ', ']', '\t', '\r', ' ', '\'', '(', ')', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', ')', ':', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', ')', ':', '<', '[', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', SignatureVisitor.SUPER, '.', '_', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', '%', '\'', ')', ',', '.', '<', '[', '_', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '%', '\'', SignatureVisitor.SUPER, '.', '_', '0', '9', 'A', 'Z', 'a', 'z', '\'', '\'', ' ', '\'', ')', ',', '<', '[', '{', '\t', '\r', '%', '.', '_', '0', '9', 'A', 'Z', 'a', 'z', ' ', '\'', '<', '>', '[', '{', '\t', '\r', '\'', '\'', ' ', '\'', '(', '<', '>', '[', '{', '\t', '\r', ' ', '\'', '(', '<', '>', '[', '{', '\t', '\r', ' ', ':', ']', '\t', '\r', ':', ']', ' ', '\'', ':', '<', ']', '{', '\t', '\r', ' ', '\'', ':', ']', '{', '\t', '\r', ' ', '\'', ':', '<', ']', '{', '\t', '\r', '\'', '\'', ' ', ']', '\t', '\r', ' ', '\t', '\r', 0};
    }

    private static byte[] init__tableLayout_single_lengths_0() {
        return new byte[]{0, 9, 1, 1, 8, 8, 7, 1, 1, 5, 5, 3, 2, 6, 5, 6, 1, 1, 2, 6, 6, 9, 5, 1, 1, 6, 3, 9, 9, 6, 12, 5, 1, 1, 9, 3, 1, 9, 9, 6, 12, 8, 8, 8, 9, 9, 6, 12, 5, 1, 1, 9, 3, 5, 1, 1, 9, 3, 10, 10, 6, 13, 9, 5, 1, 1, 10, 3, 6, 1, 1, 7, 6, 3, 2, 6, 5, 6, 1, 1, 2, 7, 7, 7, 6, 10, 5, 1, 1, 7, 3, 6, 1, 1, 7, 7, 3, 2, 6, 5, 6, 1, 1, 2, 1, 0, 0};
    }

    private static byte[] init__tableLayout_range_lengths_0() {
        return new byte[]{0, 4, 0, 0, 4, 4, 1, 0, 0, 4, 1, 1, 0, 1, 1, 1, 0, 0, 1, 4, 4, 4, 3, 0, 0, 1, 3, 4, 4, 4, 4, 3, 0, 0, 1, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 0, 0, 1, 3, 3, 0, 0, 1, 3, 4, 4, 4, 4, 4, 3, 0, 0, 1, 3, 4, 0, 0, 4, 1, 1, 0, 1, 1, 1, 0, 0, 1, 4, 4, 4, 4, 4, 3, 0, 0, 1, 3, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0};
    }

    private static short[] init__tableLayout_index_offsets_0() {
        return new short[]{0, 0, 14, 16, 18, 31, 44, 53, 55, 57, 67, 74, 79, 82, 90, 97, 105, 107, 109, 113, 124, 135, 149, 158, 160, 162, 170, 177, 191, 205, 216, 233, 242, 244, 246, 257, 264, 270, 284, 298, 309, 326, 339, 352, 365, 379, 393, 404, 421, 430, 432, 434, 445, 452, 461, 463, 465, 476, 483, 498, 513, 524, 542, 556, 565, 567, 569, 581, 588, 599, 601, 603, 615, 623, 628, 631, 639, 646, 654, 656, 658, 662, 674, 686, 698, 709, 724, 733, 735, 737, 746, 753, 761, 763, 765, 774, 783, 788, 791, 799, 806, 814, 816, 818, 822, 825, 826};
    }

    private static byte[] init__tableLayout_trans_targs_0() {
        return new byte[]{1, 2, 36, 7, 4, 11, 62, 42, 104, 1, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, 0, 4, 3, 4, 3, 5, 2, 6, 7, 4, 11, 4, 104, 5, 27, 27, 27, 0, 5, 2, 6, 7, 4, 11, 4, 104, 5, 27, 27, 27, 0, 6, 2, 7, 4, 11, 4, 104, 6, 0, 8, 0, 9, 0, 10, 2, 4, 11, 4, 10, 19, 19, 19, 0, 10, 2, 4, 11, 4, 10, 0, 11, 13, 4, 11, 12, 13, 4, 12, 15, 16, 0, 14, 4, 18, 15, 14, 14, 0, 0, 4, 0, 14, 14, 15, 16, 0, 14, 4, 18, 15, 14, 18, 17, 18, 17, 18, 4, 18, 0, 10, 2, 20, 4, 11, 4, 10, 19, 19, 19, 0, 20, 21, 23, 26, 21, 21, 20, 21, 21, 21, 0, 10, 21, 2, 22, 21, 4, 11, 21, 4, 10, 21, 21, 21, 0, 21, 23, 26, 21, 21, 21, 21, 21, 0, 25, 24, 25, 24, 10, 2, 22, 4, 11, 4, 10, 0, 21, 21, 21, 21, 21, 21, 0, 28, 2, 6, 7, 29, 4, 11, 4, 104, 28, 27, 27, 27, 0, 28, 2, 6, 7, 29, 4, 11, 4, 104, 28, 27, 27, 27, 0, 29, 30, 32, SmileConstants.TOKEN_LITERAL_TRUE, 30, 30, 29, 30, 30, 30, 0, 5, 30, 2, 6, 31, 7, 30, 4, 11, 30, 4, 104, 5, 30, 30, 30, 0, 30, 32, SmileConstants.TOKEN_LITERAL_TRUE, 30, 30, 30, 30, 30, 0, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_NULL, 5, 2, 6, 31, 7, 4, 11, 4, 104, 5, 0, 30, 30, 30, 30, 30, 30, 0, 36, 36, 37, 37, 37, 0, 38, 2, 7, 39, 4, 11, 4, 42, 104, 38, 37, 37, 37, 0, 38, 2, 7, 39, 4, 11, 4, 42, 104, 38, 37, 37, 37, 0, 39, 40, 54, 57, 40, 40, 39, 40, 40, 40, 0, SmileConstants.HEADER_BYTE_2, 40, 2, 53, 7, 40, 4, 11, 40, 4, 42, 104, SmileConstants.HEADER_BYTE_2, 40, 40, 40, 0, SmileConstants.HEADER_BYTE_2, 2, 7, 4, 11, 4, 42, 104, SmileConstants.HEADER_BYTE_2, 37, 37, 37, 0, 43, 2, 7, 4, 11, 4, 42, 104, 43, 44, 44, 44, 0, 43, 2, 7, 4, 11, 4, 42, 104, 43, 44, 44, 44, 0, 45, 2, 7, 46, 4, 11, 4, 42, 104, 45, 44, 44, 44, 0, 45, 2, 7, 46, 4, 11, 4, 42, 104, 45, 44, 44, 44, 0, 46, 47, 49, SmileConstants.TOKEN_KEY_LONG_STRING, 47, 47, 46, 47, 47, 47, 0, 43, 47, 2, 48, 7, 47, 4, 11, 47, 4, 42, 104, 43, 47, 47, 47, 0, 47, 49, SmileConstants.TOKEN_KEY_LONG_STRING, 47, 47, 47, 47, 47, 0, 51, 50, 51, 50, 43, 2, 48, 7, 4, 11, 4, 42, 104, 43, 0, 47, 47, 47, 47, 47, 47, 0, 40, 54, 57, 40, 40, 40, 40, 40, 0, 56, 55, 56, 55, SmileConstants.HEADER_BYTE_2, 2, 53, 7, 4, 11, 4, 42, 104, SmileConstants.HEADER_BYTE_2, 0, 40, 40, 40, 40, 40, 40, 0, 59, 2, 36, 7, 60, 4, 11, 4, 42, 104, 59, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, 0, 59, 2, 36, 7, 60, 4, 11, 4, 42, 104, 59, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, 0, 60, 61, 64, 67, 61, 61, 60, 61, 61, 61, 0, 62, 61, 2, 36, 63, 7, 61, 4, 11, 61, 4, 42, 104, 62, 61, 61, 61, 0, 62, 2, 36, 7, 4, 11, 4, 42, 104, 62, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, SmileConstants.HEADER_BYTE_1, 0, 61, 64, 67, 61, 61, 61, 61, 61, 0, 66, 65, 66, 65, 62, 2, 36, 63, 7, 4, 11, 4, 42, 104, 62, 0, 61, 61, 61, 61, 61, 61, 0, 68, 69, 105, 71, 73, 71, 68, 82, 82, 82, 0, 71, 70, 71, 70, 71, 69, 72, 105, 71, 73, 71, 71, 81, 81, 81, 0, 72, 69, 105, 71, 73, 71, 72, 0, 73, 75, 71, 73, 74, 75, 71, 74, 77, 78, 0, 76, 71, 80, 77, 76, 76, 0, 0, 71, 0, 76, 76, 77, 78, 0, 76, 71, 80, 77, 76, 80, 79, 80, 79, 80, 71, 80, 0, 81, 69, 72, 105, 71, 73, 71, 72, 81, 81, 81, 0, 83, 69, 105, 84, 71, 73, 71, 83, 82, 82, 82, 0, 83, 69, 105, 84, 71, 73, 71, 83, 82, 82, 82, 0, 84, 85, 87, 90, 85, 85, 84, 85, 85, 85, 0, 68, 85, 69, 105, 86, 85, 71, 73, 85, 71, 68, 85, 85, 85, 0, 85, 87, 90, 85, 85, 85, 85, 85, 0, 89, 88, 89, 88, 68, 69, 105, 86, 71, 73, 71, 68, 0, 85, 85, 85, 85, 85, 85, 0, 91, 92, 94, 106, 96, 94, 91, 0, 94, 93, 94, 93, 95, 92, 91, 94, 106, 96, 94, 95, 0, 95, 92, 91, 94, 106, 96, 94, 95, 0, 96, 98, 94, 96, 97, 98, 94, 97, 100, 101, 0, 99, 94, 103, 100, 99, 99, 0, 0, 94, 0, 99, 99, 100, 101, 0, 99, 94, 103, 100, 99, 103, 102, 103, 102, 103, 94, 103, 0, 104, 104, 0, 0, 0, 0};
    }

    private static short[] init__tableLayout_trans_actions_0() {
        return new short[]{0, 0, 0, 0, 31, 45, 0, 0, 29, 0, 1, 1, 1, 0, 53, 1, 25, 0, 39, 39, 206, 39, 203, 209, 197, 200, 39, 194, 194, 194, 0, 0, 0, 35, 0, 31, 45, 27, 29, 0, 1, 1, 1, 0, 0, 0, 0, 31, 45, 27, 29, 0, 0, 0, 0, 0, 0, 15, 15, 155, 158, 152, 15, 149, 149, 149, 0, 0, 0, 31, 45, 27, 0, 0, 1, 246, 242, 1, 1, 77, 71, 0, 1, 0, 0, 191, 23, 27, 1, 1, 21, 0, 0, 188, 0, 21, 0, 50, 0, 0, 191, 188, 27, 50, 1, 53, 1, 25, 0, 0, 23, 0, 0, 65, 65, 3, 302, 306, 298, 65, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 89, 0, 89, 5, 0, 398, 402, 0, 394, 89, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 47, 1, 5, 0, 17, 17, 0, 164, 167, 161, 17, 0, 0, 0, 0, 0, 0, 0, 0, 68, 68, 322, 68, 3, 318, 326, 310, 314, 68, 0, 0, 0, 0, 0, 0, 35, 0, 0, 31, 45, 27, 29, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 92, 0, 92, 418, 5, 92, 0, 414, 422, 0, 406, 410, 92, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 47, 1, 5, 0, 19, 19, 179, 0, 19, 176, 182, 170, 173, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 59, 59, 59, 3, 274, 278, 266, 59, 270, 59, 0, 0, 0, 0, 0, 0, 0, 0, 31, 45, 27, 0, 29, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 83, 0, 83, 5, 83, 0, 370, 374, 0, 362, 83, 366, 83, 0, 0, 0, 0, 0, 0, 0, 31, 45, 27, 0, 29, 0, 1, 1, 1, 0, 11, 11, 11, 131, 134, 125, 11, 128, 11, 122, 122, 122, 0, 0, 0, 0, 31, 45, 27, 0, 29, 0, 1, 1, 1, 0, 62, 62, 62, 3, 290, 294, 282, 62, 286, 62, 0, 0, 0, 0, 0, 0, 0, 0, 31, 45, 27, 0, 29, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 86, 0, 86, 5, 86, 0, 386, 390, 0, 378, 86, 382, 86, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 47, 1, 5, 0, 13, 13, 0, 13, 143, 146, 137, 13, 140, 13, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 47, 1, 5, 0, 9, 9, 0, 9, 116, 119, 110, 9, 113, 9, 0, 0, 0, 0, 0, 0, 0, 0, 56, 56, 56, 56, 3, 258, 262, 250, 56, 254, 56, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 45, 27, 0, 29, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 80, 0, 80, 80, 5, 80, 0, 354, 358, 0, 346, 80, 350, 80, 0, 0, 0, 0, 0, 0, 0, 0, 31, 45, 27, 0, 29, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 47, 1, 5, 0, 7, 7, 7, 0, 7, 104, 107, 98, 7, 101, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 31, 45, 27, 0, 1, 1, 1, 0, 53, 1, 25, 0, 0, 41, 221, 224, 215, 227, 212, 0, 1, 1, 1, 0, 0, 0, 37, 31, 45, 27, 0, 0, 1, 246, 242, 1, 1, 77, 71, 0, 1, 0, 0, 191, 23, 27, 1, 1, 21, 0, 0, 188, 0, 21, 0, 50, 0, 0, 191, 188, 27, 50, 1, 53, 1, 25, 0, 0, 23, 0, 0, 0, 185, 450, 454, 446, 458, 442, 185, 0, 0, 0, 0, 74, 74, 338, 3, 334, 342, 330, 74, 0, 0, 0, 0, 0, 0, 37, 0, 31, 45, 27, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 95, 0, 95, 434, 5, 0, 430, 438, 0, 426, 95, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 47, 1, 5, 0, 43, 43, 236, 0, 233, 239, 230, 43, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 33, 45, 27, 0, 0, 53, 1, 25, 0, 41, 41, 221, 215, 218, 227, 212, 41, 0, 0, 0, 35, 31, 33, 45, 27, 0, 0, 1, 246, 242, 1, 1, 77, 71, 0, 1, 0, 0, 191, 23, 27, 1, 1, 21, 0, 0, 188, 0, 21, 0, 50, 0, 0, 191, 188, 27, 50, 1, 53, 1, 25, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
