package com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent;

import com.tapjoy.TapjoyConstants;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public enum TimeUnit {
    NANOSECONDS { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.1
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return j / TimeUnit.C1;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return j / TimeUnit.C2;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return j / TimeUnit.C3;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return j / TimeUnit.C4;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return j / TimeUnit.C5;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j / TimeUnit.C6;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toNanos(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return (int) (j - (TimeUnit.C2 * j2));
        }
    },
    MICROSECONDS { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.2
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return x(j, TimeUnit.C1, 9223372036854775L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return j / TimeUnit.C1;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return j / TimeUnit.C2;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return j / 60000000;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return j / 3600000000L;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j / 86400000000L;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toMicros(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return (int) ((TimeUnit.C1 * j) - (TimeUnit.C2 * j2));
        }
    },
    MILLISECONDS { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.3
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return x(j, TimeUnit.C2, 9223372036854L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return x(j, TimeUnit.C1, 9223372036854775L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return j / TimeUnit.C1;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return j / TapjoyConstants.THROTTLE_GET_TAP_POINTS_INTERVAL;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return j / 3600000;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j / 86400000;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toMillis(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return 0;
        }
    },
    SECONDS { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.4
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return x(j, TimeUnit.C3, 9223372036L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return x(j, TimeUnit.C2, 9223372036854L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return x(j, TimeUnit.C1, 9223372036854775L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return j / 60;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return j / 3600;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j / 86400;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toSeconds(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return 0;
        }
    },
    MINUTES { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.5
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return x(j, TimeUnit.C4, 153722867L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return x(j, 60000000L, 153722867280L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return x(j, TapjoyConstants.THROTTLE_GET_TAP_POINTS_INTERVAL, 153722867280912L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return x(j, 60L, 153722867280912930L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return j / 60;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j / 1440;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toMinutes(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return 0;
        }
    },
    HOURS { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.6
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return x(j, TimeUnit.C5, 2562047L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return x(j, 3600000000L, 2562047788L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return x(j, 3600000L, 2562047788015L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return x(j, 3600L, 2562047788015215L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return x(j, 60L, 153722867280912930L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j / 24;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toHours(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return 0;
        }
    },
    DAYS { // from class: com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.7
        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toNanos(long j) {
            return x(j, TimeUnit.C6, 106751L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMicros(long j) {
            return x(j, 86400000000L, 106751991L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMillis(long j) {
            return x(j, 86400000L, 106751991167L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toSeconds(long j) {
            return x(j, 86400L, 106751991167300L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toMinutes(long j) {
            return x(j, 1440L, 6405119470038038L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toHours(long j) {
            return x(j, 24L, 384307168202282325L);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long toDays(long j) {
            return j;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        public long convert(long j, TimeUnit timeUnit) {
            return timeUnit.toDays(j);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit
        int excessNanos(long j, long j2) {
            return 0;
        }
    };

    static final long C0 = 1;
    static final long C1 = 1000;
    static final long C2 = 1000000;
    static final long C3 = 1000000000;
    static final long C4 = 60000000000L;
    static final long C5 = 3600000000000L;
    static final long C6 = 86400000000000L;
    static final long MAX = Long.MAX_VALUE;

    abstract int excessNanos(long j, long j2);

    static long x(long j, long j2, long j3) {
        if (j > j3) {
            return MAX;
        }
        if (j < (-j3)) {
            return Long.MIN_VALUE;
        }
        return j * j2;
    }

    public long convert(long j, TimeUnit timeUnit) {
        throw new AbstractMethodError();
    }

    public long toNanos(long j) {
        throw new AbstractMethodError();
    }

    public long toMicros(long j) {
        throw new AbstractMethodError();
    }

    public long toMillis(long j) {
        throw new AbstractMethodError();
    }

    public long toSeconds(long j) {
        throw new AbstractMethodError();
    }

    public long toMinutes(long j) {
        throw new AbstractMethodError();
    }

    public long toHours(long j) {
        throw new AbstractMethodError();
    }

    public long toDays(long j) {
        throw new AbstractMethodError();
    }

    public void timedWait(Object obj, long j) throws InterruptedException {
        if (j > 0) {
            long millis = toMillis(j);
            obj.wait(millis, excessNanos(j, millis));
        }
    }

    public void timedJoin(Thread thread, long j) throws InterruptedException {
        if (j > 0) {
            long millis = toMillis(j);
            thread.join(millis, excessNanos(j, millis));
        }
    }

    public void sleep(long j) throws InterruptedException {
        if (j > 0) {
            long millis = toMillis(j);
            Thread.sleep(millis, excessNanos(j, millis));
        }
    }
}
