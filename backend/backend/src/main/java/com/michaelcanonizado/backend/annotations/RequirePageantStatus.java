package com.michaelcanonizado.backend.annotations;

import com.michaelcanonizado.backend.models.PageantStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePageantStatus {
    PageantStatus[] value();
}
