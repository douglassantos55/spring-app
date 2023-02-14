package br.com.fastfood.restaurant.validation;

import br.com.fastfood.restaurant.validation.constraint.Zipcode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ZipcodeValidator implements ConstraintValidator<Zipcode, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            RestTemplate template = new RestTemplateBuilder().build();
            ResponseEntity response = template.getForEntity("https://viacep.com.br/ws/" + value + "/json/", Map.class);
            Map<String, String> body = (Map<String, String>) response.getBody();
            return response.getStatusCode().is2xxSuccessful() && !body.containsKey("erro");
        } catch (RestClientException e) {
            return false;
        }
    }
}
