package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class FreeType {
    public static int FT_PIXEL_MODE_NONE = 0;
    public static int FT_PIXEL_MODE_MONO = 1;
    public static int FT_PIXEL_MODE_GRAY = 2;
    public static int FT_PIXEL_MODE_GRAY2 = 3;
    public static int FT_PIXEL_MODE_GRAY4 = 4;
    public static int FT_PIXEL_MODE_LCD = 5;
    public static int FT_PIXEL_MODE_LCD_V = 6;
    public static int FT_ENCODING_NONE = 0;
    public static int FT_ENCODING_MS_SYMBOL = encode('s', 'y', 'm', 'b');
    public static int FT_ENCODING_UNICODE = encode('u', 'n', 'i', 'c');
    public static int FT_ENCODING_SJIS = encode('s', 'j', 'i', 's');
    public static int FT_ENCODING_GB2312 = encode('g', 'b', ' ', ' ');
    public static int FT_ENCODING_BIG5 = encode('b', 'i', 'g', '5');
    public static int FT_ENCODING_WANSUNG = encode('w', 'a', 'n', 's');
    public static int FT_ENCODING_JOHAB = encode('j', 'o', 'h', 'a');
    public static int FT_ENCODING_ADOBE_STANDARD = encode('A', 'D', 'O', 'B');
    public static int FT_ENCODING_ADOBE_EXPERT = encode('A', 'D', 'B', 'E');
    public static int FT_ENCODING_ADOBE_CUSTOM = encode('A', 'D', 'B', 'C');
    public static int FT_ENCODING_ADOBE_LATIN_1 = encode('l', 'a', 't', '1');
    public static int FT_ENCODING_OLD_LATIN_2 = encode('l', 'a', 't', '2');
    public static int FT_ENCODING_APPLE_ROMAN = encode('a', 'r', 'm', 'n');
    public static int FT_FACE_FLAG_SCALABLE = 1;
    public static int FT_FACE_FLAG_FIXED_SIZES = 2;
    public static int FT_FACE_FLAG_FIXED_WIDTH = 4;
    public static int FT_FACE_FLAG_SFNT = 8;
    public static int FT_FACE_FLAG_HORIZONTAL = 16;
    public static int FT_FACE_FLAG_VERTICAL = 32;
    public static int FT_FACE_FLAG_KERNING = 64;
    public static int FT_FACE_FLAG_FAST_GLYPHS = 128;
    public static int FT_FACE_FLAG_MULTIPLE_MASTERS = 256;
    public static int FT_FACE_FLAG_GLYPH_NAMES = 512;
    public static int FT_FACE_FLAG_EXTERNAL_STREAM = 1024;
    public static int FT_FACE_FLAG_HINTER = 2048;
    public static int FT_FACE_FLAG_CID_KEYED = Opcodes.ACC_SYNTHETIC;
    public static int FT_FACE_FLAG_TRICKY = Opcodes.ACC_ANNOTATION;
    public static int FT_STYLE_FLAG_ITALIC = 1;
    public static int FT_STYLE_FLAG_BOLD = 2;
    public static int FT_LOAD_DEFAULT = 0;
    public static int FT_LOAD_NO_SCALE = 1;
    public static int FT_LOAD_NO_HINTING = 2;
    public static int FT_LOAD_RENDER = 4;
    public static int FT_LOAD_NO_BITMAP = 8;
    public static int FT_LOAD_VERTICAL_LAYOUT = 16;
    public static int FT_LOAD_FORCE_AUTOHINT = 32;
    public static int FT_LOAD_CROP_BITMAP = 64;
    public static int FT_LOAD_PEDANTIC = 128;
    public static int FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH = 512;
    public static int FT_LOAD_NO_RECURSE = 1024;
    public static int FT_LOAD_IGNORE_TRANSFORM = 2048;
    public static int FT_LOAD_MONOCHROME = Opcodes.ACC_SYNTHETIC;
    public static int FT_LOAD_LINEAR_DESIGN = Opcodes.ACC_ANNOTATION;
    public static int FT_LOAD_NO_AUTOHINT = GL20.GL_COVERAGE_BUFFER_BIT_NV;
    public static int FT_RENDER_MODE_NORMAL = 0;
    public static int FT_RENDER_MODE_LIGHT = 1;
    public static int FT_RENDER_MODE_MONO = 2;
    public static int FT_RENDER_MODE_LCD = 3;
    public static int FT_RENDER_MODE_LCD_V = 4;
    public static int FT_RENDER_MODE_MAX = 5;
    public static int FT_KERNING_DEFAULT = 0;
    public static int FT_KERNING_UNFITTED = 1;
    public static int FT_KERNING_UNSCALED = 2;

    private static native void doneFace(long j);

    private static native void doneFreeType(long j);

    private static native int getCharIndex(long j, int i);

    private static native int getKerning(long j, int i, int i2, int i3);

    private static native boolean hasKerning(long j);

    private static native long initFreeTypeJni();

    private static native boolean loadChar(long j, int i, int i2);

    private static native boolean loadGlyph(long j, int i, int i2);

    private static native long newMemoryFace(long j, ByteBuffer byteBuffer, int i, int i2);

    private static native boolean renderGlyph(long j, int i);

    private static native boolean selectSize(long j, int i);

    private static native boolean setCharSize(long j, int i, int i2, int i3, int i4);

    private static native boolean setPixelSizes(long j, int i, int i2);

    private static class Pointer {
        long address;

        Pointer(long address) {
            this.address = address;
        }
    }

    public static class Library extends Pointer {
        LongMap<ByteBuffer> fontData;

        Library(long address) {
            super(address);
            this.fontData = new LongMap<>();
        }
    }

    public static class Face extends Pointer {
        Library library;

        private static native int getAscender(long j);

        private static native int getDescender(long j);

        private static native int getFaceFlags(long j);

        private static native long getGlyph(long j);

        private static native int getHeight(long j);

        private static native int getMaxAdvanceHeight(long j);

        private static native int getMaxAdvanceWidth(long j);

        private static native int getNumGlyphs(long j);

        private static native long getSize(long j);

        private static native int getStyleFlags(long j);

        private static native int getUnderlinePosition(long j);

        private static native int getUnderlineThickness(long j);

        public Face(long address, Library library) {
            super(address);
            this.library = library;
        }

        public int getFaceFlags() {
            return getFaceFlags(this.address);
        }

        public int getStyleFlags() {
            return getStyleFlags(this.address);
        }

        public int getNumGlyphs() {
            return getNumGlyphs(this.address);
        }

        public int getAscender() {
            return getAscender(this.address);
        }

        public int getDescender() {
            return getDescender(this.address);
        }

        public int getHeight() {
            return getHeight(this.address);
        }

        public int getMaxAdvanceWidth() {
            return getMaxAdvanceWidth(this.address);
        }

        public int getMaxAdvanceHeight() {
            return getMaxAdvanceHeight(this.address);
        }

        public int getUnderlinePosition() {
            return getUnderlinePosition(this.address);
        }

        public int getUnderlineThickness() {
            return getUnderlineThickness(this.address);
        }

        public GlyphSlot getGlyph() {
            return new GlyphSlot(getGlyph(this.address));
        }

        public Size getSize() {
            return new Size(getSize(this.address));
        }
    }

    public static class Size extends Pointer {
        private static native long getMetrics(long j);

        Size(long address) {
            super(address);
        }

        public SizeMetrics getMetrics() {
            return new SizeMetrics(getMetrics(this.address));
        }
    }

    public static class SizeMetrics extends Pointer {
        private static native int getAscender(long j);

        private static native int getDescender(long j);

        private static native int getHeight(long j);

        private static native int getMaxAdvance(long j);

        private static native int getXppem(long j);

        private static native int getXscale(long j);

        private static native int getYppem(long j);

        private static native int getYscale(long j);

        SizeMetrics(long address) {
            super(address);
        }

        public int getXppem() {
            return getXppem(this.address);
        }

        public int getYppem() {
            return getYppem(this.address);
        }

        public int getXScale() {
            return getXscale(this.address);
        }

        public int getYscale() {
            return getYscale(this.address);
        }

        public int getAscender() {
            return getAscender(this.address);
        }

        public int getDescender() {
            return getDescender(this.address);
        }

        public int getHeight() {
            return getHeight(this.address);
        }

        public int getMaxAdvance() {
            return getMaxAdvance(this.address);
        }
    }

    public static class GlyphSlot extends Pointer {
        private static native int getAdvanceX(long j);

        private static native int getAdvanceY(long j);

        private static native long getBitmap(long j);

        private static native int getBitmapLeft(long j);

        private static native int getBitmapTop(long j);

        private static native int getFormat(long j);

        private static native int getLinearHoriAdvance(long j);

        private static native int getLinearVertAdvance(long j);

        private static native long getMetrics(long j);

        GlyphSlot(long address) {
            super(address);
        }

        public GlyphMetrics getMetrics() {
            return new GlyphMetrics(getMetrics(this.address));
        }

        public int getLinearHoriAdvance() {
            return getLinearHoriAdvance(this.address);
        }

        public int getLinearVertAdvance() {
            return getLinearVertAdvance(this.address);
        }

        public int getAdvanceX() {
            return getAdvanceX(this.address);
        }

        public int getAdvanceY() {
            return getAdvanceY(this.address);
        }

        public int getFormat() {
            return getFormat(this.address);
        }

        public Bitmap getBitmap() {
            return new Bitmap(getBitmap(this.address));
        }

        public int getBitmapLeft() {
            return getBitmapLeft(this.address);
        }

        public int getBitmapTop() {
            return getBitmapTop(this.address);
        }
    }

    public static class Bitmap extends Pointer {
        private static native ByteBuffer getBuffer(long j);

        private static native int getNumGray(long j);

        private static native int getPitch(long j);

        private static native int getPixelMode(long j);

        private static native int getRows(long j);

        private static native int getWidth(long j);

        Bitmap(long address) {
            super(address);
        }

        public int getRows() {
            return getRows(this.address);
        }

        public int getWidth() {
            return getWidth(this.address);
        }

        public int getPitch() {
            return getPitch(this.address);
        }

        public ByteBuffer getBuffer() {
            return getRows() == 0 ? BufferUtils.newByteBuffer(1) : getBuffer(this.address);
        }

        public Pixmap getPixmap(Pixmap.Format format) {
            Pixmap pixmap = new Pixmap(getWidth(), getRows(), Pixmap.Format.Alpha);
            BufferUtils.copy(getBuffer(), pixmap.getPixels(), pixmap.getPixels().capacity());
            Pixmap converted = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), format);
            Pixmap.Blending blending = Pixmap.getBlending();
            Pixmap.setBlending(Pixmap.Blending.None);
            converted.drawPixmap(pixmap, 0, 0);
            Pixmap.setBlending(blending);
            pixmap.dispose();
            return converted;
        }

        public int getNumGray() {
            return getNumGray(this.address);
        }

        public int getPixelMode() {
            return getPixelMode(this.address);
        }
    }

    public static class GlyphMetrics extends Pointer {
        private static native int getHeight(long j);

        private static native int getHoriAdvance(long j);

        private static native int getHoriBearingX(long j);

        private static native int getHoriBearingY(long j);

        private static native int getVertAdvance(long j);

        private static native int getVertBearingX(long j);

        private static native int getVertBearingY(long j);

        private static native int getWidth(long j);

        GlyphMetrics(long address) {
            super(address);
        }

        public int getWidth() {
            return getWidth(this.address);
        }

        public int getHeight() {
            return getHeight(this.address);
        }

        public int getHoriBearingX() {
            return getHoriBearingX(this.address);
        }

        public int getHoriBearingY() {
            return getHoriBearingY(this.address);
        }

        public int getHoriAdvance() {
            return getHoriAdvance(this.address);
        }

        public int getVertBearingX() {
            return getVertBearingX(this.address);
        }

        public int getVertBearingY() {
            return getVertBearingY(this.address);
        }

        public int getVertAdvance() {
            return getVertAdvance(this.address);
        }
    }

    private static int encode(char a, char b, char c, char d) {
        return (a << 24) | (b << 16) | (c << '\b') | d;
    }

    public static Library initFreeType() {
        new SharedLibraryLoader().load("gdx-freetype");
        long address = initFreeTypeJni();
        if (address == 0) {
            throw new GdxRuntimeException("Couldn't initialize FreeType library");
        }
        return new Library(address);
    }

    public static void doneFreeType(Library library) {
        doneFreeType(library.address);
        for (ByteBuffer buffer : library.fontData.values()) {
            BufferUtils.disposeUnsafeByteBuffer(buffer);
        }
    }

    public static Face newFace(Library library, FileHandle font, int faceIndex) throws Throwable {
        byte[] data = font.readBytes();
        return newMemoryFace(library, data, data.length, faceIndex);
    }

    public static Face newMemoryFace(Library library, byte[] data, int dataSize, int faceIndex) {
        ByteBuffer buffer = BufferUtils.newUnsafeByteBuffer(data.length);
        BufferUtils.copy(data, 0, (Buffer) buffer, data.length);
        long address = newMemoryFace(library.address, buffer, dataSize, faceIndex);
        if (address == 0) {
            BufferUtils.disposeUnsafeByteBuffer(buffer);
            throw new GdxRuntimeException("Couldn't load font");
        }
        library.fontData.put(address, buffer);
        return new Face(address, library);
    }

    public static void doneFace(Face face) {
        doneFace(face.address);
        ByteBuffer buffer = face.library.fontData.get(face.address);
        if (buffer != null) {
            face.library.fontData.remove(face.address);
            BufferUtils.disposeUnsafeByteBuffer(buffer);
        }
    }

    public static boolean selectSize(Face face, int strikeIndex) {
        return selectSize(face.address, strikeIndex);
    }

    public static boolean setCharSize(Face face, int charWidth, int charHeight, int horzResolution, int vertResolution) {
        return setCharSize(face.address, charWidth, charHeight, horzResolution, vertResolution);
    }

    public static boolean setPixelSizes(Face face, int pixelWidth, int pixelHeight) {
        return setPixelSizes(face.address, pixelWidth, pixelHeight);
    }

    public static boolean loadGlyph(Face face, int glyphIndex, int loadFlags) {
        return loadGlyph(face.address, glyphIndex, loadFlags);
    }

    public static boolean loadChar(Face face, int charCode, int loadFlags) {
        return loadChar(face.address, charCode, loadFlags);
    }

    public static boolean renderGlyph(GlyphSlot slot, int renderMode) {
        return renderGlyph(slot.address, renderMode);
    }

    public static boolean hasKerning(Face face) {
        return hasKerning(face.address);
    }

    public static int getKerning(Face face, int leftGlyph, int rightGlyph, int kernMode) {
        return getKerning(face.address, leftGlyph, rightGlyph, kernMode);
    }

    public static int getCharIndex(Face face, int charCode) {
        return getCharIndex(face.address, charCode);
    }

    public static int toInt(int value) {
        return value < 0 ? (value - 32) >> 6 : (value + 32) >> 6;
    }
}
