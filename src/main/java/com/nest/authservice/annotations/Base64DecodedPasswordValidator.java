package com.nest.authservice.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;
import java.util.regex.Pattern;


public class Base64DecodedPasswordValidator implements ConstraintValidator<ValidDecodedPassword, String> {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    @Override
    public boolean isValid(String encodedPassword, ConstraintValidatorContext context) {
        if (encodedPassword == null || encodedPassword.isBlank()) {
            return false;
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(encodedPassword));
            return PASSWORD_PATTERN.matcher(decoded).matches();
        } catch (IllegalArgumentException e) {
            return false; // Not a valid base64 string
        }
    }
}