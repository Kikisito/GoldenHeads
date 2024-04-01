package com.github.kikisito.goldenheads.managers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SubCommandDataManager {
    String name();
    String permission() default "";
    String description() default "";
}