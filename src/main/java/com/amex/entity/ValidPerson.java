package com.amex.entity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PersonValidator.class)
public @interface ValidPerson {
    String message() default "Person entity is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}