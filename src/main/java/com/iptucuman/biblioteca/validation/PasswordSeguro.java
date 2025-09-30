package com.iptucuman.biblioteca.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordSeguroValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordSeguro {

    String message() default "El password debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y caracteres especiales.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}