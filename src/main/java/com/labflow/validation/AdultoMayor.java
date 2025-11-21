package com.labflow.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdultoMayorValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdultoMayor {
    
    String message() default "Debes ser mayor de 18 años para registrarte";
    
    int edadMinima() default 18;
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
