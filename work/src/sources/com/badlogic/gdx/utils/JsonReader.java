package com.badlogic.gdx.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: loaded from: classes.dex */
public class JsonReader {
    static final int json_en_array = 49;
    static final int json_en_main = 1;
    static final int json_en_object = 9;
    static final int json_error = 0;
    static final int json_first_final = 76;
    static final int json_start = 1;
    private Object current;
    private final Array elements = new Array(8);
    private Object root;
    private static final byte[] _json_actions = init__json_actions_0();
    private static final short[] _json_key_offsets = init__json_key_offsets_0();
    private static final char[] _json_trans_keys = init__json_trans_keys_0();
    private static final byte[] _json_single_lengths = init__json_single_lengths_0();
    private static final byte[] _json_range_lengths = init__json_range_lengths_0();
    private static final short[] _json_index_offsets = init__json_index_offsets_0();
    private static final byte[] _json_trans_targs = init__json_trans_targs_0();
    private static final byte[] _json_trans_actions = init__json_trans_actions_0();
    private static final byte[] _json_eof_actions = init__json_eof_actions_0();

    public Object parse(String json) {
        char[] data = json.toCharArray();
        return parse(data, 0, data.length);
    }

    public Object parse(Reader reader) {
        try {
            try {
                char[] data = new char[1024];
                int offset = 0;
                while (true) {
                    int length = reader.read(data, offset, data.length - offset);
                    if (length == -1) {
                        break;
                    }
                    if (length == 0) {
                        char[] newData = new char[data.length * 2];
                        System.arraycopy(data, 0, newData, 0, data.length);
                        data = newData;
                    } else {
                        offset += length;
                    }
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                    throw th;
                }
                Object obj = parse(data, 0, offset);
                try {
                    reader.close();
                } catch (IOException e2) {
                }
                return obj;
            } catch (IOException ex) {
                throw new SerializationException(ex);
            }
        } catch (Throwable th) {
            reader.close();
            throw th;
        }
    }

