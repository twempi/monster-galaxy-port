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

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JsonReader {
    static final int json_en_array = 51;
    static final int json_en_main = 1;
    static final int json_en_object = 19;
    static final int json_error = 0;
    static final int json_first_final = 78;
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
        } catch (IOException ex) {
            throw new SerializationException(ex);
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
        return parse(file.read());
    }

    /* JADX WARN: Code duplicated, block: B:10:0x003e  */
    /* JADX WARN: Code duplicated, block: B:13:0x0048  */
    /* JADX WARN: Code duplicated, block: B:147:0x0407  */
    /* JADX WARN: Code duplicated, block: B:150:0x0410 A[Catch: RuntimeException -> 0x0159, TRY_ENTER, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:153:0x041f A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:156:0x042b A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:158:0x0444 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:15:0x0052  */
    /* JADX WARN: Code duplicated, block: B:160:0x044e A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:162:0x0486  */
    /* JADX WARN: Code duplicated, block: B:163:0x0489 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:165:0x0491 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:167:0x049b A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x04ca  */
    /* JADX WARN: Code duplicated, block: B:170:0x04cd A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:172:0x04d5 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x04df A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:176:0x050e  */
    /* JADX WARN: Code duplicated, block: B:177:0x0511 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x0519 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:181:0x0523 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x054c  */
    /* JADX WARN: Code duplicated, block: B:187:0x058b  */
    /* JADX WARN: Code duplicated, block: B:189:0x0599  */
    /* JADX WARN: Code duplicated, block: B:191:0x05b4  */
    /* JADX WARN: Code duplicated, block: B:193:0x05bc  */
    /* JADX WARN: Code duplicated, block: B:195:0x05c4  */
    /* JADX WARN: Code duplicated, block: B:215:0x03ff A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:216:0x03fc A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:217:0x040a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:245:0x0054 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:246:0x0036 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:247:0x0428 A[DONT_GENERATE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:252:0x008e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:253:0x00f7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:254:0x00f4 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:255:0x0108 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:30:0x0083 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:49:0x00e0 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:54:0x0105 A[Catch: RuntimeException -> 0x0159, TryCatch #1 {RuntimeException -> 0x0159, blocks: (B:22:0x0063, B:24:0x0071, B:27:0x007a, B:42:0x00b7, B:44:0x00c9, B:45:0x00cc, B:47:0x00d8, B:48:0x00db, B:34:0x0090, B:36:0x009a, B:37:0x00a6, B:39:0x00aa, B:40:0x00b0, B:58:0x0117, B:60:0x012a, B:62:0x0134, B:63:0x0150, B:67:0x0160, B:69:0x0173, B:70:0x017b, B:72:0x0183, B:74:0x018d, B:75:0x01b7, B:77:0x01c5, B:79:0x01de, B:81:0x01e8, B:82:0x0212, B:84:0x0224, B:86:0x022c, B:88:0x0236, B:89:0x0258, B:91:0x0268, B:93:0x0270, B:95:0x027a, B:96:0x029c, B:98:0x02ac, B:100:0x02b4, B:102:0x02be, B:103:0x02da, B:105:0x02ea, B:107:0x02f2, B:109:0x02fc, B:110:0x0318, B:112:0x032a, B:119:0x035f, B:120:0x0366, B:124:0x0372, B:126:0x037a, B:128:0x0384, B:129:0x03a0, B:131:0x03b2, B:138:0x03e7, B:139:0x03ee, B:28:0x007d, B:30:0x0083, B:33:0x008e, B:49:0x00e0, B:51:0x00f4, B:52:0x00f7, B:54:0x0105, B:55:0x0108, B:150:0x0410, B:151:0x041b, B:153:0x041f, B:154:0x0425, B:156:0x042b, B:158:0x0444, B:160:0x044e, B:161:0x0478, B:163:0x0489, B:165:0x0491, B:167:0x049b, B:168:0x04bd, B:170:0x04cd, B:172:0x04d5, B:174:0x04df, B:175:0x0501, B:177:0x0511, B:179:0x0519, B:181:0x0523, B:182:0x053f), top: B:201:0x0063 }] */
    /* JADX WARN: Failed to find 'out' block for switch in B:40:0x00b0. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0033. Please report as an issue. */
    public Object parse(char[] cArr, int i, int i2) {
        int i3;
        int i4;
        int i5;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
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
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 1:
                                            z = true;
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 2:
                                            String str6 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            if (z) {
                                                str6 = unescape(str6);
                                            }
                                            if (0 != 0) {
                                                System.out.println("name: " + str6);
                                            }
                                            array.add(str6);
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 3:
                                            String str7 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            if (z) {
                                                str7 = unescape(str7);
                                            }
                                            String str8 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("string: " + str8 + "=" + str7);
                                            }
                                            string(str8, str7);
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 4:
                                            String str9 = new String(cArr, i13, i12 - i13);
                                            i13 = i12;
                                            String str10 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("number: " + str10 + "=" + Float.parseFloat(str9));
                                            }
                                            number(str10, Float.parseFloat(str9));
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 5:
                                            String str11 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("boolean: " + str11 + "=true");
                                            }
                                            bool(str11, true);
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 6:
                                            String str12 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("boolean: " + str12 + "=false");
                                            }
                                            bool(str12, false);
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 7:
                                            String str13 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("null: " + str13);
                                            }
                                            string(str13, null);
                                            i23 = i24;
                                            i22 = i25;
                                            break;
                                        case 8:
                                            String str14 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("startObject: " + str14);
                                            }
                                            startObject(str14);
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
                                                i14 = 19;
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
                                                    throw new IllegalArgumentException("Error parsing JSON on line " + i6 + " near: " + new String(cArr, i12, i2 - i12), runtimeException);
                                                }
                                                if (this.elements.size != 0) {
                                                    objPeek = this.elements.peek();
                                                    this.elements.clear();
                                                    if (objPeek instanceof ObjectMap) {
                                                        throw new IllegalArgumentException("Error parsing JSON, unmatched brace.");
                                                    }
                                                    throw new IllegalArgumentException("Error parsing JSON, unmatched bracket.");
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
                                            String str15 = array.size > 0 ? (String) array.pop() : null;
                                            if (0 != 0) {
                                                System.out.println("startArray: " + str15);
                                            }
                                            startArray(str15);
                                            int length2 = iArr.length;
                                            iArr = iArr;
                                            if (i16 == length2) {
                                                int[] iArr3 = new int[iArr.length * 2];
                                                System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                                                iArr = iArr3;
                                            }
                                            int i28 = i16 + 1;
                                            iArr[i16] = i14;
                                            i14 = 51;
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
                throw new IllegalArgumentException("Error parsing JSON on line " + i6 + " near: " + new String(cArr, i12, i2 - i12), runtimeException);
            }
            if (this.elements.size != 0) {
                objPeek = this.elements.peek();
                this.elements.clear();
                if (objPeek instanceof ObjectMap) {
                    throw new IllegalArgumentException("Error parsing JSON, unmatched brace.");
                }
                throw new IllegalArgumentException("Error parsing JSON, unmatched bracket.");
            }
            Object obj2 = this.root;
            this.root = null;
            return obj2;
        }
    }

    private static byte[] init__json_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1, 9, 1, 10, 1, 11, 2, 0, 2, 2, 0, 3, 2, 4, 9, 2, 4, 11, 2, 5, 9, 2, 5, 11, 2, 6, 9, 2, 6, 11, 2, 7, 9, 2, 7, 11};
    }

    private static short[] init__json_key_offsets_0() {
        return new short[]{0, 0, 13, 15, 17, 26, 29, 31, 35, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 53, 55, 57, 61, 74, 76, 78, 83, 87, 96, 99, 107, 109, 118, 122, 124, 131, 141, 142, 143, 144, 145, 150, 151, 152, 153, 158, 159, 160, 161, 166, 175, 190, 192, 194, 199, 212, 215, 223, 225, 234, 238, 240, 247, 257, 258, 259, 260, 261, 266, 267, 268, 269, 274, 275, 276, 277, 282, 291, 294, 300, 307, 312, 320, 323, 326, 329, 329};
    }

    private static char[] init__json_trans_keys_0() {
        return new char[]{' ', '\"', SignatureVisitor.SUPER, '0', '[', 'f', 'n', 't', '{', '\t', '\r', '1', '9', '\"', '\\', '\"', '\\', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', '0', '1', '9', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', 'a', 'l', 's', 'e', 'u', 'l', 'l', 'r', 'u', 'e', ' ', '\"', ',', '}', '\t', '\r', '\"', '\\', '\"', '\\', ' ', ':', '\t', '\r', ' ', '\"', SignatureVisitor.SUPER, '0', '[', 'f', 'n', 't', '{', '\t', '\r', '1', '9', '\"', '\\', '\"', '\\', ' ', ',', '}', '\t', '\r', ' ', '\"', '\t', '\r', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', '0', '1', '9', ' ', ',', '.', 'E', 'e', '}', '\t', '\r', '0', '9', ' ', ',', 'E', 'e', '}', '\t', '\r', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ',', '}', '\t', '\r', '0', '9', ' ', ',', '.', 'E', 'e', '}', '\t', '\r', '0', '9', 'a', 'l', 's', 'e', ' ', ',', '}', '\t', '\r', 'u', 'l', 'l', ' ', ',', '}', '\t', '\r', 'r', 'u', 'e', ' ', ',', '}', '\t', '\r', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', '\"', ',', SignatureVisitor.SUPER, '0', '[', ']', 'f', 'n', 't', '{', '\t', '\r', '1', '9', '\"', '\\', '\"', '\\', ' ', ',', ']', '\t', '\r', ' ', '\"', SignatureVisitor.SUPER, '0', '[', 'f', 'n', 't', '{', '\t', '\r', '1', '9', '0', '1', '9', ' ', ',', '.', 'E', ']', 'e', '\t', '\r', '0', '9', ' ', ',', 'E', ']', 'e', '\t', '\r', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ',', ']', '\t', '\r', '0', '9', ' ', ',', '.', 'E', ']', 'e', '\t', '\r', '0', '9', 'a', 'l', 's', 'e', ' ', ',', ']', '\t', '\r', 'u', 'l', 'l', ' ', ',', ']', '\t', '\r', 'r', 'u', 'e', ' ', ',', ']', '\t', '\r', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', '\t', '\r', ' ', '.', 'E', 'e', '\t', '\r', ' ', 'E', 'e', '\t', '\r', '0', '9', ' ', '\t', '\r', '0', '9', ' ', '.', 'E', 'e', '\t', '\r', '0', '9', ' ', '\t', '\r', ' ', '\t', '\r', ' ', '\t', '\r', 0};
    }

    private static byte[] init__json_single_lengths_0() {
        return new byte[]{0, 9, 2, 2, 7, 1, 0, 2, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 2, 2, 2, 9, 2, 2, 3, 2, 7, 1, 6, 0, 5, 2, 0, 3, 6, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 7, 11, 2, 2, 3, 9, 1, 6, 0, 5, 2, 0, 3, 6, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 7, 1, 4, 3, 1, 4, 1, 1, 1, 0, 0};
    }

    private static byte[] init__json_range_lengths_0() {
        return new byte[]{0, 2, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 2, 0, 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 2, 0, 0, 1, 2, 1, 1, 1, 2, 1, 1, 2, 2, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 0, 0};
    }

    private static short[] init__json_index_offsets_0() {
        return new short[]{0, 0, 12, 15, 18, 27, 30, 32, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 64, 67, 70, 74, 86, 89, 92, 97, 101, 110, 113, 121, 123, 131, 135, 137, 143, 152, 154, 156, 158, 160, 165, 167, 169, 171, 176, 178, 180, 182, 187, 196, 210, 213, 216, 221, 233, 236, 244, 246, 254, 258, 260, 266, 275, 277, 279, 281, 283, 288, 290, 292, 294, 299, 301, 303, 305, 310, 319, 322, 328, 334, 338, 345, 348, 351, 354, 355};
    }

    private static byte[] init__json_trans_targs_0() {
        return new byte[]{1, 2, 5, 79, 78, 9, 13, 16, 78, 1, 82, 0, 78, 4, 3, 78, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 79, 82, 0, 80, 0, 8, 8, 81, 0, 81, 0, 10, 0, 11, 0, 12, 0, 83, 0, 14, 0, 15, 0, 84, 0, 17, 0, 18, 0, 85, 0, 19, 20, 27, 86, 19, 0, 22, 50, 21, 22, 50, 21, 22, 23, 22, 0, 23, 24, 29, 30, 26, 37, 42, 46, 26, 23, 36, 0, 26, 28, 25, 26, 28, 25, 26, 27, 86, 26, 0, 27, 20, 27, 0, 25, 25, 25, 25, 25, 25, 25, 25, 0, 30, 36, 0, 26, 27, 31, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_NULL, 86, 26, 0, 32, 0, 26, 27, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_NULL, 86, 26, 32, 0, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, 0, SmileConstants.TOKEN_LITERAL_TRUE, 0, 26, 27, 86, 26, SmileConstants.TOKEN_LITERAL_TRUE, 0, 26, 27, 31, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_NULL, 86, 26, 36, 0, 38, 0, 39, 0, 40, 0, SmileConstants.HEADER_BYTE_2, 0, 26, 27, 86, 26, 0, 43, 0, 44, 0, 45, 0, 26, 27, 86, 26, 0, 47, 0, 48, 0, 49, 0, 26, 27, 86, 26, 0, 21, 21, 21, 21, 21, 21, 21, 21, 0, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 55, 56, 57, 54, 87, 64, 69, 73, 54, 51, 63, 0, 54, 77, 53, 54, 77, 53, 54, 55, 87, 54, 0, 55, SmileConstants.TOKEN_KEY_LONG_STRING, 56, 57, 54, 64, 69, 73, 54, 55, 63, 0, 57, 63, 0, 54, 55, SmileConstants.HEADER_BYTE_1, 60, 87, 60, 54, 0, 59, 0, 54, 55, 60, 87, 60, 54, 59, 0, 61, 61, 62, 0, 62, 0, 54, 55, 87, 54, 62, 0, 54, 55, SmileConstants.HEADER_BYTE_1, 60, 87, 60, 54, 63, 0, 65, 0, 66, 0, 67, 0, 68, 0, 54, 55, 87, 54, 0, 70, 0, 71, 0, 72, 0, 54, 55, 87, 54, 0, 74, 0, 75, 0, 76, 0, 54, 55, 87, 54, 0, 53, 53, 53, 53, 53, 53, 53, 53, 0, 78, 78, 0, 78, 6, 7, 7, 78, 0, 78, 7, 7, 78, 80, 0, 78, 78, 81, 0, 78, 6, 7, 7, 78, 82, 0, 78, 78, 0, 78, 78, 0, 78, 78, 0, 0, 0, 0};
    }

    private static byte[] init__json_trans_actions_0() {
        return new byte[]{0, 0, 1, 1, 21, 0, 0, 0, 17, 0, 1, 0, 28, 1, 1, 7, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0, 0, 25, 1, 1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 21, 0, 0, 0, 17, 0, 1, 0, 28, 1, 1, 7, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 9, 9, 0, 0, 0, 31, 9, 0, 0, 0, 9, 9, 0, 0, 31, 9, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 31, 9, 0, 0, 9, 9, 0, 0, 0, 31, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 13, 43, 13, 0, 0, 0, 0, 0, 0, 0, 15, 15, 49, 15, 0, 0, 0, 0, 0, 0, 0, 11, 11, 37, 11, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 1, 1, 21, 23, 0, 0, 0, 17, 0, 1, 0, 28, 1, 1, 7, 0, 0, 0, 0, 23, 0, 0, 0, 0, 1, 1, 21, 0, 0, 0, 17, 0, 1, 0, 0, 0, 0, 9, 9, 0, 0, SmileConstants.TOKEN_LITERAL_FALSE, 0, 9, 0, 0, 0, 9, 9, 0, SmileConstants.TOKEN_LITERAL_FALSE, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, SmileConstants.TOKEN_LITERAL_FALSE, 9, 0, 0, 9, 9, 0, 0, SmileConstants.TOKEN_LITERAL_FALSE, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 13, 46, 13, 0, 0, 0, 0, 0, 0, 0, 15, 15, SmileConstants.TOKEN_KEY_LONG_STRING, 15, 0, 0, 0, 0, 0, 0, 0, 11, 11, 40, 11, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 9, 0, 0, 0, 9, 0, 9, 0, 0, 9, 0, 0, 9, 9, 0, 0, 9, 0, 0, 0, 9, 0, 0, 13, 13, 0, 15, 15, 0, 11, 11, 0, 0, 0, 0};
    }

    private static byte[] init__json_eof_actions_0() {
        return new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 13, 15, 11, 0, 0};
    }

    private void set(String name, Object value) {
        if (this.current instanceof ObjectMap) {
            ((ObjectMap) this.current).put(name, value);
        } else if (this.current instanceof Array) {
            ((Array) this.current).add(value);
        } else {
            this.root = value;
        }
    }

    protected void startObject(String name) {
        ObjectMap value = new ObjectMap();
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
