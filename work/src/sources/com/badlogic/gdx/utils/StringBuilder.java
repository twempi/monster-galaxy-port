package com.badlogic.gdx.utils;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class StringBuilder implements Appendable, CharSequence {
    static final int INITIAL_CAPACITY = 16;
    public char[] chars;
    public int length;

    final char[] getValue() {
        return this.chars;
    }

    public StringBuilder() {
        this.chars = new char[16];
    }

    public StringBuilder(int capacity) {
        if (capacity < 0) {
            throw new NegativeArraySizeException();
        }
        this.chars = new char[capacity];
    }

    public StringBuilder(CharSequence seq) {
        this(seq.toString());
    }

    public StringBuilder(StringBuilder builder) {
        this.length = builder.length;
        this.chars = new char[this.length + 16];
        System.arraycopy(builder.chars, 0, this.chars, 0, this.length);
    }

    public StringBuilder(String string) {
        this.length = string.length();
        this.chars = new char[this.length + 16];
        string.getChars(0, this.length, this.chars, 0);
    }

    private void enlargeBuffer(int min) {
        int newSize = (this.chars.length >> 1) + this.chars.length + 2;
        if (min <= newSize) {
            min = newSize;
        }
        char[] newData = new char[min];
        System.arraycopy(this.chars, 0, newData, 0, this.length);
        this.chars = newData;
    }

    final void appendNull() {
        int newSize = this.length + 4;
        if (newSize > this.chars.length) {
            enlargeBuffer(newSize);
        }
        char[] cArr = this.chars;
        int i = this.length;
        this.length = i + 1;
        cArr[i] = 'n';
        char[] cArr2 = this.chars;
        int i2 = this.length;
        this.length = i2 + 1;
        cArr2[i2] = 'u';
        char[] cArr3 = this.chars;
        int i3 = this.length;
        this.length = i3 + 1;
        cArr3[i3] = 'l';
        char[] cArr4 = this.chars;
        int i4 = this.length;
        this.length = i4 + 1;
        cArr4[i4] = 'l';
    }

    final void append0(char[] value) {
        int newSize = this.length + value.length;
        if (newSize > this.chars.length) {
            enlargeBuffer(newSize);
        }
        System.arraycopy(value, 0, value, this.length, value.length);
        this.length = newSize;
    }

    final void append0(char[] value, int offset, int length) {
        if (offset > value.length || offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset out of bounds: " + offset);
        }
        if (length < 0 || value.length - offset < length) {
            throw new ArrayIndexOutOfBoundsException("Length out of bounds: " + length);
        }
        int newSize = this.length + length;
        if (newSize > this.chars.length) {
            enlargeBuffer(newSize);
        }
        System.arraycopy(value, offset, this.chars, this.length, length);
        this.length = newSize;
    }

    final void append0(char ch) {
        if (this.length == this.chars.length) {
            enlargeBuffer(this.length + 1);
        }
        char[] cArr = this.chars;
        int i = this.length;
        this.length = i + 1;
        cArr[i] = ch;
    }

    final void append0(String string) {
        if (string == null) {
            appendNull();
            return;
        }
        int adding = string.length();
        int newSize = this.length + adding;
        if (newSize > this.chars.length) {
            enlargeBuffer(newSize);
        }
        string.getChars(0, adding, this.chars, this.length);
        this.length = newSize;
    }

    final void append0(CharSequence s, int start, int end) {
        if (s == null) {
            s = "null";
        }
        if (start < 0 || end < 0 || start > end || end > s.length()) {
            throw new IndexOutOfBoundsException();
        }
        append0(s.subSequence(start, end).toString());
    }

    public int capacity() {
        return this.chars.length;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        if (index < 0 || index >= this.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return this.chars[index];
    }

    final void delete0(int start, int end) {
        if (start >= 0) {
            if (end > this.length) {
                end = this.length;
            }
            if (end != start) {
                if (end > start) {
                    int count = this.length - end;
                    if (count >= 0) {
                        System.arraycopy(this.chars, end, this.chars, start, count);
                    }
                    this.length -= end - start;
                    return;
                }
            } else {
                return;
            }
        }
        throw new StringIndexOutOfBoundsException();
    }

    final void deleteCharAt0(int location) {
        if (location < 0 || location >= this.length) {
            throw new StringIndexOutOfBoundsException(location);
        }
        int count = (this.length - location) - 1;
        if (count > 0) {
            System.arraycopy(this.chars, location + 1, this.chars, location, count);
        }
        this.length--;
    }

    public void ensureCapacity(int min) {
        if (min > this.chars.length) {
            int twice = (this.chars.length << 1) + 2;
            if (twice <= min) {
                twice = min;
            }
            enlargeBuffer(twice);
        }
    }

    public void getChars(int start, int end, char[] dest, int destStart) {
        if (start > this.length || end > this.length || start > end) {
            throw new StringIndexOutOfBoundsException();
        }
        System.arraycopy(this.chars, start, dest, destStart, end - start);
    }

    final void insert0(int index, char[] value) {
        if (index < 0 || index > this.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        if (value.length != 0) {
            move(value.length, index);
            System.arraycopy(value, 0, value, index, value.length);
            this.length += value.length;
        }
    }

    final void insert0(int index, char[] value, int start, int length) {
        if (index >= 0 && index <= length) {
            if (start >= 0 && length >= 0 && length <= value.length - start) {
                if (length != 0) {
                    move(length, index);
                    System.arraycopy(value, start, value, index, length);
                    int i = length + length;
                    return;
                }
                return;
            }
            throw new StringIndexOutOfBoundsException("offset " + start + ", length " + length + ", char[].length " + value.length);
        }
        throw new StringIndexOutOfBoundsException(index);
    }

    final void insert0(int index, char ch) {
        if (index < 0 || index > this.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        move(1, index);
        this.chars[index] = ch;
        this.length++;
    }

    final void insert0(int index, String string) {
        if (index >= 0 && index <= this.length) {
            if (string == null) {
                string = "null";
            }
            int min = string.length();
            if (min != 0) {
                move(min, index);
                string.getChars(0, min, this.chars, index);
                this.length += min;
                return;
            }
            return;
        }
        throw new StringIndexOutOfBoundsException(index);
    }

    final void insert0(int index, CharSequence s, int start, int end) {
        if (s == null) {
            s = "null";
        }
        if (index < 0 || index > this.length || start < 0 || end < 0 || start > end || end > s.length()) {
            throw new IndexOutOfBoundsException();
        }
        insert0(index, s.subSequence(start, end).toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.length;
    }

    private void move(int size, int index) {
        if (this.chars.length - this.length >= size) {
            System.arraycopy(this.chars, index, this.chars, index + size, this.length - index);
            return;
        }
        int a = this.length + size;
        int b = (this.chars.length << 1) + 2;
        int newSize = a > b ? a : b;
        char[] newData = new char[newSize];
        System.arraycopy(this.chars, 0, newData, 0, index);
        System.arraycopy(this.chars, index, newData, index + size, this.length - index);
        this.chars = newData;
    }

    final void replace0(int start, int end, String string) {
        if (start >= 0) {
            if (end > this.length) {
                end = this.length;
            }
            if (end > start) {
                int stringLength = string.length();
                int diff = (end - start) - stringLength;
                if (diff > 0) {
                    System.arraycopy(this.chars, end, this.chars, start + stringLength, this.length - end);
                } else if (diff < 0) {
                    move(-diff, end);
                }
                string.getChars(0, stringLength, this.chars, start);
                this.length -= diff;
                return;
            }
            if (start == end) {
                if (string == null) {
                    throw new NullPointerException();
                }
                insert0(start, string);
                return;
            }
        }
        throw new StringIndexOutOfBoundsException();
    }

    final void reverse0() {
        if (this.length >= 2) {
            int end = this.length - 1;
            char frontHigh = this.chars[0];
            char endLow = this.chars[end];
            boolean allowFrontSur = true;
            boolean allowEndSur = true;
            int i = 0;
            int mid = this.length / 2;
            while (i < mid) {
                char frontLow = this.chars[i + 1];
                char endHigh = this.chars[end - 1];
                boolean surAtFront = allowFrontSur && frontLow >= 56320 && frontLow <= 57343 && frontHigh >= 55296 && frontHigh <= 56319;
                if (!surAtFront || this.length >= 3) {
                    boolean surAtEnd = allowEndSur && endHigh >= 55296 && endHigh <= 56319 && endLow >= 56320 && endLow <= 57343;
                    allowEndSur = true;
                    allowFrontSur = true;
                    if (surAtFront == surAtEnd) {
                        if (surAtFront) {
                            this.chars[end] = frontLow;
                            this.chars[end - 1] = frontHigh;
                            this.chars[i] = endHigh;
                            this.chars[i + 1] = endLow;
                            frontHigh = this.chars[i + 2];
                            endLow = this.chars[end - 2];
                            i++;
                            end--;
                        } else {
                            this.chars[end] = frontHigh;
                            this.chars[i] = endLow;
                            frontHigh = frontLow;
                            endLow = endHigh;
                        }
                    } else if (surAtFront) {
                        this.chars[end] = frontLow;
                        this.chars[i] = endLow;
                        endLow = endHigh;
                        allowFrontSur = false;
                    } else {
                        this.chars[end] = frontHigh;
                        this.chars[i] = endHigh;
                        frontHigh = frontLow;
                        allowEndSur = false;
                    }
                    i++;
                    end--;
                } else {
                    return;
                }
            }
            if ((this.length & 1) == 1) {
                if (!allowFrontSur || !allowEndSur) {
                    char[] cArr = this.chars;
                    if (!allowFrontSur) {
                        endLow = frontHigh;
                    }
                    cArr[end] = endLow;
                }
            }
        }
    }

    public void setCharAt(int index, char ch) {
        if (index < 0 || index >= this.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        this.chars[index] = ch;
    }

    public void setLength(int newLength) {
        if (newLength < 0) {
            throw new StringIndexOutOfBoundsException(newLength);
        }
        if (newLength > this.chars.length) {
            enlargeBuffer(newLength);
        } else if (this.length < newLength) {
            Arrays.fill(this.chars, this.length, newLength, (char) 0);
        }
        this.length = newLength;
    }

    public String substring(int start) {
        if (start < 0 || start > this.length) {
            throw new StringIndexOutOfBoundsException(start);
        }
        return start == this.length ? "" : new String(this.chars, start, this.length - start);
    }

    public String substring(int start, int end) {
        if (start < 0 || start > end || end > this.length) {
            throw new StringIndexOutOfBoundsException();
        }
        return start == end ? "" : new String(this.chars, start, end - start);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.length == 0 ? "" : new String(this.chars, 0, this.length);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return substring(start, end);
    }

    public int indexOf(String string) {
        return indexOf(string, 0);
    }

    public int indexOf(String subString, int start) {
        if (start < 0) {
            start = 0;
        }
        int subCount = subString.length();
        if (subCount > 0) {
            if (subCount + start > this.length) {
                return -1;
            }
            char firstChar = subString.charAt(0);
            while (true) {
                int i = start;
                boolean found = false;
                while (i < this.length) {
                    if (this.chars[i] != firstChar) {
                        i++;
                    } else {
                        found = true;
                        break;
                    }
                }
                if (!found || subCount + i > this.length) {
                    break;
                }
                int o1 = i;
                int o2 = 0;
                do {
                    o2++;
                    if (o2 >= subCount) {
                        break;
                    }
                    o1++;
                } while (this.chars[o1] == subString.charAt(o2));
                if (o2 != subCount) {
                    start = i + 1;
                } else {
                    return i;
                }
            }
            return -1;
        }
        return (start < this.length || start == 0) ? start : this.length;
    }

    public int lastIndexOf(String string) {
        return lastIndexOf(string, this.length);
    }

    public int lastIndexOf(String subString, int start) {
        int subCount = subString.length();
        if (subCount > this.length || start < 0) {
            return -1;
        }
        if (subCount > 0) {
            if (start > this.length - subCount) {
                start = this.length - subCount;
            }
            char firstChar = subString.charAt(0);
            while (true) {
                int i = start;
                boolean found = false;
                while (i >= 0) {
                    if (this.chars[i] != firstChar) {
                        i--;
                    } else {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return -1;
                }
                int o1 = i;
                int o2 = 0;
                do {
                    o2++;
                    if (o2 >= subCount) {
                        break;
                    }
                    o1++;
                } while (this.chars[o1] == subString.charAt(o2));
                if (o2 != subCount) {
                    start = i - 1;
                } else {
                    return i;
                }
            }
        } else {
            int i2 = start < this.length ? start : this.length;
            return i2;
        }
    }

    public void trimToSize() {
        if (this.length < this.chars.length) {
            char[] newValue = new char[this.length];
            System.arraycopy(this.chars, 0, newValue, 0, this.length);
            this.chars = newValue;
        }
    }

    public int codePointAt(int index) {
        if (index < 0 || index >= this.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointAt(this.chars, index, this.length);
    }

    public int codePointBefore(int index) {
        if (index < 1 || index > this.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointBefore(this.chars, index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > this.length || beginIndex > endIndex) {
            throw new StringIndexOutOfBoundsException();
        }
        return Character.codePointCount(this.chars, beginIndex, endIndex - beginIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return Character.offsetByCodePoints(this.chars, 0, this.length, index, codePointOffset);
    }

    public StringBuilder append(boolean b) {
        append0(b ? "true" : "false");
        return this;
    }

    @Override // java.lang.Appendable
    public StringBuilder append(char c) {
        append0(c);
        return this;
    }

    public StringBuilder append(int i) {
        append0(Integer.toString(i));
        return this;
    }

    public StringBuilder append(long lng) {
        append0(Long.toString(lng));
        return this;
    }

    public StringBuilder append(float f) {
        append0(Float.toString(f));
        return this;
    }

    public StringBuilder append(double d) {
        append0(Double.toString(d));
        return this;
    }

    public StringBuilder append(Object obj) {
        if (obj == null) {
            appendNull();
        } else {
            append0(obj.toString());
        }
        return this;
    }

    public StringBuilder append(String str) {
        append0(str);
        return this;
    }

    public StringBuilder append(char[] ch) {
        append0(ch);
        return this;
    }

    public StringBuilder append(char[] str, int offset, int len) {
        append0(str, offset, len);
        return this;
    }

    @Override // java.lang.Appendable
    public StringBuilder append(CharSequence csq) {
        if (csq == null) {
            appendNull();
        } else {
            append0(csq.toString());
        }
        return this;
    }

    public StringBuilder append(StringBuilder builder) {
        if (builder == null) {
            appendNull();
        } else {
            append0(builder.chars, 0, builder.length);
        }
        return this;
    }

    @Override // java.lang.Appendable
    public StringBuilder append(CharSequence csq, int start, int end) {
        append0(csq, start, end);
        return this;
    }

    public StringBuilder append(StringBuilder builder, int start, int end) {
        if (builder == null) {
            appendNull();
        } else {
            append0(builder.chars, start, end);
        }
        return this;
    }

    public StringBuilder appendCodePoint(int codePoint) {
        append0(Character.toChars(codePoint));
        return this;
    }

    public StringBuilder delete(int start, int end) {
        delete0(start, end);
        return this;
    }

    public StringBuilder deleteCharAt(int index) {
        deleteCharAt0(index);
        return this;
    }

    public StringBuilder insert(int offset, boolean b) {
        insert0(offset, b ? "true" : "false");
        return this;
    }

    public StringBuilder insert(int offset, char c) {
        insert0(offset, c);
        return this;
    }

    public StringBuilder insert(int offset, int i) {
        insert0(offset, Integer.toString(i));
        return this;
    }

    public StringBuilder insert(int offset, long l) {
        insert0(offset, Long.toString(l));
        return this;
    }

    public StringBuilder insert(int offset, float f) {
        insert0(offset, Float.toString(f));
        return this;
    }

    public StringBuilder insert(int offset, double d) {
        insert0(offset, Double.toString(d));
        return this;
    }

    public StringBuilder insert(int offset, Object obj) {
        insert0(offset, obj == null ? "null" : obj.toString());
        return this;
    }

    public StringBuilder insert(int offset, String str) {
        insert0(offset, str);
        return this;
    }

    public StringBuilder insert(int offset, char[] ch) {
        insert0(offset, ch);
        return this;
    }

    public StringBuilder insert(int offset, char[] str, int strOffset, int strLen) {
        insert0(offset, str, strOffset, strLen);
        return this;
    }

    public StringBuilder insert(int offset, CharSequence s) {
        insert0(offset, s == null ? "null" : s.toString());
        return this;
    }

    public StringBuilder insert(int offset, CharSequence s, int start, int end) {
        insert0(offset, s, start, end);
        return this;
    }

    public StringBuilder replace(int start, int end, String str) {
        replace0(start, end, str);
        return this;
    }

    public StringBuilder reverse() {
        reverse0();
        return this;
    }

    public int hashCode() {
        int result = this.length + 31;
        return (result * 31) + Arrays.hashCode(this.chars);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            StringBuilder other = (StringBuilder) obj;
            int length = this.length;
            if (length != other.length) {
                return false;
            }
            char[] chars = this.chars;
            char[] chars2 = other.chars;
            if (chars == chars2) {
                return true;
            }
            if (chars == null || chars2 == null) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (chars[i] != chars2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
