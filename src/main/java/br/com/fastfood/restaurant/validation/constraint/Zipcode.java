package br.com.fastfood.restaurant.validation.constraint;

import br.com.fastfood.restaurant.validation.LessThanValidator;
import br.com.fastfood.restaurant.validation.ZipcodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ZipcodeValidator.class)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER,ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Zipcode {
    String message() default "{br.com.reconcip.restaurant.validation.constraint.Zipcode.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
