package br.com.fastfood.restaurant.validation.constraint;

import br.com.fastfood.restaurant.validation.LessThanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LessThanValidator.class)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LessThanEquals {
    String message() default "{br.com.fastfood.restaurant.validation.constraint.LessThanEquals.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String id();

    String field();

    String source();

    Class<?> entity();
}
