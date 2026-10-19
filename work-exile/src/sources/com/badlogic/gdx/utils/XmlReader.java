package com.badlogic.gdx.utils;

import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class XmlReader {
    static final int xml_en_elementBody = 15;
    static final int xml_en_main = 1;
    static final int xml_error = 0;
    static final int xml_first_final = 34;
    static final int xml_start = 1;
    private Element current;
    private Element root;
    private static final byte[] _xml_actions = init__xml_actions_0();
    private static final byte[] _xml_key_offsets = init__xml_key_offsets_0();
    private static final char[] _xml_trans_keys = init__xml_trans_keys_0();
    private static final byte[] _xml_single_lengths = init__xml_single_lengths_0();
    private static final byte[] _xml_range_lengths = init__xml_range_lengths_0();
    private static final short[] _xml_index_offsets = init__xml_index_offsets_0();
    private static final byte[] _xml_indicies = init__xml_indicies_0();
    private static final byte[] _xml_trans_targs = init__xml_trans_targs_0();
    private static final byte[] _xml_trans_actions = init__xml_trans_actions_0();
    private final Array<Element> elements = new Array<>(8);
    private final StringBuilder textBuffer = new StringBuilder(64);

    public Element parse(String xml) {
        char[] data = xml.toCharArray();
        return parse(data, 0, data.length);
    }

    public Element parse(Reader reader) throws IOException {
        char[] data = new char[1024];
        int offset = 0;
        while (true) {
            int length = reader.read(data, offset, data.length - offset);
            if (length != -1) {
                if (length == 0) {
                    char[] newData = new char[data.length * 2];
                    System.arraycopy(data, 0, newData, 0, data.length);
                    data = newData;
                } else {
                    offset += length;
                }
            } else {
                return parse(data, 0, offset);
            }
        }
    }

    public Element parse(InputStream input) throws IOException {
        return parse(new InputStreamReader(input, "ISO-8859-1"));
    }

    public Element parse(FileHandle file) throws IOException {
        return parse(file.read());
    }

    /* JADX WARN: Code duplicated, block: B:100:0x0223 A[LOOP:5: B:90:0x01fb->B:100:0x0223, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:104:0x0230  */
    /* JADX WARN: Code duplicated, block: B:106:0x023c A[LOOP:7: B:102:0x022a->B:106:0x023c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:111:0x0283  */
    /* JADX WARN: Code duplicated, block: B:113:0x0288  */
    /* JADX WARN: Code duplicated, block: B:115:0x028e  */
    /* JADX WARN: Code duplicated, block: B:117:0x02bf  */
    /* JADX WARN: Code duplicated, block: B:122:0x02e4  */
    /* JADX WARN: Code duplicated, block: B:139:0x02d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:140:0x02d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:141:0x01b9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:142:0x01c3 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:143:0x01cd A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:146:0x02dc A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:147:0x02d9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:148:0x0010 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:158:0x0050 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:159:0x00a0 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:160:0x009d A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:161:0x00af A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:165:0x0088 A[DONT_GENERATE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:168:0x0088 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:178:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:180:0x0208 A[DONT_GENERATE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:181:0x0286 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:182:0x0226 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:183:0x0220 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:187:0x0372 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:188:0x023f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:192:0x0061 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:193:0x00cb A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:194:0x00c8 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:195:0x00dc A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:20:0x0049  */
    /* JADX WARN: Code duplicated, block: B:23:0x0052 A[PHI: r7 r13
      0x0052: PHI (r7v1 int) = (r7v0 int), (r7v2 int) binds: [B:19:0x0047, B:22:0x0050] A[DONT_GENERATE, DONT_INLINE]
      0x0052: PHI (r13v2 int) = (r13v13 int), (r13v14 int) binds: [B:19:0x0047, B:22:0x0050] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:25:0x0058  */
    /* JADX WARN: Code duplicated, block: B:30:0x0070  */
    /* JADX WARN: Code duplicated, block: B:33:0x007f  */
    /* JADX WARN: Code duplicated, block: B:36:0x008b  */
    /* JADX WARN: Code duplicated, block: B:41:0x00ac  */
    /* JADX WARN: Code duplicated, block: B:43:0x00b4  */
    /* JADX WARN: Code duplicated, block: B:48:0x00d9  */
    /* JADX WARN: Code duplicated, block: B:50:0x00e3  */
    /* JADX WARN: Code duplicated, block: B:51:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:81:0x019c A[LOOP:4: B:79:0x0192->B:81:0x019c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:85:0x01cb A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:87:0x01d2  */
    /* JADX WARN: Code duplicated, block: B:88:0x01e1  */
    /* JADX WARN: Code duplicated, block: B:89:0x01f9  */
    /* JADX WARN: Code duplicated, block: B:92:0x0201  */
    /* JADX WARN: Code duplicated, block: B:97:0x0214  */
    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x000d. Please report as an issue. */
    public Element parse(char[] cArr, int i, int i2) {
        int i3;
        short s;
        byte b;
        int i4;
        byte b2;
        int i5;
        int i6;
        int i7;
        int i8;
        byte b3;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        boolean z;
        int i14;
        int i15;
        String str;
        String strEntity;
        char c;
        int i16;
        int i17;
        int i18;
        int i19 = i;
        int i20 = 0;
        String str2 = null;
        boolean z2 = false;
        byte b4 = 1;
        int i21 = 0;
        while (true) {
            switch (i21) {
                case 0:
                    if (i19 == i2) {
                        i21 = 4;
                    } else if (b4 == 0) {
                        i21 = 5;
                    } else {
                        i3 = _xml_key_offsets[b4];
                        s = _xml_index_offsets[b4];
                        b = _xml_single_lengths[b4];
                        i4 = s;
                        if (b > 0) {
                            i16 = i3;
                            i17 = (i3 + b) - 1;
                            while (true) {
                                if (i17 < i16) {
                                    i18 = i16 + ((i17 - i16) >> 1);
                                    if (cArr[i19] < _xml_trans_keys[i18]) {
                                        i17 = i18 - 1;
                                    } else if (cArr[i19] > _xml_trans_keys[i18]) {
                                        i16 = i18 + 1;
                                    } else {
                                        i8 = s + (i18 - i3);
                                    }
                                } else {
                                    i3 += b;
                                    i4 = s + b;
                                    b2 = _xml_range_lengths[b4];
                                    i8 = i4;
                                    if (b2 > 0) {
                                        i5 = i3;
                                        i6 = ((b2 << 1) + i3) - 2;
                                        while (true) {
                                            if (i6 < i5) {
                                                i7 = i5 + (((i6 - i5) >> 1) & (-2));
                                                if (cArr[i19] < _xml_trans_keys[i7]) {
                                                    i6 = i7 - 2;
                                                } else if (cArr[i19] > _xml_trans_keys[i7 + 1]) {
                                                    i5 = i7 + 2;
                                                } else {
                                                    i8 = i4 + ((i7 - i3) >> 1);
                                                }
                                            } else {
                                                i8 = i4 + b2;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            b2 = _xml_range_lengths[b4];
                            i8 = i4;
                            if (b2 > 0) {
                                i5 = i3;
                                i6 = ((b2 << 1) + i3) - 2;
                                while (true) {
                                    if (i6 < i5) {
                                        i7 = i5 + (((i6 - i5) >> 1) & (-2));
                                        if (cArr[i19] < _xml_trans_keys[i7]) {
                                            i6 = i7 - 2;
                                        } else if (cArr[i19] > _xml_trans_keys[i7 + 1]) {
                                            i5 = i7 + 2;
                                        } else {
                                            i8 = i4 + ((i7 - i3) >> 1);
                                        }
                                    } else {
                                        i8 = i4 + b2;
                                    }
                                }
                            }
                        }
                        b3 = _xml_indicies[i8];
                        b4 = _xml_trans_targs[b3];
                        if (_xml_trans_actions[b3] != 0) {
                            byte b5 = _xml_trans_actions[b3];
                            i9 = b5 + 1;
                            i10 = _xml_actions[b5];
                            while (true) {
                                i11 = i10 - 1;
                                if (i10 > 0) {
                                    int i22 = i9 + 1;
                                    switch (_xml_actions[i9]) {
                                        case 0:
                                            i20 = i19;
                                            break;
                                        case 1:
                                            c = cArr[i20];
                                            if (c != '?' || c == '!') {
                                                if (cArr[i20 + 1] != '[' && cArr[i20 + 2] == 'C' && cArr[i20 + 3] == 'D' && cArr[i20 + 4] == 'A' && cArr[i20 + 5] == 'T' && cArr[i20 + 6] == 'A' && cArr[i20 + 7] == '[') {
                                                    i20 += 8;
                                                    i19 = i20 + 2;
                                                    while (true) {
                                                        if (cArr[i19 - 2] != ']' || cArr[i19 - 1] != ']' || cArr[i19] != '>') {
                                                            i19++;
                                                        } else {
                                                            text(new String(cArr, i20, (i19 - i20) - 2));
                                                        }
                                                    }
                                                } else {
                                                    while (cArr[i19] != '>') {
                                                        i19++;
                                                    }
                                                }
                                                b4 = 15;
                                                i21 = 2;
                                            } else {
                                                z2 = true;
                                                open(new String(cArr, i20, i19 - i20));
                                            }
                                            break;
                                        case 2:
                                            z2 = false;
                                            close();
                                            b4 = 15;
                                            i21 = 2;
                                            continue;
                                            continue;
                                        case 3:
                                            close();
                                            b4 = 15;
                                            i21 = 2;
                                            continue;
                                            continue;
                                        case 4:
                                            if (z2) {
                                                b4 = 15;
                                                i21 = 2;
                                            }
                                            break;
                                        case 5:
                                            str2 = new String(cArr, i20, i19 - i20);
                                            break;
                                        case 6:
                                            attribute(str2, new String(cArr, i20, i19 - i20));
                                            break;
                                        case 7:
                                            for (i12 = i19; i12 != i20; i12--) {
                                                switch (cArr[i12 - 1]) {
                                                    case '\t':
                                                    case '\n':
                                                    case '\r':
                                                    case ' ':
                                                        break;
                                                }
                                                i13 = i20;
                                                z = false;
                                                while (true) {
                                                    i14 = i13;
                                                    if (i14 != i12) {
                                                        i13 = i14 + 1;
                                                        if (cArr[i14] != '&') {
                                                            while (true) {
                                                                i15 = i13;
                                                                if (i15 != i12) {
                                                                    i13 = i15 + 1;
                                                                    if (cArr[i15] != ';') {
                                                                        this.textBuffer.append(cArr, i20, (i13 - i20) - 1);
                                                                        str = new String(cArr, i13, (i13 - i13) - 1);
                                                                        strEntity = entity(str);
                                                                        StringBuilder sb = this.textBuffer;
                                                                        if (strEntity == null) {
                                                                            strEntity = str;
                                                                        }
                                                                        sb.append(strEntity);
                                                                        i20 = i13;
                                                                        z = true;
                                                                    }
                                                                } else {
                                                                    i13 = i15;
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (z) {
                                                            if (i20 < i12) {
                                                                this.textBuffer.append(cArr, i20, i12 - i20);
                                                            }
                                                            text(this.textBuffer.toString());
                                                            this.textBuffer.setLength(0);
                                                        } else {
                                                            text(new String(cArr, i20, i12 - i20));
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            i13 = i20;
                                            z = false;
                                            while (true) {
                                                i14 = i13;
                                                if (i14 != i12) {
                                                    i13 = i14 + 1;
                                                    if (cArr[i14] != '&') {
                                                        while (true) {
                                                            i15 = i13;
                                                            if (i15 != i12) {
                                                                i13 = i15 + 1;
                                                                if (cArr[i15] != ';') {
                                                                    this.textBuffer.append(cArr, i20, (i13 - i20) - 1);
                                                                    str = new String(cArr, i13, (i13 - i13) - 1);
                                                                    strEntity = entity(str);
                                                                    StringBuilder sb2 = this.textBuffer;
                                                                    if (strEntity == null) {
                                                                        strEntity = str;
                                                                    }
                                                                    sb2.append(strEntity);
                                                                    i20 = i13;
                                                                    z = true;
                                                                }
                                                            } else {
                                                                i13 = i15;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (z) {
                                                        if (i20 < i12) {
                                                            this.textBuffer.append(cArr, i20, i12 - i20);
                                                        }
                                                        text(this.textBuffer.toString());
                                                        this.textBuffer.setLength(0);
                                                    } else {
                                                        text(new String(cArr, i20, i12 - i20));
                                                    }
                                                    break;
                                                }
                                            }
                                            break;
                                    }
                                    i10 = i11;
                                    i9 = i22;
                                }
                            }
                        }
                        if (b4 == 0) {
                            i21 = 5;
                        } else {
                            i19++;
                            if (i19 != i2) {
                                i21 = 1;
                            }
                        }
                    }
                    break;
                case 1:
                    i3 = _xml_key_offsets[b4];
                    s = _xml_index_offsets[b4];
                    b = _xml_single_lengths[b4];
                    i4 = s;
                    if (b > 0) {
                        i16 = i3;
                        i17 = (i3 + b) - 1;
                        while (true) {
                            if (i17 < i16) {
                                i18 = i16 + ((i17 - i16) >> 1);
                                if (cArr[i19] < _xml_trans_keys[i18]) {
                                    i17 = i18 - 1;
                                } else if (cArr[i19] > _xml_trans_keys[i18]) {
                                    i16 = i18 + 1;
                                } else {
                                    i8 = s + (i18 - i3);
                                }
                            } else {
                                i3 += b;
                                i4 = s + b;
                                b2 = _xml_range_lengths[b4];
                                i8 = i4;
                                if (b2 > 0) {
                                    i5 = i3;
                                    i6 = ((b2 << 1) + i3) - 2;
                                    while (true) {
                                        if (i6 < i5) {
                                            i7 = i5 + (((i6 - i5) >> 1) & (-2));
                                            if (cArr[i19] < _xml_trans_keys[i7]) {
                                                i6 = i7 - 2;
                                            } else if (cArr[i19] > _xml_trans_keys[i7 + 1]) {
                                                i5 = i7 + 2;
                                            } else {
                                                i8 = i4 + ((i7 - i3) >> 1);
                                            }
                                        } else {
                                            i8 = i4 + b2;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        b2 = _xml_range_lengths[b4];
                        i8 = i4;
                        if (b2 > 0) {
                            i5 = i3;
                            i6 = ((b2 << 1) + i3) - 2;
                            while (true) {
                                if (i6 < i5) {
                                    i7 = i5 + (((i6 - i5) >> 1) & (-2));
                                    if (cArr[i19] < _xml_trans_keys[i7]) {
                                        i6 = i7 - 2;
                                    } else if (cArr[i19] > _xml_trans_keys[i7 + 1]) {
                                        i5 = i7 + 2;
                                    } else {
                                        i8 = i4 + ((i7 - i3) >> 1);
                                    }
                                } else {
                                    i8 = i4 + b2;
                                }
                            }
                        }
                    }
                    b3 = _xml_indicies[i8];
                    b4 = _xml_trans_targs[b3];
                    if (_xml_trans_actions[b3] != 0) {
                        byte b6 = _xml_trans_actions[b3];
                        i9 = b6 + 1;
                        i10 = _xml_actions[b6];
                        while (true) {
                            i11 = i10 - 1;
                            if (i10 > 0) {
                                int i23 = i9 + 1;
                                switch (_xml_actions[i9]) {
                                    case 0:
                                        i20 = i19;
                                        break;
                                    case 1:
                                        c = cArr[i20];
                                        if (c != '?') {
                                        }
                                        if (cArr[i20 + 1] != '[') {
                                            while (cArr[i19] != '>') {
                                                i19++;
                                            }
                                        } else {
                                            while (cArr[i19] != '>') {
                                                i19++;
                                            }
                                        }
                                        b4 = 15;
                                        i21 = 2;
                                        break;
                                    case 2:
                                        z2 = false;
                                        close();
                                        b4 = 15;
                                        i21 = 2;
                                        continue;
                                        continue;
                                    case 3:
                                        close();
                                        b4 = 15;
                                        i21 = 2;
                                        continue;
                                        continue;
                                    case 4:
                                        if (z2) {
                                            b4 = 15;
                                            i21 = 2;
                                        }
                                        break;
                                    case 5:
                                        str2 = new String(cArr, i20, i19 - i20);
                                        break;
                                    case 6:
                                        attribute(str2, new String(cArr, i20, i19 - i20));
                                        break;
                                    case 7:
                                        while (i12 != i20) {
                                            switch (cArr[i12 - 1]) {
                                                case '\t':
                                                case '\n':
                                                case '\r':
                                                case ' ':
                                                    break;
                                            }
                                            i13 = i20;
                                            z = false;
                                            while (true) {
                                                i14 = i13;
                                                if (i14 != i12) {
                                                    i13 = i14 + 1;
                                                    if (cArr[i14] != '&') {
                                                        while (true) {
                                                            i15 = i13;
                                                            if (i15 != i12) {
                                                                i13 = i15 + 1;
                                                                if (cArr[i15] != ';') {
                                                                    this.textBuffer.append(cArr, i20, (i13 - i20) - 1);
                                                                    str = new String(cArr, i13, (i13 - i13) - 1);
                                                                    strEntity = entity(str);
                                                                    StringBuilder sb3 = this.textBuffer;
                                                                    if (strEntity == null) {
                                                                        strEntity = str;
                                                                    }
                                                                    sb3.append(strEntity);
                                                                    i20 = i13;
                                                                    z = true;
                                                                }
                                                            } else {
                                                                i13 = i15;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (z) {
                                                        if (i20 < i12) {
                                                            this.textBuffer.append(cArr, i20, i12 - i20);
                                                        }
                                                        text(this.textBuffer.toString());
                                                        this.textBuffer.setLength(0);
                                                    } else {
                                                        text(new String(cArr, i20, i12 - i20));
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        i13 = i20;
                                        z = false;
                                        while (true) {
                                            i14 = i13;
                                            if (i14 != i12) {
                                                i13 = i14 + 1;
                                                if (cArr[i14] != '&') {
                                                    while (true) {
                                                        i15 = i13;
                                                        if (i15 != i12) {
                                                            i13 = i15 + 1;
                                                            if (cArr[i15] != ';') {
                                                                this.textBuffer.append(cArr, i20, (i13 - i20) - 1);
                                                                str = new String(cArr, i13, (i13 - i13) - 1);
                                                                strEntity = entity(str);
                                                                StringBuilder sb4 = this.textBuffer;
                                                                if (strEntity == null) {
                                                                    strEntity = str;
                                                                }
                                                                sb4.append(strEntity);
                                                                i20 = i13;
                                                                z = true;
                                                            }
                                                        } else {
                                                            i13 = i15;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (z) {
                                                    if (i20 < i12) {
                                                        this.textBuffer.append(cArr, i20, i12 - i20);
                                                    }
                                                    text(this.textBuffer.toString());
                                                    this.textBuffer.setLength(0);
                                                } else {
                                                    text(new String(cArr, i20, i12 - i20));
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                }
                                i10 = i11;
                                i9 = i23;
                            }
                            break;
                        }
                    }
                    if (b4 == 0) {
                        i21 = 5;
                    } else {
                        i19++;
                        if (i19 != i2) {
                            i21 = 1;
                        }
                    }
                    break;
                case 2:
                    if (b4 == 0) {
                        i21 = 5;
                    } else {
                        i19++;
                        if (i19 != i2) {
                            i21 = 1;
                        }
                    }
                    break;
            }
        }
        if (i19 < i2) {
            int i24 = 1;
            for (int i25 = 0; i25 < i19; i25++) {
                if (cArr[i25] == '\n') {
                    i24++;
                }
            }
            throw new IllegalArgumentException("Error parsing XML on line " + i24 + " near: " + new String(cArr, i19, Math.min(32, i2 - i19)));
        }
        if (this.elements.size != 0) {
            Element elementPeek = this.elements.peek();
            this.elements.clear();
            throw new IllegalArgumentException("Error parsing XML, unclosed element: " + elementPeek.getName());
        }
        Element element = this.root;
        this.root = null;
        return element;
    }

    private static byte[] init__xml_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 2, 0, 6, 2, 1, 4, 2, 2, 4};
    }

    private static byte[] init__xml_key_offsets_0() {
        return new byte[]{0, 0, 4, 9, 14, 20, 26, 30, SmileConstants.TOKEN_LITERAL_TRUE, 36, 37, 42, 46, 50, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 56, 57, 62, 67, 73, 79, 83, 88, 89, 90, 95, 99, 103, 104, 108, 109, 110, 111, 112, 115};
    }

    private static char[] init__xml_trans_keys_0() {
        return new char[]{' ', '<', '\t', '\r', ' ', '/', '>', '\t', '\r', ' ', '/', '>', '\t', '\r', ' ', '/', SignatureVisitor.INSTANCEOF, '>', '\t', '\r', ' ', '/', SignatureVisitor.INSTANCEOF, '>', '\t', '\r', ' ', SignatureVisitor.INSTANCEOF, '\t', '\r', ' ', '\"', '\'', '\t', '\r', '\"', '\"', ' ', '/', '>', '\t', '\r', ' ', '>', '\t', '\r', ' ', '>', '\t', '\r', '\'', '\'', ' ', '<', '\t', '\r', '<', ' ', '/', '>', '\t', '\r', ' ', '/', '>', '\t', '\r', ' ', '/', SignatureVisitor.INSTANCEOF, '>', '\t', '\r', ' ', '/', SignatureVisitor.INSTANCEOF, '>', '\t', '\r', ' ', SignatureVisitor.INSTANCEOF, '\t', '\r', ' ', '\"', '\'', '\t', '\r', '\"', '\"', ' ', '/', '>', '\t', '\r', ' ', '>', '\t', '\r', ' ', '>', '\t', '\r', '<', ' ', '/', '\t', '\r', '>', '>', '\'', '\'', ' ', '\t', '\r', 0};
    }

    private static byte[] init__xml_single_lengths_0() {
        return new byte[]{0, 2, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 1, 2, 1, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 2, 1, 1, 1, 1, 1, 0};
    }

    private static byte[] init__xml_range_lengths_0() {
        return new byte[]{0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0};
    }

    private static short[] init__xml_index_offsets_0() {
        return new short[]{0, 0, 4, 9, 14, 20, 26, 30, 35, 37, 39, 44, 48, 52, 54, 56, 60, 62, 67, 72, 78, 84, 88, 93, 95, 97, 102, 106, 110, 112, 116, 118, 120, 122, 124, 127};
    }

    private static byte[] init__xml_indicies_0() {
        return new byte[]{0, 2, 0, 1, 2, 1, 1, 2, 3, 5, 6, 7, 5, 4, 9, 10, 1, 11, 9, 8, 13, 1, 14, 1, 13, 12, 15, 16, 15, 1, 16, 17, 18, 16, 1, 20, 19, 22, 21, 9, 10, 11, 9, 1, 23, 24, 23, 1, 25, 11, 25, 1, 20, 26, 22, 27, 29, 30, 29, 28, 32, 31, 30, SmileConstants.TOKEN_LITERAL_FALSE, 1, 30, SmileConstants.TOKEN_LITERAL_NULL, 36, 37, 38, 36, SmileConstants.TOKEN_LITERAL_TRUE, 40, SmileConstants.HEADER_BYTE_2, 1, 42, 40, 39, 44, 1, 45, 1, 44, 43, 46, 47, 46, 1, 47, 48, 49, 47, 1, 51, 50, 53, SmileConstants.TOKEN_KEY_LONG_STRING, 40, SmileConstants.HEADER_BYTE_2, 42, 40, 1, 54, 55, 54, 1, 56, 42, 56, 1, 57, 1, 57, SmileConstants.TOKEN_LITERAL_FALSE, 57, 1, 1, SmileConstants.HEADER_BYTE_1, 59, SmileConstants.HEADER_BYTE_1, 51, 60, 53, 61, 62, 62, 1, 1, 0};
    }

    private static byte[] init__xml_trans_targs_0() {
        return new byte[]{1, 0, 2, 3, 3, 4, 11, SmileConstants.TOKEN_LITERAL_FALSE, 5, 4, 11, SmileConstants.TOKEN_LITERAL_FALSE, 5, 6, 7, 6, 7, 8, 13, 9, 10, 9, 10, 12, SmileConstants.TOKEN_LITERAL_FALSE, 12, 14, 14, 16, 15, 17, 16, 17, 18, 30, 18, 19, 26, 28, 20, 19, 26, 28, 20, 21, 22, 21, 22, 23, 32, 24, 25, 24, 25, 27, 28, 27, 29, 31, SmileConstants.TOKEN_LITERAL_TRUE, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE};
    }

    private static byte[] init__xml_trans_actions_0() {
        return new byte[]{0, 0, 0, 1, 0, 3, 3, 20, 1, 0, 0, 9, 0, 11, 11, 0, 0, 0, 0, 1, 17, 0, 13, 5, 23, 0, 1, 0, 1, 0, 0, 0, 15, 1, 0, 0, 3, 3, 20, 1, 0, 0, 9, 0, 11, 11, 0, 0, 0, 0, 1, 17, 0, 13, 5, 23, 0, 0, 0, 7, 1, 0, 0};
    }

    protected void open(String name) {
        Element child = new Element(name, this.current);
        Element parent = this.current;
        if (parent != null) {
            parent.addChild(child);
        }
        this.elements.add(child);
        this.current = child;
    }

    protected void attribute(String name, String value) {
        this.current.setAttribute(name, value);
    }

    protected String entity(String name) {
        if (name.equals("lt")) {
            return "<";
        }
        if (name.equals("gt")) {
            return ">";
        }
        if (name.equals("amp")) {
            return "&";
        }
        if (name.equals("apos")) {
            return "'";
        }
        if (name.equals("quot")) {
            return "\"";
        }
        return null;
    }

    protected void text(String text) {
        String existing = this.current.getText();
        Element element = this.current;
        if (existing != null) {
            text = existing + text;
        }
        element.setText(text);
    }

    protected void close() {
        this.root = this.elements.pop();
        this.current = this.elements.size > 0 ? this.elements.peek() : null;
    }

    public static class Element {
        private ObjectMap<String, String> attributes;
        private Array<Element> children;
        private final String name;
        private Element parent;
        private String text;

        public Element(String name, Element parent) {
            this.name = name;
            this.parent = parent;
        }

        public String getName() {
            return this.name;
        }

        public ObjectMap<String, String> getAttributes() {
            return this.attributes;
        }

        public String getAttribute(String name) {
            if (this.attributes == null) {
                throw new GdxRuntimeException("Element " + name + " doesn't have attribute: " + name);
            }
            String value = this.attributes.get(name);
            if (value == null) {
                throw new GdxRuntimeException("Element " + name + " doesn't have attribute: " + name);
            }
            return value;
        }

        public String getAttribute(String name, String defaultValue) {
            String value;
            return (this.attributes == null || (value = this.attributes.get(name)) == null) ? defaultValue : value;
        }

        public void setAttribute(String name, String value) {
            if (this.attributes == null) {
                this.attributes = new ObjectMap<>(8);
            }
            this.attributes.put(name, value);
        }

        public int getChildCount() {
            if (this.children == null) {
                return 0;
            }
            return this.children.size;
        }

        public Element getChild(int i) {
            if (this.children == null) {
                throw new GdxRuntimeException("Element has no children: " + this.name);
            }
            return this.children.get(i);
        }

        public void addChild(Element element) {
            if (this.children == null) {
                this.children = new Array<>(8);
            }
            this.children.add(element);
        }

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void removeChild(int index) {
            if (this.children != null) {
                this.children.removeIndex(index);
            }
        }

        public void removeChild(Element child) {
            if (this.children != null) {
                this.children.removeValue(child, true);
            }
        }

        public void remove() {
            this.parent.removeChild(this);
        }

        public Element getParent() {
            return this.parent;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            StringBuilder buffer = new StringBuilder(128);
            buffer.append(indent);
            buffer.append('<');
            buffer.append(this.name);
            if (this.attributes != null) {
                for (ObjectMap.Entry<String, String> entry : this.attributes.entries()) {
                    buffer.append(' ');
                    buffer.append(entry.key);
                    buffer.append("=\"");
                    buffer.append(entry.value);
                    buffer.append('\"');
                }
            }
            if (this.children == null && (this.text == null || this.text.length() == 0)) {
                buffer.append("/>");
            } else {
                buffer.append(">\n");
                String childIndent = indent + '\t';
                if (this.text != null && this.text.length() > 0) {
                    buffer.append(childIndent);
                    buffer.append(this.text);
                    buffer.append('\n');
                }
                if (this.children != null) {
                    for (Element child : this.children) {
                        buffer.append(child.toString(childIndent));
                        buffer.append('\n');
                    }
                }
                buffer.append(indent);
                buffer.append("</");
                buffer.append(this.name);
                buffer.append('>');
            }
            return buffer.toString();
        }

        public Element getChildByName(String name) {
            if (this.children == null) {
                return null;
            }
            for (int i = 0; i < this.children.size; i++) {
                Element element = this.children.get(i);
                if (element.name.equals(name)) {
                    return element;
                }
            }
            return null;
        }

        public Element getChildByNameRecursive(String name) {
            if (this.children == null) {
                return null;
            }
            for (int i = 0; i < this.children.size; i++) {
                Element element = this.children.get(i);
                if (!element.name.equals(name)) {
                    Element found = element.getChildByNameRecursive(name);
                    if (found != null) {
                        return found;
                    }
                } else {
                    return element;
                }
            }
            return null;
        }

        public Array<Element> getChildrenByName(String name) {
            Array<Element> result = new Array<>();
            if (this.children != null) {
                for (int i = 0; i < this.children.size; i++) {
                    Element child = this.children.get(i);
                    if (child.name.equals(name)) {
                        result.add(child);
                    }
                }
            }
            return result;
        }

        public Array<Element> getChildrenByNameRecursively(String name) {
            Array<Element> result = new Array<>();
            getChildrenByNameRecursively(name, result);
            return result;
        }

        private void getChildrenByNameRecursively(String name, Array<Element> result) {
            if (this.children != null) {
                for (int i = 0; i < this.children.size; i++) {
                    Element child = this.children.get(i);
                    if (child.name.equals(name)) {
                        result.add(child);
                    }
                    child.getChildrenByNameRecursively(name, result);
                }
            }
        }

        public float getFloatAttribute(String name) {
            return Float.parseFloat(getAttribute(name));
        }

        public float getFloatAttribute(String name, float defaultValue) {
            String value = getAttribute(name, null);
            if (value == null) {
                return defaultValue;
            }
            float defaultValue2 = Float.parseFloat(value);
            return defaultValue2;
        }

        public int getIntAttribute(String name) {
            return Integer.parseInt(getAttribute(name));
        }

        public int getIntAttribute(String name, int defaultValue) {
            String value = getAttribute(name, null);
            if (value == null) {
                return defaultValue;
            }
            int defaultValue2 = Integer.parseInt(value);
            return defaultValue2;
        }

        public boolean getBooleanAttribute(String name) {
            return Boolean.parseBoolean(getAttribute(name));
        }

        public boolean getBooleanAttribute(String name, boolean defaultValue) {
            String value = getAttribute(name, null);
            if (value == null) {
                return defaultValue;
            }
            boolean defaultValue2 = Boolean.parseBoolean(value);
            return defaultValue2;
        }

        public String get(String name) {
            String value = get(name, null);
            if (value == null) {
                throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name);
            }
            return value;
        }

        public String get(String name, String defaultValue) {
            String value;
            String value2;
            if (this.attributes == null || (value2 = this.attributes.get(name)) == null) {
                Element child = getChildByName(name);
                return (child == null || (value = child.getText()) == null) ? defaultValue : value;
            }
            return value2;
        }

        public int getInt(String name) {
            String value = get(name, null);
            if (value == null) {
                throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name);
            }
            return Integer.parseInt(value);
        }

        public int getInt(String name, int defaultValue) {
            String value = get(name, null);
            if (value == null) {
                return defaultValue;
            }
            int defaultValue2 = Integer.parseInt(value);
            return defaultValue2;
        }

        public float getFloat(String name) {
            String value = get(name, null);
            if (value == null) {
                throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name);
            }
            return Float.parseFloat(value);
        }

        public float getFloat(String name, float defaultValue) {
            String value = get(name, null);
            if (value == null) {
                return defaultValue;
            }
            float defaultValue2 = Float.parseFloat(value);
            return defaultValue2;
        }

        public boolean getBoolean(String name) {
            String value = get(name, null);
            if (value == null) {
                throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name);
            }
            return Boolean.parseBoolean(value);
        }

        public boolean getBoolean(String name, boolean defaultValue) {
            String value = get(name, null);
            if (value == null) {
                return defaultValue;
            }
            boolean defaultValue2 = Boolean.parseBoolean(value);
            return defaultValue2;
        }
    }
}
