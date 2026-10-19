package org.codehaus.jackson.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonBackReference {
    String value() default "defaultReference";
}
