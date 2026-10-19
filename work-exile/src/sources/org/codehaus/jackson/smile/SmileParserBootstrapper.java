package org.codehaus.jackson.smile;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SmileParserBootstrapper {
    private final boolean _bufferRecyclable;
    final IOContext _context;
    final InputStream _in;
    final byte[] _inputBuffer;
    private int _inputEnd;
    protected int _inputProcessed;
    private int _inputPtr;

    public SmileParserBootstrapper(IOContext ctxt, InputStream in) {
        this._context = ctxt;
        this._in = in;
        this._inputBuffer = ctxt.allocReadIOBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._inputProcessed = 0;
        this._bufferRecyclable = true;
    }

    public SmileParserBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen) {
        this._context = ctxt;
        this._in = null;
        this._inputBuffer = inputBuffer;
        this._inputPtr = inputStart;
        this._inputEnd = inputStart + inputLen;
        this._inputProcessed = -inputStart;
        this._bufferRecyclable = false;
    }

    public SmileParser constructParser(int generalParserFeatures, int smileFeatures, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols) throws IOException {
        String msg;
        boolean intern = JsonParser.Feature.INTERN_FIELD_NAMES.enabledIn(generalParserFeatures);
        BytesToNameCanonicalizer can = rootByteSymbols.makeChild(true, intern);
        ensureLoaded(1);
        SmileParser p = new SmileParser(this._context, generalParserFeatures, smileFeatures, codec, can, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        boolean hadSig = false;
        if (this._inputPtr < this._inputEnd && this._inputBuffer[this._inputPtr] == 58) {
            hadSig = p.handleSignature(true, true);
        }
        if (!hadSig && (SmileParser.Feature.REQUIRE_HEADER.getMask() & smileFeatures) != 0) {
            byte firstByte = this._inputPtr < this._inputEnd ? this._inputBuffer[this._inputPtr] : (byte) 0;
            if (firstByte == 123 || firstByte == 91) {
                msg = "Input does not start with Smile format header (first byte = 0x" + Integer.toHexString(firstByte & 255) + ") -- rather, it starts with '" + ((char) firstByte) + "' (plain JSON input?) -- can not parse";
            } else {
                msg = "Input does not start with Smile format header (first byte = 0x" + Integer.toHexString(firstByte & 255) + ") and parser has REQUIRE_HEADER enabled: can not parse";
            }
            throw new JsonParseException(msg, JsonLocation.NA);
        }
        return p;
    }

    public static MatchStrength hasSmileFormat(InputAccessor acc) throws IOException {
        if (!acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        byte b1 = acc.nextByte();
        if (!acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        byte b2 = acc.nextByte();
        if (b1 == 58) {
            if (b2 != 41) {
                return MatchStrength.NO_MATCH;
            }
            if (acc.hasMoreBytes()) {
                return acc.nextByte() == 10 ? MatchStrength.FULL_MATCH : MatchStrength.NO_MATCH;
            }
            return MatchStrength.INCONCLUSIVE;
        }
        if (b1 == -6) {
            if (b2 == 52) {
                return MatchStrength.SOLID_MATCH;
            }
            int ch = b2 & 255;
            if (ch >= 128 && ch < 248) {
                return MatchStrength.SOLID_MATCH;
            }
            return MatchStrength.NO_MATCH;
        }
        if (b1 == -8) {
            if (!acc.hasMoreBytes()) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (likelySmileValue(b2) || possibleSmileValue(b2, true)) {
                return MatchStrength.SOLID_MATCH;
            }
            return MatchStrength.NO_MATCH;
        }
        if (likelySmileValue(b1) || possibleSmileValue(b2, false)) {
            return MatchStrength.SOLID_MATCH;
        }
        return MatchStrength.NO_MATCH;
    }

    private static boolean likelySmileValue(byte b) {
        int ch = b & 255;
        if (ch < 224) {
            return ch >= 128 && ch <= 159;
        }
        switch (ch) {
            case -8:
            case -6:
            case 224:
            case SmileConstants.TOKEN_MISC_LONG_TEXT_UNICODE /* 228 */:
            case SmileConstants.TOKEN_MISC_BINARY_7BIT /* 232 */:
                return true;
            default:
                return false;
        }
    }

    private static boolean possibleSmileValue(byte b, boolean lenient) {
        int ch = b & 255;
        if (ch >= 128) {
            return ch <= 224;
        }
        if (lenient) {
            if (ch >= 64) {
                return true;
            }
            if (ch > -32) {
                return ch < 44;
            }
        }
        return false;
    }

    protected boolean ensureLoaded(int minimum) throws IOException {
        if (this._in == null) {
            return false;
        }
        int gotten = this._inputEnd - this._inputPtr;
        while (gotten < minimum) {
            int count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            if (count < 1) {
                return false;
            }
            this._inputEnd += count;
            gotten += count;
        }
        return true;
    }
}
