package br.com.fastfood.restaurant.validation.constraint;

import br.com.fastfood.restaurant.validation.ExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsConstraint {

    String message() default "{br.com.fastfood.restaurant.validation.constraint.ExistsConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class entityTarget();
}
