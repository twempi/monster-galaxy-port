package com.badlogic.gdx.utils;

/* JADX INFO: loaded from: classes.dex */
class ComparableTimSort {
    private static final boolean DEBUG = false;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
    private static final int MIN_GALLOP = 7;
    private static final int MIN_MERGE = 32;
    private Object[] a;
    private int minGallop;
    private final int[] runBase;
    private final int[] runLen;
    private int stackSize;
    private Object[] tmp;

    ComparableTimSort() {
        this.minGallop = 7;
        this.stackSize = 0;
        this.tmp = new Object[256];
        this.runBase = new int[40];
        this.runLen = new int[40];
    }

    public void doSort(Object[] a, int lo, int hi) {
        this.stackSize = 0;
        rangeCheck(a.length, lo, hi);
        int nRemaining = hi - lo;
        if (nRemaining >= 2) {
            if (nRemaining < 32) {
                int initRunLen = countRunAndMakeAscending(a, lo, hi);
                binarySort(a, lo, hi, lo + initRunLen);
                return;
            }
            this.a = a;
            int minRun = minRunLength(nRemaining);
            do {
                int runLen = countRunAndMakeAscending(a, lo, hi);
                if (runLen < minRun) {
                    int force = nRemaining <= minRun ? nRemaining : minRun;
                    binarySort(a, lo, lo + force, lo + runLen);
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

    private ComparableTimSort(Object[] a) {
        int stackLen;
        this.minGallop = 7;
        this.stackSize = 0;
        this.a = a;
        int len = a.length;
        Object[] newArray = new Object[len < 512 ? len >>> 1 : 256];
        this.tmp = newArray;
        if (len < 120) {
            stackLen = 5;
        } else {
            stackLen = len < 1542 ? 10 : len < 119151 ? 19 : 40;
        }
        this.runBase = new int[stackLen];
        this.runLen = new int[stackLen];
    }

    static void sort(Object[] a) {
        sort(a, 0, a.length);
    }

    static void sort(Object[] a, int lo, int hi) {
        rangeCheck(a.length, lo, hi);
        int nRemaining = hi - lo;
        if (nRemaining >= 2) {
            if (nRemaining < 32) {
                int initRunLen = countRunAndMakeAscending(a, lo, hi);
                binarySort(a, lo, hi, lo + initRunLen);
                return;
            }
            ComparableTimSort ts = new ComparableTimSort(a);
            int minRun = minRunLength(nRemaining);
            do {
                int runLen = countRunAndMakeAscending(a, lo, hi);
                if (runLen < minRun) {
                    int force = nRemaining <= minRun ? nRemaining : minRun;
                    binarySort(a, lo, lo + force, lo + runLen);
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

    private static void binarySort(Object[] a, int lo, int hi, int start) {
        if (start == lo) {
            start++;
        }
        while (start < hi) {
            Comparable<Object> pivot = (Comparable) a[start];
            int left = lo;
            int right = start;
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (pivot.compareTo(a[mid]) < 0) {
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

    private static int countRunAndMakeAscending(Object[] a, int lo, int hi) {
        int runHi;
        int runHi2 = lo + 1;
        if (runHi2 == hi) {
            return 1;
        }
        int runHi3 = runHi2 + 1;
        if (((Comparable) a[runHi2]).compareTo(a[lo]) < 0) {
            runHi = runHi3;
            while (runHi < hi && ((Comparable) a[runHi]).compareTo(a[runHi - 1]) < 0) {
                runHi++;
            }
            reverseRange(a, lo, runHi);
        } else {
            runHi = runHi3;
            while (runHi < hi && ((Comparable) a[runHi]).compareTo(a[runHi - 1]) >= 0) {
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
        int k = gallopRight((Comparable) this.a[base2], this.a, base1, len1, 0);
        int base3 = base1 + k;
        int len4 = len1 - k;
        if (len4 != 0 && (len2 = gallopLeft((Comparable) this.a[(base3 + len4) - 1], this.a, base2, len3, len3 - 1)) != 0) {
            if (len4 <= len2) {
                mergeLo(base3, len4, base2, len2);
            } else {
                mergeHi(base3, len4, base2, len2);
            }
        }
    }

    private static int gallopLeft(Comparable<Object> key, Object[] a, int base, int len, int hint) {
        int lastOfs;
        int ofs;
        int lastOfs2 = 0;
        int ofs2 = 1;
        if (key.compareTo(a[base + hint]) > 0) {
            int maxOfs = len - hint;
            while (ofs2 < maxOfs && key.compareTo(a[base + hint + ofs2]) > 0) {
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
            while (ofs2 < maxOfs2 && key.compareTo(a[(base + hint) - ofs2]) <= 0) {
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
            if (key.compareTo(a[base + m]) > 0) {
                lastOfs3 = m + 1;
            } else {
                ofs = m;
            }
        }
        return ofs;
    }

    private static int gallopRight(Comparable<Object> key, Object[] a, int base, int len, int hint) {
        int lastOfs;
        int ofs;
        int ofs2 = 1;
        int lastOfs2 = 0;
        if (key.compareTo(a[base + hint]) < 0) {
            int maxOfs = hint + 1;
            while (ofs2 < maxOfs && key.compareTo(a[(base + hint) - ofs2]) < 0) {
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
            while (ofs2 < maxOfs2 && key.compareTo(a[base + hint + ofs2]) >= 0) {
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
            if (key.compareTo(a[base + m]) < 0) {
                ofs = m;
            } else {
                lastOfs3 = m + 1;
            }
        }
        return ofs;
    }

    /* JADX WARN: Code duplicated, block: B:32:0x00a7 A[PHI: r4 r8 r16
      0x00a7: PHI (r4v8 'cursor1' int) = (r4v7 'cursor1' int), (r4v12 'cursor1' int) binds: [B:29:0x0099, B:31:0x00a5] A[DONT_GENERATE, DONT_INLINE]
      0x00a7: PHI (r8v9 'dest' int) = (r8v8 'dest' int), (r8v17 'dest' int) binds: [B:29:0x0099, B:31:0x00a5] A[DONT_GENERATE, DONT_INLINE]
      0x00a7: PHI (r16v7 'len1' int) = (r16v6 'len1' int), (r16v9 'len1' int) binds: [B:29:0x0099, B:31:0x00a5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:35:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:37:0x00c3  */
    /* JADX WARN: Code duplicated, block: B:42:0x00e1  */
    /* JADX WARN: Code duplicated, block: B:44:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:47:0x00eb  */
    /* JADX WARN: Code duplicated, block: B:53:0x00f8  */
    /* JADX WARN: Code duplicated, block: B:54:0x00fb  */
    /* JADX WARN: Code duplicated, block: B:59:0x010e A[LOOP:2: B:28:0x008e->B:59:0x010e, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:60:0x0112  */
    /* JADX WARN: Code duplicated, block: B:65:0x00b3 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x005c A[EDGE_INSN: B:66:0x005c->B:16:0x005c BREAK  A[LOOP:0: B:10:0x003d->B:52:0x00f2, LOOP_LABEL: LOOP:0: B:10:0x003d->B:52:0x00f2], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:67:0x00dd A[SYNTHETIC] */
    private void mergeLo(int base1, int len1, int base2, int len2) {
        int dest;
        int cursor2;
        int count2;
        int dest2;
        int cursor1;
        boolean z;
        boolean z2;
        Object[] a = this.a;
        Object[] tmp = ensureCapacity(len1);
        System.arraycopy(a, base1, tmp, 0, len1);
        int cursor3 = 0;
        int dest3 = base1 + 1;
        int cursor4 = base2 + 1;
        a[base1] = a[base2];
        int len3 = len2 - 1;
        if (len3 == 0) {
            System.arraycopy(tmp, 0, a, dest3, len1);
            return;
        }
        if (len1 == 1) {
            System.arraycopy(a, cursor4, a, dest3, len3);
            a[dest3 + len3] = tmp[0];
            return;
        }
        int minGallop = this.minGallop;
        int dest4 = dest3;
        int cursor5 = cursor4;
        loop0: while (true) {
            int count1 = 0;
            int count3 = 0;
            do {
                if (((Comparable) a[cursor5]).compareTo(tmp[cursor3]) < 0) {
                    int dest5 = dest4 + 1;
                    int cursor6 = cursor5 + 1;
                    a[dest4] = a[cursor5];
                    count3++;
                    count1 = 0;
                    len3--;
                    if (len3 == 0) {
                        dest4 = dest5;
                        cursor5 = cursor6;
                        break loop0;
                    } else {
                        dest4 = dest5;
                        cursor5 = cursor6;
                    }
                } else {
                    int dest6 = dest4 + 1;
                    int cursor7 = cursor3 + 1;
                    a[dest4] = tmp[cursor3];
                    count1++;
                    count3 = 0;
                    len1--;
                    if (len1 == 1) {
                        dest4 = dest6;
                        cursor3 = cursor7;
                        break loop0;
                    } else {
                        dest4 = dest6;
                        cursor3 = cursor7;
                    }
                }
            } while ((count1 | count3) < minGallop);
            while (true) {
                int count4 = gallopRight((Comparable) a[cursor5], tmp, cursor3, len1, 0);
                if (count4 != 0) {
                    System.arraycopy(tmp, cursor3, a, dest4, count4);
                    dest4 += count4;
                    cursor3 += count4;
                    len1 -= count4;
                    if (len1 <= 1) {
                        break loop0;
                    }
                    dest = dest4 + 1;
                    cursor2 = cursor5 + 1;
                    a[dest4] = a[cursor5];
                    len3--;
                    if (len3 == 0) {
                        dest4 = dest;
                        cursor5 = cursor2;
                        break loop0;
                    }
                    count2 = gallopLeft((Comparable) tmp[cursor3], a, cursor2, len3, 0);
                    if (count2 != 0) {
                        System.arraycopy(a, cursor2, a, dest, count2);
                        dest4 = dest + count2;
                        cursor5 = cursor2 + count2;
                        len3 -= count2;
                        if (len3 != 0) {
                            break loop0;
                        }
                    } else {
                        dest4 = dest;
                        cursor5 = cursor2;
                    }
                    dest2 = dest4 + 1;
                    cursor1 = cursor3 + 1;
                    a[dest4] = tmp[cursor3];
                    len1--;
                    if (len1 == 1) {
                        dest4 = dest2;
                        cursor3 = cursor1;
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
                    cursor3 = cursor1;
                } else {
                    dest = dest4 + 1;
                    cursor2 = cursor5 + 1;
                    a[dest4] = a[cursor5];
                    len3--;
                    if (len3 == 0) {
                        dest4 = dest;
                        cursor5 = cursor2;
                        break loop0;
                    }
                    count2 = gallopLeft((Comparable) tmp[cursor3], a, cursor2, len3, 0);
                    if (count2 != 0) {
                        System.arraycopy(a, cursor2, a, dest, count2);
                        dest4 = dest + count2;
                        cursor5 = cursor2 + count2;
                        len3 -= count2;
                        if (len3 != 0) {
                            break loop0;
                            break loop0;
                        }
                    } else {
                        dest4 = dest;
                        cursor5 = cursor2;
                    }
                    dest2 = dest4 + 1;
                    cursor1 = cursor3 + 1;
                    a[dest4] = tmp[cursor3];
                    len1--;
                    if (len1 == 1) {
                        dest4 = dest2;
                        cursor3 = cursor1;
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
                    cursor3 = cursor1;
                }
            }
            if (minGallop < 0) {
                minGallop = 0;
            }
            minGallop += 2;
            dest4 = dest2;
            cursor3 = cursor1;
        }
        if (minGallop < 1) {
            minGallop = 1;
        }
        this.minGallop = minGallop;
        if (len1 == 1) {
            System.arraycopy(a, cursor5, a, dest4, len3);
            a[dest4 + len3] = tmp[cursor3];
        } else {
            if (len1 == 0) {
                throw new IllegalArgumentException("Comparison method violates its general contract!");
            }
            System.arraycopy(tmp, cursor3, a, dest4, len1);
        }
    }

    /* JADX WARN: Code duplicated, block: B:32:0x00cc A[PHI: r5 r9 r18
      0x00cc: PHI (r5v7 'cursor1' int) = (r5v6 'cursor1' int), (r5v10 'cursor1' int) binds: [B:29:0x00bd, B:31:0x00ca] A[DONT_GENERATE, DONT_INLINE]
      0x00cc: PHI (r9v9 'dest' int) = (r9v8 'dest' int), (r9v16 'dest' int) binds: [B:29:0x00bd, B:31:0x00ca] A[DONT_GENERATE, DONT_INLINE]
      0x00cc: PHI (r18v7 'len1' int) = (r18v6 'len1' int), (r18v9 'len1' int) binds: [B:29:0x00bd, B:31:0x00ca] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:35:0x00de  */
    /* JADX WARN: Code duplicated, block: B:37:0x00ef  */
    /* JADX WARN: Code duplicated, block: B:42:0x0111  */
    /* JADX WARN: Code duplicated, block: B:44:0x0116  */
    /* JADX WARN: Code duplicated, block: B:47:0x011b  */
    /* JADX WARN: Code duplicated, block: B:53:0x0128  */
    /* JADX WARN: Code duplicated, block: B:54:0x012b  */
    /* JADX WARN: Code duplicated, block: B:59:0x0143 A[LOOP:2: B:28:0x00ad->B:59:0x0143, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:60:0x0147  */
    /* JADX WARN: Code duplicated, block: B:65:0x00db A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x0073 A[EDGE_INSN: B:66:0x0073->B:16:0x0073 BREAK  A[LOOP:0: B:10:0x0054->B:52:0x0122, LOOP_LABEL: LOOP:0: B:10:0x0054->B:52:0x0122], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:67:0x010d A[SYNTHETIC] */
    private void mergeHi(int base1, int len1, int base2, int len2) {
        int dest;
        int cursor1;
        int dest2;
        int cursor2;
        int count2;
        boolean z;
        boolean z2;
        Object[] a = this.a;
        Object[] tmp = ensureCapacity(len2);
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
        int minGallop = this.minGallop;
        loop0: while (true) {
            dest = dest4;
            cursor1 = cursor5;
            int count1 = 0;
            int count3 = 0;
            do {
                if (((Comparable) tmp[cursor4]).compareTo(a[cursor1]) < 0) {
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
                int count4 = len3 - gallopRight((Comparable) tmp[cursor4], a, base1, len3, len3 - 1);
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
                    count2 = len2 - gallopLeft((Comparable) a[cursor1], tmp, 0, len2, len2 - 1);
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
                    count2 = len2 - gallopLeft((Comparable) a[cursor1], tmp, 0, len2, len2 - 1);
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

    private Object[] ensureCapacity(int minCapacity) {
        int newSize;
        if (this.tmp.length < minCapacity) {
            int newSize2 = minCapacity | (minCapacity >> 1);
            int newSize3 = newSize2 | (newSize2 >> 2);
            int newSize4 = newSize3 | (newSize3 >> 4);
            int newSize5 = newSize4 | (newSize4 >> 8);
            int newSize6 = (newSize5 | (newSize5 >> 16)) + 1;
            if (newSize6 < 0) {
                newSize = minCapacity;
            } else {
                newSize = Math.min(newSize6, this.a.length >>> 1);
            }
            Object[] newArray = new Object[newSize];
            this.tmp = newArray;
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
