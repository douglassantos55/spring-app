package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.models.Restaurant;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

public class BeforeCreateRestaurantValidator implements Validator {
    private jakarta.validation.Validator validator;

    @Autowired
    public BeforeCreateRestaurantValidator(jakarta.validation.Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Restaurant.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Set<ConstraintViolation<Object>> violations = this.validator.validate(target);
        violations.forEach(violation ->
                errors.rejectValue(
                        violation.getPropertyPath().toString(),
                        "",
                        violation.getMessage()
                )
        );
    }
}
