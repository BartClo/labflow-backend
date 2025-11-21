package com.labflow.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultoMayorValidator implements ConstraintValidator<AdultoMayor, LocalDate> {

    private int edadMinima;

    @Override
    public void initialize(AdultoMayor constraintAnnotation) {
        this.edadMinima = constraintAnnotation.edadMinima();
    }

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        // Si la fecha es null, dejamos que @NotNull maneje esa validación
        if (fechaNacimiento == null) {
            return true;
        }

        // Calculamos la edad actual
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();

        return edad >= edadMinima;
    }
}
