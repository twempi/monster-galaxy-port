package com.google.gson;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'DEFAULT' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:485)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:399)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:364)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:349)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:284)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:153)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:102)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX INFO: loaded from: classes.dex */
public final class LongSerializationPolicy {
    private static final /* synthetic */ LongSerializationPolicy[] $VALUES;
    public static final LongSerializationPolicy DEFAULT;
    public static final LongSerializationPolicy STRING;
    private final Strategy strategy;

    private interface Strategy {
        JsonElement serialize(Long l);
    }

    public static LongSerializationPolicy valueOf(String name) {
        return (LongSerializationPolicy) Enum.valueOf(LongSerializationPolicy.class, name);
    }

    public static LongSerializationPolicy[] values() {
        return (LongSerializationPolicy[]) $VALUES.clone();
    }

    static {
        DEFAULT = new LongSerializationPolicy("DEFAULT", 0, new DefaultStrategy());
        STRING = new LongSerializationPolicy("STRING", 1, new StringStrategy());
        $VALUES = new LongSerializationPolicy[]{DEFAULT, STRING};
    }

    private LongSerializationPolicy(String str, int i, Strategy strategy) {
        super(str, i);
        this.strategy = strategy;
    }

    public JsonElement serialize(Long value) {
        return this.strategy.serialize(value);
    }

    private static class DefaultStrategy implements Strategy {
        private DefaultStrategy() {
        }

        @Override // com.google.gson.LongSerializationPolicy.Strategy
        public JsonElement serialize(Long value) {
            return new JsonPrimitive((Number) value);
        }
    }

    private static class StringStrategy implements Strategy {
        private StringStrategy() {
        }

        @Override // com.google.gson.LongSerializationPolicy.Strategy
        public JsonElement serialize(Long value) {
            return new JsonPrimitive(String.valueOf(value));
        }
    }
}
