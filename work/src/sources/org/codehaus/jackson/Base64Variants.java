package org.codehaus.jackson;

import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

/* JADX INFO: loaded from: classes.dex */
public final class Base64Variants {
    public static final Base64Variant MODIFIED_FOR_URL;
    static final String STD_BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    public static final Base64Variant MIME = new Base64Variant("MIME", STD_BASE64_ALPHABET, true, SignatureVisitor.INSTANCEOF, 76);
    public static final Base64Variant MIME_NO_LINEFEEDS = new Base64Variant(MIME, "MIME-NO-LINEFEEDS", Integer.MAX_VALUE);
    public static final Base64Variant PEM = new Base64Variant(MIME, "PEM", true, SignatureVisitor.INSTANCEOF, 64);

    static {
        StringBuffer sb = new StringBuffer(STD_BASE64_ALPHABET);
        sb.setCharAt(sb.indexOf("+"), SignatureVisitor.SUPER);
        sb.setCharAt(sb.indexOf("/"), '_');
        MODIFIED_FOR_URL = new Base64Variant("MODIFIED-FOR-URL", sb.toString(), false, (char) 0, Integer.MAX_VALUE);
    }

    public static Base64Variant getDefaultVariant() {
        return MIME_NO_LINEFEEDS;
    }
}
