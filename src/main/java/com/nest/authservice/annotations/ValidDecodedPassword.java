package com.nest.authservice.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD}) // FIELD-level
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64DecodedPasswordValidator.class)
@Documented
public @interface ValidDecodedPassword {
    String message() default "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character (@, $, !, %, *, ?, &).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