    public Object parse(InputStream input) {
        try {
            return parse(new InputStreamReader(input, "ISO-8859-1"));
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    public Object parse(FileHandle file) {
        try {
            return parse(file.read());
        } catch (Exception ex) {
            throw new SerializationException("Error parsing file: " + file, ex);
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0040  */
    /* JADX WARN: Code duplicated, block: B:13:0x004a  */
    /* JADX WARN: Code duplicated, block: B:148:0x0413  */
    /* JADX WARN: Code duplicated, block: B:151:0x041c A[Catch: RuntimeException -> 0x015d, TRY_ENTER, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:154:0x042b A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:157:0x0437 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:158:0x0439 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:15:0x0054  */
    /* JADX WARN: Code duplicated, block: B:160:0x044c A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:163:0x045c A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:165:0x0466 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:167:0x049a  */
    /* JADX WARN: Code duplicated, block: B:168:0x049d A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:170:0x04b6 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:172:0x04c0 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x04f9  */
    /* JADX WARN: Code duplicated, block: B:175:0x04fc A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:177:0x0504 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x050e A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:181:0x053f  */
    /* JADX WARN: Code duplicated, block: B:182:0x0542 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x054a A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:186:0x0554 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:188:0x0585  */
    /* JADX WARN: Code duplicated, block: B:189:0x0588 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:191:0x0590 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:193:0x059a A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:196:0x05c5  */
    /* JADX WARN: Code duplicated, block: B:199:0x0604  */
    /* JADX WARN: Code duplicated, block: B:201:0x0612  */
    /* JADX WARN: Code duplicated, block: B:203:0x062d  */
    /* JADX WARN: Code duplicated, block: B:205:0x0635  */
    /* JADX WARN: Code duplicated, block: B:207:0x063d  */
    /* JADX WARN: Code duplicated, block: B:227:0x040b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:228:0x0408 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:229:0x0416 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:258:0x0056 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:259:0x0038 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:260:0x0434 A[DONT_GENERATE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:261:0x0434 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:267:0x0090 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:268:0x00f9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:269:0x00f6 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:270:0x010a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:30:0x0085 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:49:0x00e2 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:54:0x0107 A[Catch: RuntimeException -> 0x015d, TryCatch #0 {RuntimeException -> 0x015d, blocks: (B:22:0x0065, B:24:0x0073, B:27:0x007c, B:42:0x00b9, B:44:0x00cb, B:45:0x00ce, B:47:0x00da, B:48:0x00dd, B:34:0x0092, B:36:0x009c, B:37:0x00a8, B:39:0x00ac, B:40:0x00b2, B:58:0x011b, B:60:0x012e, B:62:0x0138, B:63:0x0154, B:68:0x0166, B:70:0x0179, B:71:0x0181, B:73:0x0189, B:75:0x0193, B:76:0x01bd, B:78:0x01cb, B:80:0x01e4, B:82:0x01ee, B:83:0x0218, B:85:0x022a, B:87:0x0232, B:89:0x023c, B:90:0x025e, B:92:0x0270, B:94:0x0278, B:96:0x0282, B:97:0x02a4, B:99:0x02b6, B:101:0x02be, B:103:0x02c8, B:104:0x02e4, B:106:0x02f6, B:108:0x02fe, B:110:0x0308, B:111:0x0324, B:113:0x0336, B:120:0x036b, B:121:0x0372, B:125:0x037e, B:127:0x0386, B:129:0x0390, B:130:0x03ac, B:132:0x03be, B:139:0x03f3, B:140:0x03fa, B:28:0x007f, B:30:0x0085, B:33:0x0090, B:49:0x00e2, B:51:0x00f6, B:52:0x00f9, B:54:0x0107, B:55:0x010a, B:151:0x041c, B:152:0x0427, B:154:0x042b, B:155:0x0431, B:158:0x0439, B:160:0x044c, B:161:0x0454, B:163:0x045c, B:165:0x0466, B:166:0x0490, B:168:0x049d, B:170:0x04b6, B:172:0x04c0, B:173:0x04ea, B:175:0x04fc, B:177:0x0504, B:179:0x050e, B:180:0x0530, B:182:0x0542, B:184:0x054a, B:186:0x0554, B:187:0x0576, B:189:0x0588, B:191:0x0590, B:193:0x059a, B:194:0x05b6), top: B:211:0x0065 }] */
    /* JADX WARN: Failed to find 'out' block for switch in B:40:0x00b2. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0035. Please report as an issue. */
    public Object parse(char[] cArr, int i, int i2) {
        int i3;
        int i4;
        int i5;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        Object objPeek;
        int i6;
        int i7;
        byte b;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12 = i;
        int[] iArr = new int[4];
        int i13 = 0;
        Array array = new Array(8);
        boolean z = false;
        boolean z2 = false;
        RuntimeException runtimeException = null;
        if (0 != 0) {
            System.out.println();
        }
        int i14 = 1;
        int i15 = 0;
        int i16 = 0;
        while (true) {
            switch (i15) {
                case 0:
                    if (i12 == i2) {
                        i15 = 4;
                    } else if (i14 == 0) {
                        i15 = 5;
                    }
                case 1:
                    try {
                        short s = _json_key_offsets[i14];
                        short s2 = _json_index_offsets[i14];
                        byte b2 = _json_single_lengths[i14];
                        int i17 = s;
                        int i18 = s2;
                        if (b2 > 0) {
                            int i19 = s;
                            int i20 = (s + b2) - 1;
                            while (true) {
                                if (i20 >= i19) {
                                    int i21 = i19 + ((i20 - i19) >> 1);
                                    if (cArr[i12] < _json_trans_keys[i21]) {
                                        i20 = i21 - 1;
                                    } else if (cArr[i12] > _json_trans_keys[i21]) {
                                        i19 = i21 + 1;
                                    } else {
                                        i11 = s2 + (i21 - s);
                                    }
                                } else {
                                    i17 = s + b2;
                                    i18 = s2 + b2;
                                    b = _json_range_lengths[i14];
                                    i11 = i18;
                                    if (b > 0) {
                                        i8 = i17;
                                        i9 = ((b << 1) + i17) - 2;
                                        while (true) {
                                            if (i9 < i8) {
                                                i10 = i8 + (((i9 - i8) >> 1) & (-2));
                                                if (cArr[i12] < _json_trans_keys[i10]) {
                                                    i9 = i10 - 2;
                                                } else if (cArr[i12] > _json_trans_keys[i10 + 1]) {
                                                    i8 = i10 + 2;
                                                } else {
                                                    i11 = i18 + ((i10 - i17) >> 1);
                                                }
                                            } else {
                                                i11 = i18 + b;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            b = _json_range_lengths[i14];
                            i11 = i18;
                            if (b > 0) {
                                i8 = i17;
                                i9 = ((b << 1) + i17) - 2;
                                while (true) {
                                    if (i9 < i8) {
                                        i10 = i8 + (((i9 - i8) >> 1) & (-2));
                                        if (cArr[i12] < _json_trans_keys[i10]) {
                                            i9 = i10 - 2;
                                        } else if (cArr[i12] > _json_trans_keys[i10 + 1]) {
                                            i8 = i10 + 2;
                                        } else {
                                            i11 = i18 + ((i10 - i17) >> 1);
                                        }
                                    } else {
                                        i11 = i18 + b;
                                    }
                                }
                            }
                        }
                        i14 = _json_trans_targs[i11];
                        if (_json_trans_actions[i11] != 0) {
                            byte b3 = _json_trans_actions[i11];
                            int i22 = b3 + 1;
                            int i23 = _json_actions[b3];
                            while (true) {
                                int i24 = i23 - 1;
                                if (i23 > 0) {
                                    int i25 = i22 + 1;
                                    switch (_json_actions[i22]) {
                                        case 0:
                                            i13 = i12;
                                            z = false;
                                            z2 = false;
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 1:
                                            z = true;
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 2:
                                            String str8 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            if (z) {
                                                str8 = unescape(str8);
                                            }
                                            if (0 != 0) {
                                                System.out.println("name: " + str8);
                                            }
                                            array.add(str8);
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 3:
                                            if (!z2) {
                                                String str9 = new String(cArr, i13, i12 - i13);
                                                i13 = i12;
                                                if (z) {
                                                    str9 = unescape(str9);
                                                }
                                                String str10 = array.size > 0 ? (String) array.pop() : null;
                                                if (0 != 0) {
                                                    System.out.println("string: " + str10 + "=" + str9);
                                                }
                                                string(str10, str9);
                                            }
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 4:
                                            String str11 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            String str12 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("number: " + str12 + "=" + Float.parseFloat(str11));
                                            }
                                            number(str12, Float.parseFloat(str11));
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 5:
                                            String str13 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("boolean: " + str13 + "=true");
                                            }
                                            bool(str13, true);
                                            z2 = true;
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 6:
                                            String str14 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("boolean: " + str14 + "=false");
                                            }
                                            bool(str14, false);
                                            z2 = true;
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 7:
                                            String str15 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("null: " + str15);
                                            }
                                            string(str15, null);
                                            z2 = true;
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 8:
                                            String str16 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("startObject: " + str16);
                                            }
                                            startObject(str16);
                                            int length = iArr.length;
                                            iArr = iArr;
                                            if (i16 == length) {
                                                int[] iArr2 = new int[iArr.length * 2];
                                                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                                                iArr = iArr2;
                                            }
                                            int i26 = i16 + 1;
                                            try {
                                                iArr[i16] = i14;
                                                i14 = 9;
                                                i15 = 2;
                                                i16 = i26;
                                            } catch (RuntimeException e) {
                                                e = e;
                                                runtimeException = e;
                                                if (i12 < i2) {
                                                    i6 = 1;
                                                    for (i7 = 0; i7 < i12; i7++) {
                                                        if (cArr[i7] == '\n') {
                                                            i6++;
                                                        }
                                                    }
                                                    throw new SerializationException("Error parsing JSON on line " + i6 + " near: " + new String(cArr, i12, i2 - i12), runtimeException);
                                                }
                                                if (this.elements.size != 0) {
                                                    objPeek = this.elements.peek();
                                                    this.elements.clear();
                                                    if (objPeek instanceof OrderedMap) {
                                                        throw new SerializationException("Error parsing JSON, unmatched brace.");
                                                    }
                                                    throw new SerializationException("Error parsing JSON, unmatched bracket.");
                                                }
                                                Object obj = this.root;
                                                this.root = null;
                                                return obj;
                                            }
                                            break;
                                        case 9:
                                            if (0 != 0) {
                                                System.out.println("endObject");
                                            }
                                            pop();
                                            int i27 = i16 - 1;
                                            i14 = iArr[i27];
                                            i15 = 2;
                                            i16 = i27;
                                            break;
                                        case 10:
                                            String str17 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("startArray: " + str17);
                                            }
                                            startArray(str17);
                                            int length2 = iArr.length;
                                            iArr = iArr;
                                            if (i16 == length2) {
                                                int[] iArr3 = new int[iArr.length * 2];
                                                System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                                                iArr = iArr3;
                                            }
                                            int i28 = i16 + 1;
                                            iArr[i16] = i14;
                                            i14 = 49;
                                            i15 = 2;
                                            i16 = i28;
                                            break;
                                        case 11:
                                            if (0 != 0) {
                                                System.out.println("endArray");
                                            }
                                            pop();
                                            int i29 = i16 - 1;
                                            i14 = iArr[i29];
                                            i15 = 2;
                                            i16 = i29;
                                            break;
                                        default:
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                    }
                                }
                                break;
                            }
                        }
                        if (i14 == 0) {
                            i15 = 5;
                        } else {
                            i12++;
                            if (i12 != i2) {
                                i15 = 1;
                            } else if (i12 == i2) {
                                byte b4 = _json_eof_actions[i14];
                                i3 = b4 + 1;
                                i4 = _json_actions[b4];
                                while (true) {
                                    i5 = i4 - 1;
                                    if (i4 > 0) {
                                        int i30 = i3 + 1;
                                        switch (_json_actions[i3]) {
                                            case 3:
                                                if (!z2) {
                                                    str6 = new String(cArr, i13, i12 - i13);
                                                    i13 = i12;
                                                    if (z) {
                                                        str6 = unescape(str6);
                                                    }
                                                    if (array.size > 0) {
                                                        str7 = (String) array.pop();
                                                    } else {
                                                        str7 = null;
                                                    }
                                                    if (0 != 0) {
                                                        System.out.println("string: " + str7 + "=" + str6);
                                                    }
                                                    string(str7, str6);
                                                }
                                                break;
                                            case 4:
                                                str4 = new String(cArr, i13, i12 - i13);
                                                i13 = i12;
                                                if (array.size > 0) {
                                                    str5 = (String) array.pop();
                                                } else {
                                                    str5 = null;
                                                }
                                                if (0 != 0) {
                                                    System.out.println("number: " + str5 + "=" + Float.parseFloat(str4));
                                                }
                                                number(str5, Float.parseFloat(str4));
                                                break;
                                            case 5:
                                                if (array.size > 0) {
                                                    str3 = (String) array.pop();
                                                } else {
                                                    str3 = null;
                                                }
                                                if (0 != 0) {
                                                    System.out.println("boolean: " + str3 + "=true");
                                                }
                                                bool(str3, true);
                                                z2 = true;
                                                break;
                                            case 6:
                                                if (array.size > 0) {
                                                    str2 = (String) array.pop();
                                                } else {
                                                    str2 = null;
                                                }
                                                if (0 != 0) {
                                                    System.out.println("boolean: " + str2 + "=false");
                                                }
                                                bool(str2, false);
                                                z2 = true;
                                                break;
                                            case 7:
                                                if (array.size > 0) {
                                                    str = (String) array.pop();
                                                } else {
                                                    str = null;
                                                }
                                                if (0 != 0) {
                                                    System.out.println("null: " + str);
                                                }
                                                string(str, null);
                                                z2 = true;
                                                break;
                                        }
                                        i4 = i5;
                                        i3 = i30;
                                    }
                                }
                            }
                        }
                    } catch (RuntimeException e2) {
                        e = e2;
                    }
                    break;
                case 2:
                    if (i14 == 0) {
                        i15 = 5;
                    } else {
                        i12++;
                        if (i12 != i2) {
                            i15 = 1;
                        } else if (i12 == i2) {
                            byte b5 = _json_eof_actions[i14];
                            i3 = b5 + 1;
                            i4 = _json_actions[b5];
                            while (true) {
                                i5 = i4 - 1;
                                if (i4 > 0) {
                                    int i31 = i3 + 1;
                                    switch (_json_actions[i3]) {
                                        case 3:
                                            if (!z2) {
                                                str6 = new String(cArr, i13, i12 - i13);
                                                i13 = i12;
                                                if (z) {
                                                    str6 = unescape(str6);
                                                }
                                                if (array.size > 0) {
                                                    str7 = (String) array.pop();
                                                } else {
                                                    str7 = null;
                                                }
                                                if (0 != 0) {
                                                    System.out.println("string: " + str7 + "=" + str6);
                                                }
                                                string(str7, str6);
                                            }
                                            break;
                                        case 4:
                                            str4 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            if (array.size > 0) {
                                                str5 = (String) array.pop();
                                            } else {
                                                str5 = null;
                                            }
                                            if (0 != 0) {
                                                System.out.println("number: " + str5 + "=" + Float.parseFloat(str4));
                                            }
                                            number(str5, Float.parseFloat(str4));
                                            break;
                                        case 5:
                                            if (array.size > 0) {
                                                str3 = (String) array.pop();
                                            } else {
                                                str3 = null;
                                            }
                                            if (0 != 0) {
                                                System.out.println("boolean: " + str3 + "=true");
                                            }
                                            bool(str3, true);
                                            z2 = true;
                                            break;
                                        case 6:
                                            if (array.size > 0) {
                                                str2 = (String) array.pop();
                                            } else {
                                                str2 = null;
                                            }
                                            if (0 != 0) {
                                                System.out.println("boolean: " + str2 + "=false");
                                            }
                                            bool(str2, false);
                                            z2 = true;
                                            break;
                                        case 7:
                                            if (array.size > 0) {
                                                str = (String) array.pop();
                                            } else {
                                                str = null;
                                            }
                                            if (0 != 0) {
                                                System.out.println("null: " + str);
                                            }
                                            string(str, null);
                                            z2 = true;
                                            break;
                                    }
                                    i4 = i5;
                                    i3 = i31;
                                }
                            }
                        }
                    }
                    break;
                case 3:
                default:
                    break;
                case 4:
                    if (i12 == i2) {
                        byte b6 = _json_eof_actions[i14];
                        i3 = b6 + 1;
                        i4 = _json_actions[b6];
                        while (true) {
                            i5 = i4 - 1;
                            if (i4 > 0) {
                                int i32 = i3 + 1;
                                switch (_json_actions[i3]) {
                                    case 3:
                                        if (!z2) {
                                            str6 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            if (z) {
                                                str6 = unescape(str6);
                                            }
                                            if (array.size > 0) {
                                                str7 = (String) array.pop();
                                            } else {
                                                str7 = null;
                                            }
                                            if (0 != 0) {
                                                System.out.println("string: " + str7 + "=" + str6);
                                            }
                                            string(str7, str6);
                                        }
                                        break;
                                    case 4:
                                        str4 = new String(cArr, i13, i12 - i13);
                                        i13 = i12;
                                        if (array.size > 0) {
                                            str5 = (String) array.pop();
                                        } else {
                                            str5 = null;
                                        }
                                        if (0 != 0) {
                                            System.out.println("number: " + str5 + "=" + Float.parseFloat(str4));
                                        }
                                        number(str5, Float.parseFloat(str4));
                                        break;
                                    case 5:
                                        if (array.size > 0) {
                                            str3 = (String) array.pop();
                                        } else {
                                            str3 = null;
                                        }
                                        if (0 != 0) {
                                            System.out.println("boolean: " + str3 + "=true");
                                        }
                                        bool(str3, true);
                                        z2 = true;
                                        break;
                                    case 6:
                                        if (array.size > 0) {
                                            str2 = (String) array.pop();
                                        } else {
                                            str2 = null;
                                        }
                                        if (0 != 0) {
                                            System.out.println("boolean: " + str2 + "=false");
                                        }
                                        bool(str2, false);
                                        z2 = true;
                                        break;
                                    case 7:
                                        if (array.size > 0) {
                                            str = (String) array.pop();
                                        } else {
                                            str = null;
                                        }
                                        if (0 != 0) {
                                            System.out.println("null: " + str);
                                        }
                                        string(str, null);
                                        z2 = true;
                                        break;
                                }
                                i4 = i5;
                                i3 = i32;
                            }
                        }
                    }
                    break;
            }
            if (i12 < i2) {
                i6 = 1;
                while (i7 < i12) {
                    if (cArr[i7] == '\n') {
                        i6++;
                    }
                }
                throw new SerializationException("Error parsing JSON on line " + i6 + " near: " + new String(cArr, i12, i2 - i12), runtimeException);
            }
            if (this.elements.size != 0) {
                objPeek = this.elements.peek();
                this.elements.clear();
                if (objPeek instanceof OrderedMap) {
                    throw new SerializationException("Error parsing JSON, unmatched brace.");
                }
                throw new SerializationException("Error parsing JSON, unmatched bracket.");
            }
            Object obj2 = this.root;
            this.root = null;
            return obj2;
        }
    }

    private static byte[] init__json_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 8, 1, 9, 1, 10, 1, 11, 2, 0, 2, 2, 0, 3, 2, 3, 9, 2, 3, 11, 2, 4, 9, 2, 4, 11, 2, 5, 3, 2, 6, 3, 2, 7, 3, 3, 5, 3, 9, 3, 5, 3, 11, 3, 6, 3, 9, 3, 6, 3, 11, 3, 7, 3, 9, 3, 7, 3, 11};
    }

    private static short[] init__json_key_offsets_0() {
        return new short[]{0, 0, 18, 20, 22, 31, 33, 35, 39, 41, 56, 58, 60, 64, 82, 84, 86, 91, 105, 112, 114, 123, 125, 133, 137, 139, 145, 154, 161, 163, 173, 175, 184, 188, 190, 197, 205, 213, 221, 229, 236, 244, 252, 260, 267, 275, 283, 291, 298, 307, 327, 329, 331, 336, 355, 362, 364, 374, 376, 385, 389, 391, 398, 406, 414, 422, 430, 437, 445, 453, 461, 468, 476, 484, 492, 499, 508, 511, 518, 526, 533, 538, 546, 554, 562, 570, 577, 585, 593, 601, 608, 616, 624, 632, 639, 639};
    }

    private static char[] init__json_trans_keys_0() {
        return new char[]{' ', '\"', '$', SignatureVisitor.SUPER, '[', '_', 'f', 'n', 't', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', '0', '9', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', '\"', '$', ',', SignatureVisitor.SUPER, '_', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', ' ', ':', '\t', '\r', ' ', '\"', '$', SignatureVisitor.SUPER, '[', '_', 'f', 'n', 't', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', ' ', ',', '}', '\t', '\r', ' ', '\"', '$', SignatureVisitor.SUPER, '_', '}', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', ',', ':', ']', '}', '\t', '\r', '0', '9', ' ', '.', ':', 'E', 'e', '\t', '\r', '0', '9', '0', '9', ' ', ':', 'E', 'e', '\t', '\r', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ':', '\t', '\r', '0', '9', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', ',', ':', ']', '}', '\t', '\r', '0', '9', ' ', ',', '.', 'E', 'e', '}', '\t', '\r', '0', '9', '0', '9', ' ', ',', 'E', 'e', '}', '\t', '\r', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ',', '}', '\t', '\r', '0', '9', ' ', ',', ':', ']', 'a', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', 's', '}', '\t', '\r', ' ', ',', ':', ']', 'e', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', ',', ':', ']', 'u', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', ',', ':', ']', 'r', '}', '\t', '\r', ' ', ',', ':', ']', 'u', '}', '\t', '\r', ' ', ',', ':', ']', 'e', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', '\"', '$', ',', SignatureVisitor.SUPER, '[', ']', '_', 'f', 'n', 't', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', ' ', ',', ']', '\t', '\r', ' ', '\"', '$', SignatureVisitor.SUPER, '[', ']', '_', 'f', 'n', 't', '{', '\t', '\r', '0', '9', 'A', 'Z', 'a', 'z', ' ', ',', ':', ']', '}', '\t', '\r', '0', '9', ' ', ',', '.', 'E', ']', 'e', '\t', '\r', '0', '9', '0', '9', ' ', ',', 'E', ']', 'e', '\t', '\r', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ',', ']', '\t', '\r', '0', '9', ' ', ',', ':', ']', 'a', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', 's', '}', '\t', '\r', ' ', ',', ':', ']', 'e', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', ',', ':', ']', 'u', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', ',', ':', ']', 'r', '}', '\t', '\r', ' ', ',', ':', ']', 'u', '}', '\t', '\r', ' ', ',', ':', ']', 'e', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', '.', 'E', 'e', '\t', '\r', '0', '9', ' ', 'E', 'e', '\t', '\r', '0', '9', ' ', '\t', '\r', '0', '9', ' ', ',', ':', ']', 'a', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', 's', '}', '\t', '\r', ' ', ',', ':', ']', 'e', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', ',', ':', ']', 'u', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', 'l', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', ' ', ',', ':', ']', 'r', '}', '\t', '\r', ' ', ',', ':', ']', 'u', '}', '\t', '\r', ' ', ',', ':', ']', 'e', '}', '\t', '\r', ' ', ',', ':', ']', '}', '\t', '\r', 0};
    }

    private static byte[] init__json_single_lengths_0() {
        return new byte[]{0, 10, 2, 2, 7, 0, 0, 2, 0, 7, 2, 2, 2, 10, 2, 2, 3, 6, 5, 0, 5, 0, 4, 2, 0, 2, 7, 5, 0, 6, 0, 5, 2, 0, 3, 6, 6, 6, 6, 5, 6, 6, 6, 5, 6, 6, 6, 5, 7, 12, 2, 2, 3, 11, 5, 0, 6, 0, 5, 2, 0, 3, 6, 6, 6, 6, 5, 6, 6, 6, 5, 6, 6, 6, 5, 7, 1, 5, 4, 3, 1, 6, 6, 6, 6, 5, 6, 6, 6, 5, 6, 6, 6, 5, 0, 0};
    }

    private static byte[] init__json_range_lengths_0() {
        return new byte[]{0, 4, 0, 0, 1, 1, 1, 1, 1, 4, 0, 0, 1, 4, 0, 0, 1, 4, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 0, 0, 1, 4, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0};
    }

    private static short[] init__json_index_offsets_0() {
        return new short[]{0, 0, 15, 18, 21, 30, 32, 34, 38, 40, 52, 55, 58, 62, 77, 80, 83, 88, 99, 106, 108, 116, 118, 125, 129, 131, 136, 145, 152, 154, 163, 165, 173, 177, 179, 185, 193, 201, 209, 217, 224, 232, 240, 248, 255, 263, 271, 279, 286, 295, 312, 315, 318, 323, 339, 346, 348, 357, 359, 367, 371, 373, 379, 387, 395, 403, 411, 418, 426, 434, 442, 449, 457, 465, 473, 480, 489, 492, 499, 506, 512, 516, 524, 532, 540, 548, 555, 563, 571, 579, 586, 594, 602, 610, 617, 618};
    }

    private static byte[] init__json_trans_targs_0() {
        return new byte[]{1, 2, 77, 5, 76, 77, 81, 86, 90, 76, 1, 78, 77, 77, 0, 76, 4, 3, 76, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 78, 0, 79, 0, 8, 8, 80, 0, 80, 0, 9, 10, 18, 17, 19, 18, 94, 9, 20, 18, 18, 0, 12, 48, 11, 12, 48, 11, 12, 13, 12, 0, 13, 14, 27, 28, 16, 27, SmileConstants.TOKEN_LITERAL_TRUE, 40, 44, 16, 13, 29, 27, 27, 0, 16, 26, 15, 16, 26, 15, 16, 17, 94, 16, 0, 17, 10, 18, 19, 18, 94, 17, 20, 18, 18, 0, 12, 0, 13, 0, 0, 12, 18, 20, 0, 12, 21, 13, 23, 23, 12, 20, 0, 22, 0, 12, 13, 23, 23, 12, 22, 0, 24, 24, 25, 0, 25, 0, 12, 13, 12, 25, 0, 15, 15, 15, 15, 15, 15, 15, 15, 0, 16, 17, 0, 0, 94, 16, 27, 29, 0, 16, 17, 30, 32, 32, 94, 16, 29, 0, 31, 0, 16, 17, 32, 32, 94, 16, 31, 0, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, 0, SmileConstants.TOKEN_LITERAL_FALSE, 0, 16, 17, 94, 16, SmileConstants.TOKEN_LITERAL_FALSE, 0, 16, 17, 0, 0, 36, 94, 16, 27, 16, 17, 0, 0, 37, 94, 16, 27, 16, 17, 0, 0, 38, 94, 16, 27, 16, 17, 0, 0, 39, 94, 16, 27, 16, 17, 0, 0, 94, 16, 27, 16, 17, 0, 0, SmileConstants.HEADER_BYTE_2, 94, 16, 27, 16, 17, 0, 0, 42, 94, 16, 27, 16, 17, 0, 0, 43, 94, 16, 27, 16, 17, 0, 0, 94, 16, 27, 16, 17, 0, 0, 45, 94, 16, 27, 16, 17, 0, 0, 46, 94, 16, 27, 16, 17, 0, 0, 47, 94, 16, 27, 16, 17, 0, 0, 94, 16, 27, 11, 11, 11, 11, 11, 11, 11, 11, 0, 49, 50, 54, 53, 55, SmileConstants.TOKEN_KEY_LONG_STRING, 95, 54, 62, 67, 71, SmileConstants.TOKEN_KEY_LONG_STRING, 49, 56, 54, 54, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 75, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 75, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 95, SmileConstants.TOKEN_KEY_LONG_STRING, 0, 53, 50, 54, 55, SmileConstants.TOKEN_KEY_LONG_STRING, 95, 54, 62, 67, 71, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 56, 54, 54, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, 56, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 57, 59, 95, 59, SmileConstants.TOKEN_KEY_LONG_STRING, 56, 0, SmileConstants.HEADER_BYTE_1, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 59, 95, 59, SmileConstants.TOKEN_KEY_LONG_STRING, SmileConstants.HEADER_BYTE_1, 0, 60, 60, 61, 0, 61, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 95, SmileConstants.TOKEN_KEY_LONG_STRING, 61, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 63, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 64, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 65, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 66, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 68, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 69, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 70, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 72, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 73, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 74, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 0, 95, 0, SmileConstants.TOKEN_KEY_LONG_STRING, 54, 51, 51, 51, 51, 51, 51, 51, 51, 0, 76, 76, 0, 76, 0, 0, 0, 0, 76, 77, 76, 6, 7, 7, 76, 78, 0, 76, 7, 7, 76, 79, 0, 76, 76, 80, 0, 76, 0, 0, 0, 82, 0, 76, 77, 76, 0, 0, 0, 83, 0, 76, 77, 76, 0, 0, 0, 84, 0, 76, 77, 76, 0, 0, 0, 85, 0, 76, 77, 76, 0, 0, 0, 0, 76, 77, 76, 0, 0, 0, 87, 0, 76, 77, 76, 0, 0, 0, 88, 0, 76, 77, 76, 0, 0, 0, 89, 0, 76, 77, 76, 0, 0, 0, 0, 76, 77, 76, 0, 0, 0, 91, 0, 76, 77, 76, 0, 0, 0, 92, 0, 76, 77, 76, 0, 0, 0, 93, 0, 76, 77, 76, 0, 0, 0, 0, 76, 77, 0, 0, 0};
    }

    private static byte[] init__json_trans_actions_0() {
        return new byte[]{0, 0, 1, 1, 15, 1, 1, 1, 1, 11, 0, 1, 1, 1, 0, 22, 1, 1, 7, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 13, 0, 1, 1, 1, 0, 19, 1, 1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 15, 1, 1, 1, 1, 11, 0, 1, 1, 1, 0, 22, 1, 1, 7, 0, 0, 0, 0, 13, 0, 0, 0, 0, 1, 1, 1, 13, 0, 1, 1, 1, 0, 5, 0, 5, 0, 0, 5, 0, 0, 0, 5, 0, 5, 0, 0, 5, 0, 0, 0, 0, 5, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 7, 7, 0, 0, 25, 7, 0, 0, 0, 9, 9, 0, 0, 0, 31, 9, 0, 0, 0, 0, 9, 9, 0, 0, 31, 9, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 31, 9, 0, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 40, 40, 0, 0, 54, 40, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 43, 43, 0, 0, 62, 43, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 7, 7, 0, 0, 0, 25, 7, 0, 37, 37, 0, 0, 46, 37, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 1, 0, 1, 15, 17, 1, 1, 1, 1, 11, 0, 1, 1, 1, 0, 22, 1, 1, 7, 0, 0, 0, 0, 17, 0, 0, 0, 0, 1, 1, 15, 17, 1, 1, 1, 1, 11, 0, 1, 1, 1, 0, 7, 7, 0, 28, 0, 7, 0, 0, 0, 9, 9, 0, 0, SmileConstants.TOKEN_LITERAL_FALSE, 0, 9, 0, 0, 0, 0, 9, 9, 0, SmileConstants.TOKEN_LITERAL_FALSE, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, SmileConstants.TOKEN_LITERAL_FALSE, 9, 0, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 40, 40, 0, SmileConstants.HEADER_BYTE_1, 0, 40, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 43, 43, 0, 66, 0, 43, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 7, 7, 0, 28, 0, 0, 7, 0, 37, 37, 0, 50, 0, 37, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 7, 0, 0, 0, 0, 7, 0, 9, 0, 0, 0, 9, 0, 0, 9, 0, 0, 9, 0, 0, 9, 9, 0, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 40, 0, 0, 0, 0, 40, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 43, 0, 0, 0, 0, 43, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 37, 0, 0, 0, 0, 37, 0, 0, 0, 0};
    }

    private static byte[] init__json_eof_actions_0() {
        return new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 9, 9, 9, 7, 7, 7, 7, 40, 7, 7, 7, 43, 7, 7, 7, 37, 0, 0};
    }

    private void set(String name, Object value) {
        if (this.current instanceof OrderedMap) {
            ((OrderedMap) this.current).put(name, value);
        } else if (this.current instanceof Array) {
            ((Array) this.current).add(value);
        } else {
            this.root = value;
        }
    }

    protected void startObject(String name) {
        OrderedMap value = new OrderedMap();
        if (this.current != null) {
            set(name, value);
        }
        this.elements.add(value);
        this.current = value;
    }

    protected void startArray(String name) {
        Array value = new Array();
        if (this.current != null) {
            set(name, value);
        }
        this.elements.add(value);
        this.current = value;
    }

    protected void pop() {
        this.root = this.elements.pop();
        this.current = this.elements.size > 0 ? this.elements.peek() : null;
    }

    protected void string(String name, String value) {
        set(name, value);
    }

    protected void number(String name, float value) {
        set(name, Float.valueOf(value));
    }

    protected void bool(String name, boolean value) {
        set(name, Boolean.valueOf(value));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private String unescape(String value) {
        int length = value.length();
        StringBuilder buffer = new StringBuilder(length + 16);
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            char c = value.charAt(i);
            if (c != '\\') {
                buffer.append(c);
                i = i2;
            } else if (i2 != length) {
                i = i2 + 1;
                char c2 = value.charAt(i2);
                if (c2 == 'u') {
                    buffer.append(Character.toChars(Integer.parseInt(value.substring(i, i + 4), 16)));
                    i += 4;
                } else {
                    switch (c2) {
                        case Input.Keys.F /* 34 */:
                        case '/':
                        case '\\':
                            buffer.append(c2);
                            break;
                        case 'b':
                            c2 = '\b';
                            buffer.append(c2);
                            break;
                        case 'f':
                            c2 = '\f';
                            buffer.append(c2);
                            break;
                        case 'n':
                            c2 = '\n';
                            buffer.append(c2);
                            break;
                        case Opcodes.FREM /* 114 */:
                            c2 = '\r';
                            buffer.append(c2);
                            break;
                        case Opcodes.INEG /* 116 */:
                            c2 = '\t';
                            buffer.append(c2);
                            break;
                        default:
                            throw new SerializationException("Illegal escaped character: \\" + c2);
                    }
                }
            } else {
                return buffer.toString();
            }
        }
        return buffer.toString();
    }
}
