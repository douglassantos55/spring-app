package br.com.fastfood.restaurant.validation;

import br.com.fastfood.restaurant.validation.constraint.LessThanEquals;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.UUID;

public class LessThanValidator implements ConstraintValidator<LessThanEquals, Object> {
    private String id;

    private String field;

    private String source;

    private final Repositories repositories;

    private CrudRepository<?, UUID> repository;

    @Autowired
    public LessThanValidator(WebApplicationContext context) {
        this.repositories = new Repositories(context);
    }

    @Override
    public void initialize(LessThanEquals constraint) {
        this.id = constraint.id();
        this.field = constraint.field();
        this.source = constraint.source();
        this.repository = (CrudRepository<?, UUID>) this.repositories.getRepositoryFor(constraint.entity()).get();
        ConstraintValidator.super.initialize(constraint);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Method getEntityId = value.getClass().getDeclaredMethod(this.id);
            Object entity = this.repository.findById((UUID) getEntityId.invoke(value)).get();

            Method getSource = entity.getClass().getDeclaredMethod(this.source);
            Method getField = value.getClass().getDeclaredMethod(this.field);

            Comparable source = (Comparable) getSource.invoke(entity);
            Comparable field = (Comparable) getField.invoke(value);

            context.disableDefaultConstraintViolation();
            String template = "must be less than equals to " + source.toString();

            context.buildConstraintViolationWithTemplate(template)
                    .addPropertyNode(this.field)
                    .addConstraintViolation();

            return field.compareTo(source) <= 0;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
