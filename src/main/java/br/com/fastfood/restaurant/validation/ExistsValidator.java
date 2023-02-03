package br.com.fastfood.restaurant.validation;

import br.com.fastfood.restaurant.validation.constraint.ExistsConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

public class ExistsValidator implements ConstraintValidator<ExistsConstraint, UUID> {
    private final Repositories repositories;

    private CrudRepository<?, UUID> repository;

    public ExistsValidator(WebApplicationContext context) {
        this.repositories = new Repositories(context);
    }

    @Override
    public void initialize(ExistsConstraint constraint) {
        ConstraintValidator.super.initialize(constraint);
        this.repository = (CrudRepository<?, UUID>) this.repositories.getRepositoryFor(constraint.entityTarget()).get();
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return value != null && this.repository.existsById(value);
    }
}
