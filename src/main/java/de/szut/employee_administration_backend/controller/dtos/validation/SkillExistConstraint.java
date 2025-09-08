package de.szut.employee_administration_backend.controller.dtos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkillValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillExistConstraint {
    String message() default "invalid skill given";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
