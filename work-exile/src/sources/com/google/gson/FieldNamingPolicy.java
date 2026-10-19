package com.google.gson;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'UPPER_CAMEL_CASE_WITH_SPACES' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:485)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:399)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:364)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:349)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:284)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:153)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:102)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class FieldNamingPolicy {
    private static final /* synthetic */ FieldNamingPolicy[] $VALUES;
    public static final FieldNamingPolicy LOWER_CASE_WITH_DASHES;
    public static final FieldNamingPolicy LOWER_CASE_WITH_UNDERSCORES;
    public static final FieldNamingPolicy UPPER_CAMEL_CASE = new FieldNamingPolicy("UPPER_CAMEL_CASE", 0, new ModifyFirstLetterNamingPolicy(ModifyFirstLetterNamingPolicy.LetterModifier.UPPER));
    public static final FieldNamingPolicy UPPER_CAMEL_CASE_WITH_SPACES;
    private final FieldNamingStrategy2 namingPolicy;

    public static FieldNamingPolicy valueOf(String name) {
        return (FieldNamingPolicy) Enum.valueOf(FieldNamingPolicy.class, name);
    }

    public static FieldNamingPolicy[] values() {
        return (FieldNamingPolicy[]) $VALUES.clone();
    }

    static {
        final String str = " ";
        UPPER_CAMEL_CASE_WITH_SPACES = new FieldNamingPolicy("UPPER_CAMEL_CASE_WITH_SPACES", 1, new CompositionFieldNamingPolicy(str) { // from class: com.google.gson.UpperCamelCaseSeparatorNamingPolicy
            {
                super(new CamelCaseSeparatorNamingPolicy(str), new ModifyFirstLetterNamingPolicy(ModifyFirstLetterNamingPolicy.LetterModifier.UPPER));
            }
        });
        final String str2 = "_";
        LOWER_CASE_WITH_UNDERSCORES = new FieldNamingPolicy("LOWER_CASE_WITH_UNDERSCORES", 2, new CompositionFieldNamingPolicy(str2) { // from class: com.google.gson.LowerCamelCaseSeparatorNamingPolicy
            {
                super(new CamelCaseSeparatorNamingPolicy(str2), new LowerCaseNamingPolicy());
            }
        });
        final String str3 = "-";
        LOWER_CASE_WITH_DASHES = new FieldNamingPolicy("LOWER_CASE_WITH_DASHES", 3, new CompositionFieldNamingPolicy(str3) { // from class: com.google.gson.LowerCamelCaseSeparatorNamingPolicy
            {
                super(new CamelCaseSeparatorNamingPolicy(str3), new LowerCaseNamingPolicy());
            }
        });
        $VALUES = new FieldNamingPolicy[]{UPPER_CAMEL_CASE, UPPER_CAMEL_CASE_WITH_SPACES, LOWER_CASE_WITH_UNDERSCORES, LOWER_CASE_WITH_DASHES};
    }

    private FieldNamingPolicy(String str, int i, FieldNamingStrategy2 namingPolicy) {
        super(str, i);
        this.namingPolicy = namingPolicy;
    }

    FieldNamingStrategy2 getFieldNamingPolicy() {
        return this.namingPolicy;
    }
}
