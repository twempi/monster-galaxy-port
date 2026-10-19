package org.codehaus.jackson.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonIgnore {
    boolean value() default true;
}
