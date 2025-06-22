package com.nest.authservice.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD}) // ✅ FIELD-level
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64DecodedPasswordValidator.class)
@Documented
public @interface ValidDecodedPassword {
    String message() default "Invalid password format after decoding.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
