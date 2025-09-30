package com.iptucuman.biblioteca.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordSeguroValidator implements ConstraintValidator<PasswordSeguro, String> {

    @Override
    public void initialize(PasswordSeguro constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return false;
        }

        // Mínimo 8 caracteres
        if (password.length() < 8) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El password debe tener al menos 8 caracteres.")
                    .addConstraintViolation();
            return false;
        }

        // Al menos una letra mayúscula
        if (!password.matches(".*[A-Z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El password debe incluir al menos una letra mayúscula.")
                    .addConstraintViolation();
            return false;
        }

        // Al menos una letra minúscula
        if (!password.matches(".*[a-z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El password debe incluir al menos una letra minúscula.")
                    .addConstraintViolation();
            return false;
        }

        // Al menos un número
        if (!password.matches(".*\\d.*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El password debe incluir al menos un número.")
                    .addConstraintViolation();
            return false;
        }

        // Al menos un carácter especial
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El password debe incluir al menos un carácter especial (!@#$%^&*()_+-=[]{}etc.).")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}