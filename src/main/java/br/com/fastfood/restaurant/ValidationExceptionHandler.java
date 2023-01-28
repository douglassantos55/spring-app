package br.com.fastfood.restaurant;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> error : e.getConstraintViolations()){
            errors.put(error.getPropertyPath().toString(), error.getMessage());
        }
        return errors;
    }
}
