package com.badlogic.gdx.files;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
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
        return this.file.getPath().replace('\\', '/');
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

    public String pathWithoutExtension() {
        String path = this.file.getPath().replace('\\', '/');
        int dotIndex = path.lastIndexOf(46);
        return dotIndex == -1 ? path : path.substring(0, dotIndex);
    }

    public Files.FileType type() {
        return this.type;
    }

    public File file() {
        return this.type == Files.FileType.External ? new File(Gdx.files.getExternalStoragePath(), this.file.getPath()) : this.file;
    }

    public InputStream read() {
        if (this.type == Files.FileType.Classpath || ((this.type == Files.FileType.Internal && !this.file.exists()) || (this.type == Files.FileType.Local && !this.file.exists()))) {
            InputStream input = FileHandle.class.getResourceAsStream("/" + this.file.getPath().replace('\\', '/'));
            if (input == null) {
                throw new GdxRuntimeException("File not found: " + this.file + " (" + this.type + ")");
            }
            return input;
        }
        try {
            return new FileInputStream(file());
        } catch (Exception ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
            }
            throw new GdxRuntimeException("Error reading file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public BufferedInputStream read(int bufferSize) {
        return new BufferedInputStream(read(), bufferSize);
    }

    public Reader reader() {
        return new InputStreamReader(read());
    }

    public Reader reader(String charset) {
        try {
            return new InputStreamReader(read(), charset);
        } catch (UnsupportedEncodingException ex) {
            throw new GdxRuntimeException("Error reading file: " + this, ex);
        }
    }

    public BufferedReader reader(int bufferSize) {
        return new BufferedReader(new InputStreamReader(read()), bufferSize);
    }

    public BufferedReader reader(int bufferSize, String charset) {
        try {
            return new BufferedReader(new InputStreamReader(read(), charset), bufferSize);
        } catch (UnsupportedEncodingException ex) {
            throw new GdxRuntimeException("Error reading file: " + this, ex);
        }
    }

    public String readString() {
        return readString(null);
    }

    public String readString(String charset) {
        int fileLength = (int) length();
        if (fileLength == 0) {
            fileLength = 512;
        }
        StringBuilder output = new StringBuilder(fileLength);
        InputStreamReader reader = null;
        try {
            try {
                if (charset == null) {
                    InputStreamReader reader2 = new InputStreamReader(read());
                    reader = reader2;
                } else {
                    InputStreamReader reader3 = new InputStreamReader(read(), charset);
                    reader = reader3;
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

    public byte[] readBytes() throws Throwable {
        int length = (int) length();
        if (length == 0) {
            length = 512;
        }
        byte[] buffer = new byte[length];
        int position = 0;
        InputStream input = read();
        while (true) {
            try {
                try {
                    int count = input.read(buffer, position, buffer.length - position);
                    if (count == -1) {
                        break;
                    }
                    position += count;
                    if (position == buffer.length) {
                        int b = input.read();
                        if (b == -1) {
                            break;
                        }
                        byte[] newBuffer = new byte[buffer.length * 2];
                        System.arraycopy(buffer, 0, newBuffer, 0, position);
                        buffer = newBuffer;
                        int position2 = position + 1;
                        try {
                            buffer[position] = (byte) b;
                            position = position2;
                        } catch (IOException e) {
                            ex = e;
                        } catch (Throwable th) {
                            th = th;
                            if (input != null) {
                                try {
                                    input.close();
                                } catch (IOException e2) {
                                }
                            }
                            throw th;
                        }
                        throw new GdxRuntimeException("Error reading file: " + this, ex);
                    }
                } catch (IOException e3) {
                    ex = e3;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException e4) {
            }
        }
        if (position < buffer.length) {
            byte[] newBuffer2 = new byte[position];
            System.arraycopy(buffer, 0, newBuffer2, 0, position);
            return newBuffer2;
        }
        return buffer;
    }

    public int readBytes(byte[] bytes, int offset, int size) {
        InputStream input = read();
        int position = 0;
        while (true) {
            try {
                try {
                    int count = input.read(bytes, offset + position, size - position);
                    if (count <= 0) {
                        break;
                    }
                    position += count;
                } catch (IOException ex) {
                    throw new GdxRuntimeException("Error reading file: " + this, ex);
                }
            } catch (Throwable th) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                    }
                }
                throw th;
            }
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException e2) {
            }
        }
        return position - offset;
    }

    public OutputStream write(boolean append) {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot write to a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot write to an internal file: " + this.file);
        }
        parent().mkdirs();
        try {
            return new FileOutputStream(file(), append);
        } catch (Exception ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
            }
            throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x004e */
    /* JADX WARN: Code restructure failed: missing block: B:48:?, code lost:
    
        throw r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void write(InputStream input, boolean append) {
        OutputStream output = null;
        try {
            output = write(append);
            byte[] buffer = new byte[Opcodes.ACC_SYNTHETIC];
            while (true) {
                int length = input.read(buffer);
                if (length == -1) {
                    break;
                } else {
                    output.write(buffer, 0, length);
                }
                if (input != null) {
                    try {
                        input.close();
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                        throw th;
                    }
                }
                throw th;
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
            throw new GdxRuntimeException("Error stream writing to file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public Writer writer(boolean append) {
        return writer(append, null);
    }

    public Writer writer(boolean append, String charset) {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot write to a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot write to an internal file: " + this.file);
        }
        parent().mkdirs();
        try {
            FileOutputStream output = new FileOutputStream(file(), append);
            return charset == null ? new OutputStreamWriter(output) : new OutputStreamWriter(output, charset);
        } catch (IOException ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
            }
            throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public void writeString(String string, boolean append) {
        writeString(string, append, null);
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x003c */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void writeString(String string, boolean append, String charset) {
        Writer writer = null;
        try {
            writer = writer(append, charset);
            writer.write(string);
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception ex) {
            throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public void writeBytes(byte[] bytes, boolean append) {
        OutputStream output = write(append);
        try {
            try {
                output.write(bytes);
                try {
                    output.close();
                } catch (IOException e) {
                }
            } catch (Throwable th) {
                try {
                    output.close();
                } catch (IOException e2) {
                }
                throw th;
            }
        } catch (IOException ex) {
            throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
        }
    }

    public void writeBytes(byte[] bytes, int offset, int length, boolean append) {
        OutputStream output = write(append);
        try {
            try {
                output.write(bytes, offset, length);
                try {
                    output.close();
                } catch (IOException e) {
                }
            } catch (Throwable th) {
                try {
                    output.close();
                } catch (IOException e2) {
                }
                throw th;
            }
        } catch (IOException ex) {
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

    public FileHandle sibling(String name) {
        if (this.file.getPath().length() == 0) {
            throw new GdxRuntimeException("Cannot get the sibling of the root.");
        }
        return new FileHandle(new File(this.file.getParent(), name), this.type);
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
        return FileHandle.class.getResource(new StringBuilder().append("/").append(this.file.getPath().replace('\\', '/')).toString()) != null;
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
        boolean sourceDir = isDirectory();
        if (!sourceDir) {
            if (dest.isDirectory()) {
                dest = dest.child(name());
            }
            copyFile(this, dest);
            return;
        }
        if (dest.exists()) {
            if (!dest.isDirectory()) {
                throw new GdxRuntimeException("Destination exists but is not a directory: " + dest);
            }
        } else {
            dest.mkdirs();
            if (!dest.isDirectory()) {
                throw new GdxRuntimeException("Destination directory cannot be created: " + dest);
            }
        }
        if (!sourceDir) {
            dest = dest.child(name());
        }
        copyDirectory(this, dest);
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

    public long lastModified() {
        return file().lastModified();
    }

    public String toString() {
        return this.file.getPath().replace('\\', '/');
    }

    public static FileHandle tempFile(String prefix) {
        try {
            return new FileHandle(File.createTempFile(prefix, null));
        } catch (IOException ex) {
            throw new GdxRuntimeException("Unable to create temp file.", ex);
        }
    }

    public static FileHandle tempDirectory(String prefix) {
        try {
            File file = File.createTempFile(prefix, null);
            if (!file.delete()) {
                throw new IOException("Unable to delete temp file: " + file);
            }
            if (file.mkdir()) {
                return new FileHandle(file);
            }
            throw new IOException("Unable to create temp directory: " + file);
        } catch (IOException ex) {
            throw new GdxRuntimeException("Unable to create temp file.", ex);
        }
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

    private static void copyFile(FileHandle source, FileHandle dest) {
        try {
            dest.write(source.read(), false);
        } catch (Exception ex) {
            throw new GdxRuntimeException("Error copying source file: " + source.file + " (" + source.type + ")\nTo destination: " + dest.file + " (" + dest.type + ")", ex);
        }
    }

    private static void copyDirectory(FileHandle sourceDir, FileHandle destDir) {
        destDir.mkdirs();
        FileHandle[] files = sourceDir.list();
        for (FileHandle srcFile : files) {
            FileHandle destFile = destDir.child(srcFile.name());
            if (srcFile.isDirectory()) {
                copyDirectory(srcFile, destFile);
            } else {
                copyFile(srcFile, destFile);
            }
        }
    }
}
