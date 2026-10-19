package org.codehaus.jackson.io;

import java.lang.ref.SoftReference;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class JsonStringEncoder {
    private static final int INT_0 = 48;
    private static final int INT_BACKSLASH = 92;
    private static final int INT_U = 117;
    private static final int SURR1_FIRST = 55296;
    private static final int SURR1_LAST = 56319;
    private static final int SURR2_FIRST = 56320;
    private static final int SURR2_LAST = 57343;
    protected ByteArrayBuilder _byteBuilder;
    protected final char[] _quoteBuffer = new char[6];
    protected TextBuffer _textBuffer;
    private static final char[] HEX_CHARS = CharTypes.copyHexChars();
    private static final byte[] HEX_BYTES = CharTypes.copyHexBytes();
    protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder = new ThreadLocal<>();

    public JsonStringEncoder() {
        this._quoteBuffer[0] = '\\';
        this._quoteBuffer[2] = '0';
        this._quoteBuffer[3] = '0';
    }

    public static JsonStringEncoder getInstance() {
        SoftReference<JsonStringEncoder> ref = _threadEncoder.get();
        JsonStringEncoder enc = ref == null ? null : ref.get();
        if (enc == null) {
            JsonStringEncoder enc2 = new JsonStringEncoder();
            _threadEncoder.set(new SoftReference<>(enc2));
            return enc2;
        }
        return enc;
    }

    public char[] quoteAsString(String input) {
        TextBuffer textBuffer = this._textBuffer;
        if (textBuffer == null) {
            textBuffer = new TextBuffer(null);
            this._textBuffer = textBuffer;
        }
        char[] outputBuffer = textBuffer.emptyAndGetCurrentSegment();
        int[] escCodes = CharTypes.get7BitOutputEscapes();
        int escCodeCount = escCodes.length;
        int inPtr = 0;
        int inputLen = input.length();
        int outPtr = 0;
        loop0: while (inPtr < inputLen) {
            while (true) {
                char c = input.charAt(inPtr);
                if (c >= escCodeCount || escCodes[c] == 0) {
                    if (outPtr >= outputBuffer.length) {
                        outputBuffer = textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    int outPtr2 = outPtr + 1;
                    outputBuffer[outPtr] = c;
                    inPtr++;
                    if (inPtr >= inputLen) {
                        outPtr = outPtr2;
                        break loop0;
                    }
                    outPtr = outPtr2;
                }
            }
            int inPtr2 = inPtr + 1;
            int escCode = escCodes[input.charAt(inPtr)];
            int length = _appendSingleEscape(escCode, this._quoteBuffer);
            if (outPtr + length > outputBuffer.length) {
                int first = outputBuffer.length - outPtr;
                if (first > 0) {
                    System.arraycopy(this._quoteBuffer, 0, outputBuffer, outPtr, first);
                }
                outputBuffer = textBuffer.finishCurrentSegment();
                int second = length - first;
                System.arraycopy(this._quoteBuffer, first, outputBuffer, outPtr, second);
                outPtr += second;
            } else {
                System.arraycopy(this._quoteBuffer, 0, outputBuffer, outPtr, length);
                outPtr += length;
            }
            inPtr = inPtr2;
        }
        textBuffer.setCurrentLength(outPtr);
        return textBuffer.contentsAsArray();
    }

    public byte[] quoteAsUTF8(String text) {
        int outputPtr;
        int ch;
        int outputPtr2;
        int outputPtr3;
        int outputPtr4;
        ByteArrayBuilder byteBuilder = this._byteBuilder;
        if (byteBuilder == null) {
            byteBuilder = new ByteArrayBuilder((BufferRecycler) null);
            this._byteBuilder = byteBuilder;
        }
        int inputPtr = 0;
        int inputEnd = text.length();
        int outputPtr5 = 0;
        byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
        loop0: while (inputPtr < inputEnd) {
            int[] escCodes = CharTypes.get7BitOutputEscapes();
            while (true) {
                int ch2 = text.charAt(inputPtr);
                if (ch2 > 127 || escCodes[ch2] != 0) {
                    break;
                }
                if (outputPtr5 >= outputBuffer.length) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputPtr5 = 0;
                }
                int outputPtr6 = outputPtr5 + 1;
                outputBuffer[outputPtr5] = (byte) ch2;
                inputPtr++;
                if (inputPtr >= inputEnd) {
                    outputPtr5 = outputPtr6;
                    break loop0;
                }
                outputPtr5 = outputPtr6;
            }
            if (outputPtr5 >= outputBuffer.length) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputPtr5 = 0;
            }
            int inputPtr2 = inputPtr + 1;
            int ch3 = text.charAt(inputPtr);
            if (ch3 <= 127) {
                int escape = escCodes[ch3];
                outputPtr5 = _appendByteEscape(ch3, escape, byteBuilder, outputPtr5);
                outputBuffer = byteBuilder.getCurrentSegment();
                inputPtr = inputPtr2;
            } else {
                if (ch3 <= 2047) {
                    outputBuffer[outputPtr5] = (byte) ((ch3 >> 6) | 192);
                    ch = (ch3 & 63) | 128;
                    outputPtr2 = outputPtr5 + 1;
                    inputPtr = inputPtr2;
                } else if (ch3 < SURR1_FIRST || ch3 > SURR2_LAST) {
                    int outputPtr7 = outputPtr5 + 1;
                    outputBuffer[outputPtr5] = (byte) ((ch3 >> 12) | 224);
                    if (outputPtr7 >= outputBuffer.length) {
                        outputBuffer = byteBuilder.finishCurrentSegment();
                        outputPtr = 0;
                    } else {
                        outputPtr = outputPtr7;
                    }
                    outputBuffer[outputPtr] = (byte) (((ch3 >> 6) & 63) | 128);
                    ch = (ch3 & 63) | 128;
                    outputPtr2 = outputPtr + 1;
                    inputPtr = inputPtr2;
                } else {
                    if (ch3 > SURR1_LAST) {
                        _throwIllegalSurrogate(ch3);
                    }
                    if (inputPtr2 >= inputEnd) {
                        _throwIllegalSurrogate(ch3);
                    }
                    inputPtr = inputPtr2 + 1;
                    int ch4 = _convertSurrogate(ch3, text.charAt(inputPtr2));
                    if (ch4 > 1114111) {
                        _throwIllegalSurrogate(ch4);
                    }
                    int outputPtr8 = outputPtr5 + 1;
                    outputBuffer[outputPtr5] = (byte) ((ch4 >> 18) | 240);
                    if (outputPtr8 >= outputBuffer.length) {
                        outputBuffer = byteBuilder.finishCurrentSegment();
                        outputPtr3 = 0;
                    } else {
                        outputPtr3 = outputPtr8;
                    }
                    int outputPtr9 = outputPtr3 + 1;
                    outputBuffer[outputPtr3] = (byte) (((ch4 >> 12) & 63) | 128);
                    if (outputPtr9 >= outputBuffer.length) {
                        outputBuffer = byteBuilder.finishCurrentSegment();
                        outputPtr4 = 0;
                    } else {
                        outputPtr4 = outputPtr9;
                    }
                    outputBuffer[outputPtr4] = (byte) (((ch4 >> 6) & 63) | 128);
                    ch = (ch4 & 63) | 128;
                    outputPtr2 = outputPtr4 + 1;
                }
                if (outputPtr2 >= outputBuffer.length) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputPtr2 = 0;
                }
                outputBuffer[outputPtr2] = (byte) ch;
                outputPtr5 = outputPtr2 + 1;
            }
        }
        return this._byteBuilder.completeAndCoalesce(outputPtr5);
    }

    public byte[] encodeAsUTF8(String text) {
        int inputPtr;
        int outputPtr;
        int outputPtr2;
        int inputPtr2;
        int outputPtr3;
        ByteArrayBuilder byteBuilder = this._byteBuilder;
        if (byteBuilder == null) {
            byteBuilder = new ByteArrayBuilder((BufferRecycler) null);
            this._byteBuilder = byteBuilder;
        }
        int inputEnd = text.length();
        int outputPtr4 = 0;
        byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
        int outputEnd = outputBuffer.length;
        int inputPtr3 = 0;
        loop0: while (inputPtr3 < inputEnd) {
            int inputPtr4 = inputPtr3 + 1;
            int c = text.charAt(inputPtr3);
            while (true) {
                inputPtr = inputPtr4;
                if (c <= 127) {
                    if (outputPtr4 >= outputEnd) {
                        outputBuffer = byteBuilder.finishCurrentSegment();
                        outputEnd = outputBuffer.length;
                        outputPtr4 = 0;
                    }
                    int outputPtr5 = outputPtr4 + 1;
                    outputBuffer[outputPtr4] = (byte) c;
                    if (inputPtr >= inputEnd) {
                        outputPtr4 = outputPtr5;
                        break loop0;
                    }
                    inputPtr4 = inputPtr + 1;
                    c = text.charAt(inputPtr);
                    outputPtr4 = outputPtr5;
                }
            }
            if (outputPtr4 >= outputEnd) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputEnd = outputBuffer.length;
                outputPtr = 0;
            } else {
                outputPtr = outputPtr4;
            }
            if (c < 2048) {
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) ((c >> 6) | 192);
                inputPtr2 = inputPtr;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                int outputPtr6 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) ((c >> 12) | 224);
                if (outputPtr6 >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr6 = 0;
                }
                outputBuffer[outputPtr6] = (byte) (((c >> 6) & 63) | 128);
                outputPtr2 = outputPtr6 + 1;
                inputPtr2 = inputPtr;
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (inputPtr >= inputEnd) {
                    _throwIllegalSurrogate(c);
                }
                inputPtr2 = inputPtr + 1;
                c = _convertSurrogate(c, text.charAt(inputPtr));
                if (c > 1114111) {
                    _throwIllegalSurrogate(c);
                }
                int outputPtr7 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) ((c >> 18) | 240);
                if (outputPtr7 >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr7 = 0;
                }
                int outputPtr8 = outputPtr7 + 1;
                outputBuffer[outputPtr7] = (byte) (((c >> 12) & 63) | 128);
                if (outputPtr8 >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr3 = 0;
                } else {
                    outputPtr3 = outputPtr8;
                }
                outputBuffer[outputPtr3] = (byte) (((c >> 6) & 63) | 128);
                outputPtr2 = outputPtr3 + 1;
            }
            if (outputPtr2 >= outputEnd) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputEnd = outputBuffer.length;
                outputPtr2 = 0;
            }
            outputBuffer[outputPtr2] = (byte) ((c & 63) | 128);
            outputPtr4 = outputPtr2 + 1;
            inputPtr3 = inputPtr2;
        }
        return this._byteBuilder.completeAndCoalesce(outputPtr4);
    }

    private int _appendSingleEscape(int escCode, char[] quoteBuffer) {
        if (escCode < 0) {
            int value = -(escCode + 1);
            quoteBuffer[1] = 'u';
            quoteBuffer[4] = HEX_CHARS[value >> 4];
            quoteBuffer[5] = HEX_CHARS[value & 15];
            return 6;
        }
        quoteBuffer[1] = (char) escCode;
        return 2;
    }

    private int _appendByteEscape(int ch, int escCode, ByteArrayBuilder byteBuilder, int ptr) {
        byteBuilder.setCurrentSegmentLength(ptr);
        byteBuilder.append(92);
        if (escCode < 0) {
            byteBuilder.append(117);
            if (ch > 255) {
                int hi = ch >> 8;
                byteBuilder.append(HEX_BYTES[hi >> 4]);
                byteBuilder.append(HEX_BYTES[hi & 15]);
                ch &= 255;
            } else {
                byteBuilder.append(48);
                byteBuilder.append(48);
            }
            byteBuilder.append(HEX_BYTES[ch >> 4]);
            byteBuilder.append(HEX_BYTES[ch & 15]);
        } else {
            byteBuilder.append((byte) escCode);
        }
        return byteBuilder.getCurrentSegmentLength();
    }

    private int _convertSurrogate(int firstPart, int secondPart) {
        if (secondPart < SURR2_FIRST || secondPart > SURR2_LAST) {
            throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
        }
        return 65536 + ((firstPart - SURR1_FIRST) << 10) + (secondPart - SURR2_FIRST);
    }

    private void _throwIllegalSurrogate(int code) {
        if (code > 1114111) {
            throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
        }
        if (code >= SURR1_FIRST) {
            if (code <= SURR1_LAST) {
                throw new IllegalArgumentException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
            }
            throw new IllegalArgumentException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        }
        throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
    }
}
