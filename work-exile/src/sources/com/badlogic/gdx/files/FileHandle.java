package com.badlogic.gdx.files;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FileHandle {
    protected File file;
    protected Files.FileType type;

    protected FileHandle() {
    }

    public FileHandle(String fileName) {
        this.file = new File(fileName);
        this.type = Files.FileType.Absolute;
    }

    public FileHandle(File file) {
        this.file = file;
        this.type = Files.FileType.Absolute;
    }

    protected FileHandle(String fileName, Files.FileType type) {
        this.type = type;
        this.file = new File(fileName);
    }

    protected FileHandle(File file, Files.FileType type) {
        this.file = file;
        this.type = type;
    }

    public String path() {
        return this.file.getPath();
    }

    public String name() {
        return this.file.getName();
    }

    public String extension() {
        String name = this.file.getName();
        int dotIndex = name.lastIndexOf(46);
        return dotIndex == -1 ? "" : name.substring(dotIndex + 1);
    }

    public String nameWithoutExtension() {
        String name = this.file.getName();
        int dotIndex = name.lastIndexOf(46);
        return dotIndex == -1 ? name : name.substring(0, dotIndex);
    }

    public Files.FileType type() {
        return this.type;
    }

    private File file() {
        return this.type == Files.FileType.External ? new File(Gdx.files.getExternalStoragePath(), this.file.getPath()) : this.file;
    }

    public InputStream read() {
        if (this.type == Files.FileType.Classpath || (this.type == Files.FileType.Internal && !this.file.exists())) {
            InputStream input = FileHandle.class.getResourceAsStream("/" + this.file.getPath().replace('\\', '/'));
            if (input == null) {
                throw new GdxRuntimeException("File not found: " + this.file + " (" + this.type + ")");
            }
            return input;
        }
        try {
            return new FileInputStream(file());
        } catch (FileNotFoundException ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
            }
            throw new GdxRuntimeException("Error reading file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public String readString() {
        return readString(null);
    }

    public String readString(String charset) {
        StringBuilder output = new StringBuilder(512);
        InputStreamReader reader = null;
        try {
            try {
                if (charset == null) {
                    reader = new InputStreamReader(read());
                } else {
                    reader = new InputStreamReader(read(), charset);
                }
                char[] buffer = new char[256];
                while (true) {
                    int length = reader.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    output.append(buffer, 0, length);
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
                return output.toString();
            } catch (IOException ex) {
                throw new GdxRuntimeException("Error reading layout file: " + this, ex);
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0053 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public byte[] readBytes() {
        /*
            r10 = this;
            r9 = 0
            long r7 = r10.length()
            int r4 = (int) r7
            if (r4 != 0) goto La
            r4 = 512(0x200, float:7.17E-43)
        La:
            byte[] r0 = new byte[r4]
            r6 = 0
            java.io.InputStream r3 = r10.read()
        L11:
            int r7 = r0.length     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L53
            int r7 = r7 - r6
            int r1 = r3.read(r0, r6, r7)     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L53
            r7 = -1
            if (r1 != r7) goto L29
            if (r3 == 0) goto L1f
            r3.close()     // Catch: java.io.IOException -> L5a
        L1f:
            int r7 = r0.length
            if (r6 >= r7) goto L28
            byte[] r5 = new byte[r6]
            java.lang.System.arraycopy(r0, r9, r5, r9, r6)
            r0 = r5
        L28:
            return r0
        L29:
            int r6 = r6 + r1
            int r7 = r0.length     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L53
            if (r6 != r7) goto L11
            int r7 = r0.length     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L53
            int r7 = r7 * 2
            byte[] r5 = new byte[r7]     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L53
            r7 = 0
            r8 = 0
            java.lang.System.arraycopy(r0, r7, r5, r8, r6)     // Catch: java.io.IOException -> L39 java.lang.Throwable -> L53
            r0 = r5
            goto L11
        L39:
            r2 = move-exception
            com.badlogic.gdx.utils.GdxRuntimeException r7 = new com.badlogic.gdx.utils.GdxRuntimeException     // Catch: java.lang.Throwable -> L53
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L53
            r8.<init>()     // Catch: java.lang.Throwable -> L53
            java.lang.String r9 = "Error reading file: "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch: java.lang.Throwable -> L53
            java.lang.StringBuilder r8 = r8.append(r10)     // Catch: java.lang.Throwable -> L53
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L53
            r7.<init>(r8, r2)     // Catch: java.lang.Throwable -> L53
            throw r7     // Catch: java.lang.Throwable -> L53
        L53:
            r7 = move-exception
            if (r3 == 0) goto L59
            r3.close()     // Catch: java.io.IOException -> L5c
        L59:
            throw r7
        L5a:
            r7 = move-exception
            goto L1f
        L5c:
            r8 = move-exception
            goto L59
        */
        throw new UnsupportedOperationException("Method not decompiled: com.badlogic.gdx.files.FileHandle.readBytes():byte[]");
    }

    public OutputStream write(boolean append) {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot write to a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot write to an internal file: " + this.file);
        }
        try {
            return new FileOutputStream(file(), append);
        } catch (FileNotFoundException ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
            }
            throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public Writer writer(boolean append) {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot write to a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot write to an internal file: " + this.file);
        }
        try {
            return new FileWriter(file(), append);
        } catch (IOException ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
            }
            throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public FileHandle[] list() {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot list a classpath directory: " + this.file);
        }
        String[] relativePaths = file().list();
        if (relativePaths == null) {
            return new FileHandle[0];
        }
        FileHandle[] handles = new FileHandle[relativePaths.length];
        int n = relativePaths.length;
        for (int i = 0; i < n; i++) {
            handles[i] = child(relativePaths[i]);
        }
        return handles;
    }

    public FileHandle[] list(String suffix) {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot list a classpath directory: " + this.file);
        }
        String[] relativePaths = file().list();
        if (relativePaths == null) {
            return new FileHandle[0];
        }
        FileHandle[] handles = new FileHandle[relativePaths.length];
        int count = 0;
        for (String path : relativePaths) {
            if (path.endsWith(suffix)) {
                handles[count] = child(path);
                count++;
            }
        }
        if (count < relativePaths.length) {
            FileHandle[] newHandles = new FileHandle[count];
            System.arraycopy(handles, 0, newHandles, 0, count);
            return newHandles;
        }
        return handles;
    }

    public boolean isDirectory() {
        if (this.type == Files.FileType.Classpath) {
            return false;
        }
        return file().isDirectory();
    }

    public FileHandle child(String name) {
        return this.file.getPath().length() == 0 ? new FileHandle(new File(name), this.type) : new FileHandle(new File(this.file, name), this.type);
    }

    public FileHandle parent() {
        File parent = this.file.getParentFile();
        if (parent == null) {
            if (this.type == Files.FileType.Absolute) {
                parent = new File("/");
            } else {
                parent = new File("");
            }
        }
        return new FileHandle(parent, this.type);
    }

    public void mkdirs() {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot mkdirs with a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot mkdirs with an internal file: " + this.file);
        }
        file().mkdirs();
    }

    public boolean exists() {
        switch (this.type) {
            case Internal:
                if (this.file.exists()) {
                    return true;
                }
                break;
            case Classpath:
                break;
            default:
                return file().exists();
        }
        return FileHandle.class.getResourceAsStream(new StringBuilder().append("/").append(this.file.getPath().replace('\\', '/')).toString()) != null;
    }

    public boolean delete() {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot delete a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot delete an internal file: " + this.file);
        }
        return file().delete();
    }

    public boolean deleteDirectory() {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot delete a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot delete an internal file: " + this.file);
        }
        return deleteDirectory(file());
    }

    public void copyTo(FileHandle dest) {
        InputStream input = null;
        OutputStream output = null;
        try {
            try {
                input = read();
                output = dest.write(false);
                byte[] buffer = new byte[Opcodes.ACC_SYNTHETIC];
                while (true) {
                    int length = input.read(buffer);
                    if (length == -1) {
                        break;
                    } else {
                        output.write(buffer, 0, length);
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error copying source file: " + this.file + " (" + this.type + ")\nTo destination: " + dest.file + " (" + dest.type + ")", ex);
            }
        } catch (Throwable th) {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e3) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                    throw th;
                } catch (Exception e4) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public void moveTo(FileHandle dest) {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot move a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot move an internal file: " + this.file);
        }
        copyTo(dest);
        delete();
    }

    public long length() {
        if (this.type == Files.FileType.Classpath || (this.type == Files.FileType.Internal && !this.file.exists())) {
            InputStream input = read();
            try {
                long jAvailable = input.available();
                try {
                    return jAvailable;
                } catch (IOException e) {
                    return jAvailable;
                }
            } catch (Exception e2) {
                return 0L;
            } finally {
                try {
                    input.close();
                } catch (IOException e3) {
                }
            }
        }
        return file().length();
    }

    public String toString() {
        return this.file.getPath();
    }

    private static boolean deleteDirectory(File file) {
        File[] files;
        if (file.exists() && (files = file.listFiles()) != null) {
            int n = files.length;
            for (int i = 0; i < n; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return file.delete();
    }
}
