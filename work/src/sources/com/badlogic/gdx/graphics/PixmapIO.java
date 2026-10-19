package com.badlogic.gdx.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/* JADX INFO: loaded from: classes.dex */
public class PixmapIO {
    public static void writeCIM(FileHandle file, Pixmap pixmap) throws Throwable {
        CIM.write(file, pixmap);
    }

    public static Pixmap readCIM(FileHandle file) {
        return CIM.read(file);
    }

    public static void writePNG(FileHandle file, Pixmap pixmap) {
        try {
            file.writeBytes(PNG.write(pixmap), false);
        } catch (IOException ex) {
            throw new GdxRuntimeException("Error writing PNG: " + file, ex);
        }
    }

    private static class CIM {
        private static final int BUFFER_SIZE = 32000;
        private static final byte[] writeBuffer = new byte[BUFFER_SIZE];
        private static final byte[] readBuffer = new byte[BUFFER_SIZE];

        private CIM() {
        }

        public static void write(FileHandle file, Pixmap pixmap) throws Throwable {
            DataOutputStream out = null;
            try {
                try {
                    DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(file.write(false));
                    DataOutputStream out2 = new DataOutputStream(deflaterOutputStream);
                    try {
                        out2.writeInt(pixmap.getWidth());
                        out2.writeInt(pixmap.getHeight());
                        out2.writeInt(Pixmap.Format.toGdx2DPixmapFormat(pixmap.getFormat()));
                        ByteBuffer pixelBuf = pixmap.getPixels();
                        pixelBuf.position(0);
                        pixelBuf.limit(pixelBuf.capacity());
                        int remainingBytes = pixelBuf.capacity() % BUFFER_SIZE;
                        int iterations = pixelBuf.capacity() / BUFFER_SIZE;
                        synchronized (writeBuffer) {
                            for (int i = 0; i < iterations; i++) {
                                pixelBuf.get(writeBuffer);
                                out2.write(writeBuffer);
                            }
                            pixelBuf.get(writeBuffer, 0, remainingBytes);
                            out2.write(writeBuffer, 0, remainingBytes);
                        }
                        pixelBuf.position(0);
                        pixelBuf.limit(pixelBuf.capacity());
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        out = out2;
                        throw new GdxRuntimeException("Couldn't write Pixmap to file '" + file + "'", e);
                    } catch (Throwable th) {
                        th = th;
                        out = out2;
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e3) {
                            }
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    e = e4;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }

        public static Pixmap read(FileHandle file) throws Throwable {
            DataInputStream in = null;
            try {
                try {
                    DataInputStream in2 = new DataInputStream(new InflaterInputStream(new BufferedInputStream(file.read())));
                    try {
                        int width = in2.readInt();
                        int height = in2.readInt();
                        Pixmap.Format format = Pixmap.Format.fromGdx2DPixmapFormat(in2.readInt());
                        Pixmap pixmap = new Pixmap(width, height, format);
                        ByteBuffer pixelBuf = pixmap.getPixels();
                        pixelBuf.position(0);
                        pixelBuf.limit(pixelBuf.capacity());
                        synchronized (readBuffer) {
                            while (true) {
                                int readBytes = in2.read(readBuffer);
                                if (readBytes <= 0) {
                                    break;
                                }
                                pixelBuf.put(readBuffer, 0, readBytes);
                            }
                        }
                        pixelBuf.position(0);
                        pixelBuf.limit(pixelBuf.capacity());
                        if (in2 != null) {
                            try {
                                in2.close();
                            } catch (Exception e) {
                            }
                        }
                        return pixmap;
                    } catch (Exception e2) {
                        e = e2;
                        in = in2;
                        throw new GdxRuntimeException("Couldn't read Pixmap from file '" + file + "'", e);
                    } catch (Throwable th) {
                        th = th;
                        in = in2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e3) {
                            }
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    e = e4;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    private static class PNG {
        static final int ZLIB_BLOCK_SIZE = 32000;
        static int[] crcTable;

        private PNG() {
        }

        static byte[] write(Pixmap pixmap) throws IOException {
            byte[] signature = {-119, 80, 78, 71, 13, 10, 26, 10};
            byte[] header = createHeaderChunk(pixmap.getWidth(), pixmap.getHeight());
            byte[] data = createDataChunk(pixmap);
            byte[] trailer = createTrailerChunk();
            ByteArrayOutputStream png = new ByteArrayOutputStream(signature.length + header.length + data.length + trailer.length);
            png.write(signature);
            png.write(header);
            png.write(data);
            png.write(trailer);
            return png.toByteArray();
        }

        private static byte[] createHeaderChunk(int width, int height) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(13);
            DataOutputStream chunk = new DataOutputStream(baos);
            chunk.writeInt(width);
            chunk.writeInt(height);
            chunk.writeByte(8);
            chunk.writeByte(6);
            chunk.writeByte(0);
            chunk.writeByte(0);
            chunk.writeByte(0);
            return toChunk("IHDR", baos.toByteArray());
        }

        private static byte[] createDataChunk(Pixmap pixmap) throws IOException {
            int width = pixmap.getWidth();
            int height = pixmap.getHeight();
            byte[] raw = new byte[(width * 4 * height) + height];
            int dest = 0;
            for (int y = 0; y < height; y++) {
                raw[dest] = 0;
                dest++;
                for (int x = 0; x < width; x++) {
                    int pixel = pixmap.getPixel(x, y);
                    int mask = pixel & (-1);
                    int rr = (mask >> 24) & 255;
                    int gg = (mask >> 16) & 255;
                    int bb = (mask >> 8) & 255;
                    int aa = mask & 255;
                    int dest2 = dest + 1;
                    raw[dest] = (byte) rr;
                    int dest3 = dest2 + 1;
                    raw[dest2] = (byte) gg;
                    int dest4 = dest3 + 1;
                    raw[dest3] = (byte) bb;
                    dest = dest4 + 1;
                    raw[dest4] = (byte) aa;
                }
            }
            return toChunk("IDAT", toZLIB(raw));
        }

        private static byte[] createTrailerChunk() throws IOException {
            return toChunk("IEND", new byte[0]);
        }

        private static byte[] toChunk(String id, byte[] raw) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(raw.length + 12);
            DataOutputStream chunk = new DataOutputStream(baos);
            chunk.writeInt(raw.length);
            byte[] bid = new byte[4];
            for (int i = 0; i < 4; i++) {
                bid[i] = (byte) id.charAt(i);
            }
            chunk.write(bid);
            chunk.write(raw);
            int crc = updateCRC(-1, bid);
            chunk.writeInt(updateCRC(crc, raw) ^ (-1));
            return baos.toByteArray();
        }

        private static void createCRCTable() {
            crcTable = new int[256];
            for (int i = 0; i < 256; i++) {
                int c = i;
                for (int k = 0; k < 8; k++) {
                    c = (c & 1) > 0 ? (-306674912) ^ (c >>> 1) : c >>> 1;
                }
                crcTable[i] = c;
            }
        }

        private static int updateCRC(int crc, byte[] raw) {
            if (crcTable == null) {
                createCRCTable();
            }
            for (byte element : raw) {
                crc = crcTable[(crc ^ element) & 255] ^ (crc >>> 8);
            }
            return crc;
        }

        private static byte[] toZLIB(byte[] raw) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(raw.length + 6 + ((raw.length / ZLIB_BLOCK_SIZE) * 5));
            DataOutputStream zlib = new DataOutputStream(baos);
            zlib.writeByte(8);
            zlib.writeByte(29);
            int pos = 0;
            while (raw.length - pos > ZLIB_BLOCK_SIZE) {
                writeUncompressedDeflateBlock(zlib, false, raw, pos, (char) 32000);
                pos += ZLIB_BLOCK_SIZE;
            }
            writeUncompressedDeflateBlock(zlib, true, raw, pos, (char) (raw.length - pos));
            zlib.writeInt(calcADLER32(raw));
            return baos.toByteArray();
        }

        private static void writeUncompressedDeflateBlock(DataOutputStream zlib, boolean last, byte[] raw, int off, char len) throws IOException {
            zlib.writeByte((byte) (last ? 1 : 0));
            zlib.writeByte((byte) (len & 255));
            zlib.writeByte((byte) ((len & 65280) >> 8));
            zlib.writeByte((byte) ((len ^ (-1)) & 255));
            zlib.writeByte((byte) (((len ^ (-1)) & 65280) >> 8));
            zlib.write(raw, off, len);
        }

        private static int calcADLER32(byte[] bArr) {
            int s1 = 1;
            int s2 = 0;
            for (int i = 0; i < bArr.length; i++) {
                s1 = (s1 + (bArr[i] >= 0 ? bArr[i] : bArr[i] + 256)) % 65521;
                s2 = (s2 + s1) % 65521;
            }
            return (s2 << 16) + s1;
        }
    }
}
