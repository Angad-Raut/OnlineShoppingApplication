package com.projectx.microservice.authenticationservice.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UserValidator.class)
@Documented
public @interface UserValid {
    String message() default "Invalid User Data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
