package com.badlogic.gdx.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class DataInput extends DataInputStream {
    private char[] chars;

    public DataInput(InputStream in) {
        super(in);
        this.chars = new char[32];
    }

    public int readInt(boolean optimizePositive) throws IOException {
        int b = read();
        int result = b & Opcodes.LAND;
        if ((b & 128) != 0) {
            int b2 = read();
            result |= (b2 & Opcodes.LAND) << 7;
            if ((b2 & 128) != 0) {
                int b3 = read();
                result |= (b3 & Opcodes.LAND) << 14;
                if ((b3 & 128) != 0) {
                    int b4 = read();
                    result |= (b4 & Opcodes.LAND) << 21;
                    if ((b4 & 128) != 0) {
                        result |= (read() & Opcodes.LAND) << 28;
                    }
                }
            }
        }
        return optimizePositive ? result : (result >>> 1) ^ (-(result & 1));
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0024  */
    public String readString() throws IOException {
        int charCount = readInt(true);
        switch (charCount) {
            case 0:
                return null;
            case 1:
                return "";
            default:
                int charCount2 = charCount - 1;
                if (this.chars.length < charCount2) {
                    this.chars = new char[charCount2];
                }
                char[] chars = this.chars;
                int b = 0;
                for (int charIndex = 0; charIndex < charCount2; charIndex++) {
                    b = read();
                    if (b <= 127) {
                        chars[charIndex] = (char) b;
                    } else {
                        if (charIndex < charCount2) {
                            readUtf8_slow(charCount2, charIndex, b);
                        }
                        return new String(chars, 0, charCount2);
                    }
                }
                if (charIndex < charCount2) {
                    readUtf8_slow(charCount2, charIndex, b);
                }
                return new String(chars, 0, charCount2);
        }
    }

    private void readUtf8_slow(int charCount, int charIndex, int b) throws IOException {
        char[] chars = this.chars;
        while (true) {
            switch (b >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    chars[charIndex] = (char) b;
                    break;
                case 12:
                case 13:
                    chars[charIndex] = (char) (((b & 31) << 6) | (read() & 63));
                    break;
                case 14:
                    chars[charIndex] = (char) (((b & 15) << 12) | ((read() & 63) << 6) | (read() & 63));
                    break;
            }
            charIndex++;
            if (charIndex < charCount) {
                b = read() & 255;
            } else {
                return;
            }
        }
    }
}
