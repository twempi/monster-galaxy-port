package com.badlogic.gdx.utils;

import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class TimSort<T> {
    private static final boolean DEBUG = false;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
    private static final int MIN_GALLOP = 7;
    private static final int MIN_MERGE = 32;
    private T[] a;
    private Comparator<? super T> c;
    private int minGallop;
    private final int[] runBase;
    private final int[] runLen;
    private int stackSize;
    private T[] tmp;

    TimSort() {
        this.minGallop = 7;
        this.stackSize = 0;
        this.tmp = (T[]) new Object[256];
        this.runBase = new int[40];
        this.runLen = new int[40];
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void doSort(T[] a, Comparator<T> comparator, int lo, int hi) {
        this.stackSize = 0;
        rangeCheck(a.length, lo, hi);
        int nRemaining = hi - lo;
        if (nRemaining >= 2) {
            if (nRemaining < 32) {
                int initRunLen = countRunAndMakeAscending(a, lo, hi, comparator);
                binarySort(a, lo, hi, lo + initRunLen, comparator);
                return;
            }
            this.a = a;
            this.c = comparator;
            int minRun = minRunLength(nRemaining);
            do {
                int runLen = countRunAndMakeAscending(a, lo, hi, comparator);
                if (runLen < minRun) {
                    int force = nRemaining <= minRun ? nRemaining : minRun;
                    binarySort(a, lo, lo + force, lo + runLen, comparator);
                    runLen = force;
                }
                pushRun(lo, runLen);
                mergeCollapse();
                lo += runLen;
                nRemaining -= runLen;
            } while (nRemaining != 0);
            mergeForceCollapse();
        }
    }

    private TimSort(T[] tArr, Comparator<? super T> comparator) {
        int i;
        this.minGallop = 7;
        this.stackSize = 0;
        this.a = tArr;
        this.c = comparator;
        int length = tArr.length;
        this.tmp = (T[]) new Object[length < 512 ? length >>> 1 : 256];
        if (length < 120) {
            i = 5;
        } else {
            i = length < 1542 ? 10 : length < 119151 ? 19 : 40;
        }
        this.runBase = new int[i];
        this.runLen = new int[i];
    }

    static <T> void sort(T[] a, Comparator<? super T> c) {
        sort(a, 0, a.length, c);
    }

    static <T> void sort(T[] a, int lo, int hi, Comparator<? super T> c) {
        if (c == null) {
            Arrays.sort(a, lo, hi);
            return;
        }
        rangeCheck(a.length, lo, hi);
        int nRemaining = hi - lo;
        if (nRemaining >= 2) {
            if (nRemaining < 32) {
                int initRunLen = countRunAndMakeAscending(a, lo, hi, c);
                binarySort(a, lo, hi, lo + initRunLen, c);
                return;
            }
            TimSort<T> ts = new TimSort<>(a, c);
            int minRun = minRunLength(nRemaining);
            do {
                int runLen = countRunAndMakeAscending(a, lo, hi, c);
                if (runLen < minRun) {
                    int force = nRemaining <= minRun ? nRemaining : minRun;
                    binarySort(a, lo, lo + force, lo + runLen, c);
                    runLen = force;
                }
                ts.pushRun(lo, runLen);
                ts.mergeCollapse();
                lo += runLen;
                nRemaining -= runLen;
            } while (nRemaining != 0);
            ts.mergeForceCollapse();
        }
    }

    private static <T> void binarySort(T[] a, int lo, int hi, int start, Comparator<? super T> c) {
        if (start == lo) {
            start++;
        }
        while (start < hi) {
            T pivot = a[start];
            int left = lo;
            int right = start;
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (c.compare(pivot, a[mid]) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            int n = start - left;
            switch (n) {
                case 1:
                    break;
                case 2:
                    a[left + 2] = a[left + 1];
                    break;
                default:
                    System.arraycopy(a, left, a, left + 1, n);
                    a[left] = pivot;
                    start++;
                    break;
            }
            a[left + 1] = a[left];
            a[left] = pivot;
            start++;
        }
    }

    private static <T> int countRunAndMakeAscending(T[] a, int lo, int hi, Comparator<? super T> c) {
        int runHi;
        int runHi2 = lo + 1;
        if (runHi2 == hi) {
            return 1;
        }
        int runHi3 = runHi2 + 1;
        if (c.compare(a[runHi2], a[lo]) < 0) {
            runHi = runHi3;
            while (runHi < hi && c.compare(a[runHi], a[runHi - 1]) < 0) {
                runHi++;
            }
            reverseRange(a, lo, runHi);
        } else {
            runHi = runHi3;
            while (runHi < hi && c.compare(a[runHi], a[runHi - 1]) >= 0) {
                runHi++;
            }
        }
        return runHi - lo;
    }

    private static void reverseRange(Object[] a, int lo, int hi) {
        int hi2 = hi - 1;
        for (int lo2 = lo; lo2 < hi2; lo2++) {
            Object t = a[lo2];
            a[lo2] = a[hi2];
            a[hi2] = t;
            hi2--;
        }
    }

    private static int minRunLength(int n) {
        int r = 0;
        while (n >= 32) {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }

    private void pushRun(int runBase, int runLen) {
        this.runBase[this.stackSize] = runBase;
        this.runLen[this.stackSize] = runLen;
        this.stackSize++;
    }

    private void mergeCollapse() {
        while (this.stackSize > 1) {
            int n = this.stackSize - 2;
            if (n > 0 && this.runLen[n - 1] <= this.runLen[n] + this.runLen[n + 1]) {
                if (this.runLen[n - 1] < this.runLen[n + 1]) {
                    n--;
                }
                mergeAt(n);
            } else if (this.runLen[n] <= this.runLen[n + 1]) {
                mergeAt(n);
            } else {
                return;
            }
        }
    }

    private void mergeForceCollapse() {
        while (this.stackSize > 1) {
            int n = this.stackSize - 2;
            if (n > 0 && this.runLen[n - 1] < this.runLen[n + 1]) {
                n--;
            }
            mergeAt(n);
        }
    }

    private void mergeAt(int i) {
        int len2;
        int base1 = this.runBase[i];
        int len1 = this.runLen[i];
        int base2 = this.runBase[i + 1];
        int len3 = this.runLen[i + 1];
        this.runLen[i] = len1 + len3;
        if (i == this.stackSize - 3) {
            this.runBase[i + 1] = this.runBase[i + 2];
            this.runLen[i + 1] = this.runLen[i + 2];
        }
        this.stackSize--;
        int k = gallopRight(this.a[base2], this.a, base1, len1, 0, this.c);
        int base3 = base1 + k;
        int len4 = len1 - k;
        if (len4 != 0 && (len2 = gallopLeft(this.a[(base3 + len4) - 1], this.a, base2, len3, len3 - 1, this.c)) != 0) {
            if (len4 <= len2) {
                mergeLo(base3, len4, base2, len2);
            } else {
                mergeHi(base3, len4, base2, len2);
            }
        }
    }

    private static <T> int gallopLeft(T key, T[] a, int base, int len, int hint, Comparator<? super T> c) {
        int lastOfs;
        int ofs;
        int lastOfs2 = 0;
        int ofs2 = 1;
        if (c.compare(key, a[base + hint]) > 0) {
            int maxOfs = len - hint;
            while (ofs2 < maxOfs && c.compare(key, a[base + hint + ofs2]) > 0) {
                lastOfs2 = ofs2;
                ofs2 = (ofs2 << 1) + 1;
                if (ofs2 <= 0) {
                    ofs2 = maxOfs;
                }
            }
            if (ofs2 > maxOfs) {
                ofs2 = maxOfs;
            }
            lastOfs = lastOfs2 + hint;
            ofs = ofs2 + hint;
        } else {
            int maxOfs2 = hint + 1;
            while (ofs2 < maxOfs2 && c.compare(key, a[(base + hint) - ofs2]) <= 0) {
                lastOfs2 = ofs2;
                ofs2 = (ofs2 << 1) + 1;
                if (ofs2 <= 0) {
                    ofs2 = maxOfs2;
                }
            }
            if (ofs2 > maxOfs2) {
                ofs2 = maxOfs2;
            }
            int tmp = lastOfs2;
            lastOfs = hint - ofs2;
            ofs = hint - tmp;
        }
        int lastOfs3 = lastOfs + 1;
        while (lastOfs3 < ofs) {
            int m = lastOfs3 + ((ofs - lastOfs3) >>> 1);
            if (c.compare(key, a[base + m]) > 0) {
                lastOfs3 = m + 1;
            } else {
                ofs = m;
            }
        }
        return ofs;
    }

    private static <T> int gallopRight(T key, T[] a, int base, int len, int hint, Comparator<? super T> c) {
        int lastOfs;
        int ofs;
        int ofs2 = 1;
        int lastOfs2 = 0;
        if (c.compare(key, a[base + hint]) < 0) {
            int maxOfs = hint + 1;
            while (ofs2 < maxOfs && c.compare(key, a[(base + hint) - ofs2]) < 0) {
                lastOfs2 = ofs2;
                ofs2 = (ofs2 << 1) + 1;
                if (ofs2 <= 0) {
                    ofs2 = maxOfs;
                }
            }
            if (ofs2 > maxOfs) {
                ofs2 = maxOfs;
            }
            int tmp = lastOfs2;
            lastOfs = hint - ofs2;
            ofs = hint - tmp;
        } else {
            int maxOfs2 = len - hint;
            while (ofs2 < maxOfs2 && c.compare(key, a[base + hint + ofs2]) >= 0) {
                lastOfs2 = ofs2;
                ofs2 = (ofs2 << 1) + 1;
                if (ofs2 <= 0) {
                    ofs2 = maxOfs2;
                }
            }
            if (ofs2 > maxOfs2) {
                ofs2 = maxOfs2;
            }
            lastOfs = lastOfs2 + hint;
            ofs = ofs2 + hint;
        }
        int lastOfs3 = lastOfs + 1;
        while (lastOfs3 < ofs) {
            int m = lastOfs3 + ((ofs - lastOfs3) >>> 1);
            if (c.compare(key, a[base + m]) < 0) {
                ofs = m;
            } else {
                lastOfs3 = m + 1;
            }
        }
        return ofs;
    }

    /* JADX WARN: Code duplicated, block: B:34:0x00d9 A[PHI: r5 r19 r24
      0x00d9: PHI (r5v8 'cursor1' int) = (r5v7 'cursor1' int), (r5v12 'cursor1' int) binds: [B:30:0x00c5, B:32:0x00d4] A[DONT_GENERATE, DONT_INLINE]
      0x00d9: PHI (r19v9 'dest' int) = (r19v8 'dest' int), (r19v17 'dest' int) binds: [B:30:0x00c5, B:32:0x00d4] A[DONT_GENERATE, DONT_INLINE]
      0x00d9: PHI (r24v7 'len1' int) = (r24v6 'len1' int), (r24v9 'len1' int) binds: [B:30:0x00c5, B:32:0x00d4] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:37:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:39:0x00f4  */
    /* JADX WARN: Code duplicated, block: B:44:0x0118  */
    /* JADX WARN: Code duplicated, block: B:46:0x011d  */
    /* JADX WARN: Code duplicated, block: B:49:0x0124  */
    /* JADX WARN: Code duplicated, block: B:55:0x0134  */
    /* JADX WARN: Code duplicated, block: B:56:0x0137  */
    /* JADX WARN: Code duplicated, block: B:61:0x014c A[LOOP:2: B:29:0x00bc->B:61:0x014c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:62:0x0154  */
    /* JADX WARN: Code duplicated, block: B:67:0x00e5 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:68:0x0078 A[EDGE_INSN: B:68:0x0078->B:16:0x0078 BREAK  A[LOOP:0: B:10:0x0058->B:54:0x012c, LOOP_LABEL: LOOP:0: B:10:0x0058->B:54:0x012c], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:69:0x0112 A[SYNTHETIC] */
    private void mergeLo(int base1, int len1, int base2, int len2) {
        int dest;
        int count2;
        int dest2;
        int cursor1;
        boolean z;
        boolean z2;
        T[] a = this.a;
        T[] tmp = ensureCapacity(len1);
        System.arraycopy(a, base1, tmp, 0, len1);
        int cursor2 = 0;
        int dest3 = base1 + 1;
        int cursor3 = base2 + 1;
        a[base1] = a[base2];
        int len3 = len2 - 1;
        if (len3 == 0) {
            System.arraycopy(tmp, 0, a, dest3, len1);
            return;
        }
        if (len1 == 1) {
            System.arraycopy(a, cursor3, a, dest3, len3);
            a[dest3 + len3] = tmp[0];
            return;
        }
        Comparator<? super T> c = this.c;
        int minGallop = this.minGallop;
        int dest4 = dest3;
        int cursor4 = cursor3;
        loop0: while (true) {
            int count1 = 0;
            int count3 = 0;
            do {
                if (c.compare(a[cursor4], tmp[cursor2]) < 0) {
                    int dest5 = dest4 + 1;
                    int cursor5 = cursor4 + 1;
                    a[dest4] = a[cursor4];
                    count3++;
                    count1 = 0;
                    len3--;
                    if (len3 == 0) {
                        dest4 = dest5;
                        cursor4 = cursor5;
                        break loop0;
                    } else {
                        dest4 = dest5;
                        cursor4 = cursor5;
                    }
                } else {
                    int dest6 = dest4 + 1;
                    int cursor6 = cursor2 + 1;
                    a[dest4] = tmp[cursor2];
                    count1++;
                    count3 = 0;
                    len1--;
                    if (len1 == 1) {
                        dest4 = dest6;
                        cursor2 = cursor6;
                        break loop0;
                    } else {
                        dest4 = dest6;
                        cursor2 = cursor6;
                    }
                }
            } while ((count1 | count3) < minGallop);
            int cursor7 = cursor4;
            while (true) {
                int count4 = gallopRight(a[cursor7], tmp, cursor2, len1, 0, c);
                if (count4 != 0) {
                    System.arraycopy(tmp, cursor2, a, dest4, count4);
                    dest4 += count4;
                    cursor2 += count4;
                    len1 -= count4;
                    if (len1 <= 1) {
                        cursor4 = cursor7;
                        break loop0;
                    }
                    dest = dest4 + 1;
                    cursor4 = cursor7 + 1;
                    a[dest4] = a[cursor7];
                    len3--;
                    if (len3 == 0) {
                        dest4 = dest;
                        break loop0;
                    }
                    count2 = gallopLeft(tmp[cursor2], a, cursor4, len3, 0, c);
                    if (count2 != 0) {
                        System.arraycopy(a, cursor4, a, dest, count2);
                        dest4 = dest + count2;
                        cursor4 += count2;
                        len3 -= count2;
                        if (len3 != 0) {
                            break loop0;
                        }
                    } else {
                        dest4 = dest;
                    }
                    dest2 = dest4 + 1;
                    cursor1 = cursor2 + 1;
                    a[dest4] = tmp[cursor2];
                    len1--;
                    if (len1 == 1) {
                        dest4 = dest2;
                        cursor2 = cursor1;
                        break loop0;
                    }
                    minGallop--;
                    if (count4 >= 7) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (count2 >= 7) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2 && !z) {
                        break;
                    }
                    dest4 = dest2;
                    cursor7 = cursor4;
                    cursor2 = cursor1;
                } else {
                    dest = dest4 + 1;
                    cursor4 = cursor7 + 1;
                    a[dest4] = a[cursor7];
                    len3--;
                    if (len3 == 0) {
                        dest4 = dest;
                        break loop0;
                    }
                    count2 = gallopLeft(tmp[cursor2], a, cursor4, len3, 0, c);
                    if (count2 != 0) {
                        System.arraycopy(a, cursor4, a, dest, count2);
                        dest4 = dest + count2;
                        cursor4 += count2;
                        len3 -= count2;
                        if (len3 != 0) {
                            break loop0;
                            break loop0;
                        }
                    } else {
                        dest4 = dest;
                    }
                    dest2 = dest4 + 1;
                    cursor1 = cursor2 + 1;
                    a[dest4] = tmp[cursor2];
                    len1--;
                    if (len1 == 1) {
                        dest4 = dest2;
                        cursor2 = cursor1;
                        break loop0;
                    }
                    minGallop--;
                    if (count4 >= 7) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (count2 >= 7) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2 && !z) {
                        break;
                    }
                    dest4 = dest2;
                    cursor7 = cursor4;
                    cursor2 = cursor1;
                }
            }
            if (minGallop < 0) {
                minGallop = 0;
            }
            minGallop += 2;
            dest4 = dest2;
            cursor2 = cursor1;
        }
        if (minGallop < 1) {
            minGallop = 1;
        }
        this.minGallop = minGallop;
        if (len1 == 1) {
            System.arraycopy(a, cursor4, a, dest4, len3);
            a[dest4 + len3] = tmp[cursor2];
        } else {
            if (len1 == 0) {
                throw new IllegalArgumentException("Comparison method violates its general contract!");
            }
            System.arraycopy(tmp, cursor2, a, dest4, len1);
        }
    }

    /* JADX WARN: Code duplicated, block: B:32:0x00e1 A[PHI: r16 r20 r25
      0x00e1: PHI (r16v7 'cursor1' int) = (r16v6 'cursor1' int), (r16v10 'cursor1' int) binds: [B:29:0x00d0, B:31:0x00df] A[DONT_GENERATE, DONT_INLINE]
      0x00e1: PHI (r20v9 'dest' int) = (r20v8 'dest' int), (r20v16 'dest' int) binds: [B:29:0x00d0, B:31:0x00df] A[DONT_GENERATE, DONT_INLINE]
      0x00e1: PHI (r25v7 'len1' int) = (r25v6 'len1' int), (r25v9 'len1' int) binds: [B:29:0x00d0, B:31:0x00df] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:35:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:37:0x0105  */
    /* JADX WARN: Code duplicated, block: B:42:0x0129  */
    /* JADX WARN: Code duplicated, block: B:44:0x012e  */
    /* JADX WARN: Code duplicated, block: B:47:0x0133  */
    /* JADX WARN: Code duplicated, block: B:53:0x0143  */
    /* JADX WARN: Code duplicated, block: B:54:0x0146  */
    /* JADX WARN: Code duplicated, block: B:59:0x015e A[LOOP:2: B:28:0x00c2->B:59:0x015e, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:60:0x0164  */
    /* JADX WARN: Code duplicated, block: B:65:0x00f0 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x007d A[EDGE_INSN: B:66:0x007d->B:16:0x007d BREAK  A[LOOP:0: B:10:0x005e->B:52:0x013b, LOOP_LABEL: LOOP:0: B:10:0x005e->B:52:0x013b], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:67:0x0123 A[SYNTHETIC] */
    private void mergeHi(int base1, int len1, int base2, int len2) {
        int dest;
        int cursor1;
        int dest2;
        int cursor2;
        int count2;
        boolean z;
        boolean z2;
        T[] a = this.a;
        T[] tmp = ensureCapacity(len2);
        System.arraycopy(a, base2, tmp, 0, len2);
        int cursor3 = (base1 + len1) - 1;
        int cursor4 = len2 - 1;
        int dest3 = (base2 + len2) - 1;
        int dest4 = dest3 - 1;
        int cursor5 = cursor3 - 1;
        a[dest3] = a[cursor3];
        int len3 = len1 - 1;
        if (len3 == 0) {
            System.arraycopy(tmp, 0, a, dest4 - (len2 - 1), len2);
            return;
        }
        if (len2 == 1) {
            int dest5 = dest4 - len3;
            System.arraycopy(a, (cursor5 - len3) + 1, a, dest5 + 1, len3);
            a[dest5] = tmp[cursor4];
            return;
        }
        Comparator<? super T> c = this.c;
        int minGallop = this.minGallop;
        loop0: while (true) {
            dest = dest4;
            cursor1 = cursor5;
            int count1 = 0;
            int count3 = 0;
            do {
                if (c.compare(tmp[cursor4], a[cursor1]) < 0) {
                    int dest6 = dest - 1;
                    int cursor6 = cursor1 - 1;
                    a[dest] = a[cursor1];
                    count1++;
                    count3 = 0;
                    len3--;
                    if (len3 == 0) {
                        dest = dest6;
                        cursor1 = cursor6;
                        break loop0;
                    } else {
                        dest = dest6;
                        cursor1 = cursor6;
                    }
                } else {
                    int dest7 = dest - 1;
                    int cursor7 = cursor4 - 1;
                    a[dest] = tmp[cursor4];
                    count3++;
                    count1 = 0;
                    len2--;
                    if (len2 == 1) {
                        dest = dest7;
                        cursor4 = cursor7;
                        break loop0;
                    } else {
                        dest = dest7;
                        cursor4 = cursor7;
                    }
                }
            } while ((count1 | count3) < minGallop);
            while (true) {
                int count4 = len3 - gallopRight(tmp[cursor4], a, base1, len3, len3 - 1, c);
                if (count4 != 0) {
                    dest -= count4;
                    cursor1 -= count4;
                    len3 -= count4;
                    System.arraycopy(a, cursor1 + 1, a, dest + 1, count4);
                    if (len3 == 0) {
                        break loop0;
                    }
                    dest2 = dest - 1;
                    cursor2 = cursor4 - 1;
                    a[dest] = tmp[cursor4];
                    len2--;
                    if (len2 == 1) {
                        dest = dest2;
                        cursor4 = cursor2;
                        break loop0;
                    }
                    count2 = len2 - gallopLeft(a[cursor1], tmp, 0, len2, len2 - 1, c);
                    if (count2 != 0) {
                        dest = dest2 - count2;
                        cursor4 = cursor2 - count2;
                        len2 -= count2;
                        System.arraycopy(tmp, cursor4 + 1, a, dest + 1, count2);
                        if (len2 > 1) {
                            break loop0;
                        }
                    } else {
                        dest = dest2;
                        cursor4 = cursor2;
                    }
                    dest4 = dest - 1;
                    cursor5 = cursor1 - 1;
                    a[dest] = a[cursor1];
                    len3--;
                    if (len3 == 0) {
                        dest = dest4;
                        cursor1 = cursor5;
                        break loop0;
                    }
                    minGallop--;
                    if (count4 >= 7) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (count2 >= 7) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2 && !z) {
                        break;
                    }
                    dest = dest4;
                    cursor1 = cursor5;
                } else {
                    dest2 = dest - 1;
                    cursor2 = cursor4 - 1;
                    a[dest] = tmp[cursor4];
                    len2--;
                    if (len2 == 1) {
                        dest = dest2;
                        cursor4 = cursor2;
                        break loop0;
                    }
                    count2 = len2 - gallopLeft(a[cursor1], tmp, 0, len2, len2 - 1, c);
                    if (count2 != 0) {
                        dest = dest2 - count2;
                        cursor4 = cursor2 - count2;
                        len2 -= count2;
                        System.arraycopy(tmp, cursor4 + 1, a, dest + 1, count2);
                        if (len2 > 1) {
                            break loop0;
                            break loop0;
                        }
                    } else {
                        dest = dest2;
                        cursor4 = cursor2;
                    }
                    dest4 = dest - 1;
                    cursor5 = cursor1 - 1;
                    a[dest] = a[cursor1];
                    len3--;
                    if (len3 == 0) {
                        dest = dest4;
                        cursor1 = cursor5;
                        break loop0;
                    }
                    minGallop--;
                    if (count4 >= 7) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (count2 >= 7) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2 && !z) {
                        break;
                    }
                    dest = dest4;
                    cursor1 = cursor5;
                }
            }
            if (minGallop < 0) {
                minGallop = 0;
            }
            minGallop += 2;
        }
        if (minGallop < 1) {
            minGallop = 1;
        }
        this.minGallop = minGallop;
        if (len2 == 1) {
            int dest8 = dest - len3;
            System.arraycopy(a, (cursor1 - len3) + 1, a, dest8 + 1, len3);
            a[dest8] = tmp[cursor4];
        } else {
            if (len2 == 0) {
                throw new IllegalArgumentException("Comparison method violates its general contract!");
            }
            System.arraycopy(tmp, 0, a, dest - (len2 - 1), len2);
        }
    }

    private T[] ensureCapacity(int i) {
        int iMin;
        if (this.tmp.length < i) {
            int i2 = i | (i >> 1);
            int i3 = i2 | (i2 >> 2);
            int i4 = i3 | (i3 >> 4);
            int i5 = i4 | (i4 >> 8);
            int i6 = (i5 | (i5 >> 16)) + 1;
            if (i6 < 0) {
                iMin = i;
            } else {
                iMin = Math.min(i6, this.a.length >>> 1);
            }
            this.tmp = (T[]) new Object[iMin];
        }
        return this.tmp;
    }

    private static void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > arrayLen) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }
}
